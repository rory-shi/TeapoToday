package com.ryan.teapottoday.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ryan.teapottoday.CalendarActivity;
import com.ryan.teapottoday.ContentActivity;
import com.ryan.teapottoday.R;
import com.ryan.teapottoday.database.MyDatabaseHelper;
import com.ryan.teapottoday.model.ImageCacheManager;
import com.ryan.teapottoday.utils.DateUtils;
import com.ryan.teapottoday.utils.DensityUtil;

import java.util.ArrayList;

/**
 * Created by rory9 on 2016/4/3.
 */
public class FirstPageRecyclerViewAdapter extends RecyclerView.Adapter<FirstPageRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> mDataset;
    public static int HELLO_ITEM_HEIGHT = 335;
    private Context mContext;

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private boolean debug = false;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView mCardView;
        public IMyViewHolderClicks mListener;
        private ImageView ivFavorite;
        private ImageView ivInCardView;


        public ViewHolder(CardView v, IMyViewHolderClicks listener) {
            super(v);


            mListener = listener;
            mCardView = v;
            ivFavorite = (ImageView) v.findViewById(R.id.fav_in_cv);

            ivFavorite.setOnClickListener(this);
            ivInCardView = (ImageView) v.findViewById(R.id.iv_in_cv);
            ivInCardView.setOnClickListener(this);
            //v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fav_in_cv:
                    mListener.onFavImgClicked(v);
                    break;
                default:
                case R.id.iv_in_cv:
                    mListener.onImgClicked(v);
                    break;
            }
        }

        public interface IMyViewHolderClicks {
            void onFavImgClicked(View caller);

            void onImgClicked(View caller);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public FirstPageRecyclerViewAdapter(Activity context, ArrayList<String> myDataset) {
        mContext = context;
        mDataset = myDataset;


        dbHelper = new MyDatabaseHelper(mContext,"TeapotToday.db",null,2);
        db = dbHelper.getWritableDatabase();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FirstPageRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {


        // create a new view
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(cv, new FirstPageRecyclerViewAdapter.ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onFavImgClicked(View caller) {
                CardView cardView = (CardView) caller.getParent().getParent();
                String url = (String) cardView.getTag(R.string.url_tag);
                //Toast.makeText(mContext,url,Toast.LENGTH_LONG).show();

                //save to SHaredPreference
               /* SharedPreferences.Editor editor = mContext.getSharedPreferences("data",
                        Context.MODE_PRIVATE).edit();
                editor.putString("url", url);
                editor.commit();*/



              /*  Cursor cursor = db.query("Teapot", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    //此处表明存在该url，

                }
                cursor.close();*/

                int row = db.delete("Teapot", "url = ?", new String[]{url});
                caller.setBackgroundResource(R.drawable.ic_favorite_border);
                if (row == 0) {
                    ContentValues values = new ContentValues();
                    values.put("url",url);
                    db.insert("Teapot", null, values);
                    caller.setBackgroundResource(R.drawable.ic_favorite);
                }
            }

            @Override
            public void onImgClicked(View caller) {
                if (debug) {
                    Log.e("debug", "on img clicked");
                }

                CardView cardView = (CardView) caller.getParent().getParent();
                String url = (String) cardView.getTag(R.string.url_tag);
                Intent intent = new Intent(mContext, ContentActivity.class);
                intent.putExtra("url",url);
                if (debug) {
                    Log.e("url",url);
                }
                ((Activity)mContext).startActivityForResult(intent, 1);
            }
        });
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //暴力解决了复用导致的界面布局混乱，但是牺牲了效率
        holder.setIsRecyclable(false);



        CardView cvTop = holder.mCardView;
        TextView tvTitle = (TextView) cvTop.findViewById(R.id.tv_title_in_cv);
        TextView tvContent = (TextView) cvTop.findViewById(R.id.tv_content_in_cv);
        final ImageView ivPot = (ImageView) cvTop.findViewById(R.id.iv_in_cv);
        TextView tvDate = (TextView) cvTop.findViewById(R.id.tv_date_card);
        ImageView ivFav = (ImageView) cvTop.findViewById(R.id.fav_in_cv);
        if (position == 0) {

//            int dp = DensityUtil.px2dip(mContext,800);
//            Log.e("dpdpdp",dp+"");
            int px = DensityUtil.dip2px(mContext, 400);
            cvTop.getLayoutParams().height = px;
            cvTop.setAlpha(0);
        } else {
            if (position == 1) {
                int px = DensityUtil.dip2px(mContext, 168);
                cvTop.getLayoutParams().height = px;
                ivPot.setVisibility(View.GONE);
                tvDate.setVisibility(View.GONE);
               // ivFav.setVisibility(View.GONE);

                tvTitle.setText(DateUtils.getToday("gregorian") + DateUtils.getToday("week"));
                tvTitle.setTextSize(32);

                tvContent.setTextSize(22);
                tvContent.setText("农历" + DateUtils.getToday("lunar"));

            }
            String url = "http://10.0.3.2:8080/mywebapps/" + mDataset.get(position - 1);
            cvTop.setTag(R.string.url_tag, url);
            ivPot.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //get img from net
            setImageView(url, ivPot, position);

            tvDate.setText(DateUtils.getDateAgo("", position - 1));
//            tvDate.setText(mDateSet[position - 2]);

            Cursor cursor = db.query("Teapot", null, "url=?", new String[]{url}, null, null, null, null);
            if (cursor.moveToFirst()) {
                ivFav.setBackgroundResource(R.drawable.ic_favorite);
            } else {
                ivFav.setBackgroundResource(R.drawable.ic_favorite_border);
            }
            cursor.close();
        }

    }

    private void setImageView(String url, ImageView ivPot, int position) {
        /*RequestQueue mQueue = Volley.newRequestQueue(mContext);
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapLruImageCache(10*1024*1024));
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(ivPot, R.drawable.demo, R.drawable.hee);
        imageLoader.get("http://10.0.3.2:8080/mywebapps/" + mDataset.get(position - 2), listener, 200, 200);*/


        Bitmap defaultImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_img);
        Bitmap errorImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_img);
        ImageCacheManager.loadImage(mContext, url, ivPot, defaultImage, errorImage);


    }

    /*private void setImageView(final ImageView ivPot,int position) {
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
            ImageRequest imageRequest = new ImageRequest("http://10.0.3.2:8080/mywebapps/" + mDataset.get(position - 2), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    ivPot.setImageBitmap(response);
                }
            }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ivPot.setImageResource(R.drawable.demo);
                }
            });
            mQueue.add(imageRequest);
    }*/


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }
}