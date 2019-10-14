package com.atisz.appface.utils;

import android.content.Context;

import com.atisz.appface.config.Config;

import java.io.File;

/**
 * Created by wuwei on 2018/10/25.
 */

public class FileUtil {

    /**
     * 新建保存人脸相关的文件夹
     * @param context
     */
    public static void initFaceDir(Context context){
        /*File imageCamera = new File(context.getFilesDir().getPath() + Config.IMAGE_CAMERA_ROUTE);
        if (!imageCamera.exists()){
            imageCamera.mkdir();
        }
        File imageFace = new File(context.getFilesDir().getPath() + Config.IMAGE_FACE_ROUTE);
        if (!imageFace.exists()){
            imageFace.mkdir();
        }
        File dataFace = new File(context.getFilesDir().getPath() + Config.DATA_FACE_ROUTE);
        if (!dataFace.exists()){
            dataFace.mkdir();
        }
        File configFace = new File(context.getFilesDir().getPath() + Config.CONFIG_FACE_ROUTE);
        if (!configFace.exists()){
            configFace.mkdir();
        }*/
        File compressPicture = new File(context.getFilesDir().getPath() + Config.PICTURE_COMPRESS_ROUTE);
        if (!compressPicture.exists()){
            compressPicture.mkdir();
        }
    }

    /**
     * 删除指定路径文件
     * @param path
     * @return
     */
    public static boolean deleteFile(String path){
        final File file = new File(path);
        if (file.exists()){
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取路径
     * @param context
     * @return
     */
    public static String getImageCamera(Context context){
        return context.getFilesDir().getPath() + Config.IMAGE_CAMERA_ROUTE;
    }

    /**
     * 获取路径
     * @param context
     * @return
     */
    public static String getImageFace(Context context){
        return context.getFilesDir().getPath() + Config.IMAGE_FACE_ROUTE;
    }

    /**
     * 获取路径
     * @param context
     * @return
     */
    public static String getDataFace(Context context){
        return context.getFilesDir().getPath() + Config.DATA_FACE_ROUTE;
    }

    /**
     * 获取路径
     * @param context
     * @return
     */
    public static String getConfigFace(Context context){
        return context.getFilesDir().getPath() + Config.CONFIG_FACE_ROUTE;
    }

    public static String getRegisterImage(Context context){
        return context.getFilesDir().getAbsolutePath() + File.separator + "register" + File.separator + "imgs" + File.separator;
    }

    public static String getRegisterFeat(Context context){
        return context.getFilesDir().getAbsolutePath() + File.separator + "register" + File.separator + "features" + File.separator;
    }
}
