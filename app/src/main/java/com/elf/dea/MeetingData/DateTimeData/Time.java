package com.elf.dea.MeetingData.DateTimeData;

import java.io.Serializable;

public class Time implements Serializable {
    int hour;
    int second;

    public Time(int hour, int second) {
        this.hour = hour;
        this.second = second;
    }

    public Time() {

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
