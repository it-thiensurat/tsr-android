package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.SalePaymentPeriodPaymentInfo;

public class AddSalePaymentPeriodPaymentInputInfo extends BHParcelable{

	public String SalePaymentPeriodID;
	public String PaymentID;
	public float Amount;
	public String ReceiptID;
	public Date CreateDate;
	public String CreateBy;
	public Date SyncedDate;
	public float CloseAccountDiscountAmount;
	
	public static AddSalePaymentPeriodPaymentInputInfo from(SalePaymentPeriodPaymentInfo sppp) {
		AddSalePaymentPeriodPaymentInputInfo info = new AddSalePaymentPeriodPaymentInputInfo();
		info.SalePaymentPeriodID = sppp.SalePaymentPeriodID;
		info.PaymentID = sppp.PaymentID;
		info.Amount = sppp.Amount;
		info.ReceiptID = sppp.ReceiptID;
		info.CreateDate = sppp.CreateDate;
		info.CreateBy = sppp.CreateBy;
		info.SyncedDate = sppp.SyncedDate;
		info.CloseAccountDiscountAmount = sppp.CloseAccountDiscountAmount;
		return info;
	}
	
}
