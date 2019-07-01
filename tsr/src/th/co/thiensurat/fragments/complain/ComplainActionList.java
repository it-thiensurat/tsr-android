package th.co.thiensurat.fragments.complain;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.ComplainController;
import th.co.thiensurat.data.info.ComplainInfo;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TransactionService;

public class ComplainActionList extends BHFragment {

    public static final String COMPLAIN_ACTION_LIST = "COMPLAIN_ACTION_LIST";

    @InjectView
    private ListView lvCustomerList;
    @InjectView
    private Button btnSearch;
    @InjectView
    private EditText etSearch;

    private ComplainAdapter complainAdapter;

    private List<ComplainInfo> complainList;

    @Override
    protected int titleID() {
        return R.string.title_complain;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_complain_action_list;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_update_list, R.string.button_next};
    }

    @Override
    public String fragmentTag() {
        return COMPLAIN_ACTION_LIST;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindComplainList("%" + etSearch.getText().toString() + "%", true);
            }

        });

        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.complain_action_list_head, lvCustomerList, false);
        lvCustomerList.addHeaderView(header, null, false);

        if (complainList == null) {
            complainList = new ArrayList<ComplainInfo>();
        } else {
            complainList.clear();
        }
        complainAdapter = new ComplainAdapter(activity, R.layout.list_complain_action, complainList);
        lvCustomerList.setAdapter(complainAdapter);
        lvCustomerList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lvCustomerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvCustomerList.setItemChecked(position, true);
            }
        });

        bindComplainList("", false);
    }

    private void bindComplainList(final String searchText, final boolean isSearch) {
        (new BackgroundProcess(activity) {
            String employeeID = BHPreference.employeeID();

            @Override
            protected void before() {
                //if(BHPreference.PositionCode().contains(PositionController.PositionCode.SaleLeader.toString())){
                //    employeeID = null;
                //}

                if (complainList == null) {
                    complainList = new ArrayList<ComplainInfo>();
                } else {
                    complainList.clear();
                }
            }

            @Override
            protected void calling() {
                List<ComplainInfo> list = new ComplainController().getComplainListByTeamCodeAndEmployeeID(BHPreference.organizationCode(), BHPreference.teamCode()
                        , employeeID, searchText, null, new String[] {ComplainController.ComplainStatus.REQUEST.toString(), ComplainController.ComplainStatus.APPROVED.toString()});

                if (list != null && list.size() > 0) {
                    complainList.addAll(list);
                }
            }

            @Override
            protected void after() {
                if (complainAdapter != null) {
                    lvCustomerList.clearChoices();
                    complainAdapter.notifyDataSetChanged();
                }

                if (complainList != null && complainList.size() == 0) {
                    BHUtilities.alertDialog(activity, "ผลการค้นหา", "ไม่พบข้อมูลรายการที่จะแก้ไขปัญหา").show();
                }
            }
        }).start();
    }

    public class ComplainAdapter extends BHArrayAdapter<ComplainInfo> {
        class ViewHolder {
            public TextView txtRequestDate, txtComplainPaperIDAndCustomerName, txtStatus;
        }

        public ComplainAdapter(Context context, int resource, List<ComplainInfo> objects) {
            super(context, resource, objects);
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, ComplainInfo info) {
            ViewHolder vh = (ViewHolder) holder;
            vh.txtRequestDate.setText(BHUtilities.dateFormat(info.RequestDate));
            vh.txtComplainPaperIDAndCustomerName.setText(String.format("%s\n%s", info.ComplainPaperID, info.CustomerFullName));
            vh.txtStatus.setText(info.Status.equals(ComplainController.ComplainStatus.REQUEST.toString()) ? "N" : "Y");
            vh.txtComplainPaperIDAndCustomerName.setTextColor(getResources().getColor(info.Status.equals(ComplainController.ComplainStatus.APPROVED.toString()) && info.AssigneeEmpID != null && info.AssigneeEmpID.equals(BHPreference.employeeID()) ? android.R.color.black : R.color.dot_gray_dark));
        }
    }

    private void next(final ComplainInfo selectedComplain) {
        ComplainActionDetailFragment.Data data = new ComplainActionDetailFragment.Data();
        data.ComplainID = selectedComplain.ComplainID;
        ComplainActionDetailFragment fragment = BHFragment.newInstance(ComplainActionDetailFragment.class, data);
        showNextView(fragment);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_next:
                final ComplainInfo selectedComplain = lvCustomerList.getCheckedItemPosition() != -1 ? complainList.get(lvCustomerList.getCheckedItemPosition() - 1) : null;
                if (selectedComplain == null) {
                    BHUtilities.alertDialog(activity, "เลือกรายการ", "กรุณาเลือกรายการแจ้งปัญหาที่จะทำการแก้ไขปัญหา").show();
                } else {
                    next(selectedComplain);
                }
                break;
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_update_list:
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

    ProgressDialog internetDialog;

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
        request.master.syncRequestComplainRelated = true;

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
                        // Synchronize syncEditContractRelated Pass
                        Log.e("ComplainActionList", "Synchronize syncEditContractRelated Pass");
                        bindComplainList("", false);
                    }
                    stop();
                }
            }
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
}
