package th.co.thiensurat.service.FCM;

import android.os.Bundle;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.GCMProdStkAndContractController;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.controller.UserController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.GCMProdStkAndContractInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.data.info.TransactionLogInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.TransactionService;
import th.co.thiensurat.service.data.AddUserDeviceLogInputInfo;
import th.co.thiensurat.service.data.UpdateGCMProdStkAndContractInputInfo;

public class MyJobService extends JobService {

    private static final String TAG = "MyJobService";

    //private final Executor executor = Executors.newFixedThreadPool(2);

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.

        Bundle data = jobParameters.getExtras();

        try {
            if (data != null && data.size() > 0) {
                String type = data.getString("messageType");

                if (type != null) {
                    switch (Enum.valueOf(MyFirebaseMessagingService.FCMServiceType.class, type)) {

                        case LOGOUT_USER_INACTIVE_FORCE_LOGIN_FALSE:
                            /*executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    setForceLogout(data);
                                }
                            });*/

                            setForceLogout(data);
                            break;
                        case GCM_PRODUCTSTOCK_AND_CONTRACT:
                            /*executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    setProductStockAndContract(data);
                                }
                            });*/

                            setProductStockAndContract(data);
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error onStartJob: " + ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void setForceLogout(Bundle data) {
        Log.d(TAG, "Run setForceLogout");

        final String userID = data.getString("userID");
        final String userName = data.getString("userName");
        final String deviceID = data.getString("deviceID");
        final String serviceMode = data.getString("SERVICE_MODE");

        if (deviceID.equals(BHPreference.userDeviceId()) && serviceMode.equals(BHGeneral.SERVICE_MODE.toString())) {
            BHPreference.setUserNotAllowLogin(UserController.LoginType.NOT_ALLOW.toString());
            List<TransactionLogInfo> trInfo = TSRController.getTransactionLogBySyncStatus(false);
            if (MainActivity.activity != null) {
                if (trInfo == null) {
                    Log.d(TAG, "Logout1");
                    MainActivity.activity.logout(userName, deviceID, AddUserDeviceLogInputInfo.UserDeviceLogProcessType.GCM_LOGOUT.toString());
                } else {
                    TransactionService.synchronizeTransactions(new TransactionService.TransactionServiceHandler() {
                        @Override
                        protected void onFinish(Exception e) {
                            if (e == null) {
                                Log.d(TAG, "Logout2");
                                MainActivity.activity.logout(userName, deviceID, AddUserDeviceLogInputInfo.UserDeviceLogProcessType.GCM_LOGOUT.toString());
                            }
                        }
                    });
                }
            }
        }
    }

    private void setProductStockAndContract(Bundle data) {
        Log.d(TAG, "Run setProductStockAndContract");

        String gCMProdStkAndContractID = data.getString("gCMProdStkAndContractID");
        String productSerialNumber = data.getString("productSerialNumber");
        String organizationCode = data.getString("organizationCode");
        String newSubTeamCode = data.getString("newSubTeamCode");
        String newTeamCode = data.getString("newTeamCode");
        String refNo = data.getString("refNo");
        String SERVICE_MODE = data.getString("SERVICE_MODE");

        if (SERVICE_MODE.equals(BHGeneral.SERVICE_MODE.toString())) {
            ProductStockInfo productStockInfo = new ProductStockController().getProductStockByProductSerialNumber(organizationCode, productSerialNumber);
            if (productStockInfo != null) {
                if (!productStockInfo.TeamCode.equals(newSubTeamCode)) {
                    GCMProdStkAndContractInfo gcmProdStkAndContractInfo = new GCMProdStkAndContractInfo();
                    gcmProdStkAndContractInfo.GCMProdStkAndContractID = gCMProdStkAndContractID;
                    gcmProdStkAndContractInfo.ProductSerialNumber = productSerialNumber;
                    gcmProdStkAndContractInfo.OrganizationCode = organizationCode;
                    gcmProdStkAndContractInfo.ProdStkStatus = productStockInfo.Status;
                    //gcmProdStkAndContractInfo.ProductSerialNumber = productSerialNumber;
                    //gcmProdStkAndContractInfo.NewSubTeamCode = newSubTeamCode;
                    //gcmProdStkAndContractInfo.NewTeamCode = newTeamCode;

                    switch (Enum.valueOf(ProductStockController.ProductStockStatus.class, productStockInfo.Status)) {
                        case WAIT:
                        case OVER:
                        case CHECKED:
                            new ProductStockController().deleteProductStockBySerialNumber(organizationCode, productSerialNumber);
                            break;
                        case SOLD:
                            new ProductStockController().deleteProductStockBySerialNumber(organizationCode, productSerialNumber);

                            if (refNo.equals("")) {
                                ContractInfo contractInfo = new ContractController().getContractByProductSerialNumber(organizationCode, productSerialNumber);
                                if (contractInfo != null) {
                                    if(!contractInfo.SaleTeamCode.equals(newTeamCode)){
                                        new ContractController().updateContractForGCM(false, contractInfo.OrganizationCode, contractInfo.RefNo);

                                        gcmProdStkAndContractInfo.RefNo = contractInfo.RefNo;
                                        gcmProdStkAndContractInfo.LastUpdateDate = new Date();
                                        gcmProdStkAndContractInfo.LastUpdateBy = BHPreference.employeeID();
                                        UpdateGCMProdStkAndContractInputInfo updateGCMProdStkAndContractInputInfo = UpdateGCMProdStkAndContractInputInfo.from(gcmProdStkAndContractInfo);
                                        TSRService.updateGCMProdStkAndContract(updateGCMProdStkAndContractInputInfo, true);
                                    }
                                }
                            } else {
                                new ContractController().updateContractForGCM(false, organizationCode, refNo);
                                gcmProdStkAndContractInfo.RefNo = refNo;
                            }
                            break;
                    }

                    if(gcmProdStkAndContractInfo.RefNo != null){
                        GCMProdStkAndContractInfo checkGCMProdStkAndContract =  new GCMProdStkAndContractController().getGCMProdStkAndContractByGCMProdStkAndContractID(gcmProdStkAndContractInfo.GCMProdStkAndContractID);

                        if(checkGCMProdStkAndContract == null){
                            new GCMProdStkAndContractController().addGCMProdStkAndContract(gcmProdStkAndContractInfo);
                        } else {
                            new GCMProdStkAndContractController().updateGCMProdStkAndContract(gcmProdStkAndContractInfo);
                        }
                    }
                }
            }
        }
    }

}
