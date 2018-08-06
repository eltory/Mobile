package com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Classification;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition.DefinitionOptions;
import com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter.NewValidationOptionAdapter;

/**
 * 해당 분류의 검수 옵션 다이얼로그
 *
 * @author 앙LeeSungHwi띠
 * @version 1.1 2018.07.16.
 */
public class NewValidationOptionsDialog extends ArbiterDialogFragment {

    private ListView listView;
    private static NewValidationOptionAdapter validationOptionSettingAdapter;
    private HasThreadPool hasThreadPool;
    private Classification classification;
    private static DefinitionOptions option;

    public static NewValidationOptionsDialog newInstance(String title, String ok,
                                                         String cancel, int layout, HasThreadPool hasThreadPool, Classification classification) {

        NewValidationOptionsDialog frag = new NewValidationOptionsDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        frag.hasThreadPool = hasThreadPool;
        frag.classification = classification;

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
        setOptions();

        NewValidationDialog.setOptions(classification.getName(), option);
        getDialog().dismiss();
    }

    @Override
    public void onNegativeClick() {

    }

    @Override
    public void beforeCreateDialog(View view) {
        if (view != null) {
            registerListeners(view);
            populateLayersList(view);
        }
    }

    private void registerListeners(View view) {
    }

    private void populateLayersList(View view) {
        this.listView = (ListView) view.findViewById(R.id.validationOptionListView);
        this.validationOptionSettingAdapter = new NewValidationOptionAdapter
                (this.getActivity().getApplicationContext(), this, hasThreadPool, R.layout.validation_option_setting_list_item);

        if (classification.getLayers().size() == 0) {
            return;
        }

        final String geometryType = classification.getLayers().get(0).getGeometryType();

        if (geometryType.toUpperCase().contains("POLYGON")) {
            validationOptionSettingAdapter.setData(0);
        } else if (geometryType.toUpperCase().contains("LINESTRING")) {
            validationOptionSettingAdapter.setData(1);
        } else if (geometryType.toUpperCase().contains("POINT")) {
            validationOptionSettingAdapter.setData(2);
        }

        this.listView.setAdapter(this.validationOptionSettingAdapter);
    }

    public static void setOptions() {
        option = validationOptionSettingAdapter.getOptions();
    }
}
