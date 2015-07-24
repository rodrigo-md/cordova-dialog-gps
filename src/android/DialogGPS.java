package cl.rmd.cordova.dialoggps;

import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import android.graphics.Color;
import android.location.LocationManager;
import android.provider.Settings;

public class DialogGPS extends CordovaPlugin {

    @Override
    public boolean execute(String action,JSONArray args,CallbackContext callbackContext) throws JSONException {

            if(this.cordova.getActivity().isFinishing()) return true;   
            else if(action.equals("DISPLAY")) {
                LocationManager  locationManager = (LocationManager) this.cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);
                boolean gpsEnable = false;
                try {
                    gpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                }catch(Exception e) {}
                
                if(! gpsEnable) {
                    this.createDialog(args.getString(0),args.getString(1),args.getString(2),args.getJSONArray(3),callbackContext);
                    return true;
                }
            }else if (action.equals("IS_GPS_ACTIVE")) {

            }
        return false;
    }

    /**
     *  Construct de GPS dialog with de title,message and buttons especified.
     *  When positive button is pressed then its redirection to enable GPS
     *
     *  @param message              content of the dialog, that has display to the user.
     *  @param title                title of the dialog
     *  @param buttonLabels         array of labels to the positive and negative button.
     *  @param callbackContext      callback to display the result
    **/
    public synchronized void createDialog(final String title, final String message,final String description,final JSONArray buttonLabels, final CallbackContext callbackContext) {
        final CordovaInterface cordova = this.cordova;

        Runnable runnable = new Runnable() {
            @SuppressLint("NewApi")
            public void run() {

                AlertDialog.Builder builder = newDialog(cordova,title,message,description);
                builder.setCancelable(true);
                String positiveButtonName = null;

                if(buttonLabels.length() == 2) {
                    try {
                        positiveButtonName = buttonLabels.getString(1);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else if (buttonLabels.length() == 3) {
                        
                    try {
                            positiveButtonName = buttonLabels.getString(2);
                            builder.setNeutralButton(buttonLabels.getString(1),
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
                                        dialog.dismiss();
                                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK,1));
                                    }
                                });

                    }catch(JSONException e) {}
                }

                if(buttonLabels.length() == 3 || buttonLabels.length() == 2) {
                    try {
                            builder.setNegativeButton(buttonLabels.getString(0),
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK,0));
                                    }
                                });

                            builder.setPositiveButton(positiveButtonName,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
                                        dialog.dismiss();
                                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK,2));
                                        cordova.getActivity().startActivity(
                                            new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }
                                });

                            builder.setOnCancelListener(new AlertDialog.OnCancelListener() {
                                public void onCancel(DialogInterface dialog){
                                    dialog.dismiss();
                                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK,0));
                                }
                            });

                             changeTextDirection(builder);
                        
                    }catch(JSONException e) {}
                }

            };
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }

    /**
     *  Method that create a new Dialog.   
     *
     *   @param cordova     CordovaInterface to append the dialog created. 
     *                      the dialog is forced to display with the theme light default
     **/
    @SuppressLint( "NewApi" )
    private AlertDialog.Builder newDialog(CordovaInterface cordova,final String title,final String message, final String description) {
        final Context context = cordova.getActivity().getApplicationContext();
        final String packageName = context.getPackageName();
        AlertDialog.Builder builder;
        int dpi = context.getResources().getDisplayMetrics().densityDpi;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        
        
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
           builder = new AlertDialog.Builder(cordova.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
           builder = new AlertDialog.Builder(cordova.getActivity());
        }
        
        builder.setMessage(message);
        builder.setTitle(title);
        
        LinearLayout _layout = new LinearLayout(context);
        LinearLayout.LayoutParams _layout_params  = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        _layout.setLayoutParams(_layout_params);
        _layout.setPadding(0, dpToPixels(20,dpi),dpToPixels(24,dpi),0);
        _layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView  _icon = new ImageView(context);
        _icon.setImageResource(cordova.getActivity().getResources().getIdentifier("ic_location","drawable",packageName));
        TextView _description = new TextView(context);
        
        _icon.setLayoutParams(new LinearLayout.LayoutParams(dpToPixels(72,dpi),dpToPixels(72,dpi)));
        _description.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,dpToPixels(72,dpi)));

        _icon.setContentDescription("Location Icon");
        _icon.setPadding(dpToPixels(24,dpi),0,dpToPixels(24,dpi),dpToPixels(24,dpi));
        _icon.setColorFilter(Color.argb(139,0,0,0));
        
        _description.setTextSize(16);
        _description.setText(description);
        _description.setTextColor(Color.argb(139,0,0,0));
        
        _layout.addView(_icon);
        _layout.addView(_description);
        builder.setView(_layout);
        
        return builder;
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
    
    private int dpToPixels(int dp,int dpi) {
        return (int)(dp*dpi/160);
    }
}