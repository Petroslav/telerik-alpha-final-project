package com.alpha.marketplace.servicesImpl;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
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
        if(!validateRegUser(model)){
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
    public User getById(int id) {
        if(id < 0){
            //TODO error handling
            return null;
        }
        return repository.findById(id);
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public boolean editUser(User u) {
        //TODO logic for editing user profile
        return true;
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        User user = repository.getByEmail(email);
        if(user == null){
            //TODO error handling
            return false;
        }
        //TODO add encryption to check hashed passwords
        return false;
    }

    @Override
    public boolean validateRegUser(UserBindingModel model) {
        return true;
    }
}
