package com.lmn.OpenGDS_Android.Map;

import com.lmn.Arbiter_Android.Activities.MapChangeHelper;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoading;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoadingJob;

import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 웹뷰 상 데이터 처리를 위한 Javascript URL 호출 인터페이스
 *
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */
public class Map_Expansion {

    private Map_Expansion() {
    }

    private static Map_Expansion map = null;

    public interface MapChangeListener {
        MapChangeHelper getMapChangeHelper();
    }

    public interface CordovaMap {
        CordovaWebView getWebView();
    }

    public static Map_Expansion getMap() {
        if (map == null) {
            return new Map_Expansion();
        }
        return map;
    }

    /**
     * 위치검색(좌표 검색, 주소 검색)
     *
     * @param webview,latitude,longitude CordovaWebView,double,double
     * @author JiJungKeun
     */
    public void findArea(final CordovaWebView webview, final double latitude, final double longitude) {

        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {

                JSONObject coords = new JSONObject();
                JSONObject position = new JSONObject();

                try {
                    coords.put("latitude", latitude);
                    coords.put("longitude", longitude);
                    position.put("coords", coords);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String aoi = "Arbiter.Map.getMap().getLayersByName(Arbiter.AOI)[0]";
                String getMap = "Arbiter.Map.getMap()";
                String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.findme = new Arbiter.FindMe(" + getMap + "," + aoi + "); Arbiter.findme._zoom(" + position + ");'))";

                webview.loadUrl(url);
            }
        });
    }

    /**
     * 이미지 오버레이(드래그 삽입 이미지)
     *
     * @param webview,name,path CordovaWebView,String,String
     * @author JiJungKeun
     */
    public void addDrawImage(final CordovaWebView webview, final String name, final String path) {

        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {
                String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.drawBBox(\"" + name + "\", \"" + path + "\");'))";
                webview.loadUrl(url);
            }
        });
    }

    /**
     * 이미지 오버레이(경계값 삽입 이미지)
     *
     * @param webview,left,bottom,right,top,name,path CordovaWebView,double,double,double,double,String,String
     * @author JiJungKeun
     */
    public void addBoundaryImage(final CordovaWebView webview, final double left, final double bottom, final double right, final double top, final String name, final String path) {

        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {

                String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.addImageByBoundary(\"" + path + "\"," + left + "," + bottom + "," + right + "," + top + ", \"" + name + "\");'))";
                webview.loadUrl(url);
            }
        });
    }

    /**
     * 이미지 오버레이(AOI 삽입 이미지)
     *
     * @param webview,left,bottom,right,top,name,path CordovaWebView,double,double,double,double,String,String
     * @author JiJungKeun
     */
    public void addAOIImage(final CordovaWebView webview, final double left, final double bottom, final double right, final double top, final String name, final String path) {

        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {

                String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.addImageByAOI(\"" + path + "\"," + left + "," + bottom + "," + right + "," + top + ", \"" + name + "\");'))";
                webview.loadUrl(url);
            }
        });
    }

    /**
     * 이미지 오버레이(지도상 이미지 삭제)
     *
     * @param webview,path CordovaWebView,String
     * @author JiJungKeun
     */
    public void deleteImage(final CordovaWebView webview, final String path) {

        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {

                String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.deleteImage(\"" + path + "\");'))";
                webview.loadUrl(url);
            }
        });
    }

    /**
     * 이미지 오버레이(투명도 조절)
     *
     * @param webview,path,opacityValue CordovaWebView,String,double
     * @author JiJungKeun
     */
    public void setImageOpacity(final CordovaWebView webview, final String path, final double opacityValue) {

        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {

                String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.setImageOpacity(\"" + path + "\"," + opacityValue + ");'))";
                webview.loadUrl(url);
            }
        });
    }

    /**
     * 베이스 레이어 추가/변경
     *
     * @param webview,layerName CordovaWebView,String
     * @author JiJungKeun
     */
    public void baseLayers(final CordovaWebView webview, final String layerName) {

        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {

                String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.BaseLayers.setBaseLayer(\"" + layerName + "\");'))";
                webview.loadUrl(url);
            }
        });
    }

    /**
     * 지도상 검수 결과 표시 삭제
     *
     * @param webview CordovaWebView
     * @author JiJungKeun
     */
    public void removeErrorMarking(final CordovaWebView webview) {

        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {

                String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.Validator.removeErrorMarking();'))";
                webview.loadUrl(url);
            }
        });
    }

    /**
     * 에러 네비게이터
     *
     * @param webview,layerId,fid CordovaWebView,String,String
     * @author JiJungKeun
     */
    public void navigateFeature(final CordovaWebView webview, final String layerId, final String fid) {

        AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
            @Override
            public void run() {

                String url = "javascript:app.waitForArbiterInit(new Function('"
                        + "Arbiter.Validator.navigateFeature(\"" + layerId + "\",\"" + fid + "\")'))";

                webview.loadUrl(url);
            }
        });
    }
}

