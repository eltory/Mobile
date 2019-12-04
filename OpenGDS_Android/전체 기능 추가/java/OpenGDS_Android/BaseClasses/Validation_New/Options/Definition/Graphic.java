package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition;

import java.io.Serializable;

/**
 * 각 Options 에 들어갈 attribute 객체
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.16.
 */
public class Graphic implements Serializable {
    private String filter = null;
    private String relation = null;
    private Tolerance[] tolerance = null;

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setTolerance(Tolerance tolerance) {
        if (getTolerance() != null)
            this.tolerance[0] = tolerance;
    }

    public String getFilter() {
        return filter;
    }

    public String getRelation() {
        return relation;
    }

    public Tolerance[] getTolerance() {
        if (tolerance == null)
            return tolerance = new Tolerance[1];
        return tolerance;
    }
}
