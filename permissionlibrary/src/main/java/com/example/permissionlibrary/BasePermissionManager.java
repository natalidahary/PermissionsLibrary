package com.example.permissionlibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import java.util.Collections;

public abstract class BasePermissionManager {
    public static final int REQUEST_CODE = 101;

    protected abstract void requestPermissions(String[] permissions);
    protected abstract boolean hasPermissions(String[] permissions);
    protected abstract Object getContext();

    public void requestPermissions(String[] permissions, PermissionCallback callback) {
        if (hasPermissions(permissions)) {
            callback.onPermissionsGranted();
        } else {
            requestPermissions(permissions);
        }
    }

    public void handlePermissionResults(String[] permissions, int[] grantResults, PermissionCallback callback) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                if (!androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(), permissions[i])) {
                    showManualPermissionAlert(permissions[i], callback);
                    return;
                } else {
                    callback.onPermissionsDenied(Collections.singletonList(permissions[i]));
                    return;
                }
            }
        }
        callback.onPermissionsGranted();
    }

    public void showManualPermissionAlert(String permission, PermissionCallback callback) {
        Activity activity = (Activity) getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAlertDialogTheme);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        builder.setView(dialogView);

        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
        dialogMessage.setText("This permission is required for the app to function properly. Please enable it in settings.");

        builder.setTitle("Permission Required")
                .setCancelable(false)
                .setPositiveButton("Settings", (dialog, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", activity.getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    callback.onPermissionsPermanentlyDenied(Collections.singletonList(permission));
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    callback.onPermissionsDenied(Collections.singletonList(permission));
                    dialog.dismiss();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Apply the custom background color to buttons
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(androidx.core.content.ContextCompat.getColor(activity, R.color.darkGreen));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(androidx.core.content.ContextCompat.getColor(activity, R.color.cream));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(androidx.core.content.ContextCompat.getColor(activity, R.color.darkGreen));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(androidx.core.content.ContextCompat.getColor(activity, R.color.cream));
    }
}