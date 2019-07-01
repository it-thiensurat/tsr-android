package th.co.thiensurat.service.data;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.SendDocumentDetailInfo;

public class DeleteSendDocumentDetailByPrintHistoryIDInputInfo extends BHParcelable {
	public String OrganizationCode;
	public String SendDocumentID;
    public String PrintHistoryID;
	
	public static DeleteSendDocumentDetailByPrintHistoryIDInputInfo from(SendDocumentDetailInfo localInfo) {
		DeleteSendDocumentDetailByPrintHistoryIDInputInfo info = new DeleteSendDocumentDetailByPrintHistoryIDInputInfo();
		info.OrganizationCode = localInfo.OrganizationCode;
		info.SendDocumentID = localInfo.SendDocumentID;
		info.PrintHistoryID = localInfo.PrintHistoryID;
		return info;
	}
}
