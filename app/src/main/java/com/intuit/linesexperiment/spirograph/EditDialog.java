package com.intuit.linesexperiment.spirograph;

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
    public final static String EXTRA_BASE = "extra_base";
    public final static String EXTRA_INNER = "extra_inner";
    public final static String EXTRA_PERCENTAGE = "extra_percentage";

    ChangeListener changeListener = null;

    EditText outerText;
    EditText innerText;
    SeekBar percentage;
    TextView percentageLabel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Edit the spirograph");
        builder.setPositiveButton("Apply Changes", this);
        builder.setNegativeButton("Cancel Change", null);

        View v = View.inflate(getActivity(), R.layout.dialog_controls, null);
        builder.setView(v);

        outerText = (EditText) v.findViewById(R.id.outerText);
        innerText = (EditText) v.findViewById(R.id.innerText);
        percentage = (SeekBar) v.findViewById(R.id.percentageSlider);
        percentageLabel = (TextView) v.findViewById(R.id.percentageText);

        percentage.setOnSeekBarChangeListener(this);

        int baseRadius = getArguments().getInt(EXTRA_BASE, -1);
        int innerRadius = getArguments().getInt(EXTRA_INNER, -1);
        float per = getArguments().getFloat(EXTRA_PERCENTAGE, -1);

        if (baseRadius != -1)
            outerText.setText(String.format(Locale.getDefault(), "%d", baseRadius));
        if (innerRadius != -1)
            innerText.setText(String.format(Locale.getDefault(), "%d", innerRadius));
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
        if ("".equalsIgnoreCase(outerText.getText().toString())) {
            invalid = true;
            outerText.setError("Hey.  Shouldn't be empty!");
        }

        if ("".equalsIgnoreCase(outerText.getText().toString())) {
            invalid = true;
            outerText.setError("Hey.  Shouldn't be empty!");
        }

        if (!invalid && changeListener != null)
            changeListener.onChange(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        percentageLabel.setText(String.format(Locale.getDefault(), "%.1f", ((float) progress / (float) seekBar.getMax()) * 100));
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

    public int getOuter() {
        return Integer.parseInt(outerText.getText().toString());
    }

    public int getInner() {
        return Integer.parseInt(innerText.getText().toString());
    }

    public float getPercentage() {
        return (float) percentage.getProgress() / (float) percentage.getMax();
    }
}
