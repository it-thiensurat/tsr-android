package th.co.thiensurat.fragments.report;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.Column;
import th.co.thiensurat.adapter.ExpandableHeader;
import th.co.thiensurat.adapter.ExpandableListAdapter;
import th.co.thiensurat.adapter.Row;
import th.co.thiensurat.adapter.Style;
import th.co.thiensurat.adapter.Table;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.TripController;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.ReportDailySummaryInfo;
import th.co.thiensurat.data.info.TripInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetReportDailySummaryOnlineInputInfo;
import th.co.thiensurat.service.data.GetReportDailySummaryOnlineOutputInfo;

/***
 * [START] :: Fixed - [BHPROJ-0026-977] :: [Meeting@TSR@07/03/2559] [Report-รายการสรุปประจำวันของเก็บเงิน] เพิ่มเติม Report Dashboard ของระบบงานเก็บเงิน (หน้าตาจะคล้าย ๆ DailySummary)
 ***/

public class ReportDailySummaryOnlineFragment extends BHFragment {

    @InjectView
    TextView txtTeamCode, txtFortnightOrTripNumber, txtFortnightOrTripDateRange;

    @InjectView
    private ExpandableListView expListView;
    private ExpandableListAdapter expListAdapter;
    private List<ExpandableHeader> listDataHeader;
    private HashMap<ExpandableHeader, Table> listDataChild;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_daily_summary_online;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        bindData();
    }

    private void bindData() {
        (new BackgroundProcess(activity) {

            FortnightInfo fortnight;
            TripInfo trip;
            EmployeeDetailInfo employee;
            GetReportDailySummaryOnlineInputInfo input;
            GetReportDailySummaryOnlineOutputInfo output;
            ReportDailySummaryInfo report;

            @Override
            protected void before() {
                if (listDataHeader == null) {
                    listDataHeader = new ArrayList<ExpandableHeader>();
                } else {
                    listDataHeader.clear();
                }

                if (listDataChild == null) {
                    listDataChild = new HashMap<ExpandableHeader, Table>();
                } else {
                    listDataChild.clear();
                }

                input = new GetReportDailySummaryOnlineInputInfo();
                input.OrganizationCode = BHPreference.organizationCode();
                input.EffectiveDate = new Date();
                input.TopViewPositionID = BHPreference.sourceSystem();
                input.TopViewEmpID = BHPreference.employeeID();
            }

            @Override
            protected void calling() {
                fortnight = getCurrentFortnightInfo();
                trip = new TripController().getCurrentTrip();
                employee = new EmployeeDetailController().getTeamHeadDetailByTeamCodeAndEmpID(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());

                output = TSRService.getReportDailySummaryOnline(input, false);
                if (output != null && output.ResultCode == 0) {
                    report = output.Info;
                }
            }

            @Override
            protected void after() {
                boolean isSale = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Sale.toString());
                if ((isSale && fortnight != null) || (!isSale && trip != null)) {
                    txtTeamCode.setText(String.format("ทีม %s", BHPreference.teamCode()));

                    String strFortnightYear = BHUtilities.dateFormat(isSale ? fortnight.StartDate : trip.StartDate, "yy");
                    txtFortnightOrTripNumber.setText(String.format("%sที่ %d/%s", isSale ? "ปักษ์" : "ทริป", isSale ? fortnight.FortnightNumber : trip.TripNumber, strFortnightYear));

                    String strStartDate = BHUtilities.dateFormat(isSale ? fortnight.StartDate : trip.StartDate);
                    String strEndDate = BHUtilities.dateFormat(isSale ? fortnight.EndDate: trip.EndDate);
                    txtFortnightOrTripDateRange.setText(String.format("%s-%s", strStartDate, strEndDate));
                }

                if (employee == null) new EmployeeDetailInfo();
                listDataHeader.add(new ExpandableHeader("รายละเอียดพนักงาน", true, true, employee, R.layout.employee_detail_item));

                if (report != null) {
                    if (isSale) {
                        listDataHeader.add(new ExpandableHeader("รายงานขาย"));
                        List<Row> rows = new ArrayList<>();
                        rows.add(new Row(Arrays.asList(new Column("รายงานขาย"), new Column("จำนวน(เครื่อง)"), new Column("เงิน")), Style.Typeface(Typeface.BOLD)));
                        rows.add(new Row(Arrays.asList(new Column(String.format("ยอดขาย ณ %s", BHUtilities.dateFormat(report.ReportDate))), new Column(format(report.SaleSumTotal)), new Column(format(report.SaleSumMoney))), true));
                        rows.add(new Row(Arrays.asList(new Column("ขายสด", Style.Typeface(Typeface.BOLD)))));
                        rows.add(new Row(Arrays.asList(new Column("เก็บ"), new Column(format(report.CashNumber)), new Column(format(report.CashMoney)))));
                        rows.add(new Row(Arrays.asList(new Column("ค้าง"), new Column(format(report.CashOutNumber)), new Column(format(report.CashOutMoney)))));
                        rows.add(new Row(Arrays.asList(new Column("ยอดรวม"), new Column(format(report.CashSumNumber)), new Column(format(report.CashSumMoney))), true));
                        rows.add(new Row(Arrays.asList(new Column("ขายผ่อน", Style.Typeface(Typeface.BOLD)))));
                        rows.add(new Row(Arrays.asList(new Column("เก็บ"), new Column(format(report.OutNumber)), new Column(format(report.OutMoney)))));
                        rows.add(new Row(Arrays.asList(new Column("ค้าง"), new Column(format(report.OutStandingNumber)), new Column(format(report.OutStandingMoney)))));
                        rows.add(new Row(Arrays.asList(new Column("ยอดรวม"), new Column(format(report.OutSumNumber)), new Column(format(report.OutSumMoney)))));

                        Table table = new Table(rows, new Style[]{new Style(Gravity.LEFT | Gravity.CENTER_VERTICAL, 0.5f), new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.25f), new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.25f)});
                        listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), table);
                    }
                    {
                        listDataHeader.add(new ExpandableHeader("เก็บเงิน-ส่งเงิน"));
                        List<Row> rows = new ArrayList<>();
                        rows.add(new Row(Arrays.asList(new Column("ยอดเงินที่เก็บได้", Style.Typeface(Typeface.BOLD)), new Column(format(report.PaymentSumMoney)))));
                        rows.add(new Row(Arrays.asList(new Column("ส่งแล้ว"), new Column(format(report.SendMoneyAlready)))));
                        rows.add(new Row(Arrays.asList(new Column("ค้างส่ง"), new Column(format(report.SendMoneyOut))), Style.Color(R.color.bg_body_red)));

                        Table table = new Table(rows, new Style[]{new Style(Gravity.LEFT | Gravity.CENTER_VERTICAL, 0.5f), new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.5f)});
                        listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), table);
                    }
                    {
                        listDataHeader.add(new ExpandableHeader("เปลี่ยน-ถอด-คืน"));
                        List<Row> rows = new ArrayList<>();
                        rows.add(new Row(Arrays.asList(new Column(""), new Column("จำนวน(เครื่อง)"), new Column("คืน"), new Column("รอคืน")), Style.Typeface(Typeface.BOLD)));
                        rows.add(new Row(Arrays.asList(new Column("1.สินค้าเทิร์น"), new Column(format(report.ProductTradeIn)), new Column(format(report.ProductTradeInReturn)), new Column(format(report.ProductTradeInWaitReturn))), true));
                        rows.add(new Row(Arrays.asList(new Column("2.ถอดเครื่อง"))));
                        if (isSale)
                            rows.add(new Row(Arrays.asList(new Column("- ถอดเครื่องทีมตัวเอง"), new Column(format(report.ImpoundBySale)), new Column(format(report.ImpoundBySaleReturn)), new Column(format(report.ImpoundBySaleWaitReturn)))));
                        rows.add(new Row(Arrays.asList(new Column("- ถอดเครื่องทีมอื่น"), new Column(format(report.ImpoundByTeam)), new Column(format(report.ImpoundByTeamReturn)), new Column(format(report.ImpoundByTeamWaitReturn))), true));
                        rows.add(new Row(Arrays.asList(new Column("3.เปลี่ยนเครื่อง"), new Column(format(report.ChangeProduct)), new Column(format(report.ChangeProductReturn)), new Column(format(report.ChangeProductWaitReturn)))));

                        Table table = new Table(rows, new Style[]{new Style(Gravity.LEFT | Gravity.CENTER_VERTICAL, 0.4f), new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.2f)
                                , new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.2f), new Style(R.color.bg_body_red, Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.2f)});
                        listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), table);
                    }
                    {
                        listDataHeader.add(new ExpandableHeader("เอกสารนำส่ง"));
                        List<Row> rows = new ArrayList<>();
                        rows.add(new Row(Arrays.asList(new Column("เอกสารนำส่ง"), new Column("จำนวน(ใบ)"), new Column("นำส่ง"), new Column("ค้างส่ง")), Style.Typeface(Typeface.BOLD)));
                        rows.add(new Row(Arrays.asList(new Column("สัญญา"), new Column(format(report.SendContract)), new Column(format(report.SendContractAlreadySent)), new Column(format(report.SendContractWaitSent)))));
                        rows.add(new Row(Arrays.asList(new Column("ใบเสร็จ"), new Column(format(report.SendReceipt)), new Column(format(report.SendReceiptAlreadySent)), new Column(format(report.SendReceiptWaitSent)))));
                        rows.add(new Row(Arrays.asList(new Column("ใบถอดเครื่อง"), new Column(format(report.SendImpound)), new Column(format(report.SendImpoundProductAlreadySent)), new Column(format(report.SendImpoundProductWaitSent)))));
                        rows.add(new Row(Arrays.asList(new Column("ใบเปลี่ยนเครื่อง"), new Column(format(report.SendChangeProduct)), new Column(format(report.SendChangeProductAlreadySent)), new Column(format(report.SendChangeProductWaitSent)))));
                        rows.add(new Row(Arrays.asList(new Column("ใบเปลี่ยนสัญญา"), new Column(format(report.SendChangeContract)), new Column(format(report.SendChangeContractAlreadySent)), new Column(format(report.SendChangeContractWaitSent)))));
                        rows.add(new Row(Arrays.asList(new Column("ใบนำส่งเงิน"), new Column(format(report.SendManualDocument)), new Column(format(report.SendManualDocumentAlreadySent)), new Column(format(report.SendManualDocumentWaitSent)))));
                        rows.add(new Row(Arrays.asList(new Column("Slip ธนาคาร"), new Column(format(report.SendPayInSlipBank)), new Column(format(report.SendPayInSlipBankAlreadySent)), new Column(format(report.SendPayInSlipBankWaitSent)))));
                        rows.add(new Row(Arrays.asList(new Column("Slip เพย์พอยท์"), new Column(format(report.SendPayInSlipPayPoint)), new Column(format(report.SendPayInSlipPayPointAlreadySent)), new Column(format(report.SendPayInSlipPayPointWaitSent))), true));
                        rows.add(new Row(Arrays.asList(new Column("รวม"), new Column(format(report.TotalSendDocument)), new Column(format(report.TotalAlreadySent)), new Column(format(report.TotalWaitSent)))));

                        Table table = new Table(rows, new Style[]{new Style(Gravity.LEFT | Gravity.CENTER_VERTICAL, 0.4f), new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.2f)
                                , new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.2f), new Style(R.color.bg_body_red, Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.2f)});
                        listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), table);
                    }
                    {
                        listDataHeader.add(new ExpandableHeader("เปลี่ยนสัญญา-ตัดสัญญาค้าง"));
                        List<Row> rows = new ArrayList<>();
                        rows.add(new Row(Arrays.asList(new Column("จำนวน(ฉบับ)", new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 1.0f, Typeface.BOLD)))));
                        if (!isSale)
                            rows.add(new Row(Arrays.asList(new Column("เปลี่ยนสัญญาโดยพนักงาน"), new Column(format(report.ChangeContractByEmp)))));
                        rows.add(new Row(Arrays.asList(new Column("เปลี่ยนสัญญาโดยหัวหน้าหน่วย"), new Column(format(report.ChangeContractBySubTeam)))));
                        rows.add(new Row(Arrays.asList(new Column("เปลี่ยนสัญญาโดยหัวหน้าทีม"), new Column(format(report.ChangeContractByTeam)))));
                        rows.add(new Row(Arrays.asList(new Column("ตัดสัญญาค้าง"), new Column(format(report.WriteOffNPL)))));

                        Table table = new Table(rows, new Style[]{new Style(Gravity.LEFT | Gravity.CENTER_VERTICAL, 0.5f), new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.5f)});
                        listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), table);
                    }
                } else {
                    if (output == null) {
                        showWarningDialog("แจ้งเตือน", "กรุณาตรวจสอบการเชื่อมต่ออินเตอร์เน็ต");
                    } else {
                        showWarningDialog("แจ้งเตือน", "ไม่พบข้อมูล");
                    }

                }

                if (expListAdapter == null) {
                    expListAdapter = new ExpandableListAdapter(getActivity(), R.layout.expandable_header, R.layout.expandable_item, listDataHeader, listDataChild);
                    expListView.setAdapter(expListAdapter);
                } else {
                    expListAdapter.notifyDataSetChanged();
                }
            }
        }).start();
    }

    public String format(Object obj) {
        if (obj == null) return "";
        if (obj.getClass().equals(int.class) || obj.getClass().equals(Integer.class)) {
            return BHUtilities.numericFormat((int) obj);
        } else if (obj.getClass().equals(float.class) || obj.getClass().equals(Float.class)) {
            return BHUtilities.numericFormat((float) obj);
        } else if (obj.getClass().equals(double.class) || obj.getClass().equals(Double.class)) {
            return BHUtilities.numericFormat((double) obj);
        }
        return "";
    }
}
