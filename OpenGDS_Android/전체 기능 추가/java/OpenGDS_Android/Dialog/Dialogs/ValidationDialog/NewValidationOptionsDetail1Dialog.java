package com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition.Tolerance;
import com.lmn.OpenGDS_Android.ListAdapters.ConditionAdapter;
import com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter.NewValidationOptionAdapter;

/**
 * 검수 옵션의 디테일 정보 다이얼로그
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.16.
 */
public class NewValidationOptionsDetail1Dialog extends ArbiterDialogFragment {

    private ListView listView;
    private String optionName;
    private Tolerance tolerance;
    private LinearLayout layout;
    private EditText textBox;
    private EditText valueEdit;
    private Spinner condition;
    private String conditionStr="equal";

    public static NewValidationOptionsDetail1Dialog newInstance(String title, String ok,
                                                               String cancel, int layout, HasThreadPool hasThreadPool, String optionName) {

        NewValidationOptionsDetail1Dialog frag = new NewValidationOptionsDetail1Dialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setLayout(layout);
        frag.optionName = optionName;

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        this.setValidatingClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onPositiveClick();
            }
        });
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onNegativeClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPositiveClick() {
        tolerance = new Tolerance();
        try {
            tolerance.setValue(Double.parseDouble(valueEdit.getText().toString()));
            tolerance.setCondition(conditionStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        NewValidationOptionAdapter.setTolerance(optionName, tolerance);
        getDialog().dismiss();

    }

    @Override
    public void onNegativeClick() {
        NewValidationOptionAdapter.unCheck(optionName);
    }

    @Override
    public void beforeCreateDialog(View view) {
        if (view != null) {
            registerListeners(view);
            valueEdit = (EditText) view.findViewById(R.id.value_input);
            condition = (Spinner) view.findViewById(R.id.condition_spinner);
            final ConditionAdapter adapter = new ConditionAdapter(getActivity());
            condition.setAdapter(adapter);

            condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub
                    conditionStr = adapter.getItem(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }

    private void registerListeners(View view) {
    }
}
