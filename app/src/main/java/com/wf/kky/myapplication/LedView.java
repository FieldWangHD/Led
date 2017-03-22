package com.wf.kky.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;


/**
 * LED
 */

public class LedView extends View {
    public static  final int X_SUM = 100;
    public static final int Y_SUM = 20;
    private static final float mBitmapFactor = 0.1f;

    private boolean[][] mShowPositions;

    private int mWidth;
    private int mHeight;
    private int mPixWidth;
    private int mPixHeight;
    private int mOneWidth;
    private int mOneWidthHalf;
    private int mOneHeight;
    private int mOneHeightHalf;
    private float mBitmapX;
    private float mBitmapY;
    private float mBitmapArea;

    private Paint mPaint;

    public LedView(Context context) {
        this(context, null);
    }

    public LedView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        int designWidth, designHeight;
        if (widthMode == MeasureSpec.EXACTLY) {
            designWidth = widthSize;
        } else {
            // TODO: 2016/12/20 wrap_content
            designWidth = 0;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            designHeight = heightSize;
        } else {
            // TODO: 2016/12/20 wrap_content
            designHeight = 0;
        }
        setMeasuredDimension(designWidth, designHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLEDs(canvas);
    }

    private void drawLEDs(Canvas canvas) {
        for (int i = 0; i < mPixHeight; i++) { // i 行
            for (int j = 0; j < mPixWidth; j++) { // j 列
                if (mShowPositions[i][j]) {
                    canvas.drawCircle(j * mOneWidth + mOneWidthHalf, i * mOneHeight + mOneHeightHalf, 5, mPaint);
                }
            }
        }
    }

    public void setLEDStr(String str) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        TextPaint paint = new TextPaint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(20);
        paint.setTextSize(150);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(str, mWidth / 2, mHeight, paint);

        mBitmapX = bitmap.getWidth() / X_SUM;
        mBitmapY = bitmap.getHeight() / Y_SUM;
        mBitmapArea = mBitmapX * mBitmapY;

        boolean[][] result = new boolean[Y_SUM][X_SUM];
        for (int i = 0; i < Y_SUM; i++) {
            for (int j = 0; j < X_SUM; j++) {
                result[i][j] = checkIsFill(bitmap, i, j);
            }
        }

        setLEDs(result);
    }

    private boolean checkIsFill(Bitmap bitmap, int y, int x) {
        int xSum = (int) (x * mBitmapX);
        int xSum1 = (int) ((x + 1) * mBitmapX);
        int ySum = (int) (y * mBitmapY);
        int ySum1 = (int) ((y + 1) * mBitmapY);

        int count = 0;
        for (int i = xSum; i < xSum1; i++) {
            for (int j = ySum; j < ySum1; j++) {
                if (bitmap.getPixel(i, j) != 0) {
                    count++;
                }
            }
        }
        return (count / (float) mBitmapArea > mBitmapFactor);
    }

    public void setLEDs(boolean[][] pix) {
        mShowPositions = pix;
        mPixWidth = pix[0].length;
        mPixHeight = pix.length;

        mOneWidth = mWidth / mPixWidth;
        mOneWidthHalf = mOneWidth / 2;
        mOneHeight = mHeight / mPixHeight;
        mOneHeightHalf = mOneHeight / 2;

        invalidate();
    }

}
