package com.example.permissionlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ActivityPermissionManager extends BasePermissionManager {
    private final Activity activity;

    public ActivityPermissionManager(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
    }

    @Override
    protected boolean hasPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected Object getContext() {
        return activity;
    }

    public void checkCameraPermission(PermissionCallback callback) {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, callback);
    }

    public void checkStoragePermission(PermissionCallback callback) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                callback.onPermissionsGranted();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activity.startActivity(intent);
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, callback);
        }
    }

    public void checkLocationPermission(PermissionCallback callback) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            if (!hasPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, callback);
            } else if (!hasPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION})) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, callback);
            } else {
                callback.onPermissionsGranted();
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, callback);
        }
    }

    public void checkContactsPermission(PermissionCallback callback) {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, callback);
    }

    public void checkAudioPermission(PermissionCallback callback) {
        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, callback);
    }

    public void checkPhonePermission(PermissionCallback callback) {
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, callback);
    }

    public void checkSmsPermission(PermissionCallback callback) {
        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, callback);
    }

    public void checkCalendarPermission(PermissionCallback callback) {
        requestPermissions(new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, callback);
    }

    public void checkSensorsPermission(PermissionCallback callback) {
        requestPermissions(new String[]{Manifest.permission.BODY_SENSORS}, callback);
    }
}