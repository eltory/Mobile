package com.lmn.OpenGDS_Android.ListAdapters;

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

public class ValidationDetailOptionSettingAdapter extends BaseAdapter implements ArbiterAdapter<ArrayList<String>>{
    private ArrayList<String> items;
    private ArrayList<String> checkedItems;
    private ArrayList<Boolean> isChekedItems;

    private LayoutInflater inflater;
    private int itemLayout;
    private Context context;
    private String optionName;
    private Validation selectedLayer;

    public ValidationDetailOptionSettingAdapter(Context context, int itemLayout, String optionName, Validation selectedLayer) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.items = new ArrayList<String>();
        this.checkedItems = new ArrayList<String>();
        this.isChekedItems = new ArrayList<Boolean>();
        this.itemLayout = itemLayout;
        this.optionName = optionName;
        this.selectedLayer = selectedLayer;
    }

    public void setData(ArrayList<String> items){
        this.items = items;
        String[] alreadyCheckedItems = null;
        boolean lock = false;
        int index=0;

        if(optionName.equalsIgnoreCase("ATTRIBUTEFIX"))
        {
            if(selectedLayer.getAttributeFix() != null)
            {
                alreadyCheckedItems = selectedLayer.getAttributeFix().split(",");
            }
        }

        else
        {
            if(selectedLayer.getZValueAmbiguous() != null)
            {
                alreadyCheckedItems = selectedLayer.getZValueAmbiguous().split(",");
            }
        }

        // set already checked items
        for(int i=0; i<items.size(); i++) {

            if(alreadyCheckedItems != null) {
                lock = false;
                for (int j = 0; j < alreadyCheckedItems.length; j++)
                {
                    if(items.get(i).equalsIgnoreCase(alreadyCheckedItems[j]))
                    {
                        lock = true;
                        index = j;
                        break;
                    }
                }

                if(lock==true) {
                    isChekedItems.add(true);
                    checkedItems.add(alreadyCheckedItems[index]);
                }
                else
                    isChekedItems.add(false);
            }

            else
                isChekedItems.add(false);
        }

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

        String listItem = items.get(position);

        if(listItem != null){
            TextView item = (TextView) view.findViewById(R.id.attributeName);
            CheckBox checkbox = (CheckBox) view.findViewById(R.id.attributeCheckBox);

            if(item != null){
                item.setText(listItem);
            }

            view.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    CheckBox checkbox = (CheckBox) v.findViewById(R.id.attributeCheckBox);
                    checkbox.performClick();
                }

            });

            if(checkbox != null){
                checkbox.setChecked(isChecked(position));

                checkbox.setTag(Integer.valueOf(position));

                checkbox.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Integer position = (Integer) ((CheckBox) v).getTag();
                        String listItem = items.get(position);
                        boolean checked = !isChecked(position);

                        setChecked(checked, position);

                        if(checked){
                            checkedItems.add(listItem);
                        }else{
                            checkedItems.remove(listItem);
                        }
                    }
                });
            }
        }

        return view;
    }

    public boolean isChecked(int position)
    {
        return isChekedItems.get(position);
    }

    public void setChecked(boolean checked, int position)
    {
        isChekedItems.set(position, checked);
    }

    @Override
    public int getCount() {
        if(this.items != null){
            return this.items.size();
        }

        return 0;
    }

    @Override
    public String getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<String> getCheckedItems(){
        return this.checkedItems;
    }
}