package com.intuit.linesexperiment.graph.types;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by jglenn2 on 8/8/16.
 */
public class RadialGraph extends Graph {

    public boolean close = true;

    @Override
    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);

        axisList.clear();

        PointF origin = new PointF(width / 2.0f, height / 2.0f);
        axisList.add(origin);

        axisList.add(new PointF(origin.x + width / 2.0f, origin.y));
        axisList.add(new PointF(origin.x, origin.y + height / 2.0f));
        axisList.add(new PointF(origin.x - width / 2.0f, origin.y));
        axisList.add(new PointF(origin.x, origin.y - height / 2.0f));
    }

    @Override
    public void draw(Canvas c) {
        PointF origin = axisList.get(0);
        for (int ax = 2; ax < axisList.size() + (close ? 1 : 0); ax++) {
            PointF axis1 = axisList.get(ax - 1);
            PointF axis2 = axisList.get(ax < axisList.size() ? ax : 1);

            drawAxis(c, origin, axis1, axis2);
        }
    }
}
