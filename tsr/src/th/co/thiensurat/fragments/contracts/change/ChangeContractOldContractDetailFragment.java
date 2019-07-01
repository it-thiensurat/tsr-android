package th.co.thiensurat.fragments.contracts.change;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChangeContractOldContractDetailFragment extends BHFragment {

	private final String LOGIN_ORGANIZATION_CODE = BHPreference.organizationCode();//"0";

	private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());

	public static class Data extends BHParcelable {
		
		public String oldRefNo;
	}
	@InjectView private TextView txtEFFDate;
	@InjectView private TextView txtContNo;
	@InjectView private TextView txtCustomerFullName;
	@InjectView private TextView txtIDCard;
	@InjectView private TextView txtAddressIDCard;
	@InjectView private TextView txtAddressInstall;
	@InjectView private TextView txtProduct;
	@InjectView
	private TextView txtSerialNo;

	@InjectView private LinearLayout linearLayoutSaleAmount;
	@InjectView private TextView txtSaleAmount;

	@InjectView private LinearLayout linearLayoutSaleDiscount;
	@InjectView private TextView txtSaleDiscount;

	@InjectView private LinearLayout linearLayoutSaleNetAmount;
	@InjectView private TextView txtSaleNetAmount;

	@InjectView private LinearLayout linearLayoutFirstPaymentAmount;
	@InjectView private TextView txtFirstPaymentAmount;

	@InjectView private LinearLayout linearLayoutTwoPaymentAmount;
	@InjectView private TextView txtTwoPaymentAmount;

	@InjectView private LinearLayout linearLayoutNextPaymentAmount;
	@InjectView private TextView txtNextPaymentAmountLabel;
	@InjectView private TextView txtNextPaymentAmountValue, lblCustomerFullName;

	private Data dataOldContract;
	private ContractInfo contract = null;
	private List<SalePaymentPeriodInfo> spp = null;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_change_contract_old_contract_detail;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_change_contract;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_next };
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_next: {
				ChangeContractDetailFragment.Data data1 = new ChangeContractDetailFragment.Data();
				data1.oldRefNo = contract.RefNo;
				ChangeContractDetailFragment fmCCDetail = BHFragment.newInstance(ChangeContractDetailFragment.class, data1);
				showNextView(fmCCDetail);
			break;
		}
		case R.string.button_back:
			showLastView();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dataOldContract = getData();
		if (dataOldContract.oldRefNo.equals("")) {
			final String title = "กรุณาตรวจสอบสินค้า";
			String message = "ไม่พบข้อมูลสัญญาซื้อขาย!";
			Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
					.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							// TODO Auto-generated method stub
						}
					});
			setupAlert.show();
			//showMessage("ไม่พบข้อมูลสัญญาซื้อขาย!");
		} else {
			displayOldContractAndSPP();
		}
	}

	private void displayOldContractAndSPP() {

		// (1) Get Contract detail
		(new BackgroundProcess(activity) {

			ContractInfo resultContract;
			List<SalePaymentPeriodInfo> resultSPP;

			@Override
			protected void before() {
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				try {
					if(isCredit){
						resultContract = getContractByRefNoForCredit(LOGIN_ORGANIZATION_CODE, dataOldContract.oldRefNo);
					} else {
						resultContract = getContractByRefNo(LOGIN_ORGANIZATION_CODE, dataOldContract.oldRefNo);
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					if (resultContract != null) {
						contract = resultContract;
						// (2) Get SalePaymentPeriod of this contract
						new BackgroundProcess(activity) {
							@Override
							protected void calling() {
								try {
									resultSPP = getSalePaymentPeriodByRefNo(resultContract.RefNo);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@Override
							protected void after() {
								// TODO Auto-generated method stub
								super.after();
								spp = resultSPP;
								// (3) Binding contract
								bindContract();
								
								bindAddress(resultContract.RefNo);
//								bindAddressIDCard(resultContract.RefNo);
//								try {
//									Thread.sleep(1000);
//								} catch (InterruptedException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//								
//								bindAddressInstall(resultContract.RefNo);
//								try {
//									Thread.sleep(1000);
//								} catch (InterruptedException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
							}
						}.start();

					} else {
						final String title = "กรุณาตรวจสอบสินค้า";
						String message = "ไม่พบสินค้า!";
						Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
								.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int whichButton) {
										// TODO Auto-generated method stub
									}
								});
						setupAlert.show();
						//showMessage("ไม่พบสินค้า!");
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();

	}

	
	
	private void bindContract() {
		try {
			String titleLblCustomerFullName = contract.MODE > 1 ? "" + getResources().getString(R.string.change_contract_customer_fullname) : ""
					+ getResources().getString(R.string.change_contract_customer_fullname_cash);
			lblCustomerFullName.setText(titleLblCustomerFullName);

			txtEFFDate.setText(BHUtilities.dateFormat(contract.EFFDATE));
			txtContNo.setText(BHUtilities.trim(contract.CONTNO));
			//txtCustomerFullName.setText(BHUtilities.trim(contract.CustomerFullName));
			txtCustomerFullName.setText(String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));

			txtIDCard.setText(BHUtilities.trim(contract.IDCard));

			txtProduct.setText(BHUtilities.trim(contract.ProductName));
			txtSerialNo.setText(BHUtilities.trim(contract.ProductSerialNumber));

			txtSaleAmount.setText(BHUtilities.numericFormat(contract.SALES));
			txtSaleDiscount.setText(BHUtilities.numericFormat(contract.TradeInDiscount));
			txtSaleNetAmount.setText(BHUtilities.numericFormat(contract.TotalPrice));

			if (contract.TradeInDiscount == 0) {
				linearLayoutSaleAmount.setVisibility(View.GONE);
				linearLayoutSaleDiscount.setVisibility(View.GONE);
			}

			if (spp != null) {
				if (spp.size() > 0) {
					txtFirstPaymentAmount.setText(BHUtilities.numericFormat(spp.get(0).NetAmount));

					if(spp.size() != 1) {
						if (spp.size() == 2) {
							linearLayoutNextPaymentAmount.setVisibility(View.GONE);
							txtTwoPaymentAmount.setText(BHUtilities.numericFormat(spp.get(1).NetAmount));
						} else {
							if (spp.get(1).NetAmount == spp.get(2).NetAmount) {
								linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
								txtNextPaymentAmountLabel.setText("งวดที่ 2 ถึง " + BHUtilities.numericFormat(spp.size()) + " ต้องชำระงวดละ");
								txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(spp.get(1).NetAmount));
							} else {
								if (spp.size() == 3) {

									txtNextPaymentAmountLabel.setText("งวดที่ 3 ที่ต้องชำระงวดละ");
									txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(spp.get(2).NetAmount));
								} else {
									txtNextPaymentAmountLabel.setText("งวดที่ 3 ถึง " + BHUtilities.numericFormat(spp.size()) + " ต้องชำระงวดละ");
									txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(spp.get(2).NetAmount));
								}
							}
						}
					} else {
						linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
						linearLayoutNextPaymentAmount.setVisibility(View.GONE);
					}
				} else {
					linearLayoutFirstPaymentAmount.setVisibility(View.GONE);
					linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
					linearLayoutNextPaymentAmount.setVisibility(View.GONE);
				}
			} else {
				linearLayoutFirstPaymentAmount.setVisibility(View.GONE);
				linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
				linearLayoutNextPaymentAmount.setVisibility(View.GONE);
				final String title = "กรุณาตรวจสอบสินค้า";
				String message = "ไม่พบข้อมูลงวดการชำระเงิน!";
				Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
						.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int whichButton) {
								// TODO Auto-generated method stub
							}
						});
				setupAlert.show();
				//showMessage("ไม่พบข้อมูลงวดการชำระเงิน!");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void bindAddress(final String mRefNo) {
		(new BackgroundProcess(activity) {

			AddressInfo output = null;

			@Override
			protected void before() {
				// TODO Auto-generated method stub
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getAddress(mRefNo, AddressType.AddressIDCard);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					String addressString = "";
					if (output != null) {
						addressString = output.Address();
					}
					txtAddressIDCard.setText(addressString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				(new BackgroundProcess(activity) {

					AddressInfo output2 = null;

					@Override
					protected void before() {
						// TODO Auto-generated method stub
					}

					@Override
					protected void calling() {
						// TODO Auto-generated method stub
						output2 = getAddress(mRefNo, AddressType.AddressInstall);
					}

					@Override
					protected void after() {
						// TODO Auto-generated method stub
						try {
							String addressString = "";
							if (output2 != null) {
								addressString = output2.Address();
							}
							txtAddressInstall.setText(addressString);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}).start();
	}
	
//	private void bindAddressIDCard(final String mRefNo) {
//
//		(new BackgroundProcess(activity) {
//
//			AddressInfo output = null;
//
//			@Override
//			protected void before() {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			protected void calling() {
//				// TODO Auto-generated method stub
//				output = getAddress(mRefNo, AddressType.AddressIDCard);
//			}
//
//			@Override
//			protected void after() {
//				// TODO Auto-generated method stub
//				try {
//					String addressString = "";
//					if (output != null) {
//						addressString = output.Address();
//					}
//					txtAddressIDCard.setText(addressString);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
//
//	private void bindAddressInstall(final String mRefNo) {
//
//		(new BackgroundProcess(activity) {
//
//			AddressInfo output = null;
//
//			@Override
//			protected void before() {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			protected void calling() {
//				// TODO Auto-generated method stub
//				output = getAddress(mRefNo, AddressType.AddressInstall);
//			}
//
//			@Override
//			protected void after() {
//				// TODO Auto-generated method stub
//				try {
//					String addressString = "";
//					if (output != null) {
//						addressString = output.Address();
//					}
//					txtAddressInstall.setText(addressString);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}

}
