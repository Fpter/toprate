package com.example.weathertoprate.controller;

import com.example.weathertoprate.entity.WeatherEntity;
import com.example.weathertoprate.exception.ValidateException;
import com.example.weathertoprate.response.WeatherResponse;
import com.example.weathertoprate.service.WeatherService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
public class WeatherController {

    private final String OLD_FORMAT = "yyyy-MM-dd'T'hh:mm";
    private final String URL_WEATHER_FORMAT = "https://api.open-meteo.com/v1/forecast?latitude=21.04&longitude=105.80&hourly=temperature_2m&start_date=%s&end_date=%s";

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/insert")
    public ResponseEntity<Object> getData(@RequestParam String fromDate, @RequestParam String toDate) throws ParseException {
        validateDate(fromDate);
        validateDate(toDate);

        JSONObject res = new JSONObject(
                restTemplate.getForObject(String.format(URL_WEATHER_FORMAT, fromDate, toDate), String.class));
        JSONArray timeArray = res.getJSONObject("hourly").getJSONArray("time");
        List<Object> times = timeArray.toList();

        JSONArray tempArray = res.getJSONObject("hourly").getJSONArray("temperature_2m");
        List<Object> temps = tempArray.toList();

        for (int i = 0; i < times.size(); i++) {
            String dateTime = (String) times.get(i);
            double temp = Double.parseDouble(temps.get(i).toString());
            WeatherEntity entity = new WeatherEntity();
            entity.setDate(getDate(dateTime));
            entity.setTemp(temp);
            entity.setTime(getTime(dateTime));
            try {
                weatherService.save(entity);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return ResponseEntity.ok(res);
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


    /*
    * Convert string to LocalDateTime
    *
    *
    * @author AnhTQ33
    * */
    private LocalDateTime getDate(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = sdf.parse(dateTime);
        return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
    }

    private String getTime(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = sdf.parse(dateTime);
        sdf.applyPattern("hh:mm a");
        return sdf.format(d);
    }

    private void validateDate(String date) {
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception ex) {
            throw new ValidateException(ex.getLocalizedMessage());
        }
    }

}
