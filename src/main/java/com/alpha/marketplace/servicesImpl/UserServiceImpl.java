package com.alpha.marketplace.servicesImpl;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.UserBindingModel;
import com.alpha.marketplace.repositories.base.UserRepository;
import com.alpha.marketplace.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User registerUser(UserBindingModel model) {
        User u = new User();
        u.setEmail(model.getEmail());
        u.setFirstName(model.getFirstName());
        u.setLastName(model.getLastName());
        u.setPassword(model.getPass1());
        if(!model.getPass1().equals(model.getPass2())){
            u = null;
        }
        return u;
    }

    @Override
    public User getById(int id) {
        return null;
    }

    @Override
    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }
}
