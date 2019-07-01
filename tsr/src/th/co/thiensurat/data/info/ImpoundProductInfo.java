package th.co.thiensurat.data.info;

import java.io.Serializable;
import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ImpoundProductInfo extends BHParcelable implements Serializable{

	// <ImpoundProductID>string</ImpoundProductID>
	// <OrganizationCode>string</OrganizationCode>
	// <RefNo>string</RefNo>
	// <Status>string</Status>
	// <RequestProblemID>string</RequestProblemID>
	// <RequestDetail>string</RequestDetail>
	// <RequestDate>dateTime</RequestDate>
	// <RequestBy>string</RequestBy>
	// <RequestTeamCode>string</RequestTeamCode>
	// <ApproveDetail>string</ApproveDetail>
	// <ApprovedDate>dateTime</ApprovedDate>
	// <ApprovedBy>string</ApprovedBy>
	// <ResultProblemID>string</ResultProblemID>
	// <ResultDetail>string</ResultDetail>
	// <EffectiveDate>dateTime</EffectiveDate>
	// <EffectiveBy>string</EffectiveBy>
	// <ImpoundProductPaperID>string</ImpoundProductPaperID>
	// <CreateDate>dateTime</CreateDate>
	// <CreateBy>string</CreateBy>
	// <LastUpdateDate>dateTime</LastUpdateDate>
	// <LastUpdateBy>string</LastUpdateBy>
	// <CONTNO>string</CONTNO>
	// <CustomerID>string</CustomerID>
	// <TotalPrice>float</TotalPrice>
	// <InstallDate>dateTime</InstallDate>
	// <CustomerFullName>string</CustomerFullName>
	// <CompanyName>string</CompanyName>
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

	// Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
	public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
	public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)


	public String CONTNO;
	public String CustomerID;
	public float TotalPrice;
	public Date InstallDate;
	public String CustomerFullName;
	public String CompanyName;
	public String IDCard;
	public String SaleTeamCode;

	public String ProductName;
	public String ProductSerialNumber;
	public String EffectiveName;
	public String RequestName;

	//Contract
	public String SaleEmployeeCode;
	public boolean IsMigrate;

	//-- Fixed [BHPROJ-0026-978] :: Display detail of Sale
	public String SaleCode;
	public String SaleEmployeeName;
	public String TeamHeadName;


//	public enum ImpoundProductStatus {
//		REQUEST, APPROVED, COMPLETED
//	}
}
