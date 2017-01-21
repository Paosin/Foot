package com.octave.foot.adapter;

/**
 * Created by Paosin Von Scarlet on 2017/1/18.
 */

public interface ItemTouchAdapter {
    /**
     * Item已经移动的足够远的时候调用
     *
     * @param fromPosition
     * @param toPosition
     * @return
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * 当Item滑动取消的时候调用
     *
     * @param position
     */
    void onItemDismiss(int position);
}
