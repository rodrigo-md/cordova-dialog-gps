

var DialogGPS = function(message,description,callback,title,buttons){

	var _title = (title || "Use Location?");
	var _message = (message || "This app wants to change your device settings:");
	var _description = (description || "Use GPS,Wi-Fi, and mobile networks for location.");
	var _buttons = (buttons || ["No","Yes"]);

	if(Object.prototype.toString.call(_buttons) === "[object Array]") {

		cordova.exec(callback,null,'DialogGPS','DISPLAY',[_title,_message,_description,_buttons]);
			
	}
};
		
DialogGPS.gpsActive = function(successCallback,failCallback){
}
		


module.exports = DialogGPS;
