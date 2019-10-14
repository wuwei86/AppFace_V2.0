package com.atisz.appface.ui.fragmenttab;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atisz.appface.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wuwei on 2019/2/23.
 */

public class Tab4Pager extends Fragment{
    @BindView(R.id.tv_fragmenttab)
    TextView mTvFragmenttab;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragementtab, null);
        unbinder = ButterKnife.bind(this, layout);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        mTvFragmenttab.setText("第四页fragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
