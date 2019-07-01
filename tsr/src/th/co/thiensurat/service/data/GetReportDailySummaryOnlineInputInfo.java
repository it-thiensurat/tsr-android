package th.co.thiensurat.service.data;

import java.util.Date;
import th.co.bighead.utilities.BHParcelable;

/***
 *
 * Fixed - [BHPROJ-0026-977] :: [Meeting@TSR@07/03/2559] [Report-รายการสรุปประจำวันของเก็บเงิน] เพิ่มเติม Report Dashboard ของระบบงานเก็บเงิน (หน้าตาจะคล้าย ๆ DailySummary)
 *
 * ***/

public class GetReportDailySummaryOnlineInputInfo extends BHParcelable {
    public String OrganizationCode;
    public Date EffectiveDate;                  // Current Date
    public String TopViewPositionID;            // Sale || Credit
    public String TopViewEmpID;                 // EmployeeCOde
}
