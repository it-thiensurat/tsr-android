package th.co.thiensurat.fragments.report;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.viewpagerindicator.TabPageIndicator;

public class ReportDashboardSummaryMainFragment extends BHFragment {

//	@InjectView
//	private ViewPager vpTotalMain;
//	@InjectView
//	private TabPageIndicator tabTotalMain;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_dashboard_summary_main;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.menu_report_sale_summary;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] {  };
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

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
		webView.loadUrl(BHPreference.TSR_REPORT_MOBILE + "?EmpID=" + BHPreference.employeeID() + "&Report=" + getResources().getString(R.string.mobile_report_daily_summary) + "");

	}

	/*
	private void setViewPager() {
		// TODO Auto-generated method stub
		vpTotalMain.setAdapter(new FragmentPagerAdapter(
				getChildFragmentManager()) {
			private final String[] PAGE_TITLE = {"รายวัน", "รายปักษ์"};

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
						return BHFragment
								.newInstance(ReportDashboardSummaryDailyFragment.class);
					case 1:
						return BHFragment
								.newInstance(ReportDashboardSummaryFortnightFragment.class);

					default:
						break;
				}

				return null;
			}
		});

		tabTotalMain.setViewPager(vpTotalMain);
	}
	*/

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
			case R.string.button_back:
				//showLastView();
				break;
			default:
				break;
		}
	}
}
