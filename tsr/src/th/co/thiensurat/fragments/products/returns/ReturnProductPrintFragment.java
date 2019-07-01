package th.co.thiensurat.fragments.products.returns;

import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.PrinterController;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ReturnProductController.ReturnProductStatus;
import th.co.thiensurat.data.info.ReturnProductDetailInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ReturnProductPrintFragment  extends BHFragment  {

	public static class Data extends BHParcelable {
		public String ReturnProductID;
	}
	
	@InjectView private TextView txtReturnProductID;
	@InjectView private TextView txtReturnProductDate;
	@InjectView private TextView txtEmpID;
	@InjectView private TextView txtTeamCode;
	
	@InjectView private TextView txtStatus;
	@InjectView private TextView txtRemark;
	@InjectView private TextView txtEmpName;

	@InjectView private TextView txtReturnProductDetail;
	@InjectView private ListView lvReturnProductList;
	
	private List<ReturnProductDetailInfo> retProdDetailList;
	Data data;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_return_product_print;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_print, R.string.button_end };
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_return_product;
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
			case R.string.button_print:
				printDocument();
				break;
			case R.string.button_end:
				ReturnProductListFragment fmList = BHFragment.newInstance(ReturnProductListFragment.class);
				showNextView(fmList); // เปิดหน้า List หน้าแรก
				break;
			default:
				break;
		}
	}

	private void printDocument() {
		// TODO Auto-generated method stub
	    new PrinterController(activity).printReturnProduct(retProdDetailList);
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		data = getData();		
		getReturnProductDetail();
	}

	private void getReturnProductDetail() {
		(new BackgroundProcess(activity) {

			@Override
			protected void calling() {
				try {
					retProdDetailList = TSRController.getReturnProductDetailByID(BHPreference.organizationCode(), data.ReturnProductID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			protected void after() {
				if (retProdDetailList != null) {
					bindReturnProduct();
					bindReturnProductDetailList();
				}
			}

		}).start();
	}

	private void bindReturnProduct() {
		try {
			txtReturnProductID.setText(BHUtilities.trim(retProdDetailList.get(0).ReturnProductID));
			txtReturnProductDate.setText(BHUtilities.dateFormat(retProdDetailList.get(0).ReturnDate, "dd/MM/yyyy"));
			txtEmpID.setText(String.format("%s %s", BHUtilities.trim(retProdDetailList.get(0).EmpID), BHUtilities.trim(retProdDetailList.get(0).EmployeeName)));
			txtTeamCode.setText(BHUtilities.trim(retProdDetailList.get(0).TeamCode));
//			txtReceiveDate.setText(BHUtilities.dateFormat(retProdDetailList.get(0).RecevieDate,  "dd/MM/yyyy"));
			
//			txtStatus.setText(BHUtilities.trim(retProdDetailList.get(0).Status));
			String status = BHUtilities.trim(retProdDetailList.get(0).Status);
			if (status.contentEquals(ReturnProductStatus.REQUEST.toString())) {
				txtStatus.setText("รอการตรวจสอบสินค้า");
			} else if (status.contentEquals(ReturnProductStatus.REJECT.toString())) {
				txtStatus.setText("ไม่ผ่านการตรวจสอบสินค้า");
			} else if (status.contentEquals(ReturnProductStatus.PASS.toString())) {
				txtStatus.setText("ผ่านการตรวจสอบสินค้า");
			}

			txtRemark.setText(BHUtilities.trim(retProdDetailList.get(0).Remark));
			txtReturnProductDetail.setText(String.format("รายละเอียดสินค้าที่ส่งคืน (%s รายการ)", retProdDetailList.size()));
			txtEmpName.setText(String.format("( %s )", retProdDetailList.get(0).EmployeeName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bindReturnProductDetailList() {
		BHArrayAdapter<ReturnProductDetailInfo> adapter = new BHArrayAdapter<ReturnProductDetailInfo>(activity, R.layout.list_return_product_detail_item, retProdDetailList) {

			class ViewHolder {
				public TextView txtProductSerialNumber;
			}

			@Override
			protected void onViewItem(int position, View view, Object holder, ReturnProductDetailInfo info) {
				ViewHolder vh = (ViewHolder) holder;
				try {
					vh.txtProductSerialNumber.setText(info.ProductSerialNumber);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		lvReturnProductList.setAdapter(adapter);
		// Fixed - [BHPROJ-0024-863] :: 6. [Meeting@TSR@11/02/59] [Android-ส่งคืนสินค้าเข้าระบบ] ตรวจสอบ UI เรื่องการ Scrolling หน้าจอ ลูกค้าแจ้งว่าไม่สามารถ Scroll down ได้กรณีที่ข้อมูลยาว ๆ
		lvReturnProductList.setOnItemClickListener(null);
		BHUtilities.setListViewHeightBasedOnChildren(lvReturnProductList);
	}

}
