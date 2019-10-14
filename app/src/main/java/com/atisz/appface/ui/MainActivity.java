package com.atisz.appface.ui;

import android.Manifest;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.atisz.appface.R;
import com.atisz.appface.adapter.JpFragmentAdapter;
import com.atisz.appface.config.Constant;
import com.atisz.appface.entity.FaceEntity;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.ui.fragmenttab.FaceFragment;
import com.atisz.appface.ui.fragmenttab.MeFragment;
import com.atisz.appface.ui.fragmenttab.ToolFragment;
import com.atisz.appface.ui.fragmenttab.UiFragment;
import com.atisz.appface.utils.ConfigUtil;
import com.atisz.appface.utils.LogUtil;
import com.atisz.appface.utils.PermissionUtils;
import com.atisz.appface.utils.SpUtil;
import com.atisz.appface.utils.SystemUtil;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wuwei
 */
public class MainActivity extends MyBaseActivity implements View.OnClickListener, PermissionUtils.PermissionListener, NavigationView.OnNavigationItemSelectedListener, OnTabSelectListener {

    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.vp_main)
    ViewPager mVpMain;
    @BindView(R.id.jtb_main)
    JPTabBar mJtbMain;

    private FaceFragment mFaceFragment;
    private ToolFragment mToolFragment;
    private UiFragment mUiFragment;
    private MeFragment mMeFragment;
    private List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void initView(Bundle saveInstanceState) {
        isAnimation = true;
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        initFragment();
        checkPermission();
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {

        mNavView.setNavigationItemSelectedListener(this);
        mNavView.setItemIconTintList(null);

        View headerView = mNavView.getHeaderView(0);
        LinearLayout nav_header = headerView.findViewById(R.id.nav_header);
        nav_header.setOnClickListener(this);
    }

    private void initFragment() {
        mJtbMain.setTitles("人脸", "UI", "工具", "我的")
                .setNormalIcons(R.mipmap.ic_face_unselect, R.mipmap.ic_ui_unselect, R.mipmap.ic_tool_unselect, R.mipmap.ic_me_unselect)
                .setSelectedIcons(R.mipmap.ic_face_select, R.mipmap.ic_ui_select, R.mipmap.ic_tool_select, R.mipmap.ic_me_select).generate();

        mJtbMain.setTabTypeFace("fonts/Jaden.ttf");

        mFaceFragment = new FaceFragment();
        mToolFragment = new ToolFragment();
        mUiFragment = new UiFragment();
        mMeFragment = new MeFragment();

        mJtbMain.setGradientEnable(true);
        mJtbMain.setPageAnimateEnable(true);
        mJtbMain.setTabListener(this);
        mFragmentList.add(mFaceFragment);
        mFragmentList.add(mUiFragment);
        mFragmentList.add(mToolFragment);
        mFragmentList.add(mMeFragment);

        mVpMain.setAdapter(new JpFragmentAdapter(getSupportFragmentManager(),mFragmentList));

        mJtbMain.setContainer(mVpMain);

        if(mJtbMain.getMiddleView()!=null){
            mJtbMain.getMiddleView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert("中间按钮被点击");
                }
            });
        }
    }

    private void checkPermission() {
        List<String> requestPermisson = new ArrayList<>();
        requestPermisson.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requestPermisson.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        requestPermisson.add(Manifest.permission.CAMERA);
        requestPermisson.add(Manifest.permission.READ_PHONE_STATE);

        PermissionUtils.requestPermission(MainActivity.this, requestPermisson.toArray(new String[requestPermisson.size()]), this);
    }

    @Override
    public void onGranted() {
        //成功
        activeEngine();
    }

    @Override
    public void onDenied(List<String> deniedPermission) {
        for (int i = 0; i < deniedPermission.size(); i++) {
            if (deniedPermission.get(i).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                PermissionUtils.openSettingActivity(this, "文件存储");
            }

            if (deniedPermission.get(i).equals(Manifest.permission.CAMERA)) {
                PermissionUtils.openSettingActivity(this, "相机");
            }

            if (deniedPermission.get(i).equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                PermissionUtils.openSettingActivity(this, "读取外部存储");
            }

            if (deniedPermission.get(i).equals(Manifest.permission.READ_PHONE_STATE)) {
                PermissionUtils.openSettingActivity(this, "读取状态");
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void activeEngine() {
        if (!SpUtil.getBoolean(getApplicationContext(), Constant.ACTIVE_ARCFACE, false)) {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    FaceEngine faceEngine = new FaceEngine();
                    int activeCode = faceEngine.active(MainActivity.this, Constant.APP_ID, Constant.SDK_KEY);
                    emitter.onNext(activeCode);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer activeCode) {
                            if (activeCode == ErrorInfo.MOK) {
                                SpUtil.putBoolean(getApplicationContext(), Constant.ACTIVE_ARCFACE, true);
                                alert(getString(R.string.active_success));
                            } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                                SpUtil.putBoolean(getApplicationContext(), Constant.ACTIVE_ARCFACE, true);
                                alert(getString(R.string.already_activated));
                            } else {
                                SpUtil.putBoolean(getApplicationContext(), Constant.ACTIVE_ARCFACE, false);
                                alert(getString(R.string.active_failed, activeCode));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            LogUtil.i("已经激活");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header:
                //登陆界面
                startLoginActivity();
//                //测试crash崩溃
//                mDrawerLayout = null;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }





    private void downlaod(String url1, String url2) {
        //文件只能一个一个下载不能同时下载2个
        BmobFile bmobFile = new BmobFile("wuwei.db", "", url1);
        File saveFile = new File(getApplicationContext().getFilesDir().getPath(), bmobFile.getFilename());
        bmobFile.download(saveFile, new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    alert("下载成功");
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });

        BmobFile bmobFile2 = new BmobFile("wuwei2.db", "", url2);
        File saveFile2 = new File(getApplicationContext().getFilesDir().getPath(), bmobFile.getFilename());
        bmobFile2.download(saveFile, new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    alert("下载成功");
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }


    private void savaListFile(List<BmobFile> list) {
        FaceEntity faceEntity = new FaceEntity();
        faceEntity.setName("是的");
        faceEntity.setCreateTime("2018-10-23");
        faceEntity.setPhone(SystemUtil.getSystemTime());
        faceEntity.setDataFile(list.get(0));
        faceEntity.setImgFile(list.get(1));
        faceEntity.setLocaldataUrl(null);
        faceEntity.setLocalimgUrl(null);
        faceEntity.setBmobdataUrl(list.get(0).getFileUrl());
        faceEntity.setBmobimgUrl(list.get(1).getFileUrl());
        faceEntity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    alert("成功");
                } else {
                    alert("失败");
                }
            }
        });
    }

    private void savaFile(BmobFile bmobFile) {
        FaceEntity faceEntity = new FaceEntity();
        faceEntity.setName("吴威");
        faceEntity.setCreateTime("2018-10-23");
        faceEntity.setPhone("13589634569");


        faceEntity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    alert("成功");
                } else {
                    alert("失败");
                }
            }
        });
        /*faceEntity.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    alert("成功");
                } else {
                    alert("失败");
                }
            }
        });*/
    }



    private void startRecyclerViewActivity() {
        Intent intent = new Intent(MainActivity.this, RecyclerViewActivity.class);
        startActivity(intent);
    }

    private void startScrollingActivity() {
        Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
        startActivity(intent);
    }

    private void startFullActivity() {
        Intent intent = new Intent(MainActivity.this, FullActivity.class);
        startActivity(intent);
    }

    private void startBottomNavigationActivity() {
        Intent intent = new Intent(MainActivity.this, BottomNavigationActivity.class);
        startActivity(intent);
    }

    private void startJpTabBarActivity() {
        Intent intent = new Intent(MainActivity.this, JpTabBarActivity.class);
        startActivity(intent);
    }

    private void startSideslipActivity() {
        Intent intent = new Intent(MainActivity.this, SideslipActivity.class);
        startActivity(intent);
    }

    private void startLottieAnimationActivity() {
        Intent intent = new Intent(MainActivity.this, LottieAnimationActivity.class);
        startActivity(intent);
    }

    private void startAboutActivity() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_recycler_and_swipe_refresh:
                startRecyclerViewActivity();
                break;
            case R.id.nav_scrolling:
                startScrollingActivity();
                break;
            case R.id.nav_full_screen:
                startFullActivity();
                break;
            case R.id.nav_bottom_navigation:
                startBottomNavigationActivity();
                break;
            case R.id.nav_settings:
                startJpTabBarActivity();
                break;
            case R.id.nav_sideslip:
                startSideslipActivity();
                break;
            case R.id.nav_about:
                startLottieAnimationActivity();
                break;
            case R.id.nav_donate:
                startAboutActivity();
                break;
            case R.id.nav_my_apps:
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelect(int index) {

    }

    @Override
    public boolean onInterruptSelect(int index) {
        return false;
    }
}
