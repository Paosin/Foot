package com.octave.foot.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.octave.foot.bean.CenterOfPressure;
import com.octave.foot.utils.FileTools;

import java.util.List;

/**
 * Created by Paosin Von Scarlet on 2017/1/9.
 */

public class DrawPressurePath extends View {

    private float mX;
    private float mY;
    private float mWidth;
    private float mHeight;
    private int count = 0;
    private final Paint mGesturePaint = new Paint();
    private final Path mPath = new Path();
    private FileTools file = new FileTools();
    private DisplayMetrics dm = new DisplayMetrics();
    List<CenterOfPressure> data;

    public DrawPressurePath(Context context) {
        super(context);
        mGesturePaint.setAntiAlias(true);
        mGesturePaint.setStyle(Paint.Style.STROKE);
        mGesturePaint.setStrokeWidth(5);
        mGesturePaint.setColor(Color.BLACK);
        dm = context.getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        data = file.readExcel("left_foot.xls");
        mX = data.get(0).getX() + mWidth / 2;
        mY = mHeight / 2 - data.get(0).getY();
        mPath.moveTo(mX, mY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        for (int i = 1; i < data.size(); i++) {
            pointRead(data.get(i));
            //通过画布绘制多点形成的图形
            canvas.drawPath(mPath, mGesturePaint);
        }
    }

    private void pointRead(CenterOfPressure cop) {
        final float x = mWidth / 2 + cop.getX();
        final float y = mHeight / 2 - cop.getY();
        System.out.println(count + ":" + x + "=" + mWidth / 2 + "+" + cop.getX() + "-------" + y + "=" + mHeight / 2 + "-" + cop.getY());
        count++;
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
