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
}
