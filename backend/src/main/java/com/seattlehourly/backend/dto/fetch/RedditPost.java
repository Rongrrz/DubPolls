package com.seattlehourly.backend.dto.fetch;

public record RedditPost(
        String title,
        String subreddit,
        String timeAgo,
        long created_utc,
        int comments,
        String url
) {}