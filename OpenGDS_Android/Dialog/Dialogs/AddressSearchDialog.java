package com.lmn.OpenGDS_Android.Dialog.Dialogs;

import android.app.AlertDialog;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.OpenGDS_Android.ListAdapters.AddressSearchListAdapter;
import com.lmn.Arbiter_Android.R;

import org.apache.cordova.CordovaWebView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 주소 검색 다이얼로그
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class AddressSearchDialog extends ArbiterDialogFragment {

    private EditText address;
    private LinearLayout searchResultContainer;
    private ImageButton search;
    private static CordovaWebView cordova;
    private Geocoder mCoder;
    private HasThreadPool hasThreadPool;
    private List<Address> location;
    private ArrayList<String> addressResults = new ArrayList<String>();
    private DialogFragment thisDialog;

    /**
     * @param title,ok,cancel,layout,cordovaWebView,hasThreadPool String,String,String,int,CordovaWebView,HasThreadPool
     * @return AddressSearchDialog
     * @author JiJungKeun
     */
    public static AddressSearchDialog newInstance(String title, String ok,
                                                  String cancel, int layout, CordovaWebView cordovaWebView, HasThreadPool hasThreadPool) {
        AddressSearchDialog frag = new AddressSearchDialog();

        frag.setTitle(title);
        frag.setCancel(cancel);
        frag.setLayout(layout);
        cordova = cordovaWebView;
        frag.hasThreadPool = hasThreadPool;

        return frag;
    }

    /**
     * GeoCoder 생성, View 객체 생성 및 리스너 설정
     *
     * @param view View
     * @return void
     * @author JiJungKeun
     */
    @Override
    public void beforeCreateDialog(View view) {
        mCoder = new Geocoder(this.getActivity(), this.getActivity().getResources().getConfiguration().locale);
        searchResultContainer = (LinearLayout) view.findViewById(R.id.AddressSearchResultContainer);
        address = (EditText) view.findViewById(R.id.address);
        search = (ImageButton) view.findViewById(R.id.searchButton);
        search.setOnClickListener(addressSearchListener);
        thisDialog = this;
    }

    View.OnClickListener addressSearchListener = new View.OnClickListener() {
        /**
         * 주소 검색 결과 리스트 생성
         * @author JiJungKeun
         * @param v View
         * @return void
         */
        @Override
        public void onClick(View v) {

            addressResults.clear();

            String input = address.getText().toString();
            //Show maximum 5 address list
            try {
                location = mCoder.getFromLocationName(input, 5);
            } catch (IOException e) {
                Log.d("IO Exception error : ", e.getMessage());
                return;
            }

            //If result is not exist, show alert dialog.
            if (location.size() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.address_search_no_result_title)
                        .setMessage(R.string.address_search_no_result_message)
                        .setIcon(R.drawable.icon)
                        .setPositiveButton(android.R.string.ok, null)
                        .setCancelable(false)
                        .create().show();
            } else {
                AddressSearchListAdapter addressSearchListAdapter = new AddressSearchListAdapter(searchResultContainer, getActivity(), thisDialog, R.layout.address_search_result_item, hasThreadPool, cordova, location);

                for (int i = 0; i < location.size(); i++) {
                    Address addr = location.get(i);
                    int MaxIndex = addr.getMaxAddressLineIndex();
                    String item = "";
                    if (MaxIndex == 0)
                        item = addr.getAddressLine(0);
                    else {
                        for (int j = 0; j < MaxIndex; j++)
                            item = item + addr.getAddressLine(j) + " ";
                    }
                    addressResults.add(item);
                }
                addressSearchListAdapter.setData(addressResults);
            }
        }
    };

    @Override
    public void onPositiveClick() {
    }

    @Override
    public void onNegativeClick() {
    }
}
