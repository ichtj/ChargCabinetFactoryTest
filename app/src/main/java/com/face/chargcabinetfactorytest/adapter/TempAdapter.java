package com.face.chargcabinetfactorytest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.face.chargcabinetfactorytest.OnItemClickListener;
import com.face.chargcabinetfactorytest.R;
import com.face.chargcabinetfactorytest.entity.TempBean;

import java.util.List;

public class TempAdapter extends RecyclerView.Adapter<TempAdapter.ItemDataHolder> {
    private Context mContext;
    private List<TempBean> tempBeanList;
    private final LayoutInflater mLayoutInflater;
    private int nowPos=0;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClick(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public TempAdapter(Context mContext, List<TempBean> tempBeanList) {
        this.mContext = mContext;
        this.tempBeanList = tempBeanList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void updatePos(int pos,int temp){
        for (int i = 0; i < tempBeanList.size(); i++) {
            if(pos==tempBeanList.get(i).pos){
                tempBeanList.get(i).temp=temp;
                break;
            }
        }
    }

    @NonNull
    @Override
    public ItemDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutId = R.layout.item_temp_base;
        return new ItemDataHolder(mLayoutInflater.inflate(layoutId, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDataHolder itemDataHolder, int i) {
        itemDataHolder.btnItem.setText((tempBeanList.get(i).pos+1)+"口 \n温度："+tempBeanList.get(i).temp+"℃");
        if (mOnItemClickListener != null) {
            itemDataHolder.btnItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = itemDataHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(itemDataHolder.btnItem, pos);
                    //ToastUtils.success("now click position="+pos);
                    nowPos=pos;
                    notifyDataSetChanged();
                }
            });
        }

        if(nowPos==tempBeanList.get(i).pos){
            itemDataHolder.btnItem.setChecked(true);
        }else{
            itemDataHolder.btnItem.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return tempBeanList.size();
    }

    class ItemDataHolder extends RecyclerView.ViewHolder {
        RadioButton btnItem;

        public ItemDataHolder(@NonNull View itemView) {
            super(itemView);
            btnItem = itemView.findViewById(R.id.btnItem);
        }
    }
}




