package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.User;
import java.util.ArrayList;

public interface UserDao {
    boolean insertUser(User user);
    ArrayList<User> fetchAllUsers();
    User findUserById(int id);
    User findUserByEmail(String email);
    User findUserByPhone(String phone);
    boolean updateUser(User user);
    boolean deleteUser(int id);
    boolean updateActiveStatus(int id, int isActive);
    ArrayList<User> fetchUsersByRoleId(int roleId);
}