package com.atisz.appface.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atisz.appface.R;


/**
 * Created by LaoWu on 2018/4/9.
 */

public abstract class BaseInputEdit extends LinearLayout implements TextWatcher, View.OnClickListener {
    protected EditText editText;
    protected ImageView ivCancle;
    protected ImageView ivShowOrHide;

    protected int showOrHideImageViewShowId;
    protected int showOrHideImageViewHideId;

    private boolean isShowPassword = false;

    public BaseInputEdit(Context context) {
        super(context);
        initView(context, null);
    }

    public BaseInputEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public BaseInputEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseInputEdit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public String getEdit() {
        return editText.getText().toString();
    }

    public void setEditText(String text) {
        editText.setText(text == null ? "" : text);
        editText.setSelection(editText.getText().length());
    }

    protected void initView(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.InputPasswordEdit);
        setOrientation(HORIZONTAL);
        int marginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        int paddinPx = marginPx / 2;

        float textSize = typedArray.getDimension(R.styleable.InputPasswordEdit_text_size, 15);
        int textColor = typedArray.getColor(R.styleable.InputPasswordEdit_text_color, 0xFF000000);
        String hintText = typedArray.getString(R.styleable.InputPasswordEdit_hint_text);
        int inputType = 0;
        try {
            String type = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "inputType");
            if (type != null && type.contains("0x") && type.length() > 2) {
                String intType = type.substring(2, type.length());
                inputType = Integer.parseInt(intType, 16);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        editText = new EditText(context);
        if (hintText != null) {
            editText.setHint(hintText);
        }
        editText.setLines(1);
        editText.setSingleLine();
        editText.setBackgroundColor(0x00000000);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        editText.setTextColor(textColor);
        if (inputType != 0) {
            editText.setInputType(inputType);
        }
        LayoutParams etPhoneParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        etPhoneParams.weight = 1;
        etPhoneParams.gravity = Gravity.CENTER_VERTICAL;
        etPhoneParams.setMargins(marginPx, 0, marginPx, 0);
        addView(editText, etPhoneParams);
        editText.addTextChangedListener(this);

        showOrHideImageViewShowId = typedArray.getResourceId(R.styleable.InputPasswordEdit_show_password_image, -1);
        showOrHideImageViewHideId = typedArray.getResourceId(R.styleable.InputPasswordEdit_hide_password_image, -1);
        if (-1 != showOrHideImageViewHideId && -1 != showOrHideImageViewShowId) {
            ivShowOrHide = new ImageView(context);
            ivShowOrHide.setVisibility(GONE);
            LayoutParams ivShowOrHideParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ivShowOrHide.setPadding(paddinPx, 0, paddinPx, 0);
            ivShowOrHide.setImageResource(showOrHideImageViewHideId);
            this.addView(ivShowOrHide, ivShowOrHideParams);
            ivShowOrHide.setOnClickListener(this);
        }

        int cancleId = typedArray.getResourceId(R.styleable.InputPasswordEdit_cancle_image, -1);
        if (-1 != cancleId) {
            ivCancle = new ImageView(context);
            ivCancle.setImageResource(cancleId);
            ivCancle.setVisibility(GONE);
            LayoutParams ivCancleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ivCancleParams.setMargins(paddinPx, 0, marginPx, 0);
            addView(ivCancle, ivCancleParams);
            ivCancle.setOnClickListener(this);
        }
        typedArray.recycle();
    }

    @Override
    public void onClick(View view) {
        if (ivCancle != null && view == ivCancle && editText != null && editText.getText() != null) {
            editText.setText("");
        } else if (ivShowOrHide != null && view == ivShowOrHide) {
            if (isShowPassword) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ivShowOrHide.setImageResource(showOrHideImageViewHideId);
            } else {
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ivShowOrHide.setImageResource(showOrHideImageViewShowId);
            }
            isShowPassword = !isShowPassword;
            editText.setSelection(editText.getText().length());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() != 0) {
            if (ivCancle != null){
                ivCancle.setVisibility(VISIBLE);
            }
            if (ivShowOrHide != null) {
                ivShowOrHide.setVisibility(VISIBLE);
            }
        } else {
            if (ivCancle != null) {
                ivCancle.setVisibility(GONE);
            }
            if (ivShowOrHide != null) {
                ivShowOrHide.setVisibility(GONE);
            }
        }
    }
}
