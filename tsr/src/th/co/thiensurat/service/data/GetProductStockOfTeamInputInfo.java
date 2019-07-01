package th.co.thiensurat.service.data;

import th.co.bighead.utilities.BHParcelable;

public class GetProductStockOfTeamInputInfo extends BHParcelable {
	public String OrganizationCode;
	public String TeamCode;
	public String CreateBy;	// [Modified@05/08/2016] :: For support CRD take by EmpID
}
