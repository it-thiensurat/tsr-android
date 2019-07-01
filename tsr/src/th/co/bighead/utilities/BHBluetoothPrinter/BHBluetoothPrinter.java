package th.co.bighead.utilities.BHBluetoothPrinter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import th.co.thiensurat.activities.DeviceListActivity2;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.data.info.PrintTextInfo;

public class BHBluetoothPrinter {

    private static final String TAG = "BHBluetoothPrinter";
    /**********************************************************************************************/

    public static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;

    private static final String MAC_ADDRESS_Printer_1 = "00:01"; //เครื่อง datecs
    /**********************************************************************************************/
    public MainActivity mActivity;
    public BluetoothAdapter mBluetoothAdapter;

    public String mDeviceAddress;

    private DatecsThemalPrint mDatecsThemalPrint;
    private ZJMiniThemalPrint mZJMiniThemalPrint;

    /**********************************************************************************************/

    private List<List<PrintTextInfo>> mDetailPrint;
    private MainActivity.PrintHandler mHandler;
    private boolean mIsWithInterrupt;
    /**********************************************************************************************/

    public BHBluetoothPrinter(MainActivity activity) {
        mActivity = activity;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mDatecsThemalPrint = new DatecsThemalPrint(this);
        mZJMiniThemalPrint = new ZJMiniThemalPrint(this);
    }

    public void SetPrint(List<List<PrintTextInfo>> detailPrint, MainActivity.PrintHandler handler) {
        mDetailPrint = detailPrint;
        mHandler = handler;
        mIsWithInterrupt = false;

        CheckBluetooth();
    }

    public void SetPrintWithInterrupt(List<List<PrintTextInfo>> detailPrint, MainActivity.PrintHandler handler) {
        mDetailPrint = detailPrint;
        mHandler = handler;
        mIsWithInterrupt = true;

        CheckBluetooth();
    }

    private void CheckBluetooth() {
        if (mBluetoothAdapter != null) {
            //ตรวจสอบ Bluetooth
            if (!mBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "Bluetooth Disabled");

                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mActivity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                Log.d(TAG, "Bluetooth Enabled");
                OpenDeviceList();
            }
        } else {
            Log.d(TAG, "Bluetooth is not available");
        }
    }

    public void OpenDeviceList() {
        Intent serverIntent = new Intent(mActivity, DeviceListActivity2.class);
        mActivity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(mActivity, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                    OpenDeviceList();
                }
                break;

            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        String address = data.getExtras().getString(DeviceListActivity2.EXTRA_DEVICE_ADDRESS);
                        if (address != null && !address.isEmpty()) {

                            if (BluetoothAdapter.checkBluetoothAddress(address)) {

                                mDeviceAddress = address;
                                ConnectBluetoothPrinter();

                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(mActivity, "Bluetooth Connect Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void ConnectBluetoothPrinter() {
        if (mDeviceAddress.startsWith(MAC_ADDRESS_Printer_1)) {
            mDatecsThemalPrint.establishBluetoothConnection(mDeviceAddress, mDetailPrint, mHandler, mIsWithInterrupt);
        } else {
            mDatecsThemalPrint.establishBluetoothConnection(mDeviceAddress, mDetailPrint, mHandler, mIsWithInterrupt);
        }
    }

}
