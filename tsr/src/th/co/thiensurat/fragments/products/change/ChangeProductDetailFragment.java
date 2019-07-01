package th.co.thiensurat.fragments.products.change;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHBitmap;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.business.controller.TSRController.DocumentGenType;
import th.co.thiensurat.data.controller.AssignController.AssignTaskType;
import th.co.thiensurat.data.controller.ChangeProductController;
import th.co.thiensurat.data.controller.ChangeProductController.ChangeProductStatus;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ChangeProductInfo;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.ProblemInfo.ProblemType;

public class ChangeProductDetailFragment extends BHFragment {

    private int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static String IMAGE_DIRECTORY_NAME = BHPreference.RefNo();
    private Uri fileUri;
    private static String Parth;
    private static String imageTypeCode;
    public static String imageID = "";
	public static Date changeDate = new Date();

    //public static String imgChangeProduct;

	public static final String CHANGE_PRODUCT_DETAIL = "th.co.thiensurat.fragments.products.change.changeproductdetail";
	ChangeProductInfo changeProductInfo;

	public static class Data extends BHParcelable {
		public String OldProductSerialNumber;
		public String OldProductID;
		public String OldProductName;
		public String NewProductSerialNumber;
		public String NewProductID;
		public String NewProductName;
	}

	@InjectView
	private TextView txtCode, txtProduct, txtNo, txtName, txtAddress, txtTel, txtSale, txtTeam, txtDataInstall;
	@InjectView
	private EditText etMore, etNewProduct, etDate, etName;
	@InjectView
	ImageButton imageButtonWriteoffDate;
	@InjectView
	private Spinner spnProblem;
	@InjectView
	private ImageView imageViewChange;

	private Data dataProduct;
	private List<ProblemInfo> problemList;
	private ProblemInfo selectedProblem = null;
	private int ProblemPosition = -1;
	ContractInfo contractInfo;

	String Date;

	Calendar myCal = Calendar.getInstance();

	private ChangeProductInfo changeProduct;
	private AssignInfo assignInfo;
	private AddressInfo addressIDCard;

	private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());
	private ChangeProductInfo changeProductInfoForCredit;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_change_product_detail;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_camera, R.string.button_next };
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_change_product;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (savedInstanceState != null) {
			changeProductInfo = savedInstanceState.getParcelable(CHANGE_PRODUCT_DETAIL);
		}
		dataProduct = getData();

		if(isCredit){
			changeProductInfoForCredit = new ChangeProductController().getChangeProductByRefNoByStatus(BHPreference.RefNo(), ChangeProductStatus.APPROVED.toString());
		}
		
		loadData();
		setWidgetsEventListener();

        BHStorage.FolderType F = BHStorage.FolderType.Picture;
        Parth = BHStorage.getFolder(F);
        imageTypeCode = ContractImageController.ImageType.CHANGEPRODUCT.toString();

        if(imageID.equals("") || imageID == null){
            imageViewChange.setVisibility(View.INVISIBLE);
        }else{
            File newFolder = new File(String.format("%s/%s/%s/%s.jpg", BHStorage.getFolder(BHStorage.FolderType.Picture), BHPreference.RefNo(), imageTypeCode, imageID));
            fileUri = Uri.fromFile(newFolder);
            previewCapturedImage();
        }


	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putParcelable(CHANGE_PRODUCT_DETAIL, (ChangeProductInfo) changeProductInfo);
		//savedInstanceState.putParcelable("file_uri", fileUri);
	}
	
	private void loadData() {
	(new BackgroundProcess(activity) {
				
				@Override
				protected void calling() {
					// TODO Auto-generated method stub			
					problemList = getProblemByProblemType(BHPreference.organizationCode(), ProblemType.ChangeProduct.toString());		

					/* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//					contractInfo = getContractBySerialNo(BHPreference.organizationCode(), dataProduct.OldProductSerialNumber, ContractStatus.NORMAL.toString());
					contractInfo = getContractBySerialNo(BHPreference.organizationCode(), dataProduct.OldProductSerialNumber, "");

					if (contractInfo != null) {
						addressIDCard = getAddress(contractInfo.RefNo, AddressType.AddressIDCard);
					}				
				}
				
				@Override
				protected void after() {
					// TODO Auto-generated method stub
					if (problemList !=null) {
						bindProblem();
					}
					
					if (changeProductInfo == null) {
						
						if (contractInfo !=null) {
							bindContract();
						}

						txtCode.setText(dataProduct.OldProductSerialNumber); // หมายเลขเครื่องเก่า
						txtProduct.setText(dataProduct.OldProductName); // ชื่อสินค้าเก่า
						etName.setText(BHPreference.saleCode() + " " + BHPreference.userFullName()); // ผู้เปลี่ยน
						etNewProduct.setText(dataProduct.NewProductSerialNumber); // หมายเลขเครื่องใหม่
						etDate.setText(BHUtilities.dateFormat(changeDate, "dd/MM/yyyy")); //วันที่เปลี่ยน
						etDate.setEnabled(false);
						etNewProduct.setEnabled(false);
						etName.setEnabled(false);

					} else {
						// 2nd up
						if (changeProductInfo.OldProductSerialNumber != null) // หมายเลขเครื่อง
							txtCode.setText(changeProductInfo.OldProductSerialNumber);

						if (changeProductInfo.ProductName != null) // ชื่อสินค้า
							txtProduct.setText(changeProductInfo.ProductName);

//						if (changeProductInfo.RefNo != null) // เลขที่สัญญา
//							txtNo.setText(changeProductInfo.RefNo);
						if(contractInfo.CONTNO != null) 
							txtNo.setText(contractInfo.CONTNO);

						if (changeProductInfo.CustomerName != null) // ชื่อผู้ซื้อ
							txtName.setText(changeProductInfo.CustomerName);

						if (changeProductInfo.Address != null) // ที่อยู่
							txtAddress.setText(changeProductInfo.Address);

						if (changeProductInfo.TelMobile != null)// เบอร์โทร
							txtTel.setText(changeProductInfo.TelMobile);

						if (changeProductInfo.SaleEmployeeName != null)// พนักงานขาย
							txtSale.setText(changeProductInfo.SaleEmployeeName);

						if (changeProductInfo.upperEmployeeName != null)// หัวหน้าทีม
							txtTeam.setText(changeProductInfo.upperEmployeeName);

						if (changeProductInfo.InstallDate != null)
							txtDataInstall.setText(new SimpleDateFormat("dd/MM/yyyy").format(changeProductInfo.InstallDate));

						if (changeProductInfo.RequestDetail != null) // เพิ่มเติม
							etMore.setText(changeProductInfo.ResultDetail);

						if (changeProductInfo.NewProductSerialNumber != null) { // หมายเลขเครื่องใหม่
							etNewProduct.setText(changeProductInfo.NewProductSerialNumber);
							etNewProduct.setEnabled(false);
						}

						if (changeProductInfo.RequestDate != null) { // วันที่เปลี่ยน
							etDate.setText(BHUtilities.dateFormat(changeProductInfo.RequestDate, "dd/MM/yyyy"));
							etDate.setEnabled(false);
						}

						if (changeProductInfo.empName != null) { // ชื้อผู้เปลี่ยน
							etName.setText(changeProductInfo.empName);
							etName.setEnabled(false);
						}
					}
								
				}			
			}).start();
	}
	
	private void bindProblem() {
		List<String> problem = new ArrayList<String>();
		for (ProblemInfo item : problemList) {
			problem.add(String.format(item.ProblemName));

			if(isCredit && changeProductInfoForCredit.RequestProblemID.equals(item.ProblemID) && ProblemPosition < 0 ){
				ProblemPosition = problem.size() - 1;
				etMore.setText(changeProductInfoForCredit.RequestDetail); //เพิ่มเติม
			}
		}

		BHSpinnerAdapter<String> arrayProblem = new BHSpinnerAdapter<String>(activity, problem);
		spnProblem.setAdapter(arrayProblem);
		
		if (changeProductInfo != null || isCredit) {
			spnProblem.setSelection(ProblemPosition);
		}


	}

	private void bindContract() {
		txtNo.setText(contractInfo.CONTNO); // เลขที่สัญญา
        txtName.setText(String.format("%s", BHUtilities.trim(contractInfo.CustomerFullName))); //ผู้ซื้อ

		if (addressIDCard != null) {
			txtAddress.setText(addressIDCard.Address());
			txtTel.setText(addressIDCard.Telephone());
		}

		txtSale.setText(contractInfo.SaleEmployeeName); // พนักงานขาย
		txtTeam.setText(contractInfo.upperEmployeeName); // หัวหน้าทีม
		txtDataInstall.setText(BHUtilities.dateFormat(contractInfo.InstallDate, "dd/MM/yyyy")); // วันที่ติดตั้ง
	}

	private void setWidgetsEventListener() {
		spnProblem.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				try {
					if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
						selectedProblem = problemList.get(position);
						ProblemPosition = position;
					} else {
						selectedProblem = null;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				selectedProblem = null;
			}
		});
		
		final DatePickerDialog.OnDateSetListener dpl = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				myCal.set(Calendar.YEAR, year);
				myCal.set(Calendar.MONTH, monthOfYear);
				myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				changeDate = myCal.getTime();
				etDate.setText(BHUtilities.dateFormat(myCal.getTime(), "dd/MM/yyyy"));
				//etDate.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + (year + 543));
			}
		};
		imageButtonWriteoffDate.setVisibility(View.GONE);
//		imageButtonWriteoffDate.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//
//				DatePickerDialog dpd = new DatePickerDialog(arg0.getContext(), dpl, myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), myCal
//						.get(Calendar.DAY_OF_MONTH));
//				dpd.show();
//			}
//		});
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_next:
			if (validateData() == true) {
				next();
			}
			break;

		case R.string.button_back:
			showLastView();
			break;

		case R.string.button_camera:
			captureImage();
			break;

		default:
			break;
		}
	}
	
	private boolean validateData() {
		boolean ret = true;				
		String title = "ChangeProduct";
		String message = "";
				
		if (selectedProblem == null) {
			//title = "";
			message = "กรุณาเลือกสาเหตุการเปลี่ยนเครื่อง";
			showNoticeDialogBox(title, message);
			ret = false;
		} else {
			if (selectedProblem.ProblemName.trim().equals("อื่น ๆ") && etMore.getText().toString().trim().equals("")) {
				//title = "";
				message = "กรณีเลือกสาเหตุการเปลี่ยน อื่นๆ กรุณากรอกที่ช่อง  เพิ่มเติม";
				showNoticeDialogBox(title, message);
				ret = false;
			} else if (etDate.getText().toString().trim().equals("")) {
				//title = "";
				message =  "กรุณาเลือกวันที่เปลี่ยน";
				showNoticeDialogBox(title, message);
				ret = false;
			}
		}
		
		return ret;
	}
	
	private void next() {
        String strChangeProductID;
		Date currentDate = new Date();

		changeProduct = new ChangeProductInfo();
		if(isCredit){
			strChangeProductID = changeProductInfoForCredit.ChangeProductID;
			changeProduct.OrganizationCode = changeProductInfoForCredit.OrganizationCode;
			changeProduct.RefNo = changeProductInfoForCredit.RefNo;
			changeProduct.RequestProblemID = changeProductInfoForCredit.RequestProblemID;
			changeProduct.RequestDetail = changeProductInfoForCredit.RequestDetail;
			changeProduct.RequestDate = changeProductInfoForCredit.RequestDate;
			changeProduct.RequestBy = changeProductInfoForCredit.RequestBy;
			changeProduct.RequestTeamCode = changeProductInfoForCredit.RequestTeamCode;
			changeProduct.ApprovedDate = changeProductInfoForCredit.ApprovedDate;
			changeProduct.ApproveDetail = changeProductInfoForCredit.ApproveDetail;
			changeProduct.ApprovedBy = changeProductInfoForCredit.ApprovedBy;
			changeProduct.ResultProblemID = selectedProblem.ProblemID;
			changeProduct.ResultDetail = etMore.getText().toString();
			changeProduct.EffectiveDate = changeDate;
			changeProduct.EffectiveBy = BHPreference.employeeID();
			changeProduct.CreateDate = changeProductInfoForCredit.CreateDate;
			changeProduct.CreateBy = changeProductInfoForCredit.CreateBy;
		} else { //sale
			strChangeProductID = DatabaseHelper.getUUID();
			changeProduct.OrganizationCode = BHPreference.organizationCode();
			changeProduct.RefNo = contractInfo.RefNo;
			changeProduct.RequestProblemID = selectedProblem.ProblemID;
			changeProduct.RequestDetail = etMore.getText().toString();
			changeProduct.RequestDate = changeDate;
			changeProduct.RequestBy = BHPreference.employeeID();
			changeProduct.RequestTeamCode = BHPreference.teamCode();
			changeProduct.ApprovedDate = changeProduct.RequestDate;
			changeProduct.ApproveDetail = changeProduct.RequestDetail;
			changeProduct.ApprovedBy = changeProduct.RequestBy;
			changeProduct.ResultProblemID = changeProduct.RequestProblemID;
			changeProduct.ResultDetail = changeProduct.RequestDetail;
			changeProduct.EffectiveDate = changeProduct.RequestDate;
			changeProduct.EffectiveBy = changeProduct.RequestBy;
			changeProduct.CreateDate = currentDate;
			changeProduct.CreateBy = BHPreference.employeeID();
		}

        changeProduct.ChangeProductID = strChangeProductID;
		changeProduct.OldProductSerialNumber = dataProduct.OldProductSerialNumber;
		changeProduct.NewProductSerialNumber = dataProduct.NewProductSerialNumber;
		changeProduct.Status = ChangeProductStatus.COMPLETED.toString();
		changeProduct.ChangeProductPaperID = TSRController.getAutoGenerateDocumentID(DocumentGenType.ChangeProduct, BHPreference.SubTeamCode(), BHPreference.saleCode());

		changeProduct.LastUpdateDate = currentDate;
		changeProduct.LastUpdateBy = BHPreference.employeeID();
		changeProduct.SyncedDate = currentDate;
		
		changeProduct.OldProductID = dataProduct.OldProductID;
		changeProduct.NewProductID = dataProduct.NewProductID;
		//changeProduct.empName = BHPreference.employeeID();
		changeProduct.empName = BHPreference.saleCode() + " " + BHPreference.userFullName();
		
		assignInfo = new AssignInfo();
		assignInfo.AssignID = DatabaseHelper.getUUID();
		assignInfo.TaskType = AssignTaskType.ChangeProduct.toString();
		assignInfo.OrganizationCode = BHPreference.organizationCode();
		assignInfo.RefNo = contractInfo.RefNo; 
		assignInfo.AssigneeEmpID = BHPreference.employeeID();
		assignInfo.AssigneeTeamCode = BHPreference.teamCode();
		assignInfo.Order = 0;
		assignInfo.OrderExpect = 0;
		assignInfo.ReferenceID = strChangeProductID;
		assignInfo.CreateBy = assignInfo.AssigneeEmpID;
		assignInfo.CreateDate = currentDate;		
		assignInfo.LastUpdateBy = assignInfo.AssigneeEmpID;	
		assignInfo.LastUpdateDate = currentDate;
		assignInfo.SyncedDate = currentDate;
				
		contractInfo.ProductSerialNumber = dataProduct.NewProductSerialNumber;
        contractInfo.ProductID = dataProduct.NewProductID;

		/*DocumentHistoryInfo docHist = new DocumentHistoryInfo();
		docHist.PrintHistoryID = DatabaseHelper.getUUID();
		docHist.OrganizationCode = BHPreference.organizationCode();
		docHist.DatePrint = currentDate;
		docHist.DocumentType = DocumentType.ChangeProduct.toString();
		docHist.DocumentNumber = strChangeProductID;
        docHist.Selected = false;
        docHist.Deleted = false;
        docHist.CreateBy = BHPreference.employeeID();
        docHist.CreateDate = currentDate;
        docHist.LastUpdateBy = BHPreference.employeeID();
        docHist.LastUpdateDate = currentDate;
        docHist.PrintOrder = 0;*/

        ContractImageInfo contractImageInfo = null;
        if(!imageID.equals("") && imageID != null){
            contractImageInfo = new ContractImageInfo();
            contractImageInfo.ImageID = imageID;
            contractImageInfo.RefNo = contractInfo.RefNo;
            contractImageInfo.ImageName = contractImageInfo.ImageID + ".jpg";
            contractImageInfo.ImageTypeCode = ContractImageController.ImageType.CHANGEPRODUCT.toString();
            contractImageInfo.SyncedDate = currentDate;
        }

		ChangeProductResultFragment.Data data = new ChangeProductResultFragment.Data();
		data.changeProduct = changeProduct;
		data.assign = assignInfo;
		//data.docHist = docHist;
		data.addressIDCard = addressIDCard;
		data.contract = contractInfo;
		data.causeProblem = spnProblem.getSelectedItem().toString();
        data.contractImage = contractImageInfo;

		ChangeProductResultFragment fmResult = BHFragment.newInstance(ChangeProductResultFragment.class, data);
		showNextView(fmResult);		
	}
	
	private void showNoticeDialogBox(final String title, final String message) {
		Builder setupAlert;
		setupAlert = new AlertDialog.Builder(activity);
		setupAlert.setTitle(title);
		setupAlert.setMessage(message);
		setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// ??
					}
				});
		setupAlert.show();
	}

	private void captureImage() {

		/*** [START] :: Permission ***/
		/*imageID = DatabaseHelper.getUUID();

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/

		new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

			@Override
			public void onSuccess(BHPermissions bhPermissions) {
				imageID = DatabaseHelper.getUUID();

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				Uri photoURI = FileProvider.getUriForFile(getActivity(),getActivity().getApplicationContext().getPackageName() + ".provider", new File(fileUri.getPath()));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
				startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Parth + "/" + BHPreference.RefNo() + "/" + imageTypeCode);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "" + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageID + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //Bitmap bmp = (Bitmap) data.getExtras().get("data");
                //previewCapturedImage(bmp);
                previewCapturedImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showMessage("User cancelled image capture");
            } else {
                showMessage("Sorry! Failed to capture image");
            }
        }
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
