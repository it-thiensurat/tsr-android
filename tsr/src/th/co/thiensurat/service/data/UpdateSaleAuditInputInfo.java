package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.SaleAuditInfo;

public class UpdateSaleAuditInputInfo extends BHParcelable {
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

    public static UpdateSaleAuditInputInfo from(SaleAuditInfo sa) {
        UpdateSaleAuditInputInfo info = new UpdateSaleAuditInputInfo();

        info.SaleAuditID = sa.SaleAuditID;
        info.OrganizationCode = sa.OrganizationCode;
        info.RefNo = sa.RefNo;
        info.AuditDate = sa.AuditDate;
        info.AppointmentAuditDate = sa.AppointmentAuditDate;
        info.AuditorSaleCode = sa.AuditorSaleCode;
        info.AuditorEmployeeID = sa.AuditorEmployeeID;
        info.AuditorTeamCode = sa.AuditorTeamCode;
        info.IsPassFirstPayment = sa.IsPassFirstPayment;
        info.IsPassInstall = sa.IsPassInstall;
        info.HasOtherOffer = sa.HasOtherOffer;
        info.OtherOfferDetail = sa.OtherOfferDetail;
        info.ComplainProblemID = sa.ComplainProblemID;
        info.ComplainDetail = sa.ComplainDetail;
        info.AuditEmployeeLevelPath = sa.AuditEmployeeLevelPath;
        info.AppointmentPaymentDate = sa.AppointmentPaymentDate;
        info.CreateDate = sa.CreateDate;
        info.CreateBy = sa.CreateBy;
        info.LastUpdateDate = sa.LastUpdateDate;
        info.LastUpdateBy = sa.LastUpdateBy;
        info.SyncedDate = sa.SyncedDate;
        info.ComplainID = sa.ComplainID;
        info.IsPassSaleAudit = sa.IsPassSaleAudit;
        info.SaleAuditEmployeeLevelPath = sa.SaleAuditEmployeeLevelPath;

        return info;
    }
}
