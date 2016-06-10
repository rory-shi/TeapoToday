package com.ryan.teapottoday;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rory9 on 2016/6/10.
 */
public class WidgetService extends Service {

    private Timer timer;
    private SimpleDateFormat sdf = new SimpleDateFormat();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateViews();
            }
        },0,1000);

    }

    private void updateViews() {
        String time = sdf.format(new Date());
        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.widget);
        rv.setTextViewText(R.id.Description1TextView, "sdfsd");
        rv.setTextViewText(R.id.Description2TextView, "sdfsd");
        rv.setTextViewText(R.id.Description3TextView, "sdfsd");
        rv.setTextViewText(R.id.Description4TextView, "sdfsd");
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName cn = new ComponentName(getApplicationContext(),MyAppWidgetProvider.class);
        manager.updateAppWidget(cn,rv);
    }
}
