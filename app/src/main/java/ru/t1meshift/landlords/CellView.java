package ru.t1meshift.landlords;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private Paint p;
    private boolean marked;

    public CellView(Context context, LandCell cell, int x, int y) {
        super(context);
        this.cell = cell;
        this.x = x;
        this.y = y;
        this.setPadding(1,1,1,1);
        this.setClickable(true);
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = dimensionInDp;
        params.width = dimensionInDp;
        params.columnSpec = GridLayout.spec(x);
        params.rowSpec = GridLayout.spec(y);
        this.setLayoutParams(params);
        //this.requestLayout();
        p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(2);
        this.marked = false;
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
    public void mark(){ marked = true; }
    public void unmark(){ marked = false; }
    public void update() {
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
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.marked)
            p.setColor(Color.RED);
        else
            p.setColor(Color.BLACK);
        canvas.drawRect(1, 1, getWidth() - 1, getHeight() - 1, p);
    }
}
