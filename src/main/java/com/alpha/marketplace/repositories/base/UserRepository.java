package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.models.User;

import java.util.List;

public interface UserRepository {

    User findById(int id);
    User getByEmail(String email);
    List<User> getAll();
    boolean save(User u);
    boolean update(User u);
    boolean deleteById(int id);
    boolean deleteByEmail(String email);

}
