Arbiter.ImageLayer = (function() {

/* Add Image in map for getting information from images when user draws features.*/

    var imageArray = new Array();
    var imgLayerArray = new Array();
    var zIndex = 101;

	return {
		drawBBox : function(name, url){
			var options = {isBaseLayer: false, visibility: true};
            var map = Arbiter.Map.getMap();
            var size = new OpenLayers.Size(1,1);
            var boundary;
            var imgLayer;

            var drawControls;

            var boxLayer = new OpenLayers.Layer.Vector("Box layer");

            map.addLayer(boxLayer);
            map.addControl(new OpenLayers.Control.LayerSwitcher());
            map.addControl(new OpenLayers.Control.MousePosition());



             // Now we call an alert to get the bounds or coordinates from a circle or vector we have drawn
             boxLayer.events.on({
                      featuresadded: onFeaturesAdded });

             function onFeaturesAdded(event) {
                      var bounds = event.features[0].geometry.getBounds();
                      var imageLayer = new Object();
                      var boundaryObject = new Object();
                      var numOfLayers;

                      var index = Android.LoadPreferencesSize("size") -1;

                      map.removeLayer(boxLayer);

                      boundaryObject.left = bounds.left;
                      boundaryObject.right = bounds.right;
                      boundaryObject.top = bounds.top;
                      boundaryObject.bottom = bounds.bottom;

                      Android.SavePreferences("left"+index, bounds.left);
                      Android.SavePreferences("right"+index, bounds.right);
                      Android.SavePreferences("top"+index, bounds.top);
                      Android.SavePreferences("bottom"+index, bounds.bottom);

                      imageLayer.name = name;
                      imageLayer.path = url;
                      imageLayer.boundary = boundaryObject;

                      imageArray.push(imageLayer);

                      // show image data in alert
                      /*
                      var answer = "bottom: " + bounds.bottom + "\n";
                      answer += "left: " + bounds.left + "\n";
                      answer += "right: " + bounds.right + "\n";
                      answer += "top: " + bounds.top + "\n";
                      answer += "name: " + name + "\n";
                      answer += "url: " + url + "\n";
                      alert(answer);
                      */


                      control.deactivate();

                      boundary = new OpenLayers.Bounds(bounds.left, bounds.bottom, bounds.right, bounds.top);
                      imgLayer = new OpenLayers.Layer.Image(name, url, boundary, size, options);

                      map.addLayer(imgLayer);

                      for(var i=1; i<map.getNumLayers(); i++)
                      {
                       if(map.layers[i].id.indexOf("Layer_Image") != -1)
                       {
                            map.layers[i].setZIndex(zIndex);
                            zIndex = zIndex + 1;
                       }
                      }

                      zIndex = 101;

                      imgLayerArray.push(imgLayer);
                  }

             drawControls = {
                                box: new OpenLayers.Control.DrawFeature(boxLayer,
                                    OpenLayers.Handler.RegularPolygon, {
                                        handlerOptions: {
                                            sides: 4,
                                            irregular: true,
                                        }
                                    }
                                )
                            };

             for(var key in drawControls) {
                                map.addControl(drawControls[key]);
                            }

                            //map.setCenter(new OpenLayers.LonLat(0, 0), 3);

             for(key in drawControls) {
                                 var control = drawControls[key];
                                 control.activate();

        }
	},

/* redraw the images when map is initialized */
	    reloadImages : function(){
	    var options = {isBaseLayer: false, visibility: true};
        var map = Arbiter.Map.getMap();
        var size = new OpenLayers.Size(1,1);
        var boundary;
        var imgLayer;
        var imageLayer;
        var boundaryObject;


        var imgListLength = Android.LoadPreferencesSize("size");
        var left, right, bottom, top;
        var name, url;

	    for(var i = 0; i < imgListLength; i++){

	           imageLayer = new Object();
	           boundaryObject = new Object();

	           name = Android.LoadPreferences("name"+i);
	           url = Android.LoadPreferences("path"+i);
	           left = Android.LoadPreferencesBoundary("left"+i);
	           right = Android.LoadPreferencesBoundary("right"+i);
	           top = Android.LoadPreferencesBoundary("top"+i);
	           bottom = Android.LoadPreferencesBoundary("bottom"+i);

	           boundaryObject.left = left;
               boundaryObject.right = right;
               boundaryObject.top = top;
               boundaryObject.bottom = bottom;

               imageLayer.name = name;
               imageLayer.path = url;
               imageLayer.boundary = boundaryObject;

               imageArray.push(imageLayer);

               boundary = new OpenLayers.Bounds(left, bottom, right, top);
               imgLayer = new OpenLayers.Layer.Image(name, url, boundary, size, options);


               map.addLayer(imgLayer);
               imgLayerArray.push(imgLayer);
	    }
	    },

/* add images using boundary dialog */
	    addImageByBoundary : function(url, left, bottom, right, top, name){

            var options = {isBaseLayer: false, visibility: true};
            var map = Arbiter.Map.getMap();
            var size = new OpenLayers.Size(1,1);
            var boundary = new OpenLayers.Bounds(left, bottom, right,top).transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
            var imgLayer;


                      var imageLayer = new Object();
                      var boundaryObject = new Object();
                      var numOfLayers;

                      var index = Android.LoadPreferencesSize("size") -1;

                      boundaryObject.left = boundary.left;
                      boundaryObject.right = boundary.right;
                      boundaryObject.top = boundary.top;
                      boundaryObject.bottom = boundary.bottom;

                      Android.SavePreferences("left"+index, boundary.left);
                      Android.SavePreferences("right"+index, boundary.right);
                      Android.SavePreferences("top"+index, boundary.top);
                      Android.SavePreferences("bottom"+index, boundary.bottom);

                      imageLayer.name = name;
                      imageLayer.path = url;
                      imageLayer.boundary = boundaryObject;

                      imageArray.push(imageLayer);


                      imgLayer = new OpenLayers.Layer.Image(name, url, boundary, size, options);

                      map.addLayer(imgLayer);

                       var lonLat = new OpenLayers.LonLat(left, right)
                                                                 .transform(
                                                                     new OpenLayers.Projection("EPSG:4326"),
                                                                     new OpenLayers.Projection("EPSG:900913")
                                                                 );
                                                                 map.setCenter(lonLat, 6);

                      for(var i=1; i<map.getNumLayers(); i++)
                      {
                       if(map.layers[i].id.indexOf("Layer_Image") != -1)
                       {
                            map.layers[i].setZIndex(zIndex);
                            zIndex = zIndex + 1;
                       }
                      }

                      zIndex = 101;

                      imgLayerArray.push(imgLayer);
	    },

/* delete the image layer */
		deleteImage : function(url){
		var map = Arbiter.Map.getMap();
		var index = -1;

    	for(var i = 0; i < imageArray.length; i++){

    	if(imageArray[i].path == url)
    	index = i;

    	}

    	if(index != -1)
    	{
    	imageArray.splice(index,index);

    	map.removeLayer(imgLayerArray[index]);

    	imgLayerArray.splice(index,index);
    	}

    	 for(var i=1; i<map.getNumLayers(); i++)
                              {
                               if(map.layers[i].id.indexOf("Layer_Image") != -1)
                               {
                                    map.layers[i].setZIndex(zIndex);
                                    zIndex = zIndex + 1;
                               }
                              }

              zIndex = 101;

    	}
};
})();