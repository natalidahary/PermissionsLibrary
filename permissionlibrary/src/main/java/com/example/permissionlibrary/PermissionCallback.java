package com.example.permissionlibrary;

import java.util.List;

public interface PermissionCallback {
    void onPermissionsGranted();
    void onPermissionsDenied(List<String> deniedPermissions);
    void onPermissionsPermanentlyDenied(List<String> permanentlyDeniedPermissions);
}