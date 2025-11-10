package com.dubpolls.backend.controller;

import com.dubpolls.backend.dto.poll.CreatePollRequest;
import com.dubpolls.backend.dto.poll.PollOptionView;
import com.dubpolls.backend.dto.poll.PollView;
import com.dubpolls.backend.model.Poll;
import com.dubpolls.backend.service.PollService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping
    public PollView create(@Valid @RequestBody CreatePollRequest request) {
        Poll p = pollService.newPoll(request.question(), request.options(), request.closesAt());
        return modelToDto(p);
    }

    @GetMapping("/{id}")
    public PollView get(@PathVariable Long id) {
        return modelToDto(pollService.get(id));
    }

    private PollView modelToDto(Poll p) {
        List<PollOptionView> options = p.options().stream()
                .map(o -> new PollOptionView(o.getText()))
                .toList();
        return new PollView(p.id(), p.question(), p.createdAt(), p.closesAt(), options);
    }
}
