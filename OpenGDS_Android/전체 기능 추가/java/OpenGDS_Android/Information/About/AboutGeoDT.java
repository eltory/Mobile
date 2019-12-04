package com.lmn.OpenGDS_Android.Information.About;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.lmn.Arbiter_Android.R;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AboutGeoDT extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_geodt);

        TextView about=(TextView)findViewById(R.id.about);
        TextView arbiter=(TextView)findViewById(R.id.arbiter);

        about.setMovementMethod(new ScrollingMovementMethod());

        Linkify.TransformFilter mTransform=new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };

        String temp=arbiter.getText().toString();
        arbiter.setText(temp.substring(0,temp.indexOf("\n")));

        temp=about.getText().toString();
        about.setText(temp.substring(temp.indexOf("\n")+1));

        Pattern Arbiter=Pattern.compile("Arbiter_mobile");
        Pattern LGPL=Pattern.compile("LGPL 3.0");
        Linkify.addLinks(arbiter,Arbiter,"https://github.com/ROGUE-JCTD/Arbiter-Android",null,mTransform);
        Linkify.addLinks(about,Linkify.ALL);
        Linkify.addLinks((TextView)findViewById(R.id.LGPL),LGPL,"https://opensource.org/licenses/lgpl-3.0.html",null,mTransform);
    }
}
