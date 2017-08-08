package com.example.admin.bookapp.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

import com.example.admin.bookapp.R;



public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String TAG = "SETTINGS_FRAGMENT_TAG";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        Log.e(TAG, "onCreatePreferences: got here "+count);
        for (int i=0; i<count; i++){
            Preference p = preferenceScreen.getPreference(i);
           if (p instanceof PreferenceCategory){
               PreferenceCategory pcat = (PreferenceCategory)p;
               int pcount = pcat.getPreferenceCount();
               for (int j=0; j<pcount; j++){
                   Preference pref = pcat.getPreference(j);
                   if (pref instanceof ListPreference) {
                        String value = sharedPreferences.getString(pref.getKey(), "");
                        setPreferenceSummary(pref, value);
                   }
               }
           }
        }
    }




    @Override

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (null != preference) {
            if (preference instanceof ListPreference) {
                Log.e(TAG, "onSharedPreferenceChanged: got here");
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
                Log.e(TAG, "onSharedPreferenceChanged: " + value);
            }
        }
    }




    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
                Log.e(TAG, "setPreferenceSummary: pref index" +prefIndex);
            }
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
