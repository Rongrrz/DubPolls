package com.seattlehourly.backend.controller;

import com.seattlehourly.backend.dto.FetchResponse;
import com.seattlehourly.backend.service.RedditService;
import com.seattlehourly.backend.service.WeatherService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fetch")
@CrossOrigin(origins = "http://localhost:5173")
public class MainController {
    private final RedditService redditService;
    private final WeatherService weatherService;

    public MainController(RedditService redditService, WeatherService weatherService) {
        this.redditService = redditService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public FetchResponse fetchDashboard() {
        return new FetchResponse(
                redditService.getPosts(),
                weatherService.getSummary()
        );
    }
}