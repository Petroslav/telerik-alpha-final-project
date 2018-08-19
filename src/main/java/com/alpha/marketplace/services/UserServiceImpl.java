package com.alpha.marketplace.services;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.repositories.base.UserRepository;
import com.alpha.marketplace.services.base.UserService;
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
        if(!validateReg(model)){
            //TODO Error handling
            return null;
        }
        User u = new User();
        u.setEmail(model.getEmail());
        u.setFirstName(model.getFirstName());
        u.setLastName(model.getLastName());
        u.setPassword(model.getPass1());
        if(!model.getPass1().equals(model.getPass2())){
            u = null;
        }
        repository.save(u);
        return u;
    }

    @Override
    public User findById(long id) {
        return repository.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public boolean editUser(User u) {
        //TODO logic for editing user profile
        return true;
    }

    private boolean authenticateUser(String email, String password) {
        User user = repository.findByEmail(email);
        if(user == null){
            return false;
        }
        //TODO add encryption to check hashed passwords
        return false;
    }

    private boolean validateReg(UserBindingModel model) {
        return (repository.findByEmail(model.getEmail()) != null) &&
                (model.getPass1().equals(model.getPass2()));
    }
}
