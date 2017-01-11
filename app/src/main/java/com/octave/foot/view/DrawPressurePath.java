package com.octave.foot.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.octave.foot.R;
import com.octave.foot.bean.CenterOfPressure;
import com.octave.foot.utils.FileTools;

import java.util.List;

/**
 * Created by Paosin Von Scarlet on 2017/1/11.
 */

public class DrawPressurePath extends SurfaceView implements SurfaceHolder.Callback, Runnable {
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
    private int mDimension;     //线宽度
    private float multiple;     //根据绘图选取倍数
    //要绘制的图片
    private Bitmap mBitmap;
    //图片Drawable
    private Drawable mDrawable;
    //读取Excel的工具
    private FileTools file = new FileTools();
    private List<CenterOfPressure> data;

    private String mFilePath;

    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    //用于绘制的线程
    private Thread mThread;
    //线程开关
    private boolean isRunning;

    public DrawPressurePath(Context context) {
        this(context, null);
    }

    public DrawPressurePath(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        mHolder.addCallback(this);

        //可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常量
        setKeepScreenOn(true);
        setZOrderOnTop(true);

        initAttrs(attrs);
        initPoint();
    }

    //初始化属性
    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = null;
            try {
                array = getContext().obtainStyledAttributes(attrs, R.styleable.DrawPressurePath);
                mDrawable = array.getDrawable(R.styleable.DrawPressurePath_backgroundsrc);
                mFilePath = array.getString(R.styleable.DrawPressurePath_filepath);
                //dp,sp都会乘desity,px直接等于
                mDimension = (int) array.getDimension(R.styleable.DrawPressurePath_lineweight, 6);
                measureDrawable();
            } finally {
                if (array != null)
                    array.recycle();
            }
        }
    }

    private void pointRead(CenterOfPressure cop) {
        final float x = cop.getX();
        final float y = cop.getY();
        final float previousX = mX;
        final float previousY = mY;
//        System.out.println(cop.getFrame()+" x:"+x+"-----y:"+y);
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
//        mPath.lineTo(x, y);
//        mX = x;
//        mY = y;
    }

    private void draw() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (mBitmap == null) {
            mBitmap = Bitmap.createScaledBitmap(
                    drawableToBitmap(mDrawable),
                    getMeasuredWidth(), getMeasuredHeight(), true);
            multiple = (6 * (mBitmap.getHeight() / 11)) / (data.get(data.size() - 1).getY() - data.get(0).getY());
        }
        Matrix matrix = this.getMatrix();
        mCanvas.drawBitmap(mBitmap, 0, 0, mGesturePaint);

        //加入一个矩阵，用于翻转画布
        matrix.postScale(multiple, -multiple);
        matrix.postTranslate(getMeasuredWidth() / 2, getMeasuredHeight() * 0.83f);
        mPath.transform(matrix);
        mCanvas.drawPath(mPath, mGesturePaint);
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
        setMeasuredDimension((int) measureWidth(widthMode, width), (int) measureHeight(heightMode, height));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mGesturePaint.setAntiAlias(true);
        mGesturePaint.setStyle(Paint.Style.STROKE);
        mGesturePaint.setStrokeWidth(mDimension);
        mGesturePaint.setColor(Color.BLACK);

        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        //不断进行绘制
        if (isRunning) {
            drawSurface();
        }
    }

    private void drawSurface() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                long start = System.currentTimeMillis();
                draw();
                long end = System.currentTimeMillis();
                if (end - start < 100) {
                    try {
                        Thread.sleep(100 - end + start);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    //初始化点坐标
    private void initPoint() {
        data = file.readExcel(mFilePath);
        mX = data.get(0).getX();
        mY = data.get(0).getY();
        mPath.moveTo(mX, mY);
        for (int i = 1; i < data.size(); i++) {
            pointRead(data.get(i));
        }
    }
    //测量Drawable长宽
    private void measureDrawable() {
        if (mDrawable == null) {
            throw new RuntimeException("drawable 不能为空");
        }
    }

    private float measureHeight(int mode, int height) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
                mHeight = height;
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
                mWidth = width;
                break;
            case MeasureSpec.EXACTLY:
                mWidth = width;
                break;
        }
        return mWidth;
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
}
