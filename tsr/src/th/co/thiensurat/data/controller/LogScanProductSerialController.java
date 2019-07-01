package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ChangeProductInfo;
import th.co.thiensurat.data.info.LogScanProductSerialInfo;


/*** Fixed - [BHPROJ-0026-1037] :: [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา] กรณีเป็นสัญญา Migrate ให้มีปุ่ม เพื่อข้ามการ scan สินค้าได้ ***/

public class LogScanProductSerialController extends BaseController {

	public enum LogScanProductSerialTaskType {
		ChangeContract, ChangeProduct, ImpoundProduct
	}
	
	public void addLogScanProductSerial(LogScanProductSerialInfo info) {
		String sql = "INSERT INTO [LogScanProductSerial]" +
				" ( " +
				"			[LogScanProductSerialID]" +
				"           , [OrganizationCode]" +
				"           , [TaskType]" +
				"           , [RequestID]" +
				"           , [IsScanProductSerial]" +
				"           , [RefNo]" +
				"           , [ProductSerialNumber]" +
				"           , [Status]" +
				"           , [CreateDate]" +
				"           , [CreateBy]" +
				" ) " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql, new String[] { info.LogScanProductSerialID, info.OrganizationCode, info.TaskType, info.RequestID
				, valueOf(info.IsScanProductSerial), info.RefNo, info.ProductSerialNumber, info.Status, valueOf(info.CreateDate), info.CreateBy });
	}

}
