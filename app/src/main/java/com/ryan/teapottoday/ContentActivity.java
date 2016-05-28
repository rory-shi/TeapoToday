package com.ryan.teapottoday;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.joooonho.SelectableRoundedImageView;
import com.ryan.teapottoday.adapter.MyVPContentPagerAdapter;
import com.ryan.teapottoday.diyView.PullToZoomScrollView;
import com.ryan.teapottoday.model.ImageCacheManager;
import com.ryan.teapottoday.model.VolleyController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ContentActivity extends Activity {

    private ViewPager vpContent;
    private float mLastY;
    private PullToZoomScrollView svContent;
    private MyVPContentPagerAdapter myVPContentPagerAdapter;
    private static final int RECEIVE_JSON = 2;
    private ArrayList<String> teapotImgsList;
    private String artimg;
    private String dirtImg;
    private SelectableRoundedImageView imgArtisan;
    private SelectableRoundedImageView imgDirt;
    private TextView tvDirt;
    private TextView tvArtisan;
    private TextView tvDirtName;
    private TextView tvArtisanName;
    private Button btnBack;
    private boolean debug = false;
    private String[] arrs;

    private String contentDir = "http://10.0.3.2:8080/mywebapps/content_detail2";


    @SuppressWarnings("unchecked")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_JSON: {

                    if (msg.obj instanceof  ArrayList) {
                        teapotImgsList = (ArrayList<String>) msg.obj;
                    }

                    String artImg = teapotImgsList.get(0);
                    String dirtImg = teapotImgsList.get(1);
                    String teapotIntro = teapotImgsList.get(2);
                    String dirtIntro = teapotImgsList.get(3);
                    String artisanIntro = teapotImgsList.get(4);
                    String dirtName = teapotImgsList.get(5);
                    String artisanName = teapotImgsList.get(6);

                    Bitmap defaultImage = BitmapFactory.decodeResource(ContentActivity.this.getResources(), R.drawable.demo);
                    Bitmap errorImage = BitmapFactory.decodeResource(ContentActivity.this.getResources(), R.drawable.hee);
                    ImageCacheManager.loadImage(ContentActivity.this, contentDir + artImg, imgArtisan, defaultImage, errorImage);
                    ImageCacheManager.loadImage(ContentActivity.this, contentDir + dirtImg, imgDirt, defaultImage, errorImage);

                    tvDirt.setText(dirtIntro);
                    tvArtisan.setText(artisanIntro);
                    tvDirtName.setText(dirtName);
                    tvArtisanName.setText(artisanName);

                    vpContent.setAdapter(new MyVPContentPagerAdapter(ContentActivity.this, contentDir, teapotImgsList));
                    break;
                }
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_layout);

        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intentFrom = getIntent();
        String url = intentFrom.getStringExtra("url");
        if (url.length()>0) {
            arrs = url.split("/");
        }


        contentDir = url.replace(arrs[arrs.length - 1], "");
        if (BuildConfig.DEBUG) {
            Log.e("debug", url);
            Log.e("debug", arrs[arrs.length - 1]);
            Log.e("debug", contentDir);
        }


        imgArtisan = (SelectableRoundedImageView) findViewById(R.id.detail_round_img_artisan);
        imgDirt = (SelectableRoundedImageView) findViewById(R.id.detail_round_img_dirt);
        tvDirt = (TextView) findViewById(R.id.tv_dirt_intro);
        tvArtisan = (TextView) findViewById(R.id.tv_artisan_intro);
        tvDirtName = (TextView) findViewById(R.id.tv_dirt_name);
        tvArtisanName = (TextView) findViewById(R.id.tv_artisan_name);

        teapotImgsList = new ArrayList<>();
        receiveJsonFromNetwork();


        vpContent = (ViewPager) findViewById(R.id.vp_content);
        svContent = (PullToZoomScrollView) findViewById(R.id.sv_content);
//        myVPContentPagerAdapter = new MyVPContentPagerAdapter(this,contentDir,teapotImgsList);
//        vpContent.setAdapter(myVPContentPagerAdapter);
    }

    private void receiveJsonFromNetwork() {
        RequestQueue queue = VolleyController.getInstance(this).getRequestQueue();
        String url = contentDir + "content.txt";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                if (debug) {
                    Log.e("jsonresponse", response);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> imgsList = handleResponse(response);

                        Message msg = new Message();
                        msg.what = RECEIVE_JSON;
                        msg.obj = imgsList;
                        handler.sendMessage(msg);
                    }
                }).start();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //由于文字没有缓存，所以连接错误直接全部不显示
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

    private ArrayList<String> handleResponse(String response) {

        try {
            response = new String(response.getBytes(), "UTF-8");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("TeaPotToday content data");
            JSONObject jsonImg = (JSONObject) jsonArray.get(0);
            artimg = (String) jsonImg.get("artisan");
            dirtImg = (String) jsonImg.get("dirt");
            JSONArray teapotImgs = jsonImg.getJSONArray("teapot");
            teapotImgsList.add(artimg);
            teapotImgsList.add(dirtImg);
            JSONObject jsonText = (JSONObject) jsonArray.get(1);
            String teapotIntro = jsonText.getString("teapot_intro");
            String dirtIntro = jsonText.getString("dirt_intro");
            String artisanIntro = jsonText.getString("artisan_intro");
            String artisanName = jsonText.getString("artisan_name");
            String dirtName = jsonText.getString("dirt_name");
            teapotImgsList.add(teapotIntro);
            teapotImgsList.add(dirtIntro);
            teapotImgsList.add(artisanIntro);
            teapotImgsList.add(dirtName);
            teapotImgsList.add(artisanName);

            if (debug) {
                Log.e("jsonresponse", teapotIntro);
                Log.e("jsonresponse", dirtIntro);
            }


            for (int i = 0; i < teapotImgs.length(); i++) {
                teapotImgsList.add((String) teapotImgs.get(i));
                if (debug) {
                    Log.e("jsonresponse", (String) teapotImgs.get(i));
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return teapotImgsList;
    }

    //50是顶栏的高度
   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getY()<vpContent.getHeight()+100) {
            return super.dispatchTouchEvent(ev);
        }
        return onTouchEvent(ev);
    }*/

    /*public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) vpContent.getLayoutParams();
        int mHeight;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                mLastY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float y = event.getY();

                float delta = y - mLastY;

                float alphaDelta = (y - mLastY) / 6000;


                mHeight = vpContent.getHeight();
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
                if (lp.height>400 ){
                    lp.height = 400;
                }
                break;
            }
            default:
                break;
        }
        return true;
    }*/

}
