package th.co.thiensurat.data.controller;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ContractInfo;

public class ChangeContractController extends BaseController {

    public enum ChangeContractStatus {
        REQUEST, APPROVED, COMPLETED, REJECT
    }

    public void addChangeContract(ChangeContractInfo info) {
        String sql = " INSERT INTO ChangeContract (ChangeContractID, OrganizationCode, RefNo, OldSaleID, NewSaleID, Status "
                + "         , RequestProblemID, RequestDetail, RequestDate, RequestBy, RequestTeamCode "
                + "         , ApproveDetail, ApprovedDate, ApprovedBy, ResultProblemID, ResultDetail "
                + "         , EffectiveDate, EffectiveBy, ChangeContractPaperID, CreateDate, CreateBy "
                + "         , LastUpdateDate, LastUpdateBy, RequestEmployeeLevelPath, EffectiveEmployeeLevelPath)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, datetime('now'), ?, datetime('now'), ?, ?, ?)";

        executeNonQuery(sql, new String[]{info.ChangeContractID, info.OrganizationCode, info.RefNo, info.OldSaleID, info.NewSaleID, info.Status, info.RequestProblemID,
                info.RequestDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode, info.ApproveDetail, valueOf(info.ApprovedDate),
                info.ApprovedBy, info.ResultProblemID, info.ResultDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.ChangeContractPaperID,
                info.CreateBy, info.LastUpdateBy, info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath});

        TSRController.updateRunningNumber(TSRController.DocumentGenType.ChangeContract, info.ChangeContractPaperID, info.EffectiveBy);
    }

    public void updateChangeContract(ChangeContractInfo info) {

        String sql = "";

        if (info.RequestBy != null && !info.RequestBy.trim().equals("")) {
            sql = " UPDATE ChangeContract "
                    + " SET OrganizationCode = ?, Status = ?, "
                    + "     RequestProblemID = ?, RequestDetail = ?, RequestDate = ?, RequestBy = ?, RequestTeamCode = ?, "
                    + "     ChangeContractPaperID = ?, LastUpdateDate = datetime('now'), LastUpdateBy = ?, RequestEmployeeLevelPath = ? "
                    + " WHERE (ChangeContractID = ?)";
        } else if (info.ApprovedBy != null && !info.ApprovedBy.trim().equals("")) {
            sql = " UPDATE [ChangeContract] "
                    + " SET [Status] = ?, [ApproveDetail] = ?, [ApprovedDate] = datetime('now'), "
                    + "     [ApprovedBy] = ?, [LastUpdateDate] = datetime('now'), [LastUpdateBy] = ? "
                    + " WHERE ([ChangeContractID] = ?)";
            // Status = APPROVED
        } else if (info.EffectiveBy != null && !info.EffectiveBy.trim().equals("")) {
            sql = " UPDATE [ChangeContract] "
                    + " SET [Status] = ?, [ResultProblemID] = ?, [ResultDetail] = ?, "
                    + "     [EffectiveDate] = ?, [EffectiveBy] = ?, [ChangeContractPaperID] = ?, "
                    + "     [LastUpdateDate] = datetime('now'), [LastUpdateBy] = ?, [EffectiveEmployeeLevelPath] = ? "
                    + " WHERE ([ChangeContractID] = ?)";
            // Status = COMPLETED
        }

        if (info.RequestBy != null && !info.RequestBy.trim().equals("")) {
            executeNonQuery(sql, new String[]{info.OrganizationCode, info.Status, info.RequestProblemID, info.RequestDetail, valueOf(info.RequestDate),
                    info.RequestBy, info.RequestTeamCode, info.ChangeContractPaperID, info.LastUpdateBy, info.RequestEmployeeLevelPath, info.ChangeContractID});
        } else if (info.ApprovedBy != null && !info.ApprovedBy.trim().equals("")) {
            executeNonQuery(sql, new String[]{info.Status, info.ApproveDetail, info.ApprovedBy, info.LastUpdateBy, info.ChangeContractID});
        } else if (info.EffectiveBy != null && !info.EffectiveBy.trim().equals("")) {
            executeNonQuery(sql, new String[]{info.Status, info.ResultProblemID, info.ResultDetail, valueOf(info.EffectiveDate), info.EffectiveBy,
                    info.ChangeContractPaperID, info.LastUpdateBy, info.EffectiveEmployeeLevelPath, info.ChangeContractID});
        }

        TSRController.updateRunningNumber(TSRController.DocumentGenType.ChangeContract, info.ChangeContractPaperID, info.EffectiveBy);
    }

    public void updateChangeContractByCOMPLETED(ChangeContractInfo info) {

        String sql = "";
//        sql = "UPDATE [ChangeContract] SET [Status] = ?, [ResultProblemID] = ?, [ResultDetail] = ?, "
//                + "[EffectiveDate] = datetime('now'), [EffectiveBy] = ?, [ChangeContractPaperID] = ?, "
//                + "[LastUpdateDate] = datetime('now'), [LastUpdateBy] = ? " + "WHERE [ChangeContractID] = ?";
        sql = "UPDATE [ChangeContract] "
                + " SET [Status] = ?, [ResultProblemID] = ?, [ResultDetail] = ?, "
                + "         [EffectiveDate] = ?, [EffectiveBy] = ?, [ChangeContractPaperID] = ?, "
                + "         [LastUpdateDate] = ?, [LastUpdateBy] = ?, EffectiveEmployeeLevelPath = ? "
                + " WHERE ([ChangeContractID] = ?)";
        // Status = COMPLETED
        executeNonQuery(sql, new String[]{info.Status, info.ResultProblemID, info.ResultDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.ChangeContractPaperID
                , valueOf(info.LastUpdateDate), info.LastUpdateBy, info.EffectiveEmployeeLevelPath, info.ChangeContractID});

        TSRController.updateRunningNumber(TSRController.DocumentGenType.ChangeContract, info.ChangeContractPaperID, info.EffectiveBy);
    }

    public ChangeContractInfo getChangeContractByID(String OrganizationCode, String ChangeContractID) {
        String sql = "SELECT * FROM ChangeContract WHERE (ChangeContract.OrganizationCode = ?) AND (ChangeContract.ChangeContractID = ?)";
        return executeQueryObject(sql, new String[]{OrganizationCode, ChangeContractID}, ChangeContractInfo.class);
    }

    public void deleteChangeContractByID(String OrganizationCode, String ChangeContractID) {
        executeNonQuery("DELETE FROM ChangeContract WHERE (OrganizationCode = ?) AND (ChangeContractID = ?)", new String[]{OrganizationCode, ChangeContractID});
    }

    public ContractInfo getContractByChangeContractID(String OrganizationCode, String ChangeContractID) {
        String sql = " SELECT  Contract.EFFDATE, Contract.RefNo, Contract.CONTNO,"
                + "         Contract.ProductSerialNumber, Contract.TotalPrice,"
                + "         ChangeContract.EffectiveDate,"
                + "         (DebtorCustomer.PrefixName || DebtorCustomer.CustomerName) AS CustomerFullName,"
                + "         DebtorCustomer.IDCard,"
                + "         Product.ProductName,"
                + "         Problem.ProblemName "
                + " FROM ChangeContract "
                + "         LEFT JOIN Contract ON (Contract.OrganizationCode = ChangeContract.OrganizationCode) AND (Contract.RefNo = ChangeContract.NewSaleID) "
                + "         INNER JOIN DebtorCustomer ON (DebtorCustomer.OrganizationCode = Contract.OrganizationCode) AND (DebtorCustomer.CustomerID = Contract.CustomerID) "
                + "         INNER JOIN Product ON (Product.OrganizationCode = Contract.OrganizationCode) AND (Product.ProductID = Contract.ProductID) "
                + "         INNER JOIN Problem ON (Problem.OrganizationCode = ChangeContract.OrganizationCode) AND (Problem.ProblemID = ChangeContract.RequestProblemID) "
                + " WHERE (ChangeContract.OrganizationCode = ?) AND (ChangeContract.ChangeContractID = ?)";

        return executeQueryObject(sql, new String[]{OrganizationCode, ChangeContractID}, ContractInfo.class);
    }

    /*public List<ChangeContractInfo> getContractAndChangeContractForAvailableBySaleTeamCode(String organizationCode, String saleTeamCode, String ContractStatus) {
        final String sql = "SELECT distinct Cont.InstallDate, Cont.ProductSerialNumber, Cont.RefNo, Cont.CONTNO, Cont.OrganizationCode "
                + "             ,Cont.CustomerID "
                + " 			,Cust.PrefixName || ifnull(Cust.CustomerName, Cust.CompanyName) AS CustomerFullName, Cust.CompanyName, Cust.IDCard "
                + " 			,Prod.ProductName, ifnull(Sale.FirstName,'') || '  ' || ifnull(Sale.LastName,'') AS SaleEmployeeName "
                + " 			,ContST.StatusName "
                + "             ,ChangeCont.ChangeContractID, ChangeCont.Status AS Status, ChangeCont.OldSaleID, ChangeCont.NewSaleID, ChangeCont.RequestProblemID, ChangeCont.RequestDetail "
                + "             ,ChangeCont.RequestDate, ChangeCont.RequestBy, ChangeCont.RequestTeamCode"
                + "             ,ChangeCont.ApprovedDate, ChangeCont.ApprovedBy, ChangeCont.ApproveDetail"
                + "             ,ChangeCont.CreateDate, ChangeCont.CreateBy, ChangeCont.RequestEmployeeLevelPath, ChangeCont.EffectiveEmployeeLevelPath"
                + " FROM            Contract AS Cont INNER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         SalePaymentPeriod spp ON spp.RefNo = Cont.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = 0 INNER JOIN "
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         EmployeeDetailHistory AS TSEmp ON TSEmp.TreeHistoryID = Cont.SaleEmployeeLevelPath and Cont.OrganizationCode = TSEmp.OrganizationCode AND Cont.SaleTeamCode = TSEmp.TeamCode AND"
                + "                         Cont.SaleEmployeeCode = TSEmp.EmployeeCode INNER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode AND ContST.StatusName = '" + ContractInfo.ContractStatusName.COMPLETED.toString() + "'"
                + "                 Left Outer Join ChangeContract AS ChangeCont on ChangeCont.OldSaleID=Cont.RefNo and ChangeCont.Status<>'COMPLETED'"
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) "
                + "             AND (Cont.STATUS IN (" + ContractStatus + "))"
                + " ORDER BY Cont.InstallDate ASC";
        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode}, ChangeContractInfo.class);
    }

    public List<ChangeContractInfo> getContractAndChangeContractForAvailableBySaleTeamCodeAndSubTeamCode(String organizationCode, String saleTeamCode, String saleSubTeamCode, String ContractStatus) {
        final String sql = "SELECT distinct Cont.InstallDate, Cont.ProductSerialNumber, Cont.RefNo, Cont.CONTNO, Cont.OrganizationCode "
                + "             ,Cont.CustomerID "
                + " 			,Cust.PrefixName || ifnull(Cust.CustomerName, Cust.CompanyName) AS CustomerFullName, Cust.CompanyName, Cust.IDCard "
                + " 			,Prod.ProductName, ifnull(Sale.FirstName,'') || '  ' || ifnull(Sale.LastName,'') AS SaleEmployeeName "
                + " 			,ContST.StatusName "
                + "             ,ChangeCont.ChangeContractID, ChangeCont.Status AS Status, ChangeCont.OldSaleID, ChangeCont.NewSaleID, ChangeCont.RequestProblemID, ChangeCont.RequestDetail "
                + "             ,ChangeCont.RequestDate, ChangeCont.RequestBy, ChangeCont.RequestTeamCode"
                + "             ,ChangeCont.ApprovedDate, ChangeCont.ApprovedBy, ChangeCont.ApproveDetail"
                + "             ,ChangeCont.CreateDate, ChangeCont.CreateBy, ChangeCont.RequestEmployeeLevelPath, ChangeCont.EffectiveEmployeeLevelPath"
                + " FROM            Contract AS Cont INNER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         SalePaymentPeriod spp ON spp.RefNo = Cont.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = 0 INNER JOIN "
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         EmployeeDetailHistory AS TSEmp ON TSEmp.TreeHistoryID = Cont.SaleEmployeeLevelPath and Cont.OrganizationCode = TSEmp.OrganizationCode AND Cont.SaleTeamCode = TSEmp.TeamCode AND"
                + "                         Cont.SaleEmployeeCode = TSEmp.EmployeeCode INNER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode AND ContST.StatusName = '" + ContractInfo.ContractStatusName.COMPLETED.toString() + "'"
                + "                 Left Outer Join ChangeContract AS ChangeCont on ChangeCont.OldSaleID=Cont.RefNo and ChangeCont.Status<>'COMPLETED'"
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) " +
                "   AND (Cont.STATUS IN (" + ContractStatus + ")) and TSEmp.SubTeamCode = ? "
                + " ORDER BY Cont.InstallDate ASC";
        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, saleSubTeamCode}, ChangeContractInfo.class);
    }

    public List<ChangeContractInfo> getContractAndChangeContractForAvailableForCredit(String organizationCode, String EmployeeID, String ContractStatus) {
        final String sql = "SELECT distinct Cont.InstallDate, Cont.ProductSerialNumber, Cont.RefNo, Cont.CONTNO, Cont.OrganizationCode "
                + "             ,Cont.CustomerID "
                + " 			,Cust.PrefixName || ifnull(Cust.CustomerName, Cust.CompanyName) AS CustomerFullName, Cust.CompanyName, Cust.IDCard "
                + " 			,Prod.ProductName "
                + " 			,ContST.StatusName "
                + "             ,ChangeCont.ChangeContractID, ChangeCont.Status AS Status, ChangeCont.OldSaleID, ChangeCont.NewSaleID, ChangeCont.RequestProblemID, ChangeCont.RequestDetail "
                + "             ,ChangeCont.RequestDate, ChangeCont.RequestBy, ChangeCont.RequestTeamCode"
                + "             ,ChangeCont.ApprovedDate, ChangeCont.ApprovedBy, ChangeCont.ApproveDetail"
                + "             ,ChangeCont.CreateDate, ChangeCont.CreateBy, ChangeCont.RequestEmployeeLevelPath, ChangeCont.EffectiveEmployeeLevelPath"
                + " FROM            Contract AS Cont INNER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         SalePaymentPeriod spp ON spp.RefNo = Cont.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = 1 INNER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode AND ContST.StatusName = '" + ContractInfo.ContractStatusName.COMPLETED.toString() + "'"
                + "                 Left Join ChangeContract AS ChangeCont on ChangeCont.OldSaleID=Cont.RefNo AND ChangeCont.Status NOT IN ('COMPLETED', 'REJECT') "
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) "
                + "         AND (Cont.STATUS IN (" + ContractStatus + ")) "
                + "         AND (ChangeCont.RequestBy = ? OR ChangeCont.RequestBy IS NULL)"
                + " ORDER BY Cont.InstallDate ASC";
        return executeQueryList(sql, new String[]{organizationCode, EmployeeID}, ChangeContractInfo.class);
    }*/

    public List<ChangeContractInfo> getContractAndChangeContractForAvailableBySaleTeamCodeAndSearchText(String organizationCode, String saleTeamCode, String SearchText, String RefNo) {
        /*final String sql = "SELECT distinct Cont.InstallDate, Cont.ProductSerialNumber, Cont.RefNo, Cont.CONTNO, Cont.OrganizationCode "
                + "             ,Cont.CustomerID "
                + " 			,Cust.PrefixName || ifnull(Cust.CustomerName, Cust.CompanyName) AS CustomerFullName, Cust.CompanyName, Cust.IDCard "
                + " 			,Prod.ProductName, ifnull(Sale.FirstName,'') || '  ' || ifnull(Sale.LastName,'') AS SaleEmployeeName "
                + " 			,ContST.StatusName "
                + "             ,ChangeCont.ChangeContractID, ChangeCont.Status AS Status, ChangeCont.OldSaleID, ChangeCont.NewSaleID, ChangeCont.RequestProblemID, ChangeCont.RequestDetail "
                + "             ,ChangeCont.RequestDate, ChangeCont.RequestBy, ChangeCont.RequestTeamCode"
                + "             ,ChangeCont.ApprovedDate, ChangeCont.ApprovedBy, ChangeCont.ApproveDetail"
                + "             ,ChangeCont.CreateDate, ChangeCont.CreateBy, ChangeCont.RequestEmployeeLevelPath, ChangeCont.EffectiveEmployeeLevelPath"
                + " FROM            Contract AS Cont INNER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         SalePaymentPeriod spp ON spp.RefNo = Cont.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = 0 INNER JOIN "
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         EmployeeDetailHistory AS TSEmp ON TSEmp.TreeHistoryID = Cont.SaleEmployeeLevelPath and Cont.OrganizationCode = TSEmp.OrganizationCode AND Cont.SaleTeamCode = TSEmp.TeamCode AND"
                + "                         Cont.SaleEmployeeCode = TSEmp.EmployeeCode INNER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode AND ContST.StatusName = '" + ContractInfo.ContractStatusName.COMPLETED.toString() + "'"
                + "                 Left Outer Join ChangeContract AS ChangeCont on ChangeCont.OldSaleID=Cont.RefNo and ChangeCont.Status<>'COMPLETED'"
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) "
                + "         AND (Cont.STATUS IN (" + ContractStatus + ")) "
                + "         AND ifnull(Cont.ProductSerialNumber,'') || ifnull(Cust.CustomerName,'') || ifnull(Cust.CompanyName,'')  || ifnull(Cont.CONTNO,'') like ? "
                + " ORDER BY Cont.InstallDate ASC";
        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, SearchText}, ChangeContractInfo.class);*/


        ArrayList<String> args = new ArrayList<>();
        String sql = "select distinct c.RefNo, c.OrganizationCode, c.CONTNO, c.ProductSerialNumber, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName, " +
                "            c.IsMigrate " +
                "     from Contract as c " +
                "          INNER JOIN DebtorCustomer as dc ON dc.CustomerID = c.CustomerID and dc.OrganizationCode = c.OrganizationCode " +
                "          INNER JOIN SalePaymentPeriod spp ON spp.RefNo = c.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = 0 " +
                "       where c.isActive = 1 and c.STATUS = ? and c.OrganizationCode = ? and (c.StatusCode=(select StatusCode from ContractStatus where (StatusName = 'COMPLETED'))) " +
                "             and (c.SaleTeamCode = ? )  ";

        args.add(ContractInfo.ContractStatus.NORMAL.toString());
        args.add(organizationCode);
        args.add(saleTeamCode);

        if(SearchText != null){
            sql += "           and (IFNULL(dc.CustomerName, dc.CompanyName) || IFNULL(dc.IDCard, '') || IFNULL(dc.AuthorizedIDCard, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ? ) ";
            args.add("%" + SearchText + "%");
        }

        if(RefNo != null){
            sql += "           and c.RefNo = ? ";
            args.add(RefNo);
        }

        sql += "       ORDER BY c.InstallDate ASC ";
        return executeQueryList(sql, args.toArray(new String[args.size()]), ChangeContractInfo.class);

    }

    public List<ChangeContractInfo> getContractAndChangeContractForAvailableBySaleTeamCodeAndSubTeamCodeAndSearchText(String organizationCode, String saleSubTeamCode, String SearchText, String RefNo) {
        /*final String sql = "SELECT distinct Cont.InstallDate, Cont.ProductSerialNumber, Cont.RefNo, Cont.CONTNO, Cont.OrganizationCode "
                + "         , Cont.CustomerID "
                + " 		, Cust.PrefixName || ifnull(Cust.CustomerName, Cust.CompanyName) AS CustomerFullName, Cust.CompanyName, Cust.IDCard "
                + " 		, Prod.ProductName, ifnull(Sale.FirstName,'') || '  ' || ifnull(Sale.LastName,'') AS SaleEmployeeName "
                + " 		, ContST.StatusName "
                + "         , ChangeCont.ChangeContractID, ChangeCont.Status AS Status, ChangeCont.OldSaleID, ChangeCont.NewSaleID, ChangeCont.RequestProblemID, ChangeCont.RequestDetail "
                + "         , ChangeCont.RequestDate, ChangeCont.RequestBy, ChangeCont.RequestTeamCode"
                + "         , ChangeCont.ApprovedDate, ChangeCont.ApprovedBy, ChangeCont.ApproveDetail"
                + "         , ChangeCont.CreateDate, ChangeCont.CreateBy, ChangeCont.RequestEmployeeLevelPath, ChangeCont.EffectiveEmployeeLevelPath"
                + " FROM            Contract AS Cont INNER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         SalePaymentPeriod spp ON spp.RefNo = Cont.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = 0 INNER JOIN "
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         EmployeeDetailHistory AS TSEmp ON TSEmp.TreeHistoryID = Cont.SaleEmployeeLevelPath and Cont.OrganizationCode = TSEmp.OrganizationCode AND Cont.SaleTeamCode = TSEmp.TeamCode AND"
                + "                         Cont.SaleEmployeeCode = TSEmp.EmployeeCode INNER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode AND ContST.StatusName = '" + ContractInfo.ContractStatusName.COMPLETED.toString() + "'"
                + "                 Left Outer Join ChangeContract AS ChangeCont on ChangeCont.OldSaleID=Cont.RefNo and ChangeCont.Status<>'COMPLETED'"
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) "
                + "         AND (Cont.STATUS IN (" + ContractStatus + "))"
                + "         AND TSEmp.SubTeamCode = ? "
                + "         AND ifnull(Cont.ProductSerialNumber,'') || ifnull(Cust.CustomerName,'') || ifnull(Cust.CompanyName,'')  || ifnull(Cont.CONTNO,'') like ? "
                + " ORDER BY Cont.InstallDate ASC";
        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, saleSubTeamCode, SearchText}, ChangeContractInfo.class);*/


        ArrayList<String> args = new ArrayList<>();
        String sql = "select distinct c.RefNo, c.OrganizationCode, c.CONTNO, c.ProductSerialNumber, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName, " +
                "            c.IsMigrate " +
                "            , cc.ChangeContractID, cc.Status AS Status, cc.OldSaleID, cc.NewSaleID, cc.RequestProblemID, cc.RequestDetail " +
                "            , cc.RequestDate, cc.RequestBy, cc.RequestTeamCode " +
                "            , cc.ApprovedDate, cc.ApprovedBy, cc.ApproveDetail " +
                "            , cc.CreateDate, cc.CreateBy, cc.RequestEmployeeLevelPath, cc.EffectiveEmployeeLevelPath " +
                "     from Contract as c " +
                "          INNER JOIN DebtorCustomer as dc ON dc.CustomerID = c.CustomerID and dc.OrganizationCode = c.OrganizationCode " +
                "          INNER JOIN SalePaymentPeriod spp ON spp.RefNo = c.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = 0 " +
                "          INNER JOIN EmployeeDetailHistory AS TSEmp ON TSEmp.TreeHistoryID = c.SaleEmployeeLevelPath and TSEmp.OrganizationCode = c.OrganizationCode AND TSEmp.TeamCode = c.SaleTeamCode " +
                "                     and TSEmp.EmployeeCode = c.SaleEmployeeCode " +
                "          LEFT JOIN ChangeContract as cc ON cc.RefNo = c.RefNo and cc.OrganizationCode = c.OrganizationCode and cc.Status in (?, ?) " +
                "       where c.isActive = 1 and c.STATUS = ? and c.OrganizationCode = ? and (c.StatusCode=(select StatusCode from ContractStatus where (StatusName = 'COMPLETED'))) " +
                "             and (TSEmp.SubTeamCode = ? ) ";

        args.add(ChangeContractStatus.REQUEST.toString());
        args.add(ChangeContractStatus.APPROVED.toString());
        args.add(ContractInfo.ContractStatus.NORMAL.toString());
        args.add(organizationCode);
        args.add(saleSubTeamCode);

        if(SearchText != null){
            sql += "           and (IFNULL(dc.CustomerName, dc.CompanyName) || IFNULL(dc.IDCard, '') || IFNULL(dc.AuthorizedIDCard, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ? ) ";
            args.add("%" + SearchText + "%");
        }

        if(RefNo != null){
            sql += "           and c.RefNo = ? ";
            args.add(RefNo);
        }

        sql += "       ORDER BY IFNULL(cc.RequestDate, '') DESC, c.InstallDate ASC ";
        return executeQueryList(sql, args.toArray(new String[args.size()]), ChangeContractInfo.class);
    }

    public List<ChangeContractInfo> getContractAndChangeContractForAvailableForCreditAndSearchText(String organizationCode, String SearchText, String EmployeeID, String RefNo) {
        /*final String sql = "SELECT distinct Cont.InstallDate, Cont.ProductSerialNumber, Cont.RefNo, Cont.CONTNO, Cont.OrganizationCode "
                + "         , Cont.CustomerID "
                + " 		, Cust.PrefixName || ifnull(Cust.CustomerName, Cust.CompanyName) AS CustomerFullName, Cust.CompanyName, Cust.IDCard "
                + " 		, Prod.ProductName "
                + " 		, ContST.StatusName "
                + "         , ChangeCont.ChangeContractID, ChangeCont.Status AS Status, ChangeCont.OldSaleID, ChangeCont.NewSaleID, ChangeCont.RequestProblemID, ChangeCont.RequestDetail "
                + "         , ChangeCont.RequestDate, ChangeCont.RequestBy, ChangeCont.RequestTeamCode"
                + "         , ChangeCont.ApprovedDate, ChangeCont.ApprovedBy, ChangeCont.ApproveDetail"
                + "         , ChangeCont.CreateDate, ChangeCont.CreateBy, ChangeCont.RequestEmployeeLevelPath, ChangeCont.EffectiveEmployeeLevelPath"
                + " FROM            Contract AS Cont INNER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         SalePaymentPeriod spp ON spp.RefNo = Cont.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = 1 INNER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode AND ContST.StatusName = '" + ContractInfo.ContractStatusName.COMPLETED.toString() + "'"
                + "                 Left Join ChangeContract AS ChangeCont on ChangeCont.OldSaleID=Cont.RefNo AND ChangeCont.Status NOT IN ('COMPLETED', 'REJECT') "
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) " +
                "           AND (Cont.STATUS IN (" + ContractStatus + "))"
                + "         AND (ChangeCont.RequestBy = ? OR ChangeCont.RequestBy IS NULL)"
                + "         AND ifnull(Cont.ProductSerialNumber,'') || ifnull(Cust.CustomerName,'') || ifnull(Cust.CompanyName,'')  || ifnull(Cont.CONTNO,'') like ? "
                + " ORDER BY Cont.InstallDate ASC";
        return executeQueryList(sql, new String[]{organizationCode, EmployeeID, SearchText}, ChangeContractInfo.class);*/

        ArrayList<String> args = new ArrayList<>();
        String sql = "select distinct c.RefNo, c.OrganizationCode, c.CONTNO, c.ProductSerialNumber, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName, " +
                "            c.IsMigrate " +
                "            , cc.ChangeContractID, cc.Status AS Status, cc.OldSaleID, cc.NewSaleID, cc.RequestProblemID, cc.RequestDetail " +
                "            , cc.RequestDate, cc.RequestBy, cc.RequestTeamCode " +
                "            , cc.ApprovedDate, cc.ApprovedBy, cc.ApproveDetail " +
                "            , cc.CreateDate, cc.CreateBy, cc.RequestEmployeeLevelPath, cc.EffectiveEmployeeLevelPath " +
                "     from Contract as c " +
                "          INNER JOIN DebtorCustomer as dc ON dc.CustomerID = c.CustomerID and dc.OrganizationCode = c.OrganizationCode " +
                "          INNER JOIN SalePaymentPeriod spp ON spp.RefNo = c.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = 1 " +
                "          LEFT JOIN Assign as a ON a.TaskType in (?, ?) and a.RefNo = c.RefNo and a.OrganizationCode = c.OrganizationCode " +
                "          LEFT JOIN ChangeContract as cc ON cc.RefNo = c.RefNo and cc.OrganizationCode = c.OrganizationCode " +
                "       where c.isActive = 1 and c.STATUS = ? and c.OrganizationCode = ? and (c.StatusCode=(select StatusCode from ContractStatus where (StatusName = 'COMPLETED'))) " +
                "             and (a.AssigneeEmpID = ? or (cc.RequestBy = ? and cc.Status in (?, ?)))  ";

        args.add(AssignController.AssignTaskType.SalePaymentPeriod.toString());
        args.add(AssignController.AssignTaskType.SaleAudit.toString());
        args.add(ContractInfo.ContractStatus.NORMAL.toString());
        args.add(organizationCode);
        args.add(EmployeeID);
        args.add(EmployeeID);
        args.add(ChangeContractStatus.REQUEST.toString());
        args.add(ChangeContractStatus.APPROVED.toString());

        if(SearchText != null){
            sql += "           and (IFNULL(dc.CustomerName, dc.CompanyName) || IFNULL(dc.IDCard, '') || IFNULL(dc.AuthorizedIDCard, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ? ) ";
            args.add("%" + SearchText + "%");
        }

        if(RefNo != null){
            sql += "           and c.RefNo = ? ";
            args.add(RefNo);
        }

        sql += "       ORDER BY IFNULL(cc.RequestDate, '') DESC, c.InstallDate ASC ";
        return executeQueryList(sql, args.toArray(new String[args.size()]), ChangeContractInfo.class);
    }


    public List<ChangeContractInfo> getChangeContractByNewSaleIDReturnChangeContract(String OrganizationCode, String NewSaleID) {
        String sql = "SELECT ChangeContract.*, p.ProblemName "
                + "         , e.FirstName || ' ' || IFNULL (e.LastName,'') as EffectiveByEmployeeName"
                + "         , ed.SaleCode as EffectiveBySaleCode"
                + "         , IFNULL(upperemp.FirstName,'') || ' ' || IFNULL(upperemp.LastName,'') as EffectiveByUpperEmployeeName"
                + "         , t.Name as EffectiveBySaleTeamName"
                + " FROM ChangeContract "
                + "         INNER JOIN Problem p on p.ProblemID = ChangeContract.RequestProblemID"
                + "         INNER JOIN Employee as e on ChangeContract.OrganizationCode = e.OrganizationCode and ChangeContract.EffectiveBy = e.EmpID "
                + "         LEFT OUTER JOIN EmployeeDetailHistory as ed on ed.TreeHistoryID = ChangeContract.EffectiveEmployeeLevelPath and ed.OrganizationCode = e.OrganizationCode and ed.EmployeeCode = e.EmpID AND IFNULL(ed.SaleCode, '') <> ''"
                + "         LEFT OUTER JOIN Team as t on ed.TeamCode = t.Code"
                + "         LEFT OUTER JOIN Employee as upperemp on ed.OrganizationCode = upperemp.OrganizationCode and ed.ParentEmployeeCode = upperemp.EmpID"
                + " WHERE (ChangeContract.OrganizationCode = ?) AND (ChangeContract.NewSaleID = ?) ";

        return executeQueryList(sql, new String[]{OrganizationCode, NewSaleID}, ChangeContractInfo.class);
    }

//    public ChangeContractInfo getChangeContractByChangeContractID(String OrganizationCode, String ChangeContractID) {
//        String sql = "SELECT * FROM ChangeContract "
//                + "WHERE (ChangeContract.OrganizationCode = ?) AND (ChangeContract.ChangeContractID = ?)";
//
//        return executeQueryObject(sql, new String[]{OrganizationCode, ChangeContractID}, ChangeContractInfo.class);
//    }

    public void deleteChangeContractAll() {
        executeNonQuery("delete from ChangeContract", null);
    }


    public ChangeContractInfo getLastChangeContract(String employeeID, String yearMonth) {
        final String sql = "SELECT * FROM ChangeContract "
                + " WHERE (EffectiveBy = ?) AND (substr(ChangeContractPaperID,length(ChangeContractPaperID)-6,4) = ?) "
                + " ORDER BY substr(ChangeContractPaperID,length(ChangeContractPaperID)-2,3) DESC";
        List<ChangeContractInfo> tmpChangeContractList = executeQueryList(sql, new String[]{employeeID, yearMonth}, ChangeContractInfo.class);
        if (tmpChangeContractList == null)
            return null;
        else
            return tmpChangeContractList.get(0);
    }

}
