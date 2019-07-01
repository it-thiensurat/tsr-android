package th.co.thiensurat.fragments.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.R.string;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.ReportSaleAndDriverInfo;

public class ReportSummaryEmployeeTeamFragment extends BHFragment {

//	@InjectView
//	private Spinner spnFortnight;
//	@InjectView
//	private ListView lvEmp;
//
//	String start, end, year;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_employee_team;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.menu_report_emp_team;
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
		webView.loadUrl(BHPreference.TSR_REPORT_MOBILE + "?EmpID=" + BHPreference.employeeID() + "&Report=" + getResources().getString(R.string.mobile_report_emp_team) + "");

	}

	/*
	private void loadFortnight() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

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

					List<String> fortnight = new ArrayList<String>();
					for (FortnightInfo item : output) {
						start = BHUtilities.dateFormat(item.StartDate);
						end = BHUtilities.dateFormat(item.EndDate);
						year = BHUtilities.dateFormat(item.StartDate, "yyyy");
						fortnight.add(item.FortnightNumber + "/" + year + " ("
								+ start + " - " + end + ") ");
					}
					BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(
							activity, fortnight);
					spnFortnight.setAdapter(dataAdapter);
					spnFortnight
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int position, long id) {

									year = output.get(position).FortnightNumber
											+ "/"
											+ BHUtilities.dateFormat(
													output.get(position).StartDate,
													"yyyy");

									showMessage(year);
									loadData();

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> parent) {
									// TODO Auto-generated method stub

								}
							});

				}

			}
		}).start();

	}

	protected void loadData() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

			List<ReportSaleAndDriverInfo> emp;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				emp = getReportByFortnight(BHPreference.teamCode(), year);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub

				if (emp != null) {
					BHArrayAdapter<ReportSaleAndDriverInfo> employee = new BHArrayAdapter<ReportSaleAndDriverInfo>(
							activity, R.layout.list_report_emp, emp) {

						class ViewHolder {
							public TextView txtTitle;

						}

						@Override
						protected void onViewItem(int position, View view,
								Object holder, ReportSaleAndDriverInfo info) {
							// TODO Auto-generated method stub

							try {
								ViewHolder vh = (ViewHolder) holder;
								String titleString = String.format(
										"%s   ชื่อ: %s", info.EmployeeCode,
										info.EmloyeeName);
								vh.txtTitle.setText(titleString);

							} catch (Exception e) {
								e.printStackTrace();
							}

						}

					};
					lvEmp.setAdapter(employee);

				} else {
					showMessage("ไม่พบข้อมูล");
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
