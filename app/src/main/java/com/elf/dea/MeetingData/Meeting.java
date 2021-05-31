package com.elf.dea.MeetingData;

import com.elf.dea.MeetingData.DateTimeData.Date;
import com.elf.dea.MeetingData.DateTimeData.Time;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Meeting implements Serializable {
    public String name;
    int day;
    int month;
    int year;
    int hour;
    int second;
    public String location;
    Restaurant restaurant = new Restaurant();

    public Meeting() {
        this.day = 0;
        this.month = 0;
        this.year = 0;
        this.hour = 0;
        this.second = 0;
        this.name = "";
        this.location = "";
    }

    public Meeting(String name, int day, int month, int year, int hour, int second, String location, Restaurant restaurant) {
        this.name = name;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.second = second;
        this.location = location;
        this.restaurant = restaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
