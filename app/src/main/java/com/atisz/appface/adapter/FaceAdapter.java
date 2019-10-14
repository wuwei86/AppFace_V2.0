package com.atisz.appface.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.entity.FaceEntity;
import com.atisz.appface.entity.FaceLibEntity;
import com.atisz.appface.utils.GlideUtils;

import java.util.List;

/**
 * Created by wuwei on 2018/11/9.
 */

public class FaceAdapter extends RecyclerView.Adapter<FaceAdapter.MyViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<FaceLibEntity> mList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    public FaceAdapter(Context mContext, List<FaceLibEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void removeData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                mContext).inflate(R.layout.face_list_item, parent,
                false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.tv_name.setText(mList.get(position).getName());
        GlideUtils.loadImage(mContext, mList.get(position).getImgUrl(),holder.iv_face);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemLongClick(v, (int) v.getTag());
        }
        return true;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        ImageView iv_face;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_face_name);
            iv_face = (ImageView) view.findViewById(R.id.iv_face);
        }
    }
}
