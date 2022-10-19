package com.face.chargcabinetfactorytest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.face.chargcabinetfactorytest.OnItemClickListener;
import com.face.chargcabinetfactorytest.R;

public class ChargAdapter extends RecyclerView.Adapter<ChargAdapter.ItemDataHolder> {
    private Context mContext;
    private int totalSize;
    private final LayoutInflater mLayoutInflater;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClick(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ChargAdapter(Context mContext, int totalSize) {
        this.mContext = mContext;
        this.totalSize = totalSize;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ItemDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutId = R.layout.item_charg;
        return new ItemDataHolder(mLayoutInflater.inflate(layoutId, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDataHolder itemDataHolder, int i) {
        itemDataHolder.tvItem.setText((i+1)+"");
        if (mOnItemClickListener != null) {
            itemDataHolder.tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = itemDataHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(itemDataHolder.tvItem, pos);
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
        TextView tvItem;

        public ItemDataHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
        }
    }
}




