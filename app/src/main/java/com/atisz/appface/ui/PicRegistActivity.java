package com.atisz.appface.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;

import com.atisz.appface.R;
import com.atisz.appface.faceserver.FaceServer;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.uioperation.PictureSelect;
import com.atisz.appface.utils.BmobDBUtils;
import com.atisz.appface.utils.FileUtil;
import com.atisz.appface.utils.GlideUtils;
import com.atisz.appface.utils.ImageUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by wuwei on 2018/11/6.
 */

public class PicRegistActivity extends MyBaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_picture)
    ImageView mIvPic;
    @BindView(R.id.bt_register)
    QMUIRoundButton mBtRegister;
    @BindView(R.id.title)
    QMUITopBar mTitle;
    private String mPicturePath = null;
    private String mName = null;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void initView(Bundle saveInstanceState) {
        isAnimation = true;
        setContentView(R.layout.activity_picregist);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mTitle.setTitle("图片注册");
        FaceServer.getInstance().init(this);
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
        mTitle.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvPic.setOnClickListener(this);
        mBtRegister.setOnClickListener(this);
    }

    private void addPicture() {
        PictureSelect.selectorImage(mActivity,false,true,true,null);
    }

    private void picRegister() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(PicRegistActivity.this);
        builder.setTitle("注册")
                .setPlaceholder("在此输入您的姓名")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            mName = text.toString();
                            dialog.dismiss();
                            showProgress("正在注册中");
                            doRegister(mName);
                        } else {
                            alert("请填入姓名");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void doRegister(final String name) {
        if (name == null || name.equals("")){
            dismissProgress();
            alert("请先输入姓名");
            return;
        }
        if (mPicturePath == null || mPicturePath.equals("")){
            dismissProgress();
            alert("请先选择图片");
            return;
        }
        File dir = new File(mPicturePath);
        if (!dir.exists()){
            dismissProgress();
            alert("文件不存在");
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                Bitmap bitmap = BitmapFactory.decodeFile(mPicturePath);
                if (bitmap == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgress();
                            alert("decodeFile注册失败");
                        }
                    });
                }
                bitmap = ImageUtil.alignBitmapForNv21(bitmap);
                if (bitmap == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgress();
                            alert("alignBitmapForNv21注册失败");
                        }
                    });
                }
                byte[] nv21 = ImageUtil.bitmapToNv21(bitmap, bitmap.getWidth(), bitmap.getHeight());
                boolean success = FaceServer.getInstance().register(PicRegistActivity.this, nv21, bitmap.getWidth(), bitmap.getHeight(),name);
                if (success){
                    BmobDBUtils.upLoadFaceFile(name, FileUtil.getRegisterImage(getApplicationContext())+name+".jpg", FileUtil.getRegisterFeat(getApplicationContext())+name, new UploadBatchListener() {
                        @Override
                        public void onSuccess(List<BmobFile> list, List<String> list1) {
                            if (list1.size() == 2) {
                                BmobDBUtils.savaListFile(list, name, FileUtil.getRegisterImage(getApplicationContext())+name+".jpg",FileUtil.getRegisterFeat(getApplicationContext())+name,new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            alert("注册成功");
                                            dismissProgress();
                                        } else {
                                            alert("注册失败");
                                            dismissProgress();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onProgress(int i, int i1, int i2, int i3) {

                        }

                        @Override
                        public void onError(int i, String s) {
                            dismissProgress();
                            alert("上传文件失败");
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgress();
                            alert("FaceServer.register注册失败");
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_picture:
                addPicture();
                break;
            case R.id.bt_register:

                picRegister();
                break;
            default:
                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
        FaceServer.getInstance().unInit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK){
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() != 0){
                mPicturePath = selectList.get(0).getCompressPath();
                GlideUtils.loadImage(mActivity,mPicturePath,mIvPic);
            }
        }
    }
}
