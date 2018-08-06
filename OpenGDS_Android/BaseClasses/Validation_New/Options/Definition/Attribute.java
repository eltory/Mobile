package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Attribute 객체
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.17.
 */
public class Attribute implements Serializable {
    private Figure[] figure = null;
    private String filter = null;
    private String relation = null;

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setFigure(Figure figure) {
        if (getFigure() != null)
            this.figure[0] = figure;
    }

    public String getRelation() {
        return relation;
    }

    public String getFilter() {
        return filter;
    }

    public Figure[] getFigure() {
        if (figure == null)
            return figure = new Figure[1];
        return figure;
    }
}
