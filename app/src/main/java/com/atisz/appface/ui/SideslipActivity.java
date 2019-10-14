package com.atisz.appface.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.ui.base.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wuwei
 * @date 2019/2/28
 */

public class SideslipActivity extends MyBaseActivity {
    @BindView(R.id.tb_sideslip)
    Toolbar mTbSideslip;
    @BindView(R.id.dl_sideslip)
    DrawerLayout mDlSideslip;
    @BindView(R.id.tv_sideslip)
    TextView mTvSideslip;
    @BindView(R.id.abl_sideslip)
    AppBarLayout mAblSideslip;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_sideslip);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        initToolBar();
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

    }

    private void initToolBar() {
        //导航图标默认自带
        mTbSideslip.setTitle("");
        mTvSideslip.setText("侧滑栏");
        //实现了监听的开关 ，最后2个参数可以写0
        toggle = new ActionBarDrawerToggle(this, mDlSideslip, mTbSideslip, R.string.app_name, R.string.app_name);
        toggle.syncState();//同步drawerLayout

        //给drawerlayout添加监听
        //方法一：
        mDlSideslip.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        toggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
