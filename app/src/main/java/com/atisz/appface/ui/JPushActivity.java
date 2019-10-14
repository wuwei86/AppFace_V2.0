package com.atisz.appface.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.atisz.appface.R;
import com.atisz.appface.config.BroadcastFlag;
import com.atisz.appface.config.IntentFlag;
import com.atisz.appface.eventbus.RxBusUtil;
import com.atisz.appface.ui.base.MyBaseActivity;

import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wuwei on 2019/3/5.
 */

public class JPushActivity extends MyBaseActivity {
    @BindView(R.id.tv_jpush)
    TextView mTvJpush;
    private JPushBroadcastReceiver myBroadcastReceiver;
    private Subscription mObservable;

    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_jpush);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
//        initBroadcast();
        initRxBusUtil();
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
            myBroadcastReceiver = null;
        }

        //注销事件
        if (mObservable.isUnsubscribed()){
            mObservable.unsubscribe();
        }
    }

    private void initRxBusUtil(){
        mObservable = RxBusUtil.getInstance().getObservable(String.class)
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                alert("收到消息onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                alert("收到消息onError");
            }

            @Override
            public void onNext(String s) {
                alert("收到消息onNext");
                mTvJpush.setText(s);
            }
        });
    }

    //注册广播接收极光推送的消息,后期使用eventbus替换
    private void initBroadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastFlag.JPushCustomMessage);
        myBroadcastReceiver = new JPushBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, intentFilter);
    }


    private class JPushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadcastFlag.JPushCustomMessage.equals(intent.getAction())) {
                /*mQQuserEntity = (QQUserEntity) intent.getSerializableExtra(IntentFlag.QQ_token);
                final String nickname = mQQuserEntity.getNickname();*/
                final String stringExtra = intent.getStringExtra(IntentFlag.Custom_Message);
                alert(stringExtra);
                mTvJpush.setText(stringExtra);
            }
        }
    }
}
