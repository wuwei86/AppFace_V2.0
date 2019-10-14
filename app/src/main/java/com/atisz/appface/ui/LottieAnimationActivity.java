package com.atisz.appface.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.atisz.appface.R;
import com.atisz.appface.ui.base.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wuwei
 * @date 2019/2/24
 */

public class LottieAnimationActivity extends MyBaseActivity {
    @BindView(R.id.animation_view)
    LottieAnimationView mAnimationView;
    @BindView(R.id.tv_lottie)
    TextView mTvLottie;
    @BindView(R.id.tb_lottie)
    Toolbar mTbLottie;
    @BindView(R.id.abl_lottie)
    AppBarLayout mAblLottie;

    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_lottie);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert("点击");
            }
        });
        initToolBar();
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

    }


    private void initToolBar() {
        mTbLottie.setTitle("");
        mTvLottie.setText("动画");
        mTbLottie.setNavigationIcon(R.mipmap.ic_back);
        mTbLottie.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
