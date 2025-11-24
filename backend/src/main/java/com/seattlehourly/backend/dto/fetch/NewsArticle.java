package com.seattlehourly.backend.dto.fetch;

public record NewsArticle(
        String title,
        String url,
        String source,
        String timeAgo,
        long createdUtc
) {}
