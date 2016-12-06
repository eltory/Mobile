package com.lmn.OpenGDS_Android.JSInterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;

import com.lmn.Arbiter_Android.Activities.MapActivity;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;

/**
 * Created by JiJungKeun on 2016-07-28.
 */

/**
 *  Class for communicating Android with Javascript
 */
public class JSInterface {

    private Context mContext;
    private MapActivity mapActivity;
    private String sfName = "imgData";
    private String upToDateReport = "report";
    private ProgressDialog validateProgress;
    private ProgressDialog imageProgress;

    public JSInterface(Context c, MapActivity mapActivity)
    {
        mContext = c;
        this.mapActivity = mapActivity;
    }

    //* Connection with ImageLayer.js *//
    @JavascriptInterface
    public void StartAddAoiImageProgressDialog()
    {
        imageProgress = new ProgressDialog(mContext);
        imageProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        imageProgress.setIcon(mContext.getResources().getDrawable(R.drawable.icon));
        imageProgress.setTitle(mContext.getResources().getString(R.string.loading));
        imageProgress.setMessage(mContext.getResources().getString(R.string.action_AOI_image_progress));
        imageProgress.setCanceledOnTouchOutside(false);
        imageProgress.show();
    }

    //* Connection with ImageLayer.js *//
    @JavascriptInterface
    public void DismissAddAoiImageProgressDialog()
    {
        imageProgress.dismiss();
    }

    //* Connection with ImageLayer.js *//
    @JavascriptInterface
    public void SaveImageData(String key, float value){
        SharedPreferences addData = mContext.getSharedPreferences(sfName, 0);
        SharedPreferences.Editor editor = addData.edit();

        editor.putFloat(key, value);
        editor.commit();
    }

    //* Connection with ImageLayer.js *//
    @JavascriptInterface
    public String LoadImageData(String key){
        SharedPreferences loadData = mContext.getSharedPreferences(sfName, 0);
        return loadData.getString(key, "");
    }

    //* Connection with ImageLayer.js *//
    @JavascriptInterface
    public int LoadImageDataSize(String key){
        SharedPreferences loadData = mContext.getSharedPreferences(sfName, 0);
        return loadData.getInt(key, 0);
    }

    //* Connection with ImageLayer.js *//
    @JavascriptInterface
    public float LoadImageBoundary(String key){
        SharedPreferences loadData = mContext.getSharedPreferences(sfName, 0);
        return loadData.getFloat(key, 0);
    }

    //* Connection with Validator.js *//
    @JavascriptInterface
    public void StartValidationProgressDialog()
    {
        validateProgress = new ProgressDialog(mContext);
        validateProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        validateProgress.setIcon(mContext.getResources().getDrawable(R.drawable.icon));
        validateProgress.setTitle(mContext.getResources().getString(R.string.loading));
        validateProgress.setMessage(mContext.getResources().getString(R.string.action_validation_progress));
        validateProgress.setCanceledOnTouchOutside(false);
        validateProgress.show();
    }

    //* Connection with Validator.js *//
    @JavascriptInterface
    public void CreatingErrorMarkingProgressDialog()
    {
        validateProgress = new ProgressDialog(mContext);
        validateProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        validateProgress.setIcon(mContext.getResources().getDrawable(R.drawable.icon));
        validateProgress.setTitle(mContext.getResources().getString(R.string.loading));
        validateProgress.setMessage(mContext.getResources().getString(R.string.action_errorMarking_progress));
        validateProgress.setCanceledOnTouchOutside(false);
        validateProgress.show();
    }

    //* Connection with Validator.js *//
    @JavascriptInterface
    public void DismissValidationProgressDialog()
    {
        validateProgress.dismiss();
    }

    //* Connection with Validator.js *//
    @JavascriptInterface
    public void SaveValidationResult(String validationResult, boolean check)
    {
        SharedPreferences saveErrorReport = mContext.getSharedPreferences(upToDateReport, 0);
        SharedPreferences.Editor editor = saveErrorReport.edit();
        editor.putString("report", validationResult);
        editor.putBoolean("check", check);
        editor.commit();

        ArbiterDialogs_Expansion dialogs_expansion = new ArbiterDialogs_Expansion(mContext.getApplicationContext(), mContext.getResources(), mapActivity.getSupportFragmentManager());;
        dialogs_expansion.showValidationErrorReportDialog(mapActivity);
    }

    //* Connection with LayersLoader.js *//
    @JavascriptInterface
    public String ReloadValidationResult()
    {
        SharedPreferences reloadErrorMarking = mContext.getSharedPreferences(upToDateReport, 0);
        String validationResult = "";
        validationResult = reloadErrorMarking.getString("report","");

        return validationResult;
    }
}
