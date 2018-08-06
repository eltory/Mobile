package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 레이어객체에 attributes 정의
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.6.
 */
public class Layer implements Serializable{

    private ArrayList<Fix> fix = null;
    private String code = null;
    private String geometry = null;

    public void setFix(ArrayList<Fix> fix) {
        this.fix = fix;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setGeometryType(String geometry) {
        this.geometry = geometry;
    }

    public ArrayList<Fix> getFix() {
        if (fix == null)
            return fix = new ArrayList<Fix>();
        return fix;
    }

    public String getCode() {
        return code;
    }

    public String getGeometryType() {
        return geometry;
    }
}
