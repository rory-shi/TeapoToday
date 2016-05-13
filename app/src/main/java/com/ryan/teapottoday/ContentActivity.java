package com.ryan.teapottoday;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

/**
 * Created by rory9 on 2016/4/12.
 */
public class ContentActivity extends Activity{

    private ViewPager vpContent;
    private float mLastY;
    private ScrollView svContent;
    private MyVPContentPagerAdapter myVPContentPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_layout);
        vpContent = (ViewPager) findViewById(R.id.vp_content);
        svContent = (ScrollView) findViewById(R.id.sv_content);
        myVPContentPagerAdapter = new MyVPContentPagerAdapter(this);
        vpContent.setAdapter(myVPContentPagerAdapter);
    }

    //50是顶栏的高度
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getY()<vpContent.getHeight()+100) {
            return super.dispatchTouchEvent(ev);
        }
        return onTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                mLastY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float y = event.getY();

                float delta = y - mLastY;

                float alphaDelta = (y - mLastY) / 6000;

                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) vpContent.getLayoutParams();
                int mHeight = vpContent.getHeight();
                lp.height = (int) (mHeight + delta/10);
                if (lp.height<0) {
                    lp.height = 0;
                } else if (lp.height>600) {
                    lp.height = 600;
                }
                vpContent.setLayoutParams(lp);

                float alpha = vpContent.getAlpha() + alphaDelta;
                if (alpha > 1.0) {
                    alpha = 1.0f;
                } else if (alpha < 0.0) {
                    alpha = 0.0f;
                }
                vpContent.setAlpha(alpha);
             //   vpContent.scrollBy((int)(1000*alphaDelta),(int)(1000*alphaDelta));
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }
        return true;
    }
}
