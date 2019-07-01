package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class PackagePeriodDetailInfo extends BHParcelable {

	public String PaymentPeriodDetailID;
	public String Model;
	public String OrganizationCode;
	public int PaymentPeriodNumber;
	public float PaymentAmount;
	public Date SyncedDate;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public float CloseDiscountAmount;

}
