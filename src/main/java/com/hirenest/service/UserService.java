package com.hirenest.service;

import com.hirenest.dto.UserRequest;
import com.hirenest.entity.User;

import java.util.List;

public interface UserService {

    User createUser(UserRequest request);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, UserRequest request);

    void deleteUser(Long id);
}