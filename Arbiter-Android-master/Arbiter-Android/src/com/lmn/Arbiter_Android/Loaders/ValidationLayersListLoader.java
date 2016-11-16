package com.lmn.Arbiter_Android.Loaders;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.lmn.Arbiter_Android.ArbiterProject;
import com.lmn.Arbiter_Android.BaseClasses.Validation;
import com.lmn.Arbiter_Android.DatabaseHelpers.FeatureDatabaseHelper;
import com.lmn.Arbiter_Android.DatabaseHelpers.ProjectDatabaseHelper;
import com.lmn.Arbiter_Android.ProjectStructure.ProjectStructure;

import org.json.JSONObject;

import java.util.ArrayList;


public class ValidationLayersListLoader implements BaseColumns {

    private SQLiteDatabase featuresDb;
    private SQLiteDatabase projectDb;
    private String projectName;
    private String path;
    private Activity activity;
    private ArrayList<String> feature_table_name = new ArrayList<String>();

    public static final String LAYERS_TABLE_NAME = "layers";
    public static final String LAYER_TITLE = "layer_title";
    // Feature type with prefix ex. geonode:roads
    public static final String FEATURE_TYPE = "feature_type";
    public static final String SERVER_ID = "server_id";
    public static final String BOUNDING_BOX = "bbox";
    public static final String COLOR = "color";
    public static final String LAYER_VISIBILITY = "visibility";
    public static final String WORKSPACE = "workspace";
    public static final String LAYER_ORDER = "layerOrder";
    public static final String READ_ONLY = "readOnly";


    public static final String GEOMETRY_COLUMNS_TABLE_NAME = "geometry_columns";
    public static final String FEATURE_TABLE_NAME = "feature_table_name";
    public static final String FEATURE_GEOMETRY_COLUMN = "feature_geometry_column";
    public static final String FEATURE_GEOMETRY_TYPE = "feature_geometry_type";
    public static final String FEATURE_GEOMETRY_SRID = "feature_geometry_srid";
    public static final String FEATURE_ENUMERATION = "feature_enumeration";

    public static final String FID = "fid";
    public static final String ARBITER_ID = "arbiter_id";


    //DB Connection
    public ValidationLayersListLoader(Activity activity) {

        projectName = ArbiterProject.getArbiterProject().getOpenProject(activity);
        path = ProjectStructure.getProjectPath(projectName);
        this.activity = activity;

        featuresDb = FeatureDatabaseHelper.getHelper(activity.getApplicationContext(), path, false).getWritableDatabase();
        projectDb = ProjectDatabaseHelper.getHelper(activity.getApplicationContext(), path, false).getWritableDatabase();

    }

    //For getting each Feature Table Name
    public ArrayList<String> getFeature_table_name() {
        String[] columns = {_ID, // 0
                FEATURE_TABLE_NAME, // 1
                FEATURE_GEOMETRY_COLUMN, // 2
                FEATURE_GEOMETRY_TYPE, // 3
                FEATURE_GEOMETRY_SRID, // 4
                FEATURE_ENUMERATION // 5
        };

        String orderBy = FEATURE_TABLE_NAME + " COLLATE NOCASE";

        Cursor cursor = featuresDb.query(GEOMETRY_COLUMNS_TABLE_NAME, columns, null,
                null, null, null, orderBy);

        feature_table_name.clear();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            feature_table_name.add(cursor.getString(1));
        }

        cursor.close();

        return feature_table_name;
    }

    //For Error Navigator to get layerID
    public String getLayerID(String errorFeatureFID)
    {
        String layer_title = errorFeatureFID.split("\\.")[0];
        String layerID = "";

        String[] project_columns = {
                 _ID // 0
        };

        String whereClauseInProject = LAYER_TITLE + "=?";
        String[] whereArgsInProject = {
                layer_title
        };

        String project_orderBy = LAYER_ORDER + " DESC";

        Cursor project_cursor = projectDb.query(LAYERS_TABLE_NAME, project_columns, whereClauseInProject, whereArgsInProject, null, null, project_orderBy);


        for(project_cursor.moveToFirst(); !project_cursor.isAfterLast(); project_cursor.moveToNext()) {
            layerID = project_cursor.getString(0);
        }

        project_cursor.close();

        return layerID;
    }

    //Check Vector Data if exist in AOI
    public boolean existInAOI(String feature_table_name)
    {
        Cursor cursor = featuresDb.query(feature_table_name, null, null,
                null, null, null, null);

        if(cursor.moveToFirst() == false)
            return false;

        else
            return true;
    }

    // Input DB data to Validation object
    public Validation setLayerInfo(String table_name)
    {
        Validation validationLayer = new Validation();

        //get validation layer information from project.db
        String[] project_columns = {
                LAYERS_TABLE_NAME + "." + _ID, // 0
                FEATURE_TYPE, // 1
                WORKSPACE, // 2
                SERVER_ID, // 3
                LAYER_TITLE, // 4
                BOUNDING_BOX, // 5
                COLOR, // 6
                LAYER_ORDER, // 7
                LAYER_VISIBILITY, // 8
                READ_ONLY // 9
        };

        String whereClauseInProject = LAYER_TITLE + "=?";
        String[] whereArgsInProject = {
                table_name
        };

        String project_orderBy = LAYER_ORDER + " DESC";

        Cursor project_cursor = projectDb.query(LAYERS_TABLE_NAME, project_columns, whereClauseInProject, whereArgsInProject, null, null, project_orderBy);


        for(project_cursor.moveToFirst(); !project_cursor.isAfterLast(); project_cursor.moveToNext()) {
            validationLayer.setLayerTitle(project_cursor.getString(4));
            validationLayer.setLayerId(project_cursor.getString(0));
            validationLayer.setFeatureType(project_cursor.getString(1));
            validationLayer.setGeoServerId(project_cursor.getString(3));
        }

        project_cursor.close();


        //get validation layer information from features.db
        String[] features_columns = {_ID, // 0
                FEATURE_TABLE_NAME, // 1
                FEATURE_GEOMETRY_COLUMN, // 2
                FEATURE_GEOMETRY_TYPE, // 3
                FEATURE_GEOMETRY_SRID, // 4
                FEATURE_ENUMERATION // 5
        };

        String whereClauseInFeature = FEATURE_TABLE_NAME + "=?";
        String[] whereArgsInFeature = {
                table_name
        };

        String features_orderBy = FEATURE_TABLE_NAME + " DESC";

        Cursor features_cursor = featuresDb.query(GEOMETRY_COLUMNS_TABLE_NAME, features_columns, whereClauseInFeature,
                whereArgsInFeature, null, null, features_orderBy);

        for(features_cursor.moveToFirst(); !features_cursor.isAfterLast(); features_cursor.moveToNext()) {
            validationLayer.setFeatureTableName(features_cursor.getString(1));
            validationLayer.setFeatureGeometryColumn(features_cursor.getString(2));
            validationLayer.setFeatureGeometrySrid(features_cursor.getString(4));
            validationLayer.setFeatureEnumeration(features_cursor.getString(5));

            if(features_cursor.getString(3).equalsIgnoreCase("SURFACE"))
                validationLayer.setFeatureGeometryType("Polygon");

            else if(features_cursor.getString(3).equalsIgnoreCase("MULTISURFACE"))
                validationLayer.setFeatureGeometryType("MultiPolygon");

            else
                validationLayer.setFeatureGeometryType(features_cursor.getString(3));
        }

        features_cursor.close();

        //get first FID from each layers to distinguish in js
        String firstFid="";
        String[] fid_columns = {
                FID, //0
                ARBITER_ID //1
        };

        String fid_orderBy = ARBITER_ID + " ASC";

        Cursor fid_cursor = featuresDb.query(table_name, fid_columns, null,
                null, null, null, fid_orderBy);

        for(fid_cursor.moveToFirst(); !fid_cursor.isAfterLast(); fid_cursor.moveToNext()) {
            firstFid = fid_cursor.getString(0);
            break;
        }

        validationLayer.setFeatureFid(firstFid);

        fid_cursor.close();

        //get Attribute names
        Cursor attributes_cursor = featuresDb.query(table_name, null, null, null, null, null, null);

        ArrayList<String> attributeArr = new ArrayList<String>();
        for(int i=5; i<attributes_cursor.getColumnCount(); i++) {
            attributeArr.add(attributes_cursor.getColumnName(i));
        }
        validationLayer.setAttributes(attributeArr);

        attributes_cursor.close();

        //get Attribute Types
        ArrayList<String> attributeTypeArr = new ArrayList<String>();
        try {
            JSONObject json = new JSONObject(validationLayer.getFeatureEnumeration());
            for (int i = 0; i < attributeArr.size(); i++)
            { attributeTypeArr.add(json.getJSONObject(attributeArr.get(i)).getString("type").split(":")[1]); }

            validationLayer.setAttributeTypes(attributeTypeArr);
        }catch (Exception e) {e.printStackTrace();}

        return validationLayer;
    }

}
