package th.co.thiensurat.fragments.sales;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

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

public class SaleMainUnfinishedFragment extends BHPagerFragment {

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
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		GetContractStatusUnFinish();
	}

	private void GetContractStatusUnFinish() {
		// TODO Auto-generated method stub
		new BackgroundProcess(activity) {
			@Override
			protected void calling() {
				// TODO Auto-generated method stub

				if (BHPreference.IsSaleForCRD()) {
					contractList = TSRController.getContractStatusUnFinishForCRD(BHPreference.organizationCode(), BHPreference.teamCode(),
							ContractStatusName.COMPLETED.toString(), BHPreference.employeeID());
				} else {
					contractList = TSRController.getContractStatusUnFinish(BHPreference.organizationCode(), BHPreference.teamCode(),
							ContractStatusName.COMPLETED.toString());
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
						List<ContractInfo> contractListUpdate = TSRController.getContractStatusUnFinishForCRD(BHPreference.organizationCode(), BHPreference.teamCode(),
								ContractStatusName.COMPLETED.toString(), BHPreference.employeeID());
						contractList.addAll(contractListUpdate);
					} else {
						List<ContractInfo> contractListUpdate = TSRController.getContractStatusUnFinish(BHPreference.organizationCode(), BHPreference.teamCode(),
								ContractStatusName.COMPLETED.toString());
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
			}

			@Override
			protected void onViewItem(final int position, View view, Object holder, final ContractInfo info) {
				// TODO Auto-generated method stub
				ViewHolder vh = (ViewHolder) holder;
				if(info.CONTNO.equals(info.RefNo)) {
					vh.textViewContractnumber.setText("หมายเลขเครื่อง  :  " + info.ProductSerialNumber);
				} else {
					vh.textViewContractnumber.setText("เลขที่สัญญา  :  " + info.CONTNO);
				}
				
				vh.textViewName.setText          ("ชื่อลูกค้า        :  "+ BHUtilities.trim(info.CustomerFullName) +" "+ BHUtilities.trim(info.CompanyName));
				vh.textViewStatus.setText        ("สถานะ           :  "+ info.StatusName);
				if ((info.StatusCode != null) && (!info.StatusCode.equals("")))
				{
					if (Integer.parseInt(info.StatusCode) >= 7) {
						vh.imageDelete.setVisibility(View.GONE);
					}else{
						vh.imageDelete.setVisibility(View.VISIBLE);
					}
				}
				vh.imageDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final String title = "คำเตือน";
						final String message = "คุณต้องการลบข้อมูลสัญญา(" + info.CONTNO + ") ของการขายหมายเลขเครื่อง " + info.ProductSerialNumber + " หรือไม่?";
						showYesNoDialogBox(title, message, info);
						
//						Builder setupAlert;
//						setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
//								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog, int whichButton) {
//										(new BackgroundProcess(activity) {
//											ProductStockInfo productInfo;
//											@Override
//											protected void calling() {
//												// TODO Auto-generated method
//												// stub
//												deleteContract(info.RefNo, info.CustomerID);
//												productInfo = getProductStock(info.ProductSerialNumber, ProductStockStatus.SOLD);
//
//											}
//
//											@Override
//											protected void after() {
//												// TODO Auto-generated method
//												// stub
//												if(productInfo != null)
//												{ 
//													(new BackgroundProcess(activity) {
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
//														// method
//														// stub
//														GetContractStatusUnFinish();
//														showMessage("ลบข้อมูลสัญญาเลขที่ " + info.CONTNO + " เรีบยร้อยแล้ว");
//													}
//												}).start();
//												} 
//											}
//										}).start();
//									}
//								}).setNegativeButton("No", new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog, int whichButton) {
//
//									}
//								});
//						setupAlert.show();
						
						
						
					}
				});
			}
		};
		
		listViewUnfinish.setAdapter(statusfinish);
		listViewUnfinish.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				SaleUnfinishedFragment.Data data = new SaleUnfinishedFragment.Data();
				data.Statuscode = contractList.get(position).StatusCode;
				data.refno = contractList.get(position).RefNo;
				data.ProcessType = ProcessType.Sale;
				data.ProductSerialNumber = contractList.get(position).ProductSerialNumber;
				SaleUnfinishedFragment unfinish = BHFragment.newInstance(SaleUnfinishedFragment.class, data);
				showNextView(unfinish);
			}
		});
	}
	
	 private void showYesNoDialogBox(final String title, final String message, final ContractInfo cont) {
	        Builder setupAlert;
	        setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
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
