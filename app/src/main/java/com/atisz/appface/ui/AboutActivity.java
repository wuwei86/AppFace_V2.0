package com.atisz.appface.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wuwei
 * @date 2019/2/28
 */

public class AboutActivity extends MyBaseActivity {
    @BindView(R.id.tv_about)
    TextView mTvAbout;
    @BindView(R.id.tb_about)
    Toolbar mTbAbout;
    @BindView(R.id.abl_about)
    AppBarLayout mAblAbout;
    @BindView(R.id.version_groupListView)
    QMUIGroupListView mVersionGroupListView;
    private String mCurrentVersion;

    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mCurrentVersion = getCurrentVersion(this);
        initToolBar();
        initGroupListView(mCurrentVersion);

    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

    }

    private void initToolBar() {
        mTbAbout.setTitle("");
        mTvAbout.setText("版本");
        mTbAbout.setNavigationIcon(R.mipmap.ic_back);
        mTbAbout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 获取当前版本.
     *
     * @param context 上下文对象
     * @return 返回当前版本
     */
    public String getCurrentVersion(Context context) {
        try {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(this.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            return versionName + "." + versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

    private void initGroupListView(String value) {

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    if ("检查更新".equals(((QMUICommonListItemView) v).getText())){
                        Beta.checkUpgrade();
                    }
                }
            }
        };

        QMUICommonListItemView version = mVersionGroupListView.createItemView("版本号");
        version.setDetailText(value);
        QMUICommonListItemView update = mVersionGroupListView.createItemView("检查更新");


        QMUIGroupListView.newSection(mActivity)
                .addItemView(version, onClickListener)
                .addItemView(update, onClickListener)
                .addTo(mVersionGroupListView);
    }

}
