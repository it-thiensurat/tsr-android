package th.co.thiensurat.fragments.payment.next;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.RequestNextPaymentController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.RequestNextPaymentInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;

public class RequestNextPaymentPrintFragment extends BHFragment {
    @InjectView
    TextView txtEffDate, txtCONTNO, txtCustomerFullName, txtIDCard, txtAddressIDCard, txtAddressInstall, txtModel, txtProductSerialNumber, txtSales, txtTradeInDiscount, txtTotalPrice, txtOutStandingAmount, lblOutStandingAmount, txtTotalOutStanding, txtPaidAmount;
    @InjectView
    LinearLayout OutStandingAmountView, listView;
    @InjectView
    EditText edtRequestProblemDetail;

    RequestNextPaymentInfo requestNextPayment;
    AddressInfo addressIDCard;
    AddressInfo addressInstall;
    List<SalePaymentPeriodInfo> salePaymentPeriodInfoList;

    private Data data;

    public static class Data extends BHParcelable {
        String RequestNextPaymentID;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_request_next_payment_print;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_end};
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = getData();
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        setUI();
    }

    public void setUI() {
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                if (data != null && (data.RequestNextPaymentID != null)) {
                    List<RequestNextPaymentInfo> list = new RequestNextPaymentController().getRequestNextPaymentByIDOrStatusOrSearchText(BHPreference.organizationCode(),
                            null, data.RequestNextPaymentID, null, null);
                    if (list != null && list.size() > 0) {
                        requestNextPayment = list.get(0);
                        addressIDCard = getAddressByRefNoByAddressTypeCode(requestNextPayment.ContractRefNo,
                                AddressInfo.AddressType.AddressIDCard.toString());
                        addressInstall = getAddressByRefNoByAddressTypeCode(requestNextPayment.ContractRefNo,
                                AddressInfo.AddressType.AddressInstall.toString());
                    }
                    salePaymentPeriodInfoList = new SalePaymentPeriodController().getSalePaymentPeriodOutStandingByRefNo(requestNextPayment.ContractRefNo);
                }
            }

            @Override
            protected void after() {
                if (requestNextPayment != null) {
                    txtEffDate.setText(requestNextPayment.EFFDATE != null ? BHUtilities.dateFormat(requestNextPayment.EFFDATE) : "-");
                    txtCONTNO.setText(requestNextPayment.CONTNO != null ? requestNextPayment.CONTNO : "-");
                    txtCustomerFullName.setText(requestNextPayment.CustomerFullName != null ? requestNextPayment.CustomerFullName : "-");
                    txtIDCard.setText(requestNextPayment.IDCard != null ? requestNextPayment.IDCard : "-");
                    txtModel.setText(requestNextPayment.ProductName != null ? requestNextPayment.ProductName : "-");
                    txtProductSerialNumber.setText(requestNextPayment.ProductSerialNumber != null ? requestNextPayment.ProductSerialNumber : "-");
                    txtSales.setText(BHUtilities.numericFormat(requestNextPayment.SALES));
                    txtTradeInDiscount.setText(BHUtilities.numericFormat(requestNextPayment.TradeInDiscount));
                    txtTotalPrice.setText(BHUtilities.numericFormat(requestNextPayment.TotalPrice));
                    txtPaidAmount.setText(BHUtilities.numericFormat(requestNextPayment.PaidAmount));

                    edtRequestProblemDetail.setText(requestNextPayment.RequestProblemDetail != null ? requestNextPayment.RequestProblemDetail : "");
                    edtRequestProblemDetail.setEnabled(false);

                    txtAddressIDCard.setText(addressIDCard.Address());
                    txtAddressInstall.setText(addressInstall.Address());

                    if (requestNextPayment.NextPaymentPeriodNumber == 0) {
                        OutStandingAmountView.setVisibility(View.GONE);
                    } else {
                        OutStandingAmountView.setVisibility(View.VISIBLE);
                        if (requestNextPayment.NextPaymentPeriodNumber == requestNextPayment.MODE) {
                            lblOutStandingAmount.setText(String.format("คงเหลือ งวดที่ %d", requestNextPayment.MODE));
                        } else {
                            lblOutStandingAmount.setText(String.format("คงเหลือ งวดที่ %d ถึง %d", requestNextPayment.NextPaymentPeriodNumber, requestNextPayment.MODE));
                        }
                        txtOutStandingAmount.setText(BHUtilities.numericFormat(requestNextPayment.OutstandingTotal));
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

        if (!s.OverDue) {
            txtPaymentPeriodNumber.setTextColor(getResources().getColor(R.color.holo_dark));
            txtAmount.setTextColor(getResources().getColor(R.color.holo_dark));
            TextView lblBath = (TextView) v.findViewById(R.id.lblBath);
            if (lblBath != null) {
                lblBath.setTextColor(getResources().getColor(R.color.holo_dark));
            }
        }
        return v;
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_end:
                showLastView(NextPaymentListFragment.NEXT_PAYMENT_LIST_FRAGMENT);
                break;
            case R.string.button_print:
                break;
            default:
                break;
        }
    }
}
