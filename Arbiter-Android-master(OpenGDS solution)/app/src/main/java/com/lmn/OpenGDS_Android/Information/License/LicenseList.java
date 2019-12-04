package com.lmn.OpenGDS_Android.Information.License;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import com.lmn.Arbiter_Android.R;

public class LicenseList extends Activity {
    static final String[] listString = {"Android SDK", "Cordova", "GSON", "JQuery", "OpenLayers", "Proj4js"};
    static final String[] detailString = new String[6];
    private ArrayList<LicenseItem> data = null;
    private LicenseItem[] temp = new LicenseItem[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_list);

        ListView listView = (ListView) findViewById(R.id.licenseList);

        detailString[0] = getString(R.string.AndroidSDK);
        detailString[1] = getString(R.string.Cordova);
        detailString[2] = getString(R.string.GSON);
        detailString[3] = getString(R.string.JQuery);
        detailString[4] = getString(R.string.OpenLayers);
        detailString[5] = getString(R.string.Proj4js);

        data = new ArrayList<LicenseItem>();
        for (int i = 0; i < 6; i++) {
            data.add(new LicenseItem(listString[i]));
        }

        LicenseAdapter adapter = new LicenseAdapter(this, R.layout.license_item, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), LicenseDetail.class);

                intent.putExtra("detail", detailString[position]);
                startActivity(intent);
            }
        });
    }
}
