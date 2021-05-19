package com.elf.dea.UserData;

public class Interest {
    boolean travel;
    boolean sports;
    boolean music;
    boolean art;
    boolean technology;

    public Interest(boolean travel, boolean sports, boolean music, boolean art, boolean technology) {
        this.travel = travel;
        this.sports = sports;
        this.music = music;
        this.art = art;
        this.technology = technology;
    }

    public Interest() {
        this.travel = false;
        this.sports = false;
        this.music = false;
        this.art = false;
        this.technology = false;
    }

    public void setTravel(boolean travel) {
        this.travel = travel;
    }

    public void setSports(boolean sports) {
        this.sports = sports;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public void setArt(boolean art) {
        this.art = art;
    }

    public void setTechnology(boolean technology) {
        this.technology = technology;
    }

    public boolean isTravel() {
        return travel;
    }

    public boolean isSports() {
        return sports;
    }

    public boolean isMusic() {
        return music;
    }

    public boolean isArt() {
        return art;
    }

    public boolean isTechnology() {
        return technology;
    }
}
