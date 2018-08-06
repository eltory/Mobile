package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition;

public class FigureAttribute {
    private String key = null;
    private Double number = null;
    private String values = null;
    private Double interval = null;
    private String condition = null;

    public void setKey(String key) {
        this.key = key;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public void setInterval(Double interval) {
        this.interval = interval;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getKey() {
        return key;
    }

    public Double getNumber() {
        return number;
    }

    public String getValues() {
        return values;
    }

    public Double getInterval() {
        return interval;
    }

    public String getCondition() {
        return condition;
    }
}
