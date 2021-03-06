package th.co.thiensurat.fragments.sales;

import th.co.bighead.utilities.BHFragment;
import th.co.thiensurat.R;
import android.os.Bundle;

public class SaleStatusDetailFragment extends BHFragment {
	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_sales;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_status_detail;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_ok };
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_ok:
			showNextView(new SaleMainFragment());
			break;

		case R.string.button_back:
			showLastView();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

}
