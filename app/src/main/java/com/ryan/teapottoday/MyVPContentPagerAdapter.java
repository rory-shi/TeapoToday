package com.ryan.teapottoday;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by rory9 on 2016/5/13.
 */
public class MyVPContentPagerAdapter extends PagerAdapter {
    private ArrayList<View> imgList;
    private Context mContext;

    public MyVPContentPagerAdapter(Context context){
        this.mContext = context;
        imgList = new ArrayList<View>();
        LayoutInflater lf = LayoutInflater.from(mContext);
        View view1 = lf.inflate(R.layout.content_image, null);
        View view2 = lf.inflate(R.layout.content_image, null);
        View view3 = lf.inflate(R.layout.content_image,null);

        imgList.add(view1);
        imgList.add(view2);
        imgList.add(view3);
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = imgList.get(position);
        container.addView(v);
        return imgList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imgList.get(position));

    }

}
