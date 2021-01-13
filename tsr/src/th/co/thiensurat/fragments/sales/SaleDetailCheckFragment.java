package th.co.thiensurat.fragments.sales;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.ProductInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.contracts.change.ChangeContractResultFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.sales.preorder.SaleDetailCheckContractFragment_preorder;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.activities.MainActivity.select_page_preorder;

public class SaleDetailCheckFragment extends BHFragment {

	private String STATUS_CODE = "06";
	public static float paymentFirst;
	public static float paymentSecond;
	private ContractInfo Contract;
    protected ProductInfo product;

	public static class Data extends BHParcelable {

		// For ChangeContract
		public ContractInfo oldContract;
		public ContractInfo newContract;
		public List<SalePaymentPeriodInfo> newSPPList;
		public List<PaymentInfo> newPayment;
		public String selectedCauseName;
		public AddressInfo newAddressIDCard;
		public AddressInfo newAddressInstall;
		public AddressInfo newAddressPayment;
		public ChangeContractInfo chgContractRequest;
		public ChangeContractInfo chgContractApprove;
		public ChangeContractInfo chgContractAction;
		public AssignInfo assign;
		public String changeContractType;
	}

	private Data data;

	@InjectView
	ViewTitle viewTitle;

	@InjectView
	TextView textViewPrice;
	@InjectView
	TextView textViewDiscount;
	@InjectView
	TextView textViewSumPrice;
	@InjectView
	TextView textViewTotalExpenses;
	@InjectView
	LinearLayout linearLayoutInstallment;
	@InjectView
	ListView listViewInstallment;
	@InjectView
	private LinearLayout linearLayoutHeadNumber;
	@InjectView
	private TextView txtNumber1;
	@InjectView
	private TextView txtNumber2;
	@InjectView
	private TextView txtNumber3;
	@InjectView
	private TextView txtNumber4;
	@InjectView
	private TextView txtNumber5;
	@InjectView
	private TextView ProductName;

	List<SalePaymentPeriodInfo> sppList = new ArrayList<SalePaymentPeriodInfo>();

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		if(BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())){
			return R.string.title_change_contract;
		} else {
			return R.string.title_sales;
		}
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_detail_check;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		if (BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
			return new int[]{R.string.button_back, R.string.button_next};
		} else {
			return new int[] { R.string.button_back, R.string.button_detail_contract};
		}
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
			txtNumber1.setText("...");
			txtNumber2.setText("5");
			txtNumber3.setText("6");
			txtNumber4.setText("7");
			txtNumber5.setText("...");
			txtNumber3.setBackgroundResource(R.drawable.circle_number_sale_color_red);
            saveStatusCode();
		} else {
			linearLayoutHeadNumber.setVisibility(View.GONE);
		}

		if(data == null){
			data = getData();
		}

		if (BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
			viewTitle.setText("รายละเอียด");
		}
		ContactDB();


	}

    private void saveStatusCode() {
        // TODO Auto-generated method stub
        TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
    }

	private void ContactDB() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {
			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				if (BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
					Contract = data.newContract;
					product = getProductInfo(BHPreference.organizationCode(), Contract.ProductID);
				} else {
					Contract = getContract(BHPreference.RefNo());
					product = getProductInfo(BHPreference.organizationCode(), Contract.ProductID);
				}
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (Contract != null) {
					bindData();

				}
			}
		}).start();
	}

	private void bindData() {
		// TODO Auto-generated method stub
		ProductName.setText(BHUtilities.trim(product.ProductName));
		textViewPrice.setText(BHUtilities.numericFormat(Contract.SALES));
		textViewDiscount.setText(BHUtilities.numericFormat(Contract.TradeInDiscount));
		textViewSumPrice.setText(BHUtilities.numericFormat(Contract.TotalPrice));
		textViewTotalExpenses.setText(BHUtilities.numericFormat(Contract.TotalPrice) + getResources().getString(R.string.label_baht));
		ListPackage();
	}

	private void ListPackage() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {
			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				if (BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
					sppList = data.newSPPList;
				} else {
					sppList = getSalePaymentPeriodByRefNo(Contract.RefNo);
				}
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (sppList != null) {
					BHArrayAdapter<SalePaymentPeriodInfo> adapter = new BHArrayAdapter<SalePaymentPeriodInfo>(activity, R.layout.list_detailcheck, sppList) {
						class ViewHolder {
							public TextView textViewNumber, textViewPay;
						}

						@Override
						protected void onViewItem(int position, View view, Object holder, SalePaymentPeriodInfo info) {
							// TODO Auto-generated method stub
							ViewHolder vh = (ViewHolder) holder;
							vh.textViewNumber.setText("" + info.PaymentPeriodNumber);
							vh.textViewPay.setText(BHUtilities.numericFormat(info.NetAmount));
							if (Contract.MODE != 1) {
								paymentFirst = getItem(0).NetAmount;
								paymentSecond = getItem(1).NetAmount;
							}
						}
					};
					listViewInstallment.setAdapter(adapter);
				}
			}
		}).start();
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_detail_contract:
			// UpdateContractDB();

			//Log.e("select_page_preorder", String.valueOf(select_page_preorder));
			//if(select_page_preorder==1){
			//	showNextView(new SaleDetailCheckContractFragment_preorder());

			//}
			//else {
				showNextView(new SaleDetailCheckContractFragment());

			//}


			break;
		case R.string.button_back:
            	showLastView();
			break;
		case R.string.button_next:
			if(data == null){
				data = getData();
			}

			ChangeContractResultFragment.Data data2 = new ChangeContractResultFragment.Data();
			data2.oldContract = data.oldContract;
			data2.newContract = Contract;
			data2.newSPPList = data.newSPPList;
			data2.newPayment = data.newPayment;
			data2.selectedCauseName = data.selectedCauseName;
			data2.newAddressIDCard = data.newAddressIDCard;
			data2.newAddressInstall = data.newAddressInstall;
			data2.newAddressPayment = data.newAddressPayment;
			data2.chgContractRequest = data.chgContractRequest;
			data2.chgContractApprove = data.chgContractApprove;
			data2.chgContractAction = data.chgContractAction;
			data2.assign = data.assign;

			data2.changeContractType = data.changeContractType;

			ChangeContractResultFragment fmCCResult = BHFragment.newInstance(ChangeContractResultFragment.class, data2);
			showNextView(fmCCResult);

			break;
		default:
			break;
		}
	}

	// private void UpdateContractDB() {
	// // TODO Auto-generated method stub
	// (new BackgroundProcess(activity) {
	// @Override
	// protected void before() {
	// // TODO Auto-generated method stub
	// Contract.RefNo = BHPreference.RefNo();
	// Contract.STATUS = ContractStatus.NORMAL.toString();
	// }
	//
	// @Override
	// protected void calling() {
	// // TODO Auto-generated method stub
	// Contract.CONTNO =
	// getAutoGenerateDocumentID(TSRController.DocumentGenType.Contract.toString(),
	// BHPreference.teamCode(),
	// BHPreference.employeeID());
	//
	// updateContract(Contract);
	// }
	//
	// @Override
	// protected void after() {
	// // TODO Auto-generated method stub
	// showNextView(new SaleDetailCheckContractFragment());
	// }
	// }).start();
	// }
}
