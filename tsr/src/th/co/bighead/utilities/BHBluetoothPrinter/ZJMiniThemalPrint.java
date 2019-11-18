package th.co.bighead.utilities.BHBluetoothPrinter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
//import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.Command.Command;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.data.controller.ThemalPrintController;
import th.co.thiensurat.data.info.PrintTextInfo;
import zj.bluetooth.printer.version3.cn.bluetooth.sdk.BluetoothService;
import zj.bluetooth.printer.version3.command.sdk.PrintPicture;
import zj.bluetooth.printer.version3.command.sdk.PrinterCommand;
//import zj.bluetooth.printer.version3.cn.bluetooth.sdk.BluetoothService;
//import zj.bluetooth.printer.version3.cn.bluetooth.sdk.Main;
//import zj.bluetooth.printer.version3.command.sdk.Command;
//import zj.bluetooth.printer.version3.command.sdk.PrintPicture;
//import zj.bluetooth.printer.version3.command.sdk.PrinterCommand;
////import zj.bluetooth.printer.version3.command.sdk.PrinterCommand;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.view.Gravity.CENTER;

public class ZJMiniThemalPrint {

    private static final String TAG = "ZJMiniThemalPrint";
    /**********************************************************************************************/
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    /**********************************************************************************************/
    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CHOSE_BMP = 3;
    private static final int REQUEST_CAMER = 4;

    //QRcode
    private static final int QR_WIDTH = 350;
    private static final int QR_HEIGHT = 350;
    /*******************************************************************************************************/
    private static final String CHINESE = "GBK";
    private static final String THAI = "CP874";
    private static final String KOREAN = "EUC-KR";
    private static final String BIG5 = "BIG5";

    /*********************************************************************************/

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Member object for the services
    private BluetoothService mService = null;

    /**********************************************************************************************/

    private static String not_connected = "โปรดเชื่อมต่อเครื่องพิมพ์บลูทู ธ";
    private static String msg_error = "ผิดพลาดของท่านโปรดตรวจสอบ!";
    private static String empty1 = "ท่านเป็นที่ว่างเปล่าในกล่องข้อความเพื่อป้อนเนื้อหาที่จะแปลง..";

    /**********************************************************************************************/

    final String[] codebar = {"UPC_A", "UPC_E", "JAN13(EAN13)", "JAN8(EAN8)",
            "CODE39", "ITF", "CODABAR", "CODE93", "CODE128", "QR Code"};
    final byte[][] byteCodebar = {
            {0x1b, 0x40},// 复位打印机
            {0x1b, 0x40},// 复位打印机
            {0x1b, 0x40},// 复位打印机
            {0x1b, 0x40},// 复位打印机
            {0x1b, 0x40},// 复位打印机
            {0x1b, 0x40},// 复位打印机
            {0x1b, 0x40},// 复位打印机
            {0x1b, 0x40},// 复位打印机
            {0x1b, 0x40},// 复位打印机
            {0x1b, 0x40},// 复位打印机
    };

    /**********************************************************************************************/

    private BHBluetoothPrinter mBHBluetoothPrinter;
    private Runnable mJob;

    /**********************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandlerBluetooth = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Print();
                            break;

                        case BluetoothService.STATE_CONNECTING:
                            break;

                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;

                case MESSAGE_READ:
                    break;

                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Log.d(TAG, "Connected to " + mConnectedDeviceName);
                    break;

                case MESSAGE_TOAST:
                    Log.d(TAG, msg.getData().getString(TOAST));
                    break;

                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Log.d(TAG, "Device connection was lost");
                    break;

                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Log.d(TAG, "Unable to connect device");
                    break;
            }
        }
    };

    /**********************************************************************************************/

    public ZJMiniThemalPrint(BHBluetoothPrinter bhBluetoothPrinter) {
        mBHBluetoothPrinter = bhBluetoothPrinter;
    }

    private static String barcodeString = "";
    public static void setBarcodeString(String barcode) {
        barcodeString = barcode;
    }

    /**********************************************************************************************/

    public void onStart(final String address, final List<List<PrintTextInfo>> detailPrint, final MainActivity.PrintHandler handler, boolean isWithInterrupt) {

        final ProgressDialog dialog = new ProgressDialog(mBHBluetoothPrinter.mActivity);
        dialog.setTitle("Plait wait");
        dialog.setMessage("Connecting");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        try {
            mService = new BluetoothService(mBHBluetoothPrinter.mActivity, mHandlerBluetooth);

            if (mService != null) {

                if (mService.getState() == BluetoothService.STATE_NONE) {
                    // Start the Bluetooth services
                    mService.start();
                }

                BluetoothDevice device = mBHBluetoothPrinter.mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                mService.connect(device);

                if (isWithInterrupt) {
                    mJob = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mBHBluetoothPrinter.mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        InterruptProcess(detailPrint, handler, 0);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                mBHBluetoothPrinter.mActivity.toast(e.getLocalizedMessage());
                            }
                        }
                    };
                } else {
                    mJob = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                printText(detailPrint);
                                for (int i = 0; i < detailPrint.size(); i++) {
                                    handler.onBackgroundPrinting(i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                mBHBluetoothPrinter.mActivity.toast(e.getLocalizedMessage());
                            }
                        }
                    };
                }
            }
        } finally {
            dialog.dismiss();
        }
    }

    public void onDestroy() {
        // Stop the Bluetooth services
        if (mService != null) {
            mService.stop();
            mService = null;
        }

        mJob = null;

        Log.d(TAG, "--- ON DESTROY ---");
    }

    /**********************************************************************************************/

    private void Print() {
        if (mJob != null) {
            mBHBluetoothPrinter.mActivity.doJob(mJob, R.string.bluetooth_printing);
        }
    }

    private void printText(final List<List<PrintTextInfo>> detailPrint) {
        try {
            if (mBHBluetoothPrinter.mDeviceAddress != null && !mBHBluetoothPrinter.mDeviceAddress.isEmpty()) {
                //ThemalPrintController themalPrintController = new ThemalPrintController(mPrinter, mBHBluetoothPrinter.mDeviceAddress);
                //themalPrintController.setFontNormal();

                for (List<PrintTextInfo> listInfo : detailPrint) {
                    for (PrintTextInfo info : listInfo) {
                        if (info.text.equals("printHeader")) {
                            Print_Thai("บริษัท เธียรสุรัตน์ จำกัด (มหาชน)\n\n");
                            Print_Thai("43/9 หมู่ 7 ซ.ชูชาติอนุสรณ์ 4 ต.บางตลาด\n\n");
                            Print_Thai("อ.ปากเกร็ด จ.นนทบุรี 11120\n\n");
                            Print_Thai("โทร. 0 2819 8888  แฟกซ์ 0 2962 6951-3\n\n");
                            Print_Thai("อีเมล์. thiensurat@thiensurat.co.th\n\n");
                        }



//                        if (info.text.equals("printShortHeader")) {
//                            themalPrintController.printShortHeader();
//                        } else if (info.text.equals("printHeader")) {
//                            themalPrintController.printHeader();
//                        } else if (info.text.equals("selectPageMode")) {
//                            themalPrintController.selectPageMode();
//                        } else if (info.text.equals("setContractPageRegion")) {
//                            themalPrintController.setContractPageRegion();
//                        } else if (info.text.equals("printContractPageTitle")) {
//                            themalPrintController.printContractPageTitle();
//                        } else if (info.text.equals("beginContractPage")) {
//                            themalPrintController.beginContractPage();
//                        } else if (info.text.equals("endContractPage")) {
//                            themalPrintController.endContractPage();
//                        } else if (info.text.equals("selectStandardMode")) {
//                            themalPrintController.selectStandardMode();
//                        } else if (info.text.contains("setPageRegion")) {
//                            themalPrintController.setPageRegion(info.text);
//                        } else if (info.text.contains("beginPage")) {
//                            themalPrintController.beginPage(info.text);
//                        } else if (info.text.contains("endPage")) {
//                            themalPrintController.endPage();
//                        } else if (info.text.contains("printTitleBackground")) {
//                            themalPrintController.printTitleBackground(info.text);
//                        } else if (info.text.contains("printFrame")) {
//                            themalPrintController.printFrame(info.text);
//                        } else if (info.text.equals("signature")) {
//                            themalPrintController.printSignature("");
//                            //mPrinter.flush();
//                            //Thread.sleep(7000);
//                        } else if (info.isBarcode) {
//                            if (info.isBankBarcode) {
//                                String[] parts = info.text.split("\\|");
//                                themalPrintController.printBankBarcode(parts[0], parts[1], parts[2]);
//                                //mPrinter.flush();
//                                // Thread.sleep(7000);
//                            } else {
//                                themalPrintController.printBarCode128(info.text);
//                                //mPrinter.flush();
//                            }
//                        } else if (info.text.equals("logoTSRTelsale")) {
//                            themalPrintController.printLogoTSRTelsale();
//                        } else {
//                            if (info.language.equals("TH")) {
//                                themalPrintController.sendThaiMessage(info.text);
//                            } else {
//                                themalPrintController.sendEnglishMessage(info.text);
//                            }
//                        }

                    }
                    //themalPrintController.sendThaiMessage("");
                    Print_Thai("\n");
                    SendDataByte(PrinterCommand.POS_Set_Cut(1));
                    SendDataByte(PrinterCommand.POS_Set_PrtInit());
                }


                mBHBluetoothPrinter.mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mBHBluetoothPrinter.mActivity, "Print Complete", Toast.LENGTH_SHORT).show();
                        onDestroy();
                    }
                });
            } else {
                Toast.makeText(mBHBluetoothPrinter.mActivity, "ไม่พบเครื่องพิมพ์", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void InterruptProcess(final List<List<PrintTextInfo>> detailPrint, final MainActivity.PrintHandler handler, final Integer idx) {
        String txtStatus;
        switch (idx) {
            case 0:
                txtStatus = "เริ่มต้นการพิมพ์";
                break;
            default:
                txtStatus = String.format("พิมพ์หน้า %s/%s", idx + 1, detailPrint.size());
                break;
        }

        new AlertDialog.Builder(mBHBluetoothPrinter.mActivity).setTitle(txtStatus).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BHLoading.show(mBHBluetoothPrinter.mActivity);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<List<PrintTextInfo>> pagePrint = new ArrayList<>();
                        pagePrint.add(detailPrint.get(idx));
                        printText(pagePrint);
                        handler.onBackgroundPrinting(idx);


                        mBHBluetoothPrinter.mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BHLoading.close();
                                if (idx >= detailPrint.size() - 1) {
                                    new AlertDialog.Builder(mBHBluetoothPrinter.mActivity).setTitle("สิ้นสุดการพิมพ์").setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                                } else {
                                    InterruptProcess(detailPrint, handler, idx + 1);
                                }
                            }
                        });

                    }
                }).start();
            }
        }).setCancelable(false).show();
    }


    /**********************************************************************************************/


    /*
     * SendDataString
     */
    private void SendDataString(String data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Log.d(TAG, not_connected);
            return;
        }
        if (data.length() > 0) {
            try {
                mService.write(data.getBytes("CP874"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
     *SendDataByte
     */
    private void SendDataByte(byte[] data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Log.d(TAG, not_connected);
            return;
        }
        mService.write(data);
    }

    /*
     * 打印图片
     */
    private void Print_Thai(String msg) {
        SendDataByte(PrinterCommand.POS_Print_Text(msg, THAI, 255, 0, 0, 0));
    }

    private void Print_Bitmap(Bitmap bitmap, int paperWidth) {
        int nMode = 0;
        //paperWidth = 384, 576;
        if (bitmap != null) {
            /**
             * Parameters:
             * mBitmap  要打印的图片
             * nWidth   打印宽度（58和80）
             * nMode    打印模式
             * Returns: byte[]
             */
            byte[] data = PrintPicture.POS_PrintBMP(bitmap, paperWidth, nMode);
            //	SendDataByte(buffer);
            SendDataByte(Command.ESC_Init);
            SendDataByte(Command.LF);
            SendDataByte(data);
            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }
    }

    public void Print_BarCode(String str, int type) {
        Log.e("Barcode string", str);
        SendDataByte(byteCodebar[type]);
        if (type == 0) {
            if (str.length() == 11 || str.length() == 12) {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 65, 3, 168, 0, 2);
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataString("UPC_A\n");
                SendDataByte(code);
            } else {
                Log.d(TAG, msg_error);
                return;
            }
        } else if (type == 1) {
            if (str.length() == 6 || str.length() == 7) {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 66, 3, 168, 0, 2);
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataString("UPC_E\n");
                SendDataByte(code);
            } else {
                Log.d(TAG, msg_error);
                return;
            }
        } else if (type == 2) {
            if (str.length() == 12 || str.length() == 13) {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 67, 3, 168, 0, 2);
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataString("JAN13(EAN13)\n");
                SendDataByte(code);
            } else {
                Log.d(TAG, msg_error);
                return;
            }
        } else if (type == 3) {
            if (str.length() > 0) {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 68, 3, 168, 0, 2);
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataString("JAN8(EAN8)\n");
                SendDataByte(code);
            } else {
                Log.d(TAG, msg_error);
                return;
            }
        } else if (type == 4) {
            if (str.length() == 0) {
                Log.d(TAG, msg_error);
                return;
            } else {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 69, 3, 168, 1, 2);
                SendDataString("CODE39\n");
                SendDataByte(new byte[]{0x1b, 0x61, 0x00 });
                SendDataByte(code);
                SendDataString("\n");
                SendDataString(str);
            }
        } else if (type == 5) {
            if (str.length() == 0) {
                Log.d(TAG, msg_error);
                return;
            } else {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 70, 3, 168, 1, 2);
                SendDataString("ITF\n");
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataByte(code);
            }
        } else if (type == 6) {
            if (str.length() == 0) {
                Log.d(TAG, msg_error);
                return;
            } else {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 71, 3, 168, 1, 2);
                SendDataString("CODABAR\n");
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataByte(code);
            }
        } else if (type == 7) {
            if (str.length() == 0) {
                Log.d(TAG, msg_error);
                return;
            } else {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 72, 3, 168, 1, 2);
                SendDataString("CODE93\n");
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataByte(code);
            }
        } else if (type == 8) {
            if (str.length() == 0) {
                Log.d(TAG, msg_error);
                return;
            } else {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 73, 3, 168, 1, 2);
                SendDataString("CODE128\n");
                SendDataByte(new byte[]{0x1b, 0x61, 0x00 });
                SendDataByte(code);
            }
        } else if (type == 9) {
            if (str.length() == 0) {
                Log.d(TAG, empty1);
                return;
            } else {
                byte[] code = PrinterCommand.getBarCommand(str, 1, 3, 8);
                SendDataString("QR Code\n");
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataByte(code);
            }
        }
    }
    /**********************************************************************************************/

    /**
     *
     * Edit by Teerayut Klinsanga
     *
     * Created: 2019-08-02 09:00.00
     *
     * == Print with image ==
     *
     */

    public void connect(final String address, final Bitmap[] bmp, final List<List<PrintTextInfo>> detailPrint, final MainActivity.PrintHandler handler, boolean isWithInterrupt, String receiptType) {
        final ProgressDialog dialog = new ProgressDialog(mBHBluetoothPrinter.mActivity);
        dialog.setTitle("Plait wait");
        dialog.setMessage("Connecting");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        try {
            mService = new BluetoothService(mBHBluetoothPrinter.mActivity, mHandlerBluetooth);

            if (mService != null) {
                if (mService.getState() == BluetoothService.STATE_NONE) {
                    // Start the Bluetooth services
                    mService.start();
                }

                BluetoothDevice device = mBHBluetoothPrinter.mBluetoothAdapter.getRemoteDevice(address);
                mService.connect(device);

                if (isWithInterrupt) {
                    mJob = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mBHBluetoothPrinter.mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        InterruptProcess(detailPrint, handler, 0);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                mBHBluetoothPrinter.mActivity.toast(e.getLocalizedMessage());
                            }
                        }
                    };
                } else {
                    mJob = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int i = 0; i < bmp.length; i++) {
                                    printCustomBitmap(bmp[i], i, receiptType);
                                    handler.onBackgroundPrinting(i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                mBHBluetoothPrinter.mActivity.toast(e.getLocalizedMessage());
                            }
                        }
                    };
                }
            } else {
                Log.e("Check service", mService.getState() + "");
            }
        } finally {
            if ("Barcode".equals(receiptType)) {
                handler.onPrintCompleted();
            }
            dialog.dismiss();
        }
    }

    public void printCustomBitmap(Bitmap bitmap, int index, String receiptType) {
        int nMode = 0;
        //paperWidth = 384, 576;
        String path = "";
//        Log.e("print index", String.valueOf(index));
        if (bitmap != null) {
            /**
             * Parameters:
             * mBitmap  要打印的图片
             * nWidth   打印宽度（58和80）
             * nMode    打印模式
             * Returns: byte[]
             */
            byte[] data = PrintPicture.POS_PrintBMP(bitmap, 576, nMode);
            SendDataByte(Command.ESC_Init);
            SendDataByte(Command.LF);
            SendDataByte(data);
            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
//            if (!"Barcode".equals(receiptType)) {
//                SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
//                SendDataByte(PrinterCommand.POS_Set_Cut(1));
//                SendDataByte(PrinterCommand.POS_Set_PrtInit());
//            }

//            if ("Barcode".equals(receiptType)) {
//                byte[] d = PrintPicture.POS_PrintBMP(generateBarcode(barcodeString), 810, nMode);
//                SendDataByte(Command.ESC_Init);
//                SendDataByte(Command.LF);
//                SendDataByte(d);
//                Command.ESC_Align[2] = 0x02;
//                SendDataByte(Command.ESC_Align);
//                SendDataString(barcodeString);
//                SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(100));
//                SendDataByte(PrinterCommand.POS_Set_Cut(1));
//                SendDataByte(PrinterCommand.POS_Set_PrtInit());
//            }

//            if (index == 1) {
//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        SendDataByte(Command.ESC_Init);
//                        SendDataByte(Command.LF);
//                        SendDataByte(data);
//                        SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
//                        SendDataByte(PrinterCommand.POS_Set_Cut(1));
//                        SendDataByte(PrinterCommand.POS_Set_PrtInit());
//                        mService.stop();
//                    }
//                }, 2500);
//            } else {
//                SendDataByte(Command.ESC_Init);
//                SendDataByte(Command.LF);
//                SendDataByte(data);
//                SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
//                SendDataByte(PrinterCommand.POS_Set_Cut(1));
//                SendDataByte(PrinterCommand.POS_Set_PrtInit());
//            }

//            mService.stop();
        }
    }

    public Bitmap generateBarcode(String contents) {
        Bitmap bitmap = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(contents, BarcodeFormat.CODE_128,810,120);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            Log.e("create barcode", e.getLocalizedMessage());
        }

        return bitmap;
    }

//    public Bitmap createBarcode128(String contents) {
//        EnumMap<EncodeHintType, Object> hint = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
//        hint.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        BitMatrix result = null;
//        try {
//            result = new MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128, 800, 80, hint);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        int w = result.getWidth();
//        int h = result.getHeight();
//        int[] pixels = new int[w * h];
//        for (int y = 0; y < h; y++) {
//            int offset = y * w;
//            for (int x = 0; x < w; x++) {
//                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
//            }
//        }
//        Bitmap bit = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        bit.setPixels(pixels, 0, w, 0, 0, w, h);
//        return bit;
//    }
}
