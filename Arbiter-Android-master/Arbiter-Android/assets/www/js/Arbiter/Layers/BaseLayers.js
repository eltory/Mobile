Arbiter.BaseLayers = (function() {

	return {

		setBaseLayer : function(layerName){

		Arbiter.BaseLayers.loadBaseLayers();

		var map = Arbiter.Map.getMap();
        var i=0;

        while(map.layers[i] != null)
         {
               if(map.layers[i].name == layerName)
                     {
                           map.setBaseLayer(map.layers[i]);
                           break;
                     }
              i++;
         }

	},


		loadBaseLayers : function(){

		  //apiKey must be changed to own api key.
		   var apiKey = "ApTJzdkyN1DdFKkRAE6QIDtzihNaf6IWJsT-nQ_2eMoO4PN__0Tzhl2-WgJtXFSp";

           var map = Arbiter.Map.getMap();

           var bingRoad = new OpenLayers.Layer.Bing({
                                                       key: apiKey,
                                                       type: "Road"
                                                     });

           var bingAerial = new OpenLayers.Layer.Bing({
                                                       key: apiKey,
                                                       type: "Aerial"
                                                     });

           var bingHybrid = new OpenLayers.Layer.Bing({
                                                       key: apiKey,
                                                       type: "AerialWithLabels"
                                                     });


           map.addLayers([bingRoad, bingAerial, bingHybrid]);
	}
};
})();