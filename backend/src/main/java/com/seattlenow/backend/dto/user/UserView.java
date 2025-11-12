package com.dubpolls.backend.dto.user;

import java.time.Instant;

public record UserView(
        Long id,
        String username,
        String displayName,
        String email,
        Instant createdAt
) {}
