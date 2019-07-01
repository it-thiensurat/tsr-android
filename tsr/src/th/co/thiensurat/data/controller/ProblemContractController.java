package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.controller.ImpoundProductController.ImpoundProductStatus;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ProblemContractInfo;
import th.co.thiensurat.data.info.ProductStockInfo;

public class ProblemContractController extends BaseController {
    public enum ProblemContractStatus {
        REQUEST, APPROVED, COMPLETED
    }

    public List<ProblemContractInfo> getProblemContractByProblemCode() {
        List<ProblemContractInfo> ret = null;
        String sql = "SELECT  ProblemContract.*,Contract.CONTNO,Contract.OrganizationCode,Contract.productSerialNumber,Contract.saleTeamCode, DebtorCustomer.prefixName || DebtorCustomer.customerName as CustomerFullName ,"
                + "DebtorCustomer.companyName,DebtorCustomer.iDCard ,SalePaymentPeriod.PaymentPeriodNumber,ifnull(SalePaymentPeriod.PaymentAmount,0) as PaymentAmount FROM ProblemContract INNER JOIN "
                + "Contract on ProblemContract.RefNo=Contract.RefNo LEFT OUTER JOIN "
                + "DebtorCustomer on Contract.CustomerID=DebtorCustomer.CustomerID LEFT OUTER JOIN SalePaymentPeriod on "
                + "ProblemContract.SalePaymentPeriodID=SalePaymentPeriod.SalePaymentPeriodID " +
                "WHERE ProblemContract.Status <> 'COMPLETED'";

        ret = executeQueryList(sql, null, ProblemContractInfo.class);
        return ret;

    }

    public void addProblemContract(ProblemContractInfo info) {
        String sql = "INSERT INTO ProblemContract (ProblemContract,RefNo,CONTNO,SalePaymentPeriodID,PaymentPeriodNumber,ReceivedProblemDate,DataSourceSystem,Status,ProblemCode,ProbemDetail,AssignedEmployee,CreateDate,CreateBy,LastUpdateDate,LastUpdateBy,SyncedDate)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        executeNonQuery(sql, new String[]{info.ProblemContract, info.RefNo, info.CONTNO, info.SalePaymentPeriodID, valueOf(info.PaymentPeriodNumber),
                valueOf(info.ReceivedProblemDate), info.DataSourceSystem, info.Status, info.ProblemCode, info.ProbemDetail, info.AssignedEmployee,
                valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate)});
    }

    public void updateProblemContract(ProblemContractInfo info) {
        String sql = "UPDATE ProblemContract SET " + "RefNo = ?, CONTNO = ?, SalePaymentPeriodID = ?, PaymentPeriodNumber = ?, "
                + "ReceivedProblemDate = ?,DataSourceSystem = ?, Status = ?, " + "ProblemCode = ?,ProbemDetail = ?,AssignedEmployee = ? ,"
                + "CreateDate = ?, CreateBy = ?, LastUpdateDate = ? , " + "LastUpdateBy = ? WHERE ProblemContract = ?";
        executeNonQuery(sql, new String[]{info.RefNo, info.CONTNO, info.SalePaymentPeriodID, valueOf(info.PaymentPeriodNumber), valueOf(info.ReceivedProblemDate),
                info.DataSourceSystem, info.Status, info.ProblemCode, info.ProbemDetail, info.AssignedEmployee, valueOf(info.CreateDate), info.CreateBy,
                valueOf(info.LastUpdateDate), info.LastUpdateBy, info.ProblemContract});
    }

    public ProblemContractInfo getProblemContractByRefNo(String RefNo) {
        ProblemContractInfo ret = null;
        String sql = "SELECT * FROM ProblemContract WHERE RefNo = ?";
        ret = executeQueryObject(sql, new String[]{RefNo}, ProblemContractInfo.class);
        return ret;

    }

}
