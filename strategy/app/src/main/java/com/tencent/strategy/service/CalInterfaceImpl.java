package com.tencent.strategy.service;

import android.os.RemoteException;

/**
 * @author nemoqjzhang
 * @date 2018/8/23 10:13.
 */
public class CalInterfaceImpl extends ICalInterface.Stub {
    @Override
    public int add(int x, int y) throws RemoteException {
        return x + y;
    }
}
