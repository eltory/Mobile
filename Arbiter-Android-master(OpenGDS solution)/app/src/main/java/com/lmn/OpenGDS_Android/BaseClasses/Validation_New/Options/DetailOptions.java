package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options;

import com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition.OptionsDefinition;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 세부 Options 통합 객체
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.16.
 */
public class DetailOptions implements Serializable {
    private String border = null;
    private ArrayList<OptionsDefinition> definition = null;

    public void setBorder(String border) {
        this.border = border;
    }

    public void setDefinition(ArrayList<OptionsDefinition> definition) {
        this.definition = definition;
    }

    public ArrayList<OptionsDefinition> getDefinition() {
        if(this.definition == null)
            return definition = new ArrayList<OptionsDefinition>();
        return definition;
    }

    public String getBorder() {
        return border;
    }

    public void addOption(OptionsDefinition option){
        getDefinition().add(option);
    }
}
