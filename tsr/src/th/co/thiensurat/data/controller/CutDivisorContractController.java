package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.CutDivisorContractInfo;

public class CutDivisorContractController extends BaseController {

    public enum CutDivisorContractStatus {
        REQUEST, APPROVED, COMPLETED, REJECT
    }

    public List<CutDivisorContractInfo> getContractNoCutDivisorContractBySearch(String OrganizationCode, String SearchText) {
        String sql = "SELECT cutDivisor.*" +
                "       , c.RefNo AS ContractRefNo, c.OrganizationCode AS ContractOrganizationCode, c.CONTNO, c.ProductSerialNumber " +
                "       , IFNULL(dc.PrefixName,'') || IFNULL(dc.CustomerName,'') || IFNULL(dc.CompanyName,'') AS CustomerFullName " +
                " FROM Contract as c " +
                "       INNER JOIN DebtorCustomer AS dc ON dc.CustomerID = c.CustomerID " +
                "       INNER JOIN SalePaymentPeriod AS spp ON spp.RefNo = c.RefNo and spp.PaymentPeriodNumber = 1 and spp.PaymentComplete = 1 " +
                "       LEFT JOIN CutDivisorContract AS cutDivisor ON cutDivisor.RefNo = c.RefNo " +
                " WHERE (c.isActive = 1) AND (c.Status IN ('NORMAL', 'F'))" +
                "       AND (c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE (StatusName = 'COMPLETED'))) " +
                "       AND (c.MODE > 1) " +
                "       AND (cutDivisor.CutDivisorContractID IS NULL) " +
                "       AND (c.OrganizationCode = ?) " +
                "       AND (IFNULL(c.ProductSerialNumber,'') || IFNULL(dc.CustomerName,'') || IFNULL(dc.CompanyName,'')  || IFNULL(c.CONTNO,'') LIKE ? ) " +
                " ORDER BY c.CONTNO ASC, CustomerFullName ASC, c.ProductSerialNumber ASC ";
        return executeQueryList(sql, new String[]{OrganizationCode, "%" + SearchText + "%"}, CutDivisorContractInfo.class);
    }

    public void addCutDivisorContract(CutDivisorContractInfo info) {
        String sql = " INSERT INTO CutDivisorContract (CutDivisorContractID, OrganizationCode, RefNo, Status, " +
                "       RequestProblemDetail, RequestDate, RequestBy, RequestTeamCode, " +
                "       ApprovedDate, ApproveDetail, ApprovedBy, " +
                "       ResultProblemDetail, EffectiveDate, EffectiveBy, CutDivisorContractPaperID, " +
                "       CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, RequestEmployeeLevelPath, EffectiveEmployeeLevelPath) " +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeNonQuery(sql, new String[]{info.CutDivisorContractID, info.OrganizationCode, info.RefNo, info.Status,
                info.RequestProblemDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode,
                valueOf(info.ApprovedDate), info.ApproveDetail, info.ApprovedBy,
                info.ResultProblemDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.CutDivisorContractPaperID,
                valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath}); //,
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteCutDivisorContractAll() {
        executeNonQuery("DELETE FROM CutDivisorContract", null);
    }
    public void deleteCutDivisorContractByRefNo(String refNo) {
        String sql = "DELETE FROM CutDivisorContract WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[]{refNo});
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////


}
