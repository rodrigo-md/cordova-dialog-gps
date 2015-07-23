
var DialogGPS = function(message,callback,title,buttons){

	var _title = (title || "Use Location?");
	var _message = (message || "This app wants to change your device settings:\n Use GPS,Wi-Fi,and cell networks for location.\n\n");
	var _buttons = (buttons || ["No","Yes"]);

	if(Object.prototype.toString.call(_buttons) === "[object Array]") {

		cordova.exec(callback,null,'DialogGPS','DISPLAY',[_title,_message,_buttons]);
			
	}
};
		
DialogGPS.gpsActive = function(successCallback,failCallback){
}
		


module.exports = DialogGPS;
