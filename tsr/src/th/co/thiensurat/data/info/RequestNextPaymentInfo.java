package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class RequestNextPaymentInfo extends BHParcelable {

    public String RequestNextPaymentID;
    public String OrganizationCode;
    public String RefNo;
    public String PaymentID;
    public String Status;
    public String RequestProblemDetail;
    public Date RequestDate;
    public String RequestBy;
    public String RequestTeamCode;
    public Date ApprovedDate;
    public String ApproveDetail;
    public String ApprovedBy;
    public String ResultProblemDetail;
    public Date EffectiveDate;
    public String EffectiveBy;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    // Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
    public String RequestEmployeeLevelPath;    // โครงสร้างพนักงานที่ทำการขอ Request รายการร้องขอ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Request)
    public String EffectiveEmployeeLevelPath;  // โครงสร้างพนักงานที่ทำ  Action รายการร้องขอนั้น ๆ (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ Action)

    //Contract
    public String CONTNO;
    public String ProductSerialNumber;
    public String ContractRefNo;
    public Date EFFDATE;
    public float SALES;
    public float TradeInDiscount;
    public float TotalPrice;
    public float PaidAmount;
    public int MODE;
    public int NextPaymentPeriodNumber; // คงเหลืองวดที่ ถึง c.MODE
    public float OutstandingTotal; // คำนวณจำนวนเงินจากคงเหลืองวดที่ ถึง c.MODE
    public String ContractOrganizationCode; // คำนวณจำนวนเงินจากคงเหลืองวดที่ ถึง c.MODE

    // Product
    public String ProductName;

    // Customer
    public String CustomerFullName;
    public String IDCard;

    //Assign
    public String AssignID;
    public String AssigneeTeamCode;
    public String AssigneeEmpID;
    public String AssigneeDefaultAssigneeEmpID;

    // SalePaymentPeriod
    public int PaymentPeriodNumber;
    public float OutStandingAmount; // ค้างชำระเท่าไหร่ของงวดนั้นๆ
    public int HoldSalePaymentPeriod; // ค้างกี่เดือน

    public String ContractStatus;
}
