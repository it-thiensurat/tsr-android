package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.data.info.EmployeeInfo;

public class EmployeeController extends BaseController {

    public enum SourceSystem {
        Sale, Credit
    }

    public enum PublicEmployeeID {
        PUBLIC_EMPLOYEE_ID("999999");

        private String value;
        PublicEmployeeID(String val) {
            this.value = val;
        }

        @Override
        public String toString(){
            return value;
        }
    }

    public EmployeeInfo getEmployeeByID(String empID) {
        String sql = "SELECT * FROM Employee WHERE (EmpID = ?)";
        EmployeeInfo ret = executeQueryObject(sql, new String[]{empID}, EmployeeInfo.class);
        return ret;
    }

    public void addEmployee(EmployeeInfo info) {
        String sql = "INSERT INTO Employee (EmpID, OrganizationCode, FirstName, LastName, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate, Status)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.EmpID, info.OrganizationCode, info.FirstName, info.LastName, valueOf(info.CreateDate), info.CreateBy,
                valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate), info.Status});
    }

    public void updateEmployee(EmployeeInfo info) {
        String sql = "UPDATE Employee "
                + " SET FirstName = ?, LastName = ?, LastUpdateDate = ?, LastUpdateBy = ?, Status = ? "
                + " WHERE (OrganizationCode = ?) AND (EmpID = ?)";
        executeNonQuery(sql, new String[]{info.FirstName, info.LastName, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.Status, info.OrganizationCode, info.EmpID});
    }

    public List<EmployeeInfo> getEmployeesByTeamCode(String teamCode) {
        // public List<EmployeeInfo> getEmployees(String teamCode) {
        // final String QUERY_EMPLOYEE_GETS_BY_TEAM =
        // "SELECT Employee.* FROM Employee INNER JOIN TeamEmployee ON Employee.EmpID = TeamEmployee.EmpID WHERE SaleManType IS NOT NULL AND TeamEmployee.TeamCode = ?";
        //final String QUERY_EMPLOYEE_GETS_BY_TEAM = "SELECT Employee.*, EmployeeDetail.TeamCode, EmployeeDetail.SubTeamCode  FROM Employee INNER JOIN EmployeeDetail ON Employee.EmpID = EmployeeDetail.EmployeeCode WHERE EmployeeTypeCode IS NOT NULL AND EmployeeDetail.TeamCode = ?";
        final String QUERY_EMPLOYEE_GETS_BY_TEAM = "SELECT Employee.*, EmployeeDetail.TeamCode, EmployeeDetail.SubTeamCode, EmployeeDetail.PositionCode  FROM Employee INNER JOIN EmployeeDetail ON Employee.EmpID = EmployeeDetail.EmployeeCode WHERE EmployeeDetail.TeamCode = ? order by Employee.EmpID";

        return executeQueryList(QUERY_EMPLOYEE_GETS_BY_TEAM, new String[]{teamCode}, EmployeeInfo.class);
    }

    public List<EmployeeInfo> getEmployeesTeamCodeBySaleLeader(String teamCode) {
        String sql = "SELECT Employee.*, EmployeeDetail.* FROM Employee INNER JOIN EmployeeDetail ON Employee.EmpID = EmployeeDetail.EmployeeCode "
                + "WHERE PositionCode <> 'Driver'  AND EmployeeDetail.TeamCode = ? AND (Employee.Status IS NULL OR (Employee.Status IS NOT NULL AND Employee.Status <> 'R')) ORDER BY EmployeeDetail.SaleCode ASC";
        List<EmployeeInfo> ret = null;
        ret = executeQueryList(sql, new String[]{teamCode}, EmployeeInfo.class);
        return ret;

    }
    public List<EmployeeInfo> getEmployeesTeamCodeBySaleLeader_for_credit(String EMPID,String teamCode) {
        String sql = "SELECT Employee.*, EmployeeDetail.* FROM Employee INNER JOIN EmployeeDetail ON Employee.EmpID = EmployeeDetail.EmployeeCode "
                + "WHERE (EmpID = ?)  AND (PositionCode='Credit') AND PositionCode <> 'Driver'  AND EmployeeDetail.TeamCode = ? AND (Employee.Status IS NULL OR (Employee.Status IS NOT NULL AND Employee.Status <> 'R')) ORDER BY EmployeeDetail.SaleCode ASC";
        List<EmployeeInfo> ret = null;
        ret = executeQueryList(sql, new String[]{EMPID,teamCode}, EmployeeInfo.class);
        return ret;

    }
    public List<EmployeeInfo> getEmployeesTeamCodeBySubTeamLeader(String teamCode, String subTeamCode) {
        String sql = "SELECT Employee.*, EmployeeDetail.* FROM Employee INNER JOIN EmployeeDetail ON Employee.EmpID = EmployeeDetail.EmployeeCode "
                + "WHERE PositionCode <> 'Driver'  AND EmployeeDetail.TeamCode = ? AND (Employee.Status IS NULL OR (Employee.Status IS NOT NULL AND Employee.Status <> 'R')) AND ( EmployeeDetail.SubTeamCode = ? or PositionCode='SaleLeader' or EmployeeDetail.EmployeeTypeCode='100') ORDER BY EmployeeDetail.SaleCode ASC";
        List<EmployeeInfo> ret = null;
        ret = executeQueryList(sql, new String[]{teamCode, subTeamCode}, EmployeeInfo.class);
        return ret;
    }

    public List<EmployeeInfo> getEmployeesTeamCodeBySubTeamLeader_for_credit(String EMPID,String teamCode, String subTeamCode) {
        String sql = "SELECT Employee.*, EmployeeDetail.* FROM Employee INNER JOIN EmployeeDetail ON Employee.EmpID = EmployeeDetail.EmployeeCode "
                + "WHERE PositionCode <> 'Driver'  AND EmployeeDetail.TeamCode = ? AND (Employee.Status IS NULL OR (Employee.Status IS NOT NULL AND Employee.Status <> 'R')) AND ( EmployeeDetail.SubTeamCode = ? or PositionCode='SaleLeader' or EmployeeDetail.EmployeeTypeCode='100')  AND(EmpID = ?) AND (PositionCode='Credit')   ORDER BY EmployeeDetail.SaleCode ASC";
        List<EmployeeInfo> ret = null;
        ret = executeQueryList(sql, new String[]{teamCode, subTeamCode,EMPID}, EmployeeInfo.class);
        return ret;
    }
    public List<EmployeeInfo> getAllTeamMember(String organizationCode, String teamCode, String subTeamCode) {

        // String sql =
        // "SELECT        Emp.EmpID AS EmpID, Emp.OrganizationCode AS OrganizationCode, TEmp.PositionID AS PositionID, Emp.FirstName AS FirstName, Emp.LastName AS LastName, Emp.SaleManType AS SaleManType, Emp.CreateDate AS CreateDate, Emp.CreateBy AS CreateBy,"
        // +
        // " Emp.LastUpdateDate AS LastUpdateDate, Emp.LastUpdateBy AS LastUpdateBy, Emp.FirstName || ' ' || Emp.LastName AS EmpName, ST.SaleTypeName AS SaleManTypeName, TEmp.TeamCode AS TeamCode, TEmp.SubTeamCode AS SubTeamCode,"
        // +
        // " T.Name AS TeamName, TEmp.SaleCode AS SaleCode, Pos.PositionName AS PositionName, TEmp.ResponsibilityZone AS ResponsibilityZone, TEmp.IsActive AS IsActive, TEmp.upperEmployeeID AS upperEmployeeID,"
        // +
        // " EmpUP.FirstName || ' ' || EmpUP.LastName AS upperEmployeeName, Driver.EmpID AS DriverEmpID, Driver.FirstName AS DriverFirstName, Driver.LastName AS DriverLastName"
        // + " FROM            Employee AS Emp LEFT OUTER JOIN"
        // +
        // "                         SaleType AS ST ON Emp.SaleManType = ST.SaleTypeCode INNER JOIN"
        // +
        // "                         (select * from TeamEmployee where subteamcode is not null) AS TEmp ON Emp.OrganizationCode = TEmp.OrganizationCode AND Emp.EmpID = TEmp.EmpID INNER JOIN"
        // +
        // "                         Team AS T ON TEmp.OrganizationCode = T.OrganizationCode AND TEmp.TeamCode = T.Code INNER JOIN"
        // +
        // "                         Position AS Pos ON TEmp.OrganizationCode = Pos.OrganizationCode AND TEmp.PositionID = Pos.PositionID LEFT OUTER JOIN"
        // +
        // "                         Employee AS EmpUP ON TEmp.OrganizationCode = EmpUP.OrganizationCode AND (TEmp.upperEmployeeID = EmpUP.EmpID OR (TEmp.EmpID = EmpUP.EmpID AND TEmp.SubTeamCode is null))"
        // +
        // " LEFT OUTER JOIN (select teamemployee.empid, firstname, lastname, teamemployee.SubTeamCode from teamemployee inner join employee on teamemployee.empid = employee.empid where teamemployee.positionid = 'Driver') AS 'Driver' ON Temp.SubTeamCode = Driver.SubTeamCode"
        // +
        // " WHERE        (TEmp.IsActive = 1) AND (Emp.OrganizationCode=?) AND (TEmp.TeamCode = ?) AND (ST.SaleTypeName is not null) ORDER BY TEmp.TeamCode, TEmp.SubTeamCode";

        // String sql =
        // "SELECT        Emp.EmpID AS EmpID, Emp.OrganizationCode AS OrganizationCode, TEmp.PositionCode AS PositionCode, Emp.FirstName AS FirstName, Emp.LastName AS LastName, TEmp.EmployeeTypeCode AS SaleManType, Emp.CreateDate AS CreateDate, Emp.CreateBy AS CreateBy,"
        // +
        // " Emp.LastUpdateDate AS LastUpdateDate, Emp.LastUpdateBy AS LastUpdateBy, case when Emp.FirstName is null then '' else Emp.FirstName end || ' ' || case when Emp.LastName is null then '' else Emp.LastName end AS EmpName, ST.SaleTypeName AS SaleManTypeName, TEmp.TeamCode AS TeamCode, TEmp.SubTeamCode AS SubTeamCode,"
        // +
        // " T.Name AS TeamName, Pos.PositionName AS PositionName, TEmp.ParentEmployeeCode AS ParentEmployeeCode,"
        // +
        // " EmpUP.FirstName || ' ' || EmpUP.LastName AS ParentEmployeeName, Driver.employeecode AS DriverEmpID, Driver.FirstName AS DriverFirstName, Driver.LastName AS DriverLastName"
        // + " FROM            Employee AS Emp INNER JOIN "
        // +
        // "                         (select * from EmployeeDetail where subteamcode is not null) AS TEmp ON Emp.OrganizationCode = TEmp.OrganizationCode AND Emp.EmpID = TEmp.EmployeeCode INNER JOIN"
        // +
        // "                         Team AS T ON TEmp.OrganizationCode = T.OrganizationCode AND TEmp.TeamCode = T.Code INNER JOIN"
        // +
        // "                         Position AS Pos ON TEmp.OrganizationCode = Pos.OrganizationCode AND TEmp.PositionCode = Pos.PositionID LEFT OUTER JOIN"
        // +
        // "                         Employee AS EmpUP ON TEmp.OrganizationCode = EmpUP.OrganizationCode AND (TEmp.ParentEmployeeCode = EmpUP.EmpID OR (TEmp.EmployeeCode = EmpUP.EmpID AND TEmp.SubTeamCode is null))"
        // +
        // " LEFT OUTER JOIN (select employeedetail.employeecode, firstname, lastname, employeedetail.SubTeamCode from employeedetail inner join employee on employeedetail.employeecode = employee.empid where employeedetail.positioncode = 'Driver') AS 'Driver' ON Temp.SubTeamCode = Driver.SubTeamCode LEFT OUTER JOIN"
        // +
        // "                         SaleType AS ST ON TEmp.EmployeeTypeCode = ST.SaleTypeCode"
        // +
        // " WHERE         (Emp.OrganizationCode=?) AND (TEmp.TeamCode = ?) AND (ST.SaleTypeName is not null) ORDER BY TEmp.TeamCode, TEmp.SubTeamCode";

        List<EmployeeInfo> ret = null;

        if (subTeamCode == null) {
            String sql = "SELECT EmployeeDetail.*,Employee.EmpID,Employee.FirstName,Employee.LastName,Position.PositionID,Position.PositionName,Position.ParentPosition "
                    + "FROM EmployeeDetail INNER JOIN  Employee on EmployeeDetail.EmployeeCode=Employee.EmpID INNER JOIN Position on EmployeeDetail.positionCode=Position.PositionID "
                    + "WHERE (Employee.Status IS NULL OR (Employee.Status IS NOT NULL AND Employee.Status <> 'R')) AND (EmployeeDetail.OrganizationCode = ?) AND (EmployeeDetail.TeamCode = ?) AND (EmployeeDetail.SubTeamCode is not null) ORDER BY EmployeeDetail.TeamCode, EmployeeDetail.SubTeamCode asc, Position.PositionLevel desc, Position.PositionLevel desc, EmployeeDetail.SaleCode asc";

            ret = executeQueryList(sql, new String[]{organizationCode, teamCode}, EmployeeInfo.class);// PAK23
        } else {
            String sql = "SELECT EmployeeDetail.*,Employee.EmpID,Employee.FirstName,Employee.LastName,Position.PositionID,Position.PositionName,Position.ParentPosition "
                    + "FROM EmployeeDetail INNER JOIN  Employee on EmployeeDetail.EmployeeCode=Employee.EmpID INNER JOIN Position on EmployeeDetail.positionCode=Position.PositionID "
                    + "WHERE (Employee.Status IS NULL OR (Employee.Status IS NOT NULL AND Employee.Status <> 'R')) AND (EmployeeDetail.OrganizationCode = ?) AND (EmployeeDetail.TeamCode = ?) AND (EmployeeDetail.SubTeamCode = ?) ORDER BY EmployeeDetail.TeamCode, EmployeeDetail.SubTeamCode asc, Position.PositionLevel desc, Position.PositionLevel desc, EmployeeDetail.SaleCode asc";

            ret = executeQueryList(sql, new String[]{organizationCode, teamCode, subTeamCode}, EmployeeInfo.class);// PAK23
        }
        return ret;
    }

    public EmployeeInfo getSaleTeamByTeamCode(String organizationCode, String teamCode, String empID) {
        // String sql =
        // "SELECT  Sale.EmpID as EmpID, Sale.OrganizationCode as OrganizationCode, Sale.PositionID as PositionID, Sale.FirstName as FirstName, Sale.LastName as LastName,"
        // +
        // " 							Sale.SaleManType as SaleManType, Sale.CreateDate as CreateDate, Sale.CreateBy as CreateBy, Sale.LastUpdateDate as LastUpdateDate, Sale.LastUpdateBy as LastUpdateBy,"
        // +
        // "                       	Sale.TeamCode as TeamCode, Sale.SubTeamCode, Sale.SaleCode as SaleCode, Sale.ResponsibilityZone as ResponsibilityZone, Sale.IsActive as IsActive, Sale.upperEmployeeID as upperEmployeeID,"
        // +
        // "	                         Driver.DriverEmpID as DriverEmpID, Driver.DriverFirstName as DriverFirstName, Driver.DriverLastName as DriverLastName,"
        // +
        // "	                         SaleLeader.SaleLeaderEmpID as SaleLeaderEmpID, SaleLeader.SaleLeaderFirstName as SaleLeaderFirstName, SaleLeader.SaleLeaderLastName as SaleLeaderLastName, SaleLeader.SaleLeaderTeamCode as SaleLeaderTeamCode,"
        // +
        // "	                         Supervisor.SupervisorEmpID as SupervisorEmpID, Supervisor.SupervisorFirstName as SupervisorFirstName, Supervisor.SupervisorLastName as SupervisorLastName,"
        // +
        // "	                         LineManager.LineManagerEmpID as LineManagerEmpID, LineManager.LineManagerFirstName as LineManagerFirstName, LineManager.LineManagerLastName as LineManagerLastName,"
        // +
        // "	                         Manager.ManagerEmpID as ManagerEmpID, Manager.ManagerFirstName as ManagerFirstName, Manager.ManagerLastName as ManagerLastName"
        // +
        // "	FROM            (SELECT        emp.EmpID, emp.OrganizationCode, emp.PositionID, emp.FirstName, emp.LastName, emp.SaleManType, emp.CreateDate, emp.CreateBy,"
        // +
        // "	                                                    emp.LastUpdateDate, emp.LastUpdateBy, temp.TeamCode, temp.SubTeamCode, temp.SaleCode, temp.upperEmployeeID, temp.IsActive,"
        // +
        // "	                                                    temp.ResponsibilityZone"
        // +
        // "	                          FROM            Employee AS emp INNER JOIN"
        // +
        // "	                                                    (select * from TeamEmployee where SubTeamCode is not null) AS temp ON emp.EmpID = temp.EmpID"
        // +
        // "	                          WHERE        (emp.PositionID in ('Sale','SaleLeader'))) AS Sale LEFT OUTER JOIN"
        // +
        // "	                             (SELECT        emp.EmpID AS DriverEmpID, emp.OrganizationCode AS DriverOrganizationCode, emp.PositionID AS DriverPositionID,"
        // +
        // "	                                                         emp.FirstName AS DriverFirstName, emp.LastName AS DriverLastName, emp.SaleManType AS DriverSaleManType,"
        // +
        // "	                                                         temp.TeamCode AS DriverTeamCode, temp.SubTeamCode AS DriverSubTeamCode, temp.SaleCode AS DriverSaleCode, temp.upperEmployeeID AS DriverupperEmployeeID"
        // +
        // "	                               FROM            Employee AS emp INNER JOIN"
        // +
        // "	                                                         (select * from TeamEmployee where SubTeamCode is not null) AS temp ON emp.EmpID = temp.EmpID"
        // +
        // "	                               WHERE        (emp.PositionID = 'Driver')) AS Driver ON Sale.OrganizationCode = Driver.DriverOrganizationCode AND"
        // +
        // "	                         Sale.TeamCode = Driver.DriverTeamCode AND Sale.SubTeamCode = Driver.DriverSubTeamCode LEFT OUTER JOIN"
        // +
        // "	                             (SELECT        emp.EmpID AS SaleLeaderEmpID, emp.OrganizationCode AS SaleLeaderOrganizationCode, emp.PositionID AS SaleLeaderPositionID,"
        // +
        // "	                                                         emp.FirstName AS SaleLeaderFirstName, emp.LastName AS SaleLeaderLastName, emp.SaleManType AS SaleLeaderSaleManType,"
        // +
        // "	                                                         temp.TeamCode AS SaleLeaderTeamCode, temp.SaleCode AS SaleLeaderSaleCode, "
        // +
        // "	                                                         temp.upperEmployeeID AS SaleLeaderupperEmployeeID"
        // +
        // "	                               FROM            Employee AS emp INNER JOIN"
        // +
        // "	                                                         (select * from TeamEmployee where SubTeamCode is not null) AS temp ON emp.EmpID = temp.EmpID"
        // +
        // "	                               WHERE        (emp.PositionID = 'SaleLeader')) AS SaleLeader ON Sale.upperEmployeeID = SaleLeader.SaleLeaderEmpID OR (Sale.EmpID = SaleLeader.SaleLeaderEmpID AND Sale.PositionID = 'SaleLeader') AND"
        // +
        // "	                         Sale.OrganizationCode = SaleLeader.SaleLeaderOrganizationCode AND Sale.TeamCode = SaleLeader.SaleLeaderTeamCode LEFT OUTER JOIN"
        // +
        // "	                             (SELECT        emp.EmpID AS SupervisorEmpID, emp.OrganizationCode AS SupervisorOrganizationCode, emp.PositionID AS SupervisorPositionID,"
        // +
        // "	                                                         emp.FirstName AS SupervisorFirstName, emp.LastName AS SupervisorLastName, emp.SaleManType AS SupervisorSaleManType,"
        // +
        // "	                                                         temp.TeamCode AS SupervisorTeamCode, temp.SaleCode AS SupervisorSaleCode, "
        // +
        // "	                                                         temp.upperEmployeeID AS SupervisorupperEmployeeID"
        // +
        // "	                               FROM            Employee AS emp INNER JOIN"
        // +
        // "	                                                         TeamEmployee AS temp ON emp.EmpID = temp.EmpID"
        // +
        // "	                               WHERE        (emp.PositionID = 'Supervisor')) AS Supervisor ON Supervisor.SupervisorEmpID = SaleLeader.SaleLeaderupperEmployeeID AND"
        // +
        // "	                         Supervisor.SupervisorOrganizationCode = SaleLeader.SaleLeaderOrganizationCode AND "
        // +
        // "	                         Supervisor.SupervisorTeamCode = SaleLeader.SaleLeaderTeamCode LEFT OUTER JOIN"
        // +
        // "	                             (SELECT        emp.EmpID AS LineManagerEmpID, emp.OrganizationCode AS LineManagerOrganizationCode, emp.PositionID AS LineManagerPositionID,"
        // +
        // "	                                                         emp.FirstName AS LineManagerFirstName, emp.LastName AS LineManagerLastName, emp.SaleManType AS LineManagerSaleManType,"
        // +
        // "	                                                         temp.TeamCode AS LineManagerTeamCode, temp.SaleCode AS LineManagerSaleCode, "
        // +
        // "	                                                         temp.upperEmployeeID AS LineManagerupperEmployeeID"
        // +
        // "	                               FROM            Employee AS emp INNER JOIN"
        // +
        // "	                                                         TeamEmployee AS temp ON emp.EmpID = temp.EmpID"
        // +
        // "	                               WHERE        (emp.PositionID = 'LineManger')) AS LineManager ON LineManager.LineManagerEmpID = Supervisor.SupervisorupperEmployeeID AND"
        // +
        // "	                         LineManager.LineManagerOrganizationCode = Supervisor.SupervisorOrganizationCode AND "
        // +
        // "	                         LineManager.LineManagerTeamCode = Supervisor.SupervisorTeamCode LEFT OUTER JOIN"
        // +
        // "	                             (SELECT        emp.EmpID AS ManagerEmpID, emp.OrganizationCode AS ManagerOrganizationCode, emp.PositionID AS ManagerPositionID,"
        // +
        // "	                                                         emp.FirstName AS ManagerFirstName, emp.LastName AS ManagerLastName, emp.SaleManType AS ManagerSaleManType,"
        // +
        // "	                                                         temp.TeamCode AS ManagerTeamCode, temp.SaleCode AS ManagerSaleCode, temp.upperEmployeeID AS ManagerupperEmployeeID"
        // +
        // "	                               FROM            Employee AS emp INNER JOIN"
        // +
        // "	                                                         TeamEmployee AS temp ON emp.EmpID = temp.EmpID"
        // +
        // "	                               WHERE        (emp.PositionID = 'Manager')) AS Manager ON Manager.ManagerEmpID = LineManager.LineManagerupperEmployeeID AND"
        // +
        // "	                         Manager.ManagerOrganizationCode = LineManager.LineManagerOrganizationCode AND "
        // +
        // "	                         Manager.ManagerTeamCode = LineManager.LineManagerTeamCode"
        // +
        // "	WHERE        (Sale.OrganizationCode = ?) AND (Sale.TeamCode = ?) AND (Sale.EmpID = ?)";

        String sql = "SELECT  Sale.EmpID as EmpID, Sale.OrganizationCode as OrganizationCode, Sale.PositionCode as PositionCode, Sale.FirstName as FirstName, Sale.LastName as LastName,"
                + " 							Sale.EmployeeTypeCode as EmployeeTypeCode, Sale.CreateDate as CreateDate, Sale.CreateBy as CreateBy, Sale.LastUpdateDate as LastUpdateDate, Sale.LastUpdateBy as LastUpdateBy,"
                + "                       	Sale.TeamCode as TeamCode, Sale.SubTeamCode, Sale.ParentEmployeeCode as ParentEmployeeCode,"
                + "	                         Driver.DriverEmpID as DriverEmpID, Driver.DriverFirstName as DriverFirstName, Driver.DriverLastName as DriverLastName,"
                + "	                         SaleLeader.SaleLeaderEmpID as SaleLeaderEmpID, SaleLeader.SaleLeaderFirstName as SaleLeaderFirstName, SaleLeader.SaleLeaderLastName as SaleLeaderLastName, SaleLeader.SaleLeaderTeamCode as SaleLeaderTeamCode,"
                + "	                         Supervisor.SupervisorEmpID as SupervisorEmpID, Supervisor.SupervisorFirstName as SupervisorFirstName, Supervisor.SupervisorLastName as SupervisorLastName,"
                + "	                         LineManager.LineManagerEmpID as LineManagerEmpID, LineManager.LineManagerFirstName as LineManagerFirstName, LineManager.LineManagerLastName as LineManagerLastName,"
                + "	                         Manager.ManagerEmpID as ManagerEmpID, Manager.ManagerFirstName as ManagerFirstName, Manager.ManagerLastName as ManagerLastName"
                + "	FROM            (SELECT        emp.EmpID, emp.OrganizationCode, temp.PositionCode, emp.FirstName, emp.LastName, temp.EmployeeTypeCode, emp.CreateDate, emp.CreateBy,"
                + "	                                                    emp.LastUpdateDate, emp.LastUpdateBy, temp.TeamCode, temp.SubTeamCode, temp.ParentEmployeeCode "
                + "	                          FROM            Employee AS emp INNER JOIN"
                + "	                                                    (select * from employeedetail where SubTeamCode is not null or positioncode = 'SaleLeader') AS temp ON emp.EmpID = temp.EmployeeCode"
                + "	                          WHERE        (temp.PositionCode in ('Sale','SaleLeader','SubTeamLeader'))) AS Sale LEFT OUTER JOIN"
                + "	                             (SELECT        emp.EmpID AS DriverEmpID, emp.OrganizationCode AS DriverOrganizationCode, temp.PositionCode AS DriverPositionID,"
                + "	                                                         emp.FirstName AS DriverFirstName, emp.LastName AS DriverLastName, temp.EmployeeTypeCode AS DriverSaleManType,"
                + "	                                                         temp.TeamCode AS DriverTeamCode, temp.SubTeamCode AS DriverSubTeamCode, temp.ParentEmployeeCode AS DriverupperEmployeeID"
                + "	                               FROM            Employee AS emp INNER JOIN"
                + "	                                                         (select * from employeedetail where SubTeamCode is not null) AS temp ON emp.EmpID = temp.EmployeeCode"
                + "	                               WHERE        (temp.PositionCode = 'Driver')) AS Driver ON Sale.OrganizationCode = Driver.DriverOrganizationCode AND"
                + "	                         Sale.TeamCode = Driver.DriverTeamCode AND Sale.SubTeamCode = Driver.DriverSubTeamCode LEFT OUTER JOIN"
                + "	                             (SELECT        emp.EmpID AS SaleLeaderEmpID, emp.OrganizationCode AS SaleLeaderOrganizationCode, temp.PositionCode AS SaleLeaderPositionID,"
                + "	                                                         emp.FirstName AS SaleLeaderFirstName, emp.LastName AS SaleLeaderLastName, temp.EmployeeTypeCode AS SaleLeaderSaleManType,"
                + "	                                                         temp.TeamCode AS SaleLeaderTeamCode,  "
                + "	                                                         temp.ParentEmployeeCode AS SaleLeaderupperEmployeeID"
                + "	                               FROM            Employee AS emp INNER JOIN"
                + "	                                                         (select * from employeedetail ) AS temp ON emp.EmpID = temp.EmployeeCode"
                + "	                               WHERE        (temp.PositionCode = 'SaleLeader')) AS SaleLeader ON (Sale.ParentEmployeeCode = SaleLeader.SaleLeaderEmpID OR (Sale.EmpID = SaleLeader.SaleLeaderEmpID AND Sale.PositionCode = 'SaleLeader')) AND"
                + "	                         Sale.OrganizationCode = SaleLeader.SaleLeaderOrganizationCode AND Sale.TeamCode = SaleLeader.SaleLeaderTeamCode LEFT OUTER JOIN"
                + "	                             (SELECT        emp.EmpID AS SupervisorEmpID, emp.OrganizationCode AS SupervisorOrganizationCode, temp.PositionCode AS SupervisorPositionID,"
                + "	                                                         emp.FirstName AS SupervisorFirstName, emp.LastName AS SupervisorLastName, temp.EmployeeTypeCode AS SupervisorSaleManType,"
                + "	                                                         temp.TeamCode AS SupervisorTeamCode, "
                + "	                                                         temp.ParentEmployeeCode AS SupervisorupperEmployeeID"
                + "	                               FROM            Employee AS emp INNER JOIN"
                + "	                                                         employeedetail AS temp ON emp.EmpID = temp.EmployeeCode"
                + "	                               WHERE        (temp.PositionCode = 'Supervisor')) AS Supervisor ON Supervisor.SupervisorEmpID = SaleLeader.SaleLeaderupperEmployeeID AND"
                + "	                         Supervisor.SupervisorOrganizationCode = SaleLeader.SaleLeaderOrganizationCode  "
                + "	                         LEFT OUTER JOIN"
                + "	                             (SELECT        emp.EmpID AS LineManagerEmpID, emp.OrganizationCode AS LineManagerOrganizationCode, temp.PositionCode AS LineManagerPositionID,"
                + "	                                                         emp.FirstName AS LineManagerFirstName, emp.LastName AS LineManagerLastName, temp.EmployeeTypeCode AS LineManagerSaleManType,"
                + "	                                                         temp.TeamCode AS LineManagerTeamCode,  "
                + "	                                                         temp.ParentEmployeeCode AS LineManagerupperEmployeeID"
                + "	                               FROM            Employee AS emp INNER JOIN"
                + "	                                                         employeedetail AS temp ON emp.EmpID = temp.EmployeeCode"
                + "	                               WHERE        (temp.PositionCode = 'LineManager')) AS LineManager ON LineManager.LineManagerEmpID = Supervisor.SupervisorupperEmployeeID AND"
                + "	                         LineManager.LineManagerOrganizationCode = Supervisor.SupervisorOrganizationCode  "
                + "	                         LEFT OUTER JOIN"
                + "	                             (SELECT        emp.EmpID AS ManagerEmpID, emp.OrganizationCode AS ManagerOrganizationCode, temp.PositionCode AS ManagerPositionID,"
                + "	                                                         emp.FirstName AS ManagerFirstName, emp.LastName AS ManagerLastName, temp.EmployeeTypeCode AS ManagerSaleManType,"
                + "	                                                         temp.TeamCode AS ManagerTeamCode, temp.ParentEmployeeCode AS ManagerupperEmployeeID"
                + "	                               FROM            Employee AS emp INNER JOIN"
                + "	                                                         employeedetail AS temp ON emp.EmpID = temp.EmployeeCode"
                + "	                               WHERE        (temp.PositionCode = 'Manager')) AS Manager ON Manager.ManagerEmpID = LineManager.LineManagerupperEmployeeID AND"
                + "	                         Manager.ManagerOrganizationCode = LineManager.LineManagerOrganizationCode "
                + "	WHERE        (Sale.OrganizationCode = ?) AND (Sale.TeamCode = ?) AND ((Sale.EmpID = ?) OR ((SaleLeader.SaleLeaderEmpID = ?) AND (Sale.PositionCode = 'Sale')))";

        // return executeQueryObject(sql, new String[] { organizationCode,
        // teamCode, empID}, EmployeeInfo.class);//"PAK0687"
        List<EmployeeInfo> empList = executeQueryList(sql, new String[]{organizationCode, teamCode, empID, empID}, EmployeeInfo.class);
        EmployeeInfo result = null;
        if (empList != null) {
            result = empList.get(0);
        }
        return result;
    }

    /**
     * OLDEST **
     */

/*    public EmployeeInfo getEmpID(String EmpID) {
        final String QUERY_EMPLOYEE_GET_BY_ID = "SELECT * FROM Employee WHERE EmpID = ?";
        return executeQueryObject(QUERY_EMPLOYEE_GET_BY_ID, new String[]{EmpID}, EmployeeInfo.class);
    }*/

 /*   public EmployeeInfo getEmpByEmpID(String empID, String teamCode) {
         String QUERY_EMPLOYEE_GET_BY_ID = "SELECT Employee .* ,EmployeeDetail.* FROM Employee INNER JOIN EmployeeDetail on Employee.EmpID = EmployeeDetail.EmployeeCode WHERE (EmpID = ? and teamCode=? )";
         return executeQueryObject(QUERY_EMPLOYEE_GET_BY_ID, new String[]{empID, teamCode}, EmployeeInfo.class);

    }*/


    public EmployeeInfo getEmpID(String EmpID) {
        final String QUERY_EMPLOYEE_GET_BY_ID = "SELECT * FROM Employee WHERE EmpID = ?";
        return executeQueryObject(QUERY_EMPLOYEE_GET_BY_ID, new String[]{EmpID}, EmployeeInfo.class);
    }

    public EmployeeInfo getEmpByEmpID(String empID, String teamCode) {
        final String QUERY_EMPLOYEE_GET_BY_ID = "SELECT Employee .* ,EmployeeDetail.* FROM Employee INNER JOIN EmployeeDetail on Employee.EmpID = EmployeeDetail.EmployeeCode WHERE (EmpID = ?) AND (PositionCode='Sale')" +
                " AND EmployeeDetail.TeamCode = ?";
        return executeQueryObject(QUERY_EMPLOYEE_GET_BY_ID, new String[]{empID, teamCode}, EmployeeInfo.class);
    }

    public EmployeeInfo getEmpByEmpID_for_credit(String empID ,String teamCode) {
        final String QUERY_EMPLOYEE_GET_BY_ID = "SELECT Employee .* ,EmployeeDetail.* FROM Employee INNER JOIN EmployeeDetail on Employee.EmpID = EmployeeDetail.EmployeeCode WHERE  (EmpID = ?) AND (PositionCode='Credit')" +
                " AND EmployeeDetail.TeamCode = ?";
        return executeQueryObject(QUERY_EMPLOYEE_GET_BY_ID, new String[]{ empID,teamCode}, EmployeeInfo.class);
    }

/*    public EmployeeInfo getEmpByEmpID_for_credit(String empID, String teamCode) {
        String QUERY_EMPLOYEE_GET_BY_ID = "SELECT Employee .* ,EmployeeDetail.* FROM Employee INNER JOIN EmployeeDetail on Employee.EmpID = EmployeeDetail.EmployeeCode WHERE (EmpID = ?)";
        return executeQueryObject(QUERY_EMPLOYEE_GET_BY_ID, new String[]{empID}, EmployeeInfo.class);

    }*/


    public void deleteEmployeeByID(String empid) {
        String sql = "DELETE FROM Employee where EmpID=?";
        executeNonQuery(sql, new String[]{empid});
    }

    public void deleteEmployeeByTeamCode(String teamcode) {
        String sql = "DELETE " + " FROM Employee" + " WHERE EmpID IN ( SELECT EmployeeCode FROM EmployeeDetail WHERE TeamCode = ? )";
        executeNonQuery(sql, new String[]{teamcode});
    }

    public void deleteEmployeeAll() {
        String sql = "delete from Employee";
        executeNonQuery(sql, null);
    }

    public List<EmployeeInfo> getEmployee() {
        return executeQueryList("select *from Employee", null, EmployeeInfo.class);
    }

    public List<EmployeeInfo> getEmployeeForPreSale() {
        String sql = "select ed.SaleCode, e.FirstName, e.LastName " +
                "from Employee as e " +
                "inner join EmployeeDetail as ed on ed.EmployeeCode = e.EmpID and ifnull(ed.SaleCode, '') <> '' " +
                "where e.EmpID <> '999999' " +
                "order by ed.SaleCode asc";

        return executeQueryList(sql, null, EmployeeInfo.class);
    }

    public List<EmployeeInfo> getEmployeeForPreSale_SETTING(String EmployeeCode) {
        String sql = "select ed.SaleCode, e.FirstName, e.LastName " +
                "from Employee as e " +
                "inner join EmployeeDetail as ed on ed.EmployeeCode = e.EmpID and ifnull(ed.SaleCode, '') <> '' " +
                "where e.EmpID = ?  " +
                "order by ed.SaleCode asc";

        return executeQueryList(sql, new String[]{EmployeeCode}, EmployeeInfo.class);
    }


    public EmployeeInfo getEmployeeSaleCodeByEmployeeCode(String EmployeeCode) {
        String sql = "SELECT e.*, ed.SaleCode " +
                " FROM Employee e" +
                " LEFT OUTER JOIN EmployeeDetail ed on ed.EmployeeCode = e.EmpID AND ed.SaleCode IS NOT NULL AND ed.PositionCode = ?" +
                " WHERE e.EmpID = ? ";
        return executeQueryObject(sql, new String[]{PositionController.PositionCode.Sale.toString(), EmployeeCode}, EmployeeInfo.class);
    }

    public EmployeeInfo getEmployeeByEmployeeIDAndPositionCode (String OrganizationCode, String empID, String PositionCode, String teamCode) {
        String sql = "select ed.SaleCode," +
                "	ifnull(e.FirstName, '') || ' ' || ifnull(e.LastName, '') as SaleEmployeeName," +
                "	ed.TeamCode," +
                "	(select ifnull(e1.FirstName, '') || ' ' || ifnull(e1.LastName, '')" +
                "	from EmployeeDetail as ed1" +
                "	INNER JOIN Employee as e1 ON e1.EmpID =ed1. EmployeeCode" +
                "	where ed1.PositionCode = 'SaleLeader'" +
                "		and ed1.TeamCode = ed.TeamCode) as upperEmployeeName" +
                " from Employee as e" +
                " INNER JOIN EmployeeDetail as ed ON ed.EmployeeCode = e.EmpID" +
                " where e.OrganizationCode = ?" +
                " and e.EmpID = ?" +
                " and ed.PositionCode = ?";
                if(teamCode != null){
                    sql +=  " and ed.TeamCode = ?";
                    return executeQueryObject(sql, new String[]{OrganizationCode, empID, PositionCode, teamCode}, EmployeeInfo.class);
                } else {
                    return executeQueryObject(sql, new String[]{OrganizationCode, empID, PositionCode}, EmployeeInfo.class);
                }
    }

    /*public EmployeeInfo getEmployeeByEmployeeIDAndPositionCodeSaleAndCerdit (String OrganizationCode, String empID) {
        String sql = "select ed.SaleCode," +
                "	ifnull(e.FirstName, '') || ' ' || ifnull(e.LastName, '') as SaleEmployeeName," +
                "	ed.TeamCode," +
                "	(select ifnull(e1.FirstName, '') || ' ' || ifnull(e1.LastName, '')" +
                "	from EmployeeDetail as ed1" +
                "	INNER JOIN Employee as e1 ON e1.EmpID =ed1. EmployeeCode" +
                "	where ed1.PositionCode = 'SaleLeader'" +
                "		and ed1.TeamCode = ed.TeamCode) as upperEmployeeName" +
                " from Employee as e" +
                " INNER JOIN EmployeeDetail as ed ON ed.EmployeeCode = e.EmpID" +
                " where e.OrganizationCode = ?" +
                " and e.EmpID = ?" +
                " and (ed.PositionCode = ? or ed.PositionCode = ? )";
        return executeQueryObject(sql, new String[]{OrganizationCode, empID, SourceSystem.Sale.toString(), SourceSystem.Credit.toString()}, EmployeeInfo.class);
    }*/

    public EmployeeInfo getEmployeeByTreeHistoryIDAndEmployeeID (String OrganizationCode, String TreeHistoryID , String EmployeeID) {
        String sql = "select edh.SaleCode, " +
                "ifnull(e.FirstName, '') || ' ' || ifnull(e.LastName, '') as SaleEmployeeName, " +
                "edh.TeamCode, " +
                "ifnull(e1.FirstName, '') || ' ' || ifnull(e1.LastName, '') as upperEmployeeName " +
                "from Employee as e " +
                "LEFT JOIN EmployeeDetailHistory as edh ON edh.EmployeeCode = e.EmpID and edh.PositionCode in ('Sale', 'Credit') " +
                "LEFT JOIN EmployeeDetailHistory as edh1 ON edh1.TreeHistoryID = edh.TreeHistoryID and edh1.TeamCode = edh.TeamCode and edh1.PositionCode in ('SaleLeader', 'CreditTeamLeader') " +
                "LEFT JOIN Employee as e1 ON e1.EmpID = edh1.EmployeeCode " +
                "where edh.OrganizationCode = ? " +
                "and edh.TreeHistoryID = ? " +
                "and e.EmpID = ? ";
        return executeQueryObject(sql, new String[]{OrganizationCode, TreeHistoryID, EmployeeID}, EmployeeInfo.class);
    }
}
