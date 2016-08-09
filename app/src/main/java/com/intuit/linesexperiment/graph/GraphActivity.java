package com.intuit.linesexperiment.graph;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.intuit.linesexperiment.R;
import com.intuit.linesexperiment.util.ConvertUtil;

/**
 * Created by jglenn2 on 8/7/16.
 */
public class GraphActivity extends Activity implements EditDialog.ChangeListener {
    private GraphView gv;
    private Button cycleButton;

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
                com.intuit.linesexperiment.spirograph.EditDialog dialog = new com.intuit.linesexperiment.spirograph.EditDialog();
                dialog.setArguments(gv.getArgs());
                dialog.show(getFragmentManager(), "edit_dialog");
            }
        });
    }

    @Override
    public void onChange(EditDialog dialog) {
        gv.setValues(dialog.getDivisions(), dialog.getPercentage());
    }
}
