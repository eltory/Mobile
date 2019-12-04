package com.lmn.OpenGDS_Android.ListAdapters.ValidationAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.lmn.Arbiter_Android.ListAdapters.ArbiterAdapter;
import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Fix;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Layer;
import com.lmn.OpenGDS_Android.ListAdapters.TypesAdapter;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 분류-레이어에 들어가는 고정속성 어답터
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.06
 */
public class AddLayersListAdapter extends BaseAdapter implements ArbiterAdapter<ArrayList<Fix>> {
    public static final String TAG = AddLayersListAdapter.class.getName();

    private ArrayList<Fix> items;
    private Layer layer;
    private LayoutInflater inflater;
    private int itemLayout;
    private Context context;
    private StringTokenizer tokenizer;
    private ArrayList<String> values;
    private ArrayList val;
    private TypesAdapter typesAdapter;
    private int currPosition = 0;
    private FrameLayout layout;
    private EditText mEditText;

    /**
     * @param context    컨텍스트
     * @param itemLayout 다이얼로그의 레이아웃
     */
    public AddLayersListAdapter(Context context, int itemLayout) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.items = new ArrayList<Fix>();
        this.itemLayout = itemLayout;
        this.val = new ArrayList();
        this.typesAdapter = new TypesAdapter((Activity) context);
    }

    /**
     * @param context    컨텍스트
     * @param itemLayout 다이얼로그 레이아웃
     * @param layer      레이어객체
     */
    public AddLayersListAdapter(Context context, int itemLayout, Layer layer) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.itemLayout = itemLayout;
        this.val = new ArrayList();
        this.typesAdapter = new TypesAdapter((Activity) context);
        this.layer = layer != null ? this.layer = layer : new Layer();
        this.items = layer != null ? this.layer.getFix() : new ArrayList<Fix>();
    }

    public void setData(ArrayList<Fix> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public Layer getLayer() {
        return layer;
    }

    /**
     * 레이어객체에 고정속성인 Fix 추가
     *
     * @param layer Fix 객체
     */
    public void add(Fix layer) {
        items.add(layer);
    }

    /**
     * 뷰홀더 객체
     */
    public class ViewHolder {
        public <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }

        Spinner type;
        EditText name, length, value;
        CheckBox nullCheck;
        ImageButton delete;
        int position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        final Fix listItem = items.get(position);
        final ViewHolder holder;
        currPosition = position;


        if (view == null) {
            view = inflater.inflate(itemLayout, null);

            holder = new ViewHolder();
            holder.type = (Spinner) view.findViewById(R.id.types_spinner);
            holder.nullCheck = (CheckBox) view.findViewById(R.id.is_null);
            holder.delete = (ImageButton) view.findViewById(R.id.delete_fix_button);
            holder.name = (EditText) view.findViewById(R.id.fix_name_input);
            holder.length = (EditText) view.findViewById(R.id.length_input);
            holder.value = (EditText) view.findViewById(R.id.value_input);
            holder.position = position;
            holder.type.setAdapter(typesAdapter);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();

            /*  각 뷰 및 위젯마다 리스너 셋팅  */
            holder.name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int pos = v.getId();
                        final EditText Caption = (EditText) v;
                        final String input = Caption.getText().toString();
                        if (!input.equals("")) {
                            items.get(pos).setName(input);
                        }
                    }else{
                        mEditText = (EditText) v;
                    }
                }
            });
            holder.length.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int pos = v.getId();
                        final EditText Caption = (EditText) v;
                        final String input = Caption.getText().toString();
                        if (!input.equals("")) {
                            Double val = null;
                            try {
                                val = Double.parseDouble(input);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            items.get(pos).setLength(val);
                        }
                    }else{
                        mEditText = (EditText) v;
                    }
                }
            });
            // Values tokenizer
            holder.value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int pos = v.getId();
                        final EditText Caption = (EditText) v;
                        final String input = Caption.getText().toString();
                        if (!input.equals("")) {
                            ArrayList strValues = listItem.getValues();
                            tokenizer = new StringTokenizer(input, ",");
                            while (tokenizer.hasMoreTokens()) {
                                strValues.add(tokenizer.nextToken());
                            }
                            items.get(pos).setValues(strValues);
                        }
                    }else{
                        mEditText = (EditText) v;
                    }
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteFixFromList(position);
                }
            });
            holder.nullCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    items.get(position).setNull(isChecked);
                }
            });
            holder.type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    items.get(position).setType(pos != 0 ? typesAdapter.getItem(pos) : null);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        if (listItem != null) {
            /*  Fix 수정할때 기존정보 표시  */
            // 속성명
            holder.name.setText(listItem.getName() != null ? listItem.getName() : "");
            holder.name.setId(position);
            // 길이
            holder.length.setText(listItem.getLength() != null ? String.valueOf(listItem.getLength()) : "");
            holder.length.setId(position);
            // 속성값
            holder.value.setText(listItem.getValues().size() != 0 ? union(listItem.getValues()) : "");
            holder.value.setId(position);
            // 타입
            holder.type.setSelection(getIndex(holder.type, listItem.getType()));
            holder.value.setId(position);
            // 널 체크
            holder.nullCheck.setChecked(listItem.isNull());
            holder.nullCheck.setId(position);
        }
        return view;
    }

    public void setFocus(FrameLayout layout) {
        this.layout = layout;
        this.layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mEditText.isFocused()) {
                        Rect outRect = new Rect();
                        mEditText.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            mEditText.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }return false;
            }
        });
    }

    public void setWidget(ImageButton button, Button pButton){
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && mEditText!=null) {
                    if (mEditText.isFocused()) {
                        Rect outRect = new Rect();
                        mEditText.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            mEditText.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }return false;
            }
        });

        pButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && mEditText!=null) {
                    if (mEditText.isFocused()) {
                        Rect outRect = new Rect();
                        mEditText.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            mEditText.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }return false;
            }
        });
    }
    /**
     * Fix 의 Values 를 재조합 해서 표시하기 위한 메서드
     *
     * @param str String 객체를 담고있는 ArrayList
     * @return 다시 ','를 기준으로 조합된 String
     */
    public String union(ArrayList<String> str) {
        String unionString = "";
        for (int i = 0; i < str.size(); ++i) {
            if (i == str.size() - 1) {
                unionString += str.get(i);
            } else {
                unionString += (str.get(i) + ",");
            }
        }
        return unionString;
    }

    public int getIndex(Spinner spinner, String val) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); ++i) {
            if (spinner.getItemAtPosition(i).equals(val)) {
                index = i;
            }
        }
        return index;
    }

    public void addFixToList(Fix fix) {
        this.items.add(fix);
        notifyDataSetChanged();
    }

    public void deleteFixFromList(int position) {
        this.items.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (this.items != null) {
            return this.items.size();
        }
        return 0;
    }

    @Override
    public Fix getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<Fix> getItems() {
        if (items.size() == 0 || items == null)
            return null;
        return items;
    }
}
