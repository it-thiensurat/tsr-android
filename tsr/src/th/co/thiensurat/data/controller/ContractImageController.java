package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ContractImageInfo;

public class ContractImageController extends BaseController {

    public enum ImageType {
        IDCARD, PRODUCT, ADDRESS, MAP, MAPPAYMENT, LOSS, CUSTOMER, IMPOUNDPRODUCT, CHANGEPRODUCT, SALEAUDIT, PAYMENTCARD
    }

    public ContractImageInfo getContractImage(String RefNo, String ImageTypeCode) {
        ContractImageInfo ret = null;
        String sql = "select * from ContractImage where RefNo =? and ImageTypeCode =? ";
        ret = executeQueryObject(sql, new String[]{RefNo, ImageTypeCode}, ContractImageInfo.class);
        return ret;
    }

    public List<ContractImageInfo> getContractImageList(String RefNo, String ImageTypeCode) {
        String sql = "select * from ContractImage where RefNo =? and ImageTypeCode =? ";
        return executeQueryList(sql, new String[]{RefNo, ImageTypeCode}, ContractImageInfo.class);
    }

    public List<ContractImageInfo> getContractImage(String RefNo) {
        String sql = "select * from ContractImage where RefNo =? ";
        return executeQueryList(sql, new String[]{RefNo}, ContractImageInfo.class);
    }

    public ContractImageInfo getContractImageByImageID(String ImageID) {
        ContractImageInfo ret = null;
        String sql = "select * from ContractImage where ImageID = ? ";
        ret = executeQueryObject(sql, new String[]{ImageID}, ContractImageInfo.class);
        return ret;
    }

    public void addContractImage(ContractImageInfo info) {
        String sql = "INSERT INTO ContractImage (ImageID, RefNo, ImageName, ImageTypeCode, SyncedDate)" + "VALUES(?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.ImageID, info.RefNo, info.ImageName, info.ImageTypeCode, valueOf(info.SyncedDate)});
    }

    public void updateContractImage(ContractImageInfo info) {
        String sql = "update ContractImage set ImageID=?, RefNo=?, ImageName=?, ImageTypeCode=?, SyncedDate=?"
                + " where RefNo=? and ImageTypeCode=?";
        executeNonQuery(sql, new String[]{info.ImageID, info.RefNo, info.ImageName, info.ImageTypeCode, valueOf(info.SyncedDate),
                info.RefNo, info.ImageTypeCode});
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteContractImageAll() {
        String sql = "DELETE FROM ContractImage";
        executeNonQuery(sql, null);
    }
    public void deleteContractImageByImageID(String ImageID) {
        String sql = "DELETE FROM ContractImage WHERE (ImageID = ?)";
        executeNonQuery(sql, new String[]{ImageID});
    }
    public void deleteContractImageByRefNo(String refNo) {
        String sql = "DELETE FROM ContractImage WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[]{refNo});
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

}
