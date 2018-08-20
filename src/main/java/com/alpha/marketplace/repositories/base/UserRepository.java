package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.models.User;

import java.util.List;

public interface UserRepository {

    User findById(long id);
    User findByEmail(String email);
    List<User> getAll();
    boolean save(User u);
    boolean update(User u);
    boolean deleteById(long id);
    boolean deleteByEmail(String email);

}
