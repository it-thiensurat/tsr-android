package th.co.thiensurat.fragments.products.change;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ProductStockController.ProductStockStatus;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.share.BarcodeScanFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment.ScanCallBack;
import th.co.thiensurat.views.ViewTitle;

public class ChangeProductCheckFragment extends BHFragment {

	public static class Data extends BHParcelable {
		public String OldProductSerialNumber;
		public String OldProductID;
		public String OldProductName;
		public String NewProductSerialNumber;
		public String NewProductID;
		public String NewProductName;
		public Boolean statusPage;
	}

	private Data dataProduct;	

	@InjectView
	TextView textShowScan, textShowType;

	@InjectView
	ViewTitle ViewTitle1;

	@Override
	protected int titleID() {
		return R.string.title_change_product;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_change_product_check;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_next };
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dataProduct = getData();
		if (dataProduct.statusPage == false) {
			textShowScan.setText(dataProduct.OldProductSerialNumber);
			textShowType.setText(dataProduct.OldProductName);
		} else {
			ViewTitle1.setText(R.string.title_scan_new_product);
			textShowScan.setText(dataProduct.NewProductSerialNumber);
			textShowType.setText(dataProduct.NewProductName);
		}
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_next:
			next();
			break;

		case R.string.button_back:
			showLastView();
			break;

		default:
			break;
		}
	}

	private void next() {
		if (dataProduct.statusPage == true) {
			ChangeProductDetailFragment.Data dataDetail = new ChangeProductDetailFragment.Data();
			dataDetail.OldProductSerialNumber = dataProduct.OldProductSerialNumber;
			dataDetail.OldProductID = dataProduct.OldProductID;
			dataDetail.OldProductName = dataProduct.OldProductName;
			dataDetail.NewProductSerialNumber = dataProduct.NewProductSerialNumber;
			dataDetail.NewProductID = dataProduct.NewProductID;
			dataDetail.NewProductName = dataProduct.NewProductName;
			ChangeProductDetailFragment fmProductDetail = BHFragment.newInstance(ChangeProductDetailFragment.class, dataDetail);
			showNextView(fmProductDetail); // เปิดหน้าแสดงรายละเอียด
		} else {
			// Scan New สแกนเครื่องใหม่
			BarcodeScanFragment fmProductNew = BHFragment.newInstance(BarcodeScanFragment.class, new ScanCallBack() {

				@Override
				public void onResult(BHParcelable data) {

					final BarcodeScanFragment.Result barcodeResult = (BarcodeScanFragment.Result) data;	
					(new BackgroundProcess(activity) {
						ProductStockInfo resultProduct = null;	

						@Override
						protected void calling() {
							resultProduct = TSRController.getProductStock(BHPreference.organizationCode(), barcodeResult.barcode, ProductStockStatus.CHECKED.toString());
						}

						@Override
						protected void after() { 
							if (resultProduct != null) {
								ChangeProductCheckFragment.Data dataCheck = new ChangeProductCheckFragment.Data();
								dataCheck.OldProductSerialNumber = dataProduct.OldProductSerialNumber;
								dataCheck.OldProductID = dataProduct.OldProductID;
								dataCheck.OldProductName = dataProduct.OldProductName;
								dataCheck.NewProductSerialNumber = resultProduct.ProductSerialNumber;
								dataCheck.NewProductID = resultProduct.ProductID;
								dataCheck.NewProductName = resultProduct.ProductName;
								dataCheck.statusPage = true;
								ChangeProductCheckFragment fmProductCheck = BHFragment.newInstance(ChangeProductCheckFragment.class, dataCheck);
								showNextView(fmProductCheck); // เปิดหน้าแสดงรหัสสินค้่า,ชื่อสินค้า
							} else {
								final String title = "ChangeProduct";
								String message = "ไม่พบสินค้า";
                                showNoticeDialogBox(title, message);
							}				
						}
					}).start();
				}

				@Override
				public String onNextClick() {
					return null;
				}
			});

			fmProductNew.setTitle(R.string.title_change_product);
			fmProductNew.setViewTitle(R.string.title_scan_new_product);
			showNextView(fmProductNew); // เปิดหน้าสแกนเครื่องใหม่
		}
	}

	public static void showNoticeDialogBox(final String title, final String message) {
		AlertDialog.Builder setupAlert;
		setupAlert = new AlertDialog.Builder(activity);
		setupAlert.setTitle(title);
		setupAlert.setMessage(message);
		setupAlert.setPositiveButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// ??
			}
		});
		setupAlert.show();
	}
}
