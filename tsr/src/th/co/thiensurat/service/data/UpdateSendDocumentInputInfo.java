package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.SendDocumentInfo;

public class UpdateSendDocumentInputInfo extends BHParcelable {
	public String OrganizationCode;
	public String SendDocumentID;
    public int SumDocument;
	public Date SyncedDate;
    public String SentSubTeamCode;
    public String SentTeamCode;
	
	public static  UpdateSendDocumentInputInfo from(SendDocumentInfo localInfo) {
		UpdateSendDocumentInputInfo info = new UpdateSendDocumentInputInfo();
		info.OrganizationCode = localInfo.OrganizationCode;
		info.SendDocumentID = localInfo.SendDocumentID;
		info.SumDocument = localInfo.SumDocument;
		info.SyncedDate = localInfo.SyncedDate;
		info.SentSubTeamCode = localInfo.SentSubTeamCode;
		info.SentTeamCode = localInfo.SentTeamCode;
		return info;
	}
}
