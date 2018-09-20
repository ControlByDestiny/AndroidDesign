package com.designpattern.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.designpattern.R;


public class CustomImgView extends android.support.v7.widget.AppCompatImageView {
    private final static String TAG = "CustomImgView";
    //画圆形边界的笔
    private Paint mPaintBorder;
    //画圆形图形的笔
    private Paint mPaintImg;
    //画圆形背景的笔
    private Paint mPaintBK;
    //边界的宽
    private int mBorderWidth;
    //边界颜色
    private int mBorderColor;
    //背景颜色
    private int mBKColor;
    //默认圆形图形
    private Drawable mDrawable;
    //图形变换处理，用来缩放图片，以适应view控件的大小
    private Matrix mMatrix;
    private BitmapShader mBitmapShader;
    //控件的宽度
    private int mWidth;
    //控件的高度
    private int mHeight;
    //圆形半径
    private int mRadius;

    public CustomImgView(Context context) {
        this(context, null);
    }

    public CustomImgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImgView);
        int nums = typedArray.getIndexCount();
        for (int i = 0; i < nums; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomImgView_border_width:
                    mBorderWidth = (int) typedArray.getDimension(attr, 0);
                    break;
                case R.styleable.CustomImgView_border_color:
                    mBorderColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImgView_src:
                    mDrawable = typedArray.getDrawable(attr);
                    break;
                case R.styleable.CustomImgView_background_color:
                    mBKColor=typedArray.getColor(attr,0);
                    break;
            }
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        mMatrix = new Matrix();
        mPaintImg = new Paint();
        //抗锯齿
        mPaintImg.setAntiAlias(true);
        mPaintImg.setStrokeWidth(12);
        this.setLayerType(LAYER_TYPE_SOFTWARE, mPaintImg);
        /***
         * radius:模糊半径，radius越大越模糊，越小越清晰，但是如果radius设置为0，则阴影消失不见
         * dx:阴影的横向偏移距离，正值向右偏移，负值向左偏移
         * dy:阴影的纵向偏移距离，正值向下偏移，负值向上偏移
         * color: 绘制阴影的画笔颜色，即阴影的颜色（对图片阴影无效）
         ***/
        mPaintImg.setShadowLayer(5.0f, 1.0f, 1.0f, Color.GRAY);
        //给图形加边框
        mPaintBorder = new Paint();
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setStrokeWidth(mBorderWidth);
        mPaintBorder.setColor(mBorderColor);

        //画背景颜色的笔
        mPaintBK = new Paint();
        mPaintBK.setAntiAlias(true);
        mPaintBK.setColor(mBKColor);
        mPaintBK.setStyle(Paint.Style.FILL);
    }

    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 创建画布
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    //使用BitmapShader画圆图形
    private void setBitmapShader() {
        //将图片转换成Bitmap
        Bitmap bitmap = drawable2Bitmap(mDrawable);
        if (bitmap == null)
            Log.i(TAG, "bitmap null");
        //将bitmap放进图像着色器，后面两个模式是x，y轴的缩放模式，CLAMP表示拉伸
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //初始化图片与view之间伸缩比例，因为比例一般非整数，所以用float，免得精度丢失
        float scale;
        //将图片的宽度高度的最小者作为图片的边长，用来和view来计算伸缩比例
        int bitmapSize = Math.min(bitmap.getHeight(), bitmap.getWidth());
        /**注意这里，我使用的是图片最长的（就是宽度）来伸缩，那么用这个的话，
         * 我们就会发现，较短的那边（就是高度）在经过Matrix的拉伸后会发现失真，强行地被拉长，
         * 一、因为图片为了适应最长的那边可以完全在view上展示，把长的给压缩了，而短的比长的那边短，所以要强行拉伸，那么就会导致短的这边被拉伸时候失真
         *二、因为图像的变换是针对每一个像素点的，所以有些变换可能发生像素点的丢失，
         * 这里需要使用Paint.setAnitiAlias(boolean)设置来消除锯齿，这样图片变换后的效果会好很多。

         */

        //计算缩放比例，view的大小和图片的大小比例
        scale = Math.min(mWidth,mHeight)/2 * 1.0f / bitmapSize;
        //利用这个图像变换处理器，设置伸缩比例，长宽以相同比例伸缩
        mMatrix.setScale(scale, scale);
        //给那个图像着色器设置变换矩阵，绘制时就根据view的size，设置图片的size
        //使图片的较小的一边缩放到view的大小一致，这样就可以避免图片过小导致CLAMP拉伸模式或过大导致显示不全
        mBitmapShader.setLocalMatrix(mMatrix);
        //为画笔套上一个Shader的笔套
        mPaintImg.setShader(mBitmapShader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRadius = Math.min(mHeight, mWidth) / 2-mBorderWidth;
        int viewSize = mRadius * 2 + mBorderWidth;
        Log.i(TAG, "mBorderWidth=" + mBorderWidth + "   viewSize=" + viewSize);
        setMeasuredDimension(viewSize, viewSize);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable != null) {
                Log.i(TAG, "mDrawable != null");
            setBitmapShader();
            //画背景颜色
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaintBK);
            //画图
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaintImg);
           //画边框
            canvas.drawCircle(mRadius, mRadius, mRadius+  mBorderWidth, mPaintBorder);
        } else {
            //如果在xml中这个继承ImageView的类没有被set图片就用默认的ImageView方案咯
            super.onDraw(canvas);
        }
    }
}
