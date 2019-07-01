package th.co.bighead.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.R;

/**
 * Created by bighead on 26/10/2018.
 */

public class BHPermissions {

    public static final int  REQUEST_CODE = 20181109;


    public static IBHPermissions currentBHPermissionsCallback;


    public enum PermissionType {
        CAMERA("CAMERA"), STORAGE("STORAGE"), LOCATION("LOCATION");

        PermissionType(String value) {
            this.value = value;
        }

        private final String value;

        /*public String GetValue() {
            return value;
        }*/

        public String[] GetListPermission() {
            String[] list = new String[]{};

            switch (PermissionType.valueOf(this.value)) {
                case CAMERA:
                    list = new String[]{Manifest.permission.CAMERA};
                    break;

                case STORAGE:
                    list = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    break;

                case LOCATION:
                    list = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION};
                    break;
            }

            return list;
        }
    }


    public interface IBHPermissions {
        void onSuccess(BHPermissions bhPermissions);
        void onNotSuccess(BHPermissions bhPermissions);
        void onShouldShowRequest(BHPermissions bhPermissions, PermissionType... permissionType);
    }

    private boolean checkPermissions(Context context, String[] listPermission) {
        boolean b = true;

        for (String permission: listPermission) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                return false;
            }
        }
        return b;
    }

    private boolean shouldShowRequestPermissionRationale(Activity activity, String[] listPermission) {
        boolean b = true;

        for (String permission: listPermission) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                // Permission is not granted
                return false;
            }
        }
        return b;
    }

    public void requestPermissions(Activity activity, IBHPermissions callback, PermissionType... permissionType) {
        BHPermissions.currentBHPermissionsCallback = callback;

        List<String> arrayPermissionType = new ArrayList<String>();

        for (PermissionType pt : permissionType) {
            String[] listPermission = pt.GetListPermission();

            for(String strPermission : listPermission) {
                arrayPermissionType.add(strPermission);
            }
        }


        // Here, thisActivity is the current activity
        //String[] listPermissions = permissionType.GetListPermission();
        String[] listPermission = arrayPermissionType.toArray(new String[0]);
        if (!checkPermissions(activity.getApplicationContext(), listPermission)) {

            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(activity, listPermission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                BHPermissions.currentBHPermissionsCallback.onShouldShowRequest(this, permissionType);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        listPermission,
                        REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            BHPermissions.currentBHPermissionsCallback.onSuccess(this);
        }
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:

                boolean b = true;

                if(grantResults.length > 0) {
                    for (int grant : grantResults) {
                        if (grant != PackageManager.PERMISSION_GRANTED) {
                            b = false;
                            break;
                        }
                    }
                }

                // If request is cancelled, the result arrays are empty.
                if(b) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (BHPermissions.currentBHPermissionsCallback != null){
                        BHPermissions.currentBHPermissionsCallback.onSuccess(this);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    if (BHPermissions.currentBHPermissionsCallback != null){
                        BHPermissions.currentBHPermissionsCallback.onNotSuccess(this);
                    }
                }

                BHPermissions.currentBHPermissionsCallback = null;
                break;
        }

        // other 'case' lines to check for other
        // permissions this app might request.
    }


    public void openAppSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);

        /*Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + this.getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }

    private void showMessage(Activity activity, String msg) {
        AlertDialog.Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.permission_message_title))
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(activity.getString(R.string.permission_message_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        openAppSettings(activity);
                    }
                });
        setupAlert.show();
    }

    private String getMessage(Activity activity, PermissionType type) {

        switch (type) {
            case CAMERA:
                return activity.getString(R.string.permission_message_camera);

            case STORAGE:
                return activity.getString(R.string.permission_message_storage);

            case LOCATION:
                return activity.getString(R.string.permission_message_location);

            default:
                return "";
        }
    }

    public void showMessage(Activity activity, PermissionType... type) {
        if (type.length > 0) {
            if (type.length == 1) {
                showMessage(activity, getMessage(activity, type[0]));
            } else {
                if (type.length == 2 && checkPermissionType(PermissionType.CAMERA, type) && checkPermissionType(PermissionType.STORAGE, type)) {
                    showMessage(activity, activity.getString(R.string.permission_message_camera_and_storage));
                } else {
                    showMessage(activity, activity.getString(R.string.permission_message_msg));
                }
            }
        }
    }

    private boolean checkPermissionType(PermissionType typeCheck, PermissionType... type) {
        boolean b = false;

        for (PermissionType py : type) {
            if (typeCheck == py) {
                return true;
            }
        }

        return b;
    }
}
