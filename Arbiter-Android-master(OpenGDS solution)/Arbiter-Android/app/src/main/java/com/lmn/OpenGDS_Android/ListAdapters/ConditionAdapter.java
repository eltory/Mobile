package com.lmn.OpenGDS_Android.ListAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lmn.Arbiter_Android.R;

/**
 * 수치조건 리스트 어댑터
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.16.
 */
public class ConditionAdapter extends BaseAdapter {

    private final String[] types;
    private LayoutInflater inflater;

    public static class Types {
        public static final String EQUAL = "equal";
        public static final String OVER = "over";
        public static final String UNDER = "under";
    }

    public ConditionAdapter(Activity activity) {

        this.inflater = activity.getLayoutInflater();

        types = new String[3];

        types[0] = Types.EQUAL;
        types[1] = Types.OVER;
        types[2] = Types.UNDER;
    }

    public int getPositionFromType(String type) {

        for (int i = 0; i < types.length; ++i) {
            if (type.equalsIgnoreCase(types[i]))
                return i;
        }
        return 0;
    }

    @Override
    public int getCount() {
        return types.length;
    }

    @Override
    public String getItem(int position) {
        return types[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.spinner_item, null);
        }

        TextView textView = (TextView) view.findViewById(R.id.spinnerText);

        textView.setText(getItem(position).toUpperCase());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.drop_down_item, null);
        }

        TextView textView = (TextView) view.findViewById(R.id.spinnerText);

        textView.setText(getItem(position).toUpperCase());

        return view;
    }
}
