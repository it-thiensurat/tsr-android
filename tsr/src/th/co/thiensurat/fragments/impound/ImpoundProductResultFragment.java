package th.co.thiensurat.fragments.impound;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Date;
import java.util.List;

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
import th.co.thiensurat.data.controller.CutOffContractController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.controller.ProductStockController.ProductStockStatus;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.CutOffContractInfo;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.ImpoundProductInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetProductStockBySerialNoInputInfo;

public class ImpoundProductResultFragment extends BHFragment {

    private int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static int MEDIA_TYPE_IMAGE = 1;
    private static String IMAGE_DIRECTORY_NAME = "TSR_Impound_Product_Picture";

    private static String productSerialNumber;

    public static final String IMPOUND_PRODUCT_RESULT = "th.co.thiensurat.fragments.impound.impoundproductresult";
    public static final String ImpoundProblemPosition = "ImpoundProblemPosition";

    public static class Data extends BHParcelable {
        // public String Serialnumber, Name;
        public Boolean IsImpoundProductOtherTeam;
        // public String strImpoundProductID;

        public ImpoundProductInfo requestImpoundProduct;
        public ImpoundProductInfo approveImpoundProduct;
        public ImpoundProductInfo actionImpoundProduct;
        public AssignInfo assign;
        public DocumentHistoryInfo docHist;

        public AddressInfo addressIDCard;

        public ContractInfo contract;

        public Uri fileUri;
        public String pathDir;

        public String causeProblem;
        public String imageID;

    }

    @InjectView
    private TextView txtCode, txtProduct, txtNo, txtName, txtAddress, txtTel, txtSale, txtTeam, txtDataInstall, txtMoreProblem, txtChangeName, txtChangeDate,
            txtProblem;

    @InjectView
    private ImageView imageViewChange;

    private Data dataImpoundProduct;

    String Date;

    Calendar myCal = Calendar.getInstance();

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_impound_product_result;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back, R.string.button_save};
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_remove;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        dataImpoundProduct = getData();

        productSerialNumber = dataImpoundProduct.contract.ProductSerialNumber;

        txtCode.setText(dataImpoundProduct.contract.ProductSerialNumber);
        txtProduct.setText(dataImpoundProduct.contract.ProductName);


        txtNo.setText(dataImpoundProduct.contract.CONTNO);

        if (dataImpoundProduct.contract.CustomerFullName == null || (dataImpoundProduct.contract.CustomerFullName != null && dataImpoundProduct.contract.CustomerFullName.equals(""))) {
            txtName.setText(dataImpoundProduct.contract.CompanyName != null ? dataImpoundProduct.contract.CompanyName : "");
        } else {
            //txtName.setText(dataImpoundProduct.contract.CustomerFullName);
            txtName.setText(String.format("%s %s", BHUtilities.trim(dataImpoundProduct.contract.CustomerFullName), BHUtilities.trim(dataImpoundProduct.contract.CompanyName != null ? dataImpoundProduct.contract.CompanyName : "")));
        }

        txtSale.setText(dataImpoundProduct.contract.SaleEmployeeName);
        txtTeam.setText(dataImpoundProduct.contract.upperEmployeeName);
        //Date = new SimpleDateFormat("dd/MM/yy").format(dataImpoundProduct.contract.InstallDate);
        txtDataInstall.setText(BHUtilities.dateFormat(dataImpoundProduct.contract.InstallDate, "dd/MM/yyyy"));

        if (dataImpoundProduct.addressIDCard != null) {
            txtAddress.setText(dataImpoundProduct.addressIDCard.Address());
            txtTel.setText(dataImpoundProduct.addressIDCard.Telephone());
        }
        // if (dataImpoundProduct.addressIDCard.TelHome == null ||
        // dataImpoundProduct.addressIDCard.TelHome.equals(""))
        // txtTel.setText(output.TelMobile);
        // else {
        // if (output.TelMobile == null || output.TelMobile.equals(""))
        // txtTel.setText(output.TelHome);
        // else
        // txtTel.setText(output.TelHome + " , " + output.TelMobile);
        // }

        txtProblem.setText(dataImpoundProduct.causeProblem);

        if (dataImpoundProduct.actionImpoundProduct != null) {
            txtMoreProblem.setText(dataImpoundProduct.actionImpoundProduct.ResultDetail);
            EmployeeInfo emp = new EmployeeController().getEmployeeSaleCodeByEmployeeCode(dataImpoundProduct.actionImpoundProduct.EffectiveBy);
            if (emp != null) {
                txtChangeName.setText((emp.SaleCode != null ? emp.SaleCode : emp.EmpID) + " " + dataImpoundProduct.actionImpoundProduct.EffectiveName);
            } else {
                txtChangeName.setText(dataImpoundProduct.actionImpoundProduct.EffectiveBy + " " + dataImpoundProduct.actionImpoundProduct.EffectiveName);
            }
            txtChangeDate.setText(BHUtilities.dateFormat(dataImpoundProduct.actionImpoundProduct.EffectiveDate, "dd/MM/yyyy"));
        }
        // imageViewimpond.setVisibility(View.VISIBLE);
        if (dataImpoundProduct.fileUri != null) {
            previewCapturedImage();
        } else {
            imageViewChange.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_save:

                final String title = "คำเตือน";
                String message = "";
                Builder setupAlert;

                message = "ต้องการบันทึกข้อมูลการถอดเครื่องหรือไม่ ?";
                setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                if (dataImpoundProduct.IsImpoundProductOtherTeam == false) {
                                    (new BackgroundProcess(activity) {

                                        @Override
                                        protected void calling() {
                                            // TODO Auto-generated
                                            // method stub
                                            if (dataImpoundProduct.requestImpoundProduct != null) {
                                                dataImpoundProduct.requestImpoundProduct.RequestDate = myCal.getTime();
                                                dataImpoundProduct.requestImpoundProduct.LastUpdateBy = BHPreference.employeeID();
                                                dataImpoundProduct.requestImpoundProduct.CreateBy = BHPreference.employeeID();
                                                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                                                dataImpoundProduct.requestImpoundProduct.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                                                addRequestImpoundProduct(dataImpoundProduct.requestImpoundProduct);
                                            }

                                            if (dataImpoundProduct.approveImpoundProduct != null) {
                                                dataImpoundProduct.approveImpoundProduct.ApprovedDate = myCal.getTime();
                                                dataImpoundProduct.approveImpoundProduct.LastUpdateBy = BHPreference.employeeID();
                                                approveImpoundProduct(dataImpoundProduct.approveImpoundProduct, dataImpoundProduct.assign);
                                            }

                                            if (dataImpoundProduct.actionImpoundProduct != null) {
                                                dataImpoundProduct.actionImpoundProduct.ImpoundProductPaperID = getAutoGenerateDocumentID(
                                                        DocumentGenType.ImpoundProduct, BHPreference.SubTeamCode(), BHPreference.saleCode());

                                                dataImpoundProduct.actionImpoundProduct.LastUpdateBy = BHPreference.employeeID();
                                                dataImpoundProduct.actionImpoundProduct.EffectiveDate = myCal.getTime();
                                                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                                                dataImpoundProduct.actionImpoundProduct.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                                                dataImpoundProduct.actionImpoundProduct.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                                                actionImpoundProduct(dataImpoundProduct.actionImpoundProduct, true);

                                                // FIX [BHPROJ-0016-706]
                                                saveCutOffContract(dataImpoundProduct.contract.RefNo);
                                            }

                                            if (dataImpoundProduct.fileUri != null && dataImpoundProduct.imageID != null && BHPreference.RefNo() != null) {
                                                File newFolder = new File(String.format("%s/%s/%s/%s.jpg", BHStorage.getFolder(BHStorage.FolderType.Picture), BHPreference.RefNo()
                                                        , ContractImageController.ImageType.IMPOUNDPRODUCT.toString(), dataImpoundProduct.imageID));
                                                if (newFolder.exists()) {
                                                    AddContractImage();
                                                }
                                            }

                                            if (dataImpoundProduct.contract != null) {
                                                dataImpoundProduct.contract.STATUS = ContractInfo.ContractStatus.T.toString();
                                                dataImpoundProduct.contract.isActive = false;
                                                dataImpoundProduct.contract.LastUpdateDate = myCal.getTime();
                                                dataImpoundProduct.contract.LastUpdateBy = BHPreference.employeeID();

                                                //updateContract(dataImpoundProduct.contract, false);
                                                updateContract(dataImpoundProduct.contract, true);

                                                ProductStockInfo productStock = getProductStock(dataImpoundProduct.contract.ProductSerialNumber);
                                                if (productStock != null) {
                                                    productStock.Status = ProductStockStatus.RETURN.toString();
                                                    productStock.LastUpdateBy = BHPreference.employeeID();
                                                    updateProductStockStatus(productStock, true, true);
                                                } else {
                                                    // Import product from server and update to server later
                                                    GetProductStockBySerialNoInputInfo productStockInput = new GetProductStockBySerialNoInputInfo();
                                                    productStockInput.OrganizationCode = dataImpoundProduct.contract.OrganizationCode;
                                                    productStockInput.ProductSerialNumber = dataImpoundProduct.contract.ProductSerialNumber;
                                                    // get productStock from Server
                                                    productStock = TSRService.getProductStockBySerialNo(productStockInput, false).Info;

                                                    if (productStock != null && productStock.ProductSerialNumber != null) {
                                                        // import productStock from server -> LOCAL-DB
                                                        new ProductStockController().addProductStock(productStock);
                                                        // update productStock server && LOCAL-DB
                                                        productStock.Status = ProductStockStatus.RETURN.toString();
                                                        productStock.LastUpdateBy = BHPreference.employeeID();
                                                        updateProductStockStatus(productStock, true, true);
                                                    }
                                                }
                                            }

                                            if (dataImpoundProduct.docHist != null) {
                                                dataImpoundProduct.docHist.DatePrint = new Date();
                                                //addDocumentHistory(dataImpoundProduct.docHist, false);
                                                //addDocumentHistory(dataImpoundProduct.docHist, true);
                                            }
                                        }

                                        @Override
                                        protected void after() {
                                            ImpoundProductPrintFragment.Data data1 = new ImpoundProductPrintFragment.Data();

                                            // SimpleDateFormat sdf = new
                                            // SimpleDateFormat("yyyyMMdd");
                                            // String currentDateandTime =
                                            // sdf.format(Calendar.getInstance().getTime());
                                            if (dataImpoundProduct.actionImpoundProduct != null) {
                                                data1.ImpoundProductID = dataImpoundProduct.actionImpoundProduct.ImpoundProductID;
                                            } else {
                                                data1.ImpoundProductID = null;
                                            }
                                            // data1.ImpoundProductNo = "CHAN" +
                                            // currentDateandTime;
                                            // sdf = new SimpleDateFormat("dd/MM/yy");
                                            // currentDateandTime =
                                            // sdf.format(Calendar.getInstance().getTime());
                                            // data1.ImpoundProductDate = currentDateandTime;
                                            // data1.Product = txtProduct.getText().toString();
                                            // data1.SerialNo =
                                            // dataImpoundProduct.contract.ProductSerialNumber;
                                            // data1.ContractNo = txtNo.getText().toString();
                                            // data1.CustomerName =
                                            // txtName.getText().toString();
                                            // data1.Address = txtAddress.getText().toString();
                                            // data1.Tel = txtTel.getText().toString();
                                            // data1.InstallDate =
                                            // txtDataInstall.getText().toString();

                                            ImpoundProductPrintFragment fmProductCheck = BHFragment.newInstance(ImpoundProductPrintFragment.class, data1);
                                            showNextView(fmProductCheck);
                                        }

                                    }).start();
                                } else {
                                    (new BackgroundProcess(activity) {

                                        @Override
                                        protected void calling() {
                                            // TODO Auto-generated
                                            if (dataImpoundProduct.actionImpoundProduct != null) {
                                                dataImpoundProduct.actionImpoundProduct.ImpoundProductPaperID = getAutoGenerateDocumentID(
                                                        DocumentGenType.ImpoundProduct, BHPreference.SubTeamCode(), BHPreference.saleCode());

                                                dataImpoundProduct.actionImpoundProduct.EffectiveDate = myCal.getTime();
                                                dataImpoundProduct.actionImpoundProduct.LastUpdateBy = BHPreference.employeeID();
                                                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                                                dataImpoundProduct.actionImpoundProduct.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                                                TSRController.actionImpoundProduct(dataImpoundProduct.actionImpoundProduct, true);

                                                // FIX [BHPROJ-0016-706]
                                                saveCutOffContract(dataImpoundProduct.contract.RefNo);
                                            }

                                            if (dataImpoundProduct.fileUri != null && dataImpoundProduct.imageID != null) {
                                                AddContractImage();
                                            }

                                            if (dataImpoundProduct.contract != null) {
                                                dataImpoundProduct.contract.STATUS = ContractInfo.ContractStatus.T.toString();
                                                dataImpoundProduct.contract.isActive = false;
                                                dataImpoundProduct.contract.LastUpdateDate = myCal.getTime();
                                                dataImpoundProduct.contract.LastUpdateBy = BHPreference.employeeID();
                                                updateContract(dataImpoundProduct.contract, true);

                                                ProductStockInfo productStock = getProductStock(dataImpoundProduct.contract.ProductSerialNumber);
                                                if (productStock != null) {
                                                    productStock.Status = ProductStockStatus.RETURN.toString();
                                                    productStock.LastUpdateBy = BHPreference.employeeID();
                                                    updateProductStockStatus(productStock, true, true);
                                                }
                                            }

                                            if (dataImpoundProduct.docHist != null) {
                                                dataImpoundProduct.docHist.DatePrint = new Date();
                                                // addDocumentHistory(dataImpoundProduct.docHist, false);
                                            }

                                        }

                                        @Override
                                        protected void after() {
                                            ImpoundProductPrintFragment.Data data1 = new ImpoundProductPrintFragment.Data();

                                            // SimpleDateFormat sdf = new
                                            // SimpleDateFormat("yyyyMMdd");
                                            // String currentDateandTime =
                                            // sdf.format(Calendar.getInstance().getTime());
                                            if (dataImpoundProduct.actionImpoundProduct != null) {
                                                data1.ImpoundProductID = dataImpoundProduct.actionImpoundProduct.ImpoundProductID;
                                            } else {
                                                data1.ImpoundProductID = null;
                                            }
                                            // data1.ImpoundProductNo = "CHAN" +
                                            // currentDateandTime;
                                            // sdf = new SimpleDateFormat("dd/MM/yy");
                                            // currentDateandTime =
                                            // sdf.format(Calendar.getInstance().getTime());
                                            // data1.ImpoundProductDate = currentDateandTime;
                                            // data1.Product = txtProduct.getText().toString();
                                            // data1.SerialNo =
                                            // dataImpoundProduct.contract.ProductSerialNumber;
                                            // data1.ContractNo = txtNo.getText().toString();
                                            // data1.CustomerName =
                                            // txtName.getText().toString();
                                            // data1.Address = txtAddress.getText().toString();
                                            // data1.Tel = txtTel.getText().toString();
                                            // data1.InstallDate =
                                            // txtDataInstall.getText().toString();

                                            ImpoundProductPrintFragment fmProductCheck = BHFragment.newInstance(ImpoundProductPrintFragment.class, data1);
                                            showNextView(fmProductCheck);
                                        }
                                    }).start();
                                }


                            }
                        }).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // just do nothing
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

    public void saveCutOffContract(String RefNo)
    {
        Date newDate = new Date();
        CutOffContractInfo newCutOffContract;
        newCutOffContract = new CutOffContractInfo();
        newCutOffContract.CutOffContractID = DatabaseHelper.getUUID();
        newCutOffContract.OrganizationCode = BHPreference.organizationCode();
        newCutOffContract.RefNo = RefNo;
        newCutOffContract.Status = CutOffContractController.CutOffContractStatus.COMPLETED.toString();

        newCutOffContract.RequestProblemDetail = "ImpoundProduct";
        newCutOffContract.RequestDate = newDate;
        newCutOffContract.RequestBy = BHPreference.employeeID();
        newCutOffContract.RequestTeamCode = BHPreference.teamCode();
        newCutOffContract.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();

        newCutOffContract.IsAvailableContract = false;
        newCutOffContract.CreateDate = newDate;
        newCutOffContract.CreateBy = BHPreference.employeeID();
        newCutOffContract.LastUpdateDate = newDate;
        newCutOffContract.LastUpdateBy = BHPreference.employeeID();

        //Approved
        newCutOffContract.ApprovedDate = newDate;
        newCutOffContract.ApproveDetail = "ImpoundProduct";
        newCutOffContract.ApprovedBy = BHPreference.employeeID();

        //COMPLETED
        newCutOffContract.ResultProblemDetail = "ImpoundProduct";
        newCutOffContract.EffectiveDate = newDate;
        newCutOffContract.EffectiveBy = BHPreference.employeeID();
        newCutOffContract.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();

        TSRController.addCutOffContract(newCutOffContract, true);

        List<CutOffContractInfo> cutOffList = new CutOffContractController().GetCutOffContractByCutOffContractIDOfApproved("0", newCutOffContract.CutOffContractID);
        if (cutOffList != null && cutOffList.size() > 0)
        {
            for (CutOffContractInfo info : cutOffList)
            {
                TSRController.deleteAssign(info.AssignID, true);
            }
            newCutOffContract.LastAssigneeEmpID = cutOffList.get(0).LastAssigneeEmpID;
            newCutOffContract.LastAssigneeSaleCode = cutOffList.get(0).LastAssigneeSaleCode;
            newCutOffContract.LastAssigneeTeamCode = cutOffList.get(0).LastAssigneeTeamCode;
            newCutOffContract.LastAssignTaskType = cutOffList.get(0).LastAssignTaskType;
            TSRController.updateCutOffContract(newCutOffContract, true);
        }
    }
    // protected void onRestoreInstanceState(Bundle savedInstanceState) {
    // super.onSaveInstanceState(savedInstanceState);
    // fileUri = savedInstanceState.getParcelable("file_uri");
    // }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                previewCapturedImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showMessage("User cancelled image capture");
            } else {
                showMessage("Sorry! Failed to capture image");
            }
        }
    }

    private void AddContractImage() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            ContractImageInfo input = new ContractImageInfo();

            @Override
            protected void before() {
                // TODO Auto-generated method stub
                input.ImageID = dataImpoundProduct.imageID;
                input.RefNo = BHPreference.RefNo();
                input.ImageName = input.ImageID + ".jpg";
                input.ImageTypeCode = ContractImageController.ImageType.IMPOUNDPRODUCT.toString();
                input.SyncedDate = new Date();
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                addContractImage(input, true);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub

            }
        }).start();
    }

    private void previewCapturedImage() {
        try {
            imageViewChange.setVisibility(View.VISIBLE);

            String part = dataImpoundProduct.fileUri.getPath();
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

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(dataImpoundProduct.pathDir + IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "" + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
        // Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + productSerialNumber + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }
}
