package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.RequestNextPaymentInfo;

public class UpdateRequestNextPaymentInputInfo extends BHParcelable {
    public String RequestNextPaymentID;
    public String OrganizationCode;
    public String RefNo;
    public String PaymentID;
    public String Status;
    public String RequestProblemDetail;
    public Date RequestDate;
    public String RequestBy;
    public String RequestTeamCode;
    public Date ApprovedDate;
    public String ApproveDetail;
    public String ApprovedBy;
    public String ResultProblemDetail;
    public Date EffectiveDate;
    public String EffectiveBy;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    // Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
    public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
    public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

    public static UpdateRequestNextPaymentInputInfo from(RequestNextPaymentInfo coc) {
        UpdateRequestNextPaymentInputInfo info = new UpdateRequestNextPaymentInputInfo();
        info.RequestNextPaymentID = coc.RequestNextPaymentID;
        info.OrganizationCode = coc.OrganizationCode;
        info.RefNo = coc.RefNo;
        info.PaymentID = coc.PaymentID;
        info.Status = coc.Status;
        info.RequestProblemDetail = coc.RequestProblemDetail;
        info.RequestDate = coc.RequestDate;
        info.RequestBy = coc.RequestBy;
        info.RequestTeamCode  = coc.RequestTeamCode;
        info.ApprovedDate  = coc.ApprovedDate;
        info.ApproveDetail = coc.ApproveDetail;
        info.ApprovedBy = coc.ApprovedBy;
        info.ResultProblemDetail = coc.ResultProblemDetail;
        info.EffectiveDate = coc.EffectiveDate;
        info.EffectiveBy = coc.EffectiveBy;
        info.CreateDate = coc.CreateDate;
        info.CreateBy = coc.CreateBy;
        info.LastUpdateDate = coc.LastUpdateDate;
        info.LastUpdateBy = coc.LastUpdateBy;
        info.SyncedDate = coc.SyncedDate;
        info.RequestEmployeeLevelPath = coc.RequestEmployeeLevelPath;
        info.EffectiveEmployeeLevelPath = coc.EffectiveEmployeeLevelPath;
        return info;
    }
}
