package com.seattlehourly.backend.dto.fetch;

public record WeatherSummary(
        double temperature,
        double windMPH,
        int rainChance
) {}
