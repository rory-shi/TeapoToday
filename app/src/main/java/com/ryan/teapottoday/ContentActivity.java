package com.ryan.teapottoday;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Created by rory9 on 2016/4/12.
 */
public class ContentActivity extends Activity{

    private ViewPager vpContent;
    private float mLastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_layout);

        vpContent = (ViewPager) findViewById(R.id.vp_content);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                mLastY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float y = event.getY();
                float alphaDelta = (y - mLastY) / 1000;
                float alpha = vpContent.getAlpha() + alphaDelta;
                if (alpha > 1.0) {
                    alpha = 1.0f;
                } else if (alpha < 0.0) {
                    alpha = 0.0f;
                }vpContent.setAlpha(alpha);
            }
            default:
                break;
        }
        return true;
    }
}
