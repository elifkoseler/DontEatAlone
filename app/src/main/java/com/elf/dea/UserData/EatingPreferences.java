package com.elf.dea.UserData;

public class EatingPreferences {
        boolean meat;
        boolean fish;
        boolean fastfood;
        boolean traditional;
        boolean coffee;
        boolean drink;

        public EatingPreferences(boolean meat, boolean fish, boolean fastfood, boolean traditional, boolean coffee, boolean drink) {
            this.meat = meat;
            this.fish = fish;
            this.fastfood = fastfood;
            this.traditional = traditional;
            this.coffee = coffee;
            this.drink = drink;
        }
        public EatingPreferences() {
            this.meat = false;
            this.fish = false;
            this.fastfood = false;
            this.traditional = false;
            this.coffee = false;
            this.drink = false;
        }


    public void setMeat(boolean meat) {
        this.meat = meat;
    }

    public void setFish(boolean fish) {
        this.fish = fish;
    }

    public void setFastfood(boolean fastfood) {
        this.fastfood = fastfood;
    }

    public void setTraditional(boolean traditional) {
        this.traditional = traditional;
    }

    public void setCoffee(boolean coffee) {
        this.coffee = coffee;
    }

    public void setDrink(boolean drink) {
        this.drink = drink;
    }

    public boolean isMeat() {
        return meat;
    }

    public boolean isFish() {
        return fish;
    }

    public boolean isFastfood() {
        return fastfood;
    }

    public boolean isTraditional() {
        return traditional;
    }

    public boolean isCoffee() {
        return coffee;
    }

    public boolean isDrink() {
        return drink;
    }
}
