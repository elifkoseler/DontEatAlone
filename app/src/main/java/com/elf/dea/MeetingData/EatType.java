package com.elf.dea.MeetingData;

public class EatType {
    boolean meat;
    boolean fish;
    boolean fastfood;
    boolean traditional;
    boolean cafe;
    boolean bar;

    public EatType(boolean meat, boolean fish, boolean fastfood, boolean traditional, boolean cafe, boolean bar) {
        this.meat = meat;
        this.fish = fish;
        this.fastfood = fastfood;
        this.traditional = traditional;
        this.cafe = cafe;
        this.bar = bar;
    }

    public EatType() {
        this.meat = false;
        this.fish = false;
        this.fastfood = false;
        this.traditional = false;
        this.cafe = false;
        this.bar = false;
    }
}
