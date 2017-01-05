Arbiter.ImageLayer = (function() {
	var imageArray = new Array(); // Array of json object of each images
	var imgLayerArray = new Array(); // Array of image layers
	return {
		/* Input selected image using DrawBBox */
		drawBBox : function(name, url) {

			var map = Arbiter.Map.getMap();
			var options = {
				isBaseLayer : false,
				visibility : true
			};
			var size = new OpenLayers.Size(1, 1);
			var boundary;
			var imgLayer;
			var drawControls;
			var boxLayer = new OpenLayers.Layer.Vector("Box layer");

			map.addLayer(boxLayer);
			map.addControl(new OpenLayers.Control.MousePosition());

			// Now we can get the bounds or coordinates from a box vector we have drawn
			boxLayer.events.on({
				featuresadded : onFeaturesAdded
			});
			function onFeaturesAdded(event) {
				var bounds = event.features[0].geometry.getBounds();
				var imageLayer = new Object();
				var boundaryObject = new Object();
				var numOfLayers;
				var index = Android.LoadImageDataSize("size") - 1;

				map.removeLayer(boxLayer);

				boundaryObject.left = bounds.left;
				boundaryObject.right = bounds.right;
				boundaryObject.top = bounds.top;
				boundaryObject.bottom = bounds.bottom;

				//Save BBox boundary value in SharedPreference
				Android.SaveImageData("left" + index, bounds.left);
				Android.SaveImageData("right" + index, bounds.right);
				Android.SaveImageData("top" + index, bounds.top);
				Android.SaveImageData("bottom" + index, bounds.bottom);

				imageLayer.name = name;
				imageLayer.path = url;
				imageLayer.boundary = boundaryObject;

				imageArray.push(imageLayer);

				control.deactivate();

				boundary = new OpenLayers.Bounds(bounds.left, bounds.bottom,
						bounds.right, bounds.top);

				imgLayer = new OpenLayers.Layer.Image(name, url, boundary,
						size, options);

				map.addLayer(imgLayer);

				imgLayerArray.push(imgLayer);
			}

			drawControls = {
				box : new OpenLayers.Control.DrawFeature(boxLayer,
						OpenLayers.Handler.RegularPolygon, {
							handlerOptions : {
								sides : 4,
								irregular : true,
							}
						})
			};

			for ( var key in drawControls) {
				map.addControl(drawControls[key]);
				break;
			}

			for (key in drawControls) {
				var control = drawControls[key];
				control.activate();
				break;
			}
		},
		/* Add images as entering boundary value */
		addImageByBoundary : function(url, left, bottom, right, top, name) {
			var map = Arbiter.Map.getMap();
			var options = {
				isBaseLayer : false,
				visibility : true
			};
			var size = new OpenLayers.Size(1, 1);
			var boundary = new OpenLayers.Bounds(left, bottom, right, top);
			var imgLayer;
			var zoomLevel;

			var imageLayer = new Object();
			var boundaryObject = new Object();
			var numOfLayers;

			var index = Android.LoadImageDataSize("size") - 1;

			boundaryObject.left = boundary.left;
			boundaryObject.right = boundary.right;
			boundaryObject.top = boundary.top;
			boundaryObject.bottom = boundary.bottom;

			Android.SaveImageData("left" + index, boundary.left);
			Android.SaveImageData("right" + index, boundary.right);
			Android.SaveImageData("top" + index, boundary.top);
			Android.SaveImageData("bottom" + index, boundary.bottom);

			imageLayer.name = name;
			imageLayer.path = url;
			imageLayer.boundary = boundaryObject;

			imageArray.push(imageLayer);

			imgLayer = new OpenLayers.Layer.Image(name, url, boundary, size,
					options);

			map.addLayer(imgLayer);
			imgLayerArray.push(imgLayer);

			var lonLat = boundary.getCenterLonLat();
			var i = 0;
			while (map.layers[i] != null) {
				if (map.layers[i].name == "aoi") {
					zoomLevel = map.layers[i].map.zoom;
					break;
				}
				i++;
			}

			map.setCenter(lonLat, zoomLevel);
		},
		/* Add images in AOI */
		addImageByAOI : function(url, left, bottom, right, top, name) {
			var map = Arbiter.Map.getMap();
			var options = {
				isBaseLayer : false,
				visibility : true
			};
			var size = new OpenLayers.Size(1, 1);
			var boundary = new OpenLayers.Bounds(left, bottom, right, top);
			var imgLayer;
			var zoomLevel;

			var imageLayer = new Object();
			var boundaryObject = new Object();
			var numOfLayers;

			var index = Android.LoadImageDataSize("size") - 1;

			Android.StartAddAoiImageProgressDialog();

			boundaryObject.left = boundary.left;
			boundaryObject.right = boundary.right;
			boundaryObject.top = boundary.top;
			boundaryObject.bottom = boundary.bottom;

			Android.SaveImageData("left" + index, boundary.left);
			Android.SaveImageData("right" + index, boundary.right);
			Android.SaveImageData("top" + index, boundary.top);
			Android.SaveImageData("bottom" + index, boundary.bottom);

			imageLayer.name = name;
			imageLayer.path = url;
			imageLayer.boundary = boundaryObject;

			imageArray.push(imageLayer);

			imgLayer = new OpenLayers.Layer.Image(name, url, boundary, size,
					options);

			map.addLayer(imgLayer);
			imgLayerArray.push(imgLayer);

			var lonLat = boundary.getCenterLonLat();
			var i = 0;
			while (map.layers[i] != null) {
				if (map.layers[i].name == "aoi") {
					zoomLevel = map.layers[i].map.zoom;
					break;
				}
				i++;
			}
			map.setCenter(lonLat, zoomLevel);

			//Remove Progress Dialog
			Android.DismissAddAoiImageProgressDialog();
		},
		/* Reload the images when map is initialized */
		reloadImages : function() {
			var map = Arbiter.Map.getMap();
			var options = {
				isBaseLayer : false,
				visibility : true
			};
			var size = new OpenLayers.Size(1, 1);
			var boundary;
			var imgLayer;
			var imageLayer;
			var boundaryObject;
			var imageListLength = Android.LoadImageDataSize("size");
			var left, right, bottom, top;
			var name, url;

			for (var i = 0; i < imageListLength; i++) {

				imageLayer = new Object();
				boundaryObject = new Object();

				name = Android.LoadImageData("name" + i);
				url = Android.LoadImageData("path" + i);
				left = Android.LoadImageBoundary("left" + i);
				right = Android.LoadImageBoundary("right" + i);
				top = Android.LoadImageBoundary("top" + i);
				bottom = Android.LoadImageBoundary("bottom" + i);

				boundaryObject.left = left;
				boundaryObject.right = right;
				boundaryObject.top = top;
				boundaryObject.bottom = bottom;

				imageLayer.name = name;
				imageLayer.path = url;
				imageLayer.boundary = boundaryObject;

				imageArray.push(imageLayer);

				boundary = new OpenLayers.Bounds(left, bottom, right, top);
				imgLayer = new OpenLayers.Layer.Image(name, url, boundary,
						size, options);

				map.addLayer(imgLayer);
				imgLayerArray.push(imgLayer);
			}
		},
		/* Delete the selected image layer */
		deleteImage : function(url) {
			var map = Arbiter.Map.getMap();
			var index = -1;

			for (var i = 0; i < imageArray.length; i++) {
				if (imageArray[i].path == url)
					index = i;
			}

			if (index != -1) {
				imageArray.splice(index, index);

				map.removeLayer(imgLayerArray[index]);

				imgLayerArray.splice(index, index);
			}

		},
		/* set image opacity */
		setImageOpacity : function(url, opacityValue) {
			var map = Arbiter.Map.getMap();
			var index = -1;

			for (var i = 0; i < imageArray.length; i++) {
				if (imageArray[i].path == url)
					index = i;
			}

			if (index != -1) {
				imgLayerArray[index].setOpacity(opacityValue);
			}
		}
	};
})();