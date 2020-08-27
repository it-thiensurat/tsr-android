package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ContractInfo;

public class UpdateContractInputInfo extends BHParcelable {

    public String RefNo;
    public String CONTNO;
    public String CustomerID;
    public String OrganizationCode;
    public String STATUS; // สถานะสัญญา (จากระบบเดิม :- F=ชำระครบ, N=ผ่อนปกติ, R=หนี้สูญ, T=ถูกถอดเครื่อง)
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
    public String SaleEmployeeLevelPath; // โครงสร้างพนักงานขาย
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
    public boolean IsReadyForSaleAudit;   //-- Fixed - [BHPROJ-0025-742] :: รายการสัญญาที่จะนำไปตรวจสอบได้ ต้องผ่านการอนุมัติเพื่อนำไปตรวจสอบเสมอ
    public String ContractReferenceNo;  // Fixed - [BHPROJ-0016-993] :: เพิ่ม Field เพื่อเก็บค่า RefNo ของข้อมูลที่มาจากระบบเก่าตอนทำ Migrate และยังเก็บค่าเลขที่อ้างอิง/เลขที่เอกสารสัญญามือที่เกิดคู่กับสัญญาฉบับนั้น ๆ ในกรณีที่เป็นสัญญาที่เกิดจากระบบ TSR Application
    public boolean IsMigrate;           // Fixed - [BHPROJ-0026-1037] :: [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา] กรณีเป็นสัญญา Migrate ให้มีปุ่ม เพื่อข้ามการ scan สินค้าได้

    public static UpdateContractInputInfo from(ContractInfo contract) {
        UpdateContractInputInfo info = new UpdateContractInputInfo();
        info.RefNo = contract.RefNo;
        info.CONTNO = contract.CONTNO;
       // info.CONTNO = "1234";
        info.CustomerID = contract.CustomerID;
        info.OrganizationCode = contract.OrganizationCode;
        info.STATUS = contract.STATUS;
        info.StatusCode = contract.StatusCode;
        info.SALES = contract.SALES;
        info.TotalPrice = contract.TotalPrice;
        info.EFFDATE = contract.EFFDATE;
        info.HasTradeIn = contract.HasTradeIn;
        info.TradeInProductCode = contract.TradeInProductCode;
        info.TradeInBrandCode = contract.TradeInBrandCode;
        info.TradeInProductModel = contract.TradeInProductModel;
        info.TradeInDiscount = contract.TradeInDiscount;
        info.PreSaleSaleCode = contract.PreSaleSaleCode;
        info.PreSaleEmployeeCode = contract.PreSaleEmployeeCode;
        info.PreSaleEmployeeName = contract.PreSaleEmployeeName;
        info.PreSaleTeamCode = contract.PreSaleTeamCode;
        info.SaleCode = contract.SaleCode;
        info.SaleEmployeeCode = contract.SaleEmployeeCode;
        info.SaleTeamCode = contract.SaleTeamCode;
        info.InstallerSaleCode = contract.InstallerSaleCode;
        info.InstallerEmployeeCode = contract.InstallerEmployeeCode;
        info.InstallerTeamCode = contract.InstallerTeamCode;
        info.InstallDate = contract.InstallDate;
        info.ProductSerialNumber = contract.ProductSerialNumber;
        info.ProductID = contract.ProductID;
        info.SaleEmployeeLevelPath = contract.SaleEmployeeLevelPath;
        info.MODE = contract.MODE;
        info.FortnightID = contract.FortnightID;
        info.ProblemID = contract.ProblemID;
        info.svcontno = contract.svcontno;
        info.isActive = contract.isActive;
        info.MODEL = contract.MODEL;
        info.fromrefno = contract.fromrefno;
        info.fromcontno = contract.fromcontno;
        info.todate = contract.todate;
        info.tocontno = contract.tocontno;
        info.torefno = contract.torefno;
        info.CreateDate = contract.CreateDate;
        info.CreateBy = contract.CreateBy;
        info.LastUpdateDate = contract.LastUpdateDate;
        info.LastUpdateBy = contract.LastUpdateBy;
        info.SyncedDate = contract.SyncedDate;
        info.PreSaleEmployeeLevelPath = contract.PreSaleEmployeeLevelPath;
        info.InstallerEmployeeLevelPath = contract.InstallerEmployeeLevelPath;
        info.IsReadyForSaleAudit = contract.IsReadyForSaleAudit;        //-- Fixed - [BHPROJ-0025-742] :: รายการสัญญาที่จะนำไปตรวจสอบได้ ต้องผ่านการอนุมัติเพื่อนำไปตรวจสอบเสมอ
        info.ContractReferenceNo = contract.ContractReferenceNo;
        info.IsMigrate = contract.IsMigrate;

        return info;
    }

}
