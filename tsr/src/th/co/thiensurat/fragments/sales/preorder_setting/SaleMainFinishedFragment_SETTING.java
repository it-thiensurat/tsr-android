package th.co.thiensurat.fragments.sales.preorder_setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHPagerFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DatabaseManager;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.fragments.sales.SaleContractPrintFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.retrofit.api.Service;

import static java.lang.String.valueOf;
import static th.co.thiensurat.retrofit.api.client.BASE_URL;

public class SaleMainFinishedFragment_SETTING extends BHPagerFragment {

	@InjectView
	private TextView textViewFinish;
	@InjectView
	public static ListView listViewFinish;
	public static List<ContractInfo> contractList = null;

	public static ContractAdapter contractAdapter;

	ProgressDialog dialog;
	private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());
	public  boolean isContractDetails =  false;

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_sales;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_main_finished;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		GetContractStatusFinish();
	}

	private void GetContractStatusFinish() {
		// TODO Auto-generated method stub
		new BackgroundProcess(activity) {			
			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				try {
					if(isCredit){
						//-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป (Comment ตัวนี้ไปใช้ getContractStatusFinishForCreditBySearch แทน)
						//contractList = TSRController.getContractStatusFinishForCredit(BHPreference.organizationCode(), ContractStatusName.COMPLETED.toString());
						contractList = TSRController.getContractStatusFinishForCreditBySearch(BHPreference.organizationCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), "%%");
						//Log.e("user",contractList.toString());
						//Log.e("1111","1111");
					} else {
						if (BHPreference.IsSaleForCRD()) {
							if (isContractDetails) {
								contractList = TSRController.getContractStatusFinishForCreditBySearch(BHPreference.organizationCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), "%%");
								Log.e("1111","2222");
							} else {
								contractList = TSRController.getContractStatusFinishForCRD(BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), BHPreference.employeeID());
								Log.e("1111","3333");
							}
						} else {
							contractList = TSRController.getContractStatusFinish_ContractInfo_preorder_setting2(BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString());
							Log.e("1111","4444");
						}
					}
					//Log.e("TEST_SPEED","1111");
                   Log.e("contractList_SI",contractList.size()+"");
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					if (contractList != null) {
						//Log.e("TEST_SPEED","222222");
						if (isConnectingToInternet()) {
							String SOU = BHPreference.sourceSystem();
							if(SOU.equals("Credit")){
								dialog = ProgressDialog.show(activity, "", "Loading...", true);
								//new PaymentController().deletePaymentByRefNo(BHPreference.RefNo());
								load_data();
							} else {
								bindContractList();
							}
						} else {
							bindContractList();
						}
					} else {
						listViewFinish.setVisibility(View.GONE);
						textViewFinish.setVisibility(View.VISIBLE);
						if(isCredit){
							textViewFinish.setText("Finished List");
						} else {
							textViewFinish.setText("Finished Sales List");
						}
					}
				} catch (NullPointerException ex) {
					listViewFinish.setVisibility(View.GONE);
					textViewFinish.setVisibility(View.VISIBLE);
					if(isCredit){
						textViewFinish.setText("Finished List");
					} else {
						textViewFinish.setText("Finished Sales List");
					}
				}
			}
		}.start();
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
				activeNetwork.isConnectedOrConnecting();

		return isConnected;
	}

	private void load_data() {
		try {
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			Service request = retrofit.create(Service.class);
			Call call = request.data2(BHPreference.employeeID());
			call.enqueue(new Callback() {
				@Override
				public void onResponse(Call call, retrofit2.Response response) {
					Gson gson=new Gson();
					try {
						JSONObject jsonObject=new JSONObject(gson.toJson(response.body()));
//						Log.e("data","1");
//						Log.e("jsonObject",jsonObject.toString());
						JSON_PARSE_DATA_AFTER_WEBCALL(jsonObject.getJSONArray("data"));
					} catch (JSONException e) {
						e.printStackTrace();
						Log.e("data","22");
					}
				}

				@Override
				public void onFailure(Call call, Throwable t) {
					Log.e("data","2");
				}
			});
		} catch (Exception e) {
			Log.e("data","3");
		}
	}

	public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array) {
//		Log.e("array.length()", valueOf(array.length()));
		for (int i = 0; i < array.length(); i++) {
			//GetData GetDataAdapter2 = new GetData();
			JSONObject json = null;
			try {
				json = array.getJSONObject(i);
				String CONTNO=json.getString("CONTNO")+"";
				String RefNo=json.getString("RefNo")+"";

				String CustomerID=json.getString("CustomerID")+"";
				String OrganizationCode=json.getString("OrganizationCode")+"";
				String STATUS=json.getString("STATUS")+"";
				String StatusCode=json.getString("StatusCode")+"";
				String SALES=json.getString("SALES")+"";
				String TotalPrice=json.getString("TotalPrice")+"";
				//String EFFDATE=json.getString("EFFDATE")+"";

				String HasTradeIn=json.getString("HasTradeIn")+"";
				String TradeInProductCode=json.getString("TradeInProductCode")+"";
				String TradeInBrandCode=json.getString("TradeInBrandCode")+"";
				String TradeInProductModel=json.getString("TradeInProductModel")+"";
				String TradeInDiscount=json.getString("TradeInDiscount")+"";
				String PreSaleSaleCode=json.getString("PreSaleSaleCode")+"";
				String PreSaleEmployeeCode=json.getString("PreSaleEmployeeCode")+"";
				String PreSaleEmployeeName=json.getString("PreSaleEmployeeName")+"";
				String PreSaleTeamCode=json.getString("PreSaleTeamCode")+"";
				String SaleCode=json.getString("SaleCode")+"";
				String SaleEmployeeCode=json.getString("SaleEmployeeCode")+"";
				String SaleTeamCode=json.getString("SaleTeamCode")+"";
				String InstallerSaleCode=json.getString("InstallerSaleCode")+"";
				String InstallerEmployeeCode=json.getString("InstallerEmployeeCode")+"";
				String InstallerTeamCode=json.getString("InstallerTeamCode")+"";
				//String InstallDate=json.getString("InstallDate")+"";

				String ProductSerialNumber=json.getString("ProductSerialNumber")+"";
				String ProductID=json.getString("ProductID")+"";
				String SaleEmployeeLevelPath=json.getString("SaleEmployeeLevelPath")+"";
				String MODE=json.getString("MODE")+"";

				String FortnightID=json.getString("FortnightID")+"";
				String ProblemID=json.getString("ProblemID")+"";
				String svcontno=json.getString("svcontno")+"";
				String isActive=json.getString("isActive")+"";
				String MODEL=json.getString("MODEL")+"";
				String fromrefno=json.getString("fromrefno")+"";
				String fromcontno=json.getString("fromcontno")+"";
				//String todate=json.getString("todate")+"";

				String tocontno=json.getString("tocontno")+"";
				String torefno=json.getString("torefno")+"";
			//	String CreateDate=json.getString("CreateDate")+"";

				String CreateBy=json.getString("CreateBy")+"";
				//String LastUpdateDate=json.getString("LastUpdateDate")+"";

				String LastUpdateBy=json.getString("LastUpdateBy")+"";
			//	String SyncedDate=json.getString("SyncedDate")+"";

				String SaleSubTeamCode=json.getString("SaleSubTeamCode")+"";
				String TradeInReturnFlag=json.getString("TradeInReturnFlag")+"";
				String IsReadyForSaleAudit=json.getString("IsReadyForSaleAudit")+"";
				String ContractReferenceNo=json.getString("ContractReferenceNo")+"";
				String IsMigrate=json.getString("IsMigrate")+"";
				String PayStatus=json.getString("PayStatus")+"";

				String Payment_PaymentID=json.getString("Payment_PaymentID")+"";
				String Payment_OrganizationCode=json.getString("Payment_OrganizationCode")+"";
				String Payment_SendMoneyID=json.getString("Payment_SendMoneyID")+"";
				String Payment_PaymentType=json.getString("Payment_PaymentType")+"";
				String Payment_PayPartial=json.getString("Payment_PayPartial")+"";
				String Payment_BankCode=json.getString("Payment_BankCode")+"";
				String Payment_ChequeNumber=json.getString("Payment_ChequeNumber")+"";
				String Payment_ChequeBankBranch=json.getString("Payment_ChequeBankBranch")+"";
				String Payment_ChequeDate=json.getString("Payment_ChequeDate")+"";
				String Payment_CreditCardNumber=json.getString("Payment_CreditCardNumber")+"";
				String Payment_CreditCardApproveCode=json.getString("Payment_CreditCardApproveCode")+"";
				String Payment_CreditEmployeeLevelPath=json.getString("Payment_CreditEmployeeLevelPath")+"";
				String Payment_TripID=json.getString("Payment_TripID")+"";
				String Payment_Status=json.getString("Payment_Status")+"";
				String Payment_RefNo=json.getString("Payment_RefNo")+"";
				String Payment_PayPeriod=json.getString("Payment_PayPeriod")+"";
				String Payment_PAYAMT=json.getString("Payment_PAYAMT")+"";
				String Payment_CashCode=json.getString("Payment_CashCode")+"";
				String Payment_EmpID=json.getString("Payment_EmpID")+"";
				String Payment_TeamCode=json.getString("Payment_TeamCode")+"";
				String Payment_receiptkind=json.getString("Payment_receiptkind")+"";
				String Payment_Kind=json.getString("Payment_Kind")+"";
				String Payment_BookNo=json.getString("Payment_BookNo")+"";
				String Payment_ReceiptNo=json.getString("Payment_ReceiptNo")+"";
				String Payment_CreateBy=json.getString("Payment_CreateBy")+"";
				String Payment_LastUpdateBy=json.getString("Payment_LastUpdateBy")+"";

				String EFFDATE=json.getJSONObject("EFFDATE").getString("date")+"";
				String InstallDate=json.getJSONObject("InstallDate").getString("date")+"";
				String todate=json.getJSONObject("todate").getString("date")+"";
				String CreateDate=json.getJSONObject("CreateDate").getString("date")+"";
				String LastUpdateDate=json.getJSONObject("LastUpdateDate").getString("date")+"";
				String SyncedDate=json.getJSONObject("SyncedDate").getString("date")+"";
				String DatePayment=json.getJSONObject("DatePayment").getString("date")+"";

				String Payment_PayDate=json.getJSONObject("Payment_PayDate").getString("date")+"";
				String Payment_CreateDate=json.getJSONObject("Payment_CreateDate").getString("date")+"";
				String Payment_LastUpdateDate=json.getJSONObject("Payment_LastUpdateDate").getString("date")+"";
				String Payment_SyncedDate=json.getJSONObject("Payment_SyncedDate").getString("date")+"";

				if(!CONTNO.equals("null")){
					updateAssignForPostpone(PayStatus,CONTNO);
					try {
						addContract(RefNo,CONTNO,CustomerID, OrganizationCode, STATUS, StatusCode,SALES,
								TotalPrice, EFFDATE, HasTradeIn,TradeInProductCode, TradeInBrandCode,
								TradeInProductModel, TradeInDiscount, PreSaleSaleCode, PreSaleEmployeeCode, PreSaleEmployeeName, PreSaleTeamCode, SaleCode,
								SaleEmployeeCode, SaleTeamCode, InstallerSaleCode, InstallerEmployeeCode, InstallerTeamCode,
								InstallDate, ProductSerialNumber, ProductID, SaleEmployeeLevelPath, MODE, FortnightID,
								ProblemID, svcontno,isActive, MODEL, fromrefno, fromcontno, todate, tocontno,
								torefno, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate,
								SaleSubTeamCode, TradeInReturnFlag, IsReadyForSaleAudit, ContractReferenceNo,IsMigrate);
					}
					catch (Exception ex){

					}
					try {
						addPayment(Payment_PaymentID, Payment_OrganizationCode, Payment_SendMoneyID, Payment_PaymentType, Payment_PayPartial, Payment_BankCode,
								Payment_ChequeNumber, Payment_ChequeBankBranch, Payment_ChequeDate, Payment_CreditCardNumber, Payment_CreditCardApproveCode,
								Payment_CreditEmployeeLevelPath, Payment_TripID, Payment_Status, Payment_RefNo, Payment_PayPeriod, Payment_PayDate, Payment_PAYAMT,
								Payment_CashCode, Payment_EmpID, Payment_TeamCode,Payment_receiptkind, Payment_Kind, Payment_BookNo, Payment_ReceiptNo, Payment_CreateDate, Payment_CreateBy,
								Payment_LastUpdateDate, Payment_LastUpdateBy, Payment_SyncedDate);
					}
					catch (Exception ex){

					}
				}
			} catch (JSONException e) {
				//Log.e("Exception", e.getLocalizedMessage());
				e.printStackTrace();
			}
		}

		try {
			dialog.dismiss();
		} catch (Exception ex){

		}
		bindContractList();
	}

	private SQLiteDatabase database = null;
	public void updateAssignForPostpone(String STATUSNAME,String CONTNO) {
		String sql = "update Contract set [svcontno] = ? WHERE CONTNO =?";
		//  String sql = "UPDATE Assign SET [Order] = ? WHERE AssignID = ?";
		executeNonQuery2(sql, new String[]{STATUSNAME, CONTNO});
	}
	public void updateAssignForPostpone_DatePayment(String STATUSNAME,String REFNO) {
		String sql = "update Contract set [svcontno] = ? WHERE CONTNO =?";
		//  String sql = "UPDATE Assign SET [Order] = ? WHERE AssignID = ?";
		executeNonQuery2(sql, new String[]{STATUSNAME, REFNO});
	}
	public void addContract2(String RefNo,String CONTNO, String CustomerID,String OrganizationCode,String STATUS){
/*		String sql = "INSERT INTO Contract (RefNo, CONTNO, CustomerID, OrganizationCode, STATUS, StatusCode, SALES, TotalPrice)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";


		executeNonQuery3(sql, new String[]{CONTNO,CustomerID, OrganizationCode, STATUS,StatusCode,valueOf(SALES), valueOf(TotalPrice)});*/
		String sql = "INSERT INTO Contract (RefNo,CONTNO, CustomerID, OrganizationCode, STATUS)"
				+ "VALUES( ?, ?, ?, ?)";
		executeNonQuery3(sql, new String[]{RefNo,CONTNO,CustomerID, OrganizationCode, STATUS});
	}

	public void addContract(String RefNo,String CONTNO, String CustomerID,String OrganizationCode,
							String STATUS,String StatusCode,String SALES,String TotalPrice,String EFFDATE,
							String HasTradeIn,String TradeInProductCode,String TradeInBrandCode,String TradeInProductModel,
							String TradeInDiscount,String PreSaleSaleCode,String PreSaleEmployeeCode,
							String PreSaleEmployeeName,String PreSaleTeamCode,String SaleCode,String SaleEmployeeCode,
							String SaleTeamCode,String InstallerSaleCode,String InstallerEmployeeCode,String InstallerTeamCode,
							String InstallDate,String ProductSerialNumber,String ProductID,String SaleEmployeeLevelPath,
							String MODE,String FortnightID,String ProblemID,String svcontno,String isActive,String MODEL,
							String fromrefno,String fromcontno,String todate,String tocontno,String torefno,String CreateDate,
							String CreateBy,String LastUpdateDate,String LastUpdateBy,String SyncedDate,String SaleSubTeamCode,
							String TradeInReturnFlag,String IsReadyForSaleAudit,String ContractReferenceNo,String IsMigrate ) {
		String sql = "INSERT INTO Contract (RefNo,CONTNO, CustomerID, OrganizationCode, STATUS, StatusCode, SALES, TotalPrice, EFFDATE, HasTradeIn, "
				+ "TradeInProductCode, TradeInBrandCode, TradeInProductModel, TradeInDiscount, PreSaleSaleCode, PreSaleEmployeeCode, PreSaleEmployeeName, PreSaleTeamCode, "
				+ "SaleCode, SaleEmployeeCode, SaleTeamCode, InstallerSaleCode, InstallerEmployeeCode, InstallerTeamCode, InstallDate, ProductSerialNumber, "
				+ "ProductID, SaleEmployeeLevelPath, MODE, FortnightID, ProblemID, svcontno, isActive, MODEL, fromrefno, fromcontno, todate, tocontno, "
				+ "torefno, CreateDate,  CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate, SaleSubTeamCode, TradeInReturnFlag, IsReadyForSaleAudit, ContractReferenceNo, IsMigrate)"
				+ "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		executeNonQuery3(sql, new String[]{RefNo,CONTNO,CustomerID, OrganizationCode, STATUS, StatusCode,valueOf(SALES),
				valueOf(TotalPrice), valueOf(EFFDATE), valueOf(HasTradeIn),TradeInProductCode, TradeInBrandCode,
				TradeInProductModel, valueOf(TradeInDiscount), PreSaleSaleCode, PreSaleEmployeeCode, PreSaleEmployeeName, PreSaleTeamCode, SaleCode,
				SaleEmployeeCode, SaleTeamCode, InstallerSaleCode, InstallerEmployeeCode, InstallerTeamCode,
				valueOf(InstallDate), ProductSerialNumber, ProductID, SaleEmployeeLevelPath, valueOf(MODE), FortnightID,
				ProblemID, svcontno, valueOf(isActive), MODEL, fromrefno, fromcontno, valueOf(todate), tocontno,
				torefno, valueOf(CreateDate), CreateBy, valueOf(LastUpdateDate), LastUpdateBy, valueOf(SyncedDate),
				SaleSubTeamCode, valueOf(TradeInReturnFlag), valueOf(IsReadyForSaleAudit), ContractReferenceNo, valueOf(IsMigrate)});
	}

	public void addPayment(String Payment_PaymentID,String Payment_OrganizationCode,String Payment_SendMoneyID,String Payment_PaymentType,String Payment_PayPartial,String Payment_BankCode,
						   String Payment_ChequeNumber,String Payment_ChequeBankBranch,String Payment_ChequeDate,String Payment_CreditCardNumber,String Payment_CreditCardApproveCode,
						   String Payment_CreditEmployeeLevelPath,String Payment_TripID,String Payment_Status,String Payment_RefNo,String Payment_PayPeriod,String Payment_PayDate,String Payment_PAYAMT,
						   String Payment_CashCode,String Payment_EmpID,String Payment_TeamCode,String Payment_receiptkind,String Payment_Kind,String Payment_BookNo,String Payment_ReceiptNo,String Payment_CreateDate,String Payment_CreateBy,
						   String Payment_LastUpdateDate,String Payment_LastUpdateBy,String Payment_SyncedDate ) {
		String sql = "INSERT INTO Payment (PaymentID, OrganizationCode, SendMoneyID, PaymentType, PayPartial, BankCode, ChequeNumber, ChequeBankBranch, "
				+ "ChequeDate, CreditCardNumber, CreditCardApproveCode, CreditEmployeeLevelPath, TripID, Status, RefNo, PayPeriod, PayDate, PAYAMT, CashCode, EmpID, TeamCode, "
				+ "receiptkind, Kind, BookNo, ReceiptNo, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		executeNonQuery2(sql, new String[]{Payment_PaymentID,
				Payment_OrganizationCode, Payment_SendMoneyID, Payment_PaymentType,
				valueOf(Payment_PayPartial),Payment_BankCode,Payment_ChequeNumber,
				Payment_ChequeBankBranch, Payment_ChequeDate, Payment_CreditCardNumber,
				Payment_CreditCardApproveCode, Payment_CreditEmployeeLevelPath,
				Payment_TripID, Payment_Status, Payment_RefNo, Payment_PayPeriod,
				valueOf(Payment_PayDate), valueOf(Payment_PAYAMT), Payment_CashCode,
				Payment_EmpID, Payment_TeamCode, Payment_receiptkind, Payment_Kind,
				Payment_BookNo, Payment_ReceiptNo, valueOf(Payment_CreateDate),
				Payment_CreateBy, valueOf(Payment_LastUpdateDate), Payment_LastUpdateBy,
				valueOf(Payment_SyncedDate)});
	}

	protected void executeNonQuery2(String sql, String[] args) {
		openDatabase2();
		try {
			if (args == null) {
				database.execSQL(sql);
			} else {
				database.execSQL(sql, args);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeDatabase2();
		}
	}

	private void openDatabase2() {
		database = DatabaseManager.getInstance().openDatabase();
	}
	private void closeDatabase2() {
		closeDatabase2(false);
	}
	private void closeDatabase2(boolean force) {
		if(force)
			DatabaseManager.getInstance().forceCloseDatabase();
		else
			DatabaseManager.getInstance().closeDatabase();
	}

	protected void executeNonQuery3(String sql, String[] args) {
		openDatabase3();
		try {
			if (args == null) {
				database.execSQL(sql);
			} else {
				database.execSQL(sql, args);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeDatabase3();
		}
	}

	private void openDatabase3() {
		database = DatabaseManager.getInstance().openDatabase();
	}
	private void closeDatabase3() {
		closeDatabase3(false);
	}
	private void closeDatabase3(boolean force) {
		if(force)
			DatabaseManager.getInstance().forceCloseDatabase();
		else
			DatabaseManager.getInstance().closeDatabase();
	}

	public static class ContractAdapter extends BHArrayAdapter<ContractInfo> {
		public ContractAdapter(Context context, int resource, List<ContractInfo> objects) {
			super(context, resource, objects);
		}

		public class ViewHolder {
			public TextView textViewContractnumber;
			public TextView textViewName;
			public TextView textViewStatus,textViewStatus2;
			public ImageView imageDelete;
			public ImageView imageNext;
		}

		int gg=0;
		@Override
		protected void onViewItem(final int position, View view, Object holder, final ContractInfo info) {
			// TODO Auto-generated method stub
			ViewHolder vh = (ViewHolder) holder;
            gg++;
            Log.e("position",position+","+gg);
            if(gg>148){
                Log.e("info.CONTNO",info.CONTNO);
				Log.e("info.svcontno",info.svcontno+"");
            }

			vh.textViewContractnumber.setText("เลขที่สัญญา  :  "+ info.CONTNO);
			vh.textViewName.setText	         ("ชื่อลูกค้า        :  "+ BHUtilities.trim(info.CustomerFullName) +" "+ BHUtilities.trim(info.CompanyName));
			vh.textViewStatus.setText        ("สถานะ           :  "+ info.StatusName);
			String SOU=	BHPreference.sourceSystem();
			if(SOU.equals("Credit")){
				vh.textViewStatus2.setVisibility(View.VISIBLE);
				String DDD =info.svcontno+"";
				if(DDD.equals("null")){
					vh.textViewStatus2.setText        ("ชำระเงิน           :  "+ "เครดิต");
				} else {
					vh.textViewStatus2.setText        ("ชำระเงิน           :  "+ info.svcontno+"");
				}
			} else {
				vh.textViewStatus2.setVisibility(View.GONE);
			}

			vh.imageDelete.setVisibility(View.GONE);

			vh.imageNext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}
			});
		}
	}

	private void bindContractList() {
		// TODO Auto-generated method stub
		textViewFinish.setVisibility(View.GONE);
		contractAdapter = new ContractAdapter(activity, R.layout.list_main_status, contractList);
		listViewFinish.setAdapter(contractAdapter);

		listViewFinish.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				try {
					BHPreference.setRefNo(contractList.get(position).RefNo);
					BHPreference.setProcessType(ProcessType.ViewCompletedContract.toString());
					final String refNo = contractList.get(position).RefNo;

					/*** [START] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/
					new BackgroundProcess(activity) {
						ContractInfo cont;
						@Override
						protected void calling() {
							// TODO Auto-generated method stub
							try {
								cont = TSRController.getContract(refNo);
								if(cont == null){
									TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), refNo);
								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
						@Override
						protected void after() {
							// TODO Auto-generated method stub
							if (cont != null) {
								showNextView(new SaleContractPrintFragment_setting2());
							}
						}
					}.start();
					/*** [END] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/

				} catch (NullPointerException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}
}
