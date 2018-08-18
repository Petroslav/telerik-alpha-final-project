package com.alpha.marketplace.services;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.UserBindingModel;

public interface UserService {

    User registerUser(UserBindingModel u);
    User getById(int id);
    User getByUsername(String username);
}
