package com.lmn.OpenGDS_Android.Validator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.Loaders.ValidationLayersListLoader;
import com.lmn.OpenGDS_Android.Map.Map_Expansion;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 검수 관리(에러 네비게이터, 기록삭제)
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class ValidationManagement {

    private String upToDateReport = "report"; // SharedPreferences for validation report
    private TableLayout navigatorTableLayout;
    private JSONObject reportObject;
    private JSONArray detailedReports;
    private int errorSize = 0;
    private int errorPos = 0;
    private SharedPreferences getValidationData;
    private Activity activity;
    private CordovaWebView cordovaWebView;

    public ValidationManagement(Activity activity, CordovaWebView cordovaWebView) {
        this.activity = activity;
        this.cordovaWebView = cordovaWebView;
    }

    /**
     * 검수 결과 데이터 존재 유무 확인
     *
     * @return boolean
     * @author JiJungKeun
     */
    public boolean isExistValidationData() {
        getValidationData = activity.getSharedPreferences(upToDateReport, 0);

        return !getValidationData.getString("report", "").equals("");
    }

    /**
     * 에러 네비게이터 환경 세팅
     *
     * @author JiJungKeun
     */
    public void buildNavigator() {
        //NAVIGATOR REQUEST LANDSCAPE ORIENTATION
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        LinearLayout errNavigatorLayout = (LinearLayout) activity.findViewById(R.id.errorNavigator);

        if (errNavigatorLayout.getVisibility() == View.GONE) {

            //proceed existing navigator
            if (errorPos >= 0 && reportObject != null) {
                errNavigatorLayout.setVisibility(View.VISIBLE);
            }
            //start new navigator
            else {
                errNavigatorLayout.setVisibility(View.VISIBLE);
                try {
                    reportObject = new JSONObject(getValidationData.getString("report", ""));
                    detailedReports = reportObject.getJSONArray("features");
                    errorSize = detailedReports.length();
                    errorPos = 0;
                    startErrorNavigator();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //Navigator will be GONE if current state is visible
        else {

            errNavigatorLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 에러 네비게이터 실행
     *
     * @author JiJungKeun
     */
    private void startErrorNavigator() {
        navigatorTableLayout = (TableLayout) activity.findViewById(R.id.navigatorContent);
        navigatorTableLayout.removeViews(1, navigatorTableLayout.getChildCount() - 1);

        makeNavigatorTable();
        zoomToErrorFeature();

        if (errorPos >= 0)
            activity.findViewById(R.id.leftButton).setOnClickListener(leftClickListener);

        if (errorPos < errorSize)
            activity.findViewById(R.id.rightButton).setOnClickListener(rightClickListener);
    }

    ImageButton.OnClickListener leftClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            activity.findViewById(R.id.leftButton).setEnabled(false);
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {
                    activity.findViewById(R.id.leftButton).setEnabled(true);
                }
            }, 1000); // Should to wait click minimum 1 second for Vector Rendering

            navigatorTableLayout.removeViews(1, navigatorTableLayout.getChildCount() - 1);
            errorPos = errorPos - 1;
            if (errorPos < 0)
                errorPos = 0;

            makeNavigatorTable();
            zoomToErrorFeature();
        }
    };

    ImageButton.OnClickListener rightClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            activity.findViewById(R.id.rightButton).setEnabled(false);
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {
                    activity.findViewById(R.id.rightButton).setEnabled(true);
                }
            }, 1000); // Should to wait click minimum 1 second for Vector Rendering

            navigatorTableLayout.removeViews(1, navigatorTableLayout.getChildCount() - 1);
            errorPos = errorPos + 1;
            if (errorPos > errorSize - 1)
                errorPos = errorSize - 1;

            makeNavigatorTable();
            zoomToErrorFeature();
        }
    };

    /**
     * 에러 네비게이터 동적 테이블 생성
     *
     * @author JiJungKeun
     */
    private void makeNavigatorTable() {
        try {
            TableRow tr = new TableRow(activity);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackgroundColor(Color.parseColor("#BDC3C7"));
            tr.setPadding(1, 1, 1, 1);

            TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tableParams.rightMargin = 1;

            TextView errorNumber = new TextView(activity);
            errorNumber.setBackgroundColor(Color.parseColor("#34495E"));
            errorNumber.setTextSize(13);
            errorNumber.setPadding(5, 5, 5, 5);
            errorNumber.setTextColor(Color.parseColor("#ECF0F1"));
            errorNumber.setGravity(Gravity.CENTER_HORIZONTAL);
            errorNumber.setText("Error-" + (errorPos + 1));
            errorNumber.setLayoutParams(tableParams);
            tr.addView(errorNumber);

            TextView errorNameContent = new TextView(activity);
            errorNameContent.setBackgroundColor(Color.parseColor("#34495E"));
            errorNameContent.setTextSize(13);
            errorNameContent.setPadding(5, 5, 5, 5);
            errorNameContent.setTextColor(Color.parseColor("#ECF0F1"));
            errorNameContent.setGravity(Gravity.CENTER_HORIZONTAL);
            errorNameContent.setText(detailedReports.getJSONObject(errorPos).getJSONObject("properties").getString("errName"));
            errorNameContent.setLayoutParams(tableParams);
            tr.addView(errorNameContent);

            TextView errorFeatureID = new TextView(activity);
            errorFeatureID.setBackgroundColor(Color.parseColor("#34495E"));
            errorFeatureID.setTextSize(13);
            errorFeatureID.setPadding(5, 5, 5, 5);
            errorFeatureID.setTextColor(Color.parseColor("#ECF0F1"));
            errorFeatureID.setGravity(Gravity.CENTER_HORIZONTAL);
            errorFeatureID.setText(detailedReports.getJSONObject(errorPos).getJSONObject("properties").getString("featureID"));

            errorFeatureID.setLayoutParams(tableParams);
            tr.addView(errorFeatureID);

            navigatorTableLayout.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 에러 네비게이터(에러 추적)
     *
     * @author JiJungKeun
     */
    private void zoomToErrorFeature() {
        try {
            ValidationLayersListLoader validationLayer = new ValidationLayersListLoader(activity);
            String layerID = validationLayer.getLayerID(detailedReports.getJSONObject(errorPos).getJSONObject("properties").getString("featureID"));
            Map_Expansion.getMap().navigateFeature(cordovaWebView, layerID, detailedReports.getJSONObject(errorPos).getJSONObject("properties").getString("featureID"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 검수 데이터 삭제
     *
     * @author JiJungKeun
     */
    public void clearValidationData() {
        //remove shared preference data
        SharedPreferences destroyValidation = activity.getSharedPreferences(upToDateReport, 0);
        SharedPreferences.Editor validationEditor = destroyValidation.edit();
        validationEditor.clear();
        validationEditor.commit();

        //remove navigator UI and init variables
        LinearLayout errNavigatorLayout = (LinearLayout) activity.findViewById(R.id.errorNavigator);
        errNavigatorLayout.setVisibility(View.GONE);
        errorPos = 0;
        errorSize = 0;
        reportObject = null;
        detailedReports = null;

        //remove error marking
        Map_Expansion.getMap().removeErrorMarking(cordovaWebView);

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); // GO BACK TO THE SYSTEM DEFAULTS
    }
}
