package th.co.bighead.utilities.BHBluetoothPrinter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.datecs.api.emsr.EMSR;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.PrinterInformation;
import com.datecs.api.printer.ProtocolAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.bluetooth.PrinterServer;
import th.co.bighead.utilities.bluetooth.PrinterServerListener;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.data.controller.ThemalPrintController;
import th.co.thiensurat.data.info.PrintTextInfo;

public class DatecsThemalPrint {

    private static final String TAG = "DatecsThemalPrint";

    /**********************************************************************************************/

    private BHBluetoothPrinter mBHBluetoothPrinter;

    /**********************************************************************************************/

    private BluetoothSocket mBluetoothSocket;
    private PrinterServer mPrinterServer;
    private Socket mPrinterSocket;
    private Printer mPrinter;
    private ProtocolAdapter mProtocolAdapter;
    private boolean mRestart = true;
    private ProtocolAdapter.Channel mPrinterChannel;
    private PrinterInformation mPrinterInfo;

    public DatecsThemalPrint(BHBluetoothPrinter bhBluetoothPrinter) {
        mBHBluetoothPrinter = bhBluetoothPrinter;
    }

    public void establishBluetoothConnection(final String address, final List<List<PrintTextInfo>> detailPrint, final MainActivity.PrintHandler handler, boolean isWithInterrupt) {
        Log.d(TAG, "establishBluetoothConnection");

        Runnable job;
        if (isWithInterrupt) {
            job = new Runnable() {
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
            job = new Runnable() {
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

        final ProgressDialog dialog = new ProgressDialog(mBHBluetoothPrinter.mActivity);
        dialog.setTitle("Plait wait");
        dialog.setMessage("Connecting");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        closePrinterServer();

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Connecting to " + address + "...");

                btAdapter.cancelDiscovery();

                try {
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                    BluetoothDevice btDevice = btAdapter.getRemoteDevice(address);
                    InputStream in = null;
                    OutputStream out = null;

                    BluetoothSocket btSocket = null;
                    boolean isBtConnect = false;
                    int retryInterval = 0;
                    int maxRetryInterval = 4;
                    while (!isBtConnect) {
                        try {
                            btSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
                            btSocket.connect();
                            isBtConnect = true;
                        } catch (IOException e) {
                            retryInterval++;
                            //error("FAILED to connect: " + e.getMessage() + " Repeat " + retryInterval);
                            Log.d(TAG, "FAILED to connect 1: " + e.getMessage() + " Repeat " + retryInterval);
                            try {
                                btSocket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            if(retryInterval == maxRetryInterval) {
                                if(!btSocket.isConnected()) {
                                    waitForConnection(job);
                                    return;
                                }
                            }
                        }
                    }

                    try {
                        mBluetoothSocket = btSocket;
                        in = mBluetoothSocket.getInputStream();
                        out = mBluetoothSocket.getOutputStream();
                        initPrinter(in, out, job);
                    } catch (IOException e) {
                        Log.d(TAG, "FAILED to connect 2: " + e.getMessage());
                        return;
                    }
                } finally {
                    dialog.dismiss();
                }
            }
        });
        t.start();
    }

    private synchronized void closePrinterServer() {
        closeNetworkConnection();
        PrinterServer ps = mPrinterServer;
        mPrinterServer = null;
        if (ps != null) {
            if (BHGeneral.DEVELOPER_MODE) Log.d(TAG, "Close Network server");
            try {
                ps.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void closeNetworkConnection() {
        Socket s = mPrinterSocket;
        mPrinterSocket = null;
        if (s != null) {
            if (BHGeneral.DEVELOPER_MODE) Log.d(TAG, "Close Network socket");
            try {
                s.shutdownInput();
                s.shutdownOutput();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void waitForConnection(Runnable job) {
        closeActiveConnection();
        mBHBluetoothPrinter.OpenDeviceList();
        try {
            mPrinterServer = new PrinterServer(new PrinterServerListener() {
                @Override
                public void onConnect(Socket socket) {
                    if (BHGeneral.DEVELOPER_MODE)
                        Log.d(TAG, "Accept connection from " + socket.getRemoteSocketAddress().toString());
                        mBHBluetoothPrinter.mActivity.finishActivity(BHBluetoothPrinter.REQUEST_CONNECT_DEVICE);
                        mPrinterSocket = socket;
                    try {
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();
                        initPrinter(in, out, job);
                    } catch (IOException e) {
                        e.printStackTrace();
                        error(mBHBluetoothPrinter.mActivity.getString(R.string.bluetooth_failed_to_init) + ". " + e.getMessage(), mRestart, job);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void closeActiveConnection() {
        closePrinterConnection();
        closeBlutoothConnection();
        closeNetworkConnection();
        closePrinterServer();
    }

    private synchronized void closePrinterConnection() {
        if (mPrinter != null) {
            //mPrinter.release();
            mPrinter.close();
            mPrinter = null;
        }

        if (mProtocolAdapter != null) {
            //mProtocolAdapter.release();
            mProtocolAdapter.close();
            mProtocolAdapter = null;
        }
    }

    private synchronized void closeBlutoothConnection() {
        // Close Bluetooth connection
        BluetoothSocket s = mBluetoothSocket;
        mBluetoothSocket = null;
        if (s != null) {
            if (BHGeneral.DEVELOPER_MODE) Log.d(TAG, "Close Blutooth socket");
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void initPrinter(InputStream inputStream, OutputStream outputStream, Runnable job) throws IOException {
        Log.d(TAG, "Initialize printer...");

        // Here you can enable various debug information
        //ProtocolAdapter.setDebug(true);
        Printer.setDebug(true);
        EMSR.setDebug(true);

        // Check if printer is into protocol mode. Ones the object is created it can not be released
        // without closing base streams.
        mProtocolAdapter = new ProtocolAdapter(inputStream, outputStream);
        if (mProtocolAdapter.isProtocolEnabled()) {
            Log.d(TAG, "Protocol mode is enabled");

            // Into protocol mode we can callbacks to receive printer notifications
            mProtocolAdapter.setPrinterListener(new ProtocolAdapter.PrinterListener() {
                @Override
                public void onThermalHeadStateChanged(boolean overheated) {
                    if (overheated) {
                        Log.d(TAG, "Thermal head is overheated");
                        //status("OVERHEATED");
                    } else {
                        // status(null);
                    }
                }

                @Override
                public void onPaperStateChanged(boolean hasPaper) {
                    if (hasPaper) {
                        Log.d(TAG, "Event: Paper out");
                        //status("PAPER OUT");
                    } else {
                        // status(null);
                    }
                }

                @Override
                public void onBatteryStateChanged(boolean lowBattery) {
                    if (lowBattery) {
                        Log.d(TAG, "Low battery");
                        //status("LOW BATTERY");
                    } else {
                        //status(null);
                    }
                }
            });


            mProtocolAdapter.setCardListener(new ProtocolAdapter.CardListener() {
                @Override
                public void onReadCard(boolean encrypted) {
                    Log.d(TAG, "On read card(entrypted=" + encrypted + ")");
                }
            });

            // Get printer instance
            mPrinterChannel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
            mPrinter = new Printer(mPrinterChannel.getInputStream(), mPrinterChannel.getOutputStream());

            mPrinterInfo = mPrinter.getInformation();

            if (job != null) {
                mBHBluetoothPrinter.mActivity.doJob(job, R.string.bluetooth_printing);
            }

        } else {
            Log.d(TAG, "Protocol mode is disabled");
            // Protocol mode it not enables, so we should use the row streams.
            mPrinter = new Printer(mProtocolAdapter.getRawInputStream(),
                    mProtocolAdapter.getRawOutputStream());
            if (job != null) {
                mBHBluetoothPrinter.mActivity.doJob(job, R.string.bluetooth_printing);
            }
        }


        mPrinter.setConnectionListener(new Printer.ConnectionListener() {
            @Override
            public void onDisconnect() {
                mBHBluetoothPrinter.mActivity.toast("Printer is disconnected");

                mBHBluetoothPrinter.mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mBHBluetoothPrinter.mActivity.isFinishing()) {
                            // waitForConnection();
                        }
                    }
                });
            }
        });

    }

    private void error(final String text, boolean resetConnection, Runnable job) {
        if (resetConnection) {
            mBHBluetoothPrinter.mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mBHBluetoothPrinter.mActivity, text, Toast.LENGTH_SHORT).show();
                }
            });

            waitForConnection(job);
        }
    }



    private void printText(final List<List<PrintTextInfo>> detailPrint) {
        try {
            if (mBHBluetoothPrinter.mDeviceAddress != null && !mBHBluetoothPrinter.mDeviceAddress.isEmpty()) {
                ThemalPrintController themalPrintController = new ThemalPrintController(mPrinter, mBHBluetoothPrinter.mDeviceAddress);
                mPrinter.reset();
                themalPrintController.setFontNormal();
                for (List<PrintTextInfo> listInfo : detailPrint) {
                    for (PrintTextInfo info : listInfo) {
//                        switch (info.fontType) {
//                            case Normal:
//                                themalPrintController.setFontNormal();
//                                break;
//                            case Bold:
//                                themalPrintController.setFontBold();
//                                break;
//                        }
                        if (info.text.equals("printShortHeader")) {
                            themalPrintController.printShortHeader();
                        } else if (info.text.equals("printHeader")) {
                            themalPrintController.printHeader();
                        } else if (info.text.equals("selectPageMode")) {
                            themalPrintController.selectPageMode();
                        } else if (info.text.equals("setContractPageRegion")) {
                            themalPrintController.setContractPageRegion();
                        } else if (info.text.equals("printContractPageTitle")) {
                            themalPrintController.printContractPageTitle();
                        } else if (info.text.equals("beginContractPage")) {
                            themalPrintController.beginContractPage();
                        } else if (info.text.equals("endContractPage")) {
                            themalPrintController.endContractPage();
                        } else if (info.text.equals("selectStandardMode")) {
                            themalPrintController.selectStandardMode();
                        } else if (info.text.contains("setPageRegion")) {
                            themalPrintController.setPageRegion(info.text);
                        } else if (info.text.contains("beginPage")) {
                            themalPrintController.beginPage(info.text);
                        } else if (info.text.contains("endPage")) {
                            themalPrintController.endPage();
                        } else if (info.text.contains("printTitleBackground")) {
                            themalPrintController.printTitleBackground(info.text);
                        } else if (info.text.contains("printFrame")) {
                            themalPrintController.printFrame(info.text);
                        } else
                        if(info.text.equals("signature")){
                            themalPrintController.printSignature("");
                            //mPrinter.flush();
                            //Thread.sleep(7000);
                        } else
                        if (info.isBarcode) {
                            if(info.isBankBarcode) {
                                String[] parts = info.text.split("\\|");
                                themalPrintController.printBankBarcode(parts[0], parts[1], parts[2]);
                                //mPrinter.flush();
                                // Thread.sleep(7000);
                            } else {
                                themalPrintController.printBarCode128(info.text);
                                //mPrinter.flush();
                            }
                        } else if(info.text.equals("logoTSRTelsale")){
                            themalPrintController.printLogoTSRTelsale();
                        } else {
                            if (info.language.equals("TH")) {
                                themalPrintController.sendThaiMessage(info.text);
                            } else {
                                themalPrintController.sendEnglishMessage(info.text);
                            }
                            //mPrinter.flush();
                        }
                        //Thread.sleep(200);
                    }
                    //Thread.sleep(500);
                    themalPrintController.sendThaiMessage("");
                    //Thread.sleep(500);
                    mPrinter.feedPaper(100);
                    mPrinter.flush();
                    Thread.sleep(7000);
                }


                mBHBluetoothPrinter.mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mBHBluetoothPrinter.mActivity, "Print Complete", Toast.LENGTH_SHORT).show();
                        closeActiveConnection();
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
}
