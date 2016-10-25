package com.lmn.Arbiter_Android.ListAdapters;

import java.util.ArrayList;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.BaseClasses.Validation;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogs;
import com.lmn.Arbiter_Android.R;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ValidationOptionListAdapter extends BaseAdapter implements ArbiterAdapter<ArrayList<Validation>>{
    private ArrayList<Validation> items;

    private LayoutInflater inflater;
    private int itemLayout;
    private Context context;
    private DialogFragment dialog;
    private HasThreadPool hasThreadPool;

    public ValidationOptionListAdapter(Context context, DialogFragment dialog, HasThreadPool hasThreadPool, int itemLayout) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.items = new ArrayList<Validation>();
        this.itemLayout = itemLayout;
        this.dialog = dialog;
        this.hasThreadPool = hasThreadPool;
    }

    public void setData(ArrayList<Validation> items){
        this.items = items;

        notifyDataSetChanged();
    }


    /**
     * @param position The index of the list item
     * @param convertView A view that can be reused (For saving memory)
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;

        if(view == null){
            view = inflater.inflate(itemLayout, null);
        }

        Validation listItem = items.get(position);

        if(listItem != null){
            TextView layerName = (TextView) view.findViewById(R.id.ValidationOptionLayerName);
            TextView featureType = (TextView) view.findViewById(R.id.ValidationOptionFeatureType);

            if(layerName != null){
                layerName.setText(listItem.getLayerTitle());
            }

            if(featureType != null){
                featureType.setText(listItem.getFeatureGeometryType());
            }

            view.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    // 검수옵션 다이얼로그 생성

                    (new ArbiterDialogs(context.getApplicationContext(), context.getResources(),
                            dialog.getActivity().getSupportFragmentManager())).showValidationOptionSettingDialog(hasThreadPool, listItem);
                }

            });
        }

        return view;
    }

    @Override
    public int getCount() {
        if(this.items != null){
            return this.items.size();
        }

        return 0;
    }

    @Override
    public Validation getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}