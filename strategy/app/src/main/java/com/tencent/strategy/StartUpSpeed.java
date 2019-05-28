package com.tencent.strategy;

import android.util.Log;

import com.tencent.strategy.utils.ProcessUtils;

/**
 * @author nemoqjzhang
 * @date 2018/8/21 15:11.
 */

public class StartUpSpeed {
    private static final String TAG = "StartUpSpeed";

    public static void tag(String position){
        Log.i(TAG,"\nPosition:" + position
                + "\nCurrentTime:" + System.currentTimeMillis()
                + "\nProcessName:" + ProcessUtils.getProcessName()
                + "\nThreadInfo:[name:" + Thread.currentThread().getName()
                + ",id:" + Thread.currentThread().getId() + "]");
    }

}
