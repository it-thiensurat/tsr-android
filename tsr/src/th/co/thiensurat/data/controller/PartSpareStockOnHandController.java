package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.PartSpareInfo;
import th.co.thiensurat.data.info.PartSpareStockOnHandInfo;

public class PartSpareStockOnHandController extends BaseController{

    public List<PartSpareStockOnHandInfo> getPartSpareStockOnHandByTeamCode(String organizationCode, String teamCode) {
        String sql = "SELECT * FROM PartSpareStockOnHand WHERE (OrganizationCode = ?) AND (TeamCode = ?) ORDER BY EmployeeCode, PartSpareID ASC";
        return executeQueryList(sql, new String[]{organizationCode, teamCode}, PartSpareStockOnHandInfo.class);
    }

    public List<PartSpareStockOnHandInfo> getPartSpareStockOnHandByEmployeeCode(String organizationCode, String employeeCode) {
        String sql = "SELECT * FROM PartSpareStockOnHand WHERE (OrganizationCode = ?) AND (EmployeeCode = ?) ORDER BY PartSpareID ASC";
        return executeQueryList(sql, new String[]{organizationCode, employeeCode}, PartSpareStockOnHandInfo.class);
    }

    public void addPartSpareStockOnHand(PartSpareStockOnHandInfo info) {
        String sql = "INSERT INTO PartSpareStockOnHand (OrganizationCode, PartSpareID, EmployeeCode, TeamCode, QTYOnHand, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.OrganizationCode, info.PartSpareID, info.EmployeeCode, info.TeamCode, valueOf(info.QTYOnHand), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate)});
    }

    public void updatePartSpareStockOnHand(PartSpareStockOnHandInfo info) {
        String sql = "UPDATE PartSpareStockOnHand "
                + " SET TeamCode = ? "
                + "     , QTYOnHand = ? "
                + "     , LastUpdateDate = ? "
                + "     , LastUpdateBy = ? "
                + " WHERE (OrganizationCode = ?) AND (PartSpareID = ?) AND (EmployeeCode = ?)";
        executeNonQuery(sql, new String[]{info.TeamCode, valueOf(info.QTYOnHand), valueOf(info.LastUpdateDate), info.LastUpdateBy, info.OrganizationCode, info.PartSpareID, info.EmployeeCode});
    }

}
