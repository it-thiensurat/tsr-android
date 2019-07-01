package th.co.thiensurat.fragments.credit;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.EditText;
import android.widget.Toast;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TransactionService;

public class AssignEmployeeFragment extends BHFragment {

    @InjectView
    private EditText txtImport;

    @InjectView
    private EditText txtExport;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_assign_employee;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_assign_employee;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_save};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_save:

                final String importList = String.valueOf(txtImport.getText());
                final String exportList = String.valueOf(txtExport.getText());

//                if(importList == null || importList.isEmpty()) {
//                    Toast.makeText(activity, "กรุณาใส่ข้อมูลนำเข้าหรือข้อมูลส่งออก", Toast.LENGTH_LONG).show();
//                    break;
//                }
//
//                if(exportList == null || exportList.isEmpty()) {
//                    Toast.makeText(activity, "กรุณาใส่ข้อมูลนำเข้าหรือข้อมูลส่งออก", Toast.LENGTH_LONG).show();
//                    break;
//                }

                new BackgroundProcess(activity) {
                    @Override
                    protected void calling() {
                        TSRController.updateAssignForPushOrPull(BHPreference.organizationCode(), importList, BHPreference.employeeID(), AssignController.AssignAction.PULL.toString(), true);
                        TSRController.updateAssignForPushOrPull(BHPreference.organizationCode(), exportList, BHPreference.employeeID(), AssignController.AssignAction.PUSH.toString(), true);
                    }

                    @Override
                    protected void after() {
                        txtImport.setText("");
                        txtExport.setText("");
                        startSync();
                    }
                }.start();

                break;
        }
    }
    
    private void startSync() {
        TransactionService.stopService(activity);
        SynchronizeReceiver synchronizeReceiver = new SynchronizeReceiver();
        synchronizeReceiver.getInstance().start();

        //new BaseController().removeDatabase();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
        /*request.master.syncTeamRelated = false;
        request.master.syncProductRelated = false;
        request.master.syncCustomerRelated = false;
        request.master.syncPaymentRelated = false;
        request.master.syncContractRelated = true;
        request.master.syncEditContractRelated = false;
        request.master.syncSendMoneyRelated = false;
        request.master.syncMasterDataRelated = false;*/
        request.master.syncFullRelated = true;

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);
    }

    private class SynchronizeReceiver extends BroadcastReceiver implements MainActivity.IApiAccessResponse{
        private SynchronizeReceiver instance;

        private ProgressDialog dialog;
        private SynchronizeService.SynchronizeResult result;
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
                        if(result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED){
                            MainActivity.checkLogin = true;
                            //
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
            activity.showView(BHFragment.newInstance(AssignEmployeeFragment.class));
        }
    }

}
