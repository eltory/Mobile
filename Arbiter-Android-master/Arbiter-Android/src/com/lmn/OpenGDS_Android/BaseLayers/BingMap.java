package com.lmn.OpenGDS_Android.BaseLayers;

import android.app.Activity;

import com.lmn.Arbiter_Android.R;

import java.util.ArrayList;

/**
 * Created by JiJungKeun on 2016-11-22.
 */
public class BingMap {

    private ArrayList<String> baseLayers = new ArrayList<String>();
    private Activity activity;

    public BingMap(Activity activity)
    {
        this.activity = activity;
    }

    public void setBingRoad()
    {
        baseLayers.add(activity.getResources().getString(R.string.action_baseMap_BingRoad));
    }

    public void setBingAerial()
    {
        baseLayers.add(activity.getResources().getString(R.string.action_baseMap_BingAerial));
    }

    public void setBingAerialWithLabels()
    {
        baseLayers.add(activity.getResources().getString(R.string.action_baseMap_BingAerialWithLabels));
    }

    public ArrayList<String> getBingMap()
    {
        return baseLayers;
    }
}
