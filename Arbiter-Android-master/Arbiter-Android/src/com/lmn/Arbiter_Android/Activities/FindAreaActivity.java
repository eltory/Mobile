package com.lmn.Arbiter_Android.Activities;

import android.app.Activity;
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

    Geocoder mCoder;
    static double lat,lon;
    EditText address;
    ImageButton ok;
    ListView addressListView;
    ArrayAdapter <String> adapter;
    List<Address> location;
    List<Address> addressResult = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config.init(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //delete titlebar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_area);

        mCoder = new Geocoder(this,getResources().getConfiguration().locale);

        address = (EditText) findViewById(R.id.address);
        ok = (ImageButton) findViewById(R.id.find);
        addressListView = (ListView) findViewById(R.id.addressList);
        adapter = new ArrayAdapter (this, android.R.layout.simple_list_item_1);
        addressListView.setAdapter(adapter);
        ok.setOnClickListener(addressListener);
        addressListView.setOnItemClickListener(listViewExampleClickListener);

        this.setFinishOnTouchOutside(false); //prevent to exit this dialog activity when user touches outside activity.
    }
    AdapterView.OnItemClickListener listViewExampleClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parentView, View clickedView, int position, long id) {
            Intent intent = new Intent();
            double lat = location.get(position).getLatitude();
            double lon = location.get(position).getLongitude();
            String latlon = lat+","+lon;
            intent.putExtra("location", latlon);
            setResult(101, intent);
            finish();
        }
    };
    View.OnClickListener addressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            adapter.clear();
            String saddr = address.getText().toString();

            try{
                location = mCoder.getFromLocationName(saddr,5);

            }catch (IOException e)
            {
                Log.d("IO error : ", e.getMessage());
                return;
            }

            if (location.size() == 0)
            {
                Log.d("no result","no result");
                return;
            }
            else
            {
                for (int i=0; i<location.size(); i++)
                {
                    Address addr = location.get(i);
                    int MaxIndex = addr.getMaxAddressLineIndex();
                    String ad="";
                    if (MaxIndex == 0)
                        ad = addr.getAddressLine(0);
                    else
                    {
                        for (int j=0; j<MaxIndex; j++)
                            ad = ad + addr.getAddressLine(j) + " ";

                    }
                    adapter.add(ad);
                }
            }
            adapter.notifyDataSetChanged();
        }
    };
}
