package th.co.thiensurat.fragments.report;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.TeamInfo;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ReportSummaryInstallAndPaymentFragment extends BHFragment {

//	@InjectView
//	private ViewPager vpTotalMain;
//	@InjectView
//	private TabPageIndicator tabTotalMain;
//	@InjectView
//	private Spinner spnFortnight;
//	@InjectView
//	private Spinner spnEmpSale;
//
//	public String start, end, year, emp, teamcode;
//
//	ReportSummaryTotalInstallFragment installFragment = BHFragment
//			.newInstance(ReportSummaryTotalInstallFragment.class);
//
//	ReportSummaryTotalCreditFragment creditFragment = BHFragment
//			.newInstance(ReportSummaryTotalCreditFragment.class);

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_install_payment;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.menu_report_install_payment;
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
		//loadTeamSale();
		//setViewPager();


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
		webView.loadUrl(BHPreference.TSR_REPORT_MOBILE + "?EmpID=" + BHPreference.employeeID() + "&Report=" + getResources().getString(R.string.mobile_report_install_payment) + "");

	}

	/*
	private void loadTeamSale() {
		// TODO Auto-generated method stub

		(new BackgroundProcess(activity) {

			List<EmployeeInfo> sale;
			List<TeamInfo> team;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub

				if (BHPreference.PositionCode().equals("SaleLeader")
						|| BHPreference.PositionCode().equals(
								"SaleLeader,SubTeamLeader")) {
					sale = getEmployeeInfoSaleLeader(BHPreference.teamCode());
				} else if (BHPreference.PositionCode().equals("Supervisor")) {
					team = getTeam();
				}

			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (sale != null) {

					List<String> teamSale = new ArrayList<String>();
					teamSale.add(String.format(" "));
					BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(
							activity, teamSale);
					for (EmployeeInfo employee : sale) {
						teamSale.add(BHUtilities.trim(employee.FirstName + " "
								+ employee.LastName));
					}
					spnEmpSale.setAdapter(dataAdapter);
					spnEmpSale
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int position, long id) {
									// TODO Auto-generated method stub
									if (!parent.getItemAtPosition(position)
											.toString().trim().equals("")) {
										emp = sale.get(position - 1).EmpID;
										showMessage(emp);

										installFragment.refreshData(year, emp,
												teamcode);
										creditFragment.refreshData(year, emp,
												teamcode);

									}

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> parent) {
									// TODO Auto-generated method stub

								}
							});
				}
				if (team != null) {

					List<String> teamSale = new ArrayList<String>();
					teamSale.add(String.format(" "));
					BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(
							activity, teamSale);
					for (TeamInfo teamAll : team) {
						teamSale.add(BHUtilities.trim(teamAll.Code));
					}
					spnEmpSale.setAdapter(dataAdapter);
					spnEmpSale
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int position, long id) {
									// TODO Auto-generated method stub
									if (!parent.getItemAtPosition(position)
											.toString().trim().equals("")) {
										teamcode = team.get(position - 1).Code;
										showMessage(teamcode);

										installFragment.refreshData(year, emp,teamcode);
										creditFragment.refreshData(year, emp,teamcode);

									}

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
					fortnight.add(String.format(" "));
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
									if (!parent.getItemAtPosition(position)
											.toString().trim().equals("")) {
										year = output.get(position - 1).FortnightNumber
												+ "/"
												+ BHUtilities.dateFormat(
														output.get(position - 1).StartDate,
														"yyyy");

										showMessage(year);

										// report.test(team, year);

									}
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

	private void setViewPager() {
		// TODO Auto-generated method stub

		try {

			vpTotalMain.setAdapter(new FragmentPagerAdapter(
					getChildFragmentManager()) {
				private final String[] PAGE_TITLE = { "ติดตั้ง", "เก็บเงิน" };

				@Override
				public CharSequence getPageTitle(int position) {
					// TODO Auto-generated method stub
					return PAGE_TITLE[position];
				}

				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return PAGE_TITLE.length;
				}

				@Override
				public Fragment getItem(int position) {
					// TODO Auto-generated method stub

					switch (position) {
					case 0:

						//
						return installFragment;

					case 1:

						return creditFragment;

						// return
						// BHFragment.newInstance(ReportSummaryTotalCreditFragment.class);

					default:
						break;
					}
					return null;
				}
			});
			tabTotalMain.setViewPager(vpTotalMain);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
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
