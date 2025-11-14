package com.seattlehourly.backend.archived.controller;

import com.seattlehourly.backend.archived.dto.user.CreateUserRequest;
import com.seattlehourly.backend.archived.dto.user.UserView;
import com.seattlehourly.backend.archived.model.User;
import com.seattlehourly.backend.archived.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserView create(@Valid @RequestBody CreateUserRequest req) {
        User u = userService.create(req.username(), req.email());
        return modelToDto(u);
    }

    @GetMapping("/{id}")
    public UserView getById(@PathVariable Long id) {
        return modelToDto(userService.getById(id));
    }

    private UserView modelToDto(User u) {
        return new UserView(u.id(), u.username(), u.displayName(), u.email(), u.createdAt());
    }
}
