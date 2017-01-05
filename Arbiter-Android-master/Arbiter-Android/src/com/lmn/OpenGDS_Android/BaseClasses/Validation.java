package com.lmn.OpenGDS_Android.BaseClasses;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Validation 객체에 레이어 정보 및 Q/A 옵션값 저장
 * @author JiJungKeun
 * @version 1.1 2017/01/02
 */

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
    // Point isn't developed until now

    //JSON ARRAY
    private JSONArray entityDuplicatedArr = new JSONArray();
    private JSONArray selfEntityArr = new JSONArray();
    private JSONArray attributeFixArr = new JSONArray();
    private JSONArray zValueAmbiguousArr = new JSONArray();

    //Get Q/A option name list
    private String[] polygon = new String[]{"SmallArea", "EntityDuplicated", "SelfEntity", "PointDuplicated", "AttributeFix"};
    private String[] line = new String[]{"SmallLength", "EntityDuplicated", "SelfEntity", "PointDuplicated", "ConIntersected", "ConOverDegree", "ConBreak", "AttributeFix",
    "ZValueAmbiguous", "UselessPoint"};
    //private String[] point

    //Select Information
    private boolean checked;

    public Validation()
    {
    }

    /**
     * 각 레이어에 고유 Key 부여
     * @author JiJungKeun
     * @param layer Validation
     * @return String
     */
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
        try {
            if (selfEntityArr != null)
                this.selfEntityArr.put(0,selfEntityArr);

            else {
                this.selfEntityArr.put(0, null);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setEntityDuplicatedArr(String entityDuplicatedArr) {
        try {
        if(entityDuplicatedArr != null)
            this.entityDuplicatedArr.put(0,entityDuplicatedArr);

        else
            this.entityDuplicatedArr.put(0,null);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setAttributeFixArr(String attributeFixArr) {
        try {
        if(attributeFixArr != null)
            this.attributeFixArr.put(0,attributeFixArr);

        else
            this.attributeFixArr.put(0,null);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setzValueAmbiguousArr(String zValueAmbiguousArr) {
        try {
        if(zValueAmbiguousArr != null)
            this.zValueAmbiguousArr.put(0,zValueAmbiguousArr);

        else
            this.zValueAmbiguousArr.put(0,null);
        }catch (Exception e) {
            e.printStackTrace();
        }
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

    public JSONArray getEntityDuplicatedArr() {
        try {
            if(entityDuplicatedArr.get(0) == null)
                return null;
            else
                return entityDuplicatedArr;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getSelfEntityArr() {
        try {
            if(selfEntityArr.get(0) == null)
                return null;
            else
                return selfEntityArr;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getAttributeFixArr() {
        try {
            if(attributeFixArr.get(0) == null)
                return null;
            else
                return attributeFixArr;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getzValueAmbiguousArr() {
        try {
            if(zValueAmbiguousArr.get(0) == null)
                return null;
            else
                return zValueAmbiguousArr;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getPolygonOptionNames()
    {
        return polygon;
    }

    public String[] getLineOptionNames()
    {
        return line;
    }

    /**
     * 추가할 레이어 체크
     * @author JiJungKeun
     * @param check boolean
     */
    public void setChecked(boolean check){
        this.checked = check;
    }

    /**
     * 추가할 레이어가 체크 상태인지 확인
     * @author JiJungKeun
     * @return boolean
     */
    public boolean isChecked(){
        return this.checked;
    }

    /**
     * Q/A 옵션 키 값에 상응하는 value 값 리턴
     * @author JiJungKeun
     * @param option String
     * @return String
     */
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

    /**
     * Q/A 옵션 키 값에 상응하는 value 값 리턴
     * @author JiJungKeun
     * @param option String
     * @return JSONArray
     */
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
