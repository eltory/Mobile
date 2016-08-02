package com.lmn.Arbiter_Android.ListAdapters;

import java.util.ArrayList;

import com.lmn.Arbiter_Android.ArbiterProject;
import com.lmn.Arbiter_Android.BaseClasses.Image;
import com.lmn.Arbiter_Android.Dialog.Dialogs.ImagesDialog;
import com.lmn.Arbiter_Android.R;
import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.DatabaseHelpers.CommandExecutor.CommandExecutor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ImageList extends CustomList<ArrayList<Image>, Image> {

    private LayoutInflater inflater;
    private int itemLayout;
    private Activity activity;
    private Context context;
    private ArbiterProject arbiterProject;
    private HasThreadPool hasThreadPool;
    private String sfName = "imgData";
    private ArrayList<Image> imgList = new ArrayList<Image>();


    public ImageList(ViewGroup viewGroup, Activity activity, int itemLayout, HasThreadPool hasThreadPool){
        super(viewGroup);

        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.inflater =	LayoutInflater.from(this.context);
        this.itemLayout = itemLayout;
        this.arbiterProject = ArbiterProject.getArbiterProject();
        this.hasThreadPool = hasThreadPool;
    }

    @Override
    public void setData(ArrayList<Image> image){
        super.setData(image);
    }

    @Override
    public int getCount() {
        return getData().size();
    }

    @Override
    public Image getItem(int index) {
        return getData().get(index);
    }

    @Override
    public View getView(final int position) {

        View view = inflater.inflate(itemLayout, null);

        final Image image = getItem(position);

        if(image != null){

            TextView imageNameView = (TextView) view.findViewById(R.id.ImageName);
            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteImage);


            if(imageNameView != null){
                imageNameView.setText(image.getName());
            }


            if(deleteButton != null){
                deleteButton.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Log.d("imageName : ", image.getName());
                        Log.d("imagePath : ", image.getPath());

                        confirmDeleteImage(new Image(image), position);
                    }
                });
            }
        }

        return view;
    }


    private void confirmDeleteImage(final Image image, int position){

        String loading = activity.getResources().getString(R.string.loading);
        String pleaseWait = activity.getResources().getString(R.string.please_wait);

        final ProgressDialog progressDialog = ProgressDialog.show(activity, loading, pleaseWait, true);

        hasThreadPool.getThreadPool().execute(new Runnable(){
            @Override
            public void run(){

                activity.runOnUiThread(new Runnable(){

                    @Override
                    public void run(){

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setTitle(R.string.warning);


                        builder.setMessage(R.string.confirm_delete_image);


                        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteImage(image, position);
                            }
                        });

                        builder.setNegativeButton(android.R.string.cancel, null);

                        builder.create().show();

                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    private void deleteImage(final Image selectedImage, int position){

        final String projectName = arbiterProject.getOpenProject(activity);

        String title = activity.getResources().getString(R.string.loading);
        String message = activity.getResources().getString(R.string.please_wait);

        final ProgressDialog progressDialog = ProgressDialog.show(activity, title, message, true);

        CommandExecutor.runProcess(new Runnable(){
            @Override
            public void run() {

                imgList.clear();

                SharedPreferences removeData = activity.getSharedPreferences(sfName, 0);
                SharedPreferences.Editor editor = removeData.edit();

                editor.remove("name"+position);

                editor.commit();

                for(int i=0; i<removeData.getInt("size",0); i++)
                {
                    if(!(removeData.getString("name"+i,"").equals("")))
                    {
                        Image image = new Image();
                        image.setName(removeData.getString("name"+i,"" ));
                        image.setPath(removeData.getString("path"+i,"" ));
                        image.setRight(removeData.getFloat("right"+i,0 ));
                        image.setLeft(removeData.getFloat("left"+i,0 ));
                        image.setTop(removeData.getFloat("top"+i,0 ));
                        image.setBottom(removeData.getFloat("bottom"+i,0 ));
                        imgList.add(image);
                    }
                }

                editor.clear();
                editor.commit();

                for(int i=0; i<imgList.size(); i++)
                {
                    editor.putString("name"+i, imgList.get(i).getName());
                    editor.putString("path"+i, imgList.get(i).getPath());

                    editor.putFloat("left"+i, imgList.get(i).getLeft());
                    editor.putFloat("right"+i, imgList.get(i).getRight());
                    editor.putFloat("top"+i, imgList.get(i).getTop());
                    editor.putFloat("bottom"+i, imgList.get(i).getBottom());
                }

                editor.putInt("size",imgList.size());

                editor.commit();

                activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run(){

                        progressDialog.dismiss();
                        setData(imgList);
                        onDataUpdated();

                    }
                });
            }
        });

        ImagesDialog.deleteImageObject(selectedImage);
    }


    public void setItemLayout(int itemLayout){
        this.itemLayout = itemLayout;
    }

}
