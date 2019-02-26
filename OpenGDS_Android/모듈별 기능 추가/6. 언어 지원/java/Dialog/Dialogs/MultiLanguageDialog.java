package com.lmn.OpenGDS_Android.Dialog.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;

import com.lmn.Arbiter_Android.R;

import java.util.Locale;

/**
 * 다중 언어 설정
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class MultiLanguageDialog {
    private Locale locale;
    private CharSequence[] languageArr = null;
    private Activity activity;

    public MultiLanguageDialog(Activity activity) {
        this.activity = activity;
    }

    /**
     * 언어 추가
     *
     * @param languageArr CharSequence[]
     * @author JiJungKeun
     */
    public void setLanguage(CharSequence[] languageArr) {
        this.languageArr = languageArr;
    }

    /**
     * 다중 언어 설정 다이얼로그 생성
     *
     * @author JiJungKeun
     */
    public void showMultiLanguageDialog() {
        if (languageArr != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(activity.getResources().getString(R.string.action_multiple_language))
                    .setSingleChoiceItems(languageArr, -1,
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
