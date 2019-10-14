package com.atisz.appface.ui;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wuwei
 * @date 2019/2/21
 */

public class ScrollingActivity extends MyBaseActivity {
    @BindView(R.id.iv_scrolling_top)
    ImageView mIvScrollingTop;
    @BindView(R.id.tb_scrolling)
    Toolbar mTbScrolling;
    @BindView(R.id.ctb_scrolling_layout)
    CollapsingToolbarLayout mCtbScrollingLayout;
    @BindView(R.id.abl_scrolling)
    AppBarLayout mAblScrolling;
    @BindView(R.id.tv_scrolling)
    TextView mTvScrolling;
    @BindView(R.id.fab_scrolling)
    FloatingActionButton mFabScrolling;

    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_scrolling);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        initToolBar();
        mFabScrolling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert("点击");
            }
        });

        Glide.with(this).load(R.mipmap.material_design_3).apply(new RequestOptions().fitCenter()).into(mIvScrollingTop);
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

    }

    private void initToolBar() {
        mTbScrolling.setTitle("ScrollingActivity");
        mTbScrolling.setNavigationIcon(R.mipmap.ic_back);
        mTbScrolling.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            mCtbScrollingLayout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.TRANSPARENT));
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
