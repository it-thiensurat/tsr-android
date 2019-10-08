package th.co.thiensurat.fragments.sales;

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
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.DatabaseManager;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.retrofit.api.Service;

import static java.lang.String.valueOf;
import static th.co.thiensurat.retrofit.api.client.BASE_URL;

public class SaleMainFinishedFragment extends BHPagerFragment {

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

								//Log.e("1111","2222");
							} else {
								contractList = TSRController.getContractStatusFinishForCRD(BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), BHPreference.employeeID());
							//	Log.e("1111","3333");
							}
						} else {
							contractList = TSRController.getContractStatusFinish(BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString());
							//Log.e("1111","4444");
						}
					}

					//Log.e("TEST_SPEED","1111");

                   Log.e("contractList_SI",contractList.size()+"");








				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (contractList != null) {
					//Log.e("TEST_SPEED","222222");

					if (isConnectingToInternet()) {


						String SOU=	BHPreference.sourceSystem();
						if(SOU.equals("Credit")){

							dialog = ProgressDialog.show(activity, "",
									"Loading...", true);
							load_data();
						}
						else {
							bindContractList();
						}

					}
					else {
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

						Log.e("data","1");
						Log.e("jsonObject",jsonObject.toString());
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
		Log.e("array.length()", valueOf(array.length()));


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

				String EFFDATE=json.getJSONObject("EFFDATE").getString("date")+"";
				String InstallDate=json.getJSONObject("InstallDate").getString("date")+"";
				String todate=json.getJSONObject("todate").getString("date")+"";
				String CreateDate=json.getJSONObject("CreateDate").getString("date")+"";
				String LastUpdateDate=json.getJSONObject("LastUpdateDate").getString("date")+"";
				String SyncedDate=json.getJSONObject("SyncedDate").getString("date")+"";

			//	Log.e("FFFF",PaymentComplete);
				if(!CONTNO.equals("null")){

					updateAssignForPostpone(PayStatus,CONTNO);
					//addContract2(RefNo,CONTNO,CustomerID, OrganizationCode, STATUS);
//Log.e("IsMigrate",IsMigrate);


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


	//Log.e("RefNo",RefNo);
				//	TSRController.importContractFromServer(OrganizationCode,SaleTeamCode,RefNo);

					 //TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), RefNo);

					//if (contractList != null) {
					//	TSRController.importContractFromServer(BHPreference.organizationCode(), null, RefNo);
					//}

	/*				try {
						BHPreference.setRefNo(RefNo);
						BHPreference.setProcessType(ProcessType.ViewCompletedContract.toString());
						final String refNo = RefNo;

						*//*** [START] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***//*
						new BackgroundProcess(activity) {
							ContractInfo cont;
							@Override
							protected void calling() {
								// TODO Auto-generated method stub
								try {
									cont = TSRController.getContract(refNo);
									if(cont == null){

										TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), refNo);
										Log.e("xxxx",refNo);
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
									//TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), refNo);
									//Log.e("xxxx",refNo);
								}
							}
						}.start();
						*//*** [END] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***//*

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
*/
					//updateAssignForPostpone(PayStatus,CONTNO);


				}


			} catch (JSONException e) {
				//Log.e("Exception", e.getLocalizedMessage());
				e.printStackTrace();



			}
		}

		try {
			dialog.dismiss();
		}
		catch (Exception ex){

		}









		bindContractList();
	}

	private SQLiteDatabase database = null;
	public void updateAssignForPostpone(String STATUSNAME,String CONTNO) {

		String sql = "update Contract set [svcontno] = ? WHERE CONTNO =?";
		//  String sql = "UPDATE Assign SET [Order] = ? WHERE AssignID = ?";
		executeNonQuery2(sql, new String[]{STATUSNAME, CONTNO});
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


		Log.e("aaaaaaa",RefNo+CONTNO+CustomerID+ OrganizationCode+ STATUS+ StatusCode+valueOf(SALES)+
				valueOf(TotalPrice)+ valueOf(EFFDATE)+ valueOf(HasTradeIn)+TradeInProductCode+ TradeInBrandCode+
				TradeInProductModel+ valueOf(TradeInDiscount)+ PreSaleSaleCode+ PreSaleEmployeeCode+ PreSaleEmployeeName+ PreSaleTeamCode+ SaleCode+
				SaleEmployeeCode+ SaleTeamCode+ InstallerSaleCode+ InstallerEmployeeCode+ InstallerTeamCode+
				valueOf(InstallDate)+ ProductSerialNumber+ ProductID+ SaleEmployeeLevelPath+ valueOf(MODE)+ FortnightID+
				ProblemID+ svcontno+ valueOf(isActive)+ MODEL+ fromrefno+ fromcontno+ valueOf(todate)+ tocontno+
				torefno+ valueOf(CreateDate)+ CreateBy+ valueOf(LastUpdateDate)+ LastUpdateBy+ valueOf(SyncedDate)+
				SaleSubTeamCode+ valueOf(TradeInReturnFlag)+ valueOf(IsReadyForSaleAudit)+ ContractReferenceNo+ valueOf(IsMigrate));
		executeNonQuery3(sql, new String[]{RefNo,CONTNO,CustomerID, OrganizationCode, STATUS, StatusCode,valueOf(SALES),
				valueOf(TotalPrice), valueOf(EFFDATE), valueOf(HasTradeIn),TradeInProductCode, TradeInBrandCode,
				TradeInProductModel, valueOf(TradeInDiscount), PreSaleSaleCode, PreSaleEmployeeCode, PreSaleEmployeeName, PreSaleTeamCode, SaleCode,
				SaleEmployeeCode, SaleTeamCode, InstallerSaleCode, InstallerEmployeeCode, InstallerTeamCode,
				valueOf(InstallDate), ProductSerialNumber, ProductID, SaleEmployeeLevelPath, valueOf(MODE), FortnightID,
				ProblemID, svcontno, valueOf(isActive), MODEL, fromrefno, fromcontno, valueOf(todate), tocontno,
				torefno, valueOf(CreateDate), CreateBy, valueOf(LastUpdateDate), LastUpdateBy, valueOf(SyncedDate),
				SaleSubTeamCode, valueOf(TradeInReturnFlag), valueOf(IsReadyForSaleAudit), ContractReferenceNo, valueOf(IsMigrate)});
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
				}
				else {
					vh.textViewStatus2.setText        ("ชำระเงิน           :  "+ info.svcontno+"");
				}
			}
			else {
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

		/*BHArrayAdapter<ContractInfo> statusfinish = new BHArrayAdapter<ContractInfo>(activity, R.layout.list_main_status, contractList) {
			class ViewHolder {
				public TextView textViewContractnumber;
				public TextView textViewName;
				public TextView textViewStatus;
				public ImageView imageDelete;
				public ImageView imageNext;
			}

			@Override
			protected void onViewItem(final int position, View view, Object holder, final ContractInfo info) {
				// TODO Auto-generated method stub
				ViewHolder vh = (ViewHolder) holder;
				vh.textViewContractnumber.setText("เลขที่สัญญา  :  "+ info.CONTNO);
				vh.textViewName.setText	         ("ชื่อลูกค้า        :  "+ BHUtilities.trim(info.CustomerFullName) +" "+ BHUtilities.trim(info.CompanyName));
				vh.textViewStatus.setText        ("สถานะ           :  "+ info.StatusName);
				vh.imageDelete.setVisibility(View.GONE);

//				vh.imageDelete.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						final String title = "คำเตือน";
//						final String message = "คุณต้องการลบข้อมมูลสัญญา  " + info.CONTNO + " ?";
//						Builder setupAlert;
//						setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
//								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog, int whichButton) {
//										(new BackgroundProcess(activity) {
//											ProductStockInfo productInfo;
//
//											@Override
//											protected void calling() {
//												// TODO Auto-generated method
//												// stub
//												deleteContract(info.RefNo, "");
//												productInfo = getProductStock(info.ProductSerialNumber, ProductStockStatus.SOLD);
//											}
//
//											@Override
//											protected void after() {
//												// TODO Auto-generated method
//												// stub
//												(new BackgroundProcess(activity) {
//													@Override
//													protected void before() {
//														productInfo.ProductSerialNumber = info.ProductSerialNumber;
//														productInfo.OrganizationCode = BHPreference.organizationCode();
//														productInfo.Status = ProductStockStatus.CHECKED.toString();
//														productInfo.ScanByTeam = BHPreference.teamCode();
//														productInfo.ScanDate = new Date();
//													}
//
//													@Override
//													protected void calling() {
//														// TODO Auto-generated
//														// method stub
//														updateProductStockStatus(productInfo, true, true);
//													}
//
//													@Override
//													protected void after() {
//														// TODO Auto-generated
//														// method stub
//														GetContractStatusFinish();
//														showMessage("ลบข้อมมูลสัญญาเลขที่ " + info.CONTNO + " เรีบยร้อยแล้ว");
//													}
//												}).start();
//											}
//										}).start();
//									}
//								}).setNegativeButton("No", new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog, int whichButton) {
//									}
//								});
//						setupAlert.show();
//					}
//				});
				vh.imageNext.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				});
			}
		};
		listViewFinish.setAdapter(statusfinish);*/
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
								showNextView(new SaleContractPrintFragment());
							}
						}
					}.start();
					/*** [END] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}
}
