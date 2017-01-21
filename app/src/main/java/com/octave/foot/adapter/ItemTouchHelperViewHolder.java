package com.octave.foot.adapter;

/**
 * Created by Paosin Von Scarlet on 2017/1/18.
 */

public interface ItemTouchHelperViewHolder {
    /**
     * 当Item开始拖拽或者滑动的时候调用
     */
    void onItemSelected();

    /**
     * 当Item完成拖拽或者滑动的时候调用
     */
    void onItemClear();
}
