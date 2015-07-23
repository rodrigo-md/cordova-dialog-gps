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
    /**
    * @param message {string}       message for displayed.
    * @param callback {function}    callback function to send the index when a button is pressed
    * @param title {string}         title of dialog
    * @param buttons {array}        array with the buttons names with a max three names.
    **/
    cordova.dialogGPS("hello world",function(buttonIndex) {
          //do something with the buttonIndex
      });
  });
  ```
**Remember:** Remember, the object `cordova.dialogGPS` can only be accessed after the `document` is fully charged . This occurs when *deviceready* is called. Therefore it is recommended to make the call to the object within this function .

If `cordova.dialogGPS()` was call without options this will be result to a default message. If you want to customize it,  you can passed an options's array to the object.

##Options

**Message (Required)**

First argument, a string with the text to display in the dialog.

**Callback function (Optional)**

If you like use callbacks functions. You can pass a callback function as the second argument. This will result after capture a click event in the dialog:

  * 0, if the cancel button or the negative button was pressed.
  * 1, if the neutral button was pressed.
  * 2, if the positive button was pressed.

**Title (Optional)**

The title of dialog indicated with a string as the third argument.

**Buttons's Array (Optional)**

The **_buttonsLabels array_** as the third argument, with the name of the each button to be displayed in the dialog. The names must follow the next order: **Negative button,Neutral button(optional),positive button**.
  
Example: ```["Cancel","Later","Go to Settings"]```
