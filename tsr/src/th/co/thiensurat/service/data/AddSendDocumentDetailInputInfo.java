package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.SendDocumentDetailInfo;

public class AddSendDocumentDetailInputInfo extends BHParcelable {
	public String OrganizationCode;
	public String SendDocumentID;
    public String PrintHistoryID;
	public Date SyncedDate;
	
	public static AddSendDocumentDetailInputInfo from(SendDocumentDetailInfo localInfo) {
		AddSendDocumentDetailInputInfo info = new AddSendDocumentDetailInputInfo();
		info.OrganizationCode = localInfo.OrganizationCode;
		info.SendDocumentID = localInfo.SendDocumentID;
		info.PrintHistoryID = localInfo.PrintHistoryID;
		info.SyncedDate = localInfo.SyncedDate;
		return info;
	}
}
