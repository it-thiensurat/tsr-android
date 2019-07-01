package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.DocumentHistoryInfo;

public class DocumentHistoryController extends BaseController {

    public static enum DocumentType {
        Contract("0"), Receipt("1"), ChangeProduct("2"), ImpoundProduct("3"), ChangeContract("4"), ManualDocument("5"), PayInSlipBank("6"), PayInSlipPayPoint("7");
        private String value;

        private DocumentType(String val) {
            this.value = val;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public List<DocumentHistoryInfo> getDocumentHistoryByType(String organizationCode, String DocumentType) {

        List<DocumentHistoryInfo> ret = null;
        String QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = null;
        switch (DocumentType) {
            case "0": //ใบสัญญา
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, Contract.*, DebtorCustomer.*, Contract.CONTNO AS DocumentNo, Contract.isActive, "
                        + "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName "
                        + "FROM DocumentHistory "
                        + "INNER JOIN Contract on DocumentHistory.DocumentNumber = Contract.RefNo "
                        + "INNER JOIN DebtorCustomer on Contract.CustomerID = DebtorCustomer.CustomerID "
                        + "WHERE DocumentHistory.OrganizationCode = ? "
                        + "AND DocumentHistory.DocumentType = ? "
                        + "AND (DocumentHistory.Status IS NULL OR DocumentHistory.Status != 'Received') "
                        + "AND (DocumentHistory.PrintOrder > 1 OR DocumentHistory.DocumentNumber in (select cc.OldSaleID "
                        + "                                                                           from ChangeContract as cc "
                        + "                                                                           where cc.Status = 'COMPLETED')) "
                        + "ORDER BY Contract.CONTNO ASC ";
                break;
            case "1": //ใบเสร็จ
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*,Receipt.*, Contract.*, DebtorCustomer.*, Receipt.ReceiptCode AS DocumentNo, Contract.isActive, "
                        + "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName "
                        + "FROM DocumentHistory "
                        + "INNER JOIN Receipt on DocumentHistory.DocumentNumber = Receipt.ReceiptID "
                        + "INNER JOIN Contract on Receipt.RefNo = Contract.RefNo "
                        + "INNER JOIN DebtorCustomer on Contract.CustomerID = DebtorCustomer.CustomerID "
                        + "WHERE DocumentHistory.OrganizationCode = ? "
                        + "AND DocumentHistory.DocumentType = ? "
                        + "AND (DocumentHistory.Status IS NULL OR DocumentHistory.Status != 'Received') "
                        + "AND DocumentHistory.PrintOrder > 1 "
                        + "ORDER BY Receipt.ReceiptCode ASC ";
                break;
            case "2": //ใบเปลี่ยนเครื่อง
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, "
                        + "ChangeProduct.RefNo, ChangeProduct.ChangeProductPaperID as ChangeProductPaperID, ChangeProduct.ChangeProductPaperID as DocumentNo, Contract.isActive, "
                        + "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName "
                        + "FROM DocumentHistory "
                        + "INNER JOIN ChangeProduct on DocumentHistory.DocumentNumber = ChangeProduct.ChangeProductID "
                        + "INNER JOIN Contract on Contract.refNo = ChangeProduct.RefNo "
                        + "INNER JOIN DebtorCustomer on DebtorCustomer.CustomerID = Contract.CustomerID "
					    + "WHERE (DocumentHistory.OrganizationCode = ?) AND (DocumentHistory.DocumentType = ? ) "
                        + "AND DocumentHistory.PrintOrder > 1 "
                        + "ORDER BY ChangeProduct.ChangeProductPaperID ASC ";
                break;
            case "3": //ใบถอดเครื่อง
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, "
                        + "ImpoundProduct.RefNo, ImpoundProduct.ImpoundProductPaperID as ImpoundProductPaperID, ImpoundProduct.ImpoundProductPaperID as DocumentNo, Contract.isActive,"
                        + "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName "
                        + "FROM DocumentHistory "
                        + "INNER JOIN ImpoundProduct on DocumentHistory.DocumentNumber = ImpoundProduct.ImpoundProductID "
                        + "INNER JOIN Contract on Contract.refNo = ImpoundProduct.RefNo "
                        + "INNER JOIN DebtorCustomer on DebtorCustomer.CustomerID = Contract.CustomerID "
    					+ "WHERE (DocumentHistory.OrganizationCode = ?) AND (DocumentHistory.DocumentType = ?) "
                        + "AND DocumentHistory.PrintOrder > 1 "
                        + "ORDER BY ImpoundProduct.ImpoundProductPaperID ASC ";
                break;
            case "4": //ใบเปลี่ยนสัญญา
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, "
                        + "ChangeContract.NewSaleID as RefNo, ChangeContract.ChangeContractPaperID as ChangeContractPaperID, ChangeContract.ChangeContractPaperID as DocumentNo, Contract.isActive, "
                        + "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName "
                        + "FROM DocumentHistory "
                        + "INNER JOIN ChangeContract on DocumentHistory.DocumentNumber = ChangeContract.ChangeContractID "
                        + "INNER JOIN Contract on Contract.refNo = ChangeContract.NewSaleID "
                        + "INNER JOIN DebtorCustomer on DebtorCustomer.CustomerID = Contract.CustomerID "
					    + "WHERE (DocumentHistory.OrganizationCode = ?) AND (DocumentHistory.DocumentType = ?) "
                        + "AND DocumentHistory.PrintOrder > 1 "
                        + "ORDER BY ChangeContract.ChangeContractPaperID ASC ";
                break;
            case "5": //เอกสารมือ
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.rowid AS No,DocumentHistory.*, " +
                        " ManualDocument.DocumentID AS ManualDocumentID, ManualDocument.ManualDocTypeID AS ManualDocumentTypeID,  " +
                        " ManualDocument.DocumentNumber AS ManualDocumentNumber,  " +
                        " con0.CONTNO AS con0CONTNO, " +
                        " ifnull(dc0.PrefixName,'')  || ' ' ||  ifnull(dc0.CustomerName,'') || ifnull(dc0.CompanyName,'') AS dc0CustomerFullName,  con0.isActive as isActive0, " +
                        " re1.ReceiptCode AS re1ReceiptCode, " +
                        " ifnull(dc1.PrefixName,'')  || ' ' ||  ifnull(dc1.CustomerName,'') || ifnull(dc1.CompanyName,'') AS dc1CustomerFullName, con1.isActive as isActive1" +
                        " FROM DocumentHistory  " +
                        " INNER JOIN ManualDocument on DocumentHistory.DocumentNumber = ManualDocument.DocumentID " +
                        " LEFT JOIN Contract AS con0 on con0.RefNo = ManualDocument.DocumentNumber" +
                        " LEFT JOIN DebtorCustomer AS dc0 on dc0.CustomerID = con0.CustomerID" +
                        " LEFT JOIN Receipt AS re1 on re1.ReceiptID = ManualDocument.DocumentNumber  " +
                        " LEFT JOIN Contract AS con1 on con1.RefNo = re1.RefNo  " +
                        " LEFT JOIN DebtorCustomer AS dc1 on dc1.CustomerID = con1.CustomerID  " +
                        " WHERE DocumentHistory.OrganizationCode = ? " +
                        "       AND DocumentHistory.DocumentType = ? " +
                        "       AND (DocumentHistory.Status IS NULL OR DocumentHistory.Status != 'Received')  " +
                        "       AND (DocumentHistory.PrintOrder > 1 " +
                        "            OR DocumentHistory.DocumentNumber in (select md0.DocumentID " +
                        "                                                  from ManualDocument as md0 " +
                        "                                                  where md0.ManualDocTypeID = 0 " +
                        "                                                        and md0.DocumentNumber in (select cc0.OldSaleID " +
                        "                                                                                   from ChangeContract as cc0 " +
                        "                                                                                   where cc0.Status = 'COMPLETED'))) " +
                        " ORDER BY con0CONTNO OR re1ReceiptCode ASC";
                break;
            case "6": //Slip ธนาคาร
                //SendMoney.CreatedBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, SendMoney.TransactionNo, " +
                        "ifnull(Employee.FirstName,'')  || ' ' ||  ifnull(Employee.LastName,'') AS employeeFullName " +
                        "FROM DocumentHistory " +
                        "INNER JOIN SendMoney on DocumentHistory.DocumentNumber = SendMoney.SendMoneyID " +
                        "LEFT JOIN Employee on SendMoney.CreateBy = Employee.EmpID " +
                        "WHERE DocumentHistory.OrganizationCode = ? " +
                        "AND DocumentHistory.DocumentType = ? " +
                        "AND (DocumentHistory.Status IS NULL OR DocumentHistory.Status != 'Received') " +
                        "AND DocumentHistory.PrintOrder  = 1 " +
                        "ORDER BY SendMoney.TransactionNo ASC ";
                break;
            case "7": //Slip เพย์พอยท์
                //SendMoney.CreatedBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, SendMoney.TransactionNo, " +
                        "ifnull(Employee.FirstName,'')  || ' ' ||  ifnull(Employee.LastName,'') AS employeeFullName " +
                        "FROM DocumentHistory " +
                        "INNER JOIN SendMoney on DocumentHistory.DocumentNumber = SendMoney.SendMoneyID " +
                        "LEFT JOIN Employee on SendMoney.CreateBy = Employee.EmpID " +
                        "WHERE DocumentHistory.OrganizationCode = ? " +
                        "AND DocumentHistory.DocumentType = ? " +
                        "AND (DocumentHistory.Status IS NULL OR DocumentHistory.Status != 'Received') " +
                        "AND DocumentHistory.PrintOrder  = 1 " +
                        "ORDER BY SendMoney.TransactionNo ASC ";
                break;
        }
        ret = executeQueryList(QUERY_GET_DOCUMENT_HISTORY_BY_TYPE, new String[]{organizationCode, DocumentType}, DocumentHistoryInfo.class);

        return ret;
    }

    public List<DocumentHistoryInfo> getDocumentHistoryByTypeAndEmployeeCode(String organizationCode, String DocumentType,String EmployeeCode) {

        List<DocumentHistoryInfo> ret = null;
        String QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = null;
        switch (DocumentType) {
            case "0": //ใบสัญญา
                //Contract.SaleEmployeeCode = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, Contract.*, DebtorCustomer.*, Contract.CONTNO AS DocumentNo, Contract.isActive, "
                        + "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName "
                        + "FROM DocumentHistory "
                        + "INNER JOIN Contract on DocumentHistory.DocumentNumber = Contract.RefNo AND Contract.SaleEmployeeCode = ? "
                        + "INNER JOIN DebtorCustomer on Contract.CustomerID = DebtorCustomer.CustomerID "
                        + "WHERE DocumentHistory.OrganizationCode = ? "
                        + "AND DocumentHistory.DocumentType = ? "
                        + "AND (DocumentHistory.Status IS NULL OR DocumentHistory.Status != 'Received') "
                        + "AND (DocumentHistory.PrintOrder > 1 OR DocumentHistory.DocumentNumber in (select cc.OldSaleID "
                        + "                                                                           from ChangeContract as cc "
                        + "                                                                           where cc.Status = 'COMPLETED')) "
                        + "ORDER BY Contract.CONTNO ASC ";
                break;
            case "1": //ใบเสร็จ
                //Receipt.CreateBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*,Receipt.*, Contract.*, DebtorCustomer.*, Receipt.ReceiptCode AS DocumentNo, Contract.isActive, "
                        + "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName "
                        + "FROM DocumentHistory "
                        + "INNER JOIN Receipt on DocumentHistory.DocumentNumber = Receipt.ReceiptID AND Receipt.CreateBy = ? "
                        + "INNER JOIN Contract on Receipt.RefNo = Contract.RefNo "
                        + "INNER JOIN DebtorCustomer on Contract.CustomerID = DebtorCustomer.CustomerID "
                        + "WHERE DocumentHistory.OrganizationCode = ? "
                        + "AND DocumentHistory.DocumentType = ? "
                        + "AND (DocumentHistory.Status IS NULL OR DocumentHistory.Status != 'Received') "
                        + "AND (DocumentHistory.PrintOrder > 1 OR Receipt.TotalPayment = 0 ) "
                        + "ORDER BY Receipt.ReceiptCode ASC ";
                break;
            case "2": //ใบเปลี่ยนเครื่อง
                //ChangeProduct.EffectiveBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, "
                        + "ChangeProduct.RefNo, ChangeProduct.ChangeProductPaperID as ChangeProductPaperID, ChangeProduct.ChangeProductPaperID as DocumentNo, Contract.isActive, "
                        + "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName "
                        + "FROM DocumentHistory "
                        + "INNER JOIN ChangeProduct on DocumentHistory.DocumentNumber = ChangeProduct.ChangeProductID AND ChangeProduct.EffectiveBy = ? "
                        + "INNER JOIN Contract on Contract.refNo = ChangeProduct.RefNo "
                        + "INNER JOIN DebtorCustomer on DebtorCustomer.CustomerID = Contract.CustomerID "
                        + "WHERE (DocumentHistory.OrganizationCode = ?) AND (DocumentHistory.DocumentType = ? ) "
                        + "AND DocumentHistory.PrintOrder > 1 "
                        + "ORDER BY ChangeProduct.ChangeProductPaperID ASC ";
                break;
            case "3": //ใบถอดเครื่อง
                //ImpoundProduct.EffectiveBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, "
                        + "ImpoundProduct.RefNo, ImpoundProduct.ImpoundProductPaperID as ImpoundProductPaperID, ImpoundProduct.ImpoundProductPaperID as DocumentNo, Contract.isActive, "
                        + "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName "
                        + "FROM DocumentHistory "
                        + "INNER JOIN ImpoundProduct on DocumentHistory.DocumentNumber = ImpoundProduct.ImpoundProductID AND ImpoundProduct.EffectiveBy = ? "
                        + "INNER JOIN Contract on Contract.refNo = ImpoundProduct.RefNo "
                        + "INNER JOIN DebtorCustomer on DebtorCustomer.CustomerID = Contract.CustomerID "
                        + "WHERE (DocumentHistory.OrganizationCode = ?) AND (DocumentHistory.DocumentType = ?) "
                        + "AND DocumentHistory.PrintOrder > 1 "
                        + "ORDER BY ImpoundProduct.ImpoundProductPaperID ASC ";
                break;
            case "4": //ใบเปลี่ยนสัญญา
                //ChangeContract.EffectiveBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, "
                        + "ChangeContract.NewSaleID as RefNo, ChangeContract.ChangeContractPaperID as ChangeContractPaperID, ChangeContract.ChangeContractPaperID as DocumentNo, Contract.isActive, "
                        + "ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'')AS CustomerFullName "
                        + "FROM DocumentHistory "
                        + "INNER JOIN ChangeContract on DocumentHistory.DocumentNumber = ChangeContract.ChangeContractID AND ChangeContract.EffectiveBy = ? "
                        + "INNER JOIN Contract on Contract.refNo = ChangeContract.NewSaleID "
                        + "INNER JOIN DebtorCustomer on DebtorCustomer.CustomerID = Contract.CustomerID "
                        + "WHERE (DocumentHistory.OrganizationCode = ?) AND (DocumentHistory.DocumentType = ?) "
                        + "AND DocumentHistory.PrintOrder > 1 "
                        + "ORDER BY ChangeContract.ChangeContractPaperID ASC ";
                break;
            case "5": //เอกสารมือ
                //DocumentHistory.CreatedBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.rowid AS No,DocumentHistory.*, " +
                        " ManualDocument.DocumentID AS ManualDocumentID, ManualDocument.ManualDocTypeID AS ManualDocumentTypeID,  " +
                        " ManualDocument.DocumentNumber AS ManualDocumentNumber,  " +
                        " con0.CONTNO AS con0CONTNO, " +
                        " ifnull(dc0.PrefixName,'')  || ' ' ||  ifnull(dc0.CustomerName,'') || ifnull(dc0.CompanyName,'') AS dc0CustomerFullName,  con0.isActive as isActive0, " +
                        " re1.ReceiptCode AS re1ReceiptCode, " +
                        " ifnull(dc1.PrefixName,'')  || ' ' ||  ifnull(dc1.CustomerName,'') || ifnull(dc1.CompanyName,'') AS dc1CustomerFullName, con1.isActive as isActive1" +
                        " FROM DocumentHistory  " +
                        " INNER JOIN ManualDocument on DocumentHistory.DocumentNumber = ManualDocument.DocumentID " +
                        " LEFT JOIN Contract AS con0 on con0.RefNo = ManualDocument.DocumentNumber" +
                        " LEFT JOIN DebtorCustomer AS dc0 on dc0.CustomerID = con0.CustomerID" +
                        " LEFT JOIN Receipt AS re1 on re1.ReceiptID = ManualDocument.DocumentNumber  " +
                        " LEFT JOIN Contract AS con1 on con1.RefNo = re1.RefNo  " +
                        " LEFT JOIN DebtorCustomer AS dc1 on dc1.CustomerID = con1.CustomerID  " +
                        " WHERE DocumentHistory.CreateBy = ? AND DocumentHistory.OrganizationCode = ? " +
                        "       AND DocumentHistory.DocumentType = ? " +
                        "       AND (DocumentHistory.Status IS NULL OR DocumentHistory.Status != 'Received')  " +
                        "       AND (DocumentHistory.PrintOrder > 1 " +
                        "            OR DocumentHistory.DocumentNumber in (select md0.DocumentID " +
                        "                                                  from ManualDocument as md0 " +
                        "                                                  where md0.ManualDocTypeID = 0 " +
                        "                                                        and md0.DocumentNumber in (select cc0.OldSaleID " +
                        "                                                                                   from ChangeContract as cc0 " +
                        "                                                                                   where cc0.Status = 'COMPLETED'))) " +
                        " ORDER BY con0CONTNO OR re1ReceiptCode ASC";
                break;
            case "6": //Slip ธนาคาร
                //SendMoney.CreatedBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, SendMoney.TransactionNo, " +
                        "ifnull(Employee.FirstName,'')  || ' ' ||  ifnull(Employee.LastName,'') AS employeeFullName " +
                        "FROM DocumentHistory " +
                        "INNER JOIN SendMoney on DocumentHistory.DocumentNumber = SendMoney.SendMoneyID AND SendMoney.CreateBy = ? " +
                        "LEFT JOIN Employee on SendMoney.CreateBy = Employee.EmpID " +
                        "WHERE DocumentHistory.OrganizationCode = ? " +
                        "AND DocumentHistory.DocumentType = ? " +
                        "AND (DocumentHistory.Status IS NULL OR DocumentHistory.Status != 'Received') " +
                        "AND DocumentHistory.PrintOrder  = 1 " +
                        "ORDER BY SendMoney.TransactionNo ASC ";
                break;
            case "7": //Slip เพย์พอยท์
                //SendMoney.CreatedBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_BY_TYPE = "SELECT DocumentHistory.*, SendMoney.TransactionNo, " +
                        "ifnull(Employee.FirstName,'')  || ' ' ||  ifnull(Employee.LastName,'') AS employeeFullName " +
                        "FROM DocumentHistory " +
                        "INNER JOIN SendMoney on DocumentHistory.DocumentNumber = SendMoney.SendMoneyID AND SendMoney.CreateBy = ? " +
                        "LEFT JOIN Employee on SendMoney.CreateBy = Employee.EmpID " +
                        "WHERE DocumentHistory.OrganizationCode = ? " +
                        "AND DocumentHistory.DocumentType = ? " +
                        "AND (DocumentHistory.Status IS NULL OR DocumentHistory.Status != 'Received') " +
                        "AND DocumentHistory.PrintOrder  = 1 " +
                        "ORDER BY SendMoney.TransactionNo ASC ";
                break;
        }
        ret = executeQueryList(QUERY_GET_DOCUMENT_HISTORY_BY_TYPE, new String[]{EmployeeCode, organizationCode, DocumentType}, DocumentHistoryInfo.class);

        return ret;
    }

    public void updateDocumentHistory(String strSelected, String strPrintHistoryID, String strDocumentType, boolean statusSelected) {

        String sql = null;

        if (statusSelected == true) {
            sql = "UPDATE DocumentHistory SET Selected = ? WHERE PrintHistoryID = ? AND DocumentType = ?";
        } else {
            sql = "UPDATE DocumentHistory SET Deleted = ? WHERE PrintHistoryID = ? AND DocumentType = ?";
        }
        // beginTransaction();

        executeNonQuery(sql, new String[]{strSelected, strPrintHistoryID, strDocumentType});
    }

    public void addDocumentHistory(DocumentHistoryInfo info) {
        String sql = "insert into DocumentHistory (PrintHistoryID, OrganizationCode, DatePrint, DocumentType, DocumentNumber, "
                + "Selected, Deleted, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, "
                + "PrintOrder, Status, SentDate, SentEmpID, SentSaleCode, SentSubTeamCode, SentTeamCode, ReceivedDate, ReceivedEmpID)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        executeNonQuery(sql, new String[]{info.PrintHistoryID, info.OrganizationCode, valueOf(info.DatePrint), info.DocumentType, info.DocumentNumber,
                valueOf(info.Selected), valueOf(info.Deleted), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy,
                valueOf(info.PrintOrder), info.Status, valueOf(info.SentDate), info.SentEmpID, info.SentSaleCode, info.SentSubTeamCode, info.SentTeamCode, valueOf(info.ReceivedDate), info.ReceivedEmpID});
    }

    public List<DocumentHistoryInfo> getDocumentHistoryGroupByType(String organizationCode, String DocumentType) {
        List<DocumentHistoryInfo> ret = null;
        String QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = null;

        switch (DocumentType) {
            case "0":

                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, c.CONTNO as DocumentNo, "
                        + "ifnull(dc.prefixName,'') || ' ' || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER join Contract as c on dh.DocumentNumber=c.RefNo "
                        + "INNER join DebtorCustomer as dc on c.CustomerID=dc.CustomerID ";
                //+ "where (dh.OrganizationCode = ?) AND (dh.DocumentType = ?) ";

                break;

            case "1":
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, r.ReceiptCode as DocumentNo, "
                        + "ifnull(dc.prefixName,'') || ' ' || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER join Receipt as r on dh.DocumentNumber=r.ReceiptId "
                        + "INNER join Contract as c on r.RefNo=c.RefNo "
                        + "INNER join DebtorCustomer as dc on c.CustomerID=dc.CustomerID ";
                //+ "where (dh.OrganizationCode = ?) AND (dh.DocumentType = ?) ";
                break;

            case "2":
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, cp.ChangeProductPaperID as DocumentNo, "
                        + "ifnull(dc.prefixName,'') || ' ' || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER join ChangeProduct as cp on dh.DocumentNumber=cp.ChangeProductID "
                        + "INNER join Contract as c on cp.RefNo=c.RefNo "
                        + "INNER join DebtorCustomer as dc on c.CustomerID=dc.CustomerID ";
                break;

            case "3":
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, ip.ImpoundProductPaperID as DocumentNo, "
                        + "ifnull(dc.prefixName,'') || ' ' || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER join ImpoundProduct as ip on dh.DocumentNumber=ip.ImpoundProductID "
                        + "INNER join Contract as c on ip.RefNo=c.RefNo "
                        + "INNER join DebtorCustomer as dc on c.CustomerID=dc.CustomerID ";

                break;

            case "4":
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, cc.ChangeContractPaperID as DocumentNo, "
                        + "ifnull(dc.prefixName,'') || ' ' || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER join ChangeContract as cc on dh.DocumentNumber=cc.ChangeContractID "
                        + "INNER join Contract as c on cc.NewSaleID=c.RefNo "
                        + "INNER join DebtorCustomer as dc on c.CustomerID=dc.CustomerID ";
                break;

            case "5":
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, "
                        + "md.DocumentID AS ManualDocumentID, md.ManualDocTypeID AS ManualDocumentTypeID, "
                        + "con0.CONTNO AS con0CONTNO, "
                        + "ifnull(dc0.PrefixName,'')  || ' ' ||  ifnull(dc0.CustomerName,'') || ifnull(dc0.CompanyName,'') AS dc0CustomerFullName, "
                        + "re1.ReceiptCode AS re1ReceiptCode, "
                        + "ifnull(dc1.PrefixName,'')  || ' ' ||  ifnull(dc1.CustomerName,'') || ifnull(dc1.CompanyName,'') AS dc1CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER JOIN ManualDocument as md on dh.DocumentNumber = md.DocumentID "
                        + "LEFT JOIN Contract AS con0 on con0.RefNo = md.DocumentNumber "
                        + "LEFT JOIN DebtorCustomer AS dc0 on dc0.CustomerID = con0.CustomerID "
                        + "LEFT JOIN Receipt AS re1 on re1.ReceiptID = md.DocumentNumber "
                        + "LEFT JOIN Contract AS con1 on con1.RefNo = re1.RefNo "
                        + "LEFT JOIN DebtorCustomer AS dc1 on dc1.CustomerID = con1.CustomerID ";
                break;
            case "6":
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, SendMoney.TransactionNo, "
                        + "ifnull(Employee.FirstName,'')  || ' ' ||  ifnull(Employee.LastName,'') AS employeeFullName, "
                        + "(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER JOIN SendMoney on dh.DocumentNumber = SendMoney.SendMoneyID "
                        + "LEFT JOIN Employee on SendMoney.CreateBy = Employee.EmpID ";
                break;
            case "7":
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, SendMoney.TransactionNo, "
                        + "ifnull(Employee.FirstName,'')  || ' ' ||  ifnull(Employee.LastName,'') AS employeeFullName, "
                        + "(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER JOIN SendMoney on dh.DocumentNumber = SendMoney.SendMoneyID "
                        + "LEFT JOIN Employee on SendMoney.CreateBy = Employee.EmpID ";
                break;

        }

        QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE += "where (((dh.OrganizationCode = ?) AND (dh.DocumentType = ?)) "
                + " AND (dh.Selected = '1') "
                + " AND (dh.Status <> 'Received'))"
                + "order by dh.printHistoryID asc";


        ret = executeQueryList(QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE,
                new String[]{organizationCode, DocumentType, organizationCode, DocumentType}, DocumentHistoryInfo.class);

        return ret;
    }

    public List<DocumentHistoryInfo> getDocumentHistoryGroupByTypeAndEmployeeCode(String organizationCode, String DocumentType, String EmployeeCode) {
        List<DocumentHistoryInfo> ret = null;
        String QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = null;

        switch (DocumentType) {
            case "0": //ใบสัญญา
                //Contract.SaleEmployeeCode = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, c.CONTNO as DocumentNo, "
                        + "ifnull(dc.prefixName,'') || ' ' || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER join Contract as c on dh.DocumentNumber=c.RefNo And c.SaleEmployeeCode = ? "
                        + "INNER join DebtorCustomer as dc on c.CustomerID=dc.CustomerID "
                        + "where (((dh.OrganizationCode = ?) AND (dh.DocumentType = ?)) "
                        + " AND (dh.Selected = '1') "
                        + " AND (dh.Status <> 'Received'))"
                        + "order by dh.printHistoryID asc";

                break;

            case "1": //ใบเสร็จ
                //Receipt.CreateBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, r.ReceiptCode as DocumentNo, "
                        + "ifnull(dc.prefixName,'') || ' ' || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER join Receipt as r on dh.DocumentNumber=r.ReceiptId AND r.CreateBy = ? "
                        + "INNER join Contract as c on r.RefNo=c.RefNo "
                        + "INNER join DebtorCustomer as dc on c.CustomerID=dc.CustomerID "
                        + "where (((dh.OrganizationCode = ?) AND (dh.DocumentType = ?)) "
                        + " AND (dh.Selected = '1') "
                        + " AND (dh.Status <> 'Received'))"
                        + "order by dh.printHistoryID asc";
                break;

            case "2": //ใบเปลี่ยนเครื่อง
                //ChangeProduct.EffectiveBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, cp.ChangeProductPaperID as DocumentNo, "
                        + "ifnull(dc.prefixName,'') || ' ' || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER join ChangeProduct as cp on dh.DocumentNumber=cp.ChangeProductID AND cp.EffectiveBy = ? "
                        + "INNER join Contract as c on cp.RefNo=c.RefNo "
                        + "INNER join DebtorCustomer as dc on c.CustomerID=dc.CustomerID "
                        + "where (((dh.OrganizationCode = ?) AND (dh.DocumentType = ?)) "
                        + " AND (dh.Selected = '1') "
                        + " AND (dh.Status <> 'Received'))"
                        + "order by dh.printHistoryID asc";
                break;

            case "3": //ใบถอดเครื่อง
                //ImpoundProduct.EffectiveBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, ip.ImpoundProductPaperID as DocumentNo, "
                        + "ifnull(dc.prefixName,'') || ' ' || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER join ImpoundProduct as ip on dh.DocumentNumber=ip.ImpoundProductID AND ip.EffectiveBy = ? "
                        + "INNER join Contract as c on ip.RefNo=c.RefNo "
                        + "INNER join DebtorCustomer as dc on c.CustomerID=dc.CustomerID "
                        + "where (((dh.OrganizationCode = ?) AND (dh.DocumentType = ?)) "
                        + " AND (dh.Selected = '1') "
                        + " AND (dh.Status <> 'Received'))"
                        + "order by dh.printHistoryID asc";

                break;

            case "4": //ใบเปลี่ยนสัญญา
                //ChangeContract.EffectiveBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, cc.ChangeContractPaperID as DocumentNo, "
                        + "ifnull(dc.prefixName,'') || ' ' || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'') as CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER join ChangeContract as cc on dh.DocumentNumber=cc.ChangeContractID AND cc.EffectiveBy = ? "
                        + "INNER join Contract as c on cc.NewSaleID=c.RefNo "
                        + "INNER join DebtorCustomer as dc on c.CustomerID=dc.CustomerID "
                        + "where (((dh.OrganizationCode = ?) AND (dh.DocumentType = ?)) "
                        + " AND (dh.Selected = '1') "
                        + " AND (dh.Status <> 'Received'))"
                        + "order by dh.printHistoryID asc";
                break;

            case "5": //เอกสารมือ
                //DocumentHistory.CreatedBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, "
                        + "md.DocumentID AS ManualDocumentID, md.ManualDocTypeID AS ManualDocumentTypeID, "
                        + "con0.CONTNO AS con0CONTNO, "
                        + "ifnull(dc0.PrefixName,'')  || ' ' ||  ifnull(dc0.CustomerName,'') || ifnull(dc0.CompanyName,'') AS dc0CustomerFullName, "
                        + "re1.ReceiptCode AS re1ReceiptCode, "
                        + "ifnull(dc1.PrefixName,'')  || ' ' ||  ifnull(dc1.CustomerName,'') || ifnull(dc1.CompanyName,'') AS dc1CustomerFullName "
                        + ",(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER JOIN ManualDocument as md on dh.DocumentNumber = md.DocumentID "
                        + "LEFT JOIN Contract AS con0 on con0.RefNo = md.DocumentNumber "
                        + "LEFT JOIN DebtorCustomer AS dc0 on dc0.CustomerID = con0.CustomerID "
                        + "LEFT JOIN Receipt AS re1 on re1.ReceiptID = md.DocumentNumber "
                        + "LEFT JOIN Contract AS con1 on con1.RefNo = re1.RefNo "
                        + "LEFT JOIN DebtorCustomer AS dc1 on dc1.CustomerID = con1.CustomerID "
                        + "where ( (dh.CreateBy = ?) And ((dh.OrganizationCode = ?) AND (dh.DocumentType = ?)) "
                        + " AND (dh.Selected = '1') "
                        + " AND (dh.Status <> 'Received'))"
                        + "order by dh.printHistoryID asc";
                break;
            case "6": //Slip ธนาคาร
                //SendMoney.CreatedBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, SendMoney.TransactionNo, "
                        + "ifnull(Employee.FirstName,'')  || ' ' ||  ifnull(Employee.LastName,'') AS employeeFullName, "
                        + "(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER JOIN SendMoney on dh.DocumentNumber = SendMoney.SendMoneyID AND SendMoney.CreateBy = ? "
                        + "LEFT JOIN Employee on SendMoney.CreateBy = Employee.EmpID "
                        + "where (((dh.OrganizationCode = ?) AND (dh.DocumentType = ?)) "
                        + " AND (dh.Selected = '1') "
                        + " AND (dh.Status <> 'Received'))"
                        + "order by dh.printHistoryID asc";
                break;
            case "7": //Slip เพย์พอยท์
                //SendMoney.CreatedBy = EmployeeCode
                QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE = "select dh.DocumentNumber, dh.DocumentType, SendMoney.TransactionNo, "
                        + "ifnull(Employee.FirstName,'')  || ' ' ||  ifnull(Employee.LastName,'') AS employeeFullName, "
                        + "(select count(*) from DocumentHistory as h1 "
                        + "where (((h1.printHistoryID <= dh.printHistoryID) AND ((h1.OrganizationCode = ?) AND (h1.DocumentType = ?))) AND (h1.Selected = '1'))) as No "
                        + "from DocumentHistory as dh "
                        + "INNER JOIN SendMoney on dh.DocumentNumber = SendMoney.SendMoneyID AND SendMoney.CreateBy = ? "
                        + "LEFT JOIN Employee on SendMoney.CreateBy = Employee.EmpID "
                        + "where (((dh.OrganizationCode = ?) AND (dh.DocumentType = ?)) "
                        + " AND (dh.Selected = '1') "
                        + " AND (dh.Status <> 'Received'))"
                        + "order by dh.printHistoryID asc";
                break;

        }

        ret = executeQueryList(QUERY_GET_DOCUMENT_HISTORY_GROUP_BY_TYPE,
                new String[]{organizationCode, DocumentType, EmployeeCode, organizationCode, DocumentType}, DocumentHistoryInfo.class);

        return ret;
    }

    public DocumentHistoryInfo getDocumentHistoryByID(String OrganizationCode, String PrintHistoryID) {
        String sql = "SELECT * FROM DocumentHistory WHERE (OrganizationCode = ?) AND (PrintHistoryID = ?) ";
        return executeQueryObject(sql, new String[]{OrganizationCode, PrintHistoryID}, DocumentHistoryInfo.class);
    }

    public void updateDocumentHistory(DocumentHistoryInfo info) {

        String sql = "UPDATE DocumentHistory SET DatePrint = ?, LastUpdateDate = ?, LastUpdateBy = ?, " +
                "Selected = ?,  Status = ?, SentDate = ?, SentEmpID = ?, SentSaleCode = ?, SentSubTeamCode = ?, SentTeamCode = ? " +
                "where PrintHistoryID = ? ";
        executeNonQuery(sql, new String[]{valueOf(info.DatePrint), valueOf(info.LastUpdateDate), info.LastUpdateBy,
                valueOf(info.Selected), info.Status, valueOf(info.SentDate), info.SentEmpID, info.SentSaleCode, info.SentSubTeamCode, info.SentTeamCode,
                info.PrintHistoryID});

    }

    public void deleteDocumentHistoryAll() {
        String sql = "DELETE FROM DocumentHistory";
        executeNonQuery(sql, null);
    }

    public DocumentHistoryInfo getDocumentHistoryByDocumentNumber(String DocumentNumber, String DocumentType) {
        String sql = "select * from DocumentHistory where DocumentNumber = ? and DocumentType = ?  ORDER BY PrintOrder DESC";
        return executeQueryObject(sql, new String[]{DocumentNumber, DocumentType}, DocumentHistoryInfo.class);
    }


}
