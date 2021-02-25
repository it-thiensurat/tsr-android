package th.co.thiensurat.fragments.synchronize;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.info.DeviceMenuInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.SynchronizeService.SynchronizeData;
import th.co.thiensurat.service.SynchronizeService.SynchronizeMaster;
import th.co.thiensurat.service.SynchronizeService.SynchronizeResult;
import th.co.thiensurat.service.TransactionService;
import th.co.thiensurat.service.data.GetCurrentFortnightInputInfo;
import th.co.thiensurat.service.data.GetCurrentFortnightOutputInfo;
import th.co.thiensurat.service.data.GetDeviceMenusInputInfo;
import th.co.thiensurat.service.data.GetDeviceMenusOutputInfo;
import th.co.thiensurat.service.data.GetUserByUserNameInputInfo;
import th.co.thiensurat.service.data.GetUserByUserNameOutputInfo;



public class SynchronizeMainFragment extends BHFragment {

    @InjectView
    private PinnedSectionListView lvMasterTable;
    @InjectView
    private PinnedSectionListView lvTransactionTable;

    public static int timeOut = 20000;//20s

    public static int check_update_database=0;

    private static class Data extends BHParcelable {
        public List<Integer> masterList;
        public List<Integer> transactionList;

        public List<Integer> masterSelected;
        public List<Integer> transactionSelected;
    }

    private static Data data;
    private BHListViewAdapter masterAdapter;
    private BHListViewAdapter transactionAdapter;

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_synchronize_main;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_synchronize_database};
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_synchronize;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //showWarningDialog("แจ้งเตือนการเข้าระบบ", "System for " + BHPreference.sourceSystem().toString() + " With CashCode " + BHPreference.cashCode() + " " + BHPreference.departmentCode() + " " + BHPreference.subDepartmentCode() + " " + BHPreference.supervisorCode());

        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelable(BH_FRAGMENT_DEFAULT_DATA);
        }

        if (data == null) {
            getDataList();
        }

        lvMasterTable.setOnItemClickListener(null);
//        lvMasterTable.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // TODO Auto-generated method stub
//
//                if (!MainActivity.checkLogin) {
//                    AlertDialog.Builder setupAlert;
//                    setupAlert = new AlertDialog.Builder(activity)
//                            .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
//                            .setMessage("กรุณาปรับปรุงข้อมูลทั้งหมดก่อนใช้ระบบ")
//                            .setCancelable(false)
//                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    dialog.cancel();
//                                }
//                            });
//                    setupAlert.show();
//                } else {
//                    BHListViewItem item = masterAdapter.getItem(position);
//                    if (item.type == RowType.Header) {
//                        if (data.masterList.size() != data.masterSelected.size()) {
//                            data.masterSelected = new ArrayList<Integer>(data.masterList);
//                        } else {
//                            data.masterSelected = new ArrayList<Integer>();
//                        }
//                    } else if (item.type == RowType.Item) {
//                        Integer value = data.masterList.get(item.row);
//                        if (data.masterSelected.contains(value)) {
//                            data.masterSelected.remove(value);
//                        } else {
//                            data.masterSelected.add(value);
//                        }
//                    }
//
//                    masterAdapter.notifyDataSetChanged();
//                }
//            }
//        });

        lvTransactionTable.setOnItemClickListener(null);
//        lvTransactionTable.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // TODO Auto-generated method stub
//
//                if (!MainActivity.checkLogin) {
//                    AlertDialog.Builder setupAlert;
//                    setupAlert = new AlertDialog.Builder(activity)
//                            .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
//                            .setMessage("กรุณาปรับปรุงข้อมูลทั้งหมดก่อนใช้ระบบ")
//                            .setCancelable(false)
//                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    dialog.cancel();
//                                }
//                            });
//                    setupAlert.show();
//                } else {
//                    BHListViewItem item = transactionAdapter.getItem(position);
//                    if (item.type == RowType.Header) {
//                        if (data.transactionList.size() != data.transactionSelected.size()) {
//                            data.transactionSelected = new ArrayList<Integer>(data.transactionList);
//                        } else {
//                            data.transactionSelected = new ArrayList<Integer>();
//                        }
//                    } else if (item.type == RowType.Item) {
//                        Integer value = data.transactionList.get(item.row);
//                        if (data.transactionSelected.contains(value)) {
//                            data.transactionSelected.remove(value);
//                        } else {
//                            data.transactionSelected.add(value);
//                        }
//                    }
//
//                    transactionAdapter.notifyDataSetChanged();
//
//                }
//            }
//        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        bindListView();
        SynchronizeReceiver.getInstance().show();
        // showSynchronize();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        outState.putParcelable(BH_FRAGMENT_DEFAULT_DATA, data);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_synchronize_database:
                // YIM change flow full-sync
                 check_update_database=0;
                startSynchronize();
                //updateUserInfo(BHPreference.userID());
                // test();
                break;

            default:
                break;
        }
    }

    private void test() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                EmployeeInfo trans = new EmployeeController().getEmployeeByID(BHPreference.employeeID());
                log(trans.EmpID + "");
                // TransactionService.synchronizeData();
            }
        }).start();
    }


    public interface updateUserInfoAsyncResponse {
        void updateUserInfoProcessFinish();
    }

    // YIM update userInfo before full-sync data
    public void updateUserInfo(final String userName, final updateUserInfoAsyncResponse finish) {
        if (isConnectingToInternet()) {
            (new AsyncTask<String, Void, Boolean>() {

                @Override
                protected void onPreExecute() {
                    BHLoading.show(activity);
                }

                @Override
                protected Boolean doInBackground(String... urls) {

                    boolean result = false;
                    try {
                        HttpGet httpGet = new HttpGet(urls[0]);
                        HttpClient client = new DefaultHttpClient();
                        //TimeOut 20s
                        HttpParams params = client.getParams();
                        HttpConnectionParams.setConnectionTimeout(params, timeOut);
                        HttpConnectionParams.setSoTimeout(params, timeOut);

                        HttpResponse response = client.execute(httpGet);

                        int statusCode = response.getStatusLine().getStatusCode();

                        if (statusCode == 200) {
                            result = true;
                        }

                    } catch (ClientProtocolException e) {

                    } catch (IOException e) {

                    }

                    return result;
                }

                protected void onPostExecute(Boolean result) {
                    if (!result) {
                        showWarningDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้");
                        BHLoading.close();
                    } else {

                        (new BackgroundProcess(activity) {

                            private GetUserByUserNameInputInfo input;
                            private GetUserByUserNameOutputInfo result = null;

                            private GetCurrentFortnightOutputInfo outputGetCurrentFortnight = null;
                            private GetDeviceMenusOutputInfo menus = null;

                            @Override
                            protected void before() {
                                // TODO Auto-generated method stub

                               input = new GetUserByUserNameInputInfo();

                                //Log.e("moo_SourceSystem",SourceSystem);
                                input.UserName = userName;
                            }

                            @Override
                            protected void calling() {
                                // TODO Auto-generated method stub]
                                result = TSRController.getUserByUserName(input);

/*
                                try {
                                    String DD= BHApplication.getInstance().getPrefManager().getPreferrence("select_p");
                                    if(DD.equals("Credit")) {
                                        Log.e("rrrr","1");
                                       result = TSRController.getUserByUserName2(input);

                                    }
                                    else if(DD.equals("Sale")) {
                                        Log.e("rrrr","2");
                                        result = TSRController.getUserByUserName3(input);

                                    }
                                    else {
                                        Log.e("rrrr","3");
                                        result = TSRController.getUserByUserName(input);
                                    }


                                }
                                catch (Exception ex){
                                    Log.e("rrrr","4");
                                    result = TSRController.getUserByUserName(input);

                                }*/





                                // เพิ่มใหม่
                                if (result.ResultCode == 0) {
                                    GetCurrentFortnightInputInfo inputGetCurrentFortnight = new GetCurrentFortnightInputInfo();
                                    inputGetCurrentFortnight.OrganizationCode = result.Info.OrganizationCode;
                                    inputGetCurrentFortnight.ProcessType = ((result.Info.ProcessType == null) || (result.Info.ProcessType == "")) ? "Sale" : result.Info.ProcessType;         // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
                                    outputGetCurrentFortnight = TSRController.getCurrentFortnight(inputGetCurrentFortnight);

                                    GetDeviceMenusInputInfo deviceMenuInput = new GetDeviceMenusInputInfo();
                                   // deviceMenuInput.EmployeeCode = result.Info.EmpID;
                                    deviceMenuInput.EmployeeCode = result.Info.EmpID+"_"+result.Info.SourceSystem;
                                  //  deviceMenuInput.position = result.Info.SourceSystem;
                                    menus = TSRController.getDeviceMenus(deviceMenuInput);
                                }
                            }

                            @Override
                            protected void after() {
                                // TODO Auto-generated method stub
                                if (result.ResultCode == 0) {

                                    if (menus != null) {
                                        if(BHPreference.IsAdmin()) {
                                            menus.Info.add(getDeviceMenuAdmin());
                                        }
                                        BHPreference.setUserMenus(menus.Info);

                                        activity.menusInfo.clear();
                                        activity.menusInfo.addAll(BHPreference.getUserMenus());

                                        activity.mainMenuAdapter.refresh();
                                        activity.mainMenuAdapter.notifyDataSetChanged();
                                    }

                                    if (result.Info != null) {
                                        BHPreference.initPreference(result.Info, outputGetCurrentFortnight);
                                        finish.updateUserInfoProcessFinish();
                                        //startSynchronize();
                                    }
                                }
                            }
                        }).start();
                    }
                }
            }).execute(BHPreference.TSR_SERVICE_URL);
        } else {
            showWarningDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
        }
    }





    public void updateUserInfo2(final String userName,String SourceSystem, final updateUserInfoAsyncResponse finish) {
        if (isConnectingToInternet()) {
            (new AsyncTask<String, Void, Boolean>() {

                @Override
                protected void onPreExecute() {
                    BHLoading.show(activity);
                }

                @Override
                protected Boolean doInBackground(String... urls) {

                    boolean result = false;
                    try {
                        HttpGet httpGet = new HttpGet(urls[0]);
                        HttpClient client = new DefaultHttpClient();
                        //TimeOut 20s
                        HttpParams params = client.getParams();
                        HttpConnectionParams.setConnectionTimeout(params, timeOut);
                        HttpConnectionParams.setSoTimeout(params, timeOut);

                        HttpResponse response = client.execute(httpGet);

                        int statusCode = response.getStatusLine().getStatusCode();

                        if (statusCode == 200) {
                            result = true;
                        }

                    } catch (ClientProtocolException e) {

                    } catch (IOException e) {

                    }

                    return result;
                }

                protected void onPostExecute(Boolean result) {
                    if (!result) {
                        showWarningDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้");
                        BHLoading.close();
                    } else {

                        (new BackgroundProcess(activity) {

                            private GetUserByUserNameInputInfo input;
                            private GetUserByUserNameOutputInfo result = null;

                            private GetCurrentFortnightOutputInfo outputGetCurrentFortnight = null;
                            private GetDeviceMenusOutputInfo menus = null;

                            @Override
                            protected void before() {
                                // TODO Auto-generated method stub

                                input = new GetUserByUserNameInputInfo();

                                //Log.e("moo_SourceSystem",SourceSystem);
                                input.UserName = userName;
                            }

                            @Override
                            protected void calling() {
                                // TODO Auto-generated method stub]
                                result = TSRController.getUserByUserName(input);

/*
                                try {
                                    String DD= BHApplication.getInstance().getPrefManager().getPreferrence("select_p");
                                    if(DD.equals("Credit")) {
                                        Log.e("rrrr","1");
                                       result = TSRController.getUserByUserName2(input);

                                    }
                                    else if(DD.equals("Sale")) {
                                        Log.e("rrrr","2");
                                        result = TSRController.getUserByUserName3(input);

                                    }
                                    else {
                                        Log.e("rrrr","3");
                                        result = TSRController.getUserByUserName(input);
                                    }


                                }
                                catch (Exception ex){
                                    Log.e("rrrr","4");
                                    result = TSRController.getUserByUserName(input);

                                }*/





                                // เพิ่มใหม่
                                if (result.ResultCode == 0) {
                                    GetCurrentFortnightInputInfo inputGetCurrentFortnight = new GetCurrentFortnightInputInfo();
                                    inputGetCurrentFortnight.OrganizationCode = result.Info.OrganizationCode;
                                    inputGetCurrentFortnight.ProcessType = ((result.Info.ProcessType == null) || (result.Info.ProcessType == "")) ? "Sale" : result.Info.ProcessType;         // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
                                    outputGetCurrentFortnight = TSRController.getCurrentFortnight(inputGetCurrentFortnight);

                                    GetDeviceMenusInputInfo deviceMenuInput = new GetDeviceMenusInputInfo();
                                    // deviceMenuInput.EmployeeCode = result.Info.EmpID;
                                    deviceMenuInput.EmployeeCode = result.Info.EmpID+"_"+SourceSystem;
                                    //  deviceMenuInput.position = result.Info.SourceSystem;
                                    menus = TSRController.getDeviceMenus(deviceMenuInput);
                                }
                            }

                            @Override
                            protected void after() {
                                // TODO Auto-generated method stub
                                if (result.ResultCode == 0) {

                                    if (menus != null) {
                                        if(BHPreference.IsAdmin()) {
                                            menus.Info.add(getDeviceMenuAdmin());
                                        }
                                        BHPreference.setUserMenus(menus.Info);

                                        activity.menusInfo.clear();
                                        activity.menusInfo.addAll(BHPreference.getUserMenus());

                                        activity.mainMenuAdapter.refresh();
                                        activity.mainMenuAdapter.notifyDataSetChanged();
                                    }

                                    if (result.Info != null) {
                                       // BHPreference.initPreference(result.Info, outputGetCurrentFortnight);
                                        finish.updateUserInfoProcessFinish();

                                        //startSynchronize();
                                    }
                                }
                            }
                        }).start();
                    }
                }
            }).execute(BHPreference.TSR_SERVICE_URL);
        } else {
            showWarningDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
        }
    }


    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public DeviceMenuInfo getDeviceMenuAdmin(){
        DeviceMenuInfo info = new DeviceMenuInfo();
        info.MenuID = activity.getString(R.string.main_menu_admin);
        info.MenuName = activity.getString(R.string.main_menu_admin);
        info.MenuIndex = 99;

        return info;
    }

    public void startSynchronize() {
        TransactionService.stopService(activity);
        SynchronizeReceiver.getInstance().start();
        //new BaseController().removeDatabase();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeData request = new SynchronizeData();
        request.master = new SynchronizeMaster();
        request.master.syncFullRelated = true;
//        request.master.syncTeamRelated = data.masterSelected.contains(R.string.sync_team_related);
//        request.master.syncProductRelated = data.masterSelected.contains(R.string.sync_product_related);
//        request.master.syncCustomerRelated = data.masterSelected.contains(R.string.sync_customer_related);
//        request.master.syncPaymentRelated = data.masterSelected.contains(R.string.sync_payment_related);
//        request.master.syncContractRelated = data.masterSelected.contains(R.string.sync_contract_related);
//        request.master.syncEditContractRelated = data.masterSelected.contains(R.string.sync_contract_related);
//        request.master.syncSendMoneyRelated = data.masterSelected.contains(R.string.sync_sendmoney_related);
//        request.master.syncMasterDataRelated = data.masterSelected.contains(R.string.sync_masterdata_related);

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);
    }



    private void getDataList() {
        data = new Data();
        data.masterList = new ArrayList<Integer>();
        data.transactionList = new ArrayList<Integer>();

        TypedArray ar = activity.getResources().obtainTypedArray(R.array.synchronize_master_table);
        int len = ar.length();
        for (int ii = 0; ii < len; ii++) {
            data.masterList.add(Integer.valueOf(ar.getResourceId(ii, 0)));
        }
        ar.recycle();

        for (int ii = 0; ii < 15; ii++) {
            data.transactionList.add(Integer.valueOf(ii + 1));
        }

        data.masterSelected = new ArrayList<Integer>(data.masterList);
        data.transactionSelected = new ArrayList<Integer>(data.transactionList);
    }

    private void bindListView() {
        masterAdapter = new BHListViewAdapter(activity) {

            class ViewHolder {
                //-- Fixed - [BHPROJ-0026-951] :: Remove check control in Full-Synch Screen
//                public CheckedTextView text1;
                public TextView text1;
            }

            @Override
            protected boolean canHeaderSelected(int section) {
                return true;
            }

            ;

            @Override
            protected int viewForItem(int section, int row) {
                // TODO Auto-generated method stub
                //-- Fixed - [BHPROJ-0026-951] :: Remove check control in Full-Synch Screen
//                return android.R.layout.simple_list_item_checked;
                return android.R.layout.simple_list_item_1;
            }

            @Override
            protected int viewForSectionHeader(int section) {
                //-- Fixed - [BHPROJ-0026-951] :: Remove check control in Full-Synch Screen
//                return android.R.layout.simple_list_item_checked;
                return android.R.layout.simple_list_item_1;
            }

            ;

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                // TODO Auto-generated method stub
                ViewHolder vh = (ViewHolder) holder;
                Integer value = data.masterList.get(row);



                    vh.text1.setText(getResources().getString(value.intValue()));


                //-- Fixed - [BHPROJ-0026-951] :: Remove check control in Full-Synch Screen
//                vh.text1.setChecked(data.masterSelected.contains(value));
            }

            @Override
            protected void onViewSectionHeader(View view, Object holder, int section) {
                view.setBackgroundResource(R.color.bg_table_main_header);

                ViewHolder vh = (ViewHolder) holder;



                    vh.text1.setText("MASTER TABLE");


                //-- Fixed - [BHPROJ-0026-951] :: Remove check control in Full-Synch Screen
//                vh.text1.setChecked(data.masterSelected.size() == data.masterList.size());
            }

            ;

            @Override
            protected int getItemCount(int section) {
                // TODO Auto-generated method stub
                return data.masterList.size();
            }
        };
        lvMasterTable.setAdapter(masterAdapter);

        transactionAdapter = new BHListViewAdapter(activity) {

            class ViewHolder {
                //-- Fixed - [BHPROJ-0026-951] :: Remove check control in Full-Synch Screen
//                public CheckedTextView text1;
                public TextView text1;
            }

            @Override
            protected boolean canHeaderSelected(int section) {
                return true;
            }

            ;

            @Override
            protected int viewForItem(int section, int row) {
                // TODO Auto-generated method stub
                return android.R.layout.simple_list_item_checked;
            }

            @Override
            protected int viewForSectionHeader(int section) {
                return android.R.layout.simple_list_item_checked;
            }

            ;

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {
                // TODO Auto-generated method stub
                ViewHolder vh = (ViewHolder) holder;
                Integer value = data.transactionList.get(row);
                vh.text1.setText(value.toString());
                //-- Fixed - [BHPROJ-0026-951] :: Remove check control in Full-Synch Screen
//                vh.text1.setChecked(data.transactionSelected.contains(value));
            }

            @Override
            protected void onViewSectionHeader(View view, Object holder, int section) {
                view.setBackgroundResource(R.color.bg_table_main_header);

                ViewHolder vh = (ViewHolder) holder;
                vh.text1.setText("TRANSACTION TABLE");
                //-- Fixed - [BHPROJ-0026-951] :: Remove check control in Full-Synch Screen
//                vh.text1.setChecked(data.transactionSelected.size() == data.transactionList.size());
            }

            ;

            @Override
            protected int getItemCount(int section) {
                // TODO Auto-generated method stub
                return data.transactionList.size();
            }
        };
        lvTransactionTable.setAdapter(transactionAdapter);
    }

    private static class SynchronizeReceiver extends BroadcastReceiver {
        private static SynchronizeReceiver instance;

        private ProgressDialog dialog;
        private SynchronizeResult result;
        private boolean isProcessing;

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

        public static SynchronizeReceiver getInstance() {
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
            // TODO Auto-generated method stub

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
                        if (result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED) {
                            /*MainActivity.checkLogin = true;
                            //
                            final MainActivity.DownloadTask downloadTask = new MainActivity.DownloadTask(activity);

                            if(BHGeneral.isOpenDepartmentSignature){
                                String[] URL = {
                                    String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip"),
                                        String.format("%s/%s", BHPreference.TSR_SERVICE_URL, "GetSignatureImages")
                                };
                                downloadTask.execute(URL);
                            }else{
                                String URL = String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip");
                                downloadTask.execute(URL);
                            }

                            downloadTask.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });*/
                            try {

                                if((BHPreference.PositionName().equals("พนักงานขาย,พนักงานเครดิต"))|(BHPreference.PositionName().equals("พนักงานเครดิต,พนักงานขาย"))|
                                        (BHPreference.PositionName().equals("หัวหน้าทีมเครดิต,หัวหน้าหน่วยเครดิต,พนักงานขาย,พนักงานเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานเครดิต,พนักงานขาย,หัวหน้าทีมเครดิต,หัวหน้าหน่วยเครดิต"))|
                                        (BHPreference.PositionName().equals("หัวหน้าหน่วยเครดิต,หัวหน้าทีมเครดิต,พนักงานขาย,พนักงานเครดิต"))|
                                        (BHPreference.PositionName().equals("หัวหน้าทีมเครดิต,หัวหน้าหน่วยเครดิต,พนักงานเครดิต,พนักงานขาย"))|
                                        (BHPreference.PositionName().equals("หัวหน้าทีมเครดิต,หัวหน้าหน่วยเครดิต,พนักงานขาย,พนักงานเครดิต"))|
                                        (BHPreference.PositionName().equals("หัวหน้าทีมเครดิต,พนักงานเครดิต,หัวหน้าหน่วยเครดิต,พนักงานขาย"))|
                                        (BHPreference.PositionName().equals("หัวหน้าทีมเครดิต,พนักงานเครดิต,พนักงานขาย,หัวหน้าหน่วยเครดิต"))|
                                        (BHPreference.PositionName().equals("หัวหน้าทีมเครดิต,พนักงานขาย,หัวหน้าหน่วยเครดิต,พนักงานเครดิต"))|
                                        (BHPreference.PositionName().equals("หัวหน้าทีมเครดิต,พนักงานขาย,พนักงานเครดิต,หัวหน้าหน่วยเครดิต"))|
                                        (BHPreference.PositionName().equals("หัวหน้าหน่วยเครดิต,หัวหน้าทีมเครดิต,พนักงานเครดิต,พนักงานขาย"))|
                                        (BHPreference.PositionName().equals("หัวหน้าหน่วยเครดิต,หัวหน้าทีมเครดิต,พนักงานขาย,พนักงานเครดิต"))|
                                        (BHPreference.PositionName().equals("หัวหน้าหน่วยเครดิต,พนักงานเครดิต,หัวหน้าทีมเครดิต,พนักงานขาย"))|
                                        (BHPreference.PositionName().equals("หัวหน้าหน่วยเครดิต,พนักงานเครดิต,พนักงานขาย,หัวหน้าทีมเครดิต"))|
                                        (BHPreference.PositionName().equals("หัวหน้าหน่วยเครดิต,พนักงานขาย,หัวหน้าทีมเครดิต,พนักงานเครดิต"))|
                                        (BHPreference.PositionName().equals("หัวหน้าหน่วยเครดิต,พนักงานขาย,พนักงานเครดิต,หัวหน้าทีมเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานเครดิต,หัวหน้าทีมเครดิต,หัวหน้าหน่วยเครดิต,พนักงานขาย"))|
                                        (BHPreference.PositionName().equals("พนักงานเครดิต,หัวหน้าทีมเครดิต,พนักงานขาย,หัวหน้าหน่วยเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานเครดิต,หัวหน้าหน่วยเครดิต,หัวหน้าทีมเครดิต,พนักงานขาย"))|
                                        (BHPreference.PositionName().equals("พนักงานเครดิต,หัวหน้าหน่วยเครดิต,พนักงานขาย,หัวหน้าทีมเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานเครดิต,พนักงานขาย,หัวหน้าทีมเครดิต,หัวหน้าหน่วยเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานเครดิต,พนักงานขาย,หัวหน้าหน่วยเครดิต,หัวหน้าทีมเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานขาย,หัวหน้าทีมเครดิต,หัวหน้าหน่วยเครดิต,พนักงานเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานขาย,หัวหน้าทีมเครดิต,พนักงานเครดิต,หัวหน้าหน่วยเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานขาย,หัวหน้าหน่วยเครดิต,หัวหน้าทีมเครดิต,พนักงานเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานขาย,หัวหน้าหน่วยเครดิต,พนักงานเครดิต,หัวหน้าทีมเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานขาย,พนักงานเครดิต,หัวหน้าทีมเครดิต,หัวหน้าหน่วยเครดิต"))|
                                        (BHPreference.PositionName().equals("พนักงานขาย,พนักงานเครดิต,หัวหน้าหน่วยเครดิต,หัวหน้าทีมเครดิต")))
                                {

                                    BHApplication.getInstance().getPrefManager().setPreferrence("select_p", BHPreference.sourceSystem());

                                    try {
                                        String DD= BHApplication.getInstance().getPrefManager().getPreferrence("select_p");
                                        if((DD.equals("Sale"))|(DD.equals("Credit"))) {
                                            Log.e("VVVV","1111");
                                            new SynchronizeMainFragment().updateUserInfo2(BHPreference.userID(),BHPreference.sourceSystem(), new updateUserInfoAsyncResponse() {
                                                @Override
                                                public void updateUserInfoProcessFinish() {
                                                    MainActivity.checkLogin = true;
                                                    //
                                                    final MainActivity.DownloadTask downloadTask = new MainActivity.DownloadTask(activity);

                                                    if (BHGeneral.isOpenDepartmentSignature) {
                                                        String[] URL = {
                                                                String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip"),
                                                                String.format("%s/%s", BHPreference.TSR_SERVICE_URL, "GetSignatureImages")
                                                        };
                                                        downloadTask.execute(URL);
                                                    } else {
                                                        String URL = String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip");
                                                        downloadTask.execute(URL);
                                                    }

                                                    downloadTask.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                        @Override
                                                        public void onCancel(DialogInterface dialog) {
                                                            downloadTask.cancel(true);
                                                        }
                                                    });
                                                }
                                            });
                                            BHApplication.getInstance().getPrefManager().setPreferrence("select_p","null");
                                        }

                                    } catch (Exception ex){

                                    }
                                } else {
                                    Log.e("VVVV","2222");

                                    new SynchronizeMainFragment().updateUserInfo(BHPreference.userID(), new updateUserInfoAsyncResponse() {
                                        @Override
                                        public void updateUserInfoProcessFinish() {
                                            MainActivity.checkLogin = true;
                                            //
                                            final MainActivity.DownloadTask downloadTask = new MainActivity.DownloadTask(activity);

                                            if (BHGeneral.isOpenDepartmentSignature) {
                                                String[] URL = {
                                                        String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip"),
                                                        String.format("%s/%s", BHPreference.TSR_SERVICE_URL, "GetSignatureImages")
                                                };
                                                downloadTask.execute(URL);
                                            } else {
                                                String URL = String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip");
                                                downloadTask.execute(URL);
                                            }

                                            downloadTask.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    downloadTask.cancel(true);
                                                }
                                            });
                                        }
                                    });

                                }
                            }
                            catch (Exception ex){

                                new SynchronizeMainFragment().updateUserInfo(BHPreference.userID(), new updateUserInfoAsyncResponse() {
                                    @Override
                                    public void updateUserInfoProcessFinish() {
                                        MainActivity.checkLogin = true;
                                        //
                                        final MainActivity.DownloadTask downloadTask = new MainActivity.DownloadTask(activity);

                                        if (BHGeneral.isOpenDepartmentSignature) {
                                            String[] URL = {
                                                    String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip"),
                                                    String.format("%s/%s", BHPreference.TSR_SERVICE_URL, "GetSignatureImages")
                                            };
                                            downloadTask.execute(URL);
                                        } else {
                                            String URL = String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip");
                                            downloadTask.execute(URL);
                                        }

                                        downloadTask.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                downloadTask.cancel(true);
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                    stop();
                }

            }
        }
    }
}
