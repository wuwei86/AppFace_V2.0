package com.atisz.appface.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

import com.atisz.appface.ui.base.MyBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LaoWu on 2018/4/28.
 * 动态权限管理工具类
 */

public class PermissionUtils {
    private static final int REQUEST_CODE = 0;

    public interface PermissionListener {
        void onGranted();
        void onDenied(List<String> deniedPermission);
    }

    /**
     * 请求动态权限
     * @param activity
     * @param requestPermission
     * @param mListener
     */
    public static void requestPermission(Activity activity, String[] requestPermission, PermissionListener mListener) {
        List<String> deniedPermission = new ArrayList<>();
        for (String permission : requestPermission) {
            if (!checkPermission(activity, permission)) {
                deniedPermission.add(permission);
            }
        }
        if (deniedPermission.isEmpty()) {
            mListener.onGranted();
        } else {
            ActivityCompat.requestPermissions(activity, deniedPermission.toArray(new String[deniedPermission.size()]), REQUEST_CODE);
        }
    }

    /**
     * 判断是否有相应的动态权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean result;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            result = PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return result;
    }

    /**
     * 请求动态权限结果回调的判断
     */
    public static void requestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, PermissionListener permissionListener) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                List<String> deniedPermission = new ArrayList<>();
                for (int i=0; i<grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        deniedPermission.add(permissions[i]);
                    }
                }
                if (deniedPermission.size() != 0) {
                    permissionListener.onDenied(deniedPermission);
                } else {
                    permissionListener.onGranted();
                }
            }
        }
    }


    /**
     * 打开设置权限页面
     * @param activity
     * @param message
     */
    public static void openSettingActivity(final Activity activity, String message) {
        showMessageOKCancel(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (activity instanceof MyBaseActivity) {
                    ((MyBaseActivity) activity).alert("请设置必要的权限！");
                }
            }
        });
    }

    /**
     * 显示弹框
     * @param context
     * @param message
     * @param settingListener
     * @param cancelListener
     */
    private static void showMessageOKCancel(final Activity context, String message,
                                            DialogInterface.OnClickListener settingListener,
                                            DialogInterface.OnClickListener cancelListener) {
        String permissionMessage = "当前应用缺少必要权限("+message+")\n" +
                "\n 请点击“设置”-“权限”-打开所需权限。\n";
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage(permissionMessage)
                .setPositiveButton("设置", settingListener)
                .setNegativeButton("取消", cancelListener)
                .create()
                .show();

    }
}
