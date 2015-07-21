package cl.rmd.cordova;


import android.util.Log;
import android.widget.TextView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class DialogGPS extends CordovaPlugin {
	public static final String ENABLE_GPS = "Can_Enable_GPS";

	@Override
	public boolean execute(String action,JSONArray args,CallbackContext callbackContext) throws JSONException {

			if(this.cordova.getActivity().isFinishing()) return true;		
			if(action.equals("show")) {
				LocationManager  locationManager = (LocationManager) this.cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);
				boolean gpsEnable = false;
				try {
					gpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
				}catch(Exception e) {}
				
				if(! gpsEnable) {
					this.createDialog(args.getString(0),args.getString(1),args.getJSONArray(2),callbackContext);
				}
			}
		return false;
	}

	/**
	 *  Construct de GPS dialog with de title,message and buttons especified.
	 *  When positive button is pressed then its redirection to enable GPS
	 *
	 *	@param message 				content of the dialog, that has display to the user.
 	 *	@param title  				title of the dialog
	 *	@param buttonLabels         array of labels to the positive and negative button.
	 *	@param callbackContext      callback to display the result
	**/
	public synchronized void createDialog(final String title, final String message, final JSONArray buttonLabels, final CallbackContext callbackContext) {
    	final CordovaInterface cordova = this.cordova;
        Runnable runnable = new Runnable() {
            public void run() {

                AlertDialog.Builder builder = newDialog(cordova); // new AlertDialog.Builder(cordova.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setMessage(message);
                builder.setTitle(title);
                //builder.setCancelable(true);

                if(buttonLabels.length() > 0) {
                	try {
                			builder.setNegativeButton(buttonLabels.getString(0),
                        		new AlertDialog.OnClickListener() {
                        			@Override
                            		public void onClick(DialogInterface dialog, int which) {
                                		dialog.dismiss();
                                		callbackContext.success(0);;
                            		}
                            	});
                        
                	}catch(JSONException e) {}
                }
                if(buttonLabels.length() > 1) {
                	try {
                			builder.setPositiveButton(buttonLabels.getString(1),
                				new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										cordova.getActivity().startActivity(i);
										//dialog.dismiss();
										callbackContext.success(1);
									}
								});

                	}catch(JSONException e) {}
                }

                changeTextDirection(builder);
            };
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }

    /**
     *	Method that create a new Dialog.   
     *
     *	 @param cordova 	CordovaInterface to append the dialog created. 
     *						the dialog is forced to display with the theme light default
     **/
    @SuppressLint( "NewApi" )
	private AlertDialog.Builder newDialog(CordovaInterface cordova) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            return new AlertDialog.Builder(cordova.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            return new AlertDialog.Builder(cordova.getActivity());
        }
    }

    @SuppressLint("NewApi")
    private void changeTextDirection(Builder dlg){
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        dlg.create();
        AlertDialog dialog =  dlg.show();
        if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            TextView messageview = (TextView)dialog.findViewById(android.R.id.message);
            messageview.setTextDirection(android.view.View.TEXT_DIRECTION_LOCALE);
        }
    }
}
