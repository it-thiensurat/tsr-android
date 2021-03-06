package th.co.thiensurat.fragments.report;

import java.util.ArrayList;
import java.util.List;

import com.viewpagerindicator.TabPageIndicator;

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

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.R.string;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.FortnightInfo;

public class ReportSummaryComplainFragment extends BHFragment {
//
//	@InjectView
//	private ViewPager vpTotalMain;
//	@InjectView
//	private TabPageIndicator tabTotalMain;
//	@InjectView
//	private Spinner spnFortnight;
//
//	String start, end, year;
//
//	ReportSummaryCustomerProblemFirstFragment FirstPaymentFragment = BHFragment
//			.newInstance(ReportSummaryCustomerProblemFirstFragment.class);
//
//	ReportSummaryCustomerProblemNextFragment NextPaymentFragment = BHFragment
//			.newInstance(ReportSummaryCustomerProblemNextFragment.class);

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_complain;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return string.menu_report_complain;
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
		webView.loadUrl(BHPreference.TSR_REPORT_MOBILE + "?EmpID=" + BHPreference.employeeID() + "&Report=" + getResources().getString(R.string.mobile_report_complain) + "");

	}

	/*
	private void setViewPager() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		try {

			vpTotalMain.setAdapter(new FragmentPagerAdapter(
					getChildFragmentManager()) {
				private final String[] PAGE_TITLE = { "งวดแรก", "งวดต่อไป" };

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
						return FirstPaymentFragment;

					case 1:

						return NextPaymentFragment;

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

										FirstPaymentFragment.refreshData(year,
												BHPreference.teamCode());
										NextPaymentFragment.refreshData(year,
												BHPreference.teamCode());

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
