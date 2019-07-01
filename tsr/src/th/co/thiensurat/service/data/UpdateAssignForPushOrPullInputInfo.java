package th.co.thiensurat.service.data;


import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.AssignInfo;

public class UpdateAssignForPushOrPullInputInfo extends BHParcelable {
    public String OrganizationCode;
    public String ContractList;
    public String AssigneeEmpID;
    public String AssignAction;
}