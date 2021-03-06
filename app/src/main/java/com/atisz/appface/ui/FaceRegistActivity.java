package com.atisz.appface.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.atisz.appface.R;
import com.atisz.appface.entity.DrawInfo;
import com.atisz.appface.entity.FaceEntity;
import com.atisz.appface.entity.FacePreviewInfo;
import com.atisz.appface.faceserver.CompareResult;
import com.atisz.appface.faceserver.FaceServer;
import com.atisz.appface.ui.base.MyBaseActivity;
import com.atisz.appface.utils.BmobDBUtils;
import com.atisz.appface.utils.ConfigUtil;
import com.atisz.appface.utils.DrawHelper;
import com.atisz.appface.utils.LogUtil;
import com.atisz.appface.utils.SystemUtil;
import com.atisz.appface.utils.camera.CameraHelper;
import com.atisz.appface.utils.camera.CameraListener;
import com.atisz.appface.utils.face.FaceHelper;
import com.atisz.appface.utils.face.FaceListener;
import com.atisz.appface.utils.face.RequestFeatureStatus;
import com.atisz.appface.widget.FaceRectView;
import com.atisz.appface.widget.MyDialog;
import com.atisz.appface.widget.ShowFaceInfoAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuwei on 2018/11/2.
 */

public class FaceRegistActivity extends MyBaseActivity implements View.OnClickListener {

    private static final int MAX_DETECT_NUM = 10;
    private static final int WAIT_LIVENESS_INTERVAL = 50;

    /**
     * 注册人脸状态码，准备注册
     */
    private static final int REGISTER_STATUS_READY = 0;
    /**
     * 注册人脸状态码，注册中
     */
    private static final int REGISTER_STATUS_PROCESSING = 1;
    /**
     * 注册人脸状态码，注册结束（无论成功失败）
     */
    private static final int REGISTER_STATUS_DONE = 2;
    private static final float SIMILAR_THRESHOLD = 0.8F;

    private String mRegisterName = null;

    @BindView(R.id.texture_preview)
    TextureView mTexturePreview;
    @BindView(R.id.face_rect_view)
    FaceRectView mFaceRectView;
    @BindView(R.id.bt_register)
    QMUIRoundButton mBtRegister;
    @BindView(R.id.recycler_view_person)
    RecyclerView mRecyclerViewPerson;

    private CameraHelper mCameraHelper;
    private DrawHelper mDrawHelper;
    private Camera.Size mPreviewSize;

    /**
     * 相机预览显示的控件，可为SurfaceView或TextureView
     */
//    private View mPreviewView;
    /**
     * 绘制人脸框的控件
     */
//    private FaceRectView mFaceRectView;
    private Integer mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private ArrayList<CompareResult> compareResultList;
    private ShowFaceInfoAdapter adapter;
    private FaceEngine mFaceEngine;
    private FaceHelper mFaceHelper;
    private int mAfCode = -1;

    private String IMG_PATH = null;
    private String FEAT_PATH = null;

    private ConcurrentHashMap<Integer, Integer> requestFeatureStatusMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Integer> livenessMap = new ConcurrentHashMap<>();
    private CompositeDisposable getFeatureDelayedDisposables = new CompositeDisposable();

    /**
     * 活体检测的开关
     */
    private boolean livenessDetect = true;

    private int mRegisterStatus = REGISTER_STATUS_DONE;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void initView(Bundle saveInstanceState) {
        isAnimation = true;
        initScreen();

    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        FaceServer.getInstance().init(this);
        IMG_PATH = this.getFilesDir().getAbsolutePath() + File.separator + "register" + File.separator + "imgs" + File.separator;
        FEAT_PATH = this.getFilesDir().getAbsolutePath() + File.separator + "register" + File.separator + "features" + File.separator;

        compareResultList = new ArrayList<>();
        adapter = new ShowFaceInfoAdapter(compareResultList, this);
        mRecyclerViewPerson.setAdapter(adapter);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int spanCount = (int) (dm.widthPixels / (getResources().getDisplayMetrics().density * 100 + 0.5f));
        mRecyclerViewPerson.setLayoutManager(new GridLayoutManager(this, spanCount));
        mRecyclerViewPerson.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initOperation(Bundle saveInstanceState) {
        mBtRegister.setOnClickListener(this);
        initEngine();
        initCamera();
    }

    private void initScreen() {
        hideNavigation();

        setContentView(R.layout.activity_faceregist);
        //保持亮屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Activity启动后就锁定为启动时的方向
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            default:
                break;
        }
    }

    private void hideNavigation() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = this.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }

        });
    }


    /**
     * 初始化引擎
     */
    private void initEngine() {
        mFaceEngine = new FaceEngine();
        mAfCode = mFaceEngine.init(this, FaceEngine.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_LIVENESS);
        VersionInfo versionInfo = new VersionInfo();
        mFaceEngine.getVersion(versionInfo);
        LogUtil.i("initEngine:  init: " + mAfCode + "  version:" + versionInfo);

        if (mAfCode != ErrorInfo.MOK) {
            alert(getString(R.string.init_failed, mAfCode));
        }
    }

    /**
     * 销毁引擎
     */
    private void unInitEngine() {
        if (mAfCode == ErrorInfo.MOK) {
            mAfCode = mFaceEngine.unInit();
            LogUtil.i("unInitEngine: " + mAfCode);
        }
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final FaceListener faceListener = new FaceListener() {
            @Override
            public void onFail(Exception e) {
                LogUtil.e("onFail" + e.getMessage());
            }

            @Override
            public void onFaceFeatureInfoGet(@Nullable final FaceFeature faceFeature, final Integer requestId) {
                if (faceFeature != null) {
                    //不做活体检测的情况，直接搜索
                    if (!livenessDetect) {
//                        searchFace(faceFeature, requestId);
                    }
                    //活体检测通过，搜索特征
                    else if (livenessMap.get(requestId) != null && livenessMap.get(requestId) == LivenessInfo.ALIVE) {
//                        searchFace(faceFeature, requestId);
                    }
                    //活体检测未出结果，延迟100ms再执行该函数
                    else if (livenessMap.get(requestId) != null && livenessMap.get(requestId) == LivenessInfo.UNKNOWN) {
                        getFeatureDelayedDisposables.add(Observable.timer(WAIT_LIVENESS_INTERVAL, TimeUnit.MILLISECONDS)
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) {
                                        onFaceFeatureInfoGet(faceFeature, requestId);
                                    }
                                }));
                    }
                    //活体检测失败
                    else {
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.NOT_ALIVE);
                    }

                }//FR 失败
                else {
                    requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                }
            }
        };

        CameraListener cameraListener = new CameraListener() {

            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                mPreviewSize = camera.getParameters().getPreviewSize();
                mDrawHelper = new DrawHelper(mPreviewSize.width, mPreviewSize.height, mTexturePreview.getWidth(), mTexturePreview.getHeight(), displayOrientation
                        , cameraId, isMirror);

                mFaceHelper = new FaceHelper.Builder()
                        .faceEngine(mFaceEngine)
                        .frThreadNum(MAX_DETECT_NUM)
                        .previewSize(mPreviewSize)
                        .faceListener(faceListener)
                        .currentTrackId(ConfigUtil.getTrackId(FaceRegistActivity.this.getApplicationContext()))
                        .build();
            }

            @Override
            public void onPreview(final byte[] nv21, Camera camera) {
                if (mFaceRectView != null) {
                    mFaceRectView.clearFaceInfo();
                }
                List<FacePreviewInfo> facePreviewInfoList = mFaceHelper.onPreviewFrame(nv21);
                if (facePreviewInfoList != null && mFaceRectView != null && mDrawHelper != null) {
                    List<DrawInfo> drawInfoList = new ArrayList<>();
                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        String name = mFaceHelper.getName(facePreviewInfoList.get(i).getTrackId());
                        drawInfoList.add(new DrawInfo(facePreviewInfoList.get(i).getFaceInfo().getRect(), GenderInfo.UNKNOWN, AgeInfo.UNKNOWN_AGE, LivenessInfo.UNKNOWN,
                                name == null ? String.valueOf(facePreviewInfoList.get(i).getTrackId()) : name));
                    }
                    mDrawHelper.draw(mFaceRectView, drawInfoList);
                }
                if (mRegisterStatus == REGISTER_STATUS_READY && facePreviewInfoList != null && facePreviewInfoList.size() > 0) {
                    mRegisterStatus = REGISTER_STATUS_PROCESSING;
                    Observable.create(new ObservableOnSubscribe<Boolean>() {
                        @Override
                        public void subscribe(ObservableEmitter<Boolean> emitter) {
                            boolean success = FaceServer.getInstance().register(FaceRegistActivity.this, nv21.clone(), mPreviewSize.width, mPreviewSize.height, mRegisterName);//"registered " + mFaceHelper.getCurrentTrackId()
                            emitter.onNext(success);
                        }
                    })
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Boolean success) {
                                    String result = success ? "register success!" : "register failed!";
                                    alert(result);
                                    if (success == true) {
                                        upLoadFile(mRegisterName, IMG_PATH + mRegisterName + ".jpg", FEAT_PATH + mRegisterName);
                                    } else {
                                        dismissProgress();
                                        alert("注册失败");
                                    }
                                    mRegisterStatus = REGISTER_STATUS_DONE;
                                }

                                @Override
                                public void onError(Throwable e) {
                                    alert("register failed!");
                                    dismissProgress();
                                    mRegisterStatus = REGISTER_STATUS_DONE;
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
                clearLeftFace(facePreviewInfoList);

                if (facePreviewInfoList != null && facePreviewInfoList.size() > 0 && mPreviewSize != null) {

                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        if (livenessDetect) {
                            livenessMap.put(facePreviewInfoList.get(i).getTrackId(), facePreviewInfoList.get(i).getLivenessInfo().getLiveness());
                        }
                        /**
                         * 对于每个人脸，若状态为空或者为失败，则请求FR（可根据需要添加其他判断以限制FR次数），
                         * FR回传的人脸特征结果在{@link FaceListener#onFaceFeatureInfoGet(FaceFeature, Integer)}中回传
                         */
                        if (requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId()) == null
                                || requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId()) == RequestFeatureStatus.FAILED) {
                            requestFeatureStatusMap.put(facePreviewInfoList.get(i).getTrackId(), RequestFeatureStatus.SEARCHING);
                            mFaceHelper.requestFaceFeature(nv21, facePreviewInfoList.get(i).getFaceInfo(), mPreviewSize.width, mPreviewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId());
//                            Log.i(TAG, "onPreview: fr start = " + System.currentTimeMillis() + " trackId = " + facePreviewInfoList.get(i).getTrackId());
                        }
                    }
                }
            }

            @Override
            public void onCameraClosed() {

            }

            @Override
            public void onCameraError(Exception e) {

            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (mDrawHelper != null) {
                    mDrawHelper.setCameraDisplayOrientation(displayOrientation);
                }
            }
        };

        mCameraHelper = new CameraHelper.Builder()
                .metrics(metrics)
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(mCameraID != null ? mCameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(mTexturePreview)
                .cameraListener(cameraListener)
                .build();
        mCameraHelper.init();
    }

    private void upLoadFile(final String name, final String imgpath, final String featurepath) {
        final String[] filePaths = new String[2];
        filePaths[1] = imgpath;
        filePaths[0] = featurepath;
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {

                if (list1.size() == filePaths.length) {
                    savaListFile(list, name,imgpath,featurepath);
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
            }

            @Override
            public void onError(int i, String s) {
                alert("失败");
            }
        });
    }

    private void savaListFile(List<BmobFile> list, String registerName,String imgpath, String featurepath) {
        FaceEntity faceEntity = new FaceEntity();
        faceEntity.setName(registerName);
        faceEntity.setCreateTime(SystemUtil.getSystemTime());
        faceEntity.setPhone("13589634569");
        faceEntity.setDataFile(list.get(0));
        faceEntity.setImgFile(list.get(1));
        faceEntity.setLocalimgUrl(imgpath);
        faceEntity.setLocaldataUrl(featurepath);
        faceEntity.setBmobdataUrl(list.get(0).getFileUrl());
        faceEntity.setBmobimgUrl(list.get(1).getFileUrl());
        faceEntity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    alert("成功");
                    dismissProgress();
                } else {
                    alert("失败");
                    dismissProgress();
                }
            }
        });
    }

    /**
     * 删除已经离开的人脸
     *
     * @param facePreviewInfoList 人脸和trackId列表
     */
    private void clearLeftFace(List<FacePreviewInfo> facePreviewInfoList) {
        Set<Integer> keySet = requestFeatureStatusMap.keySet();
        if (compareResultList != null) {
            for (int i = compareResultList.size() - 1; i >= 0; i--) {
                if (!keySet.contains(compareResultList.get(i).getTrackId())) {
                    compareResultList.remove(i);
                    adapter.notifyItemRemoved(i);
                }
            }
        }
        if (facePreviewInfoList == null || facePreviewInfoList.size() == 0) {
            requestFeatureStatusMap.clear();
            livenessMap.clear();
            return;
        }

        for (Integer integer : keySet) {
            boolean contained = false;
            for (FacePreviewInfo facePreviewInfo : facePreviewInfoList) {
                if (facePreviewInfo.getTrackId() == integer) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                requestFeatureStatusMap.remove(integer);
                livenessMap.remove(integer);
            }
        }

    }

    @Override
    protected void onDestroy() {
        if (mCameraHelper != null) {
            mCameraHelper.release();
            mCameraHelper = null;
        }

        //faceHelper中可能会有FR耗时操作仍在执行，加锁防止crash
        if (mFaceHelper != null) {
            synchronized (mFaceHelper) {
                unInitEngine();
            }
            ConfigUtil.setTrackId(this, mFaceHelper.getCurrentTrackId());
            mFaceHelper.release();
        } else {
            unInitEngine();
        }
        if (getFeatureDelayedDisposables != null) {
            getFeatureDelayedDisposables.dispose();
            getFeatureDelayedDisposables.clear();
        }
        FaceServer.getInstance().unInit();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                showEditTextDialog();
                break;
            default:
                break;
        }
    }

    private void showEditTextDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(FaceRegistActivity.this);
        builder.setTitle("注册")
                .setPlaceholder("在此输入您的姓名")
                .setInputType(InputType.TYPE_CLASS_TEXT)

                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
//                        hideNavigation();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            //开始注册
                            if (mRegisterStatus == REGISTER_STATUS_DONE) {
                                mRegisterStatus = REGISTER_STATUS_READY;
                                mRegisterName = text.toString();
                                showProgress("正在处理中");
                            }
                            dialog.dismiss();
//                            hideNavigation();
                        } else {
                            alert("请填入姓名");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void searchFace(final FaceFeature frFace, final Integer requestId) {
        Observable
                .create(new ObservableOnSubscribe<CompareResult>() {
                    @Override
                    public void subscribe(ObservableEmitter<CompareResult> emitter) {
//                        Log.i(TAG, "subscribe: fr search start = " + System.currentTimeMillis() + " trackId = " + requestId);
                        CompareResult compareResult = FaceServer.getInstance().getTopOfFaceLib(frFace);
//                        Log.i(TAG, "subscribe: fr search end = " + System.currentTimeMillis() + " trackId = " + requestId);
                        if (compareResult == null) {
                            emitter.onError(null);
                        } else {
                            emitter.onNext(compareResult);
                        }
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompareResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CompareResult compareResult) {
                        if (compareResult == null || compareResult.getUserName() == null) {
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                            mFaceHelper.addName(requestId, "VISITOR " + requestId);
                            return;
                        }

//                        Log.i(TAG, "onNext: fr search get result  = " + System.currentTimeMillis() + " trackId = " + requestId + "  similar = " + compareResult.getSimilar());
                        if (compareResult.getSimilar() > SIMILAR_THRESHOLD) {
                            boolean isAdded = false;
                            if (compareResultList == null) {
                                requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                                mFaceHelper.addName(requestId, "VISITOR " + requestId);
                                return;
                            }
                            for (CompareResult compareResult1 : compareResultList) {
                                if (compareResult1.getTrackId() == requestId) {
                                    isAdded = true;
                                    break;
                                }
                            }
                            if (!isAdded) {
                                //对于多人脸搜索，假如最大显示数量为 MAX_DETECT_NUM 且有新的人脸进入，则以队列的形式移除
                                if (compareResultList.size() >= MAX_DETECT_NUM) {
                                    compareResultList.remove(0);
                                    adapter.notifyItemRemoved(0);
                                }
                                //添加显示人员时，保存其trackId
                                compareResult.setTrackId(requestId);
                                compareResultList.add(compareResult);
                                adapter.notifyItemInserted(compareResultList.size() - 1);
                            }
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.SUCCEED);
                            mFaceHelper.addName(requestId, compareResult.getUserName());

                        } else {
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                            mFaceHelper.addName(requestId, "VISITOR " + requestId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
