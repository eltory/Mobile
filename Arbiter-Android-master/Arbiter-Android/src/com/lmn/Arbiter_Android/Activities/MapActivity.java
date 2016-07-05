package com.lmn.Arbiter_Android.Activities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.zip.Inflater;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.json.JSONObject;

import com.lmn.Arbiter_Android.ArbiterProject;
import com.lmn.Arbiter_Android.ArbiterState;
import com.lmn.Arbiter_Android.FileReader.FileBrowser;
import com.lmn.Arbiter_Android.FileReader.Local;
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

import android.app.NativeActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
    // For CORDOVA
    private CordovaWebView cordovaWebView;

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
    Local local;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config.init(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Init(savedInstanceState);

        final CharSequence[] items = {"Korean", "English", "Portugal","Spain"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setTitle("Please select the language you want to use.");
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
        alert.show();

        MapActivity.this.keepRunning = MapActivity.this.getBooleanProperty("KeepRunning", true);
        dialogs = new ArbiterDialogs(getApplicationContext(), getResources(), getSupportFragmentManager());
        cordovaWebView = (CordovaWebView) findViewById(R.id.webView1);

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
        //첫 디폴트 프로젝트에서 누를 시, 프로젝트의 이름을 입력받음.
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
        // 서버와 연결 돼 있고, 수정한 부분이 없다면 서버의 지도 정보를 기기로 동기화 한다.
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
        // 초기화 된 위치로 이동.
        aoiButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Map.getMap().zoomToAOI(cordovaWebView);
            }
        });

        final ImageButton locationButton = (ImageButton) findViewById(R.id.locationButton);
        // loadURL을 통하여 현재 위치를 웹뷰로 나타낸다.
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

        if (this.notificationBadge == null) {
            this.notificationBadge = new NotificationBadge(this, menu);
        }

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
        switch (item.getItemId()) {
            case R.id.action_new_feature:
                if (makeSureNotEditing()) {
                    openInsertFeatureDialog();
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

            case R.id.action_image:

                Intent intent2 = new Intent(getApplicationContext(), FileBrowser.class);
                startActivityForResult(intent2, 202);


                return true;

            case R.id.action_tilesets:
                dialogs.showTilesetsDialog();
                return true;

            case R.id.action_shp:

                Intent intent3 = new Intent(getApplicationContext(), FileBrowser.class);
                startActivityForResult(intent3, 303);

                return true;

            case R.id.action_coordinate:
                dialogs.showCoordinatesDialog(cordovaWebView);
                return true;


            case R.id.action_search:

                Intent intent = new Intent(getApplicationContext(), FindAreaActivity.class);
                startActivityForResult(intent, 101);
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

    @Override
    protected void onDestroy() {
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
        if (resultCode == 101 && intent != null) {
            String result = intent.getStringExtra("location");
            String[] array = result.split(",");
            lat = Double.parseDouble(array[0]);
            lon = Double.parseDouble(array[1]);
            Map.getMap().findArea(cordovaWebView, lat, lon);
        }
        else if (resultCode == 202 && intent != null) {
            String result = intent.getExtras().getString("path");
            String name = intent.getExtras().getString("name");
            String metaPath = result;
            metaPath = metaPath.substring(0,metaPath.lastIndexOf("."));
            metaPath = metaPath+".txt";

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(metaPath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
            String str, str1="";
            String map[];
            try {
                while( (str = bufferReader.readLine()) != null )
                str1 += str;
            } catch (IOException e) {
                e.printStackTrace();
            }
            map = str1.split(",");
            double left,bottom,right,top;
            left = Double.parseDouble(map[0]);
            bottom = Double.parseDouble(map[1]);
            right = Double.parseDouble(map[2]);
            top = Double.parseDouble(map[3]);

            Map.getMap().addImageLayer(cordovaWebView, result,left,bottom,right,top,name);
        }
        else if (resultCode == 303 && intent != null)
        {
            String fileName = intent.getExtras().getString("name");
            local = new Local(cordovaWebView, fileName);
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

