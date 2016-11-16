package com.lmn.Arbiter_Android.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.lmn.Arbiter_Android.R;

import org.apache.cordova.Config;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by pc on 2016-05-01.
 */
public class FindAreaActivity extends Activity  {

    private Geocoder mCoder;
    private EditText address;
    private ImageButton ok;
    private Button cancel;
    private ListView addressListView;
    private ArrayAdapter <String> adapter;
    private List<Address> location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config.init(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_area);

        mCoder = new Geocoder(this,getResources().getConfiguration().locale);

        address = (EditText) findViewById(R.id.address);
        ok = (ImageButton) findViewById(R.id.find);
        cancel = (Button) findViewById(R.id.addressSearchButton);
        addressListView = (ListView) findViewById(R.id.addressList);
        adapter = new ArrayAdapter (this, android.R.layout.simple_list_item_1);
        addressListView.setAdapter(adapter);
        ok.setOnClickListener(addressListener);
        cancel.setOnClickListener(cancelListener);
        addressListView.setOnItemClickListener(listViewExampleClickListener);

        this.setFinishOnTouchOutside(false); //prevent to exit this dialog activity when user touches outside activity.
    }

    AdapterView.OnItemClickListener listViewExampleClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parentView, View clickedView, int position, long id) {
            Intent intent = new Intent();
            double lat = location.get(position).getLatitude();
            double lon = location.get(position).getLongitude();
            String latLon = lat+","+lon;
            intent.putExtra("location", latLon);
            setResult(101, intent);
            finish();
        }
    };

    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    View.OnClickListener addressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            adapter.clear();
            String input = address.getText().toString();

            //Show maximum 5 address list
            try{
                location = mCoder.getFromLocationName(input, 5);
            }catch (IOException e)
            {
                Log.d("IO Exception error : ", e.getMessage());
                return;
            }

            //If search has no result, show alert dialog.
            if (location.size() == 0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.address_search_no_result_title);
                builder.setMessage(R.string.address_search_no_result_message);
                builder.setIcon(R.drawable.icon);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setCancelable(false);
                builder.create().show();
            }

            else
            {
                for (int i=0; i<location.size(); i++)
                {
                    Address addr = location.get(i);
                    int MaxIndex = addr.getMaxAddressLineIndex();
                    String item="";
                    if (MaxIndex == 0)
                        item = addr.getAddressLine(0);
                    else
                    {
                        for (int j=0; j<MaxIndex; j++)
                            item = item + addr.getAddressLine(j) + " ";

                    }
                    adapter.add(item);
                }
            }
            adapter.notifyDataSetChanged();
        }
    };
}
