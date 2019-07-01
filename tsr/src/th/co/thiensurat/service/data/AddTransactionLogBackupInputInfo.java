package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.info.TransactionLogInfo;

public class AddTransactionLogBackupInputInfo extends BHParcelable {
    public String TransactionLogBackupID;     // GUID
    public String DeviceID;                 // DeviceID ที่ต้องมีการบันทึก History
    public Date BackupDate;                 //วันที Backup

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


    public static AddTransactionLogBackupInputInfo from(TransactionLogInfo info, String deviceID, Date backupDate, String empID) {
        AddTransactionLogBackupInputInfo _info = new AddTransactionLogBackupInputInfo();
        _info.TransactionLogBackupID = DatabaseHelper.getUUID();
        _info.DeviceID = deviceID;
        _info.BackupDate = backupDate;

        _info.TransactionID = info.TransactionID;
        _info.ServiceName = info.ServiceName;
        _info.ServiceInputName = info.ServiceInputName;
        _info.ServiceInputType = info.ServiceInputType;
        _info.ServiceOutputType = info.ServiceOutputType;
        _info.ServiceInputData = info.ServiceInputData;
        _info.TransactionDate = info.TransactionDate;
        _info.SyncStatus = info.SyncStatus;
        _info.SyncDate = info.SyncDate;

        _info.CreateDate = new Date();
        _info.CreateBy = empID;

        return _info;
    }

}