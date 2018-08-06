package com.lmn.OpenGDS_Android.Dialog.Dialogs.ValidationDialog;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lmn.Arbiter_Android.Activities.HasThreadPool;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoading;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoadingJob;
import com.lmn.Arbiter_Android.BaseClasses.Server;
import com.lmn.Arbiter_Android.DatabaseHelpers.ApplicationDatabaseHelper;
import com.lmn.Arbiter_Android.DatabaseHelpers.TableHelpers.ServersHelper;
import com.lmn.Arbiter_Android.Dialog.ArbiterDialogFragment;
import com.lmn.Arbiter_Android.LoaderCallbacks.LayerLoaderCallbacks;
import com.lmn.Arbiter_Android.Map.Map;
import com.lmn.Arbiter_Android.R;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.GeoServer.ServerInfo;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Classification;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.DetailOptions;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.ValidationAll;
import com.lmn.OpenGDS_Android.ListAdapters.NewOverlayList;

import org.apache.cordova.CordovaWebView;

import java.util.ArrayList;

/**
 * 마지막 서버의 레이어 선택하는 다이얼로그
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.17.
 */
public class NewValidationLayerDialog extends ArbiterDialogFragment {

    private NewOverlayList overlayList;

    @SuppressWarnings("unused")
    private LayerLoaderCallbacks layerLoaderCallbacks;


    private Map.MapChangeListener mapChangeListener;
    private HasThreadPool hasThreadPool;

    private JsonObject validation;
    private ValidationAll mobile;
    private ServerInfo serverInfo;
    private ArrayList<Classification> classifications;
    private DetailOptions options;

    private CordovaWebView cordovaWebView;
    private ApplicationDatabaseHelper appDbHelper = null;

    public static NewValidationLayerDialog newInstance(String title, String ok,
                                                       String cancel, int layout, HasThreadPool hasThreadPool, ArrayList<Classification> classifications, DetailOptions options, CordovaWebView cordovaWebView) {
        NewValidationLayerDialog frag = new NewValidationLayerDialog();

        frag.setTitle(title);
        frag.setOk(ok);
        frag.setCancel(cancel);
        frag.setLayout(layout);

        frag.hasThreadPool = hasThreadPool;
        frag.classifications = classifications;
        frag.options = options;
        frag.cordovaWebView = cordovaWebView;

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        try {
            this.mapChangeListener = (Map.MapChangeListener) this.getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(this.getActivity().toString()
                    + " must implement MapChangeListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.getActivity().getSupportLoaderManager().destroyLoader(R.id.loader_layers);
    }

    /**
     * 최종 검수 요청
     */
    @Override
    public void onPositiveClick() {
        mobile = getValidationObj();
        validation = new JsonObject();
        GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.serializeNulls().create();
        JsonElement element = gson.toJsonTree(mobile, ValidationAll.class);
        validation.add("mobile", element);
        Log.w("ValidationJson", gson.toJson(validation));

        final String result = "{\n" +
                "  \"features\": [\n" +
                "    {\n" +
                "      \"geometry\": {\n" +
                "        \"coordinates\": [\n" +
                "          127.10807460802009,\n" +
                "          37.40252689850943\n" +
                "        ],\n" +
                "        \"type\": \"Point\"\n" +
                "      },\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"layerID\": \"gis_osm_buildings\",\n" +
                "        \"errType\": \"그래픽오류\",\n" +
                "        \"errName\": \"단독존재오류\",\n" +
                "        \"comment\": \"\",\n" +
                "        \"featureID\": \"gis_osm_buildings.34\",\n" +
                "        \"the_geom\": \"POINT (127.10807460802009 37.40252689850943)\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"geometry\": {\n" +
                "        \"coordinates\": [\n" +
                "          127.10816926666462,\n" +
                "          37.402527199999994\n" +
                "        ],\n" +
                "        \"type\": \"Point\"\n" +
                "      },\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"layerID\": \"gis_osm_buildings\",\n" +
                "        \"errType\": \"그래픽오류\",\n" +
                "        \"errName\": \"단독존재오류\",\n" +
                "        \"comment\": \"\",\n" +
                "        \"featureID\": \"gis_osm_buildings.35\",\n" +
                "        \"the_geom\": \"POINT (127.10816926666462 37.402527199999994)\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"geometry\": {\n" +
                "        \"coordinates\": [\n" +
                "          127.10807460802009,\n" +
                "          37.40252689850943\n" +
                "        ],\n" +
                "        \"type\": \"Point\"\n" +
                "      },\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"layerID\": \"gis_osm_buildings\",\n" +
                "        \"errType\": \"그래픽오류\",\n" +
                "        \"errName\": \"단독존재오류\",\n" +
                "        \"comment\": \"\",\n" +
                "        \"featureID\": \"gis_osm_buildings.36\",\n" +
                "        \"the_geom\": \"POINT (127.10807460802009 37.40252689850943)\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"geometry\": {\n" +
                "        \"coordinates\": [\n" +
                "          127.10816926666462,\n" +
                "          37.402527199999994\n" +
                "        ],\n" +
                "        \"type\": \"Point\"\n" +
                "      },\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"layerID\": \"gis_osm_buildings\",\n" +
                "        \"errType\": \"그래픽오류\",\n" +
                "        \"errName\": \"단독존재오류\",\n" +
                "        \"comment\": \"\",\n" +
                "        \"featureID\": \"gis_osm_buildings.36\",\n" +
                "        \"the_geom\": \"POINT (127.10816926666462 37.402527199999994)\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"geometry\": {\n" +
                "        \"coordinates\": [\n" +
                "          127.1102114414223,\n" +
                "          37.400376550000004\n" +
                "        ],\n" +
                "        \"type\": \"Point\"\n" +
                "      },\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"layerID\": \"gis_osm_buildings\",\n" +
                "        \"errType\": \"그래픽오류\",\n" +
                "        \"errName\": \"단독존재오류\",\n" +
                "        \"comment\": \"\",\n" +
                "        \"featureID\": \"gis_osm_buildings.47\",\n" +
                "        \"the_geom\": \"POINT (127.1102114414223 37.400376550000004)\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"geometry\": {\n" +
                "        \"coordinates\": [\n" +
                "          127.1102114414223,\n" +
                "          37.400376550000004\n" +
                "        ],\n" +
                "        \"type\": \"Point\"\n" +
                "      },\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"layerID\": \"gis_osm_buildings\",\n" +
                "        \"errType\": \"그래픽오류\",\n" +
                "        \"errName\": \"단독존재오류\",\n" +
                "        \"comment\": \"\",\n" +
                "        \"featureID\": \"gis_osm_buildings.48\",\n" +
                "        \"the_geom\": \"POINT (127.1102114414223 37.400376550000004)\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"geometry\": {\n" +
                "        \"coordinates\": [\n" +
                "          127.1108543549413,\n" +
                "          37.400374400000004\n" +
                "        ],\n" +
                "        \"type\": \"Point\"\n" +
                "      },\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"layerID\": \"gis_osm_buildings\",\n" +
                "        \"errType\": \"그래픽오류\",\n" +
                "        \"errName\": \"단독존재오류\",\n" +
                "        \"comment\": \"\",\n" +
                "        \"featureID\": \"gis_osm_buildings.48\",\n" +
                "        \"the_geom\": \"POINT (127.1108543549413 37.400374400000004)\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"geometry\": {\n" +
                "        \"coordinates\": [\n" +
                "          127.1108543549413,\n" +
                "          37.400374400000004\n" +
                "        ],\n" +
                "        \"type\": \"Point\"\n" +
                "      },\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"layerID\": \"gis_osm_buildings\",\n" +
                "        \"errType\": \"그래픽오류\",\n" +
                "        \"errName\": \"단독존재오류\",\n" +
                "        \"comment\": \"\",\n" +
                "        \"featureID\": \"gis_osm_buildings.49\",\n" +
                "        \"the_geom\": \"POINT (127.1108543549413 37.400374400000004)\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"geometry\": {\n" +
                "        \"coordinates\": [\n" +
                "          127.1175257,\n" +
                "          37.4024849\n" +
                "        ],\n" +
                "        \"type\": \"Point\"\n" +
                "      },\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"layerID\": \"gis_osm_buildings\",\n" +
                "        \"errType\": \"그래픽오류\",\n" +
                "        \"errName\": \"중복점오류\",\n" +
                "        \"comment\": \"\",\n" +
                "        \"featureID\": \"gis_osm_buildings.149\",\n" +
                "        \"the_geom\": \"POINT (127.1175257 37.4024849)\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"geometry\": {\n" +
                "        \"coordinates\": [\n" +
                "          127.12120157,\n" +
                "          37.40455536\n" +
                "        ],\n" +
                "        \"type\": \"Point\"\n" +
                "      },\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"layerID\": \"gis_osm_buildings\",\n" +
                "        \"errType\": \"그래픽오류\",\n" +
                "        \"errName\": \"중복점오류\",\n" +
                "        \"comment\": \"\",\n" +
                "        \"featureID\": \"gis_osm_buildings.242\",\n" +
                "        \"the_geom\": \"POINT (127.12120157 37.40455536)\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"type\": \"FeatureCollection\"\n" +
                "}\n";

        // 최종 검수 요청
        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {
                try {
                    String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.Validator.start(" + gson.toJson(validation) + ");'))";
                    //String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.Validator.doneCallback(" + gson.fromJson(result, JsonObject.class) + ");'))";
                    cordovaWebView.loadUrl(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 서버 정보 불러와서 레이어 선택한 서버주소와 동일할 경우
     * 해당 서버의 각종 정보를 불러옴
     */
    public void getServerInfo() {
        final String url = overlayList.getLayerServerUrl();
        serverInfo = new ServerInfo();
        appDbHelper = ApplicationDatabaseHelper.getHelper(getActivity());
        SparseArray<Server> servers = ServersHelper.getServersHelper().
                getAll(appDbHelper.getWritableDatabase());

        for (int i = 0; i < servers.size(); ++i) {
            int key = servers.keyAt(i);
            try {
                Server server = servers.get(key);
                if (url.equalsIgnoreCase(server.getUrl())) {
                    serverInfo.setLayers(overlayList.getLayers());
                    serverInfo.setUrl(url.substring(0, url.length() - 4));
                    serverInfo.setUser(server.getUsername());
                    serverInfo.setPassword(server.getPassword());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 최종 검수 객체 생성
     *
     * @return 최종적으로 만들어진 검수 객체
     */
    public ValidationAll getValidationObj() {
        ValidationAll validation = new ValidationAll();
        getServerInfo();

        // 1: GeoServer
        validation.setGeoserver(serverInfo);
        // 2: Layers
        validation.setLayers(classifications);
        // 3: Options
        validation.setOptions(options);

        return validation;
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

    private void displayLayersLimit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setIcon(R.drawable.icon);
        builder.setTitle(R.string.layers_limit);
        builder.setMessage(R.string.too_many_layers);

        builder.create().show();
    }

    public void populateListView(View view) {
        populateOverlayList(view);
    }

    private void populateOverlayList(View view) {
        LinearLayout overlayList = (LinearLayout) view.findViewById(R.id.overlaysList);

        this.overlayList = new NewOverlayList(overlayList, this.getActivity(), R.layout.layer_selection_list_item, hasThreadPool);
        this.layerLoaderCallbacks = new LayerLoaderCallbacks(this.getActivity(), this.overlayList, R.id.loader_layers);
    }

    public void registerListeners(View view) {
        populateListView(view);
    }
}
