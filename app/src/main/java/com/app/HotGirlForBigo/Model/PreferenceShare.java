package com.app.hotgirlforbigo.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nguyennam on 2/22/17.
 */

public class PreferenceShare {

    SharedPreferences preferences;
    Context context;
    private SharedPreferences.Editor sharedEditor;

    public PreferenceShare(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

//Public setters
    /**
     * Set a preference string value
     * @param key the preference key to set
     * @param value the value for this key
     */
    public void setPreferenceStringValue(String key, String value) {
        if(sharedEditor == null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.commit();
        }else {
            sharedEditor.putString(key, value);
        }
    }

    /**
     * Set a preference boolean value
     * @param key the preference key to set
     * @param value the value for this key
     */
    public void setPreferenceBooleanValue(String key, boolean value) {
        if(sharedEditor == null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }else {
            sharedEditor.putBoolean(key, value);
        }
    }

    /**
     * Set a preference float value
     * @param key the preference key to set
     * @param value the value for this key
     */
    public void setPreferenceFloatValue(String key, float value) {
        if(sharedEditor == null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat(key, value);
            editor.commit();
        }else {
            sharedEditor.putFloat(key, value);
        }
    }

    //Private static getters
    // For string
    private static String gPrefStringValue(SharedPreferences aPrefs, String key) {
        return aPrefs.getString(key, (String) null);
    }

    // For boolean
    private static Boolean gPrefBooleanValue(SharedPreferences aPrefs, String key) {
        if(aPrefs.contains(key)) {
            return aPrefs.getBoolean(key, false);
        }
        return null;
    }

    // For float
    private static Float gPrefFloatValue(SharedPreferences aPrefs, String key) {
        if(aPrefs.contains(key)) {
            return aPrefs.getFloat(key, 0.0f);
        }
        return null;
    }

    /**
     * Get string preference value
     * @param key the key preference to retrieve
     * @return the value
     */
    public String getPreferenceStringValue(String key) {
        return gPrefStringValue(preferences, key);
    }

    /**
     * Get boolean preference value
     * @param key the key preference to retrieve
     * @return the value
     */
    public Boolean getPreferenceBooleanValue(String key) {
        return gPrefBooleanValue(preferences, key);
    }

    /**
     * Get float preference value
     * @param key the key preference to retrieve
     * @return the value
     */
    public Float getPreferenceFloatValue(String key) {
        return gPrefFloatValue(preferences, key);
    }

}
