package com.atisz.appface.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.atisz.appface.AppContext;
import com.atisz.appface.R;
import com.atisz.appface.config.BroadcastFlag;
import com.atisz.appface.config.IntentFlag;
import com.atisz.appface.entity.QQUserEntity;
import com.atisz.appface.entity.UserEntity;
import com.atisz.appface.tencent.BaseUIListener;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.utils.JsonHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuwei on 2019/2/13.
 */

public class UserActivity extends MyBaseActivity {

    @BindView(R.id.user_title)
    QMUITopBar mUserTitle;
    @BindView(R.id.userinfor_groupListView)
    QMUIGroupListView mUserinforGroupListView;
    private UserEntity mUserEntity;
    private QQLoginBroadcastReceiver mQqLoginBroadcastReceiver;
    private QQUserEntity mQQuserEntity;
    private String mLandmethod;


    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_user);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mUserTitle.setTitle("用户信息");
        mUserTitle.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initBroadCast();
        mLandmethod = getIntent().getStringExtra(IntentFlag.land_method);
        if (mLandmethod.equals("bmobLand")) {
            mUserEntity = (UserEntity) getIntent().getSerializableExtra(IntentFlag.bmob_token);
            initGroupListView(mLandmethod);
        } else if (mLandmethod.equals("QQLand")) {
            mQQuserEntity = (QQUserEntity) getIntent().getSerializableExtra(IntentFlag.QQ_token);
            initGroupListView(mLandmethod);
        }


    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
//        initGroupListView(mLandmethod);
    }


    private void initGroupListView(String value) {

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {

                }
            }
        };

        if (value.equals("bmobLand")) {
            QMUICommonListItemView userName = mUserinforGroupListView.createItemView("注册名");
            userName.setDetailText(mUserEntity.getUsername());

            QMUIGroupListView.newSection(mActivity)
                    .addItemView(userName, onClickListener)
                    .addTo(mUserinforGroupListView);
        } else if (value.equals("QQLand")) {
            QMUICommonListItemView userName = mUserinforGroupListView.createItemView("昵称");
            userName.setDetailText(mQQuserEntity.getNickname());
            QMUICommonListItemView sex = mUserinforGroupListView.createItemView("性别");
            sex.setDetailText(mQQuserEntity.getGender());
            QMUICommonListItemView year = mUserinforGroupListView.createItemView("年");
            year.setDetailText(mQQuserEntity.getYear());



            QMUIGroupListView.newSection(mActivity)
                    .addItemView(userName, onClickListener)
                    .addItemView(sex, onClickListener)
                    .addItemView(year, onClickListener)
                    .addTo(mUserinforGroupListView);
        }
    }

    private void initBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastFlag.QQLandComplete);
        intentFilter.addAction(BroadcastFlag.QQLandCancle);
        intentFilter.addAction(BroadcastFlag.QQLandError);
        mQqLoginBroadcastReceiver = new QQLoginBroadcastReceiver();
        registerReceiver(mQqLoginBroadcastReceiver, intentFilter);
    }

    private class QQLoginBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadcastFlag.QQLandComplete.equals(intent.getAction())) {
                mQQuserEntity = (QQUserEntity) intent.getSerializableExtra(IntentFlag.QQ_token);
                final String nickname = mQQuserEntity.getNickname();
            } else if (BroadcastFlag.QQLandCancle.equals(intent.getAction())) {

            } else if (BroadcastFlag.QQLandError.equals(intent.getAction())) {

            }
        }
    }
}
