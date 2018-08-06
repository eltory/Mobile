package com.lmn.OpenGDS_Android.BaseClasses.Validation_New;

import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers.Classification;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.DetailOptions;
import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.GeoServer.ServerInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 모든 검수 데이터 통합 객체
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.11.
 */
public class ValidationAll implements Serializable {
    private ArrayList<Classification> layers;
    private ServerInfo geoserver;
    private DetailOptions options;

    public void setLayers(ArrayList<Classification> layers) {
        this.layers = layers;
    }

    public void setGeoserver(ServerInfo geoserver) {
        this.geoserver = geoserver;
    }

    public void setOptions(DetailOptions options) {
        this.options = options;
    }

    public DetailOptions getOptions() {
        return options;
    }

    public ArrayList<Classification> getLayers() {
        return layers;
    }

    public ServerInfo getGeoserver() {
        return geoserver;
    }
}
