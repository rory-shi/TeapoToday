package com.ryan.teapottoday;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.RemoteViews;

import com.ryan.teapottoday.diyView.WidgetView;

/**
 * Created by rory9 on 2016/5/29.
 */
public class MyAppWidgetProvider extends android.appwidget.AppWidgetProvider {
    public static final String CLICK_ACTION = "com.ryan.teapottoday.action.CLICK";

    public MyAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        /*if (intent.getAction().equals(CLICK_ACTION)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap scrbBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_img);
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i<37;i++) {
                        float degree = (i*10)%360;
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                        remoteViews.setImageViewBitmap(R.id.iv_widget, rotateBitmap(context, scrbBitmap, degree));
                        Intent intentClick = new Intent();
                        intentClick.setAction(CLICK_ACTION);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
                        remoteViews.setOnClickPendingIntent(R.id.iv_widget,pendingIntent);
                        appWidgetManager.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
                        SystemClock.sleep(30);
                    }
                }
            }).start();
        }*/


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final int counter = appWidgetIds.length;
        for(int i =0;i<counter;i++) {
            int appWidgetId = appWidgetIds[i];
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
        }

    }

    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        /*RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick,0);
        remoteViews.setOnClickPendingIntent(R.id.iv_widget,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId,remoteViews);*/
        WidgetView myView = new WidgetView(context);
        myView.measure(250, 250);
        myView.layout(0, 0, 250, 250);
        myView.setDrawingCacheEnabled(true);
        Bitmap bitmap = myView.getDrawingCache();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        remoteViews.setImageViewBitmap(R.id.iv_widget, bitmap);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private Bitmap rotateBitmap(Context context, Bitmap srcbBitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        return Bitmap.createBitmap(srcbBitmap, 0, 0, srcbBitmap.getWidth(),srcbBitmap.getHeight(),matrix,true);
    }
}
