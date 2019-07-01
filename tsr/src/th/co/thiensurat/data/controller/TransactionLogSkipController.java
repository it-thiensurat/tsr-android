package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.LogScanProductSerialInfo;
import th.co.thiensurat.data.info.TransactionLogSkipInfo;



/*** Fixed-[BHPROJ-0016-1061] :: [Android-Logout-GCM] กรณีเป็นการ Logout จากการยิง GCM ให้ตรวจสอบก่อนว่า ณ เวลานั้นมีใคร Login ด้วย UserID ของเราด้วย DeviceID อื่นหรือเปล่า ถ้ามีให้ข้ามการทำ TRansactionLogService ของ Android เลย ***/


public class TransactionLogSkipController extends BaseController {

	
	public void addLogScanProductSerial(TransactionLogSkipInfo info) {
		String sql = "INSERT INTO [TransactionLogSkip]" +
				" (" +
				"           [TransactionLogSkipID]" +
				"           ,[TransactionID]" +
				"           ,[ServiceName]" +
				"           ,[ServiceInputName]" +
				"           ,[ServiceInputType]" +
				"           ,[ServiceOutputType]" +
				"           ,[ServiceInputData]" +
				"           ,[TransactionDate]" +
				"           ,[SyncStatus]" +
				"           ,[SyncDate]" +
				"           ,[CreateDate]" +
				"           ,[CreateBy]" +
				"           ,[DeviceID]" +
				" ) " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql, new String[] { info.TransactionLogSkipID, valueOf(info.TransactionID), info.ServiceName, info.ServiceInputName
				, info.ServiceInputType, info.ServiceOutputType, info.ServiceInputData, valueOf(info.TransactionDate), valueOf(info.SyncStatus)
				, valueOf(info.SyncDate), valueOf(info.CreateDate), info.CreateBy, info.DeviceID });
	}

}
