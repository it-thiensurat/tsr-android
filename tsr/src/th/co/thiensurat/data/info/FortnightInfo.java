package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class FortnightInfo extends BHParcelable {

	public String FortnightID;
	public String OrganizationCode;
	public int FortnightNumber;
	public int Year;	
	public Date StartDate;
	public Date EndDate;
	public Date SyncedDate;
	
	public int FortnightYear;
}
