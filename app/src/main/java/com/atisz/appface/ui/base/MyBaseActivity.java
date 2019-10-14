package com.atisz.appface.ui.base;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.atisz.appface.utils.AnimationUtil;
import com.atisz.appface.utils.ToastUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.ButterKnife;


/**
 * @author wuwei
 */
public abstract class MyBaseActivity extends AppCompatActivity {
    protected Context appContext;
    protected Activity mActivity;
    protected Handler mHandler;
    protected boolean isAnimation = false;
    private QMUITipDialog mTipDialogDialog = null;
//    public SharedPreferences preferences;//用于取缓存
    protected final  int STYLE_LOADING = 0x0001;
    protected final  int STYLE_SUCCESS = 0x0002;
    protected final  int STYLE_FAIL = 0x0003;
    protected final  int STYLE_INFO = 0x0004;

    protected abstract void initView(Bundle saveInstanceState);

    protected abstract void initData(Bundle saveInstanceState);

    protected abstract void initOperation(Bundle saveInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appContext = getApplicationContext();
        mActivity = MyBaseActivity.this;
        mHandler = new Handler();

//        ActivityManager.addActivity(this);

        initView(savedInstanceState);
        ButterKnife.bind(this);
        initData(savedInstanceState);
        initOperation(savedInstanceState);
    }


    protected void showProgress(String msg) {
//        dismissProgress();
        if (mTipDialogDialog == null){
            mTipDialogDialog = new QMUITipDialog.Builder(mActivity)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(msg)
                    .create();
            mTipDialogDialog.show();
        }
    }

    protected void dismissProgress() {
        if (mTipDialogDialog != null && mTipDialogDialog.isShowing()) {
            mTipDialogDialog.dismiss();
            mTipDialogDialog = null;
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (isAnimation) {
            AnimationUtil.setAnimationOfRight(this);
        }
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        if (isAnimation) {
            AnimationUtil.setAnimationOfRight(this);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (isAnimation) {
            AnimationUtil.setAnimationOfRight(this);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (isAnimation) {
            AnimationUtil.setAnimationOfLeft(this);
        }
    }

    public void alertDialog(int style,String msg) {
        QMUITipDialog  tipDialog = null;
        switch (style){
            case STYLE_LOADING:
                tipDialog = new QMUITipDialog.Builder(mActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord(msg)
                        .create();
                break;
            case STYLE_SUCCESS:
                tipDialog = new QMUITipDialog.Builder(mActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(msg)
                        .create();
                break;
            case STYLE_FAIL:
                tipDialog = new QMUITipDialog.Builder(mActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(msg)
                        .create();
                break;
            case STYLE_INFO:
                tipDialog = new QMUITipDialog.Builder(mActivity)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(msg)
                        .create();
                break;
            default:
                break;
        }
        tipDialog.show();
        final QMUITipDialog finalTipDialog = tipDialog;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finalTipDialog.dismiss();
            }
        },1500);
    }

    public void alert(String msg) {
        /*toastUtil = new ToastUtil(this, R.layout.toast_center, msg);
        toastUtil.show();*/
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void alert(int strId) {
       /* toastUtil = new ToastUtil(this, R.layout.toast_center, getString(strId));
        toastUtil.show();*/
        Toast.makeText(this,getString(strId),Toast.LENGTH_SHORT).show();
    }
}
