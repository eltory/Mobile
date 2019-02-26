package com.lmn.OpenGDS_Android.BaseLayers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.Map.Map_Expansion;

import org.apache.cordova.CordovaWebView;

import java.util.ArrayList;

/**
 * 베이스 레이어 추가 및 변경
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class BaseLayers {
    private ArrayList<String> baseLayers = new ArrayList<String>();
    private Activity activity;
    private CordovaWebView cordovaWebView;

    /**
     * @param activity,cordovaWebView Activity,CordovaWebView
     * @author JiJungKeun
     */
    public BaseLayers(Activity activity, CordovaWebView cordovaWebView) {
        this.activity = activity;
        this.cordovaWebView = cordovaWebView;

        baseLayers.add(activity.getResources().getString(R.string.action_baseMap_OpenStreetMap));
    }

    /**
     * ArrayList에 설정한 BingMap 추가
     *
     * @param bing BingMap
     * @return void
     * @author JiJungKeun
     */
    public void addBingMap(BingMap bing) {
        ArrayList<String> bingMap = bing.getBingMap();
        for (int i = 0; i < bingMap.size(); i++) {
            baseLayers.add(bingMap.get(i));
        }
    }

    /**
     * 베이스 레이어 변경 다이얼로그
     *
     * @return void
     * @author JiJungKeun
     */
    public void showDialog() {
        final String[] layers = new String[baseLayers.size()];
        for (int i = 0; i < baseLayers.size(); i++) {
            layers[i] = baseLayers.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(R.string.action_baseLayers)
                .setSingleChoiceItems(layers, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        Map_Expansion.getMap().baseLayers(cordovaWebView, layers[id]);
                        dialog.dismiss();
                    }
                });

        // create dialog
        builder.setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(R.drawable.icon)
                .create()
                .show();
    }
}
