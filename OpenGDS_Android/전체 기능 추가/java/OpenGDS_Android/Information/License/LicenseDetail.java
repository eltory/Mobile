package com.lmn.OpenGDS_Android.Information.License;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.lmn.Arbiter_Android.R;

public class LicenseDetail extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_detail);

        Intent intent=getIntent();
        TextView detail=(TextView)findViewById(R.id.detail);
        detail.setText(intent.getStringExtra("detail"));
    }
}
