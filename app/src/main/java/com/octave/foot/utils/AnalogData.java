package com.octave.foot.utils;

import com.octave.foot.bean.CenterOfPressure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Paosin Von Scarlet on 2017/1/12.
 */

public class AnalogData {
    private final int MAX_FRAME=214;
    private List<CenterOfPressure> data = new ArrayList<CenterOfPressure>();

    public static int getRandom(int smallistNum, int BiggestNum) {
        Random random = new Random();
        return (Math.abs(random.nextInt()) % (BiggestNum - smallistNum + 1))+ smallistNum;
    }

    public List<CenterOfPressure> getData() {
        for(int i=0;i<MAX_FRAME;i++) {
            CenterOfPressure c=new CenterOfPressure();
            c.setFrame(i);
            c.setX(getRandom(-10,10));
            c.setY(20+i);
            c.setMs(i);
            c.setForce(i);
            data.add(c);
        }
        return data;
    }
}