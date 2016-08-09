package com.intuit.linesexperiment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.intuit.linesexperiment.spirograph.EditDialog;
import com.intuit.linesexperiment.spirograph.SpirographView;

/**
 * Created by jglenn2 on 3/24/16.
 */
public class SpirographActivity extends AppCompatActivity implements View.OnClickListener, EditDialog.ChangeListener {
    SpirographView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spirograph);

        view = (SpirographView) findViewById(R.id.view);

        findViewById(R.id.editButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditDialog dialog = new EditDialog();
        dialog.setArguments(view.getArgs());
        dialog.show(getFragmentManager(), "edit_dialog");
    }

    @Override
    public void onChange(EditDialog dialog) {
        view.setValues(dialog.getOuter(), dialog.getInner(), dialog.getPercentage());
    }
}
