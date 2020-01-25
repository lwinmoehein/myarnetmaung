package lwinmoehein.io.myarnetmaung.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import lwinmoehein.io.myarnetmaung.Singleton.Codes;


public class PermissionHelper {
    public static void checkImgPickPermission(Activity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Codes.PICK_FROM_GALLERY);
        }
    }
}
