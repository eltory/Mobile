package com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoading;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoadingJob;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation;
import com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter.ValidationOptionListAdapter;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 검수 옵션 다이얼로그
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class ValidationOptionDialog extends ArbiterDialogFragment {

    public static final String TAG = ValidationOptionDialog.class.getName();

    private ListView listView;
    private ValidationOptionListAdapter validationOptionAdapter;
    private HasThreadPool hasThreadPool;
    private static ArrayList<Validation> checkedLayers = new ArrayList<Validation>();
    private static CordovaWebView cordovaWebView;

    // SEND TO JS FOR STARTING VALIDATION
    private JSONArray attributeArr;
    private JSONArray qaOptionArr;
    private JSONObject attribute;
    private JSONObject qaOption;
    private JSONArray layerName;
    private JSONArray firstFID;

    /**
     * @param title,ok,cancel,layout,hasThreadPool,checkedLayers,cordova String,String,String,int,HasThreadPool,ArrayList<Validation>,CordovaWebView
     * @return ValidationOptionDialog
     * @author JiJungKeun
     */
    public static ValidationOptionDialog newInstance(String title, String ok,
                                                     String cancel, int layout, HasThreadPool hasThreadPool, ArrayList<Validation> checkedLayers, CordovaWebView cordova) {

        ValidationOptionDialog frag = new ValidationOptionDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        frag.hasThreadPool = hasThreadPool;
        frag.checkedLayers = checkedLayers;

        cordovaWebView = cordova;
        return frag;
    }

    public ValidationOptionDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        this.setValidatingClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onPositiveClick();
                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onNegativeClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPositiveClick() {
        //Connection to validator.js for validation
        startValidation();
    }

    @Override
    public void onNegativeClick() {
    }

    @Override
    public void beforeCreateDialog(View view) {
        if (view != null) {
            registerListeners(view);
            populateLayersList(view);
        }
    }

    private void registerListeners(View view) {
    }


    private void populateLayersList(View view) {
        this.listView = (ListView) view.findViewById(R.id.validationOptionListView);
        this.validationOptionAdapter = new ValidationOptionListAdapter
                (this.getActivity().getApplicationContext(), this, hasThreadPool, R.layout.validation_option_layers_item);

        validationOptionAdapter.setData(checkedLayers);

        //this.addValidateLayersAdapter.setData
        this.listView.setAdapter(this.validationOptionAdapter);
    }

    public static void updateValidationLayerInfo(Validation updateLayerInfo) {
        for (int i = 0; i < checkedLayers.size(); i++) {
            if (checkedLayers.get(i).getLayerId().equals(updateLayerInfo.getLayerId()))
                checkedLayers.set(i, updateLayerInfo);
        }
    }

    /**
     * 검수 레이어 데이터 및 Q/A옵션 데이터를 JSONObject로 만들어 Javascript url 파라미터 전송
     *
     * @author JiJungKeun
     */
    public void startValidation() {
        try {
            attributeArr = new JSONArray();
            qaOptionArr = new JSONArray();
            layerName = new JSONArray();
            firstFID = new JSONArray();

            //Create Json Object for Validator server
            for (int i = 0; i < checkedLayers.size(); i++) {
                attribute = new JSONObject();
                qaOption = new JSONObject();
                layerName.put(checkedLayers.get(i).getLayerTitle());
                firstFID.put(checkedLayers.get(i).getFeatureFid());

                for (int j = 0; j < checkedLayers.get(i).getAttributes().size(); j++) {
                    attribute.put(checkedLayers.get(i).getAttributes().get(j), checkedLayers.get(i).getAttributeTypes().get(j));
                }
                attributeArr.put(attribute);

                if (checkedLayers.get(i).getFeatureGeometryType().equalsIgnoreCase("MULTIPOLYGON")
                        || checkedLayers.get(i).getFeatureGeometryType().equalsIgnoreCase("POLYGON")) {
                    for (int k = 0; k < checkedLayers.get(i).getPolygonOptionNames().length; k++) {
                        if (checkedLayers.get(i).callValidationOptionData(checkedLayers.get(i).getPolygonOptionNames()[k]) != null) {

                            if (checkedLayers.get(i).getPolygonOptionNames()[k].equalsIgnoreCase("SELFENTITY")
                                    || checkedLayers.get(i).getPolygonOptionNames()[k].equalsIgnoreCase("ENTITYDUPLICATED")
                                    || checkedLayers.get(i).getPolygonOptionNames()[k].equalsIgnoreCase("ATTRIBUTEFIX"))
                                qaOption.put(checkedLayers.get(i).getPolygonOptionNames()[k], checkedLayers.get(i).callValidationOptionDataArr(checkedLayers.get(i).getPolygonOptionNames()[k]));

                            else
                                qaOption.put(checkedLayers.get(i).getPolygonOptionNames()[k], checkedLayers.get(i).callValidationOptionData(checkedLayers.get(i).getPolygonOptionNames()[k]));
                        }
                    }
                } else if (checkedLayers.get(i).getFeatureGeometryType().equalsIgnoreCase("LINESTRING")
                        || checkedLayers.get(i).getFeatureGeometryType().equalsIgnoreCase("MULTILINESTRING")) {
                    for (int k = 0; k < checkedLayers.get(i).getLineOptionNames().length; k++) {
                        if (checkedLayers.get(i).callValidationOptionData(checkedLayers.get(i).getLineOptionNames()[k]) != null) {

                            if (checkedLayers.get(i).getLineOptionNames()[k].equalsIgnoreCase("SELFENTITY")
                                    || checkedLayers.get(i).getLineOptionNames()[k].equalsIgnoreCase("ENTITYDUPLICATED")
                                    || checkedLayers.get(i).getLineOptionNames()[k].equalsIgnoreCase("ATTRIBUTEFIX")
                                    || checkedLayers.get(i).getLineOptionNames()[k].equalsIgnoreCase("ZVALUEAMBIGUOUS"))
                                qaOption.put(checkedLayers.get(i).getLineOptionNames()[k], checkedLayers.get(i).callValidationOptionDataArr(checkedLayers.get(i).getLineOptionNames()[k]));

                            else
                                qaOption.put(checkedLayers.get(i).getLineOptionNames()[k], checkedLayers.get(i).callValidationOptionData(checkedLayers.get(i).getLineOptionNames()[k]));
                        }
                    }
                }
                qaOptionArr.put(qaOption);
            }

            //send data to validator.js
            AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
                @Override
                public void run() {
                    System.out.println("보내진 검수 옵션");
                    System.out.println(qaOptionArr);
                    String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.Validator.startValidation(" + attributeArr + "," + qaOptionArr + "," + layerName + "," + firstFID + ");'))";
                    Log.w(TAG, "attributeArr" + attributeArr);
                    Log.w(TAG, "qaOptionArr" + qaOptionArr);
                    Log.w(TAG, "layerName" + layerName);
                    Log.w(TAG, "firstFID" + firstFID);
                    cordovaWebView.loadUrl(url);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
