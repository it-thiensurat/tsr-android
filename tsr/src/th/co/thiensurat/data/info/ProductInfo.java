package th.co.thiensurat.data.info;

import th.co.bighead.utilities.BHParcelable;

public class ProductInfo extends BHParcelable {
	public String ProductID;
	public String OrganizationCode;
	public String ProductName;
	public String ProductCode;
	public String ProductModel;
	public float ProductPrice;
	public float CashDiscount;
	public float CreditDiscount;
	public String ProductDescription;
	public String Type;
	public String SyncedDate;

	// Extend Fields
	public String ProductSerialNumber;
}
