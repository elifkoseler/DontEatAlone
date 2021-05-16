package com.elf.dea.MeetingData;

public class PlaceFeature {
    boolean hasInnerSpace;
    boolean hasOuterSpace;
    boolean hasWifi;
    boolean hasSmokingArea;
    boolean hasAvailableForAnimals;

    public PlaceFeature(boolean hasInnerSpace, boolean hasOuterSpace, boolean hasWifi,
                        boolean hasSmokingArea, boolean hasAvailableForAnimals) {
        this.hasInnerSpace = hasInnerSpace;
        this.hasOuterSpace = hasOuterSpace;
        this.hasWifi = hasWifi;
        this.hasSmokingArea = hasSmokingArea;
        this.hasAvailableForAnimals = hasAvailableForAnimals;
    }

    public PlaceFeature() {
        this.hasInnerSpace = false;
        this.hasOuterSpace = false;
        this.hasWifi = false;
        this.hasSmokingArea = false;
        this.hasAvailableForAnimals = false;

    }
}
