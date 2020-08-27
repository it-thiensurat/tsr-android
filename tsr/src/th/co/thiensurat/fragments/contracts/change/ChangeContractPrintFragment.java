package th.co.thiensurat.fragments.contracts.change;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.PrinterController;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.TeamController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.TeamInfo;
import th.co.thiensurat.views.ViewTitle;

public class ChangeContractPrintFragment extends BHFragment {

    public static String FRAGMENT_CHANGE_CONTRACT_LIST_TAG = "change_contract_list_tag";

    private static final String TSR_COMMITTEE_NAME = "นายวิรัช  วงศ์นิรันดร์";
    private static final String LOGIN_SALELEADER_EMPNAME = "นายประยงค์  ทิพย์ชาติ";

    private final String LOGIN_TEAM_CODE = BHPreference.teamCode();// "PAK23";

    public static class Data extends BHParcelable {
        public String selectedCauseName;
        public ContractInfo newContract;
        public List<SalePaymentPeriodInfo> newSPPList;
        public AddressInfo newAddressIDCard;
        public AddressInfo newAddressInstall;
    }

    @InjectView
    private TextView txtEFFDate;
    @InjectView
    private TextView txtRef;
    @InjectView
    private TextView txtContNo;
    @InjectView
    private TextView txtCCDate;
    @InjectView
    private TextView txtCustomerFullName;
    @InjectView
    private TextView txtIDCard;
    @InjectView
    private TextView txtAddressIDCard;
    @InjectView
    private TextView txtAddressInstall;
    @InjectView
    private TextView txtProduct;
    @InjectView
    private TextView txtSerialNo;

    @InjectView
    private LinearLayout linearLayoutSaleAmount;
    @InjectView
    private TextView txtSaleAmount;

    @InjectView
    private LinearLayout linearLayoutSaleDiscount;
    @InjectView
    private TextView txtSaleDiscount;

    @InjectView
    private LinearLayout linearLayoutSaleNetAmount;
    @InjectView
    private TextView txtSaleNetAmount;

    @InjectView
    private LinearLayout linearLayoutPaymentNetAmount;
    @InjectView
    private TextView txtPaymentNetAmount;

    @InjectView
    private LinearLayout linearLayoutFirstPaymentAmount;
    @InjectView
    private TextView txtFirstPaymentAmount;

    @InjectView
    private LinearLayout linearLayoutTwoPaymentAmount;
    @InjectView
    private TextView txtTwoPaymentAmount;

    @InjectView
    private LinearLayout linearLayoutNextPaymentAmount;
    @InjectView
    private TextView txtNextPaymentAmountLabel;
    @InjectView
    private TextView txtNextPaymentAmountValue;

    @InjectView
    private TextView txtCCCause;

    //หลายงวด
    @InjectView
    private LinearLayout linearLayoutCredit;
    @InjectView
    private TextView txtTSRCommittee;
    @InjectView
    private TextView txtCustomerFullName2;
    @InjectView
    private TextView txtSaleLeaderName;
    @InjectView
    private TextView txtSaleEmpName;
    @InjectView
    private TextView txtSaleTeamName;
    @InjectView
    private TextView txtSaleEmpID, lblCustomerFullName;
    @InjectView
    private ViewTitle txtCaption;

    //1งวด
    @InjectView
    private LinearLayout linearLayoutCash;
    @InjectView
    private TextView txtCustomer;


    private Data dataContract;
    private ContractInfo contract = null;
    /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
    //private ManualDocumentInfo manual = null;
    /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
    private List<SalePaymentPeriodInfo> spp = null;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_change_contract_print;
    }

    @Override
    protected int titleID() {
        return R.string.title_change_contract;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_print, R.string.button_end};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        dataContract = getData();
        contract = dataContract.newContract;

        spp = dataContract.newSPPList;
        if (contract == null) {
            final String title = "กรุณาตรวจสอบสินค้า";
            String message = "ไม่พบข้อมูลสัญญาซื้อขาย!";
            Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            setupAlert.show();
            // showMessage("ไม่พบข้อมูลสัญญาซื้อขาย!");
        } else {
            bindContract();
        }
    }

    private void bindContract() {

        Log.e("aaaaa","23232");

        String titleCaption = contract.MODE > 1 ? "" + getResources().getString(R.string.sale_contract_payment) : ""
                + getResources().getString(R.string.sale_contract_installment);

        //String titleCaption = "ใบจอง";



        txtCaption.setText("aaaaa");
        String titleLblCustomerFullName = contract.MODE > 1 ? "" + getResources().getString(R.string.change_contract_customer_fullname) : ""
                + getResources().getString(R.string.change_contract_customer_fullname_cash);
        lblCustomerFullName.setText(titleLblCustomerFullName);

        /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
        /*manual = TSRController.getManualDocumentContractByDocumentNumber(contract.RefNo);
        if(manual != null){
            String ManualDocumentBookRunningNo = String.format("%4d", manual.ManualRunningNo).replace(' ', '0');
            txtRef.setText(ManualDocumentBookRunningNo);
        }else {
            txtRef.setText("");
        }*/
        txtRef.setText(contract.ContractReferenceNo != null ? contract.ContractReferenceNo : "");
        /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/

        txtEFFDate.setText(BHUtilities.dateFormat(contract.EFFDATE));
        txtContNo.setText(BHUtilities.trim(contract.CONTNO));
        txtCCDate.setText(BHUtilities.dateFormat(contract.EFFDATE));
        //txtCustomerFullName.setText(BHUtilities.trim(contract.CustomerFullName));
        txtCustomerFullName.setText(String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));

        txtIDCard.setText(BHUtilities.trim(contract.IDCard));

        if (dataContract.newAddressIDCard != null)
            txtAddressIDCard.setText(BHUtilities.trim(dataContract.newAddressIDCard.Address()));
        if (dataContract.newAddressInstall != null)
            txtAddressInstall.setText(BHUtilities.trim(dataContract.newAddressInstall.Address()));

        // bindAddressIDCard(contract.RefNo);
        // bindAddressInstall(contract.RefNo);

        txtProduct.setText(BHUtilities.trim(contract.ProductName));
        txtSerialNo.setText(BHUtilities.trim(contract.ProductSerialNumber));

        txtSaleAmount.setText(BHUtilities.numericFormat(contract.SALES));
        txtSaleDiscount.setText(BHUtilities.numericFormat(contract.TradeInDiscount));
        txtSaleNetAmount.setText(BHUtilities.numericFormat(contract.TotalPrice));

        txtPaymentNetAmount.setText(BHUtilities.numericFormat(contract.TotalPrice));

        if (contract.TradeInDiscount == 0) {
            linearLayoutSaleAmount.setVisibility(View.GONE);
            linearLayoutSaleDiscount.setVisibility(View.GONE);
        }

        if (spp != null) {

            if (spp.size() > 1) {
                linearLayoutPaymentNetAmount.setVisibility(View.GONE);

                txtFirstPaymentAmount.setText(BHUtilities.numericFormat(spp.get(0).NetAmount));

                if(spp.size() != 1) {
                    if (spp.size() == 2) {
                        linearLayoutNextPaymentAmount.setVisibility(View.GONE);
                        txtTwoPaymentAmount.setText(BHUtilities.numericFormat(spp.get(1).NetAmount));
                    } else {
                        if (spp.get(1).NetAmount == spp.get(2).NetAmount) {
                            linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
                            txtNextPaymentAmountLabel.setText("งวดที่ 2 ถึง " + BHUtilities.numericFormat(spp.size()) + " ต้องชำระงวดละ");
                            txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(spp.get(1).NetAmount));
                        } else {
                            if (spp.size() == 3) {
                                txtTwoPaymentAmount.setText(BHUtilities.numericFormat(spp.get(1).NetAmount));
                                txtNextPaymentAmountLabel.setText("งวดที่ 3 ที่ต้องชำระงวดละ");
                                txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(spp.get(2).NetAmount));
                            } else {
                                txtTwoPaymentAmount.setText(BHUtilities.numericFormat(spp.get(1).NetAmount));
                                txtNextPaymentAmountLabel.setText("งวดที่ 3 ถึง " + BHUtilities.numericFormat(spp.size()) + " ต้องชำระงวดละ");
                                txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(spp.get(2).NetAmount));
                            }
                        }
                    }
                } else {
                    linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
                    linearLayoutNextPaymentAmount.setVisibility(View.GONE);
                }
            } else {
                linearLayoutFirstPaymentAmount.setVisibility(View.GONE);
                linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
                linearLayoutNextPaymentAmount.setVisibility(View.GONE);
            }
        } else {
            linearLayoutFirstPaymentAmount.setVisibility(View.GONE);
            linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
            linearLayoutNextPaymentAmount.setVisibility(View.GONE);

            final String title = "กรุณาตรวจสอบสินค้า";
            String message = "ไม่พบข้อมูลงวดการชำระเงิน!";
            Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            setupAlert.show();
            // showMessage("ไม่พบข้อมูลงวดการชำระเงิน!");
        }

        txtCCCause.setText(BHUtilities.trim(dataContract.selectedCauseName));

        if(contract.MODE == 1){
            linearLayoutCash.setVisibility(View.VISIBLE);
            linearLayoutCredit.setVisibility(View.GONE);
            txtCustomer.setText(String.format("(%s%s)", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));

        }else {
            linearLayoutCredit.setVisibility(View.VISIBLE);
            linearLayoutCash.setVisibility(View.GONE);
            txtTSRCommittee.setText(BHUtilities.trim("(" + TSR_COMMITTEE_NAME + ")"));
            txtCustomerFullName2.setText(String.format("(%s%s)", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));

            List<ChangeContractInfo> changeContractList = TSRController.getChangeContractByNewSaleIDReturnChangeContract(BHPreference.organizationCode(), dataContract.newContract.RefNo);
            int i = 0;
            int selected = -1;
            if (changeContractList != null) {
                for (ChangeContractInfo changeContract : changeContractList) {
                    if (changeContract.EffectiveBySaleCode != null && !changeContract.EffectiveBySaleCode.isEmpty() && !changeContract.EffectiveBySaleCode.equals("")) {
                        selected = i;
                        break;
                    }
                    i++;
                }
                final ChangeContractInfo changeContract = changeContractList.get(selected > -1 ? selected : 0);
                txtSaleLeaderName
                        .setText(BHUtilities.trim("("
                                + ((changeContract.EffectiveByUpperEmployeeName == null || changeContract.EffectiveByUpperEmployeeName.isEmpty()) ? LOGIN_SALELEADER_EMPNAME : changeContract.EffectiveByUpperEmployeeName)
                                + ")"));
                txtSaleEmpName.setText(BHUtilities.trim("(" + changeContract.EffectiveByEmployeeName + ")"));
                txtSaleTeamName.setText(BHUtilities.trim(((changeContract.EffectiveBySaleTeamName == null || changeContract.EffectiveBySaleTeamName.isEmpty()) ? LOGIN_TEAM_CODE : changeContract.EffectiveBySaleTeamName)));
                txtSaleEmpID.setText(BHUtilities.trim("รหัส " + changeContract.EffectiveBySaleCode != null ? changeContract.EffectiveBySaleCode : changeContract.EffectiveBy));
            } else {
                EmployeeDetailInfo info = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                if (info != null) {
                    txtSaleLeaderName.setText(BHUtilities.trim("(" + info.EmployeeName + ")"));
                }
                txtSaleEmpName.setText(BHUtilities.trim("(" + BHPreference.userFullName() + ")"));
                TeamInfo teamInfo = new TeamController().getTeamByID(BHPreference.teamCode());
                if(teamInfo != null){
                    txtSaleTeamName.setText(BHUtilities.trim(teamInfo.Name));
                }
                txtSaleEmpID.setText(BHUtilities.trim("รหัส " + BHPreference.saleCode() != null || !BHPreference.saleCode().isEmpty() ? BHPreference.saleCode() : BHPreference.employeeID()));
            }
        }

        /*txtTSRCommittee.setText(BHUtilities.trim("(" + TSR_COMMITTEE_NAME + ")"));
        //txtCustomerFullName2.setText(BHUtilities.trim("(" + contract.CustomerFullName + ")"));
        txtCustomerFullName2.setText(String.format("%s %s", BHUtilities.trim(contract.CustomerFullName), BHUtilities.trim(contract.CompanyName)));
        List<ChangeContractInfo> changeContractList = TSRController.getChangeContractByNewSaleIDReturnChangeContract(BHPreference.organizationCode(), dataContract.newContract.RefNo);
        int i = 0;
        int selected = -1;
        if (changeContractList != null) {
            for (ChangeContractInfo changeContract : changeContractList) {
                if (changeContract.EffectiveBySaleCode != null && !changeContract.EffectiveBySaleCode.isEmpty() && !changeContract.EffectiveBySaleCode.equals("")) {
                    selected = i;
                    break;
                }
                i++;
            }
            final ChangeContractInfo changeContract = changeContractList.get(selected > -1 ? selected : 0);
            txtSaleLeaderName
                    .setText(BHUtilities.trim("("
                            + ((changeContract.EffectiveByUpperEmployeeName == null || changeContract.EffectiveByUpperEmployeeName.isEmpty()) ? LOGIN_SALELEADER_EMPNAME : changeContract.EffectiveByUpperEmployeeName)
                            + ")"));
            txtSaleEmpName.setText(BHUtilities.trim("(" + changeContract.EffectiveByEmployeeName + ")"));
            txtSaleTeamName.setText(BHUtilities.trim(((changeContract.EffectiveBySaleTeamName == null || changeContract.EffectiveBySaleTeamName.isEmpty()) ? LOGIN_TEAM_CODE : changeContract.EffectiveBySaleTeamName)));
            txtSaleEmpID.setText(BHUtilities.trim("รหัส " + changeContract.EffectiveBySaleCode != null ? changeContract.EffectiveBySaleCode : changeContract.EffectiveBy));
        } else {
            EmployeeDetailInfo info = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
            if (info != null) {
                txtSaleLeaderName.setText(BHUtilities.trim("(" + info.EmployeeName + ")"));
            }
            txtSaleEmpName.setText(BHUtilities.trim("(" + BHPreference.userFullName() + ")"));
            TeamInfo teamInfo = new TeamController().getTeamByID(BHPreference.teamCode());
            if(teamInfo != null){
                txtSaleTeamName.setText(BHUtilities.trim(teamInfo.Name));
            }
            txtSaleEmpID.setText(BHUtilities.trim("รหัส " + BHPreference.saleCode() != null || !BHPreference.saleCode().isEmpty() ? BHPreference.saleCode() : BHPreference.employeeID()));
        }*/
    }

    // private void bindAddressIDCard(final String mRefNo) {
    //
    // (new BackgroundProcess(activity) {
    //
    // AddressInfo output = null;
    //
    // @Override
    // protected void before() {
    // // TODO Auto-generated method stub
    // }
    //
    // @Override
    // protected void calling() {
    // // TODO Auto-generated method stub
    // output = getAddress(mRefNo, AddressType.AddressIDCard);
    // }
    //
    // @Override
    // protected void after() {
    // // TODO Auto-generated method stub
    // try {
    // String addressString = "";
    // if (output != null) {
    // addressString = output.Address();
    // }
    // txtAddressIDCard.setText(addressString);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }).start();
    // }
    //
    // private void bindAddressInstall(final String mRefNo) {
    //
    // (new BackgroundProcess(activity) {
    //
    // AddressInfo output = null;
    //
    // @Override
    // protected void before() {
    // // TODO Auto-generated method stub
    // }
    //
    // @Override
    // protected void calling() {
    // // TODO Auto-generated method stub
    // output = getAddress(mRefNo, AddressType.AddressInstall);
    // }
    //
    // @Override
    // protected void after() {
    // // TODO Auto-generated method stub
    // try {
    // String addressString = "";
    // if (output != null) {
    // addressString = output.Address();
    // }
    // txtAddressInstall.setText(addressString);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }).start();
    // }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_end:
                //showNextView(new ChangeContractListFragment());
                showLastView(FRAGMENT_CHANGE_CONTRACT_LIST_TAG);
                break;
            case R.string.button_print:
                printDocument();
                break;
            default:
                break;
        }

    }

    private void printDocument() {
        // TODO Auto-generated method stub
        new PrinterController(activity).printChangeContract(dataContract.newContract.RefNo);
    }

}
