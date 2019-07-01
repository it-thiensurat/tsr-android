package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ReportSaleByAreaListInfo;
import th.co.thiensurat.data.info.ReportSaleByAreaMapInfo;

/**
 * Created by Annop on 6/1/2558.
 */
public class ReportSaleByAreaController extends BaseController {
    public List<ReportSaleByAreaMapInfo> getMaps(String employeeID, String fortnightID) {
        String sql = "SELECT * FROM ReportSaleByAreaMap WHERE FortnightID = ? ORDER BY SaleDate DESC";
        List<ReportSaleByAreaMapInfo> result = executeQueryList(sql, new String[] { fortnightID }, ReportSaleByAreaMapInfo.class);
        return result;
    }

    public List<ReportSaleByAreaListInfo> getList(String employeeID, String fortnightID, int areaType) {
        String sql = "SELECT * FROM ReportSaleByAreaList WHERE FortnightID = ? AND AreaType = ? ORDER BY AreaName";
        List<ReportSaleByAreaListInfo> result = executeQueryList(sql, new String[] { fortnightID, valueOf(areaType) }, ReportSaleByAreaListInfo.class);
        return result;
    }
}
