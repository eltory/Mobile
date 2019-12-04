package com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 에러 레포트 생성
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class ValidationErrorReportDialog extends ArbiterDialogFragment {

    private JSONArray detailedErrors = new JSONArray();
    private TableLayout tl;
    private TextView currentPos;
    private TextView lastPos;
    private boolean check;
    private String upToDateReport = "report"; // SharedPreferences for validation report
    private ProgressDialog reportProgressDialog;
    private int currentItemPos = 0;
    private int itemSize = 0;
    private final int pageShow = 50; // Maximum number of item in a page

    /**
     * @param title,ok,layout,reportProgressDialog String,String,int,ProgressDialog
     * @return ValidationErrorReportDialog
     * @author JiJungKeun
     */
    public static ValidationErrorReportDialog newInstance(String title, String ok, int layout, ProgressDialog reportProgressDialog) {
        ValidationErrorReportDialog frag = new ValidationErrorReportDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setLayout(layout);
        frag.reportProgressDialog = reportProgressDialog;

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(getLayout(), null);
        beforeCreateDialog(view);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.icon)
                .setTitle(getTitle())
                .setView(view)
                .setPositiveButton(getOk(),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                onPositiveClick();
                            }
                        }
                )
                .setNegativeButton(getCancel(),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                onNegativeClick();
                            }
                        }
                )
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //Progress dialog will be dismissed when report dialog appear
                doneErrorReportDialog();
            }
        });
        dialog.setCanceledOnTouchOutside(false);

        if (check == true)
            showValidationErrorReportDialog();

        return dialog;
    }

    @Override
    public void onPositiveClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNegativeClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeCreateDialog(View view) {
        // TODO Auto-generated method stub

        tl = (TableLayout) view.findViewById(R.id.ErrorReportTable);
        currentPos = (TextView) view.findViewById(R.id.currentPosition);
        lastPos = (TextView) view.findViewById(R.id.lastPosition);

        SharedPreferences getErrorReport = this.getActivity().getSharedPreferences(upToDateReport, 0);
        try {
            JSONObject validationResult = new JSONObject(getErrorReport.getString("report", ""));
            detailedErrors = validationResult.getJSONArray("features");
            itemSize = detailedErrors.length();
            check = getErrorReport.getBoolean("check", false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (check == true) {
            if (pageShow < itemSize) {
                view.findViewById(R.id.reportLeftButton).setOnClickListener(leftClickListener);
                view.findViewById(R.id.reportRightButton).setOnClickListener(rightClickListener);
            }
        }
    }

    ImageButton.OnClickListener leftClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (currentItemPos >= pageShow) {
                tl.removeViews(1, tl.getChildCount() - 1);
                currentItemPos = currentItemPos - pageShow;
                showValidationErrorReportDialog();
            }
        }
    };

    ImageButton.OnClickListener rightClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if ((currentItemPos + pageShow) < itemSize) {
                tl.removeViews(1, tl.getChildCount() - 1);
                currentItemPos = currentItemPos + pageShow;
                showValidationErrorReportDialog();
            }
        }
    };

    /**
     * 페이징 처리하여 동적 테이블 레이아웃 레포트 생성
     *
     * @author JiJungKeun
     */
    private void showValidationErrorReportDialog() {
        int length = 0;
        int start = 0;

        currentPos.setText("" + (currentItemPos + 1));
        lastPos.setText("" + itemSize);

        //last page
        if (itemSize <= pageShow + currentItemPos) {
            length = itemSize;
        }
        //exist next page
        else {
            length = pageShow + currentItemPos;
        }

        start = currentItemPos;

        try {
            for (int i = start; i < length; i++) {
                TableRow tr = new TableRow(this.getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tr.setBackgroundColor(Color.BLACK);
                tr.setPadding(1, 1, 1, 1);

                TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                tableParams.rightMargin = 1;

                TextView Number = new TextView(this.getActivity());
                Number.setBackgroundColor(Color.parseColor("#DCDCDC"));
                Number.setTextSize(12);
                Number.setTextColor(Color.BLACK);
                Number.setGravity(Gravity.CENTER_HORIZONTAL);
                Number.setText((i + 1) + "");
                Number.setLayoutParams(tableParams);
                tr.addView(Number);

                TextView errorType = new TextView(this.getActivity());
                errorType.setBackgroundColor(Color.parseColor("#DCDCDC"));
                errorType.setTextSize(12);
                errorType.setTextColor(Color.BLACK);
                errorType.setGravity(Gravity.CENTER_HORIZONTAL);
                errorType.setText(detailedErrors.getJSONObject(i).getJSONObject("properties").getString("errType"));
                errorType.setLayoutParams(tableParams);
                tr.addView(errorType);

                TextView errorName = new TextView(this.getActivity());
                errorName.setBackgroundColor(Color.parseColor("#DCDCDC"));
                errorName.setTextSize(12);
                errorName.setTextColor(Color.BLACK);
                errorName.setGravity(Gravity.CENTER_HORIZONTAL);
                errorName.setText(detailedErrors.getJSONObject(i).getJSONObject("properties").getString("errName"));
                errorName.setLayoutParams(tableParams);
                tr.addView(errorName);

                TextView featureID = new TextView(this.getActivity());
                featureID.setBackgroundColor(Color.parseColor("#DCDCDC"));
                featureID.setTextSize(12);
                featureID.setTextColor(Color.BLACK);
                featureID.setGravity(Gravity.CENTER_HORIZONTAL);
                featureID.setText(detailedErrors.getJSONObject(i).getJSONObject("properties").getString("featureID"));
                featureID.setLayoutParams(tableParams);
                tr.addView(featureID);

                TextView errorCoordinateX = new TextView(this.getActivity());
                errorCoordinateX.setBackgroundColor(Color.parseColor("#DCDCDC"));
                errorCoordinateX.setTextSize(12);
                errorCoordinateX.setTextColor(Color.BLACK);
                errorCoordinateX.setGravity(Gravity.CENTER_HORIZONTAL);
                errorCoordinateX.setText(String.valueOf(detailedErrors.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(0)));
                errorCoordinateX.setLayoutParams(tableParams);
                tr.addView(errorCoordinateX);

                TextView errorCoordinateY = new TextView(this.getActivity());
                errorCoordinateY.setBackgroundColor(Color.parseColor("#DCDCDC"));
                errorCoordinateY.setTextSize(12);
                errorCoordinateY.setTextColor(Color.BLACK);
                errorCoordinateY.setGravity(Gravity.CENTER_HORIZONTAL);
                errorCoordinateY.setText(String.valueOf(detailedErrors.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(1)));
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

    /**
     * 레포트 생성 완료 후, ProgressDialog 제거
     *
     * @author JiJungKeun
     */
    public void doneErrorReportDialog() {
        if (reportProgressDialog != null)
            reportProgressDialog.dismiss();
    }

}
