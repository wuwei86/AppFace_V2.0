package com.atisz.appface.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.Toast;

/**
 * Created by LaoWu on 2018/4/8.
 */

public class InputPhoneEdit extends BaseInputEdit {
    protected static final int MAX_PHONE_NUM = 11;
    private String oldInputContent;

    public InputPhoneEdit(Context context) {
        super(context);
    }

    public InputPhoneEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputPhoneEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputPhoneEdit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initView(Context context, AttributeSet attributeSet) {
        super.initView(context, attributeSet);
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        super.afterTextChanged(editable);
        if (editable != null && editable.length() > MAX_PHONE_NUM) {
            Toast.makeText(getContext(), "手机号不得大于" + MAX_PHONE_NUM + "位", Toast.LENGTH_SHORT).show();
            if (oldInputContent != null) {
                editable.replace(0, editable.length(), oldInputContent);
            }
        } else if (editable.length() == MAX_PHONE_NUM) {
            if (!checkPhoneNumbe(editable.toString())){
                Toast.makeText(getContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            }
        }
        oldInputContent = editable.toString();
    }

    private boolean checkPhoneNumbe(String phone) {
        return phone.matches("^(13|14|15|16|17|18|19)\\d{9}$");
    }
}
