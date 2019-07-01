package th.co.thiensurat.service.data;

import th.co.bighead.utilities.BHParcelable;

public class GroupSalePaymentPeriodByAssigneeInfo extends BHParcelable {

	// <GroupSalePaymentPeriodByAssigneeInfo>
	// <AssigneeEmpID>string</AssigneeEmpID>
	// <AssigneeEmpName>string</AssigneeEmpName>
	// <AssigneeCount>int</AssigneeCount>
	// <SumOutstandingAmount>float</SumOutstandingAmount>
	// </GroupSalePaymentPeriodByAssigneeInfo>

	// public String AssigneeEmpID;
	// public String AssigneeEmpName;
	// public int AssigneeCount;
	// public float SumOutstandingAmount;

	// Assign.AssigneeEmpID, Employee.FirstName, Employee.LastName, COUNT(*) AS
	// AssigneeCount
	public String AssigneeEmpID;
	public String AssigneeEmpName;
	public int AssigneeCount;
}
