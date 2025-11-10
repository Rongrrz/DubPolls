package com.dubpolls.backend.model;

import java.time.Instant;
import java.util.List;

public record Poll(
        Long id,
        String question,
        Instant createdAt,
        Instant closesAt,
        List<PollOption> options
) {}
