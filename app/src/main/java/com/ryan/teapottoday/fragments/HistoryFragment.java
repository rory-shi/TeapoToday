package com.ryan.teapottoday.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryan.teapottoday.R;
import com.ryan.teapottoday.adapter.GridViewImageAdapter;
import com.ryan.teapottoday.adapter.HistoryViewPagerAdapter;
import com.ryan.teapottoday.database.MyDatabaseHelper;
import com.ryan.teapottoday.diyView.TextViewVertical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by rory9 on 2016/4/6.
 */
public class HistoryFragment extends Fragment {
//    private TextViewVertical tv;
//    private HorizontalScrollView sv;
    private ViewPager historyViewPager;
    private int[] ids = {R.drawable.history_song,R.drawable.history_ming,R.drawable.history_qing,R.drawable.history_now};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_history,container,false);
        historyViewPager = (ViewPager) view.findViewById(R.id.vp_history);

        List<View> viewList = new ArrayList<>();
        for (int i=0;i<ids.length;i++) {
            ImageView iv = new ImageView(getActivity());
            iv.setImageResource(ids[i]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewList.add(iv);
        }

        historyViewPager.setAdapter(new HistoryViewPagerAdapter(viewList));

       // tv = (TextViewVertical) view.findViewById(R.id.tv);
        /*sv = (HorizontalScrollView) view.findViewById(R.id.sv);

        //设置接口事件接收
        Handler handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case TextViewVertical.LAYOUT_CHANGED:
                        sv.scrollBy(tv.getTextWidth(), 0);//滚动到最右边
                        break;
                }
            }
        };
        tv.setHandler(handler);//将Handler绑定到TextViewVertical*/

        //创建并设置字体（这里只是为了效果好看一些，但为了让网友们更容易下载，字体库并没有一同打包
        //如果需要体验下效果的朋友可以自行在网络上搜索stxingkai.ttf并放入assets/fonts/中）
        //Typeface face=Typeface.createFromAsset(getAssets(),"fonts/stxingkai.ttf");
        //tv.setTypeface(face);

        //设置文字内容
       // tv.setText(getString(R.string.ICH_history_01));
        return view;
    }


}
