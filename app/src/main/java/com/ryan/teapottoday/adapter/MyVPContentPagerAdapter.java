package com.ryan.teapottoday.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ryan.teapottoday.R;
import com.ryan.teapottoday.model.ImageCacheManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rory9 on 2016/5/13.
 */
public class MyVPContentPagerAdapter extends PagerAdapter {
    private ArrayList<View> imgList;
    private Context mContext;
    private ImageView ivContent;

    private ArrayList<String> teapotImgsList;
    private String headImg;
    private String dirtImg;
    private boolean debug = false;


    public MyVPContentPagerAdapter(Context context, String contentDir, ArrayList<String> imgs) {
        this.mContext = context;
        imgList = new ArrayList<View>();

        teapotImgsList = imgs;


        for (int i = 7; i < teapotImgsList.size(); i++) {
            LayoutInflater lf = LayoutInflater.from(mContext);
            View view = lf.inflate(R.layout.content_image, null);


            String url = contentDir + teapotImgsList.get(i);
            view.setTag(url);
            ivContent = (ImageView) view.findViewById(R.id.iv_content_img);

            Bitmap defaultImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_img);
            Bitmap errorImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_img);
            ImageCacheManager.loadImage(mContext, url, ivContent, defaultImage, errorImage);

            ivContent.setScaleType(ImageView.ScaleType.CENTER_CROP);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View imgEntryView = inflater.inflate(R.layout.content_image, null); // 加载自定义的布局文件
                    ImageView ivContentDetail = (ImageView) imgEntryView.findViewById(R.id.iv_content_img);
                    String url = (String) v.getTag();

                    if (debug) {
                        Log.e("debug", url);
                    }
                    Bitmap defaultImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_img);
                    Bitmap errorImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_img);
                    ImageCacheManager.loadImage(mContext, url, ivContentDetail, defaultImage, errorImage);

                    final AlertDialog dialog = new AlertDialog.Builder(mContext).create();

                    //ImageView img = (ImageView) imgEntryView.findViewById(R.id.demo);
                    //imageDownloader.download("图片地址", img); // 这个是加载网络图片的，可以是自己的图片设置方法
                    dialog.setView(imgEntryView); // 自定义dialog

                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    dialogWindow.setGravity(Gravity.CENTER);
                    lp.width = 500; // 宽度
                    lp.height = 500; // 高度
                    dialogWindow.setAttributes(lp);


                    dialog.show();
                    // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
                    imgEntryView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View paramView) {
                            dialog.cancel();
                        }
                    });
                }
            });

            imgList.add(view);

        }
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
