package ru.t1meshift.landlords;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends Activity {
    //private final String INSTANCE_KEY = "Landlords";
    private GridLayout field;
    private TextView cellInfo;
    private TextView cellIncome;
    private TextView cellArmor;
    private Button buyButton;
    private Button enforceButton;
    private Button skipButton;
    private TextView moneyCount;
    private TextView movesCount;
    private ImageView playerFlagImg;
    private App app;
    private enum Move {
        enforce, buy, skip
    }

    private int playerFlag;
    private CellView[] cells;
    private Player player;
    private Player[] enemies;
    private int currentCellX = 0, currentCellY = 0;
    private int moves;
    private int enforcePrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //getSupportActionBar().hide();
            getActionBar().hide();
        }

        field = (GridLayout) findViewById(R.id.gridLayout);
        cellInfo = (TextView) findViewById(R.id.cell_xy);
        cellIncome = (TextView) findViewById(R.id.income);
        cellArmor = (TextView) findViewById(R.id.armor);
        buyButton = (Button) findViewById(R.id.buy_btn);
        enforceButton = (Button) findViewById(R.id.enforce_btn);
        skipButton = (Button) findViewById(R.id.skip_btn);
        moneyCount = (TextView) findViewById(R.id.total_income);
        movesCount = (TextView) findViewById(R.id.move_count);
        playerFlagImg = (ImageView) findViewById(R.id.player_flag);

        app = (App) getApplicationContext();

        playerFlag = app.getPlayerFlag();
        cells = app.getCells();
        player = app.getPlayer();
        enemies = app.getEnemies();
        currentCellX = app.getCurrentCellX();
        currentCellY = app.getCurrentCellY();
        moves = app.getMoves();


        switch (app.getPlayerFlag()) {
            case 1:
                playerFlagImg.setImageResource(R.drawable.flag_red);
                break;
            case 2:
                playerFlagImg.setImageResource(R.drawable.flag_green);
                break;
            case 3:
                playerFlagImg.setImageResource(R.drawable.flag_blue);
                break;
            case 4:
                playerFlagImg.setImageResource(R.drawable.flag_yellow);
                break;
        }

        showCells();

        cells[currentCellY*10+currentCellX].mark();
        cells[currentCellY*10+currentCellX].invalidate();
        showCellInfo(cells[currentCellY*10+currentCellX]); //first cell
        buyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                move(Move.buy);
            }
        });
        enforceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                move(Move.enforce);
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                move(Move.skip);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.setCells(cells);
        app.setPlayer(player);
        app.setEnemies(enemies);
        app.setCurrentCellX(currentCellX);
        app.setCurrentCellY(currentCellY);
        app.setMoves(moves);

        cells[currentCellY*10+currentCellX].unmark();

        field.removeAllViews();
    }


    private void showCellInfo(CellView clickedCell) {
        LandCell clickedCellInfo = clickedCell.getCell();
        moneyCount.setText(player.getMoney() + " (+"+ player.getTotalIncome(player.getOwnCells(cells))+")");
        movesCount.setText("Move "+moves);
        enforcePrice = player.getOwnCells(cells).size() + clickedCellInfo.getIncome() + clickedCellInfo.getArmor();

        currentCellX = clickedCell.getCellX();
        currentCellY = clickedCell.getCellY();

        cellInfo.setText("Cell ("+(clickedCell.getCellX()+1)+";"+(clickedCell.getCellY()+1)+"); belongs to "+getTeamName(clickedCellInfo.getOwner()));
        cellIncome.setText("+"+clickedCellInfo.getIncome());
        cellArmor.setText(""+clickedCellInfo.getArmor());

        if (clickedCellInfo.getOwner() == 0) {
            buyButton.setText("Capture");
            buyButton.setEnabled(true);
            enforceButton.setText("Enforce");
            enforceButton.setEnabled(false);
        } else if (clickedCellInfo.getOwner() == playerFlag) {
            buyButton.setText("Buy (" + clickedCellInfo.getPrice() + ")");
            buyButton.setEnabled(false);
            enforceButton.setText("Enforce (" + enforcePrice + ")");
            enforceButton.setEnabled(true);
        } else {
            buyButton.setText("Buy (" + clickedCellInfo.getPrice() + ")");
            buyButton.setEnabled(true);
            enforceButton.setText("Enforce");
            enforceButton.setEnabled(false);
        }
    }

    private void move(Move m) {
        switch (m) {
            case buy:
                if (cells[currentCellY*10+currentCellX].getCell().getPrice() > player.getMoney() && cells[currentCellY*10+currentCellX].getCell().getOwner() != 0) {
                    Toast.makeText(this, "You can't afford it!", Toast.LENGTH_SHORT).show();
                    return;
                }
                player.earn(player.getTotalIncome(player.getOwnCells(cells)));
                if (cells[currentCellY*10+currentCellX].getCell().getOwner() != 0)
                    player.spend(cells[currentCellY*10+currentCellX].getCell().getPrice());
                //then set owner
                cells[currentCellY * 10 + currentCellX].getCell().setOwner(playerFlag);
                break;
            case enforce:
                if (enforcePrice > player.getMoney()) {
                    Toast.makeText(this, "You can't afford it!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    player.earn(player.getTotalIncome(player.getOwnCells(cells)));
                    player.spend(enforcePrice);
                    //Toast.makeText(this, "enforcePrice = "+enforcePrice, Toast.LENGTH_SHORT).show();
                    cells[currentCellY*10+currentCellX].getCell().enforce();
                }
                break;
            case skip:
                player.earn(player.getTotalIncome(player.getOwnCells(cells)));
                break;
        }
        moves++;
        for (Player enemy : enemies) {
            CellView preferredCell;
            ArrayList<CellView> freeCells = new ArrayList<>();
            for (int i=0; i < cells.length; i++) {
                if (cells[i].getCell().getOwner() == 0)
                    freeCells.add(cells[i]);
            }
            if (freeCells.size() != 0) {
                enemy.earn(enemy.getTotalIncome(enemy.getOwnCells(cells)));
                preferredCell = freeCells.get((int) Math.floor(Math.random() * freeCells.size()));
                preferredCell.getCell().setOwner(enemy.getFlag());
            } else {
                ArrayList<CellView> ownCells = enemy.getOwnCells(cells);
                ArrayList<CellView> notOwnCells = new ArrayList<>();
                for (CellView currCell : cells) {
                    if (currCell.getCell().getOwner() != enemy.getFlag())
                        notOwnCells.add(currCell);
                }
                preferredCell = notOwnCells.get(0);
                byte enforce = (byte) Math.floor(Math.random()*5); // 0.25 chance
                for (CellView cell : notOwnCells) {
                    //int enforcePrice = ownCells.size() + cell.getCell().getIncome() + cell.getCell().getArmor();
                    if (cell.getCell().getPrice() < preferredCell.getCell().getPrice())
                        preferredCell = cell;
                }
                if (preferredCell.getCell().getPrice() <= enemy.getMoney() || enforce == 4)
                    if (enforce != 4 || ownCells.size()==0) {
                        enemy.earn(enemy.getTotalIncome(enemy.getOwnCells(cells)));
                        enemy.spend(preferredCell.getCell().getPrice());
                        preferredCell.getCell().setOwner(enemy.getFlag());
                    } else {
                        preferredCell = ownCells.get(0); //BUG их может и не быть
                        for (CellView cell : ownCells) {
                            int enforcePrice = ownCells.size() + cell.getCell().getIncome() + cell.getCell().getArmor();
                            int preferredEnforcePrice = ownCells.size() + preferredCell.getCell().getIncome() + preferredCell.getCell().getArmor();
                            if (enforcePrice < preferredEnforcePrice)
                                preferredCell = cell;
                        }
                        if (ownCells.size() + preferredCell.getCell().getIncome() + preferredCell.getCell().getArmor() <= enemy.getMoney())
                        {
                            enemy.earn(enemy.getTotalIncome(enemy.getOwnCells(cells)));
                            enemy.spend(ownCells.size() + preferredCell.getCell().getIncome() + preferredCell.getCell().getArmor());
                            preferredCell.getCell().enforce();
                        } else {
                            //Can't afford anything lol
                            enemy.earn(enemy.getTotalIncome(enemy.getOwnCells(cells)));
                        }
                    }
            }
            preferredCell.update();
            cells[currentCellY*10+currentCellX].update();
            showCellInfo(cells[currentCellY*10+currentCellX]);
        }
        /*
        TODO:
        - enemy movement (ready?)
        - win/lose algo
         */
    }
    private String getTeamName(int flag) {
        if (flag == playerFlag)
            return "you";
        switch (flag) {
            case 0:
                return "no one";
            case 1:
                return "Red";
            case 2:
                return "Green";
            case 3:
                return "Blue";
            case 4:
                return "Yellow";
            default:
                return "Unknown";
        }
    }
    private void showCells() {
        for (int y=0;y<10;y++) {
            for (int x=0;x<10;x++) {
                CellView cell = cells[y*10+x];
                cell.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //v.getId() will give you the image id
                        CellView clickedCell = (CellView) v;
                        cells[currentCellY*10+currentCellX].unmark();
                        cells[currentCellY*10+currentCellX].invalidate();
                        clickedCell.mark();
                        clickedCell.invalidate();
                        showCellInfo(clickedCell);
                    }
                });
                field.addView(cell);
            }
        }
    }
}
