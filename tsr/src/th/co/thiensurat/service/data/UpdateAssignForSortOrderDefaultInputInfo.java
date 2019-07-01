package th.co.thiensurat.service.data;


import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.AssignInfo;

public class UpdateAssignForSortOrderDefaultInputInfo extends BHParcelable {
    public String TaskType;
    public String OrganizationCode;
    public String RefNo;
    public String AssigneeEmpID;
    public int NewOrder;
    public Date LastUpdateDate;
    public String LastUpdateBy;


    public static UpdateAssignForSortOrderDefaultInputInfo from(AssignInfo a) {
        UpdateAssignForSortOrderDefaultInputInfo info = new UpdateAssignForSortOrderDefaultInputInfo();
        info.TaskType = a.TaskType;
        info.OrganizationCode = a.OrganizationCode;
        info.RefNo = a.RefNo;
        info.AssigneeEmpID = a.AssigneeEmpID;
        info.NewOrder = a.NewOrder;
        info.LastUpdateDate = a.LastUpdateDate;
        info.LastUpdateBy = a.LastUpdateBy;
        return info;
    }
}