package th.co.thiensurat.fragments.sales;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.service.data.AuthenticateInputInfo;
import th.co.thiensurat.service.data.AuthenticateOutputInfo;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SaleStatusFragment extends BHFragment {
	public static class Data extends BHParcelable {
		public String title;
	}

	@InjectView
	private TextView txtTitle;
	private Data data;

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_sales;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_add_customer };
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_status;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		data = getData();
		txtTitle.setText(data.title);
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_add_customer:
			// Intent intent = new Intent(Intents.Scan.ACTION);
			// Intent intent = new Intent(activity, CaptureActivity.class);
			// startActivityForResult(intent, 0);
			// showNextView(new SaleStatusDetailFragment());
			(new BackgroundProcess(activity) {

				AuthenticateInputInfo input = new AuthenticateInputInfo();
				AuthenticateOutputInfo result;

				@Override
				protected void before() {
					// TODO Auto-generated method stub
					input.UserName = "PAK23";
					input.Password = "123456";
				}

				@Override
				protected void calling() {
					// TODO Auto-generated method stub
					result = authenticate(input);
				}

				@Override
				protected void after() {
					// TODO Auto-generated method stub
					showMessage(result.ResultDescription);
				}
			}).start();

			// ListView lv = new ListView(activity);
			// List<ProductStockInfo> info = new ArrayList<ProductStockInfo>();
			// BHArrayAdapter<ProductStockInfo> adapter = new
			// BHArrayAdapter<ProductStockInfo>(activity, R.layout.list_menu,
			// info) {
			//
			// class ViewHolder {
			// public ImageView imgIcon;
			// public TextView txtTitle;
			// }
			//
			// @Override
			// protected void onViewItem(Object holder, ProductStockInfo info) {
			// // TODO Auto-generated method stub
			// ViewHolder vh = (ViewHolder)holder;
			// vh.txtTitle.setText(info.ProductSerialNumber);
			// }
			// };

			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
}
