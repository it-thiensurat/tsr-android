package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.CutOffContractInfo;

public class AddCutOffContractInputInfo extends BHParcelable {
    public String CutOffContractID;
    public String OrganizationCode;
    public String RefNo;
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
    public String CutOffContractPaperID;
    public boolean IsAvailableContract;
    public String AvailableContractDetail;
    public Date AvailableContractDate;
    public String AvailableContractBy;
    public String AvailableContractTeamCode;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;
    public String LastAssigneeEmpID;       // ??????????????????????/???????????????????????????????????
    public String LastAssigneeSaleCode;    // ???? SaleCode ?????????????????/???????????????????????????????????
    public String LastAssigneeTeamCode;    // ????????????????????????/???????????????????????????????????
    public String LastAssignTaskType;      // ???? TaskType ???????????????????? (SaleAudit=????????????????????????????????????? / SalePaymentPeriod=??????????????????????????????????????????????)

    // Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
    public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
    public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

    public static AddCutOffContractInputInfo from(CutOffContractInfo coc) {
        AddCutOffContractInputInfo info = new AddCutOffContractInputInfo();
        info.CutOffContractID = coc.CutOffContractID;
        info.OrganizationCode = coc.OrganizationCode;
        info.RefNo = coc.RefNo;
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
        info.CutOffContractPaperID = coc.CutOffContractPaperID;
        info.IsAvailableContract = coc.IsAvailableContract;
        info.AvailableContractDetail = coc.AvailableContractDetail;
        info.AvailableContractDate = coc.AvailableContractDate;
        info.AvailableContractBy = coc.AvailableContractBy;
        info.AvailableContractTeamCode = coc.AvailableContractTeamCode;
        info.CreateDate = coc.CreateDate;
        info.CreateBy = coc.CreateBy;
        info.LastUpdateDate = coc.LastUpdateDate;
        info.LastUpdateBy = coc.LastUpdateBy;
        info.SyncedDate = coc.SyncedDate;
        info.LastAssigneeEmpID = coc.LastAssigneeEmpID;
        info.LastAssigneeSaleCode = coc.LastAssigneeSaleCode;
        info.LastAssigneeTeamCode = coc.LastAssigneeTeamCode;
        info.LastAssignTaskType = coc.LastAssignTaskType;
        info.RequestEmployeeLevelPath = coc.RequestEmployeeLevelPath;
        info.EffectiveEmployeeLevelPath = coc.EffectiveEmployeeLevelPath;
        return info;
    }
}
