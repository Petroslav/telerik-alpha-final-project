package com.alpha.marketplace.repositories;

import com.alpha.marketplace.models.Role;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.repositories.base.RoleRepository;
import com.alpha.marketplace.repositories.base.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final SessionFactory session;

    @Autowired
    public RoleRepositoryImpl(SessionFactory session) {
        this.session = session;
    }

    @Override
    public Role findById(int id) {
        Role role = null;
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            role = sess.get(Role.class, id);
            sess.getTransaction().commit();
            System.out.println("Role retrieved successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return role;
    }

    @Override
    public Role findByName(String name) {
        List<Role> roles = null;
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            roles = sess.createQuery("FROM Role WHERE authority = :nameString", Role.class)
                    .setParameter("nameString", name)
                    .list();

            sess.getTransaction().commit();
            System.out.println("Search completed successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return roles == null? null : roles.isEmpty() ? null : roles.get(0);
    }

    @Override
    public boolean addRole(String name){
        Role role = new Role(name);
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            sess.save(role);
            sess.getTransaction().commit();
            System.out.println("Role saved successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateRole(String roleName, String newName) {
        Role role = findByName(roleName);
        role.setAuthority(newName);
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            sess.update(role);
            sess.getTransaction().commit();
            System.out.println("Role updated successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteRole(Role role) {
        try(Session sess = session.openSession()){
            sess.beginTransaction();
            sess.delete(role);
            sess.getTransaction().commit();
            System.out.println("Role deleted successfully.");
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteRoleById(int id) {
        Role role = findById(id);
        return deleteRole(role);
    }

    @Override
    public boolean deleteRoleByName(String name) {
        Role role = findByName(name);
        return deleteRole(role);
    }

}
