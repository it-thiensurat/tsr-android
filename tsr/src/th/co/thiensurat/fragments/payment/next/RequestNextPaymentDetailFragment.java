package th.co.thiensurat.fragments.payment.next;

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
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.RequestNextPaymentController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.RequestNextPaymentInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RequestNextPaymentDetailFragment extends BHFragment {
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
        String RefNo;
        String RequestNextPaymentID;
        String AssigneeDefaultAssigneeEmpID;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_request_next_payment_detail;
    }

    @Override
    protected int[] processButtons() {
        if (data != null) {
            if (data.RequestNextPaymentID != null || data.AssigneeDefaultAssigneeEmpID != null) {
                return new int[]{R.string.button_back, R.string.button_payment_credit};
            } else {
                return new int[]{R.string.button_back, R.string.button_request_next_payment};
            }
        }
        return new int[]{};
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
                if (data != null && (data.RefNo != null || data.RequestNextPaymentID != null)) {
                    List<RequestNextPaymentInfo> list = new RequestNextPaymentController().getRequestNextPaymentByIDOrStatusOrSearchText(BHPreference.organizationCode(),
                            null, data.RequestNextPaymentID, null, data.RefNo);
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

                    if (requestNextPayment.Status != null && requestNextPayment.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString())) {
                        edtRequestProblemDetail.setText(requestNextPayment.RequestProblemDetail != null ? requestNextPayment.RequestProblemDetail : "");
                        edtRequestProblemDetail.setEnabled(false);
                    } else {
                        edtRequestProblemDetail.setEnabled(true);
                    }

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
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_request_next_payment:
                if (edtRequestProblemDetail.getText().toString().trim().isEmpty()) {
                    BHUtilities.alertDialog(activity, "คำเตือน", "กรอกหมายเหตุในการขออนุมัติ").show();
                } else {
                    AlertDialog.Builder builder = BHUtilities.builderDialog(activity, "บันทึก", "ยืนยันการส่งคำร้องเก็บเงินงวดต่อไป");
                    builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveRequestNextPayment();
                        }
                    });
                    builder.show();
                }
                break;
            case R.string.button_payment_credit:
                if (requestNextPayment != null && requestNextPayment.RefNo != null && requestNextPayment.RequestNextPaymentID != null) {
                    gotoPayment(requestNextPayment);
                } else if(data.AssigneeDefaultAssigneeEmpID != null){
                    saveRequestNextPayment();
                } else {
                    BHUtilities.alertDialog(activity, "คำเตือน", "มีข้อผิดพลาดเกิดขึ้น กรุณาติดต่อเจ้าหน้าที่เพื่อตรวจสอบ").show();
                }
                break;
            default:
                break;
        }
    }

    public void saveRequestNextPayment() {
        (new BackgroundProcess(activity) {
            RequestNextPaymentInfo info;

            @Override
            protected void before() {
                info = new RequestNextPaymentInfo();
                info.OrganizationCode = BHPreference.organizationCode();
                info.RequestNextPaymentID = DatabaseHelper.getUUID();
                info.RefNo = requestNextPayment.ContractRefNo;
                info.Status = RequestNextPaymentController.RequestNextPaymentStatus.REQUEST.toString();
                info.RequestProblemDetail = edtRequestProblemDetail.getText().toString();
                info.RequestDate = new Date();
                info.RequestBy = BHPreference.employeeID();
                info.RequestTeamCode = BHPreference.teamCode();
                info.CreateDate = new Date();
                info.CreateBy = BHPreference.employeeID();
                info.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                if (data.AssigneeDefaultAssigneeEmpID != null) {
                    info.Status = RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString();
                    info.ApproveDetail = edtRequestProblemDetail.getText().toString();
                    info.ApprovedDate = new Date();
                    info.ApprovedBy = BHPreference.employeeID();
                    info.LastUpdateDate = new Date();
                    info.LastUpdateBy = BHPreference.employeeID();
//                    new RequestNextPaymentController().updateRequestNextPayment(info);

                    if (info.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString())) {
                        AssignInfo assign = new AssignInfo();
                        assign.AssignID = DatabaseHelper.getUUID();
                        assign.TaskType = AssignController.AssignTaskType.RequestNextPayment.toString();
                        assign.OrganizationCode = BHPreference.organizationCode();
                        assign.RefNo = info.RefNo;
                        assign.AssigneeEmpID = info.RequestBy;
                        assign.AssigneeTeamCode = info.RequestTeamCode;
                        assign.Order = 0;
                        assign.OrderExpect = 0;
                        assign.CreateDate = new Date();
                        assign.CreateBy = BHPreference.employeeID();
                        assign.LastUpdateDate = new Date();
                        assign.LastUpdateBy = BHPreference.employeeID();
                        assign.ReferenceID = info.RequestNextPaymentID;
                        new AssignController().addAssign(assign);
                    }
                }
            }

            @Override
            protected void calling() {
                TSRController.addRequestNextPayment(info, true);
            }

            @Override
            protected void after() {
                /* FIX [BHPROJ-0026-754] กรณีเป็นการขอเก็บเงินของสัญญาที่เป็นของตัวเองอยู่แล้ว ไม่ต้องขออนุมัติขอเก็บเงินงวดต่อไป ให้สามารถเก็บเงินงวดต่อไปได้เลย */
                RequestNextPaymentInfo ownAssigneeEmpID = null;
                if (data.AssigneeDefaultAssigneeEmpID != null) {
                    List<RequestNextPaymentInfo> list = new RequestNextPaymentController().getRequestNextPaymentByIDOrStatusOrSearchText(BHPreference.organizationCode(),
                            null, info.RequestNextPaymentID, null, null);
                    ownAssigneeEmpID = list != null && list.size() > 0 ? list.get(0) : null;
                }
                if (ownAssigneeEmpID != null && ownAssigneeEmpID.RefNo != null && ownAssigneeEmpID.RequestNextPaymentID != null
                        && ownAssigneeEmpID.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString())
                        && ownAssigneeEmpID.AssigneeEmpID != null && ownAssigneeEmpID.AssigneeEmpID.equals(BHPreference.employeeID())) {
                    gotoPayment(ownAssigneeEmpID);
                } else {
                    RequestNextPaymentPrintFragment.Data data = new RequestNextPaymentPrintFragment.Data();
                    data.RequestNextPaymentID = info.RequestNextPaymentID;
                    RequestNextPaymentPrintFragment fragment = BHFragment.newInstance(RequestNextPaymentPrintFragment.class, data);
                    showNextView(fragment);
                }
            }
        }).start();
    }

    public void gotoPayment(RequestNextPaymentInfo _requestNextPayment){
        BHPreference.setProcessType(SaleFirstPaymentChoiceFragment.ProcessType.NextPayment.toString());
        BHPreference.setRefNo(_requestNextPayment.RefNo);
        SaleFirstPaymentChoiceFragment.Data paymentData = new SaleFirstPaymentChoiceFragment.Data(_requestNextPayment.RefNo
                , SaleFirstPaymentChoiceFragment.ProcessType.NextPayment, Double.valueOf(txtTotalOutStanding.getText().toString().replace(",", "")).floatValue());
        paymentData.requestNextPaymentID = _requestNextPayment.RequestNextPaymentID;
        SaleFirstPaymentChoiceFragment fragment = BHFragment.newInstance(SaleFirstPaymentChoiceFragment.class, paymentData);
        showNextView(fragment);
    }
}
