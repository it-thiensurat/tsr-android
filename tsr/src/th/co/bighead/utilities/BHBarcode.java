package th.co.bighead.utilities;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Annop on 12/3/2558.
 */
public class BHBarcode {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    public static Bitmap generateCode128(String text, int width, int height) {
        return generate(text, BarcodeFormat.CODE_128, width, height);
    }

    public static Bitmap generate(String text, BarcodeFormat format, int width, int height) {
        String contentsToEncode = text;
        if (contentsToEncode == null) {
            return null;
        }

        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, width, height, hints);
        } catch (Exception iae) {
            // Unsupported format
            return null;
        }

        int imgWidth = result.getWidth();
        int imgHeight = result.getHeight();
        int[] pixels = new int[imgWidth * imgHeight];
        for (int y = 0; y < imgHeight; y++) {
            int offset = y * imgWidth;
            for (int x = 0; x < imgWidth; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixels, 0, imgWidth, 0, 0, imgWidth, imgHeight);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }

        return null;
    }
}
