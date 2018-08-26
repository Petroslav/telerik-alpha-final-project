package com.alpha.marketplace.services.base;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.models.edit.UserEditModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService {

    User registerUser(UserBindingModel u);
    User findById(long id);
    User findByEmail(String email);
    boolean editUser(User u, UserEditModel edit);
    boolean editUserPic(User u, MultipartFile file);
    boolean editUserPic(User u, String urlString);
    User currentUser();
}
