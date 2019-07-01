package th.co.thiensurat.fragments.credit.credit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;

public class CreditDetailFragment extends BHFragment {
    @InjectView
    TextView txtEffDate, txtCONTNO, txtCustomerFullName, txtIDCard, txtAddressIDCard, txtAddressInstall, txtModel, txtProductSerialNumber
            , txtSales, txtTradeInDiscount, txtTotalPrice, txtOutStandingAmount, lblOutStandingAmount, txtTotalOutStanding, txtPaidAmount;
    @InjectView
    LinearLayout OutStandingAmountView, listView;

    ContractInfo contract;
    AddressInfo addressIDCard;
    AddressInfo addressInstall;
    List<SalePaymentPeriodInfo> salePaymentPeriodInfoList;

    private Data data;

    public static class Data extends BHParcelable {
        String AssignID;
        Date selectedDate;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_credit_detail;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_payment_credit};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();

        setUI();
    }

    public void setUI() {
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                if (data != null && data.AssignID != null) {
                    contract = new ContractController().getContractByAssignIDForNextPayment(data.AssignID);
                    if (contract != null) {
                        addressIDCard = getAddressByRefNoByAddressTypeCode(contract.RefNo,
                                AddressInfo.AddressType.AddressIDCard.toString());
                        addressInstall = getAddressByRefNoByAddressTypeCode(contract.RefNo,
                                AddressInfo.AddressType.AddressInstall.toString());
                    }
                    salePaymentPeriodInfoList = new SalePaymentPeriodController().getSalePaymentPeriodOutStandingByAssignID(data.AssignID, BHPreference.RefNo());
                }
            }

            @Override
            protected void after() {
                if (contract != null) {
                    txtEffDate.setText(contract.EFFDATE != null ? BHUtilities.dateFormat(contract.EFFDATE) : "-");
                    txtCONTNO.setText(contract.CONTNO != null ? contract.CONTNO : "-");
                    txtCustomerFullName.setText(contract.CustomerFullName != null ? contract.CustomerFullName : "-");
                    txtIDCard.setText(contract.IDCard != null ? contract.IDCard : "-");
                    txtModel.setText(contract.ProductName != null ? contract.ProductName : "-");
                    txtProductSerialNumber.setText(contract.ProductSerialNumber != null ? contract.ProductSerialNumber : "-");
                    txtSales.setText(BHUtilities.numericFormat(contract.SALES));
                    txtTradeInDiscount.setText(BHUtilities.numericFormat(contract.TradeInDiscount));
                    txtTotalPrice.setText(BHUtilities.numericFormat(contract.TotalPrice));
                    txtPaidAmount.setText(BHUtilities.numericFormat(contract.PaidAmount));

                    txtAddressIDCard.setText(addressIDCard.Address());
                    txtAddressInstall.setText(addressInstall.Address());

                    if (contract.PaymentPeriodNumber == contract.MODE || contract.NextPaymentPeriodNumber > contract.MODE) {
                        OutStandingAmountView.setVisibility(View.GONE);
                    } else {
                        OutStandingAmountView.setVisibility(View.VISIBLE);
                        if (contract.NextPaymentPeriodNumber == contract.MODE) {
                            lblOutStandingAmount.setText(String.format("คงเหลือ งวดที่ %d", contract.MODE));
                        } else {
                            lblOutStandingAmount.setText(String.format("คงเหลือ งวดที่ %d ถึง %d", contract.NextPaymentPeriodNumber, contract.MODE));
                        }
                        txtOutStandingAmount.setText(BHUtilities.numericFormat(contract.OutstandingAmount));
                    }

                    if (salePaymentPeriodInfoList != null && salePaymentPeriodInfoList.size() > 0) {
                        float sumOutStandingAmount = 0;
                        for (SalePaymentPeriodInfo s : salePaymentPeriodInfoList) {
                            sumOutStandingAmount += s.OutstandingAmount;
                            String item1 = String.format("งวดที่ %d", s.PaymentPeriodNumber);
                            String item2 = BHUtilities.numericFormat(s.OutstandingAmount);
                            listView.addView(createItemListView(item1, item2, s));
                        }
                        txtTotalOutStanding.setText(BHUtilities.numericFormat(sumOutStandingAmount));
                    }
                }
            }
        }).start();
    }

    public View createItemListView(String item1, String item2, SalePaymentPeriodInfo s) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.list_credit_detail, null);
        TextView txtPaymentPeriodNumber = (TextView) v.findViewById(R.id.txtPaymentPeriodNumber);
        if (txtPaymentPeriodNumber != null) {
            txtPaymentPeriodNumber.setText(item1);
        }
        TextView txtAmount = (TextView) v.findViewById(R.id.txtAmount);
        if (txtAmount != null) {
            txtAmount.setText(item2);
        }

        if(!s.OverDue){
            txtPaymentPeriodNumber.setTextColor(getResources().getColor(R.color.holo_dark));
            txtAmount.setTextColor(getResources().getColor(R.color.holo_dark));
            TextView lblBath = (TextView) v.findViewById(R.id.lblBath);
            if(lblBath != null){
                lblBath.setTextColor(getResources().getColor(R.color.holo_dark));
            }
        }
        return v;
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_payment_credit:
                String sTotal = txtTotalOutStanding.getText().toString().trim().replace(",", "");
                sTotal = sTotal.isEmpty() ? "0" : sTotal;
                float total = Double.valueOf(sTotal).floatValue();
                SaleFirstPaymentChoiceFragment.Data paymentData = new SaleFirstPaymentChoiceFragment.Data(contract.RefNo,
                        SaleFirstPaymentChoiceFragment.ProcessType.Credit, total, data.AssignID, data.selectedDate);
                BHPreference.setProcessType(SaleFirstPaymentChoiceFragment.ProcessType.Credit.toString());
                BHPreference.setRefNo(contract.RefNo);
                SaleFirstPaymentChoiceFragment fragment = BHFragment.newInstance(SaleFirstPaymentChoiceFragment.class, paymentData);
                showNextView(fragment);
                break;
            default:
                break;
        }
    }
}
