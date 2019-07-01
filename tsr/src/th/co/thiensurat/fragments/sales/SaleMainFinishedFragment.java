package th.co.thiensurat.fragments.sales;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHPagerFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;

public class SaleMainFinishedFragment extends BHPagerFragment {

	@InjectView
	private TextView textViewFinish;
	@InjectView
	public static ListView listViewFinish;
	public static List<ContractInfo> contractList = null;

	public static ContractAdapter contractAdapter;

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
					} else {

						if (BHPreference.IsSaleForCRD()) {
							if (isContractDetails) {
								contractList = TSRController.getContractStatusFinishForCreditBySearch(BHPreference.organizationCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), "%%");
							} else {
								contractList = TSRController.getContractStatusFinishForCRD(BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), BHPreference.employeeID());
							}
						} else {
							contractList = TSRController.getContractStatusFinish(BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString());
						}
					}


				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (contractList != null) {					
					bindContractList();
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

	public static class ContractAdapter extends BHArrayAdapter<ContractInfo> {
		public ContractAdapter(Context context, int resource, List<ContractInfo> objects) {
			super(context, resource, objects);
		}

		public class ViewHolder {
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
