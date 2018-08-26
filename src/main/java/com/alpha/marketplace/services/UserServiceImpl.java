package com.alpha.marketplace.services;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;
import com.alpha.marketplace.models.Role;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.models.edit.UserEditModel;
import com.alpha.marketplace.repositories.base.RoleRepository;
import com.alpha.marketplace.repositories.base.UserRepository;
import com.alpha.marketplace.repositories.base.CloudUserRepository;
import com.alpha.marketplace.services.base.UserService;
import com.alpha.marketplace.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    private final String VALID_EMAIL_ADDRESS_REGEX = "^[\\\\w!#$%&'*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$";

    private final UserRepository repository;
    private final CloudUserRepository cloudUserRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(
            UserRepository repository,
            CloudUserRepository cloudUserRepository,
            RoleRepository roleRepository,
            ModelMapper mapper,
            BCryptPasswordEncoder encoder
    ) {
        this.repository = repository;
        this.cloudUserRepository = cloudUserRepository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    private boolean validateEmail(String email){
        Pattern p =Pattern.compile(VALID_EMAIL_ADDRESS_REGEX, Pattern.CASE_INSENSITIVE);
        return p.matcher(email).find();
    }

    @Override
    public User registerUser(UserBindingModel model, BindingResult errors) {
        if(!validateReg(model, errors)) {
            return null;
        }
        User u = mapper.map(model, User.class);
        Role role = roleRepository.findByName("ROLE_USER");
        String encryptedPass = encoder.encode(model.getPass1());
        u.setPassword(encryptedPass);
        u.getAuthorities().add(role);
        u.setPicURI(cloudUserRepository.getPROFILE_PICS_URL_PREFIX() + "new-user");
        repository.save(u);
        return u;
    }

    @Override
    public User findById(long id) {
        return repository.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public boolean editUser(User u, UserEditModel edit) {
        u.setFirstName(edit.getFirstName());
        u.setLastName(edit.getLastName());
        if(!edit.getOldPass().isEmpty()){
            if(!encoder.matches(edit.getOldPass(), u.getPassword()) ||!edit.getNewPass().equals(edit.getNewPassConfirm())){
                return false;
            }
            u.setPassword(encoder.encode(edit.getNewPass()));
        }
        return repository.update(u);
    }

    @Override
    public boolean editUserPic(User u, MultipartFile file) {
        byte[] bytes;
        try{
            bytes = file.getBytes();
        }catch(IOException e){
            System.out.println(e.getMessage());
            return false;
        }
        String picURI = cloudUserRepository.saveUserPic(String.valueOf(u.getId()), bytes, file.getContentType());
        if(picURI == null){
            return false;
        }
        u.setPicURI(picURI);
        return true;
    }

    @Override
    public boolean editUserPic(User u, String urlString) {
        String picURI;
        try{
            picURI = cloudUserRepository.saveUserPicFromUrl(String.valueOf(u.getId()), urlString);
        }catch(CannotFetchBytesException e){
            System.out.println(e.getMessage());
            return false;
        }
        u.setPicURI(picURI);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    @Override
    public User currentUser() {
        if(Utils.userIsAnonymous()){
            return null;
        }
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return repository.findByUsername(user.getUsername());
    }

    private boolean validateReg(UserBindingModel model, BindingResult errors) {
        boolean valid = true;
        if (!validateEmail(model.getEmail())) {
            errors.addError(new ObjectError("email", "Please input a valid e-mail"));
            valid = false;
        }
        if(repository.findByEmail(model.getEmail()) != null){
            errors.addError(new ObjectError("email", "A user with that e-mail already exists."));
            valid = false;
        }
        if(model.getPass1().equals(model.getPass2())){
            errors.addError(new ObjectError("passMismatch", "Passwords do not match"));
            valid = false;
        }
        return valid;
    }

    @Override
    public boolean addRoleToUser(String username, String role){
        try{
            User u = ((User)loadUserByUsername(username));
            u.getAuthorities().add(roleRepository.findByName(role));
            repository.update(u);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
