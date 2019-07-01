package th.co.thiensurat.fragments.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

//import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.views.ViewTitle;

public class BarcodeScanFragment extends BHFragment {
	public static abstract class ScanCallBack extends BHFragmentCallback {
		public String onNextClick() {
			return null;
		}
	}

	public static class Result extends BHParcelable {
		public String barcode;

		private Result(String barcode) {
			this.barcode = barcode;
		}
	}

	private static class Data extends BHParcelable {
		public int titleResID = -1;
		public int viewTitleResID = -1;
		public String viewTitle = null;
		public int descriptionResID = -1;
		public String description = null;
	}

	private static final String FRAGMENT_DATA = "th.co.thiensurat.barcode.data";
	private static final int REQUEST_QR_SCAN = 2468;
	@InjectView
	private ViewTitle vwTitle;
	@InjectView
	private ImageButton ibScanBarcode;
	@InjectView
	private TextView tvDescription;
	@InjectView
	private EditText edtBarcode;

	private Data data = new Data();

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_scan_barcode;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_next };
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return data.titleResID;
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_back:
			showLastView();
			break;

		case R.string.button_next:
//			fragmentCallback.getClass().isAssignableFrom(ScanCallBack.class);
//			ScanCallBack scanCallBack = (ScanCallBack) fragmentCallback;
//			if (scanCallBack != null) {
//				String barcode = scanCallBack.onNextClick();
//				Result result = new Result(barcode);
//				setResult(result);				
//			}
			
			String barcode = edtBarcode.getText().toString();
			Result result = new Result(barcode);
			setResult(result);				
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			data = savedInstanceState.getParcelable(BarcodeScanFragment.FRAGMENT_DATA);
		}

		ibScanBarcode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(activity, CaptureActivity.class);

				/*** [START] :: Permission ***/

				/*Intent intent = new IntentIntegrator(activity).createScanIntent();
				startActivityForResult(intent, REQUEST_QR_SCAN);*/

				new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

					@Override
					public void onSuccess(BHPermissions bhPermissions) {
						Intent intent = new IntentIntegrator(activity).createScanIntent();
						startActivityForResult(intent, REQUEST_QR_SCAN);
					}

					@Override
					public void onNotSuccess(BHPermissions bhPermissions) {
						bhPermissions.openAppSettings(getActivity());
					}

					@Override
					public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
						bhPermissions.showMessage(getActivity(), permissionType);
					}

				}, BHPermissions.PermissionType.CAMERA);
				/*** [END] :: Permission ***/


			}
		});

		if (data != null) {
			if (data.viewTitle != null) {
				vwTitle.setText(data.viewTitle);
			} else if (data.viewTitleResID > -1) {
				vwTitle.setText(data.viewTitleResID);
			}

			if (data.description != null) {
				tvDescription.setText(data.description);
			} else if (data.descriptionResID > -1) {
				tvDescription.setText(data.descriptionResID);
			}
		}

//        if (BHPreference.ProcessType() != null && BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.Sale.toString())){
//            edtBarcode.setEnabled(false);
//        }else {
//            edtBarcode.setEnabled(true);
//        }

		/*if(!BHGeneral.DEVELOPER_MODE){
			edtBarcode.setEnabled(false);
		}*/

		if(BHGeneral.BARCODE_KEY_IN_MODE){
			edtBarcode.setEnabled(true);
		} else {
			edtBarcode.setEnabled(false);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putParcelable(BarcodeScanFragment.FRAGMENT_DATA, data);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_QR_SCAN) {
			if (resultCode == Activity.RESULT_CANCELED) {
				showMessage("ยังไม่ได้ทำการสแกน กรุณาทำการสแกนอีกครั้ง");
			} else if (resultCode == Activity.RESULT_OK) {
				String barcode = intent.getStringExtra(Intents.Scan.RESULT);
				edtBarcode.setText(barcode);
				Result result = new Result(barcode);
				setResult(result);
			}
		}
	}

	public void setTitle(int titleResID) {
		data.titleResID = titleResID;
	}

	public void setViewTitle(int viewTitleResID) {
		data.viewTitleResID = viewTitleResID;
	}

	public void setViewTitle(String viewTitle) {
		data.viewTitle = viewTitle;
	}

	public void setDescription(int descriptionResID) {
		data.descriptionResID = descriptionResID;
	}

	public void setDescription(String description) {
		data.description = description;
	}
	
}
