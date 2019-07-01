package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class SalePaymentPeriodPaymentInfo extends BHParcelable {
	public String SalePaymentPeriodID;
	public String PaymentID;
	public float Amount;
	public String ReceiptID;
	public Date CreateDate;
	public String CreateBy;
	public Date SyncedDate;
	public float CloseAccountDiscountAmount;

	// Extend Fields
	public float SumAmount;
    public int PaymentPeriodNumber;
    public boolean PaymentComplete;
    public float NetAmount;
	public String ReceiptCode;
	public Date DatePayment;	// Fixed - [BHPROJ-0025-745]
}
