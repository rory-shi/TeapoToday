package com.ryan.teapottoday.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.ryan.teapottoday.R;
import com.ryan.teapottoday.adapter.FirstPageRecyclerViewAdapter;
import com.ryan.teapottoday.model.ImageCacheManager;
import com.ryan.teapottoday.model.VolleyController;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by rory9 on 2016/4/6.
 */
public class FirstPageFragment extends Fragment {

    public static final int UPDATE_FIRST_COLUMN = 1;
    private static final int RECEIVE_JSON = 2;

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSrl;
    private ImageView mImageView;

    private Bitmap defaultImage;

    private ArrayList<String> headUrl;

    private int timer = 0;
    private int lastVisibleItem ;


    //handler
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_FIRST_COLUMN: {
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(mImageView, "scaleX", 1.0f,1.3f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(mImageView, "scaleY", 1.0f,1.3f);

                    AnimatorSet animSet = new AnimatorSet();
                    animSet.play(scaleX).with(scaleY);
                    animSet.setDuration(10000);
                    animSet.start();

                    String content = "http://10.0.3.2:8080/mywebapps/";
                    headUrl = (ArrayList<String>) msg.obj;

                    ImageCacheManager.loadImage(getActivity(), content + headUrl.get((timer++) % 4), mImageView, defaultImage, defaultImage);

//                    handler.sendEmptyMessageDelayed(UPDATE_FIRST_COLUMN,10000);

                    Message headMsg = new Message();
                    headMsg.what = UPDATE_FIRST_COLUMN;
                    headMsg.obj = headUrl;
                    handler.sendMessageDelayed(headMsg, 10000);

                    break;
                }
                case RECEIVE_JSON:{
                    RecyclerView.Adapter mRVAdapter = new FirstPageRecyclerViewAdapter(getActivity(), (ArrayList<ArrayList<String>>) msg.obj);
                    mRecyclerView.setAdapter(mRVAdapter);

                    mSrl.setRefreshing(false);
                    break;
                }

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
        //mImageView.setScaleType(ImageView.ScaleType.FIT_END);

        defaultImage = BitmapFactory.decodeResource(getActivity().getResources(), getResources().getColor(R.color.colorBackground));


        mSrl = (SwipeRefreshLayout) view.findViewById(R.id.srl_first_page);



        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        //init recycler view
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSrl.setColorSchemeColors(R.color.colorPrimary);
        mSrl.setProgressViewOffset(false, 100, 100 + (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        receiveJsonFromNetwork(true);
                        mRecyclerView.invalidate();


                    }
                });

            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get json
        receiveJsonFromNetwork(false);

    }

    private void receiveJsonFromNetwork(final boolean refresh) {
        RequestQueue queue = VolleyController.getInstance(getActivity()).getRequestQueue();
        String url = "http://10.0.3.2:8080/mywebapps/myjson.txt";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                //Toast.makeText(getActivity(),"网络连接正常"+response.substring(0,23), Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Message listMsg = new Message();
                        listMsg.what = RECEIVE_JSON;
                        ArrayList<ArrayList<String>> dataSet =  new ArrayList<>();
                        dataSet.add(handleResponseWithKey(response, "MyTeaPot"));
                        dataSet.add(handleResponseWithKey(response, "TeapotName"));
                        dataSet.add(handleResponseWithKey(response, "TeapotBrief"));

                        listMsg.obj = dataSet;
                        handler.sendMessage(listMsg);

                        if (!refresh) {
                            Message headMsg = new Message();
                            headMsg.what = UPDATE_FIRST_COLUMN;
                            headMsg.obj = handleResponseWithKey(response,"HeadImages");
                            handler.sendMessage(headMsg);
                        }


                    }
                }).start();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //由于文字没有缓存，所以连接错误直接全部不显示
                Toast.makeText(getActivity(),"网络连接错误", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String str = new String(response.data, "UTF-8");
                    return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (Exception je) {
                    return Response.error(new ParseError(je));
                }
            }
        };
        queue.add(stringRequest);
    }

    private ArrayList<String> handleResponseWithKey(String response, String key) {
        ArrayList<String> myImageDataSet = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray(key);
            myImageDataSet.clear();
            for (int i =0; i < jsonArray.length(); i ++) {
                myImageDataSet.add((String) jsonArray.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myImageDataSet;
    }


}
