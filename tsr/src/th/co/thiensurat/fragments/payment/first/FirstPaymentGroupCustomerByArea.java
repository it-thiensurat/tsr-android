package th.co.thiensurat.fragments.payment.first;

import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FirstPaymentGroupCustomerByArea extends BHFragment {

	private static List<SalePaymentPeriodInfo> mGroupCustomerList = null;
	@InjectView
	private ListView lvGroupCustomer;

	public static class Result extends BHParcelable {
		public String assigneeEmpID;
		public boolean back;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_payment_first;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_first_payment_group_customer_by_area;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back };
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		loadData();
		setWidgetsEventListener();
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_back:
			FirstPaymentGroupCustomerByArea.Result result = new FirstPaymentGroupCustomerByArea.Result();
			result.back = true;
			showLastView(result);
			break;

		default:
			break;
		}
	}

	private void loadData() {
		(new BackgroundProcess(activity) {
			List<SalePaymentPeriodInfo> output = null;

			@Override
			protected void before() {
				// TODO Auto-generated method stub
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getNextSalePaymentPeriodByContractTeamGroupByAssignee(
						BHPreference.organizationCode(),
						BHPreference.teamCode());//BHPreference.saleCode()
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					mGroupCustomerList = output;
					bindAssignee();
				} catch (Exception ex) {
					Log.e(this.getClass().getName(), ex.getMessage());
				}
			}
		}).start();
	}

	private void setWidgetsEventListener() {

		lvGroupCustomer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				SalePaymentPeriodInfo gspp = mGroupCustomerList.get(position);
				String assigneeempid = gspp.AssigneeEmpID;
				// BHPreference.setAssigneeEmpID(assigneeempid);
				FirstPaymentGroupCustomerByArea.Result result = new FirstPaymentGroupCustomerByArea.Result();
				result.assigneeEmpID = assigneeempid;
				result.back = false;
				showLastView(result);
			}
		});
	}

	private void bindAssignee() {
		BHArrayAdapter<SalePaymentPeriodInfo> adapter = new BHArrayAdapter<SalePaymentPeriodInfo>(
				activity, R.layout.list_first_payment_group_customer_item,
				mGroupCustomerList) {

			class ViewHolder {
				public TextView txtTitle;				
				public TextView txtCount;
			}

			@Override
			protected void onViewItem(int position, View view, Object holder,
					SalePaymentPeriodInfo info) {
				// TODO Auto-generated method stub

				try {
					ViewHolder vh = (ViewHolder) holder;
//					String title = String.format("%s %d %s",
//							info.AssigneeEmpName, info.AssigneeCount, "ราย");
//					vh.txtTitle.setText(title);
					vh.txtTitle.setText(info.AssigneeEmpName);
					vh.txtCount.setText(String.format("%d ราย", info.AssigneeCount));

					if (position % 2 == 0) {
						view.setBackground(new ColorDrawable(Color
								.parseColor("#4dd1e0")));
					} else {
						view.setBackground(new ColorDrawable(Color
								.parseColor("#ffffff")));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		lvGroupCustomer.setAdapter(adapter);
	}
}
