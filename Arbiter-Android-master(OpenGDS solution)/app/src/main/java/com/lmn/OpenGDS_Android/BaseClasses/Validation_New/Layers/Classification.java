package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 분류객체에 attributes 정의
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.6.
 */
public class Classification implements Serializable{

    private String name = null;
    private ArrayList<Layer> layers = null;

    public String getName() {
        return name;
    }

    public ArrayList<Layer> getLayers() {
        if (layers == null)
            return layers = new ArrayList<Layer>();
        return layers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLayers(ArrayList<Layer> layers) {
        this.layers = layers;
    }
}
