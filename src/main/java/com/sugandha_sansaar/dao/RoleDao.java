package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Role;
import java.util.ArrayList;

public interface RoleDao {
    boolean insertRole(Role role);
    ArrayList<Role> fetchAllRoles();
    Role findRoleById(int id);
    Role findRoleByName(String roleName);
    boolean updateRole(Role role);
    boolean deleteRole(int id);
}