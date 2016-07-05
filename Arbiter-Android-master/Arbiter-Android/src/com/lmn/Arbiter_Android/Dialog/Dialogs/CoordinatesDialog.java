package com.lmn.Arbiter_Android.Dialog.Dialogs;

import android.view.View;
import android.widget.EditText;

import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.Map.Map;
import com.lmn.Arbiter_Android.R;

import org.apache.cordova.CordovaWebView;

/**
 * Created by pc on 2016-05-21.
 */
public class CoordinatesDialog extends ArbiterDialogFragment {
    private EditText lat;
    private EditText lon;
    private static CordovaWebView cordova;
    public static CoordinatesDialog newInstance(String title, String ok,
                                            String cancel, int layout, CordovaWebView cordovaWebView){
        CoordinatesDialog frag = new CoordinatesDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        cordova = cordovaWebView;
        return frag;
    }
    @Override
    public void beforeCreateDialog(View view) {

        lat = (EditText) view.findViewById(R.id.Editlat);
        lon = (EditText) view.findViewById(R.id.Editlon);

    }

    @Override
    public void onPositiveClick() {

        Double latitude = Double.parseDouble(lat.getText().toString());
        Double longitude = Double.parseDouble(lon.getText().toString());

       Map.getMap().findArea(cordova,latitude,longitude);
    }

    @Override
    public void onNegativeClick() {

    }
}
