package com.lmn.Arbiter_Android.Dialog;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.RelativeLayout;

import com.lmn.Arbiter_Android.Activities.MapActivity;
import com.lmn.Arbiter_Android.BaseClasses.Validation;
import com.lmn.Arbiter_Android.Dialog.Dialogs.AddValidateLayersDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.ValidationDetailOptionSettingDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.ValidationOptionDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.ValidationOptionSettingDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.ValidationErrorReportDialog;
import com.lmn.Arbiter_Android.R;
import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.BaseClasses.Layer;
import com.lmn.Arbiter_Android.BaseClasses.Server;
import com.lmn.Arbiter_Android.BaseClasses.Tileset;
import com.lmn.Arbiter_Android.ConnectivityListeners.ConnectivityListener;
import com.lmn.Arbiter_Android.Dialog.Dialogs.AddLayersDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.AddServerDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.AddTilesetDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.ChooseAOIDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.GoOfflineDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.LayersDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.ProjectNameDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.ServersDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.TilesetsDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.TilesetInfoDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.WelcomeDialog;
import com.lmn.Arbiter_Android.Dialog.Dialogs.DownloadingTilesetsDialog;

import org.apache.cordova.CordovaWebView;

public class ArbiterDialogs {
	private Resources resources;
	private FragmentManager fragManager;

	public ArbiterDialogs(Context context, Resources resources, FragmentManager fragManager){
		this.setResources(resources);
		this.setFragManager(fragManager);
	}

	public void setFragManager(FragmentManager fragManager){
		this.fragManager = fragManager;
	}

	public void setResources(Resources resources){
		this.resources = resources;
	}

	public void showWelcomeDialog(){
		String title = resources.getString(R.string.welcome_dialog_title);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.welcome_dialog;

		DialogFragment dialog = WelcomeDialog.newInstance(title, ok, cancel, layout);
		dialog.show(fragManager, "welcomeDialog");
	}

	public void showProjectNameDialog(ConnectivityListener connectivityListener){
		String title = resources.getString(R.string.project_name_dialog_title);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.project_name_dialog;

		DialogFragment dialog = ProjectNameDialog.newInstance(title, ok, cancel, layout, connectivityListener);
		dialog.show(fragManager, "projectNameDialog");
	}

	public void showAddServerDialog(Server server){
		String title;

		if(server != null){
			title = server.getName();
		}else{
			title = resources.getString(R.string.add_server_dialog_title);
		}

		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.add_server_dialog;

		DialogFragment dialog = AddServerDialog.newInstance(title, ok, cancel, layout, server);
		dialog.show(fragManager, "addServerDialog");
		Log.d("showAddServerDialog", "server insert");
	}

	public void showServersDialog(){
		String title = resources.getString(R.string.server_dialog_title);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.servers_dialog;

		DialogFragment dialog = ServersDialog.newInstance(title, ok, cancel, layout);
		dialog.show(fragManager, "serversDialog");
	}

	public void showAddTilesetDialog(boolean newProject, ConnectivityListener connectivityListener){
		String title = resources.getString(R.string.add_tileset_dialog_title);
		String done = resources.getString(R.string.done);
		int layout = R.layout.add_tileset_dialog;

		DialogFragment dialog = AddTilesetDialog.newInstance(title, done, layout, connectivityListener, newProject);
		dialog.show(fragManager, "addTilesetDialog");
	}

	public void showTilesetsDialog(){
		String title = resources.getString(R.string.tileset_dialog_title);
		String done = resources.getString(R.string.done);
		int layout = R.layout.tilesets_dialog;

		DialogFragment dialog = TilesetsDialog.newInstance(title, done, layout, false, null, null);
		dialog.show(fragManager, "tilesetDialog");
	}

	public void showTilesetInfoDialog(Tileset tileset){
		String title = resources.getString(R.string.tileset_info_title);
		String back = resources.getString(android.R.string.ok);
		String stop = resources.getString(R.string.stop);
		int layout = R.layout.tileset_info_dialog;

		DialogFragment dialog = TilesetInfoDialog.newInstance(title, back, stop, layout, tileset);
		dialog.show(fragManager, "tilesetInfoDialog");
	}

	public void showAddLayersDialog(ArrayList<Layer> layersInProject, ConnectivityListener connectivityListener, HasThreadPool hasThreadPool){
		String title = resources.getString(R.string.add_layers_dialog_title);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.add_layers_dialog;

		DialogFragment dialog;

		dialog = AddLayersDialog.newInstance(title, ok, cancel,
				layout, layersInProject, connectivityListener, hasThreadPool);

		dialog.show(fragManager, "addLayersDialog");
	}

	public void showAddLayersDialog(boolean creatingProject, ConnectivityListener connectivityListener){
		String title = resources.getString(R.string.add_layers_dialog_title);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.add_layers_dialog;

		DialogFragment dialog;

		dialog = AddLayersDialog.newInstance(title, ok, cancel,
				layout, creatingProject, connectivityListener);

		dialog.show(fragManager, "addLayersDialog");
	}

	public void showGoOfflineDialog(boolean creatingProject){
		String title = resources.getString(R.string.go_offline_dialog_title);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.add_layers_dialog;

		DialogFragment dialog = GoOfflineDialog.newInstance(title, ok, cancel, layout, creatingProject);
		dialog.show(fragManager, "goOfflineDialog");
	}

	public void showChooseAOIDialog(){
		String title = resources.getString(R.string.choose_aoi_dialog_title);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.choose_aoi_dialog;

		DialogFragment dialog = ChooseAOIDialog.newInstance(title, ok, cancel, layout);
		dialog.show(fragManager, "chooseAOIDialog");
	}

	public DialogFragment showDownloadingTilesetDialog(String tilesetName){
		String title = "Preparing Tileset";
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.downloading_tileset_dialog;

		DialogFragment dialog = DownloadingTilesetsDialog.newInstance(title, cancel, layout, tilesetName);
		dialog.show(fragManager, "downloadingTilesetDialog");

		return dialog;
	}

	public void showLayersDialog(HasThreadPool hasThreadPool){
		String title = resources.getString(R.string.layers_dialog_title);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.layers_dialog;

		DialogFragment dialog = LayersDialog.newInstance(title, ok, cancel, layout, hasThreadPool);
		dialog.show(fragManager, "layersDialog");
	}

	//add validation layers dialog
	public void showAddValidateLayersDialog(HasThreadPool hasThreadPool, ConnectivityListener connectivityListener, final CordovaWebView webview){
		String title = resources.getString(R.string.action_validationLayerList);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.add_validate_layers_dialog;

		DialogFragment dialog = AddValidateLayersDialog.newInstance(title, ok, cancel, layout, connectivityListener, hasThreadPool, webview);

		dialog.show(fragManager, "addValidateLayersDialog");
	}

	//validation option dialog
	public void showValidationOptionDialog(HasThreadPool hasThreadPool, ArrayList<Validation> checkedLayers, final CordovaWebView webview){
		String title = resources.getString(R.string.action_validationOption);
		String ok = resources.getString(R.string.start_validation_button);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.validation_option_dialog;

		DialogFragment dialog = ValidationOptionDialog.newInstance(title, ok, cancel, layout, hasThreadPool, checkedLayers, webview);

		dialog.show(fragManager, "validationOptionDialog");
	}

	//validation option setting dialog
	public void showValidationOptionSettingDialog(HasThreadPool hasThreadPool, Validation selectedLayer){
		String title = resources.getString(R.string.action_optionSetting);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.validation_option_setting_dialog;

		DialogFragment dialog = ValidationOptionSettingDialog.newInstance(title, ok, cancel, layout, hasThreadPool, selectedLayer);

		dialog.show(fragManager, "validationOptionSettingDialog");
	}

	//validation detail option setting dialog
	public void showValidationDetailOptionSettingDialog(HasThreadPool hasThreadPool, String optionName, Validation selectedLayer, RelativeLayout selectedOptionNameLayout){
		String title = resources.getString(R.string.action_detailOptionSetting);
		String ok = resources.getString(android.R.string.ok);
		String cancel = resources.getString(android.R.string.cancel);
		int layout = R.layout.validation_detail_option_setting_dialog;

		DialogFragment dialog = ValidationDetailOptionSettingDialog.newInstance(title, ok, cancel, layout, hasThreadPool, optionName, selectedLayer, selectedOptionNameLayout);

		dialog.show(fragManager, "validationDetailOptionSettingDialog");
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
