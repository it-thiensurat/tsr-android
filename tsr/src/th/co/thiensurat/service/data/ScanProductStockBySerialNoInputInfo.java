package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ScanProductStockBySerialNoInputInfo extends BHParcelable {

	public String OrganizationCode;
	public String ProductSerialNumber;
	public String ScanByTeam;
	//public String TeamCode;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
}
