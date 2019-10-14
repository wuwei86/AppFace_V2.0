package com.atisz.appface.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.atisz.appface.R;
import com.atisz.appface.adapter.JpFragmentAdapter;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.ui.fragmenttab.Tab1Pager;
import com.atisz.appface.ui.fragmenttab.Tab2Pager;
import com.atisz.appface.ui.fragmenttab.Tab3Pager;
import com.atisz.appface.ui.fragmenttab.Tab4Pager;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wuwei
 * @date 2019/2/23
 */

public class JpTabBarActivity extends MyBaseActivity implements OnTabSelectListener {
    @BindView(R.id.vp_jptabbar)
    ViewPager mVpJptabbar;
    @BindView(R.id.jtb_jptabbar)
    JPTabBar mJtbJptabbar;

    private Tab1Pager mTab1;

    private Tab2Pager mTab2;

    private Tab3Pager mTab3;

    private Tab4Pager mTab4;

    private List<Fragment> list = new ArrayList<>();


    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_jptabbar);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {

        mJtbJptabbar.setTitles("徽章测试", "动画类型", "特殊设置", "调试区域")
                .setNormalIcons(R.mipmap.tab1_normal, R.mipmap.tab2_normal, R.mipmap.tab3_normal, R.mipmap.tab4_normal)
                .setSelectedIcons(R.mipmap.tab1_selected, R.mipmap.tab2_selected, R.mipmap.tab3_selected, R.mipmap.tab4_selected).generate();

        mJtbJptabbar.setTabTypeFace("fonts/Jaden.ttf");
        mTab1 = new Tab1Pager();
        mTab2 = new Tab2Pager();
        mTab3 = new Tab3Pager();
        mTab4 = new Tab4Pager();

        mJtbJptabbar.setGradientEnable(true);
        mJtbJptabbar.setPageAnimateEnable(true);
        mJtbJptabbar.setTabListener(this);
        list.add(mTab1);
        list.add(mTab2);
        list.add(mTab3);
        list.add(mTab4);

        mVpJptabbar.setAdapter(new JpFragmentAdapter(getSupportFragmentManager(),list));

        mJtbJptabbar.setContainer(mVpJptabbar);

        if(mJtbJptabbar.getMiddleView()!=null){
            mJtbJptabbar.getMiddleView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert("中间按钮被点击");
                }
            });
        }

    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

    }


    @Override
    public void onTabSelect(int index) {
//        alert("点击");
    }

    @Override
    public boolean onInterruptSelect(int index) {
        return false;
    }
}
