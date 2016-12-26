package com.kiuber.blog.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Kiuber on 2016/12/26.
 */

public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getViewSize(100, widthMeasureSpec);
        int height = getViewSize(100, heightMeasureSpec);

        if (width > height) {
            height = width;
        } else {
            width = height;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int r = getMeasuredWidth() / 2;
        int centerX = getLeft() + r;
        int centerY = getTop() + r;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawCircle(centerX, centerY, r, paint);
    }

    private int getViewSize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                mySize = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                mySize = size;
                break;
            case MeasureSpec.AT_MOST:
                mySize = size;
                break;
        }
        return mySize;
    }
}
