package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.CutOffContractInfo;
import th.co.thiensurat.data.info.LogScanProductSerialInfo;


/*** Fixed - [BHPROJ-0026-1037] :: [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา] กรณีเป็นสัญญา Migrate ให้มีปุ่ม เพื่อข้ามการ scan สินค้าได้ ***/

public class AddLogScanProductSerialInputInfo extends BHParcelable {
    public String LogScanProductSerialID;
    public String OrganizationCode;
    public String TaskType;		// ประเภทของการร้องขอ (ขออนุมัติถอดเครื่อง=Impound, ขออนุมัติเปลี่ยนเครื่อง=ChangeProduct, ขออนุมัติเปลี่ยนสัญญา=ChangeContract)
    public String RequestID;	// GUID การร้องขอ (ขออนุมัติถอดเครื่องใช้-ImpoundProductID, ขออนุมัติเปลี่ยนเครื่องใช้-ChangeProductID, ขออนุมัติเปลี่ยนสัญญาใช้-ChangeContractID)
    public boolean IsScanProductSerial;		// บอกว่ามีการ  Scan Product Serial Number หรือไม่
    public String RefNo;
    public String ProductSerialNumber;		// หมายเลขเครื่อง
    public String Status;		// สถานะคำร้อง (REQUEST=คำร้องขอรออนุมัติ, APPROVED=คำร้องขอที่ถูกอนุมัติแล้วแต่รอดำเนินการ, COMPLETED=คำร้องขอที่ดำเนินการเรียบร้อยแล้ว)
    public Date CreateDate;
    public String CreateBy;
    public Date SyncedDate;

    public static AddLogScanProductSerialInputInfo from(LogScanProductSerialInfo info) {
        AddLogScanProductSerialInputInfo wsInfo = new AddLogScanProductSerialInputInfo();
        wsInfo.LogScanProductSerialID = info.LogScanProductSerialID;
        wsInfo.OrganizationCode = info.OrganizationCode;
        wsInfo.TaskType = info.TaskType;
        wsInfo.RequestID = info.RequestID;
        wsInfo.IsScanProductSerial = info.IsScanProductSerial;
        wsInfo.RefNo = info.RefNo;
        wsInfo.ProductSerialNumber = info.ProductSerialNumber;
        wsInfo.Status = info.Status;
        wsInfo.CreateDate = info.CreateDate;
        wsInfo.CreateBy = info.CreateBy;
        wsInfo.SyncedDate = info.SyncedDate;
        return wsInfo;
    }

}
