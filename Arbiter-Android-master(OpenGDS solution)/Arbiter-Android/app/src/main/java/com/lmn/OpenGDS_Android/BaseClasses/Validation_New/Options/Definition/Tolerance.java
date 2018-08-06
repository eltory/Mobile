package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition;

import java.io.Serializable;

/**
 * Tolerance 객체
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.16.
 */
public class Tolerance implements Serializable {
    private String code = null;
    private Double value = null;
    private Double interval = null;
    private String condition = null;

    public void setCode(String code) {
        this.code = code;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setInterval(Double interval) {
        this.interval = interval;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCode() {
        return code;
    }

    public double getValue() {
        return value;
    }

    public String getCondition() {
        return condition;
    }

    public Double getInterval() {
        return interval;
    }
}
