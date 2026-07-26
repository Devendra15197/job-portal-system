package com.zosh.job.service;

import com.zosh.job.dto.response.UserResponse;
import com.zosh.job.model.User;
import com.zosh.job.payload.UpdateUserRequest;

import java.util.List;

public interface UserService {

    User getUserByEmail(String email);
    User getUserById(Long id);
    List<User> getAllUsers();
    UserResponse updateprofile(String email, UpdateUserRequest updateUserRequest);

    //admin action
    UserResponse suspendUser(Long id);
    UserResponse activateUser(Long id);
    UserResponse deleteUser(Long id);

}
