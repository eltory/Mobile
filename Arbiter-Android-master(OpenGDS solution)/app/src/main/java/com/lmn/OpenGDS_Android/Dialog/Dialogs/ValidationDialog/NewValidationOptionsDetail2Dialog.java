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
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition.FigureAttribute;
import com.lmn.OpenGDS_Android.ListAdapters.ConditionAdapter;
import com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter.NewValidationOptionAdapter;

/**
 * 검수 옵션의 디테일 정보 다이얼로그
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.16.
 */
public class NewValidationOptionsDetail2Dialog extends ArbiterDialogFragment {

    private ListView listView;
    private String optionName;
    private FigureAttribute attribute;
    private LinearLayout layout;
    private EditText keyEdit;
    private EditText intervalEdit;
    private EditText valueEdit;
    private Spinner condition;
    private String conditionStr="equal";

    public static NewValidationOptionsDetail2Dialog newInstance(String title, String ok,
                                                                String cancel, int layout, HasThreadPool hasThreadPool, String optionName) {

        NewValidationOptionsDetail2Dialog frag = new NewValidationOptionsDetail2Dialog();

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
        attribute = new FigureAttribute();
        try {
            attribute.setKey(keyEdit.getText().toString());
            attribute.setNumber(Double.parseDouble(valueEdit.getText().toString()));
            attribute.setInterval(Double.parseDouble(intervalEdit.getText().toString()));
            attribute.setCondition(conditionStr);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        NewValidationOptionAdapter.setAttribute(attribute);
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
            intervalEdit = (EditText) view.findViewById(R.id.interval_input);
            keyEdit = (EditText) view.findViewById(R.id.key_input);
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
