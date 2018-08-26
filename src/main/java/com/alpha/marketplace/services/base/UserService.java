package com.alpha.marketplace.services.base;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.models.edit.UserEditModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAll();
    User registerUser(UserBindingModel u, BindingResult errors);
    User findById(long id);
    User findByEmail(String email);
    User currentUser();
    boolean editUser(User u, UserEditModel edit);
    boolean editUserPic(User u, MultipartFile file);
    boolean editUserPic(User u, String urlString);
}
