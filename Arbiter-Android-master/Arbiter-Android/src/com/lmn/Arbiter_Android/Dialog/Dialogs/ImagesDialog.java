package com.lmn.Arbiter_Android.Dialog.Dialogs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ImageButton;

import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoading;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoadingJob;
import com.lmn.Arbiter_Android.BaseClasses.Image;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogs;
import com.lmn.Arbiter_Android.ListAdapters.ImageList;
import com.lmn.Arbiter_Android.R;
import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;

import org.apache.cordova.CordovaWebView;
import org.json.JSONObject;


public class ImagesDialog extends ArbiterDialogFragment{

    private ImageList imageList;
    private final int REQ_CODE_SELECT_IMAGE = 100;
    private ImageButton addImagesBtn;
    private HasThreadPool hasThreadPool;
    private static CordovaWebView cordova;
    private String sfName = "imgData";
    private String[] items = {"Draw Image", "Set boundary value"};

    private ArrayList<Image> imgData = new ArrayList<Image>();

    public static ImagesDialog newInstance(String title, String ok,
                                           String cancel, int layout, HasThreadPool hasThreadPool, CordovaWebView cordovaWebView){
        ImagesDialog frag = new ImagesDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setLayout(layout);
        frag.hasThreadPool = hasThreadPool;
        cordova = cordovaWebView;

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onPositiveClick() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNegativeClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeCreateDialog(View view) {
        if(view != null){
            registerListeners(view);
        }
    }

    private void displayImagesLimit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.images_limit);
        builder.setMessage(R.string.too_many_images);

        builder.create().show();
    }

    public void populateListView(View view){
        populateOverlayList(view);
    }

    private void populateOverlayList(View view){

        imgData.clear();

        LinearLayout imageList = (LinearLayout) view.findViewById(R.id.imagesList);

        this.imageList = new ImageList(imageList, this.getActivity(), R.layout.images_list_item, hasThreadPool);

        SharedPreferences reloadImageData = this.getActivity().getSharedPreferences(sfName, 0);

        for(int i=0; i<reloadImageData.getInt("size", 0); i++)
        {
            Image image = new Image();
            image.setName(reloadImageData.getString("name"+i,"" ));
            image.setPath(reloadImageData.getString("path"+i,"" ));

            imgData.add(image);
        }

        this.imageList.setData(imgData); //setting previous stored image data
    }


    public void registerListeners(View view){

        this.addImagesBtn = (ImageButton) view.findViewById(R.id.add_images_button);

        populateListView(view);

        final ImagesDialog frag = this;

        this.addImagesBtn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {

                    imgData.clear();

                    Image image = getImageNameToUri(data.getData(), this.getActivity());


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle(R.string.action_image_option);

                    builder.setSingleChoiceItems(items, -1,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {

                                    //Draw Image using BBox
                                    if(id == 0) {
                                        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
                                            @Override
                                            public void run() {

                                                String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.drawBBox(\"" + image.getName() + "\", \"" + image.getPath() + "\");'))";
                                                cordova.loadUrl(url);

                                            }
                                        });
                                    }

                                    //set boundary value
                                    else
                                    {
                                        (new ArbiterDialogs(getActivity().getApplicationContext(), getActivity().getResources(),
                                                getActivity().getSupportFragmentManager())).showBoundaryDialog(cordova, image.getName(), image.getPath());
                                    }


                                    dialog.dismiss();
                                }
                            });

                    // create dialog
                    builder.create().show();


                    // add image data
                    SharedPreferences addData = this.getActivity().getSharedPreferences(sfName, 0);
                    SharedPreferences.Editor editor = addData.edit();
                    editor.putString("name"+addData.getInt("size",0), image.getName()); // 입력
                    editor.putString("path"+addData.getInt("size",0), image.getPath()); // 입력
                    editor.putInt("size", addData.getInt("size",0) + 1);

                    editor.commit();


                    for(int i=0; i<addData.getInt("size",0); i++)
                    {
                        if(!(addData.getString("name"+i,"").equals("")))
                        {
                            Image images = new Image();
                            images.setName(addData.getString("name"+i,""));
                            images.setPath(addData.getString("path"+i,""));

                            imgData.add(images);
                        }
                    }

                    imageList.setData(imgData);




                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public Image getImageNameToUri(Uri data, Context context)
    {
        Image image = new Image();
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        image.setName(imgName);
        image.setPath(imgPath);

        return image;
    }

    public static void deleteImageObject(Image image)
    {
        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {

                String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.deleteImage(\""+image.getPath()+"\");'))";
                cordova.loadUrl(url);

            }
        });

    }

}
