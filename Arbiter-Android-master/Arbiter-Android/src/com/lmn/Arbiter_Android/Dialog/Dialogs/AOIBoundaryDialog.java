package com.lmn.Arbiter_Android.Dialog.Dialogs;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lmn.Arbiter_Android.ArbiterProject;
import com.lmn.Arbiter_Android.DatabaseHelpers.ProjectDatabaseHelper;
import com.lmn.Arbiter_Android.DatabaseHelpers.TableHelpers.PreferencesHelper;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.Map.Map;
import com.lmn.Arbiter_Android.ProjectStructure.ProjectStructure;
import com.lmn.Arbiter_Android.R;

import org.apache.cordova.CordovaWebView;

/**
 * Created by pc on 2016-05-21.
 */
public class AOIBoundaryDialog extends ArbiterDialogFragment {
    private TextView left,right,bottom,top;
    private TextView TextName, TextPath;


    private static String AOIFileName, AOIFilePath;
    private String[] boundArr;

    private static CordovaWebView cordova;
    public static AOIBoundaryDialog newInstance(String title, String ok,
                                             String cancel, int layout, CordovaWebView cordovaWebView, String name, String path){
        AOIBoundaryDialog frag = new AOIBoundaryDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        cordova = cordovaWebView;
        AOIFileName = name;
        AOIFilePath = path;

        return frag;
    }

    @Override
    public void beforeCreateDialog(View view) {

        left = (TextView) view.findViewById(R.id.aoiLeft);
        bottom = (TextView) view.findViewById(R.id.aoiBottom);
        right = (TextView) view.findViewById(R.id.aoiRight);
        top = (TextView) view.findViewById(R.id.aoiTop);
        TextName = (TextView) view.findViewById(R.id.AOIFileName);
        TextPath = (TextView) view.findViewById(R.id.AOIFilePath);

        TextName.setText(AOIFileName);
        TextPath.setText(AOIFilePath);
        TextName.setSelected(true);
        TextPath.setSelected(true);

        if(!getAOIBoundaryFromDB().equals("FAIL"))
        {
            String bound = getAOIBoundaryFromDB();
            boundArr = bound.split(",");

            left.setText(boundArr[0]);
            bottom.setText(boundArr[1]);
            right.setText(boundArr[2]);
            top.setText(boundArr[3]);

            left.setSelected(true);
            bottom.setSelected(true);
            right.setSelected(true);
            top.setSelected(true);
        }

    }

    @Override
    public void onPositiveClick() {

        if(!getAOIBoundaryFromDB().equals("FAIL")) {
            Map.getMap().addAOIImage(cordova, Double.valueOf(boundArr[0]).doubleValue(), Double.valueOf(boundArr[1]).doubleValue(), Double.valueOf(boundArr[2]).doubleValue(), Double.valueOf(boundArr[3]).doubleValue(),
                    AOIFileName, AOIFilePath);
        }
    }

    @Override
    public void onNegativeClick() {

    }

    public String getAOIBoundaryFromDB()
    {
        String projectName = ArbiterProject.getArbiterProject().getOpenProject(getActivity());

        String path = ProjectStructure.getProjectPath(projectName);

        SQLiteDatabase projectDb = ProjectDatabaseHelper.getHelper(getActivity().getApplicationContext(), path, false).getWritableDatabase();

        String result = PreferencesHelper.getHelper().get(projectDb, getActivity().getApplicationContext(), PreferencesHelper.AOI);
        if (result != null) {
            return result;
        }

        else
            return "FAIL";
    }
}
