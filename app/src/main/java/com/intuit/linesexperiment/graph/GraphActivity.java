package com.intuit.linesexperiment.graph;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.intuit.linesexperiment.R;
import com.intuit.linesexperiment.controls.CornerSlider;
import com.intuit.linesexperiment.util.ConvertUtil;

/**
 * Created by jglenn2 on 8/7/16.
 */
public class GraphActivity extends Activity implements EditDialog.ChangeListener {
    private GraphView gv;
    private Button cycleButton;

    private CornerSlider cornerUL;
    private CornerSlider cornerUR;
    private CornerSlider cornerLL;
    private CornerSlider cornerLR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        gv = (GraphView) findViewById(R.id.view);

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvertUtil.saveBitmap(GraphActivity.this, gv);
            }
        });

        cycleButton = (Button) findViewById(R.id.cycleButton);
        cycleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv.cycle();
                cycleButton.setText("Cycle " + gv.getGraphIdx());
            }
        });

        findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog dialog = new EditDialog();
                dialog.setArguments(gv.getArgs());
                dialog.show(getFragmentManager(), "edit_dialog");
            }
        });

        cornerUL = (CornerSlider) findViewById(R.id.cornerUL);
        cornerUR = (CornerSlider) findViewById(R.id.cornerUR);
        cornerLL = (CornerSlider) findViewById(R.id.cornerLL);
        cornerLR = (CornerSlider) findViewById(R.id.cornerLR);

        CornerSlider.Listener listener = new CornerSlider.Listener() {
            @Override
            public void onUpdate(CornerSlider slider, float progress) {
                Log.d("Graph", "slider(" + slider.getId() + ") = " + progress);
            }
        };

        cornerUL.setListener(listener);
        cornerUR.setListener(new CornerSlider.Listener() {
            @Override
            public void onUpdate(CornerSlider slider, float progress) {
                gv.setDivisions(progress);
            }
        });
        cornerLL.setListener(new CornerSlider.Listener() {
            @Override
            public void onUpdate(CornerSlider slider, float progress) {
                gv.setAnimationSpeed(progress);
            }
        });
        cornerLR.setListener(new CornerSlider.Listener() {
            @Override
            public void onUpdate(CornerSlider slider, float progress) {
                gv.setThickness(progress);
            }
        });
    }

    @Override
    public void onChange(EditDialog dialog) {
        gv.setValues(dialog.getDivisions(), dialog.getPercentage());
    }
}
