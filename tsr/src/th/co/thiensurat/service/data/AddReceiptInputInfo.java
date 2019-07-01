package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ReceiptInfo;

public class AddReceiptInputInfo extends BHParcelable {

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
	
	public static AddReceiptInputInfo from(ReceiptInfo r) {
		AddReceiptInputInfo info = new AddReceiptInputInfo();
		info.ReceiptID = r.ReceiptID;
		info.OrganizationCode = r.OrganizationCode;
		info.ReceiptCode = r.ReceiptCode;
		info.PaymentID = r.PaymentID;
		info.RefNo = r.RefNo;
		info.DatePayment = r.DatePayment;
		info.TotalPayment = r.TotalPayment;
		info.CreateDate = r.CreateDate;
		info.CreateBy = r.CreateBy;
		info.LastUpdateDate = r.LastUpdateDate;
		info.LastUpdateBy = r.LastUpdateBy;
		return info;
	}
}
