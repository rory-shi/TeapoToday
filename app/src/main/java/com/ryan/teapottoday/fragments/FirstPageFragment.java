package com.ryan.teapottoday.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ryan.teapottoday.R;
import com.ryan.teapottoday.adapter.FirstPageRecyclerViewAdapter;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rory9 on 2016/4/6.
 */
public class FirstPageFragment extends Fragment {

    public static String TAG = "FirstPageFragmentTAG";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRVAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<String> myDataset;

    private SwipeRefreshLayout mSrl;
    private ImageView mImageView;

    private ScheduledExecutorService sche;
    private int timer = 0;
    private int lastVisibleItem ;

    //handler
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mSrl.setRefreshing(false);

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mImageView, "scaleX", 1.0f,1.3f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mImageView, "scaleY", 1.0f,1.3f);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(scaleX).with(scaleY);
            animSet.setDuration(10000);
            animSet.start();
            switch ((timer++)%4) {
                case 0:
                    mImageView.setImageResource(R.drawable.first03);
                    break;
                case 1:
                    mImageView.setImageResource(R.drawable.first02);
                    break;
                case 2:
                    mImageView.setImageResource(R.drawable.first04);
                    break;
                case 3:
                    mImageView.setImageResource(R.drawable.first01);
                    break;
                default:
                    break;
            }

        }
    };

    public FirstPageFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_page,container,false);
        mImageView = (ImageView) view.findViewById(R.id.img_first_page);
        mSrl = (SwipeRefreshLayout) view.findViewById(R.id.srl_first_page);

        mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mImageView.setImageResource(R.drawable.first03);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        //init recycler view
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mRVAdapter = new FirstPageRecyclerViewAdapter(getActivity(),myDataset);
        mRecyclerView.setAdapter(mRVAdapter);

        mSrl.setColorSchemeColors(R.color.colorPrimary);
        mSrl.setProgressViewOffset(false, 100, 100 + (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                receiveJsonFromNetwork();
                mRecyclerView.invalidate();
            }
        });
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mRVAdapter.getItemCount()) {
                    mSrl.setRefreshing(true);

                    handler.sendEmptyMessageDelayed(0, 3000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }
        });
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDataset = new ArrayList<>();

        //get json
        receiveJsonFromNetwork();


        sche = Executors.newSingleThreadScheduledExecutor();
        sche.scheduleAtFixedRate(new SlideShowTask(), 1, 10, TimeUnit.SECONDS);

    }

    private void receiveJsonFromNetwork() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://10.0.3.2:8080/mywebapps/myjson.txt";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(),"网络连接正常"+response.substring(0,23), Toast.LENGTH_SHORT).show();
                handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"网络连接错误", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void handleResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("MyTeaPot");
            for (int i =0; i < jsonArray.length(); i ++) {
                myDataset.add((String) jsonArray.get(i));
                Log.e(TAG,(String) jsonArray.get(i) );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (mImageView) {
                handler.obtainMessage().sendToTarget();
            }
        }

    }
}
