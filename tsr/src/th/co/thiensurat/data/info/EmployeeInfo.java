package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHUtilities;

public class EmployeeInfo extends BHParcelable {

	public String EmpID;
	public String OrganizationCode;
	public String FirstName;
	public String LastName;
	public String EmployeeTypeCode;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;
	
	//other
	public String EmpName;
	public String EmployeeDetailID;
	public String ParentEmployeeCode;
	public String BranchCode;
	public String DepartmentCode;
	public String SubDepartmentCode;
	public String SubTeamCode;
	public String SaleTypeCode;
	public String SaleTypeName;
	public String PositionID;
	public String PositionName;
	public String ParentPosition;
	public String TeamCode;
	public String TeamName;
	public boolean NoPrint;
	public String TeamType;
	public String ParentEmployeeName;
	public String EmployeeTypeName;
	public String PositionCode;
	//public String SupervisorCode; //No use
    public String SupervisorHeadCode;
    public String SubTeamName;
    public String SourceSystem;
    public String SourceSystemName;
	
	// -- for upperEmployee---
	public String DriverEmpID;
	public String DriverFirstName;
	public String DriverLastName;
	public String SaleLeaderEmpID;
	public String SaleLeaderFirstName;
	public String SaleLeaderLastName;
	public String SaleLeaderTeamCode;
	public String SupervisorEmpID;
	public String SupervisorFirstName;
	public String SupervisorLastName;
	public String LineManagerEmpID;
	public String LineManagerFirstName;
	public String LineManagerLastName;
	public String ManagerEmpID;
	public String ManagerFirstName;
	public String ManagerLastName;
	// public String SubTeamEmployeeID;
	// public String SubTeamEmployeeFirstName;
	// public String SubTeamEmployeeLastName;
	public String SaleCode;
	public String SaleEmployeeName;
	public String upperEmployeeName;

	public String ParentEmployeeDetailID;
	public String SupervisorCode;
	public String SubTeamHeadCode;
	public String TeamHeadCode;
	public String SubDepartmentHeadCode;
	public String DepartmentHeadCode;

	public String Status;
	
	public String FullName() {
			return BHUtilities.trim(FirstName) + " " + BHUtilities.trim(LastName);
	}

}
