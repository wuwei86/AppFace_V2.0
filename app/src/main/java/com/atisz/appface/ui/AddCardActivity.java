package com.atisz.appface.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atisz.appface.AppContext;
import com.atisz.appface.R;
import com.atisz.appface.config.IntentFlag;
import com.atisz.appface.entity.CardListEntity;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.utils.DbUtil;
import com.atisz.appface.utils.SystemUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author wuwei
 * @date 2018/10/19
 */

public class AddCardActivity extends MyBaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView mIvTitleBack;
    @BindView(R.id.tv_title_text)
    TextView mTvTitleText;
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.tvl_name)
    TextInputLayout mTvlName;
    @BindView(R.id.tvl_cardnum)
    TextInputLayout mTvlCardnum;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;

    private EditText mTvlNameEditText;
    private EditText mTvlCardnumEditText;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void initView(Bundle saveInstanceState) {
        isAnimation = true;
        setContentView(R.layout.activity_addcard);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mTvTitleText.setText("录卡");
        mIvMenu.setVisibility(View.INVISIBLE);

        mTvlNameEditText = mTvlName.getEditText();
        mTvlCardnumEditText = mTvlCardnum.getEditText();

        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        if (mAdapter == null) {
            alert(getString(R.string.nfc_msg));
            return;
        }
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
        mIvTitleBack.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);

        mTvlNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    mTvlName.setErrorEnabled(false);
                }
            }
        });
        mTvlCardnumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    mTvlCardnum.setErrorEnabled(false);
                }
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        final String stringByByte = SystemUtil.getStringByByte(tag.getId());
//        Toast.makeText(getApplicationContext(), stringByByte, Toast.LENGTH_SHORT).show();
        mTvlCardnumEditText.setText(stringByByte);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_confirm:
                addCard();
                break;
            default:
                break;
        }
    }

    private void addCard() {
        if (mTvlNameEditText.getText().toString().trim().isEmpty()) {
            mTvlName.setError("姓名不能为空");
            return;
        } else {
            mTvlName.setErrorEnabled(false);
        }

        if (mTvlCardnumEditText.getText().toString().trim().isEmpty()) {
            mTvlCardnum.setError("卡号不能为空");
            return;
        } else {
            mTvlCardnum.setErrorEnabled(false);
        }

        //增加卡，写本地数据库，上传数据到服务器
        final CardListEntity cardListEntity = new CardListEntity();
        cardListEntity.setCardNum(mTvlCardnumEditText.getText().toString().trim());
        cardListEntity.setCardName(mTvlNameEditText.getText().toString().trim());
        cardListEntity.setCreateTime(SystemUtil.getSystemTime());

        final int save = DbUtil.cardDbSave(cardListEntity);
        if (save == 3) {
            alert("请重新输入名字");
        }  else {
            cardListEntity.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        alert("上传数据失败");
                    }
                }
            });
        }
    }
}
