package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class TripInfo extends BHParcelable {
	public String TripID;
	public String OrganizationCode;
	public int TripNumber;
	public int Year;
	public Date StartDate;
	public Date EndDate;
}
