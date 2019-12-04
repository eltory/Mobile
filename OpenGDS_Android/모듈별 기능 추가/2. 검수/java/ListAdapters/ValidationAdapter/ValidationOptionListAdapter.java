package com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.ListAdapters.ArbiterAdapter;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Classification;
import com.lmn.OpenGDS_Android.BaseClasses.Validation;
import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;

import java.util.ArrayList;

/**
 * 검수 옵션 리스트 어댑터
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class ValidationOptionListAdapter extends BaseAdapter implements ArbiterAdapter<ArrayList<Validation>> {
    private ArrayList<Validation> items;

    private LayoutInflater inflater;
    private int itemLayout;
    private Context context;
    private DialogFragment dialog;
    private HasThreadPool hasThreadPool;

    /**
     * @param context,dialog,hasThreadPool,itemLayout Context,DialogFragment,HasThreadPool,int
     * @author JiJungKeun
     */
    public ValidationOptionListAdapter(Context context, DialogFragment dialog, HasThreadPool hasThreadPool, int itemLayout) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.items = new ArrayList<Validation>();
        this.itemLayout = itemLayout;
        this.dialog = dialog;
        this.hasThreadPool = hasThreadPool;
    }

    public void setData(ArrayList<Validation> items) {
        this.items = items;

        notifyDataSetChanged();
    }

    /**
     * 검수 대상 레이어 리스트(레이어 이름, 지오메트리 타입)
     *
     * @param position    The index of the list item
     * @param convertView A view that can be reused (For saving memory)
     * @param parent
     * @return View
     * @author JiJungKeun
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(itemLayout, null);
        }

        final Validation listItem = items.get(position);

        if (listItem != null) {
            TextView layerName = (TextView) view.findViewById(R.id.ValidationOptionLayerName);
            TextView featureType = (TextView) view.findViewById(R.id.ValidationOptionFeatureType);

            if (layerName != null) {
                layerName.setText(listItem.getLayerTitle());
            }

            if (featureType != null) {
                featureType.setText(listItem.getFeatureGeometryType());
            }

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    (new ArbiterDialogs_Expansion(context.getApplicationContext(), context.getResources(),
                            dialog.getActivity().getSupportFragmentManager())).showValidationOptionSettingDialog(hasThreadPool, listItem);
                }
            });
        }

        return view;
    }

    @Override
    public int getCount() {
        if (this.items != null) {
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