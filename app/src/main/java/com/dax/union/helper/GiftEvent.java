package com.dax.union.helper;

public class GiftEvent {
    public GiftEvent(String userName, String time, int level, String type) {
        this.userName = userName;
        this.time = time;
        this.level = level;
        this.type = type;
    }

    String userName;
    String time;

    int level;
    String type;




    @Override
    public String toString() {
        return level+"级"+type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
