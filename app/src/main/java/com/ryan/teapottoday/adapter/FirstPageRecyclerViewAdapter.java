package com.ryan.teapottoday.adapter;

import android.animation.Animator;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryan.teapottoday.ContentActivity;
import com.ryan.teapottoday.MyApplication;
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
    private ArrayList<String> mImgDataSet;
    private ArrayList<String> mNameDataSet;
    private ArrayList<String> mBriefDataSet;
    private Context mContext;
    ArrayList<ArrayList<String>> mDataset;

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private boolean debug = false;

    public static final int MY_IMG_DATA_SET_NUM = 0;
    public static final int MY_NAME_DATA_SET_NUM = 1;
    public static final int MY_BRIEF_DATA_SET_NUM = 2;

    private int lastAnimatedPosition = -1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView mCardView;
        public IMyViewHolderClicks mListener;
        private ImageView ivFavorite;
        private ImageView ivInCardView;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvDate;

        public ViewHolder(CardView v, IMyViewHolderClicks listener) {
            super(v);



            mListener = listener;
            mCardView = v;
            ivFavorite = (ImageView) v.findViewById(R.id.fav_in_cv);
            ivInCardView = (ImageView) v.findViewById(R.id.iv_in_cv);
            tvTitle = (TextView) v.findViewById(R.id.tv_title_in_cv);
            tvContent = (TextView) v.findViewById(R.id.tv_content_in_cv);
            tvDate = (TextView) v.findViewById(R.id.tv_date_card);

            ivFavorite.setOnClickListener(this);

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
    public FirstPageRecyclerViewAdapter(Activity context, ArrayList<ArrayList<String>> myDataset) {
        mContext = context;
        mDataset = myDataset;

        if (0!= mDataset.size()) {
            mImgDataSet = mDataset.get(MY_IMG_DATA_SET_NUM);
            mNameDataSet = mDataset.get(MY_NAME_DATA_SET_NUM);
            mBriefDataSet = mDataset.get(MY_BRIEF_DATA_SET_NUM);
        }

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

                Animator animator = ViewAnimationUtils.createCircularReveal(
                        caller,
                        caller.getWidth() / 2,
                        caller.getHeight() / 2,
                        0,
                        caller.getWidth());
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(1000);
                animator.start();

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
                if (url != "null") {
                    Intent intent = new Intent(mContext, ContentActivity.class);
                    intent.putExtra("url",url);
                    if (debug) {
                        Log.e("url",url);
                    }
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, cardView.findViewById(R.id.iv_in_cv), mContext.getString(R.string.transition_first_img));
                    //((Activity) mContext).startActivityForResult(intent, 1);
                    ActivityCompat.startActivityForResult((Activity) mContext, intent, 1, optionsCompat.toBundle());
                }

            }
        });
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        runEnterAnimation(holder.itemView, position);


        //暴力解决了复用导致的界面布局混乱，但是牺牲了效率
        holder.setIsRecyclable(false);


        CardView cvTop = holder.mCardView;
        TextView tvTitle = holder.tvTitle;
        TextView tvContent = holder.tvContent;
        TextView tvDate = holder.tvDate;

        ImageView ivPot = holder.ivInCardView;

        ImageView ivFav = holder.ivFavorite;

        String url = "";
        if (position == 0) {

//            int dp = DensityUtil.px2dip(mContext,800);
//            Log.e("dpdpdp",dp+"");
            int px = DensityUtil.dip2px(mContext, 400);
            cvTop.getLayoutParams().height = px;
            cvTop.setAlpha(0);

            url = "null";
            cvTop.setTag(R.string.url_tag, url);
        } else {

            if ((null !=mNameDataSet) && (null!=mBriefDataSet)) {
                tvTitle.setText(mNameDataSet.get(position-1));
                tvContent.setText(mBriefDataSet.get(position-1));
            }

            if (!"null".equals(url)) {
                url = MyApplication.CONTENT + "content_detail1/shengtao01.jpg";
            }

            if (null != mImgDataSet) {
                 url = MyApplication.CONTENT + mImgDataSet.get(position - 1);
            }

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
        }

    }

    private void runEnterAnimation(View view, int position) {
        if (position!=1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(800);
            view.animate()
                    .translationY(0)
                    .setStartDelay(100 * position)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    private void setImageView(String url, ImageView ivPot, int position) {


        Bitmap defaultImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_img);
        ImageCacheManager.loadImage(mContext, url, ivPot, defaultImage, defaultImage);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        if (null != mImgDataSet) {
            return mImgDataSet.size() + 1;
        } else {
            return 0;
        }

    }

    public void refresh (ArrayList<ArrayList<String>> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }
}