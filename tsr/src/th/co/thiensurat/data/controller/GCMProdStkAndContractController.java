package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.GCMProdStkAndContractInfo;

public class GCMProdStkAndContractController extends BaseController {

    public void addGCMProdStkAndContract(GCMProdStkAndContractInfo info) {
        String sql = "INSERT INTO GCMProdStkAndContract (GCMProdStkAndContractID, ProductSerialNumber, OrganizationCode, RefNo)"
                + "VALUES(?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.GCMProdStkAndContractID, info.ProductSerialNumber, info.OrganizationCode, info.RefNo});
    }

    public void updateGCMProdStkAndContract(GCMProdStkAndContractInfo info) {
        String sql = "UPDATE GCMProdStkAndContract "
                + " SET OrganizationCode = ?, RefNo = ? "
                + " WHERE GCMProdStkAndContractID = ? ";
        executeNonQuery(sql, new String[]{info.OrganizationCode, info.RefNo, info.GCMProdStkAndContractID});
    }

    public GCMProdStkAndContractInfo getGCMProdStkAndContractByGCMProdStkAndContractID(String GCMProdStkAndContractID) {
        final String sql =  "SELECT * FROM GCMProdStkAndContract WHERE GCMProdStkAndContractID = ? ";
        return executeQueryObject(sql, new String[]{GCMProdStkAndContractID}, GCMProdStkAndContractInfo.class);
    }

    public GCMProdStkAndContractInfo getGCMProdStkAndContractByRefNo(String organizationCode, String refNo) {
        final String sql =  "SELECT * FROM GCMProdStkAndContract WHERE OrganizationCode = ? AND RefNo = ? ";
        return executeQueryObject(sql, new String[]{organizationCode, refNo}, GCMProdStkAndContractInfo.class);
    }
}
