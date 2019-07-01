package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class DiscountLimitInfo extends BHParcelable {
	public String DiscountLimitID;
	public String DiscountType;
	public float DiscountPrice;
	
	public String ProductID;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
}
