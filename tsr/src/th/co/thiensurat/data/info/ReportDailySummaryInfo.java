package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

/**
 * Created by macmini04 on 25/12/2014.
 */
public class ReportDailySummaryInfo extends BHParcelable {

    public String OrganizationCode;
    public Date ReportDate;

    public String SaleLevel01;          // SaleEmployeeCode
    public String SaleLevel01_Name;
    public String SaleLevel02;          // SubTeamHeadCode
    public String SaleLevel02_Name;
    public String SaleLevel03;          // TeamHeadCode
    public String SaleLevel03_Name;
    public String SaleLevel04;          // SupervisorHeadCode
    public String SaleLevel04_Name;
    public String SaleLevel05;          // SubDepartmentHeadCode
    public String SaleLevel05_Name;
    public String SaleLevel06;          // DepartmentHeadCode
    public String SaleLevel06_Name;
    public String SaleLevel07;
    public String SaleLevel07_Name;
    public String SaleLevel08;
    public String SaleLevel08_Name;
    public String SaleLevel09;
    public String SaleLevel09_Name;
    public String SaleLevel10;
    public String SaleLevel10_Name;

    //-- (การขาย - การเก็บเงินของการขายนั้น ๆ)
    public int CashSumNumber;              // รวมจำนวนขาย (ขายสด)
    public float CashSumMoney;             // รวมยอดเงินขาย (ขายสด)
    public int CashNumber;                 // รวมจำนวนเก็บเงิน (ขายสด)
    public float CashMoney;                // รวมยอดเงินเก็บเงิน (ขายสด)
    public int OutSumNumber;               // รวมจำนวนขาย (ขายผ่อน)
    public float OutSumMoney;              // รวมยอดเงินขาย (ขายผ่อน)
    public int OutNumber;                  // รวมจำนวนเก็บเงิน (ขายผ่อน)
    public float OutMoney;                 // รวมยอดเงินเก็บเงิน (ขายผ่อน)
    public int CashOutNumber;              // รวมจำนวนค้าง (ขายสด) 
    public float CashOutMoney;             // รวมยอดเงินค้าง (ขายสด)
    public int OutStandingNumber;          // รวมจำนวนค้าง (ขายผ่อน) 
    public float OutStandingMoney;         // รวมยอดเงินค้าง (ขายผ่อน)
    public int SaleSumTotal;               // รวมจำนวนการขาย
    public float SaleSumMoney;             // รวมยอดเงินการขาย
    public float PaymentSumMoney;          // รวมยอดเงินที่เก็บได้
    public int ProductTradeIn;             // จำนวนสินค้าเทิร์น

    //-- การนำส่งเงิน
    public float SendMoneyAlready;         // ยอดเงินที่ส่งเงินแล้ว
    public float SendMoneyOut;             // ยอดเงินที่ค้างส่งเงิน

    //-- การร้องขอต่าง ๆ
    public int ImpoundBySale;              // จำนวนรายการถอดเครื่องทีมตัวเอง
    public int ImpoundByTeam;              // จำนวนรายการถอดเครื่องทีมอื่น
    public int ChangeProduct;              // จำนวนรายการเปลี่ยนเครื่อง
    public int ChangeContractBySubTeam;    // จำนวนรายการเปลี่ยนสัญญาโดยหัวหน้าหน่วย
    public int ChangeContractByTeam;       // จำนวนรายการเปลี่ยนสัญญาโดยหัวหน้าทีม
    public int WriteOffNPL;                // จำนวนรายการตัดสัญญาค้าง

    //-- การส่งเอกสาร
    /*** ประเภทเอกสารที่นำส่งเอกสาร (0=สัญญา, 1=ใบเสร็จ, 2=เปลี่ยนเครื่อง, 3=ถอดเครื่อง, 4=เปลี่ยนสัญญา, 5=เอกสารมือ, 6=Slip ธนาคาร, 7=Slip เพย์พอยท์) ***/
    /*** Contract("0"), Receipt("1"), ChangeProduct("2"), ImpoundProduct("3"), ChangeContract("4"), ManualDocument("5"), PayInSlipBank("6"), PayInSlipPayPoint("7") ***/
    public int SendContract;               // จำนวนเอกสารสัญญาที่ต้องนำส่งทั้งหมด
    public int SendReceipt;                // จำนวนเอกสารใบเสร็จที่ต้องนำส่งทั้งหมด
    public int SendChangeProduct;          // จำนวนเอกสารใบเปลี่ยนเครื่องที่ต้องนำส่งทั้งหมด
    public int SendImpound;                // จำนวนเอกสารใบถอดเครื่องที่ต้องนำส่งทั้งหมด
    public int SendChangeContract;         // จำนวนเอกสารใบเปลี่ยนสัญญาที่ต้องนำส่งทั้งหมด
    public int SendManualDocument;         // จำนวนเอกสารมือที่ต้องนำส่งทั้งหมด
    public int SendPayInSlipBank;          // จำนวนเอกสารที่เป็นหลักฐานใบ Pay-in จากทาง ธนาคาร ที่ต้องนำส่งทั้งหมด
    public int SendPayInSlipPayPoint;      // จำนวนเอกสารที่เป็นหลักฐานใบ Pay-in จากทาง เพย์พอยท์ ที่ต้องนำส่งทั้งหมด
    public int TotalSendDocument;          // จำนวนเอกสารทุกประเภทที่ต้องนำส่ง
    public int TotalAlreadySent;           // จำนวนเอกสารที่ถูกนำส่งไปแล้ว 
    public int TotalWaitSent;              // จำนวนเอกสารที่ยังคงค้างนำส่ง


    /*** [START] :: Fixed - [BHPROJ-0024-1047] :: [Android] Report Dashboard ส่วนที่ต้องเพิ่มเติมในรายงานค่ะ ***/
    //-- 1. [Tab] เอกสารนำส่ง เพิ่ม Field ต่อไปนี้ เพื่อใช้ในการแสดงผลเพิ่มเติม
    //---- 1.1. สัญญา
    public int SendContractAlreadySent;	        // จำนวนเอกสารสัญญา ที่ถูกนำส่งไปแล้ว
    public int SendContractWaitSent;	            // จำนวนเอกสารสัญญา ที่ยังคงค้างนำส่ง
    //---- 1.2. ใบเสร็จ
    public int SendReceiptAlreadySent;	            // จำนวนเอกสารใบเสร็จ ที่ถูกนำส่งไปแล้ว
    public int SendReceiptWaitSent;	            // จำนวนเอกสารใบเสร็จ ที่ยังคงค้างนำส่ง
    //---- 1.3. ใบเปลี่ยนเครื่อง
    public int SendChangeProductAlreadySent;	    // จำนวนเอกสารใบเปลี่ยนเครื่อง ที่ถูกนำส่งไปแล้ว
    public int SendChangeProductWaitSent;	        // จำนวนเอกสารใบเปลี่ยนเครื่อง ที่ยังคงค้างนำส่ง
    //---- 1.4. ใบถอดเครื่อง
    public int SendImpoundProductAlreadySent;	    // จำนวนเอกสารใบถอดเครื่อง ที่ถูกนำส่งไปแล้ว
    public int SendImpoundProductWaitSent;	        // จำนวนเอกสารใบถอดเครื่อง ที่ยังคงค้างนำส่ง
    //---- 1.5. ใบเปลี่ยนสัญญา
    public int SendChangeContractAlreadySent;	    // จำนวนเอกสารใบเปลี่ยนสัญญา ที่ถูกนำส่งไปแล้ว
    public int SendChangeContractWaitSent;	        // จำนวนเอกสารใบเปลี่ยนสัญญา ที่ยังคงค้างนำส่ง
    //---- 1.6. เอกสารมือ
    public int SendManualDocumentAlreadySent;	    // จำนวนเอกสารมือ ที่ถูกนำส่งไปแล้ว
    public int SendManualDocumentWaitSent;	        // จำนวนเอกสารมือ ที่ยังคงค้างนำส่ง
    //---- 1.7. Slip ธนาคาร
    public int SendPayInSlipBankAlreadySent;	    // จำนวนเอกสารที่เป็นหลักฐานใบ Pay-in จากทาง ธนาคาร ที่ถูกนำส่งไปแล้ว
    public int SendPayInSlipBankWaitSent;	        // จำนวนเอกสารที่เป็นหลักฐานใบ Pay-in จากทาง ธนาคาร ที่ยังคงค้างนำส่ง
    //---- 1.8. Slip เพย์พอยท์
    public int SendPayInSlipPayPointAlreadySent;   // จำนวนเอกสารที่เป็นหลักฐานใบ Pay-in จากทาง เพย์พอยท์ ที่ถูกนำส่งไปแล้ว
    public int SendPayInSlipPayPointWaitSent;	    // จำนวนเอกสารที่เป็นหลักฐานใบ Pay-in จากทาง เพย์พอยท์ ที่ยังคงค้างนำส่ง

    //-- 2. [Tab] เปลี่ยน-ตัดสัญญาค้าง ใช้เงื่อนไข + Field เดิมในการแสดงผล สำหรับงานขาย พร้อมทั้งเพิ่มเติม Field สำหรับงานเก็บเงิน ตามรายละเอียดด้านล่าง
    //---- 2.1. สำหรับงานขาย ==> อันนี้เหมือนเดิมค่ะ ใช้ ChangeContractBySubTeam - เปลี่ยนสัญญาโดยหัวหน้าหน่วย + ใช้ ChangeContractByTeam - เปลี่ยนสัญญาโดยหัวหน้าทีม
    //---- 2.2. สำหรับงานเก็บเงิน ==> เพิ่ม Field มา 1 Field คือ ใช้ ChangeContractByEmp - เปลี่ยนสัญญาโดยหัวหน้าหน่วย
    public int ChangeContractByEmp;                // จำนวนรายการเปลี่ยนสัญญาโดยพนักงาน

    //-- 3. [Tab] เปลี่ยน-ถอด-คืน เพิ่ม Field ต่อไปนี้ เพื่อใช้ในการแสดงผลเพิ่มเติม
    //---- 3.1. สินค้าเทิร์น
    public int ProductTradeInReturn;	            // จำนวนสินค้าเทิร์น ที่ส่งคืนและรับคืนเข้าระบบแล้ว
    public int ProductTradeInWaitReturn;           // จำนวนสินค้าเทิร์น ที่รอส่งคืน หรือส่งแล้วแต่รอรับคืนเข้าระบบ
    //---- 3.2. ถอดเครื่อง
    public int ImpoundBySaleReturn;	            // จำนวนรายการถอดเครื่อง(ทีมตัวเอง) ที่ส่งคืนและรับคืนเข้าระบบแล้ว [SALE]
    public int ImpoundBySaleWaitReturn;            // จำนวนรายการถอดเครื่อง(ทีมตัวเอง) ที่รอส่งคืน หรือส่งแล้วแต่รอรับคืนเข้าระบบ [SALE]
    public int ImpoundByTeamReturn;	            // จำนวนรายการถอดเครื่อง(ทีมอื่น) ที่ส่งคืนและรับคืนเข้าระบบแล้ว [SALE + CREDIT]
    public int ImpoundByTeamWaitReturn;            // จำนวนรายการถอดเครื่อง(ทีมอื่น) ที่รอส่งคืน หรือส่งแล้วแต่รอรับคืนเข้าระบบ [SALE + CREDIT]
    //---- 3.3. เปลี่ยนเครื่อง
    public int ChangeProductReturn;	            // จำนวนรายการเปลี่ยนเครื่อง ที่ส่งคืนและรับคืนเข้าระบบแล้ว
    public int ChangeProductWaitReturn;            // จำนวนรายการเปลี่ยนเครื่อง ที่รอส่งคืน หรือส่งแล้วแต่รอรับคืนเข้าระบบ
    /*** [END] :: Fixed - [BHPROJ-0024-1047] :: [Android] Report Dashboard ส่วนที่ต้องเพิ่มเติมในรายงานค่ะ ***/

}
