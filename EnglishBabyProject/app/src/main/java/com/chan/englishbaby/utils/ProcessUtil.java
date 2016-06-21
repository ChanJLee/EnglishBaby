package com.chan.englishbaby.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.chan.englishboby.BuildConfig;

/**
 * Created by chan on 16/6/21.
 */
public class ProcessUtil {
    public static boolean isMainProcess(Context context) {
        final int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : activityManager.getRunningAppProcesses()) {
            if (pid == appProcessInfo.pid) {
                return (appProcessInfo.processName.equals(BuildConfig.APPLICATION_ID));
            }
        }

        return false;
    }
}
