package com.tencent.strategy.proxy;

import android.util.Log;

/**
 * @author nemoqjzhang
 * @date 2018/9/11 21:29.
 */
public class YImpl implements YInterface {

    public static final String TAG = "YImpl";

    @Override
    public void printLog(String msg) {
        Log.i(TAG, msg);
    }

    @Override
    public int randInt() {
        return 100;
    }
}
