package com.alpha.marketplace.services;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.repositories.base.UserRepository;
import com.alpha.marketplace.services.base.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder encoder, ModelMapper mapper) {
        this.repository = repository;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @Override
    public User registerUser(UserBindingModel model) {
        if(!validateReg(model)) {
            //TODO Error handling
            return null;
        }
        User u = mapper.map(model, User.class);
        String encryptedPass = encoder.encode(model.getPass1());
        u.setPassword(encryptedPass);
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

    private boolean validateReg(UserBindingModel model) {
        return (repository.findByEmail(model.getEmail()) == null) &&
                (model.getPass1().equals(model.getPass2()));
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }
}
