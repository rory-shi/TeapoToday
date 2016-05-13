package com.ryan.teapottoday;

import android.app.Application;
import android.content.Context;

/**
 * Created by rory9 on 2016/5/13.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContextObject() {
        return context;
    }
}
