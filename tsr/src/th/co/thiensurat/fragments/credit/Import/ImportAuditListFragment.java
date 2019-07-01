package th.co.thiensurat.fragments.credit.Import;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.R;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TransactionService;

public class ImportAuditListFragment extends BHFragment {


    @InjectView
    private DragSortListView listView;
    @InjectView
    private TextView txtCountSaleAudit;
    private DragSortController mController;
    CustomerAdapter customerAdapter;
    List<AssignInfo> saleAuditList;
    private final String TAG = "ImportAuditListFragment";

    public static class Data extends BHParcelable {
    }

    private Data inputData;


    @Override
    protected int fragmentID() {
        return R.layout.fragment_credit_import_audit_list;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_import_data, R.string.button_save, R.string.button_display_map};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        inputData = getData();

        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.credit_import_audit_list_header, listView, false);
        listView.addHeaderView(header, null, false);

        mController = buildController(listView);
        listView.setFloatViewManager(mController);
        listView.setOnTouchListener(mController);
        listView.setDragEnabled(true);
        listView.setDropListener(onDrop);
        listView.setEnableAlpha(false);

        saleAuditList = new ArrayList<AssignInfo>();
        customerAdapter = new CustomerAdapter(activity, R.layout.list_credit_import_audit, saleAuditList);
        listView.setAdapter(customerAdapter);

        getSaleAuditList();
    }

    ProgressDialog internetDialog;
    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_import_data:
                internetDialog = new ProgressDialog(activity);
                internetDialog.setIndeterminate(false);
                internetDialog.setCancelable(false);
                internetDialog.setTitle("Connecting To Internet");
                internetDialog.setMessage("Checking...");
                internetDialog.show();
                // Check Internet
                handler.postDelayed(delayCheckConnectingToInternet, 2000);
                break;
            case R.string.button_save:
                SaveSaleAuditSortOrder();
                break;
            case R.string.button_display_map:
                showNextView(new ImportAuditMapFragment());
                break;
            default:
                break;
        }
    }

    Handler handler = new Handler();

    final Runnable delayCheckConnectingToInternet = new Runnable() {
        public void run() {
            if (isConnectingToInternet()) {
                internetDialog.setTitle("Connecting To Server");
                handler.postDelayed(delayCheckConnectToServer, 3000);
            } else {
                internetDialog.dismiss();
                showWarningDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
            }
        }
    };

    final Runnable delayCheckConnectToServer = new Runnable() {
        public void run() {
            checkConnectToServer();
        }
    };

    public void getSaleAuditList() {
        new BackgroundProcess(activity) {
            @Override
            protected void before() {
                if (saleAuditList == null) {
                    saleAuditList = new ArrayList<AssignInfo>();
                }
            }

            @Override
            protected void calling() {
                saleAuditList.clear();
                List<AssignInfo> result = new AssignController().getSaleAuditByAssigneeEmpID(BHPreference.organizationCode(), BHPreference.employeeID(), AddressInfo.AddressType.AddressPayment.toString());
                if (result != null && result.size() > 0) {
                    saleAuditList.addAll(result);
                }
            }

            @Override
            protected void after() {
                txtCountSaleAudit.setText(String.format("ลูกค้าที่ต้องตรวจสอบจำนวน %d คน", saleAuditList.size()));
                customerAdapter.notifyDataSetChanged();
            }
        }.start();
    }

    public class CustomerAdapter extends BHArrayAdapter<AssignInfo> {

        public CustomerAdapter(Context context, int resource, List<AssignInfo> objects) {
            super(context, resource, objects);
        }

        private class ViewHolder {
            public TextView txtNo, txtCustomerFullNameAndCONTNO, txtPaymentPeriodNumber, txtNetAmount;
            public ImageView imgMove;
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, AssignInfo info) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.txtNo.setText(String.valueOf(position + 1));

            /*** [START] :: Fixed - [BHPROJ-0026-740] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/
//            AddressInfo addressInfo = TSRController.getAddress(info.RefNo, AddressInfo.AddressType.AddressInstall);

            SpannableString txtCustomer = new SpannableString(info.CustomerFullName + "\n" + info.CONTNO + "\n" + info.getAddress().Address() + "\nTel. " + info.getAddress().Telephone());
            txtCustomer.setSpan(new ForegroundColorSpan(Color.BLACK), 0, info.CustomerFullName.length() + info.CONTNO.length(), 0);
            txtCustomer.setSpan(new ForegroundColorSpan(Color.GRAY), info.CustomerFullName.length() + info.CONTNO.length() + 1, txtCustomer.length(), 0);
            viewHolder.txtCustomerFullNameAndCONTNO.setText(txtCustomer, TextView.BufferType.SPANNABLE);

            //viewHolder.txtCustomerFullNameAndCONTNO.setText(info.CustomerFullName + "\n" + info.CONTNO);
            /*** [END] :: Fixed - [BHPROJ-0026-740] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/

            viewHolder.txtPaymentPeriodNumber.setText(String.valueOf(info.PaymentPeriodNumber));
            viewHolder.txtNetAmount.setText(BHUtilities.numericFormat(Double.valueOf(info.NetAmount)));
        }
    }

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                AssignInfo itemFrom = saleAuditList.get(from);
                customerAdapter.remove(itemFrom);
                customerAdapter.insert(itemFrom, to);
            }
        }
    };

    public void SaveSaleAuditSortOrder() {
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                if (saleAuditList != null && saleAuditList.size() > 0) {
                    int i = 1;
                    for (AssignInfo assign : saleAuditList) {
                        assign.Order = i;
//                        assign.OrderExpect = i;
                        assign.LastUpdateBy = BHPreference.employeeID();
                        assign.LastUpdateDate = new Date();
                        TSRController.updateAssign(assign, true);
                        i++;
                    }
                }
            }
        }.start();
    }

    public DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.imgMove);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
        controller.setBackgroundColor(getResources().getColor(R.color.bg_list_view_selected));
        return controller;
    }

    public void startSynchronize() {
        TransactionService.stopService(activity);
        SynchronizeReceiver synchronizeReceiver = new SynchronizeReceiver();
        synchronizeReceiver.getInstance().start();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
//        request.master.syncSaleAuditDataRelated = true;
        request.master.syncFullRelated = true;

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);

    }

    private class SynchronizeReceiver extends BroadcastReceiver implements MainActivity.IApiAccessResponse {
        private SynchronizeReceiver instance;
        private SynchronizeService.SynchronizeResult result;
        private boolean isProcessing;
        private ProgressDialog dialog;

        private SynchronizeReceiver() {
            dialog = new ProgressDialog(activity);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setTitle("");
            dialog.setMessage("");

            result = null;
            isProcessing = false;
        }

        public SynchronizeReceiver getInstance() {
            if (instance == null) {
                instance = new SynchronizeReceiver();
                LocalBroadcastManager.getInstance(activity).registerReceiver(instance, new IntentFilter(SynchronizeService.SYNCHRONIZE_BROADCAST_ACTION));
            }
            return instance;
        }

        public void show() {
            if (isProcessing && !dialog.isShowing()) {
                dialog.show();
            }
        }

        private void start() {
            if (!isProcessing) {
                isProcessing = true;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        }

        private void stop() {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(this);
            isProcessing = false;
            dialog = null;
            result = null;
            instance = null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                result = intent.getParcelableExtra(SynchronizeService.SYNCHRONIZE_RESULT_DATA_KEY);
                dialog.setTitle(result.title);
                dialog.setMessage(result.message);
                dialog.setProgress(result.progress);

                if (result.progress >= 100) {
                    dialog.setProgress(100);
                }

                if (result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED || result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                    dialog.dismiss();
                    if (result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                        showWarningDialog(result.error);
                    } else {
                        if(result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED) {
                            // Synchronize syncSaleAuditDataRelated Pass
                            Log.e(TAG, "Synchronize syncSaleAuditDataRelated Pass");

                            final MainActivity.DownloadTask downloadTask = new MainActivity.DownloadTask(activity);
                            String URL = String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip");
                            downloadTask.delegate = this;
                            downloadTask.execute(URL);

                            downloadTask.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        }
                    }
                    stop();
                }
            }
        }

        @Override
        public void postResult(String asyncresult) {
            getSaleAuditList();
        }
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private int timeOut = 20 * 1000;
    public void checkConnectToServer(){
        new BackgroundProcess(activity){
            private boolean isConnected;
            @Override
            protected void calling() {
                HttpGet httpGet = new HttpGet(BHPreference.TSR_SERVICE_URL);
                HttpClient client = new DefaultHttpClient();
                //TimeOut 20s
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params, timeOut);
                HttpConnectionParams.setSoTimeout(params, timeOut);

                HttpResponse response = null;
                try {
                    response = client.execute(httpGet);
                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == 200) {
                        isConnected = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void after() {
                if(internetDialog != null){
                    internetDialog.dismiss();
                }
                if(isConnected){
                    startSynchronize();
                }else{
                    showWarningDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้");
                }
            }
        }.start(false);
    }

    public ProgressDialog internetDialog() {
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setTitle("Connecting To Internet");
        dialog.setMessage("Checking...");
        return dialog;
    }
}
