package th.co.thiensurat.fragments.sales;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.BankInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SaleDetailCheckContractFinishedFragment extends BHFragment {

	public static class Data extends BHParcelable {
		public String refNo;
	}

	@InjectView
	private TextView textViewDateConteact;
	@InjectView
	private TextView textViewName;
	@InjectView
	private TextView textViewIDcard;
	@InjectView
	private TextView textViewAddress;
	@InjectView
	private TextView textViewInstall;
	@InjectView
	private TextView textViewPrice;
	@InjectView
	private TextView textViewDiscount;
	@InjectView
	private TextView textViewPriceSum;
	@InjectView
	private TextView textViewPriceFirstInstallment;
	@InjectView
	private TextView textViewPriceInstallment;
	@InjectView
	private TextView textViewProductSerialNumber;
	@InjectView
	private TextView textViewProduct;
	@InjectView
	private TextView textViewInstallment;
	@InjectView
	private TextView textViewContractNumber;
	@InjectView
	private LinearLayout linearLayoutFirstInstallment;
	@InjectView
	private LinearLayout linearLayoutInstallment;

	private static final String LOGIN_ORGANIZATION_CODE = "0";

	private Data dataContract;
	private ContractInfo contract;
	private List<SalePaymentPeriodInfo> sppList;
	private PaymentInfo payment;
	private BankInfo bank;

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_sales;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_detail_check_contract;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_print,
				R.string.button_receipt };
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_back:
			showLastView();
			break;
		case R.string.button_print:

			break;
		case R.string.button_receipt:
			getFirstPaymentInfo();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		dataContract = getData();
		if (dataContract.refNo.equals("")) {
			showMessage("ไม่พบข้อมูลสัญญาซื้อขาย!");
		} else {
			getContractAndSPP();
		}
	}

	private void getContractAndSPP() {

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
					resultContract = getContractByRefNo(
							LOGIN_ORGANIZATION_CODE, dataContract.refNo);
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
								sppList = resultSPP;
								// (3) Binding contract
								bindContract();
								bindAddressCard();
								bindAddressInstall();
							}
						}.start();

					} else {
						showMessage("ไม่พบสินค้า!");
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
			textViewDateConteact.setText(BHUtilities
					.dateFormat(contract.EFFDATE));
			textViewContractNumber.setText(BHUtilities.trim(contract.CONTNO));
			//textViewName.setText(BHUtilities.trim(contract.CustomerFullName));
			textViewName.setText(String.format("%s%s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));

			textViewIDcard.setText(BHUtilities.trim(contract.IDCard));

			textViewProduct.setText(BHUtilities.trim(contract.ProductName));
			textViewProductSerialNumber.setText(BHUtilities
					.trim(contract.ProductSerialNumber));

			textViewPrice.setText(BHUtilities.numericFormat(contract.SALES));
			textViewDiscount.setText(BHUtilities
					.numericFormat(contract.TradeInDiscount));
			textViewPriceSum.setText(BHUtilities
					.numericFormat(contract.TotalPrice));

			if (sppList != null) {
				if (sppList.size() > 1) {
					textViewPriceFirstInstallment.setText(BHUtilities
							.numericFormat(sppList.get(0).NetAmount));
					textViewInstallment.setText("งวดที่ 2 ถึง "
							+ BHUtilities.numericFormat(sppList.size())
							+ " ต้องชำระงวดละ");
					textViewPriceInstallment.setText(BHUtilities
							.numericFormat(sppList.get(1).NetAmount));
				} else {
					linearLayoutFirstInstallment.setVisibility(View.GONE);
					linearLayoutInstallment.setVisibility(View.GONE);
				}
			} else {
				linearLayoutFirstInstallment.setVisibility(View.GONE);
				linearLayoutInstallment.setVisibility(View.GONE);
				showMessage("ไม่พบข้อมูลงวดการชำระเงิน!");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void bindAddressCard() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

			AddressInfo output = null;

			@Override
			protected void before() {
				// TODO Auto-generated method stub
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getAddress(dataContract.refNo,
						AddressType.AddressIDCard);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				String addressString = "";
				if (output != null) {
					addressString = output.Address();
				}
				textViewAddress.setText(addressString);
			}

		}).start();
	}

	private void bindAddressInstall() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {
			AddressInfo output = null;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getAddress(dataContract.refNo,
						AddressType.AddressInstall);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				String addressString = "";
				if (output != null) {
					addressString = output.Address();
				}
				textViewInstall.setText(addressString);
			}

		}).start();
	}

	private void getFirstPaymentInfo() {

		// (1) Get payment detail
		(new BackgroundProcess(activity) {

			List<PaymentInfo> result;
			List<BankInfo> result2;

			@Override
			protected void before() {
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				try {
					result = getPaymentByRefNo(LOGIN_ORGANIZATION_CODE,
							dataContract.refNo);
					result2 = getBank();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					if (result != null) {
						// (1-A) Had been payment
						payment = result.get(0);
						// (2) Get bank detail of this payment
						if ((payment.BankCode != null)
								&& (!payment.BankCode.equals(""))) {
							for (BankInfo item : result2) {
								if (payment.BankCode.equals(item.BankCode)) {
									bank = item;
									break;
								}
							}
						} // (2)
						// (3) Go to next page for display receipt of
						// FirstPayment
						/*SaleReceiptFirstPayment.Data dataReceipt = new SaleReceiptFirstPayment.Data();
						dataReceipt.refNo = contract.RefNo;
						dataReceipt.processType = ProcessType.FirstPayment;
						dataReceipt.payment = payment; // PaymentInfo
						dataReceipt.contract = contract;
						dataReceipt.bank = bank; // BankInfo
						SaleReceiptFirstPayment fmReceipt = BHFragment
								.newInstance(SaleReceiptFirstPayment.class,
										dataReceipt);
						showNextView(fmReceipt);*/



						//SaleReceiptPayment.Data dataReceiptID = new SaleReceiptPayment.Data();
						SaleReceiptPayment_old.Data dataReceiptID = new SaleReceiptPayment_old.Data();

						dataReceiptID.PaymentID = payment.PaymentID;

						//SaleReceiptPayment fmReceipt = BHFragment.newInstance(SaleReceiptPayment.class, dataReceiptID);

						SaleReceiptPayment_old fmReceipt = BHFragment.newInstance(SaleReceiptPayment_old.class, dataReceiptID);


						showNextView(fmReceipt);
					} else {
						// (1-B) Never had payment
						showMessage("ไม่พบข้อมูลการชำระเงินงวดแรก!");
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();

	}

}