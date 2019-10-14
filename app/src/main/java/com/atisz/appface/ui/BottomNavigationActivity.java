package com.atisz.appface.ui;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.atisz.appface.R;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.widget.BottomNavigationViewHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuwei on 2019/2/22.
 */

public class BottomNavigationActivity extends MyBaseActivity {
    @BindView(R.id.view_pager_bottom_navigation)
    ViewPager mViewPagerBottomNavigation;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;
    @BindView(R.id.activity_bottom_navigation)
    LinearLayout mActivityBottomNavigation;
    private ArrayList<View> mViewList;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(Bundle saveInstanceState) {
        setContentView(R.layout.activity_bottom_navigation);

        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.argb(33, 0, 0, 0));
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        initView();
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

    }

    private void initView() {
        View view1 = getLayoutInflater().inflate(R.layout.item_view_pager_1, null);
        View view2 = getLayoutInflater().inflate(R.layout.item_view_pager_2, null);
        View view3 = getLayoutInflater().inflate(R.layout.item_view_pager_3, null);
        View view4 = getLayoutInflater().inflate(R.layout.item_view_pager_4, null);

        mViewList = new ArrayList<>();
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mViewList.add(view4);

        mViewPagerBottomNavigation.setAdapter(pagerAdapter);
        mViewPagerBottomNavigation.addOnPageChangeListener(mPageChangeListener);

        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigation);
    }


    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            ArgbEvaluator evaluator = new ArgbEvaluator();
            int evaluate = getResources().getColor(R.color.app_blue);
            if (position == 0) {
                evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.app_blue), getResources().getColor(R.color.app_green));
            } else if (position == 1) {
                evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.app_green), getResources().getColor(R.color.app_yellow));
            } else if (position == 2) {
                evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.app_yellow), getResources().getColor(R.color.app_red));
            } else {
                evaluate = getResources().getColor(R.color.app_red);
            }
            ((View) mViewPagerBottomNavigation.getParent()).setBackgroundColor(evaluate);
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mBottomNavigation.setSelectedItemId(R.id.bottom_navigation_blue);
                    break;
                case 1:
                    mBottomNavigation.setSelectedItemId(R.id.bottom_navigation_green);
                    break;
                case 2:
                    mBottomNavigation.setSelectedItemId(R.id.bottom_navigation_yellow);
                    break;
                case 3:
                    mBottomNavigation.setSelectedItemId(R.id.bottom_navigation_red);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_blue:
                    mViewPagerBottomNavigation.setCurrentItem(0);
                    return true;
                case R.id.bottom_navigation_green:
                    mViewPagerBottomNavigation.setCurrentItem(1);
                    return true;
                case R.id.bottom_navigation_yellow:
                    mViewPagerBottomNavigation.setCurrentItem(2);
                    return true;
                case R.id.bottom_navigation_red:
                    mViewPagerBottomNavigation.setCurrentItem(3);
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }
    };

}
