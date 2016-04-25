package com.ryan.teapottoday.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryan.teapottoday.ProductionRViewLLManager;
import com.ryan.teapottoday.R;
import com.ryan.teapottoday.adapter.ProductionRecyclerViewAdapter;


/**
 * Created by rory9 on 2016/4/9.
 */
public class ProductionFragment extends Fragment{
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ProductionRecyclerViewAdapter mRVAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragement_production,container,false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.production_recycler_view);
        // use a linear layout manager
        //mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
       // mLayoutManager.setAutoMeasureEnabled(false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mRVAdapter = new ProductionRecyclerViewAdapter();
        mRecyclerView.setAdapter(mRVAdapter);
        return v;
    }



}
