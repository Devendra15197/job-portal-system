package com.zosh.job.payload;

import com.zosh.job.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class SignupRequest {

    @NotBlank(message = "Full name is mandatory")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    private String phone;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotNull(message = "Role is mandatory")
    private UserRole role;
}
