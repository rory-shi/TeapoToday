package com.ryan.teapottoday.adapter;

import android.content.Context;
import android.content.Intent;
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

import com.ryan.teapottoday.ContentActivity;
import com.ryan.teapottoday.R;
import com.ryan.teapottoday.model.ImageCacheManager;

import java.util.ArrayList;

/**
 * Created by rory9 on 2016/4/3.
 */
public class FirstPageRecyclerViewAdapter extends RecyclerView.Adapter<FirstPageRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> mDataset;
    private String[] mDateSet = {"4月13日\n三月初七", "4月12日\n三月初六", "4月11日\n三月初五", "4月10日\n三月初四", "4月9日\n三月初三", "4月8日\n三月初二", "4月7日\n三月初一", "4月6日\n二月廿九",};
    public static int HELLO_ITEM_HEIGHT = 335;
    private Context mContext;

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

        public static interface IMyViewHolderClicks {
            public void onFavImgClicked(View caller);

            public void onImgClicked(View caller);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public FirstPageRecyclerViewAdapter(Context context, ArrayList<String> myDataset) {
        mContext = context;
        mDataset = myDataset;
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
                caller.setBackgroundResource(R.drawable.ic_favorite);
            }

            @Override
            public void onImgClicked(View caller) {
                Log.e("mytag", "onimg clicked");
                Intent intent = new Intent(mContext, ContentActivity.class);
                mContext.startActivity(intent);
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

            cvTop.getLayoutParams().height = 800;
            cvTop.setAlpha(0);
        } else if (position == 1) {
            cvTop.getLayoutParams().height = HELLO_ITEM_HEIGHT;
            ivPot.setVisibility(View.GONE);
            tvDate.setVisibility(View.GONE);
            ivFav.setVisibility(View.GONE);

            tvTitle.setText("4月13日，星期三");
            tvTitle.setTextSize(32);

            tvContent.setTextSize(22);
            tvContent.setText("农历三月初七");

        } else {
            ivPot.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //get img from net
            setImageView(ivPot, position);

            tvDate.setText(mDateSet[position - 2]);

        }

    }

    private void setImageView(ImageView ivPot, int position) {
        /*RequestQueue mQueue = Volley.newRequestQueue(mContext);
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapLruImageCache(10*1024*1024));
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(ivPot, R.drawable.demo, R.drawable.hee);
        imageLoader.get("http://10.0.3.2:8080/mywebapps/" + mDataset.get(position - 2), listener, 200, 200);*/

        Bitmap defaultImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.demo);
        Bitmap errorImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.hee);
        ImageCacheManager.loadImage(mContext, "http://10.0.3.2:8080/mywebapps/" + mDataset.get(position - 2), ivPot, defaultImage, errorImage);

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