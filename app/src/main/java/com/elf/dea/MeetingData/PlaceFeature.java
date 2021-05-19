package com.elf.dea.MeetingData;

public class PlaceFeature {
    boolean InnerSpace;
    boolean OuterSpace;
    boolean Wifi;
    boolean SmokingArea;
    boolean AvailableForAnimals;

    public PlaceFeature(boolean InnerSpace, boolean OuterSpace, boolean Wifi,
                        boolean SmokingArea, boolean AvailableForAnimals) {
        this.InnerSpace = InnerSpace;
        this.OuterSpace = OuterSpace;
        this.Wifi = Wifi;
        this.SmokingArea = SmokingArea;
        this.AvailableForAnimals = AvailableForAnimals;
    }

    public PlaceFeature() {
        this.InnerSpace = false;
        this.OuterSpace = false;
        this.Wifi = false;
        this.SmokingArea = false;
        this.AvailableForAnimals = false;

    }

    public boolean isInnerSpace() {
        return InnerSpace;
    }

    public void setInnerSpace(boolean InnerSpace) {
        this.InnerSpace = InnerSpace;
    }

    public boolean isOuterSpace() {
        return OuterSpace;
    }

    public void setOuterSpace(boolean OuterSpace) {
        this.OuterSpace = OuterSpace;
    }

    public boolean isWifi() {
        return Wifi;
    }

    public void setWifi(boolean Wifi) {
        this.Wifi = Wifi;
    }

    public boolean isSmokingArea() {
        return SmokingArea;
    }

    public void setSmokingArea(boolean SmokingArea) {
        this.SmokingArea = SmokingArea;
    }

    public boolean isAvailableForAnimals() {
        return AvailableForAnimals;
    }

    public void setAvailableForAnimals(boolean AvailableForAnimals) {
        this.AvailableForAnimals = AvailableForAnimals;
    }
}
