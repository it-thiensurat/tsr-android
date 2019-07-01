package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.SendMoneyInfo;

public class AddSendMoneyInputInfo extends BHParcelable {

	public String SendMoneyID;
	public String OrganizationCode;
	public Date PaymentDate;
	public String PaymentType;
	public String Reference1;
	public String Reference2;
	public float SendAmount;
	public Date SendDate;
	public String BankCode;
	public String Status;
	public String TransactionNo;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;
	public String ChannelItemID;
	public String PayeeName;
	public Date SaveTransactionNoDate;
	//-- Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
	public String SendMoneyEmployeeLevelPath;
	public String EmpID;
	public String TeamCode;

	public static AddSendMoneyInputInfo from(SendMoneyInfo sendMoney) {
		AddSendMoneyInputInfo info = new AddSendMoneyInputInfo();		
		info.SendMoneyID = sendMoney.SendMoneyID;
		info.OrganizationCode = sendMoney.OrganizationCode;
		info.PaymentDate = sendMoney.PaymentDate;
		info.PaymentType = sendMoney.PaymentType;
		info.Reference1 = sendMoney.Reference1;
		info.Reference2 = sendMoney.Reference2;
		info.SendAmount = sendMoney.SendAmount;
		info.SendDate = sendMoney.SendDate;
		info.BankCode = sendMoney.BankCode;
		info.Status = sendMoney.Status;
		info.TransactionNo = sendMoney.TransactionNo;
		info.CreateDate = sendMoney.CreateDate;
		info.CreateBy = sendMoney.CreateBy;
		info.LastUpdateDate = sendMoney.LastUpdateDate;
		info.LastUpdateBy = sendMoney.LastUpdateBy;
		info.SyncedDate = sendMoney.SyncedDate;
		info.ChannelItemID = sendMoney.ChannelItemID;
		info.PayeeName = sendMoney.PayeeName;	
		info.SaveTransactionNoDate = sendMoney.SaveTransactionNoDate;
		//-- Fixed - [BHPROJ-0016-773] :: [Meeting@BH-28/12/2558] 1. [DB-Design + Store-Procedure + Method] ออกแบบและแก้ไขโครงสร้าง table และอื่น ๆ เพื่อรองรับการทำงานของข้อมูลที่มีโครงสร้างแบบเก่า
		info.SendMoneyEmployeeLevelPath = sendMoney.SendMoneyEmployeeLevelPath;
		info.EmpID = sendMoney.EmpID;
		info.TeamCode = sendMoney.TeamCode;
		return info;
	}
}
