package th.co.thiensurat.data.info;

import java.io.Serializable;
import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ContractInfo extends BHParcelable implements Serializable{

    // DB-Field
    public String RefNo;
    public String CONTNO;
    public String CustomerID;
    public String OrganizationCode;
    public String STATUS; // สถานะสัญญา (จากระบบเดิม :- F=ชำระครบ, N=ผ่อนปกติ,
    // R=หนี้สูญ, T=ถูกถอดเครื่อง)
    public String StatusCode; // สถานะสัญญา
    public float SALES; // ราคาเงินสด
    public float TotalPrice; // ราคารวม (ราคาสุทธิ)
    public Date EFFDATE; // วันที่ทำสัญญา
    public int HasTradeIn;
    public String TradeInProductCode;
    public String TradeInBrandCode;
    public String TradeInProductModel;
    public float TradeInDiscount; // ส่วนลดเครื่องเทิร์น
    public String PreSaleSaleCode; // SaleCode ของพนักงาน Pre-Sale
    public String PreSaleEmployeeCode; // รหัสพนักงานผู้แนะนำ
    public String PreSaleEmployeeName; // ชื่อ-นามสกุลผู้แนะนำ
    public String PreSaleTeamCode; // รหัสทีมงานของพนักงาน Pre-Sale
    public String SaleCode; // SaleCode ของพนักงานขาย
    public String SaleEmployeeCode; // รหัสพนักงานขาย
    public String SaleTeamCode; // รหัสทีมงานของพนักงานขาย
    public String InstallerSaleCode; // SaleCode ของพนักงานติดตั้งเครื่อง
    public String InstallerEmployeeCode; // รหัสพนักงานติดตั้งเครื่อง
    public String InstallerTeamCode; // รหัสทีมงานของพนักงานติดตั้งเครื่อง
    public Date InstallDate; // วันที่ติดตั้ง
    public String ProductSerialNumber;
    public String ProductID;
    public String SaleEmployeeLevelPath; // โครงสร้างพนักงานที่ทำการขาย (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ)
    public int MODE; // จำนวนงวด (Mode)
    public String FortnightID; // ปักษ์งานขาย
    public String ProblemID; // ปัญหาของสัญญา
    public String svcontno; // รหัสสัญญาสารกรอง (อาจจะเพิ่ม table RelateContract)
    public boolean isActive; // สถานะการ Active ของสัญญา
    public String MODEL; // รหัส Package
    public String fromrefno; // เปลี่ยนสัญญามาจากเลขที่อ้างอิง
    public String fromcontno; // เปลี่ยนจากสัญญาเลขที่
    public Date todate; // วันที่ทำสัญญาใหม่
    public String tocontno; // เลขที่สัญญาใหม่
    public String torefno; // เลขที่อ้างอิงใหม่
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;
    public String PreSaleEmployeeLevelPath;
    public String InstallerEmployeeLevelPath;
    public String SaleSubTeamCode;
    public String TradeInBrandName;
    public boolean TradeInReturnFlag;
    public boolean IsReadyForSaleAudit; //-- Fixed - [BHPROJ-0025-742] :: รายการสัญญาที่จะนำไปตรวจสอบได้ ต้องผ่านการอนุมัติเพื่อนำไปตรวจสอบเสมอ
    public String ContractReferenceNo;  // Fixed - [BHPROJ-0016-993] :: เพิ่ม Field เพื่อเก็บค่า RefNo ของข้อมูลที่มาจากระบบเก่าตอนทำ Migrate และยังเก็บค่าเลขที่อ้างอิง/เลขที่เอกสารสัญญามือที่เกิดคู่กับสัญญาฉบับนั้น ๆ ในกรณีที่เป็นสัญญาที่เกิดจากระบบ TSR Application
    public boolean IsMigrate;           // Fixed - [BHPROJ-0026-1037] :: [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา] กรณีเป็นสัญญา Migrate ให้มีปุ่ม เพื่อข้ามการ scan สินค้าได้

    public String SPECCODE;
    // Status
    public String StatusName;

    // Product
    public String ProductName;

    // Sale
    public String SaleEmployeeName;
    public String SaleTeamName;
    public String upperEmployeeID;
    public String upperEmployeeName;
    public String SaleName;

    // Customer
    public String CustomerType;
    public String CustomerFullName;
    public String CustomerName;
    public String CompanyName;
    public String IDCard;

    // SalePaymentPeroid
    public float PaymentAmount;
    public float NextPaymentAmount;
    public float SummaryPaymentAmount;
    public float OutstandingAmount;

    // หน้า Print ChangeProduct
    public String ProblemName;

    public String EmpID;

    // หน้า ImpoundProduct
    public String Authorization;
    public String SaleLeaderNameOfSaleTeamCode;

    // Credit
    public int PaymentPeriodNumber;
    public int NextPaymentPeriodNumber;
    public float PaidAmount;

    // SaleLeaderName
    public String SaleLeaderName;

    // ContractCloseAccount
    public int HoldSalePaymentPeriod;
    public float CloseDiscountAmount;

    //public float PAYAMT; // จำนวนเงินที่จ่ายในการ Payment ครั้งนี้
    public float TotalOutstandingAmount; // จำนวนเงินที่เหลือทั้งหมดของสัญญา

    public float MinOutStandingAmount; // ค่างวดของงวดที่น้อยที่สุดที่  PaymentComplete = 0
    public int MinPaymentPeriodNumber; // งวดที่น้อยที่สุดที่  PaymentComplete = 0
    public float NextNetAmount; // ค่างวดถัดไปของงวดที่น้อยที่สุดที่  PaymentComplete = 0
    //public int NextPaymentPeriodNumber; // งวดถัดไปของงวดที่น้อยที่สุดที่  PaymentComplete = 0
    public float FirstNetAmount;
    public float SecondNetAmount;
    public float ThirdNetAmount;
    public String MinSalePaymentPeriodID;
    public float PAYAMT;
    public String CustomerFullName2;  //-- Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น

    public enum ContractStatus {
        DRAFT, NORMAL, T, F, R
    }

    public enum ContractStatusName {
        COMPLETED
    }
    //LeadOnLine
    public String leadonlineid;
    public String statuswork;
    public String statuscus;





































}
