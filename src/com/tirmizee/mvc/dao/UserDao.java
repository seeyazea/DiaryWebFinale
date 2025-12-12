package com.tirmizee.mvc.dao;

import com.tirmizee.mvc.model.User;
import com.tirmizee.mvc.model.UserRole;
public interface UserDao {

    void register(User user);

    User findByEmail(String email);

    User findById(int id);

    boolean isValid(String email, String password);

    UserRole findUserByUserAndPass(String email, String password);

    void update(User user);          // Edit Profile

    void updatePassword(int id, String newPassword);   // Change Password

    void delete(int id);             // Delete Account
}
