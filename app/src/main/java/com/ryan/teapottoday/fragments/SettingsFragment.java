package com.ryan.teapottoday.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.aboutlibraries.Libs;
import com.ryan.teapottoday.R;
import com.ryan.teapottoday.utils.DataCleanUtils;

/**
 * Created by rory9 on 2016/5/30.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Set night-mode or other UI changes

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_general);

        findPreference("cache_all").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DataCleanUtils.cleanDatabases(getActivity());
                DataCleanUtils.cleanInternalCache(getActivity());
                return false;
            }
        });

        findPreference("cache_img").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DataCleanUtils.cleanInternalCache(getActivity());
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
    }
}
