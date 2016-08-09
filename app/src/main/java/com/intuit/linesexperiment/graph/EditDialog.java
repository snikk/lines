package com.intuit.linesexperiment.graph;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.intuit.linesexperiment.R;

import java.util.Locale;

/**
 * Created by jglenn2 on 3/31/16.
 */
public class EditDialog extends DialogFragment implements DialogInterface.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public final static String EXTRA_DIVISIONS = "extra_divisions";
    public final static String EXTRA_THICKNESS = "extra_thickness";

    ChangeListener changeListener = null;

    EditText divisionsText;
    SeekBar percentage;
    TextView percentageLabel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Edit the Graph");
        builder.setPositiveButton("Apply Changes", this);
        builder.setNegativeButton("Cancel Change", null);

        View v = View.inflate(getActivity(), R.layout.dialog_graph, null);
        builder.setView(v);

        divisionsText = (EditText) v.findViewById(R.id.divisions);
        percentage = (SeekBar) v.findViewById(R.id.thicknessSlider);
        percentageLabel = (TextView) v.findViewById(R.id.thicknessText);

        percentage.setOnSeekBarChangeListener(this);

        int divisions = getArguments().getInt(EXTRA_DIVISIONS, -1);
        float per = getArguments().getFloat(EXTRA_THICKNESS, -1);

        if (divisions != -1)
            divisionsText.setText(String.format(Locale.getDefault(), "%d", divisions));
        if (per != -1) {
            percentage.setProgress((int) ((float) percentage.getMax() * per));
            percentageLabel.setText(String.format(Locale.getDefault(), "%.1f", ((float) percentage.getProgress() / (float) percentage.getMax()) * 100));
        }

        return builder.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (ChangeListener.class.isAssignableFrom(activity.getClass())) {
            changeListener = (ChangeListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        changeListener = null;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        boolean invalid = false;
        if ("".equalsIgnoreCase(divisionsText.getText().toString())) {
            invalid = true;
            divisionsText.setError("Hey.  Shouldn't be empty!");
        }

        if (!invalid && changeListener != null)
            changeListener.onChange(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        percentageLabel.setText(String.format(Locale.getDefault(), "%.1f", ((float) progress / (float) seekBar.getMax()) * 10));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface ChangeListener {
        void onChange(EditDialog dialog);
    }

    public int getDivisions() {
        return Integer.parseInt(divisionsText.getText().toString());
    }

    public float getPercentage() {
        return (float) percentage.getProgress() / (float) percentage.getMax();
    }
}
