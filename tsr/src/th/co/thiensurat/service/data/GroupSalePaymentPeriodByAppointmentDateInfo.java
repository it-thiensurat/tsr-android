package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class GroupSalePaymentPeriodByAppointmentDateInfo extends BHParcelable {

	// <GroupSalePaymentPeriodByAppointmentDateInfo>
	// <AppointmentDate>dateTime</AppointmentDate>
	// <AppointmentDateCount>int</AppointmentDateCount>
	// <SumOutstandingAmount>float</SumOutstandingAmount>
	// </GroupSalePaymentPeriodByAppointmentDateInfo>

	public Date AppointmentDate;
	public int AppointmentDateCount;
	// public float SumOutstandingAmount;
}
