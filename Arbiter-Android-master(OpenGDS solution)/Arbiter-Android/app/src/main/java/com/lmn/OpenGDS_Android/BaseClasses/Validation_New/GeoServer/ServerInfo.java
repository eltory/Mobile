package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.GeoServer;

import java.util.ArrayList;

/**
 * 레이어 검수를 위한 서버 정보
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.16.
 */
public class ServerInfo {
    private String url = null;
    private String user = null;
    private String password = null;
    private ArrayList layers = null;

    public void setLayers(ArrayList layers) {
        this.layers = layers;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList getLayers() {
        return layers;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }
}
