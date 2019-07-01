package th.co.thiensurat.data.controller;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.data.info.EmployeeDetailInfo;

public class EmployeeDetailController extends BaseController {

	public void addEmployeeDetail(EmployeeDetailInfo info) {
     String sql = "insert into EmployeeDetail(EmployeeDetailID, EmployeeCode, ParentEmployeeCode, EmployeeTypeCode, OrganizationCode,"
                +" BranchCode, DepartmentCode, SubDepartmentCode, TeamCode, SubTeamCode, PositionCode, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SupervisorHeadCode, SaleCode"
			 	+", ParentEmployeeDetailID, SupervisorCode, SubTeamHeadCode, TeamHeadCode, SubDepartmentHeadCode, DepartmentHeadCode, SourceSystem)"
                +" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[] {info.EmployeeDetailID, info.EmployeeCode, info.ParentEmployeeCode, info.EmployeeTypeCode, info.OrganizationCode,
                info.BranchCode, info.DepartmentCode, info.SubDepartmentCode, info.TeamCode, info.SubTeamCode, info.PositionCode, valueOf(info.CreateDate), info.CreateBy
				, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.SupervisorHeadCode, info.SaleCode, info.ParentEmployeeDetailID, info.SupervisorCode, info.SubTeamHeadCode,
				info.TeamHeadCode, info.SubDepartmentHeadCode, info.DepartmentHeadCode, info.SourceSystem});
    }
	
	public void deleteEmployeeDetailByTeamCode(String teamCode) {
		String sql = "delete from EmployeeDetail where TeamCode=?";
		executeNonQuery(sql, new String[] {teamCode});
	}

	public void deleteEmployeeDetailAll() {
		String sql = "delete from EmployeeDetail";
		executeNonQuery(sql, null);
	}
	
	public EmployeeDetailInfo getEmpDetailByEmpCode(String employeeCode) {
		final String QUERY_EMPLOYEEDETAIL_GET_BY_EMPCODE = "SELECT * FROM EmployeeDetail WHERE EmployeeCode = ?";
		return executeQueryObject(QUERY_EMPLOYEEDETAIL_GET_BY_EMPCODE, new String[]{employeeCode}, EmployeeDetailInfo.class);
	}
	
	public List<EmployeeDetailInfo> getEmployeeDetail() {
		return executeQueryList("select *from EmployeeDetail", null, EmployeeDetailInfo.class);
	}

    public void deleteEmployeeDetailByTeamCodeByEmployeeCode(String teamCode, String employeeCode) {
        executeNonQuery("DELETE FROM EmployeeDetail WHERE (TeamCode = ?) AND (EmployeeCode = ?)", new String[] { teamCode, employeeCode});
    }


	public EmployeeDetailInfo getTeamHeadDetailByTeamCode(String OrganizationCode, String TeamCode) {
		String sql = "SELECT ed.*" +
				" , IFNULL(e.FirstName,'') || ' ' || IFNULL(e.LastName,'') As EmployeeName" +
				" , p.PositionName" +
				" , IFNULL(SupervisorHead.FirstName,'') || ' ' || IFNULL(SupervisorHead.LastName,'') AS SupervisorHeadName" +
				" , SupervisorHeadDetail.PositionCode AS SupervisorHeadPositionCode, PositionSupervisorHead.PositionName AS SupervisorHeadPositionName" +
				" , IFNULL(SubDepartmentHead.FirstName,'') || ' ' || IFNULL(SubDepartmentHead.LastName,'') AS SubDepartmentHeadName" +
				" , SubDepartmentHeadDetail.PositionCode AS SubDepartmentHeadPositionCode, PositionSubDepartmentHead.PositionName  AS SubDepartmentHeadPositionName" +
				" , IFNULL(DepartmentHead.FirstName,'') || ' ' || IFNULL(DepartmentHead.LastName,'') AS DepartmentHeadName" +
				" , DepartmentHeadDetail.PositionCode AS DepartmentHeadPositionCode, PositionDepartmentHead.PositionName AS DepartmentHeadPositionName" +
				" FROM EmployeeDetail ed" +
				" INNER JOIN Employee e ON e.EmpID = ed.EmployeeCode" +
				" INNER JOIN Position p ON p.PositionID= ed.PositionCode" +
				" LEFT OUTER JOIN Employee SupervisorHead ON SupervisorHead.EmpID = ed.SupervisorHeadCode" +
				" LEFT OUTER JOIN EmployeeDetail SupervisorHeadDetail on SupervisorHeadDetail.EmployeeDetailID = ed.ParentEmployeeDetailID" +
				" LEFT OUTER JOIN Position PositionSupervisorHead ON PositionSupervisorHead.PositionID = SupervisorHeadDetail.PositionCode" +
				" LEFT OUTER JOIN Employee SubDepartmentHead ON SubDepartmentHead.EmpID = ed.SubDepartmentHeadCode" +
				" LEFT OUTER JOIN EmployeeDetail SubDepartmentHeadDetail on SubDepartmentHeadDetail.EmployeeDetailID = SupervisorHeadDetail.ParentEmployeeDetailID" +
				" LEFT OUTER JOIN Position PositionSubDepartmentHead ON PositionSubDepartmentHead.PositionID = SubDepartmentHeadDetail.PositionCode" +
				" LEFT OUTER JOIN Employee DepartmentHead ON DepartmentHead.EmpID = ed.DepartmentHeadCode" +
				" LEFT OUTER JOIN EmployeeDetail DepartmentHeadDetail on DepartmentHeadDetail.EmployeeDetailID = SubDepartmentHeadDetail.ParentEmployeeDetailID" +
				" LEFT OUTER JOIN Position PositionDepartmentHead ON PositionDepartmentHead.PositionID = DepartmentHeadDetail.PositionCode" +
				" WHERE e.OrganizationCode = ? AND ed.TeamCode = ? AND ed.TeamHeadCode = ed.EmployeeCode" +
				" ORDER BY p.PositionLevel DESC LIMIT 1";
		return executeQueryObject(sql, new String[]{OrganizationCode, TeamCode}, EmployeeDetailInfo.class);
	}

	public EmployeeDetailInfo getTeamHeadDetailByTeamCodeAndEmpID(String OrganizationCode, String TeamCode, String EmpID) {
		String sql = "SELECT ed.*" +
				" , IFNULL(e.FirstName,'') || ' ' || IFNULL(e.LastName,'') As EmployeeName" +
				" , p.PositionName" +
				" , IFNULL(TeamHead.FirstName,'') || ' ' || IFNULL(TeamHead.LastName,'') AS TeamHeadName" +
				" , IFNULL(SupervisorHead.FirstName,'') || ' ' || IFNULL(SupervisorHead.LastName,'') AS SupervisorHeadName" +
				" , SupervisorHeadDetail.PositionCode AS SupervisorHeadPositionCode, PositionSupervisorHead.PositionName AS SupervisorHeadPositionName" +
				" , IFNULL(SubDepartmentHead.FirstName,'') || ' ' || IFNULL(SubDepartmentHead.LastName,'') AS SubDepartmentHeadName" +
				" , SubDepartmentHeadDetail.PositionCode AS SubDepartmentHeadPositionCode, PositionSubDepartmentHead.PositionName  AS SubDepartmentHeadPositionName" +
				" , IFNULL(DepartmentHead.FirstName,'') || ' ' || IFNULL(DepartmentHead.LastName,'') AS DepartmentHeadName" +
				" , DepartmentHeadDetail.PositionCode AS DepartmentHeadPositionCode, PositionDepartmentHead.PositionName AS DepartmentHeadPositionName" +
				" FROM EmployeeDetail ed" +
				" INNER JOIN Employee e ON e.EmpID = ed.EmployeeCode" +
				" INNER JOIN Position p ON p.PositionID= ed.PositionCode" +
				" LEFT OUTER JOIN Employee TeamHead ON TeamHead.EmpID = ed.TeamHeadCode" +
				" LEFT OUTER JOIN Employee SupervisorHead ON SupervisorHead.EmpID = ed.SupervisorHeadCode" +
				" LEFT OUTER JOIN EmployeeDetail SupervisorHeadDetail on SupervisorHeadDetail.EmployeeDetailID = ed.ParentEmployeeDetailID" +
				" LEFT OUTER JOIN Position PositionSupervisorHead ON PositionSupervisorHead.PositionID = SupervisorHeadDetail.PositionCode" +
				" LEFT OUTER JOIN Employee SubDepartmentHead ON SubDepartmentHead.EmpID = ed.SubDepartmentHeadCode" +
				" LEFT OUTER JOIN EmployeeDetail SubDepartmentHeadDetail on SubDepartmentHeadDetail.EmployeeDetailID = SupervisorHeadDetail.ParentEmployeeDetailID" +
				" LEFT OUTER JOIN Position PositionSubDepartmentHead ON PositionSubDepartmentHead.PositionID = SubDepartmentHeadDetail.PositionCode" +
				" LEFT OUTER JOIN Employee DepartmentHead ON DepartmentHead.EmpID = ed.DepartmentHeadCode" +
				" LEFT OUTER JOIN EmployeeDetail DepartmentHeadDetail on DepartmentHeadDetail.EmployeeDetailID = SubDepartmentHeadDetail.ParentEmployeeDetailID" +
				" LEFT OUTER JOIN Position PositionDepartmentHead ON PositionDepartmentHead.PositionID = DepartmentHeadDetail.PositionCode" +
				" WHERE e.OrganizationCode = ? AND ed.TeamCode = ? AND ed.EmployeeCode = ?" +
				" ORDER BY p.PositionLevel ASC LIMIT 1";
		return executeQueryObject(sql, new String[]{OrganizationCode, TeamCode, EmpID}, EmployeeDetailInfo.class);
	}

	public EmployeeDetailInfo getEmployeeDetailByTeamCodeByEmployeeID(String OrganizationCode, String empID, String teamCode) {
		String sql = "SELECT ed.*" +
				" , IFNULL(e.FirstName,'') || ' ' || IFNULL(e.LastName,'') As EmployeeName" +
				" , IFNULL(TeamHead.FirstName,'') || ' ' || IFNULL(TeamHead.LastName,'') AS TeamHeadName" +
				" , IFNULL(SupervisorHead.FirstName,'') || ' ' || IFNULL(SupervisorHead.LastName,'') AS SupervisorHeadName" +
				" , IFNULL(SubDepartmentHead.FirstName,'') || ' ' || IFNULL(SubDepartmentHead.LastName,'') AS SubDepartmentHeadName" +
				" , IFNULL(DepartmentHead.FirstName,'') || ' ' || IFNULL(DepartmentHead.LastName,'') AS DepartmentHeadName" +
				" FROM Employee e" +
				" INNER JOIN EmployeeDetail ed ON ed.EmployeeCode = e.EmpID AND ed.OrganizationCode = e.OrganizationCode" +
				" INNER JOIN Position p ON p.PositionID = ed.PositionCode AND p.OrganizationCode = ed.OrganizationCode" +
				" LEFT OUTER JOIN Employee TeamHead ON TeamHead.EmpID = ed.TeamHeadCode" +
				" LEFT OUTER JOIN Employee SupervisorHead ON SupervisorHead.EmpID = ed.SupervisorHeadCode" +
				" LEFT OUTER JOIN Employee SubDepartmentHead ON SubDepartmentHead.EmpID = ed.SubDepartmentHeadCode" +
				" LEFT OUTER JOIN Employee DepartmentHead ON DepartmentHead.EmpID = ed.DepartmentHeadCode" +
				" WHERE (e.OrganizationCode = ?) AND (e.EmpID = ?) AND (ed.TeamCode = ?) " +
				" ORDER BY p.PositionLevel DESC LIMIT 1";
		return executeQueryObject(sql, new String[]{OrganizationCode, empID, teamCode}, EmployeeDetailInfo.class);
	}

	public List<EmployeeDetailInfo> getSubTeamMemberBySubTeamCode(String OrganizationCode, String SubTeamCode) {
		String sql = "SELECT ed.*" +
				" , IFNULL(e.FirstName,'') || ' ' || IFNULL(e.LastName,'') As EmployeeName" +
				" , p.PositionName" +
				" FROM EmployeeDetail ed" +
				" INNER JOIN Employee e ON e.EmpID = ed.EmployeeCode AND e.OrganizationCode = ed.OrganizationCode" +
				" INNER JOIN Position p ON p.PositionID = ed.PositionCode AND p.OrganizationCode = ed.OrganizationCode" +
				" WHERE ed.OrganizationCode = ? AND ed.SubTeamCode IS NOT NULL AND ed.SubTeamCode = ? AND (e.Status IS NULL OR (e.Status IS NOT NULL AND e.Status <> 'R'))" +
				" ORDER BY p.PositionLevel DESC, ed.SaleCode ASC";
		return executeQueryList(sql, new String[]{OrganizationCode, SubTeamCode}, EmployeeDetailInfo.class);
	}

	public List<EmployeeDetailInfo> getSubTeamMemberByEmpID(String OrganizationCode, String SubTeamCode, String EmpID, String PositionCode) {
		String sql = "SELECT ed.*" +
				" , IFNULL(e.FirstName,'') || ' ' || IFNULL(e.LastName,'') As EmployeeName" +
				" , p.PositionName" +
				" FROM EmployeeDetail ed" +
				" INNER JOIN Employee e ON e.EmpID = ed.EmployeeCode AND e.OrganizationCode = ed.OrganizationCode" +
				" INNER JOIN Position p ON p.PositionID = ed.PositionCode AND p.OrganizationCode = ed.OrganizationCode" +
				" WHERE ed.OrganizationCode = ? AND ed.SubTeamCode IS NOT NULL AND ed.SubTeamCode = ? AND ed.EmployeeCode = ? AND ed.PositionCode = ?" +
				" ORDER BY p.PositionLevel DESC, ed.SaleCode ASC";
		return executeQueryList(sql, new String[]{OrganizationCode, SubTeamCode, EmpID, PositionCode}, EmployeeDetailInfo.class);
	}

	public String getMemberByTeamCodeOrSubTeamCode(String OrganizationCode, String TeamCode, String SubTeamCode) {
		String sql = "SELECT DISTINCT ed.EmployeeCode" +
				" FROM EmployeeDetail ed" +
				" WHERE ed.OrganizationCode = ? " +
				"	AND (ed.TeamCode = ? OR ed.SubTeamCode = ?)" +
				" ORDER BY ed.EmployeeCode ASC";
		List<EmployeeDetailInfo> list = executeQueryList(sql, new String[]{OrganizationCode, TeamCode, SubTeamCode}, EmployeeDetailInfo.class);
		List<String> EmpIDOfMember = new ArrayList<>();
		if(list != null && list.size() > 0){
			for(EmployeeDetailInfo emp : list){
				EmpIDOfMember.add(emp.EmployeeCode);
			}
			return TextUtils.join(",", EmpIDOfMember);
		}else{
			return "";
		}
	}

	// Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
	public EmployeeDetailInfo getCurrentTreeHistoryID(String organizationCode) {
		final String QUERY_EMPLOYEEDETAIL_GET_TREEHISTORYID = "SELECT * FROM EmployeeDetail WHERE (OrganizationCode = ?)  LIMIT 1  ";
		return executeQueryObject(QUERY_EMPLOYEEDETAIL_GET_TREEHISTORYID, new String[]{organizationCode}, EmployeeDetailInfo.class);
	}

}
