package com.lmn.OpenGDS_Android.Capture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;

import com.lmn.Arbiter_Android.R;

import org.apache.cordova.CordovaWebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * WebView 캡쳐
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class Capture {

    private CordovaWebView cordovaWebView;
    private Activity activity;

    /**
     * @param cordovaWebView,activity CordovaWebView,Activity
     * @author JiJungKeun
     */
    public Capture(CordovaWebView cordovaWebView, Activity activity) {
        this.cordovaWebView = cordovaWebView;
        this.activity = activity;
    }

    /**
     * 웹뷰 캡쳐 후 갤러리에 이미지 파일로 저장
     *
     * @param fileName String
     * @return void
     * @author JiJungKeun
     */
    public void startCapture(String fileName) {
        fileName = fileName + ".png";

        cordovaWebView.setDrawingCacheEnabled(true);

        Bitmap screenshot = Bitmap.createBitmap(cordovaWebView.getWidth(), cordovaWebView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);

        cordovaWebView.draw(c);

        try {
            File f = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/", fileName); //이미지 저장 경로

            f.createNewFile();
            OutputStream outStream = new FileOutputStream(f);

            screenshot.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setCancelable(false)
                    .setTitle(R.string.action_capture)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        cordovaWebView.setDrawingCacheEnabled(false);

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/", fileName); //이미지 저장 경로
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }
}
