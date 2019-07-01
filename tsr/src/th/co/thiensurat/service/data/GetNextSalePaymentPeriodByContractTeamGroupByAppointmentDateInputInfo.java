package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class GetNextSalePaymentPeriodByContractTeamGroupByAppointmentDateInputInfo extends BHParcelable {

	// <info>
	// <RefNo>string</RefNo>
	// <OrganizationCode>string</OrganizationCode>
	// <SaleTeamCode>string</SaleTeamCode>
	// <AssigneeEmpID>string</AssigneeEmpID>
	// <AssigneeTeamCode>string</AssigneeTeamCode>
	// <AppointmentDate>dateTime</AppointmentDate>
	// <isFirstPayment>boolean</isFirstPayment>
	// </info>

	public String RefNo;
	public String OrganizationCode;
	public String SaleTeamCode;
	public String AssigneeEmpID;
	public String AssigneeTeamCode;
	public Date AppointmentDate;
	public boolean isFirstPayment;

}
