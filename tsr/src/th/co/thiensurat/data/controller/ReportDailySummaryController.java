package th.co.thiensurat.data.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.List;

import th.co.thiensurat.data.info.ReportDailyChangeContTranInfo;
import th.co.thiensurat.data.info.ReportDailyChangeProTranInfo;
import th.co.thiensurat.data.info.ReportDailyImpoundTranInfo;
import th.co.thiensurat.data.info.ReportDailySalesTranInfo;
import th.co.thiensurat.data.info.ReportDailySendDocumentTranInfo;
import th.co.thiensurat.data.info.ReportDailySendMoneyTranInfo;
import th.co.thiensurat.data.info.ReportDailySummaryInfo;
import th.co.thiensurat.data.info.ReportDailyWriteOffNPLTranInfo;

/**
 * Created by macmini04 on 25/12/2014.
 */
public class ReportDailySummaryController extends BaseController {

    enum SaleLevel {
        SaleLevel01, SaleLevel02, SaleLevel03, SaleLevel04, SaleLevel05, SaleLevel06, SaleLevel07, SaleLevel08, SaleLevel09, SaleLevel10
    }

    public List<ReportDailySummaryInfo> getReportDailySummary(int saleLevel, String fillterString) {
        List<ReportDailySummaryInfo> ret = null;

        String sql = "";
        String strGroupBy = "";
        String strWhere = "";

        if (saleLevel == 6) {
            strGroupBy = "SaleLevel06";
        } else if (saleLevel == 5) {
            strGroupBy = "SaleLevel06, SaleLevel05";
            strWhere = " SaleLevel06=?";
        } else if (saleLevel == 4) {
            strGroupBy = "SaleLevel06, SaleLevel05, SaleLevel04";
            strWhere = " SaleLevel05=?";
        } else if (saleLevel == 3) {
            strGroupBy = "SaleLevel06, SaleLevel05, SaleLevel04, SaleLevel03";
            strWhere = " SaleLevel04=?";
        } else if (saleLevel == 2) {
            strGroupBy = "SaleLevel06, SaleLevel05, SaleLevel04, SaleLevel03, SaleLevel02";
            strWhere = " SaleLevel03=?";
        } else if (saleLevel == 1) {
            strGroupBy = "SaleLevel06, SaleLevel05, SaleLevel04, SaleLevel03, SaleLevel02, SaleLevel01";
            strWhere = " SaleLevel02=?";
        }

        sql = "select " + strGroupBy + ",";
        sql += " count(CashSumNumber) as CashSumNumber,"
                + " sum(CashSumMoney) as CashSumMoney,"
                + " count(CashNumber) as CashNumber,"
                + " sum(CashMoney) as CashMoney,"
                + " count(CashOutNumber) as CashOutNumber,"
                + " sum(CashOutMoney) as CashOutMoney,"
                + " count(OutSumNumber) as OutSumNumber,"
                + " sum(OutSumMoney) as OutSumMoney,"
                + " count(OutNumber) as OutNumber,"
                + " sum(OutMoney) as OutMoney,"
                + " count(OutStandingNumber) as OutStandingNumber,"
                + " sum(OutStandingMoney) as OutStandingMoney,"
                + " count(SaleSumTotal) as SaleSumTotal,"
                + " sum(SaleSumMoney) as SaleSumMoney,"
                + " count(ProductTradeIn) as ProductTradeIn,"
                + " sum(SendMoneyAlready) as SendMoneyAlready,"
                + " sum(SendMoneyOut) as SendMoneyOut,"
                + " ifnull(count(ImpoundBySale),0) as ImpoundBySale,"
                + " ifnull(count(ImpoundByTeam),0) as ImpoundByTeam,"
                + " ifnull(count(ChangeProduct),0) as ChangeProduct,"
                + " ifnull(count(ChangeContractBySale),0) as ChangeContractBySale,"
                + " ifnull(count(ChangeContractByTeam),0) as ChangeContractByTeam,"
                + " ifnull(count(WriteOffVPN),0) as WriteOffVPN,"
                + " ifnull(count(SendContract),0) as SendContract,"
                + " ifnull(count(SendSlip),0) as SendSlip,"
                + " ifnull(count(SendImpound),0) as SendImpound,"
                + " ifnull(count(SendChangeProduct),0) as SendChangeProduct,"
                + " ifnull(count(SendChangeContract),0) as SendChangeContract,"
                + " ifnull(count(SendSentMoney),0) as SendSentMoney"
                + " from ReportDailySummary";

        if (saleLevel < 6) {
            sql += " where " + strWhere + "";
            sql += " group by " + strGroupBy + "";
            ret = executeQueryList(sql, new String[]{fillterString}, ReportDailySummaryInfo.class);
        } else {
            sql += " group by " + strGroupBy + "";
            ret = executeQueryList(sql, null, ReportDailySummaryInfo.class);
        }
        return ret;
    }

    // ******* DailySales ******

    public List<ReportDailySalesTranInfo> getReportDailySalesTran(String SaleLevel, String EmpD) {
        String sql = "";

        if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel01.toString())) {
            sql = "SELECT * FROM ReportDailySalesTran  WHERE  SaleLevel01 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel02.toString())) {
            sql = "SELECT * FROM ReportDailySalesTran  WHERE  SaleLevel02 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel03.toString())) {
            sql = "SELECT * FROM ReportDailySalesTran  WHERE  SaleLevel03 like ?";
        }
        return executeQueryList(sql, new String[]{EmpD + "%"}, ReportDailySalesTranInfo.class);
    }

    // ******* DailyImpound ******

    public List<ReportDailyImpoundTranInfo> getReportDailyImpoundTran(String SaleLevel, String EmpD) {
        String sql = "";

        if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel01.toString())) {
            sql = "SELECT * FROM ReportDailyImpoundTran  WHERE  SaleLevel01 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel02.toString())) {
            sql = "SELECT * FROM ReportDailyImpoundTran  WHERE  SaleLevel02 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel03.toString())) {
            sql = "SELECT * FROM ReportDailyImpoundTran  WHERE  SaleLevel03 like ?";
        }
        return executeQueryList(sql, new String[]{EmpD + "%"}, ReportDailyImpoundTranInfo.class);
    }


    // ******* DailyChangePro ******
    public List<ReportDailyChangeProTranInfo> getReportDailyChangeProTran(String SaleLevel, String EmpD) {
        String sql = "";

        if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel01.toString())) {
            sql = "SELECT * FROM ReportDailyChangeProTran  WHERE  SaleLevel01 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel02.toString())) {
            sql = "SELECT * FROM ReportDailyChangeProTran  WHERE  SaleLevel02 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel03.toString())) {
            sql = "SELECT * FROM ReportDailyChangeProTran  WHERE  SaleLevel03 like ?";
        }
        return executeQueryList(sql, new String[]{EmpD + "%"}, ReportDailyChangeProTranInfo.class);
    }

    // ******* DailyChangeCont ******
    public List<ReportDailyChangeContTranInfo> getReportDailyChangeContTran(String SaleLevel, String EmpD) {
        String sql = "";
        if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel01.toString())) {
            sql = "SELECT * FROM ReportDailyChangeContTran  WHERE  SaleLevel01 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel02.toString())) {
            sql = "SELECT * FROM ReportDailyChangeContTran  WHERE  SaleLevel02 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel03.toString())) {
            sql = "SELECT * FROM ReportDailyChangeContTran  WHERE  SaleLevel03 like ?";
        }
        return executeQueryList(sql, new String[]{EmpD + "%"}, ReportDailyChangeContTranInfo.class);
    }

    // ******* DailyWriteOffNPL ******
    public List<ReportDailyWriteOffNPLTranInfo> getReportDailyWriteOffNPLTran(String SaleLevel, String EmpD) {
        String sql = "";
        if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel01.toString())) {
            sql = "SELECT ReportDailyWriteOffNPLTran.* ,Fortnight.FortnightNumber FROM ReportDailyWriteOffNPLTran INNER JOIN Fortnight on ReportDailyWriteOffNPLTran.FortnightID = Fortnight.FortnightID  WHERE  SaleLevel01 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel02.toString())) {
            sql = "SELECT ReportDailyWriteOffNPLTran.* ,Fortnight.FortnightNumber FROM ReportDailyWriteOffNPLTran INNER JOIN Fortnight on ReportDailyWriteOffNPLTran.FortnightID = Fortnight.FortnightID  WHERE  SaleLevel02 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel03.toString())) {
            sql = "SELECT ReportDailyWriteOffNPLTran.* ,Fortnight.FortnightNumber FROM ReportDailyWriteOffNPLTran INNER JOIN Fortnight on ReportDailyWriteOffNPLTran.FortnightID = Fortnight.FortnightID  WHERE  SaleLevel03 like ?";
        }
        return executeQueryList(sql, new String[]{EmpD + "%"}, ReportDailyWriteOffNPLTranInfo.class);
    }

    // ******* DailySendMoney ******
    public List<ReportDailySendMoneyTranInfo> getReportDailySendMoneyTran(String SaleLevel, String EmpD) {
        String sql = "";
        if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel01.toString())) {
            sql = "SELECT * FROM ReportDailySendMoney  WHERE  SaleLevel01 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel02.toString())) {
            sql = "SELECT * FROM ReportDailySendMoney  WHERE  SaleLevel02 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel03.toString())) {
            sql = "SELECT * FROM ReportDailySendMoney  WHERE  SaleLevel03 like ?";
        }
        return executeQueryList(sql, new String[]{EmpD + "%"}, ReportDailySendMoneyTranInfo.class);

    }


    // ******* DailySendDocument ******
    public List<ReportDailySendDocumentTranInfo> getReportDailySendDocumentTran(String SaleLevel, String EmpD) {
        String sql = "";
        if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel01.toString())) {
            sql = "SELECT * FROM ReportDailySendDocument  WHERE  SaleLevel01 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel02.toString())) {
            sql = "SELECT * FROM ReportDailySendDocument  WHERE  SaleLevel02 like ?";
        } else if (SaleLevel.equals(ReportDailySummaryController.SaleLevel.SaleLevel03.toString())) {
            sql = "SELECT * FROM ReportDailySendDocument  WHERE  SaleLevel03 like ?";
        }
        return executeQueryList(sql, new String[]{EmpD + "%"}, ReportDailySendDocumentTranInfo.class);

    }
}
