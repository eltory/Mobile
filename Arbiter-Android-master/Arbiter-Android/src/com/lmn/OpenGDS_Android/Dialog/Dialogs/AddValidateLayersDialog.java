package com.lmn.OpenGDS_Android.Dialog.Dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.ConnectivityListeners.ConnectivityListener;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.R;
import com.lmn.Arbiter_Android.Util;

import com.lmn.OpenGDS_Android.BaseClasses.Validation;
import com.lmn.OpenGDS_Android.ListAdapters.AddValidatesLayersListAdapter;
import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;
import com.lmn.OpenGDS_Android.Loaders.ValidationLayersListLoader;

import org.apache.cordova.CordovaWebView;

import java.util.ArrayList;


public class AddValidateLayersDialog extends ArbiterDialogFragment{

    private ConnectivityListener connectivityListener;
    private ListView listView;
    private AddValidatesLayersListAdapter addValidateLayersAdapter;
    private HasThreadPool hasThreadPool;
    private ArrayList<Validation> validateLayers = new ArrayList<Validation>();
    private static CordovaWebView cordovaWebView;

    public static AddValidateLayersDialog newInstance(String title, String ok,
                                                      String cancel, int layout, ConnectivityListener connectivityListener, HasThreadPool hasThreadPool, CordovaWebView cordova){

        AddValidateLayersDialog frag = new AddValidateLayersDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);

        frag.connectivityListener = connectivityListener;
        frag.hasThreadPool = hasThreadPool;

        cordovaWebView = cordova;

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        this.setValidatingClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {

                if(connectivityListener != null && connectivityListener.isConnected()){
                    onPositiveClick();
                }else{
                    Util.showNoNetworkDialog(getActivity());
                }
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

        final ArrayList<Validation> checked  = this.addValidateLayersAdapter.getCheckedLayers();

        (new ArbiterDialogs_Expansion(getActivity().getApplicationContext(), getActivity().getResources(),
                getActivity().getSupportFragmentManager())).showValidationOptionDialog(hasThreadPool, checked, cordovaWebView);

        getDialog().dismiss();
    }

    @Override
    public void onNegativeClick() {

    }

    @Override
    public void beforeCreateDialog(View view) {
        if(view != null){
            registerListeners(view);
            populateAddLayersList(view);
        }
    }

    private void registerListeners(View view){
    }


    private void populateAddLayersList(View view){
        this.listView = (ListView) view.findViewById(R.id.addValidateLayersListView);
        this.addValidateLayersAdapter = new AddValidatesLayersListAdapter
                (this.getActivity().getApplicationContext(), R.layout.add_validate_layers_item);

        ValidationLayersListLoader loadValidationList = new ValidationLayersListLoader(getActivity());
        ArrayList<String> table_names = loadValidationList.getFeature_table_name();
        for(int i=0; i<table_names.size(); i++)
        {
            if(loadValidationList.existInAOI(table_names.get(i)) == true)
            {
                validateLayers.add(loadValidationList.setLayerInfo(table_names.get(i)));
            }
        }
        addValidateLayersAdapter.setData(validateLayers);
        this.listView.setAdapter(this.addValidateLayersAdapter);
    }
}
