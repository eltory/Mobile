package com.lmn.OpenGDS_Android.BaseClasses.Validation_New.Options.Definition;

import java.io.Serializable;

/**
 * Options 의 Definition 정의 객체
 *
 * @author LeeSungHwi
 * @version 1.1 2018.7.16.
 */
public class OptionsDefinition implements Serializable {
    private String name = null;
    private DefinitionOptions options = null;

    public OptionsDefinition() {
    }

    public OptionsDefinition(String name) {
        this.name = name;
    }

    public OptionsDefinition(String name, DefinitionOptions options) {
        this.name = name;
        this.options = options;
    }

    public void setOptions(DefinitionOptions options) {
        this.options = options;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DefinitionOptions getOptions() {
        return options;
    }

}
