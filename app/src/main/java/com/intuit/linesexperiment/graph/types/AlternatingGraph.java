package com.intuit.linesexperiment.graph.types;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by jglenn2 on 8/9/16.
 */
public class AlternatingGraph extends Graph {
    @Override
    public void draw(Canvas c) {
        for (int i = 0; i < axisList.size() - 2; i++) {
            drawAxis(c, axisList.get(i + 1), axisList.get(i), axisList.get(i + 2));
        }
    }

    @Override
    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);

        axisList.clear();

        axisList.add(new PointF(0.0f, 0.0f));
        axisList.add(new PointF((float) width * 0.3f, 0.0f));
        axisList.add(new PointF(0.0f, (float) height * 0.3f));
        axisList.add(new PointF((float) width * 0.6f, 0.0f));
        axisList.add(new PointF(0.0f, (float) height * 0.6f));
        axisList.add(new PointF((float) width, 0.0f));
        axisList.add(new PointF(0.0f, (float) height));
        axisList.add(new PointF((float) width, (float) height * 0.3f));
        axisList.add(new PointF((float) width * 0.3f, (float) height));
        axisList.add(new PointF((float) width, (float) height * 0.6f));
        axisList.add(new PointF((float) width * 0.6f, (float) height));
        axisList.add(new PointF((float) width, (float) height));
        axisList.add(new PointF((float) width, (float) height));
    }
}
