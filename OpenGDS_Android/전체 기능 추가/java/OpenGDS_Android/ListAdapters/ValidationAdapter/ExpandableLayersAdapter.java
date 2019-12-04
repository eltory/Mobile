package com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Classification;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Layer;
import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.ClassificationDialog;

import java.util.ArrayList;

/**
 * 분류-레이아웃을 표현하기 위한 ExpandableListViewAdapter
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.11.
 */
public class ExpandableLayersAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Classification> layerList;
    private ArrayList<String> geoTypes;
    private HasThreadPool hasThreadPool;
    private LayoutInflater inflater;

    public ExpandableLayersAdapter(Context context, ArrayList<Classification> layerList, ArrayList<String> geoTypes,
                                   HasThreadPool hasThreadPool) {
        this.context = context;
        this.layerList = layerList;
        this.geoTypes = geoTypes;
        this.hasThreadPool = hasThreadPool;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 부모 뷰홀더
     */
    public class ParentViewHolder {
        View back;
        TextView name;
        ImageButton addLayer, remove;
    }

    /**
     * 자식 뷰홀더
     */
    public class ChildViewHolder {
        View back;
        TextView name;
        ImageButton remove;
    }

    /**
     * ExpandableListView 의 부모객체인 Classification 리스트를 설정
     *
     * @param classifications {@link ClassificationDialog} 에서 생성된 분류리스트
     */
    public void setParent(ArrayList<Classification> classifications, ArrayList<String> geoTypes) {
        this.layerList = classifications;
        this.geoTypes = geoTypes;
        notifyDataSetChanged();
    }

    /**
     * 업데이트시 필요
     *
     * @return Classification 리스트
     */
    public ArrayList<Classification> getLayerList() {
        return layerList;
    }

    /**
     * Parent List 관련
     * <p>
     * {}
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return ParentView
     */
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, final View convertView, final ViewGroup parent) {
        View view = convertView;
        final ParentViewHolder holder;
        final Classification header = layerList.get(groupPosition);

        if (view == null) {
            view = inflater.inflate(R.layout.classification_list_item, null);

            holder = new ParentViewHolder();
            holder.back = view.findViewById(R.id.layers_load);
            holder.name = (TextView) view.findViewById(R.id.classificationName);
            holder.addLayer = (ImageButton) view.findViewById(R.id.add_layer_button);
            holder.remove = (ImageButton) view.findViewById(R.id.deleteClassification);
            view.setTag(holder);
        } else {
            holder = (ParentViewHolder) view.getTag();
        }

        if (header != null) {
            holder.name.setText(header.getName());
            holder.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExpandableListView listView = (ExpandableListView) parent;
                    if (listView.isGroupExpanded(groupPosition))
                        listView.collapseGroup(groupPosition);
                    else
                        listView.expandGroup(groupPosition);
                }
            });
            holder.addLayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (new ArbiterDialogs_Expansion(context.getApplicationContext(), context.getResources(),
                            ((FragmentActivity) context).getSupportFragmentManager())).showAddClassificationDialog(hasThreadPool, groupPosition, null, geoTypes.get(groupPosition));
                }
            });
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClassificationDialog.removeAtPosition(groupPosition);
                    notifyDataSetChanged();
                }
            });
        }
        return view;
    }


    @Override
    public Classification getGroup(int groupPosition) {
        return layerList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        if (layerList != null)
            return layerList.size();
        return 0;
    }

    /**
     * ChildListView 관련
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return childView
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildViewHolder holder;
        final Layer child = layerList.get(groupPosition).getLayers().get(childPosition);
        if (view == null) {
            view = inflater.inflate(R.layout.layer_list_item, null);

            holder = new ChildViewHolder();
            holder.back = view.findViewById(R.id.layer_load);
            holder.name = (TextView) view.findViewById(R.id.layer_name);
            holder.remove = (ImageButton) view.findViewById(R.id.delete_layer);
            view.setTag(holder);
        } else {
            holder = (ChildViewHolder) view.getTag();
        }

        if (child != null) {
            holder.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ArbiterDialogs_Expansion(context.getApplicationContext(), context.getResources(),
                            ((FragmentActivity) context).getSupportFragmentManager()).showAddClassificationDialog(hasThreadPool, groupPosition, child, geoTypes.get(groupPosition));
                }
            });
            holder.name.setText(child.getCode());
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeFix(groupPosition, childPosition);
                }
            });
        }
        return view;
    }

    public void removeFix(int parentPosition, int childPosition) {
        layerList.get(parentPosition).getLayers().remove(childPosition);
        if(getChildrenCount(parentPosition)==0){
             System.out.println("asdasd");
        }
        if (layerList.size() == 0)
            layerList = null;
        notifyDataSetChanged();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (layerList.get(groupPosition).getLayers() != null)
            return layerList.get(groupPosition).getLayers().size();
        return 0;
    }

    @Override
    public Layer getChild(int groupPosition, int childPosition) {
        return layerList.get(groupPosition).getLayers().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /* 기타 */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}


