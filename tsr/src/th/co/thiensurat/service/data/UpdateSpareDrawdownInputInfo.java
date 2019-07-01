package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.SpareDrawdownInfo;

public class UpdateSpareDrawdownInputInfo extends BHParcelable {

    public String SpareDrawdownID;          // GUID ของการขอเบิกเครื่อง/อะไหล่
    public String OrganizationCode;
    public String Status;                   // สถานะคำร้อง (REQUEST=คำร้องขอรออนุมัติ, APPROVED=คำร้องขอที่ถูกอนุมัติแล้วแต่รอดำเนินการ, COMPLETED=คำร้องขอที่ดำเนินการเรียบร้อยแล้ว,REJECT=คำร้องที่ไม่ผ่านการอนุมัติให้ดำเนินการ)
    public Date RequestDate;                // วันที่ทำการร้องขอ
    public String RequestBy;                // ผู้ที่ทำการร้องขอ (EmpID)
    public String RequestTeamCode;          // รหัสทีมของผู้ที่ทำการร้องขอ
    public String RequestDetail;            // หมายเหตุที่ขอเบิกเครื่อง/อะไหล่
    public Date ApprovedDate;               // วันที่ทำการอนุมัติ
    public String ApprovedBy;               // ผู้ที่ทำการอนุมัติ (EmpID)
    public String ApproveDetail;            // หมายเหตุการขออนุมัติ
    public Date EffectiveDate;              // วันที่ดำเนินการตามคำร้องขอ/วันที่มีผล ในที่นี้คือ วันที่พนักงานเข้ามา Print ใบเบิกเครื่อง/อะไหล่ (ครั้งแรก)
    public String EffectiveBy;              // ผู้ที่ดำเนินการตามคำร้องขอ (EmpID) ในที่นี้คือ EmpID ของพนักงานที่เข้ามา Print ใบเบิกเครื่อง/อะไหล่
    public String EffectiveDetail;          // หมายเหตุที่ดำเนินการ
    public String SpareDrawdownPaperID;     // เลขที่เอกสารใบขอเบิกเครื่อง/อะไหล่
    public int PrintCount;               // จำนวนครั้งที่มีการ Print เอกสารใบขอเบิกเครื่อง/อะไหล่
    public Date LastUpdateDate;
    public String LastUpdateBy;

    public static UpdateSpareDrawdownInputInfo from(SpareDrawdownInfo spareDrawdownInfo) {
        UpdateSpareDrawdownInputInfo info = new UpdateSpareDrawdownInputInfo();

        info.SpareDrawdownID = spareDrawdownInfo.SpareDrawdownID;
        info.OrganizationCode = spareDrawdownInfo.OrganizationCode;
        info.Status = spareDrawdownInfo.Status;
        info.RequestDate = spareDrawdownInfo.RequestDate;
        info.RequestBy = spareDrawdownInfo.RequestBy;
        info.RequestTeamCode = spareDrawdownInfo.RequestTeamCode;
        info.RequestDetail = spareDrawdownInfo.RequestDetail;
        info.ApprovedDate = spareDrawdownInfo.ApprovedDate;
        info.ApprovedBy = spareDrawdownInfo.ApprovedBy;
        info.ApproveDetail = spareDrawdownInfo.ApproveDetail;
        info.EffectiveDate = spareDrawdownInfo.EffectiveDate;
        info.EffectiveBy = spareDrawdownInfo.EffectiveBy;
        info.EffectiveDetail = spareDrawdownInfo.EffectiveDetail;
        info.SpareDrawdownPaperID = spareDrawdownInfo.SpareDrawdownPaperID;
        info.PrintCount = spareDrawdownInfo.PrintCount;
        info.LastUpdateDate = spareDrawdownInfo.LastUpdateDate;
        info.LastUpdateBy = spareDrawdownInfo.LastUpdateBy;

        return info;
    }
}