package com.octave.foot;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.octave.foot.bean.CenterOfPressure;
import com.octave.foot.utils.AnalogData;
import com.octave.foot.view.DrawPressurePath;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    @Bind(R.id.left)
    DrawPressurePath mLeft;
    @Bind(R.id.right)
    DrawPressurePath mRight;

    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        getSupportActionBar().hide();

        mHandler = new Handler();
        //为两个控件开启两个线程，测试以不同的速率传输不同的值到两个控件刷新正误
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AnalogData a = new AnalogData();
                mLeft.initPoint(a.getData());
                mHandler.postDelayed(this, 3000);
            }
        });
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AnalogData a = new AnalogData();
                mRight.initPoint(a.getData());
                mHandler.postDelayed(this, 2000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
