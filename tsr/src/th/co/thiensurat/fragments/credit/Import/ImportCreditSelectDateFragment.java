package th.co.thiensurat.fragments.credit.Import;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.controller.LimitController;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.LimitInfo;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TransactionService;

public class ImportCreditSelectDateFragment extends BHFragment {
    @InjectView
    private TextView txtCountCredit;
    @InjectView
    ListView listView;
    private PaymentAppointmentDateAdapter paymentAppointmentDateAdapter;
    private List<AssignInfo> paymentAppointmentDateList;

    private final String TAG = "ImportCreditSelectDateFragment";

    @Override
    protected int fragmentID() {
        return R.layout.fragment_credit_import_credit_select_date;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_import_data};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        paymentAppointmentDateList = new ArrayList<AssignInfo>();
        paymentAppointmentDateAdapter = new PaymentAppointmentDateAdapter(activity, R.layout.list_credit_import_credit_select_date, paymentAppointmentDateList);
        listView.setAdapter(paymentAppointmentDateAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImportCreditListFragment.Data input = new ImportCreditListFragment.Data();
                input.selectedDate = paymentAppointmentDateList.get(position).PaymentAppointmentDate;
                ImportCreditListFragment fragment = BHFragment.newInstance(ImportCreditListFragment.class, input);
                showNextView(fragment);
            }
        });
        getPaymentAppointmentDateList();
    }

    public class PaymentAppointmentDateAdapter extends BHArrayAdapter<AssignInfo> {

        public PaymentAppointmentDateAdapter(Context context, int resource, List<AssignInfo> objects) {
            super(context, resource, objects);
        }

        private class ViewHolder {
            public TextView txtPaymentAppointmentDate, txtCountCredit;
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, AssignInfo info) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.txtPaymentAppointmentDate.setText(BHUtilities.dateFormat(info.PaymentAppointmentDate));
            viewHolder.txtCountCredit.setText("จำนวน " + info.CountCreditGroupByPaymentAppointmentDate + " คน");
        }
    }

    public void getPaymentAppointmentDateList() {
        new BackgroundProcess(activity) {
            @Override
            protected void before() {
                if (paymentAppointmentDateList == null) {
                    paymentAppointmentDateList = new ArrayList<AssignInfo>();
                }
            }

            @Override
            protected void calling() {
                paymentAppointmentDateList.clear();
                List<AssignInfo> result = new AssignController().getAssignSalePaymentPeriodGroupByPaymentAppointmentDate(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());
                if (result != null && result.size() > 0) {
                    paymentAppointmentDateList.addAll(result);
                }else{
                    LimitInfo limit = new LimitController().getLimitByLimitTypeAndEmployee(LimitController.LimitType.ImportCreditDay.toString(), BHPreference.employeeID());
                    Date date = new Date();
                    ArrayList<String> dateList = new ArrayList<String>();
                    for (int i = 0; i < limit.LimitMax; i++) {
                        Date datePlus = BHUtilities.addDay(date, i);
                        dateList.add(BHUtilities.dateFormat(datePlus, "yyyy-MM-dd", BHUtilities.LOCALE_EN));
                    }
                    result = new AssignController().getSalePaymentPeriodGroupByPaymentAppointmentDate(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID(), dateList);
                    if (result != null && result.size() > 0) {
                        paymentAppointmentDateList.addAll(result);
                    }
                }
            }

            @Override
            protected void after() {
                int sum = 0;
                for (AssignInfo paymentAppointmentDate : paymentAppointmentDateList) {
                    sum += paymentAppointmentDate.CountCreditGroupByPaymentAppointmentDate;
                }
                txtCountCredit.setText(String.format("ลูกค้าที่ต้องเก็บเงินจำนวน %d คน", sum));
                paymentAppointmentDateAdapter.notifyDataSetChanged();
            }
        }.start();
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

    public void startSynchronize() {
        TransactionService.stopService(activity);
        SynchronizeReceiver synchronizeReceiver = new SynchronizeReceiver();
        synchronizeReceiver.getInstance().start();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
//        request.master.syncCreditDataRelated = true;
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
                            Log.e(TAG, "Synchronize syncCreditDataRelated Pass");
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
            getPaymentAppointmentDateList();
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

    public void checkConnectToServer() {
        new BackgroundProcess(activity) {
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
                if (internetDialog != null) {
                    internetDialog.dismiss();
                }
                if (isConnected) {
                    startSynchronize();
                } else {
                    showWarningDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได");
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
