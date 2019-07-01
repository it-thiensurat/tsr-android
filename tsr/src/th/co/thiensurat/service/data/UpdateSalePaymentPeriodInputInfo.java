package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;

public class UpdateSalePaymentPeriodInputInfo extends BHParcelable{

	public String SalePaymentPeriodID;
	public String RefNo;
	public int PaymentPeriodNumber;
	public float PaymentAmount;
	public float Discount;
	public float NetAmount;
	public boolean PaymentComplete;
	public Date PaymentDueDate;
	public Date PaymentAppointmentDate;
	public String TripID;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;
	public float CloseAccountDiscountAmount;

	public static UpdateSalePaymentPeriodInputInfo from(SalePaymentPeriodInfo spp) {
		UpdateSalePaymentPeriodInputInfo info = new UpdateSalePaymentPeriodInputInfo();
		info.SalePaymentPeriodID = spp.SalePaymentPeriodID;
		info.RefNo = spp.RefNo;
		info.PaymentPeriodNumber = spp.PaymentPeriodNumber;
		info.PaymentAmount = spp.PaymentAmount;
		info.Discount = spp.Discount;
		info.NetAmount = spp.NetAmount;
		info.PaymentComplete = spp.PaymentComplete;
		info.PaymentDueDate = spp.PaymentDueDate;
		info.PaymentAppointmentDate = spp.PaymentAppointmentDate;
		info.TripID = spp.TripID;
		info.CreateDate = spp.CreateDate;
		info.CreateBy = spp.CreateBy;
		info.LastUpdateDate = spp.LastUpdateDate;
		info.LastUpdateBy = spp.LastUpdateBy;
		info.SyncedDate = spp.SyncedDate;
		info.CloseAccountDiscountAmount = spp.CloseAccountDiscountAmount;
		return info;
	}
	
}
