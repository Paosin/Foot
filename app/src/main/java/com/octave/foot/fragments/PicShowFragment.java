package com.octave.foot.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octave.foot.R;
import com.octave.foot.utils.AnalogData;
import com.octave.foot.views.DrawPressurePath;

import butterknife.Bind;
/**
 * A simple {@link Fragment} subclass.
 */
public class PicShowFragment extends Fragment {
    public final static int SUCCESS_ADD_STEPCOUNT = 1;
    public final static int FAILURE_ADD_STEPCOUNT = 0;
    public static int StepCount = 0;

    DrawPressurePath mLeft;
    DrawPressurePath mRight;

    private View view;
    private Handler mHandler;

    public PicShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pic_show, container, false);
        mLeft = (DrawPressurePath) view.findViewById(R.id.left);
        mRight = (DrawPressurePath) view.findViewById(R.id.right);
        mHandler = new Handler();
        //为两个控件开启两个线程，测试以不同的速率传输不同的值到两个控件刷新正误
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AnalogData a = new AnalogData();
                mLeft.initPoint(a.getData());
                mHandler.postDelayed(this, 3000);
                StepCount++;
                mHandler.obtainMessage(SUCCESS_ADD_STEPCOUNT, StepCount);
            }
        });
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AnalogData a = new AnalogData();
                mRight.initPoint(a.getData());
                mHandler.postDelayed(this, 2000);
                StepCount++;
                mHandler.obtainMessage(SUCCESS_ADD_STEPCOUNT, StepCount);
            }
        });
        return view;
    }

}
