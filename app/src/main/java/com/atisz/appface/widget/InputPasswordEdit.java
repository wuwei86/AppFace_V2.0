package com.atisz.appface.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.Toast;

import com.atisz.appface.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LaoWu on 2018/4/8.
 * 封装密码输入控件、
 * 一个EditText,两个图片，分别是删除输入密码、控制密码是否可见
 * 封装了限制密码最大输入位数
 * 封装了限制密码不能输入特殊字符
 * 可以使用android:inputType属性定义密码输入类型
 */

public class InputPasswordEdit extends BaseInputEdit implements TextWatcher {
    private int maxPasswordLength = 10;     //默认密码最长为10位

    private boolean isShowPassword = false;
    private String oldInputContent;

    public InputPasswordEdit(Context context) {
        super(context);
    }

    public InputPasswordEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputPasswordEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputPasswordEdit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initView(Context context, AttributeSet attributeSet) {
        super.initView(context, attributeSet);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.InputPasswordEdit);
        maxPasswordLength = typedArray.getInteger(R.styleable.InputPasswordEdit_password_max_length, 10);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
        if (editable != null && editable.length() > maxPasswordLength) {
            Toast.makeText(getContext(), "密码最多" + maxPasswordLength + "位", Toast.LENGTH_SHORT).show();
            if (oldInputContent != null) {
                editable.replace(0, editable.length(), oldInputContent);
            }
        }
        checkSpecificChar(editable);
    }

    private void checkSpecificChar(Editable editable) {
        String limitStr = "[.`~!#$%^&*()+=|{}':;',\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
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
