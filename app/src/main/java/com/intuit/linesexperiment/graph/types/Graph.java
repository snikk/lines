package com.intuit.linesexperiment.graph.types;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jglenn2 on 8/8/16.
 */
public abstract class Graph {
    public int width;
    public int height;

    public int divisions = 50;

    public List<PointF> axisList = new ArrayList<>();

    public float normalWeight = 0.5f;

    Paint linePaint = new Paint();

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    abstract public void draw(Canvas c);

    public PointF lerp(PointF p1, PointF p2, float per) {
        return new PointF(
                (p2.x - p1.x) * per + p1.x,
                (p2.y - p1.y) * per + p1.y
        );
    }

    public void drawAxis(Canvas canvas, PointF origin, PointF point1, PointF point2) {
        for (int i = 0; i < divisions; i++) {
            float per = (float) i / (float) divisions;
            linePaint.setColor(0xFF0000FF + ((int) (0xFF * per) << 16));
            PointF p1 = lerp(point1, origin, per);
            PointF p2 = lerp(point2, origin, 1 - per);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, linePaint);
        }
    }

    public void setThickness(float v) {
        linePaint.setStrokeWidth(v);
    }

    public float getThickness() {
        return linePaint.getStrokeWidth();
    }
}
