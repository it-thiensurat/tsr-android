package th.co.thiensurat.fragments.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.viewpagerindicator.TabPageIndicator;

public class ReportSummaryTradeProductMainFragment extends BHFragment {
//
//	@InjectView
//	private ViewPager vpTotalMain;
//	@InjectView
//	private TabPageIndicator tabTotalMain;
//	@InjectView
//	private Spinner spnFortnight;
//	@InjectView
//	private Spinner spnEmpSale;
//
//	private List<String> fortnightIDList = new ArrayList<String>();
//
//	public static String SaleCode, FortnightID;
//
//	private ReportSummaryTradeProductReceiveFragment receiveFragment = BHFragment
//			.newInstance(ReportSummaryTradeProductReceiveFragment.class);
//
//	private ReportSummaryTradeProductNotReceiveFragment notReceiveFragment = BHFragment
//			.newInstance(ReportSummaryTradeProductNotReceiveFragment.class);
	
	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_main_trade_product;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.menu_report_trade_product;
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

//	@Override
//	public void onProcessButtonClicked(int buttonID) {
//		// TODO Auto-generated method stub
//		switch (buttonID) {
//		case R.string.button_print:
//
//			break;
//
//		case R.string.button_back:
//			showLastView();
//			break;
//
//		default:
//			break;
//		}
//	}
	
	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		//setViewPager();
		//loadFortnightAndSale();


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
		webView.loadUrl(BHPreference.TSR_REPORT_MOBILE + "?EmpID=" + BHPreference.employeeID() + "&Report=" + getResources().getString(R.string.mobile_report_trade_product) + "");

	}

	/*
	private void setViewPager() {
		// TODO Auto-generated method stub
		vpTotalMain.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
			private final String[] PAGE_TITLE = {"นำส่งคลัง", "ยังไม่นำส่งคลัง"};

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
						return receiveFragment;
					case 1:
						return notReceiveFragment;

					default:
						break;
				}

				return null;
			}
		});

		tabTotalMain.setViewPager(vpTotalMain);
	}

	private void loadFortnightAndSale() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {
			String start, end;
			List<FortnightInfo> output;
			List<EmployeeInfo> empList;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getAllFortnight(BHPreference.organizationCode());
				empList = getEmployees(BHPreference.teamCode());
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
							
							FortnightID = fortnightIDList.get(position).toString();
							
							//FortnightID = "TEMP01";
							//SaleCode = "%";
							
							receiveFragment.getReportData();
							notReceiveFragment.getReportData();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}
					});

				}

				if (empList != null) {
					List<String> saleList = new ArrayList<String>();
					for (EmployeeInfo item : empList) {

						saleList.add(item.EmpID + " " + item.FirstName + " " + item.LastName);

					}

					saleList.add(0, "ทีม " + BHPreference.teamCode());

					BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(activity, saleList);
					spnEmpSale.setAdapter(dataAdapter);
					spnEmpSale.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

							
							
							if (position == 0) {
								showMessage("ทีม " + BHPreference.teamCode());
								SaleCode = "%";
							}
							else {
								showMessage(empList.get(position-1).FirstName + " " + empList.get(position-1).LastName);
								SaleCode = empList.get(position-1).EmpID;
							}
								
							//FortnightID = "TEMP01";
							//SaleCode = "%";
							
							receiveFragment.getReportData();
							notReceiveFragment.getReportData();

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
