package com.zosh.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zosh.job.controller.AuthController;
import com.zosh.job.controller.UserController;
import com.zosh.job.domain.UserRole;
import com.zosh.job.domain.UserStatus;
import com.zosh.job.dto.response.UserResponse;
import com.zosh.job.model.User;
import com.zosh.job.payload.AuthResponse;
import com.zosh.job.payload.LoginRequest;
import com.zosh.job.payload.SignupRequest;
import com.zosh.job.payload.UpdateUserRequest;
import com.zosh.job.service.AuthService;
import com.zosh.job.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class JobPortalUserServiceApplicationTests {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                        new AuthController(authService),
                        new UserController(userService))
                .build();
    }

    @Test
    void signupReturnsAuthResponse() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setFullName("Aarav Sharma");
        request.setEmail("aarav.sharma@example.com");
        request.setPhone("9876543210");
        request.setPassword("password123");
        request.setRole(UserRole.ROLE_JOB_SEEKER);

        AuthResponse response = new AuthResponse();
        response.setJwt("dummy-jwt-token");
        response.setTitle("welcome Aarav Sharma");
        response.setMessage("User registered successfully");
        response.setUser(userResponse());

        when(authService.signup(any(SignupRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("dummy-jwt-token"))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.user.email").value("aarav.sharma@example.com"));

        verify(authService).signup(any(SignupRequest.class));
    }

    @Test
    void loginReturnsAuthResponse() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("aarav.sharma@example.com");
        request.setPassword("password123");

        AuthResponse response = new AuthResponse();
        response.setJwt("dummy-login-token");
        response.setTitle("welcome Back --Aarav Sharma");
        response.setMessage("User logged in successfully");
        response.setUser(userResponse());

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("dummy-login-token"))
                .andExpect(jsonPath("$.message").value("User logged in successfully"))
                .andExpect(jsonPath("$.user.fullName").value("Aarav Sharma"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void getProfileReturnsUserFromEmailHeader() throws Exception {
        when(userService.getUserByEmail("aarav.sharma@example.com")).thenReturn(user());

        mockMvc.perform(get("/api/users/profile")
                        .header("X-User-Email", "aarav.sharma@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("aarav.sharma@example.com"))
                .andExpect(jsonPath("$.role").value("ROLE_JOB_SEEKER"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(userService).getUserByEmail("aarav.sharma@example.com");
    }

    @Test
    void updateProfileReturnsUpdatedUser() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFullName("Aarav S.");
        request.setPhone("9000000000");
        request.setProfileImage("https://example.com/avatar.png");

        UserResponse response = userResponse();
        response.setFullName("Aarav S.");
        response.setPhone("9000000000");
        response.setProfileImage("https://example.com/avatar.png");

        when(userService.updateprofile(eq("aarav.sharma@example.com"), any(UpdateUserRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/users/profile")
                        .header("X-User-Email", "aarav.sharma@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Aarav S."))
                .andExpect(jsonPath("$.phone").value("9000000000"))
                .andExpect(jsonPath("$.profileImage").value("https://example.com/avatar.png"));

        verify(userService).updateprofile(eq("aarav.sharma@example.com"), any(UpdateUserRequest.class));
    }

    @Test
    void getAllUsersReturnsUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user()));

        mockMvc.perform(get("/api/users/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("aarav.sharma@example.com"));

        verify(userService).getAllUsers();
    }

    @Test
    void suspendUserReturnsSuspendedStatus() throws Exception {
        UserResponse response = userResponse();
        response.setStatus(UserStatus.SUSPENDED);

        when(userService.suspendUser(1L)).thenReturn(response);

        mockMvc.perform(patch("/api/users/{userId}/suspend", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUSPENDED"));

        verify(userService).suspendUser(1L);
    }

    @Test
    void activateUserReturnsActiveStatus() throws Exception {
        UserResponse response = userResponse();
        response.setStatus(UserStatus.ACTIVE);

        when(userService.activateUser(1L)).thenReturn(response);

        mockMvc.perform(patch("/api/users/{userId}/activate", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(userService).activateUser(1L);
    }

    @Test
    void deleteUserReturnsDeletedStatus() throws Exception {
        UserResponse response = userResponse();
        response.setStatus(UserStatus.DELETED);

        when(userService.deleteUser(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELETED"));

        verify(userService).deleteUser(1L);
    }

    private User user() {
        return User.builder()
                .id(1L)
                .fullName("Aarav Sharma")
                .email("aarav.sharma@example.com")
                .phone("9876543210")
                .password("encoded-password")
                .role(UserRole.ROLE_JOB_SEEKER)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                .lastLogin(LocalDateTime.of(2026, 1, 2, 10, 0))
                .build();
    }

    private UserResponse userResponse() {
        return UserResponse.builder()
                .id(1L)
                .fullName("Aarav Sharma")
                .email("aarav.sharma@example.com")
                .phone("9876543210")
                .role(UserRole.ROLE_JOB_SEEKER)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                .lastLogin(LocalDateTime.of(2026, 1, 2, 10, 0))
                .build();
    }

}
