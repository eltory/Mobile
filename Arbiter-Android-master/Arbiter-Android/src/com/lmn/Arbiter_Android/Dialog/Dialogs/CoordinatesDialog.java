package com.lmn.Arbiter_Android.Dialog.Dialogs;

import android.app.AlertDialog;
import android.text.InputType;
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
        lat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        lon = (EditText) view.findViewById(R.id.Editlon);
        lon.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


    }

    @Override
    public void onPositiveClick() {

        if(lat.getText().toString().length() != 0 && lon.getText().toString().length() != 0)
        {
            if(lat.getText().toString().charAt(0) != '.' && lon.getText().toString().charAt(0) != '.')
            {
                Double latitude = Double.parseDouble(lat.getText().toString());
                Double longitude = Double.parseDouble(lon.getText().toString());

                Map.getMap().findArea(cordova,latitude,longitude);
            }

            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(R.string.coordinate_input_error_title);
                builder.setMessage(R.string.coordinate_input_error_message);
                builder.setIcon(R.drawable.icon);
                builder.setPositiveButton(android.R.string.ok, null);

                builder.create().show();
            }
        }

        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.coordinate_input_error_title);
            builder.setMessage(R.string.coordinate_input_error_message);
            builder.setIcon(R.drawable.icon);
            builder.setPositiveButton(android.R.string.ok, null);

            builder.create().show();
        }
    }

    @Override
    public void onNegativeClick() {

    }
}
