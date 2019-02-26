package com.lmn.OpenGDS_Android.ListAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lmn.Arbiter_Android.R;

/**
 * 고정속성 타입 리스트 어댑터
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.10.
 */
public class TypesAdapter extends BaseAdapter {

    private final String[] types;
    private LayoutInflater inflater;

    public static class Types {
        public static final String _NULL = "null";
        public static final String DATE = "date";
        public static final String DATETIME = "datetime";
        public static final String _INTEGER = "integer";
        public static final String NUMBER = "number";
        public static final String VARCHAR2 = "varchar2";
        public static final String VARCHAR3 = "varchar3";
        public static final String VARCHAR4 = "varchar4";
    }

    public TypesAdapter(Activity activity) {

        this.inflater = activity.getLayoutInflater();

        types = new String[8];

        types[0] = Types._NULL;
        types[1] = Types.DATE;
        types[2] = Types.DATETIME;
        types[3] = Types._INTEGER;
        types[4] = Types.NUMBER;
        types[5] = Types.VARCHAR2;
        types[6] = Types.VARCHAR3;
        types[7] = Types.VARCHAR4;
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
