package com.face.chargcabinetfactorytest.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.face.chargcabinetfactorytest.OnItemClickListener;
import com.face.chargcabinetfactorytest.R;

public class LockAdapter extends RecyclerView.Adapter<LockAdapter.ItemDataHolder> {
    private Context mContext;
    private int totalSize;
    private final LayoutInflater mLayoutInflater;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClick(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public LockAdapter(Context mContext, int totalSize) {
        this.mContext = mContext;
        this.totalSize = totalSize;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ItemDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutId = R.layout.item_base;
        return new ItemDataHolder(mLayoutInflater.inflate(layoutId, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDataHolder itemDataHolder, int i) {
        itemDataHolder.btnItem.setText((i+1)+"口");
        if (mOnItemClickListener != null) {
            itemDataHolder.btnItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = itemDataHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(itemDataHolder.btnItem, pos);
                    //ToastUtils.success("now click position="+pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return totalSize;
    }

    class ItemDataHolder extends RecyclerView.ViewHolder {
        CheckBox btnItem;

        public ItemDataHolder(@NonNull View itemView) {
            super(itemView);
            btnItem = itemView.findViewById(R.id.btnItem);
        }
    }
}




