package th.co.thiensurat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.Serializable;

import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DatabaseManager;
import th.co.thiensurat.service.TransactionService.TransactionServiceHandler;
import th.co.thiensurat.service.data.SynchStatusInputInfo;
import th.co.thiensurat.service.data.SynchStatusOutputInfo;

import static th.co.thiensurat.fragments.sales.SaleContractPrintFragment.select_page;

public class SynchronizeService extends Service {
	public static final String SYNCHRONIZE_BROADCAST_ACTION = "th.co.thiensurat.service.synchronize.action";
	public static final String SYNCHRONIZE_REQUEST_DATA_KEY = "th.co.thiensurat.service.synchronize.data.request";
	public static final String SYNCHRONIZE_RESULT_DATA_KEY = "th.co.thiensurat.service.synchronize.data.result";

	public static final int SYNCHRONIZE_LOCAL_COMPLETED = 200;
	public static final int SYNCHRONIZE_LOCAL_ERROR = 300;
	public static final int SYNCHRONIZE_ALL_COMPLETED = 400;
	public static final int SYNCHRONIZE_ALL_ERROR = 500;

	public static class SynchronizeData extends BHParcelable {
		public SynchronizeMaster master;
		public SynchronizeTransaction transaction;

	}
	public static class SynchronizeData2 extends BHParcelable {
		public SynchronizeMaster2 master;
		public SynchronizeTransaction transaction;

	}

	public static class SynchronizeMaster extends BHParcelable {
		//		public boolean syncTeamRelated;
		public boolean syncProductRelated;
//		public boolean syncCustomerRelated;
//		public boolean syncPaymentRelated;
//		public boolean syncContractRelated;
//		public boolean syncEditContractRelated;

		/*** [START] :: Fixed - [BHPROJ-0026-970] :: [Meeting@TSR@07/03/2559] [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา/แจ้งปัญหา] ทำ Performance Tuning ในขณะทำการปรับปรุงรายการ ***/
		public boolean syncRequestChangeProductRelated;
		public boolean syncRequestChangeContractRelated;
		public boolean syncRequestImpoundProductRelated;
		public boolean syncRequestComplainRelated;
		public boolean syncRequestNextPaymentRelated;
		/*** [END] :: Fixed - [BHPROJ-0026-970] :: [Meeting@TSR@07/03/2559] [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา/แจ้งปัญหา] ทำ Performance Tuning ในขณะทำการปรับปรุงรายการ ***/

//		public boolean syncSendMoneyRelated;
//		public boolean syncMasterDataRelated;
//		public boolean syncSaleAuditDataRelated;
		public boolean syncCreditDataRelated;
		public boolean syncSpareDrawdownRelated;
		public boolean syncFullRelated;

		private int percent() {
//			int count = (syncTeamRelated ? 1 : 0) + (syncProductRelated ? 1 : 0) + (syncCustomerRelated ? 1 : 0) + (syncPaymentRelated ? 1 : 0)
//					+ (syncContractRelated ? 1 : 0) + (syncEditContractRelated ? 1 : 0)
//
//					/*** [START] :: Fixed - [BHPROJ-0026-970] :: [Meeting@TSR@07/03/2559] [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา/แจ้งปัญหา] ทำ Performance Tuning ในขณะทำการปรับปรุงรายการ ***/
//					+ (syncRequestChangeProductRelated ? 1 : 0) + (syncRequestChangeContractRelated ? 1 : 0)
//					+ (syncRequestImpoundProductRelated ? 1 : 0) + (syncRequestComplainRelated ? 1 : 0)
//					+ (syncRequestNextPaymentRelated ? 1 : 0)
//					/*** [END] :: Fixed - [BHPROJ-0026-970] :: [Meeting@TSR@07/03/2559] [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา/แจ้งปัญหา] ทำ Performance Tuning ในขณะทำการปรับปรุงรายการ ***/
//
//					+ (syncSendMoneyRelated ? 1 : 0) + (syncSaleAuditDataRelated ? 1 : 0)
//					+ (syncCreditDataRelated ? 1 : 0) + (syncMasterDataRelated ? 1 : 0) + (syncSpareDrawdownRelated ? 1 : 0) + (syncFullRelated ? 1 : 0);



				int count = (syncProductRelated ? 1 : 0)

						/*** [START] :: Fixed - [BHPROJ-0026-970] :: [Meeting@TSR@07/03/2559] [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา/แจ้งปัญหา] ทำ Performance Tuning ในขณะทำการปรับปรุงรายการ ***/
						+ (syncRequestChangeProductRelated ? 1 : 0) + (syncRequestChangeContractRelated ? 1 : 0)
						+ (syncRequestImpoundProductRelated ? 1 : 0) + (syncRequestComplainRelated ? 1 : 0)
						+ (syncRequestNextPaymentRelated ? 1 : 0)
						/*** [END] :: Fixed - [BHPROJ-0026-970] :: [Meeting@TSR@07/03/2559] [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา/แจ้งปัญหา] ทำ Performance Tuning ในขณะทำการปรับปรุงรายการ ***/

						+ (syncSpareDrawdownRelated ? 1 : 0) + (syncFullRelated ? 1 : 0);
				return (int) Math.ceil(100.0 / count);
		}
	}

	public static class SynchronizeMaster2 extends BHParcelable {

		public boolean syncProductRelated;

		public boolean syncRequestChangeProductRelated;
		public boolean syncRequestChangeContractRelated;
		public boolean syncRequestImpoundProductRelated;
		public boolean syncRequestComplainRelated;
		public boolean syncRequestNextPaymentRelated;

	}

	public static class SynchronizeTransaction extends BHParcelable {

	}

	public static class SynchronizeResult extends BHParcelable implements Serializable{
		public String title;
		public String message;
		public Exception error;
		public int progress;

        public SynchronizeResult() {
            this.title = "";
            this.message = "";
            this.progress = -1;
        }
	}

	public static void sendBroadcast(SynchronizeResult result) {
		/*** [START] :: Fixed - https://fabric.io/bighead5/android/apps/th.co.thiensurat/issues/5c539483f8b88c29633835b5 ***/
		/*Intent i = new Intent(SYNCHRONIZE_BROADCAST_ACTION);
		i.putExtra(SYNCHRONIZE_RESULT_DATA_KEY, (Parcelable) ((SynchronizeResult) result.copy()));
		LocalBroadcastManager.getInstance(MainActivity.activity.getApplicationContext()).sendBroadcast(i);*/
		try {
			Intent i = new Intent(SYNCHRONIZE_BROADCAST_ACTION);
			i.putExtra(SYNCHRONIZE_RESULT_DATA_KEY, (Parcelable) ((SynchronizeResult) result.copy()));
			LocalBroadcastManager.getInstance(MainActivity.activity.getApplicationContext()).sendBroadcast(i);
		}catch (Exception ex){}
		/*** [END] :: Fixed - https://fabric.io/bighead5/android/apps/th.co.thiensurat/issues/5c539483f8b88c29633835b5 ***/
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("SERVICE", "START");
		final SynchronizeData data;
		if (intent != null) {
			data = intent.getParcelableExtra(SYNCHRONIZE_REQUEST_DATA_KEY);
		} else {
			data = null;
		}

		Runnable r = new Runnable() {

			/*public void sendBroadcast(SynchronizeResult result) {
				Intent i = new Intent(SYNCHRONIZE_BROADCAST_ACTION);
                i.putExtra(SYNCHRONIZE_RESULT_DATA_KEY, (Parcelable) ((SynchronizeResult) result.copy()));
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
			}*/

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (data != null) {
					final SynchronizeResult result = new SynchronizeResult();
					SynchronizeMaster master = data.master;

					final boolean[] localSuccess = { true };

					File file = new File(DatabaseManager.getInstance().getDatabasePath());
					if(file.exists()){
						TransactionService.synchronizeTransactions(new TransactionServiceHandler() {

							@Override
							protected void onStartProcess() {
								result.message = "Upload local database";
								result.progress = 0;
								sendBroadcast(result);
							}

							@Override
							protected void onProcess(int percent) {
								result.progress = percent;
								sendBroadcast(result);
							}

							@Override
							protected void onStartUpdate() {
								result.message = "Update local database";
								result.progress = 0;
								sendBroadcast(result);
							}

							@Override
							protected void onUpdate(int percent) {
								result.progress = percent;
								sendBroadcast(result);
							}


							@Override
							protected void onFinish(Exception e) {
								// TODO Auto-generated method stub
								localSuccess[0] = e == null;
								//result.message = "Upload local database";
								if (e != null) {
									result.progress = SYNCHRONIZE_LOCAL_ERROR;
									result.error = e;
								} else {
									result.progress = SYNCHRONIZE_LOCAL_COMPLETED;
								}
								sendBroadcast(result);
							}
						});
					}

					if (localSuccess[0]) {
                        result.progress = 0;
						if (master != null) {


                            int percent = master.percent();


                            try {
//                                if (master.syncTeamRelated) {
//                                    result.message = "ปรับปรุงข้อมูลทีมงาน";
//                                    sendBroadcast(result);
//                                    TSRController.employeeImport(BHPreference.organizationCode(), BHPreference.teamCode());
//                                    result.progress += percent;
//                                }

								if(select_page==1){
									Log.e("test_pate","1");


									if (master.syncFullRelated) {
									//	result.message = "ปรับปรุงข้อมูลหลัก";
										sendBroadcast(result);
										TSRController.SynchDataFromServer2Local(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());
										//result.progress += percent;
										SynchStatusInputInfo synchStatusInputInfo = new SynchStatusInputInfo();
										synchStatusInputInfo.OrganizationCode = BHPreference.organizationCode();
										synchStatusInputInfo.TeamCode = BHPreference.teamCode();
										synchStatusInputInfo.EmpID = BHPreference.employeeID();
										synchStatusInputInfo.CallStatus = "CHECKE";
										synchStatusInputInfo.IsAdmin = BHPreference.IsAdmin();

										boolean statusSQLite = false;



										while (statusSQLite == false) {

											try {
												//RUNNING, COMPLETED, ERROR
												SynchStatusOutputInfo output = TSRService.synchDataFromServer2Local(synchStatusInputInfo, false);

												if (output.ResultCode == 0) {
													if (output.Info.Status.equals("RUNNING")) { //Running
														//result.progress = output.Info.Progress;
														sendBroadcast(result);
													} else if (output.Info.Status.equals("COMPLETED")) { //Completed

														statusSQLite = true;
													} else { //Error
														//throw new RuntimeException(info.ResultDescription);
														throw new RuntimeException(getResources().getString(R.string.error_synch));



													}
													//Thread.sleep(5000);
												} else {
													throw new RuntimeException(output.ResultDescription);
												}
											} catch (Exception e) {
												Log.e("Sync", e.getLocalizedMessage());
											}
										}






										select_page=0;
									}
								} else {

									Log.e("test_pate","2");
									if (master.syncProductRelated) {
										result.message = "ปรับปรุงข้อมูลสินค้า";
										sendBroadcast(result);
										TSRController.importProduct(BHPreference.organizationCode(), BHPreference.teamCode());
										result.progress += percent;
									}

//                                if (master.syncCustomerRelated) {
//                                    result.message = "ปรับปรุงข้อมูลลูกค้า";
//                                    sendBroadcast(result);
//                                    result.progress += percent;
//                                }
//
//                                if (master.syncPaymentRelated) {
//                                    result.message = "ปรับปรุงข้อมูลการจ่ายเงิน";
//                                    sendBroadcast(result);
//                                    result.progress += percent;
//                                }
//
//                                if (master.syncContractRelated) {
//                                    result.message = "ปรับปรุงข้อมูลสัญญา";
//                                    sendBroadcast(result);
//                                    TSRController.importContract(BHPreference.organizationCode(), BHPreference.teamCode());
//                                    result.progress += percent;
//                                }
//
//                                if (master.syncEditContractRelated) {
//                                    result.message = "ปรับปรุงข้อมูลการร้องขอต่าง ๆ";
//                                    sendBroadcast(result);
//                                    TSRController.importRequest(BHPreference.organizationCode(), BHPreference.teamCode());
//                                    result.progress += percent;
//                                }


									/*** [START] :: Fixed - [BHPROJ-0026-970] :: [Meeting@TSR@07/03/2559] [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา/แจ้งปัญหา] ทำ Performance Tuning ในขณะทำการปรับปรุงรายการ ***/
									if (master.syncRequestChangeProductRelated) {
										result.message = "ปรับปรุงข้อมูลการร้องขอเปลี่ยนเครื่อง";
										sendBroadcast(result);
										/*** [START] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
//									TSRController.importRequestChangeProduct(BHPreference.organizationCode(), BHPreference.teamCode(), result);
										TSRController.importRequestChangeProduct(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID(), result);
										/*** [END] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
										result.progress += percent;
									}
									if (master.syncRequestChangeContractRelated) {
										result.message = "ปรับปรุงข้อมูลการร้องขอเปลี่ยนสัญญา";
										sendBroadcast(result);
										/*** [START] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
//									TSRController.importRequestChangeContract(BHPreference.organizationCode(), BHPreference.teamCode(), result);
										TSRController.importRequestChangeContract(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID(), result);
										/*** [END] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
										result.progress += percent;
									}
									if (master.syncRequestImpoundProductRelated) {
										result.message = "ปรับปรุงข้อมูลการร้องขอถอดเครื่อง";
										sendBroadcast(result);
										/*** [START] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
//									TSRController.importRequestImpoundProduct(BHPreference.organizationCode(), BHPreference.teamCode(), result);
										TSRController.importRequestImpoundProduct(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID(), result);
										/*** [END] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
										result.progress += percent;
									}
									if (master.syncRequestComplainRelated) {
										result.message = "ปรับปรุงข้อมูลการร้องขอแจ้งปัญหาและแก้ไขปัญหา";
										sendBroadcast(result);
										/*** [START] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
//									TSRController.importRequestComplain(BHPreference.organizationCode(), BHPreference.teamCode(), result);
										TSRController.importRequestComplain(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID(), result);
										/*** [END] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
										result.progress += percent;
									}
									if (master.syncRequestNextPaymentRelated) {
										result.message = "ปรับปรุงข้อมูลการร้องขออนุมัติเก็บเงินค่างวด";
										sendBroadcast(result);
										/*** [START] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
//									TSRController.importRequestNextPayment(BHPreference.organizationCode(), BHPreference.teamCode(), result);
										TSRController.importRequestNextPayment(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID(), result);
										/*** [END] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
										result.progress += percent;
									}
									/*** [END] :: Fixed - [BHPROJ-0026-970] :: [Meeting@TSR@07/03/2559] [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา/แจ้งปัญหา] ทำ Performance Tuning ในขณะทำการปรับปรุงรายการ ***/

//                                if (master.syncSendMoneyRelated) {
//                                    result.message = "ปรับปรุงข้อมูลการส่งเงิน";
//                                    sendBroadcast(result);
//                                    TSRController.importSendMoney();
//                                    result.progress += percent;
//                                }
//
//								if (master.syncSaleAuditDataRelated) {
//									result.message = "ปรับปรุงข้อมูลลูกค้าตรวจสอบ";
//									sendBroadcast(result);
//									TSRController.importSaleAudit();
//									result.progress += percent;
//								}
//
//								if (master.syncCreditDataRelated) {
//									result.message = "ปรับปรุงข้อมูลลูกค้าเก็บเงิน";
//									sendBroadcast(result);
//									TSRController.importCredit();
//									result.progress += percent;
//								}

									if (master.syncSpareDrawdownRelated) {
										result.message = "ปรับปรุงข้อมูลขอเบิกอะไหล่";
										sendBroadcast(result);
										TSRController.importSpareDrawdown();
										result.progress += percent;
									}

//								if (master.syncMasterDataRelated) {
//                                    result.message = "ปรับปรุงข้อมูลหลัก";
//                                    sendBroadcast(result);
//                                    TSRController.importMasterData(BHPreference.organizationCode(), BHPreference.teamCode());
//                                    result.progress += percent;
//                                }

									if (master.syncFullRelated) {
										result.message = "ปรับปรุงข้อมูลหลัก";
										sendBroadcast(result);
										TSRController.SynchDataFromServer2Local(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());
										result.progress += percent;


										SynchStatusInputInfo synchStatusInputInfo = new SynchStatusInputInfo();
										synchStatusInputInfo.OrganizationCode = BHPreference.organizationCode();
										synchStatusInputInfo.TeamCode = BHPreference.teamCode();
										synchStatusInputInfo.EmpID = BHPreference.employeeID();
										synchStatusInputInfo.CallStatus = "CHECKE";
										synchStatusInputInfo.IsAdmin = BHPreference.IsAdmin();




										Log.e("c1",BHPreference.teamCode());
										Log.e("c2",BHPreference.employeeID());
										Log.e("c3", String.valueOf(BHPreference.IsAdmin()));
										Log.e("c4",BHPreference.organizationCode());


										boolean statusSQLite = false;
										while (statusSQLite == false) {

											try {
												//RUNNING, COMPLETED, ERROR
												SynchStatusOutputInfo output = TSRService.synchDataFromServer2Local(synchStatusInputInfo, false);
												if (output.ResultCode == 0) {
													if (output.Info.Status.equals("RUNNING")) { //Running
														result.progress = output.Info.Progress;
														sendBroadcast(result);
													} else if (output.Info.Status.equals("COMPLETED")) { //Completed
														statusSQLite = true;
													} else { //Error
														//throw new RuntimeException(info.ResultDescription);
														throw new RuntimeException(getResources().getString(R.string.error_synch));
													}
													Thread.sleep(5000);
													//Thread.sleep(5000);

												} else {
													//throw new RuntimeException(output.ResultDescription);
												}
											} catch (Exception e) {
												// TODO Auto-generated catch block
												Log.e("Sync2", e.getLocalizedMessage());
											}
										}
										//result.progress += percent;
									}
								}
                                result.title = "Update Master Table";
                                result.message = "ปรับปรุงข้อมูลเรียบร้อย";
                                result.progress = SYNCHRONIZE_ALL_COMPLETED;
                                sendBroadcast(result);

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                result.progress = SYNCHRONIZE_ALL_ERROR;
                                result.error = e;
                                sendBroadcast(result);
                            }

                        }
					}
				}
				stopSelf();
			}
		};

		new Thread(r).start();
		return super.onStartCommand(intent, flags, startId);
	}

}
