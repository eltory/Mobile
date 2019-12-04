package com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Classification;
import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;

import java.util.ArrayList;

public class NewValidationAdapter extends BaseAdapter {

    private ArrayList<Classification> classifications;
    private LayoutInflater inflater;
    private Context context;

    public NewValidationAdapter(Context context, ArrayList<Classification> classifications) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.classifications = classifications;
    }

    @Override
    public int getCount() {
        if(classifications == null)
            return 0;
        return classifications.size();
    }

    @Override
    public Classification getItem(int position) {
        return classifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        final Classification item = classifications.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.new_validation_list_item, null);

            holder = new ViewHolder();
            holder.back = view.findViewById(R.id.classification_container);
            holder.classificationName = (TextView) view.findViewById(R.id.classification_name_detail);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (item != null) {
            System.out.println("abcabc");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (new ArbiterDialogs_Expansion(context.getApplicationContext(), context.getResources(),
                            ((FragmentActivity)context).getSupportFragmentManager())).showNewValidationOptionDialog(null, item, null);
                }
            });
            holder.classificationName.setText(item.getName());
        }
        return view;
    }

    public class ViewHolder {
        TextView classificationName;
        View back;
    }
}
