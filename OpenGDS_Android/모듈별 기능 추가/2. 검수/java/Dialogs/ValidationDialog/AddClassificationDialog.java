package com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Fix;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Layer;
import com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter.AddLayersListAdapter;
import com.lmn.OpenGDS_Android.ListAdapters.GeometryTypesAdapter;

/**
 * 각 분류의 레이어 추가를 위한 다이얼로그
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.6.
 */

public class AddClassificationDialog extends ArbiterDialogFragment {

    public static final String TAG = AddClassificationDialog.class.getName();

    private AddLayersListAdapter layersListAdapter;
    private ListView listView;
    private ImageButton addFix;
    private EditText code;
    private Spinner typeSpinner;
    private GeometryTypesAdapter typeAdapter;
    private String geoType;
    private Layer layer;
    private Activity activity;
    private int position = 0;
    private static AddClassificationDialog dialog;

    public static AddClassificationDialog newInstance(String title, String ok,
                                                      String cancel, int layout, int position, Layer layer, String geoType, Activity activity) {
        AddClassificationDialog frag = new AddClassificationDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        frag.position = position;
        frag.layer = layer;
        frag.geoType = geoType;
        frag.activity = activity;

        dialog = frag;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.getActivity().getSupportLoaderManager().destroyLoader(R.id.loader_classification_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        layersListAdapter.setWidget(this.addFix, dialog.getPositiveButton());
    }

    /**
     * 옵션값 적용한 레이어와 해당 레이어가 속한 분류에 추가하여 업데이트
     */
    @Override
    public void onPositiveClick() {

        Activity activity = getActivity();
        Context context = activity.getApplicationContext();

        String title = context.getResources().getString(R.string.add_classification_dialog_title);
        String message = context.getResources().getString(R.string.please_wait);
        if (TextUtils.isEmpty(code.getText()))
            return;
        setLayer();
        ClassificationDialog.update(layer, position);
        getDialog().dismiss();
    }

    @Override
    public void onNegativeClick() {
    }


    @Override
    public void beforeCreateDialog(View view) {
        // Initialize the list of classification
        this.code = (EditText) view.findViewById(R.id.code_name_input);
        this.typeSpinner = (Spinner) view.findViewById(R.id.typesSpinner);
        this.listView = (ListView) view.findViewById(R.id.addFixListView);
        this.layersListAdapter = new AddLayersListAdapter(this.getActivity(), R.layout.add_fix, layer);
        this.typeAdapter = new GeometryTypesAdapter(getActivity());
        this.listView.setAdapter(layersListAdapter);
        this.typeSpinner.setAdapter(this.typeAdapter);

        this.code.setText(layer == null ? "" : layer.getCode());
        this.typeSpinner.setSelection(typeAdapter.getPositionFromType(geoType));
        this.typeSpinner.setBackgroundResource(android.R.color.darker_gray);
        this.typeSpinner.setEnabled(false);
        this.addFix = (ImageButton) view.findViewById(R.id.add_fix_button);
        this.addFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fix 추가 코드
                layersListAdapter.addFixToList(new Fix());
            }
        });
        layersListAdapter.setFocus((FrameLayout)view.findViewById(R.id.interceptor));
    }


    /**
     * 현재 추가되는 layer 객체가 null 일 경우 생성후 option 값 추가
     */
    public void setLayer() {
        layer = layersListAdapter.getLayer();
        if (layer == null)
            return;
        if (!code.getText().toString().equals("")) {
            layer.setCode(code.getText().toString());
        } else {
            return;
        }
        layer.setGeometryType(geoType);
        layer.setFix(layersListAdapter.getItems());
    }
}
