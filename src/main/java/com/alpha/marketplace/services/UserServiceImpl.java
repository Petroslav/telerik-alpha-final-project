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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {

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
    public User registerUser(UserBindingModel model) {
        if(!validateReg(model)) {
            //TODO Error handling
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
            String oldPass = encoder.encode(edit.getOldPass());
            //!u.getPassword().equals(oldPass) || (was in if... gotta fix later)//TODO
            if(!edit.getNewPass().equals(edit.getNewPassConfirm())){
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

    private boolean validateReg(UserBindingModel model) {
        return (repository.findByEmail(model.getEmail()) == null) &&
                (model.getPass1().equals(model.getPass2()));
    }
    @Override
    public User currentUser() {
        if(Utils.isUserNotAnonymous()){
            return null;
        }
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return repository.findByUsername(user.getUsername());
    }

}
