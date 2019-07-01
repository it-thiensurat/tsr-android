package th.co.thiensurat.fragments.credit.Spare;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.SpareDrawdownController;
import th.co.thiensurat.data.info.SpareDrawdownInfo;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TransactionService;

public class ImportDataSpareListFragment extends BHFragment {

    @InjectView
    private ListView lvSpareDrawdownList;

    private List<SpareDrawdownInfo> spareDrawdownInfoList;

    SpareDrawdownAdapter spareDrawdownAdapter;

    ProgressDialog internetDialog;
    Handler handler = new Handler();
    private int timeOut = 20 * 1000;


    @Override
    protected int fragmentID() {
        return R.layout.fragment_import_data_spare_list;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_import_data};
    }

    @Override
    protected int titleID() {
        return R.string.title_import_data_spare;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        binHeader();
        spareDrawdownInfoList = new SpareDrawdownController().getSpareDrawdownByEmployeeIdAndStatusInRequestAndApproved(BHPreference.organizationCode(), BHPreference.employeeID());

        if (spareDrawdownInfoList != null && spareDrawdownInfoList.size() > 0) {
            binSpareDrawdownAdapter();
        } else {
            lvSpareDrawdownList.setVisibility(View.GONE);
            showWarningDialog("แจ้งเตือน", "ไม่พอข้อมูลการขอเบิกเครื่อง/อะไหล่");
        }
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_import_data:
                internetDialog = new ProgressDialog(activity);
                internetDialog.setIndeterminate(false);
                internetDialog.setCancelable(false);
                internetDialog.setTitle("Connecting To Internet");
                internetDialog.setMessage("Checking...");
                internetDialog.show();
                // Check Internet
                handler.postDelayed(delayCheckConnectingToInternet, 3000);

                break;
            default:
                break;
        }
    }

    private void binHeader() {
        LayoutInflater li = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) li.inflate(R.layout.import_data_spare_list_header, lvSpareDrawdownList, false);
        lvSpareDrawdownList.addHeaderView(header, null, false);
    }

    private void binSpareDrawdownAdapter() {
        spareDrawdownAdapter = new SpareDrawdownAdapter(activity, R.layout.import_data_spare_list_item, spareDrawdownInfoList);
        lvSpareDrawdownList.setAdapter(spareDrawdownAdapter);


        lvSpareDrawdownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SpareDrawdownDetailFragment.Data data1 = new SpareDrawdownDetailFragment.Data();
                data1.spareDrawdownInfo = spareDrawdownInfoList.get(i-1);

                SpareDrawdownDetailFragment fm = BHFragment.newInstance(SpareDrawdownDetailFragment.class, data1);
                showNextView(fm);
            }
        });

        lvSpareDrawdownList.setVisibility(View.VISIBLE);
    }



    public class SpareDrawdownAdapter extends BHArrayAdapter<SpareDrawdownInfo> {

        public SpareDrawdownAdapter(Context context, int resource, List<SpareDrawdownInfo> objects) {
            super(context, resource, objects);
        }

        private class ViewHolder {
            public TextView txtDocumentNo, txtEffDate,  txtTime, txtAuthorization;
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, SpareDrawdownInfo info) {
            final ViewHolder vh = (ViewHolder) holder;

            String strStatus = "";
            switch (Enum.valueOf(SpareDrawdownInfo.SpareDrawdownStatus.class, info.Status)){
                case REQUEST:
                    strStatus = "N";
                    break;
                case APPROVED:
                    strStatus = "Y";
                    break;
            }

            vh.txtDocumentNo.setText(info.SpareDrawdownPaperID != null ? info.SpareDrawdownPaperID : "");
            vh.txtEffDate.setText(BHUtilities.dateFormat(info.RequestDate, "dd/MM/yy"));
            vh.txtTime.setText(BHUtilities.dateFormat(info.RequestDate, "HH:mm"));
            vh.txtAuthorization.setText(strStatus);
        }
    }


    //region Button นำเข้าข้อมูล
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

    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

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

    public void startSynchronize() {
        TransactionService.stopService(activity);
        SynchronizeReceiver synchronizeReceiver = new SynchronizeReceiver();
        synchronizeReceiver.getInstance().start();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
        request.master.syncSpareDrawdownRelated = true;

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);

    }

    private class SynchronizeReceiver extends BroadcastReceiver {
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
                        spareDrawdownInfoList = new SpareDrawdownController().getSpareDrawdownByEmployeeIdAndStatusInRequestAndApproved(BHPreference.organizationCode(), BHPreference.employeeID());
                        if (spareDrawdownInfoList != null && spareDrawdownInfoList.size() > 0) {
                            binSpareDrawdownAdapter();
                        } else {
                            lvSpareDrawdownList.setVisibility(View.GONE);
                            showWarningDialog("แจ้งเตือน", "ไม่พอข้อมูลการขอเบิกเครื่อง/อะไหล่่");
                        }
                    }
                    stop();
                }
            }
        }
    }
    //endregion
}
