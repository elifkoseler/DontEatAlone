package com.elf.dea.MeetingData;

import java.io.Serializable;

public class Restaurant implements Serializable {
    public String name;
    public double averageExpenses;
    EatType eatType = new EatType();
    PlaceFeature placeFeature = new PlaceFeature();
    public int score;

    public Restaurant() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAverageExpenses() {
        return averageExpenses;
    }

    public void setAverageExpenses(double averageExpenses) {
        this.averageExpenses = averageExpenses;
    }

    public EatType getEatType() {
        return eatType;
    }

    public void setEatType(EatType eatType) {
        this.eatType = eatType;
    }

    public PlaceFeature getPlaceFeature() {
        return placeFeature;
    }

    public void setPlaceFeature(PlaceFeature placeFeature) {
        this.placeFeature = placeFeature;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
