package com.lmn.OpenGDS_Android.Dialog.Dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.BaseClasses.Validation;
import com.lmn.OpenGDS_Android.ListAdapters.ValidationDetailOptionSettingAdapter;

import java.util.ArrayList;


/**
 * 세부 검수 옵션 설정
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class ValidationDetailOptionSettingDialog extends ArbiterDialogFragment{

    private ListView listView;
    private HasThreadPool hasThreadPool;
    private Validation selectedLayer;
    private String optionName;
    private LinearLayout layout;
    private EditText textBox;
    private TextView textView;
    private RelativeLayout selectedOptionNameLayout;
    private ValidationDetailOptionSettingAdapter validationDetailOptionSettingAdapter;

    /**
     * @author JiJungKeun
     * @param title,ok,cancel,layout,hasThreadPool,optionName,selectedLayer,selectedOptionNameLayout String,String,String,int,HasThreadPool,String,Validation,RelativeLayout
     * @return ValidationDetailOptionSettingDialog
     */
    public static ValidationDetailOptionSettingDialog newInstance(String title, String ok,
                                                                  String cancel, int layout, HasThreadPool hasThreadPool, String optionName, Validation selectedLayer, RelativeLayout selectedOptionNameLayout){

        ValidationDetailOptionSettingDialog frag = new ValidationDetailOptionSettingDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        frag.hasThreadPool = hasThreadPool;
        frag.selectedLayer = selectedLayer;
        frag.optionName = optionName;
        frag.selectedOptionNameLayout = selectedOptionNameLayout;

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        this.setValidatingClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                onPositiveClick();
            }
        });
    }

    @Override
    public void onCancel(DialogInterface dialog){
        onNegativeClick();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onPositiveClick() {

        //edit text box part
        if(optionName.equalsIgnoreCase("SMALLAREA"))
        {
                if(textBox.getText().toString().length() != 0)
                {
                    if (textBox.getText().toString().charAt(0) != '.')
                        selectedLayer.setSmallArea(textBox.getText().toString());

                    else
                        selectedLayer.setSmallArea(null);
                }
                else
                    selectedLayer.setSmallArea(null);
        }

        else if(optionName.equalsIgnoreCase("SMALLLENGTH"))
        {
                if(textBox.getText().toString().length() != 0)
                {
                    if(textBox.getText().toString().charAt(0) != '.')
                        selectedLayer.setSmallLength(textBox.getText().toString());

                    else
                        selectedLayer.setSmallLength(null);
                }
                else
                    selectedLayer.setSmallLength(null);
        }
        else if(optionName.equalsIgnoreCase("CONOVERDEGREE"))
        {
                if(textBox.getText().toString().length() != 0)
                {
                    if(textBox.getText().toString().charAt(0) != '.')
                        selectedLayer.setConOverDegree(textBox.getText().toString());

                    else
                        selectedLayer.setConOverDegree(null);
                }
                else
                    selectedLayer.setConOverDegree(null);
        }
        else if(optionName.equalsIgnoreCase("ATTRIBUTEFIX"))
        {
            ArrayList<String> checkedItems = validationDetailOptionSettingAdapter.getCheckedItems();

            if(checkedItems.size() > 0) {
                String value = "";

                for (int i = 0; i < checkedItems.size(); i++) {
                    value = value + checkedItems.get(i);

                    if (i < checkedItems.size() - 1)
                        value = value + ",";
                }

                selectedLayer.setAttributeFix(value);
                selectedLayer.setAttributeFixArr(value);
            }

            else {
                selectedLayer.setAttributeFix(null);
                selectedLayer.setAttributeFixArr(null);
            }
        }

        else if(optionName.equalsIgnoreCase("ZVALUEAMBIGUOUS"))
        {
            ArrayList<String> checkedItems = validationDetailOptionSettingAdapter.getCheckedItems();

            if(checkedItems.size() > 0) {
                String value = "";

                for (int i = 0; i < checkedItems.size(); i++) {
                    value = value + checkedItems.get(i);

                    if (i < checkedItems.size() - 1)
                        value = value + ",";
                }

                selectedLayer.setZValueAmbiguous(value);
                selectedLayer.setzValueAmbiguousArr(value);
            }

            else {
                selectedLayer.setZValueAmbiguous(null);
                selectedLayer.setzValueAmbiguousArr(null);
            }
        }

        else {}

        if(textBox!=null)
        {
            if (textBox.getText().toString().length() != 0)
            {
                if(textBox.getText().toString().charAt(0) != '.')
                    selectedOptionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);

                else
                    selectedOptionNameLayout.setBackgroundResource(R.color.white);
            }

            else
                selectedOptionNameLayout.setBackgroundResource(R.color.white);
        }

        if(validationDetailOptionSettingAdapter!=null)
        {
            if (validationDetailOptionSettingAdapter.getCheckedItems().size() != 0)
                selectedOptionNameLayout.setBackgroundResource(R.color.layer_list_item_pressed);

            else
                selectedOptionNameLayout.setBackgroundResource(R.color.white);
        }

        //update the selected layer info.
        ValidationOptionDialog.updateValidationLayerInfo(selectedLayer);

        getDialog().dismiss();

    }

    @Override
    public void onNegativeClick() {

    }

    @Override
    public void beforeCreateDialog(View view) {
        if(view != null){
            registerListeners(view);
                //For edit text UI
            if(optionName.equalsIgnoreCase("SMALLAREA") || optionName.equalsIgnoreCase("SMALLLENGTH") || optionName.equalsIgnoreCase("CONOVERDEGREE"))
               populateTextBox(view, optionName);

                //For check list UI
            else if(optionName.equalsIgnoreCase("ATTRIBUTEFIX") || optionName.equalsIgnoreCase("ZVALUEAMBIGUOUS"))
               populateDetailOptionsList(view);
        }
    }

    private void registerListeners(View view){
    }

    /**
     * EditText 이용하는 세부 검수 옵션
     * @author JiJungKeun
     * @param view,optionName View,String
     */
    private void populateTextBox(View view, String optionName)
    {
        if(optionName.equalsIgnoreCase("SMALLAREA")) {
            textBox = new EditText(getActivity());
            layout = (LinearLayout) view.findViewById(R.id.detailOptionLayout);
            textBox.setTextSize(18);
            textBox.setTextColor(Color.parseColor("#040404"));
            textBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            if(selectedLayer.getSmallArea() == null)
            {
                textBox.setHint("m\u00B2");
            }
            else
                textBox.setText(selectedLayer.getSmallArea());

            layout.addView(textBox);
        }

        else if(optionName.equalsIgnoreCase("SMALLLENGTH")) {
            textBox = new EditText(getActivity());
            layout = (LinearLayout) view.findViewById(R.id.detailOptionLayout);
            textBox.setTextSize(18);
            textBox.setTextColor(Color.parseColor("#040404"));
            textBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            if(selectedLayer.getSmallLength() == null)
            {
                textBox.setHint("m");
            }
            else
                textBox.setText(selectedLayer.getSmallLength());

            layout.addView(textBox);
        }

        else
        {
            textBox = new EditText(getActivity());
            layout = (LinearLayout) view.findViewById(R.id.detailOptionLayout);
            textBox.setTextSize(18);
            textBox.setTextColor(Color.parseColor("#040404"));
            textBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            if(selectedLayer.getConOverDegree() == null)
            {
                textBox.setHint("Degree");
            }
            else
                textBox.setText(selectedLayer.getConOverDegree());

            layout.addView(textBox);
        }

    }

    /**
     * 리스트 어댑터 생성하는 세부 검수 옵션
     * @author JiJungKeun
     * @param view View
     */
    private void populateDetailOptionsList(View view){
        this.listView = (ListView) view.findViewById(R.id.detailOptionListView);
        this.validationDetailOptionSettingAdapter = new ValidationDetailOptionSettingAdapter
                (this.getActivity().getApplicationContext(), R.layout.validation_detail_option_setting_list_item, optionName, selectedLayer);

        validationDetailOptionSettingAdapter.setData(selectedLayer.getAttributes());

        this.listView.setAdapter(this.validationDetailOptionSettingAdapter);
    }


}
