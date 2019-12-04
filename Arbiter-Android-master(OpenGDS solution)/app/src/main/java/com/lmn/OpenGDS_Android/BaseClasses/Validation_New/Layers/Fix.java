package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Layers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 고정속성객체에 attributes 정의
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.6.
 */
public class Fix implements Serializable {

    private String name = null;
    private String type = null;
    private Double length = null;
    private boolean isnull = false;
    private ArrayList values = null;

    public void setName(String name) {
        this.name = name;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public void setNull(boolean aNull) {
        isnull = aNull;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValues(ArrayList values) {
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public ArrayList getValues() {
        if (values == null)
            return values = new ArrayList();
        return values;
    }

    public boolean isNull() {
        return isnull;
    }

    public Double getLength() {
        return length;
    }

    public String getType() {
        return type;
    }
}
