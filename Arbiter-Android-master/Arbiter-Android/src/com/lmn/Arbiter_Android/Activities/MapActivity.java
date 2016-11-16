package com.lmn.Arbiter_Android.Activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONObject;

import com.lmn.Arbiter_Android.ArbiterProject;
import com.lmn.Arbiter_Android.ArbiterState;
import com.lmn.Arbiter_Android.Dialog.Dialogs.ValidationErrorReportDialog;
import com.lmn.Arbiter_Android.Loaders.ValidationLayersListLoader;
import com.lmn.Arbiter_Android.OOMWorkaround;
import com.lmn.Arbiter_Android.R;
import com.lmn.Arbiter_Android.Util;
import com.lmn.Arbiter_Android.About.About;
import com.lmn.Arbiter_Android.ConnectivityListeners.ConnectivityListener;
import com.lmn.Arbiter_Android.ConnectivityListeners.CookieConnectivityListener;
import com.lmn.Arbiter_Android.ConnectivityListeners.HasConnectivityListener;
import com.lmn.Arbiter_Android.ConnectivityListeners.SyncConnectivityListener;
import com.lmn.Arbiter_Android.CookieManager.ArbiterCookieManager;
import com.lmn.Arbiter_Android.CordovaPlugins.ArbiterCordova;
import com.lmn.Arbiter_Android.DatabaseHelpers.ApplicationDatabaseHelper;
import com.lmn.Arbiter_Android.DatabaseHelpers.ProjectDatabaseHelper;
import com.lmn.Arbiter_Android.DatabaseHelpers.TableHelpers.ControlPanelHelper;
import com.lmn.Arbiter_Android.DatabaseHelpers.TableHelpers.PreferencesHelper;
import com.lmn.Arbiter_Android.DatabaseHelpers.TableHelpers.SyncTableHelper;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogs;
import com.lmn.Arbiter_Android.Dialog.Dialogs.FailedSyncHelper;
import com.lmn.Arbiter_Android.Dialog.Dialogs.InsertFeatureDialog;
import com.lmn.Arbiter_Android.Dialog.ProgressDialog.SyncProgressDialog;
import com.lmn.Arbiter_Android.GeometryEditor.GeometryEditor;
import com.lmn.Arbiter_Android.Map.Map;
import com.lmn.Arbiter_Android.Notifications.Sync;
import com.lmn.Arbiter_Android.ProjectStructure.ProjectStructure;
import com.lmn.Arbiter_Android.ReturnQueues.OnReturnToMap;
import com.lmn.Arbiter_Android.Settings.Settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MapActivity extends FragmentActivity implements CordovaInterface,
        Map.MapChangeListener, Map.CordovaMap, HasThreadPool, HasConnectivityListener {

    private Geocoder mCoder;
    private ArbiterDialogs dialogs;
    private String TAG = "MAP_ACTIVITY";
    private ArbiterProject arbiterProject;
    private MapChangeHelper mapChangeHelper;
    private IncompleteProjectHelper incompleteProjectHelper;
    private boolean menuPrepared;
    private SyncConnectivityListener syncConnectivityListener;
    private CookieConnectivityListener cookieConnectivityListener;
    private NotificationBadge notificationBadge;
    private boolean isDestroyed = false;
    private CordovaWebView cordovaWebView; // For CORDOVA
    private String sfName = "imgData"; // SharedPreferences for image data
    private String upToDateReport = "report"; // SharedPreferences for validation report
    private int captureNum = 0;
    private TableLayout navigatorTableLayout;
    private JSONObject reportObject;
    private JSONArray detailedReports;
    private int errorSize=0;
    private int errorPos=0;

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private CordovaPlugin activityResultCallback;
    protected boolean activityResultKeepRunning;

    // Keep app running when pause is received. (default = true)
    // If true, then the JavaScript and native code continue to run in the background
    // when another application (activity) is started.
    protected boolean keepRunning = true;

    private FailedSyncHelper failedSyncHelper;
    static double lat, lon;
    Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config.init(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Init(savedInstanceState);

        // Multiple language dialog
        final CharSequence[] items = {getResources().getString(R.string.action_korean), getResources().getString(R.string.action_english),
                getResources().getString(R.string.action_portugal), getResources().getString(R.string.action_spain)};
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle(getResources().getString(R.string.action_multiple_language));
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0)
                            locale = new Locale("ko");

                        else if (item == 1)
                            locale = new Locale("en");

                        else if (item == 2)
                            locale = new Locale("pt");

                        else if (item == 3)
                            locale = new Locale("es");

                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                        invalidateOptionsMenu();
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setIcon(getResources().getDrawable(R.drawable.icon));
        alert.setCanceledOnTouchOutside(false);
        alert.show();

        MapActivity.this.keepRunning = MapActivity.this.getBooleanProperty("KeepRunning", true);
        dialogs = new ArbiterDialogs(getApplicationContext(), getResources(), getSupportFragmentManager());
        cordovaWebView = (CordovaWebView) findViewById(R.id.webView1);

        //add interface between javascript and android
        cordovaWebView.addJavascriptInterface(new JSInterface(this), "Android"); //You will access this via Android.method(args);

        cordovaWebView.loadUrl(ArbiterCordova.mainUrl, 5000);
        mapChangeHelper = new MapChangeHelper(MapActivity.this, cordovaWebView, incompleteProjectHelper);
        mCoder = new Geocoder(MapActivity.this);
        checkNotificationsAreComputed();

    }

    private String getProjectPath() {
        String projectName = ArbiterProject.getArbiterProject()
                .getOpenProject(this);

        return ProjectStructure.getProjectPath(projectName);
    }

    private SQLiteDatabase getProjectDatabase() {
        return ProjectDatabaseHelper.getHelper(getApplicationContext(),
                getProjectPath(), false).getWritableDatabase();
    }

    private void Init(Bundle savedInstanceState) {

        getProjectStructure();
        InitApplicationDatabase();
        InitArbiterProject();
        setListeners();
        clearControlPanelKVP();
        clearFindMe();

        this.failedSyncHelper = new FailedSyncHelper(this,
                getProjectDatabase(), this.syncConnectivityListener, this);

        this.failedSyncHelper.checkIncompleteSync();
    }

    private void clearFindMe() {

        SQLiteDatabase projectDb = (new Util()).getProjectDb(this, false);

        PreferencesHelper.getHelper().delete(projectDb, getApplicationContext(), PreferencesHelper.FINDME);
    }

    private void clearControlPanelKVP() {
        ControlPanelHelper helper = new ControlPanelHelper(this);

        helper.clearControlPanel();
    }

    private void InitApplicationDatabase() {
        ApplicationDatabaseHelper.
                getHelper(getApplicationContext());
    }

    private void getProjectStructure() {
        ProjectStructure.getProjectStructure();
    }

    private void InitArbiterProject() {
        arbiterProject = ArbiterProject.getArbiterProject();

        // This will also ensure that a project exists
        arbiterProject.getOpenProject(this);
    }

    /**
     * Set listeners
     */
    private void setListeners() {
        final MapActivity activity = this;

        ImageButton layersButton = (ImageButton) findViewById(R.id.layersButton);
        //ù ����Ʈ ������Ʈ���� ���� ��, ������Ʈ�� �̸��� �Է¹���.
        layersButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arbiterProject != null) {
                    String openProject = arbiterProject.getOpenProject(activity);
                    if (openProject.equals(activity.getResources().getString(R.string.default_project_name))) {
                        // create new project
                        Log.d("default", "default");
                        Map.getMap().createNewProject(cordovaWebView);
                    } else {
                        Log.d("dialog", "dialog");
                        dialogs.showLayersDialog(activity);
                    }
                }
            }
        });

        ImageButton syncButton = (ImageButton) findViewById(R.id.syncButton);

        syncConnectivityListener = new SyncConnectivityListener(this, syncButton);
        // ������ ���� �� �ְ�, ������ �κ��� ���ٸ� ������ ���� ������ ���� ����ȭ �Ѵ�.
        syncButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (syncConnectivityListener.isConnected() && makeSureNotEditing()) {
                    Log.d("pressed", "pressed");
                    SyncProgressDialog.show(activity);

                    getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {

                            new ArbiterCookieManager(getApplicationContext()).updateAllCookies();

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    Map.getMap().sync(cordovaWebView);
                                }
                            });
                        }
                    });
                }
            }
        });

        cookieConnectivityListener = new CookieConnectivityListener(this, this, this);

        ImageButton aoiButton = (ImageButton) findViewById(R.id.AOIButton);
        // �ʱ�ȭ �� ��ġ�� �̵�.
        aoiButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Map.getMap().zoomToAOI(cordovaWebView);
            }
        });

        final ImageButton locationButton = (ImageButton) findViewById(R.id.locationButton);
        // loadURL�� ���Ͽ� ���� ��ġ�� ����� ��Ÿ����.
        locationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locationButton.getAnimation() == null) {

                    Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_rotate);

                    rotation.setDuration(2500);

                    // 60000 / 2500
                    rotation.setRepeatCount(24);

                    locationButton.startAnimation(rotation);

                    Map.getMap().zoomToCurrentPosition(cordovaWebView);
                }
            }
        });

        ImageButton zoomInButton = (ImageButton) findViewById(R.id.zoomInButton);

        zoomInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Map.getMap().zoomIn(cordovaWebView);
            }
        });

        ImageButton zoomOutButton = (ImageButton) findViewById(R.id.zoomOutButton);

        zoomOutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Map.getMap().zoomOut(cordovaWebView);
            }
        });

        initIncompleteProjectHelper();

        incompleteProjectHelper.setSyncButton(syncButton);
    }

    // Return true if not editing
    private boolean makeSureNotEditing() {
        int editMode = mapChangeHelper.getEditMode();

        if (editMode == GeometryEditor.Mode.OFF || editMode == GeometryEditor.Mode.SELECT) {
            return true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.finish_editing_title);
        builder.setMessage(R.string.finish_editing_message);
        builder.setIcon(R.drawable.icon);
        builder.setPositiveButton(android.R.string.ok, null);

        builder.create().show();

        return false;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public NotificationBadge getNotificationBadge() {
        return this.notificationBadge;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        Log.w("MapActivity", "MapActivity onCreateOptionsMenu");

        // remove  it clause because Multiple Language updates onCreateOptionsMenu
       // if (this.notificationBadge == null)
            this.notificationBadge = new NotificationBadge(this, menu);



        return true;
    }

    private void initIncompleteProjectHelper() {
        if (incompleteProjectHelper == null) {
            incompleteProjectHelper = new IncompleteProjectHelper(this);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!this.menuPrepared) {
            initIncompleteProjectHelper();

            incompleteProjectHelper.setInsertButton(menu);

            this.menuPrepared = true;
        }

        return true;
    }

    private void openInsertFeatureDialog() {
        String title = getResources().getString(R.string.insert_feature_title);
        String cancel = getResources().getString(android.R.string.cancel);

        DialogFragment frag = InsertFeatureDialog.newInstance(title, cancel);

        if (frag != null) {
            frag.show(getSupportFragmentManager(), InsertFeatureDialog.TAG);

        }
    }

    private void startAOIActivity() {
        Intent aoiIntent = new Intent(this, AOIActivity.class);
        this.startActivity(aoiIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final MapActivity activity = this;

        switch (item.getItemId()) {
            case R.id.action_new_feature:
                if (makeSureNotEditing()) {
                    openInsertFeatureDialog();
                }
                return true;

            case R.id.action_new_image:
                if (makeSureNotEditing()) {
                    dialogs.showImagesDialog(activity, cordovaWebView);
                }
                return true;

            case R.id.action_validation:
                if (makeSureNotEditing()) {
                    dialogs.showAddValidateLayersDialog(activity, getListener(), cordovaWebView);
                    }

                return true;

            case R.id.action_capture:
                if (makeSureNotEditing()) {
                    startCapture();
                }
                return true;

            case R.id.action_servers:
                dialogs.showServersDialog();
                return true;

            case R.id.action_projects:
                if (makeSureNotEditing()) {
                    Map.getMap().goToProjects(cordovaWebView);
                }
                return true;

            case R.id.action_tilesets:
                dialogs.showTilesetsDialog();
                return true;

            case R.id.action_baseMap:
                showBaseLayers();
                return true;

            case R.id.action_validationManagement:
                startValidationManagement();
                return true;

            case R.id.action_search:
                startSearch();
                return true;

            case R.id.action_aoi:
                if (makeSureNotEditing()) {
                    if (arbiterProject != null) {
                        String openProject = arbiterProject.getOpenProject(this);
                        if (openProject.equals(this.getResources().getString(R.string.default_project_name))) {

                            this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Activity context = getActivity();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle(context.getResources().getString(R.string.error));
                                    builder.setIcon(context.getResources().getDrawable(R.drawable.icon));
                                    builder.setMessage(context.getResources().getString(R.string.error_aoi_create_project));

                                    builder.create().show();
                                }
                            });
                        } else {
                            startAOIActivity();
                        }

                    }
                }

                return true;

            case R.id.action_settings:
                new Settings(this).displaySettingsDialog(false);
                return true;

            case R.id.action_about:
                new About(this).displayAboutDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

        if (this.cordovaWebView == null) {
            return;
        } else if (this.isFinishing()) {
            this.cordovaWebView.handlePause(this.keepRunning);
        }
    }

    private void checkNotificationsAreComputed() {

        final Activity activity = this;

        SyncProgressDialog.show(this, getResources().getString(R.string.loading),
                getResources().getString(R.string.please_wait));

        getThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                SyncTableHelper helper = new SyncTableHelper(getProjectDatabase());

                final Sync sync = helper.checkNotificationsAreComputed();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (sync != null && !sync.getNotificationsAreSet()) {
                            Map.getMap().getNotifications(cordovaWebView, Integer.toString(sync.getId()));
                        } else {

                            SyncProgressDialog.dismiss(activity);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, TAG + " onResume");

        if (this.cordovaWebView == null) {
            return;
        }

        this.cordovaWebView.handleResume(this.keepRunning,
                this.activityResultKeepRunning);

        // If app doesn't want to run in background
        if (!this.keepRunning || this.activityResultKeepRunning) {

            // Restore multitasking state
            if (this.activityResultKeepRunning) {
                this.keepRunning = this.activityResultKeepRunning;
                this.activityResultKeepRunning = false;
            }
        }

        if (arbiterProject != null) {

            getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    OOMWorkaround oom = new OOMWorkaround(getActivity());
                    oom.resetSavedBounds(false);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // Setting the aoi
                            if (ArbiterState.getArbiterState().isSettingAOI()) {
                                Log.w(TAG, TAG + ".onResume() setting aoi");
                                SyncProgressDialog.show(getActivity());

                                getThreadPool().execute(new Runnable() {

                                    @Override
                                    public void run() {

                                        new ArbiterCookieManager(getApplicationContext()).updateAllCookies();

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                updateProjectAOI();
                                            }
                                        });
                                    }
                                });
                            } else if (!arbiterProject.isSameProject(getApplicationContext())) {

                                arbiterProject.makeSameProject();

                                Map.getMap().resetWebApp(cordovaWebView);

                                // If the user changed projects, check to
                                // see if the project has an aoi or not
                                incompleteProjectHelper.checkForAOI();
                            }

                            OnReturnToMap.getInstance().executeJobs(getActivity());
                        }
                    });
                }
            });
        }
    }

    private void updateProjectAOI() {
        final String aoi = ArbiterState.getArbiterState().getNewAOI();

        updateProjectAOI(aoi);
    }

    private void updateProjectAOI(String aoi) {

        Map.getMap().updateAOI(cordovaWebView, aoi);

        // Set the new aoi to null so the we know we're
        // not setting the aoi anymore.
        ArbiterState.getArbiterState().setNewAOI(null);
    }

    public void startSearch()
    {
        String[] options = new String[]{getResources().getString(R.string.action_address), getResources().getString(R.string.action_coordinate)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.action_search);

        builder.setSingleChoiceItems(options, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {

                        if(id==0)
                        {
                            Intent intent = new Intent(getApplicationContext(), FindAreaActivity.class);
                            startActivityForResult(intent, 101);
                        }

                        else
                        {
                            dialogs.showCoordinatesDialog(cordovaWebView);
                        }


                        dialog.dismiss();
                    }
                });

        // create dialog
        builder.setCancelable(false);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setIcon(R.drawable.icon);
        builder.create().show();
    }

    public void showBaseLayers()
    {
        String[] layers = new String[]{getResources().getString(R.string.action_baseMap_OpenStreetMap), getResources().getString(R.string.action_baseMap_BingRoad),
                getResources().getString(R.string.action_baseMap_BingAerial), getResources().getString(R.string.action_baseMap_BingAerialWithLabels)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.action_baseLayers);

        builder.setSingleChoiceItems(layers, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {

                        Map.getMap().baseLayers(cordovaWebView, layers[id]);


                        dialog.dismiss();
                    }
                });

        // create dialog
        builder.setCancelable(false);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setIcon(R.drawable.icon);
        builder.create().show();
    }

    private void startValidationManagement()
    {
        String[] options = new String[]{getResources().getString(R.string.action_report), getResources().getString(R.string.action_error_navigator),
                getResources().getString(R.string.action_removeErrorMarking)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.action_validationManagement);

        builder.setSingleChoiceItems(options, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {

                        if(id==0)
                        {
                            SharedPreferences getErrorReport = getActivity().getSharedPreferences(upToDateReport, 0);
                            if(!getErrorReport.getString("report","").equals(""))
                            {
                                Intent validationErrorReportIntent = new Intent(getApplicationContext(), ValidationErrorReportDialog.class);
                                validationErrorReportIntent.putExtra("report",getErrorReport.getString("report",""));
                                validationErrorReportIntent.putExtra("check",getErrorReport.getBoolean("check",false));
                                startActivity(validationErrorReportIntent);

                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                builder.setTitle(R.string.none_report_title);
                                builder.setMessage(R.string.none_report_message);
                                builder.setIcon(R.drawable.icon);
                                builder.setPositiveButton(android.R.string.ok, null);
                                builder.setCancelable(false);
                                builder.create().show();
                            }
                        }

                        else if(id==1)
                        {
                            SharedPreferences checkValidation = getActivity().getSharedPreferences(upToDateReport, 0);
                            if(checkValidation.getString("report", "").equals(""))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                builder.setTitle(R.string.cannot_start_navigator);
                                builder.setMessage(R.string.navigator_error_message);
                                builder.setIcon(R.drawable.icon);
                                builder.setPositiveButton(android.R.string.ok, null);
                                builder.setCancelable(false);
                                builder.create().show();
                            }

                            else
                            {
                                //NAVIGATOR REQUEST LANDSCAPE ORIENTATION
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                                LinearLayout errNavigatorLayout = (LinearLayout) findViewById(R.id.errorNavigator);

                                if(errNavigatorLayout.getVisibility() == View.GONE) {

                                    if(errorPos >= 0 && reportObject!=null)
                                    {
                                        errNavigatorLayout.setVisibility(View.VISIBLE);
                                    }

                                    else {
                                            errNavigatorLayout.setVisibility(View.VISIBLE);
                                            try {
                                                reportObject = new JSONObject(checkValidation.getString("report", ""));
                                                detailedReports = reportObject.getJSONArray("DetailsReport");
                                                errorSize = detailedReports.length();
                                                errorPos = 0;
                                                startErrorNavigator();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                    }
                                }
                                else
                                {
                                    errNavigatorLayout.setVisibility(View.GONE);
                                }
                            }

                        }

                        else
                        {
                            if (makeSureNotEditing()) {
                                removeErrorMarking();
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); // GO BACK TO THE SYSTEM DEFAULTS
                            }
                        }
                        dialog.dismiss();
                    }
                });

        // create dialog
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setIcon(R.drawable.icon);
        builder.setCancelable(false);
        builder.create().show();

    }

    private void startErrorNavigator()
    {

        navigatorTableLayout = (TableLayout) findViewById(R.id.navigatorContent);
        navigatorTableLayout.removeViews(1, navigatorTableLayout.getChildCount()-1);

        try {

            TableRow tr = new TableRow(getActivity());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackgroundColor(Color.parseColor("#BDC3C7"));
            tr.setPadding(1, 1, 1, 1);

            TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tableParams.rightMargin = 1;

            TextView errorNumber = new TextView(getActivity());
            errorNumber.setBackgroundColor(Color.parseColor("#34495E"));
            errorNumber.setTextSize(13);
            errorNumber.setPadding(5, 5, 5, 5);
            errorNumber.setTextColor(Color.parseColor("#ECF0F1"));
            errorNumber.setGravity(Gravity.CENTER_HORIZONTAL);
            errorNumber.setText("Error-" + (errorPos+1));
            errorNumber.setLayoutParams(tableParams);
            tr.addView(errorNumber);

            TextView errorNameContent = new TextView(getActivity());
            errorNameContent.setBackgroundColor(Color.parseColor("#34495E"));
            errorNameContent.setTextSize(13);
            errorNameContent.setPadding(5, 5, 5, 5);
            errorNameContent.setTextColor(Color.parseColor("#ECF0F1"));
            errorNameContent.setGravity(Gravity.CENTER_HORIZONTAL);
            errorNameContent.setText(detailedReports.getJSONObject(errorPos).getString("errName"));
            errorNameContent.setLayoutParams(tableParams);
            tr.addView(errorNameContent);

            TextView errorFeatureID = new TextView(getActivity());
            errorFeatureID.setBackgroundColor(Color.parseColor("#34495E"));
            errorFeatureID.setTextSize(13);
            errorFeatureID.setPadding(5, 5, 5, 5);
            errorFeatureID.setTextColor(Color.parseColor("#ECF0F1"));
            errorFeatureID.setGravity(Gravity.CENTER_HORIZONTAL);
            errorFeatureID.setText(detailedReports.getJSONObject(errorPos).getString("featureID"));
            errorFeatureID.setLayoutParams(tableParams);
            tr.addView(errorFeatureID);

            navigatorTableLayout.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            // Zoom to error feature
            ValidationLayersListLoader validationLayer = new ValidationLayersListLoader(getActivity());
            String layerID = validationLayer.getLayerID(detailedReports.getJSONObject(errorPos).getString("featureID"));
            Map.getMap().navigateFeature(cordovaWebView, layerID, detailedReports.getJSONObject(errorPos).getString("featureID"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(errorPos >= 0)
        findViewById(R.id.leftButton).setOnClickListener(leftClickListener);

        if(errorPos < errorSize)
        findViewById(R.id.rightButton).setOnClickListener(rightClickListener);
    }

    ImageButton.OnClickListener leftClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            try {

                findViewById(R.id.leftButton).setEnabled(false);
                Handler h = new Handler();
                h.postDelayed(new Runnable(){
                    public void run()
                    {
                        findViewById(R.id.leftButton).setEnabled(true);
                    }
                }, 1000); // Should to wait click minimum 1 second for Vector Rendering

                navigatorTableLayout.removeViews(1, navigatorTableLayout.getChildCount()-1);
                errorPos = errorPos - 1;
                if(errorPos < 0)
                    errorPos = 0;

                TableRow tr = new TableRow(getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tr.setBackgroundColor(Color.parseColor("#BDC3C7"));
                tr.setPadding(1, 1, 1, 1);

                TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                tableParams.rightMargin = 1;

                TextView errorNumber = new TextView(getActivity());
                errorNumber.setBackgroundColor(Color.parseColor("#34495E"));
                errorNumber.setTextSize(13);
                errorNumber.setPadding(5, 5, 5, 5);
                errorNumber.setTextColor(Color.parseColor("#ECF0F1"));
                errorNumber.setGravity(Gravity.CENTER_HORIZONTAL);
                errorNumber.setText("Error-" + (errorPos+1));
                errorNumber.setLayoutParams(tableParams);
                tr.addView(errorNumber);

                TextView errorNameContent = new TextView(getActivity());
                errorNameContent.setBackgroundColor(Color.parseColor("#34495E"));
                errorNameContent.setTextSize(13);
                errorNameContent.setPadding(5, 5, 5, 5);
                errorNameContent.setTextColor(Color.parseColor("#ECF0F1"));
                errorNameContent.setGravity(Gravity.CENTER_HORIZONTAL);
                errorNameContent.setText(detailedReports.getJSONObject(errorPos).getString("errName"));
                errorNameContent.setLayoutParams(tableParams);
                tr.addView(errorNameContent);

                TextView errorFeatureID = new TextView(getActivity());
                errorFeatureID.setBackgroundColor(Color.parseColor("#34495E"));
                errorFeatureID.setTextSize(13);
                errorFeatureID.setPadding(5, 5, 5, 5);
                errorFeatureID.setTextColor(Color.parseColor("#ECF0F1"));
                errorFeatureID.setGravity(Gravity.CENTER_HORIZONTAL);
                errorFeatureID.setText(detailedReports.getJSONObject(errorPos).getString("featureID"));
                errorFeatureID.setLayoutParams(tableParams);
                tr.addView(errorFeatureID);

                navigatorTableLayout.addView(tr, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                // Zoom and Select to error feature
                ValidationLayersListLoader validationLayer = new ValidationLayersListLoader(getActivity());
                String layerID = validationLayer.getLayerID(detailedReports.getJSONObject(errorPos).getString("featureID"));
                Map.getMap().navigateFeature(cordovaWebView, layerID, detailedReports.getJSONObject(errorPos).getString("featureID"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    ImageButton.OnClickListener rightClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            try {
                findViewById(R.id.rightButton).setEnabled(false);
                Handler h = new Handler();
                h.postDelayed(new Runnable(){
                    public void run()
                    {
                        findViewById(R.id.rightButton).setEnabled(true);
                    }
                }, 1000); // Should to wait click minimum 1 second for Vector Rendering

                navigatorTableLayout.removeViews(1, navigatorTableLayout.getChildCount()-1);
                errorPos = errorPos + 1;
                if(errorPos > errorSize-1)
                    errorPos = errorSize-1;

                TableRow tr = new TableRow(getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tr.setBackgroundColor(Color.parseColor("#BDC3C7"));
                tr.setPadding(1, 1, 1, 1);

                TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                tableParams.rightMargin = 1;

                TextView errorNumber = new TextView(getActivity());
                errorNumber.setBackgroundColor(Color.parseColor("#34495E"));
                errorNumber.setTextSize(13);
                errorNumber.setPadding(5, 5, 5, 5);
                errorNumber.setTextColor(Color.parseColor("#ECF0F1"));
                errorNumber.setGravity(Gravity.CENTER_HORIZONTAL);
                errorNumber.setText("Error-" + (errorPos+1));
                errorNumber.setLayoutParams(tableParams);
                tr.addView(errorNumber);

                TextView errorNameContent = new TextView(getActivity());
                errorNameContent.setBackgroundColor(Color.parseColor("#34495E"));
                errorNameContent.setTextSize(13);
                errorNameContent.setPadding(5, 5, 5, 5);
                errorNameContent.setTextColor(Color.parseColor("#ECF0F1"));
                errorNameContent.setGravity(Gravity.CENTER_HORIZONTAL);
                errorNameContent.setText(detailedReports.getJSONObject(errorPos).getString("errName"));
                errorNameContent.setLayoutParams(tableParams);
                tr.addView(errorNameContent);

                TextView errorFeatureID = new TextView(getActivity());
                errorFeatureID.setBackgroundColor(Color.parseColor("#34495E"));
                errorFeatureID.setTextSize(13);
                errorFeatureID.setPadding(5, 5, 5, 5);
                errorFeatureID.setTextColor(Color.parseColor("#ECF0F1"));
                errorFeatureID.setGravity(Gravity.CENTER_HORIZONTAL);
                errorFeatureID.setText(detailedReports.getJSONObject(errorPos).getString("featureID"));
                errorFeatureID.setLayoutParams(tableParams);
                tr.addView(errorFeatureID);

                navigatorTableLayout.addView(tr, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                // Zoom and Select to error feature
                ValidationLayersListLoader validationLayer = new ValidationLayersListLoader(getActivity());
                String layerID = validationLayer.getLayerID(detailedReports.getJSONObject(errorPos).getString("featureID"));
                Map.getMap().navigateFeature(cordovaWebView, layerID, detailedReports.getJSONObject(errorPos).getString("featureID"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void startCapture() {

        cordovaWebView.setDrawingCacheEnabled( true);

        Bitmap screenshot = Bitmap. createBitmap(cordovaWebView.getWidth(), cordovaWebView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);

        cordovaWebView.draw(c);

        String filename = "Capture" + captureNum + ".png";

        try {
            File f = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/", filename);

            f.createNewFile();
            OutputStream outStream = new FileOutputStream(f);

            screenshot.compress(Bitmap.CompressFormat. PNG, 100, outStream);
            outStream.close();

            AlertDialog.Builder dialog = new AlertDialog.Builder(MapActivity.this);
            dialog.setCancelable(false);
            dialog  .setTitle(R.string.action_capture)
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
        File f = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/", filename);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        captureNum++;
    }

    private void removeErrorMarking()
    {
        SharedPreferences destroyReportData = getSharedPreferences(upToDateReport, 0);
        SharedPreferences.Editor reportEditor = destroyReportData.edit();
        reportEditor.clear();
        reportEditor.commit();

        LinearLayout errNavigatorLayout = (LinearLayout) findViewById(R.id.errorNavigator);
        errNavigatorLayout.setVisibility(View.GONE);
        errorPos = 0;
        errorSize = 0;
        reportObject = null;
        detailedReports = null;

        Map.getMap().removeErrorMarking(cordovaWebView);
    }

    @Override
    protected void onDestroy() {

        //* If you terminate this app, all SharedPreferences data will be removed.
        SharedPreferences destroyImageData = getSharedPreferences(sfName, 0);
        SharedPreferences destroyReportData = getSharedPreferences(upToDateReport, 0);
        SharedPreferences.Editor imageEditor = destroyImageData.edit();
        SharedPreferences.Editor reportEditor = destroyReportData.edit();
        imageEditor.clear();
        reportEditor.clear();
        imageEditor.commit();
        reportEditor.commit();


        this.isDestroyed = true;
        super.onDestroy();
        if (this.cordovaWebView != null) {
            Log.w("MapActivity", "MapActivity onDestroy");
            cordovaWebView.handleDestroy();
        }

        if (this.failedSyncHelper != null) {
            this.failedSyncHelper.dismiss();
        }

        if (this.notificationBadge != null) {
            this.notificationBadge.onDestroy();
        }

        if (this.syncConnectivityListener != null) {
            this.syncConnectivityListener.onDestroy();
        }

        if (this.cookieConnectivityListener != null) {
            this.cookieConnectivityListener.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Map.MapChangeListener methods
     */
    public MapChangeHelper getMapChangeHelper() {
        return this.mapChangeHelper;
    }

    /**
     * Map.CordovaMap methods
     */
    @Override
    public CordovaWebView getWebView() {
        return this.cordovaWebView;
    }

    /**
     * Cordova methods
     */
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    @Override
    public Object onMessage(String message, Object obj) {
        Log.d(TAG, message);
        if (!isDestroyed && message.equals("onPageFinished")) {
            if (obj instanceof String) {
                if (obj.equals("about:blank")) {
                    this.cordovaWebView.loadUrl(ArbiterCordova.mainUrl);
                }

                this.cordovaWebView.clearHistory();
            }
        }
        return null;
    }

    @Override
    public void setActivityResultCallback(CordovaPlugin plugin) {
        this.activityResultCallback = plugin;
    }

    @Override
    public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {
        this.activityResultCallback = command;
        this.activityResultKeepRunning = this.keepRunning;

        // If multitasking turned on, then disable it for activities that return results
        if (command != null) {
            this.keepRunning = false;
        }

        // Start activity
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode The request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        CordovaPlugin callback = this.activityResultCallback;
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, intent);
        }
        //For Address Search to find selected list item
        if (resultCode == 101 && intent != null) {
            String result = intent.getStringExtra("location");
            String[] array = result.split(",");
            lat = Double.parseDouble(array[0]);
            lon = Double.parseDouble(array[1]);
            Map.getMap().findArea(cordovaWebView, lat, lon);
        }
    }

    /**
     * Get boolean property for activity.
     *
     * @param name
     * @param defaultValue
     * @return the boolean value of the named property
     */
    public boolean getBooleanProperty(String name, boolean defaultValue) {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) {
            return defaultValue;
        }
        name = name.toLowerCase(Locale.getDefault());
        Boolean p;
        try {
            p = (Boolean) bundle.get(name);
        } catch (ClassCastException e) {
            String s = bundle.get(name).toString();
            p = "true".equals(s);
        }
        if (p == null) {
            return defaultValue;
        }
        return p.booleanValue();
    }

    @Override
    public ConnectivityListener getListener() {

        return this.syncConnectivityListener;
    }
}

