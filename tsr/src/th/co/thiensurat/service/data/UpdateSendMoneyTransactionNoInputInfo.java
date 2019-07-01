package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.SendMoneyInfo;

// Fixed - [BHPROJ-0026-865] :: [Meeting@TSR@11/02/59] [Android-บันทึก Transaction No. หลังจากนำส่งเงินแล้ว] ให้เรียกใช้ WS method ชื่อ UpdateSendMoneyTransactionNo แทน
public class UpdateSendMoneyTransactionNoInputInfo extends BHParcelable {

	public String SendMoneyID;
	public String OrganizationCode;
//	public Date PaymentDate;
//	public String PaymentType;
//	public String Reference1;
//	public String Reference2;
//	public float SendAmount;
//	public Date SendDate;
//	public String BankCode;
//	public String Status;
	public String TransactionNo;
//	public Date CreateDate;
//	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
//	public Date SyncedDate;
//	public String ChannelItemID;
//	public String PayeeName;
	public Date SaveTransactionNoDate;
//	//-- Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
//	public String SendMoneyEmployeeLevelPath;
//	public String EmpID;
//	public String TeamCode;

	public static UpdateSendMoneyTransactionNoInputInfo from(SendMoneyInfo sm) {
		UpdateSendMoneyTransactionNoInputInfo info = new UpdateSendMoneyTransactionNoInputInfo();
		info.SendMoneyID = sm.SendMoneyID;
		info.OrganizationCode = sm.OrganizationCode;
//		info.PaymentDate = sm.PaymentDate;
//		info.PaymentType = sm.PaymentType;
//		info.Reference1 = sm.Reference1;
//		info.Reference2 = sm.Reference2;
//		info.SendAmount = sm.SendAmount;
//		info.SendDate = sm.SendDate;
//		info.BankCode = sm.BankCode;
//		info.Status = sm.Status;
		info.TransactionNo = sm.TransactionNo;
//		info.CreateDate = sm.CreateDate;
//		info.CreateBy = sm.CreateBy;
		info.LastUpdateDate = sm.LastUpdateDate;
		info.LastUpdateBy = sm.LastUpdateBy;
//		info.SyncedDate = sm.SyncedDate;
//		info.ChannelItemID = sm.ChannelItemID;
//		info.PayeeName = sm.PayeeName;
		info.SaveTransactionNoDate = sm.SaveTransactionNoDate;
//		//-- Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
//		info.SendMoneyEmployeeLevelPath = sm.SendMoneyEmployeeLevelPath;
//		info.EmpID = sm.EmpID;
//		info.TeamCode = sm.TeamCode;
		return info;
	}
}
