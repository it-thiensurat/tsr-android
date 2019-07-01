package th.co.thiensurat.fragments.impound;

import java.io.File;
import java.io.IOException;
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
import th.co.bighead.utilities.BHStorage.FolderType;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.AssignController.AssignTaskType;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DocumentHistoryController.DocumentType;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.ImpoundProductController.ImpoundProductStatus;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatus;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.ImpoundProductInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.ProblemInfo.ProblemType;
import th.co.thiensurat.data.info.TeamInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetTeamByIDInputInfo;
import th.co.thiensurat.service.data.GetTeamByIDOutputInfo;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ImpoundProductDetailFragment extends BHFragment {

    private int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static int MEDIA_TYPE_IMAGE = 1;
    private static String IMAGE_DIRECTORY_NAME;
    private static String imageTypeCode;
    private static String imageID;
    private Uri fileUri;
    private static String Parth;
    private static String productSerialNumber;

    public static final String IMPOUND_PRODUCT_DETAIL = "th.co.thiensurat.fragments.impound.impoundproductdetail";
    public static final String ImpoundProblemPosition = "ImpoundProblemPosition";
    private int ProblemPosition; // สาเหตุการเปลี่ยน

    private ContractInfo contractInfo = null;
    private ImpoundProductInfo impoundProductInfo = null;

    private ImpoundProductInfo requestImpoundProduct;
    private ImpoundProductInfo approveImpoundProduct;
    private ImpoundProductInfo actionImpoundProduct;
    private AssignInfo assignInfo;
    private AddressInfo addressIDCard;

    public static class Data extends BHParcelable {
        public String Serialnumber, Name;
        public Boolean IsImpoundProductOtherTeam;
        public String strImpoundProductID;
    }

    @InjectView private TextView txtCode, txtProduct, txtNo, txtName, txtAddress, txtTel, txtSale, txtTeam, txtDataInstall, etName, etDate;
    @InjectView ImageButton imageButtonWriteoffDate;

    @InjectView private Spinner spnProblem; // สาเหตุการถอด
    @InjectView private TextView etMore; // หมายเหตุการถอด

    @InjectView private ImageView imageViewimpond;

    private Data dataProduct;
    private List<ProblemInfo> problemList;
    private ProblemInfo selectedProblem = null;
//    String Date;

    Calendar myCal = Calendar.getInstance();

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_impound_product_detail;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back, R.string.button_camera,
                R.string.button_next};
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_remove;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (savedInstanceState != null) {
            savedInstanceState.getParcelable(IMPOUND_PRODUCT_DETAIL);
            savedInstanceState.getInt(ImpoundProblemPosition);
        }

        etName.setText(BHPreference.saleCode() + " " + BHPreference.userFullName());

        if (impoundProductInfo == null) {
            impoundProductInfo = new ImpoundProductInfo();
        } else {
            if (impoundProductInfo.ResultDetail != null)
                etMore.setText(impoundProductInfo.ResultDetail);
            if (impoundProductInfo.EffectiveBy != null) {
                EmployeeInfo emp = new EmployeeController().getEmployeeSaleCodeByEmployeeCode(impoundProductInfo.EffectiveBy);
                if (emp != null) {
                    etName.setText((emp.SaleCode != null ? emp.SaleCode : emp.EmpID) + " " + emp.FullName());
                } else {
                    etName.setText(impoundProductInfo.EffectiveBy + " " + impoundProductInfo.EffectiveName);
                }
            }
            if (impoundProductInfo.EffectiveDate != null){
//                etDate.setText(new SimpleDateFormat("dd/MM/yyyy")
//                        .format(impoundProductInfo.EffectiveDate));
                etDate.setText(BHUtilities.dateFormat(impoundProductInfo.EffectiveDate, "dd/MM/yyyy"));
            }
        }

        dataProduct = getData();

        txtCode.setText(dataProduct.Serialnumber);
        txtProduct.setText(dataProduct.Name);
        productSerialNumber = dataProduct.Serialnumber;
        GetAndSetDataOther();
        DateControlInit();

        FolderType F = BHStorage.FolderType.Picture;
        Parth = BHStorage.getFolder(F);

        if (fileUri != null) {
            previewCapturedImage();
        } else {
            imageViewimpond.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(IMPOUND_PRODUCT_DETAIL, impoundProductInfo);
        savedInstanceState.putInt(ImpoundProblemPosition, ProblemPosition);
        savedInstanceState.putParcelable("file_uri", fileUri);

    }

    private void DateControlInit() {
        // TODO Auto-generated method stub
        final DatePickerDialog.OnDateSetListener dpl = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCal.set(Calendar.YEAR, year);
                myCal.set(Calendar.MONTH, monthOfYear);
                myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //etDate.setText("" + dayOfMonth + " / " + (monthOfYear + 1) + " / " + (year + 543));
                etDate.setText(BHUtilities.dateFormat(myCal.getTime(), "dd/MM/yyyy"));
            }
        };

        imageButtonWriteoffDate.setVisibility(View.GONE);
//        imageButtonWriteoffDate.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//
//                DatePickerDialog dpd = new DatePickerDialog(arg0.getContext(), dpl, myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), myCal
//                        .get(Calendar.DAY_OF_MONTH));
//                dpd.show();
//            }
//        });

//        if (dataProduct.IsImpoundProductOtherTeam == false) {
//            etDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCal.getTime()));
            etDate.setText(BHUtilities.dateFormat(myCal.getTime(), "dd/MM/yyyy"));
            etDate.setEnabled(false);
//            imageButtonWriteoffDate.setVisibility(View.INVISIBLE);
//        }
    }

    private void GetAndSetDataOther() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                problemList = getProblemByProblemType(BHPreference.organizationCode(), ProblemType.ImpoundProduct.toString());
                /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                contractInfo = getContractProductSerialByStatus(BHPreference.organizationCode(), txtCode.getText().toString(), ContractStatus.NORMAL.toString());
                contractInfo = getContractProductSerialByStatus(BHPreference.organizationCode(), txtCode.getText().toString());

                /* [START] :: Fixed - [BHPROJ-0026-941] :: Default DDL+MoreDetail of Request Data */
                if(dataProduct.strImpoundProductID != null){
                    impoundProductInfo = getImpoundProductByID(BHPreference.organizationCode(), dataProduct.strImpoundProductID);
                    log("Test Impound", dataProduct.strImpoundProductID);
                }
                /* [END] :: Fixed - [BHPROJ-0026-941] :: Default DDL+MoreDetail of Request Data */

                if (contractInfo != null) {
                    addressIDCard = getAddress(contractInfo.RefNo, AddressType.AddressIDCard);
                }
                boolean updateContract = false;
                // Import SaleEmployeeName
                if (contractInfo.SaleEmployeeName == null || contractInfo.SaleName == null || (contractInfo.SaleName != null && contractInfo.SaleName.trim().equals(""))) {
                    //Log.e("ImpoundProductDetailFragment", "Import SaleEmployeeName CODE::" + contractInfo.SaleEmployeeCode);
                    updateContract = true;
//                    TSRController.importEmployeeByEmployeeCode(contractInfo.OrganizationCode, contractInfo.SaleEmployeeCode, contractInfo.SaleTeamCode);
                }
                // Import SaleLeaderOfSaleTeamCode
                if (contractInfo.SaleLeaderNameOfSaleTeamCode == null && contractInfo.SaleTeamCode != null) {
                    GetTeamByIDInputInfo team = new GetTeamByIDInputInfo();
                    team.Code = contractInfo.SaleTeamCode;
                    TeamInfo output = TSRService.getTeamByID(team, false).Info;
                    if (output != null && output.TeamHeadCode != null) {
                        //Log.e("ImpoundProductDetailFragment", "Import SaleLeaderOfSaleTeamCode TEAM::"+ contractInfo.SaleTeamCode +", CODE::" + output.TeamHeadCode);
                        updateContract = true;
//                        TSRController.importEmployeeByEmployeeCode(contractInfo.OrganizationCode, output.TeamHeadCode, contractInfo.SaleTeamCode);
                    }
                }
                // Update Contract
                if (updateContract) {
                    // Log.e("ImpoundProductDetailFragment", "Update Contract");
                    /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                    contractInfo = getContractProductSerialByStatus(BHPreference.organizationCode(), txtCode.getText().toString(), ContractStatus.NORMAL.toString());
                    contractInfo = getContractProductSerialByStatus(BHPreference.organizationCode(), txtCode.getText().toString());
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (problemList != null) {
                    List<String> problem = new ArrayList<String>();
                    for (ProblemInfo item : problemList) {
                        problem.add(String.format(item.ProblemName));
                    }
                    BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(activity, problem);
                    spnProblem.setAdapter(dataAdapter);
                }

                /* [START] :: Fixed - [BHPROJ-0026-941] :: Default DDL+MoreDetail of Request Data */
                if (impoundProductInfo != null) {
//                    spnProblem.setSelection(ProblemPosition);
                    etMore.setText(impoundProductInfo.RequestDetail);
//                    ProblemInfo reqProb = new ProblemInfo();
//                    if (impoundProductInfo.RequestProblemID != null) {
//                        reqProb = getProblemByProblemID(impoundProductInfo.RequestProblemID);
//                    }
                    ProblemInfo reqProb = null;
                    for(ProblemInfo p : problemList){
                        if(p.ProblemID.equals(impoundProductInfo.RequestProblemID)){
                            reqProb = p;
                            break;
                        }
                    }
                    if (reqProb != null)
                    {
                        spnProblem.setSelection(problemList.indexOf(reqProb));
                    }
                    else
                    {
                        spnProblem.setSelection(ProblemPosition);
                    }
                }
                /* [END] :: Fixed - [BHPROJ-0026-941] :: Default DDL+MoreDetail of Request Data */

                if (contractInfo != null) {
                    txtNo.setText(contractInfo.CONTNO);
                    if (contractInfo.CustomerFullName == null || (contractInfo.CustomerFullName != null && contractInfo.CustomerFullName.equals(""))) {
                        txtName.setText(contractInfo.CompanyName != null ? contractInfo.CompanyName : "");
                    } else {
                        txtName.setText(String.format(
                                "%s %s",
                                BHUtilities.trim(contractInfo.CustomerFullName),
                                BHUtilities.trim(contractInfo.CompanyName != null ? contractInfo.CompanyName : "")));
                    }

                    if (addressIDCard != null) {
                        txtAddress.setText(addressIDCard.Address());
                        txtTel.setText(addressIDCard.Telephone());
                    }

                    if (contractInfo.SaleEmployeeName != null) {
                        txtSale.setText(contractInfo.SaleEmployeeName);
                    }

                    if (contractInfo.SaleLeaderNameOfSaleTeamCode != null) {
                        txtTeam.setText(contractInfo.SaleLeaderNameOfSaleTeamCode);
                    }

//                    Date = new SimpleDateFormat("dd/MM/yyyy")
//                            .format(contractInfo.InstallDate);
//                    txtDataInstall.setText(Date);
                    txtDataInstall.setText(BHUtilities.dateFormat(contractInfo.InstallDate, "dd/MM/yyyy"));
                }

            }
        }).start();

        // (1) Event listener of spinner
        spnProblem.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                try {
                    if (!parent.getItemAtPosition(position).toString().trim()
                            .equals("")) {
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
    }

//    private void getEmpByServer() {
//        new BackgroundProcess(activity) {
//            @Override
//            protected void calling() {
//                TSRController.importEmployeeByEmployeeCode(
//                        contractInfo.OrganizationCode, contractInfo.SaleEmployeeCode);
//                contractInfo = getContractProductSerialByStatus(
//                        BHPreference.organizationCode(), txtCode.getText()
//                                .toString(), ContractStatus.NORMAL.toString());
//            }
//
//            @Override
//            protected void after() {
//                txtSale.setText(contractInfo.SaleEmployeeName);
//                if (contractInfo.upperEmployeeName == null) {
//                    txtTeam.setText(contractInfo.SaleEmployeeName);
//                } else {
//                    txtTeam.setText(contractInfo.upperEmployeeName);
//                }
//
//            }
//        }.start();
//
//    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_next:

                final String title = "";
                String message = "";
                Builder setupAlert = null;

                if (spnProblem.getSelectedItemId() == spnProblem.getCount() - 1
                        && etMore.getText().toString().trim().equals("")) {
                    message = "กรุณากรอกสาเหตุการถอดเครื่องอื่น ๆ";
                    setupAlert = new AlertDialog.Builder(activity)
                            .setTitle(title)
                            .setMessage(message)
                            .setPositiveButton(getResources().getString(R.string.dialog_ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {

                                        }
                                    });
                    setupAlert.show();
                } else if (etDate.getText().toString().trim().equals("")) {
                    message = "กรุณาเลือกวันที่";
                    setupAlert = new AlertDialog.Builder(activity)
                            .setTitle(title)
                            .setMessage(message)
                            .setPositiveButton(getResources().getString(R.string.dialog_ok),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    });
                    setupAlert.show();
                } else {
                    DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                    impoundProductInfo = new ImpoundProductInfo();

                    if (dataProduct.IsImpoundProductOtherTeam == false)
                        impoundProductInfo.ImpoundProductID = DatabaseHelper.getUUID();
                    else
                        impoundProductInfo.ImpoundProductID = dataProduct.strImpoundProductID;

                    impoundProductInfo.OrganizationCode = contractInfo.OrganizationCode;
                    impoundProductInfo.RefNo = contractInfo.RefNo;
                    impoundProductInfo.Status = ImpoundProductStatus.REQUEST.toString();

                    impoundProductInfo.RequestTeamCode = BHPreference.teamCode();

                    impoundProductInfo.CreateBy = impoundProductInfo.RequestBy;
                    impoundProductInfo.LastUpdateBy = impoundProductInfo.RequestBy;

                    if (dataProduct.IsImpoundProductOtherTeam == false) {

                        impoundProductInfo.RequestProblemID = selectedProblem.ProblemID;// spnProblem.getSelectedItem().toString();
                        // if (spnProblem.getSelectedItemId() == spnProblem.getCount() - 1) {
                        impoundProductInfo.RequestDetail = etMore.getText().toString();
                        // }
                        impoundProductInfo.RequestDate = myCal.getTime();
                        impoundProductInfo.RequestBy = BHPreference.employeeID();//BHPreference.userID();
                        impoundProductInfo.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                        requestImpoundProduct = (ImpoundProductInfo) impoundProductInfo.copy();

                        impoundProductInfo.Status = ImpoundProductStatus.APPROVED.toString();
                        impoundProductInfo.ApproveDetail = impoundProductInfo.RequestDetail;
                        impoundProductInfo.ApprovedDate = myCal.getTime();
                        impoundProductInfo.ApprovedBy = impoundProductInfo.RequestBy;

                        impoundProductInfo.RequestBy = null;
                        impoundProductInfo.RequestDate = null;
                        impoundProductInfo.RequestDetail = null;
                        impoundProductInfo.RequestProblemID = null;

                        approveImpoundProduct = (ImpoundProductInfo) impoundProductInfo.copy();

                        assignInfo = new AssignInfo();
                        assignInfo.AssignID = DatabaseHelper.getUUID();
                        assignInfo.TaskType = AssignTaskType.ImpoundProduct.toString();
                        assignInfo.OrganizationCode = contractInfo.OrganizationCode;
                        assignInfo.RefNo = contractInfo.RefNo;
                        assignInfo.AssigneeEmpID = BHPreference.employeeID();//BHPreference.userID();
                        assignInfo.AssigneeTeamCode = BHPreference.teamCode();
                        assignInfo.ReferenceID = impoundProductInfo.ImpoundProductID;
                        assignInfo.CreateBy = assignInfo.AssigneeEmpID;
                        assignInfo.CreateDate = myCal.getTime();
                        assignInfo.LastUpdateBy = assignInfo.AssigneeEmpID;
                        assignInfo.LastUpdateDate = myCal.getTime();

                        impoundProductInfo.Status = ImpoundProductStatus.COMPLETED.toString();
//                        impoundProductInfo.ResultProblemID = selectedProblem.ProblemID;// spnProblem.getSelectedItem().toString();
//                        impoundProductInfo.ResultDetail = impoundProductInfo.ApproveDetail;

                        impoundProductInfo.ResultProblemID = selectedProblem.ProblemID;
                        // if (spnProblem.getSelectedItemId() == spnProblem.getCount() - 1) {
                        impoundProductInfo.ResultDetail = etMore.getText().toString();
                        // }

                        impoundProductInfo.EffectiveDate = myCal.getTime();
                        impoundProductInfo.EffectiveBy = impoundProductInfo.ApprovedBy;

                        impoundProductInfo.EffectiveName = BHPreference.userFullName();

                        impoundProductInfo.ApproveDetail = null;
                        impoundProductInfo.ApprovedDate = null;
                        impoundProductInfo.ApprovedBy = null;

                        actionImpoundProduct = (ImpoundProductInfo) impoundProductInfo.copy();

                        // (new BackgroundProcess(activity) {
                        //
                        // @Override
                        // protected void before() {
                        // // TODO Auto-generated
                        // // method stub
                        //
                        // }
                        //
                        // @Override
                        // protected void calling() {
                        // // TODO Auto-generated
                        // // method stub
                        //
                        // th.co.thiensurat.business.controller.TSRController.addRequestImpoundProduct(impoundProductInfo);
                        //
                        // th.co.thiensurat.business.controller.TSRController.approveImpoundProduct(impoundProductInfo,
                        // assignInfo);
                        //
                        // impoundProductInfo.ImpoundProductPaperID =
                        // getAutoGenerateDocumentID(
                        // DocumentGenType.ImpoundProduct.toString(),
                        // BHPreference.teamCode(),
                        // BHPreference.employeeID());//
                        // impoundProductInfo.ImpoundProductID;
                        //
                        // th.co.thiensurat.business.controller.TSRController.actionImpoundProduct(impoundProductInfo);
                        // }
                        //
                        // }).start();
                    } else {

                        impoundProductInfo.Status = ImpoundProductStatus.COMPLETED.toString();
                        impoundProductInfo.ResultProblemID = selectedProblem.ProblemID;
//                        if (spnProblem.getSelectedItemId() == spnProblem.getCount() - 1) {
                        impoundProductInfo.ResultDetail = etMore.getText().toString();
                        //       }
//                        impoundProductInfo.ResultProblemID = selectedProblem.ProblemID;// spnProblem.getSelectedItem().toString();
//                        impoundProductInfo.ResultDetail = etMore.getText()
//                                .toString();
                        impoundProductInfo.EffectiveDate = myCal.getTime();
                        impoundProductInfo.EffectiveBy = BHPreference.employeeID();//BHPreference.userID();
                        impoundProductInfo.EffectiveName = BHPreference.userFullName();
                        impoundProductInfo.ImpoundProductPaperID = impoundProductInfo.ImpoundProductID;

                        actionImpoundProduct = (ImpoundProductInfo) impoundProductInfo.copy();

                        // (new BackgroundProcess(activity) {
                        //
                        // @Override
                        // protected void before() {
                        // // TODO Auto-generated
                        // }
                        //
                        // @Override
                        // protected void calling() {
                        // // TODO Auto-generated
                        // // method stub
                        // th.co.thiensurat.business.controller.TSRController.actionImpoundProduct(impoundProductInfo);
                        // }
                        //
                        // }).start();
                    }

                    // ----------------------------------------------------------------
                    // INSERT table DocumentHistory
                    docHist.PrintHistoryID = DatabaseHelper.getUUID();
                    docHist.OrganizationCode = BHPreference.organizationCode();
                    docHist.DatePrint = new Date();
                    docHist.DocumentType = DocumentType.ImpoundProduct.toString();
                    docHist.DocumentNumber = impoundProductInfo.ImpoundProductID;

                    // (new BackgroundProcess(activity) {
                    //
                    // @Override
                    // protected void calling() {
                    // // TODO Auto-generated method stub
                    // insertDocumentHistory(docHist);
                    // }
                    //
                    // @Override
                    // protected void after() {
                    // }
                    // }).start();
                    // ----------------------------------------------------------------

                    // contractInfo.isActive = false;
                    // th.co.thiensurat.business.controller.TSRController
                    // LossMainFragment fmLossMain =
                    // BHFragment.newInstance(LossMainFragment.class);
                    // showNextView(fmLossMain);

                    ImpoundProductResultFragment.Data data1 = new ImpoundProductResultFragment.Data();

                    data1.IsImpoundProductOtherTeam = dataProduct.IsImpoundProductOtherTeam;
                    data1.requestImpoundProduct = requestImpoundProduct;
                    data1.approveImpoundProduct = approveImpoundProduct;
                    data1.actionImpoundProduct = actionImpoundProduct;
                    data1.assign = assignInfo;
                    data1.docHist = docHist;

                    data1.addressIDCard = addressIDCard;

                    data1.contract = contractInfo;

                    data1.fileUri = fileUri;
                    data1.pathDir = Parth;
                    if (imageID != null) {
                        data1.imageID = imageID;
                    }

                    data1.causeProblem = spnProblem.getSelectedItem().toString();

                    ImpoundProductResultFragment fmProductResult = BHFragment.newInstance(ImpoundProductResultFragment.class, data1);
                    if (dataProduct.IsImpoundProductOtherTeam == false) {
                        BHPreference.setRefNo(requestImpoundProduct.RefNo);
                    } else {
                        BHPreference.setRefNo(impoundProductInfo.RefNo);
                    }

                    showNextView(fmProductResult);
                    // }
                    // }).setNegativeButton("ยกเลิก", new
                    // DialogInterface.OnClickListener() {
                    // public void onClick(DialogInterface dialog, int whichButton)
                    // {
                    // // just do nothing
                    // }
                    // });
                }
                // setupAlert.show();

                break;

            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_camera:
//            FolderType F = BHStorage.FolderType.Picture;
//            Parth = BHStorage.getFolder(F);
                if (contractInfo != null && contractInfo.RefNo != null) {
                    IMAGE_DIRECTORY_NAME = contractInfo.RefNo;
                }

                imageTypeCode = ContractImageController.ImageType.IMPOUNDPRODUCT.toString();
                imageID = DatabaseHelper.getUUID();

                captureImage();
                break;

            default:
                break;
        }

    }

    private void captureImage() {

        /*** [START] :: Permission ***/
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/

        new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

            @Override
            public void onSuccess(BHPermissions bhPermissions) {
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

    //
    // @Override
    // public void onSaveInstanceState(Bundle outState) {
    // super.onSaveInstanceState(outState);
    // outState.putParcelable("file_uri", fileUri);
    // }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

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

    private void previewCapturedImage() {
        try {
            imageViewimpond.setVisibility(View.VISIBLE);

            String part = fileUri.getPath();
            Bitmap bm = BHBitmap.decodeSampledBitmapFromImagePath(part, 500);
            Bitmap bitmap = BHBitmap.setRotateImageFromImagePath(part, bm);

            imageViewimpond.setImageBitmap(bitmap);
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

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode);
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
}
