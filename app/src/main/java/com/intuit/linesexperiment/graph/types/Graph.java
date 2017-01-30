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

    public float anim = 0.0f;
    public float animSpeed = 0.005f;

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

    public PointF midPoint(PointF p1, PointF p2) {
        return new PointF((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
    }

    public PointF normalize(PointF p) {
        float length = length(p);
        return new PointF(p.x / length, p.y / length);
    }

    public void rotateRight(PointF p) {
        float tmp = p.x;
        p.x = -p.y;
        p.y = tmp;
    }

    public void rotateLeft(PointF p) {
        float tmp = p.x;
        p.x = p.y;
        p.y = -tmp;
    }

    public float length(PointF p) {
        return (float) Math.sqrt(lengthSquared(p));
    }

    public PointF sub(PointF from, PointF amount) {
        return new PointF(from.x - amount.x, from.y - amount.y);
    }

    public float lengthSquared(PointF p) {
        return p.x * p.x + p.y * p.y;
    }

    public void drawAxis(Canvas canvas, PointF origin, PointF point1, PointF point2) {
//        PointF mid1 = midPoint(point1, origin);
//        PointF mid2 = midPoint(point2, origin);
//
//        PointF diff1 = sub(point1, origin);
//        PointF diff2 = sub(point2, origin);
//
//        PointF norm1 = normalize(diff1);
//        PointF norm2 = normalize(diff2);
//
//        rotateRight(norm1);
//        rotateLeft(norm2);
//
//        float angle1 = angle(diff1);
//        float angle2 = angle(diff2);
//        if (angle2 < angle1)
//            angle2 += Math.PI * 2;
//        float angle = angle2 - angle1;
//
//        float length1 = length(diff1) * (angle / 5.0f);
//        float length2 = length(diff2) * (angle / 5.0f);
//        float linePaintSize = linePaint.getStrokeWidth();
//        linePaint.setColor(0xFFFFFF00)
//        linePaint.setStrokeWidth(7.0f);
//        canvas.drawLine(mid1.x, mid1.y, mid1.x + norm1.x * length1, mid1.y + norm1.y * length1, linePaint);
//        linePaint.setColor(0xFF00FFFF);
//        canvas.drawLine(mid2.x, mid2.y, mid2.x + norm2.x * length2, mid2.y + norm2.y * length2, linePaint);
//
//        linePaint.setStrokeWidth(linePaintSize);


        anim += animSpeed;
        anim = anim % 1.0f;
        if (anim < 0.0f)
            anim += 1.0f;

        for (int i = 0; i < divisions; i++) {
            float per = (((float) i) + anim) / (float) divisions;
            linePaint.setColor(0xFF0000FF + ((int) (0xFF * per) << 16));
            PointF p1 = lerp(point1, origin, per);
            PointF p2 = lerp(point2, origin, 1 - per);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, linePaint);
        }

        linePaint.setColor(0xFFFF00FF);
        canvas.drawLine(origin.x, origin.y, point1.x, point1.y, linePaint);
        linePaint.setColor(0xFF0000FF);
        canvas.drawLine(origin.x, origin.y, point2.x, point2.y, linePaint);
    }

    public void curveTo(PointF p1, PointF offset1, PointF p2, PointF offset2) {

    }

    private float angle(PointF p) {
        return (float) Math.atan2(p.y, p.x);
    }

    public void setThickness(float v) {
        linePaint.setStrokeWidth(v);
    }

    public float getThickness() {
        return linePaint.getStrokeWidth();
    }

    public void setAnimationSpeed(float progress) {
        animSpeed = 0.02f * progress;
    }

    public void setDivisions(float progress) {
        divisions = (int) (100.0f * progress);
    }
}
