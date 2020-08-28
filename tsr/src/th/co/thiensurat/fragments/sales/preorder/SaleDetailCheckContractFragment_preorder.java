package th.co.thiensurat.fragments.sales.preorder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DocumentHistoryController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.ManualDocumentController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatus;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.ManualDocumentInfo;
import th.co.thiensurat.data.info.PackagePeriodDetailInfo;
import th.co.thiensurat.data.info.ProductInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.document.manual.preorder.ManualDocumentDetailFragment_preorder;
import th.co.thiensurat.views.ViewTitle;


public class SaleDetailCheckContractFragment_preorder extends BHFragment {

    private ContractInfo contract;
    private DebtorCustomerInfo debtorCustomer;
    private ProductInfo productInfo;
    // private List<PackagePeriodDetailInfo> packagePreiodDetailInfo;
    // private int MODE;
    // private List<SalePaymentPeriodInfo> paymentPeriodOutput;
    private AddressInfo addressIDCard = null;
    private AddressInfo addressInstall = null;
    private PackagePeriodDetailInfo maxPackagePeriod = null;
    private FortnightInfo fortnight;

    @InjectView private TextView tvTitleContract;
    @InjectView private TextView textViewDateConteact;
    @InjectView private TextView textViewName;
    @InjectView private TextView textViewIDcard;
    @InjectView private TextView textViewAddress;
    @InjectView private TextView textViewInstall;
    @InjectView private TextView textViewPrice;
    @InjectView private TextView textViewDiscount;
    @InjectView private TextView textViewPriceSum;
    @InjectView private TextView textViewPriceFirstInstallment;
    @InjectView private TextView textViewPriceInstallment;
    @InjectView private TextView textViewProductSerialNumber;
    @InjectView private TextView textViewProduct;
    @InjectView private TextView textViewInstallment;
    @InjectView private TextView textViewContractNumber;
    @InjectView private LinearLayout linearLayoutFirstInstallment;
    @InjectView private LinearLayout linearLayoutInstallment;
    @InjectView private TextView textViewNextPaymentCurrency;
    @InjectView private TextView txtSaleEmployeeName;
    @InjectView private TextView txtSaleTeamName;
    @InjectView private LinearLayout linearLayoutHeadNumber;
    @InjectView private TextView txtNumber1;
    @InjectView private TextView txtNumber2;
    @InjectView private TextView txtNumber3;
    @InjectView private TextView txtNumber4;
    @InjectView private TextView txtNumber5;
    @InjectView private TextView textViewModel,txt_date_contno,txt_contno;
    @InjectView
    private TextView txtAddressInstallPhoneNumber, txtAddressIDCardPhoneNumber;
    @InjectView
    private LinearLayout llAddressInstallPhoneNumber, llAddressIDCardPhoneNumber;

    @InjectView
    private ViewTitle title;


    Calendar c = Calendar.getInstance();

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_sales_preorder;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_detail_check_contract;
    }

    @Override
    protected int[] processButtons() {
        //, R.string.button_confirm
        return new int[]{R.string.button_back, R.string.button_save_manual_contract_preorder};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        txtNumber1.setText("...");
        txtNumber2.setText("5");
        txtNumber3.setText("6");
        txtNumber4.setText("7");
        txtNumber5.setText("...");
        txtNumber4.setBackgroundResource(R.drawable.circle_number_sale_color_red);

        textViewDateConteact.setText(BHUtilities.dateFormat(c.getTime()));

        title.setText("รายละเอียดการจอง");
        txt_contno.setText("เลขที่ใบจอง");
        txt_date_contno.setText("วันที่ทำใบจอง");

        // Not save Status Code
        // if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
        // saveStatusCode();
        // }

        // Check เคยบันทึกสัญญามือแล้วหรือไม่
        List<ManualDocumentInfo> manualDocumentContract = new ManualDocumentController().getManualDocumentByDocumentNumber(BHPreference.RefNo());
        if (manualDocumentContract != null && manualDocumentContract.size() > 0) {
            fortnight = new TSRController().getCurrentFortnightInfo();
            //-- Fixed - [BHPROJ-0024-3070] :: [Android-บันทึกข้อมูลสัญญา] แก้ไขในส่วนของการบันทึกข้อมูลสัญญาใน Field ContractReferenceNo ให้ถูกต้อง
//            updateContractDB();
            updateContractDB(manualDocumentContract.get(0));
        } else {
            ContactDB();
        }
    }

    private void ContactDB() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            private List<SalePaymentPeriodInfo> paymentPeriodOutput = null;

            // AddressInfo addressOutput;
            // GetPackagePeriodByModelInputInfo input = null;
            // List<PackagePeriodDetailInfo> packagePeriodOutput = null;

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                contract = getContractByRefNo(BHPreference.organizationCode(), BHPreference.RefNo());

                // debtorCustomer = getDebtorCustomerInfo(contract.CustomerID);
                addressIDCard = getAddress(BHPreference.RefNo(), AddressType.AddressIDCard);
                addressInstall = getAddress(BHPreference.RefNo(), AddressType.AddressInstall);
                productInfo = getProductInfo(contract.OrganizationCode, contract.ProductID);
                // input = new GetPackagePeriodByModelInputInfo();
                // input.OrganizationCode = contract.OrganizationCode;
                // input.Model = contract.MODEL;
                // packagePeriodOutput =
                // getPackagePeriodDetail(input.OrganizationCode, input.Model);
                maxPackagePeriod = getMaxPackagePeriodDetailByModel(BHPreference.organizationCode(), contract.MODEL);
                paymentPeriodOutput = getSalePaymentPeriodByRefNo(BHPreference.RefNo());
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (contract != null) {

                    // tvTitleContract.setText(contract.MODEL.equals("SA01P") ||
                    // contract.MODEL.equals("PH01P") ? ""
                    // +
                    // getResources().getString(R.string.sale_contract_payment)
                    // : ""
                    // +
                    // getResources().getString(R.string.sale_contract_installment));

                    if (paymentPeriodOutput != null) {
                        String title = getResources().getString(R.string.sale_contract_preorder);
                        tvTitleContract.setText(title);
                    }

                    // textViewName.setText(contract.CustomerFullName);
                    textViewName.setText(String.format("%s%s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));

                    textViewIDcard.setText(contract.IDCard);
                    txtSaleEmployeeName.setText(String.format("%s %s", contract.SaleCode, contract.SaleEmployeeName));
                    txtSaleTeamName.setText(String.format("%s  %s", contract.SaleTeamCode, contract.upperEmployeeName));

                    // textViewContractNumber.setText(contract.CONTNO);
                    textViewContractNumber.setText("-");
                    textViewProductSerialNumber.setText(contract.ProductSerialNumber);
                    textViewPrice.setText(BHUtilities.numericFormat(contract.SALES));
                    textViewDiscount.setText(BHUtilities.numericFormat(contract.TradeInDiscount));
                    textViewPriceSum.setText(BHUtilities.numericFormat(contract.TotalPrice));

                    String strModel = (contract.MODEL == null) ? "" : contract.MODEL;
                    textViewModel.setText(strModel);

                    // if (CONTRACT.MODEL.equals("SA01P")) {
                    if (contract.MODE == 1) {
                        linearLayoutFirstInstallment.setVisibility(View.GONE);
                        linearLayoutInstallment.setVisibility(View.GONE);
                    }

                    textViewPriceFirstInstallment.setText(BHUtilities.numericFormat(contract.PaymentAmount - contract.TradeInDiscount));
                }

                /*if (addressIDCard != null) {
                    textViewAddress.setText(addressIDCard.Address());
                }

                if (addressInstall != null) {
                    textViewInstall.setText(addressInstall.Address());
                }*/

                if( addressIDCard != null && addressInstall != null){
                    textViewAddress.setText(BHUtilities.trim(addressIDCard.Address()));
                    textViewInstall.setText(BHUtilities.trim(addressInstall.Address()));

                    switch (contract.CustomerType) {
                        case "0":
                        case "2":
                            if(addressInstall.TelMobile.equals(addressIDCard.TelMobile)){
                                llAddressIDCardPhoneNumber.setVisibility(View.GONE);
                                txtAddressInstallPhoneNumber.setText(addressInstall.TelMobile);
                            } else {
                                txtAddressIDCardPhoneNumber.setText(addressIDCard.TelMobile);
                                txtAddressInstallPhoneNumber.setText(addressInstall.TelMobile);
                            }
                            break;
                        case "1":
                            if(addressInstall.TelMobile.equals(addressIDCard.TelMobile)){
                                llAddressIDCardPhoneNumber.setVisibility(View.GONE);
                                txtAddressInstallPhoneNumber.setText(addressInstall.TelHome);
                            } else {
                                txtAddressIDCardPhoneNumber.setText(addressIDCard.TelHome);
                                txtAddressInstallPhoneNumber.setText(addressInstall.TelHome);
                            }
                            break;
                    }
                }

                if (productInfo != null) {
                    textViewProduct.setText(productInfo.ProductName);
                }

                // No use
                // if (packagePeriodOutput != null) {
                // packagePreiodDetailInfo = packagePeriodOutput;
                // for (PackagePeriodDetailInfo item : packagePreiodDetailInfo)
                // {
                // textViewInstallment.setText("งวดที่ 2 ถึงงวดที่ " +
                // item.PaymentPeriodNumber + " ต้องชำระ");
                // MODE = item.PaymentPeriodNumber;
                // }
                // if (MODE == 1) {
                // textViewInstallment.setText("");
                // textViewNextPaymentCurrency.setText("");
                // }
                // }
                if (maxPackagePeriod != null) {
                    textViewInstallment.setText(String.format("งวดที่ 2 ถึงงวดที่ %d ต้องชำระงวดละ", maxPackagePeriod.PaymentPeriodNumber));

                    for (SalePaymentPeriodInfo item : paymentPeriodOutput) {
                        textViewPriceInstallment.setText(BHUtilities.numericFormat(item.PaymentAmount));
                    }

                    if (maxPackagePeriod.PaymentPeriodNumber == 1) {
                        linearLayoutInstallment.setVisibility(View.GONE);
                        // textViewInstallment.setText("");
                        // textViewNextPaymentCurrency.setText("");
                    }
                }


                // No use
                // if (paymentPeriodOutput != null) {
                // textViewPriceFirstInstallment.setText(BHUtilities.numericFormat(paymentPeriodOutput.get(0).NetAmount));//
                // SaleDetailCheckFragment.PAYMENT_FIRST));
                // paymentPeriodOutput.remove(0);
                // if (paymentPeriodOutput.size() > 1) {
                // float totalNextPeriodPayment = 0;
                // for (SalePaymentPeriodInfo pay : paymentPeriodOutput) {
                // totalNextPeriodPayment += pay.NetAmount;
                // }
                // textViewPriceInstallment.setText(BHUtilities.numericFormat(totalNextPeriodPayment));
                // // SaleDetailCheckFragment.PAYMENT_SECOND));
                // }
                // }
            }
        }).start();
    }


    // private void saveStatusCode() {
    // TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
    // }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
//            case R.string.button_confirm:
//                // Check เคยบันทึกสัญญามือแล้วหรือไม่
//                List<ManualDocumentInfo> manualDocument = new ManualDocumentController().getManualDocumentByDocumentNumber(contract.RefNo);
//                if (manualDocument != null && manualDocument.size() > 0) {
//                    final String title = "คำเตือน";
//                    final String message = "คุณต้องการบันทึกใบสัญญา?";
//                    showYesNoDialogBox(title, message);
//                } else {
//                    String title = "กรุณาตรวจสอบข้อมูล";
//                    String message = "กรุณาบันทึกใบสัญญามือ";
//                    showNoticeDialogBox(title, message);
//                }
//                break;
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_save_manual_contract_preorder:
                // Check Manual Document [Contract] ? EditMode or AddMode
//                String CONTNO = null;
//                if (contract.RefNo.equals(contract.CONTNO) && contract.STATUS.equals(ContractStatus.DRAFT.toString())) {
//                    CONTNO = TSRController.getAutoGenerateDocumentID(TSRController.DocumentGenType.Contract.toString(),
//                            BHPreference.SubTeamCode(), contract.SaleCode);
//                } else {
//                    CONTNO = contract.CONTNO;
//                }




                ManualDocumentDetailFragment_preorder.Data data1 = new ManualDocumentDetailFragment_preorder.Data();
                data1.DocumentNumber = contract.RefNo;
                //data1.DocumentNo = CONTNO;
                data1.DocumentType = DocumentHistoryController.DocumentType.Contract.toString();
                data1.processType = SaleFirstPaymentChoiceFragment_preorder.ProcessType.Sale;

                ManualDocumentDetailFragment_preorder fmManualDocContract = BHFragment.newInstance(ManualDocumentDetailFragment_preorder.class, data1);
                showNextView(fmManualDocContract);








//                SaleContractPrintFragment fmSaleContractPrint = BHFragment.newInstance(SaleContractPrintFragment.class);
//                showNextView(fmSaleContractPrint);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    //-- Fixed - [BHPROJ-0024-3070] :: [Android-บันทึกข้อมูลสัญญา] แก้ไขในส่วนของการบันทึกข้อมูลสัญญาใน Field ContractReferenceNo ให้ถูกต้อง
//    private void updateContractDB() {
    private void updateContractDB(final ManualDocumentInfo manualDoc) {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                if(contract == null){
                    contract = getContractByRefNo(BHPreference.organizationCode(), BHPreference.RefNo());
                }
                if(contract != null){

                    Log.e("page7",BHPreference.RefNo());
                    contract.RefNo = BHPreference.RefNo();
                    contract.StatusCode = "07";
                    contract.STATUS = ContractStatus.NORMAL.toString();
                    contract.EFFDATE = new Date();

                    /**
                     *
                     * Edit by Teerayut Klinsa ga
                     * 03/03/2022
                     *
                     */
                    try {
                        contract.FortnightID = fortnight.FortnightID;
                    } catch (NullPointerException ex) {
                        AlertDialog.Builder setupAlert;
                        setupAlert = new AlertDialog.Builder(activity)
                                .setTitle("พบข้อผิดพลาด")
                                .setMessage("ไม่พบข้อมูลปักษ์\n("+ ex.getLocalizedMessage() +")")
                                .setCancelable(false);

                        setupAlert = setupAlert.setPositiveButton("ดำเนินการต่อ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }).setNeutralButton("ยกเลิกการทำรายการนี้", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                showLastView();
                            }
                        });

                        setupAlert.show();
                    }
                    /**
                     *
                     * End
                     *
                     */

                    /*** [START] :: Fixed - [BHPROJ-0024-3070] :: [Android-บันทึกข้อมูลสัญญา] แก้ไขในส่วนของการบันทึกข้อมูลสัญญาใน Field ContractReferenceNo ให้ถูกต้อง ***/
                    //contract.ContractReferenceNo = String.valueOf(manualDoc.ManualRunningNo);

                    contract.ContractReferenceNo =BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo");
                    contract.CONTNO=BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo");
                    Log.e("page7_2",String.valueOf(manualDoc.ManualRunningNo));


                    /*** [END] :: Fixed - [BHPROJ-0024-3070] :: [Android-บันทึกข้อมูลสัญญา] แก้ไขในส่วนของการบันทึกข้อมูลสัญญาใน Field ContractReferenceNo ให้ถูกต้อง ***/
                    updateContract2(contract, true);

                    SalePaymentPeriodInfo period1 = new SalePaymentPeriodController().getSalePaymentPeriodInfoByRefNoAndPaymentPeriodNumber(contract.RefNo, 1);

                    AssignInfo assignInfo = new AssignInfo();
                    assignInfo.AssignID = DatabaseHelper.getUUID();
                    assignInfo.TaskType = AssignController.AssignTaskType.SalePaymentPeriod.toString();
                    assignInfo.OrganizationCode = contract.OrganizationCode;



                    assignInfo.RefNo = contract.RefNo;
                    Log.e("page7_3",contract.RefNo);


                    if (!contract.SaleEmployeeCode.equals(EmployeeController.PublicEmployeeID.PUBLIC_EMPLOYEE_ID.toString())) {
                        assignInfo.AssigneeEmpID = contract.SaleEmployeeCode;
                    } else {
                        EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                        assignInfo.AssigneeEmpID = saleLeader != null ? saleLeader.EmployeeCode : BHPreference.employeeID();
                    }

                    assignInfo.AssigneeTeamCode = contract.SaleTeamCode;
                    assignInfo.Order = 0;
                    assignInfo.OrderExpect = 0;
                    if (period1 != null) {
                        assignInfo.ReferenceID = period1.SalePaymentPeriodID;
                    }
                    assignInfo.CreateBy = BHPreference.employeeID();
                    assignInfo.CreateDate = new Date();
                    assignInfo.LastUpdateBy = BHPreference.employeeID();
                    assignInfo.LastUpdateDate = new Date();
                    assignInfo.CONTNO=BHApplication.getInstance().getPrefManager().getPreferrence("getContractReferenceNo");

                    TSRController.addAssign(assignInfo, true);
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                SaleContractPrintFragment_preorder fmSaleContractPrint = BHFragment.newInstance(SaleContractPrintFragment_preorder.class);
                showNextView(fmSaleContractPrint);
            }
        }).start();
    }

    /*
    private void showYesNoDialogBox(final String title, final String message) {
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                (new BackgroundProcess(activity) {
                    @Override
                    protected void calling() {
                        fortnight = getCurrentFortnightInfo();
                    }

                    @Override
                    protected void after() {
                        updateContractDB();
                    }
                }).start();
            }
        }).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // just do nothing
            }
        });
        setupAlert.show();
    }

    private void showNoticeDialogBox(final String title, final String message) {
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity);
        setupAlert.setTitle(title);
        setupAlert.setMessage(message);
        setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        setupAlert.show();
    }
    */

}