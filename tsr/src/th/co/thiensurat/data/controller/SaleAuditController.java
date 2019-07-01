package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.SaleAuditInfo;

public class SaleAuditController extends BaseController {

    public SaleAuditInfo getSaleAuditBySaleAuditID(String SaleAuditID) {
        String sql = "SELECT  * FROM [SaleAudit] WHERE (SaleAuditID = ?)";
        return executeQueryObject(sql, new String[] { SaleAuditID }, SaleAuditInfo.class);
    }

    public SaleAuditInfo getSaleAuditByRefNo(String RefNo) {
        String sql = "SELECT  * FROM [SaleAudit] WHERE (RefNo = ?)";
        return executeQueryObject(sql, new String[] { RefNo }, SaleAuditInfo.class);
    }


    public void addSaleAudit(SaleAuditInfo info) {
        String sql = "INSERT INTO [SaleAudit] (SaleAuditID, OrganizationCode, RefNo, AuditDate, AppointmentAuditDate" +
                "       , AuditorSaleCode, AuditorEmployeeID, AuditorTeamCode, IsPassFirstPayment, IsPassInstall, HasOtherOffer" +
                "       , OtherOfferDetail, ComplainProblemID, ComplainDetail, AuditEmployeeLevelPath, AppointmentPaymentDate" +
                "       , CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate, ComplainID, IsPassSaleAudit, SaleAuditEmployeeLevelPath)" +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executeNonQuery(sql, new String[] { info.SaleAuditID, info.OrganizationCode, info.RefNo, valueOf(info.AuditDate)
                , valueOf(info.AppointmentAuditDate), info.AuditorSaleCode, info.AuditorEmployeeID, info.AuditorTeamCode
                , valueOf(info.IsPassFirstPayment), valueOf(info.IsPassInstall), valueOf(info.HasOtherOffer)
                , info.OtherOfferDetail, info.ComplainProblemID, info.ComplainDetail, info.AuditEmployeeLevelPath
                , valueOf(info.AppointmentPaymentDate), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate)
                , info.LastUpdateBy, valueOf(info.SyncedDate), info.ComplainID, valueOf(info.IsPassSaleAudit), info.SaleAuditEmployeeLevelPath});
    }

    public void updateSaleAudit(SaleAuditInfo info) {
        String sql = "UPDATE [SaleAudit]" +
                " SET OrganizationCode = ?, RefNo = ?, AuditDate  = ?, AppointmentAuditDate = ?" +
                "       , AuditorSaleCode = ?, AuditorEmployeeID = ?, AuditorTeamCode = ?, IsPassFirstPayment = ?, IsPassInstall = ?" +
                "       , HasOtherOffer = ?, OtherOfferDetail = ?, ComplainProblemID = ?, ComplainDetail = ?, AuditEmployeeLevelPath = ?" +
                "       , AppointmentPaymentDate = ?, CreateDate = ?, CreateBy = ?, LastUpdateDate = ?, LastUpdateBy = ?, SyncedDate = ? " +
                "       , ComplainID = ?, IsPassSaleAudit = ?, SaleAuditEmployeeLevelPath = ?" +
                " WHERE (SaleAuditID = ?)";
        executeNonQuery(sql, new String[] { info.OrganizationCode, info.RefNo, valueOf(info.AuditDate)
                , valueOf(info.AppointmentAuditDate), info.AuditorSaleCode, info.AuditorEmployeeID, info.AuditorTeamCode
                , valueOf(info.IsPassFirstPayment), valueOf(info.IsPassInstall), valueOf(info.HasOtherOffer)
                , info.OtherOfferDetail, info.ComplainProblemID, info.ComplainDetail, info.AuditEmployeeLevelPath
                , valueOf(info.AppointmentPaymentDate), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate)
                , info.LastUpdateBy, valueOf(info.SyncedDate), info.ComplainID, valueOf(info.IsPassSaleAudit), info.SaleAuditEmployeeLevelPath, info.SaleAuditID });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteSaleAuditAll() {
        executeNonQuery("DELETE FROM SaleAudit", null);
    }
    public void deleteSaleAuditByRefNo(String refNo) {
        String sql = "DELETE FROM SaleAudit WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[]{refNo});
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////


}
