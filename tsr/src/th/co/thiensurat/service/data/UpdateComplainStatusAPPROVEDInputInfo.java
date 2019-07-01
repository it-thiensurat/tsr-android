package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ComplainInfo;

public class UpdateComplainStatusAPPROVEDInputInfo extends BHParcelable {

    public String ComplainID;
    public String OrganizationCode;
    public String RefNo;
    public String Status;
    public String ApproveDetail;
    public Date ApprovedDate;
    public String ApprovedBy;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public String ReferenceID;
    public String TaskType;

    // Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
    public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
    public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

    public static UpdateComplainStatusAPPROVEDInputInfo from(ComplainInfo complain) {
        UpdateComplainStatusAPPROVEDInputInfo info = new UpdateComplainStatusAPPROVEDInputInfo();

        info.ComplainID = complain.ComplainID;
        info.OrganizationCode = complain.OrganizationCode;
        info.RefNo = complain.RefNo;
        info.Status = complain.Status;
        info.ApproveDetail = complain.ApproveDetail;
        info.ApprovedDate = complain.ApprovedDate;
        info.ApprovedBy = complain.ApprovedBy;
        info.CreateDate = complain.CreateDate;
        info.CreateBy = complain.CreateBy;
        info.LastUpdateDate = complain.LastUpdateDate;
        info.LastUpdateBy = complain.LastUpdateBy;
        info.ReferenceID = complain.ReferenceID;
        info.TaskType = complain.TaskType;
        info.RequestEmployeeLevelPath = complain.RequestEmployeeLevelPath;
        info.EffectiveEmployeeLevelPath = complain.EffectiveEmployeeLevelPath;

        return info;
    }

}

