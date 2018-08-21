package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.models.Role;

public interface RoleRepository {

    Role findById(int id);
    Role findByName(String name);
    boolean addRole(String name);
    boolean updateRole(String newName);
    boolean deleteRoleById(int id);
    boolean deleteRoleByName(String name);
}
