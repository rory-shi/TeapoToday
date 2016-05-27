package com.ryan.teapottoday.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ryan.teapottoday.CollectionItem;
import com.ryan.teapottoday.R;
import com.ryan.teapottoday.database.MyDatabaseHelper;
import com.ryan.teapottoday.model.ImageCacheManager;
import com.ryan.teapottoday.model.ImageCacheUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rory9 on 2016/4/6.
 */
public class GridViewImageAdapter extends BaseAdapter {
    private Map<Integer, Boolean> mSelectMap = new HashMap<>();
    private Context mContext;
    private ArrayList<String> urls;
    private boolean debug = true;
    private GestureDetector gestureDetector;
    public GridViewImageAdapter(Context context, HashMap<Integer,Boolean> map, ArrayList<String> urls) {
        mContext = context;
        this.mSelectMap = map;

        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CollectionItem item;
        if (convertView == null) {
            item = new CollectionItem(mContext);
            item.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.MATCH_PARENT));
        } else {
            item = (CollectionItem) convertView;
        }

       /* SharedPreferences pref = mContext.getSharedPreferences("data",
                Context.MODE_PRIVATE);
        String url = pref.getString("url", "");*/


        gestureDetector = new GestureDetector(mContext, new SingleTapConfirm());

        /*
        * LayoutInflater inflater = LayoutInflater.from(mContext);
                View imgEntryView = inflater.inflate(R.layout.content_image, null); // 加载自定义的布局文件
                ImageView ivContentDetail = (ImageView) imgEntryView.findViewById(R.id.iv_content_img);
                String url = urls.get(position);

                if (debug) {
                    Log.e("debug", url);
                }
                Bitmap defaultImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.demo);
                Bitmap errorImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.hee);
                ImageCacheManager.loadImage(mContext, url, ivContentDetail, defaultImage, errorImage);

                final AlertDialog dialog = new AlertDialog.Builder(mContext).create();

                //ImageView img = (ImageView) imgEntryView.findViewById(R.id.demo);
                //imageDownloader.download("图片地址", img); // 这个是加载网络图片的，可以是自己的图片设置方法
                dialog.setView(imgEntryView); // 自定义dialog

                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.CENTER);
                lp.width = mContext.getResources().getDisplayMetrics().widthPixels; // 宽度
                //lp.height = 500; // 高度
                dialogWindow.setAttributes(lp);


                dialog.show();
                // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
                imgEntryView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        dialog.cancel();
                    }
                });
        * */

        item.setImgUrl(urls.get(position));
        //item.setImgResId(getItem(position));
        item.setChecked(mSelectMap.get(position) == null ? false
                : mSelectMap.get(position));
        return item;
    }


    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }
    }
}
