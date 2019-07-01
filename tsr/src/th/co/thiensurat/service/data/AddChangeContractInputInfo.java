package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ChangeContractInfo;

public class AddChangeContractInputInfo extends BHParcelable {

	public String ChangeContractID;
	public String OrganizationCode;
	public String OldSaleID;
	public String NewSaleID;
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
	public String ChangeContractPaperID;
	public String CreateBy;
	public Date CreateDate;
	public Date LastUpdateDate;
	public String LastUpdateBy;

	// Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
	public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
	public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

	public static AddChangeContractInputInfo from(ChangeContractInfo changeContract) {
		AddChangeContractInputInfo info = new AddChangeContractInputInfo();
		
		info.ChangeContractID = changeContract.ChangeContractID;
		info.OrganizationCode = changeContract.OrganizationCode;
		info.OldSaleID = changeContract.OldSaleID;
		info.NewSaleID = changeContract.NewSaleID;
		info.Status = changeContract.Status;
		info.RequestProblemID = changeContract.RequestProblemID;
		info.RequestDetail = changeContract.RequestDetail;
		info.RequestDate = changeContract.RequestDate;
		info.RequestBy = changeContract.RequestBy;
		info.RequestTeamCode = changeContract.RequestTeamCode;
		info.ApproveDetail = changeContract.ApproveDetail;
		info.ApprovedDate = changeContract.ApprovedDate;
		info.ApprovedBy = changeContract.ApprovedBy;
		info.ResultProblemID = changeContract.RequestProblemID;
		info.ResultDetail = changeContract.RequestDetail;
		info.EffectiveDate = changeContract.EffectiveDate;
		info.EffectiveBy = changeContract.EffectiveBy;
		info.ChangeContractPaperID = changeContract.ChangeContractPaperID;
		info.CreateBy = changeContract.CreateBy;
		info.CreateDate = changeContract.CreateDate;
		info.LastUpdateDate = changeContract.LastUpdateDate;
		info.LastUpdateBy = changeContract.LastUpdateBy;
		info.RequestEmployeeLevelPath = changeContract.RequestEmployeeLevelPath;
		info.EffectiveEmployeeLevelPath = changeContract.EffectiveEmployeeLevelPath;

		return info;
	}
}
