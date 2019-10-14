package com.atisz.appface.ui.fragmenttab;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.ui.base.MyBaseFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wuwei on 2019/2/23.
 */

public class Tab1Pager extends MyBaseFragment {
    @BindView(R.id.title_fragment)
    QMUITopBar mTitleFragment;
    @BindView(R.id.tv_fragmenttab)
    TextView mTvFragmenttab;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragementtab, container, false);
    }

    @Override
    protected void initData() {
        mTitleFragment.setTitle("第一页");
        mTitleFragment.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        mTvFragmenttab.setText("第一页fragment");
    }

    @Override
    protected void initOperation() {

    }

    /*@Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragementtab, null);
        initView(layout);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    private void initView(View layout) {
        mTitleFragment.setTitle("第一页");
        mTitleFragment.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mTvFragmenttab.setText("第一页fragment");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/
}
