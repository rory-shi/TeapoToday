package com.ryan.teapottoday;

import android.app.Application;
import android.content.Context;

/**
 * Created by rory9 on 2016/5/13.
 */
public class MyApplication extends Application {
    private static Context context;
    public static final String CONTENT = "http://10.0.3.2:8080/mywebapps/";
    // public static final String CONTENT = "http://192.168.191.1:8080/mywebapps/";


    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public static Context getContextObject() {
        return context;
    }
}
