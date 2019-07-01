package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.SpareDrawdownInfo;

public class SpareDrawdownController extends BaseController {

    public enum SpareDrawdownStatus {
        REQUEST, APPROVED, COMPLETED , REJECT
    }

    public List<SpareDrawdownInfo> getSpareDrawdownByEmployeeIdAndStatusInRequestAndApproved(String organizationCode, String EmployeeID) {
        String sql = "SELECT * "
                + " FROM SpareDrawdown "
                + " WHERE (OrganizationCode = ?) AND (CreateBy = ?) AND ([Status] IN ('REQUEST', 'APPROVED')) "
                + " ORDER BY RequestDate ASC";
        return executeQueryList(sql, new String[]{organizationCode, EmployeeID}, SpareDrawdownInfo.class);
    }

    public void addSpareDrawdown(SpareDrawdownInfo info) {
        String sql = "INSERT INTO SpareDrawdown(SpareDrawdownID, OrganizationCode, Status, RequestDate, RequestBy, RequestTeamCode, RequestDetail, "
                + "         ApprovedDate, ApprovedBy, ApproveDetail, EffectiveDate, EffectiveBy, EffectiveDetail, "
                + "         SpareDrawdownPaperID, PrintCount, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate) "
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.SpareDrawdownID, info.OrganizationCode, info.Status, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode, info.RequestDetail,
                valueOf(info.ApprovedDate), info.ApprovedBy, info.ApproveDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.EffectiveDetail,
                info.SpareDrawdownPaperID, valueOf(info.PrintCount), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate)});
    }

    public void updateSpareDrawdown(SpareDrawdownInfo info) {
        String sql = "UPDATE SpareDrawdown "
                + " SET Status = ?, RequestDate = ?, RequestBy = ?, RequestTeamCode = ?, RequestDetail = ?, "
                + "         ApprovedDate = ?, ApprovedBy = ?, ApproveDetail = ?, EffectiveDate = ?, EffectiveBy = ?, EffectiveDetail = ?, "
                + "         SpareDrawdownPaperID = ?, PrintCount = ?, LastUpdateDate = ?, LastUpdateBy = ? "
                + "WHERE (OrganizationCode = ?) AND (SpareDrawdownID = ?)";
        executeNonQuery(sql, new String[]{info.Status, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode, info.RequestDetail,
                valueOf(info.ApprovedDate), info.ApprovedBy, info.ApproveDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.EffectiveDetail,
                info.SpareDrawdownPaperID, valueOf(info.PrintCount), valueOf(info.LastUpdateDate), info.LastUpdateBy, info.OrganizationCode, info.SpareDrawdownID});
    }

    public void deleteSpareDrawdownAll() {
        String sql = "DELETE FROM SpareDrawdown";
        executeNonQuery(sql, null);
    }




}
