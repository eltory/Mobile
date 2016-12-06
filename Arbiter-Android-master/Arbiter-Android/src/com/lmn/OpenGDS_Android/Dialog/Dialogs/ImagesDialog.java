package com.lmn.OpenGDS_Android.Dialog.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.BaseClasses.Image;
import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;
import com.lmn.OpenGDS_Android.ListAdapters.ImageList;
import com.lmn.OpenGDS_Android.Map.Map_Expansion;

import org.apache.cordova.CordovaWebView;
import java.util.ArrayList;


public class ImagesDialog extends ArbiterDialogFragment{

    private ImageList imageList;
    private final int REQ_CODE_SELECT_IMAGE = 100;
    private ImageButton addImagesBtn;
    private HasThreadPool hasThreadPool;
    private static CordovaWebView cordova;
    private String sfName = "imgData";
    private Image imageOption;
    private String[] items;
    private Resources res;
    private ArrayList<Image> imgData = new ArrayList<Image>();

    public static ImagesDialog newInstance(String title, String ok,
                                           String cancel, int layout, HasThreadPool hasThreadPool, CordovaWebView cordovaWebView, Resources res, Image imageOption){
        ImagesDialog frag = new ImagesDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setLayout(layout);
        frag.hasThreadPool = hasThreadPool;
        frag.res = res;
        frag.cordova = cordovaWebView;
        frag.imageOption = imageOption;
        frag.items = new String[imageOption.getImageBuildOptions().size()];

        for(int i=0; i<imageOption.getImageBuildOptions().size(); i++)
        {
            if(imageOption.getImageBuildOptions().get(i).equalsIgnoreCase("DRAW"))
                frag.items[i] = res.getString(R.string.draw_image);

            else if(imageOption.getImageBuildOptions().get(i).equalsIgnoreCase("BOUNDARY"))
                frag.items[i] = res.getString(R.string.set_boundary);

            else if(imageOption.getImageBuildOptions().get(i).equalsIgnoreCase("AOI"))
                frag.items[i] = res.getString(R.string.input_image_AOI);
        }

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

    public void populateListView(View view){
        populateOverlayList(view);
    }

    //Reload image data
    private void populateOverlayList(View view){

        imgData.clear();

        LinearLayout imageList = (LinearLayout) view.findViewById(R.id.imagesList);

        this.imageList = new ImageList(imageList, this.getActivity(), R.layout.images_list_item, hasThreadPool, cordova, imageOption);

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

        //Access gallery
        this.addImagesBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                                                    int position) {

                                    //Draw Image using BBox
                                    if(items[position].equalsIgnoreCase(res.getString(R.string.draw_image))) {
                                        Map_Expansion.getMap().addDrawImage(cordova, image.getName(), image.getPath());
                                    }

                                    //Set boundary value
                                    else if(items[position].equalsIgnoreCase(res.getString(R.string.set_boundary)))
                                    {
                                        (new ArbiterDialogs_Expansion(getActivity().getApplicationContext(), getActivity().getResources(),
                                                getActivity().getSupportFragmentManager())).showBoundaryImageDialog(cordova, image.getName(), image.getPath());
                                    }

                                    //Input image in AOI
                                    else if(items[position].equalsIgnoreCase(res.getString(R.string.input_image_AOI)))
                                    {
                                        (new ArbiterDialogs_Expansion(getActivity().getApplicationContext(), getActivity().getResources(),
                                                getActivity().getSupportFragmentManager())).showAOIBoundaryImageDialog(cordova, image.getName(), image.getPath());
                                    }
                                    dialog.dismiss();
                                    getDialog().dismiss();
                                }
                            });

                    // create dialog
                    builder.setCancelable(false);
                    builder.create().show();


                    // add image data in sharedPreference
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

    //Delete image object
    public static void deleteImageObject(Image image)
    {
        Map_Expansion.getMap().deleteImage(cordova, image.getPath());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
