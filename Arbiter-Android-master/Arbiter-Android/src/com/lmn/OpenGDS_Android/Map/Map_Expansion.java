package com.lmn.OpenGDS_Android.Map;


import android.util.Log;

import com.lmn.Arbiter_Android.Activities.MapChangeHelper;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoading;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoadingJob;

import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
			map = new Map_Expansion();
		}
		return map;
	}

	// Connection with CoordinateDialog.java for finding specific area
	public void findArea(final CordovaWebView webview, double latitude, double longitude) {

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
				String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.findme = new Arbiter.FindMe("+getMap+","+aoi+"); Arbiter.findme._zoom("+position+");'))";

				webview.loadUrl(url);

			}
		});
	}

	public void addDrawImage(final CordovaWebView webview, String name, String path) {

		AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
			@Override
			public void run() {

				String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.drawBBox(\"" + name + "\", \"" + path + "\");'))";
				webview.loadUrl(url);
			}
		});
	}

	public void addBoundaryImage(final CordovaWebView webview, double left, double bottom, double right, double top, String name, String path) {

		AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
			@Override
			public void run() {

				String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.addImageByBoundary(\""+path+"\","+left+","+bottom+","+right+","+top+", \"" + name + "\");'))";
				webview.loadUrl(url);
			}
		});
	}

	public void addAOIImage(final CordovaWebView webview, double left, double bottom, double right, double top, String name, String path) {

		AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
			@Override
			public void run() {

				String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.addImageByAOI(\""+path+"\","+left+","+bottom+","+right+","+top+", \"" + name + "\");'))";
				webview.loadUrl(url);
			}
		});
	}

	public void deleteImage(final CordovaWebView webview, String path) {

		AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
			@Override
			public void run() {

				String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.deleteImage(\""+path+"\");'))";
				webview.loadUrl(url);
			}
		});
	}

	public void setImageOpacity(final CordovaWebView webview, String path, double opacityValue) {

		AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
			@Override
			public void run() {

				String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.ImageLayer.setImageOpacity(\""+path+"\","+opacityValue+");'))";
				webview.loadUrl(url);
			}
		});
	}

	public void baseLayers(final CordovaWebView webview, String layerName) {

		AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
			@Override
			public void run() {

				String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.BaseLayers.setBaseLayer(\"" + layerName + "\");'))";
				webview.loadUrl(url);
			}
		});
	}

	public void removeErrorMarking(final CordovaWebView webview) {

		AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
			@Override
			public void run() {

				String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.Validator.removeErrorMarking();'))";
				webview.loadUrl(url);
			}
		});
	}


	//Navigating error feature
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

