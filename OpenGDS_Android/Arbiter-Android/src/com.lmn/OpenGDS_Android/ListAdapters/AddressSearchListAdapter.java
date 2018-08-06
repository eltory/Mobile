package com.lmn.OpenGDS_Android.ListAdapters;

import java.util.ArrayList;
import java.util.List;

import com.lmn.Arbiter_Android.ArbiterProject;
import com.lmn.Arbiter_Android.ListAdapters.CustomList;
import com.lmn.Arbiter_Android.R;
import com.lmn.Arbiter_Android.Activities.HasThreadPool;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lmn.OpenGDS_Android.Map.Map_Expansion;

import org.apache.cordova.CordovaWebView;

/**
 * 주소 검색 리스트 어댑터
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class AddressSearchListAdapter extends CustomList<ArrayList<String>, String> {

    private LayoutInflater inflater;
    private int itemLayout;
    private Activity activity;
    private Context context;
    private ArbiterProject arbiterProject;
    private HasThreadPool hasThreadPool;
    private CordovaWebView cordovaWebView;
    private List<Address> location;
    private DialogFragment dialog;

    /**
     * @param viewGroup,activity,dialog,itemLayout,hasThreadPool,cordovaWebView,location ViewGroup,Activity,DialogFragment,int,HasThreadPool,CordovaWebView,List<Address>
     * @author JiJungKeun
     */
    public AddressSearchListAdapter(ViewGroup viewGroup, Activity activity, DialogFragment dialog, int itemLayout, HasThreadPool hasThreadPool, CordovaWebView cordovaWebView, List<Address> location) {
        super(viewGroup);

        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.inflater = LayoutInflater.from(this.context);
        this.itemLayout = itemLayout;
        this.arbiterProject = ArbiterProject.getArbiterProject();
        this.hasThreadPool = hasThreadPool;
        this.cordovaWebView = cordovaWebView;
        this.location = location;
        this.dialog = dialog;

    }

    @Override
    public void setData(ArrayList<String> address) {
        super.setData(address);
    }

    @Override
    public int getCount() {
        return getData().size();
    }

    @Override
    public String getItem(int index) {
        return getData().get(index);
    }

    @Override
    public View getView(final int position) {

        View view = inflater.inflate(itemLayout, null);

        final String resultItem = getItem(position);

        if (resultItem != null) {

            RelativeLayout searchResultContainer = (RelativeLayout) view.findViewById(R.id.AddressSearchResult);
            TextView searchResultNameView = (TextView) view.findViewById(R.id.AddressName);

            if (searchResultNameView != null) {
                searchResultNameView.setText(getItem(position));

                searchResultContainer.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        double lat = location.get(position).getLatitude();
                        double lon = location.get(position).getLongitude();
                        Map_Expansion.getMap().findArea(cordovaWebView, lat, lon);
                        dialog.dismiss();
                    }
                });
            }
        }
        return view;
    }

    public void setItemLayout(int itemLayout) {
        this.itemLayout = itemLayout;
    }

}
