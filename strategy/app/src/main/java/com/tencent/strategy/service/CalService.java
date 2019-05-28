package com.tencent.strategy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.tencent.strategy.StartUpSpeed;

public class CalService extends Service {

    private CalInterfaceImpl mServiceImpl = new CalInterfaceImpl();

    public CalService() {
    }

    @Override
    public void onCreate() {
        StartUpSpeed.tag("CalService onCreate");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        StartUpSpeed.tag("CalService onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        StartUpSpeed.tag("CalService onBind");
        return mServiceImpl;
    }

    @Override
    public void onDestroy() {
        StartUpSpeed.tag("CalService onDestroy");
        super.onDestroy();
    }
}
