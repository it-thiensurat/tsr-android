package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ReportDailySummarySaleByProductInfo extends BHParcelable {
    public String OrganizationCode;
    public Date ReportDate;

    public String SaleLevel01;              // SaleEmployeeCode
    public String SaleLevel01_Name;
    public String SaleLevel02;              // SubTeamHeadCode
    public String SaleLevel02_Name;
    public String SaleLevel03;              // TeamHeadCode
    public String SaleLevel03_Name;
    public String SaleLevel04;              // SupervisorHeadCode
    public String SaleLevel04_Name;
    public String SaleLevel05;              // SubDepartmentHeadCode
    public String SaleLevel05_Name;
    public String SaleLevel06;              // DepartmentHeadCode
    public String SaleLevel06_Name;
    public String SaleLevel07;              // OrganizationHeadCode
    public String SaleLevel07_Name;
    public String SaleLevel08;
    public String SaleLevel08_Name;
    public String SaleLevel09;
    public String SaleLevel09_Name;
    public String SaleLevel10;
    public String SaleLevel10_Name;

    public String ProductSerialNumber;
    public String ProductID;
    public String ProductCode;
    public String ProductModel;
    public String ProductName;

    //-- (การขาย - การเก็บเงินของการขายนั้น ๆ)
    public int SaleSumTotal;               // รวมจำนวนการขาย (ทั้งหมด)
    public float SaleSumMoney;             // รวมยอดเงินการขาย (ทั้งหมด)

    public int CashSumNumber;              // รวมจำนวนขาย-ยอดรวม (ขายสด)
    public float CashSumMoney;             // รวมยอดเงินขาย-ยอดรวม (ขายสด)
    public int CashNumber;                 // รวมจำนวนเก็บเงิน-เก็บ (ขายสด)
    public float CashMoney;                // รวมยอดเงินเก็บเงิน-เก็บ (ขายสด)
    public int CashOutNumber;              // รวมจำนวนค้าง-ค้าง (ขายสด) 
    public float CashOutMoney;             // รวมยอดเงินค้าง-ค้าง (ขายสด)

    public int OutSumNumber;               // รวมจำนวนขาย-ยอดรวม (ขายผ่อน)
    public float OutSumMoney;              // รวมยอดเงินขาย-ยอดรวม (ขายผ่อน)
    public int OutNumber;                  // รวมจำนวนเก็บเงิน-เก็บ (ขายผ่อน)
    public float OutMoney;                 // รวมยอดเงินเก็บเงิน-เก็บ (ขายผ่อน)
    public int OutStandingNumber;          // รวมจำนวนค้าง-ค้าง (ขายผ่อน) 
    public float OutStandingMoney;         // รวมยอดเงินค้าง-ค้าง (ขายผ่อน)


    //public float PaymentSumMoney;          // รวมยอดเงินที่เก็บได้
    //public int ProductTradeIn;             // จำนวนสินค้าเทิร์น
}
