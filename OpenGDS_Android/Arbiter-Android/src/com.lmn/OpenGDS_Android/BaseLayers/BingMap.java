package com.lmn.OpenGDS_Android.BaseLayers;

import android.app.Activity;

import com.lmn.Arbiter_Android.R;

import java.util.ArrayList;

/**
 * BingMap 설정
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class BingMap {

    private ArrayList<String> baseLayers = new ArrayList<String>();
    private Activity activity;

    /**
     * @param activity Activity
     * @author JiJungKeun
     */
    public BingMap(Activity activity) {
        this.activity = activity;
    }

    public void setBingRoad() {
        baseLayers.add(activity.getResources().getString(R.string.action_baseMap_BingRoad));
    }

    public void setBingAerial() {
        baseLayers.add(activity.getResources().getString(R.string.action_baseMap_BingAerial));
    }

    public void setBingAerialWithLabels() {
        baseLayers.add(activity.getResources().getString(R.string.action_baseMap_BingAerialWithLabels));
    }

    /**
     * 설정한 BingMap 리턴
     *
     * @return ArrayList<String>
     * @author JiJungKeun
     */
    public ArrayList<String> getBingMap() {
        return baseLayers;
    }
}
