package th.co.bighead.utilities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import th.co.thiensurat.data.controller.ContractImageController;

public class BHBitmap {

    public static String getImageTypeFromImagePath(String imgPath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);

        /*int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;*/

        String imageType = options.outMimeType;
        return imageType;
    }



    public static int calculateInSampleSize(BitmapFactory.Options options, int size) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > size || width > size) {

            int imageMaxSize = Math.max(width, height);
            inSampleSize = imageMaxSize / size;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromImagePath(String imgPath, int size) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, size);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    public static Bitmap decodeSampledBitmapFromImagePathForUploadToServer(String imgPath, int size) {

        Bitmap bitmap = decodeSampledBitmapFromImagePath(imgPath, size);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int maxSize = Math.max(width, height);
        double ratio = (double)size / (double)maxSize;

        int newWidth = (int)(width * ratio);
        int newHeight = (int)(height * ratio);
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        bitmap.recycle();

        return newBitmap;
    }

    public static Bitmap setRotateImageFromImagePath(String imgPath, Bitmap bitmap)  {
        Bitmap newBitmap = null;

        ExifInterface ei;
        try {
            ei = new ExifInterface(imgPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    newBitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    newBitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    newBitmap = rotateImage(bitmap, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    newBitmap = bitmap;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            newBitmap = bitmap;
        }

        return newBitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    public static String getBase64ToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();
            return Base64.encodeToString(image, 0);
        }

        return null;
    }
}
