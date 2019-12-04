package com.lmn.OpenGDS_Android.Dialog.Dialogs.Image;

import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;

import com.lmn.Arbiter_Android.ArbiterProject;
import com.lmn.Arbiter_Android.DatabaseHelpers.ProjectDatabaseHelper;
import com.lmn.Arbiter_Android.DatabaseHelpers.TableHelpers.PreferencesHelper;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.ProjectStructure.ProjectStructure;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.Map.Map_Expansion;

import org.apache.cordova.CordovaWebView;

/**
 * AOI 이미지 삽입
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class AOIBoundaryImageDialog extends ArbiterDialogFragment {
    private TextView left, right, bottom, top; //boundary value
    private TextView TextName, TextPath; // image name, path
    private String AOIFileName, AOIFilePath;
    private String[] boundArr;
    private CordovaWebView cordova;

    /**
     * @param title,ok,cancel,layout,cordovaWebView,name,path String,String,String,int,CordovaWebView,String,String
     * @return AOIBoundaryImageDialog
     * @author JiJungKeun
     */
    public static AOIBoundaryImageDialog newInstance(String title, String ok,
                                                     String cancel, int layout, CordovaWebView cordovaWebView, String name, String path) {
        AOIBoundaryImageDialog frag = new AOIBoundaryImageDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        frag.cordova = cordovaWebView;
        frag.AOIFileName = name;
        frag.AOIFilePath = path;

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

    @Override
    public void onPositiveClick() {
        Map_Expansion.getMap().addAOIImage(cordova, Double.valueOf(boundArr[0]).doubleValue(), Double.valueOf(boundArr[1]).doubleValue(), Double.valueOf(boundArr[2]).doubleValue(), Double.valueOf(boundArr[3]).doubleValue(),
                AOIFileName, AOIFilePath);
    }

    @Override
    public void onNegativeClick() {
    }

    /**
     * DB로부터 AOI 경계값 추출 반환
     *
     * @return result
     * @author JiJungKeun
     */
    public String getAOIBoundaryFromDB() {
        String projectName = ArbiterProject.getArbiterProject().getOpenProject(getActivity());

        String path = ProjectStructure.getProjectPath(projectName);

        SQLiteDatabase projectDb = ProjectDatabaseHelper.getHelper(getActivity().getApplicationContext(), path, false).getWritableDatabase();

        String result = PreferencesHelper.getHelper().get(projectDb, getActivity().getApplicationContext(), PreferencesHelper.AOI); //AOI boundary value

        return result;
    }
}
