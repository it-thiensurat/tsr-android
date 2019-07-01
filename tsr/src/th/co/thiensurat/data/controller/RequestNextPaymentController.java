package th.co.thiensurat.data.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.RequestNextPaymentInfo;

public class RequestNextPaymentController extends BaseController {

    public enum RequestNextPaymentStatus {
        REQUEST, APPROVED, COMPLETED, REJECT
    }


    public void addRequestNextPayment(RequestNextPaymentInfo info) {
        String sql = "INSERT INTO RequestNextPayment (RequestNextPaymentID, OrganizationCode, RefNo, PaymentID, Status, "
                + "       RequestProblemDetail, RequestDate, RequestBy, RequestTeamCode, "
                + "       ApprovedDate, ApproveDetail, ApprovedBy, "
                + "       ResultProblemDetail, EffectiveDate, EffectiveBy, "
                + "       CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, RequestEmployeeLevelPath, EffectiveEmployeeLevelPath) "
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeNonQuery(sql, new String[]{info.RequestNextPaymentID, info.OrganizationCode, info.RefNo, info.PaymentID, info.Status,
                info.RequestProblemDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode,
                valueOf(info.ApprovedDate), info.ApproveDetail, info.ApprovedBy,
                info.ResultProblemDetail, valueOf(info.EffectiveDate), info.EffectiveBy,
                valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath});
    }

    public void updateRequestNextPayment(RequestNextPaymentInfo info) {
        String sql = "UPDATE RequestNextPayment "
                + " SET RefNo = ?, PaymentID = ?, Status = ?, "
                + "         RequestProblemDetail = ?, RequestDate = ?, RequestBy = ?, RequestTeamCode = ?, "
                + "         ApprovedDate = ?, ApproveDetail = ?, ApprovedBy = ?, "
                + "         ResultProblemDetail = ?, EffectiveDate = ?, EffectiveBy = ?, "
                + "         LastUpdateDate = ?, LastUpdateBy = ?, RequestEmployeeLevelPath = ?, EffectiveEmployeeLevelPath = ?"
                + " WHERE (OrganizationCode = ?) AND (RequestNextPaymentID = ?)";

        executeNonQuery(sql, new String[]{info.RefNo, info.PaymentID, info.Status,
                info.RequestProblemDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode,
                valueOf(info.ApprovedDate), info.ApproveDetail, info.ApprovedBy,
                info.ResultProblemDetail, valueOf(info.EffectiveDate), info.EffectiveBy,
                valueOf(info.LastUpdateDate), info.LastUpdateBy, info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath,
                info.OrganizationCode, info.RequestNextPaymentID});
    }

    public RequestNextPaymentInfo getRequestNextPaymentByRequestNextPaymentID(String OrganizationCode, String RequestNextPaymentID) {
        RequestNextPaymentInfo ret = null;
        String sql = "SELECT * FROM RequestNextPayment WHERE (OrganizationCode = ?) AND (RequestNextPaymentID = ?)";
        ret = executeQueryObject(sql, new String[]{OrganizationCode, RequestNextPaymentID}, RequestNextPaymentInfo.class);
        return ret;
    }

    public void deleteRequestNextPaymentAll() {
        executeNonQuery("DELETE FROM RequestNextPayment", null);
    }

    public List<RequestNextPaymentInfo> getRequestNextPaymentByIDOrStatusOrSearchText(String OrganizationCode, String SearchText, String RequestNextPaymentID, String RequestNextPaymentStatus, String RefNo) {
        String sql = "SELECT rnp.*" +
                "       , IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName, dc.IDCard" +
                "       , c.CONTNO, c.EFFDATE, c.ProductSerialNumber, c.SALES, c.TradeInDiscount, c.TotalPrice, c.MODE, c.Status AS ContractStatus " +
                "       , minPeriod.PaymentPeriodNumber AS PaymentPeriodNumber, minPeriod.NetAmount - IFNULL(sppp.SumAmount, 0) AS OutStandingAmount" +
                "       , IFNULL(countHoldSalePaymentPeriod.HoldSalePaymentPeriod, 0) AS HoldSalePaymentPeriod" +
                "       , c.RefNo AS ContractRefNo, c.OrganizationCode AS ContractOrganizationCode" +
                "       , p.ProductName" +
                "       , a.AssigneeTeamCode, a.AssigneeEmpID" +
                "       , IFNULL(resultPaidAmount.PaidAmount, 0) AS PaidAmount" +
                /*
                "       , CASE WHEN IFNULL(resultOutstandingTotal.NextPaymentPeriodNumber, 0) = 0 OR minPeriod.PaymentPeriodNumber = c.MODE " +
                "               THEN 0 " +
                "               ELSE CASE WHEN resultOutstandingTotal.NextPaymentPeriodNumber = minPeriod.PaymentPeriodNumber " +
                "                       THEN resultOutstandingTotal.NextPaymentPeriodNumber + 1 " +
                "                       ELSE resultOutstandingTotal.NextPaymentPeriodNumber" +
                "                   END" +
                "       END AS NextPaymentPeriodNumber " +
                */
                "       , IFNULL(resultOutstandingTotal.NextPaymentPeriodNumber, 0) AS NextPaymentPeriodNumber" +
                /*
                "       , CASE WHEN IFNULL(resultOutstandingTotal.NextPaymentPeriodNumber, 0) = 0 OR minPeriod.PaymentPeriodNumber = c.MODE " +
                "               THEN 0 " +
                "               ELSE CASE WHEN resultOutstandingTotal.NextPaymentPeriodNumber = minPeriod.PaymentPeriodNumber " +
                "                       THEN resultOutstandingTotal.OutstandingTotal - minPeriod.NetAmount " +
                "                       ELSE resultOutstandingTotal.OutstandingTotal" +
                "                   END" +
                "       END AS OutstandingTotal " +
                */
                "       , IFNULL(resultOutstandingTotal.OutstandingTotal, 0) AS OutstandingTotal" +
                "       , assigneeDefault.AssigneeEmpID AS AssigneeDefaultAssigneeEmpID" +
                " FROM Contract c" +
                "       INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                "       INNER JOIN Product p ON p.ProductID = c.ProductID" +
                "       INNER JOIN SaleAudit sa ON sa.RefNo = c.RefNo AND sa.IsPassSaleAudit = 1" +
                "       INNER JOIN SalePaymentPeriod lastPeriod ON lastPeriod.RefNo = c.RefNo AND lastPeriod.PaymentPeriodNumber = c.MODE " +
                "               AND lastPeriod.PaymentComplete = 0" +
                "       INNER JOIN (" +
                "               SELECT MIN(PaymentPeriodNumber) AS PaymentPeriodNumber, RefNo " +
                "               FROM SalePaymentPeriod " +
                "               WHERE PaymentComplete = 0 " +
                "               GROUP BY RefNo" +
                "       ) AS findMinPeriod ON findMinPeriod.RefNo = c.RefNo AND findMinPeriod.PaymentPeriodNumber > 1" +
                "       INNER JOIN SalePaymentPeriod minPeriod ON minPeriod.RefNo = c.RefNo AND minPeriod.PaymentPeriodNumber = findMinPeriod.PaymentPeriodNumber" +
                "       LEFT OUTER JOIN Assign assigneeDefault ON assigneeDefault.ReferenceID = minPeriod.SalePaymentPeriodID" +
                "       LEFT OUTER JOIN (" +
                "               SELECT SUM(Amount) AS SumAmount, SalePaymentPeriodID" +
                "               FROM SalePaymentPeriodPayment" +
                "               GROUP BY SalePaymentPeriodID" +
                "       ) AS sppp ON sppp.SalePaymentPeriodID = minPeriod.SalePaymentPeriodID" +
                "       LEFT OUTER JOIN RequestNextPayment rnp ON rnp.RefNo = c.RefNo " +
                "               AND (rnp.Status = '" + RequestNextPaymentController.RequestNextPaymentStatus.REQUEST.toString() + "' " +
                "                   OR rnp.Status = '" + RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString() + "')" +
                "       LEFT OUTER JOIN (" +
                "               SELECT COUNT(*) AS HoldSalePaymentPeriod, RefNo" +
                "               FROM SalePaymentPeriod" +
                "               WHERE PaymentComplete = 0 AND DATE(PaymentDueDate) < DATE('now')" +
                "               GROUP BY RefNo" +
                "       ) AS countHoldSalePaymentPeriod ON countHoldSalePaymentPeriod.RefNo = c.RefNo" +
                "       LEFT OUTER JOIN (" +
                "               SELECT SUM(PAYAMT) AS PaidAmount, RefNo" +
                "               FROM Payment" +
                "               GROUP BY RefNo" +
                "       ) AS resultPaidAmount ON resultPaidAmount.RefNo = c.RefNo" +
//                " LEFT OUTER JOIN (" +
//                "       SELECT MAX(PaymentPeriodNumber) AS PaymentPeriodNumber" +
//                "       FROM SalePaymentPeriod " +
//                "       WHERE DATE(PaymentDueDate) <= DATE('now') AND sppOutStanding.PaymentComplete = 0" +
//                "       GROUP BY RefNo" +
//                " ) AS resultNextPeriodNumber ON resultNextPeriodNumber.RefNo = c.RefNo" +
                "       LEFT OUTER JOIN (" +
                "               SELECT SUM(NetAmount) AS OutstandingTotal, MIN(PaymentPeriodNumber) AS NextPaymentPeriodNumber, RefNo" +
                "               FROM SalePaymentPeriod" +
                //"               WHERE (DATE(PaymentDueDate) > DATE('now') OR DATE(PaymentAppointmentDate) > DATE('now')) " +
                //"                   AND PaymentComplete = 0" +
                "               WHERE PaymentComplete = 0" +
                "               GROUP BY RefNo" +
                "       ) AS resultOutstandingTotal ON resultOutstandingTotal.RefNo = c.RefNo" +
                "       LEFT OUTER JOIN Assign a ON a.RefNo = c.RefNo AND a.TaskType = '" + AssignController.AssignTaskType.RequestNextPayment.toString() + "'" +
                "               AND a.ReferenceID = rnp.RequestNextPaymentID" +
                " WHERE (c.isActive = 1) AND (c.STATUS = '" + ContractInfo.ContractStatus.NORMAL.toString() + "') AND (c.OrganizationCode = ?)" +
                "       AND (c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE (StatusName = 'COMPLETED'))) " +
                "       AND (rnp.RequestNextPaymentID IS NULL OR (rnp.RequestNextPaymentID IS NOT NULL AND (rnp.Status <> 'APPROVED' OR (rnp.Status = 'APPROVED' AND a.AssignID IS NOT NULL))))";

        ArrayList<String> args = new ArrayList<>();
        args.add(OrganizationCode);

        if (RefNo != null) {
            sql += " AND (c.RefNo = ?)";
            args.add(RefNo);
        }

        if (RequestNextPaymentID != null) {
            sql += " AND (rnp.RequestNextPaymentID = ?)";
            args.add(RequestNextPaymentID);
        }

        if (RequestNextPaymentStatus != null) {
            sql += " AND (rnp.Status = ?)";
            args.add(RequestNextPaymentStatus);
        }

        if (SearchText != null) {
            sql += " AND (IFNULL(dc.CustomerName, dc.CompanyName) || IFNULL(dc.IDCard, '') || IFNULL(dc.AuthorizedIDCard, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ?)";
            args.add("%" + SearchText + "%");
        }

        sql += " ORDER BY rnp.RequestDate DESC ";
        return executeQueryList(sql, args.toArray(new String[args.size()]), RequestNextPaymentInfo.class);
    }

    /***
     * [START] - Fixed - [Android-เก็บเงินค่างวด] เปลี่ยนเงื่อนไขการนับจำนวนงวดที่ค้าง
     ***/

    public List<RequestNextPaymentInfo> getNewRequestNextPaymentForNextPaymentListFragment(String SearchText, String OrganizationCode, String RefNo, String EmpID, String TeamCode, String sourceSystem, String positionCode)  {
        ArrayList<String> args = new ArrayList<>();
        String sql = "select c.RefNo as ContractRefNo, c.CONTNO, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName, " +
                "            rnp.Status, rnp.RequestNextPaymentID, " +
                "            IFNULL(countHoldSalePaymentPeriod.HoldSalePaymentPeriod, 0) AS HoldSalePaymentPeriod " +
                "     from Contract as c " +
                "          INNER JOIN DebtorCustomer as dc ON dc.CustomerID = c.CustomerID and dc.OrganizationCode = c.OrganizationCode " +
                "          INNER JOIN SaleAudit as sa ON sa.RefNo = c.RefNo and sa.OrganizationCode = c.OrganizationCode and sa.IsPassSaleAudit = 1 " +
                "          LEFT JOIN Assign as a ON a.TaskType in (?, ?) and a.RefNo = c.RefNo and a.OrganizationCode = c.OrganizationCode " +
                "          LEFT JOIN RequestNextPayment as rnp ON rnp.RefNo = c.RefNo and rnp.OrganizationCode = c.OrganizationCode and rnp.Status NOT IN (?, ?) " +
                "          LEFT OUTER JOIN (SELECT COUNT(*) AS HoldSalePaymentPeriod, RefNo " +
                "                           FROM SalePaymentPeriod " +
                "                           WHERE PaymentComplete = 0 AND DATE(PaymentDueDate) < DATE(?) " +
                "                           GROUP BY RefNo " +
                "                          ) AS countHoldSalePaymentPeriod ON countHoldSalePaymentPeriod.RefNo = c.RefNo " +
                "       where c.isActive = 1 and c.STATUS = ? and c.OrganizationCode = ? ";

        args.add(AssignController.AssignTaskType.SalePaymentPeriod.toString());
        args.add(AssignController.AssignTaskType.SaleAudit.toString());
        args.add(RequestNextPaymentStatus.REJECT.toString());
        args.add(RequestNextPaymentStatus.COMPLETED.toString());
        args.add(valueOf(new TripController().getDueDate()));
        args.add(ContractInfo.ContractStatus.NORMAL.toString());
        args.add(OrganizationCode);

        //region ใช้แยก Sale และ Credit
        switch (EmployeeController.SourceSystem.valueOf(EmployeeController.SourceSystem.class, sourceSystem)) {
            case Sale:
                List<String> listPositionCode = Arrays.asList(positionCode.split(","));
                if (listPositionCode.contains("SaleLeader")) { //หัวหน้าทีม
                    sql += "  and (c.SaleTeamCode = ? ";
                    args.add(TeamCode);
                } else {
                    sql += "  and (c.SaleEmployeeCode = ? ";
                    args.add(EmpID);
                }
                break;
            case Credit:
                sql += "       and (a.AssigneeEmpID = ? ";
                args.add(EmpID);
                break;
        }

        sql += "                    or (rnp.RequestBy = ? and rnp.Status in (?, ?))) ";
        args.add(EmpID);
        args.add(RequestNextPaymentController.RequestNextPaymentStatus.REQUEST.toString());
        args.add(RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString());
        //endregion

        if(SearchText != null){
            sql += "           and (IFNULL(dc.CustomerName, dc.CompanyName) || IFNULL(dc.IDCard, '') || IFNULL(dc.AuthorizedIDCard, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ? ) ";
            args.add("%" + SearchText + "%");
        }

        if(RefNo != null){
            sql += "           and c.RefNo = ? ";
            args.add(RefNo);
        }

        sql += "       GROUP BY c.RefNo, c.CONTNO, dc.PrefixName, dc.CustomerName, dc.CompanyName, rnp.Status " +
               "       ORDER BY IFNULL(rnp.RequestDate, '') DESC, c.CONTNO ASC";
        return executeQueryList(sql, args.toArray(new String[args.size()]), RequestNextPaymentInfo.class);
    }

    /***
     * [END] - Fixed - [Android-เก็บเงินค่างวด] เปลี่ยนเงื่อนไขการนับจำนวนงวดที่ค้าง
     ***/

    /*** [START] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
    public void deleteRequestNextPaymentByID(String OrganizationCode, String RequestNextPaymentID) {
        executeNonQuery("DELETE FROM RequestNextPayment WHERE (OrganizationCode = ?) AND (RequestNextPaymentID = ?) ", new String[] { OrganizationCode, RequestNextPaymentID });
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/

}
