package com.example.weathertoprate.controller;

import com.example.weathertoprate.entity.WeatherEntity;
import com.example.weathertoprate.exception.ValidateException;
import com.example.weathertoprate.response.WeatherResponse;
import com.example.weathertoprate.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
public class WeatherController {

    private final String OLD_FORMAT = "yyyy-MM-dd'T'hh:mm";

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/insert")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getData(@RequestParam String fromDate, @RequestParam String toDate) throws ParseException {
        validateDate(fromDate);
        validateDate(toDate);

        weatherService.insertData(fromDate, toDate);
    }

    @GetMapping("/get")
    public ResponseEntity<WeatherResponse> get(@RequestParam String date) {

        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception ex) {
            throw new ValidateException(ex.getLocalizedMessage());
        }

        WeatherEntity timeMin = weatherService.getMin(date);
        WeatherEntity timeMax = weatherService.getMax(date);
        return ResponseEntity.ok(WeatherResponse.builder()
                .timeWithMaxTemp(timeMax)
                .timeWithMinTemp(timeMin)
                .build());
    }

    private void validateDate(String date) {
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception ex) {
            throw new ValidateException(ex.getLocalizedMessage());
        }
    }

}
