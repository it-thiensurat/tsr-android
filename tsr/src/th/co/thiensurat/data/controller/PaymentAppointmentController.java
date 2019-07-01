package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.PaymentAppointmentInfo;

public class PaymentAppointmentController extends BaseController {
	public void addAppointment(PaymentAppointmentInfo info) {
		String sql = "INSERT INTO PaymentAppointment(AppointmentID, OrganizationCode, RefNo, SalePaymentPeriodID, AppointmentDate, "
				+ "AppointmentDetail, CreateDate, CreateBy, SyncedDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql, new String[] { info.AppointmentID, info.OrganizationCode, info.RefNo, info.SalePaymentPeriodID, valueOf(info.AppointmentDate),
				info.AppointmentDetail, valueOf(info.CreateDate), info.CreateBy, valueOf(info.SyncedDate) });

	}
	
	public void deletePaymentAppointmentAll() {
		String sql = "delete from PaymentAppointment";
		executeNonQuery(sql, null);
	}
	
	public List<PaymentAppointmentInfo> getPaymentAppointment() {
		return executeQueryList("select *from PaymentAppointment", null, PaymentAppointmentInfo.class);
	}
}
