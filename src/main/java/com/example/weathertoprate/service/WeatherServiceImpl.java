package com.example.weathertoprate.service;

import com.example.weathertoprate.entity.WeatherEntity;
import com.example.weathertoprate.exception.BusinessException;
import com.example.weathertoprate.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService{

    @Autowired
    private WeatherRepository weatherRepository;

    @Override
    public WeatherEntity save(WeatherEntity entity) {
        try {
            return weatherRepository.save(entity);
        }catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
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
}
