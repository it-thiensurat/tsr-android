package th.co.thiensurat.fragments.payment.first;

import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
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

public class FirstPaymentGroupCustomerByAppointmentDateFragment extends
		BHFragment {

	// /private static List<GroupSalePaymentPeriodByAppointmentDateInfo>
	// mGroupCustomerList;
	private static List<SalePaymentPeriodInfo> mGroupCustomerList = null;

	public static class Result extends BHParcelable {
		public Date paymentAppointmentDate;
		public boolean back;
	}

	@InjectView
	private ListView lvGroupCustomer;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_first_payment_group_customer_by_appointmentdate;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_payment_first;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back };
	}

	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		loadData();
		setWidgetsEventListener();
	}

	public void onProcessButtonClicked(int buttonID) {
		switch (buttonID) {
		case R.string.button_back:
			FirstPaymentGroupCustomerByAppointmentDateFragment.Result result = new FirstPaymentGroupCustomerByAppointmentDateFragment.Result();
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
                output = getNextSalePaymentPeriodByContractTeamGroupByAppointmentDate(
                        BHPreference.organizationCode(),
                        BHPreference.teamCode(), isLeader() ? null : BHPreference.employeeID());//BHPreference.saleCode()
			}

            private boolean isLeader() {
                String[] arrUserPosition = BHPreference.PositionCode() != null ?
                        BHPreference.PositionCode().split(",") : new String[0];
                boolean isLeader = false;
                for (int i = 0; i < arrUserPosition.length; i++) {
                    if (arrUserPosition[i].toString().toLowerCase().equals("saleleader")
                            || arrUserPosition[i].toString().toLowerCase().equals("subteamleader")) {
                        isLeader  = true;
                        break;
                    }
                }
                return isLeader;
            }

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					mGroupCustomerList = output;
					bindGroupCustomerList();
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.getMessage());
				}
			}
		}).start();
	}

	private void bindGroupCustomerList() {

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
					// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
					// Locale.US);
					// sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
					// String appointmentDateString = sdf
					// .format(info.PaymentAppointmentDate);
					
//					String appointmentDateString = BHUtilities.dateFormat(
//							info.PaymentAppointmentDate, "dd/MM/yyyy");
//					String title = String.format("%s %s %d %s",
//							appointmentDateString, "จำนวน",
//							(int) info.PaymentAppointmentDateCount, "ราย");
//					vh.txtTitle.setText(title);
					
					vh.txtTitle.setText(BHUtilities.dateFormat(info.PaymentAppointmentDate, "dd/MM/yyy"));
					vh.txtCount.setText(String.format("จำนวน %d ราย", info.PaymentAppointmentDateCount));
					
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

	private void setWidgetsEventListener() {
		lvGroupCustomer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				SalePaymentPeriodInfo gsppa = mGroupCustomerList.get(position);
				FirstPaymentGroupCustomerByAppointmentDateFragment.Result result = new FirstPaymentGroupCustomerByAppointmentDateFragment.Result();
				result.paymentAppointmentDate = (Date) gsppa.PaymentAppointmentDate;
				result.back = false;
				showLastView(result);
			}
		});
	}
}
