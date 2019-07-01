package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.PartSpareInfo;

public class PartSpareController extends BaseController{

    public List<PartSpareInfo> getPartSpareByIsActive(String organizationCode, boolean isActive) {
        String sql = "SELECT * FROM PartSpare WHERE (OrganizationCode = ?) AND (IsActive = ?) ORDER BY PartSpareName ASC";
        return executeQueryList(sql, new String[]{organizationCode, valueOf(isActive)}, PartSpareInfo.class);
    }

    public List<PartSpareInfo> getPartSpareUNIONProduct(String organizationCode) {
        String sql = "SELECT * "
                + " FROM (SELECT  ps.PartSpareID, ps.PartSpareCode, ps.PartSpareName, ps.PartSpareUnit, 1 AS IsPartSpare "
                + "             FROM PartSpare AS ps "
                + "             WHERE (ps.OrganizationCode = ?) AND (ps.IsActive = 1) "
                + "       UNION "
                + "       SELECT p.ProductID AS PartSpareID, p.ProductCode AS PartSpareCode, p.ProductName AS PartSpareName, '' AS PartSpareUnit, 0 AS IsPartSpare "
                + "             FROM Product AS p "
                + "             WHERE (p.OrganizationCode = ?) "
                + "       ) AS PartSpare "
                + " ORDER BY PartSpare.IsPartSpare ASC, PartSpare.PartSpareName ASC";
        return executeQueryList(sql, new String[]{organizationCode, organizationCode}, PartSpareInfo.class);
    }

}
