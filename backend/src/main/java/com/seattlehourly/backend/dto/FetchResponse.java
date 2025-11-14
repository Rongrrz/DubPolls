package com.seattlehourly.backend.dto;

import com.seattlehourly.backend.dto.fetch.RedditPost;

import java.util.List;

public record FetchResponse(
        List<RedditPost> redditPosts
) { }
