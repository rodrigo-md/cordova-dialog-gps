cordova.define("cl.rmd.cordova.dialoggps.DialogGPS", function(require, exports, module) { 
/*global window, jQuery, angular, cordova */
"use strict";

var getPromiseOrCallback = function(successCallbackCallback,failCallbackCallback,command,options){
	var toReturn=deferred=injector=$q=null;
	if(successCallbackCallback === undefined && failCallback === undefined){
		//construccion de promesas en caso de no ingresar callbacks
		if(window.jQuery){
			deferred = jQuery.Deferred();
			toReturn = deferred;
		}else if(window.angular){
			injector = angular.injector(['ng']);
			$q = injector.get('$q');
			deferred = $q.defer();
			toReturn = deferred.promise;
		}else{
			return console.error('[DialogGPS-PLUGIN] dialogGPS plugin either needs a successCallbackCallback and failCallback callback, or jQuery/AngularJS defined for using promises');
		}
		
		successCallback = deferred.resolve;
		failCallback = deferred.reject;
	}else{
		//verificar si successCallback y failCallback son realmente funciones
		if(typeof successCallback !== null && failCallback !== null){
			if(typeof successCallback !== "function" && typeof failCallback !== "function"){
				return console.error('[DialogGPS-PLUGIN] successCallback and failCallback parameters, if are defined they must be callbacks functions');
			}
		}else{
			return console.error('[DialogGPS-PLUGIN] successCallback and failCallback parameters, if are defined they cannot be null');
		}
	}
	
	cordova.exec(successCallback,failCallback,'DialogGPS',command,options);
	return toReturn; //retorna una promesa si es que existe si no entonces null
	
}

var DialogGPS = {
		gpsActive : function(successCallback,failCallback){
			return getPromiseOrCallback(successCallback,failCallback,'gpsActive',[]);
			
		},
		this:function(successCallback,failCallback,options){
			//receive {title:[string],message:[string],buttons:[array]}
			return getPromiseOrCallback(successCallback,failCallback,'show',[{
				title:options.title,
				message:options.message,
				buttons:options.buttons
			}]);
		}
		
		};

}

module.exports = DialogGPS;
