package com.seattlehourly.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seattlehourly.backend.dto.fetch.RedditPost;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RedditService {
    private static final String SUBREDDIT_URL = "https://www.reddit.com/r/%s/new/.json?limit=%d";
    private static final List<String> SUBREDDITS =
            List.of("udub", "Seattle", "SeattleWA", "Seahawks", "Mariners");

    private final RestTemplate rest;
    private final ObjectMapper mapper;
    private List<RedditPost> cachedPosts = List.of();


    public RedditService(RestTemplate redditRestTemplate, ObjectMapper mapper) {
        this.rest = redditRestTemplate;
        this.mapper = mapper;
    }

    // Called once on startup to warm the cache
    @PostConstruct
    public void initCache() {
        fetchAllSeattleClusterPosts();
    }

    public List<RedditPost> getPosts() {
        return cachedPosts;
    }

    @Scheduled(fixedRate = 300_000) // 5 minutes per
    public void fetchAllSeattleClusterPosts() {
        List<RedditPost> combined = new ArrayList<>();
        for (String subreddit : SUBREDDITS) {
            combined.addAll(fetchSubredditPosts(subreddit, 5));
        }

        combined.sort((a, b) -> Long.compare(b.created_utc(), a.created_utc()));

        cachedPosts = combined.stream()
                .limit(5)
                .toList();
    }

    private List<RedditPost> fetchSubredditPosts(String subreddit, int limit) {
        String url = SUBREDDIT_URL.formatted(subreddit, limit);

        try {
            String json = rest.getForObject(url, String.class);
            if (json == null) return List.of();

            JsonNode root = mapper.readTree(json);
            JsonNode children = root.path("data").path("children");

            List<RedditPost> posts = new ArrayList<>();

            if (children.isArray()) {
                for (JsonNode child : children) {
                    JsonNode data = child.path("data");

                    String title = data.path("title").asText("");
                    String permalink = data.path("permalink").asText("");
                    int comments = data.path("num_comments").asInt(0);
                    long createdUtc = data.path("created_utc").asLong(0);

                    if (title.isBlank() || permalink.isBlank()) continue;

                    var newPost = new RedditPost(
                            title,
                            subreddit,
                            formatTimeAgo(createdUtc),
                            createdUtc,
                            comments,
                            "https://www.reddit.com" + permalink
                    );
                    posts.add(newPost);
                }
            }

            return posts;
        } catch (Exception e) {
            System.err.println("Reddit fetch failed for r/" + subreddit + ": " + e.getMessage());
            return List.of();
        }
    }

    private String formatTimeAgo(long createdUtc) {
        Instant created = Instant.ofEpochSecond(createdUtc);
        long minutes = Duration.between(created, Instant.now()).toMinutes();

        if (minutes < 1) return "just now";
        if (minutes == 1) return "1 min ago";
        if (minutes < 60) return minutes + " mins ago";

        long hours = minutes / 60;
        if (hours == 1) return "1 hour ago";
        return hours + " hours ago";
    }
}
