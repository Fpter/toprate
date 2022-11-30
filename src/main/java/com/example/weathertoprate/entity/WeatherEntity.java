package com.example.weathertoprate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDateTime;

class WeatherID implements Serializable{
    private LocalDateTime date;
    private String time;

    public WeatherID(LocalDateTime date, String time) {
        this.date = date;
        this.time = time;
    }

    public WeatherID() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

@Entity
@Table(name = "weather")
@Data
@IdClass(WeatherID.class)
public class WeatherEntity implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Column(name = "temp")
    private double temp;

    @Id
    @Column(name = "date")
    private LocalDateTime date;

    @Id
    @Column(name = "time")
    private String time;

}
