package th.co.thiensurat.fragments.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.FortnightInfo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class ReportSummaryPaymentAppointmentFragment extends BHFragment {
//	@InjectView
//	private Spinner spnFortnight;
//	@InjectView
//	private ListView lvSummary;
//	private BHListViewAdapter adapter;
//
//	private List<ReportSummaryRangOfFirstReceiveInfo> reportDataList;
//	private List<String> fortnightIDList = new ArrayList<String>();

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_payment_appointment;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.menu_report_payment_appointment;
	}
	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		//return new int[] { R.string.button_back, R.string.button_print };
		/*** [START] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
//		return new int[] { R.string.button_back };
		return null;
		/*** [END] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//loadFortnight();


		setWebView();


	}
	@InjectView
	private WebView webView;

	private  void setWebView()
	{
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
				view.loadUrl(urlNewString);
				BHLoading.show(getActivity());

				return true;
			}

			Runnable rRun;
			Handler handler = new Handler();;

			@Override
			public void onPageStarted(WebView view, String url, Bitmap facIcon) {
				//SHOW LOADING IF IT ISNT ALREADY VISIBLE

				BHLoading.show(getActivity());
				rRun=new Runnable() {
					@Override
					public void run() {
						showMessage(getResources().getString(R.string.mobile_report_error));
						BHLoading.close();
					}
				};
				int delay = Integer.parseInt(getActivity().getResources().getString(R.string.mobile_report_delay));
				handler.postDelayed(rRun, delay);

			}

			@Override
			public void onPageFinished(WebView view, String url) {

				handler.removeCallbacks(rRun);
				showMessage(getResources().getString(R.string.mobile_report_success));
				BHLoading.close();
			}


		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(BHPreference.TSR_REPORT_MOBILE + "?EmpID=" + BHPreference.employeeID() + "&Report=" + getResources().getString(R.string.mobile_report_payment_appointment) + "");

	}

	/*
	private void getReportData(final String FortnightID) {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

			@Override
			protected void before() {
				// TODO Auto-generated method stub

			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				// reportDataList =
				// TSRController.getReportSummaryImpoundProduct("TEMP01",
				// "PAK-23");
				reportDataList = TSRController.getReportSummaryRangOfFirstReceive(FortnightID, BHPreference.teamCode());

			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {

					if (reportDataList != null)
						bindSummaryList();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}).start();
	}

	private void bindSummaryList() {

		adapter = new BHListViewAdapter(activity) {
			class ViewHolder {
				public TextView txtDateRang;
				public TextView txtEmpName;
				public TextView txtNumber;
				public TextView txtFresh;
				public TextView txtOutstanding;

			}

			@Override
			protected int viewForSectionHeader(int section) {
				// TODO Auto-generated method stub

				return R.layout.list_report_summary_rang_of_first_receive_header;

			}

			@Override
			protected int viewForItem(int section, int row) {
				// TODO Auto-generated method stub
				return R.layout.list_report_summary_rang_of_first_receive_item;
			}

			@Override
			protected void onViewItem(View view, Object holder, int section, int row) {
				// TODO Auto-generated method stub
				try {
					ViewHolder vh = (ViewHolder) holder;
					vh.txtDateRang.setText(BHUtilities.dateFormat(reportDataList.get(row).DateRang).toString());
					vh.txtEmpName.setText(reportDataList.get(row).EmpName);
					vh.txtNumber.setText(String.valueOf(reportDataList.get(row).Number));
					vh.txtFresh.setText(String.valueOf(reportDataList.get(row).Fresh));
					vh.txtOutstanding.setText(String.valueOf(reportDataList.get(row).Outstanding));

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			protected int getItemCount(int section) {
				// TODO Auto-generated method stub
				return reportDataList.size();
			}
		};

		lvSummary.setAdapter(adapter);
	}

	private void loadFortnight() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {
			String start, end;
			List<FortnightInfo> output;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getAllFortnight(BHPreference.organizationCode());
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub

				if (output != null) {

					fortnightIDList.clear();

					List<String> fortnight = new ArrayList<String>();
					for (FortnightInfo item : output) {
						start = BHUtilities.dateFormat(item.StartDate);
						end = BHUtilities.dateFormat(item.EndDate);
						String year = BHUtilities.dateFormat(item.StartDate, "yyyy");
						fortnight.add(item.FortnightNumber + "/" + year + " (" + start + " - " + end + ") ");

						fortnightIDList.add(item.FortnightID);
					}
					BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(activity, fortnight);
					spnFortnight.setAdapter(dataAdapter);
					spnFortnight.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

							start = BHUtilities.dateFormat(output.get(position).StartDate, "yyyy-MM-dd", new Locale("us"));
							end = BHUtilities.dateFormat(output.get(position).EndDate, "yyyy-MM-dd", new Locale("us"));

							showMessage(start + " - " + end);
							getReportData(fortnightIDList.get(position).toString());

						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}
					});

				}

			}
		}).start();

	}
	*/

	/*** [START] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
	/*
	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_print:

			break;

		case R.string.button_back:
			showLastView();
			break;

		default:
			break;
		}
	}
	*/
	/*** [END] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/

}
