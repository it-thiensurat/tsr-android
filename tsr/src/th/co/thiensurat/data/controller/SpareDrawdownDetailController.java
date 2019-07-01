package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.SpareDrawdownDetailInfo;

public class SpareDrawdownDetailController extends BaseController {

    public List<SpareDrawdownDetailInfo> getSpareDrawdownDetailBySpareDrawdownID(String SpareDrawdownID) {
        String sql = "SELECT IFNULL(ps.PartSpareName,'') || IFNULL(p.ProductName,'') AS PartSpareName, "
                + "         IFNULL(ps.PartSpareCode,'') || IFNULL(p.ProductCode,'') AS PartSpareCode,"
                + "         IFNULL(ps.PartSpareUnit,'') AS PartSpareUnit, sdd.* "
                + " FROM SpareDrawdownDetail AS sdd "
                + "         LEFT JOIN PartSpare AS ps ON ps.PartSpareID = sdd.PartSpareIDOrProductID "
                + "         LEFT JOIN Product AS p ON p.ProductID = sdd.PartSpareIDOrProductID "
                + " WHERE (sdd.SpareDrawdownID = ?) "
                + " ORDER BY sdd.IsPartSpare ASC, PartSpareName ASC";
        return executeQueryList(sql, new String[]{SpareDrawdownID}, SpareDrawdownDetailInfo.class);
    }

    public void addSpareDrawdownDetail(SpareDrawdownDetailInfo info) {
        String sql = "INSERT INTO SpareDrawdownDetail(SpareDrawdownID, PartSpareIDOrProductID, IsPartSpare, RequestDetail, RequestQTY, "
                + "         ApproveDetail, ApproveQTY, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate) "
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.SpareDrawdownID, info.PartSpareIDOrProductID, valueOf(info.IsPartSpare), info.RequestDetail, valueOf(info.RequestQTY),
                info.ApproveDetail, valueOf(info.ApproveQTY), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate)});
    }

    public void updateSpareDrawdownDetail(SpareDrawdownDetailInfo info) {
        String sql = "UPDATE SpareDrawdownDetail "
                + " SET IsPartSpare = ?, RequestDetail = ?, RequestQTY = ?, "
                + "         ApproveDetail = ?, ApproveQTY = ?, CreateDate = ?, CreateBy = ?, LastUpdateDate = ?, LastUpdateBy = ? "
                + " WHERE (SpareDrawdownID = ?) AND (PartSpareIDOrProductID = ?) ";
        executeNonQuery(sql, new String[]{valueOf(info.IsPartSpare), info.RequestDetail, valueOf(info.RequestQTY),
                info.ApproveDetail, valueOf(info.ApproveQTY), valueOf(info.LastUpdateDate), info.LastUpdateBy, info.SpareDrawdownID, info.PartSpareIDOrProductID});
    }

    public void deleteSpareDrawdownDetailAll() {
        String sql = "DELETE FROM SpareDrawdownDetail";
        executeNonQuery(sql, null);
    }

}
