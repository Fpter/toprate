package com.example.weathertoprate.service;

import com.example.weathertoprate.entity.WeatherEntity;

import java.text.ParseException;

public interface WeatherService {
    void insertData(String fromDate, String toDate) throws ParseException;

    WeatherEntity getMin(String date);

    WeatherEntity getMax(String date);
}
