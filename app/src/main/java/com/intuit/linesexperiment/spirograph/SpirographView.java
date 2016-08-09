package com.intuit.linesexperiment.spirograph;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jglenn2 on 3/24/16.
 */
public class SpirographView extends View {
    Paint baseCircleLine;
    Paint baseCircleFill;
    Paint innerCircleLine;
    Paint innerCircleFill;
    Paint lines;
    Paint dotFill;

    float baseRadius;
    int baseTeeth;
    float innerRadius;
    int innerTeeth;
    float dot;
    float dotPer;

    float currentPosition = 0.0f;
    float previousPosition = 0.0f;

    float toothDepth;
    float toothWidth;

    Path path;

    List<PointF> points = new ArrayList<>();

    private Path innerGearPath = new Path();
    private Path baseGearPath = new Path();

    public SpirographView(Context context) {
        super(context);
    }

    public SpirographView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpirographView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpirographView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setValues(100, 7, 0.8f);

        path = new Path();

        baseCircleLine = new Paint();
        baseCircleLine.setColor(0xFF00FF00);
        baseCircleLine.setStyle(Paint.Style.STROKE);
//        baseCircleLine.setStrokeWidth(20.0f);
        baseCircleFill = new Paint();
        baseCircleFill.setColor(0x8800FF00);
        baseCircleFill.setStyle(Paint.Style.FILL);

        innerCircleLine = new Paint();
        innerCircleLine.setColor(0xFFFF0000);
        innerCircleLine.setStyle(Paint.Style.STROKE);
//        innerCircleLine.setStrokeWidth(20.0f);
        innerCircleFill = new Paint();
        innerCircleFill.setColor(0x88FF0000);
        innerCircleFill.setStyle(Paint.Style.FILL);

        lines = new Paint();
        lines.setColor(0xFFFFFF00);
        lines.setStyle(Paint.Style.STROKE);

        dotFill = new Paint();
        dotFill.setColor(0xFF000000);
        dotFill.setStyle(Paint.Style.FILL);
    }

    public void setValues(int outer, int inner, float percentage) {
        baseRadius = 700.0f;
        baseTeeth = outer;
        innerTeeth = inner;

        toothDepth = 0.05f * baseRadius;
        toothWidth = (float) (Math.PI * 2 / baseTeeth) * baseRadius;
        innerRadius = (toothWidth * innerTeeth) / (float) (Math.PI * 2);

        dotPer = percentage;
        dot = innerRadius * dotPer;

        drawGear(baseGearPath, baseRadius);
        drawGear(innerGearPath, innerRadius);

        points.clear();
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        int min = (getWidth() < getHeight() ? getWidth() : getHeight()) / 2;

        canvas.drawColor(0xFF0000FF);

        canvas.save();
        canvas.translate(getWidth() / 2.0f, getHeight() / 2.0f);
        canvas.scale(min / (baseRadius * 1.1f), min / (baseRadius * 1.1f));

        for (int i = 1; i < points.size(); i++) {
            PointF prev = points.get(i - 1);
            PointF curr = points.get(i);

            canvas.drawLine(prev.x, prev.y, curr.x, curr.y, lines);
        }

//        canvas.drawCircle(0.0f, 0.0f, baseRadius, baseCircleFill);
//        canvas.drawCircle(0.0f, 0.0f, baseRadius, baseCircleLine);
        canvas.drawPath(baseGearPath, baseCircleFill);
        canvas.drawPath(baseGearPath, baseCircleLine);
        canvas.drawCircle(0.0f, 0.0f, baseRadius, baseCircleLine);

        canvas.drawLine(0.0f, 0.0f, baseRadius, 0.0f, baseCircleLine);
        canvas.save();
        float diff = baseRadius - innerRadius;
        canvas.translate((float) Math.cos(currentPosition) * diff, (float) Math.sin(currentPosition) * diff);
        canvas.rotate((-((currentPosition * baseRadius) / innerRadius) + currentPosition) / (float) (Math.PI / 180.0));
//        canvas.drawCircle(0.0f, 0.0f, innerRadius, innerCircleFill);
//        canvas.drawCircle(0.0f, 0.0f, innerRadius, innerCircleLine);
        canvas.drawPath(innerGearPath, innerCircleFill);
        canvas.drawPath(innerGearPath, innerCircleLine);
        canvas.drawCircle(0.0f, 0.0f, innerRadius, innerCircleLine);
        canvas.drawLine(0.0f, 0.0f, innerRadius, 0.0f, innerCircleLine);

        canvas.drawCircle(dot, 0.0f, 0.05f, dotFill);

        canvas.restore();

        canvas.restore();
    }

    public void drawGear(Path p, float radius) {
        p.rewind();
        p.moveTo(radius, 0.0f);
        float tooth = toothWidth / radius;
        for (float pos = 0.0f; pos < Math.PI * 2; pos += tooth) {
            p.lineTo((float) Math.cos(pos) * (radius - toothDepth), (float) Math.sin(pos) * (radius - toothDepth));
            p.lineTo((float) Math.cos(pos) * (radius + toothDepth), (float) Math.sin(pos) * (radius + toothDepth));
            p.lineTo((float) Math.cos(pos + (tooth / 2.0f)) * (radius + toothDepth), (float) Math.sin(pos + (tooth / 2.0f)) * (radius + toothDepth));
            p.lineTo((float) Math.cos(pos + (tooth / 2.0f)) * (radius - toothDepth), (float) Math.sin(pos + (tooth / 2.0f)) * (radius - toothDepth));
        }
        p.close();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF p;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previousPosition = (float) Math.atan2(event.getY() - ((float) getHeight() / 2.0f), event.getX() - ((float) getWidth() / 2.0f));

                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("Intuit", "event.getHistorySize() = " + event.getHistorySize());
                for (int i = 0; i < event.getHistorySize(); i++) {
                    float position = (float) Math.atan2(event.getHistoricalY(i) - ((float) getHeight() / 2.0f), event.getHistoricalX(i) - ((float) getWidth() / 2.0f));
                    float diff = previousPosition - position;
                    if (diff > Math.PI) {
                        diff -= 2 * Math.PI;
                    } else if (diff < -Math.PI) {
                        diff += 2 * Math.PI;
                    }

                    previousPosition = position;
                    currentPosition -= diff;

                    points.add(getPosition(currentPosition));
                }

                break;
        }
        postInvalidate();
        Log.d("Test", "currentPosition = " + currentPosition);
        return true;
    }

    public PointF getPosition(float rotation) {
        PointF offset = new PointF();

        float angle = -((rotation * baseRadius) / innerRadius) + rotation;
        offset.x = (float) Math.cos(angle) * dot;
        offset.y = (float) Math.sin(angle) * dot;

        PointF p = new PointF();

        float diff = baseRadius - innerRadius;
        p.x = (float) Math.cos(rotation) * diff + offset.x;
        p.y = (float) Math.sin(rotation) * diff + offset.y;

        return p;
    }

    public Bundle getArgs() {
        Bundle b = new Bundle();

        b.putInt(EditDialog.EXTRA_BASE, baseTeeth);
        b.putInt(EditDialog.EXTRA_INNER, innerTeeth);
        b.putFloat(EditDialog.EXTRA_PERCENTAGE, dotPer);

        return b;
    }
}
