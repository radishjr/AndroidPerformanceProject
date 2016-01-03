package com.nekoproject.androidperformanceproject;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by Administrator on 2015/12/27.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .detectCustomSlowCalls()
                        //.detectCustomSlowCalls()
                        //.detectDiskReads()
                        //.detectDiskWrites()
                        //.detectNetwork()
                        //.detectResourceMismatches()
                        //.penaltyDialog()
                        //.penaltyLog()
                .penaltyDropBox()
                .penaltyFlashScreen()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .detectLeakedRegistrationObjects()
                .detectLeakedClosableObjects()
                .penaltyDropBox()
                .build());

        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e("Crash", "Crash", ex);
            }
        });
    }
}
