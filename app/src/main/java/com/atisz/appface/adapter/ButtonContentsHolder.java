package com.atisz.appface.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.atisz.appface.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.util.ArrayList;


/**
 * @author wuwei
 * @date 2019/2/19
 */

public class ButtonContentsHolder extends BaseViewHolder<String> {

    private ArrayList<String> mArrayList = new ArrayList<String>();
    private TextView mTvRvtitle;


    public ButtonContentsHolder(ViewGroup itemView, ArrayList list) {
        super(itemView, R.layout.recyclerview_main);
        mTvRvtitle = $(R.id.tv_rvtitle);
        this.mArrayList = list;
    }

    @Override
    public void setData(String data) {
        super.setData(data);
        final String string = mArrayList.get(getDataPosition());
        mTvRvtitle.setText(string);
    }
}
