package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.DamageProductInfo;

/**
 * Created by Tanawut on 6/3/2558.
 */
public class DamageProductController extends BaseController {

    public void addDamageProduct(DamageProductInfo info) {
        String sql = "INSERT INTO DamageProduct (DamageProductID,OrganizationCode,EmpID,TeamCode,SubTeamCode," +
                "ReturnDate,ReturnType,ReturnCause,ProductSerialNumber,CreateDate,CreateBy,LastUpdateDate," +
                "LastUpdateBy,SyncedDate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executeNonQuery(sql, new String[]{info.DamageProductID, info.OrganizationCode, info.EmpID,
                info.TeamCode, info.SubTeamCode, valueOf(info.ReturnDate), info.ReturnType, info.ReturnCause,
                info.ProductSerialNumber, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate)
                , info.LastUpdateBy, valueOf(info.SyncedDate)});
    }
}
