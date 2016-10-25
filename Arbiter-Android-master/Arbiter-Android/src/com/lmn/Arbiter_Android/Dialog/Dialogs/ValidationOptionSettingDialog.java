package com.lmn.Arbiter_Android.Dialog.Dialogs;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ListView;


import com.lmn.Arbiter_Android.BaseClasses.Validation;
import com.lmn.Arbiter_Android.ListAdapters.ValidationOptionSettingListAdapter;
import com.lmn.Arbiter_Android.R;
import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;


public class ValidationOptionSettingDialog extends ArbiterDialogFragment{

    private ListView listView;
    private ValidationOptionSettingListAdapter validationOptionSettingAdapter;
    private HasThreadPool hasThreadPool;
    private Validation selectedLayer;

    public static ValidationOptionSettingDialog newInstance(String title, String ok,
                                                     String cancel, int layout, HasThreadPool hasThreadPool, Validation selectedLayer){

        ValidationOptionSettingDialog frag = new ValidationOptionSettingDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        frag.hasThreadPool = hasThreadPool;
        frag.selectedLayer = selectedLayer;

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        this.setValidatingClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                // 검수 옵션 미완성시 예외처리 다이얼로그 형성
                onPositiveClick();
            }
        });
    }

    @Override
    public void onCancel(DialogInterface dialog){
        onNegativeClick();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onPositiveClick() {
        //형성된 데이터 리턴
        getDialog().dismiss();
    }

    @Override
    public void onNegativeClick() {

    }

    @Override
    public void beforeCreateDialog(View view) {
        if(view != null){
            registerListeners(view);
            populateLayersList(view);
        }
    }

    private void registerListeners(View view){
    }


    private void populateLayersList(View view){
        this.listView = (ListView) view.findViewById(R.id.optionSettingListView);
        this.validationOptionSettingAdapter = new ValidationOptionSettingListAdapter
                (this.getActivity().getApplicationContext(),this, hasThreadPool, R.layout.validation_option_setting_list_item, selectedLayer);

        if(selectedLayer.getFeatureGeometryType().equalsIgnoreCase("MULTIPOLYGON") || selectedLayer.getFeatureGeometryType().equalsIgnoreCase("POLYGON"))
        {
            String[] polygonArr = selectedLayer.getPolygonOptionNames();
            validationOptionSettingAdapter.setData(polygonArr);
        }

        else if(selectedLayer.getFeatureGeometryType().equalsIgnoreCase("MULTILINESTRING") || selectedLayer.getFeatureGeometryType().equalsIgnoreCase("LINESTRING"))
        {
            String[] lineArr = selectedLayer.getLineOptionNames();
            validationOptionSettingAdapter.setData(lineArr);
        }

        //Point
        else
        {

        }

        this.listView.setAdapter(this.validationOptionSettingAdapter);

    }

}
