package th.co.thiensurat.fragments.payment.first;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class FirstPaymentCustomerDetailFragment extends BHFragment {

	private ContractInfo contract;
	private AddressInfo addressIDCard;
	private AddressInfo addressInstall;
	@InjectView
	private TextView txtEffDate;
	@InjectView
	private TextView txtContno;
	@InjectView
	private TextView txtCustomerFullName;
	@InjectView
	private TextView txtAddressIDCard;
	@InjectView
	private TextView txtAddressInstall;
	@InjectView
	private TextView txtModel;
	@InjectView
	private TextView txtProductSerialNumber;
	@InjectView
	private TextView txtSales;
	@InjectView
	private TextView txtTradeInDiscount;
	@InjectView
	private TextView txtTotalPrice;
	@InjectView
	private TextView txtIDCard;
	@InjectView
	private TextView txtOutstandingAmount;
	@InjectView
	private TextView txtPaidAmount;

	private String mRefNo;
	private float mPaymentAmount;// ค่างวด
	private float mSummaryPaymentAmount;//ยอดรวมที่ชำระแล้ว
	private float mOutstandingAmount;// ยอดค้างชำระ

	public static class Data extends BHParcelable {
		public String refNo;
	}

	private Data data;

	// @Override
	// public String fragmentTag() {
	// // TODO Auto-generated method stub
	// return SaleFirstpaymentChoiceFragment.FRAGMENT_SALE_FIRST_PAYMENT_TAG;
	// }

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_payment_first;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_first_payment_customer_detail;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_next };
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		data = getData();
		mRefNo = data.refNo;
		bindContract();
		bindAddressByIdCard();
		setWidgetsEventListener();
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_back:
			showLastView();
			break;
		case R.string.button_next:
			SaleFirstPaymentChoiceFragment fm = BHFragment.newInstance(
					SaleFirstPaymentChoiceFragment.class);
			BHPreference.setRefNo(mRefNo);
			BHPreference.setProcessType(ProcessType.FirstPayment.toString());
			
			showNextView(fm);
			break;
		default:
			break;
		}
	}

	private void setWidgetsEventListener() {

	}

	private void bindContract() {

		(new BackgroundProcess(activity) {
			ContractInfo output = null;

			@Override
			protected void before() {
				// TODO Auto-generated method stub
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				String organizationCode = BHPreference.organizationCode();
				output = getContractByRefNoByPaymentPeriodNumber(
						organizationCode, mRefNo, "1");

			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub

				try {
					if (output != null) {
						contract = output;
						// SimpleDateFormat sdf = new SimpleDateFormat(
						// "dd/MM/yyyy", Locale.US);
						// sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
						// String installDate =
						// sdf.format(contract.InstallDate);
						txtEffDate.setText(BHUtilities
								.dateFormat(contract.InstallDate));

						String contno = (contract.CONTNO == null || contract.CONTNO
								.isEmpty()) ? "" : contract.CONTNO;
						txtContno.setText(contno);

//						String customerFullName = (contract.CustomerFullName == null || contract.CustomerFullName
//								.isEmpty()) ? "" : contract.CustomerFullName;
						
						String customerFullName = (String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));

						// String idCard = "3-7503-00254-85-9";// Fix Test
						String idCard = contract.IDCard;
						// txtCustomerFullName.setText(String.format("%s %s",
						// customerFullName, idCard));
						txtCustomerFullName.setText(customerFullName);
						txtIDCard.setText(idCard);

						String model = (contract.ProductName == null || contract.ProductName
								.isEmpty()) ? "" : contract.ProductName;
						txtModel.setText(model);

						String productSerialNumber = (contract.ProductSerialNumber == null || contract.ProductID
								.isEmpty()) ? "" : contract.ProductSerialNumber;
						txtProductSerialNumber.setText(productSerialNumber);

						// String sales = Float.toString(contract.SALES);
						String sales = BHUtilities
								.numericFormat(contract.SALES);
						txtSales.setText(sales);

						// String tradeInDiscount = Float
						// .toString(contract.TradeInDiscount);
						String tradeInDiscount = BHUtilities
								.numericFormat(contract.TradeInDiscount);
						txtTradeInDiscount.setText(tradeInDiscount);

						// String totalPrice =
						// Float.toString(contract.TotalPrice);
						String totalPrice = BHUtilities
								.numericFormat(contract.TotalPrice);
						txtTotalPrice.setText(totalPrice);
						mPaymentAmount = contract.PaymentAmount;// ค่างวด
						mSummaryPaymentAmount = contract.SummaryPaymentAmount;//ยอดที่ชำระแล้ว
						txtPaidAmount.setText(BHUtilities.numericFormat(mSummaryPaymentAmount));
						mOutstandingAmount = contract.OutstandingAmount;// ยอดค้างชำระ
						txtOutstandingAmount.setText(BHUtilities.numericFormat(mOutstandingAmount));
					}
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.getMessage());
				}
			}
		}).start();
	}

	private void bindAddressByIdCard() {

		(new BackgroundProcess(activity) {
			AddressInfo output = null;

			@Override
			protected void before() {
				// TODO Auto-generated method stub
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getAddressByRefNoByAddressTypeCode(mRefNo,
						AddressType.AddressIDCard.toString());
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					if (output != null) {
						addressIDCard = output;
//						String addressIDCardString = String
//								.format("%s %s %s %s %s",
//										(addressIDCard.AddressDetail == null || addressIDCard.AddressDetail
//												.isEmpty()) ? ""
//												: addressIDCard.AddressDetail,
//										(addressIDCard.SubDistrictName == null || addressIDCard.SubDistrictName
//												.isEmpty()) ? ""
//												: addressIDCard.SubDistrictName,
//										(addressIDCard.DistrictName == null || addressIDCard.DistrictName
//												.isEmpty()) ? ""
//												: addressIDCard.DistrictName,
//										(addressIDCard.ProvinceName == null || addressIDCard.ProvinceName
//												.isEmpty()) ? ""
//												: addressIDCard.ProvinceName,
//										(addressIDCard.Zipcode == null || addressIDCard.Zipcode
//												.isEmpty()) ? ""
//												: addressIDCard.Zipcode);
						txtAddressIDCard.setText(addressIDCard.Address());
					}

					bindAddressInstall();

				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.getMessage());
				}
			}
		}).start();
	}

	private void bindAddressInstall() {
		(new BackgroundProcess(activity) {
			AddressInfo output;

			@Override
			protected void before() {
				// TODO Auto-generated method stub
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getAddressByRefNoByAddressTypeCode(mRefNo,
						AddressType.AddressInstall.toString());
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					if (output != null) {
						addressInstall = output;
//						String addressInstallString = String
//								.format("%s %s %s %s %s",
//										(addressInstall.AddressDetail == null || addressInstall.AddressDetail
//												.isEmpty()) ? ""
//												: addressInstall.AddressDetail,
//										(addressInstall.SubDistrictName == null || addressInstall.SubDistrictName
//												.isEmpty()) ? ""
//												: addressInstall.SubDistrictName,
//										(addressInstall.DistrictName == null || addressInstall.DistrictName
//												.isEmpty()) ? ""
//												: addressInstall.DistrictName,
//										(addressInstall.ProvinceName == null || addressInstall.ProvinceName
//												.isEmpty()) ? ""
//												: addressInstall.ProvinceName,
//										(addressInstall.Zipcode == null || addressInstall.Zipcode
//												.isEmpty()) ? ""
//												: addressInstall.Zipcode);
						txtAddressInstall.setText(addressInstall.Address());
					}
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.getMessage());
				}
			}
		}).start();
	}
}
