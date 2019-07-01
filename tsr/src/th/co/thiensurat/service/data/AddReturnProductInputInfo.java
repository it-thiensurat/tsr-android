package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ReturnProductInfo;

public class AddReturnProductInputInfo extends BHParcelable {
    public String ReturnProductID;
    public String OrganizationCode;
    public String EmpID;
    public String TeamCode;
    public String SubTeamCode;
    public Date ReturnDate;
    public String FortnightID;
    public Date RecevieDate;
    public String Remark;
    public String Status;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;
	
	public static AddReturnProductInputInfo from(ReturnProductInfo input) {
		AddReturnProductInputInfo info = new AddReturnProductInputInfo();
	    info.ReturnProductID = input.ReturnProductID;
	    info.OrganizationCode = input.OrganizationCode;
	    info.EmpID = input.EmpID;
	    info.TeamCode = input.TeamCode;
	    info.SubTeamCode = input.SubTeamCode;
	    info.ReturnDate = input.ReturnDate;
	    info.FortnightID = input.FortnightID;
	    info.RecevieDate = input.RecevieDate;
	    info.Remark = input.Remark;
	    info.Status = input.Status;
	    info.CreateDate = input.CreateDate;
	    info.CreateBy = input.CreateBy;
	    info.LastUpdateDate = input.LastUpdateDate;
	    info.LastUpdateBy = input.LastUpdateBy;
	    info.SyncedDate = input.SyncedDate;
		return info;
	}
}
