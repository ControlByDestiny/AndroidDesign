package com.designpattern.CustomView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomView extends View {
    private final static String TAG = "CustomView";
    private int mWidth = 0;
    private int mHeight = 0;
    private int mRadius = 0;
    private Paint mPaint;
    private Matrix mMatrix;
    private int mImgRadius=0;

    //如果view是在java代码里new的，则调用此构造方法
    public CustomView(Context context) {
        super(context);
    }

    //如果view是在xml里申明的，则调用此构造方法
    //自定义属性是从AttributeSet传进来的
    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    //不会自动调用，一般是在第二个构造函数里主动调用，如果view有Style属性时
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //API 21之后才会使用，不会自动调用，一般是在第二个构造函数里主动调用，如果view有Style属性时
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private static void Usages() {
        StackTraceElement[] info = Thread.currentThread().getStackTrace();
        for (StackTraceElement tempInfo : info) {
            Log.i(TAG, tempInfo.toString());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRadius = Math.min(mWidth, mHeight) / 2;
        mImgRadius=mRadius-10;
        setMeasuredDimension(mRadius * 2, mRadius * 2);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//        mPaint.setStrokeWidth(0.1f);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.BLACK);
//        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        Log.i(TAG, "---draw---");
    }
    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        Log.i(TAG, "---onDrawForeground---");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.i(TAG, "---dispatchDraw---");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mPaint.reset();
//        mPaint.setAntiAlias(true);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        Log.i(TAG, "---onDraw---");
    }

    public void setCustomBitmap(Bitmap bitmap) {

    }

    public void setCustomDrawable(Drawable drawable) {
        Log.i(TAG, "---setCustomDrawable---");
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 创建画布
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        BitmapShader mBitmapShader=new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale ;
        // 取小值，如果取大值的话，则不能覆盖view
        int bitmapWidth = Math.min(bitmap.getWidth(), getHeight());
        scale = mWidth * 1.0f / bitmapWidth;
        mMatrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(mBitmapShader);
    }
}
