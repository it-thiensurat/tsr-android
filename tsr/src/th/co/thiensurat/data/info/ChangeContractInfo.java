package th.co.thiensurat.data.info;

import java.io.Serializable;
import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ChangeContractInfo extends BHParcelable implements Serializable{
	public String ChangeContractID;
	public String OrganizationCode;
	public String RefNo;
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

    // Contract
    public Date InstallDate;
    public String ProductSerialNumber;
    public String CustomerID;
    public String CONTNO;
	public boolean IsMigrate;

    // Contract Status
    public String StatusName;

    // Customer
    public String CustomerFullName;
    public String CompanyName;
    public String IDCard;

    // Product
    public String ProductName;

    // Sale
    public String SaleEmployeeName;

	// Problem
	public String ProblemName;

	// Employee
	public String EffectiveByEmployeeName;
	public String EffectiveBySaleCode;
	public String EffectiveByUpperEmployeeName;
	public String EffectiveBySaleTeamName;

}
