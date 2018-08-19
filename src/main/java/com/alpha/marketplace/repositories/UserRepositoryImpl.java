package com.alpha.marketplace.repositories;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.repositories.base.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private SessionFactory session;

    @Autowired
    public UserRepositoryImpl(SessionFactory session) {
        this.session = session;
    }

    @Override
    public User findById(int id) {
        User u = null;
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            u = sess.get(User.class, id);
            sess.getTransaction().commit();
            System.out.println("User retrieved successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public User getByEmail(String email) {
        User u = null;
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            u = (User) sess
                    .createQuery("FROM User WHERE email = :emailString")
                    .setParameter("emailString", email);
            sess.getTransaction().commit();
            System.out.println("User retrieved successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public boolean save(User u) {
        return false;
    }

    @Override
    public boolean update(User u) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public boolean deleteByEmail(String email) {
        return false;
    }
}
