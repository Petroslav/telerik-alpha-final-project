package com.alpha.marketplace.services.base;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.models.edit.UserEditModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAll();
    List<User> searchUsers(String criteria);
    User registerUser(UserBindingModel u);
    User findById(long id);
    User findByEmail(String email);
    User currentUser();
    boolean updateUser(User u);
    boolean editUser(User u, UserEditModel edit);
    boolean editUserPic(User u, MultipartFile file);
    boolean editUserPic(User u, String urlString);
    boolean addRoleToUser(long id, String role);
    boolean removeRoleFromUser(long id, String role);
}