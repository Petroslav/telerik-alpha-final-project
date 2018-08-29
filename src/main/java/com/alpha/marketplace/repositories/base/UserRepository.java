package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.models.User;

import java.util.List;

public interface UserRepository {

    User findById(long id);
    User findByEmail(String email);
    User findByUsername(String username);
    List<User> getAll();
    List<User> search(String criteria);
    boolean save(User u);
    boolean update(User u);
    boolean deleteById(long id);
    boolean deleteByEmail(String email);

}
