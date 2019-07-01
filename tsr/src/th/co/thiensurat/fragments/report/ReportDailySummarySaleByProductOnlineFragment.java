package th.co.thiensurat.fragments.report;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import th.co.thiensurat.data.info.ReportDailySummarySaleByProductInfo;
import th.co.thiensurat.data.info.TripInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetReportDailySummarySaleByProductOnlineInputInfo;

public class ReportDailySummarySaleByProductOnlineFragment extends BHFragment {

    @InjectView
    TextView txtTeamCode, txtFortnightOrTripNumber, txtFortnightOrTripDateRange;

    @InjectView
    private ExpandableListView expListView;
    private ExpandableListAdapter expListAdapter;
    private List<ExpandableHeader> listDataHeader;
    private HashMap<ExpandableHeader, Table> listDataChild;

    private final Style[] mainHeaderRowStyle =  new Style[]{new Style(Gravity.LEFT | Gravity.CENTER_VERTICAL, 1f)};
    private final Style[] headerRowStyle =  new Style[]{new Style(Gravity.LEFT | Gravity.CENTER_VERTICAL, 0.5f), new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.25f), new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.25f)};
    private final Style[] detailRowStyle = new Style[]{new Style(Gravity.LEFT | Gravity.CENTER_VERTICAL, 0.05f), new Style(Gravity.LEFT | Gravity.CENTER_VERTICAL, 0.45f), new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.25f), new Style(Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0.25f)};


    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_daily_summary_sale_by_product_online;
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
            GetReportDailySummarySaleByProductOnlineInputInfo input;
            List<ReportDailySummarySaleByProductInfo> report;
            Calendar c = Calendar.getInstance();

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

                //Test
                //c.set(2016, 4, 18);
                input = new GetReportDailySummarySaleByProductOnlineInputInfo();
                input.OrganizationCode = BHPreference.organizationCode();
                input.EffectiveDate = c.getTime();
                input.TopViewPositionID = BHPreference.sourceSystem();
                input.TopViewEmpID = BHPreference.employeeID();
            }

            @Override
            protected void calling() {
                fortnight = getCurrentFortnightInfo();
                trip = new TripController().getCurrentTrip();
                employee = new EmployeeDetailController().getTeamHeadDetailByTeamCodeAndEmpID(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());
                report = TSRService.getReportDailySummarySaleByProductOnline(input, false).Info;
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

                if (report == null) report = new ArrayList<ReportDailySummarySaleByProductInfo>();
                if(report.size() >= 1 ){
                    employee.SaleSumTotal = report.get(0).SaleSumTotal;
                    employee.SaleSumMoney =  report.get(0).SaleSumMoney;
                    employee.ReportDate = report.get(0).ReportDate;
                } else {
                    employee.SaleSumTotal = 0;
                    employee.SaleSumMoney =  0f;
                    employee.ReportDate = c.getTime();
                }
                listDataHeader.add(new ExpandableHeader("รายละเอียดพนักงาน", true, true, employee, R.layout.employee_detail_item_by_product));

                if (isSale) {
                    for(ReportDailySummarySaleByProductInfo info : report){
                        listDataHeader.add(new ExpandableHeader(info.ProductName));
                        Table table = new Table();

                        addRow(table, new Row(Arrays.asList(new Column(""), new Column("จำนวน(เครื่อง)"), new Column("เงิน")), true,Style.Typeface(Typeface.BOLD)), headerRowStyle);

                        //ขายสด
                        addRow(table, new Row(Arrays.asList(new Column("ขายสด", Style.Typeface(Typeface.BOLD)), new Column(format(info.CashSumNumber)), new Column(format(info.CashSumMoney)))), headerRowStyle);
                        addRow(table, new Row(Arrays.asList(new Column(""), new Column("เก็บ"), new Column(""), new Column(format(info.CashMoney)))), detailRowStyle);
                        addRow(table, new Row(Arrays.asList(new Column(""), new Column("ค้าง"), new Column(""), new Column(format(info.CashOutMoney))), true), detailRowStyle);

                        //ขายผ่อน
                        addRow(table, new Row(Arrays.asList(new Column("ขายผ่อน", Style.Typeface(Typeface.BOLD)), new Column(format(info.OutSumNumber)), new Column(format(info.OutSumMoney)))), headerRowStyle);
                        addRow(table, new Row(Arrays.asList(new Column(""), new Column("เก็บ"), new Column(""), new Column(format(info.OutMoney)))), detailRowStyle);
                        addRow(table, new Row(Arrays.asList(new Column(""), new Column("ค้าง"), new Column(""), new Column(format(info.OutStandingMoney))), true), detailRowStyle);

                        addRow(table, new Row(Arrays.asList(new Column("ยอดรวม"), new Column(format(info.CashSumNumber + info.OutSumNumber)), new Column(format(info.CashSumMoney + info.OutSumMoney)))), headerRowStyle);
                        listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), table);
                    }
                }

                if (expListAdapter == null) {
                    expListAdapter = new ExpandableListAdapter(getActivity(), R.layout.expandable_header_by_product, R.layout.expandable_item, listDataHeader, listDataChild);
                    expListView.setAdapter(expListAdapter);
                } else {
                    expListAdapter.notifyDataSetChanged();
                }
            }
        }).start();
    }

    private void addRow(Table table, Row row, Style[] style){
        table.rows.add(setStyles(row, style));
    }

    public Row setStyles(Row row, Style[] style) {
        if (!(row != null && style != null)) return null;
        int i = 0;
        for (Column c : row.getFields()) {
            Style tempStyle = style[i];
            if (c.getStyle() == null) {
                c.setStyle(new Style(tempStyle.getColor(), tempStyle.getTypeface(), tempStyle.getGravity(), tempStyle.getLayout_weight()));
            } else {
                if (tempStyle.getColor() > Integer.MIN_VALUE)
                    c.getStyle().setColor(tempStyle.getColor());
                if (tempStyle.getTypeface() > Integer.MIN_VALUE)
                    c.getStyle().setTypeface(tempStyle.getTypeface());
                if(style.length == row.getFields().size()) {
                    if (tempStyle.getGravity() > Integer.MIN_VALUE && c.getStyle().getGravity() == Integer.MIN_VALUE)
                        c.getStyle().setGravity(tempStyle.getGravity());
                    if (tempStyle.getLayout_weight() > Float.MIN_VALUE && c.getStyle().getLayout_weight() == Float.MIN_VALUE)
                        c.getStyle().setLayout_weight(tempStyle.getLayout_weight());
                }
            }
            i++;
        }
        return row;
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
