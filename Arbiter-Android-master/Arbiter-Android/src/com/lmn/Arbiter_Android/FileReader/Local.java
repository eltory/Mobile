package com.lmn.Arbiter_Android.FileReader;

import android.util.Log;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.GeoJSONObject;
import com.cocoahero.android.geojson.Point;
import com.cocoahero.android.geojson.Polygon;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoading;
import com.lmn.Arbiter_Android.AppFinishedLoading.AppFinishedLoadingJob;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import diewald_shapeFile.files.dbf.DBF_Field;
import diewald_shapeFile.files.dbf.DBF_File;
import diewald_shapeFile.files.shp.SHP_File;
import diewald_shapeFile.files.shp.shapeTypes.ShpMultiPoint;
import diewald_shapeFile.files.shp.shapeTypes.ShpPoint;
import diewald_shapeFile.files.shp.shapeTypes.ShpPolyLine;
import diewald_shapeFile.files.shp.shapeTypes.ShpPolygon;
import diewald_shapeFile.files.shp.shapeTypes.ShpShape;
import diewald_shapeFile.files.shx.SHX_File;
import diewald_shapeFile.shapeFile.ShapeFile;

/**
 * Created by master on 2016-05-19.
 */
public class Local {

    //GEOJSON RESULT
    JSONObject geoJSON = new JSONObject();

    JSONArray featuresJSONArray = new JSONArray();

    JSONObject featureJSONObject = new JSONObject();

    JSONArray geometriesJSONArray = new JSONArray();

    JSONObject geometryJSONObject = new JSONObject();
    JSONObject propertiesJSONObject = new JSONObject();

    //JSONARRAY FOR NESTED COORDINATES
    JSONArray coordinate = new JSONArray();
    JSONArray coordinates = new JSONArray();
    JSONArray coordinatesArray = new JSONArray();

    //JSON OBJECT FOR EACH TYPE
    JSONObject polygonJSONObject = new JSONObject();
    JSONObject lineStringJSONObject = new JSONObject();
    JSONObject pointJSONObject;

    //NULL OBJECT FOR PROPERTIES
    JSONObject nullObject = new JSONObject();


    private double centerPointLon = 0;
    private double centerPointLat = 0;

    public Local(final CordovaWebView webview, String fileName)
    {
        double points[][] = {};
        double point[]={};

        DBF_File.LOG_INFO           = !false;
        DBF_File.LOG_ONLOAD_HEADER  = false;
        DBF_File.LOG_ONLOAD_CONTENT = false;

        SHX_File.LOG_INFO           = !false;
        SHX_File.LOG_ONLOAD_HEADER  = false;
        SHX_File.LOG_ONLOAD_CONTENT = false;

        SHP_File.LOG_INFO           = !false;
        SHP_File.LOG_ONLOAD_HEADER  = false;
        SHP_File.LOG_ONLOAD_CONTENT = false;

        try {
            // LOAD SHAPE FILE (.shp, .shx, .dbf)
            Log.d("fileNamedd",fileName);
            ShapeFile shapefile = new ShapeFile("/storage/emulated/0/DCIM/", fileName).READ();


            //Test various type of shp file
            //kz_astana_streets : PolyLine epsg43
            //KOR_roads : PolyLine
            //kz_astana_buildings : Polygon epsg
            //N3A_A0010000 : Polygon
            //N3A_A0053326 : Polygon
            //N3L_F0030000 : PolyLine
            //N3P_F0057215 :Point
            //N3A_A0070000 : Polygon
            //N3A_A0080000 : Polygon
            //N3A_B0010000 : Polygon
            //N3L_A0010000 : PolyLine
            //N3L_A0020000 : PolyLine
            //N3L_A0033320 : PolyLine
            //N3L_B0020000 : PolyLine
            //N3L_F0010000 : PolyLine
            //N3L_F0040000 : PolyLine
            //N3P_F0020000 : Point


            // TEST: printing some content
            ShpShape.Type shape_type = shapefile.getSHP_shapeType();
            Log.d("Shape_type", "shape type : " + shape_type);

            int number_of_shapes = shapefile.getSHP_shapeCount();
            int number_of_fields = shapefile.getDBF_fieldCount();

            Log.d("NUMBER OF SHAPES", "NUMBER OF SHAPES  :" + number_of_shapes);
            Log.d("NUMBER OF FIELDS", "NUMBER OF FIELDS  :" + number_of_fields);


            //number_of_shapes
            for(int i = 0; i < 50; i++){

                if(shape_type.toString().equals("Polygon")){
                    ShpPolygon shape = shapefile.getSHP_shape(i);
                    points = shape.getPoints();

                    polygonJSONObject = new JSONObject();
                    coordinate = new JSONArray();
                    coordinates = new JSONArray();

                    if(i==0)
                        propertiesJSONObject.put("properties", nullObject);

                }

                else if(shape_type.toString().equals("PolyLine")){
                    ShpPolyLine shape = shapefile.getSHP_shape(i);
                    points = shape.getPoints();

                    lineStringJSONObject = new JSONObject();
                    coordinate = new JSONArray();
                    coordinates = new JSONArray();

                    if(i==0)
                        propertiesJSONObject.put("properties", nullObject);

                }

                else if(shape_type.toString().equals("MultiPoint")){
                    ShpMultiPoint shape = shapefile.getSHP_shape(i);

                    points = shape.getPoints();

                }

                else if(shape_type.toString().equals("Point")){
                    ShpPoint shape = shapefile.getSHP_shape(i);
                    point = shape.getPoint();

                    pointJSONObject = new JSONObject();
                    coordinate = new JSONArray();

                    coordinate.put(0,point[0]);
                    coordinate.put(1,point[1]);

                    pointJSONObject.put("type","Point");
                    pointJSONObject.put("coordinates", coordinate);

                    //make geometries
                    geometriesJSONArray.put(pointJSONObject);

                    if(i==0)
                    {
                        propertiesJSONObject.put("properties", nullObject);

                        centerPointLon = 11;
                        centerPointLat = 80;

                        /*
                        centerPointLon = point[0];
                        centerPointLat = point[1];
                        */
                    }

                }

                else {
                    Log.d("Error","Not defined type");
                    break;
                }


                if(shape_type.toString().equals("MultiPoint") || shape_type.toString().equals("Polygon") || shape_type.toString().equals("PolyLine")) {
                    //make coordinates JSONArray
                    for (int j = 0; j < points.length; j++) {

                        if(i==0 && j==0)
                        {
                            centerPointLon = points[j][0];
                            centerPointLat = points[j][1];

                        }
                        coordinate.put(0, points[j][0]  * 20037508.34 / 180);

                        points[j][1] = Math.log(Math.tan((90 + points[j][1]) * Math.PI / 360)) / (Math.PI / 20037508.34);
                        points[j][1] = Math.max(-20037508.34, Math.min(points[j][1], 20037508.34));

                        coordinate.put(1, points[j][1]);

                        coordinates.put(j, coordinate);
                    }

                    if(shape_type.toString().equals("Polygon")) {

                        coordinatesArray.put(coordinates);
                        polygonJSONObject.put("type", "Polygon");
                        polygonJSONObject.put("coordinates", coordinatesArray);


                        //make geometries
                        geometriesJSONArray.put(polygonJSONObject);
                    }

                    else if(shape_type.toString().equals("PolyLine")) {

                        coordinatesArray.put(coordinates);
                        lineStringJSONObject.put("type", "MultiLineString");
                        lineStringJSONObject.put("coordinates", coordinatesArray);


                        //make geometries
                        geometriesJSONArray.put(lineStringJSONObject);
                    }
                }


            }

            geometryJSONObject.put("geometries", geometriesJSONArray);
            geometryJSONObject.put("type", "GeometryCollection");

            featureJSONObject.put("type","Feature");
            featureJSONObject.put("properties",propertiesJSONObject.get("properties"));
            featureJSONObject.put("geometry",geometryJSONObject);

            featuresJSONArray.put(featureJSONObject);

            geoJSON.put("features", featuresJSONArray);
            geoJSON.put("type", "FeatureCollection");

            Log.d("geoJSON", "GEOJSON RESULT : " + geoJSON.toString());

            AppFinishedLoading.getInstance().onAppFinishedLoading(new AppFinishedLoadingJob() {
                @Override
                public void run() {
                    String url = "javascript:app.waitForArbiterInit(new Function('Arbiter.Layers.createLocalLayer(" + geoJSON + ","+centerPointLon+","+centerPointLat+");'))";
                    webview.loadUrl(url);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}