package com.atisz.appface.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.atisz.appface.AppContext;
import com.atisz.appface.R;
import com.atisz.appface.config.Constant;
import com.atisz.appface.config.IntentFlag;
import com.atisz.appface.entity.QQUserEntity;
import com.atisz.appface.entity.UserEntity;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.utils.JsonHelper;
import com.atisz.appface.utils.RegexUtils;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by wuwei on 2019/1/25.
 */

public class LoginActivity extends MyBaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    @BindView(R.id.login_title)
    QMUITopBar mLoginTitle;
    @BindView(R.id.tv_user_name)
    AutoCompleteTextView mTvUserName;
    @BindView(R.id.input_user_name)
    TextInputLayout mInputUserName;
    @BindView(R.id.tv_password)
    EditText mTvPassword;
    @BindView(R.id.input_password)
    TextInputLayout mInputPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_forgot_password)
    Button mBtnForgotPassword;
    @BindView(R.id.btn_forgot_register)
    Button mBtnForgotRegister;
    @BindView(R.id.email_login_form)
    LinearLayout mEmailLoginForm;
    @BindView(R.id.form_login)
    ScrollView mFormLogin;
    @BindView(R.id.iv_wechatregist)
    ImageView mIvWechatregist;
    @BindView(R.id.iv_micblogregist)
    ImageView mIvMicblogregist;
    @BindView(R.id.iv_qqregist)
    ImageView mIvQqregist;
    private Tencent mTencent;
    private UserInfo mUserInfo;
    private QQUserEntity mQQuserEntity;


    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mLoginTitle.setTitle("注册");
        mLoginTitle.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTencent = AppContext.getTencent();
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
        mBtnForgotPassword.setOnClickListener(this);
        mBtnForgotRegister.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mTvPassword.setOnEditorActionListener(this);
        mIvWechatregist.setOnClickListener(this);
        mIvQqregist.setOnClickListener(this);
        mIvMicblogregist.setOnClickListener(this);
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            super.doComplete(values);
            initOpenidAndToken(values);
            getUserInformation(values);

        }
    };

    private void getUserInformation(JSONObject values) {
        mUserInfo = new UserInfo(this, AppContext.getTencent().getQQToken());
        mUserInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                alert("登陆成功");
                final JSONObject jsonObject = (JSONObject) o;
                final String jsonString = jsonObject.toString();
                mQQuserEntity = JsonHelper.fromJSONObject(jsonString, QQUserEntity.class);
                startUserActivity("QQLand",IntentFlag.QQ_token,mQQuserEntity);
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                attemptLogin();
                break;
            case R.id.btn_forgot_password:
                Snackbar.make(v, "Oh oh !", Snackbar.LENGTH_LONG)
                        .setAction("^_^", null).show();
                break;
            case R.id.btn_forgot_register:
                Snackbar.make(v, "Oh oh !", Snackbar.LENGTH_LONG)
                        .setAction("^_^", null).show();
                break;
            case R.id.iv_wechatregist:
                Snackbar.make(v, "Oh oh !", Snackbar.LENGTH_LONG)
                        .setAction("^_^", null).show();
                break;
            case R.id.iv_qqregist:
                QQLogin();
                break;
            case R.id.iv_micblogregist:
                Snackbar.make(v, "Oh oh !", Snackbar.LENGTH_LONG)
                        .setAction("^_^", null).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void QQLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
        }
    }

    /**
     * 编辑完之后点击软键盘上的回车键触发
     *
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == R.id.btn_login || actionId == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    /**
     * 注册
     */
    private void attemptLogin() {
        mInputUserName.setError(null);
        mInputPassword.setError(null);

        final String username = mTvUserName.getText().toString();
        final String password = mTvPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            mInputUserName.setError(getString(R.string.error_no_name));
            focusView = mInputUserName;
            cancel = true;
        } else if (!RegexUtils.isMobileExact(username) && !RegexUtils.isEmail(username)) {
            mInputUserName.setError(getString(R.string.error_invalid_name));
            focusView = mInputUserName;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mInputPassword.setError(getString(R.string.error_no_password));
            focusView = mInputPassword;
            cancel = true;
        } else if (!(password.length() > 4 && password.length() < 20)) {
            mInputPassword.setError(getString(R.string.error_invalid_password));
            focusView = mInputPassword;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress("正在登陆");
            bmobLogin(username, password);
        }
    }

    /**
     * 账号密码登陆
     *
     * @param username
     * @param password
     */
    private void bmobLogin(String username, String password) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.login(new SaveListener<UserEntity>() {
            @Override
            public void done(UserEntity userEntity, BmobException e) {
                if (e == null) {
                    startUserActivity("bmobLand", IntentFlag.bmob_token, userEntity);
                } else {
                    alert("登陆失败");
                }
                dismissProgress();
            }
        });
    }

    private void startUserActivity(String method, String key, Serializable value) {
        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
        //登陆方式
        if (method.equals("bmobLand")){
            intent.putExtra(IntentFlag.land_method, method);
            intent.putExtra(key, value);
        } else if (method.equals("QQLand")){
            intent.putExtra(IntentFlag.land_method, method);
            intent.putExtra(key, value);
        }

        startActivity(intent);
        finish();
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            alert("QQ登陆完成");
            doComplete((JSONObject) o);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError uiError) {
            alert("QQ登陆错误");
        }

        @Override
        public void onCancel() {
            alert("QQ登陆取消");
        }
    }
}
