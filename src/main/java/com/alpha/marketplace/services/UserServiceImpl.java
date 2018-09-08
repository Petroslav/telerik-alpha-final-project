package com.alpha.marketplace.services;

import com.alpha.marketplace.exceptions.CannotFetchBytesException;
import com.alpha.marketplace.exceptions.ErrorMessages;
import com.alpha.marketplace.exceptions.VersionMismatchException;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserServiceImpl implements UserService {
    private final String VALID_EMAIL_ADDRESS_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9]+.[a-z.]+$";
    private final String DEFAULT_PIC = "http://marketplace-user-pics.storage.googleapis.com/new-user.jpg";
    private final String DEFAULT_ROLE = "ROLE_USER";
    private final String ROLE_PREFIX = "ROLE_";

    private final ExecutorService updateWorker;

    private final UserRepository repository;
    private final CloudUserRepository cloudUserRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;

    private Map<Long, User> users;

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
        users = new HashMap<>();
        getAll();
        updateWorker = Executors.newFixedThreadPool(1);

    }

    @Override
    public List<User> getAll() {
        users.clear();
        List<User> all = repository.getAll();
        all.forEach(u -> users.put(u.getId(), u));
        return all;
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
    public User findByIdFromMemory(long id) { return users.get(id); }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public boolean updateUser(User u) {
        try{
           repository.update(u);
           users.put(u.getId(), u);
        }catch(VersionMismatchException e){
            System.out.println("VERSION MISMATCH");
            return false;
        }
        return true;
    }

    @Override
    public boolean editUser(User u, UserEditModel edit) {
        u.setFirstName(edit.getFirstName());
        u.setLastName(edit.getLastName());
        if(!edit.getPicture().isEmpty()) {
            try {
                if (u.getPicBlobId() != null) {
                    cloudUserRepository.deleteUserPic(u.getPicBlobId());
                }
                String e = edit.getPicture().getOriginalFilename();
                Blob b = cloudUserRepository.saveUserPic(u.getId() + e.substring(e.lastIndexOf(".")), edit.getPicture().getBytes(), edit.getPicture().getContentType());
                u.setPicBlobId(b.getBlobId());
                u.setPicURI(b.getMediaLink());
            }catch (IOException e) {
                e.printStackTrace();
                System.out.println("SAD LIFE");
            }
        }
        if(!edit.getOldPass().isEmpty()) {
            if (!encoder.matches(edit.getOldPass(), u.getPassword())) {
                return false;
            }
            u.setPassword(encoder.encode(edit.getNewPass()));
        }

        return updateUser(u);
    }

    @Override
    public boolean banUser(long id) {
        User gettingBanned = findById(id);
        if(!gettingBanned.isAdmin() || getCurrentUser().isOwner()){
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
        Blob blob = cloudUserRepository.saveUserPic(u.getId() + "", bytes, file.getContentType());
        if(blob == null){
            return false;
        }
        u.setPicBlobId(blob.getBlobId());
        u.setPicURI(blob.getMediaLink());
        return updateUser(u);
    }

    @Override
    public boolean editUserPic(User u, String urlString) {
        Blob b;
        try{
            b = cloudUserRepository.saveUserPicFromUrl(u.getId() + "", urlString);
            u.setPicBlobId(b.getBlobId());
            u.setPicURI(b.getMediaLink());
        }catch(CannotFetchBytesException e){
            System.out.println(e.getMessage());
            return false;
        }
        return updateUser(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    @Override
    public User getCurrentUser() {
        if(Utils.userIsAnonymous()){
            return null;
        }
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return repository.findByUsername(user.getUsername());
    }

    @Override
    public boolean addRoleToUser(long id, String role){
        if(!role.startsWith(ROLE_PREFIX)){
            role = ROLE_PREFIX + role;
        }
        try{
            User u = findById(id);
            Role r = roleRepository.findByName(role);
            if(r == null) return false;
            boolean success = u.getAuthorities().add(r);
            if(!success) return false;
            repository.update(u);
            users.put(u.getId(), u);
        } catch (VersionMismatchException e) {
            System.out.println("Version mismatch, aborting operation.");
        }
        return true;
    }

    @Override
    public boolean removeRoleFromUser(long id, String role) {
        if(!role.startsWith(ROLE_PREFIX)){
            role = ROLE_PREFIX + role;
        }
        try{
            User u = findById(id);
            Role r = roleRepository.findByName(role);
            if(r == null) return false;
            boolean success = u.getAuthorities().remove(r);
            if(!success) return false;
            repository.update(u);
            users.put(u.getId(), u);
        } catch (VersionMismatchException e) {
            System.out.println("Version mismatch, aborting operation.");
            return false;
        }
        return true;
    }

    @Override
    public void reloadMemory() {
        users.clear();
        getAll().forEach(user -> users.put(user.getId(), user));
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

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }


    // THIS IS A TEST METHOD. IN CASE IT MAKES IT INTO THE FINAL PRODUCT I AM VERY AND TRULY SORRY.
//    @Override
//    public void wtf(){
//        System.out.println("WAT");
//        int wat = 22222;
//        for(int i = 1; i < 101; i++){
//            wat += i;
//        }
//        Thread b = new Thread(() -> {
//            for(int i = 1; i <= 100; i++){
//                User u1 = repository.findById(1);
//                if(i == 1){
//                    updateLn(i, u1);
//                    continue;
//                }
//                updateLn(i, u1);
//            }
//            System.out.println("DONE 2!");
//        });
//        Thread d = new Thread(() -> {
//            for(int i = 1; i <= 100; i++){
//                User u1 = repository.findById(1);
//                if(i == 1){
//                    updateFn(i, u1);
//                    continue;
//                }
//                updateFn(i, u1);
//            }
//            System.out.println("DONE 4!");
//        });
//
//        b.start();
//        d.start();
//        while(b.isAlive() || d.isAlive()){
//            continue
//        }
//        System.out.println("SUM 100: " + wat);
//        User gg = repository.findById(1);
//        System.out.println(gg.getUsername());
//        System.out.println(gg.getFirstName());
//        System.out.println(gg.getLastName());
//    }
//
//    private void updateLn(int i, User u1){
//        try {
//            u1.setLastName((Integer.parseInt(u1.getLastName()) + i) + "");
//            repository.update(u1);
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//            updateLn(i, repository.findById(1));
//        }
//    }
//
//    private void updateFn(int i, User u1){
//        try {
//            u1.setFirstName((Integer.parseInt(u1.getFirstName()) + i) + "");
//            repository.update(u1);
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//            updateFn(i, repository.findById(1));
//        }
//    }
}
