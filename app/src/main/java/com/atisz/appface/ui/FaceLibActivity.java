package com.atisz.appface.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.atisz.appface.R;
import com.atisz.appface.RecyclerViewDivider.DividerItemDecoration;
import com.atisz.appface.adapter.FaceAdapter;
import com.atisz.appface.config.Config;
import com.atisz.appface.entity.FaceEntity;
import com.atisz.appface.entity.FaceLibEntity;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.utils.FileUtil;
import com.atisz.appface.utils.SystemUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author wuwei
 * @date 2018/11/9
 */

public class FaceLibActivity extends MyBaseActivity {
    @BindView(R.id.title)
    QMUITopBar mTitle;
    @BindView(R.id.rv_facelist)
    RecyclerView mRvFacelist;


    private FaceAdapter mFaceAdapter;
    private String mImagePath;
    private String mDataPath;
    public static final String IMG_SUFFIX = ".jpg";
    public static final String SAVE_IMG_DIR = "register" + File.separator + "imgs";
    public static final String SAVE_DATA_DIR = "register" + File.separator + "features";

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private List<FaceLibEntity> mFaceLibEntities;
    private List<FaceEntity> mFaceEntities;

    @Override
    protected void initView(Bundle saveInstanceState) {
        isAnimation = true;
        setContentView(R.layout.activity_facelib);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        mTitle.setTitle("人脸库");
        getLocalFaceLib();
//        getBmobFaceLib();
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
        mTitle.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initFaceRecyclerView(){
        mRvFacelist.setLayoutManager(new LinearLayoutManager(this));
        mRvFacelist.addItemDecoration(new DividerItemDecoration(FaceLibActivity.this, DividerItemDecoration.VERTICAL_LIST));
        mRvFacelist.setItemAnimator(new DefaultItemAnimator());
        mFaceAdapter = new FaceAdapter(this, mFaceLibEntities);
        setFaceAdapterClick();
        mRvFacelist.setAdapter(mFaceAdapter);
    }

    private void setFaceAdapterClick() {
        mFaceAdapter.setOnItemClickListener(new FaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                alert("点击了");
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new QMUIDialog.MessageDialogBuilder(FaceLibActivity.this)
                        .setTitle("删除当前选项")
                        .setMessage("确定要删除吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                deleteLocalFace(position);
                            }
                        })
                        .create(mCurrentDialogStyle).show();
            }
        });
    }

    private void getLocalFaceLib() {
        mFaceLibEntities = new ArrayList<FaceLibEntity>();
        mImagePath = getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + SAVE_IMG_DIR;
        mDataPath = getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + SAVE_DATA_DIR;
        List<String> allFile = SystemUtil.getAllFile(mImagePath, false);
        List<String> fileRealName = SystemUtil.getFileRealName(allFile);
        for (int i = 0; i < fileRealName.size(); i++) {
            final FaceLibEntity faceEntity = new FaceLibEntity();
            faceEntity.setName(fileRealName.get(i));
            faceEntity.setImgUrl(mImagePath + File.separator + fileRealName.get(i) + IMG_SUFFIX);
            faceEntity.setDataUrl(mDataPath + File.separator + fileRealName.get(i));
            mFaceLibEntities.add(faceEntity);
        }

        initFaceRecyclerView();
    }

    private void deleteLocalFace(int index) {
        showProgress("正在删除");
        final FaceLibEntity faceLibEntity = mFaceLibEntities.get(index);
        final String name = faceLibEntity.getName();
        FileUtil.deleteFile(faceLibEntity.getDataUrl());
        FileUtil.deleteFile(faceLibEntity.getImgUrl());

        BmobQuery<FaceEntity> bmobQuery = new BmobQuery();
        bmobQuery.addWhereEqualTo("name",name);
        bmobQuery.findObjects(new FindListener<FaceEntity>() {
            @Override
            public void done(List<FaceEntity> list, BmobException e) {
                if (e == null) {
                    final FaceEntity faceEntity = list.get(0);
                    deletBmobFace(faceEntity);
                } else {
                    dismissProgress();
                    alert("失败");
                }
            }
        });

        mFaceLibEntities.remove(index);
        mFaceAdapter.notifyDataSetChanged();
    }

    private void deletBmobFace(FaceEntity faceEntity) {
        faceEntity.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    dismissProgress();
                } else {
                    dismissProgress();
                }
            }
        });
    }

    private void getBmobFaceLib() {
        mFaceEntities = new ArrayList<FaceEntity>();
        BmobQuery<FaceEntity> bmobQuery = new BmobQuery();
        bmobQuery.findObjects(new FindListener<FaceEntity>() {
            @Override
            public void done(List<FaceEntity> list, BmobException e) {
                if (e == null) {
                    mFaceEntities = list;
                    initFaceRecyclerView();
                    alert("查询成功");
                } else {
                    alert("识别");
                }
            }
        });
    }

    private void deletBmobFace(int index) {
        final FaceEntity faceEntity = mFaceEntities.get(index);
//        final String objectId = faceEntity.getObjectId();
        FileUtil.deleteFile(faceEntity.getLocaldataUrl());
        FileUtil.deleteFile(faceEntity.getLocalimgUrl());
        mFaceLibEntities.remove(index);
        mFaceAdapter.notifyDataSetChanged();

//        faceEntity.setObjectId(objectId);
        faceEntity.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    alert("删除成功");
                } else {
                    alert("删除失败");
                }
            }
        });
    }
}
