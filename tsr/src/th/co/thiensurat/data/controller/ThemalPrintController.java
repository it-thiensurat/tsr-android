package th.co.thiensurat.data.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.text.format.Time;

import com.datecs.api.printer.Printer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHBarcode;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.printer.PrintPic;
import th.co.thiensurat.R;
import th.co.thiensurat.data.info.ShortReceiptInfo;

/**
 * Created by bighead on 3/15/2016.
 */
public class ThemalPrintController {

//    BluetoothService mService = null;
    Printer mPrinter;
    String mPrinterAddress;
    public static int signatureWidthLimit = 250;
    public static int signatureHeightLimit = 100;

    private int printingWidth = 576;
    public static final int maxTextLength = 47;

    private static Dictionary getSpecialChar(){
        Dictionary SpecialChar = new Hashtable();

        SpecialChar.put("-24-19","140");//หยาดน้ำค้าง+เอก
        SpecialChar.put("-23-19","141");//หยาดน้ำค้าง+โทร
        SpecialChar.put("-22-19","142");//หยาดน้ำค้าง+ตรี
        SpecialChar.put("-21-19","143");//หยาดน้ำค้าง+จัตวา

        SpecialChar.put("-19-24","140");//หยาดน้ำค้าง+เอก
        SpecialChar.put("-19-23","141");//หยาดน้ำค้าง+โทร
        SpecialChar.put("-19-22","142");//หยาดน้ำค้าง+ตรี
        SpecialChar.put("-19-21","143");//หยาดน้ำค้าง+จัตวา

        SpecialChar.put("-47-24","146");//ไม้หันอากาศ+เอก
        SpecialChar.put("-47-23","147");//ไม้หันอากาศ+โทร
        SpecialChar.put("-47-22","148");//ไม้หันอากาศ+ตรี
        SpecialChar.put("-47-21","149");//ไม้หันอากาศ+จัตวา

        SpecialChar.put("-44-24","150");//อิ+เอก
        SpecialChar.put("-44-23","151");//อิ+โทร
        SpecialChar.put("-44-22","152");//อิ+ตรี
        SpecialChar.put("-44-21","153");//อิ+จัตวา
        SpecialChar.put("-44-20","154");//อิ+การันต์

        SpecialChar.put("-43-24","155");//อี+เอก
        SpecialChar.put("-43-23","156");//อี+โทร
        SpecialChar.put("-43-22","157");//อี+ตรี
        SpecialChar.put("-43-21","158");//อี+จัตวา

        SpecialChar.put("-42-24","219");//อึ+เอก
        SpecialChar.put("-42-23","220");//อึ+โทร
        SpecialChar.put("-42-22","221");//อึ+ตรี
        SpecialChar.put("-42-21","222");//อึ+จัตวา

        SpecialChar.put("-41-24","251");//อื+เอก
        SpecialChar.put("-41-23","252");//อื+โทร
        SpecialChar.put("-41-22","253");//อื+ตรี
        SpecialChar.put("-41-21","254");//อื+จัตวา

        return SpecialChar;
    }

    private static String[] topUpperChar = new String[]{"่","้","๊","๋","์","ํ"};
    private static String[] upperChar = new String[]{"ิ","ี","ึ","ื","ั","่","้","๊","๋","์","็","ํ"};
    private static String[] lowerChar = new String[]{"ุ","ู" };


    // zone Alphabet
    private ThemalPrintController() {


    }

    private void sendCommand(byte[] cmd) throws IOException {
        this.mPrinter.write(cmd);
    }

    public ThemalPrintController(com.datecs.api.printer.Printer Printer, String printerAddress)
    {
        this.mPrinter=Printer;
        this.mPrinterAddress = printerAddress;
    }

    public void selectPageMode() throws IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            mPrinter.selectPageMode();
        }
    }

    public void selectStandardMode() throws IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            mPrinter.selectStandardMode();
        }
    }

    public void setContractPageRegion() throws IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            //mPrinter.setPageRegion(0, 0, 570, 1700, Printer.PAGE_LEFT);
            mPrinter.setPageXY(0, 4);
        }
    }

    public void printContractPageTitle() throws  IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            mPrinter.drawPageRectangle(0, 0, 570, 64, Printer.FILL_INVERTED);
        }
    }

    public  void beginContractPage()  throws IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            mPrinter.setPageXY(0, 66);
            //mPrinter.drawPageFrame(0, 0, 570, 1700, Printer.FILL_BLACK, 1);
        }
    }

    public  void endContractPage()  throws IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            printPage();
        }
    }

    public void printPage() throws  IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            mPrinter.printPage();
        }
    }

    private int getPageDirection(String pagedirection) {
        int direction = 0;
        if(pagedirection.equals("alignLeft")) {
            direction = Printer.PAGE_LEFT;
        } else if(pagedirection.equals("alignRight")) {
            direction = Printer.PAGE_LEFT;
        } else if(pagedirection.equals("alignBottom")) {
            direction = Printer.PAGE_BOTTOM;
        } else if(pagedirection.equals("alignTop")) {
            direction = Printer.PAGE_TOP;
        }
        return direction;
    }

    public void setPageRegion(String pageRegion) throws IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            pageRegion = pageRegion.replace("setPageRegion(","").replace(")", "");
            String[] region = pageRegion.split(",");
            mPrinter.setPageRegion( Integer.parseInt(region[0].trim()),
                    Integer.parseInt(region[1].trim()),
                    Integer.parseInt(region[2].trim()),
                    Integer.parseInt(region[3].trim()),
                    getPageDirection(region[4].trim()));
            mPrinter.setPageXY(0, 4);
        }
    }

    public  void beginPage(String pageXY)  throws IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            pageXY = pageXY.replace("beginPage(","").replace(")", "");
            String[] XY = pageXY.split(",");
            mPrinter.setPageXY(Integer.parseInt(XY[0].trim()), Integer.parseInt(XY[1].trim()));
        }
    }

    public  void endPage()  throws IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            printPage();
        }
    }

    public void printHeader() throws  IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            Context mContext = BHApplication.getContext();
            final AssetManager assetManager = mContext.getAssets();
            final Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open("tsr_header.png"),
                    null, options);
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            final int[] argb = new int[width * height];
            bitmap.getPixels(argb, 0, width, 0, 0, width, height);
            bitmap.recycle();
            mPrinter.printCompressedImage(argb, width, height, Printer.ALIGN_LEFT, true);
        } else {
            sendThaiMessage("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)");
            sendThaiMessage("43/9 หมู่ 7 ซ.ชูชาติอนุสรณ์ 4 ต.บางตลาด");
            sendThaiMessage("อ.ปากเกร็ด จ.นนทบุรี 11120");
            sendThaiMessage("โทร. 0 2819 8888  แฟกซ์ 0 2962 6951-3");
            sendThaiMessage("อีเมล์. thiensurat@thiensurat.co.th");
        }
    }

    public void printShortHeader() throws  IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            Context mContext = BHApplication.getContext();
            final AssetManager assetManager = mContext.getAssets();
            final Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open("tsr_short_header.png"), null, options);
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            final int[] argb = new int[width * height];
            bitmap.getPixels(argb, 0, width, 0, 0, width, height);
            bitmap.recycle();
            mPrinter.printCompressedImage(argb, width, height, Printer.ALIGN_LEFT, true);
        } else {
            sendThaiMessage("           บริษัท เธียรสุรัตน์ จำกัด (มหาชน)");
            sendThaiMessage("        เลขประจำตัวผู้เสียภาษี 0107556000213");
            sendThaiMessage("                    โทร. 1210");
        }
    }

    public void printTitleBackground(String Region) throws  IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            Region = Region.replace("printTitleBackground(","").replace(")", "");
            String[] region = Region.split(",");
            mPrinter.drawPageRectangle(Integer.parseInt(region[0].trim()),
                    Integer.parseInt(region[1].trim()),
                    Integer.parseInt(region[2].trim()),
                    Integer.parseInt(region[3].trim()),
                    Printer.FILL_INVERTED);
        }
    }

    public void printFrame(String Region) throws  IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            Region = Region.replace("printFrame(","").replace(")", "");
            String[] region = Region.split(",");
            mPrinter.drawPageFrame(Integer.parseInt(region[0].trim()),
                    Integer.parseInt(region[1].trim()),
                    Integer.parseInt(region[2].trim()),
                    Integer.parseInt(region[3].trim()),
                    Printer.FILL_BLACK,
                    1);
        }
    }


    public void sendEnglishMessage(String message)  throws IOException{
        byte[] cmd = new byte[3];
        cmd[0] = 0x1b;
        cmd[1] = 0x33;
        cmd[2] = 0x1;
        sendCommand(cmd);

        byte[] send;
        try {
            send = message.getBytes("TIS-620");
        } catch (UnsupportedEncodingException var5) {
            send = message.getBytes();
        }
        sendCommand(send);
        commitPrint();
    }

    public void sendThaiMessage(String message)  throws IOException {
        message = message.replaceAll("ำ", "ํา");

        byte[] cmd = new byte[3];
        cmd[0] = 0x1b;
        cmd[1] = 0x33;
        cmd[2] = 0x1;
        sendCommand(cmd);

        Dictionary SpecialChar = getSpecialChar();

        String msg1 = "";
        String msg2 = "";
        String msg3 = "";
        String ch = "";
        Boolean _found = false;
        Boolean isLevel3 = false;
        int spaceCount = 0;

        // ค้นหาสระ และวรรณยุกต์ ด้านบน
        for(int i=0; i<message.length(); i++) {
            for(int j=0; j< upperChar.length; j++) {
                ch = message.substring(i,i+1);
                if(ch.equals(upperChar[j])) {
                    _found = true;
                    break;
                } else {
                    _found = false;
                }
            }
            String nextCh = "";
            if(_found) {
                //ตรวจสอบตัวอักษรถัดไปเป็นวรรณยุกต์หรือไม่
                if(i < message.length() - 1) {
                    for(int l=0; l<topUpperChar.length; l++) {
                        nextCh = message.substring(i+1,i+2);
                        if(nextCh.equals(topUpperChar[l])) {
                            _found = true;
                            break;
                        } else {
                            _found = false;
                        }
                    }
                } else {
                    _found = false;
                }

                if(_found) {
                    i++;
                    spaceCount--;
                    for (int sp = 0; sp < spaceCount; sp++) {
                        byte[] spaceByte = new byte[]{(byte) 32};
                        sendCommand(spaceByte);
                    }
                    spaceCount = 0;

                    String SpecialString = "";
                    try {
                        byte[] chbyte = ch.getBytes("TIS-620");
                        Byte chByte = chbyte[0];

                        byte[] nextChbyte = nextCh.getBytes("TIS-620");
                        Byte nextChByte = nextChbyte[0];

                        SpecialString = String.format("%d%d", chByte.intValue(), nextChByte.intValue());
                    } catch (UnsupportedEncodingException var5) {
                        byte[] chbyte = ch.getBytes();
                        Byte chByte = chbyte[0];

                        byte[] nextChbyte = nextCh.getBytes();
                        Byte nextChByte = nextChbyte[0];

                        SpecialString = String.format("%d%d", chByte.intValue(), nextChByte.intValue());
                    }

                    String spString =  SpecialChar.get(SpecialString).toString();
                    sendCommand(new byte[]{(byte) Integer.parseInt(spString)});

                } else {
                    spaceCount--;
                    for (int sp = 0; sp < spaceCount; sp++) {
                        byte[] spaceByte = new byte[]{(byte) 32};
                        sendCommand(spaceByte);
                    }
                    spaceCount = 0;
                    try {
                        sendCommand(ch.getBytes("TIS-620"));
                    } catch (UnsupportedEncodingException var5) {
                        sendCommand(ch.getBytes());
                    }
                }
            } else {
                for(int k=0; k<lowerChar.length; k++) {
                    ch = message.substring(i,i+1);
                    if(ch.equals(lowerChar[k])) {
                        _found = true;
                        break;
                    } else {
                        _found = false;
                    }
                }
                if(!_found)
                    spaceCount++;
            }
        }

        //  ค้นหาตัวอักษร
        for(int i=0; i<message.length(); i++) {
            _found = false;
            for(int j=0; j< upperChar.length; j++) {
                if(_found) break;
                ch = message.substring(i,i+1);
                if(ch.equals(upperChar[j])) {
                    _found = true;
                    break;
                } else {
                    for(int k=0; k<lowerChar.length; k++)
                    {
                        ch = message.substring(i,i+1);
                        if(ch.equals(lowerChar[k])) {
                            _found = true;
                            break;
                        } else {
                            _found = false;
                        }
                    }
                }
            }
            if(!_found) {
                msg2 = msg2 + message.substring(i,i+1);
            }
        }


        // ค้นหาสระด้านล่าง
        for(int i=0; i<message.length(); i++) {
            for(int j=0; j< lowerChar.length; j++) {
                ch = message.substring(i,i+1);
                if(ch.equals(lowerChar[j])) {
                    _found = true;
                    break;
                } else {
                    _found = false;
                }
            }
            if(_found) {
                msg3 = msg3.substring(0,msg3.length()-1);
                msg3 = msg3 + message.substring(i,i+1);
            } else {
                for(int k=0; k<upperChar.length; k++) {
                    ch = message.substring(i,i+1);
                    if(ch.equals(upperChar[k])) {
                        _found = true;
                        break;
                    } else {
                        _found = false;
                    }
                }
                if(!_found)
                    msg3 = msg3 + " ";
            }
        }


        byte[] send;


        try {
            send = msg1.getBytes("TIS-620");
        } catch (UnsupportedEncodingException var5) {
            send = msg1.getBytes();
        }
        sendCommand(send);
        commitPrint();

        try {
            send = msg2.getBytes("TIS-620");
        } catch (UnsupportedEncodingException var5) {
            send = msg2.getBytes();
        }
        sendCommand(send);
        commitPrint();

        if(!msg3.replace(" ","").isEmpty()) {
            try {
                send = msg3.getBytes("TIS-620");
            } catch (UnsupportedEncodingException var5) {
                send = msg3.getBytes();
            }
            sendCommand(send);
            commitPrint();
        }
    }

    public void commitPrint() throws IOException{
        //byte[] tail = new byte[]{(byte)10, (byte)13, (byte)0};
        //byte[] tail = new byte[]{(byte)10};
        //sendCommand(tail);
        mPrinter.feedPaper(10);
    }

     public void setFontBold() throws IOException {
        byte[] cmd = new byte[3];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;
        cmd[2] = 0x08;
        sendCommand(cmd);
    }

    public void setFontNormal () throws IOException {
        byte[] cmd = new byte[3];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;
        //cmd[2] &= 0xEF;
        cmd[2] = 0x00;
        sendCommand(cmd);
    }


    public void printSignature(String EmployeeID) throws IOException {
        printSignatureImage(GenerateSignature(EmployeeID));
    }

    public void printLogoTSRTelsale() throws IOException {

        Bitmap tempBitmap;
        try {
            tempBitmap = BitmapFactory.decodeResource(BHApplication.getContext().getResources(), R.drawable.logo_tsr_telsale);

            float defaultSize = 150;
            float width =  (float)tempBitmap.getWidth();
            float height = (float)tempBitmap.getHeight();
            float ratio = 0;

            if (width > defaultSize) {
                ratio = defaultSize / width;
            } else {
                ratio = width / defaultSize;
            }

            int newWidth =  Math.round((float)tempBitmap.getWidth() * ratio);
            int newHeight = Math.round((float) tempBitmap.getHeight() * ratio);
            Bitmap newBitmap = Bitmap.createScaledBitmap(tempBitmap, newWidth, newHeight, true);

            /*Base64
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);*/

            printLogoTSRTelsaleImage(newBitmap);


        }catch(Exception e){
            e.printStackTrace();
        }
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


    public Bitmap GenerateSignature(String EmployeeID){

        Bitmap tempSignature = null,signature;
        Bitmap img = null;
        Canvas cv;
        try {
            //YIM Check and resize Department signature image
            if (BHGeneral.isOpenDepartmentSignature&&BHPreference.hasDepartmentSignatureImage()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                tempSignature = BitmapFactory.decodeFile(BHPreference.departmentSignaturePath + BHPreference.departmentSignatureName, options);
            }else{
                Context mContext = BHApplication.getContext();
                tempSignature = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sample_signature);
            }

            if(tempSignature != null){
                signature = scaleBitmapByHeight(tempSignature);
                img = Bitmap.createBitmap(signature.getWidth(), signature.getHeight(), Bitmap.Config.ARGB_8888);
                img.setHasAlpha(true);
                cv = new Canvas(img);
                cv.drawBitmap(signature, 0, 2, null);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        /*if (img != null) {
            try {
                File file = new File("/mnt/sdcard/signature.png");
                boolean deleted = file.delete(); // if want to check delete success
                OutputStream stream = new FileOutputStream("/mnt/sdcard/signature.png");

                img.compress(Bitmap.CompressFormat.PNG, 100, stream);

                stream.flush();
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        return img;
    }

    //@SuppressLint("SdCardPath")
    private void printSignatureImage(Bitmap signatureBigmap) throws IOException {
        if (signatureBigmap != null) {
            if(mPrinterAddress.startsWith("00:01")) {

                /*** [START] :: Print ***/
                /*final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;

                //Context mContext = BHApplication.getContext();
                //final AssetManager assetManager = mContext.getAssets();
                final Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/signature.png", options);

                final int width = bitmap.getWidth();
                final int height = bitmap.getHeight();
                final int[] argb = new int[width * height];
                bitmap.getPixels(argb, 0, width, 0, 0, width, height);
                bitmap.recycle();
                mPrinter.printCompressedImage(argb, width, height, Printer.ALIGN_LEFT, true);*/

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;

                final int width = signatureBigmap.getWidth();
                final int height = signatureBigmap.getHeight();
                final int[] argb = new int[width * height];
                signatureBigmap.getPixels(argb, 0, width, 0, 0, width, height);
                signatureBigmap.recycle();
                mPrinter.printCompressedImage(argb, width, height, Printer.ALIGN_LEFT, true);
                /*** [END] :: Print ***/

            } else {

                /*** [START] :: Print ***/
                /*byte[] sendData = null;
                PrintPic pg = new PrintPic();
                pg.initCanvas(300);
                pg.initPaint();
                pg.drawImage(0, 0, "/mnt/sdcard/signature.png");

                sendData = pg.printDraw();
                sendCommand(sendData);
                commitPrint();*/

                byte[] sendData = null;
                PrintPic pg = new PrintPic();
                pg.initCanvas(300);
                pg.initPaint();
                pg.drawImage(0, 0, signatureBigmap);

                sendData = pg.printDraw();
                sendCommand(sendData);
                commitPrint();
                /*** [END] :: Print ***/
            }
        }
    }

    //@SuppressLint("SdCardPath")
    private void printLogoTSRTelsaleImage(Bitmap bitmap) throws IOException {
        if(mPrinterAddress.startsWith("00:01")) {

            /*final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            //Context mContext = BHApplication.getContext();
            //final AssetManager assetManager = mContext.getAssets();
            //final Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/signature.png", options);*/

            Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            newBitmap.setHasAlpha(true);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, 0, 0, null);

            final int width = newBitmap.getWidth();
            final int height = newBitmap.getHeight();
            final int[] argb = new int[width * height];
            newBitmap.getPixels(argb, 0, width, 0, 0, width, height);
            newBitmap .recycle();

            //mPrinter.reset();
            mPrinter.printCompressedImage(argb, width, height, Printer.ALIGN_RIGHT, true);
            //mPrinter.feedPaper(200);
            //mPrinter.flush();

        } else {

            /*** [START] :: Fixed - [BHPROJ-1036-7957] - รบกวน นำ Logo 1210 ออกก่อน พบปัญหาภาษาจีนเยอะมากครับ ***/
            /*Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_4444, true);

            byte[] sendData = null;
            PrintPic pg = new PrintPic();
            pg.initCanvas(printingWidth);
            pg.initPaint();

            //pg.drawImage(0, 0, "/mnt/sdcard/signature.png");
            try {
                float x = printingWidth - newBitmap.getWidth();
                float y = 0;
                pg.canvas.drawBitmap(newBitmap, x, y , null);
                if(pg.length < y + (float)newBitmap.getHeight()) {
                    pg.length = y + (float)newBitmap.getHeight();
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            sendData = pg.printDraw();
            sendCommand(sendData);
            commitPrint();*/
            /*** [END] :: Fixed - [BHPROJ-1036-7957] - รบกวน นำ Logo 1210 ออกก่อน พบปัญหาภาษาจีนเยอะมากครับ ***/
        }
    }

    public void printBankBarcode(String ref1, String ref2, String Amount) throws IOException {
        printPaymentBarcodeImage(GeneratePaymentBarcode(null, ref1, ref2, Amount));
    }

    //@SuppressLint("SdCardPath")
    private void printPaymentBarcodeImage(Bitmap barcodeBitmap) throws IOException {
        if (barcodeBitmap != null) {
            if(mPrinterAddress.startsWith("00:01")) {
                // print to Datecs Printer
                final int width = barcodeBitmap.getWidth();
                final int height = barcodeBitmap.getHeight();
                final int[] argb = new int[width * height];
                barcodeBitmap.getPixels(argb, 0, width, 0, 0, width, height);
                barcodeBitmap.recycle();
                mPrinter.printCompressedImage(argb, width, height, Printer.ALIGN_CENTER, true);
            } else {

                /*** [START] :: Print ***/
                // print to China mini thermal printer
                /*byte[] sendData = null;
                PrintPic pg = new PrintPic();
                pg.initCanvas(800);
                pg.initPaint();
                pg.drawImage(0, 0, "/mnt/sdcard/barcode_SendMoney.png");
                sendData = pg.printDraw();
                sendCommand(sendData);
                commitPrint();*/

                byte[] sendData = null;
                PrintPic pg = new PrintPic();
                pg.initCanvas(800);
                pg.initPaint();
                pg.drawImage(0, 0, barcodeBitmap);
                sendData = pg.printDraw();
                sendCommand(sendData);
                commitPrint();

                /*** [END] :: Print ***/
            }
        }
    }


    public Bitmap GeneratePaymentBarcode(String TaxID, String Ref1, String Ref2, String Amount) {
        Bitmap barcodeBitmap = null;
        String CarriageReturn = "11110111010";

        // Start Barcode =  START_B | CODE_C 01 07 55 60 00 21 30 CODE_A 0 <CR> CODE_C {Ref1}
        String StartBarcode = "1101001000010101111000101110111101100110110010011000100111010001101110111101011011001100110111001001101101100011101011110100111011001111011101010111011110";

        int CheckSumSummary = (104*1) + (92*1) + (99*2) + (1*3) + (7*4) + (55*5) + (60*6) + (0*7) + (21*8) + (30*9) + (101*10) + (16*11) + (77*12) + (99*13);
        int CurrentPosition = 14;
        // Add Space to start of barcode
        String barcodeString = String.format("0000000000%s" , StartBarcode);

        Map<String,String> CODE_C = new HashMap<String, String>();
        CODE_C.put("00", "11011001100");
        CODE_C.put("01", "11001101100");
        CODE_C.put("02", "11001100110");
        CODE_C.put("03", "10010011000");
        CODE_C.put("04", "10010001100");
        CODE_C.put("05", "10001001100");
        CODE_C.put("06", "10011001000");
        CODE_C.put("07", "10011000100");
        CODE_C.put("08", "10001100100");
        CODE_C.put("09", "11001001000");
        CODE_C.put("10", "11001000100");
        CODE_C.put("11", "11000100100");
        CODE_C.put("12", "10110011100");
        CODE_C.put("13", "10011011100");
        CODE_C.put("14", "10011001110");
        CODE_C.put("15", "10111001100");
        CODE_C.put("16", "10011101100");
        CODE_C.put("17", "10011100110");
        CODE_C.put("18", "11001110010");
        CODE_C.put("19", "11001011100");
        CODE_C.put("20", "11001001110");
        CODE_C.put("21", "11011100100");
        CODE_C.put("22", "11001110100");
        CODE_C.put("23", "11101101110");
        CODE_C.put("24", "11101001100");
        CODE_C.put("25", "11100101100");
        CODE_C.put("26", "11100100110");
        CODE_C.put("27", "11101100100");
        CODE_C.put("28", "11100110100");
        CODE_C.put("29", "11100110010");
        CODE_C.put("30", "11011011000");
        CODE_C.put("31", "11011000110");
        CODE_C.put("32", "11000110110");
        CODE_C.put("33", "10100011000");
        CODE_C.put("34", "10001011000");
        CODE_C.put("35", "10001000110");
        CODE_C.put("36", "10110001000");
        CODE_C.put("37", "10001101000");
        CODE_C.put("38", "10001100010");
        CODE_C.put("39", "11010001000");
        CODE_C.put("40", "11000101000");
        CODE_C.put("41", "11000100010");
        CODE_C.put("42", "10110111000");
        CODE_C.put("43", "10110001110");
        CODE_C.put("44", "10001101110");
        CODE_C.put("45", "10111011000");
        CODE_C.put("46", "10111000110");
        CODE_C.put("47", "10001110110");
        CODE_C.put("48", "11101110110");
        CODE_C.put("49", "11010001110");
        CODE_C.put("50", "11000101110");
        CODE_C.put("51", "11011101000");
        CODE_C.put("52", "11011100010");
        CODE_C.put("53", "11011101110");
        CODE_C.put("54", "11101011000");
        CODE_C.put("55", "11101000110");
        CODE_C.put("56", "11100010110");
        CODE_C.put("57", "11101101000");
        CODE_C.put("58", "11101100010");
        CODE_C.put("59", "11100011010");
        CODE_C.put("60", "11101111010");
        CODE_C.put("61", "11001000010");
        CODE_C.put("62", "11110001010");
        CODE_C.put("63", "10100110000");
        CODE_C.put("64", "10100001100");
        CODE_C.put("65", "10010110000");
        CODE_C.put("66", "10010000110");
        CODE_C.put("67", "10000101100");
        CODE_C.put("68", "10000100110");
        CODE_C.put("69", "10110010000");
        CODE_C.put("70", "10110000100");
        CODE_C.put("71", "10011010000");
        CODE_C.put("72", "10011000010");
        CODE_C.put("73", "10000110100");
        CODE_C.put("74", "10000110010");
        CODE_C.put("75", "11000010010");
        CODE_C.put("76", "11001010000");
        CODE_C.put("77", "11110111010");
        CODE_C.put("78", "11000010100");
        CODE_C.put("79", "10001111010");
        CODE_C.put("80", "10100111100");
        CODE_C.put("81", "10010111100");
        CODE_C.put("82", "10010011110");
        CODE_C.put("83", "10111100100");
        CODE_C.put("84", "10011110100");
        CODE_C.put("85", "10011110010");
        CODE_C.put("86", "11110100100");
        CODE_C.put("87", "11110010100");
        CODE_C.put("88", "11110010010");
        CODE_C.put("89", "11011011110");
        CODE_C.put("90", "11011110110");
        CODE_C.put("91", "11110110110");
        CODE_C.put("92", "10101111000");
        CODE_C.put("93", "10100011110");
        CODE_C.put("94", "10001011110");
        CODE_C.put("95", "10111101000");
        CODE_C.put("96", "10111100010");
        CODE_C.put("97", "11110101000");
        CODE_C.put("98", "11110100010");
        CODE_C.put("99", "10111011110");

        Map<String,String> CODE_A = new HashMap<String, String>();


        CODE_A.put("0","11011001100");
        CODE_A.put("1","11001101100");
        CODE_A.put("2","11001100110");
        CODE_A.put("3","10010011000");
        CODE_A.put("4","10010001100");
        CODE_A.put("5","10001001100");
        CODE_A.put("6","10011001000");
        CODE_A.put("7","10011000100");
        CODE_A.put("8","10001100100");
        CODE_A.put("9","11001001000");
        CODE_A.put("10","11001000100");
        CODE_A.put("11","11000100100");
        CODE_A.put("12","10110011100");
        CODE_A.put("13","10011011100");
        CODE_A.put("14","10011001110");
        CODE_A.put("15","10111001100");
        CODE_A.put("16","10011101100");
        CODE_A.put("17","10011100110");
        CODE_A.put("18","11001110010");
        CODE_A.put("19","11001011100");
        CODE_A.put("20","11001001110");
        CODE_A.put("21","11011100100");
        CODE_A.put("22","11001110100");
        CODE_A.put("23","11101101110");
        CODE_A.put("24","11101001100");
        CODE_A.put("25","11100101100");
        CODE_A.put("26","11100100110");
        CODE_A.put("27","11101100100");
        CODE_A.put("28","11100110100");
        CODE_A.put("29","11100110010");
        CODE_A.put("30","11011011000");
        CODE_A.put("31","11011000110");
        CODE_A.put("32","11000110110");
        CODE_A.put("33","10100011000");
        CODE_A.put("34","10001011000");
        CODE_A.put("35","10001000110");
        CODE_A.put("36","10110001000");
        CODE_A.put("37","10001101000");
        CODE_A.put("38","10001100010");
        CODE_A.put("39","11010001000");
        CODE_A.put("40","11000101000");
        CODE_A.put("41","11000100010");
        CODE_A.put("42","10110111000");
        CODE_A.put("43","10110001110");
        CODE_A.put("44","10001101110");
        CODE_A.put("45","10111011000");
        CODE_A.put("46","10111000110");
        CODE_A.put("47","10001110110");
        CODE_A.put("48","11101110110");
        CODE_A.put("49","11010001110");
        CODE_A.put("50","11000101110");
        CODE_A.put("51","11011101000");
        CODE_A.put("52","11011100010");
        CODE_A.put("53","11011101110");
        CODE_A.put("54","11101011000");
        CODE_A.put("55","11101000110");
        CODE_A.put("56","11100010110");
        CODE_A.put("57","11101101000");
        CODE_A.put("58","11101100010");
        CODE_A.put("59","11100011010");
        CODE_A.put("60","11101111010");
        CODE_A.put("61","11001000010");
        CODE_A.put("62","11110001010");
        CODE_A.put("63","10100110000");
        CODE_A.put("64","10100001100");
        CODE_A.put("65", "10010110000");
        CODE_A.put("66", "10010000110");
        CODE_A.put("67", "10000101100");
        CODE_A.put("68", "10000100110");
        CODE_A.put("69", "10110010000");
        CODE_A.put("70", "10110000100");
        CODE_A.put("71", "10011010000");
        CODE_A.put("72", "10011000010");
        CODE_A.put("73", "10000110100");
        CODE_A.put("74", "10000110010");
        CODE_A.put("75", "11000010010");
        CODE_A.put("76", "11001010000");
        CODE_A.put("77", "11110111010");
        CODE_A.put("78", "11000010100");
        CODE_A.put("79", "10001111010");
        CODE_A.put("80", "10100111100");
        CODE_A.put("81", "10010111100");
        CODE_A.put("82", "10010011110");
        CODE_A.put("83", "10111100100");
        CODE_A.put("84", "10011110100");
        CODE_A.put("85", "10011110010");
        CODE_A.put("86", "11110100100");
        CODE_A.put("87", "11110010100");
        CODE_A.put("88", "11110010010");
        CODE_A.put("89", "11011011110");
        CODE_A.put("90", "11011110110");
        CODE_A.put("91", "11110110110");
        CODE_A.put("92", "10101111000");
        CODE_A.put("93", "10100011110");
        CODE_A.put("94", "10001011110");
        CODE_A.put("95", "10111101000" );
        CODE_A.put("96", "10111100010" );
        CODE_A.put("97", "11110101000" );
        CODE_A.put("98", "11110100010" );
        CODE_A.put("99",  "10111011110" );
        CODE_A.put("100", "10111101110" );
        CODE_A.put("101", "11101011110" );
        CODE_A.put("102", "11110101110");
        CODE_A.put("103", "11010000100" );
        CODE_A.put("104", "11010010000" );
        CODE_A.put("105", "11010011100" );
        CODE_A.put("",  "11000111010" );

        // Add ref1

        for(int i=0; i<Ref1.length(); i+=2) {
            String key = String.format("%s%s", Ref1.charAt(i), Ref1.charAt(i+1));
            barcodeString = String.format("%s%s", barcodeString, CODE_C.get(key));
            CheckSumSummary = CheckSumSummary + (Integer.parseInt(key) *  CurrentPosition);
            CurrentPosition++;
        }

        // Add Code A
        barcodeString = String.format("%s%s", barcodeString, "11101011110");
        CheckSumSummary = CheckSumSummary + (101 *  CurrentPosition);
        CurrentPosition++;

        // Add Carriage Return
        barcodeString = String.format("%s%s", barcodeString, CarriageReturn);
        CheckSumSummary = CheckSumSummary + (77 *  CurrentPosition);
        CurrentPosition++;

        if(Ref2 != null && !Ref2.isEmpty()) {
            // Add Code C
            barcodeString = String.format("%s%s", barcodeString, "10111011110");
            CheckSumSummary = CheckSumSummary + (99 *  CurrentPosition);
            CurrentPosition++;

            // Add Ref2
            for(int i=0; i<Ref2.length(); i+=2) {
                String key = String.format("%s%s", Ref2.charAt(i), Ref2.charAt(i+1));
                barcodeString = String.format("%s%s", barcodeString, CODE_C.get(key));
                CheckSumSummary = CheckSumSummary + (Integer.parseInt(key) *  CurrentPosition);
                CurrentPosition++;
            }
            // Add Code A
            barcodeString = String.format("%s%s", barcodeString, "11101011110");
            CheckSumSummary = CheckSumSummary + (101 *  CurrentPosition);
            CurrentPosition++;
        }

        // Add Carriage Return
        barcodeString = String.format("%s%s", barcodeString, CarriageReturn);
        CheckSumSummary = CheckSumSummary + (77 *  CurrentPosition);
        CurrentPosition++;

        // Add Money Amount 10 digit (100000) = 1,000.00
        if(Amount != "0") {
            // Add Code C
            barcodeString = String.format("%s%s", barcodeString, "10111011110");
            CheckSumSummary = CheckSumSummary + (99 * CurrentPosition);
            CurrentPosition++;

            //50000
            int hasOneDigit = Amount.length() % 2;
            for(int i=0; i < Amount.length() - hasOneDigit; i+=2) {
                String key = String.format("%s%s", Amount.charAt(i), Amount.charAt(i+1));
                barcodeString = String.format("%s%s", barcodeString, CODE_C.get(key));
                CheckSumSummary = CheckSumSummary + (Integer.parseInt(key) *  CurrentPosition);
                CurrentPosition++;
            }
            if(hasOneDigit == 1) {
                // Add Code A
                barcodeString = String.format("%s%s", barcodeString, "11101011110");
                CheckSumSummary = CheckSumSummary + (101 *  CurrentPosition);
                CurrentPosition++;

                String key = String.format("%s", Amount.charAt(Amount.length()-1));
                // Shift to value = 16 for charactor "0"
                key = String.format("%d",Integer.parseInt(key) + 16);
                barcodeString = String.format("%s%s", barcodeString, CODE_A.get(key));
                CheckSumSummary = CheckSumSummary + (Integer.parseInt(key) *  CurrentPosition);
                CurrentPosition++;

            }

        } else {
            barcodeString = String.format("%s%s", barcodeString, "10011101100");
            CheckSumSummary = CheckSumSummary + (16 * CurrentPosition);
            CurrentPosition++;
        }

        int CheckSum = CheckSumSummary % 103;
        String CheckSumString = CODE_A.get(String.format("%d",CheckSum));
        barcodeString = String.format("%s%s", barcodeString, CheckSumString);

        String StopBarcode = "1100011101011";
        barcodeString = String.format("%s%s", barcodeString, StopBarcode);
        barcodeString = String.format("%s%s", barcodeString, "0000000000");

        int barWidth = 1;
        barcodeBitmap = Bitmap.createBitmap(barcodeString.length()*barWidth, 50,  Bitmap.Config.ARGB_8888);

        int start = 0;
        for(int i=1; i<=barcodeString.length(); i++) {
            for(int w=0; w<barWidth; w++) {
                for(int j=0; j<50; j++){
                    barcodeBitmap.setPixel( start, j, barcodeString.charAt(i-1) == '1' ? Color.BLACK : Color.WHITE);
                }
                start++;
            }
        }

        /*if (barcodeBitmap != null) {
            try {
                File file = new File("/mnt/sdcard/barcode_SendMoney.png");
                boolean deleted = file.delete();
                OutputStream stream = null;
                stream = new FileOutputStream("/mnt/sdcard/barcode_SendMoney.png");
                barcodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        return barcodeBitmap;
    }

    public void printBarCode128(String text) throws IOException {
        if(mPrinterAddress.startsWith("00:01")) {
            Bitmap bitmap = BHBarcode.generateCode128(text, 580, 150);

            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            final int[] argb = new int[width * height];
            bitmap.getPixels(argb, 0, width, 0, 0, width, height);
            bitmap.recycle();
            mPrinter.printCompressedImage(argb, width, height, Printer.ALIGN_CENTER, true);
        } else {
            setBarCodeWidth();
            setAligns(1);

            byte[] cmd = new byte[4 + text.length()];
            cmd[0] = 0x1D; //GS
            cmd[1] = 0x6B; //k
            cmd[2] = 0x49; //m
            cmd[3] = (byte) text.length(); //n
            int i = 4;

            byte[] bytes;
            try {
                bytes = text.getBytes("UTF-8");
            } catch (UnsupportedEncodingException var5) {
                bytes = text.getBytes();
            }

            for (byte b : bytes){
                cmd[i] = b;
                i++;
            }

            sendCommand(cmd);

            //sendThaiMessage(text);

            setAligns(0);
        }
    }

    public void setBarCodeWidth()  throws IOException {
        byte[] cmd = new byte[3];
        cmd[0] = 0x1D; //GS
        cmd[1] = 0x77; //w
        cmd[2] = 0x2; //n
        sendCommand(cmd);
    }

    public void setAligns(int x)  throws IOException {
        /*0,48 Left justification
        1, 49 Centering
        2, 50 Right justification*/

        byte[] cmd = new byte[3];
        cmd[0] = 0x1B; //ESC
        cmd[1] = 0x61; //a
        cmd[2] = (byte) x; //n
        sendCommand(cmd);
    }


    public static String getAlphabetOnly(String message)
    {
        //  ค้นหาตัวอักษร
        String result="";
        boolean _found = false;
        String ch = "";
        for(int i=0; i<message.length(); i++) {
            _found = false;
            for(int j=0; j< upperChar.length; j++) {
                if(_found) break;
                ch = message.substring(i,i+1);
                if(ch.equals(upperChar[j])) {
                    _found = true;
                    break;
                } else {
                    for(int k=0; k<lowerChar.length; k++)
                    {
                        ch = message.substring(i,i+1);
                        if(ch.equals(lowerChar[k])) {
                            _found = true;
                            break;
                        } else {
                            _found = false;
                        }
                    }
                }
            }
            if(!_found) {
                result = result + message.substring(i,i+1);
            }
        }

        return result;
    }


    //zone storage .txt .png

    public static void SaveImage(Context ctx,ShortReceiptInfo[]shortReceiptInfos)  {
        delTempPrint(ctx);     //clear รูปเก่า
        if (shortReceiptInfos.length > 0) {

            String tmpDir = ctx.getResources().getString(R.string.folder_picture_tsr);
            File dir = new File(Environment.getExternalStorageDirectory() + "/" + tmpDir);
            dir.mkdir();        //create folder
            Time now = new Time();
            now.setToNow();
            try {
                OutputStream stream = null;

                for (int i=0;i< shortReceiptInfos.length;i++) {

                    NumberFormat numberFormat = new DecimalFormat("##00");
                    String format = numberFormat.format(i);                 // 00

                    if (shortReceiptInfos[i].receiptHeader!=null) {




                        stream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/header_" + format + ".png");
                        shortReceiptInfos[i].receiptHeader.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        if (shortReceiptInfos[i].receiptDetail != null) {
                            stream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/detail_" + format + ".png");
                            shortReceiptInfos[i].receiptDetail.compress(Bitmap.CompressFormat.PNG, 100, stream);

                            //write txt
                            File myFile = new File(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/receiptDetail1_" + format + ".txt");
                            myFile.createNewFile();
                            FileOutputStream fOut = new FileOutputStream(myFile);
                            OutputStreamWriter myOutWriter =
                                    new OutputStreamWriter(fOut);
                            myOutWriter.append(shortReceiptInfos[i].receiptDetail1);
                            myOutWriter.close();
                            fOut.close();

                            myFile = new File(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/receiptDetail2_" + format + ".txt");
                            myFile.createNewFile();
                            fOut = new FileOutputStream(myFile);
                            myOutWriter =
                                    new OutputStreamWriter(fOut);
                            myOutWriter.append(shortReceiptInfos[i].receiptDetail2);
                            myOutWriter.close();
                            fOut.close();


                        }
                        if (shortReceiptInfos[i].receiptTailer != null) {
                            stream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/tailer_" + format + ".png");
                            shortReceiptInfos[i].receiptTailer.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        }

                        stream.close();
                    }
                    if (shortReceiptInfos[i].listTxt.size() > 0)
                    {
                        //write txt
                        File myFile = new File(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/listTxt_" + format + ".txt");
                        myFile.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter =
                                new OutputStreamWriter(fOut);
                        String writeTxt = "";
                        for (int j=0;j<shortReceiptInfos[i].listTxt.size();j++)
                        {
                            writeTxt+=shortReceiptInfos[i].listTxt.get(j)+"\n";
                        }
                        myOutWriter.append(writeTxt);
                        myOutWriter.close();
                        fOut.close();
                    }
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }


    }
    public static void SaveImage(Context ctx ,Bitmap []finalBitmap)  {
        delTempPrint(ctx); //clear รูปเก่า
        Bitmap []bitmap = finalBitmap;
        if (bitmap.length > 0) {
            Bitmap resultBitmap = bitmap[0];
            for (int i = 0; i < bitmap.length - 1; i++) {
                resultBitmap = combineImages( resultBitmap, bitmap[i+1]);
            }

            String tmpDir = ctx.getResources().getString(R.string.folder_picture_tsr);
            File dir = new File(Environment.getExternalStorageDirectory() + "/" + tmpDir);
            dir.mkdir();        //create folder
            Time now = new Time();
            now.setToNow();
            try {
                OutputStream stream = null;
                stream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/header_00.png");

                resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }


    }
    public static Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        if(c.getHeight() > s.getHeight()) {
            width = c.getWidth();
            height = c.getHeight()+ s.getHeight();
        } else {
            width = s.getWidth();
            height = c.getHeight()+ c.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, 0f, (c.getHeight()), null);


        return cs;
    }
    public static void delTempPrint(Context ctx) {
        String tmpDir= ctx.getResources().getString(R.string.folder_picture_tsr);
        File dir = new File(Environment.getExternalStorageDirectory()+"/"+tmpDir);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {

                new File(dir, children[i]).delete();

            }
        }
    }

    public static String readText(String path) {
        File file = new File(path);

        String line="";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));


            String resultLine="";
            while ((resultLine = br.readLine()) != null) {
                // do something with the line you just read, e.g.
                line+= resultLine+"\n";
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (line);

    }

    public static List<String> readTextList(String path) {
        File file = new File(path);

        List<String> line=new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));


            String resultLine="";
            while ((resultLine = br.readLine()) != null) {
                // do something with the line you just read, e.g.
                line.add(resultLine);
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (line);

    }

    public static void addLongText(List<String> stringList,String msg)
    {
        //ถ้า มี String ที่ยาว กว่า 48 ตัว ฟังชั่นนี้จัดตัดขึ้นบรรทัดใหม่ให้
        int maxLenght = 48;
        String resultLength = getAlphabetOnly(msg);


        String resultMsg = msg;
        int checkLoop = (resultMsg.length()/maxLenght);
        for (int i=0;i<=checkLoop;i++)
        {

            if (resultMsg.equals(""))
            {
                break;
            }
            String _step = (resultMsg.length()<maxLenght) ? resultMsg :  resultMsg.substring(0, maxLenght);

            String _stepAlphabet = getAlphabetOnly(_step);
            if (_stepAlphabet.length()<maxLenght && !resultMsg.equals(_step))
            {
                int _defference = maxLenght - _stepAlphabet.length();       //จำนวนตัวอักษร ที่ต้องเพิ่ม

//                String _step2 = (resultMsg.length()<(_step.length()+_defference)) ? resultMsg.substring(_step.length(),resultMsg.length()) : resultMsg.substring(_step.length(),(_step.length()+_defference));
//
//                String _stepAlphabet2 = getAlphabetOnly(_step+_step2);
//                if (_stepAlphabet2.length()<maxLenght)
//                {
//                    int _defference2 = maxLenght - _stepAlphabet.length();       //จำนวนตัวอักษร ที่ต้องเพิ่ม
//                }

                while (getAlphabetOnly(_step).length()!=maxLenght)
                {
                    String _step2 = (resultMsg.length()<(_step.length()+_defference)) ? resultMsg.substring(_step.length(),resultMsg.length()) : resultMsg.substring(_step.length(),(_step.length()+_defference));
                    String _stepAlphabet2 = getAlphabetOnly(_step+_step2);

                    for (int j=1;j<=3;j++)
                    {
                        String _step2Growing = (resultMsg.length()<(_step.length()+_defference+j)) ? resultMsg.substring(_step.length(),resultMsg.length()) : resultMsg.substring(_step.length(),(_step.length()+_defference+j));
                        String _stepAlphabet2Growing = getAlphabetOnly(_step+_step2Growing);
                        if (_stepAlphabet2.length()==_stepAlphabet2Growing.length())
                        {
                            _step2 = _step2Growing;
                        }
                    }


                    if (_step2.equals(""))
                    {
                        break;
                    }

                    if (_stepAlphabet2.length()>48)
                    {
                        _defference--;
                    }
                    else if (_stepAlphabet2.length()<48)
                    {
                        _defference = maxLenght - _stepAlphabet2.length();       //จำนวนตัวอักษร ที่ต้องเพิ่ม
                        _step += _step2;
                    }
                    else
                    {
                        _step += _step2;
                        break;
                    }

                }



            }


            int memoryIncrement = 0;
//            if (getAlphabetOnly(_step).length()<maxLenght && msg.length() > maxLenght)
//            {
//                int calIsEmptyChar= maxLenght - getAlphabetOnly(_step).length();
//                for (int j=0;j<=calIsEmptyChar;j++)
//                {
//                    String _step2 = (resultMsg.length()<calIsEmptyChar) ? resultMsg.substring(_step.length(),resultMsg.length()) :  resultMsg.substring(_step.length(), calIsEmptyChar);
//                    if ((_step2.length()-getAlphabetOnly(_step2).length())!=0)
//                        calIsEmptyChar = calIsEmptyChar-(_step2.length()-getAlphabetOnly(_step2).length());
//                    _step+= _step2;
//                    memoryIncrement=calIsEmptyChar;
//                }
//            }

//            int _alphabetStepLength = getAlphabetOnly(_step).length();
//            int memoryIncrement = 0;
//            if (_alphabetStepLength<maxLenght )
//            {
//                int initValue = maxLenght-_alphabetStepLength;
//                for (int j=0;j<=initValue;j++)
//                {
//                    if (resultMsg.length() > _step.length()) {
//                        String resultIncrement = (resultLength.length() < maxLenght) ? resultMsg.substring(_step.length(), resultMsg.length() + j) : resultMsg.substring(_step.length(), maxLenght + j);
//                        int checkVoWel = resultIncrement.length() - getAlphabetOnly(resultIncrement).length();
//                        if (checkVoWel > 0)
//                            initValue--;
//                        _step += resultIncrement;
//                    }
//                    memoryIncrement = initValue;
//                }
//            }


            //resultMsg = (resultMsg.length()<maxLenght+memoryIncrement) ? resultMsg.substring(resultMsg.length()) : resultMsg.substring(maxLenght+memoryIncrement);

            resultMsg = resultMsg.substring(_step.length());
            resultLength = getAlphabetOnly(resultMsg);


            stringList.add(_step);
        }

    }

    public static String getBetweenSpace(String left,String right)
    {
        int totalSpace=20;
        String result=left;
        int _aplabetOnly = getAlphabetOnly(left).length();
        for (int i=_aplabetOnly;i<totalSpace;i++)
        {
            result += " ";
        }
        result += right;

        return result;
    }

    public static String calculateLeght(String left,String right)
    {
        /*int maxLength = 48 - left.length();
        int resultLegth = maxLength - right.length();
        String result = left;
        for (int i=0;i<resultLegth;i++)
        {
            result += " ";
        }
        result += right;
        return  result;*/

        int maxLength = 48 - getAlphabetOnly(left).length();
        int resultLegth = maxLength - getAlphabetOnly(right).length();
        String result = left;
        for (int i=0;i<resultLegth;i++)
        {
            result += " ";
        }
        result += right;
        return  result;
    }

    public static String calculateCenter(String center)
    {
        int maxLength = maxTextLength - getAlphabetOnly(center).length();
        String result = center;
        for (int i=0;i<maxLength;i++)
        {
            if((maxLength/2) < i){
                result = " " + result;
            } else {
                result += " ";
            }
        }
        return  result;
    }

    public static String calculateCenterByLength(String center, int length)
    {
        int maxLength = length - getAlphabetOnly(center).length();
        String result = center;
        for (int i=0;i<maxLength;i++)
        {
            if((maxLength/2) < i){
                result = " " + result;
            } else {
                result += " ";
            }
        }
        return  result;
    }

    public static String calculateOffSetLeft(String left, String right, int offSetLeft)
    {
        int totalSpace = offSetLeft;
        String result = left;
        int _aplabetOnly = getAlphabetOnly(left).length();
        for (int i=_aplabetOnly;i<totalSpace;i++)
        {
            result += " ";
        }
        result += right;

        return result;
    }

    public static String calculateLeghtRight(String left, String right)
    {
        int maxLength = maxTextLength - getAlphabetOnly(left).length();
        int resultLegth = maxLength - getAlphabetOnly(right).length();
        String result = left;
        for (int i=0;i<resultLegth;i++)
        {
            result += " ";
        }
        result += right;
        return  result;
    }
}
