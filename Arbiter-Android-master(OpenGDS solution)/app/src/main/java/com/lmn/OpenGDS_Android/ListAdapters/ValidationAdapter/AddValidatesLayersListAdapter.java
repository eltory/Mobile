package com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lmn.Arbiter_Android.ListAdapters.ArbiterAdapter;
import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 검수 대상 레이어 추가 리스트 어댑터
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class AddValidatesLayersListAdapter extends BaseAdapter implements ArbiterAdapter<ArrayList<Validation>> {
    private ArrayList<Validation> items;
    private ArrayList<Validation> checkedLayers;

    private LayoutInflater inflater;
    private int itemLayout;
    private Context context;

    /**
     * @param context,itemLayout Context,int
     * @author JiJungKeun
     */
    public AddValidatesLayersListAdapter(Context context, int itemLayout) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.items = new ArrayList<Validation>();
        this.checkedLayers = new ArrayList<Validation>();
        this.itemLayout = itemLayout;
    }

    public void setData(ArrayList<Validation> items) {
        this.items = items;

        setCheckedLayers();

        notifyDataSetChanged();
    }

    /**
     * 선택할 레이어 체크 상태
     *
     * @author JiJungKeun
     */
    private void setCheckedLayers() {
        if (items != null && !items.isEmpty()
                && !checkedLayers.isEmpty()) {

            HashMap<String, Integer> layersAlreadyChecked = new HashMap<String, Integer>();

            String key = null;
            Validation currentLayer = null;
            int i;

            // Add all of the layers that are checked
            for (i = 0; i < checkedLayers.size(); i++) {
                currentLayer = checkedLayers.get(i);

                key = Validation.buildLayerKey(currentLayer);

                if (!layersAlreadyChecked.containsKey(key)) {
                    layersAlreadyChecked.put(key, i);
                }
            }

            // If the layer is supposed to be checked, check it
            for (i = 0; i < items.size(); i++) {
                currentLayer = items.get(i);

                key = Validation.buildLayerKey(currentLayer);

                if (layersAlreadyChecked.containsKey(key)) {
                    currentLayer.setChecked(true);

                    // Replace the Layer in the checkedLayers list
                    // with the Layer from the new list, for
                    // unchecking to work properly
                    replaceCheckedLayer(layersAlreadyChecked, key, currentLayer);
                }
            }
        }
    }

    private void replaceCheckedLayer(HashMap<String, Integer> layersAlreadyChecked,
                                     String key, Validation layer) {

        int replaceAt = layersAlreadyChecked.get(key);

        checkedLayers.set(replaceAt, layer);
    }


    /**
     * 검수 대상 후보 레이어 리스트(레이어 이름, 좌표계)
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

        Validation listItem = items.get(position);

        if (listItem != null) {
            TextView layerName = (TextView) view.findViewById(R.id.ValidateLayerName);
            TextView SRIDName = (TextView) view.findViewById(R.id.ValidateSRIDName);
            CheckBox checkbox = (CheckBox) view.findViewById(R.id.addValidateLayerCheckbox);

            if (layerName != null) {
                layerName.setText(listItem.getLayerTitle());
            }

            if (SRIDName != null) {
                SRIDName.setText(listItem.getFeatureGeometrySrid());
            }

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    CheckBox checkbox = (CheckBox) v.findViewById(R.id.addValidateLayerCheckbox);
                    checkbox.performClick();
                }

            });

            if (checkbox != null) {
                checkbox.setChecked(listItem.isChecked());

                checkbox.setTag(Integer.valueOf(position));

                checkbox.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Integer position = (Integer) ((CheckBox) v).getTag();
                        Validation listItem = items.get(position);
                        boolean checked = !listItem.isChecked();

                        listItem.setChecked(checked);

                        if (checked) {
                            checkedLayers.add(listItem);
                        } else {
                            checkedLayers.remove(listItem);
                        }
                    }
                });
            }
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

    public ArrayList<Validation> getCheckedLayers() {
        return this.checkedLayers;
    }
}