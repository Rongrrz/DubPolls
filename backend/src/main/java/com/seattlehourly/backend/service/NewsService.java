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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.seattlehourly.backend.utils.TimeUtils.formatTimeAgo;

@Service
public class NewsService {

    private static final String FEED_URL = "https://www.king5.com/feeds/googlenews";

    private final RestTemplate restTemplate = new RestTemplate();

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
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        Document doc = factory.newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        NodeList urlNodes = doc.getElementsByTagName("url");
        List<NewsArticle> articles = new ArrayList<>(Math.min(urlNodes.getLength(), 3));

        for (int i = 0; i < urlNodes.getLength(); i++) {
            Element urlEl = (Element) urlNodes.item(i);
            String link = getText(urlEl, "loc");
            NodeList newsNodes = urlEl.getElementsByTagName("news:news");
            if (link == null || newsNodes.getLength() == 0) continue;

            Element newsEl = (Element) newsNodes.item(0);
            String title = getText(newsEl, "news:title");
            String pubDateStr = getText(newsEl, "news:publication_date");
            if (title == null) continue;

            long createdUtc;
            try {
                var date = java.time.LocalDate.parse(pubDateStr);
                createdUtc = date.atStartOfDay(java.time.ZoneOffset.UTC)
                        .toInstant()
                        .toEpochMilli();
            } catch (Exception e) {
                createdUtc = System.currentTimeMillis();
            }

            articles.add(new NewsArticle(
                    title,
                    link,
                    "King 5",
                    formatTimeAgo(createdUtc),
                    createdUtc
            ));
        }

        articles.sort(Comparator.comparingLong(NewsArticle::createdUtc).reversed());
        if (articles.size() > 3) {
            articles = articles.subList(0, 3);
        }

        return List.copyOf(articles);
    }

    private String getText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0) return null;
        Node node = list.item(0);
        if (node == null) return null;
        return node.getTextContent();
    }
}
