package com.alpha.marketplace.services;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;

public interface UserService {

    User registerUser(UserBindingModel u);
    User getById(int id);
    User getByEmail(String email);
    boolean editUser(User u);
    boolean authenticateUser(String email, String password);
    boolean validateRegUser(UserBindingModel model);
}
