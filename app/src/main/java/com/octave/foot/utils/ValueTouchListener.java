package com.octave.foot.utils;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.SubcolumnValue;

/**
 * Created by Paosin Von Scarlet on 2017/1/18.
 */

public class ValueTouchListener implements ColumnChartOnValueSelectListener {
    @Override
    public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
        // generateLineData(value.getColor(), 100);
    }

    @Override
    public void onValueDeselected() {
        // generateLineData(ChartUtils.COLOR_GREEN, 0);
    }
}
