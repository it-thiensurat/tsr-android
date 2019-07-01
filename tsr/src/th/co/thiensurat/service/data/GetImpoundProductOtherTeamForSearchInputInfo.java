package th.co.thiensurat.service.data;

import th.co.bighead.utilities.BHParcelable;

public class GetImpoundProductOtherTeamForSearchInputInfo extends BHParcelable{
	
//	<OrganizationCode>string</OrganizationCode>
//    <AssigneeTeamCode>string</AssigneeTeamCode>
//    <RequestTeamCode>string</RequestTeamCode>
//    <SearchText>string</SearchText>
//    <EmployeeID>string</EmployeeID>
	
	public String OrganizationCode;
	public String AssigneeTeamCode;
	public String RequestTeamCode;
	public String SearchText;
	public String EmployeeID;
	public boolean PaymentComplete;
}
