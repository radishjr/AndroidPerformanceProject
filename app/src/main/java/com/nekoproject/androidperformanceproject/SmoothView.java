package com.nekoproject.androidperformanceproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by neko on 12/30/2015.
 */
public class SmoothView extends View {

    Boolean isClicked = false;
    String name = "";
    Paint mTextPaint;
    Paint mBackgroundPaint;
    int centerX;
    int centerY;
    int radius;

    public SmoothView(Context context) {
        super(context);
        init();
    }

    public SmoothView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmoothView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.GRAY);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(48);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = 200;
        heightMeasureSpec = 200;
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        radius =  Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mBackgroundPaint.setColor(Color.BLUE);
                mTextPaint.setColor(Color.WHITE);
                invalidate();
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mBackgroundPaint.setColor(Color.GRAY);
                mTextPaint.setColor(Color.BLACK);
                isClicked = false;
                invalidate();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(
                centerX,
                centerY,
                radius,
                mBackgroundPaint
        );

        canvas.drawText(name, centerX, centerY, mTextPaint);
    }

    public void setText(String text){
        name = text;
    }

}
