package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ContractCloseAccountInfo;
import th.co.thiensurat.data.info.CutOffContractInfo;

public class CutOffContractController extends BaseController {

    public enum CutOffContractStatus {
        REQUEST, APPROVED, COMPLETED, REJECT
    }

    public List<CutOffContractInfo> getContractNoCutOffContractBySearch(String OrganizationCode, String Search) {
        String sql = "SELECT c.RefNo, c.CONTNO, c.ProductSerialNumber, " +
                "       ifnull(dc.PrefixName,'') || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName " +
                " FROM Contract as c " +
                "       INNER JOIN DebtorCustomer AS dc ON (dc.CustomerID = c.CustomerID) " +
                "       INNER JOIN SalePaymentPeriod AS spp ON (spp.RefNo = c.RefNo) AND (spp.PaymentPeriodNumber = 1) AND (spp.PaymentComplete = 1) " +
                "       LEFT JOIN CutOffContract AS coc ON (coc.RefNo = c.RefNo) AND (coc.IsAvailableContract = 0) " +
                " WHERE (c.isActive = 1) AND (c.Status IN ('NORMAL', 'F'))"+
                "       AND (c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE (StatusName = 'COMPLETED'))) " +
                "       AND (c.MODE > 1) " +
                "       AND (coc.CutOffContractID IS NULL) " +
                "       AND (c.OrganizationCode = ?) " +
                "       AND (ifnull(c.ProductSerialNumber,'') || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'')  || ifnull(c.CONTNO,'') like ? ) " +
                "ORDER BY c.CONTNO ASC, CustomerFullName ASC, c.ProductSerialNumber ASC ";
        return executeQueryList(sql, new String[]{OrganizationCode, Search}, CutOffContractInfo.class);
    }

    public void addCutOffContract(CutOffContractInfo info) {
        String sql = " INSERT INTO CutOffContract (CutOffContractID, OrganizationCode, RefNo, Status, " +
                "       RequestProblemDetail, RequestDate, RequestBy, RequestTeamCode, " +
                "       ApprovedDate, ApproveDetail, ApprovedBy, " +
                "       ResultProblemDetail, EffectiveDate, EffectiveBy, " +
                "       CutOffContractPaperID, IsAvailableContract, AvailableContractDetail, AvailableContractDate, AvailableContractBy, AvailableContractTeamCode, " +
                "       CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, LastAssigneeEmpID, LastAssigneeSaleCode, LastAssigneeTeamCode, LastAssignTaskType, "+
                "       RequestEmployeeLevelPath, EffectiveEmployeeLevelPath)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeNonQuery(sql, new String[]{info.CutOffContractID, info.OrganizationCode, info.RefNo, info.Status,
                info.RequestProblemDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode,
                valueOf(info.ApprovedDate), info.ApproveDetail, info.ApprovedBy,
                info.ResultProblemDetail, valueOf(info.EffectiveDate), info.EffectiveBy,
                info.CutOffContractPaperID, valueOf(info.IsAvailableContract), info.AvailableContractDetail,
                valueOf(info.AvailableContractDate), info.AvailableContractBy, info.AvailableContractTeamCode,
                valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy,
                info.LastAssigneeEmpID, info.LastAssigneeSaleCode, info.LastAssigneeTeamCode, info.LastAssignTaskType,
                info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath});
    }

    public void updateCutOffContract(CutOffContractInfo info) {
        String sql = "UPDATE CutOffContract " +
                " SET OrganizationCode = ?,  RefNo = ?,  Status = ?,  " +
                "       RequestProblemDetail = ?,  RequestDate = ?,  RequestBy = ?,  RequestTeamCode = ?, " +
                "       ApprovedDate = ?,  ApproveDetail = ?,  ApprovedBy = ?," +
                "       ResultProblemDetail = ?,  EffectiveDate = ?,  EffectiveBy = ?, " +
                "       CutOffContractPaperID = ?,  IsAvailableContract = ?,  AvailableContractDetail = ?, " +
                "       AvailableContractDate = ?,  AvailableContractBy = ?,  AvailableContractTeamCode = ?, " +
                "       CreateDate = ?,  CreateBy = ?,  LastUpdateDate = ?,  LastUpdateBy = ?,  LastAssigneeEmpID = ?, " +
                "       LastAssigneeSaleCode = ?,  LastAssigneeTeamCode = ?,  LastAssignTaskType = ?, " +
                "       RequestEmployeeLevelPath = ?, EffectiveEmployeeLevelPath = ?" +
                " WHERE (CutOffContractID = ?)";
        executeNonQuery(sql, new String[] {info.OrganizationCode, info.RefNo, info.Status,
                info.RequestProblemDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode,
                valueOf(info.ApprovedDate), info.ApproveDetail, info.ApprovedBy,
                info.ResultProblemDetail, valueOf(info.EffectiveDate), info.EffectiveBy,
                info.CutOffContractPaperID, valueOf(info.IsAvailableContract), info.AvailableContractDetail,
                valueOf(info.AvailableContractDate), info.AvailableContractBy, info.AvailableContractTeamCode,
                valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy,
                info.LastAssigneeEmpID, info.LastAssigneeSaleCode, info.LastAssigneeTeamCode, info.LastAssignTaskType,
                info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath, info.CutOffContractID });
    }

    public List<CutOffContractInfo> GetCutOffContractByCutOffContractIDOfApproved(String OrganizationCode, String CutOffContractID){
        String sql = "SELECT " +
                "       a.AssignID, " +
                "       a.AssigneeEmpID as LastAssigneeEmpID, " +
                "       a.AssigneeTeamCode as LastAssigneeTeamCode, " +
                "       a.TaskType as LastAssignTaskType, " +
                "       fn.SaleCode as LastAssigneeSaleCode" +
                " FROM CutOffContract as coc" +
                "       LEFT JOIN Assign as a ON a.RefNo = coc.RefNo and (a.TaskType = 'SaleAudit' or a.TaskType = 'SalePaymentPeriod')" +
                "       LEFT JOIN SaleAudit as sa ON sa.SaleAuditID = a.ReferenceID and IFNULL(sa.IsPassSaleAudit, 0) = 0" +
                "       LEFT JOIN SalePaymentPeriod as spp ON spp.SalePaymentPeriodID = a.ReferenceID and spp.PaymentComplete = 0 and spp.PaymentPeriodNumber > 1" +
                "       LEFT JOIN EmployeeDetail as fn ON fn.EmployeeCode = a.AssigneeEmpID and fn.SaleCode IS NOT NULL" +
                " WHERE (coc.[OrganizationCode] = ?) AND ([CutOffContractID] = ?)" +
                "       AND (spp.SalePaymentPeriodID IS NOT NULL or sa.SaleAuditID IS NOT NULL)";
        return executeQueryList(sql, new String[]{OrganizationCode, CutOffContractID}, CutOffContractInfo.class);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteCutOffContractAll() {
        executeNonQuery("DELETE FROM CutOffContract", null);
    }
    public void deleteCutOffContractByRefNo(String refNo) {
        String sql = "DELETE FROM CutOffContract WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[]{refNo});
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////


}
