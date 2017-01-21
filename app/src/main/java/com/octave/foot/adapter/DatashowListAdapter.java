package com.octave.foot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.octave.foot.R;
import com.octave.foot.utils.ValueTouchListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

import static com.octave.foot.fragments.PicShowFragment.StepCount;


/**
 * Created by Paosin Von Scarlet on 2017/1/17.
 */

public class DatashowListAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchAdapter {

    public final static String[] months = new String[]{"Jan", "Feb", "Mar",
            "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",};

    private Context mContext;
    private List<Integer> listOrder;

    public DatashowListAdapter(Context mContext, List<Integer> listOrder) {
        this.mContext = mContext;
        this.listOrder = listOrder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new mDatashowRecyclerViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datashow_word, parent, false));
        else
            return new mDatashowRecyclerViewHolder2(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datashow_chart, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        int i;
        switch (listOrder.get(position)) {
            case 0:
                i = 0;
                break;
            case 1:
                i = 1;
                break;
            default:
                i = 0;
                break;
        }
        return i;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                mDatashowRecyclerViewHolder holder1 = (mDatashowRecyclerViewHolder) holder;
                holder1.textView.setText(StepCount+"");
                break;
            case 1:
                final mDatashowRecyclerViewHolder2 holder2 = (mDatashowRecyclerViewHolder2) holder;
                holder2.mChart.setInteractive(false);
                initChartData(holder2.mChart);
//                holder2.mChart.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        FrameLayout layout = new FrameLayout(mContext);
//                        //设置帧布局宽高
//                        FrameLayout.LayoutParams frameLayout = new FrameLayout.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT);
//                        //设置控件宽高
//                        FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT);
//                        layout.addView(holder2.mChart,viewParams);
//                        Fragment fragment = new Fragment();
//                    }
//                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    /**
     * 在item移动的时候执行
     * @param fromPosition
     * @param toPosition
     * @return
     */
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(listOrder, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * 在Item删除的时候执行
     * @param position
     */
    @Override
    public void onItemDismiss(int position) {
        listOrder.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 通用ViewHolder类，仍需改进
     */
    static class mCommonViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public mCommonViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 当被选择的时候，item有什么变化，应加入高级动画
         */
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.GRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * 文字的ViewHolder
     */
    private class mDatashowRecyclerViewHolder
            extends mCommonViewHolder {
        CardView cardView;
        TextView textView;
        public mDatashowRecyclerViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_data_word);
            textView = (TextView) itemView.findViewById(R.id.word_datashow);
        }

    }

    /**
     * 统计图ViewHolder
     */
    private class mDatashowRecyclerViewHolder2
            extends mCommonViewHolder {
        CardView cardView;
        ColumnChartView mChart;

        public mDatashowRecyclerViewHolder2(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_data_chart);
            mChart = (ColumnChartView) itemView.findViewById(R.id.chart_datashow);
        }
    }

    /**
     * 初始化统计图的函数，柱状
     * 月份分布
     * @param mChart
     */
    public void initChartData(ColumnChartView mChart) {
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
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
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

    View.OnClickListener onClickListener =new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();

        }
    };
}
