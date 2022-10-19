package com.face.chargcabinetfactorytest;

import android.view.View;

public interface OnItemClickListener {
    /**
     * 单击事件
     *
     * @param view
     * @param position 当前Item position
     */
    void onItemClick(View view, int position);

    /**
     * 长按事件
     *
     * @param view
     * @param position 当前Item position
     */
    void onItemLongClick(View view, int position);
}
