package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ChangeProductInfo;

public class ActionChangeProductInputInfo extends BHParcelable{

	public String ChangeProductID;
	public String OrganizationCode;
	public String RefNo; //เลขที่สัญญา
	public String OldProductSerialNumber; //หมายเลขเครื่อง
	public String NewProductSerialNumber; //หมายเลขเครื่องใหม่
	public String Status;
	public String RequestProblemID; //สาเหตุการเปลี่ยน
	public String RequestDetail; //เพิ่มเติม
	public Date RequestDate; //วันที่เปลี่ยน
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

	// Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
	public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
	public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

	public static ActionChangeProductInputInfo from(ChangeProductInfo changeProduct) {
		ActionChangeProductInputInfo info = new ActionChangeProductInputInfo();
		
		info.ChangeProductID = changeProduct.ChangeProductID;
		info.OrganizationCode = changeProduct.OrganizationCode;
		info.RefNo = changeProduct.RefNo;
		info.OldProductSerialNumber = changeProduct.OldProductSerialNumber;
		info.NewProductSerialNumber = changeProduct.NewProductSerialNumber;
		info.Status = changeProduct.Status;
		info.RequestProblemID = changeProduct.RequestProblemID;
		info.RequestDetail = changeProduct.RequestDetail;
		info.RequestDate = changeProduct.RequestDate;
		info.RequestBy = changeProduct.RequestBy;
		info.RequestTeamCode = changeProduct.RequestTeamCode;
		info.ApproveDetail = changeProduct.ApproveDetail;
		info.ApprovedDate = changeProduct.ApprovedDate;
		info.ApprovedBy = changeProduct.ApprovedBy;
		info.ResultProblemID = changeProduct.ResultProblemID;
		info.ResultDetail = changeProduct.ResultDetail;
		info.EffectiveDate = changeProduct.EffectiveDate;
		info.EffectiveBy = changeProduct.EffectiveBy;
		info.ChangeProductPaperID = changeProduct.ChangeProductPaperID;
		info.CreateDate = changeProduct.CreateDate;
		info.CreateBy = changeProduct.CreateBy;
		info.LastUpdateDate = changeProduct.LastUpdateDate;
		info.LastUpdateBy = changeProduct.LastUpdateBy;
		info.RequestEmployeeLevelPath = changeProduct.RequestEmployeeLevelPath;
		info.EffectiveEmployeeLevelPath = changeProduct.EffectiveEmployeeLevelPath;

		return info;
	}
}
