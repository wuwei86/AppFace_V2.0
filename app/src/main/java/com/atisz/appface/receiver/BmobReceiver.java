package com.atisz.appface.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.atisz.appface.utils.LogUtil;

import cn.bmob.push.PushConstants;

/**
 *
 * @author wuwei
 * @date 2018/10/24
 */

public class BmobReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            LogUtil.i("收到消息"+intent.getStringExtra("msg"));

        }
    }
}
