package com.lmn.OpenGDS_Android.Dialog.Dialogs;

import android.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.Map.Map_Expansion;

import org.apache.cordova.CordovaWebView;

/**
 * Created by pc on 2016-05-21.
 */
public class BoundaryImageDialog extends ArbiterDialogFragment {
    private EditText left, right, bottom, top;
    private TextView TextName, TextPath;
    private String imgName, imgPath;
    private CordovaWebView cordova;

    public static BoundaryImageDialog newInstance(String title, String ok,
                                                  String cancel, int layout, CordovaWebView cordovaWebView, String name, String path){
        BoundaryImageDialog frag = new BoundaryImageDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        frag.cordova = cordovaWebView;
        frag.imgName = name;
        frag.imgPath = path;

        return frag;
    }

    @Override
    public void beforeCreateDialog(View view) {

        left = (EditText) view.findViewById(R.id.EditLeft);
        bottom = (EditText) view.findViewById(R.id.EditBottom);
        right = (EditText) view.findViewById(R.id.EditRight);
        top = (EditText) view.findViewById(R.id.EditTop);

        left.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        bottom.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        right.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        top.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        TextName = (TextView) view.findViewById(R.id.TextName);
        TextPath = (TextView) view.findViewById(R.id.TextPath);

        TextName.setText(imgName);
        TextPath.setText(imgPath);
        TextName.setSelected(true);
        TextPath.setSelected(true);
    }

    @Override
    public void onPositiveClick() {

            if (left.getText().toString().length() != 0 && right.getText().toString().length() != 0 && bottom.getText().toString().length() != 0
                    && top.getText().toString().length() != 0) {
                if (left.getText().toString().charAt(0) != '.' && right.getText().toString().charAt(0) != '.' && bottom.getText().toString().charAt(0) != '.'
                        && top.getText().toString().charAt(0) != '.') {

                    Double leftBound = Double.parseDouble(left.getText().toString());
                    Double rightBound = Double.parseDouble(right.getText().toString());
                    Double topBound = Double.parseDouble(top.getText().toString());
                    Double bottomBound = Double.parseDouble(bottom.getText().toString());

                    Map_Expansion.getMap().addBoundaryImage(cordova, leftBound, bottomBound, rightBound, topBound, imgName, imgPath);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.boundary_image_input_error_title);
                    builder.setMessage(R.string.boundary_image_input_error_message);
                    builder.setIcon(R.drawable.icon);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setCancelable(false);
                    builder.create().show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.boundary_image_input_error_title);
                builder.setMessage(R.string.boundary_image_input_error_message);
                builder.setIcon(R.drawable.icon);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setCancelable(false);
                builder.create().show();
            }
        }

    @Override
    public void onNegativeClick() {
    }
}
