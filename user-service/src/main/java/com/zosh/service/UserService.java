package com.zosh.service;

import com.zosh.modal.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id) throws Exception;
    List<User> getAllUsers();
    User updateUser(Long id,User user) throws Exception;
    void deleteUser(Long id) throws Exception;

}
