package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ContractCloseAccountInfo extends BHParcelable {
	public String ContractCloseAccountID;
	public String OrganizationCode;
	public String RefNo; //เลขที่สัญญา
	public String PaymentID;
	public String SalePaymentPeriodID;
	public float OutstandingAmount;
	public float DiscountAmount;
	public float NetAmount;
	public Date EffectiveDate;
	public String EffectiveBy;
	public String EffectiveTeamCode;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;
}
