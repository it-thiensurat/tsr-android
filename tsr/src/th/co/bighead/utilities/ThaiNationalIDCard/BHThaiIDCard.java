package th.co.bighead.utilities.ThaiNationalIDCard;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import amlib.ccid.Error;
import amlib.ccid.Reader;
import amlib.hw.HWType;
import amlib.hw.HardwareInterface;
import th.co.bighead.utilities.BHLoading;


public class BHThaiIDCard {

    public enum ResultNotSuccess {
        ConnectNoDevice, PermissionDenied, NoCard, ErrorPowerOn, ErrorNewAPDUThailandIdCardType, ErrorDataPersonal
    }

    private static final String TAG = "BHThaiIDCard_Log";
    private static final String ACTION_USB_PERMISSION = "th.co.bighead.USB_PERMISSION";

    private Context mContext;
    public IntentFilter mFilter;
    public PendingIntent mPermissionIntent;

    public Reader mReader;
    public HardwareInterface mMyDev;


    public UsbDevice mUsbDevice;
    public UsbManager mUsbManager;

    private IAPDU_THAILAND_IDCARD mAPDU;

    private BHThaiIDCard mBHThaiIDCard;
    private ResultBHThaiIDCard mDelegate;


    public BHThaiIDCard(Context context, ResultBHThaiIDCard delegate) {
        this.mContext = context;
        this.mBHThaiIDCard = this;
        this.mDelegate = delegate;

        //createHardwareInterface();
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BHLoading.show(mContext);
                createHardwareInterface();
            }
        });


    }

    private void createHardwareInterface() {
        Log.d(TAG, "Create HardwareInterface");

        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        try {
            mMyDev = new HardwareInterface(HWType.eUSB);
        } catch (Exception e) {
            logException(e);
            return;
        }

        toRegisterReceiver();
        connect();
    }

    private void toRegisterReceiver() {
        Log.d(TAG, "To Register Receiver");
        // Register receiver for USB permission
        mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        mFilter = new IntentFilter();
        mFilter.addAction(ACTION_USB_PERMISSION);
        mFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mContext.registerReceiver(mReceiver, mFilter);
    }

    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Broadcast Receiver");

            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    BHLoading.show(mContext);
                                    onDevPermit(device);
                                }
                            });
                            //onDevPermit( device );
                            //poweron();
                        }
                    } else {
                        Log.d(TAG, "Permission denied for device " + device.getDeviceName());
                        mDelegate.onNotSuccess(mBHThaiIDCard, ResultNotSuccess.PermissionDenied);
                    }
                }

            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Log.d(TAG, "Action USB Device Detached");
                onDetache(intent);

//                synchronized (this) {
//                    updateReaderList(intent);
//                }

            }
        }/*end of onReceive(Context context, Intent intent) {*/
    };


    private void onDevPermit(UsbDevice dev) {
        Log.d(TAG, "On Dev Permit");

        try {
            mUsbDevice = dev;
//            updateViewReader();
            new OpenTask().execute(dev);
        } catch (Exception e) {
            logException(e);
        }
    }

    private class OpenTask extends AsyncTask<UsbDevice, Void, Boolean> {

        @Override
        protected Boolean doInBackground(UsbDevice... params) {
            boolean result = false;
            try {
                result = InitReader();
                if (!result) {
                    Log.d(TAG, "fail to initial reader");
                    return result;
                }
                //status = mReader.connect();
            } catch (Exception e) {
                logException(e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Log.d(TAG, "Open fail");
            } else {
                Log.d(TAG, "Open successfully");

                int resultPowerOn = powerOn();
                if (resultPowerOn == Error.READER_SUCCESSFUL) {
                    int resultNewAPDUType = newAPDUThailandIdCardType();
                    if (resultNewAPDUType == Error.READER_SUCCESSFUL) {

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Personal personal = readAll();

                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (personal != null) {
                                            mDelegate.onSuccess(mBHThaiIDCard, personal);
                                        } else {
                                            //Error
                                            mDelegate.onNotSuccess(mBHThaiIDCard, ResultNotSuccess.ErrorDataPersonal);
                                        }
                                    }
                                });
                            }
                        });
                        thread.start();


                    } else {
                        //Error
                        mDelegate.onNotSuccess(mBHThaiIDCard, ResultNotSuccess.ErrorNewAPDUThailandIdCardType);
                    }
                } else if (resultPowerOn == Error.READER_NO_CARD) {
                    mDelegate.onNotSuccess(mBHThaiIDCard, ResultNotSuccess.NoCard);
                } else {
                    //Error
                    mDelegate.onNotSuccess(mBHThaiIDCard, ResultNotSuccess.ErrorPowerOn);
                }
            }
        }
    }

    private boolean InitReader() {
        Log.d(TAG, "Init Reader");

        boolean result = false;
        try {
            result = mMyDev.Init(mUsbManager, mUsbDevice);
            if (!result) {
                Log.d(TAG, "Device init fail");
                return result;
            }
            mReader = new Reader(mMyDev);
            mReader.setSlot((byte) 0);

        } catch (Exception e) {
            logException(e);
        }
        return result;
    }


    private void onDetache(Intent intent) {
        UsbDevice udev = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        if (udev != null) {
            if (udev.equals(mUsbDevice)) {
                closeReaderUp();
            }
        } else {
            Log.d(TAG, "usb device is null");
        }
    }

    private void closeReaderUp() {
        Log.d(TAG, "Closing reader...");

        if (mReader != null) {
            mReader.close();
        }
        mMyDev.Close();
        mContext.unregisterReceiver(mReceiver);
    }

    private int powerOn() {
        Log.d(TAG, "Power On");

        int result = Error.READER_SUCCESSFUL;

        //check slot status first
        if (getSlotStatus() == Error.READER_NO_CARD) {
            Log.d(TAG, "Card Absent");
            return Error.READER_NO_CARD;
        }
        try {
            result = mReader.setPower(Reader.CCID_POWERON);
        } catch (Exception e) {
            logException(e);
        }
        return result;
    }

    private int getSlotStatus() {
        Log.d(TAG, "Get Slot Status");

        int ret = Error.READER_NO_CARD;
        byte[] pCardStatus = new byte[1];

        /*detect card hotplug events*/
        try {
            if (mReader.getCardStatus(pCardStatus) == Error.READER_SUCCESSFUL) {
                //Log.d(TAG,"cmd OK  mSlotStatus = " +mSlotStatus);
                if (pCardStatus[0] == Reader.SLOT_STATUS_CARD_ABSENT) {
                    Log.d(TAG, "READER_NO_CARD");
                    ret = Error.READER_NO_CARD;
                } else {
                    Log.d(TAG, "READER_SUCCESSFUL");
                    ret = Error.READER_SUCCESSFUL;
                }
            }
        } catch (Exception e) {
            logException(e);
        }
        return ret;
    }

    private int newAPDUThailandIdCardType() {
        Log.d(TAG, "New APDU Thailand Id Card Type");

        int ret = Error.READER_SUCCESSFUL;

        try {
            byte[] atr = mReader.getAtr();
            if (atr == null || atr.length < 2) {
                return Error.READER_TRANSMIT_ERROR;
            }

            if (atr[0] == 0x3B && atr[1] == 0x67) {
                /* corruption card */
                mAPDU = new APDU_THAILAND_IDCARD_TYPE_01();
            } else {
                mAPDU = new APDU_THAILAND_IDCARD_TYPE_02();
            }
        } catch (Exception e) {
            logException(e);
            return Error.READER_TRANSMIT_ERROR;
        }

        return ret;
    }


    private String GetUTF8FromAsciiBytes(byte[] ascii_bytes) {
        if (ascii_bytes != null) {
            try {
                String str = new String(ascii_bytes, "TIS620");
                return convertStringTIS620ByListDecAsciiToSpace(str);

            } catch (UnsupportedEncodingException e) {
                logException(e);
            }
        }

        return null;
    }


    private String convertStringTIS620ByListDecAsciiToSpace(String s) {
        List<Integer> listDecAscii = new ArrayList<Integer>();
        listDecAscii.add(0);
        listDecAscii.add(144);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int decAscii = (int) c;

            if (listDecAscii.indexOf(decAscii) != -1) {
                c = ' ';
            }

            sb.append(c);
        }
        return sb.toString();
    }

    private byte[] SendCommand(byte[] pSendAPDU) {
        return SendCommand(pSendAPDU, true);
    }

    private byte[] SendCommand(byte[] pSendAPDU, boolean isSelect) {
        byte[] pRecvRes = new byte[300];
        int[] pRevAPDULen = new int[1];
        int sendLen, result;

        pRevAPDULen[0] = 300;
        sendLen = pSendAPDU.length;

        try {
            Log.d(TAG, "Send APDU: " + logBuffer(pSendAPDU, sendLen));

            result = mReader.transmit(pSendAPDU, sendLen, pRecvRes, pRevAPDULen);
            if (result == Error.READER_SUCCESSFUL) {
                Log.d(TAG, "Receive APDU: " + logBuffer(pRecvRes, pRevAPDULen[0]));

                if (isSelect && pRevAPDULen[0] == 2) {
                    byte[] getResponse = SendCommand(mAPDU.APDU_GET_RESPONSE(new byte[]{pRecvRes[1]}), false);

                    return getResponse;
                } else {
                    return Arrays.copyOfRange(pRecvRes, 0, pRevAPDULen[0]);
                }

            } else {
                Log.d(TAG, "Fail to Send APDU: " + Integer.toString(result) + "(" + Integer.toHexString(mReader.getCmdFailCode()) + ")");
            }
        } catch (Exception e) {
            logException(e);
        }

        return null;
    }

    /*private byte[] SendPhotoCommand() {
        try (ByteArrayOutputStream s = new ByteArrayOutputStream()) {
            byte[][] cmds_photo = mAPDU.Photo();

            for (int i = 0; i < cmds_photo.length; i++) {
                Log.d(TAG, "SendPhotoCommand: " + i);

                byte[] b = SendCommand(cmds_photo[i]);
                if (b != null) {
                    s.write(b, 0, b.length);
                }
            }
            return s.toByteArray();
        } catch (Exception e) {
            logException(e);
        }

        return null;
    }*/

    public byte[] SendPhotoCommand() {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] message = new byte[]{(byte) 0x80, (byte) 0xb0, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00};
            byte[] pictureBuffer;


            int offset = 0x017b, blockLength, index = 0;
            //ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int PERSONAL_PIC_LENGTH = 5118;

            while (index < PERSONAL_PIC_LENGTH) {
                blockLength = ((PERSONAL_PIC_LENGTH - index) > 0xff) ? 0xff : (PERSONAL_PIC_LENGTH - index);

                message[2] = (byte) ((offset >> 8) & 0xff);
                message[3] = (byte) (offset & 0xff);
                message[6] = (byte) blockLength;

                byte[] data = SendCommand(message);

                buffer.write(data, 0, blockLength);

                offset += blockLength;
                index += blockLength;
            }

            pictureBuffer = buffer.toByteArray();

            return pictureBuffer;
        } catch (Exception e) {
            logException(e);
        }

        return null;
    }

    private void connect() {
        Log.d(TAG, "Connect");

        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        if (deviceList != null && deviceList.size() > 0) {
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();

                //your code
                if (device != null && (device.getProductId() == 38208)) {
                    Log.d(TAG, "VendorId " + device.getVendorId() + ", ProductId " + device.getProductId());
                    mUsbManager.requestPermission(device, mPermissionIntent);
                    break;
                }
            }
        } else {
            Log.d(TAG, "No Device");
            mDelegate.onNotSuccess(mBHThaiIDCard, ResultNotSuccess.ConnectNoDevice);
        }
    }


    private Personal readAll() {
        try {
            Personal personal = new Personal();
            if (Open()) {

                personal.setCID(GetUTF8FromAsciiBytes(SendCommand(mAPDU.CID())));
                personal.setTHFullname(GetUTF8FromAsciiBytes(SendCommand(mAPDU.TH_Fullname())));
                personal.setENFullname(GetUTF8FromAsciiBytes(SendCommand(mAPDU.EN_Fullname())));
                personal.setDateOfBirth(GetUTF8FromAsciiBytes(SendCommand(mAPDU.Date_Of_Birth())));
                personal.setGender(GetUTF8FromAsciiBytes(SendCommand(mAPDU.Gender())));
                personal.setCardIssuer(GetUTF8FromAsciiBytes(SendCommand(mAPDU.Card_Issuer())));
                personal.setIssueDate(GetUTF8FromAsciiBytes(SendCommand(mAPDU.Issue_Date())));
                personal.setExpireDate(GetUTF8FromAsciiBytes(SendCommand(mAPDU.Expire_Date())));
                personal.setAddress(GetUTF8FromAsciiBytes(SendCommand(mAPDU.Address())));

                // Photo
                personal.setPhotoRaw(SendPhotoCommand());


                //Close();
                return personal;
            }
        } catch (Exception e) {
            logException(e);
        }

        return null;
    }

    private boolean Open() {
        try {
            byte[] pSendAPDU = mAPDU.APDU_SELECT(mAPDU.AID_MOI());

            byte[] pRecvRes = new byte[300];
            int[] pRevAPDULen = new int[1];
            int sendLen, result;

            pRevAPDULen[0] = 300;
            sendLen = pSendAPDU.length;

            result = mReader.transmit(pSendAPDU, sendLen, pRecvRes, pRevAPDULen);
            if (result == Error.READER_SUCCESSFUL) {
                Log.d(TAG, "Receive APDU: " + logBuffer(pRecvRes, pRevAPDULen[0]));

                return true;
            } else {
                Log.d(TAG, "Fail to Send APDU: " + Integer.toString(result) + "(" + Integer.toHexString(mReader.getCmdFailCode()) + ")");
            }
        } catch (Exception e) {
            logException(e);
        }

        return false;
    }

    public Boolean Close() {
        try {
            synchronized (this) {
                BHLoading.close();
            }

            closeReaderUp();
            return true;
        } catch (Exception e) {
            logException(e);
            return false;
        }


    }


    private byte[] toByteArray(String hexString) {

        int hexStringLength = hexString.length();
        byte[] byteArray = null;
        int count = 0;
        char c;
        int i;

        // Count number of hex characters
        for (i = 0; i < hexStringLength; i++) {

            c = hexString.charAt(i);
            if (c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
                    && c <= 'f') {
                count++;
            }
        }

        byteArray = new byte[(count + 1) / 2];
        boolean first = true;
        int len = 0;
        int value;
        for (i = 0; i < hexStringLength; i++) {

            c = hexString.charAt(i);
            if (c >= '0' && c <= '9') {
                value = c - '0';
            } else if (c >= 'A' && c <= 'F') {
                value = c - 'A' + 10;
            } else if (c >= 'a' && c <= 'f') {
                value = c - 'a' + 10;
            } else {
                value = -1;
            }

            if (value >= 0) {

                if (first) {

                    byteArray[len] = (byte) (value << 4);

                } else {

                    byteArray[len] |= value;
                    len++;
                }

                first = !first;
            }
        }

        return byteArray;
    }

    private String logBuffer(byte[] buffer, int bufferLength) {

        String bufferString = "";
        String dbgString = "";

        for (int i = 0; i < bufferLength; i++) {

            String hexChar = Integer.toHexString(buffer[i] & 0xFF);
            if (hexChar.length() == 1) {
                hexChar = "0" + hexChar;
            }

            if (i % 16 == 0) {
                if (dbgString != "") {
                    bufferString += dbgString;
                    dbgString = "";
                }
            }

            dbgString += hexChar.toUpperCase() + " ";
        }

        if (dbgString != "") {
            bufferString += dbgString;
        }

        return bufferString;
    }

    private void logException(Exception e) {
        Log.e(TAG, "Get Exception : " + e.getMessage());
    }


}
