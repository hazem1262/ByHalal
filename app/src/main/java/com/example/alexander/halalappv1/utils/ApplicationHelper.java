package com.example.alexander.halalappv1.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ApplicationHelper {

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String keyHash = null;
        try {
            // getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();
            // getting package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Log.d("Package Name=", packageName);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                keyHash = new String(Base64.encode(messageDigest.digest(), 0));

                Log.d("Key Hash=", keyHash);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return keyHash;
    }
}
