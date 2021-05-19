package com.elf.dea.UserData;

import com.elf.dea.MeetingData.Meeting;

public class User {
    public String name;
    public String username;
    public String email;
    public String phone;
    public String location; //spinner ile seçenek sunulacak
    public String profilePhotoName;
    public int birthYear;
    public int score;

    public Interest interest = new Interest();
    public EatingPreferences eatingPreferences = new EatingPreferences();
    public Meeting meetingPreferences = new Meeting();

    public User() {
    }


    public User(String name, String username, String email, String phone, String location,
                String profilePhotoName, int birthYear, int score, Interest interest) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.profilePhotoName = profilePhotoName;
        this.birthYear = birthYear;
        this.score = score;
        this.interest = interest;
    }


}
