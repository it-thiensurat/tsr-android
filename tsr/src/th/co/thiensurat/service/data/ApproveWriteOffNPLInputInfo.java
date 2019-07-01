package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.WriteOffNPLInfo;

public class ApproveWriteOffNPLInputInfo extends BHParcelable {

//	public WriteOffNPLInfo WriteOffNPLInfo;
	
	public String WriteOffNPLID;
	public String OrganizationCode;
	public String RefNo;
	public String Status;
	public String RequestProblemID;
	public String RequestDetail;
	public Date RequestDate;
	public String RequestBy;
	public String RequestTeamCode;
	public String ApproveDetail;
	public Date ApprovedDate;
	public String ApprovedBy;
	public String ResultProblemID;
	public String ResultDetail;
	public Date EffectiveDate;
	public String EffectiveBy;
	public String WriteOffNPLPaperID;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;

	// Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
	public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
	public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

	public static ApproveWriteOffNPLInputInfo from(WriteOffNPLInfo writeOffNPL) {
		ApproveWriteOffNPLInputInfo info = new ApproveWriteOffNPLInputInfo();
		info.WriteOffNPLID = writeOffNPL.WriteOffNPLID;
		info.OrganizationCode = writeOffNPL.OrganizationCode;
		info.RefNo = writeOffNPL.RefNo;
		info.Status = writeOffNPL.Status;
		info.RequestProblemID = writeOffNPL.RequestProblemID;
		info.RequestDetail = writeOffNPL.RequestDetail;
		info.RequestDate = writeOffNPL.RequestDate;
		info.RequestBy = writeOffNPL.RequestBy;
		info.RequestTeamCode = writeOffNPL.RequestTeamCode;
		info.ApproveDetail = writeOffNPL.ApproveDetail;
		info.ApprovedDate = writeOffNPL.ApprovedDate;
		info.ApprovedBy = writeOffNPL.ApprovedBy;
		info.ResultProblemID = writeOffNPL.RequestProblemID;
		info.ResultDetail = writeOffNPL.ResultDetail;
		info.EffectiveDate = writeOffNPL.EffectiveDate;
		info.EffectiveBy = writeOffNPL.EffectiveBy;
		info.WriteOffNPLPaperID = writeOffNPL.WriteOffNPLPaperID;
		info.CreateDate = writeOffNPL.CreateDate;
		info.CreateBy = writeOffNPL.CreateBy;
		info.LastUpdateDate = writeOffNPL.LastUpdateDate;
		info.LastUpdateBy = writeOffNPL.LastUpdateBy;
		info.SyncedDate = writeOffNPL.SyncedDate;
		info.RequestEmployeeLevelPath = writeOffNPL.RequestEmployeeLevelPath;
		info.EffectiveEmployeeLevelPath = writeOffNPL.EffectiveEmployeeLevelPath;
		return info;
	}
}
