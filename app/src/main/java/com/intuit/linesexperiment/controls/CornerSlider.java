package com.intuit.linesexperiment.controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.intuit.linesexperiment.R;

/**
 * Created by jglenn2 on 8/10/16.
 */
public class CornerSlider extends View {
    public static final int UPPER_LEFT = 0;
    public static final int UPPER_RIGHT = 1;
    public static final int LOWER_LEFT = 2;
    public static final int LOWER_RIGHT = 3;

    public static final int TYPE_SPRING = 0;
    public static final int TYPE_SLIDER = 1;

    public int anchor;
    public int type;
    public PointF center = new PointF();
    public Paint linePaint;
    public Paint controlPaint;
    public Paint accessPaint;
    private float diam = 80;
    private float offset = 0.0f;
    private float oldPos = 0.0f;
    public float position = 0.0f;
    private Listener listener;

    public boolean shown = false;

    public RectF hitbox = new RectF();
    private boolean dragging = false;
    private float overallRotation = 0.0f;
    private float oldOverallRotation = 0.0f;

//    public CornerSlider(Context context) {
//        super(context);
//    }

    public CornerSlider(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CornerSlider, 0, 0);

        anchor = a.getInt(R.styleable.CornerSlider_anchor_position, UPPER_LEFT);
        type = a.getInt(R.styleable.CornerSlider_type, TYPE_SPRING);

        a.recycle();
    }

//    public CornerSlider(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public CornerSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        linePaint = new Paint();
        linePaint.setColor(0xFFFF0000);

        controlPaint = new Paint();
        controlPaint.setColor(0xFF0000FF);
        controlPaint.setStrokeWidth(diam * 0.7f);

        accessPaint = new Paint();
        accessPaint.setColor(0xFF00FF00);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        switch (anchor) {
            case UPPER_LEFT:
                hitbox.left = -diam;
                hitbox.right = diam;
                hitbox.top = (getMeasuredHeight() + 50) - diam;
                hitbox.bottom = (getMeasuredHeight() + 50) + diam;

                center.x = 0.0f;
                center.y = 0.0f;
                break;
            case UPPER_RIGHT:
                hitbox.left = getMeasuredWidth() - diam;
                hitbox.right = getMeasuredWidth() + diam;
                hitbox.top = (getMeasuredHeight() + 50) - diam;
                hitbox.bottom = (getMeasuredHeight() + 50) + diam;

                center.x = getMeasuredWidth();
                center.y = 0.0f;
                break;
            case LOWER_LEFT:
                hitbox.left = -diam;
                hitbox.right = diam;
                hitbox.top = -diam;
                hitbox.bottom = diam;

                center.x = 0.0f;
                center.y = getMeasuredHeight();
                break;
            case LOWER_RIGHT:
                hitbox.left = getMeasuredWidth() - diam;
                hitbox.right = getMeasuredWidth() + diam;
                hitbox.top = -diam;
                hitbox.bottom = diam;

                center.x = getMeasuredWidth();
                center.y = getMeasuredHeight();
                break;
        }

        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(0xFFFFFF00);

        canvas.save();
        canvas.translate(center.x, center.y);

        canvas.drawCircle(0.0f, 0.0f, getWidth() - diam, linePaint);

        switch (anchor) {
            case UPPER_LEFT:
                canvas.rotate(-overallRotation);
                canvas.drawCircle(0.0f, getHeight() - diam, diam, accessPaint);
                canvas.save();
                canvas.rotate(0);
                break;
            case UPPER_RIGHT:
                canvas.rotate(overallRotation);
                canvas.drawCircle(0.0f, getHeight() - diam, diam, accessPaint);
                canvas.save();
                canvas.rotate(90);
                break;
            case LOWER_LEFT:
                canvas.rotate(overallRotation);
                canvas.drawCircle(0.0f, -(getHeight() - diam), diam, accessPaint);
                canvas.save();
                canvas.rotate(-90);
                break;
            case LOWER_RIGHT:
                canvas.rotate(-overallRotation);
                canvas.drawCircle(0.0f, -(getHeight() - diam), diam, accessPaint);
                canvas.save();
                canvas.rotate(-180);
                break;
        }

        canvas.rotate(type == TYPE_SPRING ? 45.0f : 0.0f);
        canvas.rotate(position);
        canvas.drawLine(0.0f, 0.0f, getWidth() - diam, 0.0f, controlPaint);
        canvas.drawCircle(getWidth() - diam, 0.0f, diam, controlPaint);
        canvas.restore();

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float diffX, diffY, newAngle;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                diffX = event.getX() - center.x;
                diffY = event.getY() - center.y;
                offset = (float) Math.atan2(diffY, diffX);

                if (hitbox.contains(event.getX(), event.getY())) {
                    dragging = true;
                    oldOverallRotation = overallRotation;
                } else {
                    oldPos = position;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (dragging) {
                    diffX = event.getX() - center.x;
                    diffY = event.getY() - center.y;
                    newAngle = (float) Math.atan2(diffY, diffX) - offset;
                    overallRotation = oldOverallRotation + newAngle / ((float) Math.PI / 180.0f);
                } else {
                    diffX = event.getX() - center.x;
                    diffY = event.getY() - center.y;
                    newAngle = (float) Math.atan2(diffY, diffX) - offset;
                    position = oldPos + newAngle / ((float) Math.PI / 180.0f);
                    switch (type) {
                        case TYPE_SPRING:
                            if (position < -45.0f)
                                position = -45.0f;
                            else if (position > 45.0f)
                                position = 45.0f;
                            break;
                        case TYPE_SLIDER:
                            if (position < 0.0f)
                                position = 0.0f;
                            else if (position > 90.0f)
                                position = 90.0f;
                            break;
                    }
                    if (listener != null)
                        listener.onUpdate(this, getProgress());
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                dragging = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    public float getProgress() {
        switch (type) {
            case TYPE_SPRING:
                return position / 45.0f;
            case TYPE_SLIDER:
                return position / 90.0f;
        }

        return 0.0f;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onUpdate(CornerSlider slider, float progress);
    }
}
