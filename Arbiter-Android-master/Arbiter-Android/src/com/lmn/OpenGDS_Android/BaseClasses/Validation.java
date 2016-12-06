package com.lmn.OpenGDS_Android.BaseClasses;


import org.json.JSONArray;

import java.util.ArrayList;

/* Class for creating validation data */
public class Validation {

    // get layer information from DB
    private String geoServerId;
    private String layerTitle;
    private String layerId;
    private String featureType;
    private String featureTableName;
    private String featureGeometryColumn;
    private String featureGeometrySrid;
    private String featureGeometryType;
    private String featureEnumeration;
    private String featureFid;
    private ArrayList<String> attributes;
    private ArrayList<String> attributeTypes;

    // validation options
    // -- POLYGON --
    private String smallArea;
    private String entityDuplicated;
    private String selfEntity;
    private String pointDuplicated;
    private String attributeFix;

    // -- LINE --
    private String smallLength;
    private String conIntersected;
    private String conOverDegree;
    private String conBreak;
    private String zValueAmbiguous;
    private String useLessPoint;

    // -- POINT --


    //JSON ARRAY
    private JSONArray entityDuplicatedArr = new JSONArray();
    private JSONArray selfEntityArr = new JSONArray();
    private JSONArray attributeFixArr = new JSONArray();
    private JSONArray zValueAmbiguousArr = new JSONArray();

    // get option name list
    private String[] polygon = new String[]{"SmallArea", "EntityDuplicated", "SelfEntity", "PointDuplicated", "AttributeFix"};
    private String[] line = new String[]{"SmallLength", "EntityDuplicated", "SelfEntity", "PointDuplicated", "ConIntersected", "ConOverDegree", "ConBreak", "AttributeFix",
    "ZValueAmbiguous", "UselessPoint"};
    //private String[] point

    // Select Information
    private boolean checked;

    public Validation()
    {

    }

    public static String buildLayerKey(Validation layer){
        return Integer.valueOf(layer.getGeoServerId()).toString() + ":" +
                layer.getFeatureType();
    }

    //Setter
    public void setGeoServerId(String geoServerId) {this.geoServerId = geoServerId;}
    public void setLayerTitle(String layerTitle)
    {
        this.layerTitle = layerTitle;
    }
    public void setLayerId(String layerId)
    {
        this.layerId = layerId;
    }
    public void setFeatureType(String featureType)
    {
        this.featureType = featureType;
    }
    public void setFeatureTableName(String featureTableName) { this.featureTableName = featureTableName;}
    public void setFeatureGeometryColumn(String featureGeometryColumn) { this.featureGeometryColumn = featureGeometryColumn;}
    public void setFeatureGeometrySrid(String featureGeometrySrid) { this.featureGeometrySrid = featureGeometrySrid; }
    public void setFeatureEnumeration(String featureEnumeration) { this.featureEnumeration = featureEnumeration; }
    public void setFeatureFid(String featureFid) { this.featureFid = featureFid;}
    public void setFeatureGeometryType(String featureGeometryType) { this.featureGeometryType = featureGeometryType; }
    public void setAttributes(ArrayList<String> attributes) { this.attributes = attributes; }
    public void setAttributeTypes(ArrayList<String> attributeTypes) { this.attributeTypes = attributeTypes; }

    public void setSmallArea(String smallArea) { this.smallArea = smallArea; }
    public void setEntityDuplicated(String entityDuplicated) { this.entityDuplicated = entityDuplicated; }
    public void setSelfEntity(String selfEntity) { this.selfEntity = selfEntity; }
    public void setPointDuplicated(String pointDuplicated) { this.pointDuplicated = pointDuplicated; }
    public void setAttributeFix(String attributeFix)
    {
        this.attributeFix = attributeFix;
    }

    public void setSmallLength(String smallLength)
    {
        this.smallLength = smallLength;
    }
    public void setConIntersected(String conIntersected)
    {
        this.conIntersected = conIntersected;
    }
    public void setConOverDegree(String conOverDegree)
    {
        this.conOverDegree = conOverDegree;
    }
    public void setConBreak(String conBreak)
    {
        this.conBreak = conBreak;
    }
    public void setZValueAmbiguous(String zValueAmbiguous) { this.zValueAmbiguous = zValueAmbiguous; }
    public void setUseLessPoint(String useLessPoint)
    {
        this.useLessPoint = useLessPoint;
    }

    public void setSelfEntityArr(String selfEntityArr) {
        if(selfEntityArr != null)
            this.selfEntityArr.put(selfEntityArr);

        else
            this.selfEntityArr = null;
    }
    public void setEntityDuplicatedArr(String entityDuplicatedArr) {
        if(entityDuplicatedArr != null)
            this.entityDuplicatedArr.put(entityDuplicatedArr);

        else
            this.entityDuplicatedArr = null;
    }
    public void setAttributeFixArr(String attributeFixArr) {
        if(attributeFixArr != null)
            this.attributeFixArr.put(attributeFixArr);

        else
            this.attributeFixArr = null;
    }
    public void setzValueAmbiguousArr(String zValueAmbiguousArr) {
        if(zValueAmbiguousArr != null)
            this.zValueAmbiguousArr.put(zValueAmbiguousArr);

        else
            this.zValueAmbiguousArr = null;
    }

    // Getter
    public String getGeoServerId()
    {
        return geoServerId;
    }
    public String getLayerTitle()
    {
        return layerTitle;
    }
    public String getLayerId()
    {
        return layerId;
    }
    public String getFeatureType()
    {
        return featureType;
    }
    public String getFeatureTableName() { return featureTableName; }
    public String getFeatureGeometryColumn()
    {
        return featureGeometryColumn;
    }
    public String getFeatureGeometrySrid()
    {
        return featureGeometrySrid;
    }
    public String getFeatureEnumeration()
    {
        return featureEnumeration;
    }
    public String getFeatureFid() { return featureFid;}
    public String getFeatureGeometryType() { return featureGeometryType; }
    public ArrayList<String> getAttributes() {return attributes;}
    public ArrayList<String> getAttributeTypes() {return attributeTypes;}

    public String getSmallArea()
    {
        return smallArea;
    }
    public String getEntityDuplicated()
    {
        return entityDuplicated;
    }
    public String getSelfEntity()
    {
        return selfEntity;
    }
    public String getPointDuplicated()
    {
        return pointDuplicated;
    }
    public String getAttributeFix()
    {
        return attributeFix;
    }

    public String getSmallLength()
    {
        return smallLength;
    }
    public String getConIntersected()
    {
        return conIntersected;
    }
    public String getConOverDegree()
    {
        return conOverDegree;
    }
    public String getConBreak()
    {
        return conBreak;
    }
    public String getZValueAmbiguous()
    {
        return zValueAmbiguous;
    }
    public String getUseLessPoint()
    {
        return useLessPoint;
    }

    public JSONArray getEntityDuplicatedArr() {return entityDuplicatedArr;}
    public JSONArray getSelfEntityArr() {return selfEntityArr;}
    public JSONArray getAttributeFixArr() {return attributeFixArr;}
    public JSONArray getzValueAmbiguousArr() {return zValueAmbiguousArr;}

    public String[] getPolygonOptionNames()
    {
        return polygon;
    }

    public String[] getLineOptionNames()
    {
        return line;
    }

    public void setChecked(boolean check){
        this.checked = check;
    }
    public boolean isChecked(){
        return this.checked;
    }

    public String callValidationOptionData(String option)
    {
        if(option.equalsIgnoreCase("SmallArea"))
            return getSmallArea();

        else if(option.equalsIgnoreCase("EntityDuplicated"))
            return getEntityDuplicated();

        else if(option.equalsIgnoreCase("SelfEntity"))
            return getSelfEntity();

        else if(option.equalsIgnoreCase("PointDuplicated"))
            return getPointDuplicated();

        else if(option.equalsIgnoreCase("AttributeFix"))
            return getAttributeFix();

        else if(option.equalsIgnoreCase("SmallLength"))
            return getSmallLength();

        else if(option.equalsIgnoreCase("ConIntersected"))
            return getConIntersected();

        else if(option.equalsIgnoreCase("ConOverDegree"))
            return getConOverDegree();

        else if(option.equalsIgnoreCase("ConBreak"))
            return getConBreak();

        else if(option.equalsIgnoreCase("ZValueAmbiguous"))
            return getZValueAmbiguous();

        else if(option.equalsIgnoreCase("UselessPoint"))
            return getUseLessPoint();

        else
            return null;
    }


    public JSONArray callValidationOptionDataArr(String option)
    {

        if(option.equalsIgnoreCase("EntityDuplicated"))
            return getEntityDuplicatedArr();

        else if(option.equalsIgnoreCase("SelfEntity"))
            return getSelfEntityArr();

        else if(option.equalsIgnoreCase("AttributeFix"))
            return getAttributeFixArr();

        else if(option.equalsIgnoreCase("ZValueAmbiguous"))
            return getzValueAmbiguousArr();

        else
            return null;
    }

}
