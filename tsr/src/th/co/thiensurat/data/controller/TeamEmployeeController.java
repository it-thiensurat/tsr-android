package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.EmployeeDetailInfo;

public class TeamEmployeeController extends BaseController {


//	public TeamEmployeeInfo getTeamEmp(String TeamCode, String EmpID, String OrganizationCode) {
//		final String QUERY_TEAM_EMP_GET_BY_ID = "SELECT * FROM TeamEmployee WHERE TeamCode = ? AND EmpID = ? AND OrganizationCode = ?";
//		return executeQueryObject(QUERY_TEAM_EMP_GET_BY_ID, new String[] { TeamCode, EmpID, OrganizationCode }, TeamEmployeeInfo.class);
//	}
	
	public EmployeeDetailInfo getTeamEmp(String TeamCode, String EmpID, String OrganizationCode) {
		final String QUERY_TEAM_EMP_GET_BY_ID = "SELECT * FROM EmployeeDetail WHERE TeamCode = ? AND EmpID = ? AND OrganizationCode = ?";
		return executeQueryObject(QUERY_TEAM_EMP_GET_BY_ID, new String[] { TeamCode, EmpID, OrganizationCode }, EmployeeDetailInfo.class);
	}
	
//	public TeamEmployeeInfo getTeamEmployee(String teamCode, String empID) {
//		final String QUERY_TEAM_EMPLOYEE_GET_BY_ID = "SELECT * FROM TeamEmployee WHERE TeamCode = ? AND EmpID = ?";
//		return executeQueryObject(QUERY_TEAM_EMPLOYEE_GET_BY_ID, new String[] { teamCode, empID }, TeamEmployeeInfo.class);
//	}

	public EmployeeDetailInfo getTeamEmployee(String teamCode, String empID) {
		final String QUERY_TEAM_EMPLOYEE_GET_BY_ID = "SELECT * FROM EmployeeDetail WHERE TeamCode = ? AND EmpID = ?";
		return executeQueryObject(QUERY_TEAM_EMPLOYEE_GET_BY_ID, new String[] { teamCode, empID }, EmployeeDetailInfo.class);
	}
}
