package com.alpha.marketplace.services;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;
import com.alpha.marketplace.exceptions.ErrorMessages;
import com.alpha.marketplace.models.Role;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.models.edit.UserEditModel;
import com.alpha.marketplace.repositories.base.RoleRepository;
import com.alpha.marketplace.repositories.base.UserRepository;
import com.alpha.marketplace.repositories.base.CloudUserRepository;
import com.alpha.marketplace.services.base.UserService;
import com.alpha.marketplace.utils.Utils;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
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
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final String VALID_EMAIL_ADDRESS_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9]+.[a-z.]+$";
    private final String DEFAULT_PIC = "http://marketplace-user-pics.storage.googleapis.com/new-user.jpg";
    private final String DEFAULT_ROLE = "ROLE_USER";
    private final String ROLE_PREFIX = "ROLE_";

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

    @Override
    public List<User> searchUsers(String criteria) {
        return repository.search(criteria);
    }

    @Override
    public User registerUser(UserBindingModel model) {
        if(!validateReg(model)) {
            return null;
        }
        User u = mapper.map(model, User.class);
        Role role = roleRepository.findByName(DEFAULT_ROLE);
        String encryptedPass = encoder.encode(model.getPass());
        u.setPassword(encryptedPass);
        u.getAuthorities().add(role);
        u.setPicURI(DEFAULT_PIC);
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
    public boolean updateUser(User u) {
        return repository.update(u);
    }

    @Override
    public boolean editUser(User u, UserEditModel edit) {
        u.setFirstName(edit.getFirstName());
        u.setLastName(edit.getLastName());
        if(!edit.getPicture().isEmpty()){
            try {
                if(u.getPicBlobId() != null){
                    cloudUserRepository.deleteUserPic(u.getPicBlobId());
                }
                String e = edit.getPicture().getOriginalFilename();
                Blob b = cloudUserRepository.saveUserPic(u.getId() + e.substring(e.lastIndexOf(".")), edit.getPicture().getBytes(), edit.getPicture().getContentType());
                u.setPicBlobId(b.getBlobId());
                u.setPicURI(b.getMediaLink());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("SAD LIFE");
            }
        }
        if(!edit.getOldPass().isEmpty()){
            if(!encoder.matches(edit.getOldPass(), u.getPassword())){
                return false;
            }
            u.setPassword(encoder.encode(edit.getNewPass()));
        }
        return repository.update(u);
    }

    @Override
    public boolean banUser(long id) {
        User gettingBanned = findById(id);
        if(!gettingBanned.isAdmin() || currentUser().isOwner()){
            gettingBanned.ban();
            return updateUser(gettingBanned);
        }else{
            return false;
        }
    }

    @Override
    public boolean unbanUser(long id) {
        User gettingUnbanned = findById(id);
        gettingUnbanned.unban();
        return updateUser(gettingUnbanned);
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
        Blob blob = cloudUserRepository.saveUserPic(String.valueOf(u.getId()), bytes, file.getContentType());
        if(blob == null){
            return false;
        }
        u.setPicBlobId(blob.getBlobId());
        u.setPicURI(blob.getMediaLink());
        return true;
    }

    @Override
    public boolean editUserPic(User u, String urlString) {
        Blob b;
        try{
            b = cloudUserRepository.saveUserPicFromUrl(String.valueOf(u.getId()), urlString);
        }catch(CannotFetchBytesException e){
            System.out.println(e.getMessage());
            return false;
        }
        u.setPicBlobId(b.getBlobId());
        u.setPicURI(b.getMediaLink());
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

    @Override
    public boolean addRoleToUser(long id, String role){
        if(!role.contains(ROLE_PREFIX)){
            role = ROLE_PREFIX + role;
        }
        try{
            User u = findById(id);
            Role r = roleRepository.findByName(role);
            if(r == null) return false;
            boolean success = u.getAuthorities().add(r);
            if(!success) return false;
            return repository.update(u);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeRoleFromUser(long id, String role) {
        if(!role.contains(ROLE_PREFIX)){
            role = ROLE_PREFIX + role;
        }
        try{
            User u = findById(id);
            Role r = roleRepository.findByName(role);
            if(r == null) return false;
            boolean success = u.getAuthorities().remove(r);
            if(!success) return false;
            return repository.update(u);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean validateReg(UserBindingModel model) {
        if (!validateEmail(model.getEmail())) {
            return false;
        }
        if(model.getPass().length() < 6 || model.getPass().length() > 16){
            return false;
        }
        if(findByEmail(model.getEmail()) != null){
            return false;
        }
        try{
            repository.findByUsername(model.getUsername());
        }catch(UsernameNotFoundException e){
            return true;
        }
        return false;
    }

    private boolean validateEmail(String email){
        return email.matches(VALID_EMAIL_ADDRESS_REGEX);
    }
}
