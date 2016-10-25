Arbiter.Validator = (function() {


  return{

           startValidation : function(attributeJsonArr, qaOptionJsonArr, layerNameArr, FIDArr) {

            var map = Arbiter.Map.getMap();

            var index;
            var aoiLayer;
            var aoiFeature;
            var multiPolygonObject = new OpenLayers.Geometry.MultiPolygon();
            var geoJsonFormat = new OpenLayers.Format.GeoJSON();
            var featureVector = new OpenLayers.Feature.Vector();
            var extentLayer = new OpenLayers.Layer.Vector();


            for(var i=0; i<map.layers.length; i++)
            {
            if(map.layers[i].name == "aoi")
                 index = i;
            }

            aoiLayer = map.layers[index];
            aoiFeature = aoiLayer.getFeaturesByAttribute()[0];
            multiPolygonObject.addComponent(aoiFeature.geometry);
            geoJsonFormat.parseFeature(multiPolygonObject);
            featureVector.geometry = multiPolygonObject;
            extentLayer.addFeatures(featureVector);

            //call aoi extent geojson format.
            var extentValue =  geoJsonFormat.write(extentLayer.getFeaturesByAttribute());
            var extentObj = JSON.parse(extentValue);
            extentObj.features[0].id = "valiExtent";

            var i = 0;
            var validationGeoJson;
            var layers = { };

            while(map.layers[i] != null)
            {
            if(map.layers[i].name.indexOf("wfs") != -1)
            {
              for(var j=0; j<FIDArr.length; j++)
              {
              if(map.layers[i].features[0].fid.indexOf(FIDArr[j]) != -1)
              {
               layers[layerNameArr[j]] = { feature : geoJsonFormat.write(map.layers[i].getFeaturesByAttribute()), attribute : attributeJsonArr[j], qaOption : qaOptionJsonArr[j], weight : 0 };
               console.log("adas" + layerNameArr[j], layerNameArr[j]);
               console.log("adzzzzas" + attributeJsonArr[j], attributeJsonArr[j]);
              }
              }
            }
            i++;
            }

            validationGeoJson = { extent : JSON.stringify(extentObj), layers };
            console.log("validationGeoJson : " + validationGeoJson, validationGeoJson);

            //send geojson data to validation server
            // CALL BACK METHOD가 오기전까지 비동기처리하여 UI 로딩 생성
            Arbiter.Validator.sendObjectRequest("http://175.116.181.13:8089/opengds/validator/validate.ajax", validationGeoJson, Arbiter.Validator.doneCallback);
        },

        sendObjectRequest : function(url, params, doneCallback)
        {
           var deferredObj =
           		$.ajax({
           		url : url,
           		type : "POST",
           		dataType : "json",
           		contentType : "application/json; charset=UTF-8",
           		cache : false,
           		data : JSON.stringify(params),
           		traditional: true
           	});
           	deferredObj.done(function(data, textStatus, jqXHR) {
           		Arbiter.Validator.processDone(data, textStatus, jqXHR, doneCallback);
           	});
           	deferredObj.fail(function(jqXHR, textStatus, errorThrown) {
           		Arbiter.Validator.processFail(jqXHR, textStatus, errorThrown);
           	});
           	return deferredObj;
        },

        processDone : function(data, textStatus, jqXHR, doneCallback)
        {
          	if (typeof(data) !== 'undefined' && typeof(data.errorCode) !== 'undefined') {
          		alertPopup("Inform",data.errorDesc);
          	} else if (typeof(doneCallback) !== 'undefined') {
          		doneCallback(data);
          	}
        },

        processFail : function(jqXHR, textStatus, errorThrown)
        {
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

        doneCallback : function(result)
        {
          console.log("zsxzsszszzzzzz" + result, result);

          // ERROR 표시 함수 호출
          if(result.Error != false)
          Arbiter.Validator.resultErrorMarking(result);

          // JS INTERFACE 이용하여 자바로 ERROR REPORT DATA 보내기
          // ERROR REPORT 확인후, result 이용하여 ERROR NAVIGATOR 함수 호출

        },

        //show marking point if error exists
        resultErrorMarking : function(result)
        {
           var map = Arbiter.Map.getMap();
           var errorResults = result.DetailsReport;
           var markingPoints = new Array();
           var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;

           renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;

           for(var i=0; i<errorResults.length; i++)
           {
              markingPoints[i] = new OpenLayers.Feature.Vector(
                 new OpenLayers.Geometry.Point(
                      errorResults[i].errCoorX, errorResults[i].errCoorY));
           }

           var layer = new OpenLayers.Layer.Vector('Points', {
                           styleMap: new OpenLayers.StyleMap({
                               pointRadius: map.getResolution()+2,
                               fillOpacity: 0,
                               strokeColor : "#FF0000"
                           }),
                           renderers: renderer
                       });
           layer.addFeatures(markingPoints);

           map.addLayer(layer);
        }
	};
})();