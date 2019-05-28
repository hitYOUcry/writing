package com.java;

import java.util.concurrent.TimeUnit;

/**
 * @author nemoqjzhang
 * @date 2018/10/16 20:47.
 */
public class ThreadTest {

    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                long startTime = System.nanoTime();
                while (i < 1000000){
                    i++;
                    getInstance();
                }
                long endTime = System.nanoTime();
                System.out.println("getInstance     : timeCost = " + (endTime - startTime) + "ns");
            }
        });
        t.start();


        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                long startTime = System.nanoTime();
                while (i < 1000000){
                    i++;
                    getInstanceFake();
                }
                long endTime = System.nanoTime();
                System.out.println("getInstanceFake : timeCost = " + (endTime - startTime) + "ns");
            }
        });
        t2.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    private static volatile ThreadTest sInstance = null;

    private ThreadTest() {}

    public static ThreadTest getInstance() {
        if (sInstance == null) {
            synchronized (ThreadTest.class) {
                if (sInstance == null) {
                    sInstance = new ThreadTest();
                }
            }
        }
        return sInstance;
    }



    private static volatile ThreadTest sInstanceFake = null;

    public static ThreadTest getInstanceFake() {
        ThreadTest result = sInstanceFake;
        if (result == null) {
            synchronized (ThreadTest.class) {
                result = sInstanceFake;
                if (result == null) {
                    result = sInstanceFake = new ThreadTest();
                }
            }
        }
        return result;
    }
}
