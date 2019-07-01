package th.co.thiensurat.fragments.products.returns;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHListViewItem;
import th.co.bighead.utilities.BHListViewItem.RowType;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.FortnightController;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.controller.ReturnProductController.ReturnProductStatus;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.data.info.ReturnProductDetailInfo;
import th.co.thiensurat.data.info.ReturnProductInfo;

public class ReturnProductAddFragment extends BHFragment  {

	@InjectView private TextView txtWaitProductCount;
	@InjectView private PinnedSectionListView lvProduct;

	private static class Data extends BHParcelable {
		public List<Integer> masterList;
		public List<Integer> masterSelected;
	}

	private Data data;
	private BHListViewAdapter productAdapter;
	private List<ProductStockInfo> prodStkList;

	
	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_return_product_add;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back , R.string.button_save_return_product};
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
		case R.string.button_save_return_product:
			/*** [START] :: Fixed - [BHPROJ-0026-971] :: Add confirm message before ReturnProduct ***/
			AlertDialog.Builder setupAlert;
			setupAlert = new AlertDialog.Builder(activity)
					.setTitle("ยืนยันการทำรายการ")
					.setMessage("ท่านต้องการยืนยันการทำรายการส่งคืนสินค้าเข้าระบบใช่หรือไม่?")
					.setCancelable(false)
					.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							saveReturnProduct();
						}
					})
					.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.cancel();
						}
					});
			setupAlert.show();
			/*** [END] :: Fixed - [BHPROJ-0026-971] :: Add confirm message before ReturnProduct ***/
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
		try 
		{
			if (savedInstanceState != null) {
				data = savedInstanceState.getParcelable(BH_FRAGMENT_DEFAULT_DATA);
			}
	
			if (data == null) {
				getDataList();
			}
			
			setWidgetsEventListener();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putParcelable(BH_FRAGMENT_DEFAULT_DATA, data);
		super.onSaveInstanceState(outState);
	}

	private void setWidgetsEventListener() {
		try {
			lvProduct.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					BHListViewItem item = productAdapter.getItem(position);
					if (item.type == RowType.Header) {
						if (data.masterList.size() != data.masterSelected.size()) {
							data.masterSelected = new ArrayList<Integer>(data.masterList);
						} else {
							data.masterSelected = new ArrayList<Integer>();
						}
					} else if (item.type == RowType.Item) {
						Integer value = data.masterList.get(item.row);
						if (data.masterSelected.contains(value)) {
							data.masterSelected.remove(value);
						} else {
							data.masterSelected.add(value);
						}
					}
					productAdapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getDataList() {
		(new BackgroundProcess(activity) {
			@Override
			protected void calling() {
				try {

//					prodStkList = TSRController.getAvailableProductStockForReturn(BHPreference.organizationCode(), BHPreference.teamCode());
					prodStkList = TSRController.getAvailableProductStockForReturn(BHPreference.organizationCode(), BHPreference.selectTeamCodeOrSubTeamCode(), BHPreference.teamCode());
					
					/*
					prodStkList = new ArrayList<ProductStockInfo>();
					ProductStockInfo info;
					
					info = new ProductStockInfo();
					info.ProductSerialNumber = "PH55510001";
					info.ProductID = "10";
					info.Status = "CHECKED";
					prodStkList.add(info);

					info = new ProductStockInfo();
					info.ProductSerialNumber = "PH55510002";
					info.ProductID = "10";
					info.Status = "CHECKED";
					prodStkList.add(info);

					info = new ProductStockInfo();
					info.ProductSerialNumber = "SA55520000";
					info.ProductID = "12";
					info.Status = "RETURN";
					prodStkList.add(info);
					*/
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	//calling
			@Override
			protected void after() {
				try {
					if (prodStkList != null) {
						data = new Data();
						data.masterList = new ArrayList<Integer>();
						int len = prodStkList.size();
						for (int ii = 0; ii < len; ii++) {
							data.masterList.add(Integer.valueOf(ii));
						}
						data.masterSelected = new ArrayList<Integer>(data.masterList);
						txtWaitProductCount.setText(String.valueOf(data.masterList.size()));
						bindListView();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	//after
		}).start();
	}

	private void bindListView() {
		productAdapter = new BHListViewAdapter(activity) {

			class ViewHolder {
				public CheckedTextView text1;
				public TextView txtProductSerial;
				public TextView txtStatus;
			}

			@Override
			protected boolean canHeaderSelected(int section) {
				return true;
			};

			@Override
			protected int viewForItem(int section, int row) {
				// TODO Auto-generated method stub
//				return android.R.layout.simple_list_item_checked;
				return R.layout.list_return_product_add_item;
			}

			@Override
			protected int viewForSectionHeader(int section) {
//				return android.R.layout.simple_list_item_checked;
				return R.layout.list_return_product_add_header;
			};

			@Override
			protected void onViewItem(View view, Object holder, int section, int row) {
				// TODO Auto-generated method stub
				ViewHolder vh = (ViewHolder) holder;
				Integer value = data.masterList.get(row);
//				vh.text1.setText(prodStkList.get(row).ProductSerialNumber);
				vh.text1.setChecked(data.masterSelected.contains(value));
                vh.txtProductSerial.setText(prodStkList.get(row).ProductSerialNumber);

				/* [START] :: Fixed - [BHPROJ-0024-862]] 5. [Meeting@TSR@11/02/59] [Android-ส่งคืนสินค้าเข้าระบบ] แก้ไข Wording สถานะเดิมของสินค้า (SourceStatus) จาก EN --> TH */
                vh.txtStatus.setText(prodStkList.get(row).Status);
				if (prodStkList.get(row).Status.contentEquals(ProductStockController.ProductStockStatus.WAIT.toString())) {
					vh.txtStatus.setText("รอตรวจสอบ");
				} else if (prodStkList.get(row).Status.contentEquals(ProductStockController.ProductStockStatus.CHECKED.toString())) {
					vh.txtStatus.setText("ตรวจสอบแล้ว");
				} else if (prodStkList.get(row).Status.contentEquals(ProductStockController.ProductStockStatus.SOLD.toString())) {
					vh.txtStatus.setText("ขายแล้ว");
				} else if (prodStkList.get(row).Status.contentEquals(ProductStockController.ProductStockStatus.OVER.toString())) {
					vh.txtStatus.setText("สินค้าเกิน");
				} else if (prodStkList.get(row).Status.contentEquals(ProductStockController.ProductStockStatus.RETURN.toString())) {
					vh.txtStatus.setText("ถอดเครื่อง/เปลี่ยนเครื่อง");
				} else if (prodStkList.get(row).Status.contentEquals(ProductStockController.ProductStockStatus.DAMAGE.toString())) {
					vh.txtStatus.setText("ชำรุด");
				} else if (prodStkList.get(row).Status.contentEquals(ProductStockController.ProductStockStatus.TEAM_DESTROY.toString())) {
					vh.txtStatus.setText("ยุบทีม");
				} else if (prodStkList.get(row).Status.contentEquals(ProductStockController.ProductStockStatus.TRADEIN.toString())) {
					vh.txtStatus.setText("เครื่องเทิร์น");
				} else if (prodStkList.get(row).Status.contentEquals(ProductStockController.ProductStockStatus.WAIT_RETURN.toString())) {
					vh.txtStatus.setText("รอตรวจสอบจากการส่งคืนสินค้าเข้าระบบ");
				}
				/* [END] :: Fixed - [BHPROJ-0024-862]] 5. [Meeting@TSR@11/02/59] [Android-ส่งคืนสินค้าเข้าระบบ] แก้ไข Wording สถานะเดิมของสินค้า (SourceStatus) จาก EN --> TH */

			}

			@Override
			protected void onViewSectionHeader(View view, Object holder, int section) {
				view.setBackgroundResource(R.color.bg_table_main_header);

				ViewHolder vh = (ViewHolder) holder;
//				vh.text1.setText("เลือกสินค้าส่งคืนเข้าระบบทั้งหมด");
				vh.text1.setChecked(data.masterSelected.size() == data.masterList.size());
			};

			@Override
			protected int getItemCount(int section) {
				// TODO Auto-generated method stub
				return data.masterList.size();
			}
		};
		lvProduct.setAdapter(productAdapter);
	}

	private void saveReturnProduct() {
		(new BackgroundProcess(activity) {
			
			ReturnProductInfo retProd = new ReturnProductInfo();
			List<ReturnProductDetailInfo> retProdDetList = new ArrayList<ReturnProductDetailInfo>();

			@Override
			protected void before() {
				// TODO Auto-generated method stub
//			    retProd.ReturnProductID = DatabaseHelper.getUUID();
			    retProd.ReturnProductID = getAutoGenerateDocumentID(TSRController.DocumentGenType.ReturnProduct, BHPreference.SubTeamCode(), BHPreference.saleCode());
			    retProd.OrganizationCode = BHPreference.organizationCode();
			    retProd.EmpID = BHPreference.employeeID();
			    retProd.TeamCode = BHPreference.teamCode();
			    retProd.SubTeamCode= BHPreference.selectTeamCodeOrSubTeamCode();

			    retProd.ReturnDate = new Date();
//			    retProd.FortnightID = new TSRController().getCurrentFortnight(BHPreference.organizationCode()).FortnightID;
//			    retProd.RecevieDate = new Date();
                retProd.FortnightID = new FortnightController().getCurrentFortnightInfo().FortnightID;
                retProd.Status = ReturnProductStatus.REQUEST.toString();
			    retProd.CreateDate = new Date();
			    retProd.CreateBy = BHPreference.employeeID();
			    retProd.LastUpdateDate = new Date();
			    retProd.LastUpdateBy = BHPreference.employeeID();				
			    retProd.SyncedDate = new Date();
				
				ReturnProductDetailInfo retProdDet;

				for (int ii = 0; ii < prodStkList.size(); ii++) {
					boolean chk = data.masterSelected.contains(data.masterList.get(ii));
//					showMessage(String.format("Row(%s): %s(%s) is Checked = %s", ii, prodStkList.get(ii).ProductSerialNumber, prodStkList.get(ii).Status, chk));
					
					if (chk) {
						retProdDet = new ReturnProductDetailInfo();
						retProdDet.OrganizationCode = BHPreference.organizationCode();
						retProdDet.ReturnProductID = retProd.ReturnProductID;
                        // IF TRADE IN FROM CONTRACT (ProductSerialNumber = TradeInProductCode)
                        // ELSE DAMAGE, TEAM_DESTROY, CHECKED, RETURN FROM PRODUCT_STOCK (ProductSerialNumber = ProductSerialNumber)
                        retProdDet.ProductSerialNumber = prodStkList.get(ii).ProductSerialNumber != null? prodStkList.get(ii).ProductSerialNumber:"";
						retProdDet.ProductID = prodStkList.get(ii).ProductID;

                        retProdDet.RefNo = prodStkList.get(ii).RefNo;
                        retProdDet.TradeInProductModel = prodStkList.get(ii).TradeInProductModel;
                        retProdDet.TradeInBrandCode = prodStkList.get(ii).TradeInBrandCode;
                        
                        /** Fixed [BHPRJ00301-3728] **/
                        retProdDet.SourceStatus = prodStkList.get(ii).Status;
                        retProdDet.Status = ReturnProductStatus.REQUEST.toString();
                        retProdDet.CreateDate = new Date();
                        retProdDet.CreateBy = BHPreference.employeeID();
                        retProdDet.LastUpdateDate = new Date();
                        retProdDet.LastUpdateBy = BHPreference.employeeID();		
						retProdDetList.add(retProdDet);
					}
				}

				if (retProdDetList.size() <= 0) {
					showMessage("ไม่มีการเลือกสินค้าเพื่อส่งคืนเข้าระบบ!");
				}
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				try {
					if (retProdDetList.size() > 0) {
						TSRController.insertRequestReturnProduct(retProd, retProdDetList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					if (retProdDetList.size() > 0) {
						ReturnProductDisplayFragment.Data dataReturn = new ReturnProductDisplayFragment.Data();
						dataReturn.ReturnProductID = retProd.ReturnProductID;
						ReturnProductDisplayFragment fmDisplay = BHFragment.newInstance(ReturnProductDisplayFragment.class, dataReturn);
						showNextView(fmDisplay);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	

}
