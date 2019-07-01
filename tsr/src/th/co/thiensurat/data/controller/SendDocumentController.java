package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.SendDocumentInfo;

public class SendDocumentController extends BaseController{

    public void addSendDocument(SendDocumentInfo info) {
        String sql = "INSERT INTO SendDocument (SendDocumentID, OrganizationCode, SumDocument, SyncedDate, SentSubTeamCode, SentTeamCode) "
                + "VALUES(?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[] {info.SendDocumentID, info.OrganizationCode, valueOf(info.SumDocument), valueOf(info.SyncedDate), info.SentSubTeamCode, info.SentTeamCode});
    }

    public void updateSendDocument(SendDocumentInfo info) {
        String sql = "UPDATE SendDocument SET SumDocument = ? where SentTeamCode = ? AND SentSubTeamCode = ?";
        executeNonQuery(sql, new String[] {valueOf(info.SumDocument), info.SentTeamCode, info.SentSubTeamCode});
    }

    public SendDocumentInfo getSendDocumentBySentTeamCodeAndSentSubTeamCode(String SentTeamCode, String SentSubTeamCode){
        SendDocumentInfo ret = null;
        String sql = "SELECT * FROM SendDocument WHERE SentTeamCode = ? AND SentSubTeamCode = ?";
        ret = executeQueryObject(sql, new String[] { SentTeamCode, SentSubTeamCode }, SendDocumentInfo.class);
        return ret;
    }

    public void deleteSendDocumentAll() {
        String sql = "DELETE FROM SendDocument";
        executeNonQuery(sql, null);
    }

}
