package com.lmn.Arbiter_Android.Dialog.Dialogs;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.Map.Map;
import com.lmn.Arbiter_Android.R;

import org.apache.cordova.CordovaWebView;

/**
 * Created by pc on 2016-05-21.
 */
public class BoundaryDialog extends ArbiterDialogFragment {
    private EditText left,right,bottom,top;
    private TextView TextName, TextPath;


    private static String imgName, imgPath;

    private static CordovaWebView cordova;
    public static BoundaryDialog newInstance(String title, String ok,
                                                String cancel, int layout, CordovaWebView cordovaWebView, String name, String path){
        BoundaryDialog frag = new BoundaryDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        cordova = cordovaWebView;
        imgName = name;
        imgPath = path;

        return frag;
    }
    @Override
    public void beforeCreateDialog(View view) {

        left = (EditText) view.findViewById(R.id.EditLeft);
        bottom = (EditText) view.findViewById(R.id.EditBottom);
        right = (EditText) view.findViewById(R.id.EditRight);
        top = (EditText) view.findViewById(R.id.EditTop);
        TextName = (TextView) view.findViewById(R.id.TextName);
        TextPath = (TextView) view.findViewById(R.id.TextPath);

        TextName.setText(imgName);
        TextPath.setText(imgPath);
        TextName.setSelected(true);
        TextPath.setSelected(true);
    }

    @Override
    public void onPositiveClick() {

        Double leftBound = Double.parseDouble(left.getText().toString());
        Double rightBound = Double.parseDouble(right.getText().toString());
        Double topBound = Double.parseDouble(top.getText().toString());
        Double bottomBound = Double.parseDouble(bottom.getText().toString());

        Map.getMap().addBoundaryImage(cordova,leftBound,bottomBound,rightBound,topBound,imgName,imgPath);
    }

    @Override
    public void onNegativeClick() {

    }
}
