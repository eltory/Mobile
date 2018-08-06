package com.lmn.OpenGDS_Android.ListAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.ArbiterProject;
import com.lmn.Arbiter_Android.DatabaseHelpers.CommandExecutor.CommandExecutor;
import com.lmn.Arbiter_Android.ListAdapters.CustomList;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.Dialog.Dialogs.Image.ImagesDialog;
import com.lmn.OpenGDS_Android.BaseClasses.Image;
import com.lmn.OpenGDS_Android.Map.Map_Expansion;

import org.apache.cordova.CordovaWebView;

import java.util.ArrayList;

/**
 * 이미지 레이어 리스트 어댑터
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class ImageList extends CustomList<ArrayList<Image>, Image> {

    private LayoutInflater inflater;
    private int itemLayout;
    private Activity activity;
    private Context context;
    private ArbiterProject arbiterProject;
    private HasThreadPool hasThreadPool;
    private String sfName = "imgData";
    private ArrayList<Image> imgList = new ArrayList<Image>();
    private String[] items = new String[1];
    private SeekBar opacityVolumn;
    private TextView showCurrentOpacity;
    private double opacityValue = 0;
    private CordovaWebView cordovaWebView;
    private Image imageOption;

    /**
     * @param viewGroup,activity,itemLayout,hasThreadPool,cordovaWebView,imageOption ViewGroup,Activity,int,HasThreadPool,CordovaWebView,Image
     * @author JiJungKeun
     */
    public ImageList(ViewGroup viewGroup, Activity activity, int itemLayout, HasThreadPool hasThreadPool, CordovaWebView cordovaWebView, Image imageOption) {
        super(viewGroup);

        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.inflater = LayoutInflater.from(this.context);
        this.itemLayout = itemLayout;
        this.arbiterProject = ArbiterProject.getArbiterProject();
        this.hasThreadPool = hasThreadPool;
        this.cordovaWebView = cordovaWebView;
        this.imageOption = imageOption;

        items[0] = activity.getResources().getString(R.string.image_option_opacity);
    }

    @Override
    public void setData(ArrayList<Image> image) {
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

        if (image != null) {

            RelativeLayout imageItemContainer = (RelativeLayout) view.findViewById(R.id.ImageItemContainer);
            TextView imageNameView = (TextView) view.findViewById(R.id.ImageName);
            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteImage);

            if (imageNameView != null) {
                imageNameView.setText(image.getName());

                if (imageOption.getImageOpacity() == true) {
                    imageItemContainer.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setImageOption(new Image(image), position);
                        }
                    });
                }
            }


            if (deleteButton != null) {
                deleteButton.setOnClickListener(new OnClickListener() {

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

    /**
     * 이미지 레이어 투명도 조절
     *
     * @param image,position Image,int
     * @author JiJungKeun
     */
    private void setImageOption(final Image image, int position) {

        String loading = activity.getResources().getString(R.string.loading);
        String pleaseWait = activity.getResources().getString(R.string.please_wait);

        final ProgressDialog progressDialog = ProgressDialog.show(activity, loading, pleaseWait, true);

        hasThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setTitle(R.string.image_option_setting);

                        builder.setSingleChoiceItems(items, -1,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        //Opacity
                                        if (id == 0) {
                                            View innerView = activity.getLayoutInflater().inflate(R.layout.image_opacity_seek_bar, null);

                                            AlertDialog.Builder opacityBuilder = new AlertDialog.Builder(activity);
                                            opacityBuilder.setView(innerView);
                                            opacityVolumn = (SeekBar) innerView.findViewById(R.id.SeekBar_Volumn);
                                            showCurrentOpacity = (TextView) innerView.findViewById(R.id.Show_CurrentOpacityVolumn);
                                            setSeekbar();

                                            opacityBuilder.setTitle(R.string.image_option_opacity);
                                            opacityBuilder.setIcon(R.drawable.icon);
                                            opacityBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    opacityValue = opacityValue / 10;

                                                    Map_Expansion.getMap().setImageOpacity(cordovaWebView, image.getPath(), opacityValue);
                                                }
                                            });
                                            opacityBuilder.setCancelable(false);
                                            opacityBuilder.create().show();
                                        }

                                        dialog.dismiss();
                                    }
                                });

                        // create dialog
                        builder.setNegativeButton(android.R.string.cancel, null);
                        builder.setIcon(R.drawable.icon);
                        builder.setCancelable(false);
                        builder.create().show();

                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 투명도 조절 SeekBar 설정
     *
     * @author JiJungKeun
     */
    private void setSeekbar() {
        int maxOpacity = 10;
        int currentOpacity = 0;

        opacityVolumn.setMax(maxOpacity);
        opacityVolumn.setProgress(currentOpacity);
        opacityVolumn.incrementProgressBy(1);

        opacityVolumn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                showCurrentOpacity.setText("" + progress);
                opacityValue = progress;
            }
        });
    }

    /**
     * 이미지 삭제 확인
     *
     * @param image,position Image,int
     * @author JiJungKeun
     */
    private void confirmDeleteImage(final Image image, int position) {

        String loading = activity.getResources().getString(R.string.loading);
        String pleaseWait = activity.getResources().getString(R.string.please_wait);
        final int pos = position;
        final ProgressDialog progressDialog = ProgressDialog.show(activity, loading, pleaseWait, true);

        hasThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setTitle(R.string.warning)
                                .setMessage(R.string.confirm_delete_image)
                                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteImage(image, pos);
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, null)
                                .create().show();

                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 이미지 삭제(SharedPreference 데이터 삭제)
     *
     * @param selectedImage,position Image,int
     * @author JiJungKeun
     */
    private void deleteImage(final Image selectedImage, int position) {

        final String projectName = arbiterProject.getOpenProject(activity);
        final int pos = position;
        String title = activity.getResources().getString(R.string.loading);
        String message = activity.getResources().getString(R.string.please_wait);

        final ProgressDialog progressDialog = ProgressDialog.show(activity, title, message, true);

        CommandExecutor.runProcess(new Runnable() {
            @Override
            public void run() {

                imgList.clear();

                SharedPreferences removeData = activity.getSharedPreferences(sfName, 0);
                SharedPreferences.Editor editor = removeData.edit();
                editor.remove("name" + pos);
                editor.commit();

                for (int i = 0; i < removeData.getInt("size", 0); i++) {
                    if (!(removeData.getString("name" + i, "").equals(""))) {
                        Image image = new Image();
                        image.setName(removeData.getString("name" + i, ""));
                        image.setPath(removeData.getString("path" + i, ""));
                        image.setRight(removeData.getFloat("right" + i, 0));
                        image.setLeft(removeData.getFloat("left" + i, 0));
                        image.setTop(removeData.getFloat("top" + i, 0));
                        image.setBottom(removeData.getFloat("bottom" + i, 0));
                        imgList.add(image);
                    }
                }

                editor.clear();
                editor.commit();

                for (int i = 0; i < imgList.size(); i++) {
                    editor.putString("name" + i, imgList.get(i).getName())
                            .putString("path" + i, imgList.get(i).getPath())
                            .putFloat("left" + i, imgList.get(i).getLeft())
                            .putFloat("right" + i, imgList.get(i).getRight())
                            .putFloat("top" + i, imgList.get(i).getTop())
                            .putFloat("bottom" + i, imgList.get(i).getBottom());
                }

                editor.putInt("size", imgList.size());
                editor.commit();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        setData(imgList);
                        onDataUpdated();
                    }
                });
            }
        });
        ImagesDialog.deleteImageObject(selectedImage);
    }

    public void setItemLayout(int itemLayout) {
        this.itemLayout = itemLayout;
    }
}
