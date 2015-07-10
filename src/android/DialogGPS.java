package cl.rmd.cordova;

import java.util.ArrayList;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

public class DialogGPS extends CordovaPlugin {
	public static final String ENABLE_GPS = "Can_Enable_GPS";
	private final Context MainContext = this.cordova.getActivity().getApplicationContext();
	private AlertDialog.Builder builder;
	@Override
	public boolean execute(String action,JSONArray args,CallbackContext callbackContext) throws JSONException {
		try{
			//validar si estan todos los parametros y el callback
			if(args != null && args.length() > 0){
				//validar elementos dentro
				
				JSONObject params = args.getJSONObject(0);
				ArrayList<String> list = new ArrayList<String>();
				JSONArray buttons = (JSONArray) params.get("buttons");
				if(buttons != null && params.getString("title")!= null && params.getString("message")!= null){
					int length = buttons.length();
					for(int i=0;i<length;i++){
						list.add(buttons.getString(i));
					}
					//comportamiento por default (solo redirecciona)
					builder = new AlertDialog.Builder(MainContext);
					builder.setTitle(params.getString("title"));
					builder.setMessage(params.getString("message"));
					builder.setPositiveButton(list.get(0),new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							MainContext.startActivity(i);
						}
					});
					builder.setNegativeButton(list.get(1), new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
						
					});
					builder.create().show();
					callbackContext.success();
					if(ENABLE_GPS.equals(action)){
						//intent to modify GPS state from here
						//create on versions 
					}
				}else{
					new Exception("There are empty properties in the JSONObject.");
				}
				
			}else{
				new Exception("Required arguments are null.");
			}
			
		}catch(Exception e){
			callbackContext.error(e.getMessage());
		}
		return false;
	}
}
