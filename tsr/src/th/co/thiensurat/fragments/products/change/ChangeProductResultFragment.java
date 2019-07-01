package th.co.thiensurat.fragments.products.change;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;

import th.co.bighead.utilities.BHBitmap;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.ProductStockController.ProductStockStatus;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ChangeProductInfo;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ProductStockInfo;

public class ChangeProductResultFragment extends BHFragment {

    private Uri fileUri;
    private static String Parth;
    private static String imageTypeCode;
    public static String imageID;


    public static class Data extends BHParcelable {
		public ChangeProductInfo changeProduct;
		public AssignInfo assign;
		//public DocumentHistoryInfo docHist;
		public AddressInfo addressIDCard;
		public ContractInfo contract;
        public ContractImageInfo contractImage;
		public String causeProblem;
	}

	@InjectView
	private TextView txtCode, txtProduct, txtNo, txtCusName, txtAddress, txtTel, txtSale, txtTeam, txtDataInstall, txtProblem, txtMore, txtNewProduct, txtEffDate,
			txtName;
	@InjectView
	private ImageView imageViewChange;

	private Data dataChangeProduct;
	
	Calendar myCal = Calendar.getInstance();

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_change_product_result;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_save };
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_change_product;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		dataChangeProduct = getData();
		
		txtCode.setText(dataChangeProduct.changeProduct.OldProductSerialNumber);
		//txtProduct.setText(dataChangeProduct.actionChangeProduct.ProductName);
		txtProduct.setText(dataChangeProduct.contract.ProductName);
		txtNo.setText(dataChangeProduct.contract.CONTNO);
		
		if (dataChangeProduct.contract.CustomerFullName.equals("")) {
			txtName.setText(dataChangeProduct.contract.CompanyName);
		} else {
			txtName.setText(dataChangeProduct.contract.CustomerFullName);
		}

        txtCusName.setText(String.format("%s", BHUtilities.trim(dataChangeProduct.contract.CustomerFullName)));

		if (dataChangeProduct.addressIDCard != null) {
			txtAddress.setText(dataChangeProduct.addressIDCard.Address());
			txtTel.setText(dataChangeProduct.addressIDCard.Telephone());
		}
		
		txtSale.setText(dataChangeProduct.contract.SaleEmployeeName);
		txtTeam.setText(dataChangeProduct.contract.upperEmployeeName);
		txtDataInstall.setText(BHUtilities.dateFormat(dataChangeProduct.contract.InstallDate, "dd/MM/yyyy"));
		txtProblem.setText(dataChangeProduct.causeProblem);
		txtMore.setText(dataChangeProduct.changeProduct.ResultDetail);
		txtNewProduct.setText(dataChangeProduct.changeProduct.NewProductSerialNumber);
        txtEffDate.setText(BHUtilities.dateFormat(dataChangeProduct.changeProduct.EffectiveDate, "dd/MM/yyyy"));
		txtName.setText(dataChangeProduct.changeProduct.empName);

        if(dataChangeProduct.contractImage != null){
            BHStorage.FolderType F = BHStorage.FolderType.Picture;
            Parth = BHStorage.getFolder(F);
            imageTypeCode = ContractImageController.ImageType.CHANGEPRODUCT.toString();

            File newFolder = new File(String.format("%s/%s/%s/%s.jpg", BHStorage.getFolder(BHStorage.FolderType.Picture), BHPreference.RefNo(), imageTypeCode, dataChangeProduct.contractImage.ImageID));
            if(newFolder.exists()){
                fileUri = Uri.fromFile(newFolder);
                previewCapturedImage();
            }else{
                Log.e("TAG", "File Not Found");
            }
        }else{
            imageViewChange.setVisibility(View.INVISIBLE);
        }
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		super.onProcessButtonClicked(buttonID);

		switch (buttonID) {
		case R.string.button_save:
			final String title = "ChangeProduct";
			String message = "";
			Builder setupAlert;
			message = "ต้องการบันทึกข้อมูลการเปลี่ยนเครื่องหรือไม่";
			setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					save();					
				}
			}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					// TODO Auto-generated method stub					
				}
			});
			setupAlert.show();			
			break;
		case R.string.button_back:
			showLastView();
			break;

		default:
			break;
		}
	}
	
	private void save() {
		(new BackgroundProcess(activity) {

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
													
				ProductStockInfo oldProductStock = getProductStock(dataChangeProduct.changeProduct.OldProductSerialNumber);
				if (oldProductStock != null)
				{
					oldProductStock.Status = ProductStockStatus.RETURN.toString();
				}
				
				ProductStockInfo newProductStock = getProductStock(dataChangeProduct.changeProduct.NewProductSerialNumber);
				if (newProductStock != null)
				{
					newProductStock.Status = ProductStockStatus.SOLD.toString();
				}
                if(dataChangeProduct.contractImage != null && dataChangeProduct.contractImage.ImageID != null && !dataChangeProduct.contractImage.ImageID.equals("")){
                    File newFolder = new File(String.format("%s/%s/%s/%s.jpg", BHStorage.getFolder(BHStorage.FolderType.Picture), BHPreference.RefNo(), imageTypeCode, dataChangeProduct.contractImage.ImageID));
                    if(!newFolder.exists()){
                        dataChangeProduct.contractImage = null;
                    }
                }

				TSRController.saveChangeProduct(oldProductStock, newProductStock, dataChangeProduct.contract, dataChangeProduct.changeProduct, dataChangeProduct.assign, dataChangeProduct.contractImage, BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString()));
								
			}
			
			@Override
			protected void after() {
				ChangeProductPrintFragment.Data data1 = new ChangeProductPrintFragment.Data();
				data1.ChangeProductID = dataChangeProduct.changeProduct.ChangeProductID;
				ChangeProductPrintFragment fmPrint = BHFragment.newInstance(ChangeProductPrintFragment.class, data1);
				showNextView(fmPrint);
			}

		}).start();
	}


    private void previewCapturedImage() {
        try {
            imageViewChange.setVisibility(View.VISIBLE);

			String part = fileUri.getPath();
			Bitmap bm = BHBitmap.decodeSampledBitmapFromImagePath(part, 500);
			Bitmap bitmap = BHBitmap.setRotateImageFromImagePath(part, bm);

            imageViewChange.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }
}
