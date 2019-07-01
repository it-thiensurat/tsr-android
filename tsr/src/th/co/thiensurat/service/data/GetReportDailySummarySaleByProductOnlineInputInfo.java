package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class GetReportDailySummarySaleByProductOnlineInputInfo extends BHParcelable {
    public String OrganizationCode;
    public Date EffectiveDate;                  // Current Date
    public String TopViewPositionID;            // Sale || Credit
    public String TopViewEmpID;                 // EmployeeCOde
}
