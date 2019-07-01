package th.co.thiensurat.fragments.impound;

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
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.ImpoundProductInfo;
import th.co.thiensurat.fragments.document.manual.ManualDocumentDetailFragment;

import android.os.Bundle;
import android.widget.TextView;

public class ImpoundProductPrintFragment extends BHFragment {

    public static String FRAGMENT_IMPOUND_PRODUCT_LIST_TAG = "impound_product_list_tag";

    public static class Data extends BHParcelable {
        public String ImpoundProductID;//, ImpoundProductNo, ImpoundProductDate, Product, SerialNo, ContractNo, CustomerName, Address, Tel, InstallDate;
    }

    private Data dataContract;
    String Date;

    @InjectView
    private TextView txtNo, txtEffDate, txtProduct, txtSerialNo, txtContractNo, txtCustomerName, txtAddress, txtTel, txtInstallDate, txtCodeEmp, txtTeamCode;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_impound_product_print;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_print, R.string.button_end};
    }

    @Override
    protected int titleID() {
        return R.string.title_remove;
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {

            case R.string.button_end:
                showLastView(FRAGMENT_IMPOUND_PRODUCT_LIST_TAG);
                break;
            case R.string.button_save_manual_impound_product:
                ManualDocumentDetailFragment.Data data1 = new ManualDocumentDetailFragment.Data();

                data1.DocumentNo = txtNo.getText().toString();
                data1.DocumentType = DocumentType.ImpoundProduct.toString();

                ManualDocumentDetailFragment fmManualDocDetail = BHFragment.newInstance(ManualDocumentDetailFragment.class, data1);
                showNextView(fmManualDocDetail);
                break;
            case R.string.button_print:
                printDocument();
                break;
            default:
                break;
        }
    }

    private void printDocument() {
        new PrinterController(activity).printImpoundProduct(dataContract.ImpoundProductID);
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        dataContract = getData();
        getImpoundProductByID(dataContract.ImpoundProductID);

    }

    private void getImpoundProductByID(final String ImpoundProductID) {
        (new BackgroundProcess(activity) {

            ImpoundProductInfo info;
            String strOrganizationCode;
            //-- Fixed [BHPROJ-0026-978] :: Display detail of Sale
//            EmployeeInfo employeeInfo;
            AddressInfo output;

            @Override
            protected void before() {
                strOrganizationCode = BHPreference.organizationCode();
            }

            @Override
            protected void calling() {
                info = getImpoundProductByID(strOrganizationCode, ImpoundProductID);
                if(info != null) {
                    //-- Fixed [BHPROJ-0026-978] :: Display detail of Sale
//                    employeeInfo = getEmployeeDetailByEmployeeIDAndPositionCode(BHPreference.organizationCode(), info.SaleEmployeeCode, "Sale", info.SaleTeamCode);
                    output = getAddress(info.RefNo, AddressType.AddressIDCard);
                }
            }

            @Override
            protected void after() {
                if (info != null) {
                    txtNo.setText(info.ImpoundProductPaperID);

                    txtEffDate.setText(BHUtilities.dateFormat(info.EffectiveDate, "dd/MM/yyyy"));
                    txtProduct.setText(info.ProductName);
                    txtSerialNo.setText(info.ProductSerialNumber);
                    txtContractNo.setText(info.CONTNO);
                    //txtCustomerName.setText(info.CustomerFullName);
                    txtCustomerName.setText(String.format("%s %s", BHUtilities.trim(info.CustomerFullName), BHUtilities.trim(info.CompanyName)));
                    txtInstallDate.setText(BHUtilities.dateFormat(info.InstallDate, "dd/MM/yyyy"));

                    /*** [START] :: Fixed [BHPROJ-0026-978] :: Display detail of Sale ***/
                    txtCodeEmp.setText("รหัสพนักงาน  " + info.SaleCode + " : " + info.SaleEmployeeName);
                    txtTeamCode.setText("ทีม " + info.SaleTeamCode + " : " + info.TeamHeadName);
                    /*** [END] :: Fixed [BHPROJ-0026-978] :: Display detail of Sale ***/
                }

                /*** [START] :: Fixed [BHPROJ-0026-978] :: Display detail of Sale ***/
//                if (employeeInfo != null) {
//                    txtCodeEmp.setText("รหัสพนักงาน  " + employeeInfo.SaleCode + " : " + employeeInfo.SaleEmployeeName);
//                    txtTeamCode.setText("ทีม " + employeeInfo.TeamCode + " : " + employeeInfo.upperEmployeeName);
//                }
                /*** [END] :: Fixed [BHPROJ-0026-978] :: Display detail of Sale ***/

                if (output != null) {
                    txtAddress.setText(output.AddressDetail + ' ' + output.SubDistrictName + ' ' + output.DistrictName + ' ' + output.ProvinceName
                            + ' ' + output.Zipcode);
                    txtAddress.setText(output.Address());
                    if (output.TelHome == null || output.TelHome.equals(""))
                        txtTel.setText(output.TelMobile);
                    else {
                        if (output.TelMobile == null || output.TelMobile.equals(""))
                            txtTel.setText(output.TelHome);
                        else
                            txtTel.setText(output.TelHome + " , " + output.TelMobile);
                    }
                }
            }
        }).start();
    }
}
