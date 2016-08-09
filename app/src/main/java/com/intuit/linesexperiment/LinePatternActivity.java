package com.intuit.linesexperiment;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.Locale;

/**
 * Created by jglenn2 on 3/24/16.
 */
public class LinePatternActivity extends AppCompatActivity {

    ChordView chord;
    SeekBar mulSlider;
    EditText mulEdit;
    ValueAnimator mulAnim;
    SeekBar maxSlider;
    EditText maxEdit;

    double mulUpdate = 0.0;
    Thread mulThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_pattern);

        chord = (ChordView) findViewById(R.id.chordView);

        mulSlider = (SeekBar) findViewById(R.id.mulSlider);
        mulEdit= (EditText) findViewById(R.id.mulValue);
        maxSlider = (SeekBar) findViewById(R.id.maxSlider);
        maxEdit= (EditText) findViewById(R.id.maxValue);

        mulSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mulUpdate = (double) (progress - 500) / (double) (seekBar.getMax() * 2);
                    mulUpdate /= 5;
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {
                if (mulAnim != null) {
                    mulAnim.cancel();
                    mulAnim = null;
                }

                if (mulThread == null) {
                    mulThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (true) {
                                    chord.mul += mulUpdate;
                                    chord.postInvalidate();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mulEdit.setText(String.format(Locale.getDefault(), "%.3f", chord.mul));
                                        }
                                    });

                                    Thread.sleep(16);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    mulThread.start();
                }
            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                if (mulThread != null) {
                    mulThread.interrupt();
                    mulThread = null;
                }

                mulUpdate = 0.0f;

                mulAnim = ValueAnimator.ofInt(seekBar.getProgress(), 500);
                mulAnim.setDuration(Math.abs(seekBar.getProgress() - 500));
                mulAnim.setInterpolator(new DecelerateInterpolator());
                mulAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int animProgress = (Integer) animation.getAnimatedValue();
                        Log.d("Test", "Progress = " + animProgress);
                        mulSlider.setProgress(animProgress);
                    }
                });
                mulAnim.start();
            }
        });

        maxSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                chord.max = progress + 10;
                chord.invalidate();
                maxEdit.setText("" + chord.max);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

//        maxEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                int val = Integer.parseInt(s.toString());
//                if (val < 10) {
//                    val = 10;
//                } else if (val > maxSlider.getMax() + 10) {
//                    val = maxSlider.getMax() + 10;
//                }
//                maxSlider.setProgress(val);
//            }
//        });
//
//        mulEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    double val = Double.parseDouble(s.toString());
//                    chord.mul = val;
//                    chord.invalidate();
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
