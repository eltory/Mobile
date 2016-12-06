package com.lmn.OpenGDS_Android.Dialog;

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

import com.lmn.OpenGDS_Android.BaseClasses.Image;
import com.lmn.OpenGDS_Android.BaseClasses.Validation;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.AOIBoundaryImageDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ImagesDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.BoundaryImageDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.AddressSearchDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.CoordinateSearchDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.AddValidateLayersDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDetailOptionSettingDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationErrorReportDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationOptionDialog;
import com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationOptionSettingDialog;

import org.apache.cordova.CordovaWebView;

import java.util.ArrayList;

public class ArbiterDialogs_Expansion {
	private Resources resources;
	private FragmentManager fragManager;

	public ArbiterDialogs_Expansion(Context context, Resources resources, FragmentManager fragManager){
		this.setResources(resources);
		this.setFragManager(fragManager);
	}

	public void setFragManager(FragmentManager fragManager){
		this.fragManager = fragManager;
	}

	public void setResources(Resources resources){
		this.resources = resources;
	}

	public void showCoordinateSearchDialog(final CordovaWebView webview){
		String title = resources.getString(R.string.action_coordinate);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.coordinates_search_dialog;

		DialogFragment dialog;

		dialog = CoordinateSearchDialog.newInstance(title, ok, cancel, layout, webview);

		dialog.show(fragManager, "OpenGDS_CoordinateSearchDialog");
	}

	public void showAddressSearchDialog(final CordovaWebView webview, HasThreadPool hasThreadPool){
		String title = resources.getString(R.string.action_address);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.address_search_dialog;

		DialogFragment dialog;

		dialog = AddressSearchDialog.newInstance(title, ok, cancel, layout, webview, hasThreadPool);

		dialog.show(fragManager, "OpenGDS_AddressSearchDialog");
	}

	public void showImagesDialog(HasThreadPool hasThreadPool, final CordovaWebView webview, Image imageOption){
		String title = resources.getString(R.string.action_image);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.images_dialog;

		DialogFragment dialog;

		dialog = ImagesDialog.newInstance(title, ok, cancel, layout, hasThreadPool, webview, resources, imageOption);
		dialog.show(fragManager, "OpenGDS_ImagesDialog");
	}

	public void showBoundaryImageDialog(final CordovaWebView webview, String name, String path){
		String title = resources.getString(R.string.action_boundary);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.boundary_image_dialog;

		DialogFragment dialog;

		dialog = BoundaryImageDialog.newInstance(title, ok, cancel, layout, webview, name, path);

		dialog.show(fragManager, "OpenGDS_BoundaryDialog");
	}

	public void showAOIBoundaryImageDialog(final CordovaWebView webview, String name, String path){
		String title = resources.getString(R.string.action_AOI_image);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.aoi_image_dialog;

		DialogFragment dialog;

		dialog = AOIBoundaryImageDialog.newInstance(title, ok, cancel, layout, webview, name, path);

		dialog.show(fragManager, "OpenGDS_AOIBoundaryDialog");
	}

	//add validation layers dialog
	public void showAddValidateLayersDialog(HasThreadPool hasThreadPool, ConnectivityListener connectivityListener, final CordovaWebView webview){
		String title = resources.getString(R.string.action_validationLayerList);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.add_validate_layers_dialog;

		DialogFragment dialog = AddValidateLayersDialog.newInstance(title, ok, cancel, layout, connectivityListener, hasThreadPool, webview);

		dialog.show(fragManager, "OpenGDS_AddValidateLayersDialog");
	}

	//validation option dialog
	public void showValidationOptionDialog(HasThreadPool hasThreadPool, ArrayList<Validation> checkedLayers, final CordovaWebView webview){
		String title = resources.getString(R.string.action_validationOption);
		String ok = resources.getString(R.string.start_validation_button);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.validation_option_dialog;

		DialogFragment dialog = ValidationOptionDialog.newInstance(title, ok, cancel, layout, hasThreadPool, checkedLayers, webview);

		dialog.show(fragManager, "OpenGDS_ValidationOptionDialog");
	}

	//validation option setting dialog
	public void showValidationOptionSettingDialog(HasThreadPool hasThreadPool, Validation selectedLayer){
		String title = resources.getString(R.string.action_optionSetting);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.validation_option_setting_dialog;

		DialogFragment dialog = ValidationOptionSettingDialog.newInstance(title, ok, cancel, layout, hasThreadPool, selectedLayer);

		dialog.show(fragManager, "OpenGDS_ValidationOptionSettingDialog");
	}

	//validation detail option setting dialog
	public void showValidationDetailOptionSettingDialog(HasThreadPool hasThreadPool, String optionName, Validation selectedLayer, RelativeLayout selectedOptionNameLayout){
		String title = resources.getString(R.string.action_detailOptionSetting);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.validation_detail_option_setting_dialog;

		DialogFragment dialog = ValidationDetailOptionSettingDialog.newInstance(title, ok, cancel, layout, hasThreadPool, optionName, selectedLayer, selectedOptionNameLayout);

		dialog.show(fragManager, "OpenGDS_ValidationDetailOptionSettingDialog");
	}

	public void showValidationErrorReportDialog(MapActivity mapActivity){
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

}
