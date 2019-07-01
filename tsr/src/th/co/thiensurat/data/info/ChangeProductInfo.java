package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ChangeProductInfo extends BHParcelable {
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
	public Date SyncedDate;

	// Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
	public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
	public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

	public String OldProductID;
	public String NewProductID;
	public String ProductName; //ชื่อสินค้า
	public String ProductSerialNumber;
	public String CustomerName; //ชื่อผู้ซื้อ
	public String Address; //ที่อยู่
	public String TelMobile; //เบอร์โทร
	public String SaleEmployeeName; //พนักงานขาย
	public String upperEmployeeName; //หัวหน้าทีม
	public Date InstallDate; //วันที่ติดตั้ง
	public String empName; //ชื้อผู้เปลี่ยน
	public String CONTNO;
	public String CustomerFullName;
	public String CompanyName;


	public String ContractStatus;
	public boolean IsMigrate;

//	public enum ChangeProductStatus {
//		REQUEST, APPROVED, COMPLETED
//	}
}
