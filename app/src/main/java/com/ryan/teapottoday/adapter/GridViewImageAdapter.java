package com.ryan.teapottoday.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ryan.teapottoday.CollectionItem;
import com.ryan.teapottoday.R;
import com.ryan.teapottoday.database.MyDatabaseHelper;
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
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<String> urls;

    public GridViewImageAdapter(Context context, HashMap<Integer,Boolean> map) {
        mContext = context;
        this.mSelectMap = map;

        urls = new ArrayList<>();

        dbHelper = new MyDatabaseHelper(mContext,"TeapotToday.db",null,2);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Teapot", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String url = cursor.getString(cursor.getColumnIndex("url"));
                urls.add(url);
            } while ( cursor.moveToNext());
        }
        cursor.close();
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
    public View getView(int position, View convertView, ViewGroup parent) {
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



        item.setImgUrl(urls.get(position));
        //item.setImgResId(getItem(position));
        item.setChecked(mSelectMap.get(position) == null ? false
                : mSelectMap.get(position));
        return item;
    }

}
