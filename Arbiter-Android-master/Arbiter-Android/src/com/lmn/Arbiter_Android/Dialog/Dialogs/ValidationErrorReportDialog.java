package com.lmn.Arbiter_Android.Dialog.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lmn.Arbiter_Android.R;

import org.apache.cordova.Config;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by JiJungKeun on 2016-10-27.
 */
public class ValidationErrorReportDialog extends Activity implements View.OnClickListener{

    private JSONObject validationResult = null;
    private JSONArray detailedErrors = new JSONArray();
    private TableLayout tl;
    private boolean check;
    private Button closeButton;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.validation_report_table);

        tl = (TableLayout) findViewById(R.id.ErrorReportTable);
        closeButton = (Button)findViewById(R.id.ErrorReportButton);
        closeButton.setOnClickListener(this);

        Intent intent = getIntent();

        try {
            validationResult = new JSONObject(intent.getStringExtra("report"));
            check = intent.getBooleanExtra("check",false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        showValidationErrorReportDialog();
        this.setFinishOnTouchOutside(false); //prevent to exit this dialog activity when user touches outside activity.
    }

    public void onClick(View v)
    {
        this.finish();
    }

    private void showValidationErrorReportDialog()
    {
        if(check == true)
        {
            try {
                detailedErrors = validationResult.getJSONArray("DetailsReport");
                for (int i = 0; i < detailedErrors.length(); i++) {
                    TableRow tr = new TableRow(this);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tr.setBackgroundColor(Color.BLACK);
                    tr.setPadding(1,1,1,1);

                    TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT);
                    tableParams.rightMargin=1;

                    TextView Number = new TextView(this);
                    Number.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    Number.setTextSize(12);
                    Number.setTextColor(Color.BLACK);
                    Number.setGravity(Gravity.CENTER_HORIZONTAL);
                    Number.setText((i+1)+"");
                    Number.setLayoutParams(tableParams);
                    tr.addView(Number);

                    TextView errorType = new TextView(this);
                    errorType.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    errorType.setTextSize(12);
                    errorType.setTextColor(Color.BLACK);
                    errorType.setGravity(Gravity.CENTER_HORIZONTAL);
                    errorType.setText(detailedErrors.getJSONObject(i).getString("errType"));
                    errorType.setLayoutParams(tableParams);
                    tr.addView(errorType);

                    TextView errorName = new TextView(this);
                    errorName.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    errorName.setTextSize(12);
                    errorName.setTextColor(Color.BLACK);
                    errorName.setGravity(Gravity.CENTER_HORIZONTAL);
                    errorName.setText(detailedErrors.getJSONObject(i).getString("errName"));
                    errorName.setLayoutParams(tableParams);
                    tr.addView(errorName);

                    TextView featureID = new TextView(this);
                    featureID.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    featureID.setTextSize(12);
                    featureID.setTextColor(Color.BLACK);
                    featureID.setGravity(Gravity.CENTER_HORIZONTAL);
                    featureID.setText(detailedErrors.getJSONObject(i).getString("featureID"));
                    featureID.setLayoutParams(tableParams);
                    tr.addView(featureID);

                    TextView errorCoordinateX = new TextView(this);
                    errorCoordinateX.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    errorCoordinateX.setTextSize(12);
                    errorCoordinateX.setTextColor(Color.BLACK);
                    errorCoordinateX.setGravity(Gravity.CENTER_HORIZONTAL);
                    errorCoordinateX.setText(detailedErrors.getJSONObject(i).getString("errCoorX"));
                    errorCoordinateX.setLayoutParams(tableParams);
                    tr.addView(errorCoordinateX);

                    TextView errorCoordinateY = new TextView(this);
                    errorCoordinateY.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    errorCoordinateY.setTextSize(12);
                    errorCoordinateY.setTextColor(Color.BLACK);
                    errorCoordinateY.setGravity(Gravity.CENTER_HORIZONTAL);
                    errorCoordinateY.setText(detailedErrors.getJSONObject(i).getString("errCoorY"));
                    errorCoordinateY.setLayoutParams(tableParams);
                    tr.addView(errorCoordinateY);
                       /* Add row to TableLayout. */
                    tl.addView(tr, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
