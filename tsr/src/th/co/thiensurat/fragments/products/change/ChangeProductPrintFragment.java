package th.co.thiensurat.fragments.products.change;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.PrinterController;
import th.co.thiensurat.data.controller.DocumentHistoryController.DocumentType;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.ChangeProductInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.fragments.document.manual.ManualDocumentDetailFragment;

public class ChangeProductPrintFragment extends BHFragment {

	public static class Data extends BHParcelable {
		public String ChangeProductID, ChangeProductNo, ChangeProductDate, Product, OldSerialNo, NewSerialNo, ContractNo, CustomerName, Address, Tel,
				InstallDate;
		public boolean StatusPageHistory;
	}

	private Data dataContract;
	String Date;

	@InjectView
	private TextView txtNo, txtEffDate, txtProduct, txtOldSerialNo, txtNewSerialNo, txtContractNo, txtCustomerName, txtAddress, txtTel, txtInstallDate,txtCodeEmp,txtTeamCode;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_change_product_print;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_change_product;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		// return new int[] { R.string.button_back };

		dataContract = getData();
		int[] ret;
		if (dataContract !=null && dataContract.StatusPageHistory == true) {
			ret =new int[] { R.string.button_back, R.string.button_print };
		} else {
			//ret = new int[] { R.string.button_print, R.string.button_save_manual_change_product, R.string.button_end };
			ret = new int[] { R.string.button_print, R.string.button_end };
		}
		
		return ret;
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_back:
			showLastView();
			break;
		case R.string.button_end:
			ChangeProductCustomerListFragment fmMain = BHFragment.newInstance(ChangeProductCustomerListFragment.class);
			showNextView(fmMain); // เปิดหน้า List หน้าแรก
			break;
		case R.string.button_save_manual_change_product:
			ManualDocumentDetailFragment.Data data1 = new ManualDocumentDetailFragment.Data();

			data1.DocumentNo = txtNo.getText().toString();
			data1.DocumentType = DocumentType.ChangeProduct.toString();

			ManualDocumentDetailFragment fmManualDocDetail = BHFragment.newInstance(ManualDocumentDetailFragment.class, data1);
			showNextView(fmManualDocDetail);
			break;
        case R.string.button_print:
            new PrinterController(activity).printChangeProduct(dataContract.ChangeProductID);
                break;
		default:
			break;
		}
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dataContract = getData();
		loadData();

	}
	
	private void loadData() {
		(new BackgroundProcess(activity) {
			
			ChangeProductInfo changeProduct=null;
			ContractInfo contract = null;
			AddressInfo address = null;
			
			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				changeProduct = getChangeProductByID(BHPreference.organizationCode(), dataContract.ChangeProductID);
				contract = getContractByRefNoForSendDocuments(BHPreference.organizationCode(),changeProduct.RefNo);
				address = getAddress(changeProduct.RefNo, AddressType.AddressIDCard);
			}
			
			@Override
			protected void after() {
				if (changeProduct !=null) {
					txtNo.setText(changeProduct.ChangeProductPaperID);
                    txtEffDate.setText(BHUtilities.dateFormat(changeProduct.EffectiveDate, "dd/MM/yyyy"));
					txtProduct.setText(changeProduct.ProductName);
					txtOldSerialNo.setText(changeProduct.OldProductSerialNumber);
					txtNewSerialNo.setText(changeProduct.NewProductSerialNumber);
					txtCustomerName.setText(String.format("%s", BHUtilities.trim(changeProduct.CustomerFullName)));
					txtInstallDate.setText(BHUtilities.dateFormat(changeProduct.InstallDate, "dd/MM/yyyy"));
				}
				
				if (contract !=null) {
					txtContractNo.setText(contract.CONTNO);
					txtCodeEmp.setText(String.format("รหัสพนักงานขาย %s : %s", contract.EmpID, contract.SaleEmployeeName));
					txtTeamCode.setText(String.format("ทีม %s : %s", contract.SaleTeamCode, contract.upperEmployeeName));
				}
				
				if (address !=null) {
					txtAddress.setText(address.Address());
                    txtTel.setText(address.Telephone());
				}
			}
		}).start();
	}

}
