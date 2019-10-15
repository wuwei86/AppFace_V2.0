package com.atisz.appface.tencent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.atisz.appface.config.BroadcastFlag;
import com.atisz.appface.config.IntentFlag;
import com.atisz.appface.entity.QQUserEntity;
import com.atisz.appface.utils.JsonHelper;
import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author asus
 */
public class BaseUIListener implements IUiListener {
	private Context mContext;
	private String mScope;
	private boolean mIsCaneled;
	private boolean ismIsCaneled = true;

	
	public BaseUIListener(Context mContext) {
		super();
		this.mContext = mContext;
	}

	
	public BaseUIListener(Context mContext, String mScope) {
		super();
		this.mContext = mContext;
		this.mScope = mScope;
	}
	
	public void cancel() {
		mIsCaneled = true;
	}


	@Override
	public void onComplete(Object response) {
		if (mIsCaneled){
			return;
		}
		final JSONObject jsonObject = (JSONObject) response;
		final String jsonString = jsonObject.toString();
		final QQUserEntity QQuserEntity = JsonHelper.fromJSONObject(jsonString, QQUserEntity.class);
		Intent intent = new Intent();
		intent.setAction(BroadcastFlag.QQLandComplete);
		intent.putExtra(IntentFlag.QQ_token, QQuserEntity);
		mContext.sendBroadcast(intent);
	}

	@Override
	public void onError(UiError e) {
		if (mIsCaneled){
			return;
		}
		Intent intent = new Intent();
		intent.setAction(BroadcastFlag.QQLandError);
		intent.putExtra(IntentFlag.QQ_token, (Serializable)e);
		mContext.sendBroadcast(intent);
	}

	@Override
	public void onCancel() {
		if (mIsCaneled) {
			return;
		}
		Intent intent = new Intent();
		intent.setAction(BroadcastFlag.QQLandCancle);
		mContext.sendBroadcast(intent);
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

}
