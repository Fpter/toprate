package com.example.weathertoprate.repository;

import com.example.weathertoprate.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, LocalDateTime> {
    @Query(value = "select top 1 *\n" +
            "from weather where date=?1\n" +
            "ORDER by temp ", nativeQuery = true)
    Optional<WeatherEntity> getMin(String date);

    @Query(value = "select top 1 *\n" +
            "from weather where date=?1\n" +
            "ORDER by temp desc", nativeQuery = true)
    Optional<WeatherEntity> getMax(String date);

}
