package ru.t1meshift.landlords;

import android.os.Parcelable;

/**
 * Created by t1meshft on 09.10.2016.
 */
public class LandCell {
    private int owner; //0 - no one, 1-4 - R,G,B,Y
    private int income;
    private int armor;
    public LandCell(int income, int armor) {
        this.income = income;
        this.armor = armor;
        this.owner = 0;
    }
    public int getOwner() {
        return this.owner;
    }
    public void setOwner(int owner) {
        this.owner = owner;
    }
    public int getIncome() {
        return this.income;
    }
    public int getArmor() {
        return this.armor;
    }
    public void enforce() {
        this.armor++;
    }
    public int getPrice() {
        return (this.income + this.armor)*10;
    }
}
