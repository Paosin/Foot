package com.octave.foot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.octave.foot.R;
import com.octave.foot.utils.ChartUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.view.LineChartView;

import static com.octave.foot.fragments.PicShowFragment.StepCount;


/**
 * Created by Paosin Von Scarlet on 2017/1/17.
 */

public class DatashowListAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchAdapter, View.OnClickListener, View.OnLongClickListener {
    //数据展示，数字文字部分
    public final int TYPE_DATASHOW_WORD = 0;
    //数据展示，柱状图部分
    public final int TYPE_DATASHOW_CHART = 1;

    private Context mContext;
    private List<Integer> listOrder;
    private RecyclerViewOnItemClickListener onItemClickListener;
    private RecyclerViewOnItemLongClickListener onItemLongClickListener;
    private boolean isEditMode;

    public DatashowListAdapter(Context mContext, int maxItemViewCount) {
        this.mContext = mContext;
        this.listOrder = initListOrder(maxItemViewCount);
    }

    private List<Integer> initListOrder(int maxItemViewCount) {
        List<Integer> a = new ArrayList<Integer>();
        for (int i = 0; i < maxItemViewCount; i++) {
            a.add(i);
        }
        return a;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        if (viewType == TYPE_DATASHOW_WORD) {
            final View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datashow_word, parent, false);
            final mDatashowRecyclerViewHolder vh = new mDatashowRecyclerViewHolder(root);
//            root.setOnClickListener(this);
//            root.setOnLongClickListener(this);
            vh.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!isEditMode) {
                        RecyclerView recyclerView = ((RecyclerView) parent);
                        startEditMode(recyclerView);
                    }
                    return true;
                }
            });
//            vh.imgBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = vh.getAdapterPosition();
//                    onItemDismiss(position);
//                }
//            });
            return vh;
        } else {
            final View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datashow_chart, parent, false);
            final mDatashowRecyclerViewHolder2 vh = new mDatashowRecyclerViewHolder2(root);
            vh.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!isEditMode) {
                        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
                        RecyclerView recyclerView = ((RecyclerView) parent);
                        startEditMode(recyclerView);
                    }
                    return true;
                }
            });
            vh.imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = vh.getAdapterPosition();
                    onItemDismiss(position);
                }
            });
//            root.setOnClickListener(this);
//            root.setOnLongClickListener(this);
            return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int i;
        switch (listOrder.get(position)) {
            case 0:
                i = TYPE_DATASHOW_WORD;
                break;
            case 1:
                i = TYPE_DATASHOW_CHART;
                break;
            default:
                i = TYPE_DATASHOW_WORD;
                break;
        }
        return i;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_DATASHOW_WORD:
                mDatashowRecyclerViewHolder holder1 = (mDatashowRecyclerViewHolder) holder;
//                holder1.imgBtn.setVisibility(View.INVISIBLE);
                holder1.root.setTag(position);
                break;
            case TYPE_DATASHOW_CHART:
                final mDatashowRecyclerViewHolder2 holder2 = (mDatashowRecyclerViewHolder2) holder;
                holder2.imgBtn.setVisibility(View.INVISIBLE);
                holder2.root.setTag(position);
                ChartUtils.initLineChartData(holder2.mChart);
                break;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            switch (getItemViewType(position)) {
                case TYPE_DATASHOW_WORD:
                    mDatashowRecyclerViewHolder holder1 = (mDatashowRecyclerViewHolder) holder;
                    holder1.textView.setText(StepCount + "");
                    break;
                case TYPE_DATASHOW_CHART:
                    onBindViewHolder(holder, position);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    /**
     * 在item移动的时候执行
     *
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
     *
     * @param position
     */
    @Override
    public void onItemDismiss(int position) {
        listOrder.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClickListener(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return onItemLongClickListener != null && onItemLongClickListener.onItemLongClickListener(v, (Integer) v.getTag());
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
     * 开启编辑模式
     *
     * @param parent
     */
    private void startEditMode(RecyclerView parent) {
        isEditMode = true;

        int visibleChildCount = parent.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.img_edit_datashow);
            if (imgEdit != null)
                imgEdit.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 完成编辑模式
     *
     * @param parent
     */
    private void cancelEditMode(RecyclerView parent) {
        isEditMode = false;

        int visibleChildCount = parent.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.img_edit_datashow);
            if (imgEdit != null) {
                imgEdit.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 文字的ViewHolder
     */
    private class mDatashowRecyclerViewHolder
            extends mCommonViewHolder {
        CardView cardView;
        TextView textView;
        ImageView imgBtn;
        View root;

        public mDatashowRecyclerViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            cardView = (CardView) itemView.findViewById(R.id.card_data_word);
            textView = (TextView) itemView.findViewById(R.id.step_word_datashow);
            imgBtn = (ImageView) itemView.findViewById(R.id.img_edit_datashow);
        }

    }

    /**
     * 统计图ViewHolder
     */
    private class mDatashowRecyclerViewHolder2
            extends mCommonViewHolder {
        CardView cardView;
        //        ColumnChartView mChart;
        LineChartView mChart;
        ImageView imgBtn;
        View root;

        public mDatashowRecyclerViewHolder2(View itemView) {
            super(itemView);
            root = itemView;
            cardView = (CardView) itemView.findViewById(R.id.card_data_chart);
//            mChart = (ColumnChartView) itemView.findViewById(R.id.chart_datashow);
            mChart = (LineChartView) itemView.findViewById(R.id.chart_datashow);
            imgBtn = (ImageView) itemView.findViewById(R.id.img_edit_datashow);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();

        }
    };

    /*设置点击事件*/
    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /*设置长按事件*/
    public void setOnItemLongClickListener(RecyclerViewOnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 当Item被点击时调用
     */
    public interface RecyclerViewOnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    /**
     * 当Item被长按的时候调用
     */
    public interface RecyclerViewOnItemLongClickListener {
        boolean onItemLongClickListener(View view, int position);
    }
}
