package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.PaymentAppointmentInfo;

public class AddPaymentAppointmentInputInfo extends BHParcelable {
	public String AppointmentID;
	public String OrganizationCode;
	public String RefNo;
	public String SalePaymentPeriodID;
	public Date AppointmentDate;
	public String AppointmentDetail;
	public Date CreateDate;
	public String CreateBy;
	public Date SyncedDate;

	public static AddPaymentAppointmentInputInfo from(PaymentAppointmentInfo pa) {
		AddPaymentAppointmentInputInfo info = new AddPaymentAppointmentInputInfo();
		info.AppointmentID = pa.AppointmentID;
		info.OrganizationCode = pa.OrganizationCode;
		info.RefNo = pa.RefNo;
		info.SalePaymentPeriodID = pa.SalePaymentPeriodID;
		info.AppointmentDate = pa.AppointmentDate;
		info.AppointmentDetail = pa.AppointmentDetail;
		info.CreateDate = pa.CreateDate;
		info.CreateBy = pa.CreateBy;
		info.SyncedDate = pa.SyncedDate;
		return info;
	}
}
