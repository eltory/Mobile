package com.lmn.OpenGDS_Android.ListAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.ArbiterProject;
import com.lmn.Arbiter_Android.BaseClasses.ColorMap;
import com.lmn.Arbiter_Android.BaseClasses.Layer;
import com.lmn.Arbiter_Android.DatabaseHelpers.CommandExecutor.CommandExecutor;
import com.lmn.Arbiter_Android.DatabaseHelpers.ProjectDatabaseHelper;
import com.lmn.Arbiter_Android.DatabaseHelpers.TableHelpers.LayersHelper;
import com.lmn.Arbiter_Android.GeometryEditor.GeometryEditor;
import com.lmn.Arbiter_Android.ListAdapters.CustomList;
import com.lmn.Arbiter_Android.Map.Map;
import com.lmn.Arbiter_Android.OrderLayers.OrderLayersModel;
import com.lmn.Arbiter_Android.OrderLayers.OrderLayersModelException;
import com.lmn.Arbiter_Android.ProjectStructure.ProjectStructure;
import com.lmn.Arbiter_Android.R;


import java.util.ArrayList;

/**
 * 검수 마지막 단계 서버레이어 선택
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.18.
 */
public class NewOverlayList extends CustomList<ArrayList<Layer>, Layer> {

    private LayoutInflater inflater;
    private int itemLayout;
    private Activity activity;
    private Context context;
    private ArbiterProject arbiterProject;
    private OrderLayersModel orderLayersModel;
    private Map.MapChangeListener mapChangeListener;
    private HasThreadPool hasThreadPool;
    private ArrayList<Layer> layers;
    private boolean[] isChecked;
    private String currServer = null;
    private ViewGroup viewGroup;

    public NewOverlayList(ViewGroup viewGroup, Activity activity, int itemLayout, HasThreadPool hasThreadPool) {
        super(viewGroup);
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.inflater = LayoutInflater.from(this.context);
        this.itemLayout = itemLayout;
        this.arbiterProject = ArbiterProject.getArbiterProject();
        this.hasThreadPool = hasThreadPool;
        this.viewGroup = viewGroup;
        this.layers = new ArrayList<Layer>();

        try {
            this.orderLayersModel = new OrderLayersModel(this);
        } catch (OrderLayersModelException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            mapChangeListener = (Map.MapChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MapChangeListener");
        }
    }

    @Override
    public void setData(ArrayList<Layer> layers) {
        super.setData(layers);
        this.orderLayersModel.setLayers(layers);
    }

    @Override
    public int getCount() {
        return getData().size();
    }

    @Override
    public Layer getItem(int index) {
        return getData().get(index);
    }

    @Override
    public View getView(final int position) {
        if (this.isChecked == null || this.isChecked.length == 0)
            this.isChecked = new boolean[getData().size()];
        final View view = inflater.inflate(itemLayout, null);

        final Layer layer = getItem(position);

        if (layer != null) {
            if (layer.getColor() != null) {
                View layerColorView = view.findViewById(R.id.layerColor);

                if (layerColorView != null) {
                    layerColorView.setBackgroundColor(Color.parseColor(ColorMap.COLOR_MAP.get(layer.getColor())));
                }
            }
            TextView layerNameView = (TextView) view.findViewById(R.id.layerName);
            TextView serverNameView = (TextView) view.findViewById(R.id.serverName);
            final View back = view.findViewById(R.id.list_selector);
            final CheckBox isCheck = (CheckBox) view.findViewById(R.id.addLayerCheckbox);

            if (layerNameView != null) {
                layerNameView.setText(layer.getFeatureType());
            }

            if (serverNameView != null) {
                serverNameView.setText(layer.getServerName());
            }
            if (isChecked != null) {
                isCheck.setChecked(isChecked[position]);
                if (layer.getServerName().equalsIgnoreCase(currServer) || currServer == null) {

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i = -1;
                            isChecked[position] = !isChecked[position];
                            isCheck.setChecked(!isCheck.isChecked());
                            if (isChecked[position]) {
                                layers.add(layer);
                                currServer = layer.getServerName();
                                i = 1;
                            } else {
                                layers.remove(layer);
                                currServer = null;
                                i = 0;
                            }
                            if (unCheckedOk(i))
                                onDataUpdated();
                        }
                    });
                } else {
                    isChecked[position] = false;
                    isCheck.setChecked(false);
                    back.setClickable(false);
                    back.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.darker_gray));
                }
            }
        }

        return view;
    }

    /**
     * 검수할 서버 내의 레이어들만 선택하기 위하여
     * 검수할 서버 선택시 다른 서버 선택불가 블러처리
     *
     * @param i 현재 선택된 레이어 개수
     * @return 서버별 레이어 선택 블러처리
     */
    public boolean unCheckedOk(int i) {
        int n = 0;
        for (boolean b : isChecked) {
            if (b) n++;
        }
        return n == i;
    }

    public void setItemLayout(int itemLayout) {
        this.itemLayout = itemLayout;
    }

    public ArrayList<String> getLayers() {
        ArrayList<String> layer = new ArrayList<String>();
        for (Layer l : layers) {
            layer.add(l.getFeatureType());
        }
        return layer;
    }

    public String getLayerServerUrl() {
        if (layers.size() != 0)
            return layers.get(0).getServerUrl();
        return null;
    }

    public Layer getLayer() {
        if (layers.size() != 0)
            return layers.get(0);
        return null;
    }
}
