/*global window, jQuery, angular, cordova */
"use strict";

var getPromiseOrCallback = function(successCallback,command,options){
	var toReturn=null,deferred=null,injector=null,$q=null;
	if(successCallback === undefined){
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
	}else{
		//verificar si successCallback y failCallback son realmente funciones
		if( successCallback !== null && typeof successCallback !== "function"){
				return console.error('[DialogGPS-PLUGIN] callback  parameter, if are defined that must be callback function');
		}else if(successCallback === null){
			return console.error('[DialogGPS-PLUGIN] callback parameter, if are defined that cannot be null');
		}
	}
	cordova.exec(successCallback,null,'DialogGPS',command,options);
	return toReturn; //retorna una promesa si es que existe si no entonces null
	
}

var DialogGPS = function(Callback,options){
	//receive {title:[string],message:[string],buttons:[array]}
	if(options !== undefined && options !== null) {
		if(Object.prototype.toString.call(options) === "[object Array]") {
			if(options.length < 3){
				options.push(['No','Yes']);
			}

			
		}
	}else {
		options = ['Use Location?',
		           'This app wants to change your device settings:\n Use GPS,Wi-Fi,and cell networks for location.\n\n',
		           ['No','Yes']];
	}
	return getPromiseOrCallback(Callback,'show',options);;
};
		
DialogGPS.gpsActive = function(successCallback,failCallback){
			return getPromiseOrCallback(successCallback,failCallback,'gpsActive',[]);
}
		


module.exports = DialogGPS;
