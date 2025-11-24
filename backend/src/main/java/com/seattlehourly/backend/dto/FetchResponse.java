package com.seattlehourly.backend.dto;

import com.seattlehourly.backend.dto.fetch.NewsArticle;
import com.seattlehourly.backend.dto.fetch.RedditPost;
import com.seattlehourly.backend.dto.fetch.WeatherSummary;

import java.util.List;

public record FetchResponse(
        List<RedditPost> redditPosts,
        List<NewsArticle> news,
        WeatherSummary weather
) { }
