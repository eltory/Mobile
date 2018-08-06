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
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition.OptionsDefinition;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.DetailOptions;
import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;
import com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter.NewValidationAdapter;

import org.apache.cordova.CordovaWebView;

import java.util.ArrayList;

/**
 * 업데이트된 검수 방식
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.16.
 */
public class NewValidationDialog extends ArbiterDialogFragment {

    public static final String TAG = ValidationOptionDialog.class.getName();

    private ListView listView;
    private NewValidationAdapter adapter;
    private HasThreadPool hasThreadPool;
    private static CordovaWebView cordovaWebView;
    private ArrayList<Classification> classifications;
    private static OptionsDefinition definition;
    private static DetailOptions option;

    public static NewValidationDialog newInstance(String title, String ok,
                                                  String cancel, int layout, HasThreadPool hasThreadPool, ArrayList<Classification> classifications, CordovaWebView cordova) {

        NewValidationDialog frag = new NewValidationDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        frag.classifications = classifications;
        cordovaWebView = cordova;
        return frag;
    }

    public NewValidationDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        this.setValidatingClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onPositiveClick();
                getDialog().dismiss();
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

    /**
     * 검수옵션값 설정후 검사할 레이어(서버) 선택 다이얼로그 {@link NewValidationLayerDialog}호출
     * 분류(ArrayList)와 옵션(ArrayList)를 전달
     */
    @Override
    public void onPositiveClick() {

        // 2단계) 서버의 레이어 선택 다이얼로그 실행
        (new ArbiterDialogs_Expansion(getActivity().getApplicationContext(), getActivity().getResources(),
                getActivity().getSupportFragmentManager())).showLayerSelectionDialog(hasThreadPool, classifications, option, cordovaWebView);
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
        this.adapter = new NewValidationAdapter(getActivity(), classifications);
        this.listView.setAdapter(this.adapter);
    }

    /**
     * Option 객체 데이터 셋팅
     *
     * @param name    옵션 이름
     * @param options 옵션 정의
     */
    public static void setOptions(String name, DefinitionOptions options) {
        if (option == null)
            option = new DetailOptions();
        if(!hasName(name)) {
            definition = new OptionsDefinition(name, options);
            option.getDefinition().add(definition);
        }
    }

    /**
     * 옵션중복처리
     *
     * @param name 현재 옵션에 추가된 검수옵션 이름
     * @return
     */
    public static boolean hasName(String name){
        ArrayList<OptionsDefinition> arrayList =  option.getDefinition();
        for(OptionsDefinition o : arrayList) {
           if(o.getName().equalsIgnoreCase(name)){
               return true;
           }
        }
        return false;
    }
}
