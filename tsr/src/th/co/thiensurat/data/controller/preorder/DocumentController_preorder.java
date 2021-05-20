package th.co.thiensurat.data.controller.preorder;

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

import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.R;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.ThemalPrintController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.ManualDocumentInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.PrintTextInfo;

//import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;
//import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;

//import static android.graphics.Color.BLACK;
//import static android.graphics.Color.WHITE;

public class DocumentController_preorder {

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

        if(ThemalPrintController.getAlphabetOnly(text).length() <= maxLength){
            result.add(text);
        } else {
            String[] textArray = text.split("\\s+");
            result.add("");
            for(int i = 0; i < textArray.length; i++){
                int index = result.size() - 1;
                String tempText = result.get(index);

                if (tempText.equals("")) {
                    tempText += textArray[i];
                } else {
                    tempText += " " + textArray[i];
                }

                if(ThemalPrintController.getAlphabetOnly(tempText).length() <= maxLength){
                    result.set(index, tempText);
                } else {
                    String[] tempTextArray = tempText.split("\\s+");
                    String resultTempText1 = "";
                    String resultTempText2 = "";

                    if(tempTextArray.length != 1) {
                        for (int j = 0; j < tempTextArray.length; j++) {
                            if (j == (tempTextArray.length - 1)) {
                                resultTempText2 = tempTextArray[j];
                            } else {
                                if(resultTempText1.equals("")) {
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

                        for(int j = 0; j< tempStr.size(); j++){
                            if(j == 0 ){
                                result.set(index, tempStr.get(j));
                            } else{
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

    private static List<String> alignTextByLength(String text, int length){
        List<String> result = new ArrayList<String>();

        if(ThemalPrintController.getAlphabetOnly(text).length() <= length){
            result.add(text);
        } else {
            result.add("");
            int start = 0;
            for (int i = 0; i < text.length(); i++){
                int index = result.size() - 1;
                String tempText = text.substring(start, i + 1);

                if(ThemalPrintController.getAlphabetOnly(tempText).length() <= length){
                    result.set(index, tempText);
                } else {
                    if(i != text.length() - 1){
                        start = i;
                        result.add("");
                    }
                }
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignCenter(String text){
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempText = getTextByPrintText(text, ThemalPrintController.maxTextLength );

        for(int i = 0; i < tempText.length; i++){
            result.add(new PrintTextInfo(ThemalPrintController.calculateCenter(tempText[i])));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignCenter(String text, PrintTextInfo.FontType fontType){
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempText = getTextByPrintText(text, ThemalPrintController.maxTextLength );

        for(int i = 0; i < tempText.length; i++){
            result.add(new PrintTextInfo(ThemalPrintController.calculateCenter(tempText[i]), fontType));
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftByOffSetLeft(String left, String right){
        List<PrintTextInfo> result = new ArrayList<>();

        int offSetLeft = 20;
        String[] tempLeft = getTextByPrintText(left, offSetLeft);
        String[] tempRight = getTextByPrintText(right, ThemalPrintController.maxTextLength - offSetLeft);

        int size = Math.max(tempLeft.length, tempRight.length);

        for(int i = 0; i < size; i++){
            result.add(new PrintTextInfo(ThemalPrintController.calculateOffSetLeft(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "", offSetLeft)));
        }
        return result;
    }




    private static List<PrintTextInfo> getTextAlignLeftRight(String left, String right){
        List<PrintTextInfo> result = new ArrayList<>();

        if(ThemalPrintController.getAlphabetOnly(String.format("%s %s", left, right)).length() <=  ThemalPrintController.maxTextLength){
            result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(left, right)));
        } else {
            String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
            String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

            int size = Math.max(tempLeft.length, tempRight.length);

            for(int i = 0; i < size; i++){
                result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : "")));
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextAlignLeftRight(String left, String right, PrintTextInfo.FontType fontType){
        List<PrintTextInfo> result = new ArrayList<>();

        if(ThemalPrintController.getAlphabetOnly(String.format("%s %s", left, right)).length() <=  ThemalPrintController.maxTextLength){
            result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(left, right), fontType));
        } else {
            String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
            String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

            int size = Math.max(tempLeft.length, tempRight.length);

            for(int i = 0; i < size; i++){
                result.add(new PrintTextInfo(ThemalPrintController.calculateLeghtRight(i < tempLeft.length ? tempLeft[i] : "", i < tempRight.length ? tempRight[i] : ""), fontType));
            }
        }
        return result;
    }

    private static List<PrintTextInfo> getTextLeftRightAlignCenter(String left, String right){
        return getTextLeftRightAlignCenter( left,  right,  "TH");
    }

    private static List<PrintTextInfo> getTextLeftRightAlignCenter(String left, String right, String Language){
        List<PrintTextInfo> result = new ArrayList<>();

        String[] tempLeft = getTextByPrintText(left, (ThemalPrintController.maxTextLength / 2) - 2);
        String[] tempRight = getTextByPrintText(right, (ThemalPrintController.maxTextLength / 2) - 2);

        int size = Math.max(tempLeft.length, tempRight.length);

        for(int i = 0; i < size; i++){
            result.add(new PrintTextInfo(String.format(" %s  %s ",
                    ThemalPrintController.calculateCenterByLength(i < tempLeft.length ? tempLeft[i] : "", (ThemalPrintController.maxTextLength / 2) - 2),
                    ThemalPrintController.calculateCenterByLength(i < tempRight.length ? tempRight[i] : "", (ThemalPrintController.maxTextLength / 2) - 2))
                    , Language));
        }
        return result;
    }


    private static List<PrintTextInfo> getTextAlignLeft(String left){
        return getTextAlignLeft(left, "TH");
    }

    private static List<PrintTextInfo> getTextAlignLeft(String left, String language){
        List<PrintTextInfo> result = new ArrayList<>();

        if(ThemalPrintController.getAlphabetOnly(left).length() <=  ThemalPrintController.maxTextLength){
            result.add(new PrintTextInfo(left, language));
        } else {
            String[] tempLeft = getTextByPrintText(left, ThemalPrintController.maxTextLength);

            for(int i = 0; i < tempLeft.length; i++){
                result.add(new PrintTextInfo(tempLeft[i], language));
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
        String title = contract.MODE > 1 ? "ใบจอง" : "ใบจอง";
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
        cv.drawText("วันที่ทำใบจอง", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.dateFormat(contract.EFFDATE), xValue, yy, pValue);

        if (manual != null) {
            yy += fontSize + lineSpace;
            cv.drawText("เลขที่อ้างอิง", xTitle, yy, pTitle);
            //cv.drawText(String.format("%s/%4d", BHUtilities.trim(manual.ManualVolumeNo), manual.ManualRunningNo).replace(' ', '0'), xValue, yy, pValue);
            cv.drawText(String.format("%4d", manual.ManualRunningNo).replace(' ', '0'), xValue, yy, pValue);
        }

        yy += fontSize + lineSpace;
        cv.drawText("เลขที่ใบจอง", xTitle, yy, pTitle);
        cv.drawText(BHUtilities.trim(contract.CONTNO), xValue, yy, pValue);

        String[] texts = getText(String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)), pValue, LAYOUT_WIDTH - xValue);
        for (int ii = 0; ii < texts.length; ii++) {
            yy += fontSize + lineSpace;
            if (ii == 0) {
                cv.drawText(contract.MODE > 1 ? "ผู้เช่าซื้อ" : "ผู้จอง", xTitle, yy, pTitle);
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
                    cv.drawText(String.format("%sผู้จอง", getSignatureUnderline(pSignature, (LAYOUT_WIDTH) - (getWidth("ผู้จอง", pSignature) + (LAYOUT_WIDTH / 2)))), LAYOUT_WIDTH / 2, yy, pSignature);
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
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            // YIM Change TSR_COMMITTEE_NAME
            if(BHGeneral.isOpenDepartmentSignature&&BHPreference.hasDepartmentSignatureImage()){
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "("+saleLeader.DepartmentHeadName+")";
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
        listText.addAll(getTextAlignCenter(contract.MODE > 1 ? "ใบจอง" : "ใบจอง", PrintTextInfo.FontType.Bold));
        listText.add(new PrintTextInfo("printContractPageTitle"));
        listText.add(new PrintTextInfo("beginContractPage"));

        listText.addAll(getTextAlignLeftByOffSetLeft(" วันที่ทำใบจอง", BHUtilities.dateFormat(contract.EFFDATE)));

        /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
        /*if (manual != null) {
            listText.addAll(getTextAlignLeftByOffSetLeft("เลขที่อ้างอิง", String.format("%4d", manual.ManualRunningNo).replace(' ', '0')));
        }*/
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่อ้างอิง", contract.ContractReferenceNo != null ? contract.ContractReferenceNo : ""));
        /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/

        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่ใบจอง", BHUtilities.trim(contract.CONTNO)));
        listText.addAll(getTextAlignLeftByOffSetLeft(contract.MODE > 1 ? " ผู้เช่าซื้อ" : " ผู้จอง", String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName))));
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่บัตร", BHUtilities.trim(contract.IDCard)));
        /*listText.addAll(getTextAlignLeftByOffSetLeft("ที่อยู่บัตร", defaultAddress.Address()));
        listText.addAll(getTextAlignLeftByOffSetLeft("ที่ติดตั้ง", installAddress.Address()));*/


        switch (contract.CustomerType) {
            case "0":
            case "2":
                if(defaultAddress.TelMobile.equals(installAddress.TelMobile)){
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
                if(defaultAddress.TelMobile.equals(installAddress.TelMobile)){
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
            listText.addAll(getTextLeftRightAlignCenter("           ผู้ขาย       ", "          ผู้จอง        "));
            String TSR = "(นายวิรัช วงศ์นิรันดร์)";
            // YIM Change TSR_COMMITTEE_NAME
            if(BHGeneral.isOpenDepartmentSignature&&BHPreference.hasDepartmentSignatureImage()){
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "("+saleLeader.DepartmentHeadName+")";
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
            if(BHGeneral.isOpenDepartmentSignature&&BHPreference.hasDepartmentSignatureImage()){
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "("+saleLeader.DepartmentHeadName+")";
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
        listText.addAll(getTextAlignLeftByOffSetLeft(" เลขที่ใบจอง", paymentInfo.CONTNO));
        listText.addAll(getTextAlignLeft(" ชื่อลูกค้า " + debtorCustomerInfo.CustomerFullName()));


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

                try {
                    String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                    if (getOrganizationCode.equals("1")) {
                        listText.addAll(getTextAlignLeftRight(BHUtilities.trim(" งวด 1 (ชำระบางส่วน)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));

                        // tsr
                    } else {
                        if (paymentInfo.MODE == 1) {

                            listText.addAll(getTextAlignLeftRight(BHUtilities.trim(" มัดจำ (ชำระบางส่วน)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));

                            // alpine
                        } else {
                            listText.addAll(getTextAlignLeftRight(BHUtilities.trim(" มัดจำ (ชำระบางส่วน)"), "ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));


                            // tsrl
                        }
                    }

                }
                catch (Exception ec){


                }





            }
            listText.addAll(getTextAlignLeftRight(" ", "รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท"));
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
                if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                    listText.addAll(getTextAlignLeftRight(BHUtilities.trim(String.format(" งวด %d", paymentInfo.MODE)), BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท"));
                } else {
                    listText.addAll(getTextAlignLeftRight(BHUtilities.trim(String.format(" งวด %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท"));
                }
            } else{
                String txtPeriodAmountLabel = "";
                if (paymentInfo.BalancesOfPeriod == 0) {
                    if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE){
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
        } else{
            /**ยอดเงินคงเหลือของงวดนั้น**/
            if (paymentInfo.BalancesOfPeriod != 0) {
                listText.addAll(getTextAlignLeftRight(String.format(" คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), "ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท"));
                listText.addAll(getTextAlignLeftRight(" วันนัดชำระ", BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate)));
            }

            /**ยอดคงเหลือของงวดถัดไป**/
            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {
                } else {
                    if((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE){

                        listText.addAll(getTextAlignLeftRight(String.format(" คงเหลือ งวดที่ %d เป็นเงิน", paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));



                    }else {




                        try {
                            String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                            if (getOrganizationCode.equals("1")) {
                                listText.addAll(getTextAlignLeftRight(String.format(" คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber + 1, paymentInfo.MODE), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));

                                // tsr
                            } else {
                                if (paymentInfo.MODE == 1) {


                                    // alpine
                                } else {
                                    listText.addAll(getTextAlignLeftRight(String.format(" คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber , paymentInfo.MODE-1), BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท"));


                                    // tsrl
                                }
                            }

                        }
                        catch (Exception ec){


                        }



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

    public static Bitmap shortHeaderPrintTSRL() {
        Bitmap bmp = null;
        try {
            ins = mContext.getResources().getAssets().open("tsrl_logo.png");
            bmp = BitmapFactory.decodeStream(ins);
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static Bitmap shortHeaderPrintAlpine() {
        Bitmap bmp = null;
        try {
            ins = mContext.getResources().getAssets().open("alpine_logo.png");
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

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

   public static Bitmap mergeSignature(Bitmap bmp1, Bitmap bmp2, File path) {
        Bitmap result = Bitmap.createBitmap((bmp1.getWidth() + bmp2.getWidth()), bmp1.getHeight(), Config.RGB_565 );
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




    public static int signatureWidthLimit = 220;
    public static int signatureHeightLimit = 80;
    public static Bitmap generatSignature() {
        Bitmap tempSignature = null,signature;
        Bitmap img = null;
        Canvas cv;
        try {
            if (BHGeneral.isOpenDepartmentSignature&&BHPreference.hasDepartmentSignatureImage()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Config.ARGB_8888;
                tempSignature = BitmapFactory.decodeFile(BHPreference.departmentSignaturePath + BHPreference.departmentSignatureName, options);
            }else{
                Context mContext = BHApplication.getContext();
                tempSignature = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sample_signature);
            }

            if(tempSignature != null){
                signature = scaleBitmapByHeight(tempSignature);
                img = Bitmap.createBitmap(signature.getWidth(), signature.getHeight(), Config.ARGB_8888);
                img.setHasAlpha(true);
                cv = new Canvas(img);
                cv.drawBitmap(signature, 0, 0, null);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return img;
    }

    public static Bitmap generateCustomerSignature(String contract) {
        Bitmap tempSignature = null,signature;
        Bitmap img = null;
        Canvas cv;

        File file = new File(getAlbumStorageDir(contract), String.format("Signature_%s.jpg", contract));
        if (file.exists()) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            tempSignature = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
            tempSignature = Bitmap.createScaledBitmap(tempSignature, tempSignature.getWidth(), tempSignature.getHeight(), true);
        }

        if(tempSignature != null){
            img = Bitmap.createBitmap(tempSignature.getWidth(), tempSignature.getHeight(), Config.ARGB_8888);
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
        int width =  bm.getWidth();
        int height = bm.getHeight();

        if(bm.getHeight() > signatureHeightLimit){
            width = (int) (1f * signatureHeightLimit / height * width);
            height = signatureHeightLimit;
        }

        if(width > signatureWidthLimit){
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

    public static  Bitmap getNewContactImage(ContractInfo contract, AddressInfo defaultAddress, AddressInfo installAddress) {
        ReceiptBuilder receiptBuilder = new ReceiptBuilder(RECEIPT_WIDTH);
        receiptBuilder.setMargin(5, 0);
        receiptBuilder.setTextSize(24);
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.setColor(Color.BLACK);
//        receiptBuilder.addText("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)", true);
//        receiptBuilder.addParagraph();
//        receiptBuilder.addText("43/9 หมู่ 7 ซ.ชูชาติอนุสรณ์ 4 ต.บางตลาด", true);
//        receiptBuilder.addParagraph();
//        receiptBuilder.addText("อ.ปากเกร็ด จ.นนทบุรี 11120", true);
//        receiptBuilder.addParagraph();
//        receiptBuilder.addText("โทร. 0 2819 8888 แฟกซ์ 0 2962 6951-3", true);
//        receiptBuilder.addParagraph();
//        receiptBuilder.addText("อีเมล thiensurat@thiensurat.co.th", true);
//        receiptBuilder.addParagraph();
//        receiptBuilder.addBlankSpace(10);










        //  แก้ต่อ

        try {
            String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

            if (getOrganizationCode.equals("1")) {
                receiptBuilder.addImage(shortHeaderPrint());
            } else {
                if (contract.MODE == 1) {
                    receiptBuilder.addImage(shortHeaderPrintAlpine());

                    // alpine
                } else {
                    receiptBuilder.addImage(shortHeaderPrintTSRL());

                    // tsrl
                }
            }

        }
        catch (Exception ec){
            receiptBuilder.addImage(shortHeaderPrint());

        }






        //receiptBuilder.addImage(shortHeaderPrint());


        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(10);
        receiptBuilder.setAlign(Align.CENTER);
        receiptBuilder.addText(contract.MODE > 1 ? "ใบจอง" : "ใบจอง");
        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("วันที่ทำใบจอง", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHUtilities.dateFormat(contract.EFFDATE));

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("เลขที่อ้างอิง", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(contract.ContractReferenceNo != null ? contract.ContractReferenceNo : "", true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("เลขที่ใบจอง", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(contract.CONTNO), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText(contract.MODE > 1 ? "ผู้เช่าซื้อ" : "ผู้จอง", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("เลขที่บัตร", false);
        receiptBuilder.setAlign(Align.RIGHT);
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
                if(defaultAddress.TelMobile.equals(installAddress.TelMobile)){
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("ที่อยู่บัตร", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    String[] texts = getText(defaultAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < texts.length; ii++) {
                        cvAddr.drawText(texts[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result1 = Bitmap.createBitmap(RECEIPT_WIDTH, (int)yy, Config.ARGB_8888);
                    cvAddr = new Canvas(result1);
                    cvAddr.drawBitmap(imgAddress, RECEIPT_WIDTH / 2, 0, null);
                    imgAddress.recycle();
                    receiptBuilder.addImage(result1);
                    receiptBuilder.addParagraph();
//                    receiptBuilder.addText(AddressInfo.addr1(defaultAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    receiptBuilder.setAlign(Paint.Align.RIGHT);
//                    receiptBuilder.addText(AddressInfo.addr2(defaultAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    if (AddressInfo.addrCheckMultiLine(defaultAddress.Address()) == 3) {
//                        receiptBuilder.setAlign(Paint.Align.RIGHT);
//                        receiptBuilder.addText(AddressInfo.addr3(defaultAddress.Address()), true);
//                        receiptBuilder.addParagraph();
//                    }
                    yy = 19;
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    String[] textsInstall = getText(installAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < textsInstall.length; ii++) {
                        cvInstall.drawText(textsInstall[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result2 = Bitmap.createBitmap(RECEIPT_WIDTH, (int)yy, Config.ARGB_8888);
                    cvInstall = new Canvas(result2);
                    cvInstall.drawBitmap(imgInstall, RECEIPT_WIDTH / 2, 0, null);
                    imgInstall.recycle();
                    receiptBuilder.addImage(result2);
//                    receiptBuilder.addText(AddressInfo.addr1(installAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    receiptBuilder.setAlign(Paint.Align.RIGHT);
//                    receiptBuilder.addText(AddressInfo.addr2(installAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    if (AddressInfo.addrCheckMultiLine(installAddress.Address()) == 3) {
//                        receiptBuilder.setAlign(Paint.Align.RIGHT);
//                        receiptBuilder.addText(AddressInfo.addr3(installAddress.Address()), true);
//                        receiptBuilder.addParagraph();
//                    }
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    receiptBuilder.addText(installAddress.TelMobile, true);
                } else {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("ที่อยู่บัตร", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    String[] texts = getText(defaultAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < texts.length; ii++) {
                        cvAddr.drawText(texts[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result1 = Bitmap.createBitmap(RECEIPT_WIDTH, (int)yy, Config.ARGB_8888);
                    cvAddr = new Canvas(result1);
                    cvAddr.drawBitmap(imgAddress, RECEIPT_WIDTH / 2, 0, null);
                    imgAddress.recycle();
                    receiptBuilder.addImage(result1);
                    receiptBuilder.addParagraph();
//                    receiptBuilder.addText(AddressInfo.addr1(defaultAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    receiptBuilder.setAlign(Paint.Align.RIGHT);
//                    receiptBuilder.addText(AddressInfo.addr2(defaultAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    if (AddressInfo.addrCheckMultiLine(defaultAddress.Address()) == 3) {
//                        receiptBuilder.setAlign(Paint.Align.RIGHT);
//                        receiptBuilder.addText(AddressInfo.addr3(defaultAddress.Address()), true);
//                        receiptBuilder.addParagraph();
//                    }
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    receiptBuilder.addText(defaultAddress.TelMobile, true);
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    yy = 19;
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    String[] textsInstall = getText(installAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < textsInstall.length; ii++) {
                        cvInstall.drawText(textsInstall[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result2 = Bitmap.createBitmap(RECEIPT_WIDTH, (int)yy, Config.ARGB_8888);
                    cvInstall = new Canvas(result2);
                    cvInstall.drawBitmap(imgInstall, RECEIPT_WIDTH / 2, 0, null);
                    imgInstall.recycle();
                    receiptBuilder.addImage(result2);
//                    receiptBuilder.addText(AddressInfo.addr1(installAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    receiptBuilder.setAlign(Paint.Align.RIGHT);
//                    receiptBuilder.addText(AddressInfo.addr2(installAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    if (AddressInfo.addrCheckMultiLine(installAddress.Address()) == 3) {
//                        receiptBuilder.setAlign(Paint.Align.RIGHT);
//                        receiptBuilder.addText(AddressInfo.addr3(installAddress.Address()), true);
//                        receiptBuilder.addParagraph();
//                    }
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    receiptBuilder.addText(installAddress.TelMobile, true);
                }
                break;
            case "1":
                if(defaultAddress.TelMobile.equals(installAddress.TelMobile)){
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("ที่อยู่บัตร", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    String[] texts = getText(defaultAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < texts.length; ii++) {
                        cvAddr.drawText(texts[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result1 = Bitmap.createBitmap(RECEIPT_WIDTH, (int)yy, Config.ARGB_8888);
                    cvAddr = new Canvas(result1);
                    cvAddr.drawBitmap(imgAddress, RECEIPT_WIDTH / 2, 0, null);
                    imgAddress.recycle();
                    receiptBuilder.addImage(result1);
                    receiptBuilder.addParagraph();
//                    receiptBuilder.addText(AddressInfo.addr1(defaultAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    receiptBuilder.setAlign(Paint.Align.RIGHT);
//                    receiptBuilder.addText(AddressInfo.addr2(defaultAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    if (AddressInfo.addrCheckMultiLine(defaultAddress.Address()) == 3) {
//                        receiptBuilder.setAlign(Paint.Align.RIGHT);
//                        receiptBuilder.addText(AddressInfo.addr3(defaultAddress.Address()), true);
//                        receiptBuilder.addParagraph();
//                    }
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    yy = 19;
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    String[] textsInstall = getText(installAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < textsInstall.length; ii++) {
                        cvInstall.drawText(textsInstall[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result2 = Bitmap.createBitmap(RECEIPT_WIDTH, (int)yy, Config.ARGB_8888);
                    cvInstall = new Canvas(result2);
                    cvInstall.drawBitmap(imgInstall, RECEIPT_WIDTH / 2, 0, null);
                    imgInstall.recycle();
                    receiptBuilder.addImage(result2);
//                    receiptBuilder.addText(AddressInfo.addr1(installAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    receiptBuilder.setAlign(Paint.Align.RIGHT);
//                    receiptBuilder.addText(AddressInfo.addr2(installAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    if (AddressInfo.addrCheckMultiLine(installAddress.Address()) == 3) {
//                        receiptBuilder.setAlign(Paint.Align.RIGHT);
//                        receiptBuilder.addText(AddressInfo.addr3(installAddress.Address()), true);
//                        receiptBuilder.addParagraph();
//                    }
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    receiptBuilder.addText(installAddress.TelHome, true);
                } else {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("ที่อยู่บัตร", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    String[] texts = getText(defaultAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < texts.length; ii++) {
                        cvAddr.drawText(texts[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result1 = Bitmap.createBitmap(RECEIPT_WIDTH, (int)yy, Config.ARGB_8888);
                    cvAddr = new Canvas(result1);
                    cvAddr.drawBitmap(imgAddress, RECEIPT_WIDTH / 2, 0, null);
                    imgAddress.recycle();
                    receiptBuilder.addImage(result1);
                    receiptBuilder.addParagraph();
//                    receiptBuilder.addText(AddressInfo.addr1(defaultAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    receiptBuilder.setAlign(Paint.Align.RIGHT);
//                    receiptBuilder.addText(AddressInfo.addr2(defaultAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    if (AddressInfo.addrCheckMultiLine(defaultAddress.Address()) == 3) {
//                        receiptBuilder.setAlign(Paint.Align.RIGHT);
//                        receiptBuilder.addText(AddressInfo.addr3(defaultAddress.Address()), true);
//                        receiptBuilder.addParagraph();
//                    }
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    receiptBuilder.addText(defaultAddress.TelHome, true);
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("ที่ติดตั้ง", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    String[] textsInstall = getText(installAddress.Address(), pValue, RECEIPT_WIDTH / 2);
                    for (int ii = 0; ii < textsInstall.length; ii++) {
                        cvInstall.drawText(textsInstall[ii], RECEIPT_WIDTH / 2, yy, pValue);
                        yy += fontSize + lineSpace;
                    }

                    Bitmap result2 = Bitmap.createBitmap(RECEIPT_WIDTH, (int)yy, Config.ARGB_8888);
                    cvInstall = new Canvas(result2);
                    cvInstall.drawBitmap(imgInstall, RECEIPT_WIDTH / 2, 0, null);
                    imgInstall.recycle();
                    receiptBuilder.addImage(result2);
//                    receiptBuilder.addText(AddressInfo.addr1(installAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    receiptBuilder.setAlign(Paint.Align.RIGHT);
//                    receiptBuilder.addText(AddressInfo.addr2(installAddress.Address()), true);
//                    receiptBuilder.addParagraph();
//                    if (AddressInfo.addrCheckMultiLine(installAddress.Address()) == 3) {
//                        receiptBuilder.setAlign(Paint.Align.RIGHT);
//                        receiptBuilder.addText(AddressInfo.addr3(installAddress.Address()), true);
//                        receiptBuilder.addParagraph();
//                    }
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("เบอร์โทรติดต่อ", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    receiptBuilder.addText(installAddress.TelHome, true);
                }
                break;
        }

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("สินค้า", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(contract.ProductName), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("รุ่น", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(contract.MODEL), true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("เลขเครื่อง", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHUtilities.trim(contract.ProductSerialNumber), true);

        if (contract.TradeInDiscount > 0) {
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Align.LEFT);
            receiptBuilder.addText("ราคา", false);
            receiptBuilder.setAlign(Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(contract.SALES) + " บาท", true);

            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Align.LEFT);
            receiptBuilder.addText("ส่วนลดเครื่องแสดง", false);
            receiptBuilder.setAlign(Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(contract.TradeInDiscount) + " บาท", true);

            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Align.LEFT);
            receiptBuilder.addText("ราคาสุทธิ", false);
            receiptBuilder.setAlign(Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(contract.TotalPrice) + " บาท", true);
        } else {
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Align.LEFT);
            receiptBuilder.addText("ราคาขาย", false);
            receiptBuilder.setAlign(Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(contract.SALES) + " บาท", true);
        }

        if (contract.MODE > 1) {
            float sum = contract.PaymentAmount - contract.TradeInDiscount;



            try {
                String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                if (getOrganizationCode.equals("1")) {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("งวดที่ 1 ต้องชำระ", false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    receiptBuilder.addText(BHUtilities.numericFormat(sum) + " บาท", true);

                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText("งวดที่ 2 ถึงงวดที่" + BHUtilities.numericFormat(contract.MODE) + " ต้องชำระงวดละ", false);
                    receiptBuilder.setAlign(Align.RIGHT);                }
                else {
                    if (contract.MODE == 1) {


                        // alpine
                    } else {



                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Align.LEFT);
                        receiptBuilder.addText("มัดจำที่ต้องชำระ", false);
                        receiptBuilder.setAlign(Align.RIGHT);
                        receiptBuilder.addText(BHUtilities.numericFormat(sum) + " บาท", true);

                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Align.LEFT);
                        receiptBuilder.addText("งวดที่ 1 ถึงงวดที่" + BHUtilities.numericFormat(contract.MODE-1) + " ต้องชำระงวดละ", false);
                        receiptBuilder.setAlign(Align.RIGHT);

                        // tsrl
                    }
                }

            }
            catch (Exception ec){


            }










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
/*File file = new File(getAlbumStorageDir(contract.CONTNO), String.format("contract_%s.jpg", contract));
            contractSign = mergeSignature(signature, customeSign, file);
            final int width = signature.getWidth();
            final int height = signature.getHeight();
            final int[] argb = new int[width * height];
            signature.getPixels(argb, 0, width, 0, 0, width, height);
            signature.recycle();*/

            contractSign = customeSign;
        } else {
           // contractSign = signature;
        }







        if (contract.MODE == 1) {
            Log.e("aaa","1");





        if (customeSign != null) {
                receiptBuilder.setAlign(Align.RIGHT);

            receiptBuilder.addImage(customeSign);
            }




            yy += 25;

            String TSR = "";
            if(BHGeneral.isOpenDepartmentSignature&&BHPreference.hasDepartmentSignatureImage()){
                EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (saleLeader != null) {
                    TSR = "("+saleLeader.DepartmentHeadName+")";
                }
            }

            //   cv.drawText(String.format("%s", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), RECEIPT_WIDTH / 4, yy, pSignature);
            //    cv.drawText(String.format("%s", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), (RECEIPT_WIDTH / 4) * 3, yy, pSignature);

            cv.drawText(String.format(".............................พนักงานขาย", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), RECEIPT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format(".............................ผู้จอง", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), (RECEIPT_WIDTH / 4) * 3, yy, pSignature);


            // String[] texts1 = getText(TSR, pSignature, RECEIPT_WIDTH / 2);

            String salename = "(" + BHUtilities.trim(contract.SaleEmployeeName != null ? contract.SaleEmployeeName : "") + ")";
            //   String salecode = "รหัส " + BHUtilities.trim(contract.SaleCode);

            //   String saleteamname = "(" + BHUtilities.trim(contract.upperEmployeeName != null ? contract.upperEmployeeName : "") + ")";
            //  String saleteamcode = "" + BHUtilities.trim(contract.SaleTeamName != null ? contract.SaleTeamName : "");

            if (contract.SaleEmployeeName.equals(contract.upperEmployeeName)) {
                salename = "";
                //  salecode = "";
            }

            //cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), RECEIPT_WIDTH / 4, yy, pSignature);
            // cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), (RECEIPT_WIDTH / 4) * 3, yy, pSignature);

            //texts1 = getText(saleteamname, pSignature, RECEIPT_WIDTH / 2);
            // texts2 = getText(salename, pSignature, RECEIPT_WIDTH / 2);
            String[] texts1 = getText(salename, pSignature, RECEIPT_WIDTH / 2);


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

            Log.e("aaa","2");





                if (customeSign != null) {
                    receiptBuilder.setAlign(Align.RIGHT);

                    receiptBuilder.addImage(customeSign);
                }




                yy += 25;

                String TSR = "";
                if(BHGeneral.isOpenDepartmentSignature&&BHPreference.hasDepartmentSignatureImage()){
                    EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                    if (saleLeader != null) {
                        TSR = "("+saleLeader.DepartmentHeadName+")";
                    }
                }

                //   cv.drawText(String.format("%s", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), RECEIPT_WIDTH / 4, yy, pSignature);
                //    cv.drawText(String.format("%s", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), (RECEIPT_WIDTH / 4) * 3, yy, pSignature);

                //cv.drawText(String.format("%sพนักงานขาย", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), RECEIPT_WIDTH / 4, yy, pSignature);
               // cv.drawText(String.format("%sผู้จอง", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), (RECEIPT_WIDTH / 4) * 3, yy, pSignature);
            cv.drawText(String.format(".............................พนักงานขาย", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), RECEIPT_WIDTH / 4, yy, pSignature);
            cv.drawText(String.format(".............................ผู้จอง", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - 60)), (RECEIPT_WIDTH / 4) * 3, yy, pSignature);


                // String[] texts1 = getText(TSR, pSignature, RECEIPT_WIDTH / 2);

                String salename = "(" + BHUtilities.trim(contract.SaleEmployeeName != null ? contract.SaleEmployeeName : "") + ")";
                //   String salecode = "รหัส " + BHUtilities.trim(contract.SaleCode);

                //   String saleteamname = "(" + BHUtilities.trim(contract.upperEmployeeName != null ? contract.upperEmployeeName : "") + ")";
                //  String saleteamcode = "" + BHUtilities.trim(contract.SaleTeamName != null ? contract.SaleTeamName : "");

                if (contract.SaleEmployeeName.equals(contract.upperEmployeeName)) {
                    salename = "";
                    //  salecode = "";
                }

                //cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), RECEIPT_WIDTH / 4, yy, pSignature);
                // cv.drawText(String.format("%sพยาน", getSignatureUnderline(pSignature, (RECEIPT_WIDTH / 2) - (getWidth("พยาน", pSignature) + 50))), (RECEIPT_WIDTH / 4) * 3, yy, pSignature);

                //texts1 = getText(saleteamname, pSignature, RECEIPT_WIDTH / 2);
                // texts2 = getText(salename, pSignature, RECEIPT_WIDTH / 2);
                String[] texts1 = getText(salename, pSignature, RECEIPT_WIDTH / 2);


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















        }

        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText(String.format(" รหัส %s %s", BHUtilities.trim(contract.SaleCode), BHUtilities.trim(contract.SaleEmployeeName)), true);
        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText(String.format(" %s %s", BHUtilities.trim(contract.SaleTeamName), BHUtilities.trim(contract.upperEmployeeName)), true);
        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(10);






/*        receiptBuilder.setTextSize(22);
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("** หมายเหตุ: **", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("1. การนัดนี้ต้องไม่นานเกิน 7 วัน นับจากวันนัด", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("2. ถ้าเลยคืนวันที่นัดไปแล้ว ถือว่าการนัดเป็นโมฆะ พนักงานคนอื่นสามารถตกลงติดตั้งกับลูกค้าได้", true);
        receiptBuilder.addParagraph();
        receiptBuilder.setTextSize(20);

        receiptBuilder.addText("อื่นสามารถตกลงติดตั้งกับลูกค้าได้", true);
        receiptBuilder.addParagraph();
        receiptBuilder.setTextSize(20);

        receiptBuilder.addText("3. ใบนัดติดตั้งต้องยื่นต่อหัวหน้าฝ่ายขายในวันถัดมาของการทำงาน เพื่อเป็นหลักฐานยืนยันว่าได้นัดติดตั้งจริง", true);
        receiptBuilder.addParagraph();

        receiptBuilder.addText("เพื่อเป็นหลักฐานยืนยันว่าได้นัดติดตั้งจริง", true);
        receiptBuilder.addParagraph();


        receiptBuilder.addText("4. ให้ยึดที่อยู่ที่ติดตั้งเป็นหลัก (ในกรณีชื่อ ล/ค ไม่ตรง)", true);
        receiptBuilder.addParagraph();*/

        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(10);
        receiptBuilder.setAlign(Align.CENTER);
        receiptBuilder.addText("ข้อมูลเพิ่มเติม");

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("ผู้รับเงินงวด 1 ที่เหลือ", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHApplication.getInstance().getPrefManager().getPreferrence("FirstPeriodPayBy"));



        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("จำนวน", false);
        receiptBuilder.setAlign(Align.RIGHT);
        int number = Integer.parseInt(BHApplication.getInstance().getPrefManager().getPreferrence("FirstPeriodPayAmount"));
        String str = NumberFormat.getNumberInstance(Locale.US).format(number);
        receiptBuilder.addText(str+" บาท");


        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("ผู้ทำสัญญา", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHApplication.getInstance().getPrefManager().getPreferrence("ContractBy"));


        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("วันที่สะดวกนัดติดตั้ง", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHApplication.getInstance().getPrefManager().getPreferrence("DateInstall")+" น.");


        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("ข้อมูลน้ำดิบ", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHApplication.getInstance().getPrefManager().getPreferrence("WaterInfo"));

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("ปัญหาสภาพน้ำ", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHApplication.getInstance().getPrefManager().getPreferrence("WaterProblem").equals("อื่นๆ (ระบุ)") ? BHApplication.getInstance().getPrefManager().getPreferrence("WaterProblemMore"):BHApplication.getInstance().getPrefManager().getPreferrence("WaterProblem"));

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("ผู้จัดส่งสินค้า", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHApplication.getInstance().getPrefManager().getPreferrence("ShippingBy"));



        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText(BHApplication.getInstance().getPrefManager().getPreferrence("ShippingBy").equals("ทีมขายจัดส่งสินค้าเอง") ? "วันที่":"จัดส่งที่",false );
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHApplication.getInstance().getPrefManager().getPreferrence("ShippingBy").equals("ทีมขายจัดส่งสินค้าเอง") ? BHApplication.getInstance().getPrefManager().getPreferrence("ShippingDate") : BHApplication.getInstance().getPrefManager().getPreferrence("ShippingTo"));


        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("เบอร์สำรองลูกค้า", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHApplication.getInstance().getPrefManager().getPreferrence("TelnoCus"));

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("ข้อมูล อื่นๆ", false);



     //   receiptBuilder.setAlign(Align.RIGHT);
        String SS=BHApplication.getInstance().getPrefManager().getPreferrence("InstallDetails");
        //String SS="จากการตรวจในที่เกิดเหตุพบเก้าอี้ล้มกระจัดกระจาย และพบรอยเลือดหลายจุดทั่วศาลา ส่วนผู้ได้มี รายแรกคือ นายยิ่งพันธิ์ กันเกตุ กำนันตำบลดอนทราย อาการสาหัส รายที่ 2 นายนคร วันเพ็ญ ผู้สมัครเทศบาลตำบลดอนทราย รายที่ 3 นางสมถวิล ศรีรัตน์ รายที่ 4 นางมณเฑียร ใจธรรม ถูกส่งตัวไปรักษาที่โรงพยาบาลเจ็ดเสมียน และ โรงพยาบาลราชบุรี ส่วนรายที่ 5 คือ นางวราพร เนียมรักษา ผู้สมัครนายกเทศบาลตำบลดอนทราย อาการสาหัส ถูกส่งตัวไปรักษาที่โรงพยาบาลกรุงเทพเมืองราชบุรี แต่นางวราพร เนียมรักษา ทนพิษบาดแผลไม่ไหว";
     //   receiptBuilder.addText(SS);








        receiptBuilder.addBlankSpace(10);
        //receiptBuilder.setAlign(Align.RIGHT);
        String[] texts = getText(SS, pValue, RECEIPT_WIDTH / 1);
        for (int ii = 0; ii < texts.length; ii++) {
           // cvAddr.drawText(texts[ii], RECEIPT_WIDTH / 2, yy, pValue);
           // yy += fontSize + lineSpace;
            receiptBuilder.setTextSize(22);
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Align.LEFT);
            receiptBuilder.addText(texts[ii], false);
        }




        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(40);

        receiptBuilder.setTextSize(25);
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("ข้อกำหนดอุปกรณ์ติดตั้งมาตรฐาน : ", true);

        receiptBuilder.setTextSize(22);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("อุปกรณ์มาตรฐานการติดตั้ง บริษัทให้ในส่วนที่ใช้ตามจริงเท่านั้น ", true);


        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(20);

        receiptBuilder.setTextSize(25);
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("เงื่อนไขการให้บริการ :", true);

        receiptBuilder.setTextSize(22);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("1. กรณีโปรโมชั่นติดตั้งฟรี  แต่ลูกค้าไม่ใช้บริการติดตั้ง ", true);
       receiptBuilder.addParagraph();
        receiptBuilder.addText("ทางบริษัท ขอสงวนสิทธิ์ไม่ให้อุปกรณ์ตามมาตรฐาน", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("2. กรณีโปรโมชั่นติดตั้งฟรี  แต่ลูกใช้อุปกรณ์เกินมาตรฐาน ", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("ลูกค้าจะต้องชำระค่าอุปกรณ์ตามเงื่อนไขบริการ ติดตั้ง", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("ทางบริษัท ขอสงวนสิทธิ์ไม่ให้อุปกรณ์ตามมาตรฐาน", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("3.กรณีใช้อุปกรณ์เกินมาตรฐาน และไม่ได้ใช้อุปกรณ์ของบริษัท ", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("ลูกค้าต้องชำระค่าแรง 50% ของราคาอุปกรณ์ติดตั้ง", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("ทางบริษัท ขอสงวนสิทธิ์ไม่ให้อุปกรณ์ตามมาตรฐาน", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("4.การติดตั้งเครื่องกรองน้ำทุกระบบ ถ้าแรงดันน้ำเกิน 3.5 บาร์", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("จะต้องติดตั้งวาล์วลดแรงดัน ก่อนเข้าเครื่อง", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("5. บริษัทขอสงวนสิทธิ์ไม่ติดตั้งสินค้า หากหน้างาน", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("ไม่มีการเตรียมพื้นที่ให้พร้อม เช่น ระบบน้ำ, ระบบไฟ,", true);
        receiptBuilder.addParagraph();
        receiptBuilder.addText("พื้นที่จัดวางสินค้าไม่ได้ข้อกำหนด", true);



        receiptBuilder.addBlankSpace(60);








        Bitmap bmp = receiptBuilder.build();

        return scaleBitmap(bmp, bmp.getWidth(), bmp.getHeight());
    }





































































































































    public static Bitmap getNewReceiptImage(PaymentInfo paymentInfo, DebtorCustomerInfo debtorCustomerInfo, AddressInfo addressInfo) {
        ReceiptBuilder receiptBuilder = new ReceiptBuilder(576);
        receiptBuilder.setMargin(5, 0);
        receiptBuilder.setAlign(Align.CENTER);
        receiptBuilder.setColor(Color.BLACK);
        receiptBuilder.setTextSize(26);



        try {
            String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

            if (getOrganizationCode.equals("1")) {
                receiptBuilder.addImage(shortHeaderPrint());
            } else {
                if (paymentInfo.MODE == 1) {
                    receiptBuilder.addImage(shortHeaderPrintAlpine());

                    // alpine
                } else {
                    receiptBuilder.addImage(shortHeaderPrintTSRL());

                    // tsrl
                }
            }

        }
        catch (Exception ec){
            receiptBuilder.addImage(shortHeaderPrint());

        }


        //receiptBuilder.addImage(shortHeaderPrint());

        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(10);
        receiptBuilder.setTextSize(24);
        receiptBuilder.addText("ใบรับเงิน");
        receiptBuilder.addParagraph();


        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(10);

        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("วันที่รับเงิน", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(BHUtilities.dateFormat(paymentInfo.PayDate) + " เวลา " + BHUtilities.dateFormat(paymentInfo.PayDate, "HH:mm") + " น.", true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("เลขที่", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(paymentInfo.ReceiptCode, true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("เลขที่ใบจอง", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(paymentInfo.CONTNO, true);

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("ชื่อลูกค้า", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(debtorCustomerInfo.CustomerFullName(), true);

        if (paymentInfo.ManualVolumeNo != null && paymentInfo.ManualRunningNo > 0) {
            String ManualDocumentBookRunningNo = String.format("%s/%d", BHUtilities.trim(paymentInfo.ManualVolumeNo), paymentInfo.ManualRunningNo).replace(' ', '0');
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Align.LEFT);
            receiptBuilder.addText("อ้างอิงสัญญาเลขที่", false);
            receiptBuilder.setAlign(Align.RIGHT);
            receiptBuilder.addText(ManualDocumentBookRunningNo, true);
        }

        receiptBuilder.addParagraph();
        receiptBuilder.setAlign(Align.LEFT);
        receiptBuilder.addText("", false);
        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addText(paymentInfo.ProductName, true);

        if (paymentInfo.MODE == 1) {
            if (paymentInfo.BalancesOfPeriod == 0) {



                try {
                    String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                    if (getOrganizationCode.equals("1")) {
                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Align.LEFT);
                        receiptBuilder.addText(BHUtilities.trim(" งวด 1 (ชำระครบ)"), false);
                        receiptBuilder.setAlign(Align.RIGHT);
                        receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);

                        // tsr
                    } else {
                        if (paymentInfo.MODE == 1) {

                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(BHUtilities.trim("ชำระครบ"), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);


                            // alpine
                        } else {


                            // tsrl
                        }
                    }

                }
                catch (Exception ec){


                }







            } else {


                try {
                    String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                    if (getOrganizationCode.equals("1")) {

                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Align.LEFT);
                        receiptBuilder.addText(BHUtilities.trim(" งวด 1 (ชำระบางส่วน)"), false);
                        receiptBuilder.setAlign(Align.RIGHT);
                        receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);

                        // tsr
                    } else {
                        if (paymentInfo.MODE == 1) {


                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(BHUtilities.trim(" มัดจำ (ชำระบางส่วน)"), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);


                            // alpine
                        } else {


                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(BHUtilities.trim(" งวด 1 (ชำระบางส่วน)"), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);


                            // tsrl
                        }
                    }

                }
                catch (Exception ec){


                }






            }
            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Align.LEFT);
            receiptBuilder.addText("", false);
            receiptBuilder.setAlign(Align.RIGHT);
            receiptBuilder.addText("รวม " + BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);
        } else {
            if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {


/*                if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE) {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText(BHUtilities.trim(String.format(" งวด %d", paymentInfo.MODE)), false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท", true);
                } else {
                    receiptBuilder.addParagraph();
                    receiptBuilder.setAlign(Align.LEFT);
                    receiptBuilder.addText(BHUtilities.trim(String.format(" งวด %d-%d", paymentInfo.PaymentPeriodNumber, paymentInfo.MODE)), false);
                    receiptBuilder.setAlign(Align.RIGHT);
                    receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount) + " บาท", true);
                }
                */






                String txtPeriodAmountLabel = "";
                if (paymentInfo.BalancesOfPeriod == 0) {
                    if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE){
                        txtPeriodAmountLabel = "ชำระงวดที่ %d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "ชำระงวดที่ %d";
                    }





                    try {
                        String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                        if (getOrganizationCode.equals("1")) {
                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);
                            // tsr
                        } else {
                            if (paymentInfo.MODE == 1) {


                                // alpine
                            } else {
                                receiptBuilder.addParagraph();
                                receiptBuilder.setAlign(Align.LEFT);
                                receiptBuilder.addText(BHUtilities.trim(String.format("ชำระค่ามัดจำ")), false);
                                receiptBuilder.setAlign(Align.RIGHT);
                                receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);

                                // tsrl
                            }
                        }

                    }
                    catch (Exception ec){


                    }






                } else {


                    try {
                        String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                        if (getOrganizationCode.equals("1")) {
                            // tsr
                            txtPeriodAmountLabel = " งวด %d (ชำระบางส่วน)";
                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);

                        } else {
                            if (paymentInfo.MODE == 1) {

                                txtPeriodAmountLabel = " มัดจำ (ชำระบางส่วน)";
                                receiptBuilder.addParagraph();
                                receiptBuilder.setAlign(Align.LEFT);
                                receiptBuilder.addText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), false);
                                receiptBuilder.setAlign(Align.RIGHT);
                                receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);

                                // alpine
                            } else {

                                txtPeriodAmountLabel = " มัดจำ (ชำระบางส่วน)";
                                receiptBuilder.addParagraph();
                                receiptBuilder.setAlign(Align.LEFT);
                                receiptBuilder.addText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), false);
                                receiptBuilder.setAlign(Align.RIGHT);
                                receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);


                                // tsrl
                            }
                        }

                    }
                    catch (Exception ec){


                    }





                }




            } else{


                String txtPeriodAmountLabel = "";
                if (paymentInfo.BalancesOfPeriod == 0) {
                    if(paymentInfo.PaymentPeriodNumber == paymentInfo.MODE){
                        txtPeriodAmountLabel = "ชำระงวดที่ %d (ชำระครบ)";
                    } else {
                        txtPeriodAmountLabel = "ชำระงวดที่ %d";
                    }





                    try {
                        String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                        if (getOrganizationCode.equals("1")) {
                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);
                            // tsr
                        } else {
                            if (paymentInfo.MODE == 1) {


                                // alpine
                            } else {
                                receiptBuilder.addParagraph();
                                receiptBuilder.setAlign(Align.LEFT);
                                receiptBuilder.addText(BHUtilities.trim(String.format("ชำระค่ามัดจำ")), false);
                                receiptBuilder.setAlign(Align.RIGHT);
                                receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);

                                // tsrl
                            }
                        }

                    }
                    catch (Exception ec){


                    }






                } else {


                    try {
                        String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                        if (getOrganizationCode.equals("1")) {
                            // tsr
                            txtPeriodAmountLabel = " งวด %d (ชำระบางส่วน)";
                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);

                        } else {
                            if (paymentInfo.MODE == 1) {

                                txtPeriodAmountLabel = " มัดจำ (ชำระบางส่วน)";
                                receiptBuilder.addParagraph();
                                receiptBuilder.setAlign(Align.LEFT);
                                receiptBuilder.addText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), false);
                                receiptBuilder.setAlign(Align.RIGHT);
                                receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);

                                // alpine
                            } else {

                                txtPeriodAmountLabel = " มัดจำ (ชำระบางส่วน)";
                                receiptBuilder.addParagraph();
                                receiptBuilder.setAlign(Align.LEFT);
                                receiptBuilder.addText(BHUtilities.trim(String.format(txtPeriodAmountLabel, paymentInfo.PaymentPeriodNumber)), false);
                                receiptBuilder.setAlign(Align.RIGHT);
                                receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Amount) + " บาท", true);


                                // tsrl
                            }
                        }

                    }
                    catch (Exception ec){


                    }





                }
            }
        }

        if (paymentInfo.CloseAccountPaymentPeriodNumber == paymentInfo.PaymentPeriodNumber && paymentInfo.BalancesOfPeriod == 0) {
 /*           receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Align.LEFT);
            receiptBuilder.addText("ส่วนลดตัดสด", false);
            receiptBuilder.setAlign(Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.CloseAccountDiscountAmount) + " บาท", true);

            receiptBuilder.addParagraph();
            receiptBuilder.setAlign(Align.LEFT);
            receiptBuilder.addText("จำนวนที่ชำระ", false);
            receiptBuilder.setAlign(Align.RIGHT);
            receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.CloseAccountOutstandingAmount - paymentInfo.CloseAccountDiscountAmount) + " บาท", true);
        */

            /**ยอดเงินคงเหลือของงวดนั้น**/
            if (paymentInfo.BalancesOfPeriod != 0) {



                try {
                    String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                    if (getOrganizationCode.equals("1")) {
                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Align.LEFT);
                        receiptBuilder.addText(String.format(" คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), false);
                        receiptBuilder.setAlign(Align.RIGHT);
                        receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท", true);

                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Align.LEFT);
                        receiptBuilder.addText("วันนัดชำระ", false);
                        receiptBuilder.setAlign(Align.RIGHT);
                        receiptBuilder.addText(BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate), true);

                        // tsr
                    } else {
                        if (paymentInfo.MODE == 1) {

                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(String.format(" คงเหลือ "), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท", true);

                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText("วันนัดชำระ", false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText(BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate), true);


                            // alpine
                        } else {

                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(String.format(" คงเหลือ "), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท", true);

                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText("วันนัดชำระ", false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText(BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate), true);

                            // tsrl
                        }
                    }

                }
                catch (Exception ec){


                }










            }
            /**ยอดคงเหลือของงวดถัดไป**/
            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {
                } else {
                    if((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE){
                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Align.LEFT);
                        receiptBuilder.addText(String.format(" คงเหลือ งวดที่ %d เป็นเงิน", paymentInfo.MODE), false);
                        receiptBuilder.setAlign(Align.RIGHT);
                        receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", true);
                    }else {



                        try {
                            String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                            if (getOrganizationCode.equals("1")) {
                                receiptBuilder.addParagraph();
                                receiptBuilder.setAlign(Align.LEFT);
                                receiptBuilder.addText(String.format(" คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber+1 , paymentInfo.MODE), false);
                                receiptBuilder.setAlign(Align.RIGHT);
                                receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", true);

                                // tsr
                            } else {
                                if (paymentInfo.MODE == 1) {


                                    // alpine
                                } else {

                                    receiptBuilder.addParagraph();
                                    receiptBuilder.setAlign(Align.LEFT);
                                    receiptBuilder.addText(String.format(" คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber , paymentInfo.MODE-1), false);
                                    receiptBuilder.setAlign(Align.RIGHT);
                                    receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", true);


                                    // tsrl
                                }
                            }

                        }
                        catch (Exception ec){


                        }








                    }
                }
            }













        } else{

            /**ยอดเงินคงเหลือของงวดนั้น**/
            if (paymentInfo.BalancesOfPeriod != 0) {



                try {
                    String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                    if (getOrganizationCode.equals("1")) {
                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Align.LEFT);
                        receiptBuilder.addText(String.format(" คงเหลืองวดที่ %d", paymentInfo.PaymentPeriodNumber), false);
                        receiptBuilder.setAlign(Align.RIGHT);
                        receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท", true);

                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Align.LEFT);
                        receiptBuilder.addText("วันนัดชำระ", false);
                        receiptBuilder.setAlign(Align.RIGHT);
                        receiptBuilder.addText(BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate), true);

                        // tsr
                    } else {
                        if (paymentInfo.MODE == 1) {

                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(String.format(" คงเหลือ "), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท", true);

                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText("วันนัดชำระ", false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText(BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate), true);


                            // alpine
                        } else {

                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText(String.format(" คงเหลือ "), false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText("ราคา " + BHUtilities.numericFormat(paymentInfo.BalancesOfPeriod) + " บาท", true);

                            receiptBuilder.addParagraph();
                            receiptBuilder.setAlign(Align.LEFT);
                            receiptBuilder.addText("วันนัดชำระ", false);
                            receiptBuilder.setAlign(Align.RIGHT);
                            receiptBuilder.addText(BHUtilities.dateFormat(paymentInfo.PaymentAppointmentDate), true);

                            // tsrl
                        }
                    }

                }
                catch (Exception ec){


                }










            }
            /**ยอดคงเหลือของงวดถัดไป**/
            if (paymentInfo.Balances - paymentInfo.BalancesOfPeriod != 0) {
                if (paymentInfo.MODE == 1) {
                } else {
                    if((paymentInfo.PaymentPeriodNumber + 1) == paymentInfo.MODE){
                        receiptBuilder.addParagraph();
                        receiptBuilder.setAlign(Align.LEFT);
                        receiptBuilder.addText(String.format(" คงเหลือ งวดที่ %d เป็นเงิน", paymentInfo.MODE), false);
                        receiptBuilder.setAlign(Align.RIGHT);
                        receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", true);
                    }else {



                        try {
                            String getOrganizationCode= BHApplication.getInstance().getPrefManager().getPreferrence("getOrganizationCode");

                            if (getOrganizationCode.equals("1")) {
                                receiptBuilder.addParagraph();
                                receiptBuilder.setAlign(Align.LEFT);
                                receiptBuilder.addText(String.format(" คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber+1 , paymentInfo.MODE), false);
                                receiptBuilder.setAlign(Align.RIGHT);
                                receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", true);

                                // tsr
                            } else {
                                if (paymentInfo.MODE == 1) {


                                    // alpine
                                } else {

                                    receiptBuilder.addParagraph();
                                    receiptBuilder.setAlign(Align.LEFT);
                                    receiptBuilder.addText(String.format(" คงเหลือ งวดที่ %d ถึง %d เป็นเงิน", paymentInfo.PaymentPeriodNumber , paymentInfo.MODE-1), false);
                                    receiptBuilder.setAlign(Align.RIGHT);
                                    receiptBuilder.addText(BHUtilities.numericFormat(paymentInfo.Balances - paymentInfo.BalancesOfPeriod) + " บาท", true);


                                    // tsrl
                                }
                            }

                        }
                        catch (Exception ec){


                        }








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

        String sale = String.format("(%s) %s", paymentInfo.SaleEmployeeName != null ? paymentInfo.SaleEmployeeName : "", paymentInfo.CashCode);

        yy += 25;
        int Value = RECEIPT_WIDTH / 2;
        String[] texts = getText(sale, pSignature, RECEIPT_WIDTH);
        for (int ii = 0; ii < texts.length; ii++) {
            if (ii == 0) {
                cv.drawText(String.format("%sผู้รับเงิน", getSignatureUnderline(pSignature, (RECEIPT_WIDTH) - (getWidth("ผู้รับเงิน", pSignature) + (RECEIPT_WIDTH / 2)))), RECEIPT_WIDTH / 2, yy, pSignature);
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
        receiptBuilder.addBlankSpace(10);

        receiptBuilder.setAlign(Align.RIGHT);
        receiptBuilder.addImage(logoTelesale());
        receiptBuilder.addParagraph();
        receiptBuilder.addBlankSpace(10);

        return receiptBuilder.build();
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






}
