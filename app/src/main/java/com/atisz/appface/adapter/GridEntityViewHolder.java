package com.atisz.appface.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.entity.GridEntity;
import com.atisz.appface.utils.GlideUtils;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.util.ArrayList;

/**
 *
 * @author wuwei
 * @date 2019/2/21
 */

public class GridEntityViewHolder extends BaseViewHolder<GridEntity> {
    private ArrayList<GridEntity> mArrayList = new ArrayList<GridEntity>();

    private TextView mTvtitle;
    private ImageView mIvsrc;

    public GridEntityViewHolder(ViewGroup parent,ArrayList list) {
        super(parent, R.layout.recyclerview_grid);
        mTvtitle = $(R.id.tv_gridname);
        mIvsrc = $(R.id.iv_grid);
        this.mArrayList = list;
    }

    @Override
    public void setData(GridEntity data) {
        super.setData(data);
        mTvtitle.setText(mArrayList.get(getDataPosition()).getName());
//        GlideUtils.loadImage(getContext(),mArrayList.get(getDataPosition()).getId(),mIvsrc);
        mIvsrc.setImageResource(mArrayList.get(getDataPosition()).getId());
    }
}
