package th.co.thiensurat.fragments.sales.preorder_setting;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPagerFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ProductStockController.ProductStockStatus;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatusName;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.sales.SaleUnfinishedFragment;
import static th.co.thiensurat.fragments.sales.preorder_setting.SaleMainFragment_preorder_setting.select_page_s;

public class SaleMainUnfinishedFragment_preorder_setting extends BHPagerFragment {

	@InjectView
	private TextView textViewUnfinished;


	@InjectView
	private ListView listViewUnfinish;
	private List<ContractInfo> contractList;

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_sales;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_main_unfinished;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState)
	{
	select_page_s=0;

		GetContractStatusUnFinish();
	}

	private void GetContractStatusUnFinish() {
		// TODO Auto-generated method stub
		new BackgroundProcess(activity) {
			@Override
			protected void calling() {
				// TODO Auto-generated method stub

				if (BHPreference.IsSaleForCRD()) {
					Log.e("qqqqq","1");
					contractList = TSRController.getContractStatusUnFinishForCRD_SETTING(BHPreference.organizationCode(), BHPreference.teamCode(),
							ContractStatusName.COMPLETED.toString(), BHPreference.employeeID(),BHApplication.getInstance().getPrefManager().getPreferrence("ProductSerialNumber_MS"));
				}
				else if (BHPreference.IsSaleForTS()) {
					Log.e("qqqqq","2");
					contractList = TSRController.getContractStatusUnFinishForCRD_SETTING(BHPreference.organizationCode(), BHPreference.teamCode(),
							ContractStatusName.COMPLETED.toString(), BHPreference.employeeID(),BHApplication.getInstance().getPrefManager().getPreferrence("ProductSerialNumber_MS"));
				}
				else {
					Log.e("qqqqq","3");
					contractList = TSRController.getContractStatusUnFinish_SETTING(BHPreference.organizationCode(), BHPreference.teamCode(),
							ContractStatusName.COMPLETED.toString(),BHApplication.getInstance().getPrefManager().getPreferrence("ProductSerialNumber_MS"));
				}

                boolean updateContractList = false;
                if(contractList != null && contractList.size() > 0){
                    for(ContractInfo info : contractList ){
                        if(info.StatusCode.equals("07") || info.StatusCode.equals("08")){
                            SalePaymentPeriodInfo firstPaymentComplete = new SalePaymentPeriodController()
                                    .getSalePaymentPeriodInfoByRefNoAndPaymentPeriodNumber(info.RefNo, 1);
                            if(firstPaymentComplete != null && firstPaymentComplete.PaymentComplete){
                                TSRController.updateStatusCode(info.RefNo, "11");
                                updateContractList = true;
                            }
                        }
                    }
                }
                if(updateContractList && contractList != null && contractList.size() > 0){
                    contractList.clear();
					if (BHPreference.IsSaleForCRD()) {
						List<ContractInfo> contractListUpdate = TSRController.getContractStatusUnFinishForCRD_SETTING(BHPreference.organizationCode(), BHPreference.teamCode(),
								ContractStatusName.COMPLETED.toString(), BHPreference.employeeID(),BHApplication.getInstance().getPrefManager().getPreferrence("ProductSerialNumber_MS"));
						contractList.addAll(contractListUpdate);
					}
					else  if (BHPreference.IsSaleForTS()) {
						List<ContractInfo> contractListUpdate = TSRController.getContractStatusUnFinishForCRD_SETTING(BHPreference.organizationCode(), BHPreference.teamCode(),
								ContractStatusName.COMPLETED.toString(), BHPreference.employeeID(),BHApplication.getInstance().getPrefManager().getPreferrence("ProductSerialNumber_MS"));
						contractList.addAll(contractListUpdate);
					}

					else {
						List<ContractInfo> contractListUpdate = TSRController.getContractStatusUnFinish_SETTING(BHPreference.organizationCode(), BHPreference.teamCode(),
								ContractStatusName.COMPLETED.toString(),BHApplication.getInstance().getPrefManager().getPreferrence("ProductSerialNumber_MS"));
						contractList.addAll(contractListUpdate);
					}
                }
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (contractList != null) {
					bindContractList();
				} else {
					listViewUnfinish.setVisibility(View.GONE);
					textViewUnfinished.setVisibility(View.VISIBLE);
				}
			}
		}.start();
	}

	private void bindContractList() {
		// TODO Auto-generated method stub
		textViewUnfinished.setVisibility(View.GONE);
		BHArrayAdapter<ContractInfo> statusfinish = new BHArrayAdapter<ContractInfo>(activity, R.layout.list_main_status, contractList) {
			class ViewHolder {
				public TextView textViewContractnumber;
				public TextView textViewName;
				public TextView textViewStatus;
				public ImageView imageDelete;
				public  LinearLayout aaa;
			}

			@Override
			protected void onViewItem(final int position, View view, Object holder, final ContractInfo info) {
				// TODO Auto-generated method stub
				ViewHolder vh = (ViewHolder) holder;



	/*			if(!info.ProductSerialNumber.equals("-")) {
					vh.aaa.setVisibility(View.VISIBLE);

				}
				else {
					vh.aaa.setVisibility(View.GONE);
				}*/




				if (info.CONTNO.equals(info.RefNo)) {
						vh.textViewContractnumber.setText("หมายเลขเครื่อง  :  " + info.ProductSerialNumber);
					} else {
						vh.textViewContractnumber.setText("เลขที่สัญญา  :  " + info.CONTNO);
					}

					vh.textViewName.setText("ชื่อลูกค้า        :  " + BHUtilities.trim(info.CustomerFullName) + " " + BHUtilities.trim(info.CompanyName));
					vh.textViewStatus.setText("สถานะ           :  " + info.StatusName);
					if ((info.StatusCode != null) && (!info.StatusCode.equals(""))) {
						if (Integer.parseInt(info.StatusCode) >= 7) {
							vh.imageDelete.setVisibility(View.GONE);
						} else {
							vh.imageDelete.setVisibility(View.VISIBLE);
						}
					}
					vh.imageDelete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {



/*						// TODO Auto-generated method stub
						final String title = "คำเตือน";
						final String message = "คุณต้องการลบข้อมูลสัญญา(" + info.CONTNO + ") ของการขายหมายเลขเครื่อง " + info.ProductSerialNumber + " หรือไม่?";
						showYesNoDialogBox(title, message, info);
						*/


							LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
							View mView = layoutInflaterAndroid.inflate(R.layout.custom_dialog_delete, null);
							Builder alertDialogBuilderUserInput = new Builder(activity);
							alertDialogBuilderUserInput.setView(mView);


							final TextView dialogTitle2 = (TextView) mView.findViewById(R.id.dialogTitle2);
							final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

							dialogTitle2.setText("คุณต้องการลบข้อมูลสัญญา(" + info.CONTNO + ") ของการขายหมายเลขเครื่อง " + info.ProductSerialNumber + " หรือไม่?");


							alertDialogBuilderUserInput
									.setCancelable(false)
									.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialogBox, int id) {

											String OK = userInputDialogEditText.getText().toString();
											if (OK.equals("ตกลง")) {

												deleteContract(info);
												Log.e("status", "ok");

											} else {
												Log.e("status", "error");
											}

											// ToDo get user input here
										}
									})

									.setNegativeButton("ยกเลิก",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialogBox, int id) {
													dialogBox.cancel();
												}
											});

							AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
							alertDialogAndroid.show();


						}
					});


				//}




			}
		};

		listViewUnfinish.setAdapter(statusfinish);
		listViewUnfinish.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				SaleUnfinishedFragment_preorder_setting.Data data = new SaleUnfinishedFragment_preorder_setting.Data();
				data.Statuscode = contractList.get(position).StatusCode;
				data.refno = contractList.get(position).RefNo;
				data.ProcessType = ProcessType.Sale;
				data.ProductSerialNumber = contractList.get(position).ProductSerialNumber;

				Log.e("qqqq",contractList.get(position).RefNo);

				BHApplication.getInstance().getPrefManager().setPreferrence("REFNO_R",contractList.get(position).RefNo);

				SaleUnfinishedFragment_preorder_setting unfinish = BHFragment.newInstance(SaleUnfinishedFragment_preorder_setting.class, data);
				showNextView(unfinish);
			}
		});
	}

	 private void showYesNoDialogBox(final String title, final String message, final ContractInfo cont) {
	        Builder setupAlert;
	        setupAlert = new Builder(activity).setTitle(title).setMessage(message)
	                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	deleteContract(cont);
	                    }
	                }).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                        // just do nothing
	                    }
	                });
	        setupAlert.show();
	    }
	 
	 private void deleteContract(final ContractInfo cont) {
		 (new BackgroundProcess(activity) {
			 ProductStockInfo ps = null;
			 
			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				deleteContract(cont.RefNo, cont.CustomerID);
				ps = getProductStock(cont.ProductSerialNumber, ProductStockStatus.SOLD);
				if (ps !=null) {
					ps.ProductSerialNumber = cont.ProductSerialNumber;
					ps.OrganizationCode = BHPreference.organizationCode();
					ps.Status = ProductStockStatus.CHECKED.toString();
					ps.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.teamCode();
					ps.ScanDate = new Date();
					updateProductStock(ps,true);
				}
			}
			
			@Override
			protected void after() {
				// TODO Auto-generated
				// method stub
				GetContractStatusUnFinish();
				showMessage("ลบข้อมูลสัญญาเลขที่ " + cont.CONTNO + " เรียบร้อยแล้ว");
			}
			
		}).start();
	 }
}
