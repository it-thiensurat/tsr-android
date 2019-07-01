package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ChangePartSpareInfo;

public class UpdateChangePartSpareInputInfo extends BHParcelable {

    public String ChangePartSpareID;
    public String OrganizationCode;
    public String RefNo; 					//เลขที่สัญญา
    public String Status;          			// สถานะคำร้อง (REQUEST=คำร้องขอรออนุมัติ, APPROVED=คำร้องขอที่ถูกอนุมัติแล้วแต่รอดำเนินการ, COMPLETED=คำร้องขอที่ดำเนินการเรียบร้อยแล้ว,REJECT=คำร้องที่ไม่ผ่านการอนุมัติให้ดำเนินการ)
    public Date RequestDate;      			// วันที่ทำการร้องขอ
    public String RequestBy;       			// ผู้ที่ทำการร้องขอ (EmpID)
    public String RequestTeamCode;    		// รหัสทีมของผู้ที่ทำการร้องขอ
    public String RequestProblemDetail; 	// หมายเหตุที่ขอเบิกเครื่อง/อะไหล่
    public Date ApprovedDate;     			// วันที่ทำการอนุมัติ
    public String ApprovedBy;       		// ผู้ที่ทำการอนุมัติ (EmpID)
    public String ApproveDetail;      		// หมายเหตุการขออนุมัติ
    public Date EffectiveDate;       		// วันที่ดำเนินการตามคำร้องขอ/วันที่มีผล ในที่นี้คือ วันที่พนักงานเข้ามา Print ใบเบิกเครื่อง/อะไหล่ (ครั้งแรก)
    public String EffectiveBy;          	// ผู้ที่ดำเนินการตามคำร้องขอ (EmpID) ในที่นี้คือ EmpID ของพนักงานที่เข้ามา Print ใบเบิกเครื่อง/อะไหล่
    public String EffectiveTeamCode;    	// รหัสทีมของผู้ที่ดำเนินการตามคำร้องขอ ในที่นี้คือ รหัสทีมของพนักงานเข้ามา Print ใบเบิกเครื่อง/อะไหล่
    public String EffectiveDetail;      	// หมายเหตุที่ดำเนินการ
    public String ChangePartSparePaperID;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    public static UpdateChangePartSpareInputInfo from(ChangePartSpareInfo info) {
        UpdateChangePartSpareInputInfo wsInfo = new UpdateChangePartSpareInputInfo();
        wsInfo.ChangePartSpareID = info.ChangePartSpareID;
        wsInfo.OrganizationCode = info.OrganizationCode;
        wsInfo.RefNo = info.RefNo;
        wsInfo.Status = info.Status;
        wsInfo.RequestDate = info.RequestDate;
        wsInfo.RequestBy = info.RequestBy;
        wsInfo.RequestTeamCode = info.RequestTeamCode;
        wsInfo.RequestProblemDetail = info.RequestProblemDetail;
        wsInfo.ApprovedDate = info.ApprovedDate;
        wsInfo.ApprovedBy = info.ApprovedBy;
        wsInfo.ApproveDetail = info.ApproveDetail;
        wsInfo.EffectiveDate = info.EffectiveDate;
        wsInfo.EffectiveBy = info.EffectiveBy;
        wsInfo.EffectiveTeamCode = info.EffectiveTeamCode;
        wsInfo.EffectiveDetail = info.EffectiveDetail;
        wsInfo.ChangePartSparePaperID = info.ChangePartSparePaperID;
        wsInfo.CreateDate = info.CreateDate;
        wsInfo.CreateBy = info.CreateBy;
        wsInfo.LastUpdateDate = info.LastUpdateDate;
        wsInfo.LastUpdateBy = info.LastUpdateBy;
        wsInfo.SyncedDate = info.SyncedDate;
        return wsInfo;
    }

}
