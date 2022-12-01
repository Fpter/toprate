package com.example.weathertoprate.service;

import com.example.weathertoprate.entity.WeatherEntity;
import com.example.weathertoprate.exception.BusinessException;
import com.example.weathertoprate.repository.WeatherRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService{
    private final String OLD_FORMAT = "yyyy-MM-dd'T'hh:mm";
    private final String URL_WEATHER_FORMAT = "https://api.open-meteo.com/v1/forecast?latitude=21.04&longitude=105.80&hourly=temperature_2m&start_date=%s&end_date=%s";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeatherRepository weatherRepository;


    @Override
    public void insertData(String fromDate, String toDate) throws ParseException {
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
                weatherRepository.save(entity);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public WeatherEntity getMin(String date) {
        String messageError = String.format("%s not found", date);
        return weatherRepository.getMin(date).orElseThrow(() -> new BusinessException(messageError));
    }

    @Override
    public WeatherEntity getMax(String date) {
        String messageError = String.format("%s not found", date);
        return weatherRepository.getMax(date).orElseThrow(() -> new BusinessException(messageError));
    }

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
}
