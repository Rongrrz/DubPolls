package com.dubpolls.backend.dto.poll;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

/**
 * Incoming JSON payload for creating a poll.
 * Represents what the client sends to the backend.
 */
public record CreatePollRequest(
        @NotBlank String question,

        @Size(min = 2, max = 10)
        List<@NotBlank String> options,

        Instant closesAt
) {}
