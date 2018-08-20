package com.alpha.marketplace.services.base;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;

public interface UserService {

    User registerUser(UserBindingModel u);
    User findById(long id);
    User findByEmail(String email);
    User findByPublisherName(String name);
    boolean editUser(User u);
}
