package th.co.thiensurat.fragments.sales;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.PackageInfo;
import th.co.thiensurat.data.info.PackagePeriodDetailInfo;
import th.co.thiensurat.data.info.ProductInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;

public class SaleListPackagePaymentFragment extends BHFragment {

	private String STATUS_CODE = "04";
	private ContractInfo Contract;
	private List<PackageInfo> ListPackage;
	private String model;
	private float saleprice;
	private List<PackagePeriodDetailInfo> MODE;

	@InjectView
	private ListView listViewPackage;
	@InjectView
	private TextView ProductName;
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

	protected ProductInfo product;

	@Override
	public String fragmentTag() {
		// TODO Auto-generated method stub
		return SalePayFragment.FRAGMENT_SALE_PAY_TAG;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_sales;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_list_package_payment;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_next };
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {

		if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
			saveStatusCode();

			txtNumber4.setBackgroundResource(R.drawable.circle_number_sale_color_red);

		}

		model = null;
		saleprice = 0;
		MODE = null;

		ContactDB();
	}

	private void saveStatusCode() {
		TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
	}

	private void ContactDB() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {
			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				Contract = getContract(BHPreference.RefNo());
				product = getProductInfo(BHPreference.organizationCode(), Contract.ProductID);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (Contract != null) {
					(new BackgroundProcess(activity) {
						@Override
						protected void calling() {
							// TODO Auto-generated method stub
							ListPackage = getPackageByProductInfo(Contract.OrganizationCode, Contract.ProductID);
						}

						@Override
						protected void after() {
							// TODO Auto-generated method stub
							if (ListPackage != null) {
								BHArrayAdapter<PackageInfo> adapter = new BHArrayAdapter<PackageInfo>(activity, R.layout.list_package_pay, ListPackage) {
									class ViewHolder {
										public TextView textView, textViewPrice;
									}

									@Override
									protected void onViewItem(int position, View view, Object holder, final PackageInfo info) {
										// TODO Auto-generated method stub
										ViewHolder vh = (ViewHolder) holder;
										vh.textView.setText("" + info.PackageTitle);
										vh.textViewPrice.setText(BHUtilities.numericFormat(info.TotalPrice));

									}
								};
								listViewPackage.setAdapter(adapter);
								ProductName.setText(product.ProductName);
								listViewPackage.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO Auto-generated method stub
										model = ListPackage.get(position).Model;
										saleprice = ListPackage.get(position).TotalPrice;
										GetMODE();
									}

								});
							}
						}
					}).start();
				}
			}
		}).start();
	}

	private void GetMODE() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				MODE = getPackagePeriodDetail(BHPreference.organizationCode(), model);
			}
		}).start();
	}

	private void updateContractDB() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {
			@Override
			protected void before() {
				// TODO Auto-generated method stub
				Contract.RefNo = BHPreference.RefNo();
				Contract.SALES = saleprice;
				Contract.MODEL = model;
				Contract.MODE = MODE.size();
                Contract.TradeInDiscount = Float.parseFloat("0");
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				updateContract(Contract, true);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				showNextView(new SalePayFragment());
			}
		}).start();
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_next:
			if(model != null){
				updateContractDB();
			}else {
				//showDialog("แจ้งเตือน", "กรุณาเลือกประเภทชำระเงิน");

				String title = "แจ้งเตือน";
				String message = "กรุณาเลือกประเภทชำระเงิน";
				showWarningDialog(title, message);
			}

			break;
		case R.string.button_back:
			showLastView();
			break;
		default:
			break;
		}
	}
}