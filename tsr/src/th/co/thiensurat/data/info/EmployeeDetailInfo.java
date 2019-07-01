package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class EmployeeDetailInfo extends BHParcelable {

	/** DB-Field **/
	public String EmployeeDetailID;
	public String EmployeeCode; 
	public String ParentEmployeeCode;
	public String EmployeeTypeCode;
	public String OrganizationCode;
	public String BranchCode;
	public String DepartmentCode;
	public String SubDepartmentCode;
	public String TeamCode;
	public String SubTeamCode;
	public String PositionCode;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;
	//public String SupervisorCode;//No use
	public String SupervisorHeadCode;
    public String SourceSystem;

	// Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
	public String TreeHistoryID;


	/*** [START] :: [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย ***/
	/** Extra-Field **/
//    public String FirstName;
//    public String LastName;
//	public String EmployeeName;
    public String SaleCode;
//	public String TeamName;
//	public String SubTeamName;
//	public String PositionName;

	public String ParentEmployeeDetailID;

	public String TeamHeadCode;
//	public String TeamHeadName;

	public String SubTeamHeadCode;
//	public String SubTeamHeadName;

	public String SupervisorCode;
//	public String SupervisorHeadName;
	public String SupervisorHeadPositionCode;
	public String SupervisorHeadPositionName;

	public String SubDepartmentHeadCode;
//	public String SubDepartmentHeadName;
	public String SubDepartmentHeadPositionCode;
	public String SubDepartmentHeadPositionName;

	public String DepartmentHeadCode;
//	public String DepartmentHeadName;
	public String DepartmentHeadPositionCode;
	public String DepartmentHeadPositionName;

//	public String SourceSystemName;
	public String Status;

	public String ProcessType; // ฝ่ายงานที่เป็นเจ้าของข้อมูล :- อ่านจากตาราง OrganizeUnitProcess ที่ ProcessName = 'SubSystem'
	public String FirstName;
	public String LastName;
	public String EmployeeName;
	public String PositionName;
	public String SubTeamName;
	public String SubTeamHeadName;
	public String TeamName;
	public String TeamHeadName;
	public String SupervisorName;
	public String SupervisorHeadName;
	public String SubDepartmentName;
	public String SubDepartmentHeadName;
	public String DepartmentName;
	public String DepartmentHeadName;
	public String OrganizationName;
	public String OrganizationHeadName;
	public String SourceSystemName;
	/*** [END] :: [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย ***/


	//ReportDailySummarySaleByProductOnlineFragment
	public int SaleSumTotal;               // รวมจำนวนการขาย (ทั้งหมด)
	public float SaleSumMoney;             // รวมยอดเงินการขาย (ทั้งหมด)
	public Date ReportDate;
	
}
