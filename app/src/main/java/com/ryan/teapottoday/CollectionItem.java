package com.ryan.teapottoday;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ryan.teapottoday.model.ImageCacheManager;

/**
 * Created by rory9 on 2016/4/7.
 */
public class CollectionItem extends RelativeLayout implements Checkable {
    private Context mContext;
    private boolean mChecked;
    private ImageView mImgView = null;
    private ImageView mSeclectView = null;

    public CollectionItem(Context context) {
        this(context, null, 0);
    }

    public CollectionItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollectionItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.collection_item, this);
        mImgView = (ImageView) findViewById(R.id.iv_collection);
        mSeclectView = (ImageView) findViewById(R.id.iv_check_collection);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        setBackground(checked ? getResources().getDrawable(R.drawable.background,null) : null);
        mSeclectView.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public void setImgResId(int resId) {
        if (mImgView != null) {
            mImgView.setBackgroundResource(resId);//设置背景
        }
    }
    public void setImgUrl(String url) {
        if (mImgView != null) {
            Bitmap defaultImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_img);
            Bitmap errorImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_img);
            ImageCacheManager.loadImage(mContext, url, mImgView, defaultImage, errorImage);
        }
    }
}
