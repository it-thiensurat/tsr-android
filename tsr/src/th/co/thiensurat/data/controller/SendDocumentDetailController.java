package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.SendDocumentDetailInfo;

public class SendDocumentDetailController extends BaseController{

    public void addSendDocumentDetail(SendDocumentDetailInfo info) {
        String sql = "INSERT INTO SendDocumentDetail (SendDocumentID, OrganizationCode, PrintHistoryID, SyncedDate) "
                + "VALUES(?, ?, ?, ?)";
        executeNonQuery(sql, new String[] {info.SendDocumentID, info.OrganizationCode, info.PrintHistoryID, valueOf(info.SyncedDate)});
    }

    public void deleteSendDocumentDetail(SendDocumentDetailInfo info) {
        String sql = "DELETE FROM SendDocumentDetail WHERE (SendDocumentID = ?) AND (OrganizationCode = ?) AND (PrintHistoryID = ?)";
        executeNonQuery(sql, new String[] {info.SendDocumentID, info.OrganizationCode, info.PrintHistoryID});
    }

    public void deleteSendDocumentDetailAll() {
        String sql = "DELETE FROM SendDocumentDetail";
        executeNonQuery(sql, null);
    }


    public SendDocumentDetailInfo getSendDocumentDetailByPrintHistoryID(String PrintHistoryID) {
        String sql = "select * from SendDocumentDetail where PrintHistoryID = ? ";
        return executeQueryObject(sql, new String[]{PrintHistoryID}, SendDocumentDetailInfo.class);
    }
}
