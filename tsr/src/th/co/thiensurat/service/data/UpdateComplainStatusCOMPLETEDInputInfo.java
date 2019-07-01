package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ComplainInfo;

public class UpdateComplainStatusCOMPLETEDInputInfo extends BHParcelable {

    public String ComplainID;
    public String OrganizationCode;
    public String RefNo;
    public String Status;
    public String ResultProblemID;
    public String ResultDetail;
    public Date EffectiveDate;
    public String EffectiveBy;
    public String ComplainPaperID;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public String ReferenceID;
    public String TaskType;

    // Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
    public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
    public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

    public static UpdateComplainStatusCOMPLETEDInputInfo from(ComplainInfo complain) {
        UpdateComplainStatusCOMPLETEDInputInfo info = new UpdateComplainStatusCOMPLETEDInputInfo();

        info.ComplainID = complain.ComplainID;
        info.OrganizationCode = complain.OrganizationCode;
        info.RefNo = complain.RefNo;
        info.Status = complain.Status;
        info.ResultProblemID = complain.ResultProblemID;
        info.ResultDetail = complain.ResultDetail;
        info.EffectiveDate = complain.EffectiveDate;
        info.EffectiveBy = complain.EffectiveBy;
        info.ComplainPaperID = complain.ComplainPaperID;
        info.LastUpdateDate = complain.LastUpdateDate;
        info.LastUpdateBy = complain.LastUpdateBy;
        info.ReferenceID = complain.ReferenceID;
        info.TaskType = complain.TaskType;
        info.RequestEmployeeLevelPath = complain.RequestEmployeeLevelPath;
        info.EffectiveEmployeeLevelPath = complain.EffectiveEmployeeLevelPath;

        return info;
    }

}
