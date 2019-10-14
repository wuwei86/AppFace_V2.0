package com.atisz.appface.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by LaoWu on 2018/6/15 0015.
 */

public abstract class MyBaseFragment extends Fragment {
    protected Activity mActivity;
    protected View MyView;
    private ProgressDialog progressDialog;

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract void initData();
    protected abstract void initOperation();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        MyView = initView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, MyView);
        initData();
        initOperation();
        return MyView;
    }

    protected void showProgress(String msg) {
        dismissProgress();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("提示");
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    protected void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void alert(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void alert(int strId) {
        Toast.makeText(getActivity(), strId, Toast.LENGTH_SHORT).show();
    }
}
