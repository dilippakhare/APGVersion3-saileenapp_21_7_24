package com.apgautomation.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


import com.apgautomation.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by bt18 on 08/13/2016.
 */
public class BitmapUtilities
{
    public static File saveToExtenal(Bitmap bitmapImage,Context act)
    {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        Log.d("ROOT", root);


        File myDir = new File(root +"/"+ act.getString(R.string.app_name).replace(" ",""));
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, out);

            //  Toast.makeText(act,  file.getAbsolutePath()+"", 1000).show();

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (Build.VERSION.SDK_INT >= 18)
        {

        }
        else
        {
            act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));// Environment.getExternalStorageDirectory())));
        }
        return file;
    }


    public static File saveToExtenal1(Bitmap bitmapImage,Context act)
    {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        Log.d("ROOT", root);


        File myDir = new File(root + "/com.apgautomation");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, out);

            //  Toast.makeText(act,  file.getAbsolutePath()+"", 1000).show();

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (Build.VERSION.SDK_INT >= 18)
        {

        }
        else
        {
            act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));// Environment.getExternalStorageDirectory())));
        }
        return file;
    }

}
