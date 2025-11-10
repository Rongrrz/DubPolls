package com.dubpolls.backend.service;

import com.dubpolls.backend.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<Long, User> byId = new HashMap<>();
    private final Map<String, Long> idByUsername = new HashMap<>(); // normalized username -> id
    private final AtomicLong ids = new AtomicLong(0);

    public User create(String rawUsername, String email) {
        String username = normalize(rawUsername);
        Long id = ids.incrementAndGet();
        User u = User.newUser(id, username, email);
        byId.put(id, u);
        idByUsername.put(username, id);
        return u;
    }

    public User getById(Long id) {
        User u = byId.get(id);
        if (u == null) throw new NoSuchElementException();
        return u;
    }

    public User getByUsername(String rawUsername) {
        Long id = idByUsername.get(normalize(rawUsername));
        if (id == null) throw new NoSuchElementException();
        return getById(id);
    }

    private String normalize(String username) {
        return username == null ? null : username.trim().toLowerCase();
    }
}
