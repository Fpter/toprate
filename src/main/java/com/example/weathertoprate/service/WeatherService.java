package com.example.weathertoprate.service;

import com.example.weathertoprate.entity.WeatherEntity;

public interface WeatherService {
    WeatherEntity save(WeatherEntity entity);

    WeatherEntity getMin(String date);

    WeatherEntity getMax(String date);
}
