package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.AssignInfo;

/**
 * Created by bighead on 05/09/2016.
 */
public class UpdateNewAssignForSortOrderDefaultInputInfo extends BHParcelable {
    public String RefNo;
    public String OrganizationCode;
    public String TaskType;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public String AssigneeEmpID;
    public int NewOrder;
    public String SalePaymentPeriodID;
    public int NewPaymentDueDay;

    public static UpdateNewAssignForSortOrderDefaultInputInfo from(AssignInfo a) {
        UpdateNewAssignForSortOrderDefaultInputInfo info = new UpdateNewAssignForSortOrderDefaultInputInfo();
        info.RefNo = a.RefNo;
        info.OrganizationCode = a.OrganizationCode;
        info.TaskType = a.TaskType;
        info.LastUpdateDate = a.LastUpdateDate;
        info.LastUpdateBy = a.LastUpdateBy;
        info.AssigneeEmpID = a.AssigneeEmpID;
        info.NewOrder = a.NewOrder;
        info.SalePaymentPeriodID = a.SalePaymentPeriodID;
        info.NewPaymentDueDay = a.NewPaymentDueDay;

        return info;
    }
}