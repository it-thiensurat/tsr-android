package zj.bluetooth.printer.version3.cn.bluetooth.sdk;

import java.io.UnsupportedEncodingException;

import zj.bluetooth.printer.version3.command.sdk.Command;
import zj.bluetooth.printer.version3.command.sdk.PrintPicture;
import zj.bluetooth.printer.version3.command.sdk.PrinterCommand;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Main {
    /******************************************************************************************************/
    // Debugging
    private static final String TAG = "Main_Activity";
    private static final boolean DEBUG = true;
    /******************************************************************************************************/
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    /*******************************************************************************************************/
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
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    private BluetoothService mService = null;

    private Activity mActivity;
    /******************************************************************************************************/

    private static String not_connected = "โปรดเชื่อมต่อเครื่องพิมพ์บลูทู ธ";
    private static String msg_error = "ผิดพลาดของท่านโปรดตรวจสอบ!";
    private static String empty1 = "ท่านเป็นที่ว่างเปล่าในกล่องข้อความเพื่อป้อนเนื้อหาที่จะแปลง..";
    /******************************************************************************************************/

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

    /******************************************************************************************************/

    public Main(Activity activity) {
        mActivity = activity;
    }

    public void onCreate() {
        if (DEBUG)
            Log.d(TAG, "+++ ON CREATE +++");

        // Set up the window layout
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            if (DEBUG)
                Log.d(TAG, "Bluetooth is not available");
        }
    }

    public void onStart() {

        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
            if (mService == null)
                KeyListenerInit();//监听
        }
    }

    public synchronized void onResume() {

        if (mService != null) {

            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mService.start();
            }
        }
    }

    public synchronized void onPause() {
        if (DEBUG)
            Log.d(TAG, "- ON PAUSE -");
    }

    public void onStop() {
        if (DEBUG)
            Log.d(TAG, "-- ON STOP --");
    }

    public void onDestroy() {
        // Stop the Bluetooth services
        if (mService != null)
            mService.stop();
        if (DEBUG)
            Log.d(TAG, "--- ON DESTROY ---");
    }

    /*****************************************************************************************************/
    private void KeyListenerInit() {
        mService = new BluetoothService(mActivity, mHandler);
    }

    /*****************************************************************************************************/
    /*
     * SendDataString
     */
    private void SendDataString(String data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            if (DEBUG)
                Log.d(TAG, not_connected);
            return;
        }
        if (data.length() > 0) {
            try {
                mService.write(data.getBytes("GBK"));
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
            if (DEBUG)
                Log.d(TAG, not_connected);
            return;
        }
        mService.write(data);
    }

    /****************************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (DEBUG)
                        Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            //Print();
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
                    if (DEBUG)
                        Log.d(TAG, "Connected to " + mConnectedDeviceName);
                    break;

                case MESSAGE_TOAST:
                    if (DEBUG)
                        Log.d(TAG, msg.getData().getString(TOAST));
                    break;

                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    if (DEBUG)
                        Log.d(TAG, "Device connection was lost");
                    break;

                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    if (DEBUG)
                        Log.d(TAG, "Unable to connect device");
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DEBUG)
            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE: {
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    if (BluetoothAdapter.checkBluetoothAddress(address)) {
                        BluetoothDevice device = mBluetoothAdapter
                                .getRemoteDevice(address);
                        // Attempt to connect to the device
                        mService.connect(device);
                    }
                }
                break;
            }
            case REQUEST_ENABLE_BT: {
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a session
                    KeyListenerInit();
                } else {
                    // User did not enable Bluetooth or an error occured
                    if (DEBUG)
                        Log.d(TAG, "BT not enabled");
                }
                break;
            }
        }
    }

    /****************************************************************************************************/
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

    private void Print_BarCode(String str, int type) {
        if (type == 0) {
            if (str.length() == 11 || str.length() == 12) {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 65, 3, 168, 0, 2);
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataString("UPC_A\n");
                SendDataByte(code);
            } else {
                if (DEBUG)
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
                if (DEBUG)
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
                if (DEBUG)
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
                if (DEBUG)
                    Log.d(TAG, msg_error);
                return;
            }
        } else if (type == 4) {
            if (str.length() == 0) {
                if (DEBUG)
                    Log.d(TAG, msg_error);
                return;
            } else {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 69, 3, 168, 1, 2);
                SendDataString("CODE39\n");
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataByte(code);
            }
        } else if (type == 5) {
            if (str.length() == 0) {
                if (DEBUG)
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
                if (DEBUG)
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
                if (DEBUG)
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
                if (DEBUG)
                    Log.d(TAG, msg_error);
                return;
            } else {
                byte[] code = PrinterCommand.getCodeBarCommand(str, 73, 3, 168, 1, 2);
                SendDataString("CODE128\n");
                SendDataByte(new byte[]{0x1b, 0x61, 0x00});
                SendDataByte(code);
            }
        } else if (type == 9) {
            if (str.length() == 0) {
                if (DEBUG)
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
    /************************************************************************************************/
}
