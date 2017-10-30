package ru.t1meshift.landlords;

import android.app.Application;

public class App extends Application {

    private int playerFlag = 1; //0 - no one, 1-4 - R,G,B,Y
    private CellView[] cells = new CellView[100];
    private Player player = new Player(playerFlag);
    private Player[] enemies = new Player[3];
    private int currentCellX = 0, currentCellY = 0;
    private int moves = 1;

    public int getPlayerFlag() {
        return playerFlag;
    }

    public void setPlayerFlag(int playerFlag) {
        this.playerFlag = playerFlag;
    }

    public CellView[] getCells() {
        return cells;
    }

    public void setCells(CellView[] cells) {
        this.cells = cells;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player[] getEnemies() {
        return enemies;
    }

    public void setEnemies(Player[] enemies) {
        this.enemies = enemies;
    }

    public int getCurrentCellX() {
        return currentCellX;
    }

    public void setCurrentCellX(int currentCellX) {
        this.currentCellX = currentCellX;
    }

    public int getCurrentCellY() {
        return currentCellY;
    }

    public void setCurrentCellY(int currentCellY) {
        this.currentCellY = currentCellY;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Creating bots...
        int tempFlag = 1;
        for (int i=0;i<4;i++) {
            if (tempFlag>4) break;
            if (tempFlag != playerFlag)
                enemies[i] = new Player(tempFlag);
            else if(tempFlag<4)
                enemies[i] = new Player(++tempFlag);
            tempFlag++;
        }

        for (int y=0;y<10;y++) {
            for (int x=0;x<10;x++) {
                CellView cell = new CellView(this, new LandCell((int)Math.ceil(Math.random()*10),0),x,y);
                cells[y*10+x] = cell;
            }
        }
    }
}
