package com.ryan.teapottoday.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ryan.teapottoday.R;
import com.ryan.teapottoday.adapter.GridViewImageAdapter;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by rory9 on 2016/4/6.
 */
public class CollectionFragment extends Fragment implements AbsListView.MultiChoiceModeListener{

    private GridView mGridView;
    private GridViewImageAdapter mGridViewAdapter;
    private Toolbar mToolBar;

    private TextView mActionText;

    private HashMap<Integer, Boolean> mSelectMap = new HashMap<>();

    private static final int MENU_SELECT_ALL = 0;
    private static final int MENU_SELECT_NONE = MENU_SELECT_ALL + 1;


    public CollectionFragment (){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_collection,container,false);
        mGridView = (GridView) view.findViewById(R.id.grid_collection);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mGridViewAdapter =  new GridViewImageAdapter(getActivity(),mSelectMap);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setMultiChoiceModeListener(this);
       // mGridView.setOnClickListener();

//        mToolBar.startActionMode(this);
        return view;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        mActionText.setText(formatString(mGridView.getCheckedItemCount()));
        mSelectMap.put(position,checked);
        mode.invalidate();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.actionbar_layout,null);
        mActionText = (TextView) v.findViewById(R.id.action_text);
        mActionText.setText(formatString(mGridView.getCheckedItemCount()));
        mode.setCustomView(v);
        getActivity().getMenuInflater().inflate(R.menu.fragment_collection, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.getItem(MENU_SELECT_ALL).setEnabled(
                mGridView.getCheckedItemCount() != mGridView.getCount());
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_select_all:
                for (int i = 0; i < mGridView.getCount(); i++) {
                    mGridView.setItemChecked(i, true);
                    mSelectMap.put(i, true);
                }
                break;
            case R.id.menu_select_none:
                for (int i = 0; i < mGridView.getCount(); i++) {
                    mGridView.setItemChecked(i, false);
                    mSelectMap.clear();
                }
                break;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mGridViewAdapter.notifyDataSetChanged();
    }

    private String formatString(int count) {
        return String.format(getString(R.string.selection),count);
    }

}
