package com.gracecode.android.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gracecode.android.R;

import java.util.concurrent.TimeUnit;

public class RadioSeekRuler extends TextView {
    public static final int SCALE_ZOOM_SIZE = 10;
    private static final int SCALE_STEP = 6 * SCALE_ZOOM_SIZE;
    private static final int SCALE_BIG_STEP = 30 * SCALE_ZOOM_SIZE;
    private static final int SCALE_TITLE_FONT_SIZE = 26;

    private AttributeSet mAttrs;
    private int mMax;
    private static Paint mScalePaint;
    private static Paint mCurrentScalePaint;
    private static Paint mScaleTitlePaint;

    public RadioSeekRuler(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.RadioSeekBar);
        try {
            setMax(styledAttributes.getInt(R.styleable.RadioSeekBar_max, RadioSeekBar.NOT_SETTED));
        } finally {
            styledAttributes.recycle();
        }

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mAttrs = attrs;

        mScalePaint = getScalePaint();
        mCurrentScalePaint = getCurrentScalePaint();
        mScaleTitlePaint = getScaleTitlePaint();
    }

    private static Paint getScalePaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLUE);
        return paint;
    }

    private static Paint getCurrentScalePaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        return paint;
    }

    private static Paint getScaleTitlePaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(1);
        paint.setColor(Color.BLUE);
        paint.setTextSize(SCALE_TITLE_FONT_SIZE);
        return paint;
    }

    @Override
    public void setWidth(int pixels) {
        super.setWidth(getWidthPixels(pixels));
    }

    private int getWidthPixels(int pixels) {
        pixels *= SCALE_ZOOM_SIZE;
        if (getParent() instanceof ViewGroup) {
            pixels += ((ViewGroup) getParent()).getWidth();
        }

        return pixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                if (getParent() instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) getParent();
                    setMeasuredDimension(getWidthPixels(getMax()), viewGroup.getHeight());
                }
                return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void drawScale(Canvas canvas, int offset) {
        int height = getHeight(), width = getWidth(), middleLine = height / 2,
                scaleWidth = width - offset, scaleHeight = height / 8;

        for (int i = offset; i <= scaleWidth; i += SCALE_STEP) {
            if (i % SCALE_BIG_STEP == 0) {
                canvas.drawLine(i, middleLine - scaleHeight, i, middleLine + scaleHeight, mCurrentScalePaint);
                drawScaleTitle(canvas, i, offset);
            } else {
                canvas.drawLine(i, middleLine - scaleHeight / 2, i, middleLine + scaleHeight / 2, mScalePaint);
            }
        }
    }

    private void drawScaleTitle(Canvas canvas, int i, int offset) {
        int width = 32 * 2, height = 32;
        canvas.drawText(toTimeString((i - offset) / SCALE_ZOOM_SIZE * 1000),
                i - width / 2, getHeight() - height, mScaleTitlePaint);
    }


    public static String toTimeString(long millis) {
        // @see https://stackoverflow.com/questions/625433/how-to-convert-milliseconds-to-x-mins-x-seconds-in-java
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int offset = 0;
        if (getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) getParent();
            offset = viewGroup.getWidth() / 2;
        }

        drawScale(canvas, offset);
    }


    public void setMax(int max) {
        this.mMax = max;
    }

    public int getMax() {
        return this.mMax;
    }
}
