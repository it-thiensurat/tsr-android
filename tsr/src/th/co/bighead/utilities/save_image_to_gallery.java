package th.co.bighead.utilities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Random;

import static th.co.thiensurat.activities.MainActivity.activity;

public class save_image_to_gallery {

    public static   Bitmap getResizedBiBitmaptmap(Bitmap bm, String NAME_IMAGE, String IMAGE_TYPE) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        int bitmapByteCount= BitmapCompat.getAllocationByteCount(bm);
        bitmapByteCount=bitmapByteCount/10000;


        Log.e("resizedBitmap1", String.valueOf(bitmapByteCount));
        float scaleWidth = 0,scaleHeight=0;

        if(bitmapByteCount>=16000){
            scaleWidth = ((float) width/17) / width;
            scaleHeight = ((float) height/17) / height;
        }
        else if(bitmapByteCount>=15000){
            scaleWidth = ((float) width/16) / width;
            scaleHeight = ((float) height/16) / height;
        }
        else  if(bitmapByteCount>=14000){
            scaleWidth = ((float) width/15) / width;
            scaleHeight = ((float) height/15) / height;
        }
        else   if(bitmapByteCount>=13000){
            scaleWidth = ((float) width/14) / width;
            scaleHeight = ((float) height/14) / height;
        }

        else if(bitmapByteCount>=12000){
            scaleWidth = ((float) width/13) / width;
            scaleHeight = ((float) height/13) / height;
        }
        else if(bitmapByteCount>=11000){
            scaleWidth = ((float) width/12) / width;
            scaleHeight = ((float) height/12) / height;
        }

        else if(bitmapByteCount>=10000){
            scaleWidth = ((float) width/11) / width;
            scaleHeight = ((float) height/11) / height;
        }

        else if(bitmapByteCount>=9000){
            scaleWidth = ((float) width/10) / width;
            scaleHeight = ((float) height/10) / height;
        }

        else if(bitmapByteCount>=8000){
            scaleWidth = ((float) width/9) / width;
            scaleHeight = ((float) height/9) / height;
        }

        else if(bitmapByteCount>=7000){
            scaleWidth = ((float) width/8) / width;
            scaleHeight = ((float) height/8) / height;
        }

        else if(bitmapByteCount>=6000){
            scaleWidth = ((float) width/7) / width;
            scaleHeight = ((float) height/7) / height;
        }


        else {
            scaleWidth = ((float) width/6) / width;
            scaleHeight = ((float) height/6) / height;
        }




        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();

        // Log.e("resizedBitmap2", String.valueOf(resizedBitmap));

        //  convertToPNG(resizedBitmap);
        SaveImage(resizedBitmap,NAME_IMAGE,IMAGE_TYPE);
        return resizedBitmap;
    }




    public static   void SaveImage(Bitmap segg,String NAME_IMAGE,String IMAGE_TYPE) {

        OutputStream fOut = null;
        Random generator = new Random();
        // int n = 10000;
        // n = generator.nextInt(n);
        //  String fileName = "Image-"+ n +".png";
        String fileName = NAME_IMAGE;

        final String appDirectoryName = "TSR APP IMAGE";
        final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appDirectoryName);

        imageRoot.mkdirs();

        final String appDirectoryName2 = IMAGE_TYPE;
        final File imageRoot2 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES+"/TSR APP IMAGE"), appDirectoryName2);

        imageRoot2.mkdirs();


        final File file = new File(imageRoot2, fileName);
        //final File file = new File(imageRoot2, fileName);
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            segg.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        }
        catch (Exception ex){

        }



        try {

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"stego");
        values.put(MediaStore.Images.Media.DESCRIPTION, "stego description");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
        // values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));

        // values.put(MediaStore.MediaColumns.DATA, filePath);

        values.put("_data", file.getAbsolutePath());
        // values.put("_data",filePath);

        ContentResolver cr = activity.getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }









    // MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    public static void addImageToGallery(final String filePath,String IMAGE_TYPE,String NAME_IMAGE) {





        ContentValues values = new ContentValues();


        values.put(MediaStore.Images.Media.TITLE,"stego");
        values.put(MediaStore.Images.Media.DESCRIPTION, "stego description");


        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values );
    }
}
