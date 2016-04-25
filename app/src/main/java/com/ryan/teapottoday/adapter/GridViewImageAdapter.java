package com.ryan.teapottoday.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ryan.teapottoday.CollectionItem;
import com.ryan.teapottoday.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rory9 on 2016/4/6.
 */
public class GridViewImageAdapter extends BaseAdapter {
    private int[] mImgIds;
    private Map<Integer, Boolean> mSelectMap = new HashMap<>();
    private Context mContext;

    public GridViewImageAdapter(Context context, int[] imgIds, HashMap<Integer,Boolean> map) {
        mContext = context;
        this.mImgIds = imgIds;
        this.mSelectMap = map;
    }

    @Override
    public int getCount() {
        return mImgIds.length;
    }

    @Override
    public Integer getItem(int position) {
        return mImgIds[position];
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
        item.setImgResId(getItem(position));
        item.setChecked(mSelectMap.get(position) == null ? false
                : mSelectMap.get(position));
        return item;
    }

}
