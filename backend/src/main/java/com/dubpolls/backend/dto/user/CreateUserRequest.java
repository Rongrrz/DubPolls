package com.dubpolls.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank
        @Size(min = 5, max = 12)
        @Pattern(regexp = "^[a-zA-Z0-9_]+$")
        String username,

        @Size(max = 12)
        String displayName,

        @Email
        @Size(max = 50)
        String email
) {}
