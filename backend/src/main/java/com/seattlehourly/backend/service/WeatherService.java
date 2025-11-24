package com.seattlehourly.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seattlehourly.backend.config.Constants;
import com.seattlehourly.backend.dto.fetch.WeatherSummary;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private WeatherSummary cachedSummary;

    public WeatherService(
            @Qualifier("emptyRestTemplate") RestTemplate restTemplate,
            ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public WeatherSummary getSummary() {
        return cachedSummary;
    }

    @PostConstruct
    public void initialize() {
        updateSummary();
    }

    @Scheduled(fixedRate = 300_000)
    public void updateSummary() {
        String url =
                "https://api.open-meteo.com/v1/forecast"
                        + "?latitude=" + Constants.SEATTLE_LAT
                        + "&longitude=" + Constants.SEATTLE_LON
                        + "&hourly=precipitation_probability"
                        + "&current_weather=true"
                        + "&temperature_unit=fahrenheit"
                        + "&windspeed_unit=mph"
                        + "&timezone=auto";
        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);

            JsonNode current = root.path("current_weather");

            double temperature = current.path("temperature").asDouble();
            double wind = current.path("windspeed").asDouble();
            String currentTime = current.path("time").asText();

            JsonNode hourly = root.path("hourly");
            JsonNode times = hourly.path("time");
            JsonNode rainArray = hourly.path("precipitation_probability");

            int rainChance = 0;
            for (int i = 0; i < times.size(); i++) {
                if (currentTime.equals(times.get(i).asText())) {
                    rainChance = rainArray.get(i).asInt();
                    break;
                }
            }
            cachedSummary = new WeatherSummary(temperature, wind, rainChance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch weather", e);
        }
    }
}
