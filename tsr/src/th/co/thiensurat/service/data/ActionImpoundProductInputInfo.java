package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ImpoundProductInfo;

public class ActionImpoundProductInputInfo extends BHParcelable {

//	public ImpoundProductInfo ImpoundProductInfo;

	public String ImpoundProductID;
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
	public String ImpoundProductPaperID;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public String CONTNO;
	public String CustomerID;
	public float TotalPrice;
	public Date InstallDate;
	public String CustomerFullName;
	public String CompanyName;

	// Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
	public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
	public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

	
	
//	public String ProductName;
//	public String ProductSerialNumber;

//	public enum ImpoundProductStatus {
//		REQUEST, APPROVED, COMPLETED
//	}

	
	public static ActionImpoundProductInputInfo from(ImpoundProductInfo impoundProduct) {
		ActionImpoundProductInputInfo info = new ActionImpoundProductInputInfo();
		
		info.ImpoundProductID = impoundProduct.ImpoundProductID;
		info.OrganizationCode = impoundProduct.OrganizationCode;
		info.RefNo = impoundProduct.RefNo;
		info.Status = impoundProduct.Status;
		info.RequestProblemID = impoundProduct.RequestProblemID;
		info.RequestDetail = impoundProduct.RequestDetail;
		info.RequestDate = impoundProduct.RequestDate;
		info.RequestBy = impoundProduct.RequestBy;
		info.RequestTeamCode = impoundProduct.RequestTeamCode;
		info.ApproveDetail = impoundProduct.ApproveDetail;
		info.ApprovedDate = impoundProduct.ApprovedDate;
		info.ApprovedBy = impoundProduct.ApprovedBy;
//        info.ResultProblemID = impoundProduct.RequestProblemID;
//		  info.ResultDetail = impoundProduct.RequestDetail;
		info.ResultProblemID = impoundProduct.ResultProblemID;
		info.ResultDetail = impoundProduct.ResultDetail;
		info.EffectiveDate = impoundProduct.EffectiveDate;
		info.EffectiveBy = impoundProduct.EffectiveBy;
		info.ImpoundProductPaperID = impoundProduct.ImpoundProductPaperID;
		info.CreateDate = impoundProduct.CreateDate;
		info.CreateBy = impoundProduct.CreateBy;
		info.LastUpdateDate = impoundProduct.LastUpdateDate;
		info.LastUpdateBy = impoundProduct.LastUpdateBy;
		info.CONTNO = impoundProduct.CONTNO;
		info.CustomerID = impoundProduct.CustomerID;
		info.TotalPrice = impoundProduct.TotalPrice;
		info.InstallDate = impoundProduct.InstallDate;
		info.CustomerFullName = impoundProduct.CustomerFullName;
		info.CompanyName = impoundProduct.CompanyName;
//		info.ProductName = impoundProduct.ProductName;
//		info.ProductSerialNumber = impoundProduct.ProductSerialNumber;
		info.RequestEmployeeLevelPath = impoundProduct.RequestEmployeeLevelPath;
		info.EffectiveEmployeeLevelPath = impoundProduct.EffectiveEmployeeLevelPath;

		return info;
	}
}
