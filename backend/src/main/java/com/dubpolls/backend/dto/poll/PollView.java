package com.dubpolls.backend.dto.poll;

import java.time.Instant;
import java.util.List;

/**
 * Outgoing JSON DTO representing a poll.
 * Returned by the controller when a poll is created, fetched, or listed.
 */
public record PollView(
        Long id,
        String question,
        Instant createdAt,
        Instant closesAt,
        List<PollOptionView> options
) {}
