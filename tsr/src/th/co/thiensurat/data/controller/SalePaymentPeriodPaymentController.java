package th.co.thiensurat.data.controller;

import java.util.Date;
import java.util.List;

import th.co.thiensurat.data.info.SalePaymentPeriodPaymentInfo;

public class SalePaymentPeriodPaymentController extends BaseController {

	public void addSalePaymentPeriodPayment(SalePaymentPeriodPaymentInfo info) {
		final String QUERY_INSERT = "INSERT INTO SalePaymentPeriodPayment (SalePaymentPeriodID, PaymentID, Amount, ReceiptID, CreateDate, CreateBy, SyncedDate, CloseAccountDiscountAmount)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(QUERY_INSERT, new String[] { info.SalePaymentPeriodID, info.PaymentID, valueOf(info.Amount), info.ReceiptID, valueOf(info.CreateDate),
				info.CreateBy, valueOf(info.SyncedDate), valueOf(info.CloseAccountDiscountAmount) });
	}
	
	public List<SalePaymentPeriodPaymentInfo> getSalePaymentPeriodPayment() {
		return executeQueryList("select *from SalePaymentPeriodPayment", null, SalePaymentPeriodPaymentInfo.class);
	}

    public List<SalePaymentPeriodPaymentInfo> getSalePaymentPeriodPaymentByPaymentID(String PaymentID) {
        String sql = "SELECT sppp.*, spp.PaymentPeriodNumber, spp.PaymentComplete, spp.NetAmount, r.ReceiptCode " +
                " FROM SalePaymentPeriodPayment sppp " +
                " INNER JOIN SalePaymentPeriod spp on spp.SalePaymentPeriodID = sppp.SalePaymentPeriodID" +
				" INNER JOIN Receipt r on r.ReceiptID = sppp.ReceiptID" +
                " WHERE sppp.PaymentID = ?" +
                " ORDER BY spp.PaymentPeriodNumber ASC" ;
        return executeQueryList(sql,  new String[] {PaymentID}, SalePaymentPeriodPaymentInfo.class);
    }

    public List<SalePaymentPeriodPaymentInfo> getSalePaymentPeriodPaymentBySalePaymentPeriodID(String SalePaymentPeriodID) {
        String sql = "SELECT sppp.*, r.ReceiptCode, r.DatePayment " +   // Fixed - [BHPROJ-0025-745]
                " FROM SalePaymentPeriodPayment sppp " +
                "       INNER JOIN Receipt r on r.ReceiptID = sppp.ReceiptID " +
                " WHERE sppp.SalePaymentPeriodID = ? " +
                " ORDER BY sppp.CreateDate ASC" ;
        return executeQueryList(sql,  new String[] {SalePaymentPeriodID}, SalePaymentPeriodPaymentInfo.class);
    }

    public SalePaymentPeriodPaymentInfo getSummarySalePaymentPeriodPaymentBySalePaymentPeriodIDAndPayDate(String SalePaymentPeriodID, Date PayDate) {
        // receipt outstanding
        String sql = "SELECT IFNULL(SUM(sppp.Amount), 0) as SumAmount, spp.NetAmount" +
                " FROM SalePaymentPeriod spp" +
                " INNER JOIN SalePaymentPeriodPayment sppp ON sppp.SalePaymentPeriodID = spp.SalePaymentPeriodID" +
                " INNER JOIN Payment p ON p.PaymentID = sppp.PaymentID" +
                " WHERE spp.SalePaymentPeriodID = ? and p.PayDate <= ?" +
                " GROUP BY sppp.SalePaymentPeriodID, spp.NetAmount" ;
        return executeQueryObject(sql,  new String[] {SalePaymentPeriodID, valueOf(PayDate)}, SalePaymentPeriodPaymentInfo.class);
    }


//    public SalePaymentPeriodPaymentInfo getSummaryAmount(String refNO) {
//    	String sql = "select IFNULL(sum(Amount),0) as Amount"
//					+" from SalePaymentPeriod as spp inner join" 
//					+" SalePaymentPeriodPayment as sppp"
//					+" on spp.SalePaymentPeriodID=sppp.SalePaymentPeriodID"
//					+" where spp.RefNo=?"
//					+" group by spp.RefNo";
//    	return executeQueryObject(sql, new String[] {refNO}, SalePaymentPeriodPaymentInfo.class);
//    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteSalePaymentPeriodPaymentAll() {
        String sql = "DELETE FROM SalePaymentPeriodPayment";
        executeNonQuery(sql, null);
    }
    public void deleteSalePaymentPeriodPaymentByRefNo(String refNo) {
        String sql = "DELETE FROM SalePaymentPeriodPayment WHERE (PaymentID IN (SELECT PaymentID FROM Payment WHERE RefNo = ?))";
        executeNonQuery(sql, new String[]{refNo});
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////




}
