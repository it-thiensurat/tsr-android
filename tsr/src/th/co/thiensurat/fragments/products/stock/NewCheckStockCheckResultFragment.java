package th.co.thiensurat.fragments.products.stock;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.controller.ProductStockController.ProductStockStatus;
import th.co.thiensurat.data.info.ProductStockInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NewCheckStockCheckResultFragment extends BHFragment {

	private static final String PRODUCT_STOCK_STATUS_OVER = ProductStockStatus.OVER.toString();// "OVER";
	private static final String PRODUCT_STOCK_STATUS_CHECKED = ProductStockStatus.CHECKED.toString();// "CHECKED";
	private static final String PRODUCT_STOCK_STATUS_WAIT = ProductStockStatus.WAIT.toString();// "WAIT";
//    private static final String PRODUCT_STOCK_STATUS_DAMAGE = "DAMAGE";	// ชำรุด
//    private static final String PRODUCT_STOCK_STATUS_TEAM_DESTROY = "TEAM_DESTROY";	// ยุบทีม

	private static final String LIST_PRODUCT = "ScannedProductStockList";

	public static class Data extends BHParcelable {
		public int scannedCount;
		public List<ProductStockInfo> scannedProductList;
	}

	@InjectView private LinearLayout linearLayoutDisplayOver;
	@InjectView private LinearLayout linearLayoutDisplayMiss;
	@InjectView private ListView lvProductWait, lvProductOver;

	@InjectView private TextView txtProduct;
	@InjectView private TextView txtNumResult;
	@InjectView private TextView txtSumResult;

	@InjectView private TextView txtOverCount;
	@InjectView private TextView txtWaitCount;
	@InjectView private TextView txtCheckedCount;
	

	ArrayAdapter<String> adapter;
	private Data resultData;
	List<ProductStockInfo> resultProductList;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_check_stock_check_result;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_check_stock;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_showlist };
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_showlist:
			try {
				NewCheckStockMainFragment fmMain = new NewCheckStockMainFragment();
				showNextView(fmMain);
			} catch (Exception e) {
				e.printStackTrace();
			}
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

		try {
			resultData = getData();
			// String date = BHUtilities.dateFormat(new Date(), "dd/MM/yyyy");
			// txtDateNow.setText("ณ   วันที่   "+ date);

			if (savedInstanceState != null) {
				if (savedInstanceState.containsKey(LIST_PRODUCT)) {
					resultProductList = savedInstanceState.getParcelableArrayList(LIST_PRODUCT);
				}
			}

			if (resultProductList == null) {
				resultProductList = resultData.scannedProductList;
			}

			int stockCount = 0;
			int waitCount = 0;
			int overCount = 0;
			int checkedCount = 0;

			for (ProductStockInfo item : resultProductList) {
				if (!item.Status.equals(PRODUCT_STOCK_STATUS_OVER)) {
					stockCount++;
				}
				if (item.Status.equals(PRODUCT_STOCK_STATUS_WAIT)) {
					waitCount++;
				} else if (item.Status.equals(PRODUCT_STOCK_STATUS_OVER)) {
					overCount++;
				} else if (item.Status.equals(PRODUCT_STOCK_STATUS_CHECKED)) {
					checkedCount++;
				}
			}

			txtProduct.setText(String.valueOf(waitCount)); // รอตรวจสอบ
			// txtNumResult.setText(String.valueOf(checkedCount + overCount)); // สินค้าที่ scan ไปแล้ว (CHECKED+OVER) ==> ใช้ไม่ได้
			// เพราะบางทีสินค้าที่ OVER อาจจะยังไม่ได้ scan ในครั้งนี้ แต่เป็นสินค้าคงค้างจากครั้งก่อน
			txtNumResult.setText(String.valueOf(resultData.scannedCount)); // สินค้าที่ scan ไปแล้ว (ใช้วิธีรับค่ามาจากหน้าที่แล้วแทน)
			txtSumResult.setText(String.valueOf(stockCount)); // สินค้าทั้งหมดที่ต้อง scan (สินค้าที่เบิกจากคลัง)

			txtCheckedCount.setText(String.valueOf(checkedCount)); // สินค้าที่ถูกต้อง
			txtOverCount.setText(String.valueOf(overCount)); // สินค้าที่ไม่ถูกต้อง
			txtWaitCount.setText(String.valueOf(waitCount)); // สินค้าที่ขาด

			bindOverProduct();
			bindMissProduct();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(LIST_PRODUCT, (ArrayList<ProductStockInfo>) resultProductList);
	}

	private void bindOverProduct() {
		try {
			linearLayoutDisplayOver.setVisibility(View.GONE);
			if (resultData.scannedProductList != null) {
				List<String> overProduct = new ArrayList<String>();
				for (ProductStockInfo item : resultData.scannedProductList) {
					if (item.Status.equals(PRODUCT_STOCK_STATUS_OVER)) {
						// overProduct.add(String.format(item.ProductSerialNumber));
						overProduct.add(String.format(("%s   %s     %s"), item.ProductSerialNumber, "เป็นของทีม", item.TeamCode));
					}
				}
				if (overProduct.size() > 0) {
					linearLayoutDisplayOver.setVisibility(View.VISIBLE);
					lvProductOver.setVisibility(View.VISIBLE);
					adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, overProduct);
					lvProductOver.setAdapter(adapter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bindMissProduct() {
		try {
			linearLayoutDisplayMiss.setVisibility(View.GONE);
			if (resultData.scannedProductList != null) {
				List<String> missProduct = new ArrayList<String>();
				for (ProductStockInfo item : resultData.scannedProductList) {
					if (item.Status.equals(PRODUCT_STOCK_STATUS_WAIT)) {
						missProduct.add(String.format(("               %s                    %s"), item.ProductSerialNumber, "(ขาด)"));
					}
				}
				if (missProduct.size() > 0) {
					linearLayoutDisplayMiss.setVisibility(View.VISIBLE);
					lvProductWait.setVisibility(View.VISIBLE);
					adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, missProduct);
					lvProductWait.setAdapter(adapter);
				}
			}
			/*
			(new BackgroundProcess(activity) {

				GetProductStockByStatusInputInfo input = new GetProductStockByStatusInputInfo();
				GetProductStockByStatusOutputInfo output = new GetProductStockByStatusOutputInfo();

				@Override
				protected void before() {
					// TODO Auto-generated method stub
					input.OrganizationCode = BHPreference.organizationCode();
					input.TeamCode = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.teamCode();
					input.Status = PRODUCT_STOCK_STATUS_WAIT;
				}

				@Override
				protected void calling() {
					// TODO Auto-generated method stub
					output = TSRService.getProductStockByStatus(input, false);

				}

				@Override
				protected void after() {
					// TODO Auto-generated method stub
					if (output.Info != null) {
						List<String> missProduct = new ArrayList<String>();
						for (ProductStockInfo item : output.Info) {
							if (item.Status.equals(PRODUCT_STOCK_STATUS_WAIT)) {
								missProduct.add(String.format(("               %s                    %s"), item.ProductSerialNumber, "(ขาด)"));
							}
						}
						if (missProduct.size() > 0) {
							linearLayoutDisplayMiss.setVisibility(View.VISIBLE);
							lvProductWait.setVisibility(View.VISIBLE);
							adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, missProduct);
							lvProductWait.setAdapter(adapter);
						}
					}
				}
			}).start();
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
