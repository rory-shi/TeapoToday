package com.ryan.teapottoday.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ryan.teapottoday.R;
import com.ryan.teapottoday.adapter.GridViewImageAdapter;
import com.ryan.teapottoday.database.MyDatabaseHelper;
import com.ryan.teapottoday.model.ImageCacheManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by rory9 on 2016/4/6.
 */
public class CollectionFragment extends Fragment implements AbsListView.MultiChoiceModeListener{

    private GridView mGridView;
    private GridViewImageAdapter mGridViewAdapter;
    private Toolbar mToolBar;
    private ArrayList<String> urls;
    private TextView mActionText;

    private HashMap<Integer, Boolean> mSelectMap = new HashMap<>();

    private static final int MENU_SELECT_ALL = 0;
    private static final int MENU_SELECT_NONE = MENU_SELECT_ALL + 1;


    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public CollectionFragment (){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urls = new ArrayList<>();

        dbHelper = new MyDatabaseHelper(getActivity(),"TeapotToday.db",null,2);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Teapot", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String url = cursor.getString(cursor.getColumnIndex("url"));
                urls.add(url);
            } while ( cursor.moveToNext());
        }
        cursor.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_collection,container,false);
        mGridView = (GridView) view.findViewById(R.id.grid_collection);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mGridViewAdapter =  new GridViewImageAdapter(getActivity(),mSelectMap,urls);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setMultiChoiceModeListener(this);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View imgEntryView = inflater.inflate(R.layout.content_image, null); // 加载自定义的布局文件
                ImageView ivContentDetail = (ImageView) imgEntryView.findViewById(R.id.iv_content_img);
                ivContentDetail.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivContentDetail.setMinimumWidth(getActivity().getResources().getDisplayMetrics().widthPixels+200);
                String url = urls.get(position);

                Bitmap defaultImage = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.default_img);
                Bitmap errorImage = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.default_img);
                ImageCacheManager.loadImage(getActivity(), url, ivContentDetail, defaultImage, errorImage);

                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

                //ImageView img = (ImageView) imgEntryView.findViewById(R.id.demo);
                //imageDownloader.download("图片地址", img); // 这个是加载网络图片的，可以是自己的图片设置方法
                dialog.setView(imgEntryView); // 自定义dialog

                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.CENTER);
                lp.width = getActivity().getResources().getDisplayMetrics().widthPixels+200; // 宽度
                //lp.height = 500; // 高度
                dialogWindow.setAttributes(lp);


                dialog.show();
                // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
                imgEntryView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        dialog.cancel();
                    }
                });
            }
        });

//        mToolBar.startActionMode(this);
        return view;
    }



    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        mActionText.setText(formatString(mGridView.getCheckedItemCount()));
        mSelectMap.put(position, checked);
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
                selectNone();

                break;
            case R.id.menu_delete:
                SparseBooleanArray array = mGridView.getCheckedItemPositions();
                //Log.e("tagagagagag",array+"");
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(getActivity(),"TeapotToday.db",null,2);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //从大到小删除，否则集合的顺序会乱
                for (int i=urls.size()-1;i>=0;i--) {
                    if (array.get(i)){
                        db.delete("Teapot", "url = ?", new String[]{urls.get(i)});
                        urls.remove(i);
                    }
                }

                mGridView.invalidate();
                selectNone();
                break;

        }
        return true;
    }

    private void selectNone() {
        for (int i = 0; i < mGridView.getCount(); i++) {
            mGridView.setItemChecked(i, false);
            mSelectMap.clear();
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mGridViewAdapter.notifyDataSetChanged();
    }

    private String formatString(int count) {
        return String.format(getString(R.string.selection),count);
    }



}
