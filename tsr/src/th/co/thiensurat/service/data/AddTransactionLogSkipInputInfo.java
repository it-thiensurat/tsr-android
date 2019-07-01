package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.LogScanProductSerialInfo;
import th.co.thiensurat.data.info.TransactionLogSkipInfo;


/*** Fixed-[BHPROJ-0016-1061] :: [Android-Logout-GCM] กรณีเป็นการ Logout จากการยิง GCM ให้ตรวจสอบก่อนว่า ณ เวลานั้นมีใคร Login ด้วย UserID ของเราด้วย DeviceID อื่นหรือเปล่า ถ้ามีให้ข้ามการทำ TRansactionLogService ของ Android เลย ***/


public class AddTransactionLogSkipInputInfo extends BHParcelable {
    public String TransactionLogSkipID;     // GUID
    public int TransactionID;               // ลำดับ TransactionLog ใน Android
    public String ServiceName;              // ชื่อ Service
    public String ServiceInputName;         // ชื่อ Input Parameter ของ Service นั้น
    public String ServiceInputType;         // ประเภท Object ของ Input Parameter ของ Service นั้น
    public String ServiceOutputType;        // ประเภท Object ของ Output Parameter ของ Service นั้น
    public String ServiceInputData;         // ค่าข้อมูลที่ถูกส่งมาใน Input Parameter แต่ละตัว
    public Date TransactionDate;            // วันที่ทำรายการ
    public boolean SyncStatus;              // สถานะการ Synch ข้อมูลมาที่ Server (1=true=ส่งข้อมูลเข้า Server แล้ว, 0=false=ยังไม่ได้ส่งข้อมูลมาที่ Server)
    public Date SyncDate;                   // วันที่ทำการ Synch ข้อมูลมาที่ Server
    public Date CreateDate;                 // เวลาที่มีการบันทึก History
    public String CreateBy;                 // EmpID ของพนักงานที่ต้องมีการบันทึก History
    public String DeviceID;                 // DeviceID ที่ต้องมีการบันทึก History

    public static AddTransactionLogSkipInputInfo from(TransactionLogSkipInfo info) {
        AddTransactionLogSkipInputInfo wsInfo = new AddTransactionLogSkipInputInfo();
        wsInfo.TransactionLogSkipID = info.TransactionLogSkipID;
        wsInfo.TransactionID = info.TransactionID;
        wsInfo.ServiceName = info.ServiceName;
        wsInfo.ServiceInputName = info.ServiceInputName;
        wsInfo.ServiceInputType = info.ServiceInputType;
        wsInfo.ServiceOutputType = info.ServiceOutputType;
        wsInfo.ServiceInputData = info.ServiceInputData;
        wsInfo.TransactionDate = info.TransactionDate;
        wsInfo.SyncStatus = info.SyncStatus;
        wsInfo.SyncDate = info.SyncDate;
        wsInfo.CreateDate = info.CreateDate;
        wsInfo.CreateBy = info.CreateBy;
        wsInfo.DeviceID = info.DeviceID;
        return wsInfo;
    }

}
