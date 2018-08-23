package com.alpha.marketplace.services;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;
import com.alpha.marketplace.models.Role;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.models.edit.UserEditModel;
import com.alpha.marketplace.repositories.base.RoleRepository;
import com.alpha.marketplace.repositories.base.UserRepository;
import com.alpha.marketplace.services.base.CloudUserService;
import com.alpha.marketplace.services.base.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final CloudUserService cloudUserService;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(
            UserRepository repository,
            CloudUserService cloudUserService,
            RoleRepository roleRepository,
            ModelMapper mapper,
            BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.cloudUserService = cloudUserService;
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
        u.setPicURI(cloudUserService.getPROFILE_PICS_URL_PREFIX() + "new-user");
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
        String oldPass = encoder.encode(edit.getOldPass());
        if(!u.getPassword().equals(oldPass) || !edit.getNewPass().equals(edit.getNewPassConfirm())){
            return false;
        }
        u.setPassword(encoder.encode(edit.getNewPass()));
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
        String picURI = cloudUserService.saveUserPic(String.valueOf(u.getId()), bytes, file.getContentType());
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
            picURI = cloudUserService.saveUserPicFromUrl(String.valueOf(u.getId()), urlString);
        }catch(CannotFetchBytesException e){
            System.out.println(e.getMessage());
            return false;
        }
        u.setPicURI(picURI);
        return true;
    }

    private boolean validateReg(UserBindingModel model) {
        return (repository.findByEmail(model.getEmail()) == null) &&
                (model.getPass1().equals(model.getPass2()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }
}
