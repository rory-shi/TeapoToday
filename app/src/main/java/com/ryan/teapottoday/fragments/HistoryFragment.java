package com.ryan.teapottoday.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import butterknife.Bind;
import butterknife.ButterKnife;


public class HistoryFragment extends Fragment {
//    private TextViewVertical tv;
//    private HorizontalScrollView sv;

    @Bind(R.id.vp_history)
    ViewPager historyViewPager;
   // private ViewPager historyViewPager;
    private List<View> viewList;
    private int[] ids = {R.drawable.history_song,R.drawable.history_ming,R.drawable.history_qing,R.drawable.history_now};

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_history,container,false);
     //   historyViewPager = (ViewPager) view.findViewById(R.id.vp_history);
        ButterKnife.bind(this,view);

        viewList = new ArrayList<>();
        for (int id : ids) {
            ImageView iv = new ImageView(getActivity());
            iv.setImageResource(id);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewList.add(iv);
            historyViewPager.setAdapter(new HistoryViewPagerAdapter(viewList));
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        });

        return view;
    }


}
