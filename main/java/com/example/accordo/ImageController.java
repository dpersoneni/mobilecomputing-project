package com.example.accordo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageController {

    public static String fromBitmapToBase64(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
            }

            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }  catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Bitmap fromBaseToBitmap(String base64) throws IllegalArgumentException {
        try {
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
