Arbiter.Validator = (function () {
    return {

        /* Make GeoJSON format object and send it to OpenGDS/Validator Server */
        startValidation: function (attributeJsonArr, qaOptionJsonArr, layerNameArr, FIDArr) {
            // Loading UI
            Android.StartValidationProgressDialog();

            var map = Arbiter.Map.getMap();
            var index;
            var aoiLayer;
            var aoiFeature;
            var multiPolygonObject = new OpenLayers.Geometry.MultiPolygon();
            var geoJsonFormat = new OpenLayers.Format.GeoJSON();
            var featureVector = new OpenLayers.Feature.Vector();
            var extentLayer = new OpenLayers.Layer.Vector();

            for (var i = 0; i < map.layers.length; i++) {
                if (map.layers[i].name == "aoi")
                    index = i;
            }

            aoiLayer = map.layers[index];
            aoiFeature = aoiLayer.getFeaturesByAttribute()[0];
            multiPolygonObject.addComponent(aoiFeature.geometry);
            geoJsonFormat.parseFeature(multiPolygonObject);
            featureVector.geometry = multiPolygonObject;
            extentLayer.addFeatures(featureVector);

            // call aoi extent geoJson format.
            var extentValue = geoJsonFormat.write(extentLayer.getFeaturesByAttribute());
            var extentObj = JSON.parse(extentValue);
            var i = 0;
            var validationGeoJson;
            var layers = {};

            extentObj.features[0].id = "validExtent";

            while (map.layers[i] != null) {
                if (map.layers[i].name.indexOf("wfs") != -1) {
                    for (var j = 0; j < FIDArr.length; j++) {
                        if (map.layers[i].features[0].fid.indexOf(FIDArr[j]) != -1) {
                            layers[layerNameArr[j]] = {
                                feature: geoJsonFormat.write(map.layers[i].getFeaturesByAttribute()),
                                attribute: attributeJsonArr[j],
                                qaOption: qaOptionJsonArr[j],
                                weight: 0
                            };
                        }
                    }
                }
                i++;
            }

            validationGeoJson = {extent: JSON.stringify(extentObj), layers};
            console.log("validationGeoJson : " + validationGeoJson, validationGeoJson);

            // Send to Validator Server
            Arbiter.Validator.sendObjectRequest("http://175.116.181.39:8889/opengds/validator/validate.ajax", validationGeoJson, Arbiter.Validator.doneCallback);
        },

        // 확장된 아비터 검수 함수
        start: function (attributeJsonArr) {
            Android.StartValidationProgressDialog();
            console.log("validationGeoJson : " + attributeJsonArr);
            // Send to Validator Server
            Arbiter.Validator.sendObjectRequest("http://175.116.181.32:8089/geodt/mobile/validate.do", attributeJsonArr, Arbiter.Validator.doneCallback);
        },

        sendObjectRequest: function (url, params, doneCallback) {
            var deferredObj =
                $.ajax({
                    url: url,
                    type: "POST",
                    dataType: "json",
                    contentType: "application/json; charset=UTF-8",
                    cache: false,
                    data: JSON.stringify(params),
                    traditional: true
                });
            deferredObj.done(function (data, textStatus, jqXHR) {
                Arbiter.Validator.processDone(data, textStatus, jqXHR, doneCallback);
            });
            deferredObj.fail(function (jqXHR, textStatus, errorThrown) {
                Arbiter.Validator.processFail(jqXHR, textStatus, errorThrown);
            });
            return deferredObj;
        },

        processDone: function (data, textStatus, jqXHR, doneCallback) {
            if (typeof(data) !== 'undefined' && typeof(data.errorCode) !== 'undefined') {
                alertPopup("Inform", data.errorDesc);
            } else if (typeof(doneCallback) !== 'undefined') {
                doneCallback(data);
            }
        },

        processFail: function (jqXHR, textStatus, errorThrown) {
            Android.DismissValidationProgressDialog();

            if (typeof (console) !== 'undefined' && typeof (console.log) !== 'undefined') {
                console.log(textStatus + " - " + jqXHR.status + " (" + errorThrown + ")");
            }
            if (jqXHR.status == 500) {
                alert("Internal System Error");
            } else if (jqXHR.status == 404) {
                alert("Not Found / Wrong path");
            } else if (jqXHR.status == 408) {
                alert("Please try again in a few seconds");
            }
            if (jqXHR.getResponseHeader("SESSION_EXPIRED") != null) {
                alert("Session Expired");
            }
        },

        doneCallback: function (result) {
            Android.DismissValidationProgressDialog();
            console.log("Validation result from Validator Server" + JSON.stringify(result), JSON.stringify(result));

            // Show Error marking if result has validation error data
            if (result !== null)
                Arbiter.Validator.resultErrorMarking(result);

            // Save validation result for Error Report
            if (result !== null)
                Android.SaveValidationResult(JSON.stringify(result), true);
            else
                Android.SaveValidationResult(JSON.stringify(result), false);
        },

        // Show marking point if error exists
        resultErrorMarking: function (result) {

            Android.CreatingErrorMarkingProgressDialog();

            var map = Arbiter.Map.getMap();
            var layer;
            var errorResults = result.features;
            var epsg4326 = new OpenLayers.Projection('EPSG:4326');
            var epsg900913 = new OpenLayers.Projection('EPSG:900913');
            //var errorResults = result.DetailsReport;
            var markingPoints = new Array();
            var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;

            renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;

            for (var i = 0; i < errorResults.length; i++) {
            var str = errorResults[i].properties.the_geom;
                markingPoints[i] = new OpenLayers.Feature.Vector(
                    new OpenLayers.Geometry.Point(
                        str.substring(str.indexOf("(")+1, str.lastIndexOf(" ")), str.substring(str.lastIndexOf(" "), str.indexOf(")"))
                       ).transform(epsg4326, epsg900913));

            }

            var i = 0;
            while (map.layers[i] != null) {
                if (map.layers[i].name == "ErrorMarking") {
                    layer = map.layers[i];
                }
                i++;
            }

            if (layer == null) {
                layer = new OpenLayers.Layer.Vector('ErrorMarking', {
                    styleMap: new OpenLayers.StyleMap({
                        pointRadius: map.getResolution() + 5,
                        fillOpacity: 0,
                        strokeColor: "#FF0000"
                    }),
                    renderers: renderer
                });
            }

            else {
                layer.removeAllFeatures();
            }
            layer.addFeatures(markingPoints);
            map.addLayer(layer);

            Android.DismissValidationProgressDialog();
        },

        /* Remove Error marking */
        removeErrorMarking: function () {
            var map = Arbiter.Map.getMap();
            var i = 0;

            while (map.layers[i] != null) {
                if (map.layers[i].name == "ErrorMarking") {
                    map.removeLayer(map.layers[i]);
                    continue;
                }
                i++;
            }
        },

        /* Error Navigator for finding error feature */
        navigateFeature: function (layerId, fid) {
            var map = Arbiter.Map.getMap();
            var layer = Arbiter.Layers.getLayerById(layerId, Arbiter.Layers.type.WFS);
            var feature = layer.getFeatureByFid(fid);
            var controlPanel = Arbiter.Controls.ControlPanel;

            controlPanel.unselect();

            if (Arbiter.Util.existsAndNotNull(feature)) {
                feature.geometry.calculateBounds();
                var bounds = feature.geometry.getBounds();
                var zoomForExtent = map.getZoomForExtent(bounds);

                if (zoomForExtent > 18) {
                    var centroid = feature.geometry.getCentroid();
                    map.setCenter(new OpenLayers.LonLat(centroid.x, centroid.y), 18);
                }
                else {
                    map.zoomToExtent(bounds);
                }
                controlPanel.select(feature);
            }
        }
    };
})();