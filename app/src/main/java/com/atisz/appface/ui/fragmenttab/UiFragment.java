package com.atisz.appface.ui.fragmenttab;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atisz.appface.R;
import com.atisz.appface.ui.base.MyBaseFragment;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author wuwei
 * @date 2019/3/1
 */

public class UiFragment extends MyBaseFragment {
    @BindView(R.id.ui_title)
    QMUITopBar mUiTitle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_ui, container, false);
    }

    @Override
    protected void initData() {
        initTitle();
    }

    @Override
    protected void initOperation() {

    }

    private void initTitle() {
        mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
        mUiTitle.setTitle("UI");
        mUiTitle.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_about_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert("点击事件");
                    }
                });
        mUiTitle.addLeftImageButton(R.mipmap.ic_sideslip, R.id.topbar_left_Sideslip_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                            mDrawerLayout.openDrawer(GravityCompat.START);
                        }
                    }
                });
    }
}
