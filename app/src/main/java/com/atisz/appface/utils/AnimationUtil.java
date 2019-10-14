/**
 * @(#) AnimationUtil.java 2014/06/14 09:18
 *
 * 版权所有 (c) 北京银软网络技术有限公司
 * 北京市海淀区上地国际创业园西区1号
 * 保留所有权利
 */
package com.atisz.appface.utils;

import android.app.Activity;

import com.atisz.appface.R;


/**
 * activity切换动画，设置新界面的进入方式和老界面的退出方式
 * @author laowu
 * @version 1.0
 */
public class AnimationUtil {
    /**
     * 添加动画效果为 新的左进，老的左出
     */
    public static void setAnimationOfLeft(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_change_left_in, R.anim.activity_change_left_out);
    }

    /**
     * 添加动画效果为 新的右进，老的右出
     */
    public static  void setAnimationOfRight(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_change_right_in, R.anim.activity_change_right_out);
    }

    /**
     * 添加动画效果为 新的下进 老的不变
     * @param activity
     */
    public static  void setAnimationOfButton(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_change_buttom_in, R.anim.activity_change_buttom_out);
    }

    /**
     * 添加动画效果为 新的上进 老的不变
     * @param activity
     */
    public static  void setAnimationOfUp(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_change_up_in, R.anim.activity_change_up_out);
    }

    /**
     * 新的淡入 老的不变
     */
    public static void setAnimationOfAlphaIn(Activity activity){
       activity.overridePendingTransition(R.anim.activity_change_alpha_in,R.anim.activity_change_alpha_out);
    }

    /**
     * 新的中间放大进入 老的不变
     * @param activity
     */
    public static void setAnimationZoomCenterIn(Activity activity){
        activity.overridePendingTransition(R.anim.activity_change_zoom_in,R.anim.activity_change_zoom_out);
    }

}