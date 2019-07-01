package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class SendMoneyInfo extends BHParcelable {
	public SendMoneyInfo() {

	}

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

	// Extend Fields
	public String PaymentTypeName;
	public String ChannelItemName;
	public String AccountCode1;
	public String AccountCode2;
	public String ChannelID;
	public String ChannelCode;
	public String ChannelName;

    public String FirstName;
    public String LastName;

	// SUM
	public float SumSendMoney;

}
