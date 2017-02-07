package com.octave.foot.adapter;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;


/**
 * Created by Paosin Von Scarlet on 2017/1/17.
 */

public class ItemCallBack extends ItemTouchHelper.Callback {
    public static final float ALPHA_FULL = 1.0f;
    private ItemTouchAdapter mAdapter;

    public ItemCallBack(ItemTouchAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //判断recyclerView传入的LayoutManager是不是GridLayoutManager
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            //Grid的拖拽方向，上、下、左、右
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            //设置为0，表示不支持滑动
            final int swipeFlags = 0;
            //创建拖拽或者滑动标志的快速方式
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            //List类型的拖拽方向，上或者下
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            //List类型支持滑动，左或者右
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            //创建拖拽或者滑动标志的快速方式
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //如果两个item不是一个类型的，我们让他不可以拖拽
//        if (viewHolder.getItemViewType() != target.getItemViewType()) {
//            return false;
//        }

        //得到拖动ViewHolder的position
        int fromPosition = viewHolder.getAdapterPosition();
        //得到目标ViewHolder的position
        int toPosition = target.getAdapterPosition();
        mAdapter.onItemMove(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    /**
     * 当拖拽开始的时候调用
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //拖拽的时候改变一下选中Item的颜色
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                //让ViewHolder知道Item开始选中
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                //回调ItemTouchHelperVIewHolder的方法
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    /**
     * 当拖拽结束时调用
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(ALPHA_FULL);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            //Item移动完成之后的回调
            itemViewHolder.onItemClear();
        }
    }

    /**
     *放下动画
     * @param itemView
     */
    private void putDownAnimation(View itemView) {

    }

    /**
     * 拾起动画
     * @param itemView
     */
    private void pickUpAnimation(View itemView) {

    }

    /**
     * 告诉ItemTouchHelper是否需要RecyclerView支持长按拖拽,返回true是支持，false是不支持
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * 告诉ItemTouchHelper是否需要RecyclerView支持滑动,返回true是支持，false是不支持
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
        return super.interpolateOutOfBoundsScroll(recyclerView, viewSize, viewSizeOutOfBounds, totalSize, msSinceStartScroll);
    }
}
