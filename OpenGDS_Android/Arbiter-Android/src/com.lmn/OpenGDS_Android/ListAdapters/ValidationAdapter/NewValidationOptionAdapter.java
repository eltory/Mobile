package com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.ListAdapters.ArbiterAdapter;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Classification;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition.Attribute;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition.DefinitionOptions;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition.Figure;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition.FigureAttribute;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition.Tolerance;
import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;
import com.lmn.OpenGDS_Android.BaseClasses.Validation;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * 세부설정을 타입지정하여 화면상에 보여주기 위한 어댑터
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.17.
 */
public class NewValidationOptionAdapter extends BaseAdapter {

    private static boolean[] checkedItems;
    private LayoutInflater inflater;
    private int itemLayout;
    private Context context;
    private DialogFragment dialog;
    private HasThreadPool hasThreadPool;
    private DefinitionOptions options;
    private static Tolerance tolerance;
    private static Attribute attribute;

    private static final String[] optStr = new String[]{
            "SmallArea",
            "SmallLength",
            "EntityDuplicated",
            "SelfEntity",
            "PointDuplicated",
            "ConIntersected",
            "ConOverDegree",
            "ConBreak",
            "ZValueAmbiguous",
            "UselessPoint"
    };

    private static Tolerance[] tolerances = new Tolerance[10];
    private static Figure figure = null;

    private boolean[] visible;
    private int[] polygon = new int[]{0, 2, 3, 4};
    private int[] line = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
    private int[] point = new int[]{2, 3};
    private static int[] geoType;

    public NewValidationOptionAdapter(Context context, DialogFragment dialog, HasThreadPool hasThreadPool, int itemLayout) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.itemLayout = itemLayout;
        this.dialog = dialog;
        this.hasThreadPool = hasThreadPool;
        this.checkedItems = new boolean[optStr.length];
        this.visible = new boolean[optStr.length];
    }

    /**
     * GeoType 설정하기
     *
     * @param geoType GeometryType
     */
    public void setData(int geoType) {
        if (geoType == 0) {
            this.geoType = polygon;
        } else if (geoType == 1) {
            this.geoType = line;
        } else if (geoType == 2) {
            this.geoType = point;
        }
        setItemType();
        notifyDataSetChanged();
    }

    /**
     * 해당 타입별로 리스트에 보여지는 아이템 설정
     */
    public void setItemType() {
        for (int i : geoType) {
            visible[i] = true;
        }
    }

    public class ViewHolder {
        TextView optionNameView;
        RelativeLayout back;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(itemLayout, null);

            holder = new ViewHolder();
            holder.optionNameView = (TextView) view.findViewById(R.id.optionItemName);
            holder.back = (RelativeLayout) view.findViewById(R.id.OptionSettingContainer);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final String listItem = optStr[geoType[position]];

        if (listItem != null) {

            holder.optionNameView.setText(listItem);
            holder.back.setBackgroundResource(R.color.white);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = geoType[position];
                    if ((pos == 0 || pos == 1 || pos == 6) && !checkedItems[pos]) {
                        (new ArbiterDialogs_Expansion(context.getApplicationContext(), context.getResources(),
                                dialog.getActivity().getSupportFragmentManager())).showDetailOption1Dialog(hasThreadPool, optStr[geoType[position]]);
                    } else if (pos == 8 && !checkedItems[pos]) {
                        (new ArbiterDialogs_Expansion(context.getApplicationContext(), context.getResources(),
                                dialog.getActivity().getSupportFragmentManager())).showDetailOption2Dialog(hasThreadPool, optStr[geoType[position]]);
                    } {
                        checkedItems[geoType[position]] = !checkedItems[geoType[position]];
                    }
                    holder.back.setBackgroundResource(getBackColor(geoType[position]));
                }
            });
        }
        return view;
    }

    /**
     * Tolerance 추가
     *
     * @param at  포지션
     * @param tol tolerance 객체
     */
    public static void setTolerance(String at, Tolerance tol) {
        tolerances[getPosition(at)] = tol;
    }

    /**
     * Attribute 추가
     *
     * @param attr
     */
    public static void setAttribute(FigureAttribute attr) {
        figure = new Figure();
        figure.setAttribute(attr);
    }

    /**
     * 세부옵션 다이얼로그 취소버튼시 해당 위치의 옵션 체크해제
     *
     * @param at 옵션 스트링
     */
    public static void unCheck(String at) {
        checkedItems[geoType[getPosition(at)]] = false;
    }

    /**
     * Builder 를 활용하여 type 별로 구분하지 않고 동일하게 처리
     * 세부설정 객체인 DefinitionOptions 생성
     *
     * @return 세부설정 객체
     */
    public DefinitionOptions getOptions() {
        return options = new DefinitionOptions.Builder()
                .setSmallArea(checkedItems[0], tolerances[0])
                .setSmallLength(checkedItems[1], tolerances[1])
                .setEntityDuplicated(checkedItems[2])
                .setSelfEntity(checkedItems[3])
                .setPointDuplicated(checkedItems[4])
                .setConIntersected(checkedItems[5])
                .setConOverDegree(checkedItems[6], tolerances[6])
                .setConBreak(checkedItems[7])
                .setZValueAmbiguous(checkedItems[8], figure)
                .setUseLessPoint(checkedItems[9])
                .build();
    }

    /**
     * 옵션 값의 위치를 반환하는 메서드
     *
     * @param at 찾고자하는 옵션
     * @return 옵션 위치
     */
    public static int getPosition(String at) {
        for (int i = 0; i < optStr.length; ++i) {
            if (optStr[i].equalsIgnoreCase(at))
                return i;
        }
        return 0;
    }

    /**
     * 터치에 따라 변하는 backgroundColor
     *
     * @param pos 현재 backgroundColor
     * @return 터치에 반응한 새로운 backgroundColor
     */
    public int getBackColor(int pos) {
        return checkedItems[pos] ? R.color.layer_list_item_pressed : R.color.white;
    }

    @Override
    public int getCount() {
        return geoType.length;
    }

    @Override
    public String getItem(int position) {
        return this.optStr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}