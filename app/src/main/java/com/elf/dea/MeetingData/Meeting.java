package com.elf.dea.MeetingData;

import java.time.LocalDate;
import java.time.LocalTime;

public class Meeting {
    public String name;
    public LocalDate date;
    public LocalTime time;
    public String location;
    Restaurant restaurant = new Restaurant();

    public Meeting() {
    }

    public Meeting(String name, LocalDate date, LocalTime time, String location, Restaurant restaurant) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.restaurant = restaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
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
}
