package th.co.thiensurat.fragments.sendmoney;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TabPageIndicator;


public class SendMoneySummaryMainFragment extends BHFragment {
	
	@InjectView private ViewPager vpSendMoney;
	@InjectView private TabPageIndicator tsSendMoney;

	
	@Override
	public String fragmentTag() {
		// TODO Auto-generated method stub
		return SendMoneyPrintFragment.FRAGMENT_SEND_MONEY_SUMMARY_MAIN_TAG;
	}
	
	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_payment_send;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sendmoney_summary_main;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		setWidgetsEventListener();
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
	}

	private void setWidgetsEventListener() {
	
		vpSendMoney.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
			private final int PAGE_COUNT = 2;
			private final String[] PAGE_TITLE = { "รายการรอสร้างใบส่งเงิน", "รายการสร้างใบส่งเงินแล้ว" };

			@Override
			public CharSequence getPageTitle(int position) {
				// TODO Auto-generated method stub
				return PAGE_TITLE[position];
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return PAGE_COUNT;
			}
			
			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub				
				switch (position) {
				case 0:
					return BHFragment.newInstance(SendMoneySummaryNotSendListFragment.class);

				case 1:
					return BHFragment.newInstance(SendMoneySummarySendListFragment.class);

				default:
					break;
				}
				return null;
			}
		});	
		
		tsSendMoney.setViewPager(vpSendMoney);

	}

}
