package com.alpha.marketplace.services;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.repositories.base.UserRepository;
import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User registerUser(UserBindingModel model) {
        if(!validateReg(model)){
            //TODO Error handling
            return null;
        }
        User u = new User();
        u.setEmail(model.getEmail());
        u.setPublisherName(model.getPublisherName());
        u.setFirstName(model.getFirstName());
        u.setLastName(model.getLastName());
        u.setPassword(model.getPass1());
        //ENCRYPT PASSWORDS AND THEN CHECK THEM
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
    public User findByPublisherName(String name) {
        return repository.findByPublisherName(name);
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
        return (repository.findByEmail(model.getEmail()) == null) &&
                (model.getPass1().equals(model.getPass2()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
