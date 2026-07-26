package com.zosh.job.service.impl;

import com.zosh.job.domain.UserRole;
import com.zosh.job.domain.UserStatus;
import com.zosh.job.mapper.UserMapper;
import com.zosh.job.model.User;
import com.zosh.job.payload.AuthResponse;
import com.zosh.job.payload.LoginRequest;
import com.zosh.job.payload.SignupRequest;
import com.zosh.job.repository.UserRepository;
import com.zosh.job.security.CustomUserDetailsService;
import com.zosh.job.security.JwtProvider;
import com.zosh.job.service.AuthService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;


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
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .status(UserStatus.ACTIVE)
                .lastLogin(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), req.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication, savedUser.getId());


        AuthResponse response = new AuthResponse();
        response.setTitle("welcome " + savedUser.getFullName());
        response.setMessage("User registered successfully");
        response.setJwt(jwt);
        response.setUser(UserMapper.toDto(savedUser));
        return response;
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        Authentication authentication = authenticate(req.getEmail(), req.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(req.getEmail());
        String jwt = jwtProvider.generateToken(authentication, user.getId());

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setTitle("welcome Back --" + user.getFullName());
        response.setMessage("User logged in successfully");
        response.setJwt(jwt);
        response.setUser(UserMapper.toDto(user));
        return response;
    }

    private Authentication authenticate(@Email(message = "Email should be valid") @NotBlank(message = "Email is mandatory") String email, @NotBlank(message = "Password is mandatory") String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            if (userDetails == null) {
                throw new RuntimeException("User not found with email " + email);
            }
            throw new RuntimeException("Invalid password for email " + email);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
