package com.elf.dea.MeetingData;

import java.io.Serializable;

public class Meeting implements Serializable {
    public String name;
    int day;
    int month;
    int year;
    int hour;
    int second;
    String district;
    String address;
    String imageUrl;
    Restaurant restaurant = new Restaurant();

    public Meeting() {
        this.day = 0;
        this.month = 0;
        this.year = 0;
        this.hour = 0;
        this.second = 0;
        this.name = "";
        this.district = "";
        this.address = "";
        this.imageUrl = "";
    }

    public Meeting(String name, int day, int month, int year, int hour, int second, String district, Restaurant restaurant, String address) {
        this.name = name;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.second = second;
        this.district = district;
        this.restaurant = restaurant;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district ) {
        this.district  = district ;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
