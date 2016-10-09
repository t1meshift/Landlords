package ru.t1meshift.landlords;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import ru.t1meshift.landlords.LandCell;

/**
 * Created by t1meshft on 09.10.2016.
 */
public class CellView extends ImageView {
    private int x, y;
    private LandCell cell;
    public CellView(Context context, LandCell cell, int x, int y) {
        super(context);
        this.cell = cell;
        this.x = x;
        this.y = y;
        this.setClickable(true);
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = dimensionInDp;
        params.width = dimensionInDp;
        params.columnSpec = GridLayout.spec(x);
        params.rowSpec = GridLayout.spec(y);
        this.setLayoutParams(params);
        //this.requestLayout();
        this.update();
    }
    public int getCellX() {
        return this.x;
    }
    public int getCellY() {
        return this.y;
    }
    public LandCell getCell() {
        return this.cell;
    }
    public void update() {
        if (this.cell.getOwner() == 0) {
            this.setBackgroundColor(0xffcccccc);
        } else {
            this.setBackgroundColor(Color.rgb(73,255,134));
            switch (this.cell.getOwner()) {
                case 1:
                    this.setImageResource(R.drawable.flag_red);
                    break;
                case 2:
                    this.setImageResource(R.drawable.flag_green);
                    break;
                case 3:
                    this.setImageResource(R.drawable.flag_blue);
                    break;
                case 4:
                    this.setImageResource(R.drawable.flag_yellow);
                    break;
                default:
                    this.setBackgroundColor(0xffcccccc);
                    break;
            }
        }
    }
}
