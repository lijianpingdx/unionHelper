package com.dax.union.helper;

public class StoreEvent {
    String userName;
    String time;
    int rice=-0;
    int stone=0;
    int wood=0;
    int iron=0;
    int coin=0;
    long total=0L;

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

    public int getRice() {
        return rice;
    }

    public void addRice(int rice) {
        this.rice += rice;
        total+=rice;
    }

    public int getStone() {
        return stone;
    }

    public void addStone(int stone) {
        this.stone += stone;
        total+=stone;
    }

    public int getWood() {
        return wood;
    }

    public void addWood(int wood) {
        this.wood += wood;
        total+=wood;
    }

    public int getIron() {
        return iron;
    }

    public void addIron(int iron) {
        this.iron += iron;
        total+=iron;
    }

    public int getCoin() {
        return coin;
    }

    public void addCoin(int coin) {
        this.coin += coin;
        total+=coin;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
