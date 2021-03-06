package com.alpha.marketplace.services.base;

import com.alpha.marketplace.exceptions.VersionMismatchException;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.models.edit.UserEditModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {

    List<User> getAll();
    List<User> searchUsers(String criteria);
    User registerUser(UserBindingModel u, BindingResult errors);
    User findById(long id);
    User findByIdFromMemory(long id);
    User findByEmail(String email);
    User getCurrentUser();
    boolean updateUser(User u);
    boolean editUser(User u, UserEditModel edit) throws VersionMismatchException;
    boolean banUser(long id);
    boolean unbanUser(long id);
    boolean editUserPic(User u, MultipartFile file);
    boolean editUserPic(User u, String urlString);
    boolean addRoleToUser(long id, String role);
    boolean removeRoleFromUser(long id, String role);
    void reloadMemory();
    Map<Long, User> getUsers();
}