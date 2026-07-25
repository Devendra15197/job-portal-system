package com.zosh.job.service.impl;

import com.zosh.job.domain.UserRole;
import com.zosh.job.model.User;
import com.zosh.job.payload.AuthResponse;
import com.zosh.job.payload.LoginRequest;
import com.zosh.job.payload.SignupRequest;
import com.zosh.job.repository.UserRepository;
import com.zosh.job.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Override
    public AuthResponse signup(SignupRequest req) throws Exception {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new Exception("Email already exists");

        }
        if (req.getRole() == UserRole.ROLE_ADMIN) {
            throw new Exception("Cannot self-register as role admin");
        }
        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .password(req.getPassword())
                .role(req.getRole())
                .lastLong(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setTitle("welcome " + savedUser.getFullName());
        response.setMessage("User registered successfully");
        response.setJwt("dummy-jwt-token");
        response.setUser();
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        return null;
    }
}
