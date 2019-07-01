package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ChangeProductInfo;

public class AddChangeProductInputInfo extends BHParcelable{
	public String ChangeProductID;
	public String OrganizationCode;
	public String RefNo; 
	public String OldProductSerialNumber;
	public String NewProductSerialNumber;
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
	public String ChangeProductPaperID;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;

	// Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
	public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
	public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

	public static AddChangeProductInputInfo from(ChangeProductInfo cp) {
		AddChangeProductInputInfo info = new AddChangeProductInputInfo();
		info.ChangeProductID = cp.ChangeProductID;
		info.OrganizationCode = cp.OrganizationCode;
		info.RefNo = cp.RefNo;
		info.OldProductSerialNumber = cp.OldProductSerialNumber;
		info.NewProductSerialNumber = cp.NewProductSerialNumber;
		info.Status = cp.Status;
		info.RequestProblemID = cp.RequestProblemID;
		info.RequestDetail = cp.RequestDetail;
		info.RequestDate = cp.RequestDate;
		info.RequestBy = cp.RequestBy;
		info.RequestTeamCode = cp.RequestTeamCode;
		info.ApprovedDate = cp.ApprovedDate;
		info.ApprovedBy = cp.ApprovedBy;
		info.ResultProblemID = cp.ResultProblemID;
		info.ResultDetail = cp.ResultDetail;
		info.EffectiveDate = cp.EffectiveDate;
		info.EffectiveBy = cp.EffectiveBy;
		info.ChangeProductPaperID = cp.ChangeProductPaperID;
		info.CreateDate = cp.CreateDate;
		info.CreateBy = cp.CreateBy;
		info.LastUpdateDate = cp.LastUpdateDate;
		info.LastUpdateBy = cp.LastUpdateBy;
		info.SyncedDate = cp.SyncedDate;
		info.RequestEmployeeLevelPath = cp.RequestEmployeeLevelPath;
		info.EffectiveEmployeeLevelPath = cp.EffectiveEmployeeLevelPath;

		return info;
	}
}
