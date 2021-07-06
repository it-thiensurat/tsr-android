package th.co.thiensurat.data.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

//import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;
//import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;
import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHBarcode;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.SignatureActivity;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ReturnProductController.ReturnProductStatus;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ChangeProductInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.ImpoundProductInfo;
import th.co.thiensurat.data.info.ManualDocumentInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.PrintTextInfo;
import th.co.thiensurat.data.info.ReturnProductDetailInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.SendDocumentInfo;
import th.co.thiensurat.data.info.SendMoneyInfo;
import th.co.thiensurat.data.info.ShortReceiptInfo;

//import static android.graphics.Color.BLACK;
//import static android.graphics.Color.WHITE;

public class DocumentController {

    private static Context mContext = BHApplication.getContext();

    private static int LINE_SPACE = 4;
    private static int LINE_FEED = 250;
    private static int LAYOUT_WIDTH = 1080;
    private static int PRINTER_LAYOUT_WIDTH = 580;

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private static String[] getText(String text, Paint p, float width) {
        String[] texts = text.split("\\s+");
        List<String> result = new ArrayList<String>();
        /*result.add(texts[0]);
        int ii = 1;
        while (ii < texts.length) {
            int index = result.size() - 1;
            String txt = result.get(index);
            txt += " " + texts[ii];
            Rect rect = new Rect();
            p.getTextBounds(txt, 0, txt.length(), rect);
//            Log.i("TEXT", txt + ": " + rect.width());
//            StaticLayout layout = new StaticLayout(txt, new TextPaint(p), rect.width() , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//            Log.i("TEXT", "Static " + txt + ": " + layout.getWidth());

            if (rect.width() < width) {
                result.set(index, txt);
            } else {
                result.add(texts[ii]);
            }

            ii++;
        }*/


        result.add("");
        int ii = 0;
        while (ii < texts.length) {
            int index = result.size() - 1;
            String txt = result.get(index);

            if (result.get(index).equals("")) {
                txt += texts[ii];
            } else {
                txt += " " + texts[ii];
            }
            //txt += " " + texts[ii];
            Rect rect = new Rect();
            p.getTextBounds(txt, 0, txt.length(), rect);
//            Log.i("TEXT", txt + ": " + rect.width());
//            StaticLayout layout = new StaticLayout(txt, new TextPaint(p), rect.width() , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//            Log.i("TEXT", "Static " + txt + ": " + layout.getWidth());

            if (rect.width() < width) {
                result.set(index, txt);
            } else {
                if (result.get(index).equals("")) {
                    result.set(index, txt);
                } else {
                    result.add(texts[ii]);
                }

                String newTxt = result.get(result.size() - 1);
                if (getWidth(newTxt, p) > width) {
                    int jj = newTxt.length() - 1;
                    boolean checkNewTxt = false;
                    while (checkNewTxt == false) {
                        String subTxt = newTxt.substring(0, jj);
                        if (getWidth(subTxt, p) < width) {
                            //result.add(subTxt);
                            result.set(result.size() - 1, subTxt);
                            checkNewTxt = true;
                        }
                        jj--;
                    }

                    if (jj != newTxt.length() - 1) {
                        String subTxt = newTxt.substring(jj + 1, newTxt.length());

                        if (ii + 1 < texts.length) {
                            if (getWidth(subTxt + " " + texts[ii + 1], p) < width) {
                                texts[ii + 1] = subTxt + " " + texts[ii + 1];
                            } else {
                                result.add(subTxt);
                            }
                            //texts[ii + 1] = subTxt  + " " + texts[ii + 1];
                        } else {
                            result.add(subTxt);
                        }
                    }
                } else {
                    result.set(result.size() - 1, texts[ii]);
                }
            }
            ii++;
        }

        return result.toArray(new String[result.size()]);
    }

    private static String[] getTextByPrintText(String text, int maxLength) {
        List<String> result = new ArrayList<String>();

        if (ThemalPrintController.getAlphabetOnly(text).length() <= maxLength) {
            result.add(text);
        } else {
            String[] textArray = text.split("\\s+");
            result.add("");
            for (int i = 0; i < textArray.length; i++) {
                int index = result.size() - 1;
                String tempText = result.get(index);

                if (tempText.equals("")) {
                    tempText += textArray[i];
                } else {
                    tempText += " " + textArray[i];
                }

                if (ThemalPrintController.getAlphabetOnly(tempText).length() <= maxLength) {
                    result.set(index, tempText);
                } else {
                    String[] tempTextArray = tempText.split("\\s+");
                    String resultTempText1 = "";
                    String resultTempText2 = "";

                    if (tempTextArray.length != 1) {
                        for (int j = 0; j < tempTextArray.length; j++) {
                            if (j == (tempTextArray.length - 1)) {
                                resultTempText2 = tempTextArray[j];
                            } else {
                                if (resultTempText1.equals("")) {
                                    resultTempText1 += tempTextArray[j];
                                } else {
                                    resultTempText1 += " " + tempTextArray[j];
                                }
                            }
                        }
                        result.set(index, resultTempText1);
                        result.addAll(alignTextByLength(resultTempText2, maxLength));
                    } else {
                        List<String> tempStr = alignTextByLength(tempTextArray[0], maxLength);

                        for (int j = 0; j < tempStr.size(); j++) {
                            if (j == 0) {
                                result.set(index, tempStr.get(j));
                            } else {
                                result.add(tempStr.get(j));
                            }
                        }
                        //result.addAll(alignTextByLength(tempTextArray[0], maxLength));
                    }
                }
            }
        }

        return result.toArray(new String[result.size()]);
    }

    private static List<String> alignTextByLength(String text, int length) {
        List<String> result = new ArrayList<String>();

        if (ThemalPrintController.getAlphabetOnly(text).length() <= length) {
            result.add(text);
        } else {
            result.add("");
            int start = 0;
            for (int i = 0; i < text.length(); i++) {
                int index = result.size() - 1;
                String tempText = text.substring(start, i + 1);

                if (ThemalPrintController.getAlphabetOnly(tempText).length() <= length) {
                    result.set(index, tempText);
                } else {
                    if (i != text.length() - 1) {
                        start = i;
                        result.add("");
                    }
                }
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignCenter(String text) {
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempText = getTextByPrintText(text, ThemalPrintController.maxTextLength);

        for (int i = 0; i < tempText.length; i++) {
            result.add(new PrintTextInfo(ThemalPrintController.calculateCenter(tempText[i])));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignCenter(String text, PrintTextInfo.FontType fontType) {
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempText = getTextByPrintText(text, ThemalPrintController.maxTextLength);

        for (int i = 0; i < tempText.length; i++) {
            result.add(new PrintTextInfo(ThemalPrintController.calculateCenter(tempText[i]), fontType));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftByOffSetLeft(String left, String right) {
        List<PrintTextInfo> result = new ArrayList<>();

        int offSetLeft = 20;
        String[] tempLeft = getTextByPrintText(left, offSetLeft);
        String[] tempRight = getTextByPrintText(right, ThemalPrintController.maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);

        for (int i = 0; i < size; i++) {
            result.add(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft)));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftByOffSetLeft(String left, String right, int offSetLeft) {
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempLeft = getTextByPrintText(left, offSetLeft);
        String[] tempRight = getTextByPrintText(right, ThemalPrintController.maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);

        for (int i = 0; i < size; i++) {
            result.add(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft)));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftByOffSetLeft(String left, String right, PrintTextInfo.FontType fontType) {
        List<PrintTextInfo> result = new ArrayList<>();

        int offSetLeft = 20;
        String[] tempLeft = getTextByPrintText(left, offSetLeft);
        String[] tempRight = getTextByPrintText(right, ThemalPrintController.maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);

        for (int i = 0; i < size; i++) {
            result.add(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft), fontType));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftByOffSetLeft(String left, String right, int offSetLeft, PrintTextInfo.FontType fontType) {
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempLeft = getTextByPrintText(left, offSetLeft);
        String[] tempRight = getTextByPrintText(right, ThemalPrintController.maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);

        for (int i = 0; i < size; i++) {
            result.add(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft), fontType));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftRight(String left, String right) {
        List<PrintTextInfo> result = new ArrayList<>();

        if (ThemalPrintController.getAlphabetOnly(String.format("%s %s", left, right)).length() <= ThemalPrintController.maxTextLength) {
            result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(left, right)));
        } else {
            String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
            String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

            int size = Math.max(tempLeft.length, tempRight.length);

            for (int i = 0; i < size; i++) {
                result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "")));
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftRight(String left, String right, PrintTextInfo.FontType fontType) {
        List<PrintTextInfo> result = new ArrayList<>();

        if (ThemalPrintController.getAlphabetOnly(String.format("%s %s", left, right)).length() <= ThemalPrintController.maxTextLength) {
            result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(left, right), fontType));
        } else {
            String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
            String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

            int size = Math.max(tempLeft.length, tempRight.length);

            for (int i = 0; i < size; i++) {
                result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : ""), fontType));
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextLeftRightAlignCenter(String left, String right) {
        return getTextLeftRightAlignCenter(left, right, "TH");
    }

    private static List<PrintTextInfo> getTextLeftRightAlignCenter(String left, String right, String Language) {
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
        String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

        int size = Math.max(tempLeft.length, tempRight.length);

        for (int i = 0; i < size; i++) {
            result.add(new PrintTextInfo(String.format(" %s  %s ",
                    ThemalPrintController.calculateCenterByLength(i < tempLeft.length ? tempLeft[i] : "", (ThemalPrintController.maxTextLength / 2) - 2),
                    ThemalPrintController.calculateCenterByLength(i < tempRight.length ? tempRight[i] : "", (ThemalPrintController.maxTextLength / 2) - 2))
                    , Language));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextLeftRightAlignCenter(String left, String right, PrintTextInfo.FontType fontType) {
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
        String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

        int size = Math.max(tempLeft.length, tempRight.length);

        for (int i = 0; i < size; i++) {
            result.add(new PrintTextInfo(String.format(" %s  %s ",
                    ThemalPrintController.calculateCenterByLength(i < tempLeft.length ? tempLeft[i] : "", (ThemalPrintController.maxTextLength / 2) - 2),
                    ThemalPrintController.calculateCenterByLength(i < tempRight.length ? tempRight[i] : "", (ThemalPrintController.maxTextLength / 2) - 2)), fontType
            ));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeft(String left) {
        return getTextAlignLeft(left, "TH");
    }

    private static List<PrintTextInfo> getTextAlignLeft(String left, String language) {
        List<PrintTextInfo> result = new ArrayList<>();

        if (ThemalPrintController.getAlphabetOnly(left).length() <= ThemalPrintController.maxTextLength) {
            result.add(new PrintTextInfo(left, language));
        } else {
            String[] tempLeft = getTextByPrintText(left, ThemalPrintController.maxTextLength);

            for (int i = 0; i < tempLeft.length; i++) {
                result.add(new PrintTextInfo(tempLeft[i], language));
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeft(String left, PrintTextInfo.FontType fontType) {
        List<PrintTextInfo> result = new ArrayList<>();

        if (ThemalPrintController.getAlphabetOnly(left).length() <= ThemalPrintController.maxTextLength) {
            result.add(new PrintTextInfo(left, fontType));
        } else {
            String[] tempLeft = getTextByPrintText(left, ThemalPrintController.maxTextLength);

            for (int i = 0; i < tempLeft.length; i++) {
                result.add(new PrintTextInfo(tempLeft[i], fontType));
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextForDocumentHistory(String num, String left, String right) {
        List<PrintTextInfo> result = new ArrayList<>();

        if (ThemalPrintController.getAlphabetOnly(String.format("%s  %s %s", num, left, right)).length() <= ThemalPrintController.maxTextLength) {
            result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(String.format("%s  %s", num, left), right)));
        } else {
            String[] tempNum = getTextByPrintText(num, 3);
            String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 5);
            String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 5);

            int size = Math.max(tempLeft.length, tempRight.length);
            size = Math.max(size, tempNum.length);

            for (int i = 0; i < size; i++) {
                //result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "")));

                result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(String.format("%s  %s", i < tempNum.length ? tempNum[i] : "   ", i < tempLeft.length ? tempLeft[i] : ""), i < tempRight.length ? tempRight[i] : "")));
            }
        }
        return result;
    }


    private static int getWidth(String text, Paint p) {
        Rect rect = new Rect();
        p.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    private static String getSignatureUnderline(Paint p, int width) {
        Rect rect1 = new Rect();
        p.getTextBounds(".", 0, 1, rect1);
        Rect rect2 = new Rect();
        p.getTextBounds("..", 0, 2, rect2);
        int ww = width / ((rect2.width() - rect1.width() * 2) + rect1.width());
        char[] ch = new char[ww];
        Arrays.fill(ch, '.');
        return new String(ch);
    }

    public static void saveImage(Bitmap bitmap) {

        try {
//			img.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File("/sdcard/ImageAfterAddingText.png")));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(File.createTempFile("PrintImage", ".jpg", new File(BHStorage.getFolder(BHStorage.FolderType.Picture)))));
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(mContext.getCacheDir() + "/image.jpg")));
            // dest is Bitmap, if you want to preview the final image, you can
            // display it on screen also before saving
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static Bitmap scaleBitmapByWidth(Bitmap bm) {
        return scaleBitmapByWidth(bm, PRINTER_LAYOUT_WIDTH);
    }

    public static Bitmap scaleBitmapByWidth(Bitmap bm, int width) {
        float ratio = width / (float) bm.getWidth();
        int height = (int) (ratio * bm.getHeight());

        Bitmap scaledBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.postScale(ratio, ratio);
//        scaleMatrix.setScale(ratio, ratio);

        Canvas canvas = new Canvas(scaledBitmap);
//        canvas.setMatrix(scaleMatrix);
//        canvas.drawBitmap(bm, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bm, scaleMatrix, new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        //BitMapToString(scaledBitmap);
        return scaledBitmap;
    }

    //ไว้ดูรูปตอนพิมพ์
    /*public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }*/


    public static Bitmap scaleBitmapByHeight(Bitmap bm, int height) {
        int width = (int) (1f * height / bm.getHeight() * bm.getWidth());

        return Bitmap.createScaledBitmap(bm, width, height, true);
    }

    public static Bitmap getShortHeader() {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 650, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
//		cv.drawColor(Color.WHITE);
//		cv.drawColor(Color.TRANSPARENT, Mode.CLEAR);

        int logoHeight = 180;
        Bitmap logo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tsr_logo_gray);
        cv.drawBitmap(scaleBitmapByHeight(logo, logoHeight), 0, 2, null);

        float x = 385;
        float y = 0;
        float fontSize = 46;

        Paint p = new Paint();

        p.setTextSize(34);
        p.setTypeface(null);
        y += fontSize + LINE_SPACE;
        cv.drawText("43/9 หมู่ 7 ซ.ชูชาติอนุสรณ์ 4 ต.บางตลาด ", x, y, p);

        y += fontSize + LINE_SPACE;
        cv.drawText("อ.ปากเกร็ด จ.นนทบุรี 11120", x, y, p);

        y += fontSize + LINE_SPACE;
        cv.drawText("เลขประจำตัวภาษี 1122331565468", x, y, p);

        return img;
    }

    public static Bitmap getHeader() {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 650, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
//		cv.drawColor(Color.WHITE);
//		cv.drawColor(Color.TRANSPARENT, Mode.CLEAR);

        int logoHeight = 220;
        Bitmap logo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tsr_logo_gray);
        cv.drawBitmap(scaleBitmapByHeight(logo, logoHeight), 0, 2, null);

        float x = 1;
        float y = logoHeight + LINE_SPACE;
        float fontSize = 56;

        Paint p = new Paint();
        p.setTextSize(fontSize);
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        y += fontSize + LINE_SPACE;
        cv.drawText("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)", x, y, p);

        p.setTextSize(44);
        p.setTypeface(null);
        y += fontSize + LINE_SPACE;
        cv.drawText("43/9 หมู่ 7 ซ.ชูชาติอนุสรณ์ 4 ต.บางตลาด", x, y, p);

        y += fontSize + LINE_SPACE;
        cv.drawText("อ.ปากเกร็ด จ.นนทบุรี 11120", x, y, p);

        y += fontSize + LINE_SPACE;
        cv.drawText("โทร. 0 2819 8888  แฟกซ์ 0 2962 6951-3", x, y, p);

        y += fontSize + LINE_SPACE;
        cv.drawText("อีเมล์. thiensurat@thiensurat.co.th", x, y, p);

        return img;
    }

    public static List<PrintTextInfo> getTextHeader() {
        List<PrintTextInfo> listHeader = new ArrayList<>();

//        listHeader.addAll(getTextAlignLeft("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)", PrintTextInfo.FontType.Bold));
//        listHeader.addAll(getTextAlignLeft("43/9 หมู่ 7 ซ.ชูชาติอนุสรณ์ 4 ต.บางตลาด", PrintTextInfo.FontType.Bold));
//        listHeader.addAll(getTextAlignLeft("อ.ปากเกร็ด จ.นนทบุรี 11120", PrintTextInfo.FontType.Bold));
//        listHeader.addAll(getTextAlignLeft("โทร. 0 2819 8888  แฟกซ์ 0 2962 6951-3", PrintTextInfo.FontType.Bold));
//        listHeader.addAll(getTextAlignLeft("อีเมล์. thiensurat@thiensurat.co.th", PrintTextInfo.FontType.Bold));

        listHeader.add(new PrintTextInfo("printHeader"));

        return listHeader;
    }

    public static List<PrintTextInfo> getTextShortHeader() {
        List<PrintTextInfo> listHeader = new ArrayList<>();

//        listHeader.addAll(getTextAlignCenter("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)", PrintTextInfo.FontType.Bold));
//        listHeader.addAll(getTextAlignCenter("เลขประจำตัวผู้เสียภาษี 0107556000213", PrintTextInfo.FontType.Bold));
//        listHeader.addAll(getTextAlignCenter("โทร. 1210", PrintTextInfo.FontType.Bold));
        listHeader.add(new PrintTextInfo("printShortHeader"));
        return listHeader;
    }

    public static Bitmap getImageShortHeader() {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 650, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
//		cv.drawColor(Color.WHITE);
//		cv.drawColor(Color.TRANSPARENT, Mode.CLEAR);

        float y = 0;
        float fontSize = 44;
        float lineSpace = fontSize;

        Paint p = new Paint();
        p.setTextSize(fontSize);
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextAlign(Align.CENTER);
        y += LINE_FEED / 2;
        cv.drawText("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)", LAYOUT_WIDTH / 2, y, p);


        y += fontSize + lineSpace;
        cv.drawText("เลขประจำตัวผู้เสียภาษี 0107556000213", LAYOUT_WIDTH / 2, y, p);

        y += fontSize + lineSpace;
        cv.drawText("โทร. 1210", LAYOUT_WIDTH / 2, y, p);

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) y, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return result;
    }

    public static Bitmap getContract(ContractInfo contract, AddressInfo defaultAddress, AddressInfo installAddress, ManualDocumentInfo manual) {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        String title = contract.MODE > 1 ? "ใบสัญญาเช่าซื้อ" : "ใบสัญญาซื้อขาย";
        cv.drawText(title, LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        yy += fontSize * 3;
        cv.drawText("วันที่ทำสัญญา", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(contract.EFFDATE), xValue, yy, pValue);

        if (manual != null) {
            yy += fontSize + lineSpace;
            cv.drawText("เลขที่อ้างอิง", xTitle, yy, pTitle);
            //cv.drawText(String.format("%s/%4d", BHUtilities.trim(manual.ManualVolumeNo), manual.ManualRunningNo).replace(' ', '0'), xValue, yy, pValue);
            cv.drawText(String.format("%4d", manual.ManualRunningNo).replace(' ', '0'), xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(contract.CONTNO), xValue, yy, pValue);

        String[] texts = getText(String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText(contract.MODE > 1 ? "ผู้เช่าซื้อ" : "ผู้ซื้อ", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }
        /*yy += fontSize + lineSpace;
        cv.drawText("ผู้ซื้อ", xTitle, yy, pTitle);
        cv.drawText(String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)), xValue, yy, pValue);*/


        yy += fontSize + lineSpace;
        cv.drawText("เลขที่บัตร", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(contract.IDCard), xValue, yy, pValue);

        //xValue = 250;
        texts = getText(defaultAddress.Address(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่อยู่บัตร", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        texts = getText(installAddress.Address(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่ติดตั้ง", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        texts = getText(BHUtilities.trim(contract.ProductName), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("สินค้า", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("รุ่น", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(contract.MODEL), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขเครื่อง", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(contract.ProductSerialNumber), xValue, yy, pValue);


        if (contract.TradeInDiscount > 0) {

            xValue = LAYOUT_WIDTH - 100;
            pValue.setTextAlign(Align.RIGHT);
            yy += fontSize + lineSpace;
            cv.drawText("ราคา", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(contract.SALES) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("ส่วนลดเครื่องแสดง", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(contract.TradeInDiscount) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("ราคาสุทธิ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(contract.TotalPrice) + " บาท", xValue, yy, pValue);
        } else {

            xValue = LAYOUT_WIDTH - 100;
            pValue.setTextAlign(Align.RIGHT);
            yy += fontSize + lineSpace;
            cv.drawText("ราคาขาย", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(contract.SALES) + " บาท", xValue, yy, pValue);

        }

        if (contract.MODE > 1) {
            yy += fontSize + lineSpace;
            float sum = contract.PaymentAmount - contract.TradeInDiscount;
            cv.drawText("งวดที่ 1 ต้องชำระ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sum) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("งวดที่ 2 ถึงงวดที่ " + BHUtilities.numericFormat(contract.MODE) + " ต้องชำระงวดละ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(contract.NextPaymentAmount) + " บาท", xValue, yy, pValue);

        }

        pValue.setTextAlign(Align.LEFT);
        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.CENTER);


        String customer = String.format("(%s %s)", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName));

        if (contract.MODE == 1) {

            yy += 200;
            int Value = LAYOUT_WIDTH / 2;
            int ww = getWidth(customer, pValue) + 50;

            /*cv.drawText(getSignatureUnderline(pSignature, ww), Value, yy, pSignature);
            cv.drawText("ผู้ซื้อ", Value + 190, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText(customer, Value, yy, pSignature);*/

            texts = getText(customer, pSignature, LAYOUT_WIDTH);
            for (int ii = 0; ii < texts.length; ii++) {
                if (ii == 0) {
                    /*cv.drawText(getSignatureUnderline(pSignature, ww), Value, yy, pSignature);
                    cv.drawText("ผู้ซื้อ", Value + 190, yy, pValue);*/
                    cv.drawText(String.format("%sผู้ซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH) - (getWidth("ผู้ซื้อ", pSignature) + (LAYOUT_WIDTH / 2)))), LAYOUT_WIDTH / 2, yy, pSignature);
                }
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], Value, yy, pSignature);
            }

        } else {

            /*yy += 200;
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            int xx = getWidth(TSR, pValue) + 10;
            int ww = getWidth(customer, pValue) + 50;

            int width = LAYOUT_WIDTH / 6;
            cv.drawText(getSignatureUnderline(pSignature, xx), width, yy, pSignature); // วาดจุด
            cv.drawText(String.format("ผู้ให้เช่าซื้อ"), width + 190, yy, pValue);

            xValue = LAYOUT_WIDTH - (ww / 2 + 120);
            cv.drawText("ผู้ซื้อ", LAYOUT_WIDTH - 115, yy, pValue);
            cv.drawText(getSignatureUnderline(pSignature, ww), xValue, yy, pSignature);

            yy += fontSize + lineSpace;
            cv.drawText(TSR, 230, yy, pSignature);
            cv.drawText(customer, xValue, yy, pSignature);*/

            //int width = LAYOUT_WIDTH / 6;
            //xValue = LAYOUT_WIDTH - (ww / 2 + 120);

            /****/
            yy += 200;
            String TSR = "";
            // YIM Change TSR_COMMITTEE_NAME
            if (BHGeneral.isOpenDepartmentSignature && BHPreference.hasDepartmentSignatureImage()) {
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "(" + saleLeader.DepartmentHeadName + ")";
                }
            }
            cv.drawText(String.format("%sผู้ให้เช่าซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("ผู้ให้เช่าซื้อ", pSignature) + 50))), LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format("%sผู้เช่าซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("ผู้เช่าซื้อ", pSignature) + 50))), (LAYOUT_WIDTH / 4) * 3, yy, pSignature);

            String[] texts1 = getText(TSR, pSignature, LAYOUT_WIDTH / 2);
            String[] texts2 = getText(customer, pSignature, LAYOUT_WIDTH / 2);
            int num;
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], LAYOUT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
                }
            }
            /****/


            /*yy += 200;
            String salename = "(" + BHUtilities.trim(contract.SaleEmployeeName) + ")";
            String salecode = "รหัส " + BHUtilities.trim(contract.SaleCode);

            String saleteamname = "(" + BHUtilities.trim(contract.upperEmployeeName) + ")";
            String saleteamcode = "" + BHUtilities.trim(contract.SaleTeamName);

            int zz = getWidth(saleteamname, pValue) + 50;
            xValue = LAYOUT_WIDTH - (zz / 2 + 120);

            int vv = getWidth(salename, pValue) + 50;
            xValue = LAYOUT_WIDTH - (vv / 2 + 120);

            int r = LAYOUT_WIDTH / 6;
            cv.drawText(getSignatureUnderline(pSignature, zz), r, yy, pSignature);
            cv.drawText("พยาน", r + 190, yy, pValue);

            cv.drawText("พยาน", LAYOUT_WIDTH - 115, yy, pValue);
            cv.drawText(getSignatureUnderline(pSignature, vv), xValue, yy, pSignature);

            yy += fontSize + lineSpace;
            cv.drawText(saleteamname, 230, yy, pSignature);
            cv.drawText(salename, xValue, yy, pSignature);
            yy += fontSize + lineSpace;
            cv.drawText(saleteamcode, 240, yy, pSignature);
            cv.drawText(salecode, xValue, yy, pSignature);*/

            /****/
            yy += 200;
            String salename = "(" + BHUtilities.trim(contract.SaleEmployeeName != null ? contract.SaleEmployeeName : "") + ")";
            String salecode = "รหัส " + BHUtilities.trim(contract.SaleCode);

            String saleteamname = "(" + BHUtilities.trim(contract.upperEmployeeName != null ? contract.upperEmployeeName : "") + ")";
            String saleteamcode = "" + BHUtilities.trim(contract.SaleTeamName != null ? contract.SaleTeamName : "");

            cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), (LAYOUT_WIDTH / 4) * 3, yy, pSignature);

            texts1 = getText(saleteamname, pSignature, LAYOUT_WIDTH / 2);
            texts2 = getText(salename, pSignature, LAYOUT_WIDTH / 2);
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], LAYOUT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
                }
            }

            yy += fontSize + lineSpace;
            cv.drawText(saleteamcode, LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(salecode, (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
            /****/
        }

        xValue = 1;
        yy += fontSize + lineSpace + 50;
        cv.drawText(String.format("รหัส %s %s", BHUtilities.trim(contract.SaleCode), BHUtilities.trim(contract.SaleEmployeeName)), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText(String.format("%s %s", BHUtilities.trim(contract.SaleTeamName), BHUtilities.trim(contract.upperEmployeeName)), xValue, yy, pValue);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }

    //public static List<PrintTextInfo> getTextContract(ContractInfo contract, AddressInfo defaultAddress, AddressInfo installAddress, ManualDocumentInfo manual) {
    public static List<PrintTextInfo> getTextContract(ContractInfo contract, AddressInfo defaultAddress, AddressInfo installAddress) {
        List<PrintTextInfo> listText = new ArrayList<>();
        String[] texts;

        listText.addAll(getTextHeader());


        listText.add(new PrintTextInfo("selectPageMode"));
        listText.add(new PrintTextInfo("setContractPageRegion"));
        listText.addAll(getTextAlignCenter(contract.MODE > 1 ? "ใบสัญญาเช่าซื้อ" : "ใบสัญญาซื้อขาย", PrintTextInfo.FontType.Bold));
        listText.add(new PrintTextInfo("printContractPageTitle"));
        listText.add(new PrintTextInfo("beginContractPage"));

        listText.addAll(getTextAlignLeftByOffSetLeft(" วันที่ทำสัญญา", BHUtilities.dateFormat(contract.EFFDATE)));

        /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
        /*if (manual != null) {
            listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่อ้างอิง", String.format("%4d", manual.ManualRunningNo).replace(' ', '0')));
        }*/
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่อ้างอิง", contract.ContractReferenceNo != null ? contract.ContractReferenceNo : ""));
        /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/

        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่สัญญา", BHUtilities.trim(contract.CONTNO)));
        listText.addAll(getTextAlignLeftByOffSetLeft(contract.MODE > 1 ? " ผู้เช่าซื้อ" : " ผู้ซื้อ", String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName))));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่บัตร", BHUtilities.trim(contract.IDCard)));
        /*listText.addAll(getTextAlignLeftByOffSetLeft("ที่อยู่บัตร", defaultAddress.Address()));
        listText.addAll(getTextAlignLeftByOffSetLeft("ที่ติดตั้ง", installAddress.Address()));*/


        switch (contract.CustomerType) {
            case "0":
            case "2":
                if (defaultAddress.TelMobile.equals(installAddress.TelMobile)) {
                    listText.addAll(getTextAlignLeftByOffSetLeft(" ที่อยู่บัตร", defaultAddress.Address()));
                    listText.addAll(getTextAlignLeftByOffSetLeft(" ที่ติดตั้ง", installAddress.Address()));
                    listText.addAll(getTextAlignLeftByOffSetLeft(" เบอร์โทรติดต่อ", installAddress.TelMobile));
                } else {
                    listText.addAll(getTextAlignLeftByOffSetLeft(" ที่อยู่บัตร", defaultAddress.Address()));
                    listText.addAll(getTextAlignLeftByOffSetLeft(" เบอร์โทรติดต่อ", defaultAddress.TelMobile));
                    listText.addAll(getTextAlignLeftByOffSetLeft(" ที่ติดตั้ง", installAddress.Address()));
                    listText.addAll(getTextAlignLeftByOffSetLeft(" เบอร์โทรติดต่อ", installAddress.TelMobile));
                }
                break;
            case "1":
                if (defaultAddress.TelMobile.equals(installAddress.TelMobile)) {
                    listText.addAll(getTextAlignLeftByOffSetLeft(" ที่อยู่บัตร", defaultAddress.Address()));
                    listText.addAll(getTextAlignLeftByOffSetLeft(" ที่ติดตั้ง", installAddress.Address()));
                    listText.addAll(getTextAlignLeftByOffSetLeft(" เบอร์โทรติดต่อ", installAddress.TelHome));
                } else {
                    listText.addAll(getTextAlignLeftByOffSetLeft(" ที่อยู่บัตร", defaultAddress.Address()));
                    listText.addAll(getTextAlignLeftByOffSetLeft(" เบอร์โทรติดต่อ", defaultAddress.TelHome));
                    listText.addAll(getTextAlignLeftByOffSetLeft(" ที่ติดตั้ง", installAddress.Address()));
                    listText.addAll(getTextAlignLeftByOffSetLeft(" เบอร์โทรติดต่อ", installAddress.TelHome));
                }
                break;
        }

        listText.addAll(getTextAlignLeftByOffSetLeft(" สินค้า", BHUtilities.trim(contract.ProductName)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" รุ่น", BHUtilities.trim(contract.MODEL)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขเครื่อง", BHUtilities.trim(contract.ProductSerialNumber)));

        if (contract.TradeInDiscount > 0) {
            listText.addAll(getTextAlignLeftRight(" ราคา", BHUtilities.numericFormat(contract.SALES) + " บาท"));
            listText.addAll(getTextAlignLeftRight(" ส่วนลดเครื่องแสดง", BHUtilities.numericFormat(contract.TradeInDiscount) + " บาท"));
            listText.addAll(getTextAlignLeftRight(" ราคาสุทธิ", BHUtilities.numericFormat(contract.TotalPrice) + " บาท"));
        } else {
            listText.addAll(getTextAlignLeftRight(" ราคาขาย", BHUtilities.numericFormat(contract.SALES) + " บาท"));
        }

        if (contract.MODE > 1) {
            float sum = contract.PaymentAmount - contract.TradeInDiscount;
            listText.addAll(getTextAlignLeftRight(" งวดที่ 1 ต้องชำระ", BHUtilities.numericFormat(sum) + " บาท"));
            listText.addAll(getTextAlignLeftRight(" งวดที่ 2 ถึงงวดที่ " + BHUtilities.numericFormat(contract.MODE) + " ต้องชำระงวดละ", BHUtilities.numericFormat(contract.NextPaymentAmount) + " บาท"));
        }

        //listText.add(new PrintTextInfo(""));

        String customer = String.format("(%s%s)", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName));

        if (contract.MODE == 1) {
            //listText.addAll(getTextAlignCenter("...........................ผู้ซื้อ"));
            //listText.addAll(getTextAlignCenter(customer));

            listText.add(new PrintTextInfo(""));
            listText.add(new PrintTextInfo("signature"));
            listText.addAll(getTextLeftRightAlignCenter("......................", ".....................", "EN"));
            listText.addAll(getTextLeftRightAlignCenter("           ผู้ขาย       ", "          ผู้ซื้อ        "));
            String TSR = "";
            // YIM Change TSR_COMMITTEE_NAME
            if (BHGeneral.isOpenDepartmentSignature && BHPreference.hasDepartmentSignatureImage()) {
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "(" + saleLeader.DepartmentHeadName + ")";
                }
            }
            listText.addAll(getTextLeftRightAlignCenter(TSR, customer));


        } else {
            listText.add(new PrintTextInfo(""));
            listText.add(new PrintTextInfo("signature"));
            listText.addAll(getTextLeftRightAlignCenter("......................", ".....................", "EN"));
            listText.addAll(getTextLeftRightAlignCenter("     ผู้ให้เช่าซื้อ       ", "       ผู้เช่าซื้อ        "));
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            // YIM Change TSR_COMMITTEE_NAME
            if (BHGeneral.isOpenDepartmentSignature && BHPreference.hasDepartmentSignatureImage()) {
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "(" + saleLeader.DepartmentHeadName + ")";
                }
            }
            listText.addAll(getTextLeftRightAlignCenter(TSR, customer));

            String saleteamname = "" + BHUtilities.trim(contract.upperEmployeeName != null ? contract.upperEmployeeName : "") + "";
            String saleteamcode = "" + BHUtilities.trim(contract.SaleTeamName != null ? contract.SaleTeamName : "");

            String salename = "" + BHUtilities.trim(contract.SaleEmployeeName != null ? contract.SaleEmployeeName : "") + "";
            String salecode = "รหัส " + BHUtilities.trim(contract.SaleCode);
            listText.add(new PrintTextInfo(""));
            listText.addAll(getTextLeftRightAlignCenter(".................พยาน", ".................พยาน"));
            listText.addAll(getTextLeftRightAlignCenter(saleteamname, salename));
            listText.addAll(getTextLeftRightAlignCenter(saleteamcode, salecode));
        }

        listText.add(new PrintTextInfo(""));
        listText.addAll(getTextAlignLeft(String.format(" รหัส %s %s", BHUtilities.trim(contract.SaleCode), BHUtilities.trim(contract.SaleEmployeeName))));
        listText.addAll(getTextAlignLeft(String.format(" %s %s", BHUtilities.trim(contract.SaleTeamName), BHUtilities.trim(contract.upperEmployeeName))));
        listText.add(new PrintTextInfo(""));
        listText.add(new PrintTextInfo("logoTSRTelsale"));
        listText.add(new PrintTextInfo("endContractPage"));
        listText.add(new PrintTextInfo("selectStandardMode"));
        return listText;
    }


    public static Bitmap getNewReceipt(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {

        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบรับเงิน", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        int xTitle = 1;
        int xValue = 480;


        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        String bahtLabel = " บาท";


        yy += fontSize * 3;
        cv.drawText("วันที่รับเงิน", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(paymentInfo.PayDate), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่", xTitle, yy, pTitle);
        cv.drawText(paymentInfo.ReceiptCode, xValue, yy, pValue);

        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            yy += fontSize + lineSpace;
            cv.drawText("เลขที่อ้างอิง", xTitle, yy, pTitle);

            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            cv.drawText(ManualDocumentBookRunningNo, xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitle);
        cv.drawText(paymentInfo.CONTNO, xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("วันที่ทำสัญญา", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(paymentInfo.EFFDATE), xValue, yy, pValue);

        String[] texts = new String[]{""};
        texts = getText(debtorCustomerInfo.CustomerFullName(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ชื่อลูกค้า", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        /*yy += fontSize + lineSpace;
        cv.drawText("ชื่อลูกค้า", xTitle, yy, pTitle);
        cv.drawText(debtorCustomerInfo.CustomerFullName(), xValue, yy, pValue);*/

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่บัตร", xTitle, yy, pTitle);
        cv.drawText(debtorCustomerInfo.IDCard, xValue, yy, pValue);


        //xValue = 250;
        texts = new String[]{""};
        texts = getText(addressInfo.Address(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่อยู่ติดตั้ง", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        texts = new String[]{""};
        texts = getText(paymentInfo.ProductName, pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ชื่อสินค้า", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("รุ่น", xTitle, yy, pTitle);
        cv.drawText(paymentInfo.MODEL, xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("รหัสสินค้า", xTitle, yy, pTitle);
        //-- Fixed - [BHPROJ-0025-770] :: [Data Migration] ใบเสร็จที่ไม่มี ProductSerialNumber ไม่สามารถพิมพ์ใบเสร็จได้ เนื่องจากค่า ProductSerialNumber = null
        cv.drawText(((paymentInfo.ProductSerialNumber != null) ? paymentInfo.ProductSerialNumber : "-"), xValue, yy, pValue);


        /**ส่วนลดตัดสด**/

        texts = new String[]{""};
        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            texts = getText(BHUtilities.trim(String.format("ชำระงวดที่ %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), pValue, LAYOUT_WIDTH / 3);

            for (int ii = 0; ii < texts.length; ii++) {
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], xTitle, yy, pTitle);
                if (ii == 0) {
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + bahtLabel, xValue, yy, pValue);
                }
            }

            /*yy += fontSize + lineSpace;
            cv.drawText(String.format("ชำระงวดที่ %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE), xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + bahtLabel, xValue, yy, pValue);*/

            yy += fontSize + lineSpace;
            cv.drawText("ส่วนลดตัดสด", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountDiscountAmount) + bahtLabel, xValue, yy, pValue);
        }

        /**ยอดเงินที่จ่ายมาตามใบเสร็จ**/
        texts = new String[]{""};
        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
                texts = getText(BHUtilities.trim("ค่างวดเงินสด (ชำระครบ)"), pValue, LAYOUT_WIDTH / 3);
            } else {
                texts = getText(BHUtilities.trim("ค่างวดเงินสด (ชำระบางส่วน)"), pValue, LAYOUT_WIDTH / 3);
            }
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                texts = getText("จำนวนที่ชำระ", pValue, LAYOUT_WIDTH / 3);
            } else {
                if (paymentInfo.BalancesOfPeriod == 0) {
                    String txtPeriodAmountLabel = "";
                    if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                        txtPeriodAmountLabel = "ค่างวดที่ %d/%d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "ค่างวดที่ %d/%d";
                    }
                    texts = getText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), pValue, LAYOUT_WIDTH / 3);
                } else {
                    texts = getText(BHUtilities.trim(String.format("ค่างวดที่ %d/%d (ชำระบางส่วน)", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), pValue, LAYOUT_WIDTH / 3);
                }
            }

        }
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            cv.drawText(texts[ii], xTitle, yy, pTitle);
            if (ii == 0) {
                /*** [START] :: Fixed - [BHPROJ-0026-751] :: แก้ไขการแสดงผลในส่วนของ ยอดชำระเงิน ให้เป็นตัวสีแดง + ตัวหนา + เพิ่มขนาดตัวหนังสือมา 1 ระดับี ***/
                if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                    Paint pValueNew = new Paint(pValue);
                    pValueNew.setTextSize(pValueNew.getTextSize() + 1);
                    pValueNew.setTypeface(Typeface.DEFAULT_BOLD);
                    pValueNew.setColor(Color.RED);

                    //cv.drawText(BHUtilities.numericFormat(paymentInfo.Amount) + bahtLabel, xValue, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountNetAmount), xValue, yy, pValueNew);

                    Rect rect = new Rect();
                    pValueNew.getTextBounds(BHUtilities.numericFormat(paymentInfo.CloseAccountNetAmount), 0, BHUtilities.numericFormat(paymentInfo.CloseAccountNetAmount).length(), rect);

                    cv.drawText(bahtLabel, xValue + rect.width(), yy, pValue);

                } else {
                    Paint pValueNew = new Paint(pValue);
                    pValueNew.setTextSize(pValueNew.getTextSize() + 1);
                    pValueNew.setTypeface(Typeface.DEFAULT_BOLD);
                    pValueNew.setColor(Color.RED);

                    //cv.drawText(BHUtilities.numericFormat(paymentInfo.Amount) + bahtLabel, xValue, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.Amount), xValue, yy, pValueNew);

                    Rect rect = new Rect();
                    pValueNew.getTextBounds(BHUtilities.numericFormat(paymentInfo.Amount), 0, BHUtilities.numericFormat(paymentInfo.Amount).length(), rect);

                    cv.drawText(bahtLabel, xValue + rect.width(), yy, pValue);
                }
                /*** [END] :: Fixed - [BHPROJ-0026-751] :: แก้ไขการแสดงผลในส่วนของ ยอดชำระเงิน ให้เป็นตัวสีแดง + ตัวหนา + เพิ่มขนาดตัวหนังสือมา 1 ระดับ***/
            }
        }

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            texts = getText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(paymentInfo.CloseAccountNetAmount)), pValue, LAYOUT_WIDTH - xValue);
            for (int ii = 0; ii < texts.length; ii++) {
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], xValue, yy, pValue);
            }
        } else {
            texts = getText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(paymentInfo.Amount)), pValue, LAYOUT_WIDTH - xValue);
            for (int ii = 0; ii < texts.length; ii++) {
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], xValue, yy, pValue);
            }
        }


        /*yy += fontSize + lineSpace;
        cv.drawText(BHUtilities.ThaiBaht(BHUtilities.numericFormat(paymentInfo.Amount)), xValue, yy, pValue);*/

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {

        } else {
            /**ยอดเงินคงเหลือของงวดนั้น**/
            if (paymentInfo.BalancesOfPeriod != 0) {
                yy += fontSize + lineSpace;
                if (paymentInfo.MODE == 1) {
                    cv.drawText("คงเหลือเงินสด", xTitle, yy, pTitle);
                } else {
                    cv.drawText(String.format("คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), xTitle, yy, pTitle);
                }
                cv.drawText(BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + bahtLabel, xValue, yy, pValue);
            }

            /**ยอดคงเหลือของงวดถัดไป**/
            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                yy += fontSize + lineSpace;

                if (paymentInfo.MODE == 1) {
                    cv.drawText("คงเหลือเงินสด", xTitle, yy, pTitle);
                } else {
                /*if (paymentInfo.BalancesOfPeriod == 0) {
                    if ((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE) {
                        cv.drawText(String.format("คงเหลืองวดที่ %d", paymentInfo.MODE), xTitle, yy, pTitle);
                    } else {
                        cv.drawText(String.format("คงเหลืองวดที่ %d - %d", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), xTitle, yy, pTitle);
                    }
                } else {
                    if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                        cv.drawText(String.format("คงเหลืองวดที่ %d", paymentInfo.MODE), xTitle, yy, pTitle);
                    } else {
                        cv.drawText(String.format("คงเหลืองวดที่ %d - %d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE), xTitle, yy, pTitle);
                    }
                }*/
                    cv.drawText(String.format("คงเหลืองวดที่ %d - %d", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), xTitle, yy, pTitle);
                }
                cv.drawText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + bahtLabel, xValue, yy, pValue);
            }
        }

        switch (Enum.valueOf(PaymentInfo.PaymentType1.class, paymentInfo.PaymentType)) {
            case Cash:
                break;
            case Credit: /**ชำระโดยบัตรเครดิต**/
                yy += fontSize + lineSpace;
                cv.drawText("ชำระโดยบัตรเครดิต", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.trim(paymentInfo.BankName), xValue, yy, pValue);

                yy += fontSize + lineSpace;
                cv.drawText("เลขที่บัตร", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.trim(paymentInfo.CreditCardNumber), xValue, yy, pValue);
                break;
            case Cheque: /**ชำระโดยเช็ค**/
                yy += fontSize + lineSpace;
                cv.drawText("ชำระโดยเช็ค", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.trim(paymentInfo.BankName), xValue, yy, pValue);


                yy += fontSize + lineSpace;
                cv.drawText("สาขา", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.trim(paymentInfo.ChequeBankBranch), xValue, yy, pValue);

                yy += fontSize + lineSpace;
                cv.drawText("เลขที่เช็ค", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.trim(paymentInfo.ChequeNumber), xValue, yy, pValue);

                yy += fontSize + lineSpace;
                cv.drawText("ลงวันที่", xTitle, yy, pTitle);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = formatter.parse(paymentInfo.ChequeDate);
                    cv.drawText(BHUtilities.dateFormat(date), xValue, yy, pValue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }

        pValue.setTextAlign(Align.LEFT);
        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.CENTER);

        /**พนักงานที่ออกใบเสร็จ**/
        yy += 200;
        String sale = String.format("(%s)", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "");
        String team = String.format("(%s)", paymentInfo.TeamCode != null ? paymentInfo.TeamCode : "");
        int ww = getWidth(sale, pValue) + 50;

        cv.drawText(getSignatureUnderline(pSignature, ww), xValue, yy, pSignature);
        cv.drawText("ผู้รับเงิน", xValue + 190, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText(sale, xValue, yy, pSignature);
        yy += fontSize + lineSpace;
        cv.drawText(team, xValue, yy, pSignature);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }


    public static ShortReceiptInfo getShortReceipt(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {

        ShortReceiptInfo shortReceiptInfo = new ShortReceiptInfo();

        Bitmap receiptHeader = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        receiptHeader.setHasAlpha(true);

        Canvas cv = new Canvas(receiptHeader);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getShortHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += 210;
        header.recycle();

        float fontSize = 60;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบรับเงิน", LAYOUT_WIDTH / 2, yy, p);


        Bitmap resultHeader = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(resultHeader);
        cv.drawBitmap(receiptHeader, 0, 0, null);
        receiptHeader.recycle();

        shortReceiptInfo.receiptHeader = scaleBitmapByWidth(resultHeader);

        //zone detail
        String detail = "\n";
        detail += "Receipt No. " + paymentInfo.ReceiptCode + " (VAT Included)\n";
        detail += "Receipt Date. " + BHUtilities.dateFormat(paymentInfo.PayDate) + "\n";
        detail += "Contract No. " + paymentInfo.CONTNO;

        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            detail += "Ref No. " + ManualDocumentBookRunningNo;
        }
        detail += "\n";
        //detail += paymentInfo.ProductName;

        shortReceiptInfo.receiptDetail1 = detail;

        //zone detail bitmap
        Bitmap receiptDetail = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        receiptDetail.setHasAlpha(true);

        cv = new Canvas(receiptDetail);
        cv.drawColor(Color.WHITE);

        yy = 21;

        fontSize = 42;
        float lineSpace = fontSize / 2;
        int xTitle = 1;

        yy += lineSpace;
        cv.drawColor(Color.WHITE);

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);


        cv.drawText(paymentInfo.ProductName, xTitle, yy, pValue);
        /**ยอดเงินที่จ่ายมาตามใบเสร็จ**/
        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
                // detail +=BHUtilities.trim("งวด 1");

                cv.drawText(BHUtilities.trim("งวด 1"), 650, yy, pValue);
            }
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            } else {
                if (paymentInfo.BalancesOfPeriod == 0) {
                    //detail += BHUtilities.trim(String.format("งวด %d/%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE))+"\n";
                    cv.drawText(BHUtilities.trim(String.format("งวด %d/%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), 650, yy, pValue);
                }
            }

        }
        yy += lineSpace;

        Bitmap resultDetail = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(resultDetail);
        cv.drawBitmap(receiptDetail, 0, 0, null);
        receiptDetail.recycle();

        shortReceiptInfo.receiptDetail = scaleBitmapByWidth(resultDetail);

        detail = "";    //clear

        detail += ThemalPrintController.calculateLeght("", "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท\n");

        detail += ThemalPrintController.calculateLeght("", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท\n");


        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {

        } else {
            /**ยอดเงินคงเหลือของงวดนั้น**/
            if (paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {

                } else {
                    detail += String.format("คงขาด งวด %d", paymentInfo.PaymentPeriodNumber) + " " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท";

                }

            }

            /**ยอดคงเหลือของงวดถัดไป**/
            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {

                if (paymentInfo.MODE == 1) {
                } else {
                    detail += String.format("คงขาด งวด %d-%d", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE) + " " + BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท";
                }

            }
        }

        shortReceiptInfo.receiptDetail2 = detail;


        //zone tailer
        Bitmap receiptTailer = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        receiptTailer.setHasAlpha(true);

        cv = new Canvas(receiptTailer);
        cv.drawColor(Color.WHITE);

        yy = 0;

        fontSize = 42;

        int xValue = 200;

        String bahtLabel = " บาท";

        pValue.setTextAlign(Align.LEFT);
        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.LEFT);

        String sale = String.format("%s", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "");
        String team = String.format("%s", paymentInfo.TeamCode != null ? paymentInfo.TeamCode : "");

        yy += fontSize + lineSpace;
        cv.drawText("ผุ้รับเงิน", xTitle, yy, pValue);
        cv.drawText(sale, xValue, yy, pValue);

        yy += lineSpace;

        String[] texts = new String[]{""};
        texts = getText(debtorCustomerInfo.CustomerFullName(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ชื่อลูกค้า", xTitle, yy, pValue);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        yy += LINE_FEED;


        Bitmap resultTailer = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(resultTailer);
        cv.drawBitmap(receiptTailer, 0, 0, null);
        receiptTailer.recycle();

        shortReceiptInfo.receiptTailer = scaleBitmapByWidth(resultTailer);

        return shortReceiptInfo;

    }

    public static ShortReceiptInfo getTextReceipt(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {
        ShortReceiptInfo shortReceiptInfo = new ShortReceiptInfo();


        /*shortReceiptInfo.listTxt.add("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)");
        shortReceiptInfo.listTxt.add("เลขประจำตัวผู้เสียภาษี 0107556000213");
        shortReceiptInfo.listTxt.add("ใบรับเงิน");*/


        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateCenter("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)"));
        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateCenter("เลขประจำตัวผู้เสียภาษี 0107556000213"));
        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateCenter("ใบรับเงิน"));

        shortReceiptInfo.listTxt.add(ThemalPrintController.getBetweenSpace("เลขที่ใบเสร็จ", paymentInfo.ReceiptCode + " (รวม Vat)"));
        shortReceiptInfo.listTxt.add(ThemalPrintController.getBetweenSpace("วันที่รับเงิน", (BHUtilities.dateFormat(paymentInfo.PayDate) + "")));
        shortReceiptInfo.listTxt.add(ThemalPrintController.getBetweenSpace("เลขที่สัญญา", paymentInfo.CONTNO));

        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            shortReceiptInfo.listTxt.add(ThemalPrintController.getBetweenSpace("อ้างอิงสัญญาเลขที่", ManualDocumentBookRunningNo + ""));
        }


        ThemalPrintController.addLongText(shortReceiptInfo.listTxt, paymentInfo.ProductName);

        /**ยอดเงินที่จ่ายมาตามใบเสร็จ**/
        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
                // detail +=BHUtilities.trim("งวด 1");

                shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim("งวด 1 (ชำระครบ)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                //shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("    ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            } else {
                shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim("งวด 1 (ชำระบางส่วน)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            }
            shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("        ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                    shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim(String.format("งวด %d", paymentInfo.MODE)), BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท"));
                } else {
                    shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim(String.format("งวด %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท"));
                }
            } else {
                String txtPeriodAmountLabel = "";
                if (paymentInfo.BalancesOfPeriod == 0) {
                    //detail += BHUtilities.trim(String.format("งวด %d/%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE))+"\n";

                    if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                        txtPeriodAmountLabel = "งวด %d/%d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "งวด %d/%d";
                    }
                    shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                    //shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("        ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                } else {
                    txtPeriodAmountLabel = "งวด %d (ชำระบางส่วน)";
                    shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                }
                shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("        ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            }
        }

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("ส่วนลดตัดสด", BHUtilities.numericFormat(paymentInfo.CloseAccountDiscountAmount) + " บาท"));
            shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("จำนวนที่ชำระ", BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount - paymentInfo.CloseAccountDiscountAmount) + " บาท"));

        } else {
            /**ยอดเงินคงเหลือของงวดนั้น**/
            if (paymentInfo.BalancesOfPeriod != 0) {
                shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), "ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท"));
                shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght("วันนัดชำระ", BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate)));
            }

            /**ยอดคงเหลือของงวดถัดไป**/
            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {
                } else {
                    if ((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE) {
                        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงขาด งวด %d", paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));
                    } else {
                        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงขาด งวด %d-%d", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));
                    }
                }
            }

            /**ยอดเงินคงเหลือของงวดนั้น**/
            /*if (paymentInfo.Balances != 0) {
                if (paymentInfo.MODE == 1) {
                    shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงขาด งวด %d", paymentInfo.PaymentPeriodNumber), BHUtilities.numericFormat(paymentInfo.Balances) + " บาท"));
                } else {
                    if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE){
                        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงขาด งวด %d", paymentInfo.PaymentPeriodNumber), BHUtilities.numericFormat(paymentInfo.Balances) + " บาท"));
                    } else{
                        int paymentPeriodNumber = paymentInfo.PaymentPeriodNumber;
                        if(paymentInfo.BalancesOfPeriod == 0){
                            paymentPeriodNumber++;
                        }
                        shortReceiptInfo.listTxt.add(ThemalPrintController.calculateLeght(String.format("คงขาด งวด %d-%d", paymentPeriodNumber, paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances) + " บาท"));
                    }
                }
            }*/
        }

        String sale = String.format("%s", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "");
        String team = String.format("%s", paymentInfo.TeamCode != null ? paymentInfo.TeamCode : "");


        ThemalPrintController.addLongText(shortReceiptInfo.listTxt, "ผู้รับเงิน " + sale);
        ThemalPrintController.addLongText(shortReceiptInfo.listTxt, "ชื่อลูกค้า " + debtorCustomerInfo.CustomerFullName());


        return shortReceiptInfo;
    }


    public static List<PrintTextInfo> getTextReceiptNew(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {
        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextShortHeader());
        listText.add(new PrintTextInfo("selectPageMode"));
        //listText.add(new PrintTextInfo("setPageRegion(0, 0, 570, 650 ,alignLeft)"));
        listText.add(new PrintTextInfo("beginPage(0, 4)"));
        listText.addAll(getTextAlignCenter("ใบรับเงิน", PrintTextInfo.FontType.Bold));
        listText.add(new PrintTextInfo("printTitleBackground(0, 0, 570, 64)"));
        listText.add(new PrintTextInfo("beginPage(0, 66)"));
        //listText.add(new PrintTextInfo("printFrame(0, 0, 570, 650)"));
        /** [Start] Fixed - [BHPROJ-0026-3267][Android-ใบเสร็จรับเงินที่พิมพ์ออกมา] ปรับรูปแบบใบเสร็จตามรายละเอียดที่แจ้งมาในรูป **/
        listText.addAll(getTextAlignLeftByOffSetLeft(" วันที่รับเงิน", BHUtilities.dateFormat(paymentInfo.PayDate) + " เวลา " + BHUtilities.dateFormat(paymentInfo.PayDate, "HH:mm") + " น."));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่", paymentInfo.ReceiptCode));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่สัญญา", paymentInfo.CONTNO));
        try {
            listText.addAll(getTextAlignLeft(" ชื่อลูกค้า " + debtorCustomerInfo.CustomerFullName()));
        } catch (Exception e){
            listText.addAll(getTextAlignLeft(" ชื่อลูกค้า " + paymentInfo.CustomerName));
        }



        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            listText.addAll(getTextAlignLeftByOffSetLeft(" อ้างอิงสัญญาเลขที่", ManualDocumentBookRunningNo));
        }


        listText.addAll(getTextAlignLeft(" " + paymentInfo.ProductName));

        /**ยอดเงินที่จ่ายมาตามใบเสร็จ**/
        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
                listText.addAll(getTextAlignLeftRight(BHUtilities.trim(" งวด 1 (ชำระครบ)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            } else {
                listText.addAll(getTextAlignLeftRight(BHUtilities.trim(" งวด 1 (ชำระบางส่วน)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            }
            listText.addAll(getTextAlignLeftRight(" ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                    listText.addAll(getTextAlignLeftRight(BHUtilities.trim(String.format(" งวด %d", paymentInfo.MODE)), BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท"));
                } else {
                    listText.addAll(getTextAlignLeftRight(BHUtilities.trim(String.format(" งวด %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท"));
                }
            } else {
                String txtPeriodAmountLabel = "";
                if (paymentInfo.BalancesOfPeriod == 0) {
                    if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                        txtPeriodAmountLabel = "ชำระงวดที่ %d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "ชำระงวดที่ %d";
                    }
                    listText.addAll(getTextAlignLeftRight(" " + BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                } else {
                    txtPeriodAmountLabel = " งวด %d (ชำระบางส่วน)";
                    listText.addAll(getTextAlignLeftRight(" " + BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
                }
                //listText.addAll(getTextAlignLeftRight("", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            }
        }

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            listText.addAll(getTextAlignLeftRight(" ส่วนลดตัดสด", BHUtilities.numericFormat(paymentInfo.CloseAccountDiscountAmount) + " บาท"));
            listText.addAll(getTextAlignLeftRight(" จำนวนที่ชำระ", BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount - paymentInfo.CloseAccountDiscountAmount) + " บาท"));
        } else {
            /**ยอดเงินคงเหลือของงวดนั้น**/
            if (paymentInfo.BalancesOfPeriod != 0) {
                listText.addAll(getTextAlignLeftRight(String.format(" คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), "ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท"));
                listText.addAll(getTextAlignLeftRight(" วันนัดชำระ", BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate)));
            }

            /**ยอดคงเหลือของงวดถัดไป**/
            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {
                } else {
                    if ((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE) {
                        listText.addAll(getTextAlignLeftRight(String.format(" คงเหลือ งวดที่ %d เป็นเงิน", paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));
                    } else {
                        listText.addAll(getTextAlignLeftRight(String.format(" คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));
                    }
                }
            }
        }

        String sale = String.format("%s", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "");
        //String team = String.format("%s", paymentInfo.TeamCode != null ? paymentInfo.TeamCode : "");

        listText.addAll(getTextAlignCenter("  "));
        listText.addAll(getTextAlignCenter(" ____________________________ผู้รับเงิน"));
        listText.addAll(getTextAlignCenter(String.format(" (%s) %s", sale, paymentInfo.CashCode)));
        /** [End] Fixed - [BHPROJ-0026-3267][Android-ใบเสร็จรับเงินที่พิมพ์ออกมา] ปรับรูปแบบใบเสร็จตามรายละเอียดที่แจ้งมาในรูป **/

        listText.add(new PrintTextInfo(""));
        listText.add(new PrintTextInfo("logoTSRTelsale"));
        listText.add(new PrintTextInfo("endPage"));
        listText.add(new PrintTextInfo("selectStandardMode"));
        return listText;
    }

    public static Bitmap getImageReceiptNew(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {

        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        float fontSize = 44;

        Bitmap header = getImageShortHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();


        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);

        yy += fontSize * 2;
        cv.drawText("ใบรับเงิน", LAYOUT_WIDTH / 2, yy, p);


        float lineSpace = fontSize;//fontSize / 2;
        int xTitle = 1;
        int xValue = 480;

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);

        String[] texts = new String[]{""};

        /** [Start] Fixed - [BHPROJ-0026-3267][Android-ใบเสร็จรับเงินที่พิมพ์ออกมา] ปรับรูปแบบใบเสร็จตามรายละเอียดที่แจ้งมาในรูป **/
        yy += fontSize + lineSpace;
        cv.drawText("วันที่รับเงิน", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(paymentInfo.PayDate) + " เวลา " + BHUtilities.dateFormat(paymentInfo.PayDate, "HH:mm") + " น.", xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่", xTitle, yy, pTitle);
        cv.drawText(paymentInfo.ReceiptCode, xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitle);
        cv.drawText(paymentInfo.CONTNO, xValue, yy, pValue);

        texts = getText(debtorCustomerInfo.CustomerFullName(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ชื่อลูกค้า", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            yy += fontSize + lineSpace;
            cv.drawText("อ้างอิงสัญญาเลขที่", xTitle, yy, pTitle);
            cv.drawText(ManualDocumentBookRunningNo, xValue, yy, pValue);
        }

        texts = getText(paymentInfo.ProductName, pTitle, LAYOUT_WIDTH - xTitle);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            cv.drawText(texts[ii], xTitle, yy, pTitle);
        }

        /**ยอดเงินที่จ่ายมาตามใบเสร็จ**/
        pValue.setTextAlign(Align.RIGHT);
        int xValueAlignRIGHT = LAYOUT_WIDTH - 5;

        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
                yy += fontSize + lineSpace;
                cv.drawText(BHUtilities.trim("งวด 1 (ชำระครบ)"), xTitle, yy, pTitle);
                cv.drawText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", xValueAlignRIGHT, yy, pValue);
            } else {
                yy += fontSize + lineSpace;
                cv.drawText(BHUtilities.trim("งวด 1 (ชำระบางส่วน)"), xTitle, yy, pTitle);
                cv.drawText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", xValueAlignRIGHT, yy, pValue);
            }

            yy += fontSize + lineSpace;
            cv.drawText("รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", xValueAlignRIGHT, yy, pValue);
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                    yy += fontSize + lineSpace;
                    cv.drawText(BHUtilities.trim(String.format("งวด %d", paymentInfo.MODE)), xTitle, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท", xValueAlignRIGHT, yy, pValue);
                } else {
                    yy += fontSize + lineSpace;
                    cv.drawText(BHUtilities.trim(String.format("งวด %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), xTitle, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท", xValueAlignRIGHT, yy, pValue);
                }
            } else {
                String txtPeriodAmountLabel = "";
                if (paymentInfo.BalancesOfPeriod == 0) {
                    if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                        txtPeriodAmountLabel = "ชำระงวดที่ %d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "ชำระงวดที่ %d";
                    }

                    yy += fontSize + lineSpace;
                    cv.drawText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), xTitle, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", xValueAlignRIGHT, yy, pValue);
                } else {
                    txtPeriodAmountLabel = "งวด %d (ชำระบางส่วน)";

                    yy += fontSize + lineSpace;
                    cv.drawText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), xTitle, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", xValueAlignRIGHT, yy, pValue);
                }
                //listText.addAll(getTextAlignLeftRight("", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
            }
        }

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            yy += fontSize + lineSpace;
            cv.drawText("ส่วนลดตัดสด", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountDiscountAmount) + " บาท", xValueAlignRIGHT, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("จำนวนที่ชำระ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount - paymentInfo.CloseAccountDiscountAmount) + " บาท", xValueAlignRIGHT, yy, pValue);
        } else {
            /**ยอดเงินคงเหลือของงวดนั้น**/
            if (paymentInfo.BalancesOfPeriod != 0) {
                yy += fontSize + lineSpace;
                cv.drawText(String.format("คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), xTitle, yy, pTitle);
                cv.drawText("ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท", xValueAlignRIGHT, yy, pValue);

                yy += fontSize + lineSpace;
                cv.drawText("วันนัดชำระ", xTitle, yy, pTitle);
                cv.drawText(BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate), xValueAlignRIGHT, yy, pValue);

            }

            /**ยอดคงเหลือของงวดถัดไป**/
            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {
                } else {
                    if ((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE) {
                        yy += fontSize + lineSpace;
                        cv.drawText(String.format("คงเหลือ งวดที่ %d เป็นเงิน", paymentInfo.MODE), xTitle, yy, pTitle);
                        cv.drawText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", xValueAlignRIGHT, yy, pValue);
                    } else {
                        yy += fontSize + lineSpace;
                        cv.drawText(String.format("คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), xTitle, yy, pTitle);
                        cv.drawText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", xValueAlignRIGHT, yy, pValue);
                    }
                }
            }
        }


        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.CENTER);

        String sale = String.format("%s", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "");
        //String team = String.format("%s", paymentInfo.TeamCode != null ? paymentInfo.TeamCode : "");

        yy += fontSize * 4;
        cv.drawText("____________________________ผู้รับเงิน", LAYOUT_WIDTH / 2, yy, pSignature);

        yy += fontSize + lineSpace;
        cv.drawText(String.format("(%s) %s", sale, paymentInfo.CashCode), LAYOUT_WIDTH / 2, yy, pSignature);
        /** [End] Fixed - [BHPROJ-0026-3267][Android-ใบเสร็จรับเงินที่พิมพ์ออกมา] ปรับรูปแบบใบเสร็จตามรายละเอียดที่แจ้งมาในรูป **/

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);

    }


    public static Bitmap getReturnProduct(List<ReturnProductDetailInfo> returnList, int newHeight) {

        int defaultHeight = 3000;
        Bitmap img;
        if (newHeight == 0) {
            img = Bitmap.createBitmap(LAYOUT_WIDTH, defaultHeight, Config.ARGB_8888);
        } else {
            img = Bitmap.createBitmap(LAYOUT_WIDTH, newHeight, Config.ARGB_8888);
        }

        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบส่งคืนสินค้า", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        int xTitle = 1;
        int xValue = 480;


        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        yy += fontSize * 3;
        cv.drawText("เลขที่ใบส่งคืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(returnList.get(0).ReturnProductID), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("วันที่ส่งคืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(returnList.get(0).ReturnDate), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("พนักงานที่ส่งคืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(String.format("%s %s", BHUtilities.trim(returnList.get(0).EmpID), BHUtilities.trim(returnList.get(0).EmployeeName)), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("ทีมงานที่คืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(returnList.get(0).TeamCode), xValue, yy, pValue);

        String status = returnList.get(0).Status;

        if (status.equals(ReturnProductStatus.REQUEST.toString())) {
            status = "รอการตรวจสอบสินค้า";
        } else if (status.equals(ReturnProductStatus.REJECT.toString())) {
            status = "ไม่ผ่านการตรวจสอบสินค้า";
        } else if (status.equals(ReturnProductStatus.PASS.toString())) {
            status = "ผ่านการตรวจสอบสินค้า";
        }

        yy += fontSize + lineSpace;
        cv.drawText("สถานะการรับคืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(status), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("หมายเหตุการรับคืนสินค้า", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(returnList.get(0).Remark), xValue, yy, pValue);

        yy += fontSize * 2;
        cv.drawText("รายละเอียดสินค้าที่ส่งคืน (" + returnList.size() + " รายการ)", xTitle, yy, pTitle);

        for (ReturnProductDetailInfo info : returnList) {

            yy += fontSize + lineSpace;
            cv.drawText("รหัสสินค้า", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.trim(info.ProductSerialNumber), xValue, yy, pValue);

        }

        yy += LINE_FEED;

        Bitmap result;
        if (defaultHeight > yy && newHeight == 0) {
            result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            return scaleBitmapByWidth(result);
        } else if (newHeight != 0) {
            result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            return scaleBitmapByWidth(result);
        } else {
            img.recycle();
            return getReturnProduct(returnList, Math.round(yy));
        }
    }

    public static List<PrintTextInfo> getTextReturnProduct(List<ReturnProductDetailInfo> returnList) {
        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextHeader());
        listText.addAll(getTextAlignCenter("ใบส่งคืนสินค้า", PrintTextInfo.FontType.Bold));

        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่ใบส่งคืนสินค้า", BHUtilities.trim(returnList.get(0).ReturnProductID)));
        listText.addAll(getTextAlignLeftByOffSetLeft("วันที่ส่งคืนสินค้า", BHUtilities.dateFormat(returnList.get(0).ReturnDate)));
        listText.addAll(getTextAlignLeftByOffSetLeft("พนักงานที่ส่งคืนสินค้า", String.format("%s %s", BHUtilities.trim(returnList.get(0).EmpID), BHUtilities.trim(returnList.get(0).EmployeeName))));
        listText.addAll(getTextAlignLeftByOffSetLeft("ทีมงานที่คืนสินค้า", BHUtilities.trim(returnList.get(0).TeamCode)));

        String status = returnList.get(0).Status;
        if (status.equals(ReturnProductStatus.REQUEST.toString())) {
            status = "รอการตรวจสอบสินค้า";
        } else if (status.equals(ReturnProductStatus.REJECT.toString())) {
            status = "ไม่ผ่านการตรวจสอบสินค้า";
        } else if (status.equals(ReturnProductStatus.PASS.toString())) {
            status = "ผ่านการตรวจสอบสินค้า";
        }

        listText.addAll(getTextAlignLeftByOffSetLeft("สถานะการรับคืนสินค้า", BHUtilities.trim(status)));
        listText.addAll(getTextAlignLeftByOffSetLeft("หมายเหตุการรับคืนสินค้า", BHUtilities.trim(returnList.get(0).Remark)));

        listText.addAll(getTextAlignLeft("รายละเอียดสินค้าที่ส่งคืน (" + returnList.size() + " รายการ)"));
        for (ReturnProductDetailInfo info : returnList) {
            listText.addAll(getTextAlignLeftByOffSetLeft("รหัสสินค้า", BHUtilities.trim(info.ProductSerialNumber)));
        }

        /*** [START] :: Fixed-[BHPROJ-0024-3214] :: [LINE-25/07/2016][Android-ระบบส่งคืนสินค้าเข้าระบบ] เพิ่มเติมส่วนที่ให้ผู้ส่งคืนเซ็นชื่อในตอนพิมพ์ ***/
        listText.addAll(getTextAlignCenter("...............ผู้ส่ง", PrintTextInfo.FontType.Normal));
        listText.addAll(getTextAlignCenter(String.format("( %s )", BHUtilities.trim(returnList.get(0).EmployeeName))));
        /*** [END] :: Fixed-[BHPROJ-0024-3214] :: [LINE-25/07/2016][Android-ระบบส่งคืนสินค้าเข้าระบบ] เพิ่มเติมส่วนที่ให้ผู้ส่งคืนเซ็นชื่อในตอนพิมพ์ ***/

        return listText;
    }

    public static Bitmap getSendMoney(SendMoneyInfo sendMoney) {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);

        yy += fontSize + LINE_SPACE;
        cv.drawText(String.format("ใบนำส่ง%s", sendMoney.PaymentTypeName), LAYOUT_WIDTH / 2, yy, p);
        yy += fontSize + LINE_SPACE * 3;

        Paint SubTitle = new Paint(p);
        SubTitle.setTextSize(65);
        cv.drawText(String.format("(%s)", sendMoney.ChannelItemName), LAYOUT_WIDTH / 2, yy, SubTitle);

        yy += 50;

        Bitmap barcode;
        String barcodeNum;

        /*** [START] :: Fixed - [BHPROJ-0024-2061] :: [Android-พิมพ์ใบนำส่งเงิน] กรณีพิมพ์ใบนำส่งเงินออกเครื่อง Printer มีการส่งค่า Barcode ออกมาไม่ครบถ้วน  ***/
        /*if (sendMoney.ChannelCode.equals("HeadOffice")) {
            barcode = BHBarcode.generateCode128(sendMoney.Reference2, 850, 300);
            barcodeNum = sendMoney.Reference2;
        } else {
            barcode = BHBarcode.generateCode128(sendMoney.AccountCode1, 850, 300);
            barcodeNum = sendMoney.AccountCode1;
        }*/

        barcode = BHBarcode.generateCode128(sendMoney.Reference1, 850, 300);
        barcodeNum = sendMoney.Reference1;
        /*** [END] :: Fixed - [BHPROJ-0024-2061] :: [Android-พิมพ์ใบนำส่งเงิน] กรณีพิมพ์ใบนำส่งเงินออกเครื่อง Printer มีการส่งค่า Barcode ออกมาไม่ครบถ้วน ***/

        if (barcode != null) {
            cv.drawBitmap(barcode, (LAYOUT_WIDTH - barcode.getWidth()) / 2, yy, null);
            yy += barcode.getHeight();
        }

        fontSize = 42;
        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);

        yy += fontSize + 10;
        cv.drawText(barcodeNum, LAYOUT_WIDTH / 2, yy, pTitle);
        pTitle.setTextAlign(Align.LEFT);
        yy += fontSize * 2;

        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        cv.drawText("ชื่อผู้ส่งเงิน :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(sendMoney.FirstName + " " + sendMoney.LastName), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("รหัสผู้ส่ง :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(sendMoney.CreateBy), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("วันที่ส่ง :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(sendMoney.SendDate, BHUtilities.DEFAULT_DATE_FORMAT), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่อ้างอิง :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(sendMoney.Reference2), xValue, yy, pValue);

//        if (sendMoney.PaymentType.equals("Credit")) {
//
//            yy += fontSize + lineSpace;
//            cv.drawText("ธนาคาร :", xTitle, yy, pTitle);
//            cv.drawText(BHUtilities.trim(sendMoney.PayeeName), xValue, yy,
//                    pValue);
//        }

        yy += fontSize + lineSpace;
        cv.drawText("ชื่อผู้รับเงิน/สาขา :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(sendMoney.PayeeName), xValue, yy, pValue);

        if (sendMoney.PaymentType.toLowerCase().equals("credit")) {
            yy += fontSize + lineSpace;
            cv.drawText("เชลล์สลิป :", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท", xValue, yy, pValue);
        } else if (sendMoney.PaymentType.toLowerCase().equals("cheque")) {
            yy += fontSize + lineSpace;
            cv.drawText("เช็ค :", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท", xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("จำนวนเงิน :", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท", xValue, yy, pValue);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }

    public static List<PrintTextInfo> getTextSendMoney(SendMoneyInfo sendMoney) {

        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextHeader());
        listText.add(new PrintTextInfo("selectPageMode"));
        listText.add(new PrintTextInfo("setPageRegion(0, 0, 570, 1000 ,alignLeft)"));
        listText.add(new PrintTextInfo("beginPage(0, 66)"));
        listText.addAll(getTextAlignCenter(String.format("ใบนำส่ง%s", sendMoney.PaymentTypeName), PrintTextInfo.FontType.Bold));

        listText.addAll(getTextAlignCenter(String.format("(%s)", sendMoney.ChannelItemName), PrintTextInfo.FontType.Bold));
        if (sendMoney.Reference2.length() > 8) {
            String strBarcodeNo = sendMoney.Reference2;
            listText.add(new PrintTextInfo(strBarcodeNo, PrintTextInfo.FontType.Normal, true));
        }

        listText.addAll(getTextAlignLeftByOffSetLeft(" ชื่อผู้ส่งเงิน :", BHUtilities.trim(sendMoney.FirstName + " " + sendMoney.LastName)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" รหัสผู้ส่ง :", BHUtilities.trim(sendMoney.CreateBy)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" วันที่ส่ง :", BHUtilities.dateFormat(sendMoney.SendDate, BHUtilities.DEFAULT_DATE_FORMAT)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" Ref. 1 :", BHUtilities.trim(sendMoney.Reference1)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" Ref. 2 :", BHUtilities.trim(sendMoney.Reference2)));
        listText.addAll(getTextAlignLeftByOffSetLeft(" ชื่อผู้รับเงิน/สาขา :", BHUtilities.trim(sendMoney.PayeeName)));

        if (sendMoney.PaymentType.toLowerCase().equals("credit")) {
            listText.addAll(getTextAlignLeftByOffSetLeft(" เชลล์สลิป :", BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท"));
        } else if (sendMoney.PaymentType.toLowerCase().equals("cheque")) {
            listText.addAll(getTextAlignLeftByOffSetLeft(" เช็ค :", BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท"));
        }
        listText.addAll(getTextAlignLeftByOffSetLeft(" จำนวนเงิน :", BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท"));
//
//        listText.add(new PrintTextInfo("setPageRegion(470, 0, 100, 1000 ,alignBottom)"));
//        listText.add(new PrintTextInfo("beginPage(0, 4)"));
//        String SendMoneyBarcode = String.format("%s|%s|%s", sendMoney.Reference1, "", BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", ""));
//        listText.add(new PrintTextInfo(SendMoneyBarcode, PrintTextInfo.FontType.Normal, true, true, 2));
//        listText.addAll(getTextAlignLeft(String.format(" | 010755600021300 %s %s", sendMoney.Reference1, BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", "")), "EN"));
//        //listText.addAll(getTextAlignCenter("", PrintTextInfo.FontType.Normal));

        listText.add(new PrintTextInfo("endPage"));
        listText.add(new PrintTextInfo("selectStandardMode"));

        if (sendMoney.Reference2.length() == 8) {

            listText.addAll(getTextAlignLeft("----------------------------------------", "EN"));
            listText.addAll(getTextAlignLeft(" สำหรับธนาคาร", PrintTextInfo.FontType.Normal));
            String SendMoneyBarcode = String.format("%s|%s|%s", sendMoney.Reference1, "", BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", ""));
            listText.add(new PrintTextInfo(SendMoneyBarcode, PrintTextInfo.FontType.Normal, true, true));
            listText.addAll(getTextAlignLeft(String.format(" | 010755600021300 %s %s", sendMoney.Reference1, BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", "")), "EN"));
            listText.addAll(getTextAlignCenter("", PrintTextInfo.FontType.Normal));
            listText.addAll(getTextAlignLeft(String.format(" เพื่อเข้าบัญชี %s", sendMoney.ChannelItemName)));
            listText.addAll(getTextAlignLeft(String.format(" EMPID(Ref.1): %s", BHUtilities.trim(sendMoney.Reference1)), "EN"));
            listText.addAll(getTextAlignLeft(" ", "EN"));
            listText.addAll(getTextAlignLeft(String.format(" TNSNO(Ref.2): %s", BHUtilities.trim(sendMoney.Reference2)), "EN"));
            listText.addAll(getTextAlignLeft(String.format(" จำนวนเงิน %s บาท", BHUtilities.numericFormat(sendMoney.SendAmount))));

            //listText.addAll(getTextAlignLeft(String.format("%s %s", sendMoney.Reference2, BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", ""))));
            //listText.add(new PrintTextInfo("selectStandardMode"));
        }
        return listText;
    }

    public static Bitmap getSendDocument(SendDocumentInfo sendDocument) {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบส่งคืนเอกสาร", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        int xTitle = 1;
        int xValue = 480;

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy,
                Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }


    public static Bitmap getDocumentHistory(List<DocumentHistoryInfo> outputContract,
                                            List<DocumentHistoryInfo> outputReceipt,
                                            List<DocumentHistoryInfo> outputChangeProduct,
                                            List<DocumentHistoryInfo> outputImpoundProduct,
                                            List<DocumentHistoryInfo> outputChangeContract,
                                            List<DocumentHistoryInfo> outputManualDocument,
                                            List<DocumentHistoryInfo> outputPayInSlipBank,
                                            List<DocumentHistoryInfo> outputPayInSlipPayPoint,
                                            int newHeight) {
        int num = 0;
        int defaultHeight = 3000;
        Bitmap img;
        if (newHeight == 0) {
            img = Bitmap.createBitmap(LAYOUT_WIDTH, defaultHeight, Config.ARGB_8888);
        } else {
            img = Bitmap.createBitmap(LAYOUT_WIDTH, newHeight, Config.ARGB_8888);
        }

        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบส่งเอกสาร", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pTitleL = new Paint(p);
        pTitleL.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleL.setTextSize(fontSize);
        pTitleL.setTextAlign(Align.LEFT);

        Paint pTitleR = new Paint(p);
        pTitleR.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleR.setTextSize(fontSize);
        pTitleR.setTextAlign(Align.RIGHT);

        Paint pValue = new Paint(p);
        pValue.setTypeface(null);
        pValue.setTextSize(fontSize);
        pValue.setTextAlign(Align.RIGHT);

        yy += fontSize * 3;
        cv.drawText("รายการส่งเอกสาร ประจำวันที่", xTitle, yy, pTitleL);
        cv.drawText(BHUtilities.dateFormat(new Date()), LAYOUT_WIDTH - 10, yy, pValue);


        EmployeeInfo outputEmp = TSRController.getEmployeeByID(BHPreference.employeeID());
        if (outputEmp != null) {
            yy += fontSize + lineSpace;
            cv.drawText("พนักงาน", xTitle, yy, pTitleL);
            cv.drawText(outputEmp.FullName(), LAYOUT_WIDTH - 10, yy, pValue);
        }

        Paint pListView1 = new Paint(p);
        pListView1.setTypeface(null);
        pListView1.setTextSize(fontSize);
        pListView1.setTextAlign(Align.CENTER);

        Paint pListView2 = new Paint(p);
        pListView2.setTypeface(null);
        pListView2.setTextSize(fontSize);
        pListView2.setTextAlign(Align.LEFT);

        Paint pListView3 = new Paint(p);
        pListView3.setTypeface(null);
        pListView3.setTextSize(fontSize);
        pListView3.setTextAlign(Align.RIGHT);

        String[] texts = new String[]{""};
        if (outputContract != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารใบสัญญา", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputContract.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputContract) {
                num++;
                texts = getText(info.CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.DocumentNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputReceipt != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารใบเสร็จ", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputReceipt.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputReceipt) {
                num++;
                texts = getText(info.CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.DocumentNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputChangeProduct != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารใบเปลี่ยนเครื่อง", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputChangeProduct.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputChangeProduct) {
                num++;
                texts = getText(info.CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.DocumentNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputImpoundProduct != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารใบถอดเครื่อง", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputImpoundProduct.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputImpoundProduct) {
                num++;
                texts = getText(info.CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.DocumentNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputChangeContract != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารใบเปลี่ยนสัญญา", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputChangeContract.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputChangeContract) {
                num++;
                texts = getText(info.CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.DocumentNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputManualDocument != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสารมือ", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputManualDocument.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputManualDocument) {
                num++;
                if (info.ManualDocumentTypeID.equals("0")) {
                    texts = getText(info.dc0CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                    for (int ii = 0; ii < texts.length; ii++) {
                        yy += fontSize + lineSpace;
                        if (ii == 0) {
                            cv.drawText(info.No, xTitle + 50, yy, pListView1);
                            cv.drawText(info.con0CONTNO, xTitle + 100, yy, pListView2);
                        }
                        cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                    }
                } else if (info.ManualDocumentTypeID.equals("1")) {
                    texts = getText(info.dc1CustomerFullName, pListView3, LAYOUT_WIDTH - 650);
                    for (int ii = 0; ii < texts.length; ii++) {
                        yy += fontSize + lineSpace;
                        if (ii == 0) {
                            cv.drawText(info.No, xTitle + 50, yy, pListView1);
                            cv.drawText(info.re1ReceiptCode, xTitle + 100, yy, pListView2);
                        }
                        cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                    }
                }
            }
        }

        if (outputPayInSlipBank != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสาร Slip ธนาคาร", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputPayInSlipBank.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputPayInSlipBank) {
                num++;
                texts = getText(info.employeeFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.TransactionNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        if (outputPayInSlipPayPoint != null) {
            yy += fontSize + fontSize;
            cv.drawText("เอกสาร Slip เพย์พอยท์", xTitle, yy, pTitleL);
            cv.drawText(String.format("%d ใบ", outputPayInSlipPayPoint.size()), LAYOUT_WIDTH - 10, yy, pTitleR);
            for (DocumentHistoryInfo info : outputPayInSlipPayPoint) {
                num++;
                texts = getText(info.employeeFullName, pListView3, LAYOUT_WIDTH - 650);
                for (int ii = 0; ii < texts.length; ii++) {
                    yy += fontSize + lineSpace;
                    if (ii == 0) {
                        cv.drawText(info.No, xTitle + 50, yy, pListView1);
                        cv.drawText(info.TransactionNo, xTitle + 100, yy, pListView2);
                    }
                    cv.drawText(texts[ii], LAYOUT_WIDTH - 10, yy, pListView3);
                }
            }
        }

        yy += fontSize + fontSize;
        cv.drawText("จำนวนเอกสารทั้งหมด", xTitle, yy, pTitleL);
        cv.drawText(String.format("%d ใบ", num), LAYOUT_WIDTH - 10, yy, pTitleR);

        yy += fontSize + fontSize;

        Bitmap barcode;
        String strBarcodeNo = BHPreference.teamCode() + "|" + BHPreference.SubTeamCode() + "|" + BHPreference.saleCode();
        barcode = BHBarcode.generateCode128(strBarcodeNo, 1080, 200);
        if (barcode != null) {
            cv.drawBitmap(barcode, (LAYOUT_WIDTH - barcode.getWidth()) / 2, yy, null);
            yy += barcode.getHeight();
        }

        fontSize = 42;
        Paint pBarcodeNo = new Paint(p);
        pBarcodeNo.setTypeface(Typeface.DEFAULT_BOLD);
        pBarcodeNo.setTextSize(fontSize);
        pBarcodeNo.setTextAlign(Align.CENTER);

        yy += fontSize + 10;
        cv.drawText(strBarcodeNo, LAYOUT_WIDTH / 2, yy, pBarcodeNo);


        yy += LINE_FEED;

        Bitmap result;
        if (defaultHeight > yy && newHeight == 0) {
            result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            return scaleBitmapByWidth(result);
        } else if (newHeight != 0) {
            result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            return scaleBitmapByWidth(result);
        } else {
            img.recycle();
            return getDocumentHistory(outputContract,
                    outputReceipt,
                    outputChangeProduct,
                    outputImpoundProduct,
                    outputChangeContract,
                    outputManualDocument,
                    outputPayInSlipBank,
                    outputPayInSlipPayPoint,
                    Math.round(yy));
        }
    }

    public static List<PrintTextInfo> getTextDocumentHistory(List<DocumentHistoryInfo> outputContract,
                                                             List<DocumentHistoryInfo> outputReceipt,
                                                             List<DocumentHistoryInfo> outputChangeProduct,
                                                             List<DocumentHistoryInfo> outputImpoundProduct,
                                                             List<DocumentHistoryInfo> outputChangeContract,
                                                             List<DocumentHistoryInfo> outputManualDocument,
                                                             List<DocumentHistoryInfo> outputPayInSlipBank,
                                                             List<DocumentHistoryInfo> outputPayInSlipPayPoint) {
        List<PrintTextInfo> listText = new ArrayList<>();
        int num = 0;

        listText.addAll(getTextHeader());
        listText.addAll(getTextAlignCenter("ใบส่งเอกสาร", PrintTextInfo.FontType.Bold));
        listText.addAll(getTextAlignLeftRight("รายการส่งเอกสาร ประจำวันที่", BHUtilities.dateFormat(new Date())));

        EmployeeInfo outputEmp = TSRController.getEmployeeByID(BHPreference.employeeID());
        if (outputEmp != null) {
            listText.addAll(getTextAlignLeftRight("พนักงาน", outputEmp.FullName()));
        }

        if (outputContract != null) {
            num += outputContract.size();
            listText.addAll(getTextAlignLeftRight("เอกสารใบสัญญา", String.format("%d ใบ", outputContract.size())));
            for (int i = 0; i < outputContract.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputContract.get(i).DocumentNo, outputContract.get(i).CustomerFullName));
            }
        }

        if (outputReceipt != null) {
            num += outputReceipt.size();
            listText.addAll(getTextAlignLeftRight("เอกสารใบเสร็จ", String.format("%d ใบ", outputReceipt.size())));
            for (int i = 0; i < outputReceipt.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputReceipt.get(i).DocumentNo, outputReceipt.get(i).CustomerFullName));
            }
        }

        if (outputChangeProduct != null) {
            num += outputChangeProduct.size();
            listText.addAll(getTextAlignLeftRight("เอกสารใบเปลี่ยนเครื่อง", String.format("%d ใบ", outputChangeProduct.size())));
            for (int i = 0; i < outputChangeProduct.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputChangeProduct.get(i).DocumentNo, outputChangeProduct.get(i).CustomerFullName));
            }
        }

        if (outputImpoundProduct != null) {
            num += outputImpoundProduct.size();
            listText.addAll(getTextAlignLeftRight("เอกสารใบถอดเครื่อง", String.format("%d ใบ", outputImpoundProduct.size())));
            for (int i = 0; i < outputImpoundProduct.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputImpoundProduct.get(i).DocumentNo, outputImpoundProduct.get(i).CustomerFullName));
            }
        }

        if (outputChangeContract != null) {
            num += outputChangeContract.size();
            listText.addAll(getTextAlignLeftRight("เอกสารใบเปลี่ยนสัญญา", String.format("%d ใบ", outputChangeContract.size())));
            for (int i = 0; i < outputChangeContract.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputChangeContract.get(i).DocumentNo, outputChangeContract.get(i).CustomerFullName));
            }
        }

        if (outputManualDocument != null) {
            num += outputManualDocument.size();
            listText.addAll(getTextAlignLeftRight("เอกสารมือ", String.format("%d ใบ", outputManualDocument.size())));
            for (int i = 0; i < outputManualDocument.size(); i++) {
                if (outputManualDocument.get(i).ManualDocumentTypeID.equals("0")) {
                    listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputManualDocument.get(i).con0CONTNO, outputManualDocument.get(i).dc0CustomerFullName));
                } else if (outputManualDocument.get(i).ManualDocumentTypeID.equals("1")) {
                    listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputManualDocument.get(i).re1ReceiptCode, outputManualDocument.get(i).dc1CustomerFullName));
                }
            }
        }

        if (outputPayInSlipBank != null) {
            num += outputPayInSlipBank.size();
            listText.addAll(getTextAlignLeftRight("เอกสาร Slip ธนาคาร", String.format("%d ใบ", outputPayInSlipBank.size())));
            for (int i = 0; i < outputPayInSlipBank.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputPayInSlipBank.get(i).TransactionNo, outputPayInSlipBank.get(i).employeeFullName));
            }
        }

        if (outputPayInSlipPayPoint != null) {
            num += outputPayInSlipPayPoint.size();
            listText.addAll(getTextAlignLeftRight("เอกสาร Slip เพย์พอยท์", String.format("%d ใบ", outputPayInSlipPayPoint.size())));
            for (int i = 0; i < outputPayInSlipPayPoint.size(); i++) {
                listText.addAll(getTextForDocumentHistory(String.format("%3d", i + 1), outputPayInSlipPayPoint.get(i).TransactionNo, outputPayInSlipPayPoint.get(i).employeeFullName));
            }
        }

        listText.addAll(getTextAlignLeftRight("จำนวนเอกสารทั้งหมด", String.format("%d ใบ", num)));

        //listText.add(new PrintTextInfo(""));

        /*** [START] :: Fixed-[BHPROJ-0024-3215] :: [LINE-25/07/2016][Android-ระบบส่งเอกสาร] กรณีที่ รหัสทีม/รหัสหน่วย/SaleCode ยาวเกินไป ทำให้ Barcode ที่ใช้ส่งเอกสารมีความยาวเกิน 23 ตัวอักษร เครื่องพิมพ์จะไม่สามารถพิมพ์ Barcode ได้ ***/
//        String strBarcodeNo = BHPreference.teamCode() + "|" + BHPreference.SubTeamCode() + "|" + BHPreference.saleCode();
        String strBarcodeNo = BHPreference.SubTeamCode() + "|" + BHPreference.saleCode();
        /*** [END] :: Fixed-[BHPROJ-0024-3215] :: [LINE-25/07/2016][Android-ระบบส่งเอกสาร] กรณีที่ รหัสทีม/รหัสหน่วย/SaleCode ยาวเกินไป ทำให้ Barcode ที่ใช้ส่งเอกสารมีความยาวเกิน 23 ตัวอักษร เครื่องพิมพ์จะไม่สามารถพิมพ์ Barcode ได้ ***/

        listText.add(new PrintTextInfo(strBarcodeNo, PrintTextInfo.FontType.Normal, true));
        listText.addAll(getTextAlignCenter(strBarcodeNo, PrintTextInfo.FontType.Normal));

        return listText;
    }

    public static Bitmap getChangeProduct(ChangeProductInfo changeProduct, ContractInfo contract, AddressInfo address) {
        int num = 0;
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        cv.drawText("ใบเปลี่ยนเครื่อง/รับเครื่อง", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pTitleL = new Paint(p);
        pTitleL.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleL.setTextSize(fontSize);
        pTitleL.setTextAlign(Align.LEFT);

        Paint pValueL = new Paint(p);
        pValueL.setTypeface(null);
        pValueL.setTextSize(fontSize);
        pValueL.setTextAlign(Align.LEFT);

        Paint pTitleR = new Paint(p);
        pTitleR.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleR.setTextSize(fontSize);
        pTitleR.setTextAlign(Align.RIGHT);

        Paint pValueR = new Paint(p);
        pValueR.setTypeface(null);
        pValueR.setTextSize(fontSize);
        pValueR.setTextAlign(Align.RIGHT);

        yy += fontSize * 3;
        cv.drawText("เลขที่", xTitle, yy, pTitleL);
        cv.drawText(changeProduct.ChangeProductPaperID, xTitle + 125, yy, pValueL);
        cv.drawText("วันที่", LAYOUT_WIDTH - 250, yy, pTitleR);
        cv.drawText(BHUtilities.dateFormat(changeProduct.EffectiveDate, "dd/MM/yyyy"), LAYOUT_WIDTH - 10, yy, pValueR);


        yy += fontSize + lineSpace;
        cv.drawText(changeProduct.ProductName, xTitle, yy, pValueL);

        yy += fontSize + lineSpace;
        cv.drawText("หมายเลขเครื่องเก่า", xTitle, yy, pTitleL);
        cv.drawText(changeProduct.OldProductSerialNumber, xValue, yy, pValueL);

        yy += fontSize + lineSpace;
        cv.drawText("หมายเลขเครื่องใหม่", xTitle, yy, pTitleL);
        cv.drawText(changeProduct.NewProductSerialNumber, xValue, yy, pValueL);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitleL);
        cv.drawText(contract.CONTNO, xValue, yy, pValueL);

        String[] texts = getText(String.format("%s", BHUtilities.trim(changeProduct.CustomerFullName)), pValueL, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ผู้ซื้อ", xTitle, yy, pTitleL);
            }
            cv.drawText(texts[ii], xValue, yy, pValueL);
        }

        texts = getText(address.Address(), pValueL, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่อยู่", xTitle, yy, pTitleL);
            }
            cv.drawText(texts[ii], xValue, yy, pValueL);
        }

        texts = getText(address.Telephone(), pValueL, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("เบอร์โทร", xTitle, yy, pTitleL);
            }
            cv.drawText(texts[ii], xValue, yy, pValueL);
        }

        yy += fontSize + lineSpace;
        cv.drawText("วันที่ติดตั้ง", xTitle, yy, pTitleL);
        cv.drawText(BHUtilities.dateFormat(changeProduct.InstallDate, "dd/MM/yyyy"), xValue, yy, pValueL);


        Paint pTitleC = new Paint(p);
        pTitleC.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleC.setTextSize(fontSize);
        pTitleC.setTextAlign(Align.CENTER);

        Paint pValueC = new Paint(p);
        pValueC.setTypeface(null);
        pValueC.setTextSize(fontSize);
        pValueC.setTextAlign(Align.CENTER);


        yy += fontSize + fontSize + fontSize;
        cv.drawText("บริษัทได้เปลี่ยนเครื่องใหม่และรับคืนเครื่องเก่าเรียบร้อยแล้ว", LAYOUT_WIDTH / 2, yy, pTitleC);

        yy += LINE_FEED + (LINE_FEED / 2);
        cv.drawText("____________________", (LAYOUT_WIDTH / 2) / 2, yy, pValueC);
        cv.drawText("____________________", ((LAYOUT_WIDTH / 2) / 2) + (LAYOUT_WIDTH / 2), yy, pValueC);

        yy += fontSize + fontSize;
        cv.drawText("ผู้รับเปลี่ยนเครื่อง", (LAYOUT_WIDTH / 2) / 2, yy, pValueC);
        cv.drawText("ลูกค้าผู้เปลี่ยนเครื่อง", ((LAYOUT_WIDTH / 2) / 2) + (LAYOUT_WIDTH / 2), yy, pValueC);


        yy += fontSize + fontSize + fontSize;
        cv.drawText(String.format("รหัสพนักงานขาย %s : %s", contract.SaleCode, contract.SaleEmployeeName), xTitle, yy, pValueL);

        yy += fontSize + fontSize;
        cv.drawText(String.format("ทีม %s : %s", contract.SaleTeamCode, contract.upperEmployeeName), xTitle, yy, pValueL);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy,
                Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }

    public static List<PrintTextInfo> getTextChangeProduct(ChangeProductInfo changeProduct, ContractInfo contract, AddressInfo address) {
        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextHeader());
        listText.addAll(getTextAlignCenter("ใบเปลี่ยนเครื่อง/รับเครื่อง", PrintTextInfo.FontType.Bold));
        listText.addAll(getTextAlignLeftRight(String.format("%s %s", "เลขที่", changeProduct.ChangeProductPaperID), String.format("%s %s", "วันที่", BHUtilities.dateFormat(changeProduct.EffectiveDate, "dd/MM/yyyy"))));

        listText.addAll(getTextAlignLeft(changeProduct.ProductName));

        listText.addAll(getTextAlignLeftByOffSetLeft("หมายเลขเครื่องเก่า", changeProduct.OldProductSerialNumber));
        listText.addAll(getTextAlignLeftByOffSetLeft("หมายเลขเครื่องใหม่", changeProduct.NewProductSerialNumber));
        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่สัญญา", contract.CONTNO));
        listText.addAll(getTextAlignLeftByOffSetLeft("ผู้ซื้อ", String.format("%s", BHUtilities.trim(changeProduct.CustomerFullName))));
        listText.addAll(getTextAlignLeftByOffSetLeft("ที่อยู่", address.Address()));
        listText.addAll(getTextAlignLeftByOffSetLeft("เบอร์โทร", address.Telephone()));
        listText.addAll(getTextAlignLeftByOffSetLeft("วันที่ติดตั้ง", BHUtilities.dateFormat(changeProduct.InstallDate, "dd/MM/yyyy")));

        listText.addAll(getTextAlignCenter("บริษัทได้เปลี่ยนเครื่องใหม่และรับคืนเครื่องเก่าเรียบร้อยแล้ว"));

        listText.add(new PrintTextInfo(""));

        listText.addAll(getTextLeftRightAlignCenter("______________________", "______________________"));
        listText.addAll(getTextLeftRightAlignCenter("ผู้รับเปลี่ยนเครื่อง", "ลูกค้าผู้เปลี่ยนเครื่อง"));

        listText.addAll(getTextAlignLeft(String.format("รหัสพนักงานขาย %s : %s", contract.SaleCode, contract.SaleEmployeeName)));
        listText.addAll(getTextAlignLeft(String.format("ทีม %s : %s", contract.SaleTeamCode, contract.upperEmployeeName)));

        return listText;
    }

    public static Bitmap getImpoundProduct(ImpoundProductInfo impoundProduct, AddressInfo address) {
        int num = 0;
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
//        cv.drawText("ใบเปลี่ยนเครื่อง/รับเครื่อง", LAYOUT_WIDTH / 2, yy, p);
        cv.drawText("ใบรับคืนเครื่อง", LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pTitleL = new Paint(p);
        pTitleL.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleL.setTextSize(fontSize);
        pTitleL.setTextAlign(Align.LEFT);

        Paint pValueL = new Paint(p);
        pValueL.setTypeface(null);
        pValueL.setTextSize(fontSize);
        pValueL.setTextAlign(Align.LEFT);

        Paint pTitleR = new Paint(p);
        pTitleR.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleR.setTextSize(fontSize);
        pTitleR.setTextAlign(Align.RIGHT);

        Paint pValueR = new Paint(p);
        pValueR.setTypeface(null);
        pValueR.setTextSize(fontSize);
        pValueR.setTextAlign(Align.RIGHT);

        yy += fontSize * 3;

        cv.drawText("เลขที่", xTitle, yy, pTitleL);
        cv.drawText(impoundProduct.ImpoundProductPaperID, xTitle + 125, yy, pValueL);
        cv.drawText("วันที่", LAYOUT_WIDTH - 250, yy, pTitleR);
        cv.drawText(BHUtilities.dateFormat(impoundProduct.EffectiveDate, "dd/MM/yyyy"), LAYOUT_WIDTH - 10, yy, pValueR);


        yy += fontSize + lineSpace;
        cv.drawText(impoundProduct.ProductName, xTitle, yy, pValueL);


        yy += fontSize + lineSpace;
        cv.drawText("หมายเลขเครื่อง", xTitle, yy, pTitleL);
        cv.drawText(impoundProduct.ProductSerialNumber, xValue, yy, pValueL);

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitleL);
        cv.drawText(impoundProduct.CONTNO, xValue, yy, pValueL);

        yy += fontSize + lineSpace;
        cv.drawText("ผู้ซื้อ", xTitle, yy, pTitleL);
        cv.drawText(String.format("%s %s", BHUtilities.trim(impoundProduct.CustomerFullName), BHUtilities.trim(impoundProduct.CompanyName)), xValue, yy, pValueL);


        String[] texts = getText(address.Address(), pValueL, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่อยู่", xTitle, yy, pTitleL);
            }
            cv.drawText(texts[ii], xValue, yy, pValueL);
        }

        texts = getText(address.Telephone(), pValueL, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("เบอร์โทร", xTitle, yy, pTitleL);
            }
            cv.drawText(texts[ii], xValue, yy, pValueL);
        }

        yy += fontSize + lineSpace;
        cv.drawText("วันที่ติดตั้ง", xTitle, yy, pTitleL);
        cv.drawText(BHUtilities.dateFormat(impoundProduct.InstallDate, "dd/MM/yyyy"), xValue, yy, pValueL);


        Paint pTitleC = new Paint(p);
        pTitleC.setTypeface(Typeface.DEFAULT_BOLD);
        pTitleC.setTextSize(fontSize);
        pTitleC.setTextAlign(Align.CENTER);

        Paint pValueC = new Paint(p);
        pValueC.setTypeface(null);
        pValueC.setTextSize(fontSize);
        pValueC.setTextAlign(Align.CENTER);


        yy += fontSize + fontSize + fontSize;
//        cv.drawText("บริษัทได้เปลี่ยนเครื่องใหม่และรับคืนเครื่องเก่าเรียบร้อยแล้ว", LAYOUT_WIDTH / 2, yy, pTitleC);
        cv.drawText("บริษัทได้รับคืนเครื่องดังกล่าวเรียบร้อยแล้ว", LAYOUT_WIDTH / 2, yy, pTitleC);

        yy += LINE_FEED + (LINE_FEED / 2);
        cv.drawText("____________________", (LAYOUT_WIDTH / 2) / 2, yy, pValueC);
        cv.drawText("____________________", ((LAYOUT_WIDTH / 2) / 2) + (LAYOUT_WIDTH / 2), yy, pValueC);

        yy += fontSize + fontSize;
        cv.drawText("ผู้รับคืนเครื่อง", (LAYOUT_WIDTH / 2) / 2, yy, pValueC);
        cv.drawText("ลูกค้าผู้คืนเครื่อง", ((LAYOUT_WIDTH / 2) / 2) + (LAYOUT_WIDTH / 2), yy, pValueC);


        yy += fontSize + fontSize + fontSize;
        cv.drawText(String.format("รหัสพนักงานขาย %s : %s", impoundProduct.SaleCode, impoundProduct.SaleEmployeeName), xTitle, yy, pValueL);

        yy += fontSize + fontSize;
        cv.drawText(String.format("ทีม %s : %s", impoundProduct.SaleTeamCode, impoundProduct.TeamHeadName), xTitle, yy, pValueL);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy,
                Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }

    public static List<PrintTextInfo> getTextImpoundProduct(ImpoundProductInfo impoundProduct, AddressInfo address) {
        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextHeader());
        listText.addAll(getTextAlignCenter("ใบรับคืนเครื่อง", PrintTextInfo.FontType.Bold));
        listText.addAll(getTextAlignLeftRight(String.format("%s %s", "เลขที่", impoundProduct.ImpoundProductPaperID), String.format("%s %s", "วันที่", BHUtilities.dateFormat(impoundProduct.EffectiveDate, "dd/MM/yyyy"))));

        listText.addAll(getTextAlignLeft(impoundProduct.ProductName));

        listText.addAll(getTextAlignLeftByOffSetLeft("หมายเลขเครื่อง", impoundProduct.ProductSerialNumber));
        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่สัญญา", impoundProduct.CONTNO));
        listText.addAll(getTextAlignLeftByOffSetLeft("ผู้ซื้อ", String.format("%s %s", BHUtilities.trim(impoundProduct.CustomerFullName), BHUtilities.trim(impoundProduct.CompanyName))));
        listText.addAll(getTextAlignLeftByOffSetLeft("ที่อยู่", address.Address()));
        listText.addAll(getTextAlignLeftByOffSetLeft("เบอร์โทร", address.Telephone()));
        listText.addAll(getTextAlignLeftByOffSetLeft("วันที่ติดตั้ง", BHUtilities.dateFormat(impoundProduct.InstallDate, "dd/MM/yyyy")));

        listText.addAll(getTextAlignCenter("บริษัทได้รับคืนเครื่องดังกล่าวเรียบร้อยแล้ว"));

        listText.add(new PrintTextInfo(""));

        listText.addAll(getTextLeftRightAlignCenter("______________________", "______________________"));
        listText.addAll(getTextLeftRightAlignCenter("ผู้รับคืนเครื่อง", "ลูกค้าผู้คืนเครื่อง"));

        listText.addAll(getTextAlignLeft(String.format("รหัสพนักงานขาย %s : %s", impoundProduct.SaleCode, impoundProduct.SaleEmployeeName)));
        listText.addAll(getTextAlignLeft(String.format("ทีม %s : %s", impoundProduct.SaleTeamCode, impoundProduct.TeamHeadName)));

        return listText;
    }

    public static Bitmap getChangeContract(ChangeContractInfo changeContract, ContractInfo oldContract, ContractInfo newContract, AddressInfo defaultAddress, AddressInfo installAddress, List<SalePaymentPeriodInfo> sspList, ManualDocumentInfo manual) {
        Bitmap img = Bitmap.createBitmap(LAYOUT_WIDTH, 3000, Config.ARGB_8888);
        img.setHasAlpha(true);

        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        float yy = 0;
        Bitmap header = getHeader();
        cv.drawBitmap(header, 0, yy, null);
        yy += header.getHeight();
        header.recycle();

        float fontSize = 90;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(fontSize);
        p.setTextAlign(Align.CENTER);
        yy += fontSize + LINE_SPACE;
        String title = newContract.MODE > 1 ? "ใบสัญญาเช่าซื้อ" : "ใบสัญญาซื้อขาย";
        cv.drawText(title, LAYOUT_WIDTH / 2, yy, p);

        fontSize = 42;
        float lineSpace = fontSize / 2;
        float xTitle = 1;
        float xValue = 400;

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.DEFAULT_BOLD);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setTypeface(null);

        yy += fontSize * 3;
        cv.drawText("วันที่ทำสัญญา", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(newContract.EFFDATE), xValue, yy, pValue);

        if (manual != null) {
            yy += fontSize + lineSpace;
            cv.drawText("เลขที่อ้างอิง", xTitle, yy, pTitle);
            //cv.drawText(String.format("%s/%4d", BHUtilities.trim(manual.ManualVolumeNo), manual.ManualRunningNo).replace(' ', '0'), xValue, yy, pValue);
            cv.drawText(String.format("%4d", manual.ManualRunningNo).replace(' ', '0'), xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่สัญญา", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(newContract.CONTNO) + " (" + changeContract.ProblemName.substring(0, 2) + ")", xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("วันที่เปลี่ยนสัญญา", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(changeContract.EffectiveDate), xValue, yy, pValue);

        /*yy += fontSize + lineSpace;
        cv.drawText("ผู้ซื้อ", xTitle, yy, pTitle);
        cv.drawText(String.format("%s %s", BHUtilities.trim(newContract.CustomerFullName), BHUtilities.trim(newContract.CompanyName)), xValue, yy, pValue);*/

        String[] texts = getText(String.format("%s %s", BHUtilities.trim(newContract.CustomerFullName), BHUtilities.trim(newContract.CompanyName)), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText(newContract.MODE > 1 ? "ผู้เช่าซื้อ" : "ผู้ซื้อ", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }


        yy += fontSize + lineSpace;
        cv.drawText("เลขที่บัตร", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(newContract.IDCard), xValue, yy, pValue);

        //xValue = 250;
        texts = getText(defaultAddress.Address(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่อยู่บัตร", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        texts = getText(installAddress.Address(), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("ที่ติดตั้ง", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        texts = getText(BHUtilities.trim(newContract.ProductName), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText("สินค้า", xTitle, yy, pTitle);
            }
            cv.drawText(texts[ii], xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("รุ่น", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(newContract.MODEL), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText("เลขเครื่อง", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(newContract.ProductSerialNumber), xValue, yy, pValue);


        if (newContract.TradeInDiscount > 0) {

            xValue = LAYOUT_WIDTH - 100;
            pValue.setTextAlign(Align.RIGHT);
            yy += fontSize + lineSpace;
            cv.drawText("ราคา", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(newContract.SALES) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("ส่วนลดเครื่องแสดง", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(newContract.TradeInDiscount) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("ราคาสุทธิ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(newContract.TotalPrice) + " บาท", xValue, yy, pValue);
        } else {

            xValue = LAYOUT_WIDTH - 100;
            pValue.setTextAlign(Align.RIGHT);
            yy += fontSize + lineSpace;
            cv.drawText("ราคาขาย", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(newContract.SALES) + " บาท", xValue, yy, pValue);

        }

        if (sspList.size() > 0) {
            /*yy += fontSize + lineSpace;
            float sum = newContract.PaymentAmount - newContract.TradeInDiscount;
            cv.drawText("งวดที่ 1 ต้องชำระ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sum) + " บาท", xValue, yy, pValue);

            yy += fontSize + lineSpace;
            cv.drawText("งวดที่ 2 ถึงงวดที่ " + BHUtilities.numericFormat(newContract.MODE) + " ต้องชำระงวดละ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sspList.get(1).PaymentAmount) + " บาท", xValue, yy, pValue);*/

            yy += fontSize + lineSpace;
            cv.drawText("งวดที่ 1 ต้องชำระ", xTitle, yy, pTitle);
            cv.drawText(BHUtilities.numericFormat(sspList.get(0).NetAmount) + " บาท", xValue, yy, pValue);

            if (sspList.size() != 1) {
                if (sspList.size() == 2) {
                    yy += fontSize + lineSpace;
                    cv.drawText("งวดที่ 2 ต้องชำระ", xTitle, yy, pTitle);
                    cv.drawText(BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท", xValue, yy, pValue);
                } else {
                    if (sspList.get(1).NetAmount == sspList.get(2).NetAmount) {
                        yy += fontSize + lineSpace;
                        cv.drawText("งวดที่ 2 ถึงงวดที่ " + BHUtilities.numericFormat(sspList.size()) + " ต้องชำระงวดละ", xTitle, yy, pTitle);
                        cv.drawText(BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท", xValue, yy, pValue);

                    } else {
                        if (sspList.size() == 3) {
                            yy += fontSize + lineSpace;
                            cv.drawText("งวดที่ 2 ต้องชำระ", xTitle, yy, pTitle);
                            cv.drawText(BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท", xValue, yy, pValue);

                            yy += fontSize + lineSpace;
                            cv.drawText("งวดที่ 3 ต้องชำระ", xTitle, yy, pTitle);
                            cv.drawText(BHUtilities.numericFormat(sspList.get(2).NetAmount) + " บาท", xValue, yy, pValue);
                        } else {
                            yy += fontSize + lineSpace;
                            cv.drawText("งวดที่ 2 ต้องชำระ", xTitle, yy, pTitle);
                            cv.drawText(BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท", xValue, yy, pValue);

                            yy += fontSize + lineSpace;
                            cv.drawText("งวดที่ 3 ถึงงวดที่ " + BHUtilities.numericFormat(sspList.size()) + " ต้องชำระงวดละ", xTitle, yy, pTitle);
                            cv.drawText(BHUtilities.numericFormat(sspList.get(2).NetAmount) + " บาท", xValue, yy, pValue);
                        }
                    }
                }
            }
        }

        pValue.setTextAlign(Align.LEFT);
        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.CENTER);


        String customer = String.format("(%s %s)", BHUtilities.trim(newContract.CustomerFullName), BHUtilities.trim(newContract.CompanyName));

        if (newContract.MODE == 1) {
            yy += 200;
            int Value = LAYOUT_WIDTH / 2;
            int ww = getWidth(customer, pValue) + 50;

            texts = getText(customer, pSignature, LAYOUT_WIDTH);
            for (int ii = 0; ii < texts.length; ii++) {
                if (ii == 0) {
                    /*cv.drawText(getSignatureUnderline(pSignature, ww), Value, yy, pSignature);
                    cv.drawText("ผู้ซื้อ", Value + 190, yy, pValue);*/
                    cv.drawText(String.format("%sผู้ซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH) - (getWidth("ผู้ซื้อ", pSignature) + (LAYOUT_WIDTH / 2)))), LAYOUT_WIDTH / 2, yy, pSignature);
                }
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], Value, yy, pSignature);
            }

        } else {

            /*yy += 200;
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            int xx = getWidth(TSR, pValue) + 10;
            int ww = getWidth(customer, pValue) + 50;

            int width = LAYOUT_WIDTH / 6;
            cv.drawText(getSignatureUnderline(pSignature, xx), width, yy, pSignature); // วาดจุด
            cv.drawText(String.format("ผู้ให้เช่าซื้อ"), width + 190, yy, pValue);

            xValue = LAYOUT_WIDTH - (ww / 2 + 120);
            cv.drawText("ผู้ซื้อ", LAYOUT_WIDTH - 115, yy, pValue);
            cv.drawText(getSignatureUnderline(pSignature, ww), xValue, yy, pSignature);

            yy += fontSize + lineSpace;
            cv.drawText(TSR, 230, yy, pSignature);
            cv.drawText(customer, xValue, yy, pSignature);*/

            /****/
            yy += 200;
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            // YIM Change TSR_COMMITTEE_NAME
            if (BHGeneral.isOpenDepartmentSignature && BHPreference.hasDepartmentSignatureImage()) {
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "(" + saleLeader.DepartmentHeadName + ")";
                }
            }


            cv.drawText(String.format("%sผู้ให้เช่าซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("ผู้ให้เช่าซื้อ", pSignature) + 50))), LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format("%sผู้เช่าซื้อ", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("ผู้เช่าซื้อ", pSignature) + 50))), (LAYOUT_WIDTH / 4) * 3, yy, pSignature);

            String[] texts1 = getText(TSR, pSignature, LAYOUT_WIDTH / 2);
            String[] texts2 = getText(customer, pSignature, LAYOUT_WIDTH / 2);
            int num;
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], LAYOUT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
                }
            }
            /****/



            /*yy += 200;
            String salename = "(" + BHUtilities.trim(changeContract.EffectiveByEmployeeName) + ")";
            String salecode = "รหัส " + BHUtilities.trim(changeContract.EffectiveBySaleCode != null ? changeContract.EffectiveBySaleCode : changeContract.EffectiveBy);

            String saleteamname = "(" + BHUtilities.trim(changeContract.EffectiveByUpperEmployeeName) + ")";
            String saleteamcode = "" + BHUtilities.trim(changeContract.EffectiveBySaleTeamName);

            int zz = getWidth(saleteamname, pValue) + 50;
            xValue = LAYOUT_WIDTH - (zz / 2 + 120);

            int vv = getWidth(salename, pValue) + 50;
            xValue = LAYOUT_WIDTH - (vv / 2 + 120);

            int r = LAYOUT_WIDTH / 6;
            cv.drawText(getSignatureUnderline(pSignature, zz), r, yy, pSignature);
            cv.drawText("พยาน", r + 190, yy, pValue);

            cv.drawText("พยาน", LAYOUT_WIDTH - 115, yy, pValue);
            cv.drawText(getSignatureUnderline(pSignature, vv), xValue, yy, pSignature);

            yy += fontSize + lineSpace;
            cv.drawText(saleteamname, 230, yy, pSignature);
            cv.drawText(salename, xValue, yy, pSignature);
            yy += fontSize + lineSpace;
            cv.drawText(saleteamcode, 240, yy, pSignature);
            cv.drawText(salecode, xValue, yy, pSignature);*/

            /****/
            yy += 200;
            String salename = "(" + BHUtilities.trim(changeContract.EffectiveByEmployeeName) + ")";
            String salecode = "รหัส " + BHUtilities.trim(changeContract.EffectiveBySaleCode != null ? changeContract.EffectiveBySaleCode : changeContract.EffectiveBy);

            String saleteamname = "(" + BHUtilities.trim(changeContract.EffectiveByUpperEmployeeName) + ")";
            String saleteamcode = "" + BHUtilities.trim(changeContract.EffectiveBySaleTeamName);

            cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (LAYOUT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), (LAYOUT_WIDTH / 4) * 3, yy, pSignature);

            texts1 = getText(saleteamname, pSignature, LAYOUT_WIDTH / 2);
            texts2 = getText(salename, pSignature, LAYOUT_WIDTH / 2);
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], LAYOUT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
                }
            }

            yy += fontSize + lineSpace;
            cv.drawText(saleteamcode, LAYOUT_WIDTH / 4, yy, pSignature);
            cv.drawText(salecode, (LAYOUT_WIDTH / 4) * 3, yy, pSignature);
            /****/

        }

        xValue = 1;
        yy += fontSize + lineSpace + 50;
        cv.drawText(String.format("รหัส %s %s", BHUtilities.trim(newContract.SaleCode), BHUtilities.trim(newContract.SaleEmployeeName)), xValue, yy, pValue);

        yy += fontSize + lineSpace;
        cv.drawText(String.format("%s %s", BHUtilities.trim(newContract.SaleTeamName), BHUtilities.trim(newContract.upperEmployeeName)), xValue, yy, pValue);

        yy += LINE_FEED;

        Bitmap result = Bitmap.createBitmap(LAYOUT_WIDTH, (int) yy, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return scaleBitmapByWidth(result);
    }

    //public static List<PrintTextInfo> getTextChangeContract(ChangeContractInfo changeContract, ContractInfo oldContract, ContractInfo newContract, AddressInfo defaultAddress, AddressInfo installAddress, List<SalePaymentPeriodInfo> sspList, ManualDocumentInfo manual) {
    public static List<PrintTextInfo> getTextChangeContract(ChangeContractInfo changeContract, ContractInfo oldContract, ContractInfo newContract, AddressInfo defaultAddress, AddressInfo installAddress, List<SalePaymentPeriodInfo> sspList) {
        List<PrintTextInfo> listText = new ArrayList<>();

        listText.addAll(getTextHeader());

        String title = newContract.MODE > 1 ? "ใบสัญญาเช่าซื้อ" : "ใบสัญญาซื้อขาย";
        listText.addAll(getTextAlignCenter(title, PrintTextInfo.FontType.Bold));

        listText.addAll(getTextAlignLeftByOffSetLeft("วันที่ทำสัญญา", BHUtilities.dateFormat(newContract.EFFDATE)));

        /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
        /*if (manual != null) {
            listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่อ้างอิง", String.format("%4d", manual.ManualRunningNo).replace(' ', '0')));
        }*/
        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่อ้างอิง", newContract.ContractReferenceNo != null ? newContract.ContractReferenceNo : ""));
        /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/

        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่สัญญา", BHUtilities.trim(newContract.CONTNO) + " (" + changeContract.ProblemName.substring(0, 2) + ")"));
        listText.addAll(getTextAlignLeftByOffSetLeft("วันที่เปลี่ยนสัญญา", BHUtilities.dateFormat(changeContract.EffectiveDate)));
        listText.addAll(getTextAlignLeftByOffSetLeft(newContract.MODE > 1 ? "ผู้เช่าซื้อ" : "ผู้ซื้อ", String.format("%s %s", BHUtilities.trim(newContract.CustomerFullName), BHUtilities.trim(newContract.CompanyName))));
        listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่บัตร", BHUtilities.trim(newContract.IDCard)));
        listText.addAll(getTextAlignLeftByOffSetLeft("ที่อยู่บัตร", defaultAddress.Address()));
        listText.addAll(getTextAlignLeftByOffSetLeft("ที่ติดตั้ง", installAddress.Address()));
        listText.addAll(getTextAlignLeftByOffSetLeft("สินค้า", BHUtilities.trim(newContract.ProductName)));
        listText.addAll(getTextAlignLeftByOffSetLeft("รุ่น", BHUtilities.trim(newContract.MODEL)));
        listText.addAll(getTextAlignLeftByOffSetLeft("เลขเครื่อง", BHUtilities.trim(newContract.ProductSerialNumber)));

        if (newContract.TradeInDiscount > 0) {
            listText.addAll(getTextAlignLeftByOffSetLeft("ราคา", BHUtilities.numericFormat(newContract.SALES) + " บาท"));
            listText.addAll(getTextAlignLeftByOffSetLeft("ส่วนลดเครื่องแสดง", BHUtilities.numericFormat(newContract.TradeInDiscount) + " บาท"));
            listText.addAll(getTextAlignLeftByOffSetLeft("ราคาสุทธิ", BHUtilities.numericFormat(newContract.TotalPrice) + " บาท"));
        } else {
            listText.addAll(getTextAlignLeftByOffSetLeft("ราคาขาย", BHUtilities.numericFormat(newContract.SALES) + " บาท"));
        }

        if (sspList.size() > 0) {
            listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 1 ต้องชำระ", BHUtilities.numericFormat(sspList.get(0).NetAmount) + " บาท"));
            if (sspList.size() != 1) {
                if (sspList.size() == 2) {
                    listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 2 ต้องชำระ", BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท"));
                } else {
                    if (sspList.get(1).NetAmount == sspList.get(2).NetAmount) {
                        listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 2 ถึงงวดที่ " + BHUtilities.numericFormat(sspList.size()) + " ต้องชำระงวดละ", BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท"));
                    } else {
                        if (sspList.size() == 3) {
                            listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 2 ต้องชำระ", BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท"));
                            listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 3 ต้องชำระ", BHUtilities.numericFormat(sspList.get(2).NetAmount) + " บาท"));
                        } else {
                            listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 2 ต้องชำระ", BHUtilities.numericFormat(sspList.get(1).NetAmount) + " บาท"));
                            listText.addAll(getTextAlignLeftByOffSetLeft("งวดที่ 3 ถึงงวดที่ " + BHUtilities.numericFormat(sspList.size()) + " ต้องชำระงวดละ", BHUtilities.numericFormat(sspList.get(2).NetAmount) + " บาท"));
                        }
                    }
                }
            }
        }

        listText.add(new PrintTextInfo(""));

        String customer = String.format("(%s%s)", BHUtilities.trim(newContract.CustomerFullName), BHUtilities.trim(newContract.CompanyName));
        if (newContract.MODE == 1) {
            listText.addAll(getTextAlignCenter("...........................ผู้ซื้อ"));
            listText.addAll(getTextAlignCenter(customer));
        } else {

            listText.add(new PrintTextInfo(""));
            listText.add(new PrintTextInfo("signature"));
            listText.addAll(getTextLeftRightAlignCenter("......................", ".....................", "EN"));
            listText.addAll(getTextLeftRightAlignCenter("       ผู้ให้เช่าซื้อ       ", "       ผู้เช่าซื้อ        "));
            String TSR = "";
            // YIM Change TSR_COMMITTEE_NAME
            if (BHGeneral.isOpenDepartmentSignature && BHPreference.hasDepartmentSignatureImage()) {
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "(" + saleLeader.DepartmentHeadName + ")";
                }
            }

            listText.addAll(getTextLeftRightAlignCenter(TSR, customer));

            String salename = "(" + BHUtilities.trim(changeContract.EffectiveByEmployeeName) + ")";
            String salecode = "รหัส " + BHUtilities.trim(changeContract.EffectiveBySaleCode != null ? changeContract.EffectiveBySaleCode : changeContract.EffectiveBy);

            String saleteamname = "(" + BHUtilities.trim(changeContract.EffectiveByUpperEmployeeName) + ")";
            String saleteamcode = "" + BHUtilities.trim(changeContract.EffectiveBySaleTeamName);

            listText.addAll(getTextLeftRightAlignCenter("..................พยาน", "..................พยาน"));
            listText.addAll(getTextLeftRightAlignCenter(saleteamname, salename));
            listText.addAll(getTextLeftRightAlignCenter(saleteamcode, salecode));
        }

        listText.add(new PrintTextInfo(""));
        listText.addAll(getTextAlignLeft(String.format("รหัส %s %s", BHUtilities.trim(newContract.SaleCode), BHUtilities.trim(newContract.SaleEmployeeName))));
        listText.addAll(getTextAlignLeft(String.format("%s %s", BHUtilities.trim(newContract.SaleTeamName), BHUtilities.trim(newContract.upperEmployeeName))));

        return listText;
    }

    /**
     * Edit by Teerayut Klinsanga
     * <p>
     * Created: 2019-07-11 11:00.00
     * <p>
     * == Print with image ==
     */

//    public static Bitmap getPaymentReceiptImage(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInf) {
//        ReceiptBuilder receiptBuilder = new ReceiptBuilder(576);
//        receiptBuilder.setMargin(5, 0);
//        receiptBuilder.addImage(getHeader());
//        receiptBuilder.addParagraph();
//        receiptBuilder.setAlign(Align.CENTER);
//        receiptBuilder.setColor(Color.BLACK);
//        receiptBuilder.setTextSize(22);
//
//        return receiptBuilder.build();
//    }
//
    private static InputStream ins = null;

    public static Bitmap headerPrint() {
        Bitmap bmp = null;
        try {
            ins = mContext.getResources().getAssets().open("tsr_header.png");
            bmp = BitmapFactory.decodeStream(ins);
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static Bitmap shortHeaderPrint() {
        Bitmap bmp = null;
        try {
            ins = mContext.getResources().getAssets().open("tsr_short_header.png");
            bmp = BitmapFactory.decodeStream(ins);
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static Bitmap backgroundTitle() {
        Bitmap bmp = null;
        try {
            ins = mContext.getResources().getAssets().open("title_background.png");
            bmp = BitmapFactory.decodeStream(ins);
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static Bitmap logoTelesale() {
        Bitmap bmp = null;
        try {
            ins = mContext.getResources().getAssets().open("logo_tsr_telsale.png");
            bmp = BitmapFactory.decodeStream(ins);
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResizedBitmap(bmp, 120, 120);
    }

    private static Bitmap getStringForMerge() {
        Bitmap bmp = null;
        File file = new File(getAlbumStorageDir("textmerge"), String.format("text_center.jpg"));
        if (file.exists()) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
            bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true);
        } else {
            bmp = createStringForMerge();
        }

        return bmp;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap mergeSignature(Bitmap bmp1, Bitmap bmp2, File path) {
//        Bitmap result = Bitmap.createBitmap((bmp1.getWidth() + bmp2.getWidth()), bmp1.getHeight(), Config.ARGB_8888 );
        Bitmap result = Bitmap.createBitmap(RECEIPT_WIDTH, bmp1.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, (bmp1.getWidth() + 50), 10, null);
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
            result.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap mergeBMP(Bitmap bmp1, Bitmap bmp2, File path) {
        Bitmap result = Bitmap.createBitmap(450, 150, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, bmp1.getWidth(), 0, null);
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
            result.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap mergeBMP2(Bitmap bmp1, Bitmap bmp2, File path) {
        Bitmap result = Bitmap.createBitmap(576, 150, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, bmp1.getWidth(), 15, null);
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
            result.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int signatureWidthLimit = 220;
    public static int signatureHeightLimit = 80;

    public static Bitmap generatSignature() {
        Bitmap tempSignature = null, signature;
        Bitmap img = null;
        Canvas cv;
        try {
            if (BHGeneral.isOpenDepartmentSignature && BHPreference.hasDepartmentSignatureImage()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                tempSignature = BitmapFactory.decodeFile(BHPreference.departmentSignaturePath + BHPreference.departmentSignatureName, options);
            } else {
                Context mContext = BHApplication.getContext();
                tempSignature = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sample_signature);
            }

            if (tempSignature != null) {
                signature = scaleBitmapByHeight(tempSignature);
                img = Bitmap.createBitmap(signature.getWidth(), signature.getHeight(), Bitmap.Config.ARGB_8888);
                img.setHasAlpha(true);
                cv = new Canvas(img);
                cv.drawBitmap(signature, 0, 0, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    public static Bitmap generateCustomerSignature(String contract) {
        Bitmap tempSignature = null, signature;
        Bitmap img = null;
        Canvas cv;

        File file = new File(getAlbumStorageDir(contract), String.format("Signature_%s.jpg", contract));
        if (file.exists()) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            tempSignature = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
            tempSignature = Bitmap.createScaledBitmap(tempSignature, tempSignature.getWidth(), tempSignature.getHeight(), true);
        }

        if (tempSignature != null) {
            img = Bitmap.createBitmap(tempSignature.getWidth(), tempSignature.getHeight(), Bitmap.Config.ARGB_8888);
            img.setHasAlpha(true);
            cv = new Canvas(img);
            cv.drawBitmap(tempSignature, 100, 0, null);

            img = getResizedBitmap(img, 250, 80);
        }

        return img;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static Bitmap scaleBitmapByHeight(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        if (bm.getHeight() > signatureHeightLimit) {
            width = (int) (1f * signatureHeightLimit / height * width);
            height = signatureHeightLimit;
        }

        if (width > signatureWidthLimit) {
            height = (int) (1f * signatureWidthLimit / width * height);
            width = signatureWidthLimit;
        }

        Bitmap bitmap = Bitmap.createScaledBitmap(bm, width, height, true);
        return bitmap;
    }

    public static File getAlbumStorageDir(String albumName) {
        File file = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        }
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    private static int RECEIPT_WIDTH = 576;

    public static Bitmap getNewContactImage(ContractInfo contract, AddressInfo defaultAddress, AddressInfo installAddress) {
        ReceiptBuilder receiptBuilder = new ReceiptBuilder(RECEIPT_WIDTH);
        receiptBuilder.setMargin(5, 0);
        receiptBuilder.setAlign(Align.CENTER);
        receiptBuilder.setColor(Color.BLACK);
        receiptBuilder.setTextSize(26);
        receiptBuilder.addImage(shortHeaderPrint());
        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(10);
        receiptBuilder.setAlign(Align.CENTER);
        receiptBuilder.addText(contract.MODE > 1 ? "ใบสัญญาเช่าซื้อ" : "ใบสัญญาซื้อขาย");
        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("วันที่ทำสัญญา", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.dateFormat(contract.EFFDATE));

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่อ้างอิง", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(contract.ContractReferenceNo != null ? contract.ContractReferenceNo : "", true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่สัญญา", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(contract.CONTNO), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText(contract.MODE > 1 ? "ผู้เช่าซื้อ" : "ผู้ซื้อ", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่บัตร", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(contract.IDCard), true);

        Bitmap img = Bitmap.createBitmap(RECEIPT_WIDTH, 300, Config.ARGB_8888);
        img.setHasAlpha(true);

        Bitmap imgAddress = Bitmap.createBitmap(RECEIPT_WIDTH, 300, Config.ARGB_8888);
        img.setHasAlpha(true);

        Bitmap imgInstall = Bitmap.createBitmap(RECEIPT_WIDTH, 300, Config.ARGB_8888);
        img.setHasAlpha(true);

        float yy = 19;
        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        Canvas cvAddr = new Canvas(imgAddress);
        cvAddr.drawColor(Color.WHITE);

        Canvas cvInstall = new Canvas(imgInstall);
        cvInstall.drawColor(Color.WHITE);

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);

        float fontSize = 23;
        float lineSpace = 10;

        Paint pTitle = new Paint(p);
        pTitle.setTypeface(Typeface.MONOSPACE);
        pTitle.setTextSize(fontSize);
        pTitle.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle);
        pValue.setColor(Color.BLACK);
        pValue.setTextAlign(Align.RIGHT);
        pValue.setTypeface(null);

        Bitmap result = null;
        switch (contract.CustomerType) {
            case "0":
            case "2":
                Log.e("Address", String.valueOf(defaultAddress.Address()));
                if (defaultAddress.TelMobile.equals(installAddress.TelMobile)) {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ที่อยู่บัตร", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    String[] texts = getText(defaultAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < texts.length; ii++) {
                        cvAddr.drawText(texts[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result1 = Bitmap.createBitmap(RECEIPT_WIDTH, (int) yy, Config.ARGB_8888);
                    cvAddr = new Canvas(result1);
                    cvAddr.drawBitmap(imgAddress, RECEIPT_WIDTH / 2, 0, null);
                    imgAddress.recycle();
                    receiptBuilder.addImage(result1);
                    receiptBuilder.addParagraph();
                    yy = 19;
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    String[] textsInstall = getText(installAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < textsInstall.length; ii++) {
                        cvInstall.drawText(textsInstall[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result2 = Bitmap.createBitmap(RECEIPT_WIDTH, (int) yy, Config.ARGB_8888);
                    cvInstall = new Canvas(result2);
                    cvInstall.drawBitmap(imgInstall, RECEIPT_WIDTH / 2, 0, null);
                    imgInstall.recycle();
                    receiptBuilder.addImage(result2);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(installAddress.TelMobile, true);
                } else {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ที่อยู่บัตร", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    String[] texts = getText(defaultAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < texts.length; ii++) {
                        cvAddr.drawText(texts[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result1 = Bitmap.createBitmap(RECEIPT_WIDTH, (int) yy, Config.ARGB_8888);
                    cvAddr = new Canvas(result1);
                    cvAddr.drawBitmap(imgAddress, RECEIPT_WIDTH / 2, 0, null);
                    imgAddress.recycle();
                    receiptBuilder.addImage(result1);
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(defaultAddress.TelMobile, true);
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    yy = 19;
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    String[] textsInstall = getText(installAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < textsInstall.length; ii++) {
                        cvInstall.drawText(textsInstall[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result2 = Bitmap.createBitmap(RECEIPT_WIDTH, (int) yy, Config.ARGB_8888);
                    cvInstall = new Canvas(result2);
                    cvInstall.drawBitmap(imgInstall, RECEIPT_WIDTH / 2, 0, null);
                    imgInstall.recycle();
                    receiptBuilder.addImage(result2);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(installAddress.TelMobile, true);
                }
                break;
            case "1":
                if (defaultAddress.TelMobile.equals(installAddress.TelMobile)) {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ที่อยู่บัตร", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    String[] texts = getText(defaultAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < texts.length; ii++) {
                        cvAddr.drawText(texts[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result1 = Bitmap.createBitmap(RECEIPT_WIDTH, (int) yy, Config.ARGB_8888);
                    cvAddr = new Canvas(result1);
                    cvAddr.drawBitmap(imgAddress, RECEIPT_WIDTH / 2, 0, null);
                    imgAddress.recycle();
                    receiptBuilder.addImage(result1);
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    yy = 19;
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    String[] textsInstall = getText(installAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < textsInstall.length; ii++) {
                        cvInstall.drawText(textsInstall[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result2 = Bitmap.createBitmap(RECEIPT_WIDTH, (int) yy, Config.ARGB_8888);
                    cvInstall = new Canvas(result2);
                    cvInstall.drawBitmap(imgInstall, RECEIPT_WIDTH / 2, 0, null);
                    imgInstall.recycle();
                    receiptBuilder.addImage(result2);

                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(installAddress.TelHome, true);
                } else {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ที่อยู่บัตร", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    String[] texts = getText(defaultAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < texts.length; ii++) {
                        cvAddr.drawText(texts[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result1 = Bitmap.createBitmap(RECEIPT_WIDTH, (int) yy, Config.ARGB_8888);
                    cvAddr = new Canvas(result1);
                    cvAddr.drawBitmap(imgAddress, RECEIPT_WIDTH / 2, 0, null);
                    imgAddress.recycle();
                    receiptBuilder.addImage(result1);
                    receiptBuilder.addParagraph();

                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(defaultAddress.TelHome, true);
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    String[] textsInstall = getText(installAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < textsInstall.length; ii++) {
                        cvInstall.drawText(textsInstall[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result2 = Bitmap.createBitmap(RECEIPT_WIDTH, (int) yy, Config.ARGB_8888);
                    cvInstall = new Canvas(result2);
                    cvInstall.drawBitmap(imgInstall, RECEIPT_WIDTH / 2, 0, null);
                    imgInstall.recycle();
                    receiptBuilder.addImage(result2);
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(installAddress.TelHome, true);
                }
                break;
        }

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("สินค้า", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(contract.ProductName), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("รุ่น", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(contract.MODEL), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขเครื่อง", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(contract.ProductSerialNumber), true);

        if (contract.TradeInDiscount > 0) {
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("ราคา", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(contract.SALES) + " บาท", true);

            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("ส่วนลดเครื่องแสดง", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(contract.TradeInDiscount) + " บาท", true);

            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("ราคาสุทธิ", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(contract.TotalPrice) + " บาท", true);
        } else {
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("ราคาขาย", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(contract.SALES) + " บาท", true);
        }

        if (contract.MODE > 1) {
            float sum = contract.PaymentAmount - contract.TradeInDiscount;
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("งวดที่ 1 ต้องชำระ", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(sum) + " บาท", true);

            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("งวดที่ 2 ถึงงวดที่" + BHUtilities.numericFormat(contract.MODE) + " ต้องชำระงวดละ", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(contract.NextPaymentAmount) + " บาท", true);
        }
        receiptBuilder.addBlankSpace(40);

        String customer = String.format("(%s%s)", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName));

        img = Bitmap.createBitmap(RECEIPT_WIDTH, 500, Config.ARGB_8888);
        img.setHasAlpha(true);

        yy = 0;
        cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        Paint pSignature = new Paint(pValue);
        pSignature.setTextAlign(Align.CENTER);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap signature = generatSignature();
        Bitmap customeSign = generateCustomerSignature(contract.CONTNO);
        Bitmap contractSign = null;

        if (customeSign != null) {
            File file = new File(getAlbumStorageDir(contract.CONTNO), String.format("contract_%s.jpg", contract));
            contractSign = mergeSignature(signature, customeSign, file);
            final int width = signature.getWidth();
            final int height = signature.getHeight();
            final int[] argb = new int[width * height];
            signature.getPixels(argb, 0, width, 0, 0, width, height);
            signature.recycle();
        } else {
            contractSign = signature;
        }

        if (contract.MODE == 1) {
            if (customeSign != null) {
                receiptBuilder.setAlign(Align.CENTER);
            } else {
                receiptBuilder.setAlign(Align.LEFT);
            }

            receiptBuilder.addImage(contractSign);
            yy += 25;

            String TSR = "";
            if (BHGeneral.isOpenDepartmentSignature && BHPreference.hasDepartmentSignatureImage()) {
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "(" + saleLeader.DepartmentHeadName + ")";
                }
            }

            cv.drawText(String.format("%s", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), RECEIPT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format("%s", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), (RECEIPT_WIDTH / 4) * 3, yy, pSignature);

            String[] texts1 = getText(TSR, pSignature, RECEIPT_WIDTH / 2);
            String[] texts2 = getText(customer, pSignature, RECEIPT_WIDTH / 2);
            int num;
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], RECEIPT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (RECEIPT_WIDTH / 4) * 3, yy, pSignature);
                }
            }

            result = Bitmap.createBitmap(RECEIPT_WIDTH, 120, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            receiptBuilder.addImage(result);
        } else {
            if (customeSign != null) {
                receiptBuilder.setAlign(Align.CENTER);
            } else {
                receiptBuilder.setAlign(Align.LEFT);
            }

            receiptBuilder.addImage(contractSign);
            yy += 25;
            String TSR = "";
            if (BHGeneral.isOpenDepartmentSignature && BHPreference.hasDepartmentSignatureImage()) {
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "(" + saleLeader.DepartmentHeadName + ")";
                }
            }
            cv.drawText(String.format("%sผู้ให้เช่าซื้อ", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - (getWidth("ผู้ให้เช่าซื้อ", pSignature) + 50))), RECEIPT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format("%sผู้เช่าซื้อ", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - (getWidth("ผู้เช่าซื้อ", pSignature) + 50))), (RECEIPT_WIDTH / 4) * 3, yy, pSignature);

            String[] texts1 = getText(TSR, pSignature, RECEIPT_WIDTH / 2);
            String[] texts2 = getText(customer, pSignature, RECEIPT_WIDTH / 2);
            int num;
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], RECEIPT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (RECEIPT_WIDTH / 4) * 3, yy, pSignature);
                }
            }

            yy += 120;
            String salename = "(" + BHUtilities.trim(contract.SaleEmployeeName != null ? contract.SaleEmployeeName : "") + ")";
            String salecode = "รหัส " + BHUtilities.trim(contract.SaleCode);


            String saleteamname = "(" + BHUtilities.trim(contract.upperEmployeeName != null ? contract.upperEmployeeName : "") + ")";
            String saleteamcode = "" + BHUtilities.trim(contract.SaleTeamName != null ? contract.SaleTeamName : "");

            if (contract.SaleEmployeeName.equals(contract.upperEmployeeName)) {
                salename = "";
                salecode = "";
            }

            cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), RECEIPT_WIDTH / 4, yy, pSignature);


            cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), (RECEIPT_WIDTH / 4) * 3, yy, pSignature);

            texts1 = getText(saleteamname, pSignature, RECEIPT_WIDTH / 2);
            texts2 = getText(salename, pSignature, RECEIPT_WIDTH / 2);
            if (texts1.length > texts2.length) {
                num = texts1.length;
            } else {
                num = texts2.length;
            }

            for (int ii = 0; ii < num; ii++) {
                yy += fontSize + lineSpace;
                if (texts1.length > ii) {
                    cv.drawText(texts1[ii], RECEIPT_WIDTH / 4, yy, pSignature);
                }

                if (texts2.length > ii) {
                    cv.drawText(texts2[ii], (RECEIPT_WIDTH / 4) * 3, yy, pSignature);
                }
            }

            yy += fontSize + lineSpace;
            cv.drawText(saleteamcode, RECEIPT_WIDTH / 4, yy, pSignature);
            cv.drawText(salecode, (RECEIPT_WIDTH / 4) * 3, yy, pSignature);

            result = Bitmap.createBitmap(RECEIPT_WIDTH, 300, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            receiptBuilder.addImage(result);
        }

        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText(String.format(" รหัส %s %s", BHUtilities.trim(contract.SaleCode), BHUtilities.trim(contract.SaleEmployeeName)), true);
        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText(String.format(" %s %s", BHUtilities.trim(contract.SaleTeamName), BHUtilities.trim(contract.upperEmployeeName)), true);
        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(5);

        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addText("QR Code สำหรับชำระเงิน (Mobile banking)", true);
        receiptBuilder.setAlign(Paint.Align.CENTER);
        Bitmap bmpPromtpay = createQRCodePromtpay(contract);
        receiptBuilder.addImage(bmpPromtpay);
        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(5);

        receiptBuilder.setTextSize(22);
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("** หมายเหตุ: ผู้เช่าซื้อหรือผู้ซื้อมีสิทธิเลิกสัญญาโดยการ", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("ส่งหนังสือแสดงเจตนาได้ภายใน 7 วัน นับแต่วันที่ผู้เช่าซื้อ", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("หรือผู้ซื้อได้รับสินค้าและผู้ให้เช่าซื้อหรือผู้ขายจะคืนเงิน", true);
        receiptBuilder.addParagraph();
        receiptBuilder.setTextSize(20);
        receiptBuilder.addText("เต็มจำนวนที่ผู้เช่าซื้อหรือผู้ซื้อได้จ่ายไปเพื่อเช่าซื้อหรือซื้อสินค้า", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("ภายใน 15 วันนับแต่วันที่ผู้เช่าซื้อหรือผู้ซื้อใช้สิทธิในการคืนสินค้า", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(20);

        Bitmap bmp = receiptBuilder.build();

        return scaleBitmap(bmp, bmp.getWidth(), bmp.getHeight());
    }

    public static Bitmap getNewReceiptImage(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {
        Log.e("payment info", String.valueOf(paymentInfo));
        ReceiptBuilder receiptBuilder = new ReceiptBuilder(RECEIPT_WIDTH);
        receiptBuilder.setMargin(5, 0);
        receiptBuilder.setAlign(Align.CENTER);
        receiptBuilder.setColor(Color.BLACK);
        receiptBuilder.setTextSize(26);
        receiptBuilder.addImage(shortHeaderPrint());
        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(10);
        receiptBuilder.setTextSize(24);
        receiptBuilder.addText("ใบรับเงิน");
        receiptBuilder.addParagraph();
        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(10);

        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("วันที่รับเงิน", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
//        receiptBuilder.addText(paymentInfo.paymentDate + "", true);
        receiptBuilder.addText(BHUtilities.dateFormat(paymentInfo.PayDate, BHUtilities.DEFAULT_DATE_TIME_FORMAT), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(paymentInfo.ReceiptCode, true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("เลขที่สัญญา", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(paymentInfo.CONTNO, true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("ชื่อลูกค้า", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        try {
            receiptBuilder.addText(debtorCustomerInfo.CustomerFullName(), true);
        } catch (Exception e) {
            receiptBuilder.addText(paymentInfo.CustomerName, true);
        }

        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("อ้างอิงสัญญาเลขที่", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(ManualDocumentBookRunningNo, true);
        }

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(paymentInfo.ProductName, true);

        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {
                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addText(BHUtilities.trim(" งวด 1 (ชำระครบ)"), false);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);
            } else {
                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addText(BHUtilities.trim(" งวด 1 (ชำระบางส่วน)"), false);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);
            }
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText("รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText(BHUtilities.trim(String.format(" งวด %d", paymentInfo.MODE)), false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท", true);
                } else {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText(BHUtilities.trim(String.format(" งวด %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท", true);
                }
            } else {
                String txtPeriodAmountLabel = "";
                if (paymentInfo.BalancesOfPeriod == 0) {
                    if (paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                        txtPeriodAmountLabel = "ชำระงวดที่ %d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "ชำระงวดที่ %d";
                    }
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);
                } else {
                    txtPeriodAmountLabel = " งวด %d (ชำระบางส่วน)";
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);
                }
            }
        }

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("ส่วนลดตัดสด", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.CloseAccountDiscountAmount) + " บาท", true);

            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("จำนวนที่ชำระ", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount - paymentInfo.CloseAccountDiscountAmount) + " บาท", true);
        } else {
            /**ยอดเงินคงเหลือของงวดนั้น**/
            if (paymentInfo.BalancesOfPeriod != 0) {
                receiptBuilder.addParagraph();
                receiptBuilder.setAlign(Paint.Align.LEFT);
                receiptBuilder.addText(String.format(" คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), false);
                receiptBuilder.setAlign(Paint.Align.RIGHT);
                receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท", true);

                if (paymentInfo.PaymentAppointmentDate != null) {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Paint.Align.LEFT);
                    receiptBuilder.addText("วันนัดชำระ", false);
                    receiptBuilder.setAlign(Paint.Align.RIGHT);
                    receiptBuilder.addText(BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate), true);
                }
            }
            /**ยอดคงเหลือของงวดถัดไป**/
            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {
                } else {
                    if ((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE) {
                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Paint.Align.LEFT);
                        receiptBuilder.addText(String.format(" คงเหลือ งวดที่ %d เป็นเงิน", paymentInfo.MODE), false);
                        receiptBuilder.setAlign(Paint.Align.RIGHT);
                        receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", true);
                    } else {
                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Paint.Align.LEFT);
                        receiptBuilder.addText(String.format(" คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), false);
                        receiptBuilder.setAlign(Paint.Align.RIGHT);
                        receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", true);
                    }
                }
            }
        }

        receiptBuilder.addBlankSpace(60);
            Bitmap img = Bitmap.createBitmap(RECEIPT_WIDTH, 80, Config.ARGB_8888);
            img.setHasAlpha(true);

            float yy = 0;
            Canvas cv = new Canvas(img);
            cv.drawColor(Color.WHITE);

            Paint p = new Paint();
            p.setColor(Color.BLACK);
            p.setStyle(Style.FILL_AND_STROKE);
            p.setAntiAlias(true);

            float fontSize = 22;
            float lineSpace = fontSize / 2;
            Paint pTitle2 = new Paint(p);
            pTitle2.setTypeface(Typeface.DEFAULT);
            pTitle2.setTextSize(fontSize);
            pTitle2.setTextAlign(Align.LEFT);

            Paint pValue = new Paint(pTitle2);
            pValue.setTypeface(null);
            pValue.setTextAlign(Align.LEFT);
            Paint pSignature = new Paint(pValue);
            pSignature.setTextAlign(Align.CENTER);
            String sale = "";
            if (paymentInfo.PaymentType.equals("Qrcode")) {
                sale = String.format("%s", "(TMB QR CODE)");
            } else {
                sale = String.format("(%s) %s", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "", paymentInfo.CashCode);
            }
            yy += 25;
            int Value = RECEIPT_WIDTH / 2;
            String[] texts = getText(sale, pSignature, RECEIPT_WIDTH);
            for (int ii = 0; ii < texts.length; ii++) {
                if (!paymentInfo.PaymentType.equals("Qrcode")) {
                    if (ii == 0) {
                        cv.drawText(String.format("%sผู้รับเงิน", getSignatureUnderline(pSignature, (RECEIPT_WIDTH) - (getWidth("ผู้รับเงิน", pSignature) + (RECEIPT_WIDTH / 2)))), RECEIPT_WIDTH / 2, yy, pSignature);
                    }
                }
                yy += fontSize + lineSpace;
                cv.drawText(texts[ii], Value, yy, pSignature);
            }

            Bitmap result = Bitmap.createBitmap(RECEIPT_WIDTH, 80, Config.ARGB_8888);
            cv = new Canvas(result);
            cv.drawBitmap(img, 0, 0, null);
            img.recycle();

            receiptBuilder.addImage(result);
            receiptBuilder.addParagraph();

        String qrcodeSurvey = "http://app.thiensurat.co.th/VerifySeller/index.php?EmpID=" + paymentInfo.EmpID;
        Bitmap bmpSurvey = createQRCodeForSurvey(qrcodeSurvey);

        Bitmap bottomReceipt = null;
        File file = new File(getAlbumStorageDir(paymentInfo.CONTNO), String.format("bottomreceipt_%s.jpg", paymentInfo.CONTNO));
        if (file.exists()) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bottomReceipt = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
            bottomReceipt = Bitmap.createScaledBitmap(bottomReceipt, bottomReceipt.getWidth(), bottomReceipt.getHeight(), true);
        } else {
            bottomReceipt = mergeBMP(bmpSurvey, getStringForMerge(), file);
        }

        Bitmap bmpMerge =null;
        File fileS = new File(getAlbumStorageDir(paymentInfo.CONTNO), String.format("telesale_with_survey_%s.jpg", paymentInfo.CONTNO));
        if (fileS.exists()) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmpMerge = BitmapFactory.decodeFile(fileS.getAbsolutePath(), bmOptions);
            bmpMerge = Bitmap.createScaledBitmap(bmpMerge, bmpMerge.getWidth(), bmpMerge.getHeight(), true);
        } else {
            bmpMerge = mergeBMP2(bottomReceipt, logoTelesale(), fileS);
        }

        receiptBuilder.setAlign(Paint.Align.CENTER);
        receiptBuilder.addImage(bmpMerge);
        receiptBuilder.addParagraph();

//        if ((paymentInfo.PaymentPeriodNumber + 1) > 1) {
        if (paymentInfo.PaymentPeriodNumber > 1) {
            try {
                receiptBuilder.setAlign(Paint.Align.CENTER);
                String qrcode = String.format("|010755600021301" + CR + "%s" + CR + "%s" + CR + "%s", paymentInfo.IDCard, paymentInfo.CONTNO, "0");
                Bitmap bmpQR = createQRCodeForReceipt(qrcode);
                receiptBuilder.addImage(bmpQR);
                receiptBuilder.setAlign(Align.CENTER);
                receiptBuilder.addImage(textBottomQRCode());
                receiptBuilder.addParagraph();
            } catch (NullPointerException e) {

            }
        }

        return receiptBuilder.build();
    }

    public final static char CR = (char) 0x0D;

    public static Bitmap getNewSendMoneyImage(SendMoneyInfo sendMoney) {
        ReceiptBuilder receiptBuilder = new ReceiptBuilder(576);
        receiptBuilder.setMargin(5, 0);
        receiptBuilder.setAlign(Align.CENTER);
        receiptBuilder.setColor(Color.BLACK);
        receiptBuilder.setTextSize(24);
        receiptBuilder.addImage(shortHeaderPrint());
        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(10);
        receiptBuilder.setAlign(Align.CENTER);

        receiptBuilder.addText(String.format("ใบนำส่ง%s", sendMoney.PaymentTypeName));
        receiptBuilder.addParagraph();
        receiptBuilder.addText(String.format("(%s)", sendMoney.ChannelItemName));

        if (sendMoney.Reference2.length() > 8) {
            String strBarcodeNo = sendMoney.Reference2;
            receiptBuilder.addParagraph();
            receiptBuilder.addImage(BHBarcode.generateCode128(strBarcodeNo, 560, 125));
        }

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("ชื่อผู้ส่งเงิน", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(sendMoney.FirstName + " " + sendMoney.LastName), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("รหัสผู้ส่ง", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(sendMoney.CreateBy), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("วันที่ส่ง", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.dateFormat(sendMoney.SendDate, BHUtilities.DEFAULT_DATE_FORMAT), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("Ref. 1", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(sendMoney.Reference1), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("Ref. 2", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(sendMoney.Reference2), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("ชื่อผู้รับเงิน/สาขา", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(sendMoney.PayeeName), true);

        if (sendMoney.PaymentType.toLowerCase().equals("credit")) {
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("เชลล์สลิป", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท", true);
        } else if (sendMoney.PaymentType.toLowerCase().equals("cheque")) {
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("เช็ค", false);
            receiptBuilder.setAlign(Paint.Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท", true);
        }

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Paint.Align.LEFT);
        receiptBuilder.addText("จำนวนเงิน", false);
        receiptBuilder.setAlign(Paint.Align.RIGHT);
        receiptBuilder.addText(BHUtilities.numericFormat(sendMoney.SendAmount) + " บาท", true);

        if (sendMoney.Reference2.length() == 8) {

            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText("สำหรับธนาคาร", true);

            receiptBuilder.setAlign(Paint.Align.CENTER);
            String SendMoneyBarcode = String.format("|010755600021300" + CR + "%s" + CR + "%s" + CR + "%s", sendMoney.Reference1, "", BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", ""));

            Bitmap bmpQR = createQRCode(SendMoneyBarcode);
            receiptBuilder.addImage(bmpQR);
            receiptBuilder.addBlankSpace(20);
            Bitmap bmp = BHBarcode.generateCode128(SendMoneyBarcode, 725, 95);
            receiptBuilder.addImage(bmp);
            receiptBuilder.addParagraph();

            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText(String.format("| 010755600021300 %s %s", sendMoney.Reference1, BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", "")));
            receiptBuilder.addParagraph();

            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText(String.format("เพื่อเข้าบัญชี %s", sendMoney.ChannelItemName));
            receiptBuilder.addParagraph();

            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText(String.format("EMPID(Ref.1): %s", BHUtilities.trim(sendMoney.Reference1)));
            receiptBuilder.addParagraph();

            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText(String.format("TNSNO(Ref.2): %s", BHUtilities.trim(sendMoney.Reference2)));
            receiptBuilder.addParagraph();

            receiptBuilder.setAlign(Paint.Align.LEFT);
            receiptBuilder.addText(String.format("จำนวนเงิน %s บาท", BHUtilities.numericFormat(sendMoney.SendAmount)));
            receiptBuilder.addParagraph();
        }

        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(20);

//        Bitmap bitmap = receiptBuilder.build();
//        return scaleBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight());
        return receiptBuilder.build();
    }

    public static String barcodeString(SendMoneyInfo sendMoney) {
        String SendMoneyBarcode = "";
        if (sendMoney.Reference2.length() > 8) {
            SendMoneyBarcode = sendMoney.Reference2;
        } else if (sendMoney.Reference2.length() == 8) {
            SendMoneyBarcode = String.format("|010755600021300" + CR + "%s" + CR + "%s" + CR + "%s", sendMoney.Reference1, "", BHUtilities.numericFormat(sendMoney.SendAmount).replace(",", "").replace(".", ""));
        }

        return SendMoneyBarcode;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public static Bitmap createBarcode128(String contents) {
        String contentsToEncode = contents;
        EnumMap<EncodeHintType, Object> hint = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hint.put(EncodeHintType.MARGIN, 4);
        hint.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hint = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hint.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        BitMatrix result = null;
        try {
            result = new MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128, 780, 100, hint);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bit = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bit.setPixels(pixels, 0, w, 0, 0, w, h);
        return bit;
    }

    public static Bitmap createQRCode(String contents) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(contents, BarcodeFormat.QR_CODE, 250, 250);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap createQRCodePromtpay(ContractInfo contract) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            String payload          = "000201010212";
            String merchant         = "30";
            String AIDField         = "00";
            String AID              = "A000000677010112";
            String AIDLength        = "16";
            String billerID         = "0115010755600021389";
            String ref1             = contract.ProductSerialNumber;
            String ref2             = contract.CONTNO;
            String ref1Field        = "02";
            String ref2Field        = "03";
            String ref1Length       = ref1.length() < 10 ? "0" + ref1.length() : String.valueOf(ref1.length());
            String ref2Length       = ref2.length() < 10 ? "0" + ref2.length() : String.valueOf(ref2.length());
            String compField        = "59";
            String comp             = "THIENSURAT PUBLIC CO";
            String compLength       = String.valueOf(comp.length());
            String qrcodeField      = "62";
            String qrcodeIDField    = "07";
            String qrcodeID         = "DTSR" + ref1;
            String qrcodeIDLength   = String.valueOf((qrcodeID.length() + qrcodeIDField.length()) + 2);
            String checkSumField    = "6304";
            String currencyTHB      = "5303764";
            String currencyTag      = "54";
            String currencyCode     = "5802TH";

            String amount           = "0.00";//String.format("%.2f", (contract.PaymentAmount - contract.TradeInDiscount));

            String tag30Length      = String.valueOf(AIDField.length()
                    + AIDLength.length()
                    + AID.length()
                    + billerID.length()
                    + ref1Field.length()
                    + ref1Length.length()
                    + ref1.length()
                    + ref2Field.length()
                    + ref2Length.length()
                    + ref2.length());

            String qrcode = payload
                    + merchant
                    + tag30Length
                    + AIDField
                    + AIDLength
                    + AID
                    + billerID
                    + ref1Field
                    + ref1Length
                    + ref1
                    + ref2Field
                    + ref2Length
                    + ref2
                    + currencyTHB
                    + currencyTag
                    + String.valueOf(amount.length() < 10 ? "0" + amount.length() : amount.length())
                    + amount
                    + currencyCode
                    + compField
                    + compLength
                    + comp
                    + qrcodeField
                    + qrcodeIDLength
                    + qrcodeIDField
                    + String.valueOf(qrcodeID.length())
                    + qrcodeID
                    + checkSumField;
            qrcode = qrcode + checksumQRcode(qrcode);
            BitMatrix bitMatrix = multiFormatWriter.encode(qrcode, BarcodeFormat.QR_CODE, 250, 250);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap createQRCodeForReceipt(String contents) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(contents, BarcodeFormat.QR_CODE, 250, 250);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap createQRCodeForSurvey(String contents) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(contents, BarcodeFormat.QR_CODE, 155, 155);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
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

    public static Bitmap createStringForMerge() {
        File file = new File(getAlbumStorageDir("textmerge"), String.format("text_center.jpg"));
        Bitmap img = Bitmap.createBitmap(RECEIPT_WIDTH, 150, Config.ARGB_8888);
        img.setHasAlpha(true);

        float yy = 0;
        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);

        float fontSize = 20;
        float lineSpace = fontSize / 2;
        Paint pTitle2 = new Paint(p);
        pTitle2.setTypeface(Typeface.DEFAULT);
        pTitle2.setTextSize(fontSize);
        pTitle2.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle2);
        pValue.setTextAlign(Align.LEFT);
        Paint pSignature = new Paint(pValue);
        pTitle2.setTypeface(Typeface.DEFAULT);
        pTitle2.setTextSize(fontSize);
        pSignature.setTextAlign(Align.CENTER);

        yy += 22;
        int Value = RECEIPT_WIDTH / 4;
        yy += fontSize + lineSpace;
        cv.drawText("ขอเชิญร่วมแสดงความคิดเห็น", Value, yy, pSignature);
        yy += fontSize + lineSpace;
        cv.drawText("เพื่อนำมาพัฒนาและปรับปรุง",Value, yy, pSignature);
        yy += fontSize + lineSpace;
        cv.drawText("บริการของเรา", Value, yy, pSignature);

        Bitmap result = Bitmap.createBitmap(RECEIPT_WIDTH, 150, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            result.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static Bitmap textBottomQRCode() {
        Bitmap img = Bitmap.createBitmap(RECEIPT_WIDTH, 180, Config.ARGB_8888);
        img.setHasAlpha(true);

        float yy = 0;
        Canvas cv = new Canvas(img);
        cv.drawColor(Color.WHITE);

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Style.FILL_AND_STROKE);
        p.setAntiAlias(true);

        float fontSize = 20;
        float lineSpace = fontSize / 2;
        Paint pTitle2 = new Paint(p);
        pTitle2.setTypeface(Typeface.DEFAULT);
        pTitle2.setTextSize(fontSize);
        pTitle2.setTextAlign(Align.LEFT);

        Paint pValue = new Paint(pTitle2);
        pValue.setTextAlign(Align.LEFT);
        Paint pSignature = new Paint(pValue);
        pTitle2.setTypeface(Typeface.DEFAULT);
        pTitle2.setTextSize(fontSize);
        pSignature.setTextAlign(Align.CENTER);

        int Value = RECEIPT_WIDTH / 2;
        yy += fontSize + lineSpace;
        cv.drawText("สามารถชำระเงินได้ที่", Value, yy, pSignature);
        yy += fontSize + lineSpace;
        cv.drawText("เคาน์เตอร์เซอร์วิส, เทสโก้โลตัส, หรือผ่าน",Value, yy, pSignature);
        yy += fontSize + lineSpace;
        cv.drawText("Mobile banking SCB, BBL, Kbank, KTB, Tbank", Value, yy, pSignature);

        Bitmap result = Bitmap.createBitmap(RECEIPT_WIDTH, 180, Config.ARGB_8888);
        cv = new Canvas(result);
        cv.drawBitmap(img, 0, 0, null);
        img.recycle();

        return result;
    }

    public static Bitmap createBarcodeByLibrary(String content) {
        Bitmap bitmap = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        File barcode = new File(getAlbumStorageDir("barcode"), "barcode.jpg");
//
//        BarCode code128 = new BarCode();
//        code128.setCodeToEncode(content);
//        code128.setSymbology(IBarCode.CODE128);
//        code128.setX(2);
//        code128.setY(50);
//        code128.setRightMargin(0);
//        code128.setLeftMargin(0);
//        code128.setTopMargin(0);
//        code128.setBottomMargin(0);
//        code128.setChecksumEnabled(false);
//        code128.setFnc1(IBarCode.FNC1_NONE);
//
//        try
//        {
//            code128.draw(barcode.getAbsolutePath());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        bitmap = BitmapFactory.decodeFile(barcode.getAbsolutePath(), bmOptions);

//        Code128 code128 = new Code128();
        return bitmap;
    }

    public static String checksumQRcode(String input) {
        try {
            int crc = 0xFFFF;
            int polynomial = 0x1021;
            byte[] bytes = input.getBytes();
            for (byte b : bytes) {
                for (int i = 0; i < 8; i++) {
                    boolean bit = ((b >> (7 - i) & 1) == 1);
                    boolean c15 = ((crc >> 15 & 1) == 1);
                    crc <<= 1;
                    if (c15 ^ bit) crc ^= polynomial;
                }
            }
            crc &= 0xffff;
            return String.format("%04x", crc).toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     *
     *
     * End
     *
     */
}
