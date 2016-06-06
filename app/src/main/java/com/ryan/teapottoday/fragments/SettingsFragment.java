package com.ryan.teapottoday.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mikepenz.aboutlibraries.Libs;
import com.ryan.teapottoday.R;
import com.ryan.teapottoday.utils.DataCleanUtils;

import io.github.xhinliang.lunarcalendar.Main;

/**
 * Created by rory9 on 2016/5/30.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.preference_head,container,false);
        //Set night-mode or other UI changes

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_general);

        findPreference("cache_all").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("清除所有缓存？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanUtils.cleanDatabases(getActivity());
                        DataCleanUtils.cleanInternalCache(getActivity());
                        Toast.makeText(getActivity(), "已清除所有缓存。", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                /*builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });*/
                builder.create().show();


                return false;
            }
        });

        findPreference("cache_img").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("清除所有缓存（不含收藏夹）？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanUtils.cleanInternalCache(getActivity());
                        Toast.makeText(getActivity(), "已清除收藏夹外所有缓存。", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                /*builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });*/
                builder.create().show();
                return false;
            }
        });

        findPreference("about").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        //config About Library
                        new Libs.Builder().withActivityTitle("About").withFields(R.string.class.getFields()).start(getActivity());
                        return false;
                    }
                });
        findPreference("feedback").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.setType("message/rfc822");
                        email.putExtra(Intent.EXTRA_EMAIL, new String[] {"touring311@qq.com"});
                        email.putExtra(Intent.EXTRA_SUBJECT, "每日一壶应用反馈");
                        //email.putExtra(Intent.EXTRA_TEXT   , "Did you get this mail? if so please reply back");
                        startActivity(Intent.createChooser(email, "Choose an Email Client"));
                        return false;
                    }
                });
    }
}
