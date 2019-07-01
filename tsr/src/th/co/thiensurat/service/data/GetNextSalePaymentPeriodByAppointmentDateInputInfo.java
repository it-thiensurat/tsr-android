package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class GetNextSalePaymentPeriodByAppointmentDateInputInfo extends BHParcelable {

	// <info>
	// <RefNo>string</RefNo>
	// <OrganizationCode>string</OrganizationCode>
	// <SaleTeamCode>string</SaleTeamCode>
	// <AssigneeEmpID>string</AssigneeEmpID>
	// <AppointmentDateString>string</AppointmentDateString>
	// <isFirstPayment>boolean</isFirstPayment>
	// </info>
	// <info>
	// <RefNo>string</RefNo>
	// <OrganizationCode>string</OrganizationCode>
	// <SaleTeamCode>string</SaleTeamCode>
	// <AssigneeEmpID>string</AssigneeEmpID>
	// <AppointmentDate>dateTime</AppointmentDate>
	// <isFirstPayment>boolean</isFirstPayment>
	// </info>

	public String RefNo;
	public String OrganizationCode;
	public String SaleTeamCode;
	public String AssigneeEmpID;
	public String AppointmentDateString;
	public Date AppointmentDate;
	public boolean isFirstPayment;

}
