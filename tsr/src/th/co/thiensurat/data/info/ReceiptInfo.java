package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ReceiptInfo extends BHParcelable {

	public String ReceiptID;
	public String OrganizationCode;
	public String ReceiptCode;
	public String PaymentID;
	public String RefNo;
	public Date DatePayment;
	public float TotalPayment;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;

    //DebtorCustomer
    public String CustomerFullName;


	//For Do Void Receipt
	public float PaymentAmount;
	public float PaymentDiscount;


}
