package com.lmn.OpenGDS_Android.Dialog.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;

import com.lmn.Arbiter_Android.R;

import java.util.Locale;

/**
 * Created by JiJungKeun on 2016-11-18.
 */
public class MultiLanguageDialog
{
    private Locale locale;
    private CharSequence[] languageArr = null;
    private Activity activity;

    public MultiLanguageDialog(Activity activity)
    {
        this.activity = activity;
    }
    //Set wanted language to Multiple-language dialog
    public void setLanguage(CharSequence[] languageArr)
    {
        this.languageArr = languageArr;
    }

    //Show Multi-language setting dialog
    public void showMultiLanguageDialog() {
        if (languageArr != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(activity.getResources().getString(R.string.action_multiple_language));
            builder.setSingleChoiceItems(languageArr, -1,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int position) {

                            if (languageArr[position].equals("Korean"))
                                locale = new Locale("ko");

                            else if (languageArr[position].equals("English"))
                                locale = new Locale("en");

                            else if (languageArr[position].equals("Portugal"))
                                locale = new Locale("pt");

                            else if (languageArr[position].equals("Spain"))
                                locale = new Locale("es");

                            Configuration config = new Configuration();
                            config.locale = locale;
                            activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
                            activity.invalidateOptionsMenu();
                            dialog.dismiss();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.setIcon(activity.getResources().getDrawable(R.drawable.icon));
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }
    }
}
