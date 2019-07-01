package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.AssignInfo;

public class AddOrUpdateAssignActionWriteOffNPLInputInfo extends BHParcelable {

//	public AssignInfo AssignInfo;
	
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

//	public enum AssignTaskType {
//		ChangeProduct, ImpoundProduct, ChangeContract, WriteOffNPL, SalePaymentPeriod
//	}
	
	public static AddOrUpdateAssignActionWriteOffNPLInputInfo from(AssignInfo assign) {
		AddOrUpdateAssignActionWriteOffNPLInputInfo info = new AddOrUpdateAssignActionWriteOffNPLInputInfo();
		
		info.AssignID = assign.AssignID;
		info.TaskType = assign.TaskType;
		info.OrganizationCode = assign.OrganizationCode;
		info.RefNo = assign.RefNo;
		info.AssigneeEmpID = assign.AssigneeEmpID;
		info.AssigneeTeamCode = assign.AssigneeTeamCode;
		info.Order = assign.Order;
		info.OrderExpect = assign.OrderExpect;
		info.CreateDate = assign.CreateDate;
		info.CreateBy = assign.CreateBy;
		info.LastUpdateDate = assign.LastUpdateDate;
		info.LastUpdateBy = assign.LastUpdateBy;
		info.ReferenceID = assign.ReferenceID;
		
		return info;
	}
}
