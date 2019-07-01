package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.AssignInfo;

public class AddAssignInputInfo extends BHParcelable {
	public String AssignID;
	public String TaskType;
	public String OrganizationCode;
	public String RefNo;
	public String AssigneeEmpID;
	public String AssigneeTeamCode;
	public int Order;
	public int OrderExpect;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public String ReferenceID;
	public Date SyncedDate;
	
	public static AddAssignInputInfo from(AssignInfo a) {
		AddAssignInputInfo info = new AddAssignInputInfo();
		info.AssignID = a.AssignID;
		info.TaskType = a.TaskType;
		info.OrganizationCode = a.OrganizationCode;
		info.RefNo = a.RefNo;
		info.AssigneeEmpID = a.AssigneeEmpID;
		info.AssigneeTeamCode = a.AssigneeTeamCode;
		info.Order = a.Order;
		info.OrderExpect = a.OrderExpect;
		info.CreateDate = a.CreateDate;
		info.CreateBy = a.CreateBy;
		info.LastUpdateDate = a.LastUpdateDate;
		info.LastUpdateBy = a.LastUpdateBy;
		info.ReferenceID = a.ReferenceID;
		info.SyncedDate = a.SyncedDate;
		return info;
	}
}
