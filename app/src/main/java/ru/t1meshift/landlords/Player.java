package ru.t1meshift.landlords;

import java.util.ArrayList;

/**
 * Created by t1meshft on 09.10.2016.
 */
public class Player {
    private int flag;
    private int money;
    public Player(int flag) {
        this.flag = flag;
        this.money = 0;
    }
    public int getFlag() {
        return this.flag;
    }
    public ArrayList<CellView> getOwnCells(CellView[] cells) {
        ArrayList<CellView> result = new ArrayList<>();
        for (int i=0; i < cells.length; i++) {
            if (cells[i].getCell().getOwner() == this.flag)
                result.add(cells[i]);
        }
        return result;
    }
    public int getTotalIncome(ArrayList<CellView> ownCells) {
        int totalIncome = 0;
        CellView[] cells = ownCells.toArray(new CellView[ownCells.size()]);
        for (int i = 0; i < cells.length; i++) {
            totalIncome += cells[i].getCell().getIncome();
        }
        return totalIncome;
    }
    public int getMoney() {
        return this.money;
    }
    public void spend(int amount) {
        this.money -= amount;
    }
    public void earn(int amount) {
        this.money += amount;
    }
}
