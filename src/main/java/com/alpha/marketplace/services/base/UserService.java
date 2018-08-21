package com.alpha.marketplace.services.base;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User registerUser(UserBindingModel u);
    User findById(long id);
    User findByEmail(String email);
    boolean editUser(User u);
}
