package com.lmn.OpenGDS_Android.Dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.RelativeLayout;

import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.Activities.MapActivity;
import com.lmn.Arbiter_Android.ConnectivityListeners.ConnectivityListener;
import com.lmn.Arbiter_Android.R;

import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Classification;
import com.lmn.OpenGDS_Android.BaseClasses.Image;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Layer;
import com.lmn.OpenGDS_Android.BaseClasses.Validation;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.DetailOptions;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.Image.AOIBoundaryImageDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.AddClassificationDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.ClassificationDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.Image.ImagesDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.Image.BoundaryImageDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.AddressSearchDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.CoordinateSearchDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.AddValidateLayersDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.NewValidationDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.NewValidationLayerDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.NewValidationOptionsDetail1Dialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.NewValidationOptionsDetail2Dialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.NewValidationOptionsDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.ValidationDetailOptionSettingDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.ValidationErrorReportDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.ValidationOptionDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog.ValidationOptionSettingDialog;

import org.apache.cordova.CordovaWebView;

import java.util.ArrayList;

/**
 * 다이얼로그 인터페이스 클래스
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class ArbiterDialogs_Expansion {
    private Resources resources;
    private FragmentManager fragManager;
    private Activity activity;
    private Context context;

    public ArbiterDialogs_Expansion(Context context, Resources resources, FragmentManager fragManager) {
        this.setResources(resources);
        this.setFragManager(fragManager);
        this.context = context;
    }
    public ArbiterDialogs_Expansion(Activity context, Resources resources, FragmentManager fragManager) {
        this.setResources(resources);
        this.setFragManager(fragManager);
        this.activity = context;
    }

    public void setFragManager(FragmentManager fragManager) {
        this.fragManager = fragManager;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public void showCoordinateSearchDialog(final CordovaWebView webview) {
        String title = resources.getString(R.string.action_coordinate);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.coordinates_search_dialog;

        DialogFragment dialog;

        dialog = CoordinateSearchDialog.newInstance(title, ok, cancel, layout, webview);

        dialog.show(fragManager, "OpenGDS_CoordinateSearchDialog");
    }

    public void showAddressSearchDialog(final CordovaWebView webview, HasThreadPool hasThreadPool) {
        String title = resources.getString(R.string.action_address);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.address_search_dialog;

        DialogFragment dialog;

        dialog = AddressSearchDialog.newInstance(title, ok, cancel, layout, webview, hasThreadPool);

        dialog.show(fragManager, "OpenGDS_AddressSearchDialog");
    }

    public void showImagesDialog(HasThreadPool hasThreadPool, final CordovaWebView webview, Image imageOption) {
        String title = resources.getString(R.string.action_image);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.images_dialog;

        DialogFragment dialog;

        dialog = ImagesDialog.newInstance(title, ok, cancel, layout, hasThreadPool, webview, resources, imageOption);
        dialog.show(fragManager, "OpenGDS_ImagesDialog");
    }

    public void showBoundaryImageDialog(final CordovaWebView webview, String name, String path) {
        String title = resources.getString(R.string.action_boundary);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.boundary_image_dialog;

        DialogFragment dialog;

        dialog = BoundaryImageDialog.newInstance(title, ok, cancel, layout, webview, name, path);

        dialog.show(fragManager, "OpenGDS_BoundaryDialog");
    }

    public void showAOIBoundaryImageDialog(final CordovaWebView webview, String name, String path) {
        String title = resources.getString(R.string.action_AOI_image);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.aoi_image_dialog;

        DialogFragment dialog;

        dialog = AOIBoundaryImageDialog.newInstance(title, ok, cancel, layout, webview, name, path);

        dialog.show(fragManager, "OpenGDS_AOIBoundaryDialog");
    }

    //add validation layers dialog
    public void showAddValidateLayersDialog(HasThreadPool hasThreadPool, ConnectivityListener connectivityListener, final CordovaWebView webview) {
        String title = resources.getString(R.string.action_validationLayerList);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.add_validate_layers_dialog;

        DialogFragment dialog = AddValidateLayersDialog.newInstance(title, ok, cancel, layout, connectivityListener, hasThreadPool, webview);

        dialog.show(fragManager, "OpenGDS_AddValidateLayersDialog");
    }

    //validation option dialog
    public void showValidationOptionDialog(HasThreadPool hasThreadPool, ArrayList<Validation> checkedLayers, final CordovaWebView webview) {
        String title = resources.getString(R.string.action_validationOption);
        String ok = resources.getString(R.string.start_validation_button);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.validation_option_dialog;

        DialogFragment dialog = ValidationOptionDialog.newInstance(title, ok, cancel, layout, hasThreadPool, checkedLayers, webview);

        dialog.show(fragManager, "OpenGDS_ValidationOptionDialog");
    }

    //validation option setting dialog
    public void showValidationOptionSettingDialog(HasThreadPool hasThreadPool, Validation selectedLayer) {
        String title = resources.getString(R.string.action_optionSetting);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.validation_option_setting_dialog;

        DialogFragment dialog = ValidationOptionSettingDialog.newInstance(title, ok, cancel, layout, hasThreadPool, selectedLayer);

        dialog.show(fragManager, "OpenGDS_ValidationOptionSettingDialog");
    }

    //validation detail option setting dialog
    public void showValidationDetailOptionSettingDialog(HasThreadPool hasThreadPool, String optionName, Validation selectedLayer, RelativeLayout selectedOptionNameLayout) {
        String title = resources.getString(R.string.action_detailOptionSetting);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.validation_detail_option_setting_dialog;

        DialogFragment dialog = ValidationDetailOptionSettingDialog.newInstance(title, ok, cancel, layout, hasThreadPool, optionName, selectedLayer, selectedOptionNameLayout);

        dialog.show(fragManager, "OpenGDS_ValidationDetailOptionSettingDialog");
    }

    public void showValidationErrorReportDialog(MapActivity mapActivity) {
        String title = resources.getString(R.string.report);
        String ok = resources.getString(android.R.string.ok);
        int layout = R.layout.validation_report_table;

        //Create Progress dialog
        ProgressDialog reportProgressDialog = new ProgressDialog(mapActivity);
        reportProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        reportProgressDialog.setIcon(mapActivity.getResources().getDrawable(R.drawable.icon));
        reportProgressDialog.setTitle(R.string.loading);
        reportProgressDialog.setMessage(mapActivity.getString(R.string.action_report_progress));
        reportProgressDialog.setCanceledOnTouchOutside(false);
        reportProgressDialog.show();

        DialogFragment dialog = ValidationErrorReportDialog.newInstance(title, ok, layout, reportProgressDialog);
        dialog.show(fragManager, "validationReportDialog");
    }

    //add classification dialog
    public void showClassificationDialog(HasThreadPool hasThreadPool, final CordovaWebView webview) {
        String title = resources.getString(R.string.action_validationLayerList);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.classification_dialog;

        //TODO : 분류추가 다이얼로그로 바꾸기
        DialogFragment dialog = ClassificationDialog.newInstance(title, ok, cancel, layout, hasThreadPool, webview);

        dialog.show(fragManager, "OpenGDS_AddClassificationDialog");
    }

    public void showAddClassificationDialog(HasThreadPool hasThreadPool, int position, Layer layer, String geoType) {
        String title = resources.getString(R.string.action_validationLayerList);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.add_layer_dialog;

        //TODO : 분류추가 다이얼로그로 바꾸기
        DialogFragment dialog = AddClassificationDialog.newInstance(title, ok, cancel, layout, position, layer, geoType, activity);

        dialog.show(fragManager, "OpenGDS_AddClassificationDialog");
    }

    // New validation dialog
    public void showNewValidationDialog(HasThreadPool hasThreadPool, ArrayList<Classification> checkedLayers, final CordovaWebView webview) {
        String title = resources.getString(R.string.action_validationOption);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.validation_option_dialog;

        DialogFragment dialog = NewValidationDialog.newInstance(title, ok, cancel, layout, hasThreadPool, checkedLayers, webview);

        dialog.show(fragManager, "OpenGDS_ValidationOptionDialog");
    }

    // New validation option dialog
    public void showNewValidationOptionDialog(HasThreadPool hasThreadPool, Classification checkedLayers, final CordovaWebView webview) {
        String title = resources.getString(R.string.action_validationOption);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.validation_option_dialog;

        DialogFragment dialog = NewValidationOptionsDialog.newInstance(title, ok, cancel, layout, hasThreadPool, checkedLayers);

        dialog.show(fragManager, "OpenGDS_ValidationOptionDialog");
    }

    // New validation option detail dialog
    public void showNewValidationOptionDetailDialog(HasThreadPool hasThreadPool, String checkedLayers) {
        String title = resources.getString(R.string.action_validationOption);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.validation_detail_option_setting_dialog;

        DialogFragment dialog = NewValidationOptionsDetail1Dialog.newInstance(title, ok, cancel, layout, hasThreadPool, checkedLayers);

        dialog.show(fragManager, "OpenGDS_ValidationOptionDialog");
    }

    public void showDetailOption1Dialog(HasThreadPool hasThreadPool, String checkedLayers) {
        String title = resources.getString(R.string.action_validationOption);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.detail_option_1;

        DialogFragment dialog = NewValidationOptionsDetail1Dialog.newInstance(title, ok, cancel, layout, hasThreadPool, checkedLayers);

        dialog.show(fragManager, "OpenGDS_ValidationOptionDialog");
    }

    public void showDetailOption2Dialog(HasThreadPool hasThreadPool, String checkedLayers) {
        String title = resources.getString(R.string.action_validationOption);
        String ok = resources.getString(android.R.string.ok);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.detail_option_2;

        DialogFragment dialog = NewValidationOptionsDetail2Dialog.newInstance(title, ok, cancel, layout, hasThreadPool, checkedLayers);

        dialog.show(fragManager, "OpenGDS_ValidationOptionDialog");
    }

    public void showLayerSelectionDialog(HasThreadPool hasThreadPool, ArrayList<Classification> checkedLayers, DetailOptions options, final CordovaWebView webview) {
        String title = resources.getString(R.string.action_validationOption);
        String ok = resources.getString(R.string.start_validation_button);
        String cancel = resources.getString(android.R.string.cancel);
        int layout = R.layout.layer_selection_dialog;

        DialogFragment dialog = NewValidationLayerDialog.newInstance(title, ok, cancel, layout, hasThreadPool, checkedLayers, options,webview);

        dialog.show(fragManager, "OpenGDS_ValidationOptionDialog");
    }
}
