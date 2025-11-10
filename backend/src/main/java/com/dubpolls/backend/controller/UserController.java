package com.dubpolls.backend.controller;

import com.dubpolls.backend.dto.user.CreateUserRequest;
import com.dubpolls.backend.dto.user.UserView;
import com.dubpolls.backend.model.User;
import com.dubpolls.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService users;

    public UserController(UserService users) { this.users = users; }

    @PostMapping
    public UserView create(@Valid @RequestBody CreateUserRequest req) {
        User u = users.create(req.username(), req.email());
        return modelToDto(u);
    }

    @GetMapping("/{id}")
    public UserView getById(@PathVariable Long id) {
        return modelToDto(users.getById(id));
    }

    private UserView modelToDto(User u) {
        return new UserView(u.id(), u.username(), u.displayName(), u.email(), u.createdAt());
    }
}
