package com.elf.dea.data;

import java.time.LocalDate;

public class User {
    public String name;
    public String username;
    public String phone;
    public String location;
    public String profilePhotoName;
    public int birthYear;
    public int score;

    public User() {
    }

    public User(String name, String username, String phone, String location, String profilePhotoName, int birthYear, int score) {
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.location = location;
        this.profilePhotoName = profilePhotoName;
        this.birthYear = birthYear;
        this.score = score;
    }
}
