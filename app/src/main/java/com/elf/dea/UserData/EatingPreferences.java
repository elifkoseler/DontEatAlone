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
    }
