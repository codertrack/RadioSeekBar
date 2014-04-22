package com.gracecode.android.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.HorizontalScrollView;
import com.gracecode.android.R;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: mingcheng
 * Date: 14-4-21
 */
public class RadioSeekBar extends HorizontalScrollView {
    public static final int NOT_SETTED = -1;
    private static final long DEFUALT_DURATION = 300;
    private int mProgress = NOT_SETTED;
    private int mMax = NOT_SETTED;

    private RadioSeekRuler mRadioSeekRuler;
    private AttributeSet mAttrs;
    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    public interface OnSeekBarChangeListener {
        public abstract void onProgressChanged(RadioSeekBar radioSeekBar, int paramInt);

        public abstract void onStartTrackingTouch(RadioSeekBar radioSeekBar);

        public abstract void onStopTrackingTouch(RadioSeekBar radioSeekBar);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        mOnSeekBarChangeListener = listener;
    }

    public RadioSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.RadioSeekBar);

        try {
            setMax(styledAttributes.getInt(R.styleable.RadioSeekBar_max, NOT_SETTED));
            setProgress(styledAttributes.getInt(R.styleable.RadioSeekBar_progress, NOT_SETTED));
        } finally {
            styledAttributes.recycle();
        }

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mAttrs = attrs;
        mRadioSeekRuler = new RadioSeekRuler(getContext(), attrs);
        addView(mRadioSeekRuler);

        setHorizontalScrollBarEnabled(false);


        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mOnSeekBarChangeListener.onStartTrackingTouch(RadioSeekBar.this);
                            break;
                        case MotionEvent.ACTION_UP:
                            mOnSeekBarChangeListener.onStopTrackingTouch(RadioSeekBar.this);
                            break;
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
    }

    public void setMax(int max) {
        this.mMax = max;
    }

    public int getMax() {
        return mMax;
    }

    public void setProgress(int progress, boolean anim) {
        mProgress = progress;

        if (anim) {
            synchronized (this) {
                ValueAnimator animator = ValueAnimator.ofInt(getScrollX(), progress * RadioSeekRuler.SCALE_ZOOM_SIZE);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(DEFUALT_DURATION);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int offset = (Integer) valueAnimator.getAnimatedValue();
                        scrollTo(offset, getScrollY());
                    }
                });

                animator.start();
            }
        } else {
            setScrollX(progress);
        }
    }

    public void setProgress(int progress) {
        setProgress(progress, false);
    }

    public int getCurrentPostition() {
        return getScrollX() / RadioSeekRuler.SCALE_ZOOM_SIZE;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onProgressChanged(this, getCurrentPostition());
        }
    }
}
