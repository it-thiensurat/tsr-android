package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class SaveTransactionNoInputInfo extends BHParcelable {
	
	public String OrganizationCode;
	public String SendMoneyID;
	public String TransactionNo;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	
	public static SaveTransactionNoInputInfo from(String organizationCode, String sendMoneyID, String transactionNo, Date lastUpdateDate, String lastUpdateBy) {
		SaveTransactionNoInputInfo info = new SaveTransactionNoInputInfo();
		
		info.OrganizationCode = organizationCode;
		info.SendMoneyID = sendMoneyID;
		info.TransactionNo = transactionNo;
		info.LastUpdateDate = lastUpdateDate;
		info.LastUpdateBy = lastUpdateBy;
		
		return info;
		
	}

}
