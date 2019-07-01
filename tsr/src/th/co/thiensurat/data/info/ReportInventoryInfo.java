package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.controller.BaseController;

public class ReportInventoryInfo extends BHParcelable{
	
	public String ReportInventoryID;
	public String FortnightID;
	public String Fortnight;
	public String FortnightTime;
	public String ProductID;
	public String ProductName;
	public String ProductCode;
	public String ProductModel;
	public String TeamCode;
	public Date ReportDate;
	public int RemainProduct;
	public int PickProduct;
	public int InstallProduct;
	public int ChangeProduct;
	public int ReturnProduct;
	public int BalanceProduct;
	
	


}
