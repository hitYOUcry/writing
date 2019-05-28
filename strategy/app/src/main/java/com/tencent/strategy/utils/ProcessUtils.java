package com.tencent.strategy.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by nemoqjzhang on 2018/1/22 14:57.
 * @author nemoqjzhang
 *
 * 除去下载相关的操作，都放到RemoteOpService所在的进程进程
 *
 */


public class ProcessUtils {

    private static final String TAG = "DSDK_ProcessUtils";

    private static String sProcessName;

    /**
     * 约定的SDK进程名称会包含该字符串
     */
    private static String PROCESS_SDK_DOWNLOAD = "TMAssistantDownloadSDKService";

    /**
     * RemoteOpService所在的进程
     */
    private static String PROCESS_REMOTE_OP = "com.tencent.mobileqq";


    /**
     * 判断是否是RemoteOpService所在进程，可能会比较耗时，不要在UI线程操作
     *
     * @return 是否RemoteOpService所在进程(默认是)
     */
    public static boolean isRemoteOpProcess() {
        if (TextUtils.isEmpty(sProcessName)) {
            try {
                sProcessName = readProcessName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(sProcessName)) {
            Log.i(TAG,"current process:"+sProcessName
            +",is remote op process:" + sProcessName.equals(PROCESS_REMOTE_OP));
            return sProcessName.equals(PROCESS_REMOTE_OP);
        }
        //当获取不到当前进程信息时，为了防止出现死循环默认是RemoteOpService所在进程。
        return true;
    }


    /**
     * 判断是否是下载service所在进程
     * @return 是否是下载Service所在进程
     */
    public static boolean isDownloadServiceProcess() {
        if (TextUtils.isEmpty(sProcessName)) {
            try {
                sProcessName = readProcessName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(sProcessName)) {
            Log.i(TAG,"current process:"+sProcessName
                    +",is sdk download service process:" + sProcessName.contains(PROCESS_SDK_DOWNLOAD));
            return sProcessName.contains(PROCESS_SDK_DOWNLOAD);
        }
        //当获取不到当前进程信息时，默认是。
        return true;
    }



    /**
     * 只能在RemoteOpService所在进程调用，
     * 用于初始化RemoteOpService服务进程的名称，如果用户不小心设置的进程名和约定的值不一致
     * 需要重置PROCESS_REMOTE_OP
     */
    public static void initRemoteOpProcessName() {
        try {
            sProcessName = readProcessName();
            Log.i(TAG, "remote op process is:" + sProcessName);
            if (!sProcessName.equals(PROCESS_REMOTE_OP)) {
                PROCESS_REMOTE_OP = sProcessName;
                Log.i(TAG, "remote op process don't equals \"com.tencent.mobileqq\" set PROCESS_REMOTE_OP = " + PROCESS_REMOTE_OP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 只能在TMAssistantDownloadService所在进程调用，
     * 用于初始化TMAssistantDownloadService服务进程的名称，如果用户不小心设置的进程名和约定的值不一致
     * 需要重置PROCESS_SDK_DOWNLOAD
     */
    public static void initDownloadServiceProcessName() {
        try {
            sProcessName = readProcessName();
            Log.i(TAG, "SDK process is:" + sProcessName);
            if (!sProcessName.contains(PROCESS_SDK_DOWNLOAD)) {
                PROCESS_SDK_DOWNLOAD = sProcessName;
                Log.i(TAG, "SDK process don't contain \"TMAssistantDownloadSDKService\" set PROCESS_SDK_DOWNLOAD = " + PROCESS_SDK_DOWNLOAD);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get process name by reading {@code /proc/self/cmdline}.
     *
     * @return Process name or null if there was an error reading from {@code /proc/self/cmdline}.
     * It is unknown how this error can occur in practice and should be considered extremely
     * rare.
     */
    public static synchronized String getProcessName() {
        if (TextUtils.isEmpty(sProcessName)) {
            try {
                sProcessName = readProcessName();
            } catch (IOException e) {
            }
        }
        Log.d(TAG, "process:" + sProcessName);
        return sProcessName;
    }


    /**
     * Maximum length allowed in {@code /proc/self/cmdline}.  Imposed to avoid a large buffer
     * allocation during the init path.
     */
    public static final int CMDLINE_BUFFER_SIZE = 64;

    private static String readProcessName() throws IOException {
        byte[] cmdlineBuffer = new byte[CMDLINE_BUFFER_SIZE];

        // Avoid using a Reader to not pick up a forced 16K buffer.  Silly java.io...
        FileInputStream stream = new FileInputStream("/proc/self/cmdline");
        boolean success = false;
        try {
            int n = stream.read(cmdlineBuffer);
            success = true;
            int endIndex = indexOf(cmdlineBuffer, 0, n, (byte) 0 /* needle */);
            return new String(cmdlineBuffer, 0, endIndex > 0 ? endIndex : n);
        } finally {
            try {
                stream.close();
            } catch (Throwable t) {
            }
        }
    }

    private static int indexOf(byte[] haystack, int offset, int length, byte needle) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle) {
                return i;
            }
        }
        return -1;
    }

}
