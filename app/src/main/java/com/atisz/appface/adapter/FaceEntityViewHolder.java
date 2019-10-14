package com.atisz.appface.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.entity.FaceEntity;
import com.atisz.appface.utils.GlideUtils;
import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import butterknife.BindView;

/**
 * @author wuwei
 * @date 2019/2/20
 */

public class FaceEntityViewHolder extends BaseViewHolder<FaceEntity> {

    private ImageView mIvFace;
    private TextView mTvFaceName;

    public FaceEntityViewHolder(ViewGroup parent) {
        super(parent, R.layout.face_list_item);
        mIvFace = $(R.id.iv_face);
        mTvFaceName = $(R.id.tv_face_name);
    }

    @Override
    public void setData(FaceEntity data) {
        super.setData(data);
        mTvFaceName.setText(data.getName());
        GlideUtils.loadImage(getContext(),data.getBmobimgUrl(),mIvFace);
    }
}
