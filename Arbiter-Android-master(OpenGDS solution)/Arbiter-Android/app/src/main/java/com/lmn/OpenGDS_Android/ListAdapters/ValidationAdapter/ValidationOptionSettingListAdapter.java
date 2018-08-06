package com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.ListAdapters.ArbiterAdapter;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;
import com.lmn.OpenGDS_Android.BaseClasses.Validation;

/**
 * 검수 옵션 설정 리스트 어댑터
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class ValidationOptionSettingListAdapter extends BaseAdapter implements ArbiterAdapter<String[]> {
    private String[] items;
    private LayoutInflater inflater;
    private int itemLayout;
    private Context context;
    private DialogFragment dialog;
    private HasThreadPool hasThreadPool;
    private Validation selectedLayer;

    /**
     * @param context,dialog,hasThreadPool,itemLayout,selectedLayer Context,DialogFragment,HasThreadPool,int,Validation
     * @author JiJungKeun
     */
    public ValidationOptionSettingListAdapter(Context context, DialogFragment dialog, HasThreadPool hasThreadPool, int itemLayout, Validation selectedLayer) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.itemLayout = itemLayout;
        this.dialog = dialog;
        this.selectedLayer = selectedLayer;
        this.hasThreadPool = hasThreadPool;
    }

    public void setData(String[] items) {
        this.items = items;

        notifyDataSetChanged();
    }

    /**
     * 지오메트리 타입 별 Q/A 옵션 리스트, Select 이벤트
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

        final String listItem = items[position];

        if (listItem != null) {

            TextView optionNameView = (TextView) view.findViewById(R.id.optionItemName);
            final RelativeLayout optionNameLayout = (RelativeLayout) view.findViewById(R.id.OptionSettingContainer);

            if (optionNameView != null) {
                optionNameView.setText(listItem);
            }

            if (selectedLayer.callValidationOptionData(listItem) != null)
                optionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);

            else
                optionNameLayout.setBackgroundResource(R.color.white);

            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    ColorDrawable drawable = (ColorDrawable) optionNameLayout.getBackground();
                    int background_color = drawable.getColor();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (background_color == context.getResources().getColor(R.color.layer_list_item_pressed))
                                optionNameLayout.setBackgroundResource(R.color.white);

                            else
                                optionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);

                            break;

                        case MotionEvent.ACTION_UP:
                            if (background_color == context.getResources().getColor(R.color.layer_list_item_pressed))
                                optionNameLayout.setBackgroundResource(R.color.white);

                            else
                                optionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);

                            setDetailOption(listItem, optionNameLayout);
                            break;
                    }
                    return true;
                }
            });
        }

        return view;
    }

    /**
     * 세부 검수 옵션 설정이 없는 Q/A 옵션은 바로 데이터 저장
     *
     * @param listItem,optionNameLayout String,RelativeLayout
     * @author JiJungKeun
     */
    public void setDetailOption(String listItem, RelativeLayout optionNameLayout) {
        ColorDrawable drawable = (ColorDrawable) optionNameLayout.getBackground();
        int background_color = drawable.getColor();

        if (listItem.equalsIgnoreCase("SELFENTITY")) {

            if (background_color == context.getResources().getColor(R.color.layer_list_item_pressed)) {
                selectedLayer.setSelfEntity(null);
                selectedLayer.setSelfEntityArr(null);
                optionNameLayout.setBackgroundResource(R.color.white);
            } else {
                selectedLayer.setSelfEntity(selectedLayer.getLayerTitle());
                selectedLayer.setSelfEntityArr(selectedLayer.getLayerTitle());
                optionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);
            }
        } else if (listItem.equalsIgnoreCase("ENTITYDUPLICATED")) {
            if (background_color == context.getResources().getColor(R.color.layer_list_item_pressed)) {
                selectedLayer.setEntityDuplicated(null);
                selectedLayer.setEntityDuplicatedArr(null);
                optionNameLayout.setBackgroundResource(R.color.white);
            } else {
                selectedLayer.setEntityDuplicated(selectedLayer.getLayerTitle());
                selectedLayer.setEntityDuplicatedArr(selectedLayer.getLayerTitle());
                optionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);
            }
        } else if (listItem.equalsIgnoreCase("POINTDUPLICATED")) {
            if (background_color == context.getResources().getColor(R.color.layer_list_item_pressed)) {
                selectedLayer.setPointDuplicated(null);
                optionNameLayout.setBackgroundResource(R.color.white);
            } else {
                selectedLayer.setPointDuplicated("");
                optionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);
            }
        } else if (listItem.equalsIgnoreCase("CONINTERSECTED")) {
            if (background_color == context.getResources().getColor(R.color.layer_list_item_pressed)) {
                selectedLayer.setConIntersected(null);
                optionNameLayout.setBackgroundResource(R.color.white);
            } else {
                selectedLayer.setConIntersected("");
                optionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);
            }
        } else if (listItem.equalsIgnoreCase("CONBREAK")) {
            if (background_color == context.getResources().getColor(R.color.layer_list_item_pressed)) {
                selectedLayer.setConBreak(null);
                optionNameLayout.setBackgroundResource(R.color.white);
            } else {
                selectedLayer.setConBreak("");
                optionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);
            }
        } else if (listItem.equalsIgnoreCase("USELESSPOINT")) {

            if (background_color == context.getResources().getColor(R.color.layer_list_item_pressed)) {
                selectedLayer.setUseLessPoint(null);
                optionNameLayout.setBackgroundResource(R.color.white);
            } else {
                selectedLayer.setUseLessPoint("");
                optionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);
            }
        }

        //For some, detail options are needed to create dialog
        else {
            (new ArbiterDialogs_Expansion(context.getApplicationContext(), context.getResources(),
                    dialog.getActivity().getSupportFragmentManager())).showValidationDetailOptionSettingDialog(hasThreadPool, listItem, selectedLayer, optionNameLayout);
        }
    }

    @Override
    public int getCount() {
        if (this.items != null) {
            return this.items.length;
        }
        return 0;
    }

    @Override
    public String getItem(int position) {
        return this.items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}