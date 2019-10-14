package com.atisz.appface.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.adapter.base.BaseListAdapter;
import com.atisz.appface.entity.CardListEntity;

import java.util.List;

import butterknife.BindView;

/**
 * @author wuwei
 * @date 2018/10/19
 */

public class CardManagerAdapter extends BaseListAdapter {


    public CardManagerAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    @Override
    protected View createView(int i, View view, ViewGroup viewGroup) {
        return inflater.inflate(R.layout.card_manager_list_item, viewGroup, false);
    }

    @Override
    protected ViewHolder createViewHolder(int position, View view) {
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends ViewHolder {

        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_cardnum)
        TextView mTvCardnum;
        @BindView(R.id.tv_time)
        TextView mTvTime;

        public MyViewHolder(View view) {
            super(view);
        }

        @Override
        public void refresh(Context context, Object item, View view) {
            CardListEntity data = (CardListEntity) item;
            mTvName.setText(data.getCardName());
            mTvCardnum.setText(data.getCardNum());
            mTvTime.setText(data.getCreateTime());
        }
    }
}
