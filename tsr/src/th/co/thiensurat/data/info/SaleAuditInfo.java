package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class SaleAuditInfo  extends BHParcelable {
    public String SaleAuditID;
    public String OrganizationCode;
    public String RefNo;
    public Date AuditDate;
    public Date AppointmentAuditDate;
    public String AuditorSaleCode;
    public String AuditorEmployeeID;
    public String AuditorTeamCode;
    public boolean IsPassFirstPayment;
    public boolean IsPassInstall;
    public boolean HasOtherOffer;
    public String OtherOfferDetail;
    public String ComplainProblemID;
    public String ComplainDetail;
    public String AuditEmployeeLevelPath;
    public Date AppointmentPaymentDate;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;
    public String ComplainID;
    public boolean IsPassSaleAudit;

    // Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
    public String SaleAuditEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการตรวจสอบ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ)

    // EXTRA
    public int Order;
    public String CustomerFullName;
    public int PaymentPeriodNumber;
    public float NetAmount;
    public String CONTNO;
}
