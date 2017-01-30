package com.intuit.linesexperiment.graph;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.intuit.linesexperiment.graph.types.AlternatingGraph;
import com.intuit.linesexperiment.graph.types.Graph;
import com.intuit.linesexperiment.graph.types.RadialGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jglenn2 on 8/7/16.
 */
public class GraphView extends View {
    private static final int HITBOX = 100;

    public int touchIdx;
    public PointF touchOffset = new PointF();

    private Graph[] graphs = new Graph[] {new RadialGraph(), new AlternatingGraph()};
    private int graphIdx = 0;

    public GraphView(Context context) {
        super(context);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GraphView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        graphs[graphIdx].setDimensions(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        graphs[graphIdx].draw(canvas);

        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Graph graph;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                graph = graphs[graphIdx];
                for (int i = 0; i < graph.axisList.size(); i++) {
                    PointF ax = graph.axisList.get(i);
                    RectF hitbox = new RectF(ax.x - HITBOX, ax.y - HITBOX, ax.x + HITBOX, ax.y + HITBOX);
                    if (hitbox.contains(event.getX(), event.getY())) {
                        touchIdx = i;
                        touchOffset.x = event.getX() - ax.x;
                        touchOffset.y = event.getY() - ax.y;
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchIdx >= 0) {
                    graph = graphs[graphIdx];
                    PointF ax = graph.axisList.get(touchIdx);
                    ax.x = event.getX() - touchOffset.x;
                    ax.y = event.getY() - touchOffset.y;
                    graph.axisList.set(touchIdx, ax);

                    invalidate();

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                touchIdx = -1;
                break;
        }

        return super.onTouchEvent(event);
    }

    public void cycle() {
        graphIdx = ++graphIdx % graphs.length;
        graphs[graphIdx].setDimensions(getWidth(), getHeight());
        invalidate();
    }

    public int getGraphIdx() {
        return graphIdx;
    }

    public Bundle getArgs() {
        Bundle b = new Bundle();

        b.putInt(EditDialog.EXTRA_DIVISIONS, graphs[graphIdx].divisions);
        b.putFloat(EditDialog.EXTRA_THICKNESS, graphs[graphIdx].getThickness() / 10.0f);

        return b;
    }

    public void setValues(int divisions, float percentage) {
        for (Graph graph : graphs) {
            graph.divisions = divisions;
            graph.setThickness(percentage * 10.0f);
        }

        invalidate();
    }

    public void setThickness(float percentage) {
        for (Graph graph : graphs) {
            graph.setThickness(10.0f * percentage);
        }
    }

    public void setAnimationSpeed(float progress) {
        for (Graph graph : graphs) {
            graph.setAnimationSpeed(progress);
        }
    }

    public void setDivisions(float progress) {
        for (Graph graph : graphs) {
            graph.setDivisions(progress);
        }
    }
}
