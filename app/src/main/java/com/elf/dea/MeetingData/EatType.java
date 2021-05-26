package com.elf.dea.MeetingData;

import java.io.Serializable;

public class EatType implements Serializable {
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

    public boolean isMeat() {
        return meat;
    }

    public void setMeat(boolean meat) {
        this.meat = meat;
    }

    public boolean isFish() {
        return fish;
    }

    public void setFish(boolean fish) {
        this.fish = fish;
    }

    public boolean isFastfood() {
        return fastfood;
    }

    public void setFastfood(boolean fastfood) {
        this.fastfood = fastfood;
    }

    public boolean isTraditional() {
        return traditional;
    }

    public void setTraditional(boolean traditional) {
        this.traditional = traditional;
    }

    public boolean isCafe() {
        return cafe;
    }

    public void setCafe(boolean cafe) {
        this.cafe = cafe;
    }

    public boolean isBar() {
        return bar;
    }

    public void setBar(boolean bar) {
        this.bar = bar;
    }
}
