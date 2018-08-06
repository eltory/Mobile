package com.lmn.OpenGDS_Android.Dialog.Dialogs.Image;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.ArbiterProject;
import com.lmn.Arbiter_Android.DatabaseHelpers.ProjectDatabaseHelper;
import com.lmn.Arbiter_Android.DatabaseHelpers.TableHelpers.PreferencesHelper;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.ProjectStructure.ProjectStructure;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.BaseClasses.Image;
import com.lmn.OpenGDS_Android.Dialog.ArbiterDialogs_Expansion;
import com.lmn.OpenGDS_Android.ListAdapters.ImageList;
import com.lmn.OpenGDS_Android.Map.Map_Expansion;

import org.apache.cordova.CordovaWebView;

import java.util.ArrayList;

/**
 * 이미지 다이얼로그(갤러리 연동 및 리스트 관리)
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class ImagesDialog extends ArbiterDialogFragment {

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

    /**
     * @param title,ok,cancel,layout,hasThreadPool,cordovaWebView,res,imageOption String,String,String,int,HasThreadPool,CordovaWebView,Resources,Image
     * @return ImagesDialog
     * @author JiJungKeun
     */
    public static ImagesDialog newInstance(String title, String ok,
                                           String cancel, int layout, HasThreadPool hasThreadPool, CordovaWebView cordovaWebView, Resources res, Image imageOption) {
        ImagesDialog frag = new ImagesDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setLayout(layout);
        frag.hasThreadPool = hasThreadPool;
        frag.res = res;
        frag.cordova = cordovaWebView;
        frag.imageOption = imageOption;
        frag.items = new String[imageOption.getImageBuildOptions().size()];

        for (int i = 0; i < imageOption.getImageBuildOptions().size(); i++) {
            if (imageOption.getImageBuildOptions().get(i).equalsIgnoreCase("DRAW"))
                frag.items[i] = res.getString(R.string.draw_image);

            else if (imageOption.getImageBuildOptions().get(i).equalsIgnoreCase("BOUNDARY"))
                frag.items[i] = res.getString(R.string.set_boundary);

            else if (imageOption.getImageBuildOptions().get(i).equalsIgnoreCase("AOI"))
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
        if (view != null) {
            registerListeners(view);
        }
    }

    public void populateListView(View view) {
        populateOverlayList(view);
    }

    /**
     * 이미지 리스트 생성 시, 기존에 추가된 데이터 리로드
     *
     * @param view View
     * @author JiJungKeun
     */
    private void populateOverlayList(View view) {

        imgData.clear();

        LinearLayout imageList = (LinearLayout) view.findViewById(R.id.imagesList);

        this.imageList = new ImageList(imageList, this.getActivity(), R.layout.images_list_item, hasThreadPool, cordova, imageOption);

        SharedPreferences reloadImageData = this.getActivity().getSharedPreferences(sfName, 0);

        for (int i = 0; i < reloadImageData.getInt("size", 0); i++) {
            //check if image is built on map using left boundary
            if (reloadImageData.getFloat("left" + i, -1) != -1) {
                Image image = new Image();
                image.setName(reloadImageData.getString("name" + i, ""));
                image.setPath(reloadImageData.getString("path" + i, ""));

                imgData.add(image);
            }
        }

        this.imageList.setData(imgData); //setting previous stored image data
    }


    public void registerListeners(View view) {

        this.addImagesBtn = (ImageButton) view.findViewById(R.id.add_images_button);

        populateListView(view);

        //Access gallery
        this.addImagesBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

    }


    /**
     * 이미지 선택 시, 이미지 빌드 옵션 설정
     *
     * @param requestCode,resultCode,data int,int,Intent
     * @author JiJungKeun
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    imgData.clear();

                    final Image image = getImageNameToUri(data.getData(), this.getActivity());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle(R.string.action_image_option)
                            .setSingleChoiceItems(items, -1,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int position) {

                                            //Draw Image using BBox
                                            if (items[position].equalsIgnoreCase(res.getString(R.string.draw_image))) {
                                                Map_Expansion.getMap().addDrawImage(cordova, image.getName(), image.getPath());
                                            }

                                            //Set boundary value
                                            else if (items[position].equalsIgnoreCase(res.getString(R.string.set_boundary))) {
                                                (new ArbiterDialogs_Expansion(getActivity().getApplicationContext(), getActivity().getResources(),
                                                        getActivity().getSupportFragmentManager())).showBoundaryImageDialog(cordova, image.getName(), image.getPath());
                                            }

                                            //Input image in AOI
                                            else if (items[position].equalsIgnoreCase(res.getString(R.string.input_image_AOI))) {
                                                //check if AOI is exist
                                                String result = "";
                                                try {
                                                    String projectName = ArbiterProject.getArbiterProject().getOpenProject(getActivity());
                                                    String path = ProjectStructure.getProjectPath(projectName);
                                                    SQLiteDatabase projectDb = ProjectDatabaseHelper.getHelper(getActivity().getApplicationContext(), path, false).getWritableDatabase();
                                                    result = PreferencesHelper.getHelper().get(projectDb, getActivity().getApplicationContext(), PreferencesHelper.AOI);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                if (result != null && !result.equalsIgnoreCase("")) {
                                                    (new ArbiterDialogs_Expansion(getActivity().getApplicationContext(), getActivity().getResources(),
                                                            getActivity().getSupportFragmentManager())).showAOIBoundaryImageDialog(cordova, image.getName(), image.getPath());
                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                                    builder.setTitle("AOI image fail")
                                                            .setMessage("AOI image is not operated properly.")
                                                            .setIcon(R.drawable.icon)
                                                            .setPositiveButton(android.R.string.ok, null)
                                                            .create().show();
                                                }
                                            }
                                            dialog.dismiss();
                                            getDialog().dismiss();
                                        }
                                    })
                            .setCancelable(false)
                            .create().show();


                    // add image data in sharedPreference
                    SharedPreferences addData = this.getActivity().getSharedPreferences(sfName, 0);
                    SharedPreferences.Editor editor = addData.edit();
                    editor.putString("name" + addData.getInt("size", 0), image.getName()); // 입력
                    editor.putString("path" + addData.getInt("size", 0), image.getPath()); // 입력
                    editor.putInt("size", addData.getInt("size", 0) + 1);

                    editor.commit();

                    for (int i = 0; i < addData.getInt("size", 0); i++) {
                        if (!(addData.getString("name" + i, "").equals(""))) {
                            Image images = new Image();
                            images.setName(addData.getString("name" + i, ""));
                            images.setPath(addData.getString("path" + i, ""));

                            imgData.add(images);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 이미지 선택 시, 이미지 데이터 저장 및 반환
     *
     * @param data,context Uri,Context
     * @author JiJungKeun
     */
    public Image getImageNameToUri(Uri data, Context context) {
        Image image = new Image();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        image.setName(imgName);
        image.setPath(imgPath);

        return image;
    }

    /**
     * 지도 상에서 이미지 객체 삭제
     *
     * @param image Image
     * @author JiJungKeun
     */
    public static void deleteImageObject(Image image) {
        Map_Expansion.getMap().deleteImage(cordova, image.getPath());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
