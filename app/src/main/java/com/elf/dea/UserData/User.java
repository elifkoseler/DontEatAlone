package com.elf.dea.UserData;

import com.elf.dea.MeetingData.Meeting;

import java.io.Serializable;

public class User implements Serializable{

    String name;
    String username;
    String email;
    String phone;
    String location; //spinner ile seçenek sunulacak
    String profileImageUrl;
    int birthYear;
    int score;
    int count;


    public Interest interest = new Interest();
    public EatingPreferences eatingPreferences = new EatingPreferences();
    public Meeting meetingPreferences = new Meeting();

    public User() {
    }

    public User(String name, String username, String email, String phone, String location, String profileImageUrl, int birthYear,
                int score, Interest interest, EatingPreferences eatingPreferences, Meeting meetingPreferences) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.profileImageUrl = profileImageUrl;
        this.birthYear = birthYear;
        this.score = score;
        this.interest = interest;
        this.eatingPreferences = eatingPreferences;
        this.meetingPreferences = meetingPreferences;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Interest getInterest() {
        return interest;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }

    public EatingPreferences getEatingPreferences() {
        return eatingPreferences;
    }

    public void setEatingPreferences(EatingPreferences eatingPreferences) {
        this.eatingPreferences = eatingPreferences;
    }

    public Meeting getMeetingPreferences() {
        return meetingPreferences;
    }

    public void setMeetingPreferences(Meeting meetingPreferences) {
        this.meetingPreferences = meetingPreferences;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
