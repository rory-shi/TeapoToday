package com.ryan.teapottoday.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryan.teapottoday.R;

public class ProductionRecyclerViewAdapter extends RecyclerView.Adapter<ProductionRecyclerViewAdapter.ViewHolder> {

    private int lastAnimatedPosition = -1;
    private int[] mDataset = {R.drawable.pd_tools,R.drawable.pd1,R.drawable.pd2,R.drawable.pd3,R.drawable.pd4,R.drawable.pd5,R.drawable.pd6,
            R.drawable.pd7,R.drawable.pd8,R.drawable.pd9,R.drawable.pd10,R.drawable.pd11,R.drawable.pd12,R.drawable.pd13,
            R.drawable.pd14,R.drawable.pd15,R.drawable.pd16,R.drawable.pd17,R.drawable.pd18,R.drawable.pd19,R.drawable.pd20_0,
            R.drawable.pd20_1,R.drawable.pd22,R.drawable.pd23,R.drawable.pd25,R.drawable.pd26,R.drawable.pd_complete,};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView mImageView = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.production_item,parent,false);
        return new ViewHolder(mImageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        runEnterAnimation(holder.itemView,position);

        ImageView mImg = holder.mImageView;
        mImg.setImageResource(mDataset[position]);
        mImg.setAdjustViewBounds(true);
        mImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private void runEnterAnimation(View view, int position) {


        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(400);
            view.animate()
                    .translationY(0)
                    .setStartDelay(100 * position)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(350)
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;

        public ViewHolder(ImageView v) {
            super(v);
            mImageView = v;
        }
    }
}
