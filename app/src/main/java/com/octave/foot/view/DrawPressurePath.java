package com.octave.foot.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.octave.foot.ImageUtils;
import com.octave.foot.R;
import com.octave.foot.bean.CenterOfPressure;
import com.octave.foot.utils.FileTools;

import java.util.List;

/**
 * Created by Paosin Von Scarlet on 2017/1/9.
 */

public class DrawPressurePath extends View {
    //路径坐标
    private float mX;
    private float mY;
    //View宽高
    private float mWidth;
    private float mHeight;
    //画笔
    private final Paint mGesturePaint = new Paint();
    //路径
    private final Path mPath = new Path();
    //要绘制的图片
    Bitmap mBitmap;
    //图片Drawable
    private Drawable mDrawable;
    //读取Excel的工具
    private FileTools file = new FileTools();
    //获取View宽高的第一个方法，没用
    private DisplayMetrics dm = new DisplayMetrics();
    List<CenterOfPressure> data;

    public DrawPressurePath(Context context) {
        this(context, null);
    }

    public DrawPressurePath(Context context, AttributeSet attrs) {
        super(context, attrs);

        mGesturePaint.setAntiAlias(true);
        mGesturePaint.setStyle(Paint.Style.STROKE);
        mGesturePaint.setStrokeWidth(2);
        mGesturePaint.setColor(Color.BLACK);
        initAttrs(attrs);
    }
    private void initPoint(){
        data = file.readExcel("left_foot.xls");
        mX = data.get(0).getX();
        mY = data.get(0).getY();
        mPath.moveTo(mX, mY);
        for (int i = 1; i < data.size(); i++) {
            pointRead(data.get(i));
        }
    }
    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = null;
            try {
                array = getContext().obtainStyledAttributes(attrs, R.styleable.DrawPressurePath);
                mDrawable = array.getDrawable(R.styleable.DrawPressurePath_src);
                measureDrawable();
            } finally {
                if (array != null)
                    array.recycle();
            }
        }
    }

    private void measureDrawable() {
        if (mDrawable == null) {
            throw new RuntimeException("drawable 不能为空");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽度的模式与大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = getMeasuredWidth();
        //获取高度的模式与大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = getMeasuredHeight();
        setMeasuredDimension((int)measureWidth(widthMode, width), (int)measureHeight(heightMode, height));
    }

    private float measureHeight(int mode, int height) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mHeight = height;
                break;
        }
        return mHeight;
    }

    private float measureWidth(int mode, int width) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mWidth = width;
                break;
        }
        return mWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        initPoint();
        if (mBitmap == null) {
            mBitmap = Bitmap.createScaledBitmap(
                    drawableToBitmap(mDrawable),
                    getMeasuredWidth(), getMeasuredHeight(), true);
            System.out.println("----onDraw----" + getMeasuredWidth() + " " + getMeasuredHeight());
        }
        //加入一个矩阵，用于追至翻转画布
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        matrix.postTranslate(0, mBitmap.getHeight());
        canvas.setMatrix(matrix);
        canvas.drawBitmap(mBitmap,getLeft(),getTop(), mGesturePaint);
        canvas.scale(3f,3f);
        canvas.translate(100,20);
        canvas.drawPath(mPath, mGesturePaint);

    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    private void pointRead(CenterOfPressure cop) {
        final float x = cop.getX();
        final float y = cop.getY();
        final float previousX = mX;
        final float previousY = mY;

        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);
        //两点之间的距离大于等于3时，生成贝塞尔绘制曲线
        if (dx >= 3 || dy >= 3) {
            //设置贝塞尔曲线的操作点为起点和终点的一半
            float cX = (x + previousX) / 2;
            float cY = (y + previousY) / 2;

            //二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，cX, cY为终点
            mPath.quadTo(previousX, previousY, cX, cY);
            //第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mX = x;
            mY = y;
        }
    }
}
