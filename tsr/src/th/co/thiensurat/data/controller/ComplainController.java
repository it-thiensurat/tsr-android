package th.co.thiensurat.data.controller;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ComplainInfo;

public class ComplainController extends BaseController {

    public enum ComplainStatus {
        REQUEST, APPROVED, COMPLETED, REJECT
    }

    public enum TaskType {
        SaleAudit, Other
    }

    public void addComplain(ComplainInfo info) {
        String sql = " INSERT INTO Complain (ComplainID, OrganizationCode, RefNo, Status, RequestProblemID, RequestDetail, "
                + "			RequestDate, RequestBy, RequestTeamCode, ApproveDetail, "
                + "			ApprovedDate, ApprovedBy, ResultProblemID, ResultDetail, EffectiveDate, EffectiveBy, ComplainPaperID, "
                + "			CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate, ReferenceID, TaskType, RequestEmployeeLevelPath, EffectiveEmployeeLevelPath)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.ComplainID, info.OrganizationCode, info.RefNo, info.Status
                , info.RequestProblemID, info.RequestDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode
                , info.ApproveDetail, valueOf(info.ApprovedDate), info.ApprovedBy, info.ResultProblemID, info.ResultDetail
                , valueOf(info.EffectiveDate), info.EffectiveBy, info.ComplainPaperID, valueOf(info.CreateDate),
                info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate), info.ReferenceID
                , info.TaskType, info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath});
    }

    public void addComplainStatusREQUEST(ComplainInfo info) {
        String sql = " INSERT INTO Complain (ComplainID, OrganizationCode, RefNo, Status, "
                + "			RequestProblemID, RequestDetail, RequestDate, RequestBy, RequestTeamCode, "
                + "			CreateDate, CreateBy, "
                + "			LastUpdateDate, LastUpdateBy, ReferenceID, TaskType, ComplainPaperID, RequestEmployeeLevelPath) "
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.ComplainID, info.OrganizationCode, info.RefNo, info.Status,
                info.RequestProblemID, info.RequestDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode,
                valueOf(info.CreateDate), info.CreateBy,
                valueOf(info.LastUpdateDate), info.LastUpdateBy, info.ReferenceID, info.TaskType, info.ComplainPaperID, info.RequestEmployeeLevelPath});

        TSRController.updateRunningNumber(TSRController.DocumentGenType.Complain, info.ComplainPaperID, info.RequestBy);
    }

    public void updateComplainStatusAPPROVED(ComplainInfo info) {
        String sql = " UPDATE Complain SET Status = ?, ApproveDetail = ?, ApprovedDate = ?, ApprovedBy = ?, "
                + "			LastUpdateDate = ?, LastUpdateBy = ? , ReferenceID = ?, TaskType = ?"
                + " WHERE (ComplainID = ?) AND (RefNo = ?) AND (OrganizationCode = ?)";
        executeNonQuery(sql, new String[]{info.Status, info.ApproveDetail, valueOf(info.ApprovedDate), info.ApprovedBy,
                valueOf(info.LastUpdateDate), info.LastUpdateBy, info.ReferenceID, info.TaskType,
                info.ComplainID, info.RefNo, info.OrganizationCode});
    }

    public void updateComplainStatusCOMPLETED(ComplainInfo info) {
        String sql = " UPDATE Complain SET Status = ?, ResultProblemID = ?, ResultDetail = ?, EffectiveDate = ?, EffectiveBy = ?, ComplainPaperID = ?, "
                + "			LastUpdateDate = ?, LastUpdateBy = ?  , ReferenceID = ?, TaskType = ?, EffectiveEmployeeLevelPath = ?"
                + " WHERE (ComplainID = ?) AND (RefNo = ?) AND (OrganizationCode = ?)";
        executeNonQuery(sql, new String[]{info.Status, info.ResultProblemID, info.ResultDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.ComplainPaperID,
                valueOf(info.LastUpdateDate), info.LastUpdateBy, info.ReferenceID, info.TaskType, info.EffectiveEmployeeLevelPath,
                info.ComplainID, info.RefNo, info.OrganizationCode});
    }

    public ComplainInfo getLastComplain(String employeeID, String yearMonth) {
        final String sql = "SELECT * FROM Complain "
                + " WHERE (RequestBy = ?) AND (substr(ComplainPaperID, length(ComplainPaperID)-6,4) = ?) "
                + " ORDER BY substr(ComplainPaperID, length(ComplainPaperID)-2,3) DESC";
        List<ComplainInfo> tmpComplain = executeQueryList(sql, new String[]{employeeID, yearMonth}, ComplainInfo.class);

        if (tmpComplain == null)
            return null;
        else
            return tmpComplain.get(0);
    }

    public List<ComplainInfo> getComplainListByTeamCodeAndEmployeeID(String organizationCode, String assigneeTeamCode, String assigneeEmpID, String searchText, String complainID, String[] complainStatus) {
        String sql = "SELECT DISTINCT cp.* " +
                " 			, c.CONTNO, c.EFFDATE, c.SaleCode, c.SaleEmployeeCode, c.SaleTeamCode, c.ProductSerialNumber" +
                " 			, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName, IFNULL(dc.IDCard, '') AS IDCard" +
                " 			, p.ProductName" +
                " 			, IFNULL(e.FirstName, '') || '  ' || IFNULL(e.LastName, '') AS SaleEmployeeName" +
                " 			, IFNULL(headTeam.FirstName, '') || '  ' || IFNULL(headTeam.LastName, '') AS SaleLeaderName" +
                " 			, pbRequest.ProblemName AS RequestProblemName, pbAction.ProblemName AS ResultProblemName" +
                "           , a.AssigneeEmpID" +
                " FROM Complain cp" +
                " 			INNER JOIN Problem pbRequest on pbRequest.ProblemID = cp.RequestProblemID" +
                " 			LEFT OUTER JOIN Problem pbAction on pbAction.ProblemID = cp.ResultProblemID" +
                " 			INNER JOIN Contract c ON c.RefNo = cp.RefNo" +
                " 			INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                " 			INNER JOIN Product p ON p.ProductID = c.ProductID" +
                " 			LEFT OUTER JOIN Assign a ON a.RefNo = cp.RefNo AND a.ReferenceID = cp.ComplainID" +
                " 			LEFT OUTER JOIN Employee e ON e.EmpID = c.SaleEmployeeCode " +
                " 			LEFT OUTER JOIN EmployeeDetailHistory AS ed ON ed.TreeHistoryID = c.SaleEmployeeLevelPath and ed.EmployeeCode = e.EmpID AND ed.SaleCode = c.SaleCode AND ed.TeamCode = c.SaleTeamCode" +
                " 			LEFT OUTER JOIN Employee headTeam ON headTeam.EmpID = ed.TeamHeadCode" +
                " WHERE (c.OrganizationCode = ?)";

        ArrayList<String> args = new ArrayList<>();
        args.add(organizationCode);

        if (complainStatus != null && complainStatus.length > 0) {
            sql += " AND (cp.Status IN (" + BHUtilities.makePlaceholders(complainStatus) + "))";
            for(String s : complainStatus) args.add(s);
        }

        if (assigneeTeamCode != null) { // own team assign
            sql += " AND (a.AssigneeTeamCode = ? OR cp.RequestTeamCode = ?)";
            args.add(assigneeTeamCode);
            args.add(assigneeTeamCode);
            if (assigneeEmpID != null) {
                sql += " AND (a.AssigneeEmpID = ? OR cp.RequestBy = ?)";
                args.add(assigneeEmpID);
                args.add(assigneeEmpID);
            }
        } else {
            // other team
        }

        if (complainID != null) {
            sql += " AND (cp.ComplainID = ?)";
            args.add(complainID);
        }

        if (searchText != null) {
            sql += " AND (IFNULL(dc.CustomerName, dc.CompanyName) || IFNULL(dc.IDCard, '') || c.ProductSerialNumber || c.CONTNO LIKE ?)";
            args.add("%" + searchText + "%");
        }

        return executeQueryList(sql, args.toArray(new String[args.size()]), ComplainInfo.class);
    }

    public void deleteComplainAll() {
        executeNonQuery("DELETE FROM Complain", null);
    }

    public ComplainInfo getComplainByComplainID(String ComplainID) {
        final String sql = "SELECT * FROM Complain WHERE (ComplainID = ?)";
        return executeQueryObject(sql, new String[]{ComplainID}, ComplainInfo.class);
    }

    /*** [START] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
    public void deleteComplainByID(String OrganizationCode, String ComplainID) {
        executeNonQuery("DELETE FROM Complain WHERE (OrganizationCode = ?) AND (ComplainID = ?) ", new String[] { OrganizationCode, ComplainID });
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/

}
