package com.gracecode.android;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.gracecode.android.ui.widget.RadioSeekBar;
import com.gracecode.android.ui.widget.RadioSeekRuler;

public class ExampleActivity extends Activity {
    private static final String TAG = "RadioSeekBar";
    private ValueAnimator mOpenAnimator;
    private RadioSeekBar mRadioSeekBar;
    private TextView mStatusTextView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mStatusTextView = (TextView) findViewById(R.id.status);

        mRadioSeekBar = (RadioSeekBar) findViewById(R.id.seekbar);
        mRadioSeekBar.setOnSeekBarChangeListener(new RadioSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(RadioSeekBar radioSeekBar, int paramInt) {
                mStatusTextView.setText("Progress: "
                        + paramInt + "/" + radioSeekBar.getMax()
                        + "(" + RadioSeekRuler.toTimeString(paramInt * 1000) + ")");
            }

            @Override
            public void onStartTrackingTouch(RadioSeekBar radioSeekBar) {
                Log.e(TAG, "Start Touch, Current Postion: " + radioSeekBar.getCurrentPostition());
            }

            @Override
            public void onStopTrackingTouch(RadioSeekBar radioSeekBar) {
                Log.e(TAG, "Stop Touch, Current Postion: " + radioSeekBar.getCurrentPostition());
            }
        });

        findViewById(R.id.random_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRadioSeekBar.setProgress((int) (mRadioSeekBar.getMax() * Math.random()), true);
            }
        });
    }


}
