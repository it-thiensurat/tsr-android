package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class SendDocumentInfo extends BHParcelable {
	public String OrganizationCode;
	public String SendDocumentID;
    public int SumDocument;
	public Date SyncedDate;
    public String SentSubTeamCode;
    public String SentTeamCode;
}
