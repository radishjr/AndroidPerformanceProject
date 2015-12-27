package com.nekoproject.androidperformanceproject;

import android.app.Application;
import android.util.Log;

/**
 * Created by Administrator on 2015/12/27.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e("Crash", "Crash", ex);
            }
        });
    }
}
