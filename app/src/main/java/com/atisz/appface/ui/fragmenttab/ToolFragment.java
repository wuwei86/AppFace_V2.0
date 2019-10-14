package com.atisz.appface.ui.fragmenttab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.ui.JPushActivity;
import com.atisz.appface.ui.MapActivity;
import com.atisz.appface.ui.UsbSerialActivity;
import com.atisz.appface.ui.base.MyBaseFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

/**
 * @author wuwei
 * @date 2019/3/1
 */

public class ToolFragment extends MyBaseFragment implements View.OnClickListener {
    @BindView(R.id.tool_title)
    QMUITopBar mToolTitle;
    @BindView(R.id.tv_map)
    TextView mTvMap;
    @BindView(R.id.tv_jpush)
    TextView mTvJpush;
    @BindView(R.id.tv_usbhost)
    TextView mTvUsbhost;
    private DrawerLayout mDrawerLayout;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_tool, container, false);
    }

    @Override
    protected void initData() {
        initTitle();
    }

    @Override
    protected void initOperation() {
        mTvMap.setOnClickListener(this);
        mTvJpush.setOnClickListener(this);
        mTvUsbhost.setOnClickListener(this);
    }

    private void initTitle() {
        mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
        mToolTitle.setTitle("功能");
        mToolTitle.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_about_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert("点击事件");
                    }
                });

        mToolTitle.addLeftImageButton(R.mipmap.ic_sideslip, R.id.topbar_left_Sideslip_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                            mDrawerLayout.openDrawer(GravityCompat.START);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_map:
                startMapActivity();
                break;
            case R.id.tv_jpush:
                startJPushActivity();
                break;
            case R.id.tv_usbhost:
                startUsbHostActivity();
                break;
            default:
                break;
        }
    }

    private void startMapActivity() {
        Intent intent = new Intent(mActivity, MapActivity.class);
        startActivity(intent);
    }

    private void startJPushActivity() {
        Intent intent = new Intent(mActivity, JPushActivity.class);
        startActivity(intent);
    }

    private void startUsbHostActivity() {
        Intent intent = new Intent(mActivity, UsbSerialActivity.class);
        startActivity(intent);
    }

}
