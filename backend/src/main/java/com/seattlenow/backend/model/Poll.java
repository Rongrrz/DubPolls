package com.dubpolls.backend.model;

import java.time.Instant;
import java.util.List;

public record Poll(
        Long id,
        String question,
        Instant createdAt,
        Instant closesAt,
        List<PollOption> options
) {
    public static Poll newPoll(Long id, String question, Instant closesAt, List<PollOption> options) {
        return new Poll(id, question, Instant.now(), closesAt, options);
    }
}
