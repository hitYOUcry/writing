package com.tencent.strategy;

import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.strategy.proxy.EInstrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author nemoqjzhang
 * @date 2018/9/11 21:58.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();



        hookAMSForStartPluginAc();
        hookActivityThreadForStartActivity();


    }

    private void hookActivityThreadForStartActivity() {
        try {
            Class<?> c_activityThread = Class.forName("android.app.ActivityThread");
            Field f_sCurrentActivityThread = c_activityThread.getDeclaredField("sCurrentActivityThread");
            f_sCurrentActivityThread.setAccessible(true);
            Object o_sCurrentActivityThread = f_sCurrentActivityThread.get(null);


            Field f_mH = c_activityThread.getDeclaredField("mH");
            f_mH.setAccessible(true);
            final Object o_mH = f_mH.get(o_sCurrentActivityThread);


            Class<?> c_Handler = Class.forName("android.os.Handler");
            Field f_mCallback = c_Handler.getDeclaredField("mCallback");
            f_mCallback.setAccessible(true);
//
//            Object h_mCallback = Proxy.newProxyInstance(o_sCurrentActivityThread.getClass().getClassLoader(),
//                    new Class[]{android.os.Handler.Callback.class}, new InvocationHandler() {
//                        @Override
//                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                            /*case LAUNCH_ACTIVITY: {
//                                Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
//                                final ActivityClientRecord r = (ActivityClientRecord) msg.obj;
//
//                                r.packageInfo = getPackageInfoNoCheck(
//                                        r.activityInfo.applicationInfo, r.compatInfo);
//                                handleLaunchActivity(r, null, "LAUNCH_ACTIVITY");
//                                Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
//                            } break;*/
//                            if(TextUtils.equals("handleMessage",method.getName())){
//                                Message msg = (Message) args[0];
//                                if(100 == msg.what){
//                                    Class<?> c_ActivityClientRecord = Class.forName("android.app.ActivityThread$ActivityClientRecord");
//                                    Field f_Intent = c_ActivityClientRecord.getDeclaredField("intent");
//                                    f_Intent.setAccessible(true);
//                                    Intent intent = (Intent) f_Intent.get(msg.obj);
//
//                                    Intent oriIntent = intent.getParcelableExtra("ORI_INTENT");
//
//                                    if(oriIntent != null){
//                                        f_Intent.set(msg.obj,oriIntent);
//                                    }
//
//                                }
//                                Handler handler = (Handler) o_mH;
//                                handler.handleMessage(msg);
//                            }
//                            return false;
//                        }
//                    });
//
//
            f_mCallback.set(o_mH,new HookedHandlerCallback((Handler) o_mH));


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    static class HookedHandlerCallback implements Handler.Callback {
        Handler mBase;

        public HookedHandlerCallback(Handler base){
            mBase = base;
        }

        @Override
        public boolean handleMessage(Message msg) {
            if(100 == msg.what){
                Class<?> c_ActivityClientRecord = null;
                try {
                    c_ActivityClientRecord = Class.forName("android.app.ActivityThread$ActivityClientRecord");
                    Field f_Intent = c_ActivityClientRecord.getDeclaredField("intent");
                    f_Intent.setAccessible(true);
                    Intent intent = (Intent) f_Intent.get(msg.obj);

                    Intent oriIntent = intent.getParcelableExtra("ORI_INTENT");

                    if(oriIntent != null){
                        f_Intent.set(msg.obj,oriIntent);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            mBase.handleMessage(msg);
            return true;
        }
    }

    private void hookAMSForStartPluginAc() {
        try {
            Class<?> activityManagerClazz = Class.forName("android.app.ActivityManager");
            Field iAMS = activityManagerClazz.getDeclaredField("IActivityManagerSingleton");
            iAMS.setAccessible(true);
            final Object iAMSObj = iAMS.get(null);

            //android.util.Singleton
            Class<?> singletonClazz = Class.forName("android.util.Singleton");

            final Field instanceField = singletonClazz.getDeclaredField("mInstance");
            instanceField.setAccessible(true);

            final Object iAMSObj_mInstance = instanceField.get(iAMSObj);

            Class<?> IAMClass = Class.forName("android.app.IActivityManager");
            Object hookedIAM = Proxy.newProxyInstance(activityManagerClazz.getClassLoader(),
                    new Class[]{IAMClass}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            StringBuilder sb = new StringBuilder();
                            sb.append("[");
                            if (args != null) {
                                for (Object arg : args) {
                                    sb.append(arg);
                                    sb.append(", ");
                                }
                            }
                            sb.append("]");
                            Log.i("HOOK_AMS", "method name:" + method.getName() + " with args:" + sb.toString());


                            String methodName = method.getName();
                            if(TextUtils.equals(methodName,"startActivity") && args != null){
                                //拦截开始Activity方法
                                int intentIndex = 0;
                                for(int i = 0;i<args.length;i++){
                                    if(args[i] instanceof Intent){
                                        intentIndex = i;
                                        break;
                                    }
                                }
                                Intent oriIntent = (Intent) args[intentIndex];


                                Intent newIntent = new Intent();
                                ComponentName componentName = new ComponentName(oriIntent.getComponent().getPackageName(),
                                        "com.tencent.strategy.activity.StubActivity");
                                newIntent.setComponent(componentName);
                                newIntent.putExtra("ORI_INTENT",oriIntent);

                                args[intentIndex] = newIntent;
                            }



                            return method.invoke(iAMSObj_mInstance, args);
                        }
                    });

            instanceField.set(iAMSObj, hookedIAM);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }



    }

    private void hookAMS() {

        try {
            Class<?> activityManagerClazz = Class.forName("android.app.ActivityManager");
            Field iAMS = activityManagerClazz.getDeclaredField("IActivityManagerSingleton");
            iAMS.setAccessible(true);
            final Object iAMSObj = iAMS.get(null);


            //android.util.Singleton
            Class<?> singletonClazz = Class.forName("android.util.Singleton");


            final Field instanceField = singletonClazz.getDeclaredField("mInstance");
            instanceField.setAccessible(true);

            final Object iAMSObj_mInstance = instanceField.get(iAMSObj);

            Class<?> IAMClass = Class.forName("android.app.IActivityManager");
            Object hookedIAM = Proxy.newProxyInstance(activityManagerClazz.getClassLoader(),
                    new Class[]{IAMClass}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            StringBuilder sb = new StringBuilder();
                            sb.append("[");
                            if (args != null) {
                                for (Object arg : args) {
                                    sb.append(arg);
                                    sb.append(", ");
                                }
                            }
                            sb.append("]");
                            Log.i("HOOK_AMS", "method name:" + method.getName() + " with args:" + sb.toString());
                            return method.invoke(iAMSObj_mInstance, args);
                        }
                    });

            instanceField.set(iAMSObj, hookedIAM);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    private void hookInstrumentation() throws Exception {
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        // 拿到原始的 mInstrumentation字段
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

        // 创建代理对象
        Instrumentation evilInstrumentation = new EInstrumentation(mInstrumentation);

        // 偷梁换柱
        mInstrumentationField.set(currentActivityThread, evilInstrumentation);
    }
}
