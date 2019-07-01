package th.co.thiensurat.data.info;

import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHParcelable;

public class UserInfo extends BHParcelable {
	public  String UserID;
	public String UserName;
	public String Password;
	public boolean IsActive;
	public String EmpID;
	
	public String RoleCode;
	public String RoleName;
	
	public String OrganizationCode;
//	public String TeamCode;
	
	public String SaleCode;
//	public String CashCode;
	
	public EmployeeDetailInfo EmployeeDetail;
	public String FortnightYear;
	public String PositionCode;
	public String PositionName;
	
	public List<UserInfo> UserPosition;
	public String TeamCode;
	public String SubTeamCode;
	
	public String UserFullName;
	
    public String DepartmentCode;
    public String SubDepartmentCode;
    public String SupervisorCode;
    public String SourceSystem;
	public String SourceSystemName;

	public String ProcessType;		// [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย

	/*** [START] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ ***/
	public String DateFormatGenerateDocument;
	public int RunningNumberReceipt;
	public int RunningNumberChangeContract;
	public int RunningNumberReturnProduct;
	public int RunningNumberImpoundProduct;
	public int RunningNumberChangeProduct;
	public int RunningNumberComplain;
	/*** [END] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ   ***/

	/*** [START] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ ***/
	public String SendMoneyYearFormatGenerate;
	public String SendMoneyReference1FormatGenerate;
	public int RunningNumberReference1;
	public int RunningNumberReference2;
	/*** [END] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ   ***/

	/*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
	public PlatformVersionInfo PlatformVersion;
	/*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/

	public Date CurrentServerDate; //Fixed - [BHPROJ-1036-9259] - พบการเปลี่ยนวันที่บนmobileทำให้สามารถ​แก้ไขสัญญาได้
}
