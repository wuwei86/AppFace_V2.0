package com.atisz.appface.ui.fragmenttab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arcsoft.face.FaceEngine;
import com.atisz.appface.R;
import com.atisz.appface.ui.CardListActivity;
import com.atisz.appface.ui.FaceDetActivity;
import com.atisz.appface.ui.FaceLibActivity;
import com.atisz.appface.ui.FaceRegistActivity;
import com.atisz.appface.ui.MainActivity;
import com.atisz.appface.ui.PicRegistActivity;
import com.atisz.appface.ui.base.MyBaseFragment;
import com.atisz.appface.utils.ConfigUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author wuwei
 * @date 2019/3/1
 */

public class FaceFragment extends MyBaseFragment implements View.OnClickListener {
    @BindView(R.id.face_title)
    QMUITopBar mFaceTitle;
    @BindView(R.id.tv_faceregister)
    TextView mTvFaceregister;
    @BindView(R.id.tv_facedetect)
    TextView mTvFacedetect;
    @BindView(R.id.tv_nfcregister)
    TextView mTvNfcregister;
    @BindView(R.id.tv_facelib)
    TextView mTvFacelib;
    @BindView(R.id.tv_crash)
    TextView mTvCrash;

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private DrawerLayout mDrawerLayout;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_face, container, false);

    }

    @Override
    protected void initData() {
        initTitle();
    }

    @Override
    protected void initOperation() {
        mTvFaceregister.setOnClickListener(this);
        mTvFacedetect.setOnClickListener(this);
        mTvNfcregister.setOnClickListener(this);
        mTvFacelib.setOnClickListener(this);
        mTvCrash.setOnClickListener(this);
    }

    private void initTitle(){
        //获取父activity控件
        mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
        mFaceTitle.setTitle("人脸");
        mFaceTitle.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_about_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert("点击事件");
                    }
                });
        mFaceTitle.addLeftImageButton(R.mipmap.ic_sideslip, R.id.topbar_left_Sideslip_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                            mDrawerLayout.openDrawer(GravityCompat.START);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_faceregister:
                startFaceRegistActivity();
                break;
            case R.id.tv_facedetect:
                startFaceDetActivity();
                break;
            case R.id.tv_nfcregister:
                startNfcActivity();
                break;
            case R.id.tv_facelib:
                startFacelibActivity();
                break;
            case R.id.tv_crash:
                CrashReport.testJavaCrash();
                alert("测试1234");
                break;
            default:
                break;
        }
    }

    private void startFaceRegistActivity() {
        ConfigUtil.setFtOrient(mActivity, FaceEngine.ASF_OP_270_ONLY);

        final String[] items = new String[]{"视频注册", "图片注册"};
        final int checkedIndex = 0;
        new QMUIDialog.CheckableDialogBuilder(mActivity)
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(mActivity, FaceRegistActivity.class);
                            startActivity(intent);
                        } else if (which == 1) {
                            Intent intent = new Intent(mActivity, PicRegistActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void startFaceDetActivity() {
        Intent intent = new Intent(mActivity, FaceDetActivity.class);
        startActivity(intent);
    }

    private void startNfcActivity() {
        Intent intent = new Intent(mActivity, CardListActivity.class);
        startActivity(intent);
    }

    private void startFacelibActivity() {
        Intent intent = new Intent(mActivity, FaceLibActivity.class);
        startActivity(intent);

        /*       //上传多个文件
        final String[] filePaths = new String[2];
        filePaths[0] = getApplicationContext().getDatabasePath("card.db").toString();
        filePaths[1] = getApplicationContext().getDatabasePath("card.db").toString();
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {

                if (list1.size() == filePaths.length) {
                    savaListFile(list);
//                    dismissProgress();
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
//                showProgress("正在上传中");
            }

            @Override
            public void onError(int i, String s) {

            }
        });
//        //上传单个文件
//        String picPath = getApplicationContext().getDatabasePath("card.db").toString();
//        final BmobFile bmobFile = new BmobFile(new File(picPath));
//        bmobFile.upload(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e == null){
//                    //数据关联
//                    savaFile(bmobFile);
//                } else {
//                    alert("上传文件失败");
//                }
//            }
//
//            @Override
//            public void onProgress(Integer value) {
//                super.onProgress(value);
//            }
//        });*/
    }
}
