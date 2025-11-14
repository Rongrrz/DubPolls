package com.seattlehourly.backend.archived.dto.user;

import java.time.Instant;

public record UserView(
        Long id,
        String username,
        String displayName,
        String email,
        Instant createdAt
) {}
