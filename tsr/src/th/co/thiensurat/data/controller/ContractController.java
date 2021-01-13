package th.co.thiensurat.data.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHApplication;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.GCMProdStkAndContractInfo;

import static th.co.thiensurat.fragments.sales.preorder_setting.SaleMainFragment_preorder_setting.select_page_s;

public class ContractController extends BaseController {

    public ContractInfo getContractByRefNo(String organizationCode, String refNO) {
        ContractInfo ret = null;
        String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/
                + "     , dc.CustomerType, dc.PrefixName || ifnull(dc.CustomerName,'') as CustomerFullName, ifnull(dc.CompanyName, '') as CompanyName, dc.IDCard "
                + "     , p.ProductCode, p.ProductName, p.ProductModel "
                + "     , e.EmpID, e.FirstName || ' ' || ifnull (e.LastName,'') as SaleEmployeeName, t.Name as SaleTeamName, ed.TeamCode "
                + "     , upperemp.EmpID AS upperEmployeeID, IFNULL(upperemp.FirstName,'') || ' ' || IFNULL(upperemp.LastName,'') AS upperEmployeeName"
                + "     , cs.StatusName,  ifnull(spp.PaymentAmount,0) as PaymentAmount"
                + " FROM Contract AS c "
                + "     INNER JOIN DebtorCustomer AS dc ON (c.OrganizationCode=dc.OrganizationCode) AND (c.CustomerID=dc.CustomerID) "
                + "     INNER JOIN Product AS p ON (c.OrganizationCode=p.OrganizationCode) AND (c.ProductID=p.ProductID) "
                + "     INNER JOIN Employee AS e ON (c.OrganizationCode=e.OrganizationCode) AND (c.SaleEmployeeCode=e.EmpID) "
                + "     LEFT OUTER JOIN EmployeeDetailHistory AS ed ON (ed.TreeHistoryID = c.SaleEmployeeLevelPath) "
                + "             AND (ed.PositionCode = 'Sale') AND (ed.OrganizationCode=e.OrganizationCode) AND  (ed.EmployeeCode=e.EmpID) "
                + "             AND (ed.TeamCode = c.SaleTeamCode) AND (ed.SaleCode = c.SaleCode) "
                + "     LEFT OUTER JOIN Team AS t ON (ed.TeamCode=t.Code) "
                + "     LEFT OUTER JOIN Employee AS upperemp ON (ed.OrganizationCode=upperemp.OrganizationCode) AND (ed.TeamHeadCode=upperemp.EmpID) "
                + "     LEFT OUTER JOIN ContractStatus AS cs ON (c.StatusCode=cs.StatusCode) "
                + "     LEFT OUTER JOIN SalePaymentPeriod AS spp ON (c.RefNo=spp.RefNo) AND (spp.PaymentPeriodNumber='1')"
                + " WHERE (c.isActive='1') AND (c.OrganizationCode=?) AND (c.RefNo=?) ";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractByRefNo_tsrl(String organizationCode, String refNO) {
        ContractInfo ret = null;
        String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                        + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                        + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                        + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                        + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                        + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                        + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/
                        + "     , dc.CustomerType, dc.PrefixName || ifnull(dc.CustomerName,'') as CustomerFullName, ifnull(dc.CompanyName, '') as CompanyName, dc.IDCard "
                        + "     , p.ProductCode, p.ProductName, p.ProductModel "
                        + "     , e.EmpID, e.FirstName || ' ' || ifnull (e.LastName,'') as SaleEmployeeName, t.Name as SaleTeamName, ed.TeamCode "
                        + "     , upperemp.EmpID AS upperEmployeeID, IFNULL(upperemp.FirstName,'') || ' ' || IFNULL(upperemp.LastName,'') AS upperEmployeeName"
                        + "     , cs.StatusName,  ifnull(spp.PaymentAmount,0) as PaymentAmount"
                        + " FROM Contract AS c "
                        + "     INNER JOIN DebtorCustomer AS dc ON (c.OrganizationCode=dc.OrganizationCode) AND (c.CustomerID=dc.CustomerID) "
                        + "     INNER JOIN Product AS p ON (c.OrganizationCode=p.OrganizationCode) AND (c.ProductID=p.ProductID) "
                        + "     INNER JOIN Employee AS e ON (c.OrganizationCode=e.OrganizationCode) AND (c.SaleEmployeeCode=e.EmpID) "
                        + "     LEFT OUTER JOIN EmployeeDetailHistory AS ed ON (ed.TreeHistoryID = c.SaleEmployeeLevelPath) "
                        + "             AND (ed.PositionCode = 'Sale') AND (ed.OrganizationCode=e.OrganizationCode) AND  (ed.EmployeeCode=e.EmpID) "
                        + "             AND (ed.TeamCode = c.SaleTeamCode) AND (ed.SaleCode = c.SaleCode) "
                        + "     LEFT OUTER JOIN Team AS t ON (ed.TeamCode=t.Code) "
                        + "     LEFT OUTER JOIN Employee AS upperemp ON (ed.OrganizationCode=upperemp.OrganizationCode) AND (ed.TeamHeadCode=upperemp.EmpID) "
                        + "     LEFT OUTER JOIN ContractStatus AS cs ON (c.StatusCode=cs.StatusCode) "
                        + "     LEFT OUTER JOIN SalePaymentPeriod AS spp ON (c.RefNo=spp.RefNo) AND (spp.PaymentPeriodNumber='1')"
                        + " WHERE (c.isActive='1') AND (c.OrganizationCode=?) AND (c.RefNo=?) ";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractByRefNoNotCheckActive(String organizationCode, String refNO) {
        ContractInfo ret = null;
        String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "     , dc.CustomerType, dc.PrefixName || ifnull(dc.CustomerName,'') as CustomerFullName, ifnull(dc.CompanyName, '') as CompanyName, dc.IDCard, p.ProductCode, p.ProductName, p.ProductModel, "
                + "     e.EmpID, e.FirstName || ' ' || ifnull (e.LastName,'') as SaleEmployeeName, t.Name as SaleTeamName, ed.TeamCode, "
                + "     upperemp.EmpID as upperEmployeeID, upperemp.FirstName || ' ' || upperemp.LastName as upperEmployeeName, cs.StatusName,  ifnull(spp.PaymentAmount,0) as PaymentAmount"
                + " FROM Contract as c inner join DebtorCustomer as dc on c.OrganizationCode=dc.OrganizationCode and c.CustomerID=dc.CustomerID inner join"
                + "     Product as p on c.OrganizationCode=p.OrganizationCode and c.ProductID=p.ProductID inner join"
                + "     Employee as e on c.OrganizationCode=e.OrganizationCode and c.SaleEmployeeCode=e.EmpID left outer join"
                + "     EmployeeDetailHistory as ed on ed.PositionCode = 'Sale' AND ed.TreeHistoryID = c.SaleEmployeeLevelPath and ed.OrganizationCode=e.OrganizationCode and  ed.EmployeeCode=e.EmpID " +
                "           AND ed.TeamCode = c.SaleTeamCode AND ed.SaleCode = c.SaleCode left outer join"
                + "     Team as t on ed.TeamCode=t.Code left outer join "
                + "     Employee as upperemp on ed.OrganizationCode=upperemp.OrganizationCode and ed.TeamHeadCode=upperemp.EmpID left outer join "
                + "     ContractStatus as cs on c.StatusCode=cs.StatusCode left outer join"
                + "     SalePaymentPeriod as spp on c.RefNo=spp.RefNo and spp.PaymentPeriodNumber='1'"
                + " WHERE (c.OrganizationCode=?) AND (c.RefNo=?)";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractByRefNoNotCheckActive_tsrl(String organizationCode, String refNO) {
        ContractInfo ret = null;
        String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                        + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                        + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                        + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                        + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                        + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                        + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + "     , dc.CustomerType, dc.PrefixName || ifnull(dc.CustomerName,'') as CustomerFullName, ifnull(dc.CompanyName, '') as CompanyName, dc.IDCard, p.ProductCode, p.ProductName, p.ProductModel, "
                        + "     e.EmpID, e.FirstName || ' ' || ifnull (e.LastName,'') as SaleEmployeeName, t.Name as SaleTeamName, ed.TeamCode, "
                        + "     upperemp.EmpID as upperEmployeeID, upperemp.FirstName || ' ' || upperemp.LastName as upperEmployeeName, cs.StatusName,  ifnull(spp.PaymentAmount,0) as PaymentAmount"
                        + " FROM Contract as c inner join DebtorCustomer as dc on c.OrganizationCode=dc.OrganizationCode and c.CustomerID=dc.CustomerID inner join"
                        + "     Product as p on c.OrganizationCode=p.OrganizationCode and c.ProductID=p.ProductID inner join"
                        + "     Employee as e on c.OrganizationCode=e.OrganizationCode and c.SaleEmployeeCode=e.EmpID left outer join"
                        + "     EmployeeDetailHistory as ed on ed.PositionCode = 'Sale' AND ed.TreeHistoryID = c.SaleEmployeeLevelPath and ed.OrganizationCode=e.OrganizationCode and  ed.EmployeeCode=e.EmpID " +
                        "           AND ed.TeamCode = c.SaleTeamCode AND ed.SaleCode = c.SaleCode left outer join"
                        + "     Team as t on ed.TeamCode=t.Code left outer join "
                        + "     Employee as upperemp on ed.OrganizationCode=upperemp.OrganizationCode and ed.TeamHeadCode=upperemp.EmpID left outer join "
                        + "     ContractStatus as cs on c.StatusCode=cs.StatusCode left outer join"
                        + "     SalePaymentPeriod as spp on c.RefNo=spp.RefNo and spp.PaymentPeriodNumber='1'"
                        + " WHERE (c.OrganizationCode=?) AND (c.RefNo=?)";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractByRefNoForCredit(String organizationCode, String refNO) {
        ContractInfo ret = null;
        String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "     , dc.PrefixName || ifnull(dc.CustomerName,'') as CustomerFullName, ifnull(dc.CompanyName, '') as CompanyName, dc.IDCard, p.ProductCode, p.ProductName, p.ProductModel, "
                + "     cs.StatusName,  ifnull(spp.PaymentAmount,0) as PaymentAmount"
                + " FROM Contract as c inner join DebtorCustomer as dc on c.OrganizationCode=dc.OrganizationCode and c.CustomerID=dc.CustomerID inner join"
                + "     Product as p on c.OrganizationCode=p.OrganizationCode and c.ProductID=p.ProductID left outer join "
                + "     ContractStatus as cs on c.StatusCode=cs.StatusCode left outer join"
                + "     SalePaymentPeriod as spp on c.RefNo=spp.RefNo and spp.PaymentPeriodNumber='1'"
                + " WHERE (c.isActive='1') AND (c.OrganizationCode=?) AND (c.RefNo=?)";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractByRefNoForSendDocuments(String organizationCode, String refNO) {
        ContractInfo ret = null;
        String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "     , dc.CustomerType, dc.PrefixName || ifnull(dc.CustomerName,'') as CustomerFullName, ifnull(dc.CompanyName, '') as CompanyName, dc.IDCard, p.ProductCode, p.ProductName, p.ProductModel, "
                + "     e.EmpID, e.FirstName || ' ' || ifnull (e.LastName,'') as SaleEmployeeName, t.Name as SaleTeamName, ed.TeamCode, "
                + "     upperemp.EmpID as upperEmployeeID, upperemp.FirstName || ' ' || upperemp.LastName as upperEmployeeName, cs.StatusName,  ifnull(spp.PaymentAmount,0) as PaymentAmount"
                + " FROM Contract as c inner join DebtorCustomer as dc on c.OrganizationCode=dc.OrganizationCode and c.CustomerID=dc.CustomerID inner join"
                + "     Product as p on c.OrganizationCode=p.OrganizationCode and c.ProductID=p.ProductID inner join"
                + "     Employee as e on c.OrganizationCode=e.OrganizationCode and c.SaleEmployeeCode=e.EmpID left outer join"
                + "     EmployeeDetailHistory as ed on ed.PositionCode = 'Sale' AND ed.TreeHistoryID = c.SaleEmployeeLevelPath and ed.OrganizationCode=e.OrganizationCode and  ed.EmployeeCode=e.EmpID AND ed.TeamCode = c.SaleTeamCode AND ed.SaleCode = c.SaleCode left outer join"
                + "     Team as t on ed.TeamCode=t.Code left outer join "
                + "     Employee as upperemp on ed.OrganizationCode=upperemp.OrganizationCode and ed.TeamHeadCode=upperemp.EmpID left outer join "
                + "     ContractStatus as cs on c.StatusCode=cs.StatusCode left outer join"
                + "     SalePaymentPeriod as spp on c.RefNo=spp.RefNo and spp.PaymentPeriodNumber='1'"
                + " WHERE (c.OrganizationCode = ?) AND (c.RefNo = ?)";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractByRefNoForSendDocuments_tsrl(String organizationCode, String refNO) {
        ContractInfo ret = null;
        String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                        + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                        + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                        + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                        + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                        + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                        + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + "     , dc.CustomerType, dc.PrefixName || ifnull(dc.CustomerName,'') as CustomerFullName, ifnull(dc.CompanyName, '') as CompanyName, dc.IDCard, p.ProductCode, p.ProductName, p.ProductModel, "
                        + "     e.EmpID, e.FirstName || ' ' || ifnull (e.LastName,'') as SaleEmployeeName, t.Name as SaleTeamName, ed.TeamCode, "
                        + "     upperemp.EmpID as upperEmployeeID, upperemp.FirstName || ' ' || upperemp.LastName as upperEmployeeName, cs.StatusName,  ifnull(spp.PaymentAmount,0) as PaymentAmount"
                        + " FROM Contract as c inner join DebtorCustomer as dc on c.OrganizationCode=dc.OrganizationCode and c.CustomerID=dc.CustomerID inner join"
                        + "     Product as p on c.OrganizationCode=p.OrganizationCode and c.ProductID=p.ProductID inner join"
                        + "     Employee as e on c.OrganizationCode=e.OrganizationCode and c.SaleEmployeeCode=e.EmpID left outer join"
                        + "     EmployeeDetailHistory as ed on ed.PositionCode = 'Sale' AND ed.TreeHistoryID = c.SaleEmployeeLevelPath and ed.OrganizationCode=e.OrganizationCode and  ed.EmployeeCode=e.EmpID AND ed.TeamCode = c.SaleTeamCode AND ed.SaleCode = c.SaleCode left outer join"
                        + "     Team as t on ed.TeamCode=t.Code left outer join "
                        + "     Employee as upperemp on ed.OrganizationCode=upperemp.OrganizationCode and ed.TeamHeadCode=upperemp.EmpID left outer join "
                        + "     ContractStatus as cs on c.StatusCode=cs.StatusCode left outer join"
                        + "     SalePaymentPeriod as spp on c.RefNo=spp.RefNo and spp.PaymentPeriodNumber='1'"
                        + " WHERE (c.OrganizationCode = ?) AND (c.RefNo = ?)";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO}, ContractInfo.class);
        return ret;
    }
    public ContractInfo getContractByRefNoForPrintChangeContract(String organizationCode, String refNO) {
        ContractInfo ret = null;
        String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "     , dc.PrefixName || ifnull(dc.CustomerName,'') as CustomerFullName, dc.CompanyName, dc.IDCard, p.ProductCode, p.ProductName, p.ProductModel, "
                + "     e.EmpID, e.FirstName || ' ' || ifnull (e.LastName,'') as SaleEmployeeName, t.Name as SaleTeamName, ed.TeamCode, "
                + "     upperemp.EmpID as upperEmployeeID, upperemp.FirstName || ' ' || upperemp.LastName as upperEmployeeName, cs.StatusName,  ifnull(spp.PaymentAmount,0) as PaymentAmount"
                + " FROM Contract as c inner join DebtorCustomer as dc on c.OrganizationCode=dc.OrganizationCode and c.CustomerID=dc.CustomerID inner join"
                + "     Product as p on c.OrganizationCode=p.OrganizationCode and c.ProductID=p.ProductID inner join"
                + "     Employee as e on c.OrganizationCode=e.OrganizationCode and c.SaleEmployeeCode=e.EmpID left outer join"
                + "     EmployeeDetailHistory as ed on ed.TreeHistoryID = c.SaleEmployeeLevelPath and ed.OrganizationCode=e.OrganizationCode and  ed.EmployeeCode=e.EmpID left outer join"
                + "     Team as t on ed.TeamCode=t.Code left outer join "
                + "     Employee as upperemp on ed.OrganizationCode=upperemp.OrganizationCode and ed.ParentEmployeeCode=upperemp.EmpID left outer join "
                + "     ContractStatus as cs on c.StatusCode=cs.StatusCode left outer join"
                + "     SalePaymentPeriod as spp on c.RefNo=spp.RefNo and spp.PaymentPeriodNumber='1'"
                + " WHERE (ed.PositionCode = 'Sale') AND ((c.OrganizationCode=?) AND (c.RefNo=?))";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractByRefNoForChangeContract(String organizationCode, String refNO) {
        ContractInfo ret = null;
        String sql = 
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " FROM Contract AS c "
                + " WHERE (c.OrganizationCode = ?) AND (c.RefNo = ?) ";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractByRefNoForManualDocument(String organizationCode, String refNO) {
        ContractInfo ret = null;
        String sql = 
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "     , DebtorCustomer.* " 
                + "     , ifnull(DebtorCustomer.PrefixName,'')  || ' ' ||  ifnull(DebtorCustomer.CustomerName,'') || ifnull(DebtorCustomer.CompanyName,'') AS CustomerFullName " 
                + " FROM Contract AS c" 
                + "     INNER JOIN DebtorCustomer on c.CustomerID = DebtorCustomer.CustomerID " 
                + " WHERE (c.organizationCode = ?) AND (c.RefNo = ?) ";
        ret = executeQueryObject(sql, new String[]{organizationCode, refNO}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractByRefNoByPaymentPeriodNumber(String organizationCode, String refNO, String paymentPeriodNumber) {
        ContractInfo ret = null;
        String sql = 
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT Cont.*,"
                " SELECT Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "     , Cust.IDCard, Prod.ProductName, Cust.PrefixName || Cust.CustomerName AS CustomerFullName, Cust.CompanyName, Team.Name AS SaleTeamName, "
                + "     upperemp.EmpID as upperEmployeeID, upperemp.FirstName || ' ' || upperemp.LastName as upperEmployeeName, ContST.StatusName,"
                + "     spp.PaymentAmount, spp.NetAmount - ifnull(sumperiodpayamt.SummaryPaymentAmount,0) as OutstandingAmount, ifnull(sumperiodpayamt.SummaryPaymentAmount,0) as SummaryPaymentAmount "
                + " FROM Contract AS Cont " +
                "       INNER JOIN DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID " +
                "       INNER JOIN Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID" +
                "       INNER JOIN Employee AS emp ON Cont.OrganizationCode = emp.OrganizationCode AND Cont.SaleEmployeeCode = emp.EmpID " +
                "       LEFT OUTER JOIN EmployeeDetailHistory AS ed ON ed.TreeHistoryID = Cont.SaleEmployeeLevelPath and Cont.OrganizationCode = ed.OrganizationCode AND Cont.SaleTeamCode = ed.TeamCode " +
                "           AND Cont.SaleEmployeeCode = ed.EmployeeCode AND Cont.SaleCode = ed.SaleCode" +
                "       LEFT OUTER JOIN Team ON Cont.OrganizationCode = Team.OrganizationCode AND Cont.SaleTeamCode = Team.Code " +
                "       LEFT OUTER JOIN Employee as upperemp on ed.OrganizationCode=upperemp.OrganizationCode and ed.ParentEmployeeCode=upperemp.EmpID " +
                "       left outer join "
                + "         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode	inner join "
                + "     SalePaymentPeriod as spp on Cont.RefNo=spp.RefNo left outer join	"
                + "     (select SalePaymentPeriodID, ifnull(sum(Amount),0) as SummaryPaymentAmount" + " from SalePaymentPeriodPayment"
                + "         group by SalePaymentPeriodID) as sumperiodpayamt" + "	on spp.SalePaymentPeriodID=sumperiodpayamt.SalePaymentPeriodID "
                + " WHERE (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.RefNo = ?) AND (spp.PaymentPeriodNumber=?)   ";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO, paymentPeriodNumber}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractByRefNoByPaymentPeriodNumberNotTeam(String organizationCode, String refNO, String paymentPeriodNumber) {
        ContractInfo ret = null;
        String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT Cont.*,"
                " SELECT Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "     , Cust.IDCard, Prod.ProductName, Cust.PrefixName || Cust.CustomerName AS CustomerFullName, Cust.CompanyName,  "
                + "     spp.PaymentAmount, spp.NetAmount - ifnull(sumperiodpayamt.SummaryPaymentAmount,0) as OutstandingAmount, ifnull(sumperiodpayamt.SummaryPaymentAmount,0)"
                + "     as SummaryPaymentAmount "
                + " FROM Contract AS Cont INNER JOIN DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID "
                + "     INNER JOIN Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID	inner join SalePaymentPeriod as spp on Cont.RefNo=spp.RefNo left outer join	"
                + "         (select SalePaymentPeriodID, ifnull(sum(Amount),0) as SummaryPaymentAmount from SalePaymentPeriodPayment group by SalePaymentPeriodID) as sumperiodpayamt	"
                + "             on spp.SalePaymentPeriodID=sumperiodpayamt.SalePaymentPeriodID "
                + " WHERE (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.RefNo = ?) AND (spp.PaymentPeriodNumber=?)";

        ret = executeQueryObject(sql, new String[]{organizationCode, refNO, paymentPeriodNumber}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContractBySerialNo(String organizationCode, String productSerialNumber) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT Cont.*,"
                " SELECT Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " 			, Cust.PrefixName || Cust.CustomerName AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                + " 			 upperEmp.FirstName || '  ' || upperEmp.LastName AS upperEmployeeName"
                + " FROM            Contract AS Cont INNER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         Team ON Cont.OrganizationCode = Team.OrganizationCode AND Cont.SaleTeamCode = Team.Code LEFT OUTER JOIN"
                + "                         EmployeeDetailHistory AS  TSEmp ON TSEmp.TreeHistoryID = Cont.SaleEmployeeLevelPath and Cont.OrganizationCode = TSEmp.OrganizationCode AND Cont.SaleTeamCode = TSEmp.TeamCode AND"
                + "                         Cont.SaleEmployeeCode = TSEmp.EmployeeCode LEFT OUTER JOIN"
                + "                         Employee AS upperEmp ON TSEmp.OrganizationCode = upperEmp.OrganizationCode AND"
                + "                         TSEmp.ParentEmployeeCode = upperEmp.EmpID LEFT OUTER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.ProductSerialNumber = ?)";
        return executeQueryObject(sql, new String[]{organizationCode, productSerialNumber}, ContractInfo.class);
    }

    public List<ContractInfo> getContractBySaleTeamCode(String organizationCode, String saleTeamCode) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " 			, Cust.PrefixName || Cust.CustomerName AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                + " 			 ContST.StatusName"
                + " FROM            Contract AS Cont INNER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         EmployeeDetailHistory AS  TSEmp ON TSEmp.TreeHistoryID = Cont.SaleEmployeeLevelPath and Cont.OrganizationCode = TSEmp.OrganizationCode AND Cont.SaleTeamCode = TSEmp.TeamCode AND"
                + "                         Cont.SaleEmployeeCode = TSEmp.EmployeeCode LEFT OUTER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (Cont.STATUS = '"
                + ContractInfo.ContractStatus.NORMAL.toString() + "') ";
        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode}, ContractInfo.class);
    }

    public List<ContractInfo> getContractStatusFinish(String EMPID,String organizationCode, String saleTeamCode, String StatusName,String DD) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                + " 			 ContST.StatusName"
                + " FROM            Contract AS Cont INNER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                + " WHERE (Cont.ProductSerialNumber != '-')  AND (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')  AND " +
                        "(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(Cont.ProductSerialNumber, '0', ''),'1', ''),'2', ''),'3', ''),'4', ''),'5', ''),'6', ''),'7', ''),'8', ''),'9', ''))  " +
                        " NOT IN" +
                        DD
                + " ORDER BY Cont.CONTNO ASC";

        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName}, ContractInfo.class);
    }


    public List<ContractInfo> getContractStatusFinish_ContractInfo_preorder(String organizationCode, String saleTeamCode, String StatusName) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        + " FROM            Contract AS Cont INNER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                        + " WHERE   (Cont.ProductSerialNumber = '-') AND  (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                        + " ORDER BY Cont.CONTNO DESC";

        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName}, ContractInfo.class);
    }


    public List<ContractInfo> getContractStatusFinish_ContractInfo_preorder_setting2(String organizationCode, String saleTeamCode, String StatusName,String DD) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        + " FROM            Contract AS Cont INNER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                      //  + " WHERE   (substr(Cont.ProductSerialNumber,1, 2) ='WG' or substr(Cont.ProductSerialNumber,1, 2) ='AZ' or substr(Cont.ProductSerialNumber,1, 2) ='WI' or substr(Cont.ProductSerialNumber,1, 2) ='WS' or substr(Cont.ProductSerialNumber,1, 2) ='WK' or substr(Cont.ProductSerialNumber,1, 2) ='WJ' or substr(Cont.ProductSerialNumber,1, 3) ='SFK' or substr(Cont.ProductSerialNumber,1, 3) ='SFL' or substr(Cont.ProductSerialNumber,1, 2) ='WO')  AND  (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                        + " WHERE    (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T') AND " +
                        "(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(Cont.ProductSerialNumber, '0', ''),'1', ''),'2', ''),'3', ''),'4', ''),'5', ''),'6', ''),'7', ''),'8', ''),'9', ''))  " +
                        " IN" +
                        DD
                        + " ORDER BY Cont.CONTNO ASC";

        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName}, ContractInfo.class);
    }

    public List<ContractInfo> getContractStatusFinish_ContractInfo_preorder_SETTING() {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                      //  + " FROM            Contract AS Cont INNER JOIN"
                        + " FROM            Contract AS Cont LEFT JOIN"

                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                       // + " WHERE   (Cont.ProductSerialNumber = '-') AND  (Cont.isActive = 1) AND (Cont.OrganizationCode = ?)  AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                        + " WHERE   (Cont.ProductSerialNumber = '-') AND (Cont.StatusCode='13') "
                       // + " WHERE   (Cont.ProductSerialNumber = '-') "


                        + " ORDER BY Cont.CONTNO ASC";

        return executeQueryList(sql, new String[]{}, ContractInfo.class);
    }


    public List<ContractInfo> getContractStatusFinish_ContractInfo_preorder_SETTING_S(String S) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        //  + " FROM            Contract AS Cont INNER JOIN"
                        + " FROM            Contract AS Cont LEFT JOIN"

                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                        // + " WHERE   (Cont.ProductSerialNumber = '-') AND  (Cont.isActive = 1) AND (Cont.OrganizationCode = ?)  AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                        + " WHERE   (Cont.ProductSerialNumber = '-') AND (Cont.StatusCode='13')  AND (Cont.CONTNO LIKE  ? OR CustomerFullName LIKE  ?) "
                        // + " WHERE   (Cont.ProductSerialNumber = '-') "


                        + " ORDER BY Cont.CONTNO ASC";

        return executeQueryList(sql, new String[]{"%" + S + "%","%" + S + "%"}, ContractInfo.class);
    }
    public List<ContractInfo> getContractStatusFinishForCRD(String EMPID,String organizationCode, String saleTeamCode, String StatusName, String EmployeeID,String DD) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        + " FROM            Contract AS Cont INNER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                        + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T') AND (Cont.CreateBy = ?) AND" +
                        "(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(Cont.ProductSerialNumber, '0', ''),'1', ''),'2', ''),'3', ''),'4', ''),'5', ''),'6', ''),'7', ''),'8', ''),'9', ''))  " +
                        " NOT IN" +
                        DD
                        + " ORDER BY Cont.CONTNO ASC";

        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName, EmployeeID}, ContractInfo.class);
    }

    public List<ContractInfo> getContractStatusFinishForCRD_SETTING(String EMPID,String organizationCode, String saleTeamCode, String StatusName, String EmployeeID,String DD) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        + " FROM            Contract AS Cont INNER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                        + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T') AND (Cont.CreateBy = ?) AND" +
                        "(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(Cont.ProductSerialNumber, '0', ''),'1', ''),'2', ''),'3', ''),'4', ''),'5', ''),'6', ''),'7', ''),'8', ''),'9', ''))  " +
                        " IN" +
                        DD
                        + " ORDER BY Cont.CONTNO ASC";

        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName, EmployeeID}, ContractInfo.class);
    }

    public List<ContractInfo> getContractStatusFinishForCRD_ContractInfo_preorder(String organizationCode, String saleTeamCode, String StatusName, String EmployeeID) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        + " FROM            Contract AS Cont INNER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                        + " WHERE    (Cont.ProductSerialNumber = '-') AND (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T') AND (Cont.CreateBy = ?)"
                        + " ORDER BY Cont.CONTNO ASC";

        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName, EmployeeID}, ContractInfo.class);
    }


    public List<ContractInfo> getContractStatusFinishBySearch(String organizationCode, String saleTeamCode, String StatusName, String strSearch) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName, IFNULL(Cust.CustomerName,'') || IFNULL(Cust.CompanyName,'') AS CustomerFullName2"
                        + " FROM            Contract AS Cont INNER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                        + " WHERE   (Cont.ProductSerialNumber != '-')  AND  (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                        //+ "            AND (Cont.CONTNO LIKE  ? OR Cont.ProductSerialNumber LIKE  ? OR CustomerFullName2 LIKE  ?)"
                        + "            AND (Cont.CONTNO LIKE  ? OR CustomerFullName2 LIKE  ?)"
                        + " ORDER BY Cont.CONTNO ASC";

        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName, strSearch, strSearch}, ContractInfo.class);
    }

    //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป (Comment ตัวนี้ไปใช้ getContractStatusFinishForCreditBySearch แทน)
//
//    public List<ContractInfo> getContractStatusFinishForCredit(String organizationCode, String StatusName) {
//        final String sql =
//                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
//                //"SELECT distinct Cont.*,"
//                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
//                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
//                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
//                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
//                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
//                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
//                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
//                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/
//
//                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
//                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
//                        + " 			 ContST.StatusName"
//                        + " FROM            Contract AS Cont INNER JOIN"
//                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
//                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
//                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
//                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
//                        + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
//                        + " ORDER BY Cont.CONTNO ASC";
//
//        return executeQueryList(sql, new String[]{organizationCode, StatusName}, ContractInfo.class);
//    }

    public List<ContractInfo> getContractStatusFinishForCreditBySearch(String EMPID,String organizationCode, String StatusName, String strSearch,String DD) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/
                + " 	,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                + " 	 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                + " 	 ContST.StatusName, IFNULL(Cust.CustomerName,'') || IFNULL(Cust.CompanyName,'') AS CustomerFullName2"
                + " 	 , MaxPay.MaxPaymentDate"   //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป
                + " FROM  Contract AS Cont "
                + "      INNER JOIN DebtorCustomer AS Cust ON (Cont.OrganizationCode = Cust.OrganizationCode) AND (Cont.CustomerID = Cust.CustomerID) "
                + "      INNER JOIN Product AS Prod ON (Cont.OrganizationCode = Prod.OrganizationCode) AND (Cont.ProductID = Prod.ProductID) "
                + "      INNER JOIN Employee AS Sale ON (Cont.OrganizationCode = Sale.OrganizationCode) AND (Cont.SaleEmployeeCode = Sale.EmpID) "
                + "      INNER JOIN ContractStatus AS ContST ON (Cont.StatusCode = ContST.StatusCode) "
                + "      LEFT OUTER JOIN (SELECT RefNo, MAX(PAYDATE) AS MaxPaymentDate FROM Payment GROUP BY RefNo) AS MaxPay ON (Cont.RefNo = MaxPay.RefNo) "  //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป
                + " WHERE  (Cont.CreateBy =?)  AND  (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                //+ "            AND (Cont.CONTNO LIKE  ? OR Cont.ProductSerialNumber LIKE  ? OR CustomerFullName2 LIKE  ?)"
                + "      AND (Cont.CONTNO LIKE  ? OR CustomerFullName2 LIKE  ?) AND" +
                        "(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(Cont.ProductSerialNumber, '0', ''),'1', ''),'2', ''),'3', ''),'4', ''),'5', ''),'6', ''),'7', ''),'8', ''),'9', ''))  " +
                        " NOT IN" +
                        DD
                + " ORDER BY MaxPay.MaxPaymentDate DESC, Cont.CONTNO ASC";  //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป

        return executeQueryList(sql, new String[]{EMPID,organizationCode, StatusName, strSearch, strSearch}, ContractInfo.class);
    }

    public List<ContractInfo> getContractStatusFinishForCreditBySearch_SETTING(String EMPID,String organizationCode, String StatusName, String strSearch,String DD) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/
                        + " 	,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 	 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 	 ContST.StatusName, IFNULL(Cust.CustomerName,'') || IFNULL(Cust.CompanyName,'') AS CustomerFullName2"
                        + " 	 , MaxPay.MaxPaymentDate"   //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป
                        + " FROM  Contract AS Cont "
                        + "      INNER JOIN DebtorCustomer AS Cust ON (Cont.OrganizationCode = Cust.OrganizationCode) AND (Cont.CustomerID = Cust.CustomerID) "
                        + "      INNER JOIN Product AS Prod ON (Cont.OrganizationCode = Prod.OrganizationCode) AND (Cont.ProductID = Prod.ProductID) "
                        + "      INNER JOIN Employee AS Sale ON (Cont.OrganizationCode = Sale.OrganizationCode) AND (Cont.SaleEmployeeCode = Sale.EmpID) "
                        + "      INNER JOIN ContractStatus AS ContST ON (Cont.StatusCode = ContST.StatusCode) "
                        + "      LEFT OUTER JOIN (SELECT RefNo, MAX(PAYDATE) AS MaxPaymentDate FROM Payment GROUP BY RefNo) AS MaxPay ON (Cont.RefNo = MaxPay.RefNo) "  //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป
                        + " WHERE  (Cont.CreateBy =?)  AND  (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                        //+ "            AND (Cont.CONTNO LIKE  ? OR Cont.ProductSerialNumber LIKE  ? OR CustomerFullName2 LIKE  ?)"
                        + "      AND (Cont.CONTNO LIKE  ? OR CustomerFullName2 LIKE  ?) AND" +
                        "(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(Cont.ProductSerialNumber, '0', ''),'1', ''),'2', ''),'3', ''),'4', ''),'5', ''),'6', ''),'7', ''),'8', ''),'9', ''))  " +
                        " IN" +
                        DD
                        + " ORDER BY MaxPay.MaxPaymentDate DESC, Cont.CONTNO ASC";  //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป

        return executeQueryList(sql, new String[]{EMPID,organizationCode, StatusName, strSearch, strSearch}, ContractInfo.class);
    }

    public List<ContractInfo> getContractStatusFinishForCreditBySearch_all(String organizationCode, String StatusName, String strSearch) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/
                        + " 	,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 	 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 	 ContST.StatusName, IFNULL(Cust.CustomerName,'') || IFNULL(Cust.CompanyName,'') AS CustomerFullName2"
                        + " 	 , MaxPay.MaxPaymentDate"   //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป
                        + " FROM  Contract AS Cont "
                        + "      INNER JOIN DebtorCustomer AS Cust ON (Cont.OrganizationCode = Cust.OrganizationCode) AND (Cont.CustomerID = Cust.CustomerID) "
                        + "      INNER JOIN Product AS Prod ON (Cont.OrganizationCode = Prod.OrganizationCode) AND (Cont.ProductID = Prod.ProductID) "
                        + "      INNER JOIN Employee AS Sale ON (Cont.OrganizationCode = Sale.OrganizationCode) AND (Cont.SaleEmployeeCode = Sale.EmpID) "
                        + "      INNER JOIN ContractStatus AS ContST ON (Cont.StatusCode = ContST.StatusCode) "
                        + "      LEFT OUTER JOIN (SELECT RefNo, MAX(PAYDATE) AS MaxPaymentDate FROM Payment GROUP BY RefNo) AS MaxPay ON (Cont.RefNo = MaxPay.RefNo) "  //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป
                        + " WHERE (Cont.ProductSerialNumber != '-') AND  (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                        //+ "            AND (Cont.CONTNO LIKE  ? OR Cont.ProductSerialNumber LIKE  ? OR CustomerFullName2 LIKE  ?)"
                        + "      AND (Cont.CONTNO LIKE  ? OR CustomerFullName2 LIKE  ?)"
                        + " ORDER BY MaxPay.MaxPaymentDate DESC, Cont.CONTNO ASC";  //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป

        return executeQueryList(sql, new String[]{organizationCode, StatusName, strSearch, strSearch}, ContractInfo.class);
    }

    public List<ContractInfo> getContractStatusFinishForCreditBySearch_ContractInfo_preorder(String organizationCode, String StatusName, String strSearch) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/
                        + " 	,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 	 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 	 ContST.StatusName, IFNULL(Cust.CustomerName,'') || IFNULL(Cust.CompanyName,'') AS CustomerFullName2"
                        + " 	 , MaxPay.MaxPaymentDate"   //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป
                        + " FROM  Contract AS Cont "
                        + "      INNER JOIN DebtorCustomer AS Cust ON (Cont.OrganizationCode = Cust.OrganizationCode) AND (Cont.CustomerID = Cust.CustomerID) "
                        + "      INNER JOIN Product AS Prod ON (Cont.OrganizationCode = Prod.OrganizationCode) AND (Cont.ProductID = Prod.ProductID) "
                        + "      INNER JOIN Employee AS Sale ON (Cont.OrganizationCode = Sale.OrganizationCode) AND (Cont.SaleEmployeeCode = Sale.EmpID) "
                        + "      INNER JOIN ContractStatus AS ContST ON (Cont.StatusCode = ContST.StatusCode) "
                        + "      LEFT OUTER JOIN (SELECT RefNo, MAX(PAYDATE) AS MaxPaymentDate FROM Payment GROUP BY RefNo) AS MaxPay ON (Cont.RefNo = MaxPay.RefNo) "  //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป
                        + " WHERE (Cont.ProductSerialNumber = '-') AND (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                        //+ "            AND (Cont.CONTNO LIKE  ? OR Cont.ProductSerialNumber LIKE  ? OR CustomerFullName2 LIKE  ?)"
                        + "      AND (Cont.CONTNO LIKE  ? OR CustomerFullName2 LIKE  ?)"
                        + " ORDER BY MaxPay.MaxPaymentDate DESC, Cont.CONTNO ASC";  //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป

        return executeQueryList(sql, new String[]{organizationCode, StatusName, strSearch, strSearch}, ContractInfo.class);
    }

    public List<ContractInfo> getContractStatusFinishForCreditBySearch_ContractInfo_preorder_CREDIT(String organizationCode, String StatusName, String strSearch,String EMPID) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/
                        + " 	,Cust.PrefixName || IFNULL(Cust.CustomerName,'') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 	 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 	 ContST.StatusName, IFNULL(Cust.CustomerName,'') || IFNULL(Cust.CompanyName,'') AS CustomerFullName2"
                        + " 	 , MaxPay.MaxPaymentDate"   //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป
                        + " FROM  Contract AS Cont "
                        + "      INNER JOIN DebtorCustomer AS Cust ON (Cont.OrganizationCode = Cust.OrganizationCode) AND (Cont.CustomerID = Cust.CustomerID) "
                        + "      INNER JOIN Product AS Prod ON (Cont.OrganizationCode = Prod.OrganizationCode) AND (Cont.ProductID = Prod.ProductID) "
                        + "      INNER JOIN Employee AS Sale ON (Cont.OrganizationCode = Sale.OrganizationCode) AND (Cont.SaleEmployeeCode = Sale.EmpID) "
                        + "      INNER JOIN ContractStatus AS ContST ON (Cont.StatusCode = ContST.StatusCode) "
                        + "      LEFT OUTER JOIN (SELECT RefNo, MAX(PAYDATE) AS MaxPaymentDate FROM Payment GROUP BY RefNo) AS MaxPay ON (Cont.RefNo = MaxPay.RefNo) "  //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป
                        + " WHERE (Cont.ProductSerialNumber = '-') AND (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                        //+ "            AND (Cont.CONTNO LIKE  ? OR Cont.ProductSerialNumber LIKE  ? OR CustomerFullName2 LIKE  ?)"
                        + "      AND (Cont.CONTNO LIKE  ? OR CustomerFullName2 LIKE  ?)"
                        + "      AND (Cont.CreateBy =  ?)"
                        + " ORDER BY MaxPay.MaxPaymentDate DESC, Cont.CONTNO ASC";  //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป

        return executeQueryList(sql, new String[]{organizationCode, StatusName, strSearch, strSearch,EMPID}, ContractInfo.class);
    }
    // private static final String QUERY_CONTRACT_GET_BY_STATUS_UNFINISH =
    // "SELECT * FROM Contract WHERE organizationCode = ? AND  SaleTeamCode = ? AND StatusCode <> ?";

    public List<ContractInfo> getContractStatusUnFinish(String EMPID,String organizationCode, String saleTeamCode, String StatusName,String DD) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName, '') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                + " 			 ContST.StatusName"
                + " FROM            Contract AS Cont LEFT OUTER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                + " WHERE   (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName <> ?) AND " +
                        "(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(Cont.ProductSerialNumber, '0', ''),'1', ''),'2', ''),'3', ''),'4', ''),'5', ''),'6', ''),'7', ''),'8', ''),'9', ''))  " +
                        " NOT IN" +
                        DD;



        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName}, ContractInfo.class);
    }


    public List<ContractInfo> getContractStatusUnFinish_SETTING(String organizationCode, String saleTeamCode, String StatusName,String DD) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName, '') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        + " FROM            Contract AS Cont LEFT OUTER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                     //   + " WHERE (substr(Cont.ProductSerialNumber,1, 2) ='WG' or  substr(Cont.ProductSerialNumber,1, 2) ='AZ' or substr(Cont.ProductSerialNumber,1, 2) ='WI' or substr(Cont.ProductSerialNumber,1, 2) ='WS' or substr(Cont.ProductSerialNumber,1, 2) ='WK' or substr(Cont.ProductSerialNumber,1, 2) ='WJ' or substr(Cont.ProductSerialNumber,1, 3) ='SFK' or substr(Cont.ProductSerialNumber,1, 3) ='SFL' or substr(Cont.ProductSerialNumber,1, 2) ='WO') AND  (Cont.ProductSerialNumber != '-') AND   (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName <> ?)";
        + " WHERE   (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName <> ?) AND " +
                        "(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(Cont.ProductSerialNumber, '0', ''),'1', ''),'2', ''),'3', ''),'4', ''),'5', ''),'6', ''),'7', ''),'8', ''),'9', ''))  " +
                        " IN" +
                        DD;

        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName}, ContractInfo.class);
    }



    public List<ContractInfo> getContractStatusUnFinish_ContractInfo_preorder(String organizationCode, String saleTeamCode, String StatusName) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName, '') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        + " FROM            Contract AS Cont LEFT OUTER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                        + " WHERE  (Cont.ProductSerialNumber = '-') AND  (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName <> ?)";


        //Log.e("xxxxx_moo_test",organizationCode+","+saleTeamCode+","+StatusName);
        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName}, ContractInfo.class);
    }


    public List<ContractInfo> getContractStatusUnFinishForCRD(String EMPID,String organizationCode, String saleTeamCode, String StatusName, String SaleEmployeeCode,String DD) {

        Log.e("ttttt",saleTeamCode+","+StatusName+","+SaleEmployeeCode);
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName, '') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        + " FROM            Contract AS Cont LEFT OUTER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                       // + " WHERE substr(Cont.ProductSerialNumber,1, 2) !='WG' AND  substr(Cont.ProductSerialNumber,1, 2) !='WI' AND substr(Cont.ProductSerialNumber,1, 2) !='WS' AND substr(Cont.ProductSerialNumber,1, 2) !='WK' AND substr(Cont.ProductSerialNumber,1, 2) !='WJ' AND substr(Cont.ProductSerialNumber,1, 3) !='SFK' AND substr(Cont.ProductSerialNumber,1, 3) !='SFL' AND substr(Cont.ProductSerialNumber,1, 2) !='WO'  AND (Cont.ProductSerialNumber != '-') AND   (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName <> ?) AND (Cont.CreateBy = ?)";
        + " WHERE   (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName <> ?) AND (Cont.CreateBy = ?) AND " +
                        "(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(Cont.ProductSerialNumber, '0', ''),'1', ''),'2', ''),'3', ''),'4', ''),'5', ''),'6', ''),'7', ''),'8', ''),'9', ''))  " +
                        " NOT IN" +
                       DD;


        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName, SaleEmployeeCode}, ContractInfo.class);
    }



    public List<ContractInfo> getContractStatusUnFinishForCRD_SETTING(String organizationCode, String saleTeamCode, String StatusName, String SaleEmployeeCode,String DD) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName, '') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        + " FROM            Contract AS Cont LEFT OUTER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                     //   + " WHERE (substr(Cont.ProductSerialNumber,1, 2) ='WG' or  substr(Cont.ProductSerialNumber,1, 2) ='WI' or substr(Cont.ProductSerialNumber,1, 2) ='WS' or substr(Cont.ProductSerialNumber,1, 2) ='WK' or substr(Cont.ProductSerialNumber,1, 2) ='WJ' or substr(Cont.ProductSerialNumber,1, 3) ='SFK' or substr(Cont.ProductSerialNumber,1, 3) ='SFL' or substr(Cont.ProductSerialNumber,1, 2) ='WO') AND  (Cont.ProductSerialNumber != '-') AND  (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName <> ?) AND (Cont.CreateBy = ?)";
        + " WHERE    (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName <> ?) AND (Cont.CreateBy = ?) AND " +
                        "(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(Cont.ProductSerialNumber, '0', ''),'1', ''),'2', ''),'3', ''),'4', ''),'5', ''),'6', ''),'7', ''),'8', ''),'9', ''))  " +
                        " IN" +
                        DD;
        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName, SaleEmployeeCode}, ContractInfo.class);
    }



    public List<ContractInfo> getContractStatusUnFinishForCRD_ContractInfo_preorder(String organizationCode, String saleTeamCode, String StatusName, String SaleEmployeeCode) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " 			,Cust.PrefixName || IFNULL(Cust.CustomerName, '') AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                        + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                        + " 			 ContST.StatusName"
                        + " FROM            Contract AS Cont LEFT OUTER JOIN"
                        + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                        + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                        + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                        + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                        + " WHERE    (Cont.ProductSerialNumber = '-') AND (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName <> ?) AND (Cont.CreateBy = ?)";
        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName, SaleEmployeeCode}, ContractInfo.class);
    }


    public List<ContractInfo> getContractStatusFinishByEFFDATE(String organizationCode, String saleTeamCode, String StatusName, Date StartDate, Date EndDate, boolean inDay) {
        if (inDay) {
            final String sql =
                    /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                    //"SELECT distinct Cont.*,"
                    " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                    + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                    + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                    + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                    + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                    + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                    + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                    /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                    + " 			,ifnull(Cust.PrefixName, '') || ifnull(Cust.CustomerName, Cust.CompanyName) AS CustomerFullName, ifnull(Cust.CompanyName, '') AS CompanyName, Cust.IDCard,"
                    + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                    + " 			 ContST.StatusName"
                    + " FROM            Contract AS Cont INNER JOIN"
                    + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                    + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                    + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                    + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                    + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                    + "                AND (Cont.EFFDATE BETWEEN ? AND ?)"
                    + " ORDER BY date(Cont.EFFDATE) ASC, Cont.ProductSerialNumber";

            return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName, valueOf(StartDate), valueOf(EndDate)}, ContractInfo.class);
        } else {
            final String sql =
                    /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                    //"SELECT distinct Cont.*,"
                    " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                    + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                    + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                    + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                    + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                    + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                    + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                    /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                    + " 			,ifnull(Cust.PrefixName, '') || ifnull(Cust.CustomerName, '') AS CustomerFullName, ifnull(Cust.CompanyName, '') AS CompanyName, Cust.IDCard,"
                    + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                    + " 			 ContST.StatusName"
                    + " FROM            Contract AS Cont INNER JOIN"
                    + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                    + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                    + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                    + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                    + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                    + " ORDER BY date(Cont.EFFDATE) ASC, Cont.ProductSerialNumber";

            return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName}, ContractInfo.class);
        }
    }

    public List<ContractInfo> getContractStatusFinishByEFFDATEAndSearch(String organizationCode, String saleTeamCode, String StatusName, Date StartDate, Date EndDate, String Search, boolean inDay) {
        if(inDay){
            final String sql =
                    /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                    //"SELECT distinct Cont.*,"
                    " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                    + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                    + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                    + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                    + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                    + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                    + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                    /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                    + " 			,ifnull(Cust.PrefixName, '') || ifnull(Cust.CustomerName, Cust.CompanyName) AS CustomerFullName, ifnull(Cust.CompanyName, '') AS CompanyName, Cust.IDCard,"
                    + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                    + " 			 ContST.StatusName"
                    + " FROM            Contract AS Cont INNER JOIN"
                    + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                    + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                    + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                    + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                    + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                    + "                AND (Cont.EFFDATE BETWEEN ? AND ?) AND (Cont.ProductSerialNumber LIKE  ? OR CustomerFullName LIKE  ? OR Cont.CONTNO LIKE ?)"
                    + " ORDER BY date(Cont.EFFDATE) ASC, Cont.ProductSerialNumber";

            return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName, valueOf(StartDate), valueOf(EndDate), Search, Search, Search}, ContractInfo.class);
        }
        else{
            final String sql =
                    /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                    //"SELECT distinct Cont.*,"
                    " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                    + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                    + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                    + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                    + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                    + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                    + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                    /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                    + " 			,ifnull(Cust.PrefixName, '') || ifnull(Cust.CustomerName, '') AS CustomerFullName, ifnull(Cust.CompanyName, '') AS CompanyName, Cust.IDCard,"
                    + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                    + " 			 ContST.StatusName"
                    + " FROM            Contract AS Cont INNER JOIN"
                    + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                    + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                    + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                    + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                    + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (ContST.StatusName = ?) AND (Cont.Status != 'T')"
                    + "                AND (Cont.ProductSerialNumber LIKE  ? OR CustomerFullName LIKE  ? OR Cont.CONTNO LIKE ?)"
                    + " ORDER BY date(Cont.EFFDATE) ASC, Cont.ProductSerialNumber";

            return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, StatusName, Search, Search, Search}, ContractInfo.class);
        }

    }


    public List<ContractInfo> getContractForEditContractsCustomerDetailsFragmentForCredit(String organizationCode, String Search) {
        String sql =" SELECT distinct c.[RefNo], c.[CONTNO], c.[CustomerID], c.[OrganizationCode], c.[ContractReferenceNo], "
                + "          ifnull(dc.PrefixName, '') || ifnull(dc.CustomerName, dc.CompanyName) AS CustomerFullName "
                + "   FROM   Contract AS c "
                + "          INNER JOIN DebtorCustomer AS dc ON dc.OrganizationCode = c.OrganizationCode AND dc.CustomerID = c.CustomerID "
                + "   WHERE  (c.isActive = 1) AND (c.OrganizationCode = ?) and (c.Status != ?) AND (c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE (StatusName = 'COMPLETED')))"
                + "          and (IFNULL(dc.CustomerName, dc.CompanyName) || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ? ) "
                + "   ORDER BY date(c.EFFDATE) ASC, c.ProductSerialNumber";

            return executeQueryList(sql, new String[]{organizationCode, ContractInfo.ContractStatus.T.toString(), Search}, ContractInfo.class);
    }

    public List<ContractInfo> getContractFinish(String OrganizationCode, String StatusCode) {
        String QUERY_CONTRACT_GET_FINISH =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " FROM Contract AS Cont "
                + " WHERE (Cont.OrganizationCode = ?) AND (Cont.StatusCode = ?) ";
        return executeQueryList(QUERY_CONTRACT_GET_FINISH, new String[]{OrganizationCode, StatusCode}, ContractInfo.class);
    }

    public void addContract(ContractInfo info) {
        String sql = "INSERT INTO Contract (RefNo, CONTNO, CustomerID, OrganizationCode, STATUS, StatusCode, SALES, TotalPrice, EFFDATE, HasTradeIn, "
                + "TradeInProductCode, TradeInBrandCode, TradeInProductModel, TradeInDiscount, PreSaleSaleCode, PreSaleEmployeeCode, PreSaleEmployeeName, PreSaleTeamCode, "
                + "SaleCode, SaleEmployeeCode, SaleTeamCode, InstallerSaleCode, InstallerEmployeeCode, InstallerTeamCode, InstallDate, ProductSerialNumber, "
                + "ProductID, SaleEmployeeLevelPath, MODE, FortnightID, ProblemID, svcontno, isActive, MODEL, fromrefno, fromcontno, todate, tocontno, "
                + "torefno, CreateDate,  CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate, SaleSubTeamCode, TradeInReturnFlag, IsReadyForSaleAudit, ContractReferenceNo, IsMigrate)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.RefNo, info.CONTNO, info.CustomerID, info.OrganizationCode, info.STATUS, info.StatusCode, valueOf(info.SALES),
                valueOf(info.TotalPrice), valueOf(info.EFFDATE), valueOf(info.HasTradeIn), info.TradeInProductCode, info.TradeInBrandCode,
                info.TradeInProductModel, valueOf(info.TradeInDiscount), info.PreSaleSaleCode, info.PreSaleEmployeeCode, info.PreSaleEmployeeName, info.PreSaleTeamCode, info.SaleCode,
                info.SaleEmployeeCode, info.SaleTeamCode, info.InstallerSaleCode, info.InstallerEmployeeCode, info.InstallerTeamCode,
                valueOf(info.InstallDate), info.ProductSerialNumber, info.ProductID, info.SaleEmployeeLevelPath, valueOf(info.MODE), info.FortnightID,
                info.ProblemID, info.svcontno, valueOf(info.isActive), info.MODEL, info.fromrefno, info.fromcontno, valueOf(info.todate), info.tocontno,
                info.torefno, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate),
                info.SaleSubTeamCode, valueOf(info.TradeInReturnFlag), valueOf(info.IsReadyForSaleAudit), info.ContractReferenceNo, valueOf(info.IsMigrate)});
    }

    public void addContract_PREORDER_SETTING(ContractInfo info) {
        String sql = "INSERT INTO Contract (RefNo, CONTNO, CustomerID, OrganizationCode, STATUS, StatusCode, SALES, TotalPrice, EFFDATE, HasTradeIn, "
                + "TradeInProductCode, TradeInBrandCode, TradeInProductModel, TradeInDiscount, PreSaleSaleCode, PreSaleEmployeeCode, PreSaleEmployeeName, PreSaleTeamCode, "
                + "SaleCode, SaleEmployeeCode, SaleTeamCode, InstallerSaleCode, InstallerEmployeeCode, InstallerTeamCode, InstallDate, ProductSerialNumber, "
                + "ProductID, SaleEmployeeLevelPath, MODE, FortnightID, ProblemID, svcontno, isActive, MODEL, fromrefno, fromcontno, todate, tocontno, "
                + "torefno, CreateDate,  CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate, SaleSubTeamCode, TradeInReturnFlag, IsReadyForSaleAudit, ContractReferenceNo, IsMigrate)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.RefNo, info.CONTNO, info.CustomerID, info.OrganizationCode, info.STATUS, info.StatusCode, valueOf(info.SALES),
                valueOf(info.TotalPrice), valueOf(info.EFFDATE), valueOf(info.HasTradeIn), info.TradeInProductCode, info.TradeInBrandCode,
                info.TradeInProductModel, valueOf(info.TradeInDiscount), info.PreSaleSaleCode, info.PreSaleEmployeeCode, info.PreSaleEmployeeName, info.PreSaleTeamCode, info.SaleCode,
                info.SaleEmployeeCode, info.SaleTeamCode, info.InstallerSaleCode, info.InstallerEmployeeCode, info.InstallerTeamCode,
                valueOf(info.InstallDate), info.ProductSerialNumber, info.ProductID, info.SaleEmployeeLevelPath, valueOf(info.MODE), info.FortnightID,
                info.ProblemID, info.svcontno, valueOf(info.isActive), info.MODEL, info.fromrefno, info.fromcontno, valueOf(info.todate), info.tocontno,
                info.torefno, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate),
                info.SaleSubTeamCode, valueOf(info.TradeInReturnFlag), valueOf(info.IsReadyForSaleAudit), info.ContractReferenceNo, valueOf(info.IsMigrate)  });
    }


    public ContractInfo getContract(String refNo) {
        ContractInfo ret = null;
        final String QUERY_CONTRACT_GET_BY_ID =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " FROM Contract AS Cont "
                + " WHERE (Cont.RefNo = ?)";

        ret = executeQueryObject(QUERY_CONTRACT_GET_BY_ID, new String[]{refNo}, ContractInfo.class);
        return ret;
    }

    public ContractInfo getContract_ContractInfo_preorder(String refNo) {
        ContractInfo ret = null;
        final String QUERY_CONTRACT_GET_BY_ID =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + " FROM Contract AS Cont "
                        + " WHERE (Cont.RefNo = ?)";

        ret = executeQueryObject(QUERY_CONTRACT_GET_BY_ID, new String[]{refNo}, ContractInfo.class);
        return ret;
    }

    public void updateContract(ContractInfo info) {
        /*** [START] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/

        ContractInfo contractInfo = executeQueryObject("SELECT * FROM Contract WHERE (RefNo = ?)", new String[]{info.RefNo}, ContractInfo.class);
        if (contractInfo != null) {
            if (contractInfo.StatusCode != null && !contractInfo.StatusCode.isEmpty()
                    && info.StatusCode != null && !info.StatusCode .isEmpty()) {

                int oldStatusCode = Integer.parseInt(contractInfo.StatusCode);
                int newStatusCode = Integer.parseInt(info.StatusCode );

                if (oldStatusCode >= 7 && newStatusCode < 7) {
                    return;
                }

            }
        }
        /*** [END] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/



        GCMProdStkAndContractInfo gcmProdStkAndContractInfo = new GCMProdStkAndContractController().getGCMProdStkAndContractByRefNo(info.OrganizationCode, info.RefNo);

        if(gcmProdStkAndContractInfo != null) {
            info.isActive = false;
        }

        final String QUERY_CONTRACT_UPDATE = "UPDATE Contract "
                + " SET CONTNO = ?, CustomerID = ?, OrganizationCode = ?, STATUS = ?, StatusCode = ?, "
                + "     SALES = ?, TotalPrice = ?, EFFDATE = ?, HasTradeIn = ?, TradeInProductCode = ?, TradeInBrandCode = ?, TradeInProductModel = ?, "
                + "     TradeInDiscount = ?, PreSaleSaleCode = ?, PreSaleEmployeeCode = ?, PreSaleTeamCode = ?, SaleCode = ?, SaleEmployeeCode = ?, "
                + "     SaleTeamCode = ?, InstallerSaleCode = ?, InstallerEmployeeCode = ?, InstallerTeamCode = ?, InstallDate = ?, ProductSerialNumber = ?, "
                + "     ProductID = ?, SaleEmployeeLevelPath = ?, MODE = ?, FortnightID = ?, ProblemID = ?, svcontno = ?, isActive = ?, MODEL = ?, "
                + "     todate = ?, tocontno = ?, torefno = ?, "
                + "     CreateDate = ?,  CreateBy = ?, LastUpdateDate = ?, LastUpdateBy = ?, SyncedDate = ?, PreSaleEmployeeName = ?, TradeInReturnFlag = ?, ContractReferenceNo= ?, IsMigrate = ? "
                + " WHERE (RefNo = ?)";
        executeNonQuery(QUERY_CONTRACT_UPDATE, new String[]{info.CONTNO, info.CustomerID, info.OrganizationCode, info.STATUS, info.StatusCode,
                valueOf(info.SALES), valueOf(info.TotalPrice), valueOf(info.EFFDATE), valueOf(info.HasTradeIn), info.TradeInProductCode, info.TradeInBrandCode,
                info.TradeInProductModel, valueOf(info.TradeInDiscount), info.PreSaleSaleCode, info.PreSaleEmployeeCode, info.PreSaleTeamCode, info.SaleCode,
                info.SaleEmployeeCode, info.SaleTeamCode, info.InstallerSaleCode, info.InstallerEmployeeCode, info.InstallerTeamCode,
                valueOf(info.InstallDate), info.ProductSerialNumber, info.ProductID, info.SaleEmployeeLevelPath, valueOf(info.MODE), info.FortnightID,
                info.ProblemID, info.svcontno, valueOf(info.isActive), info.MODEL, valueOf(info.todate), info.tocontno, info.torefno, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate),
                info.LastUpdateBy, valueOf(info.SyncedDate), info.PreSaleEmployeeName, valueOf(info.TradeInReturnFlag), info.ContractReferenceNo, valueOf(info.IsMigrate), info.RefNo});
    }

    public void updateContract_set(ContractInfo info) {
        /*** [START] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/

        ContractInfo contractInfo = executeQueryObject("SELECT * FROM Contract WHERE (RefNo = ?)", new String[]{info.RefNo}, ContractInfo.class);
        if (contractInfo != null) {
            if (contractInfo.StatusCode != null && !contractInfo.StatusCode.isEmpty()
                    && info.StatusCode != null && !info.StatusCode .isEmpty()) {

                int oldStatusCode = Integer.parseInt(contractInfo.StatusCode);
                int newStatusCode = Integer.parseInt(info.StatusCode );

                if (oldStatusCode >= 7 && newStatusCode < 7) {
                    return;
                }

            }
        }
        /*** [END] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/



        GCMProdStkAndContractInfo gcmProdStkAndContractInfo = new GCMProdStkAndContractController().getGCMProdStkAndContractByRefNo(info.OrganizationCode, info.RefNo);

        if(gcmProdStkAndContractInfo != null) {
            info.isActive = false;
        }

        final String QUERY_CONTRACT_UPDATE = "UPDATE Contract "
                + " SET CONTNO = ?, CustomerID = ?, OrganizationCode = ?, STATUS = ?, StatusCode = ?, "
                + "     SALES = ?, TotalPrice = ?, EFFDATE = ?, HasTradeIn = ?, TradeInProductCode = ?, TradeInBrandCode = ?, TradeInProductModel = ?, "
                + "     TradeInDiscount = ?, PreSaleSaleCode = ?, PreSaleEmployeeCode = ?, PreSaleTeamCode = ?, SaleCode = ?, SaleEmployeeCode = ?, "
                + "     SaleTeamCode = ?, InstallerSaleCode = ?, InstallerEmployeeCode = ?, InstallerTeamCode = ?, InstallDate = ?, ProductSerialNumber = ?, "
                + "     ProductID = ?, SaleEmployeeLevelPath = ?, MODE = ?, FortnightID = ?, ProblemID = ?, svcontno = ?, isActive = ?, MODEL = ?, "
                + "     todate = ?, tocontno = ?, torefno = ?, "
                + "     CreateDate = ?,  CreateBy = ?, LastUpdateDate = ?, LastUpdateBy = ?, SyncedDate = ?, PreSaleEmployeeName = ?, TradeInReturnFlag = ?, ContractReferenceNo= ?, IsMigrate = ? "
                + " WHERE (RefNo = ?)";
        executeNonQuery(QUERY_CONTRACT_UPDATE, new String[]{info.CONTNO, info.CustomerID, info.OrganizationCode, info.STATUS, info.StatusCode,
                valueOf(info.SALES), valueOf(info.TotalPrice), valueOf(info.EFFDATE), valueOf(info.HasTradeIn), info.TradeInProductCode, info.TradeInBrandCode,
                info.TradeInProductModel, valueOf(info.TradeInDiscount), info.PreSaleSaleCode, info.PreSaleEmployeeCode, info.PreSaleTeamCode, info.SaleCode,
                info.SaleEmployeeCode, info.SaleTeamCode, info.InstallerSaleCode, info.InstallerEmployeeCode, info.InstallerTeamCode,
                valueOf(info.InstallDate), info.ProductSerialNumber, info.ProductID, info.SaleEmployeeLevelPath, valueOf(info.MODE), info.FortnightID,
                info.ProblemID, info.svcontno, valueOf(info.isActive), info.MODEL, valueOf(info.todate), info.tocontno, info.torefno, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate),
                info.LastUpdateBy, valueOf(info.SyncedDate), info.PreSaleEmployeeName, valueOf(info.TradeInReturnFlag), info.ContractReferenceNo, valueOf(info.IsMigrate), info.RefNo});
    }
    public void updateContract_setting(ContractInfo info) {
        /*** [START] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/

       // ContractInfo contractInfo = executeQueryObject("SELECT * FROM Contract WHERE (RefNo = ?)", new String[]{info.RefNo}, ContractInfo.class);
        ContractInfo contractInfo;
        GCMProdStkAndContractInfo gcmProdStkAndContractInfo;

        if(select_page_s==1){
             contractInfo = executeQueryObject("SELECT * FROM Contract WHERE (RefNo = ?)", new String[]{BHApplication.getInstance().getPrefManager().getPreferrence("REFNO_R2")}, ContractInfo.class);
             gcmProdStkAndContractInfo = new GCMProdStkAndContractController().getGCMProdStkAndContractByRefNo("0", BHApplication.getInstance().getPrefManager().getPreferrence("REFNO_R2"));

        }
        else {
             contractInfo = executeQueryObject("SELECT * FROM Contract WHERE (RefNo = ?)", new String[]{BHApplication.getInstance().getPrefManager().getPreferrence("REFNO_R")}, ContractInfo.class);
             gcmProdStkAndContractInfo = new GCMProdStkAndContractController().getGCMProdStkAndContractByRefNo("0", BHApplication.getInstance().getPrefManager().getPreferrence("REFNO_R"));

        }


  /*      if (contractInfo != null) {
            if (contractInfo.StatusCode != null && !contractInfo.StatusCode.isEmpty()
                    && info.StatusCode != null && !info.StatusCode .isEmpty()) {

                int oldStatusCode = Integer.parseInt(contractInfo.StatusCode);
                int newStatusCode = Integer.parseInt(info.StatusCode );

                if (oldStatusCode >= 7 && newStatusCode < 7) {
                    return;
                }

            }
        }*/
        /*** [END] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/



     //   GCMProdStkAndContractInfo gcmProdStkAndContractInfo = new GCMProdStkAndContractController().getGCMProdStkAndContractByRefNo(info.OrganizationCode, info.RefNo);
        //GCMProdStkAndContractInfo gcmProdStkAndContractInfo = new GCMProdStkAndContractController().getGCMProdStkAndContractByRefNo(info.OrganizationCode, info.RefNo);

        if(gcmProdStkAndContractInfo != null) {
            info.isActive = false;
        }

        final String QUERY_CONTRACT_UPDATE = "UPDATE Contract "
                + " SET CONTNO = ?, CustomerID = ?, OrganizationCode = ?, STATUS = ?, StatusCode = ?, "
                + "     SALES = ?, TotalPrice = ?, EFFDATE = ?, HasTradeIn = ?, TradeInProductCode = ?, TradeInBrandCode = ?, TradeInProductModel = ?, "
                + "     TradeInDiscount = ?, PreSaleSaleCode = ?, PreSaleEmployeeCode = ?, PreSaleTeamCode = ?, SaleCode = ?, SaleEmployeeCode = ?, "
                + "     SaleTeamCode = ?, InstallerSaleCode = ?, InstallerEmployeeCode = ?, InstallerTeamCode = ?, InstallDate = ?, ProductSerialNumber = ?, "
                + "     ProductID = ?, SaleEmployeeLevelPath = ?, MODE = ?, FortnightID = ?, ProblemID = ?, svcontno = ?, isActive = ?, MODEL = ?, "
                + "     todate = ?, tocontno = ?, torefno = ?, "
                + "     CreateDate = ?,  CreateBy = ?, LastUpdateDate = ?, LastUpdateBy = ?, SyncedDate = ?, PreSaleEmployeeName = ?, TradeInReturnFlag = ?, ContractReferenceNo= ?, IsMigrate = ? "
                + " WHERE (RefNo = ?)";
        executeNonQuery(QUERY_CONTRACT_UPDATE, new String[]{info.CONTNO, info.CustomerID, info.OrganizationCode, info.STATUS, info.StatusCode,
                valueOf(info.SALES), valueOf(info.TotalPrice), valueOf(info.EFFDATE), valueOf(info.HasTradeIn), info.TradeInProductCode, info.TradeInBrandCode,
                info.TradeInProductModel, valueOf(info.TradeInDiscount), info.PreSaleSaleCode, info.PreSaleEmployeeCode, info.PreSaleTeamCode, info.SaleCode,
                info.SaleEmployeeCode, info.SaleTeamCode, info.InstallerSaleCode, info.InstallerEmployeeCode, info.InstallerTeamCode,
                valueOf(info.InstallDate), info.ProductSerialNumber, info.ProductID, info.SaleEmployeeLevelPath, valueOf(info.MODE), info.FortnightID,
                info.ProblemID, info.svcontno, valueOf(info.isActive), info.MODEL, valueOf(info.todate), info.tocontno, info.torefno, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate),
                info.LastUpdateBy, valueOf(info.SyncedDate), info.PreSaleEmployeeName, valueOf(info.TradeInReturnFlag), info.ContractReferenceNo, valueOf(info.IsMigrate), info.RefNo});
    }

    public void updateTradeInReturnFlag(ContractInfo info) {
        final String QUERY_CONTRACT_UPDATE_TRADE_IN_RETURN_FLAG = "UPDATE Contract SET TradeInReturnFlag = ?, LastUpdateDate = ?, LastUpdateBy = ? WHERE (RefNo = ?)";
        executeNonQuery(QUERY_CONTRACT_UPDATE_TRADE_IN_RETURN_FLAG, new String[]{valueOf(info.TradeInReturnFlag), valueOf(info.LastUpdateDate),
                info.LastUpdateBy, info.RefNo});
    }

    public void updateStatusCode(String refNo, String statusCode) {

        /*** [START] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/

        ContractInfo contractInfo = executeQueryObject("SELECT * FROM Contract WHERE (RefNo = ?)", new String[]{refNo}, ContractInfo.class);
        if (contractInfo != null) {
            if (contractInfo.StatusCode != null && !contractInfo.StatusCode.isEmpty()
                    && statusCode != null && !statusCode.isEmpty()) {

                int oldStatusCode = Integer.parseInt(contractInfo.StatusCode);
                int newStatusCode = Integer.parseInt(statusCode);

                if (oldStatusCode >= 7 && newStatusCode < 7) {
                    return;
                }

            }
        }

        /*** [END] :: Fixed - [BHPROJ-1036-9114] - เลขที่สัญญาไม่ถูกต้อง ***/



        String sql = "UPDATE Contract SET StatusCode=? WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[]{statusCode, refNo});
    }

    /**
     * OLDEST **
     */
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*public List<ContractInfo> getcustomeridContract(String CustomerID) {
        final String QUERY_CONTRACT_CUSTOMET = "SELECT * FROM Contract WHERE CustomerID = ?";
        return executeQueryList(QUERY_CONTRACT_CUSTOMET, new String[]{CustomerID}, ContractInfo.class);
    }*/
    public ContractInfo getcustomeridContract(String CustomerID) {
        final String QUERY_CONTRACT_CUSTOMET =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " FROM Contract AS Cont "
                + " WHERE (Cont.CustomerID = ?)";
        return executeQueryObject(QUERY_CONTRACT_CUSTOMET, new String[]{CustomerID}, ContractInfo.class);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ContractInfo getContractByCONTNO(String CONTNO) {
        final String QUERY_CONTRACT_GET_BY_CONTNO =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " FROM Contract AS Cont "
                + " WHERE (Cont.CONTNO = ?) ";
        return executeQueryObject(QUERY_CONTRACT_GET_BY_CONTNO, new String[]{CONTNO}, ContractInfo.class);
    }

    /**
     * OLDEST **
     */

    public ContractInfo getLastContract(String documentTypeCode, String subTeamCode, String employeeID, String yearMonth) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " FROM Contract AS Cont "
                + " WHERE (substr(Cont.contno,1,1) = ?) AND (substr(Cont.contno,2,8) = ?) AND (substr(Cont.contno,10,2) = ?) AND (substr(Cont.contno,12,4) = ?) "
                + " ORDER BY Cont.contno DESC";
        List<ContractInfo> tmpContractList = executeQueryList(sql, new String[]{documentTypeCode, subTeamCode, employeeID, yearMonth}, ContractInfo.class);

        if (tmpContractList == null)
            return null;
        else
            return tmpContractList.get(0);
    }

    public ContractInfo getLastContract(String saleCode, String yearMonth) {
        final String sql = 
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " FROM Contract AS Cont "
                + " WHERE (Cont.SaleCode = ?) AND (Cont.STATUS <> 'DRAFT') AND (substr((Cont.ContNo,length((Cont.ContNo)-6,4) = ?) "
                + " ORDER BY substr((ont.ContNo,length(ContNo)-2,3) DESC";
        List<ContractInfo> tmpContractList = executeQueryList(sql, new String[]{saleCode, yearMonth}, ContractInfo.class);

        if (tmpContractList == null)
            return null;
        else
            return tmpContractList.get(0);
    }

    public List<ContractInfo> getContract() {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " FROM Contract AS Cont ";
        return executeQueryList(sql, null, ContractInfo.class);
    }

    public ContractInfo getContractByIDCard(String idCard) {
        String sql = 
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.*,"
                " SELECT distinct Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/
                + "     , dc.IDCard "
                + " FROM Contract as Cont INNER JOIN DebtorCustomer as dc on Cont.CustomerID = dc.CustomerID"
                + " WHERE (dc.IDCard = ?) ";
        return executeQueryObject(sql, new String[]{idCard}, ContractInfo.class);
    }


    /***
     * [START] - Fixed - [Android-เก็บเงินค่างวด] เปลี่ยนเงื่อนไขการนับจำนวนงวดที่ค้าง
     ***/

    public ContractInfo getContractByAssignIDForNextPayment(String assignID) {
        String sql = "SELECT DISTINCT" +
                "       c.CONTNO, c.Mode, c.EFFDATE, c.ProductSerialNumber, c.SALES, c.TradeInDiscount, c.TotalPrice, c.RefNo, c.MODEL, c.ContractReferenceNo" +
                "       , IFNULL(dc.PrefixName,'') || IFNULL(dc.CustomerName,'') || IFNULL(dc.CompanyName,'') AS CustomerFullName, dc.IDCard" +
                "       , p.ProductName" +
                "       , spp.PaymentPeriodNumber" +
                "       , sppMax.NextPaymentPeriodNumber " +
                "       , IFNULL(SUM(sppSumNext.NetAmount), 0) AS OutstandingAmount" +
                "       , IFNULL(resultPaidAmount.PaidAmount, 0) AS PaidAmount" +
                " FROM Assign a " +
                "       INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID  = a.ReferenceID" +
                "       INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                "       INNER JOIN DebtorCustomer dc on c.OrganizationCode = dc.OrganizationCode AND c.CustomerID = dc.CustomerID " +
                "       LEFT OUTER JOIN Product p ON c.OrganizationCode = p.OrganizationCode AND c.ProductID = p.ProductID" +
                "       LEFT OUTER JOIN (" +
                "       		SELECT MAX(IFNULL(sppNext.PaymentPeriodNumber, spp.PaymentPeriodNumber)) + 1 AS NextPaymentPeriodNumber, a.RefNo " +
                "             FROM  Assign a" +
                "             INNER JOIN SalePaymentPeriod spp on spp.SalePaymentPeriodID = a.ReferenceID" +
                "              LEFT OUTER JOIN SalePaymentPeriod sppNext on sppNext.RefNo = a.RefNo " +
//                "           AND DATE(sppNext.PaymentDueDate) < DATE('now') " +
                "                 AND (DATE(sppNext.PaymentDueDate) < DATE(?) OR sppNext.PaymentPeriodNumber <= spp.PaymentPeriodNumber) " +
                "                 AND sppNext.PaymentComplete = 0" +
                "            WHERE a.AssignID = ?" +
                "       		GROUP BY a.RefNo, spp.PaymentPeriodNumber" +
                "        ) AS sppMax ON sppMax.RefNo = a.RefNo" +
                "       LEFT OUTER JOIN SalePaymentPeriod sppSumNext on sppSumNext.RefNo = a.RefNo and sppSumNext.PaymentPeriodNumber >= sppMax.NextPaymentPeriodNumber" +
                "       LEFT OUTER JOIN (" +
                "            SELECT SUM(sppp.Amount) AS PaidAmount, a.RefNo " +
                "            FROM Assign a " +
                "               INNER JOIN SalePaymentPeriod spp ON spp.RefNo = a.RefNo" +
                "               INNER JOIN SalePaymentPeriodPayment sppp ON sppp.SalePaymentPeriodID = spp.SalePaymentPeriodID" +
                "            WHERE a.AssignID = ?" +
                "            GROUP BY a.RefNo" +
                "       ) AS resultPaidAmount ON resultPaidAmount.RefNo = a.RefNo" +
                " WHERE a.AssignID = ?" +
                " GROUP BY c.CONTNO, c.Mode, c.EFFDATE, c.ProductSerialNumber, c.SALES, c.TradeInDiscount, c.TotalPrice, c.RefNo" +
                "   , CustomerFullName, dc.IDCard, p.ProductName, sppMax.NextPaymentPeriodNumber, spp.PaymentPeriodNumber, c.MODEL, c.ContractReferenceNo";
        return executeQueryObject(sql, new String[]{ valueOf(new TripController().getDueDate()) , assignID, assignID, assignID}, ContractInfo.class);
    }

    public ContractInfo getContractByRefNoForNextPayment(String OrganizationCode, String RefNo) {
        String sql = "SELECT " +
                "       c.SaleEmployeeLevelPath, c.CONTNO, c.Mode, c.EFFDATE, ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber, c.SALES, c.TradeInDiscount, c.TotalPrice, c.RefNo, c.MODEL, c.CustomerID" +
                "       , c.ProductID, c.OrganizationCode, c.ContractReferenceNo" +
                "       , IFNULL(dc.PrefixName,'') || IFNULL(dc.CustomerName,'') || IFNULL(dc.CompanyName,'') AS CustomerFullName, dc.IDCard" +
                "       , p.ProductName" +
                "       , IFNULL(countHoldSalePaymentPeriod.HoldSalePaymentPeriod, 0) AS HoldSalePaymentPeriod" +
                "       , IFNULL(packageDetail.CloseDiscountAmount, 0) AS CloseDiscountAmount" +
                "       , IFNULL(sppMin.NetAmount, 0) - IFNULL(paidMin.SumAmount, 0) AS MinOutStandingAmount, IFNULL(sppMin.PaymentPeriodNumber, 0) AS MinPaymentPeriodNumber" +
                "       , sppMin.SalePaymentPeriodID AS MinSalePaymentPeriodID" +
                "       , IFNULL(sppNext.NetAmount, 0) AS NextNetAmount, IFNULL(sppNext.PaymentPeriodNumber, 0) AS NextPaymentPeriodNumber" +
                "       , IFNULL(sppFirst.NetAmount, 0) AS FirstNetAmount" +
                "       , IFNULL(sppSecond.NetAmount, 0) AS SecondNetAmount" +
                "       , IFNULL(sppThird.NetAmount, 0) AS ThirdNetAmount" +
                "       , c.TotalPrice - IFNULL(paidTotal.SumPAYAMT, 0) AS TotalOutstandingAmount" +
                " FROM Contract c " +
                "       INNER JOIN DebtorCustomer dc on c.OrganizationCode = dc.OrganizationCode AND c.CustomerID = dc.CustomerID " +
                "       INNER JOIN Product p ON c.OrganizationCode = p.OrganizationCode AND c.ProductID = p.ProductID" +
                "       LEFT OUTER JOIN (" +
                "               SELECT COUNT(*) AS HoldSalePaymentPeriod, RefNo" +
                "               FROM SalePaymentPeriod" +
                "               WHERE PaymentComplete = 0 AND RefNo = ? AND DATE(PaymentDueDate) < DATE(?)" +
                "               GROUP BY RefNo" +
                "       ) AS countHoldSalePaymentPeriod ON countHoldSalePaymentPeriod.RefNo = c.RefNo" +
                "       INNER JOIN (" +
                "               SELECT MIN(PaymentPeriodNumber) AS PaymentPeriodNumber, RefNo " +
                "               FROM SalePaymentPeriod " +
                "               WHERE PaymentComplete = 0 AND RefNo = ?" +
                "               GROUP BY RefNo" +
                "       ) AS findMinPeriod ON findMinPeriod.RefNo = c.RefNo" +
                "       LEFT OUTER JOIN PackagePeriodDetail packageDetail ON packageDetail.Model = c.MODEL" +
                "               AND packageDetail.PaymentPeriodNumber = findMinPeriod.PaymentPeriodNumber" +
                "               AND packageDetail.OrganizationCode = c.OrganizationCode" +
                "       LEFT OUTER JOIN SalePaymentPeriod sppMin ON sppMin.RefNo = c.RefNo AND sppMin.PaymentPeriodNumber = findMinPeriod.PaymentPeriodNumber" +
                "       LEFT OUTER JOIN SalePaymentPeriod sppNext ON sppNext.RefNo = c.RefNo AND sppMin.SalePaymentPeriodID IS NOT NULL AND sppNext.PaymentPeriodNumber = (sppMin.PaymentPeriodNumber + 1)" +
                "       INNER JOIN SalePaymentPeriod sppFirst ON sppFirst.RefNo = c.RefNo AND sppFirst.PaymentPeriodNumber = 1" +
                "       LEFT OUTER JOIN SalePaymentPeriod sppSecond ON sppSecond.RefNo = c.RefNo AND sppSecond.PaymentPeriodNumber = 2" +
                "       LEFT OUTER JOIN SalePaymentPeriod sppThird ON sppThird.RefNo = c.RefNo AND sppThird.PaymentPeriodNumber = 3" +
                "       LEFT OUTER JOIN (" +
                "                           SELECT SUM(sppp.Amount) AS SumAmount, spp.SalePaymentPeriodID " +
                "                           FROM SalePaymentPeriod as spp " +
                "                           INNER JOIN SalePaymentPeriodPayment as sppp ON sppp.SalePaymentPeriodID = spp.SalePaymentPeriodID " +
                "                           WHERE spp.RefNo = ? " +
                "                           GROUP BY sppp.SalePaymentPeriodID " +
                "                        ) AS paidMin ON paidMin.SalePaymentPeriodID = sppMin.SalePaymentPeriodID " +
                "       LEFT OUTER JOIN (" +
                "               SELECT SUM(PAYAMT) AS SumPAYAMT, RefNo" +
                "               FROM Payment" +
                "               WHERE RefNo = ?" +
                "               GROUP BY RefNo" +
                "       ) AS paidTotal ON paidTotal.RefNo = c.RefNo" +
                " WHERE (c.OrganizationCode = ?) AND (c.RefNo = ?) AND (c.IsActive = 1)";
        return executeQueryObject(sql, new String[]{RefNo, valueOf(new TripController().getDueDate()), RefNo, RefNo, RefNo, OrganizationCode, RefNo}, ContractInfo.class);
    }

    /***
     * [END] - Fixed - [Android-เก็บเงินค่างวด] เปลี่ยนเงื่อนไขการนับจำนวนงวดที่ค้าง
     ***/

    /////////////////////////////////////////////// [START] Fixed - [BHPROJ-0024-418] ////////////////////////////////////////////////////////
    /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */

    public ContractInfo getContractByProductSerialNumberByStatus(String organizationCode, String productSerialNumber) { // , String status
        // Called by ChangeProductCustomerListFragment.java
        String sql = 
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.*"
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "     ,dc.PrefixName || dc.CustomerName AS CustomerFullName, dc.CompanyName, dc.IDCard,"
                + "	    p.ProductName, e.FirstName || ' ' || e.LastName as SaleEmployeeName"
                + "	FROM Contract as c inner join"
                + "	    DebtorCustomer as dc on c.OrganizationCode=dc.OrganizationCode and c.CustomerID=dc.CustomerID inner join"
                + "	    Product as p on c.OrganizationCode=dc.OrganizationCode and c.ProductID=p.ProductID inner join"
                + "	    Employee as e on c.SaleEmployeeCode=e.EmpID"
                + "	WHERE (c.OrganizationCode = ?)" + " AND (c.isActive = 1)" + "	AND (c.ProductSerialNumber = ?)"
//                + "     AND (c.Status = ?) ";
                + "	    AND (c.Status IN ('NORMAL', 'F'))"
                + "     AND (c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE (StatusName = 'COMPLETED'))) ";
        return executeQueryObject(sql, new String[]{organizationCode, productSerialNumber}, ContractInfo.class);    // , status
    }

    public ContractInfo getContractProductSerialByStatus(String organizationCode, String productSerialNumber) {     // , String status
        // Called by ImpoundProductDetailFragment.java + ImpoundProductListOtherTeamFragment.java
        ContractInfo ret = null;
        String sql = 
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT DISTINCT Contract.* " 
                " SELECT Contract.[RefNo],Contract.[CONTNO],Contract.[CustomerID],Contract.[OrganizationCode],Contract.[STATUS],Contract.[StatusCode],Contract.[SALES],Contract.[TotalPrice],Contract.[EFFDATE],Contract.[HasTradeIn]"
                + "     ,Contract.[TradeInProductCode],Contract.[TradeInBrandCode],Contract.[TradeInProductModel],Contract.[TradeInDiscount]"
                + "     ,Contract.[PreSaleSaleCode],Contract.[PreSaleEmployeeCode],Contract.[PreSaleTeamCode],Contract.[SaleCode],Contract.[SaleEmployeeCode],Contract.[SaleTeamCode] "
                + "     ,Contract.[InstallerSaleCode],Contract.[InstallerEmployeeCode],Contract.[InstallerTeamCode],Contract.[InstallDate],ifnull(Contract.[ProductSerialNumber],'') AS ProductSerialNumber,Contract.[ProductID]"
                + "     ,Contract.[SaleEmployeeLevelPath],Contract.[MODE],Contract.[FortnightID],Contract.[ProblemID],Contract.[svcontno],Contract.[isActive],Contract.[MODEL]"
                + "     ,Contract.[fromrefno],Contract.[fromcontno],Contract.[todate],Contract.[tocontno],Contract.[torefno],Contract.[CreateDate],Contract.[CreateBy],Contract.[LastUpdateDate],Contract.[LastUpdateBy],Contract.[SyncedDate]"
                + "     ,Contract.[PreSaleEmployeeLevelPath],Contract.[InstallerEmployeeLevelPath],Contract.[PreSaleEmployeeName],Contract.[EmployeeHistoryID],Contract.[SaleSubTeamCode],Contract.[TradeInReturnFlag], Contract.[IsReadyForSaleAudit], Contract.[ContractReferenceNo], Contract.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "       , Product.ProductName  " +
                "       , DebtorCustomer.PrefixName || IFNULL(DebtorCustomer.CustomerName,'') AS CustomerFullName" +
                "       , DebtorCustomer.CompanyName" +
                "       , DebtorCustomer.IDCard " +
                "       , Contract.SaleCode || '   ' || IFNULL(Employee.FirstName,'') || '   ' || IFNULL(Employee.LastName, '') AS SaleEmployeeName  " +
                "       , IFNULL(Employee.FirstName,'') || '   ' || IFNULL(Employee.LastName, '') AS SaleName  " +
                //"     , Employee.FirstName || '   ' || Employee.LastName AS SaleEmployeeName  " +
                //"     , EmployeeDetail.ParentEmployeeCode AS upperEmployeeID" +
                //"     , upperEmployee.FirstName || '   ' || upperEmployee.LastName AS upperEmployeeName" +
                "       , SaleLeader.EmpID AS upperEmployeeID" +
                "       , Contract.SaleTeamCode || '   ' || SaleLeader.FirstName || '   ' || SaleLeader.LastName AS SaleLeaderNameOfSaleTeamCode  " +
                "       , Contract.SaleTeamCode || '   ' || SaleLeader.FirstName || '   ' || SaleLeader.LastName AS upperEmployeeName  " +
                " FROM Contract   INNER JOIN  Product on Contract.ProductID = Product.ProductID" +
                "       INNER JOIN DebtorCustomer on Contract.CustomerID = DebtorCustomer.CustomerID" +
                "       LEFT OUTER JOIN Employee  on Contract.SaleEmployeeCode = Employee.EmpID" +
                //"       LEFT OUTER JOIN EmployeeDetail on Employee.EmpID = EmployeeDetail.EmployeeCode AND EmployeeDetail.TeamCode = Contract.SaleTeamCode AND EmployeeDetail.SaleCode = Contract.SaleCode" +
                //"     LEFT OUTER JOIN Employee  as upperEmployee on EmployeeDetail.ParentEmployeeCode = upperEmployee.EmpID" +
                "       LEFT OUTER JOIN EmployeeDetailHistory AS  SaleLeaderDetail ON SaleLeaderDetail.TreeHistoryID = Contract.SaleEmployeeLevelPath and SaleLeaderDetail.TeamCode = Contract.SaleTeamCode" +
                "                   AND SaleLeaderDetail.PositionCode = 'SaleLeader'" +
                "       LEFT OUTER JOIN Employee AS SaleLeader on SaleLeader.EmpID = SaleLeaderDetail.EmployeeCode" +
                " WHERE (Contract.isActive = 1) AND (Contract.OrganizationCode = ?) AND (Contract.ProductSerialNumber = ?) " +
//                "       AND (Contract.Status = ?) ";
                "       AND (Contract.Status IN ('NORMAL', 'F')) " +
                "       AND (Contract.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE (StatusName = 'COMPLETED'))) ";

//        AND (EmployeeDetail.PositionCode = 'Sale')
        ret = executeQueryObject(sql, new String[]{organizationCode, productSerialNumber}, ContractInfo.class);         // , status
        return ret;
    }

    public List<ContractInfo> getContractListByRefNoOrSearchText(String organizationCode, String searchText, String refNo) {    // String status,
        // Called by ComplainDetailFragment.java + ComplainRequestList.java
        String sql = 
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT c.* " 
                " SELECT c.[RefNo],c.[CONTNO],c.[CustomerID],c.[OrganizationCode],c.[STATUS],c.[StatusCode],c.[SALES],c.[TotalPrice],c.[EFFDATE],c.[HasTradeIn]"
                + "     ,c.[TradeInProductCode],c.[TradeInBrandCode],c.[TradeInProductModel],c.[TradeInDiscount]"
                + "     ,c.[PreSaleSaleCode],c.[PreSaleEmployeeCode],c.[PreSaleTeamCode],c.[SaleCode],c.[SaleEmployeeCode],c.[SaleTeamCode] "
                + "     ,c.[InstallerSaleCode],c.[InstallerEmployeeCode],c.[InstallerTeamCode],c.[InstallDate],ifnull(c.[ProductSerialNumber],'') AS ProductSerialNumber,c.[ProductID]"
                + "     ,c.[SaleEmployeeLevelPath],c.[MODE],c.[FortnightID],c.[ProblemID],c.[svcontno],c.[isActive],c.[MODEL]"
                + "     ,c.[fromrefno],c.[fromcontno],c.[todate],c.[tocontno],c.[torefno],c.[CreateDate],c.[CreateBy],c.[LastUpdateDate],c.[LastUpdateBy],c.[SyncedDate]"
                + "     ,c.[PreSaleEmployeeLevelPath],c.[InstallerEmployeeLevelPath],c.[PreSaleEmployeeName],c.[EmployeeHistoryID],c.[SaleSubTeamCode],c.[TradeInReturnFlag], c.[IsReadyForSaleAudit], c.[ContractReferenceNo], c.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "       , IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName, IFNULL(dc.IDCard, '') AS IDCard" +
                "       , p.ProductName" +
                "       , IFNULL(e.FirstName, '') || '  ' || IFNULL(e.LastName, '') AS SaleEmployeeName" +
                "       , IFNULL(headTeam.FirstName, '') || '  ' || IFNULL(headTeam.LastName, '') AS SaleLeaderName" +
                " FROM Contract c" +
                "       INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                "       INNER JOIN Product p ON p.ProductID = c.ProductID" +
                "       LEFT OUTER JOIN Employee e ON e.EmpID = c.SaleEmployeeCode " +
                "       LEFT OUTER JOIN EmployeeDetailHistory AS ed ON ed.TreeHistoryID = c.SaleEmployeeLevelPath and ed.EmployeeCode = e.EmpID AND ed.SaleCode = c.SaleCode AND ed.TeamCode = c.SaleTeamCode" +
                "       LEFT OUTER JOIN Employee headTeam ON headTeam.EmpID = ed.TeamHeadCode" +
                " WHERE (c.isActive = 1) " +
//                "       AND (c.Status = ?) " +
                "       AND (c.Status IN ('NORMAL', 'F')) " +
                "       AND (c.OrganizationCode = ?)" +
                "       AND (c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE (StatusName = 'COMPLETED'))) ";

        ArrayList<String> args = new ArrayList<>();
        //args.add(status);
        args.add(organizationCode);

        if(refNo != null){
            sql += " AND c.RefNo = ?";
            args.add(refNo);
        }

        if(searchText != null)
        {
            sql += " AND (IFNULL(dc.CustomerName, dc.CompanyName) || IFNULL(dc.IDCard, '') || c.ProductSerialNumber || c.CONTNO LIKE ?)";
            args.add("%" + searchText + "%");
        }

        return executeQueryList(sql, args.toArray(new String[args.size()]), ContractInfo.class);
    }

    public List<ContractInfo> getContractListForComplainRequestList(String SearchText, String OrganizationCode, String RefNo, String EmpID, String TeamCode, String sourceSystem, String positionCode)  {
        ArrayList<String> args = new ArrayList<>();
        String sql = "select c.RefNo , c.CONTNO, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName " +
                "     from Contract as c " +
                "          INNER JOIN DebtorCustomer as dc ON dc.CustomerID = c.CustomerID and dc.OrganizationCode = c.OrganizationCode " +
                "          LEFT JOIN Assign as a ON a.TaskType in (?, ?) and a.RefNo = c.RefNo and a.OrganizationCode = c.OrganizationCode " +
                "     where c.isActive = 1 and c.STATUS in (?, ?) and c.OrganizationCode = ? ";

        args.add(AssignController.AssignTaskType.SalePaymentPeriod.toString());
        args.add(AssignController.AssignTaskType.SaleAudit.toString());
        args.add(ContractInfo.ContractStatus.NORMAL.toString());
        args.add(ContractInfo.ContractStatus.F.toString());
        args.add(OrganizationCode);

        //region ใช้แยก Sale และ Credit
        switch (EmployeeController.SourceSystem.valueOf(EmployeeController.SourceSystem.class, sourceSystem)) {
            case Sale:
                List<String> listPositionCode = Arrays.asList(positionCode.split(","));
                if (listPositionCode.contains("SaleLeader")) { //หัวหน้าทีม
                    sql += "  and c.SaleTeamCode = ? ";
                    args.add(TeamCode);
                } else {
                    sql += "  and c.SaleEmployeeCode = ? ";
                    args.add(EmpID);
                }
                break;
            case Credit:
                sql += "       and a.AssigneeEmpID = ? ";
                args.add(EmpID);
                break;
        }
        //endregion

        if(SearchText != null){
            sql += "           and (IFNULL(dc.CustomerName, dc.CompanyName) || IFNULL(dc.IDCard, '') || IFNULL(dc.AuthorizedIDCard, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ? ) ";
            args.add("%" + SearchText + "%");
        }

        if(RefNo != null){
            sql += "           and c.RefNo = ? ";
            args.add(RefNo);
        }

        sql += "       GROUP BY c.RefNo, c.CONTNO, dc.PrefixName, dc.CustomerName, dc.CompanyName " +
                "      ORDER BY c.CONTNO ASC";
        return executeQueryList(sql, args.toArray(new String[args.size()]), ContractInfo.class);
    }

    public ContractInfo getContractBySerialNo(String organizationCode, String productSerialNumber, String status) {
        // Called by ChangeContractListFragment.java + ImpoundProductListFragment.java + LossDetailFragment.java + LossMainFragment.java + ChangeProductDetailFragment.java
        String sql = 
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT Cont.* " 
                " SELECT Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate], "
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + "         ifnull(Cust.PrefixName,'') || ' ' ||ifnull(Cust.CustomerName,'') || ifnull(Cust.CompanyName,'') AS CustomerFullName, "
                + "         Cust.IDCard, Prod.ProductName, "
                + "         ifnull(Cont.SaleCode,'') || ' ' || ifnull(Sale.FirstName,'') || '  ' || ifnull(Sale.LastName,'') AS SaleEmployeeName,"
                + "         ifnull(Cont.SaleTeamCode,'') || ' ' || upperEmp.FirstName || '  ' || upperEmp.LastName AS upperEmployeeName"
                + " FROM            Contract AS Cont "
                + "         INNER JOIN DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode "
                + "                         AND Cont.CustomerID = Cust.CustomerID "
                + "         INNER JOIN Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode "
                + "                         AND Cont.ProductID = Prod.ProductID "
                + "         INNER JOIN Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode "
                + "                         AND Cont.SaleEmployeeCode = Sale.EmpID "
                + "         INNER JOIN Team ON Cont.OrganizationCode = Team.OrganizationCode "
                + "                         AND Cont.SaleTeamCode = Team.Code "
                + "         LEFT OUTER JOIN EmployeeDetailHistory AS  TSEmp ON TSEmp.TreeHistoryID = Cont.SaleEmployeeLevelPath and Cont.OrganizationCode = TSEmp.OrganizationCode "
                + "                         AND Cont.SaleTeamCode = TSEmp.TeamCode "
                + "                         AND Cont.SaleEmployeeCode = TSEmp.EmployeeCode "
                + "                         AND TSEmp.SaleCode = Cont.SaleCode "
                + "         LEFT OUTER JOIN Employee AS upperEmp ON TSEmp.OrganizationCode = upperEmp.OrganizationCode "
                + "                         AND TSEmp.ParentEmployeeCode = upperEmp.EmpID "
                + "         LEFT OUTER JOIN ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.ProductSerialNumber = ?) "
                + "         AND (Cont.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE (StatusName = 'COMPLETED'))) ";

                if (status.equals("")) {
                    sql = sql + " AND (Cont.Status IN ('NORMAL', 'F')) ";
                } else {
                    sql = sql + " AND (Cont.Status = ?)";
                }

        if (status.equals("")) {
            return executeQueryObject(sql, new String[]{organizationCode, productSerialNumber}, ContractInfo.class);
        } else {
            return executeQueryObject(sql, new String[]{organizationCode, productSerialNumber, status}, ContractInfo.class);
        }
    }

    public ContractInfo getContractByRefNo(String organizationCode, String refNo, String status) {
        String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT Cont.* "
                " SELECT Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                        + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                        + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                        + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                        + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                        + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                        + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate], "
                        /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                        + "         ifnull(Cust.PrefixName,'') || ' ' ||ifnull(Cust.CustomerName,'') || ifnull(Cust.CompanyName,'') AS CustomerFullName, "
                        + "         Cust.IDCard, Prod.ProductName, "
                        + "         ifnull(Cont.SaleCode,'') || ' ' || ifnull(Sale.FirstName,'') || '  ' || ifnull(Sale.LastName,'') AS SaleEmployeeName,"
                        + "         ifnull(Cont.SaleTeamCode,'') || ' ' || upperEmp.FirstName || '  ' || upperEmp.LastName AS upperEmployeeName"
                        + " FROM            Contract AS Cont "
                        + "         INNER JOIN DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode "
                        + "                         AND Cont.CustomerID = Cust.CustomerID "
                        + "         LEFT OUTER JOIN Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode "
                        + "                         AND Cont.ProductID = Prod.ProductID "
                        + "         INNER JOIN Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode "
                        + "                         AND Cont.SaleEmployeeCode = Sale.EmpID "
                        + "         INNER JOIN Team ON Cont.OrganizationCode = Team.OrganizationCode "
                        + "                         AND Cont.SaleTeamCode = Team.Code "
                        + "         LEFT OUTER JOIN EmployeeDetailHistory AS  TSEmp ON TSEmp.TreeHistoryID = Cont.SaleEmployeeLevelPath and Cont.OrganizationCode = TSEmp.OrganizationCode "
                        + "                         AND Cont.SaleTeamCode = TSEmp.TeamCode "
                        + "                         AND Cont.SaleEmployeeCode = TSEmp.EmployeeCode "
                        + "                         AND TSEmp.SaleCode = Cont.SaleCode "
                        + "         LEFT OUTER JOIN Employee AS upperEmp ON TSEmp.OrganizationCode = upperEmp.OrganizationCode "
                        + "                         AND TSEmp.ParentEmployeeCode = upperEmp.EmpID "
                        + "         LEFT OUTER JOIN ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                        + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.RefNo = ?) "
                        + "         AND (Cont.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE (StatusName = 'COMPLETED'))) ";

        if (status.equals("")) {
            sql = sql + " AND (Cont.Status IN ('NORMAL', 'F')) ";
        } else {
            sql = sql + " AND (Cont.Status = ?)";
        }

        if (status.equals("")) {
            return executeQueryObject(sql, new String[]{organizationCode, refNo}, ContractInfo.class);
        } else {
            return executeQueryObject(sql, new String[]{organizationCode, refNo, status}, ContractInfo.class);
        }
    }

    //public List<ContractInfo> getContractForPaymentPeriodNumber1ByPaymentComplete(String organizationCode, String saleTeamCode, String Status, boolean PaymentComplete) {
    public List<ContractInfo> getContractForPaymentPeriodNumber1ByPaymentComplete(String organizationCode, String saleTeamCode, boolean PaymentComplete) {
        // Called by ImpoundProductListFragment.java
        List<ContractInfo> ret = null;
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT distinct Cont.* "
                " SELECT Cont.[RefNo],Cont.[CONTNO],Cont.[CustomerID],Cont.[OrganizationCode],Cont.[STATUS],Cont.[StatusCode],Cont.[SALES],Cont.[TotalPrice],Cont.[EFFDATE],Cont.[HasTradeIn]"
                + "     ,Cont.[TradeInProductCode],Cont.[TradeInBrandCode],Cont.[TradeInProductModel],Cont.[TradeInDiscount]"
                + "     ,Cont.[PreSaleSaleCode],Cont.[PreSaleEmployeeCode],Cont.[PreSaleTeamCode],Cont.[SaleCode],Cont.[SaleEmployeeCode],Cont.[SaleTeamCode] "
                + "     ,Cont.[InstallerSaleCode],Cont.[InstallerEmployeeCode],Cont.[InstallerTeamCode],Cont.[InstallDate],ifnull(Cont.[ProductSerialNumber],'') AS ProductSerialNumber,Cont.[ProductID]"
                + "     ,Cont.[SaleEmployeeLevelPath],Cont.[MODE],Cont.[FortnightID],Cont.[ProblemID],Cont.[svcontno],Cont.[isActive],Cont.[MODEL]"
                + "     ,Cont.[fromrefno],Cont.[fromcontno],Cont.[todate],Cont.[tocontno],Cont.[torefno],Cont.[CreateDate],Cont.[CreateBy],Cont.[LastUpdateDate],Cont.[LastUpdateBy],Cont.[SyncedDate]"
                + "     ,Cont.[PreSaleEmployeeLevelPath],Cont.[InstallerEmployeeLevelPath],Cont.[PreSaleEmployeeName],Cont.[EmployeeHistoryID],Cont.[SaleSubTeamCode],Cont.[TradeInReturnFlag], Cont.[IsReadyForSaleAudit], Cont.[ContractReferenceNo], Cont.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " 			,IFNULL(Cust.PrefixName,'') || IFNULL(Cust.CustomerName, Cust.CompanyName) AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                + " 			 Prod.ProductName, ifnull(Sale.FirstName,'') || '  ' || ifnull(Sale.LastName,'') AS SaleEmployeeName,"
                + " 			 ContST.StatusName"
                + " FROM            Contract AS Cont LEFT OUTER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         EmployeeDetailHistory AS  TSEmp ON TSEmp.TreeHistoryID = Cont.SaleEmployeeLevelPath and Cont.OrganizationCode = TSEmp.OrganizationCode AND Cont.SaleTeamCode = TSEmp.TeamCode AND"
                + "                         Cont.SaleEmployeeCode = TSEmp.EmployeeCode AND Cont.SaleCode = TSEmp.SaleCode LEFT OUTER JOIN"
                + "                         Employee AS upperEmp ON TSEmp.OrganizationCode = upperEmp.OrganizationCode AND"
                + "                         TSEmp.ParentEmployeeCode = upperEmp.EmpID LEFT OUTER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                + "                         INNER JOIN SalePaymentPeriod spp ON spp.RefNo = Cont.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = ?"
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) "
//                + "                         AND (Cont.Status = ?)";
                + "                         AND (Cont.Status IN ('NORMAL'))"
                + "                         AND (Cont.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE (StatusName = 'COMPLETED'))) ";
        ret = executeQueryList(sql, new String[]{valueOf(PaymentComplete), organizationCode, saleTeamCode}, ContractInfo.class);    // , Status
        return ret;
    }

    /////////////////////////////////////////////// [END] Fixed - [BHPROJ-0024-418] ////////////////////////////////////////////////////////

    public ContractInfo getContractBySerialNoForCredit(String organizationCode, String productSerialNumber, String status) {
        final String sql =
                /*** [START] :: Fixed - [BHPROJ-0025-760] ***/
                //"SELECT DISTINCT Contract.* "
                " SELECT Contract.[RefNo],Contract.[CONTNO],Contract.[CustomerID],Contract.[OrganizationCode],Contract.[STATUS],Contract.[StatusCode],Contract.[SALES],Contract.[TotalPrice],Contract.[EFFDATE],Contract.[HasTradeIn]"
                + "     ,Contract.[TradeInProductCode],Contract.[TradeInBrandCode],Contract.[TradeInProductModel],Contract.[TradeInDiscount]"
                + "     ,Contract.[PreSaleSaleCode],Contract.[PreSaleEmployeeCode],Contract.[PreSaleTeamCode],Contract.[SaleCode],Contract.[SaleEmployeeCode],Contract.[SaleTeamCode] "
                + "     ,Contract.[InstallerSaleCode],Contract.[InstallerEmployeeCode],Contract.[InstallerTeamCode],Contract.[InstallDate],ifnull(Contract.[ProductSerialNumber],'') AS ProductSerialNumber,Contract.[ProductID]"
                + "     ,Contract.[SaleEmployeeLevelPath],Contract.[MODE],Contract.[FortnightID],Contract.[ProblemID],Contract.[svcontno],Contract.[isActive],Contract.[MODEL]"
                + "     ,Contract.[fromrefno],Contract.[fromcontno],Contract.[todate],Contract.[tocontno],Contract.[torefno],Contract.[CreateDate],Contract.[CreateBy],Contract.[LastUpdateDate],Contract.[LastUpdateBy],Contract.[SyncedDate]"
                + "     ,Contract.[PreSaleEmployeeLevelPath],Contract.[InstallerEmployeeLevelPath],Contract.[PreSaleEmployeeName],Contract.[EmployeeHistoryID],Contract.[SaleSubTeamCode],Contract.[TradeInReturnFlag], Contract.[IsReadyForSaleAudit], Contract.[ContractReferenceNo], Contract.[IsMigrate]"
                /*** [END] :: Fixed - [BHPROJ-0025-760] ***/

                + " FROM Contract "
                + " WHERE (Contract.isActive = 1) AND (Contract.OrganizationCode = ?) AND (Contract.ProductSerialNumber = ?) AND (Contract.STATUS IN (" + status + "))";
        return executeQueryObject(sql, new String[]{organizationCode, productSerialNumber}, ContractInfo.class);
    }

   /*
    public List<ContractInfo> getContractByStatusImpoundPro(String organizationCode, String saleTeamCode, String status) {
        final String sql = "SELECT distinct Cont.*,"
                + " 			Cust.PrefixName || Cust.CustomerName AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                + " 			 ContST.StatusName"
                + " FROM            Contract AS Cont LEFT OUTER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         Team ON Cont.OrganizationCode = Team.OrganizationCode AND Cont.SaleTeamCode = Team.Code LEFT OUTER JOIN"
                + "                         EmployeeDetail AS TSEmp ON Cont.OrganizationCode = TSEmp.OrganizationCode AND Cont.SaleTeamCode = TSEmp.TeamCode AND"
                + "                         Cont.SaleEmployeeCode = TSEmp.EmployeeCode LEFT OUTER JOIN"
                + "                         Employee AS upperEmp ON TSEmp.OrganizationCode = upperEmp.OrganizationCode AND"
                + "                         TSEmp.ParentEmployeeCode = upperEmp.EmpID LEFT OUTER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode LEFT OUTER JOIN"
                + "							ImpoundProduct AS ImpPro ON Cont.RefNo = ImpPro.RefNo"
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode = ?) AND (Cont.Status = ?)"
                + "            AND (ImpPro.Status <> 'COMPLETED')";
        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode, status}, ContractInfo.class);
    }

    public List<ContractInfo> getContractBySerialNoList(String organizationCode, String productSerialNumber, String status) {
        final String sql = "SELECT Cont.*,"
                + " 			Cust.PrefixName || Cust.CustomerName AS CustomerFullName, Cust.CompanyName, Cust.IDCard,"
                + " 			 Prod.ProductName, Sale.FirstName || '  ' || Sale.LastName AS SaleEmployeeName,"
                + " 			 upperEmp.FirstName || '  ' || upperEmp.LastName AS upperEmployeeName"
                + " FROM            Contract AS Cont INNER JOIN"
                + "                         DebtorCustomer AS Cust ON Cont.OrganizationCode = Cust.OrganizationCode AND Cont.CustomerID = Cust.CustomerID INNER JOIN"
                + "                         Product AS Prod ON Cont.OrganizationCode = Prod.OrganizationCode AND Cont.ProductID = Prod.ProductID INNER JOIN"
                + "                         Employee AS Sale ON Cont.OrganizationCode = Sale.OrganizationCode AND Cont.SaleEmployeeCode = Sale.EmpID INNER JOIN"
                + "                         Team ON Cont.OrganizationCode = Team.OrganizationCode AND Cont.SaleTeamCode = Team.Code LEFT OUTER JOIN"
                + "                         EmployeeDetail AS TSEmp ON Cont.OrganizationCode = TSEmp.OrganizationCode AND Cont.SaleTeamCode = TSEmp.TeamCode AND"
                + "                         Cont.SaleEmployeeCode = TSEmp.EmployeeCode LEFT OUTER JOIN"
                + "                         Employee AS upperEmp ON TSEmp.OrganizationCode = upperEmp.OrganizationCode AND"
                + "                         TSEmp.ParentEmployeeCode = upperEmp.EmpID LEFT OUTER JOIN"
                + "                         ContractStatus AS ContST ON Cont.StatusCode = ContST.StatusCode"
                + " WHERE     (Cont.isActive = 1) AND (Cont.OrganizationCode = ?) AND (Cont.ProductSerialNumber = ?) AND (Cont.Status = ?)";
        return executeQueryList(sql, new String[]{organizationCode, productSerialNumber, status}, ContractInfo.class);
    }
    */

    public ContractInfo getContractByProductSerialNumber(String organizationCode, String productSerialNumber) {
        final String sql =  "SELECT * FROM Contract WHERE OrganizationCode = ? AND ProductSerialNumber = ? ";
        return executeQueryObject(sql, new String[]{organizationCode, productSerialNumber}, ContractInfo.class);
    }

    public void updateContractForGCM(boolean isActive, String organizationCode, String refNo) {
        String sql = "UPDATE Contract SET isActive = ? WHERE OrganizationCode = ? AND RefNo = ? ";
        executeNonQuery(sql, new String[]{valueOf(isActive), organizationCode, refNo});
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteContractAll() {
        String sql = "delete from Contract";
        executeNonQuery(sql, null);
    }
    public void deleteContractByRefNo(String refNo) {
        String sql = "DELETE FROM Contract WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[]{refNo});
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /*** [START] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/
    public void updateContractForSortOrderDefaultForCredit(String RefNo, String OrganizationCode, String PAYDAY, Date LastUpdateDate, String LastUpdateBy) {
        String sql = "UPDATE [Contract] " +
                "   SET " +
                "     PAYDAY = ?, " +
                "     LastUpdateDate = ?, " +
                "     LastUpdateBy = ? " +
                "   WHERE RefNo = ? " +
                "       AND OrganizationCode = ? ";
        executeNonQuery(sql, new String[]{PAYDAY, valueOf(LastUpdateDate), LastUpdateBy, RefNo, OrganizationCode});
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/



    public void doVoidContract(String RefNo, String ContractNo) {
        String sql  = "Update [Contract] set STATUS = 'T', isActive = 0 where RefNo = ? and CONTNO = ?";
        executeNonQuery(sql, new String[]{RefNo, ContractNo});
    }

    public ContractInfo getContractByRefNo(String RefNo) {
        String sql =  "SELECT * FROM Contract WHERE RefNo = ?";
        return executeQueryObject(sql, new String[]{RefNo}, ContractInfo.class);
    }
}
