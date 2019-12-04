package com.lmn.OpenGDS_Android.Information;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.Information.About.AboutGeoDT;
import com.lmn.OpenGDS_Android.Information.License.LicenseList;

public class AboutMain extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_main);

        LinearLayout geoDTLayout=(LinearLayout)findViewById(R.id.geoMobileLayout);
        LinearLayout openSourceLayout=(LinearLayout)findViewById(R.id.openSourceLayout);

        geoDTLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AboutGeoDT.class);
                startActivity(intent);
            }
        });

        openSourceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LicenseList.class);
                startActivity(intent);
            }
        });
    }
}

