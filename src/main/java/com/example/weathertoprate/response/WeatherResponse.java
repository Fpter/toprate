package com.example.weathertoprate.response;

import com.example.weathertoprate.entity.WeatherEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeatherResponse {
    private WeatherEntity timeWithMinTemp;
    private WeatherEntity timeWithMaxTemp;
}
