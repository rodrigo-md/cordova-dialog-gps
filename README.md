# cordova-dialog-gps
Plugin for phonegap/cordova to display a dialog and redirect to GPS settings when de GPS is disable.

##Installation

Just type the following statement in your cli Cordova or phonegap .

`phonegap plugin add https://github.com/rodrigo-martinezd/cordova-dialog-gps.git`

or
`cordova plugin add https://github.com/rodrigo-martinezd/cordova-dialog-gps.git`

##Use

After installation , you can access the object *dialogGPS* embedded in *cordova*
  ```javascript
  document.addEventListener("deviceready",function() {
    cordova.dialogGPS();
  });
  ```
**Remember:** Remember, the object `cordova.dialogGPS` can only be accessed after the `document` is fully charged . This occurs when *deviceready* is called. Therefore it is recommended to make the call to the object within this function .

If `cordova.dialogGPS()` was call without options this will be result to a default message. If you want to customize it,  you can passed an options's array to the object.

##Options

**Callback function**

If you like use callbacks functions. You can pass a callback function as the first argument. This will result after capture a click event in the dialog:

  * 0, if the cancel button or the negative button was pressed.
  * 1, if the neutral button was pressed.
  * 2, if the positive button was pressed.

**Options's Array**

To custom the dialog window you can passes a options array with the next arguments:
  * the **_title_** of the dialog as the first argument.
  * the **_message_** or content of the dialog as the second argument.
  * the **_buttonsLabels array_** as the third argument, with the name of the each button to be displayed in the dialog. The names must follow the next order: **Negative button,Neutral button(optional),positive button**.
  
    Example: ```["Cancel","Later","Go to Settings"]```
