package com.alpha.marketplace.repositories;

import com.alpha.marketplace.exceptions.ErrorMessages;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.repositories.base.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory session;

    @Autowired
    public UserRepositoryImpl(SessionFactory session) {
        this.session = session;
    }

    @Override
    public User findById(long id) {
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
    public User findByEmail(String email) {
        List<User> matches = null;
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            matches = sess.createQuery("FROM User WHERE email = :emailString", User.class)
                    .setParameter("emailString", email)
                    .list();

            sess.getTransaction().commit();
            System.out.println("Search completed successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return matches == null? null : matches.isEmpty() ? null : matches.get(0);
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        List<User> matches = null;
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            matches = sess.createQuery("FROM User WHERE username = :usernameString", User.class)
                    .setParameter("usernameString", username)
                    .list();
            sess.getTransaction().commit();
            System.out.println("Search completed successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        if(matches == null || matches.isEmpty()){
            throw new UsernameNotFoundException(ErrorMessages.USERNAME_DOES_NOT_EXIST);
        }
        return matches.get(0);
    }

    @Override
    public List<User> getAll() {
        List<User> users = null;
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            users = sess.createQuery("FROM User", User.class).list();
            sess.getTransaction().commit();
            System.out.println("Users retrieved successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> search(String criteria) {
        List<User> matches;
        try(Session sess = session.openSession()){
            sess.beginTransaction();

            matches = sess
                    .createQuery("FROM User WHERE username LIKE :name", User.class)
                    .setParameter("name", "%" + criteria + "%")
                    .list();
            sess.getTransaction().commit();
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
        return matches;
    }

    @Override
    public boolean save(User u) {
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            sess.save(u);
            sess.getTransaction().commit();
            System.out.println("Users saved successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(User u) {
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            sess.update(u);
            sess.getTransaction().commit();
            System.out.println("User updated successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteById(long id) {
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            sess.delete(findById(id));
            sess.getTransaction().commit();
            System.out.println("User deleted successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;        }
        return true;
    }

    @Override
    public boolean deleteByEmail(String email) {
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            sess.delete(findByEmail(email));
            sess.getTransaction().commit();
            System.out.println("User deleted successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
