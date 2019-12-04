package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition;

import java.io.Serializable;

/**
 * Figure 객체
 *
 * @author LeeSungHwi
 * @version 1.1 2018.07.17.
 */
public class Figure implements Serializable {
    private String code = null;
    private FigureAttribute[] attribute = null;

    public void setCode(String code) {
        this.code = code;
    }

    public void setAttribute(FigureAttribute attribute) {
        if (getAttribute() != null)
            this.attribute[0] = attribute;
    }

    public String getCode() {
        return code;
    }

    public FigureAttribute[] getAttribute() {
        if (attribute == null)
            return attribute = new FigureAttribute[1];
        return attribute;
    }
}
