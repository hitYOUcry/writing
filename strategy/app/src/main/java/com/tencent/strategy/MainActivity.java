package com.tencent.strategy;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.Unsafe;
import com.tencent.strategy.activity.TargetActivity;
import com.tencent.strategy.db.MainDataBase;
import com.tencent.strategy.db.StaffDao;
import com.tencent.strategy.proxy.EResource;
import com.tencent.strategy.proxy.YImpl;
import com.tencent.strategy.proxy.YInterface;
import com.tencent.strategy.service.ICalInterface;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity {

    MainDataBase mDB;
    StaffDao mDao;

    int abc = 20;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TextView txv = findViewById(R.id.textView);
        txv.setText(RecyclerView.SavedState.class.getClassLoader().getClass().getCanonicalName());
        txv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TargetActivity.class);
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
            }
        });

        //        mDB = Room.databaseBuilder(getApplicationContext(), MainDataBase.class, "main_db")
        //                .allowMainThreadQueries()
        //                .addMigrations(MIGRATION_1_2)
        //                .addMigrations(MIGRATION_2_3)
        //                .build();
        //        mDao = mDB.staffDao();
        //
        //
        //        Staff s = new Staff();
        //        s.name = "NYY" + s.id;
        //        s.price = 10000;
        //        s.age = 27;
        //        s.gender = 1;
        //        mDao.save(s);
        //
        //
        //        List<Staff> ss = mDao.getAll();
        //        Log.i("MainActivity_ROOM", "all size:" + ss.size());
        //        for (Staff staff : ss) {
        //            Log.i("MainActivity_ROOM", staff.toString());
        //        }
        //
        //        StartUpSpeed.tag("try bind service");
        //        Intent intent = new Intent(this, CalService.class);
        //        bindService(intent, mConnection, BIND_AUTO_CREATE);

        //
        //        YInterface y = new YImpl();
        //
        //        y = (YInterface) Proxy.newProxyInstance(y.getClass().getClassLoader(), y.getClass().getInterfaces(),
        //                new InvocationHandler() {
        //                    private YImpl yy = new YImpl();
        //
        //                    @Override
        //                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //                        //                        Log.e(YImpl.TAG, "method:" + method.getName());
        //                        //
        //                        //                        if (args != null) {
        //                        //                            for (Object obj : args) {
        //                        //                                Log.e(YImpl.TAG, "obj:" + obj);
        //                        //                            }
        //                        //                        }
        //                        if (args == null) {
        //                            return 20;
        //                        }
        //
        //                        return method.invoke(yy, args);
        //                    }
        //                });
        //        y.printLog("MMP");
        //        Log.e(YImpl.TAG, "y.randInt() = " + y.randInt());
        //
        //        hookResources();
        //
        //        Log.e(YImpl.TAG, "getResources().getString(1111) = " + getResources().getString(1111));

        hookPMS();

        try {
            getPackageManager().getApplicationInfo("123", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Object obj = new Object();

        Log.i("DARK_MAGIC", "obj adderss=" + Unsafe.getObjectAddress(obj));

        try {
            Field field = this.getClass().getDeclaredField("abc");
            long abcOffset = Unsafe.objectFieldOffset(field);
            Unsafe.putInt(this, abcOffset, 1000);
            Log.i("DARK_MAGIC", "abc=" + abc);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        long start = System.currentTimeMillis();
        PackageManager pm = getPackageManager();
        int pkgSize = 0;
        List<PackageInfo> pkgInfos = pm.getInstalledPackages(0);
        pkgSize = pkgInfos.size();
        for (PackageInfo pkgInfo : pkgInfos) {
            Log.i("DARK_MAGIC","pkgName:" + pkgInfo.packageName + ",versionCode:" + pkgInfo.versionCode);
        }
        Log.i("DARK_MAGIC","timeCost:" + (System.currentTimeMillis() - start) + ",pkgSize="+pkgSize);

    }

    private void hookPMS() {
        try {
            Class<?> atClazz = Class.forName("android.app.ActivityThread");
            Field sPMField = atClazz.getDeclaredField("sPackageManager");
            sPMField.setAccessible(true);

            final Object obj = sPMField.get(null);

            Class<?> ipmClazz = Class.forName("android.content.pm.IPackageManager");
            Object hookedATpm = Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                    new Class[]{ipmClazz}, new InvocationHandler() {
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
                            Log.i("HOOK_PMS", "method name:" + method.getName() + " with args:" + sb.toString());
                            return method.invoke(obj, args);
                        }
                    });
            sPMField.setAccessible(true);
            sPMField.set(null, hookedATpm);

            PackageManager pm = getPackageManager();
            Class<?> apmClass = Class.forName("android.app.ApplicationPackageManager");
            Field mPMField = apmClass.getDeclaredField("mPM");
            mPMField.setAccessible(true);
            mPMField.set(pm, hookedATpm);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void hookResources() {
        Object obj = this;
        try {
            Resources res = getResources();

            Resources hookedRes = new EResource(res.getAssets(), res.getDisplayMetrics(), res.getConfiguration());

            Field resObj = obj.getClass()
                    .getSuperclass()
                    .getDeclaredField("mResources");
            resObj.setAccessible(true);

            resObj.set(obj, hookedRes);


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StartUpSpeed.tag("[onServiceConnected]");
            ICalInterface i = ICalInterface.Stub.asInterface(service);
            try {
                Toast.makeText(MainActivity.this, "7+9=" + i.add(7, 9), Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}
