package th.co.thiensurat.data.controller;

import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.SendMoneyInfo;

public class SendMoneyController extends BaseController {

    public enum SendMoneyStatus {
        SENT, RECEIVED
    }
    public void addSendMoney(SendMoneyInfo info) {
        String sql = "INSERT INTO SendMoney(SendMoneyID, OrganizationCode, PaymentType, PaymentDate, Reference1, Reference2,SendAmount, "
                + "         SendDate, BankCode, Status, TransactionNo, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate, ChannelItemID, PayeeName, SaveTransactionNoDate "
                + "         , SendMoneyEmployeeLevelPath, EmpID, TeamCode)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeNonQuery(sql, new String[]{info.SendMoneyID, info.OrganizationCode, info.PaymentType, valueOf(info.PaymentDate), info.Reference1,
                info.Reference2, valueOf(info.SendAmount), valueOf(info.SendDate), info.BankCode, info.Status, info.TransactionNo,
                valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate), info.ChannelItemID,
                info.PayeeName, valueOf(info.SaveTransactionNoDate), info.SendMoneyEmployeeLevelPath, info.EmpID, info.TeamCode});

        TSRController.updateRunningNumberReference1AndReference2(info.Reference1, info.Reference2);
    }

//    public List<SendMoneyInfo> getSummarySendMoneyGroupByPaymentDate(String organizationCode, String EmpIDOfMember) {
//        List<SendMoneyInfo> ret = null;
//        String sql = "select sm.SendMoneyID, sm.OrganizationCode, datetime(date(sm.PaymentDate)) as PaymentDate, sm.PaymentType, sm.Reference1, sm.Reference2, ifnull(sm.SendAmount,0) as SendAmount, sm.SendDate, sm.BankCode, sm.Status, sm.TransactionNo, sm.PayeeName,"
//                + " ci.ChannelItemID, ci.ChannelItemName, ci.AccountCode1, ci.AccountCode2,"
//                + " c.ChannelID, c.ChannelCode, c.ChannelName,"
//                + " 		CASE WHEN sm.PaymentType= 'Credit' THEN 'เซลล์สลิป'"
//                + " 			WHEN sm.PaymentType= 'Cheque' THEN 'เช็ค'"
//                + " 			WHEN sm.PaymentType= 'Cash' THEN 'เงินสด'"
//                + " 		ELSE '' END AS PaymentTypeName"
//                + " from SendMoney as sm left outer join ChannelItem as ci on sm.ChannelItemID=ci.ChannelItemID inner join Channel as c on ci.ChannelID=c.ChannelID"
//                + " where sm.OrganizationCode = ? AND sm.CreateBy IS NOT NULL AND sm.CreateBy IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + ")"
//                + " order by sm.PaymentDate asc  ";
//        ret = executeQueryList(sql, new String[]{organizationCode, EmpIDOfMember}, SendMoneyInfo.class);
//        return ret;
//    }

    public List<SendMoneyInfo> getSummarySendMoneyGroupByPaymentDate(String organizationCode, String EmpIDOfMember) {
        List<SendMoneyInfo> ret = null;
        String sql = "SELECT sm.SendMoneyID, sm.OrganizationCode, datetime(date(sm.PaymentDate)) as PaymentDate, "
                + "         sm.PaymentType, sm.Reference1, sm.Reference2, ifnull(sm.SendAmount,0) as SendAmount, sm.SendDate, sm.BankCode, sm.Status, sm.TransactionNo, sm.PayeeName,"
                + "         sm.SendMoneyEmployeeLevelPath, sm.EmpID, sm.TeamCode ,"
                + "         ci.ChannelItemID, ci.ChannelItemName, ci.AccountCode1, ci.AccountCode2,"
                + "         c.ChannelID, c.ChannelCode, c.ChannelName,"
                + " 		CASE WHEN sm.PaymentType= 'Credit' THEN 'เซลล์สลิป'"
                + " 			WHEN sm.PaymentType= 'Cheque' THEN 'เช็ค'"
                + " 			WHEN sm.PaymentType= 'Cash' THEN 'เงินสด'"
                + " 		ELSE '' END AS PaymentTypeName"
                + "         , sm.SaveTransactionNoDate "
                + " FROM SendMoney AS sm "
                + "         LEFT OUTER JOIN ChannelItem AS ci ON (sm.ChannelItemID=ci.ChannelItemID) "
                + "         INNER JOIN Channel AS c ON (ci.ChannelID=c.ChannelID)"
                + " WHERE (sm.OrganizationCode = ?) AND (sm.CreateBy IS NOT NULL) AND (sm.CreateBy IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + "))"
                + "         AND (date(sm.CreateDate) >= date('now','-1 month')) "  //-- Fixed - [BHPROJ-0026-3284][Android-SendMoney] [Tab] รอบันทึก TransactionNo. ให้แสดงย้อนหลังแค่ 1 เดือน (นับจากวันที่ปัจจุบัน)
                + " ORDER BY sm.PaymentDate asc  ";
        ret = executeQueryList(sql, BHUtilities.makeStringArray(new String[]{organizationCode, EmpIDOfMember}), SendMoneyInfo.class);
        return ret;
    }

    public void deleteSendMoneyByID(String sendMoneyID) {
        String sql = "DELETE FROM SendMoney WHERE (SendMoneyID = ?)";
        executeNonQuery(sql, new String[]{sendMoneyID});
    }

    public void deleteSendMoneyAll() {
        executeNonQuery("delete from SendMoney", null);
    }

    public SendMoneyInfo getSendMoneyByID(String sendMoneyID) {
        SendMoneyInfo ret = null;
        String sql = "SELECT * FROM SendMoney WHERE (SendMoneyID = ?)";
        ret = executeQueryObject(sql, new String[]{sendMoneyID}, SendMoneyInfo.class);
        return ret;
    }

    public SendMoneyInfo getSendMoneyByIDForPrint(String sendMoneyID) {
        String sql = "SELECT s.*, ci.AccountCode1, c.ChannelName, e.FirstName, e.LastName, ci.ChannelItemName, c.ChannelCode," +
                "    CASE" +
                "        WHEN s.PaymentType = 'Credit' THEN 'เซลล์สลิป'" +
                "        WHEN s.PaymentType = 'Cheque' THEN 'เช็ค'" +
                "        WHEN s.PaymentType = 'Cash' THEN 'เงินสด'" +
                "        ELSE ''" +
                "    END AS PaymentTypeName" +
                " FROM SendMoney s " +
                " INNER JOIN ChannelItem ci ON s.ChannelItemID = ci.ChannelItemID" +
                " INNER JOIN Channel c ON ci.ChannelID = c.ChannelID" +
                " LEFT OUTER JOIN Employee e ON s.CreateBy = e.EmpID" +
                " WHERE s.SendMoneyID = ?";
        return executeQueryObject(sql, new String[]{sendMoneyID}, SendMoneyInfo.class);
    }

//	public void updateTransactionNo(String sendMoneyID, String transactionNo) {
//		String sql = "UPDATE SendMoney SET TransactionNo=? WHERE SendMoneyID=?";
//		executeNonQuery(sql, new String[] { transactionNo, sendMoneyID });
//	}

    public SendMoneyInfo getSendMoneyByID(String organizationCode, String sendMoneyID) {
        SendMoneyInfo ret = null;
        String sql = "SELECT sm.PayeeName, sm.Reference2, c.ChannelCode, c.ChannelName, ci.AccountCode1, sm.PaymentType, sm.SendDate, ifnull(sm.SendAmount,0) as SendAmount "
                + "         , CASE WHEN sm.PaymentType= 'Credit' THEN 'เซลล์สลิป'"
                + "            WHEN sm.PaymentType= 'Cheque' THEN 'เช็ค'"
                + "            WHEN sm.PaymentType= 'Cash' THEN 'เงินสด'"
                + "             ELSE '' "
                + "         END AS PaymentTypeName "
                + "         , sm.SaveTransactionNoDate "
                + "         , sm.SendMoneyEmployeeLevelPath, sm.EmpID, sm.TeamCode "
                + " FROM SendMoney AS sm "
                + "         LEFT OUTER JOIN ChannelItem AS ci ON (sm.ChannelItemID = ci.ChannelItemID) "
                + "         INNER JOIN Channel as c ON (ci.ChannelID = c.ChannelID) "
                + " WHERE (sm.OrganizationCode = ?) AND (sm.SendMoneyID = ?)";
        ret = executeQueryObject(sql, new String[]{organizationCode, sendMoneyID}, SendMoneyInfo.class);
        return ret;
    }

    public void updateSendMoney(SendMoneyInfo info) {
        String sql = "UPDATE SendMoney"
                + " SET [PaymentDate]=?,"
                + "	    [PaymentType]=?,"
                + "	    [Reference1]=?,"
                + "	    [Reference2]=?,"
                + "	    [SendAmount]=?,"
                + "	    [SendDate]=?,"
                + "	    [BankCode]=?,"
                + "	    [Status]=?,"
                + "	    [TransactionNo]=?,"
                + "	    [LastUpdateDate]=?,"
                + "	    [LastUpdateBy]=?,"
                + "	    [ChannelItemID]=?,"
                + "	    [PayeeName]=?,"
                + "     [SaveTransactionNoDate]=?, "
                + "     [SendMoneyEmployeeLevelPath]=?, "
                + "     [EmpID]=?, "
                + "     [TeamCode]=? "
                + " WHERE ([OrganizationCode] = ?) AND ([SendMoneyID] = ?)";
        executeNonQuery(sql, new String[]{valueOf(info.PaymentDate),
                info.PaymentType,
                info.Reference1,
                info.Reference2,
                valueOf(info.SendAmount),
                valueOf(info.SendDate),
                info.BankCode,
                info.Status,
                info.TransactionNo,
                valueOf(info.LastUpdateDate),
                info.LastUpdateBy,
                info.ChannelItemID,
                info.PayeeName,
                valueOf(info.SaveTransactionNoDate),
                info.SendMoneyEmployeeLevelPath,
                info.EmpID,
                info.TeamCode,
                info.OrganizationCode,
                info.SendMoneyID});
    }

    public List<SendMoneyInfo> getSendMoneyByTransactionNo(String organizationCode, String TransactionNo, String PaymentType) {
        String sql = "SELECT * FROM SendMoney sm WHERE (sm.OrganizationCode = ?) AND (sm.TransactionNo = ?) AND (PaymentType = ?)";
        return executeQueryList(sql, new String[]{organizationCode, TransactionNo, PaymentType}, SendMoneyInfo.class);
    }

    // Fixed - [BHPROJ-0026-865] :: [Meeting@TSR@11/02/59] [Android-บันทึก Transaction No. หลังจากนำส่งเงินแล้ว] ให้เรียกใช้ WS method ชื่อ UpdateSendMoneyTransactionNo แทน
    public void updateSendMoneyTransactionNo(SendMoneyInfo info) {
        String sql = "UPDATE SendMoney"
                + " SET [TransactionNo]=?,"
                + "	    [LastUpdateDate]=?,"
                + "	    [LastUpdateBy]=?,"
                + "     [SaveTransactionNoDate]=? "
                + " WHERE ([OrganizationCode] = ?) AND ([SendMoneyID] = ?)";
        executeNonQuery(sql, new String[]{info.TransactionNo,
                valueOf(info.LastUpdateDate),
                info.LastUpdateBy,
                valueOf(info.SaveTransactionNoDate),
                info.OrganizationCode,
                info.SendMoneyID});
    }


    /*** [START] :: Fixed - [BHPROJ-0026-1056] :: [Android-บันทึก TransactionNo] ตรวจสอบ Not Existing ของ Field [SendMoney].SaveTransactionDate (Date + Time) ***/
    public List<SendMoneyInfo> getSendMoneyBySaveTransactionDate(String organizationCode, Date saveTransactionNoDate) {
        String sql = "SELECT * FROM SendMoney sm WHERE (sm.OrganizationCode = ?) AND (datetime(sm.SaveTransactionNoDate) = datetime(?))";
        return executeQueryList(sql, new String[]{organizationCode, valueOf(saveTransactionNoDate)}, SendMoneyInfo.class);
    }


    /*** [END] :: Fixed - [BHPROJ-0026-1056] :: [Android-บันทึก TransactionNo] ตรวจสอบ Not Existing ของ Field [SendMoney].SaveTransactionDate (Date + Time) ***/

    // YIM Edit here
    public SendMoneyInfo getLastSendMoneyToBankAndPayPoint(String EmpID,boolean isReference1) {
        if(BHGeneral.isChangeSolutionGenerateReference && isReference1){
            String sql = "Select MAX(substr(Reference1, 0, 9)) AS Reference1 from SendMoney WHERE CreateBy = ? AND Reference1 LIKE '2%'";
            return executeQueryObject(sql, new String[]{EmpID}, SendMoneyInfo.class);
        }else{
            String sql = "Select * from SendMoney WHERE CreateBy = ? AND ChannelItemID in (Select ChannelItemID from ChannelItem WHERE ChannelID = '0DEFE83D9B3E43059AE624D2D3EBBEA1' OR ChannelID = 'A42D219AE52049059FEE2783F5289B2D') ORDER BY CreateDate DESC LIMIT 1";
            return executeQueryObject(sql, new String[]{EmpID}, SendMoneyInfo.class);
        }
    }

}
