package com.seattlehourly.backend.service;

import com.seattlehourly.backend.dto.fetch.NewsArticle;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.seattlehourly.backend.utils.TimeUtils.formatTimeAgo;

@Service
public class NewsService {

    private static final String FEED_URL = "https://www.king5.com/feeds/googlenews";

    private final RestTemplate restTemplate = new RestTemplate();
    private final DateTimeFormatter RFC_1123 = DateTimeFormatter.RFC_1123_DATE_TIME;

    private List<NewsArticle> cachedArticles = List.of();

    public List<NewsArticle> getNews() {
        return cachedArticles;
    }

    @PostConstruct
    public void init() {
        refreshFeed();
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    private void refreshFeed() {
        try {
            String xml = restTemplate.getForObject(FEED_URL, String.class);
            if (xml == null) return;

            cachedArticles = parseRss(xml);
        } catch (Exception e) {
            System.out.println("Bad stuff happened while refreshing news feed");
        }
    }

    private List<NewsArticle> parseRss(String xml) throws Exception {
        List<NewsArticle> articles = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        Document doc = factory.newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        NodeList urlNodes = doc.getElementsByTagName("url");

        for (int i = 0; i < urlNodes.getLength(); i++) {
            Element urlEl = (Element) urlNodes.item(i);
            String link = getText(urlEl, "loc");

            NodeList newsNodes = urlEl.getElementsByTagName("news:news");
            if (newsNodes.getLength() == 0) continue;
            Element newsEl = (Element) newsNodes.item(0);

            String title = getText(newsEl, "news:title");
            String pubDateStr = getText(newsEl, "news:publication_date");

            if (link == null || title == null) {
                continue; // skip incomplete entries
            }

            long createdUtc;
            if (pubDateStr != null && !pubDateStr.isEmpty()) {
                try {
                    var date = java.time.LocalDate.parse(pubDateStr);
                    var zdt = date.atStartOfDay(java.time.ZoneOffset.UTC);
                    createdUtc = zdt.toInstant().toEpochMilli();
                } catch (Exception e) {
                    createdUtc = System.currentTimeMillis();
                }
            } else {
                createdUtc = System.currentTimeMillis();
            }

            NewsArticle article = new NewsArticle(
                    title,
                    link,
                    "King 5",
                    formatTimeAgo(createdUtc),
                    createdUtc
            );

            articles.add(article);
        }

        // sort newest → oldest
        articles.sort(Comparator.comparingLong(NewsArticle::createdUtc).reversed());
        if (articles.size() > 3) {
            articles = new ArrayList<>(articles.subList(0, 3));
        }
        
        return Collections.unmodifiableList(articles);
    }


    private String getText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0) return null;
        Node node = list.item(0);
        if (node == null) return null;
        return node.getTextContent();
    }
}
