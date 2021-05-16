package com.elf.dea.UserData;

public class EatingPreferences {
    Likes likes;
    Dislikes dislikes;

    public EatingPreferences() {
        this.likes = new Likes();
        this.dislikes = new Dislikes();
    }
}
