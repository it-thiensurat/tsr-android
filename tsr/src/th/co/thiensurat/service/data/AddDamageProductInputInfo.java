package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.DamageProductInfo;

public class AddDamageProductInputInfo extends BHParcelable {
    public String DamageProductID;
    public String OrganizationCode;
    public String EmpID;
    public String TeamCode;
    public String SubTeamCode;
    public Date ReturnDate;
    public String ReturnType;
    public String ReturnCause;
    public String ProductSerialNumber;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;
	
	public static  AddDamageProductInputInfo from(DamageProductInfo localInfo) {
		AddDamageProductInputInfo info = new AddDamageProductInputInfo();
		info.DamageProductID = localInfo.DamageProductID;
		info.OrganizationCode = localInfo.OrganizationCode;
		info.EmpID = localInfo.EmpID;
		info.TeamCode = localInfo.TeamCode;
		info.SubTeamCode = localInfo.SubTeamCode;
		info.ReturnDate = localInfo.ReturnDate;
		info.ReturnType = localInfo.ReturnType;
		info.ReturnCause = localInfo.ReturnCause;
		info.ProductSerialNumber = localInfo.ProductSerialNumber;
		info.CreateDate = localInfo.CreateDate;
		info.CreateBy = localInfo.CreateBy;
		info.LastUpdateDate = localInfo.LastUpdateDate;
		info.LastUpdateBy = localInfo.LastUpdateBy;
		info.SyncedDate = localInfo.SyncedDate;
		return info;
	}
}
