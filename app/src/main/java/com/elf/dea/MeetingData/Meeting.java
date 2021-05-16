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
}
