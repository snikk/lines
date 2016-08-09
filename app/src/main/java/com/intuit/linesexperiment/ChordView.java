package com.intuit.linesexperiment;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jglenn2 on 3/18/16.
 */
public class ChordView extends View {
    public static final double RADIANS = Math.PI / 180.0;

    public int max = 100;
    public double mul = 2;

    public Paint linePaint;

    public ChordView(Context context) {
        super(context);
    }

    public ChordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        linePaint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.translate(getWidth() / 2.0f, getHeight() / 2.0f);

        double inc = (Math.PI * 2) / (double) max;
        int size = (getWidth() < getHeight() ? getWidth() : getHeight()) / 2;

        for (int i = 0; i < max; i++) {
            linePaint.setColor(0xFFFF0000 + (0xFF * i / max));
            canvas.drawLine((float) Math.sin(inc * i) * size, (float) Math.cos(inc * i) * size, (float) Math.sin(inc * i * mul) * size, (float) Math.cos(inc * i * mul) * size, linePaint);
        }
    }
}
