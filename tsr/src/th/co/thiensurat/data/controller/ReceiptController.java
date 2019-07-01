package th.co.thiensurat.data.controller;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.ReceiptInfo;

public class ReceiptController extends BaseController {

	public void addReceipt(ReceiptInfo info) {
		String sql = "INSERT INTO Receipt (ReceiptID, OrganizationCode, ReceiptCode, PaymentID, RefNo, DatePayment, TotalPayment, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql,
				new String[] { info.ReceiptID, info.OrganizationCode, info.ReceiptCode, info.PaymentID, info.RefNo, valueOf(info.DatePayment),
						valueOf(info.TotalPayment), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy,
						valueOf(info.SyncedDate) });

        TSRController.updateRunningNumber(TSRController.DocumentGenType.Receipt, info.ReceiptCode, null);
	}

	/// Remove by Anan.
//	public ReceiptInfo getLastReceipt(String documentTypeCode, String subteamCode, String employeeID, String yearMonth)
//	{
//		final String sql = "SELECT * FROM Receipt WHERE substr(receiptcode,1,1) = ? and substr(receiptcode,2,8) = ? and substr(receiptcode,10,2) = ? and substr(receiptcode,12,4) = ? order by receiptcode desc";
//		List<ReceiptInfo> tmpReceiptInfoList = executeQueryList(sql, new String[] { documentTypeCode, subteamCode, employeeID, yearMonth }, ReceiptInfo.class);
//		if (tmpReceiptInfoList == null)
//			return null;
//		else
//		    return tmpReceiptInfoList.get(0);
//	}

    public ReceiptInfo getLastReceiptWithSaleCode(String saleCode, String yearMonth) {

        final String sql = "select * from Receipt where ReceiptCode like '2%'  " +
                " and substr(ReceiptCode,2,?) = ? and substr(ReceiptCode,?+2,4) = ? " +
                " order by substr(ReceiptCode,?+6,4) desc" ;
        List<ReceiptInfo> tmpReceiptInfoList = executeQueryList(sql, new String[] { valueOf(saleCode.length()), saleCode ,valueOf(saleCode.length()), yearMonth, valueOf( saleCode.length())}, ReceiptInfo.class);
        if (tmpReceiptInfoList == null)
            return null;
        else
            return tmpReceiptInfoList.get(0);

    }

	public ReceiptInfo getLastReceipt(String employeeID, String yearMonth)
	{
		final String sql = "select * from Receipt where CreateBy = ?  and substr(ReceiptCode,length(ReceiptCode)-7,4) = ?  order by substr(ReceiptCode,length(ReceiptCode)-3,4) desc";
		List<ReceiptInfo> tmpReceiptInfoList = executeQueryList(sql, new String[] { employeeID, yearMonth }, ReceiptInfo.class);
		if (tmpReceiptInfoList == null)
			return null;
		else
		    return tmpReceiptInfoList.get(0);
	}

	public List<ReceiptInfo> getReceipt() {
		return executeQueryList("select * from Receipt", null, ReceiptInfo.class);
	}

    public List<ReceiptInfo> getReceiptByRefNo(String RefNo ) {
        String sql = "select * from Receipt where RefNo = ? order by CreateDate";
        return executeQueryList(sql, new String[]{RefNo}, ReceiptInfo.class);
    }

    public ReceiptInfo getReceiptByReceiptID (String organizationCode, String ReceiptID)
    {
        ReceiptInfo ret = null;
        final String sql = "select Receipt.*, Contract.*, DebtorCustomer.*, " +
                "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName " +
                "from Receipt " +
                "INNER JOIN Contract on Receipt.RefNo = Contract.RefNo " +
                "INNER JOIN DebtorCustomer on Contract.CustomerID = DebtorCustomer.CustomerID " +
                "where Receipt.organizationCode = ? " +
                "AND Receipt.ReceiptID = ? ";
        ret = executeQueryObject(sql, new String[]{organizationCode, ReceiptID}, ReceiptInfo.class);
        return ret;
    }

    public void updateReceipt (ReceiptInfo info) {
        final String QUERY_CONTRACT_UPDATE = "UPDATE Receipt SET ReceiptID = ?, OrganizationCode = ?, ReceiptCode = ?, PaymentID = ?, " +
                "RefNo = ?, DatePayment = ?, TotalPayment = ?, LastUpdateDate = ?, " +
                "LastUpdateBy = ? WHERE ReceiptID = ?";
        executeNonQuery(QUERY_CONTRACT_UPDATE, new String[]{info.ReceiptID, info.OrganizationCode, info.ReceiptCode, info.PaymentID,
                info.RefNo, valueOf(info.DatePayment), valueOf(info.TotalPayment), valueOf(info.LastUpdateDate),
                info.LastUpdateBy, info.ReceiptID});

        TSRController.updateRunningNumber(TSRController.DocumentGenType.Receipt, info.ReceiptCode, null);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteReceiptAll() {
        String sql = "delete from Receipt";
        executeNonQuery(sql, null);
    }
    public void deleteReceiptByRefNo(String refNo) {
        String sql = "DELETE FROM Receipt WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[]{refNo});
    }

    /*public void doVoidReceipt(String receiptId) {



        // Get Receipt Info
        String sql = "select * from Receipt where ReceiptID = ?";
        ReceiptInfo receipt = executeQueryObject(sql, new String[]{receiptId}, ReceiptInfo.class);

        //Update Receipt to void
        sql = "update Receipt set TotalPayment = 0  where ReceiptID = ?";
        executeNonQuery(sql, new String[]{receiptId});


        // Get payment amount
        sql = "select  sum(Amount) from [dbo].[SalePaymentPeriodPayment] where ReceiptID = ? group by ReceiptID";

        //Reserve PaymentPeriod Status

        sql =   "update SalePaymentPeriod " +
                "set PaymentComplete = 0, "  +
                "CloseAccountDiscountAmount = 0 where SalePaymentPeriodID in (select SalePaymentPeriodID " +
                "from SalePaymentPeriodPayment where ReceiptID = ?)";
        executeNonQuery(sql, new String[]{receiptId});

        ///Update Payment Amount
        sql =   "Update Payment " +
                "set PAYAMT = PAYAMT - (select  sum(Amount) from [SalePaymentPeriodPayment] where ReceiptID = ? group by ReceiptID) " +
                "where PaymentID = ?";
        executeNonQuery(sql,new String[]{receipt.ReceiptID, receipt.PaymentID});

        /// void  SalePayment Period Payment
        sql = "update SalePaymentPeriodPayment " +
                "set Amount = 0, CloseAccountDiscountAmount = 0 " +
                "where ReceiptID = ?";
        executeNonQuery(sql, new String[]{receiptId});

        // Reverse Contract Status
        // Get Contract Info
        sql = "select  * from Contract where RefNo = ?";
        ContractInfo contract = executeQueryObject(sql, new String[]{receipt.RefNo}, ContractInfo.class);

        if(contract.STATUS.equals("F")){
            sql = "Update Contract set STATUS = 'NORMAL', isActive = 1 where CustomerID = ?";
            executeNonQuery(sql, new String[]{contract.CustomerID});
        }

        // Reverse Customer Status
        sql = "select * from DebtorCustomer where CustomerID = ?";
        DebtorCustomerInfo customer = executeQueryObject(sql, new String[]{contract.CustomerID}, DebtorCustomerInfo.class);
        if(customer.DebtStatus == 2){
            sql = "update DebtorCustomer set DebtStatus = 1 where CustomerID = ?";
            executeNonQuery(sql, new String[]{customer.CustomerID});
        }

        // Revserse Close Account discount
        sql = "delete from ContractCloseAccount where PaymentID = ? and RefNo =?";
        executeNonQuery(sql, new String[]{receipt.PaymentID, receipt.RefNo});

    }*/

    public ReceiptInfo getReceiptForDoVoidReceipt(String receiptID, String refNo){
        String sql = "select " +
                        "r.ReceiptID, " +
                        "r.OrganizationCode, " +
                        "r.ReceiptCode, " +
                        "r.RefNo, " +
                        "r.PaymentID, " +
                        "ifnull(r.TotalPayment, 0) AS PaymentAmount, " +
                        "sum(ifnull(sppp.CloseAccountDiscountAmount, 0)) AS PaymentDiscount " +
                        "from Receipt as r " +
                        "inner join SalePaymentPeriodPayment as sppp on sppp.ReceiptID = r.ReceiptID " +
                        "where r.ReceiptID = ? and r.RefNo = ? " +
                        "group by r.ReceiptID, r.OrganizationCode, r.ReceiptCode, r.RefNo, r.PaymentID, r.TotalPayment";
        return executeQueryObject(sql, new String[]{receiptID, refNo}, ReceiptInfo.class);
    }

    public void doVoidReceipt(String ReceiptID, String RefNo, String PaymentID, float PaymentAmount, float PaymentDiscount) {
        String sql = null;

        /***Update Receipt TotalPayment = 0***/
        sql = "update Receipt " +
                "   set TotalPayment = 0 " +
                "where ReceiptID = ?";
        executeNonQuery(sql, new String[]{ReceiptID});


        /***Reserve PaymentPeriod Status***/
        sql = "update SalePaymentPeriod " +
                "   set PaymentComplete = 0, " +
                "   CloseAccountDiscountAmount = 0 " +
                "where SalePaymentPeriodID in ( " +
                "                                   select s.SalePaymentPeriodID " +
                "                                   from SalePaymentPeriod s " +
                "                                   inner join SalePaymentPeriodPayment sp on s.SalePaymentPeriodID = sp.SalePaymentPeriodID " +
                "                                   where sp.ReceiptID = ? " +
                "                               )";
        executeNonQuery(sql, new String[]{ReceiptID});


        /***Update Payment Amount***/
        sql = "Update Payment " +
                "   set PAYAMT = PAYAMT - ? " +
                "where PaymentID = ?";
        executeNonQuery(sql, new String[]{valueOf(PaymentAmount), PaymentID});


        /***Update Payment Amount by increase Discount***/
        sql = "Update Payment " +
                "   set PAYAMT = PAYAMT + ? " +
                "where PaymentID = ?";
        executeNonQuery(sql, new String[]{valueOf(PaymentDiscount), PaymentID});


        /***Void SalePayment Period Payment***/
        sql = "update SalePaymentPeriodPayment " +
                "   set Amount = 0, " +
                "   CloseAccountDiscountAmount = 0 " +
                "where ReceiptID = ?";
        executeNonQuery(sql, new String[]{ReceiptID});


        /***Revert Close Account***/
        sql = "delete from ContractCloseAccount " +
                "where PaymentID = ? " +
                "       and RefNo = ?";
        executeNonQuery(sql, new String[]{PaymentID, RefNo});


        ContractInfo contractInfo = executeQueryObject("select * from Contract where RefNo = ?", new String[]{RefNo}, ContractInfo.class);
        if (contractInfo != null) {

            /***Revert Contract Status***/
            if (contractInfo.STATUS != null
                    && contractInfo.STATUS.equals("F")) {

                sql = "Update Contract " +
                        "   set STATUS = 'NORMAL', " +
                        "   isActive = 1 " +
                        "where RefNo = ?";
                executeNonQuery(sql, new String[]{RefNo});

            }

            /***Revert Customer Status***/
            if (contractInfo.CustomerID != null) {
                DebtorCustomerInfo debtorCustomerInfo = executeQueryObject("select * from DebtorCustomer where CustomerID = ?", new String[]{contractInfo.CustomerID}, DebtorCustomerInfo.class);
                if (debtorCustomerInfo != null
                        && debtorCustomerInfo.DebtStatus == 2) {

                    sql = "Update DebtorCustomer " +
                            "   set DebtStatus = 1 " +
                            "where CustomerID = ?";
                    executeNonQuery(sql, new String[]{contractInfo.CustomerID});
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /*** [START] :: Fixed - [BHPROJ-1036-8870] - ใบเสร็จรับเงินหายไปหลังจากเครื่องค้าง ***/
    public ReceiptInfo getReceiptByReceiptCode(String ReceiptCode)
    {
        String sql = "SELECT * FROM Receipt WHERE ReceiptCode = ? LIMIT 1";
        return executeQueryObject(sql, new String[]{ReceiptCode}, ReceiptInfo.class);
    }
    /*** [END] :: Fixed - [BHPROJ-1036-8870] - ใบเสร็จรับเงินหายไปหลังจากเครื่องค้าง  ***/

    public ReceiptInfo getReceiptByReceiptID (String ReceiptID){
        String sql =  "SELECT * FROM Receipt WHERE ReceiptID = ?";
        return executeQueryObject(sql, new String[]{ReceiptID}, ReceiptInfo.class);
    }

}
