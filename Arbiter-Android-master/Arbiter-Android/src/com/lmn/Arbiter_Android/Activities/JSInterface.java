package com.lmn.Arbiter_Android.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.webkit.JavascriptInterface;

/**
 * Created by JiJungKeun on 2016-07-28.
 */

/* This class is used for communicating image data between android and js */
public class JSInterface {

    Context mContext;
    private String sfName = "imgData";

    JSInterface(Context c){
        mContext = c;
    }

    @JavascriptInterface
    public void SavePreferences(String key, float value){

        SharedPreferences addData = mContext.getSharedPreferences(sfName, 0);
        SharedPreferences.Editor editor = addData.edit();

        editor.putFloat(key, value);
        editor.commit();

    }

    @JavascriptInterface
    public String LoadPreferences(String key){
        SharedPreferences loadData = mContext.getSharedPreferences(sfName, 0);
        return loadData.getString(key, "");
    }

    @JavascriptInterface
    public int LoadPreferencesSize(String key){
        SharedPreferences loadData = mContext.getSharedPreferences(sfName, 0);
        return loadData.getInt(key, 0);
    }

    @JavascriptInterface
    public float LoadPreferencesBoundary(String key){
        SharedPreferences loadData = mContext.getSharedPreferences(sfName, 0);
        return loadData.getFloat(key, 0);
    }
}
