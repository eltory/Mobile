package com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Classification;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Layer;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.ValidationAll;
import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;
import com.lmn.OpenGDS_Android.Dialog.NewArbiterDialogFragment;
import com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter.ExpandableLayersAdapter;
import com.lmn.OpenGDS_Android.ListAdapters.GeometryTypesAdapter;

import org.apache.cordova.CordovaWebView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 분류 추가를 위한 다이얼로그
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.06
 */

public class ClassificationDialog extends NewArbiterDialogFragment<ArrayList<Classification>, ArrayList<String>, Classification, String> {

    private static ArrayList<Classification> classifications = new ArrayList<Classification>();
    private static ArrayList<String> geoTypes = new ArrayList<String>();
    private ValidationAll validation = null;
    // 분류 중복 방지
    private static HashSet c_names = new HashSet();
    private static ExpandableLayersAdapter adapter;
    private static CordovaWebView cordovaWebView;
    private Spinner typeSpinner;
    private GeometryTypesAdapter typeAdapter;
    private ExpandableListView listView;
    private EditText nameInput;
    private static String geoType;
    private HasThreadPool hasThreadPool;
    private Activity context;
    private Pair<ArrayList<Classification>, ArrayList<String>> pair;

    public static ClassificationDialog newInstance(String title, String ok,
                                                   String cancel, int layout, HasThreadPool hasThreadPool, CordovaWebView cordova) {
        ClassificationDialog frag = new ClassificationDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        frag.hasThreadPool = hasThreadPool;
        cordovaWebView = cordova;

        return frag;
    }

    private static ArrayList<String> getClassificationNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < classifications.size(); ++i) {
            names.add(classifications.get(i).getName());
        }
        return names;
    }

    private static boolean isLayerNameContains(int position, String name) {
        if (classifications.get(position) == null)
            return false;
        ArrayList<Layer> layers = classifications.get(position).getLayers();
        for (int i = 0; i < layers.size(); ++i) {
            if (layers.get(i).getCode().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        dataLoad();
        setRetainInstance(true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getActivity().getSupportLoaderManager().destroyLoader(R.id.loader_classification_list);
    }

    @Override
    public void onStop() {
        super.onStop();
        dataSave();
    }

    /**
     * 확인시 검수 2단계인 각 분류의 레이어에 적용할 검수 옵션 다이얼로그 {@link NewValidationDialog}호출
     * 현재 생성된 분류(ArrayList) 전달
     */
    @Override
    public void onPositiveClick() {

        classifications = adapter.getLayerList();
        validation.setLayers(classifications);
        dataSave();

        // 1단계) 분류의 세부옵션 선택 다이얼로그 실행
        (new ArbiterDialogs_Expansion(getActivity().getApplicationContext(), getActivity().getResources(),
                getActivity().getSupportFragmentManager())).showNewValidationDialog(hasThreadPool, classifications, cordovaWebView);
        getDialog().dismiss();
    }

    @Override
    public void onNegativeClick() {
        dataSave(classifications, geoTypes, "classifications", "geoTypes");
    }

    @Override
    public void beforeCreateDialog(View view) {

        // Initialize the list of classification
        this.listView = (ExpandableListView) view.findViewById(R.id.classificationListView);
        this.adapter = new ExpandableLayersAdapter(getActivity(), classifications, geoTypes, hasThreadPool);
        this.listView.setAdapter(adapter);
        this.nameInput = (EditText) view.findViewById(R.id.classification_name);
        this.typeSpinner = (Spinner) view.findViewById(R.id.typesSpinner);
        this.typeAdapter = new GeometryTypesAdapter(getActivity());
        this.typeSpinner.setAdapter(this.typeAdapter);
        this.validation = new ValidationAll();
        dataLoad();

        ImageButton addClassification = (ImageButton) view.findViewById(R.id.add_classification_button);
        this.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                geoType = typeAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        if (addClassification != null) {
            addClassification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nameInput.getText().toString().equals("") || c_names.contains(nameInput.getText().toString()))
                        return;
                    Classification classification = new Classification();
                    String name = nameInput.getText().toString();
                    classification.setName(name);
                    c_names.add(name);
                    classifications.add(classification);
                    adapter.setParent(classifications, geoTypes);
                    geoTypes.add(geoType);
                    nameInput.setText("");
                }
            });
        }
    }

    /**
     * layer 추가 다이얼로그에서 확인 버튼 누를시 분류 리스트에 추가
     *
     * @param layer    분류 추가되는 layer 객체
     * @param position 선택한 분류 위치에 layer 객체 추가하기 위한 선택포지션
     */
    public static void update(Layer layer, int position) {
        if (!isLayerNameContains(position, layer.getCode())) {
            classifications.get(position).getLayers().add(layer);
            adapter.setParent(classifications, geoTypes);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 해당 position 의 classification 삭제
     *
     * @param position delete 될 classification 위치
     */
    public static void removeAtPosition(int position) {
        try {
            c_names.remove(classifications.get(position).getName());
            classifications.remove(position);
            geoTypes.remove(position);
            adapter.setParent(classifications, geoTypes);
            adapter.notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * 현재 검수 분류를 저장
     */
    private void dataSave() {
        Gson gson = new Gson();
        String data = gson.toJson(classifications);
        String types = gson.toJson(geoTypes);
        SharedPreferences preferences = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("classifications", data);
        editor.putString("geoTypes", types).commit();
    }

    /**
     * 기존 저장된 검수 분류를 불러옴
     */
    private void dataLoad() {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getPreferences(Context.MODE_PRIVATE);
        String data = preferences.getString("classifications", null);
        String types = preferences.getString("geoTypes", null);
        if (data != null) {
            classifications = gson.fromJson(data, new TypeToken<List<Classification>>() {
            }.getType());
        }
        if (types != null) {
            geoTypes = gson.fromJson(types, new TypeToken<List<String>>() {
            }.getType());
        }
        setDontDuplicated();
    }

    public void setDontDuplicated() {
        c_names = new HashSet<String>(getClassificationNames());
    }
}
