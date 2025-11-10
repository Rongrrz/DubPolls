package com.dubpolls.backend.model;

import java.time.Instant;

public record User(
        Long id,
        String username,
        String displayName,
        String email,
        Instant createdAt
) {
    public static User newUser(Long id, String username, String email) {
        return new User(id, username, username, email, Instant.now());
    }
}
