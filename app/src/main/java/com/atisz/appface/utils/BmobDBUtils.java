package com.atisz.appface.utils;


import com.atisz.appface.entity.FaceEntity;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 *
 * @author wuwei
 * @date 2018/10/23
 */

public class BmobDBUtils {

    public static void upLoadFaceFile(final String name, String imgpath, String featurepath,UploadBatchListener listener){
        final String[] filePaths = new String[2];
        filePaths[1] = imgpath;
        filePaths[0] = featurepath;

        BmobFile.uploadBatch(filePaths, listener);

    }

    public static void savaListFile(List<BmobFile> list, String registerName,String imgpath, String featurepath,SaveListener<String> listener){
        FaceEntity faceEntity = new FaceEntity();
        faceEntity.setName(registerName);
        faceEntity.setCreateTime(SystemUtil.getSystemTime());
        faceEntity.setPhone("13589634569");
        faceEntity.setDataFile(list.get(0));
        faceEntity.setImgFile(list.get(1));
        faceEntity.setLocaldataUrl(featurepath);
        faceEntity.setLocalimgUrl(imgpath);
        faceEntity.setBmobdataUrl(list.get(0).getFileUrl());
        faceEntity.setBmobimgUrl(list.get(1).getFileUrl());
        faceEntity.save(listener);
    }
}
