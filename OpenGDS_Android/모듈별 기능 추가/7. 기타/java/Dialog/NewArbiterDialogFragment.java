package com.lmn.OpenGDS_Android.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Classification;

import java.util.ArrayList;
import java.util.List;

/**
 * 각 단계마다 저장을 효율적으로 하기 위하여 부모 클래스에
 * dataSave, dataLoad 메서드 작성후 상속하여 쓰게함
 *
 * @param <T> 주 데이터
 * @param <S> 보조 데이터
 * @param <M> 주 데이터 복원객체
 * @param <N> 보조 데이터 복원객체
 * @author LeeSungHwi
 * @version 1.1 2018.07.18.
 */
public abstract class NewArbiterDialogFragment<T, S, M, N> extends DialogFragment {
    protected String title;
    protected String ok;
    protected String cancel;
    protected int layout;
    protected View.OnClickListener validatingClickListener = null;
    private AlertDialog myDialog;
    public static final String TAG = NewArbiterDialogFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        if (validatingClickListener != null) {
            Button positiveButton = myDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(validatingClickListener);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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

        this.myDialog = dialog;
        this.myDialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public View.OnClickListener getValidatingClickListener() {
        return this.validatingClickListener;
    }

    public void setValidatingClickListener(View.OnClickListener validatingClickListener) {
        this.validatingClickListener = validatingClickListener;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOk() {
        return this.ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getCancel() {
        return this.cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public int getLayout() {
        return this.layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    /**
     * 각 단계에서 알맞은 데이터 형식 저장
     *
     * @param t       주 데이터
     * @param s       보조 데이터
     * @param dataKey 주 데이터 key 값
     * @param typeKey 보조 데이터 key 값
     */
    public void dataSave(T t, S s, String dataKey, String typeKey) {
        Gson gson = new Gson();
        String data = gson.toJson(t);
        String types = gson.toJson(s);
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(dataKey, data);
        editor.putString(typeKey, types).commit();
    }

    /**
     * 각 단계에서 알맞은 데이터 형식 불러옴
     *
     * @param dataKey 주 데이터 key 값
     * @param typeKey 보조 데이터 key 값
     * @return Pair(주 데이터, 보조 데이터)
     */
    public Pair<T, S> dataLoad(String dataKey, String typeKey) {
        Gson gson = new Gson();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String data = preferences.getString(dataKey, null);
        String types = preferences.getString(typeKey, null);
        T list1 = null;
        S list2 = null;
        if (data != null) {
            list1 = gson.fromJson(data, new TypeToken<List<M>>() {
            }.getType());
            Log.e(TAG, "list1 : " + data);
        }
        if (types != null) {
            list2 = gson.fromJson(types, new TypeToken<List<N>>() {
            }.getType());
            Log.e(TAG, "list2 : " + types);
        }
        return Pair.create(list1, list2);
    }

    public abstract void beforeCreateDialog(View view);

    public abstract void onPositiveClick();

    public abstract void onNegativeClick();
}
