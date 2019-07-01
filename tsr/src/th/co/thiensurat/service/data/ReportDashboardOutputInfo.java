package th.co.thiensurat.service.data;

import java.util.List;

import th.co.bighead.utilities.BHParcelable;

public class ReportDashboardOutputInfo extends BHParcelable {

    public List<ReportDashboardTeamInfo> DashboardTeam;
    public List<ReportDashboardPaymentInfo> DashboardPayment;
    public ReportDashboardPaymentAmountInfo DashboardPaymentAmount;
    public ReportDashboardRequestInfo DashboardRequest;
}