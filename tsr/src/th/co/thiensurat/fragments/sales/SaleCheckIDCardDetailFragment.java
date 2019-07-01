package th.co.thiensurat.fragments.sales;

import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.DistrictInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.PersonTypeCardInfo;
import th.co.thiensurat.data.info.ProvinceInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.SubDistrictInfo;

public class SaleCheckIDCardDetailFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String customerID, IDCard, RefNo;

        //region For ChangeContract
        // For ChangeContract
        public String selectedCauseName;
        public ChangeContractInfo chgContractRequest;
        public ChangeContractInfo chgContractApprove;
        public ChangeContractInfo chgContractAction;
        public AssignInfo assign;
        public static ContractInfo oldContract;
        public ContractInfo newContract;
        public List<SalePaymentPeriodInfo> newSPPList;
        public List<PaymentInfo> newPayment;

        public ContractImageInfo newContractImageInfo;
        //endregion
    }

    public Data data;

    private DebtorCustomerInfo debtorcustomerinfo;
    private ContractInfo contractInfo;
    private List<AddressInfo> addressInfo;
    private SubDistrictInfo subDistrictInfo;
    private DistrictInfo districtInfo;
    private ProvinceInfo provinceInfo;

    //บุคคลธรรมาดา
    final private String id_card = "บัตรประชาชน";
    final private String driving_license = "ใบขับขี่";
    final private String official_card = "บัตรข้าราชการ";

    //บุคคลต่างชาติ
    final private String passport = "หนังสือเดินทาง";
    final private String alien = "บัตรต่างด้าว";


    @InjectView
    TextView AddressData;
    @InjectView
    TextView Address;


    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.caption_customer_detail_1;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_check_idcard_detail;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_cancel, R.string.button_use_information};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        data = getData();
        Detail(data.customerID);


    }

    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_cancel:
                showLastView();
                break;
            case R.string.button_use_information:
                New2SaleCustomerAddressCardFragment.Data useInformation = new New2SaleCustomerAddressCardFragment.Data();
                ContractInfo contractInfo = TSRController.getContract(data.RefNo);
                useInformation.useAddressIDCard = TSRController.getAddress(data.RefNo, AddressInfo.AddressType.AddressIDCard);
                useInformation.useAddressInstall = TSRController.getAddress(data.RefNo, AddressInfo.AddressType.AddressInstall);
                useInformation.useAddressPayment = TSRController.getAddress(data.RefNo, AddressInfo.AddressType.AddressPayment);

                if(!contractInfo.CustomerID.equals("") && contractInfo.CustomerID != null){
                    useInformation.useDebtorCustomer = TSRController.getDebCustometByID(contractInfo.CustomerID);
                }

                if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.ChangeContract.toString())) {
                    useInformation.selectedCauseName = data.selectedCauseName;
                    useInformation.chgContractRequest = data.chgContractRequest;
                    useInformation.chgContractApprove = data.chgContractApprove;
                    useInformation.chgContractAction = data.chgContractAction;
                    useInformation.assign = data.assign;
                    useInformation.oldContract = data.oldContract;
                    useInformation.newContract = data.newContract;
                    useInformation.newSPPList = data.newSPPList;
                    useInformation.newPayment = data.newPayment;

                    useInformation.newDebtorCustomer = null;
                    useInformation.newAddressIDCard = null;
                    useInformation.newAddressInstall = null;
                    useInformation.newAddressPayment = null;

                    useInformation.newContractImageInfo = data.newContractImageInfo;
                }


                New2SaleCustomerAddressCardFragment fm = BHFragment.newInstance(New2SaleCustomerAddressCardFragment.class, useInformation);
                showNextView(fm);
                break;
            default:
                break;
        }
    }

    private void Detail(final String customerID) {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                debtorcustomerinfo = getDebCustometByID(customerID);
                addressInfo = getAddressByRefNo(data.RefNo);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                String strAddressData = "";
                String strAddress = "";
                if (addressInfo != null) {
                    if (debtorcustomerinfo.CustomerType.equals("0") || debtorcustomerinfo.CustomerType.equals("2")) {
                        //บุคคล
                        String strDay = BHUtilities.dateFormat(debtorcustomerinfo.Brithday, "dd");
                        String strMonth = BHUtilities.getMonthTH(BHUtilities.dateFormat(debtorcustomerinfo.Brithday, "MM"));
                        String strYear = BHUtilities.dateFormat(debtorcustomerinfo.Brithday, "yyyy");

                        DateFormat df = new SimpleDateFormat("yyyy");
                        Date date = new Date();
                        int age = Integer.parseInt(df.format(date)) - (Integer.parseInt(strYear) - 543);

                        if (debtorcustomerinfo.CustomerType.equals("0")) {
                            if (debtorcustomerinfo.IDCardType.equals(PersonTypeCardInfo.PersonTypeCardEnum.IDCARD.toString())) {
                                strAddressData += "ประเภทบัตร : " + id_card + "\n";
                                strAddressData += "บัตรประชาชน : " + debtorcustomerinfo.IDCard + "\n";
                            } else if (debtorcustomerinfo.IDCardType.equals(PersonTypeCardInfo.PersonTypeCardEnum.DRIVINGLICENSE.toString())) {
                                strAddressData += "ประเภทบัตร : " + driving_license + "\n";
                                strAddressData += "ใบขับขี่ : " + debtorcustomerinfo.IDCard + "\n";
                            } else if (debtorcustomerinfo.IDCardType.equals(PersonTypeCardInfo.PersonTypeCardEnum.OFFICIALCARD.toString())) {
                                strAddressData += "ประเภทบัตร : " + official_card + "\n";
                                strAddressData += "บัตรข้าราชการ : " + debtorcustomerinfo.IDCard + "\n";
                            }
                        } else if (debtorcustomerinfo.CustomerType.equals("2")) {
                            if (debtorcustomerinfo.IDCardType.equals(PersonTypeCardInfo.PersonTypeCardEnum.PASSPORT.toString())) {
                                strAddressData += "ประเภทบัตร : " + passport + "\n";
                            } else if (debtorcustomerinfo.IDCardType.equals(PersonTypeCardInfo.PersonTypeCardEnum.OUTLANDER.toString())) {
                                strAddressData += "ประเภทบัตร : " + alien + "\n";
                            }
                            strAddressData += "เลขที่บัตร : " + debtorcustomerinfo.IDCard + "\n";
                        }

                        strAddressData += "ชื่อ : " + debtorcustomerinfo.PrefixName + "" + debtorcustomerinfo.CustomerName + "\n";
                        strAddressData += "เกิดวันที่ : " + strDay + " " + strMonth + " " + strYear + "\n";
                        strAddressData += "เพศ : " + debtorcustomerinfo.Sex + "\n";
                        strAddressData += "อายุ : " + age + "\n";

                        for (AddressInfo item : addressInfo) {
                            if (item.AddressTypeCode.equals(AddressInfo.AddressType.AddressIDCard.toString())) {
                                subDistrictInfo = getSubDistrictBySubDistrictCode(item.SubDistrictCode);
                                districtInfo = getDistrictByDistrictCode(item.DistrictCode);
                                provinceInfo = getProvinceByProvinceCode(item.ProvinceCode);


                                if (debtorcustomerinfo.CustomerType.equals("0")) {
                                    strAddress += "ที่อยู่ตามบัตรประชาชน\n";
                                } else if (debtorcustomerinfo.CustomerType.equals("2")) {
                                    strAddress += "ที่อยู่ตามบัตร\n";
                                }

                                strAddress += "บ้านเลขที่ " + item.AddressDetail + " หมู่ที่ " + item.AddressDetail2 +
                                        " ซอย/ตรอก " + item.AddressDetail3 + " ถนน " + item.AddressDetail4 +
                                        " ตำบล/แขวง " + subDistrictInfo.SubDistrictName + " อำเภอ/เขต " + districtInfo.DistrictName +
                                        " จังหวัด " + provinceInfo.ProvinceName + " " + item.Zipcode + "\n";
                                strAddress += "เบอร์บ้าน : " + item.TelHome + "\n";
                                strAddress += "เบอร์ที่ทำงาน : " + item.TelOffice + "\n";
                                strAddress += "เบอร์มือถือ : " + item.TelMobile + "\n";
                                strAddress += "อีเมล์ : " + item.EMail + "\n\n";

                            } else if (item.AddressTypeCode.equals(AddressInfo.AddressType.AddressPayment.toString())) {
                                subDistrictInfo = getSubDistrictBySubDistrictCode(item.SubDistrictCode);
                                districtInfo = getDistrictByDistrictCode(item.DistrictCode);
                                provinceInfo = getProvinceByProvinceCode(item.ProvinceCode);


                                strAddress += "ที่อยู่เก็บเงิน\n";
                                strAddress += "บ้านเลขที่ " + item.AddressDetail + " หมู่ที่ " + item.AddressDetail2 +
                                        " ซอย/ตรอก " + item.AddressDetail3 + " ถนน " + item.AddressDetail4 +
                                        " ตำบล/แขวง " + subDistrictInfo.SubDistrictName + " อำเภอ/เขต " + districtInfo.DistrictName +
                                        " จังหวัด " + provinceInfo.ProvinceName + " " + item.Zipcode + "\n";
                                strAddress += "เบอร์บ้าน : " + item.TelHome + "\n";
                                strAddress += "เบอร์ที่ทำงาน : " + item.TelOffice + "\n";
                                strAddress += "เบอร์มือถือ : " + item.TelMobile + "\n";
                                strAddress += "อีเมล์ : " + item.EMail + "\n\n";

                            } else if (item.AddressTypeCode.equals(AddressInfo.AddressType.AddressInstall.toString())) {
                                subDistrictInfo = getSubDistrictBySubDistrictCode(item.SubDistrictCode);
                                districtInfo = getDistrictByDistrictCode(item.DistrictCode);
                                provinceInfo = getProvinceByProvinceCode(item.ProvinceCode);


                                strAddress += "ที่อยู่ติดตั้ง\n";
                                strAddress += "บ้านเลขที่ " + item.AddressDetail + " หมู่ที่ " + item.AddressDetail2 +
                                        " ซอย/ตรอก " + item.AddressDetail3 + " ถนน " + item.AddressDetail4 +
                                        " ตำบล/แขวง " + subDistrictInfo.SubDistrictName + " อำเภอ/เขต " + districtInfo.DistrictName +
                                        " จังหวัด " + provinceInfo.ProvinceName + " " + item.Zipcode + "\n";
                                strAddress += "เบอร์บ้าน : " + item.TelHome + "\n";
                                strAddress += "เบอร์ที่ทำงาน : " + item.TelOffice + "\n";
                                strAddress += "เบอร์มือถือ : " + item.TelMobile + "\n";
                                strAddress += "อีเมล์ : " + item.EMail + "\n\n";
                            }
                        }

                        if (!strAddressData.equals("") && !strAddress.equals("")) {
                            AddressData.setText(strAddressData);
                            Address.setText(strAddress);
                        }

                    } else if (debtorcustomerinfo.CustomerType.equals("1")) {
                        //นิติบุคคล
                        strAddressData += "เลขประจำตัวผู้เสียภาษี : " + debtorcustomerinfo.IDCard + "\n";
                        strAddressData += debtorcustomerinfo.PrefixName + " " + debtorcustomerinfo.CompanyName + "\n";
                        strAddressData += "กรรมการผู้มีอำนาจ : " + debtorcustomerinfo.AuthorizedName + "\n";
                        strAddressData += "เลขที่บัตร : " + debtorcustomerinfo.AuthorizedIDCard + "\n";

                        for (AddressInfo item : addressInfo) {
                            if (item.AddressTypeCode.equals(AddressInfo.AddressType.AddressIDCard.toString())) {
                                subDistrictInfo = getSubDistrictBySubDistrictCode(item.SubDistrictCode);
                                districtInfo = getDistrictByDistrictCode(item.DistrictCode);
                                provinceInfo = getProvinceByProvinceCode(item.ProvinceCode);

                                strAddress += "ที่ตั้งบริษัท\n";
                                strAddress += "บ้านเลขที่ " + item.AddressDetail + " หมู่ที่ " + item.AddressDetail2 +
                                        " ซอย/ตรอก " + item.AddressDetail3 + " ถนน " + item.AddressDetail4 +
                                        " ตำบล/แขวง " + subDistrictInfo.SubDistrictName + " อำเภอ/เขต " + districtInfo.DistrictName +
                                        " จังหวัด " + provinceInfo.ProvinceName + " " + item.Zipcode + "\n";
                                strAddress += "เบอร์โทรศัพท์1 : " + item.TelHome + "\n";
                                strAddress += "เบอร์โทรศัพท์2 : " + item.TelOffice + "\n";
                                strAddress += "เบอร์แฟกซ์ : " + item.TelMobile + "\n";
                                strAddress += "อีเมล์ : " + item.EMail + "\n\n";

                            } else if (item.AddressTypeCode.equals(AddressInfo.AddressType.AddressPayment.toString())) {
                                subDistrictInfo = getSubDistrictBySubDistrictCode(item.SubDistrictCode);
                                districtInfo = getDistrictByDistrictCode(item.DistrictCode);
                                provinceInfo = getProvinceByProvinceCode(item.ProvinceCode);

                                strAddress += "ที่อยู่เก็บเงิน\n";
                                strAddress += "บ้านเลขที่ " + item.AddressDetail + " หมู่ที่ " + item.AddressDetail2 +
                                        " ซอย/ตรอก " + item.AddressDetail3 + " ถนน " + item.AddressDetail4 +
                                        " ตำบล/แขวง " + subDistrictInfo.SubDistrictName + " อำเภอ/เขต " + districtInfo.DistrictName +
                                        " จังหวัด " + provinceInfo.ProvinceName + " " + item.Zipcode + "\n";
                                strAddress += "เบอร์โทรศัพท์1 : " + item.TelHome + "\n";
                                strAddress += "เบอร์โทรศัพท์2 : " + item.TelOffice + "\n";
                                strAddress += "เบอร์แฟกซ์ : " + item.TelMobile + "\n";
                                strAddress += "อีเมล์ : " + item.EMail + "\n\n";

                            } else if (item.AddressTypeCode.equals(AddressInfo.AddressType.AddressInstall.toString())) {
                                subDistrictInfo = getSubDistrictBySubDistrictCode(item.SubDistrictCode);
                                districtInfo = getDistrictByDistrictCode(item.DistrictCode);
                                provinceInfo = getProvinceByProvinceCode(item.ProvinceCode);

                                strAddress += "ที่อยู่ติดตั้ง\n";
                                strAddress += "บ้านเลขที่ " + item.AddressDetail + " หมู่ที่ " + item.AddressDetail2 +
                                        " ซอย/ตรอก " + item.AddressDetail3 + " ถนน " + item.AddressDetail4 +
                                        " ตำบล/แขวง " + subDistrictInfo.SubDistrictName + " อำเภอ/เขต " + districtInfo.DistrictName +
                                        " จังหวัด " + provinceInfo.ProvinceName + " " + item.Zipcode + "\n";
                                strAddress += "เบอร์โทรศัพท์1 : " + item.TelHome + "\n";
                                strAddress += "เบอร์โทรศัพท์2 : " + item.TelOffice + "\n";
                                strAddress += "เบอร์แฟกซ์ : " + item.TelMobile + "\n";
                                strAddress += "อีเมล์ : " + item.EMail + "\n\n";
                            }
                        }

                        if (strAddressData != null && strAddress != null) {
                            AddressData.setText(strAddressData);
                            Address.setText(strAddress);
                        }

                    }/*else if(debtorcustomerinfo.CustomerType.equals("2")){

                    }*/
                } else {
                    showMessage("ไม่พบประวัติการซื้อ");
                }
                //showMessage(debtorcustomerinfo.CustomerType);
            }

        }).start();
    }
}

