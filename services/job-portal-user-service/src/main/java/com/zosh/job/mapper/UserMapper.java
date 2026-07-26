package com.zosh.job.mapper;

import com.zosh.job.dto.response.UserResponse;
import com.zosh.job.model.User;

import java.util.List;

public class UserMapper {
    public static UserResponse toDto(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFullName(user.getFullName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setProfileImage(user.getProfileImage());
        userResponse.setLastLogin(user.getLastLogin());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setRole(user.getRole());
        userResponse.setStatus(user.getStatus());
        return userResponse;
    }

    public static List<UserResponse> toDtoList(List<User> users) {
        return users.stream().map(UserMapper::toDto).toList();
    }
}
