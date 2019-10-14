package com.atisz.appface.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuwei on 2018/9/26.
 */

public class SystemUtil {
    protected static final int MAX_PHONE_NUM = 11;
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 校验手机号码
     */
    public static boolean checkPhoneNumbe(Context context, String phone) {
        if (phone != null && phone.length() != MAX_PHONE_NUM) {
            Toast.makeText(context, "手机号码位数不对", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.length() == MAX_PHONE_NUM) {
            if (!phone.matches("^(13|14|15|16|17|18|19)\\d{9}$")) {
                Toast.makeText(context, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /**
     * 判断文件是否存在
     *
     * @param filepath
     * @param filename
     * @return
     */
    public static boolean checkFileExit(String filepath, String filename) {
        File file = new File(filepath);
        String[] list = file.list();
        if (list.length > 0) {
            //有文件
            for (int i = 0; i < list.length; i++) {
                int dot = list[i].lastIndexOf('.');
                if ((dot > -1) && (dot < (list[i].length()))) {
                    String name = list[i].substring(0, dot);
                    if (filename.equals(name)) {
                        return true;
                    }
                }
            }
        } else {
            //没有文件
            return false;
        }
        return false;
    }

    /**
     * 获取文件夹下所有文件
     * @param directoryPath
     * @param isAddDirectory
     * @return
     */
    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if(isAddDirectory){
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(),isAddDirectory));
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    /**
     * 获取真实文件名
     * @param filepath
     * @return
     */
    public static String getFileRealName(String filepath){
        int start = filepath.lastIndexOf("/")+1;
        int end = filepath.lastIndexOf(".");
        if (start != -1 && end != -1 && end > start) {
            return filepath.substring(start, end);
        } else {
            return filepath;
        }
    }

    /**
     * 获取真实文件名
     * @param filepath
     * @return
     */
    public static List<String> getFileRealName(List<String> filepath){
        List<String> list = new ArrayList<String>();
        for (int i=0;i<filepath.size();i++){
            int start = filepath.get(i).lastIndexOf("/")+1;
            int end = filepath.get(i).lastIndexOf(".");
            if (start != -1 && end != -1 && end > start) {
                list.add(filepath.get(i).substring(start,end));
            }
        }
        return list;
    }

    /**********************************************************************/
    /**
     * 获取最佳分辨率
     * @param sizes
     * @param metrics
     * @return
     */
    public static Camera.Size getBestSupportedSize(List<Camera.Size> sizes, DisplayMetrics metrics) {
        Camera.Size bestSize = sizes.get(0);
        float screenRatio = (float) metrics.widthPixels / (float) metrics.heightPixels;
        if (screenRatio > 1) {
            screenRatio = 1 / screenRatio;
        }

        for (Camera.Size s : sizes) {
            if (Math.abs((s.height / (float) s.width) - screenRatio) < Math.abs(bestSize.height /
                    (float) bestSize.width - screenRatio)) {
                bestSize = s;
            }
        }
        return bestSize;
    }

    /********************************************************************/

    /**
     * 数组转字符串
     * @param bytes
     * @return
     */
    public static String getStringByByte(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte item : bytes) {
            stringBuilder.append(HEX_CHAR[item >>> 4 & 0xf]);
            stringBuilder.append(HEX_CHAR[item & 0xf]);
//            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }


    public static int dp2px(Activity activity,int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                activity.getResources().getDisplayMetrics());
    }


    /**
     * dpתpx
     *
     */
    public static int dip2px(Context ctx,float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     *	pxתdp
     */
    public static int px2dip(Context ctx,float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /********************************************************************/
    /**
     * 获取系统时间
     * @return
     */
    public static String getSystemTime(){
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String date = simpleDateFormat.format(new Date());

        return date;
    }
}
