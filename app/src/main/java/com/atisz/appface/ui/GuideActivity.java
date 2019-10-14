package com.atisz.appface.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.ui.base.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

/**
 * Created by wuwei on 2019/1/24.
 */

public class GuideActivity extends MyBaseActivity {
    @BindView(R.id.banner_guide_background)
    BGABanner mBannerGuideBackground;
    @BindView(R.id.banner_guide_foreground)
    BGABanner mBannerGuideForeground;
    @BindView(R.id.tv_guide_skip)
    TextView mTvGuideSkip;
    @BindView(R.id.btn_guide_enter)
    Button mBtnGuideEnter;

    @Override
    protected void initView(Bundle saveInstanceState) {
        hideNavigation();
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {

    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
        mBannerGuideForeground.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter,R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });

        processLogic();
    }

    private void hideNavigation() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = this.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }

        });
    }

    private void processLogic() {
        // Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        // 设置数据源
        mBannerGuideBackground.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.mipmap.uoko_guide_background_1,
                R.mipmap.uoko_guide_background_2,
                R.mipmap.uoko_guide_background_3);

        mBannerGuideForeground.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.mipmap.uoko_guide_foreground_1,
                R.mipmap.uoko_guide_foreground_2,
                R.mipmap.uoko_guide_foreground_3);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mBannerGuideForeground.setBackgroundResource(android.R.color.white);
    }
}
