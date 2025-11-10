package com.dubpolls.backend.service;

import com.dubpolls.backend.model.Poll;
import com.dubpolls.backend.model.PollOption;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PollService {
    private final Map<Long, Poll> polls = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public Poll newPoll(String question, List<String> options, Instant closesAt) {
        var pollId = idCounter.incrementAndGet();
        var pollOptions = options.stream().map(PollOption::new).toList();
        var poll = Poll.newPoll(pollId, question, closesAt, pollOptions);
        polls.put(pollId, poll);
        return poll;
    }

    public Poll get(Long id) {
        Poll poll =  polls.get(id);
        if (poll == null) throw new NoSuchElementException();
        return poll;
    }
}
