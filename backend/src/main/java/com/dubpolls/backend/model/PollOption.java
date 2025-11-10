package com.dubpolls.backend.model;

import java.util.concurrent.atomic.AtomicInteger;

public class PollOption {
    private final String text;
    private final AtomicInteger votes = new AtomicInteger(0);

    public PollOption(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public int getVotes() {
        return votes.get();
    }

    public int incrementVote() {
        return votes.incrementAndGet();
    }
}
