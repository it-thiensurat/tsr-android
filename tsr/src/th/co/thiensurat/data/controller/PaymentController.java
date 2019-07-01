package th.co.thiensurat.data.controller;

import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.data.info.PaymentInfo;

public class PaymentController extends BaseController {

    // GetSummaryPaymentGroupByPaymentDate
//    public List<PaymentInfo> getSummaryPaymentGroupByPaymentDate(
//            String organizationCode) {
//        List<PaymentInfo> ret;
//        // String sql =
//        // "select datetime(date(PayDate)) as PayDate, ifnull(sum(PAYAMT),0) as SumPaymentAmount"
//        // +"	from Payment"
//        // +"	where OrganizationCode = ?"
//        // +"	group by datetime(date(PayDate))";
//        String sql = "SELECT p.PayDate, ifnull(p.PAYAMT,0)-ifnull(sm.SendAmount,0) as PAYAMT"
//                + "	FROM ("
//                + "	select datetime(date(PayDate)) as PayDate,"
//                + "	 ifnull(sum(PAYAMT),0) as PAYAMT"
//                + "	 from Payment"
//                + "	where OrganizationCode = ?"
//                + "	group by datetime(date(PayDate))"
//                + "	) AS p left outer join"
//                + "	( select datetime(date(PaymentDate)) as PaymentDate,"
//                + "	 ifnull(sum(SendAmount),0) as SendAmount"
//                + "	 from SendMoney"
//                + "	where OrganizationCode = ?"
//                + "	group by datetime(date(PaymentDate))"
//                + "	) as sm on p.PayDate=sm.PaymentDate"
//                + " where ifnull(p.PAYAMT,0)-ifnull(sm.SendAmount,0) > 0";
//        ret = executeQueryList(sql, new String[]{organizationCode,
//                organizationCode}, PaymentInfo.class);
//        return ret;
//    }

    public List<PaymentInfo> getSummaryPaymentGroupByPaymentDate(
            String organizationCode, String TeamCode, String EmpIDOfMember) {
        String sql = "SELECT  p.PayDate, IFNULL(p.PAYAMT, 0) - IFNULL(sm.SendAmount, 0) as PAYAMT "
                + " FROM ("
                + "         SELECT DATE(p.PayDate) as PayDate, IFNULL(SUM(p.PAYAMT), 0) as PAYAMT"
                + "	        FROM Payment p "
                /*** [START] Fixed - [BHPROJ-0026-928] :: [Payment].Status = 'Y' (คือ ต้องทำการส่งเงินด้วย)   ***/
//                + "                 INNER JOIN Contract c ON c.RefNo = p.RefNo"
//                + "	        WHERE (c.isActive = 1 OR (c.isActive = 0 AND c.torefno IS NULL))"
                + "	        WHERE (ifnull(p.[Status], 'Y') = 'Y')"
                /*** [END] Fixed - [BHPROJ-0026-928] ***/

                /*** [START] Fixed - [BHPROJ-0024-604] ***/
//                + "               AND p.OrganizationCode = ? AND p.TeamCode = ? AND p.EmpID IS NOT NULL AND p.EmpID IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + ")"
                + "               AND (p.OrganizationCode = ?) AND (p.TeamCode = ?) AND (p.CreateBy IS NOT NULL) AND (p.CreateBy IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + "))"
                /*** [END] Fixed - [BHPROJ-0024-604] ***/

                + "	        GROUP BY DATE(p.PayDate)"
                + "	) AS p "
                + " LEFT OUTER JOIN ( "
                + "         SELECT DATE(PaymentDate) as PaymentDate"
                + "	            , IFNULL(SUM(SendAmount),0) as SendAmount"
                + "     	FROM SendMoney"
                + "	        WHERE OrganizationCode = ? AND CreateBy IS NOT NULL AND CreateBy IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + ")"
                //+ "             AND (Reference2 NOT LIKE '%Migrate%')"
                + "	        GROUP BY DATE(PaymentDate)"
                + "	) AS sm ON p.PayDate = sm.PaymentDate"
                + " WHERE IFNULL(p.PAYAMT, 0) - IFNULL(sm.SendAmount, 0) > 0";
        return executeQueryList(sql, BHUtilities.makeStringArray(new String[]{organizationCode, TeamCode, EmpIDOfMember, organizationCode, EmpIDOfMember}), PaymentInfo.class);
    }
    //YIM check date match And payment.status is 'Y'
    public PaymentInfo getMoneyOnHand(String OrganizationCode, String PaymentType, String TeamCode, String EmpIDOfMember) {
        /*
        String sql = " SELECT IFNULL(p.SumPayment, 0) AS SumPayment, IFNULL(sm.SumSendMoney, 0) AS PAYAMT,IFNULL(p.SumPayment, 0) - IFNULL(sm.SumSendMoney, 0) AS MoneyOnHand "
                + " FROM ("
                + "         SELECT IFNULL(SUM(p.PAYAMT), 0) AS SumPayment"
                + "         FROM Payment p"
                + "         INNER JOIN Contract c ON c.RefNo = p.RefNo"
                + "	        WHERE (c.isActive = 1 OR (c.isActive = 0 AND c.torefno IS NULL))"
                + "             AND p.OrganizationCode = ? AND p.PaymentType = ? AND p.TeamCode = ? AND p.EmpID IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + ")"
                + " ) AS p LEFT OUTER JOIN ("
                + "         SELECT IFNULL(SUM(SendAmount), 0) AS SumSendMoney "
                + "         FROM SendMoney "
                + "         WHERE OrganizationCode = ? AND PaymentType = ? AND CreateBy IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + ")"
                + " ) AS sm";
                */
        String sql = " SELECT (SUM(IFNULL(p.SumPayment, 0)) - SUM(IFNULL(sm.SumSendMoney, 0))) AS MoneyOnHand "
                + " FROM ("
                + "         SELECT Status,DATE(PayDate) as PayDate,IFNULL(SUM(PAYAMT), 0) as SumPayment"
                + "	        FROM Payment"
                + "	        WHERE (IFNULL(Status, 'Y') = 'Y')"
                + "             AND (OrganizationCode = ?)"
                + "             AND (PaymentType = ?)"
                + "             AND (TeamCode = ?)"
                + "             AND (CreateBy IS NOT NULL)"
                + "             AND (CreateBy IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + "))"
                + "	        GROUP BY DATE(PayDate)"
                + "	) AS p "
                + " LEFT OUTER JOIN ( "
                + "         SELECT DATE(PaymentDate) as PaymentDate,IFNULL(SUM(SendAmount),0) as SumSendMoney"
                + "     	FROM SendMoney"
                + "	        WHERE (OrganizationCode = ?)"
                + "             AND (PaymentType = ?)"
                + "             AND (CreateBy IS NOT NULL)"
                + "             AND (CreateBy IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + ")) "
                + "             AND (IFNULL(Reference2,'') <> 'MigrateRef2') "
                + "	        GROUP BY DATE(PaymentDate)"
                + "	) AS sm ON p.PayDate = sm.PaymentDate"
                + " WHERE IFNULL(p.SumPayment, 0) - IFNULL(sm.SumSendMoney, 0) > 0";
        return executeQueryObject(sql, BHUtilities.makeStringArray(new String[]{OrganizationCode, PaymentType, TeamCode, EmpIDOfMember, OrganizationCode, PaymentType, EmpIDOfMember}), PaymentInfo.class);


    }

    // GetSummaryPaymentGroupByPaymentTypeByPaymentDate
//    public List<PaymentInfo> getSummaryPaymentGroupByPaymentTypeByPaymentDate(
//            String organizationCode, Date paymentDate) {
//        List<PaymentInfo> ret;
//        // String sql = "select PaymentType, ifnull(sum(PAYAMT),0) as PAYAMT,"
//        // +" CASE WHEN PaymentType= 'Credit' THEN 'เซลล์สลิป'"
//        // +" WHEN PaymentType= 'Cheque' THEN 'เช็ค'"
//        // +" WHEN PaymentType= 'Cash' THEN 'เงินสด'"
//        // +" ELSE '' END AS PaymentTypeName"
//        // +"	from Payment"
//        // +"	where OrganizationCode = ?"
//        // +" and (strftime('%Y-%m-%d', PayDate) =?)"
//        // +"	group by PaymentType";
//        String sql = "select p.PaymentType,   ifnull(p.PAYAMT,0) as PAYAMT, ifnull(sm.SendAmount,0) as SendAmount, "
//                + "	 ifnull(p.PAYAMT,0) - ifnull(sm.SendAmount,0) as DiffSendAmount,"
//                + " 		CASE WHEN p.PaymentType= 'Credit' THEN 'เซลล์สลิป'"
//                + " 			WHEN p.PaymentType= 'Cheque' THEN 'เช็ค'"
//                + " 			WHEN p.PaymentType= 'Cash' THEN 'เงินสด'"
//                + " 		ELSE '' END AS PaymentTypeName"
//                + "	from"
//                + "	("
//                + "	select datetime(date(PayDate)) as PayDate, PaymentType, ifnull(sum(PAYAMT),0) as PAYAMT"
//                + "	from Payment"
//                + "	 where OrganizationCode = ?"
//                + "	and (strftime('%Y-%m-%d', PayDate) = ?)"
//                + "	group by datetime(date(PayDate)), PaymentType"
//                + "	) as p left outer join"
//                + "	("
//                + "	select datetime(date(PaymentDate)) as PaymentDate, PaymentType, ifnull(sum(SendAmount),0) as SendAmount"
//                + "	from SendMoney"
//                + "	 where OrganizationCode = ?"
//                + "	and (strftime('%Y-%m-%d', PaymentDate) = ?)"
//                + "	group by datetime(date(PaymentDate)), PaymentType"
//                + "	) as sm on datetime(date(p.PayDate))=datetime(date(sm.PaymentDate)) and p.PaymentType=sm.PaymentType"
//                + "	where  DiffSendAmount > 0";
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String paymentDateString = sdf.format(paymentDate);
//
//        ret = executeQueryList(sql, new String[]{organizationCode,
//                        paymentDateString, organizationCode, paymentDateString},
//                PaymentInfo.class);
//        return ret;
//    }

    public List<PaymentInfo> getSummaryPaymentGroupByPaymentTypeByPaymentDate(
            String organizationCode, Date paymentDate, String TeamCode, String EmpIDOfMember) {
        String sql = "SELECT p.PaymentType "
                + "         , IFNULL(p.PAYAMT, 0) AS PAYAMT"
                + "         , IFNULL(sm.SendAmount, 0) AS SendAmount"
                + "         , IFNULL(p.PAYAMT, 0) - IFNULL(sm.SendAmount, 0) AS DiffSendAmount"
                + "         , CASE WHEN p.PaymentType= 'Credit' THEN 'เซลล์สลิป'"
                + "               WHEN p.PaymentType= 'Cheque' THEN 'เช็ค'"
                + "               WHEN p.PaymentType= 'Cash' THEN 'เงินสด'"
                + "               ELSE '' END AS PaymentTypeName"
                + " FROM ("
                + "         SELECT DATE(p.PayDate) AS PayDate"
                + "                 , p.PaymentType"
                + "                 , IFNULL(SUM(p.PAYAMT), 0) AS PAYAMT"
                + "          FROM Payment p"
                /*** [START] Fixed - [BHPROJ-0026-928] :: [Payment].Status = 'Y' (คือ ต้องทำการส่งเงินด้วย)   ***/
//                + "                 INNER JOIN Contract c ON c.RefNo = p.RefNo"
//                + "	         WHERE ((c.isActive = 1) OR ((c.isActive = 0) AND (c.torefno IS NULL)))"
                + "	         WHERE (ifnull(p.[Status], 'Y') = 'Y')"
                + "                 AND (p.OrganizationCode = ?) AND (DATE(p.PayDate) = ?)"
                /*** [END] Fixed - [BHPROJ-0026-928] ***/

                /*** [START] Fixed - [BHPROJ-0024-604] ***/
//                + "                AND p.TeamCode = ? AND p.EmpID IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + ")"
                + "                AND (p.TeamCode = ?) AND (p.CreateBy IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + ")) "
                /*** [END] Fixed - [BHPROJ-0024-604] ***/

                + "          GROUP BY DATE(p.PayDate), p.PaymentType"
                + " ) AS p LEFT OUTER JOIN ("
                + "         SELECT DATE(PaymentDate) AS PaymentDate"
                + "                , PaymentType"
                + "                , IFNULL(SUM(SendAmount), 0) AS SendAmount"
                + "         FROM SendMoney"
                + "         WHERE OrganizationCode = ? AND DATE(PaymentDate) = ?"
                + "                AND CreateBy IN (" + BHUtilities.makePlaceholders(EmpIDOfMember) + ")"
                + "         GROUP BY DATE(PaymentDate), PaymentType"
                + " ) AS sm ON DATE(p.PayDate) = DATE(sm.PaymentDate) AND p.PaymentType = sm.PaymentType"
                + " WHERE  DiffSendAmount > 0";
        return executeQueryList(sql, BHUtilities.makeStringArray(new String[]{organizationCode, valueOf(paymentDate, dateFormatWithoutTime), TeamCode, EmpIDOfMember, organizationCode, valueOf(paymentDate, dateFormatWithoutTime), EmpIDOfMember}), PaymentInfo.class);
    }

    public void addPayment(PaymentInfo info) {
        String sql = "INSERT INTO Payment (PaymentID, OrganizationCode, SendMoneyID, PaymentType, PayPartial, BankCode, ChequeNumber, ChequeBankBranch, "
                + "ChequeDate, CreditCardNumber, CreditCardApproveCode, CreditEmployeeLevelPath, TripID, Status, RefNo, PayPeriod, PayDate, PAYAMT, CashCode, EmpID, TeamCode, "
                + "receiptkind, Kind, BookNo, ReceiptNo, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.PaymentID,
                info.OrganizationCode, info.SendMoneyID, info.PaymentType,
                valueOf(info.PayPartial), info.BankCode, info.ChequeNumber,
                info.ChequeBankBranch, info.ChequeDate, info.CreditCardNumber,
                info.CreditCardApproveCode, info.CreditEmployeeLevelPath,
                info.TripID, info.Status, info.RefNo, info.PayPeriod,
                valueOf(info.PayDate), valueOf(info.PAYAMT), info.CashCode,
                info.EmpID, info.TeamCode, info.receiptkind, info.Kind,
                info.BookNo, info.ReceiptNo, valueOf(info.CreateDate),
                info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy,
                valueOf(info.SyncedDate)});
    }

    public List<PaymentInfo> getPaymentByRefNo(String organizationCode,
                                               String refNo) {
        String sql = "SELECT * FROM Payment WHERE (OrganizationCode = ?) AND (RefNo = ?) ORDER BY PayDate ASC";
        return executeQueryList(sql, new String[]{organizationCode, refNo},
                PaymentInfo.class);
    }

    public List<PaymentInfo> getPaymentByRefNoAndReceiptID(String organizationCode,
                                                           String refNo, String receiptID) {
        String sql = "SELECT * FROM Payment WHERE (Payment.OrganizationCode = ?) AND (Payment.RefNo = ?) AND (Payment.PaymentID = (select Receipt.PaymentID from Receipt where Receipt.ReceiptID = ? ))ORDER BY PayDate ASC";
        return executeQueryList(sql, new String[]{organizationCode, refNo, receiptID},
                PaymentInfo.class);
    }

    public PaymentInfo getPaymentRefNo(String refNo) {
        String sql = "SELECT * FROM Payment WHERE RefNo = ? Order By PayDate";
        return executeQueryObject(sql, new String[]{refNo},
                PaymentInfo.class);
    }

    public PaymentInfo getSummaryPaymentByRefNo(String RefNo, Date PayDate) {
//        String sql = "SELECT IFNULL(SUM(p.PAYAMT), 0) AS SumPayment" +
//                " FROM Payment p " +
//                " WHERE p.RefNo = ?" ;

        String sql = "SELECT IFNULL(SUM(p.PAYAMT), 0) AS SumPayment, p.RefNo" +
                " FROM Payment p" +
                " WHERE p.RefNo = ?" +
                " AND p.PayDate < ?" +
                " GROUP BY p.RefNo";
        return executeQueryObject(sql, new String[]{RefNo, valueOf(PayDate)}, PaymentInfo.class);
    }


//	public PaymentInfo getPaymentReceiptByRefNo(String organizationCode, String refNo) {
//		String sql = "SELECT pay.PayDate, r.ReceiptCode, c.CONTNO, c.EFFDATE, dc.PrefixName || dc.CustomerName as CustomerName,"
//				+" dc.IDCard, p.ProductCode, p.ProductModel, pay.PAYAMT"
//				+" FROM Payment as pay inner join Contract as c on pay.OrganizationCode=c.OrganizationCode and pay.RefNo=c.RefNo inner join DebtorCustomer as dc on c.OrganizationCode=dc.OrganizationCode and c.CustomerID=dc.CustomerID inner join Product as p on c.OrganizationCode=p.OrganizationCode and c.ProductID=p.ProductID inner join Receipt as r on pay.PaymentID=r.PaymentID"
//				+" where pay.OrganizationCode and pay.RefNo=?";
//	return executeQueryObject(sql, new String[] { organizationCode, refNo },
//			PaymentInfo.class);
//	}


    public List<PaymentInfo> getPayment() {
        return executeQueryList("select *from Payment", null, PaymentInfo.class);
    }

    public List<PaymentInfo> getPaymentForReceiptByRefNo(String RefNo,String OrganizationCode) {
        String documentType = DocumentHistoryController.DocumentType.Receipt.toString();

        /** Fixed - [BHPROJ-0026-3267][Android-ใบเสร็จรับเงินที่พิมพ์ออกมา] ปรับรูปแบบใบเสร็จตามรายละเอียดที่แจ้งมาในรูป **/
        String sql ="SELECT p.CashCode, p.RefNo, sppp.SalePaymentPeriodID, sppp.PaymentID, sppp.ReceiptID, r.ReceiptCode, r.CreateBy, p.PAYAMT, p.PayDate, p.PaymentType, p.ChequeNumber, p.ChequeBankBranch, p.ChequeDate, p.CreditCardNumber, p.CreditCardApproveCode, p.CreditEmployeeLevelPath, p.EmpID, spp.NetAmount, spp.PaymentPeriodNumber, spp.PaymentAppointmentDate, sppp.Amount, " +
                "           spp.NetAmount - sppp.Amount  - sppp.CloseAccountDiscountAmount - (SELECT ifnull(sum(SalePaymentPeriodPayment.Amount), 0) + ifnull(sum(SalePaymentPeriodPayment.CloseAccountDiscountAmount), 0) " +
                "                                                                            FROM Payment " +
                "                                                                                 INNER JOIN SalePaymentPeriodPayment ON SalePaymentPeriodPayment.PaymentID = Payment.PaymentID " +
                "                                                                            WHERE Payment.RefNo = p.RefNo " +
                "                                                                                  AND Payment.PayDate < p.PayDate " +
                "                                                                                  AND SalePaymentPeriodPayment.SalePaymentPeriodID = sppp.SalePaymentPeriodID ) AS BalancesOfPeriod, " +
                "           (SELECT sum(NetAmount) " +
                "            FROM SalePaymentPeriod " +
                "            WHERE RefNo = p.RefNo) - (SELECT ifnull(sum(SalePaymentPeriodPayment.Amount), 0) + ifnull(sum(SalePaymentPeriodPayment.CloseAccountDiscountAmount), 0) " +
                "                                       FROM SalePaymentPeriod " +
                "                                       INNER JOIN SalePaymentPeriodPayment ON SalePaymentPeriodPayment.SalePaymentPeriodID = SalePaymentPeriod.SalePaymentPeriodID " +
                "                                       INNER JOIN Payment ON Payment.PaymentID = SalePaymentPeriodPayment.PaymentID AND Payment.RefNo = SalePaymentPeriod.RefNo AND Payment.PayDate <= p.PayDate " +
                "                                       WHERE SalePaymentPeriod.RefNo = p.RefNo AND SalePaymentPeriod.PaymentPeriodNumber <= spp.PaymentPeriodNumber) AS Balances, " +
                "            c.EFFDATE, c.CONTNO, c.CustomerID, c.MODE, c.MODEL, c.ProductSerialNumber, Product.ProductName, ifnull(b.BankName, '') AS BankName, " +
                "            md.ManualVolumeNo, md.ManualRunningNo, " +
                "            ifnull(e.FirstName, '') || ' ' || ifnull(e.LastName, '') as SaleEmployeeName, p.TeamCode, " +
                "            cca.OutstandingAmount AS CloseAccountOutstandingAmount, cca.DiscountAmount AS CloseAccountDiscountAmount, cca.NetAmount AS CloseAccountNetAmount, ssp1.PaymentPeriodNumber AS CloseAccountPaymentPeriodNumber, " +
                "            cca.PaymentID AS CloseAccountPaymentID " +
                "FROM Payment AS p " +
                "     INNER JOIN SalePaymentPeriodPayment AS sppp ON sppp.PaymentID = p.PaymentID " +
                "     INNER JOIN SalePaymentPeriod AS spp ON spp.SalePaymentPeriodID = sppp.SalePaymentPeriodID " +
                "     INNER JOIN Receipt AS r ON r.ReceiptID = sppp.ReceiptID " +
                "     INNER JOIN Contract AS c ON c.RefNo = p.RefNo " +
                "     INNER JOIN DebtorCustomer AS dc ON dc.CustomerID = c.CustomerID " +
                "     INNER JOIN Product ON Product.ProductID = c.ProductID " +
                "     LEFT  JOIN Bank AS b ON b.BankCode = p.BankCode " +
                "     LEFT  JOIN ManualDocument AS md ON md.DocumentID= (SELECT DocumentID FROM ManualDocument " +
                "                                                        WHERE DocumentNumber = r.ReceiptID AND ManualDocTypeID = ? " +
                "                                                              AND CreatedDate = (SELECT MAX(CreatedDate) FROM ManualDocument " +
                "                                                                                  WHERE DocumentNumber = r.ReceiptID AND ManualDocTypeID = ? )) " +
                "     LEFT  JOIN Employee AS e ON e.EmpID = p.EmpID " +
                "     LEFT  JOIN ContractCloseAccount AS cca ON cca.RefNo = p.RefNo " +
                "     LEFT  JOIN SalePaymentPeriod AS ssp1 ON ssp1.SalePaymentPeriodID = cca.SalePaymentPeriodID " +
                "WHERE p.RefNo = ? " +
                "AND p.OrganizationCode = ? " +
                //"AND  (spp.PaymentPeriodNumber <= ssp1.PaymentPeriodNumber OR ssp1.PaymentPeriodNumber IS NULL) " +
                //"ORDER BY spp.PaymentPeriodNumber,p.PayDate  ASC";
                "ORDER BY p.PayDate ASC, spp.PaymentPeriodNumber ASC";
        return executeQueryList(sql, new String[] { documentType, documentType, RefNo, OrganizationCode}, PaymentInfo.class);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deletePaymentAll() {
        String sql = "DELETE FROM Payment";
        executeNonQuery(sql, null);
    }
    public void deletePaymentByRefNo(String refNo) {
        String sql = "DELETE FROM Payment WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[] { refNo });
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public PaymentInfo getPaymentByPaymentID(String PaymentID) {
        String sql =  "SELECT * FROM Payment WHERE PaymentID = ?";
        return executeQueryObject(sql, new String[]{PaymentID}, PaymentInfo.class);
    }

}
