package com.atisz.appface.ui;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.atisz.appface.R;
import com.atisz.appface.ui.base.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wuwei
 * @date 2019/2/21
 */

public class FullActivity extends MyBaseActivity {
    @BindView(R.id.vv_fullscreen)
    VideoView mVvFullscreen;
    @BindView(R.id.rl_fullscreen)
    RelativeLayout mRlFullscreen;
    @BindView(R.id.pb_fullscreen)
    ProgressBar mPbFullscreen;

    private boolean isShowBar;

    private final int MESSAGE_HIDE_BARS = 0x001;
    private final int MESSAGE_VIDEO_PREPARED = 0x002;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_HIDE_BARS:
                    hideBars();
                    break;
                case MESSAGE_VIDEO_PREPARED:
                    Animation animation = new AlphaAnimation(1.0f, 0.0f);
                    animation.setDuration(500);
                    mRlFullscreen.startAnimation(animation);
                    mRlFullscreen.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_fullscreen);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.hide();
        }
    }

    @Override
    protected void initData(Bundle saveInstanceState) {

    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

    }

    private void playVideo() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.full_screen_google);
        mVvFullscreen.setVideoURI(uri);
        mVvFullscreen.start();
        mVvFullscreen.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_VIDEO_PREPARED, 500);
            }
        });

        mVvFullscreen.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVvFullscreen.start();
            }
        });

        mVvFullscreen.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mPbFullscreen.setVisibility(View.VISIBLE);
                return true;
            }
        });

        mVvFullscreen.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!isShowBar) {
                        showBars();
                    } else {
                        hideBars();
                    }
                }
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showBars() {
        isShowBar = true;
        mVvFullscreen.setSystemUiVisibility(View.VISIBLE);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.GRAY);
        window.setNavigationBarColor(Color.GRAY);

        mHandler.removeMessages(MESSAGE_HIDE_BARS);
        mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_BARS, 2000);

        /*mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 2000);*/
    }

    private void hideBars() {
        isShowBar = false;
        mVvFullscreen.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRlFullscreen.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBars();
        playVideo();
    }
}
