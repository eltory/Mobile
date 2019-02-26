package com.lmn.OpenGDS_Android.Information.License;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.lmn.Arbiter_Android.R;

public class LicenseAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<LicenseItem> data;
    private int layout;

    public LicenseAdapter(Context context,int layout,ArrayList<LicenseItem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }

        LicenseItem licenseItem=data.get(position);

        TextView textView=(TextView)convertView.findViewById(R.id.title);
        textView.setText(licenseItem.getTitle());

        return convertView;
    }
}
