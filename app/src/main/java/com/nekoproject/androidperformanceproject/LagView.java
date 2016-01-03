package com.nekoproject.androidperformanceproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by neko on 12/30/2015.
 */
public class LagView extends View {

    Boolean isClicked = false;
    Rect rect;
    String name = "";

    public LagView(Context context) {
        super(context);
        rect = new Rect(0, 0, 100, 100);
    }

    public LagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rect = new Rect(0,0,w,h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = 200;
        heightMeasureSpec = 200;
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                isClicked = true;
                invalidate();
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
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
        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        try {
            int[] allocated = new int[1000000]; //240 + M
        }catch (OutOfMemoryError e){
            name = "OOM";
        }

        mTextPaint.setTextSize(48);
        if(isClicked) {
            mTextPaint.setColor(Color.WHITE);
        }

        if(isClicked) {
            mBackgroundPaint.setColor(Color.BLUE);
        }else{
            mBackgroundPaint.setColor(Color.GRAY);
        }
        // Draw the shadow
        canvas.drawCircle(
                getMeasuredWidth() / 2,
                getMeasuredHeight() / 2,
                Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2,
                mBackgroundPaint
        );

        canvas.drawText(name, getMeasuredWidth() / 2, getMeasuredHeight() / 2, mTextPaint);
    }

    public void setText(String text){
        name = text;
    }

}
