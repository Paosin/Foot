package com.octave.foot.utils;

import android.graphics.Color;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Paosin Von Scarlet on 2017/2/3.
 */

public class ChartUtils {
    public static final String[] months = new String[]{"Jan", "Feb", "Mar",
            "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",};
    /**
     * 初始化柱状图的函数
     * 月份分布
     * @param mChart
     */
    public static void initColumnChartData(ColumnChartView mChart) {
        ColumnChartData mChartData;
        //每集合显示多少柱
        int nSubCol = 1;
        //显示多少集合
        int nCol = months.length;
        //保存所有柱
        List<Column> columns = new ArrayList<Column>();
        //保存每个柱的值
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        //对每个集合柱子进行遍历
        for (int i = 0; i < nCol; i++) {
            values = new ArrayList<SubcolumnValue>();
            //循环所有柱子(list)
            for (int j = 0; j < nSubCol; j++) {
                //创建一个柱子，然后设置值和颜色，并添加到List
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, lecho.lib.hellocharts.util.ChartUtils.pickColor()));
            }
            //设置x轴柱子所对应的属性名称
            axisValues.add(new AxisValue(i).setLabel(months[i]));
            //将每个属性的拥有的柱子，添加到Column中
            Column column = new Column(values);

            //是否显示每个柱子的Lable
            column.setHasLabels(true);
            //设置每个柱子的Lable是否选中，为false，表示不用选中，一直显示在柱子上
            column.setHasLabelsOnlyForSelected(false);
            //将每个属性得列全部添加到List中
            columns.add(column);
//            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        //设置Columns添加到Data中
        mChartData = new ColumnChartData(columns);
        //设置X轴显示在底部，并且显示每个属性的Lable，字体颜色为黑色，X轴的名字为“学历”，每个柱子的Lable斜着显示，距离X轴的距离为8
        mChartData.setAxisXBottom(new Axis(axisValues).setHasLines(true)
                .setTextColor(Color.BLACK));        //属性值含义同X轴
        mChartData.setAxisYLeft(new Axis().setHasLines(true)
                .setTextColor(Color.BLACK).setMaxLabelChars(2));        //最后将所有值显示在View中
        mChart.setColumnChartData(mChartData);

        // Set value touch listener that will trigger changes for chartTop.
        mChart.setOnValueTouchListener(new ValueTouchListener());

// Set selection mode to keep selected month column highlighted.
        mChart.setValueSelectionEnabled(true);

        mChart.setZoomType(ZoomType.HORIZONTAL);
    }

    public static void initLineChartData(LineChartView mChart) {
        LineChartData mChartData;
        List<Line> lines = new ArrayList<Line>();//定义线的集合
        float[][] randomNumbersTab = setPointsValues();
        //设置Point属性
        for (int i = 0; i < 1; i++) {
            List<PointValue> pointValues = new ArrayList<PointValue>();
            for (int j = 0; j < 12; j++) {
                pointValues.add(new PointValue(j, randomNumbersTab[i][j]));
            }
            //设置Line属性
            Line line = new Line(pointValues);//将值设置给折线
            line.setColor(Color.GREEN);// 设置折线颜色
            line.setStrokeWidth(2);// 设置折线宽度
            line.setFilled(false);// 设置折线覆盖区域是否填充
            line.setCubic(true);// 是否设置为曲线
            line.setPointColor(Color.GREEN);// 设置节点颜色
            line.setPointRadius(3);// 设置节点半径
            line.setHasLabels(true);// 是否显示节点数据
            line.setHasLines(true);// 是否显示折线
            line.setHasPoints(true);// 是否显示节点
            line.setShape(ValueShape.DIAMOND);// 节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
            line.setHasLabelsOnlyForSelected(true);// 隐藏数据，触摸可以显示
            lines.add(line);// 将数据集合添加线
        }
        mChartData = new LineChartData(lines);//将所有的线加入线数据类中
        mChartData.setBaseValue(Float.NEGATIVE_INFINITY);//设置基准数(大概是数据范围)
        //其他的一些属性方法 可自行查看效果
        mChartData.setValueLabelBackgroundAuto(true);            //设置数据背景是否跟随节点颜色
        mChartData.setValueLabelBackgroundColor(Color.BLUE);     //设置数据背景颜色
        mChartData.setValueLabelBackgroundEnabled(true);         //设置是否有数据背景
        mChartData.setValueLabelsTextColor(Color.WHITE);           //设置数据文字颜色
        mChartData.setValueLabelTextSize(10);                    //设置数据文字大小
        mChartData.setValueLabelTypeface(Typeface.MONOSPACE);    //设置数据文字样式
        //如果显示坐标轴
        if (false) {
            Axis axisX = new Axis();                    //X轴
            Axis axisY = new Axis().setHasLines(true);  //Y轴
            axisX.setTextColor(Color.GRAY);             //X轴灰色
            axisY.setTextColor(Color.GRAY);             //Y轴灰色
            //setLineColor()：此方法是设置图表的网格线颜色 并不是轴本身颜色
            //如果显示名称
            if (false) {
                axisX.setName("Axis X");                //设置名称
                axisY.setName("Axis Y");
            }
            mChartData.setAxisXBottom(axisX);            //设置X轴位置 下方
            mChartData.setAxisYLeft(axisY);              //设置Y轴位置 左边
        } else {
            mChartData.setAxisXBottom(null);
            mChartData.setAxisYLeft(null);
        }

        mChart.setLineChartData(mChartData);    //设置图表控件
        final Viewport v = new Viewport(mChart.getMaximumViewport());
        v.bottom = -5;
        v.top=105;
        mChart.setMaximumViewport(v);
        mChart.setCurrentViewportWithAnimation(v);
    }

    public static float[][] setPointsValues() {
        float[][] randomNumbersTab = new float[4][12];
        //图上线最多4条 maxNumberOfLine
        for (int i = 0; i < 4; ++i) {
            //图上结点数为12条 numberOfPoint
            for (int j = 0; j < 12; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }
        return randomNumbersTab;
    }
}
