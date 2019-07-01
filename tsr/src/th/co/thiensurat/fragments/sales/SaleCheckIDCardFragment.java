package th.co.thiensurat.fragments.sales;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetDebtorCustomerInputInfo;
import th.co.thiensurat.service.data.GetDebtorCustomerOutputInfo;

public class SaleCheckIDCardFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String idCard, personType, cardTypeName, personTypeCard;

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

        public DebtorCustomerInfo tmpDebtorCustomer;
        public AddressInfo tmpAddress;

        public AddressInfo addressInstall;
        public AddressInfo addressPayment;
    }

    public Data data;

    public static String logPrefixName = "";//คำนำหน้าชื่อ
    public static String logProvince = "";//จังหวัด
    public static String logDistrict = "";//อำเภอ
    public static String logParish = "";//ตำบล
    public static String logSpinnerTypeCard = "";//ประเภทบัตร

    private DebtorCustomerInfo debtorCustomerInfo;

    private List<DebtorCustomerInfo> debtorcustomerinfo;
    public String customerid;

    @InjectView
    TextView textviewCardType;
    @InjectView
    TextView textviewStatus;
    @InjectView
    EditText editTextIdentificationCard;
    @InjectView
    ListView listViewShow;

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_check_idcard;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        data = getData();

        String IDCard = data.idCard;
        textviewCardType.setText(data.cardTypeName);
        editTextIdentificationCard.setText(IDCard);
        CheckIDCard(IDCard, data.personType, data.personTypeCard);
    }

    private void CheckIDCard(final String iDCard, final String personType, final String personTypeCard) {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                debtorcustomerinfo = getidcardDebcustomer(iDCard, personType, personTypeCard);
                if (debtorcustomerinfo == null || (debtorcustomerinfo != null && debtorcustomerinfo.size() == 0)) {
                    // Search From Server
                    GetDebtorCustomerInputInfo input = new GetDebtorCustomerInputInfo();
                    input.IDCard = iDCard;
                    input.CustomerType = personType;
                    input.IDCardType = personTypeCard;

                    GetDebtorCustomerOutputInfo output = TSRService.getDebtorCustomerByIDCard(input, false);
                    if (output != null && output.ResultCode == 0) {
                        List<DebtorCustomerInfo> customerList = output.Info;
                        if (customerList != null && customerList.size() > 0) {
                            if (debtorcustomerinfo == null) {
                                debtorcustomerinfo = new ArrayList<DebtorCustomerInfo>();
                            } else {
                                debtorcustomerinfo.clear();
                            }
                            for(DebtorCustomerInfo dc : customerList){
                                importContractFromServer(dc.OrganizationCode, null, dc.RefNo);
                                debtorcustomerinfo.add(dc);
                            }
                        }
                    }
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (debtorcustomerinfo != null) {
                    bindlistDebtormer();
                    if (BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
                        customerid = debtorcustomerinfo.get(debtorcustomerinfo.size() - 1).CustomerID;
                    } else {
                        customerid = DatabaseHelper.getUUID();
                    }

                } else {
                    textviewStatus.setText("ไม่พบประวัติการซื้อ");
                    customerid = DatabaseHelper.getUUID();
                }
            }

        }).start();
    }

    private void bindlistDebtormer() {
        // TODO Auto-generated method stub
        BHArrayAdapter<DebtorCustomerInfo> adapter = new BHArrayAdapter<DebtorCustomerInfo>(activity, R.layout.list_sale_check_idcard, debtorcustomerinfo) {

            class ViewHolder {
                public TextView textViewproductID, textViewDate, textViewstatus;
            }

            @Override
            protected void onViewItem(int position, View view, Object holder, final DebtorCustomerInfo info) {
                // TODO Auto-generated method stub
                ViewHolder vh = (ViewHolder) holder;

                vh.textViewproductID.setText(info.ProductSerialNumber);
                vh.textViewDate.setText(BHUtilities.dateFormat(info.EFFDATE));
                vh.textViewstatus.setText("สถานะ : " + info.STATUS);
            }
        };
        listViewShow.setAdapter(adapter);
        listViewShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                debtorCustomerInfo = (DebtorCustomerInfo) listViewShow.getItemAtPosition(position);

                SaleCheckIDCardDetailFragment.Data data1 = new SaleCheckIDCardDetailFragment.Data();
                data1.customerID = debtorCustomerInfo.CustomerID;
                data1.IDCard = debtorCustomerInfo.IDCard;
                data1.RefNo = debtorCustomerInfo.RefNo;

                //changeContract
                if (BHPreference.ProcessType().equals(ProcessType.ChangeContract.toString())) {
                    data1.selectedCauseName = data.selectedCauseName;
                    data1.chgContractRequest = data.chgContractRequest;
                    data1.chgContractApprove = data.chgContractApprove;
                    data1.chgContractAction = data.chgContractAction;
                    data1.assign = data.assign;
                    data1.oldContract = data.oldContract;
                    data1.newContract = data.newContract;
                    data1.newSPPList = data.newSPPList;
                    data1.newPayment = data.newPayment;

                    data1.newContractImageInfo = data.newContractImageInfo;
                }

                SaleCheckIDCardDetailFragment fm = BHFragment.newInstance(SaleCheckIDCardDetailFragment.class, data1);
                showNextView(fm);
            }
        });
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                New2SaleCustomerAddressCardFragment.tmpDebtorCustomerInfo = data.tmpDebtorCustomer;
                New2SaleCustomerAddressCardFragment.tmpAddressInfo = data.tmpAddress;
                showLastView();
                break;
            default:
                break;
        }
    }

}
