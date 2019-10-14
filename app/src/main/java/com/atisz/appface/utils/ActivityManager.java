package com.atisz.appface.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by LaoWu on 2018/6/22 0022.
 * Activity管理类
 */

public class ActivityManager {
    private static Stack<Activity> activities = new Stack<>();

    /**
     * 往堆栈中放入一个Activity
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activities.push(activity);
    }

    /**
     * 获取当前的Activity,堆栈中最上面的Activity
     */
    public static Activity getCurrentActivity() {
        return activities.lastElement();
    }

    /**
     * 结束堆栈最上面的Activity
     */
    public static void finishCurrentActivity() {
        Activity activity = activities.lastElement();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     * @param activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activities.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的activity
     */
    public static void finishActivity(Class<?> activity) {
        for (Activity activity1 : activities) {
            if (activity1.getClass().equals(activity)) {
                finishActivity(activity1);
            }
        }
    }

    /**
     * 结束全部的Activity
     */
    public static void finishAllActivity() {
        Activity activity;
        while (!activities.empty()) {
            activity = activities.pop();
            activity.finish();
        }
    }
}
