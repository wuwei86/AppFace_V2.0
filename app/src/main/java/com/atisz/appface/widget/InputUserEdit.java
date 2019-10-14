package com.atisz.appface.widget;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuwei on 2018/6/19.
 */

public class InputUserEdit extends BaseInputEdit{
    protected static final int MAX_TEXT_NUM = 8;
    private String oldInputContent;

    public InputUserEdit(Context context) {
        super(context);
    }

    public InputUserEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputUserEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputUserEdit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initView(Context context, AttributeSet attributeSet) {
        super.initView(context, attributeSet);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        super.afterTextChanged(editable);

        if (editable != null && editable.length() > MAX_TEXT_NUM){
            Toast.makeText(getContext(),"用户名太长",Toast.LENGTH_SHORT).show();
            if (oldInputContent != null){
                editable.replace(0,editable.length(),oldInputContent);
            }
        }
        checkSpecificChar(editable);
    }

    private void checkSpecificChar(Editable editable) {
        String limitStr = "[.`~!#$%^&*()+=|{}':;',\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、?]";
        Pattern pattern = Pattern.compile(limitStr);
        Matcher matcher = pattern.matcher(editable.toString());
        if (matcher.find()) {
            Toast.makeText(getContext(), "密码中不能含有特殊字符", Toast.LENGTH_SHORT).show();
            if (oldInputContent != null) {
                editable.replace(0, editable.length(), oldInputContent);
            }
        } else {
            oldInputContent = editable.toString();
        }
    }
}
