package com.zosh.job.service.impl;

import com.zosh.job.domain.UserStatus;
import com.zosh.job.dto.response.UserResponse;
import com.zosh.job.mapper.UserMapper;
import com.zosh.job.model.User;
import com.zosh.job.payload.UpdateUserRequest;
import com.zosh.job.repository.UserRepository;
import com.zosh.job.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserResponse updateprofile(String email, UpdateUserRequest updateUserRequest) {
        User user = getUserByEmail(email);
        if (updateUserRequest.getFullName() != null) {
            user.setFullName(updateUserRequest.getFullName());
        }
        if (updateUserRequest.getPhone() != null) {
            user.setPhone(updateUserRequest.getPhone());
        }
        if (updateUserRequest.getProfileImage() != null) {
            user.setProfileImage(updateUserRequest.getProfileImage());
        }
        User updatedUser = userRepository.save(user);
        return UserMapper.toDto(updatedUser);
    }

    @Override
    public UserResponse suspendUser(Long id) {
        User user = getUserById(id);

        user.setStatus(UserStatus.SUSPENDED);
        user.setSuspendedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return UserMapper.toDto(updatedUser);
    }

    @Override
    public UserResponse activateUser(Long id) {
        User user = getUserById(id);

        user.setStatus(UserStatus.ACTIVE);
        user.setSuspendedAt(null);

        User updatedUser = userRepository.save(user);
        return UserMapper.toDto(updatedUser);
    }

    @Override
    public UserResponse deleteUser(Long id) {
        User user = getUserById(id);

        user.setStatus(UserStatus.DELETED);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
        return UserMapper.toDto(user);
    }
}
