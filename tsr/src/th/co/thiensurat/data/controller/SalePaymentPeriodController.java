package th.co.thiensurat.data.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodPaymentInfo;

public class SalePaymentPeriodController extends BaseController {

    private static final String QUERY_SALE_PAYMENT_PERIOD_CUSTOMER_LIST =
            "select distinct spp.SalePaymentPeriodID, spp.RefNo, spp.PaymentPeriodNumber, spp.PaymentAmount, spp.Discount, spp.NetAmount,"
                    + " spp.PaymentComplete, spp.PaymentDueDate, spp.PaymentAppointmentDate, spp.CreateDate, spp.CreateBy, spp.LastUpdateDate, spp.LastUpdateBy, spp.CloseAccountDiscountAmount, "
                    + "    c.CONTNO, c.OrganizationCode, c.InstallDate, c.SaleTeamCode, c.CustomerID, c.ProductSerialNumber,"
                    + "	   f.FortnightNumber, f.Year as FortnightYear,"

                    /*
                    // [31-03-2015] Comment By Parada for solve issue of occur duplicate record in list of customer of FirstPayment
					 *
                    + "    nf.FortnightNumber as NextFortnightNumber, nf.Year as NextFortnightYear,"
                     * 
                    */

                    + "    dc.IDCard, dc.PrefixName || IFNULL(dc.CustomerName, '') AS CustomerFullName, dc.CompanyName,"
                    + "    a.AssignID, a.AssigneeEmpID, a.AssigneeTeamCode,"
                    + "    e.FirstName || ' ' || e.LastName AS AssigneeEmpName,"
                    + "    ifnull(sumperiodpayamt.SummaryPaymentAmount, 0) AS SummaryPaymentAmount,"
                    + "    ifnull(spp.NetAmount, 0)- ifnull(sumperiodpayamt.SummaryPaymentAmount, 0) AS OutstandingAmount,"
                    + "    ifnull(period.CountPostpone, 0) AS CountPostpone"
                    + " from Contract as c inner join DebtorCustomer as dc"
                    + " on c.CustomerID=dc.CustomerID inner join SalePaymentPeriod as spp"
                    + " on c.RefNo=spp.RefNo left outer join Assign as a"
                    + " on spp.SalePaymentPeriodID=a.ReferenceID and a.TaskType='SalePaymentPeriod' left outer join Employee as e"
                    + " on a.AssigneeEmpID=e.EmpID  left outer join Fortnight as f"
                    + " on c.FortnightID=f.FortnightID  left outer join fortnight nf"
                    + " on (f.year = nf.year - 1 and f.fortnightnumber - (f.fortnightnumber - 1) = nf.fortnightnumber) or (f.year = nf.year and f.fortnightnumber+1 = nf.fortnightnumber) left outer join"
                    + " (select SalePaymentPeriodID, ifnull(sum(Amount),0) as SummaryPaymentAmount"
                    + "   from SalePaymentPeriodPayment"
                    + "   group by SalePaymentPeriodID"
                    + " ) as sumperiodpayamt"
                    + " on spp.SalePaymentPeriodID=sumperiodpayamt.SalePaymentPeriodID left outer join"
                    + " (select SalePaymentPeriodID, count(SalePaymentPeriodID) as CountPostpone"
                    + "  from PaymentAppointment"
                    + " group by SalePaymentPeriodID"
                    + " ) as period"
                    + " on spp.SalePaymentPeriodID=period.SalePaymentPeriodID"
                    + " where c.isActive='1'"
                    + " and spp.PaymentPeriodNumber='1'"
                    + " and ifnull(spp.NetAmount, 0)- ifnull(sumperiodpayamt.SummaryPaymentAmount, 0) > 0"
                    + " and spp.PaymentComplete='0'"
                    + " AND c.STATUS = 'NORMAL' AND c.StatusCode = (Select StatusCode FROM ContractStatus WHERE StatusName = 'COMPLETED')"
                    + " and c.OrganizationCode=?"
                    + " AND (c.SaleTeamCode = ?)";

    private static final String QUERY_SALE_PAYMENT_PERIOD_BY_REFNO = "SELECT SPP.SalePaymentPeriodID, SPP.RefNo, SPP.PaymentPeriodNumber, "
            + "                 (SPP.PaymentAmount - (SELECT IFNULL(SUM(SPPP.Amount), 0) FROM SalePaymentPeriodPayment AS SPPP WHERE SPPP.SalePaymentPeriodID = SPP.SalePaymentPeriodID)) AS PaymentAmount, "
            + "                 SPP.Discount, SPP.NetAmount,"
            + "    				SPP.PaymentComplete, SPP.PaymentDueDate, SPP.PaymentAppointmentDate, SPP.CreateDate, SPP.CreateBy, SPP.LastUpdateDate, SPP.LastUpdateBy, SPP.CloseAccountDiscountAmount "
            + "    				FROM SalePaymentPeriod AS SPP" + "    				WHERE (RefNo = ?) " + "    				ORDER BY SPP.PaymentPeriodNumber";

    private static final String QUERY_SALE_PAYMENT_PERIOD_BY_REFNO_CREDIT = "SELECT SPP.SalePaymentPeriodID, SPP.RefNo, SPP.PaymentPeriodNumber, "
            + "                 (SPP.PaymentAmount - (SELECT IFNULL(SUM(SPPP.Amount), 0) FROM SalePaymentPeriodPayment AS SPPP WHERE SPPP.SalePaymentPeriodID = SPP.SalePaymentPeriodID)) AS PaymentAmount, "
            + "                 SPP.Discount, SPP.NetAmount,"
            + "    				SPP.PaymentComplete, SPP.PaymentDueDate, SPP.PaymentAppointmentDate, SPP.CreateDate, SPP.CreateBy, SPP.LastUpdateDate, SPP.LastUpdateBy, SPP.CloseAccountDiscountAmount "
            + "    				FROM SalePaymentPeriod AS SPP" + "    				WHERE (RefNo = ?) AND spp.PaymentComplete = 0 " + "    				ORDER BY SPP.PaymentPeriodNumber";

    public List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByContractTeam(String organizationCode, String saleTeamCode) {
        List<SalePaymentPeriodInfo> ret = null;
        //String sql = QUERY_SALE_PAYMENT_PERIOD_CUSTOMER_LIST + "AND c.STATUS = 'NORMAL' AND (c.SaleTeamCode = ?)" + " order by c.InstallDate";
        String sql = QUERY_SALE_PAYMENT_PERIOD_CUSTOMER_LIST + " order by c.InstallDate";

        ret = executeQueryList(sql, new String[]{organizationCode, saleTeamCode}, SalePaymentPeriodInfo.class);
        return ret;
    }

    public List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByAssigneeEmpID(String organizationCode, String saleTeamCode, String assigneeEmpID) {

        List<SalePaymentPeriodInfo> ret = null;
        String sql = QUERY_SALE_PAYMENT_PERIOD_CUSTOMER_LIST;

        if (assigneeEmpID == null || assigneeEmpID.isEmpty()) {
            sql += " and a.AssigneeEmpID is null or a.AssigneeEmpID = ''"
                    + " order by c.InstallDate";
            // sql += " and ifnull([Assign].[AssigneeEmpID], '') = ''";
            ret = executeQueryList(sql, new String[]{organizationCode, saleTeamCode}, SalePaymentPeriodInfo.class);
        } else {
            sql += " and a.AssigneeEmpID = ?"
                    + " order by c.InstallDate";
            ret = executeQueryList(sql, new String[]{organizationCode, saleTeamCode, assigneeEmpID}, SalePaymentPeriodInfo.class);
        }
        return ret;
    }

    public List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByContractTeamByAppointmentDate(String organizationCode, String assigneeTeamCode,
                                                                                               Date paymentAppointmentDate) {

        // +
        // " AND (strftime('%Y-%m-%d', SalePaymentPeriod.PaymentAppointmentDate) =?)";
        String sql = QUERY_SALE_PAYMENT_PERIOD_CUSTOMER_LIST + " AND (strftime('%Y-%m-%d', spp.PaymentAppointmentDate) =?)";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String paymentAppointmentDateString = sdf.format(paymentAppointmentDate);

        return executeQueryList(sql, new String[]{organizationCode, assigneeTeamCode, paymentAppointmentDateString}, SalePaymentPeriodInfo.class);
    }

    public List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByContractTeamGroupByAssignee(String organizationCode, String saleTeamCode) {
//		String sql = "SELECT     Assign.AssigneeEmpID, CASE WHEN Assign.AssigneeEmpID is null THEN 'ไม่มีกลุ่ม' ELSE Employee.FirstName || ' ' || Employee.LastName END as AssigneeEmpName, COUNT(*) AS AssigneeCount"
//				+ " FROM      SalePaymentPeriod INNER JOIN "
//				+ " 						Contract ON Contract.RefNo = SalePaymentPeriod.RefNo INNER JOIN"
//				+ "    						DebtorCustomer ON Contract.CustomerID = DebtorCustomer.CustomerID LEFT OUTER JOIN"
//				+ "    						Assign ON SalePaymentPeriod.SalePaymentPeriodID = Assign.ReferenceID AND Assign.TaskType = 'SalePaymentPeriod' LEFT OUTER JOIN"
//				+ " 						Employee ON Assign.AssigneeEmpID = Employee.EmpID LEFT OUTER JOIN"
//				+ "        						(SELECT        SalePaymentPeriod_1.SalePaymentPeriodID, ifnull(SUM(SalePaymentPeriodPayment.Amount), 0) AS SummaryPaymentAmount"
//				+ "          						FROM            SalePaymentPeriod AS SalePaymentPeriod_1 INNER JOIN"
//				+ "                                    SalePaymentPeriodPayment ON"
//				+ "                                    SalePaymentPeriod_1.SalePaymentPeriodID = SalePaymentPeriodPayment.SalePaymentPeriodID INNER JOIN"
//				+ "                                    Payment ON SalePaymentPeriodPayment.PaymentID = Payment.PaymentID"
//				+ "          						GROUP BY SalePaymentPeriod_1.SalePaymentPeriodID) AS SumPeriodPayAmt ON"
//				+ "    						SalePaymentPeriod.SalePaymentPeriodID = SumPeriodPayAmt.SalePaymentPeriodID LEFT OUTER JOIN"
//				+ "        						(SELECT        SalePaymentPeriodID, COUNT(SalePaymentPeriodID) AS CountPostpone"
//				+ "          						FROM            PaymentAppointment"
//				+ "          						GROUP BY SalePaymentPeriodID) AS Period ON SalePaymentPeriod.SalePaymentPeriodID = Period.SalePaymentPeriodID"
//				+ " WHERE        (Contract.isActive = 1) AND (SalePaymentPeriod.PaymentPeriodNumber = 1) AND (SalePaymentPeriod.PaymentComplete = 0) "				
//				+ " AND (Contract.OrganizationCode = ?) AND (Contract.SaleTeamCode = ?)"
//				+ " GROUP BY Assign.AssigneeEmpID, Employee.FirstName, Employee.LastName";

        String sql = "select a.AssigneeEmpID, CASE WHEN a.AssigneeEmpID is null THEN 'ไม่มีกลุ่ม' ELSE e.FirstName || ' ' || e.LastName END as AssigneeEmpName, COUNT(*) AS AssigneeCount"
                + " from Contract as c inner join DebtorCustomer as dc"
                + " on c.CustomerID=dc.CustomerID inner join SalePaymentPeriod as spp"
                + " on c.RefNo=spp.RefNo left outer join Assign as a"
                + " on spp.SalePaymentPeriodID=a.ReferenceID and a.TaskType='SalePaymentPeriod' left outer join Employee as e"
                + " on a.AssigneeEmpID=e.EmpID  left outer join Fortnight as f"
                + " on c.FortnightID=f.FortnightID left outer join"
                + " (select SalePaymentPeriodID, ifnull(sum(Amount),0) as SummaryPaymentAmount"
                + "   from SalePaymentPeriodPayment"
                + "   group by SalePaymentPeriodID"
                + " ) as sumperiodpayamt"
                + " on spp.SalePaymentPeriodID=sumperiodpayamt.SalePaymentPeriodID left outer join"
                + " (select SalePaymentPeriodID, count(SalePaymentPeriodID) as CountPostpone"
                + "  from PaymentAppointment"
                + " group by SalePaymentPeriodID"
                + " ) as period"
                + " on spp.SalePaymentPeriodID=period.SalePaymentPeriodID"
                + " where c.isActive='1'"
                + " and spp.PaymentPeriodNumber='1'"
                + " and ifnull(spp.NetAmount, 0)- ifnull(sumperiodpayamt.SummaryPaymentAmount, 0) > 0"
                + " and spp.PaymentComplete='0'"
                + " AND c.STATUS = 'NORMAL' AND c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE StatusName = 'COMPLETED')"
                + " and c.OrganizationCode=?"
                + " AND (c.SaleTeamCode = ?)"
                + " group by a.AssigneeEmpID, e.FirstName, e.LastName";

        return executeQueryList(sql, new String[]{organizationCode, saleTeamCode}, SalePaymentPeriodInfo.class);
    }

    public List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByContractTeamGroupByAppointmentDate(String organizationCode, String saleTeamCode, String empID) {
//		String sql = "SELECT     datetime(date(SalePaymentPeriod.PaymentAppointmentDate)) AS PaymentAppointmentDate, COUNT(*) AS PaymentAppointmentDateCount"
//				+ " FROM      SalePaymentPeriod INNER JOIN "
//				+ " 						Contract ON Contract.RefNo = SalePaymentPeriod.RefNo INNER JOIN"
//				+ "    						DebtorCustomer ON Contract.CustomerID = DebtorCustomer.CustomerID LEFT OUTER JOIN"
//				+ "    						Assign ON SalePaymentPeriod.SalePaymentPeriodID = Assign.ReferenceID AND Assign.TaskType = 'SalePaymentPeriod' LEFT OUTER JOIN"
//				+ " 						Employee ON Assign.AssigneeEmpID = Employee.EmpID LEFT OUTER JOIN"
//				+ "        						(SELECT        SalePaymentPeriod_1.SalePaymentPeriodID, ifnull(SUM(SalePaymentPeriodPayment.Amount), 0) AS SummaryPaymentAmount"
//				+ "          						FROM            SalePaymentPeriod AS SalePaymentPeriod_1 INNER JOIN"
//				+ "                                    SalePaymentPeriodPayment ON"
//				+ "                                    SalePaymentPeriod_1.SalePaymentPeriodID = SalePaymentPeriodPayment.SalePaymentPeriodID INNER JOIN"
//				+ "                                    Payment ON SalePaymentPeriodPayment.PaymentID = Payment.PaymentID"
//				+ "          						GROUP BY SalePaymentPeriod_1.SalePaymentPeriodID) AS SumPeriodPayAmt ON"
//				+ "    						SalePaymentPeriod.SalePaymentPeriodID = SumPeriodPayAmt.SalePaymentPeriodID LEFT OUTER JOIN"
//				+ "        						(SELECT        SalePaymentPeriodID, COUNT(SalePaymentPeriodID) AS CountPostpone"
//				+ "          						FROM            PaymentAppointment"
//				+ "          						GROUP BY SalePaymentPeriodID) AS Period ON SalePaymentPeriod.SalePaymentPeriodID = Period.SalePaymentPeriodID"
//				+ " WHERE        (Contract.isActive = 1) AND (SalePaymentPeriod.PaymentPeriodNumber = 1) AND (SalePaymentPeriod.PaymentComplete = 0) "
//				+ " AND (Contract.OrganizationCode = ?) AND (Contract.SaleTeamCode = ?)" 
//				+ " GROUP BY datetime(date(SalePaymentPeriod.PaymentAppointmentDate))";

        String sql = "select datetime(date(spp.PaymentAppointmentDate)) AS PaymentAppointmentDate, COUNT(*) AS PaymentAppointmentDateCount"
                + " from Contract as c inner join DebtorCustomer as dc"
                + " on c.CustomerID=dc.CustomerID inner join SalePaymentPeriod as spp"
                + " on c.RefNo=spp.RefNo left outer join Assign as a"
                + " on spp.SalePaymentPeriodID=a.ReferenceID and a.TaskType='SalePaymentPeriod' left outer join Employee as e"
                + " on a.AssigneeEmpID=e.EmpID  left outer join Fortnight as f"
                + " on c.FortnightID=f.FortnightID left outer join"
                + " (select SalePaymentPeriodID, ifnull(sum(Amount),0) as SummaryPaymentAmount"
                + "   from SalePaymentPeriodPayment"
                + "   group by SalePaymentPeriodID"
                + " ) as sumperiodpayamt"
                + " on spp.SalePaymentPeriodID=sumperiodpayamt.SalePaymentPeriodID left outer join"
                + " (select SalePaymentPeriodID, count(SalePaymentPeriodID) as CountPostpone"
                + "  from PaymentAppointment"
                + " group by SalePaymentPeriodID"
                + " ) as period"
                + " on spp.SalePaymentPeriodID=period.SalePaymentPeriodID"
                + " where c.isActive='1'"
                + " and spp.PaymentPeriodNumber='1'"
                + " and ifnull(spp.NetAmount, 0)- ifnull(sumperiodpayamt.SummaryPaymentAmount, 0) > 0"
                + " and spp.PaymentComplete='0'"
                + " AND c.STATUS = 'NORMAL' AND c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE StatusName = 'COMPLETED')"
                + " and c.OrganizationCode=?"
                + " AND (c.SaleTeamCode = ?)";

        List<String> args = new ArrayList<String>();
        args.add(organizationCode);
        args.add(saleTeamCode);

        if (empID != null) {
            sql += " AND a.AssigneeEmpID = ? ";
            args.add(empID);
        }
        sql += " group by datetime(date(spp.PaymentAppointmentDate))";

        return executeQueryList(sql, args.toArray(new String[args.size()]), SalePaymentPeriodInfo.class);
    }

    public List<SalePaymentPeriodInfo> getNextPaymentCustomerList(String tripID) {
        final String QUERY_NEXT_PAYMENT_GET_CUSTOMERS = "SELECT NextPayment.*, Contract.CONTNO CONTNO, "
                + "CASE Customer.CustomerType WHEN 0 THEN IFNULL(Customer.PrefixName, '') || CustomerName WHEN 1 THEN CompanyName END CustomerFullName "
                + "FROM SalePaymentPeriod NextPayment INNER JOIN (" + "SELECT RefNo, MAX(PaymentPeriodNumber) PaymentPeriodNumber "
                + "FROM SalePaymentPeriod INNER JOIN Trip ON SalePaymentPeriod.TripID = Trip.TripID "
                + "WHERE (SalePaymentPeriod.TripID = ? OR (Trip.EndDate < (SELECT StartDate FROM Trip WHERE TripID = ?)))"
                + "GROUP BY SalePaymentPeriod.RefNo) Payment "
                + "ON NextPayment.RefNo = Payment.RefNo AND NextPayment.PaymentPeriodNumber = Payment.PaymentPeriodNumber " + "INNER JOIN "
                + "(SELECT RefNo FROM SalePaymentPeriod WHERE PaymentPeriodNumber = 1 AND PaymentComplete = 1) FirstPayment "
                + "ON NextPayment.RefNo = FirstPayment.RefNo " + "INNER JOIN Contract ON NextPayment.RefNo = Contract.RefNo "
                + "INNER JOIN DebtorCustomer Customer ON Contract.CustomerID = Customer.CustomerID " + "WHERE Contract.STATUS = 'N'";
        List<SalePaymentPeriodInfo> customers = executeQueryList(QUERY_NEXT_PAYMENT_GET_CUSTOMERS, new String[]{tripID, tripID}, SalePaymentPeriodInfo.class);
        // final String QUERY_NEXT_PAYMENT_GET_CUSTOMERS =
        // "SELECT NextPayment.* FROM SalePaymentPeriod NextPayment ";
        // List<SalePaymentPeriodInfo> customers =
        // executeQueryList(QUERY_NEXT_PAYMENT_GET_CUSTOMERS, null,
        // SalePaymentPeriodInfo.class);

        return customers;
    }

    public List<SalePaymentPeriodInfo> getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(String refNo) {
        String sql = "SELECT * " +
                "FROM SalePaymentPeriod WHERE (RefNo = ?) " +
                "ORDER BY PaymentPeriodNumber ASC";
        return executeQueryList(sql, new String[]{refNo}, SalePaymentPeriodInfo.class);
    }


    public List<SalePaymentPeriodInfo> getSalePaymentPeriodByRefNo(String refNo) {
        String sql = QUERY_SALE_PAYMENT_PERIOD_BY_REFNO;
        return executeQueryList(sql, new String[]{refNo}, SalePaymentPeriodInfo.class);
    }


    public List<SalePaymentPeriodInfo> getSalePaymentPeriodByRefNoComplete(String refNo) {
        String sql = QUERY_SALE_PAYMENT_PERIOD_BY_REFNO_CREDIT;
        return executeQueryList(sql, new String[]{refNo}, SalePaymentPeriodInfo.class);
    }

    public void addSalePaymentPeriod(SalePaymentPeriodInfo info) {
        String sql = "INSERT INTO SalePaymentPeriod (SalePaymentPeriodID, RefNo, PaymentPeriodNumber, PaymentAmount, Discount, NetAmount, PaymentComplete, PaymentDueDate, PaymentAppointmentDate, TripID, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate, CloseAccountDiscountAmount)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[]{info.SalePaymentPeriodID, info.RefNo, valueOf(info.PaymentPeriodNumber), valueOf(info.PaymentAmount),
                valueOf(info.Discount), valueOf(info.NetAmount), valueOf(info.PaymentComplete), valueOf(info.PaymentDueDate),
                valueOf(info.PaymentAppointmentDate), info.TripID, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy,
                valueOf(info.SyncedDate), valueOf(info.CloseAccountDiscountAmount)});
    }

    public void addSalePaymentPeriodList(List<SalePaymentPeriodInfo> infoList) {
        for (int i = 0; i < infoList.size(); i++) {
            addSalePaymentPeriod(infoList.get(i));
        }
    }

    // GetSumCurrentSalePaymentPeriodByAssigneeEmpIDGroupByAppointmentDate
    public List<SalePaymentPeriodInfo> getSummaryFirstPayment(String organizationCode, String teamCode, String assigneeEmpID) {
        List<SalePaymentPeriodInfo> ret = null;
        String sql = "select datetime(date(spp.PaymentAppointmentDate)) as PaymentAppointmentDate, count(*) as PaymentAppointmentDateCount, sum(spp.NetAmount) as SummaryNetAmount"
                + " from SalePaymentPeriod as spp inner join Contract as c"
                + " on spp.RefNo=c.RefNo inner join DebtorCustomer AS cust"
                + " on c.OrganizationCode = cust.OrganizationCode and c.CustomerID = cust.CustomerID left outer join Assign as a"
                + " on spp.RefNo=a.RefNo and a.TaskType='SalePaymentPeriod' and spp.SalePaymentPeriodID=a.ReferenceID"
                + " where (c.isActive = 1 OR (c.isActive = 0 AND c.torefno IS NULL))" +
                "   and spp.PaymentPeriodNumber = '1'"
                + " and c.OrganizationCode = ?"
                + " and a.AssigneeTeamCode = ?";

        ArrayList<String> args = new ArrayList<>();
        args.add(organizationCode);
        args.add(teamCode);

        if (assigneeEmpID != null) {
            sql += " and a.AssigneeEmpID = ?";
            args.add(assigneeEmpID);
        }

        sql += " group by datetime(date(spp.PaymentAppointmentDate))";

        // + " and spp.PaymentComplete = '0'"
        ret = executeQueryList(sql, args.toArray(new String[args.size()]), SalePaymentPeriodInfo.class);

        return ret;
    }

    // GetSumCurrentPaymentByEmpIDGroupByPaymentDate
    public List<SalePaymentPeriodInfo> getSummaryFirstPaymentComplete(String organizationCode, String teamCode, String empID) {

//        String sql = "SELECT DATETIME(DATE(spp.PaymentAppointmentDate)) AS PaymentAppointmentDate" +
//                ", COUNT(*) AS PaymentAppointmentDateCount, SUM(sppp.FirstPaymentAmount) AS SummaryNetAmount" +
//                " FROM SalePaymentPeriod spp " +
//                " INNER JOIN (" +
//                "   SELECT SUM(Amount) AS FirstPaymentAmount, SalePaymentPeriodID" +
//                "   FROM SalePaymentPeriodPayment" +
//                "   GROUP BY SalePaymentPeriodID" +
//                " ) sppp ON sppp.SalePaymentPeriodID = spp.SalePaymentPeriodID" +
//                " INNER JOIN Contract c ON spp.RefNo = c.RefNo " +
//                " INNER JOIN DebtorCustomer cust ON c.OrganizationCode = cust.OrganizationCode " +
//                "   AND c.CustomerID = cust.CustomerID " +
//                " LEFT OUTER JOIN Assign as a on spp.RefNo = a.RefNo and a.TaskType = 'SalePaymentPeriod' " +
//                "   AND spp.SalePaymentPeriodID = a.ReferenceID" +
//                " WHERE spp.PaymentPeriodNumber = '1'" +
//                "   AND c.OrganizationCode = ?" +
//                "   AND a.AssigneeTeamCode = ?";

        String sql = "SELECT DATE(p.PayDate) AS PayDate, COUNT(DISTINCT p.RefNo) AS CountRefNo, SUM(sppPay.Amount) AS SumFirstPaymentAmount" +
                " FROM Payment p" +
                " INNER JOIN Contract c ON c.RefNo = p.RefNo" +
                " INNER JOIN SalePaymentPeriodPayment sppPay ON sppPay.PaymentID = p.PaymentID" +
                " INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = sppPay.SalePaymentPeriodID AND spp.PaymentPeriodNumber = 1" +
                " WHERE (c.isActive = 1 OR (c.isActive = 0 AND c.torefno IS NULL))" +
                "   AND p.OrganizationCode = ?" +
                "   AND p.TeamCode = ?";

        ArrayList<String> args = new ArrayList<>();
        args.add(organizationCode);
        args.add(teamCode);

        if (empID != null) {
            sql += " AND p.EmpID = ?";
            args.add(empID);
        }
        sql += " GROUP BY DATE(p.PayDate)";

        return executeQueryList(sql, args.toArray(new String[args.size()]), SalePaymentPeriodInfo.class);
    }

    public List<SalePaymentPeriodInfo> getFirstPaymentCompleteByPayDate(String organizationCode, String teamCode, String empID, Date paymentDate) {
        String sql = "SELECT c.CONTNO, spp.RefNo" +
                ", spp.PaymentPeriodNumber" +
                ", SUM(sppPay.Amount) AS FirstPaymentAmount" +
                ", dc.PrefixName || ' ' || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName" +
                " FROM Payment p" +
                " INNER JOIN SalePaymentPeriodPayment sppPay ON sppPay.PaymentID = p.PaymentID" +
                " INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = sppPay.SalePaymentPeriodID AND spp.PaymentPeriodNumber = 1" +
                " INNER JOIN Contract c ON c.RefNo = p.RefNo" +
                " INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                " WHERE (c.isActive = 1 OR (c.isActive = 0 AND c.torefno IS NULL))" +
                "   AND p.OrganizationCode = ? " +
                "   AND DATE(p.PayDate) = DATE(?) " +
                "   AND p.TeamCode = ? ";

        ArrayList<String> args = new ArrayList<>();
        args.add(organizationCode);
        args.add(valueOf(paymentDate));
        args.add(teamCode);

        if (empID != null) {
            sql += " and p.EmpID = ?";
            args.add(empID);
        }
        sql += " GROUP BY p.RefNo ORDER BY CustomerFullName, c.CONTNO";

        return executeQueryList(sql, args.toArray(new String[args.size()]), SalePaymentPeriodInfo.class);
    }

    // GetNextSalePaymentPeriodByAssigneeEmpIDGroupByAppointmentDate
    public List<SalePaymentPeriodInfo> getSummaryNextPayment(String organizationCode, String teamCode, String assigneeEmpID) {
        List<SalePaymentPeriodInfo> ret = null;
        String sql = "select datetime(date(spp.PaymentAppointmentDate)) as PaymentAppointmentDate, count(*) as PaymentAppointmentDateCount,ifnull(sum(spp.NetAmount),0)- ifnull(sum(sumperiodpayamt.SummaryPaymentAmount),0) as SummaryNetAmount"
                + " from SalePaymentPeriod as spp inner join Contract as c"
                + " on spp.RefNo=c.RefNo inner join DebtorCustomer AS cust"
                + " on c.OrganizationCode = cust.OrganizationCode and c.CustomerID = cust.CustomerID left outer join Assign as a"
                + " on spp.RefNo=a.RefNo and a.TaskType='SalePaymentPeriod' and spp.SalePaymentPeriodID=a.ReferenceID left outer join"
                + " (select SalePaymentPeriodID, ifnull(sum(Amount),0) as SummaryPaymentAmount"
                + " from SalePaymentPeriodPayment"
                + " group by SalePaymentPeriodID"
                + " ) as sumperiodpayamt"
                + " on spp.SalePaymentPeriodID=sumperiodpayamt.SalePaymentPeriodID"
                + " where (c.isActive = 1)" +
                "   and spp.PaymentComplete = 0"
                + " and spp.PaymentPeriodNumber = '1'"
                + " and c.OrganizationCode = ?"
                + " and a.AssigneeTeamCode = ?";

        ArrayList<String> args = new ArrayList<>();
        args.add(organizationCode);
        args.add(teamCode);

        if (assigneeEmpID != null) {
            sql += " and a.AssigneeEmpID = ?";
            args.add(assigneeEmpID);
        }
        sql += " group by datetime(date(spp.PaymentAppointmentDate))";

        ret = executeQueryList(sql, args.toArray(new String[args.size()]), SalePaymentPeriodInfo.class);
        return ret;
    }

    public List<SalePaymentPeriodInfo> getNextPayment(String refNo) {
        final String QUERY_SALE_PAYMENT_PERIOD_GETS = "SELECT Period.*, Period.NetAmount - IFNULL(Payment.PayAmount, 0) OutstandingAmount "
                + "FROM SalePaymentPeriod Period "
                + "LEFT OUTER JOIN (SELECT SalePaymentPeriodID, SUM(Amount) PayAmount FROM SalePaymentPeriodPayment GROUP BY SalePaymentPeriodID) Payment ON Period.SalePaymentPeriodID = Payment.SalePaymentPeriodID "
                + "WHERE Period.PaymentComplete = 0 AND Period.RefNo = ? ORDER BY PaymentPeriodNumber";
        return executeQueryList(QUERY_SALE_PAYMENT_PERIOD_GETS, new String[]{refNo}, SalePaymentPeriodInfo.class);
    }

//    public SalePaymentPeriodInfo getSalePaymentPeriodInfo(String RefNo) {
//        final String QUERY_SALEPAYMENTPERIOD_GET_BY_ID = "SELECT * FROM SalePaymentPeriod WHERE RefNo = ?";
//        return executeQueryObject(QUERY_SALEPAYMENTPERIOD_GET_BY_ID, new String[]{RefNo}, SalePaymentPeriodInfo.class);
//    }

    public SalePaymentPeriodInfo getByID(String periodID) {
        final String QUERY_SALEPAYMENTPERIOD_GET_BY_ID = "SELECT * FROM SalePaymentPeriod WHERE SalePaymentPeriodID = ?";
        return executeQueryObject(QUERY_SALEPAYMENTPERIOD_GET_BY_ID, new String[]{periodID}, SalePaymentPeriodInfo.class);
    }

    public void updateSalePaymentPeriod(SalePaymentPeriodInfo info) {
        final String QUERY_UDPATE = "UPDATE SalePaymentPeriod SET RefNo = ?, PaymentPeriodNumber = ?, PaymentAmount = ?, Discount = ?, NetAmount = ?, "
                + "PaymentComplete = ?, PaymentDueDate = ?, PaymentAppointmentDate = ?, TripID = ?, CreateDate = ?, CreateBy = ?, "
                + "LastUpdateDate = ?, LastUpdateBy = ?, SyncedDate = ?, CloseAccountDiscountAmount = ? WHERE SalePaymentPeriodID = ?";
        executeNonQuery(QUERY_UDPATE, new String[]{info.RefNo, valueOf(info.PaymentPeriodNumber), valueOf(info.PaymentAmount), valueOf(info.Discount),
                valueOf(info.NetAmount), valueOf(info.PaymentComplete), valueOf(info.PaymentDueDate), valueOf(info.PaymentAppointmentDate), info.TripID,
                valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate), valueOf(info.CloseAccountDiscountAmount), info.SalePaymentPeriodID});
    }

    public List<SalePaymentPeriodInfo> getPaymentSummary(String refNo, String paymentID) {
        final String QUERY_SALE_PAYMENT_SUMMARY = "SELECT Period.*, Period.NetAmount - IFNULL(Payment.PayAmount, 0) OutstandingAmount, IFNULL(Current.Amount, 0) CurrentPaymentAmount, ReceiptCode, ReceiptDate, ReceiptID "
                + "FROM SalePaymentPeriod Period "
                + "LEFT OUTER JOIN (SELECT SalePaymentPeriodID, SUM(Amount) PayAmount FROM SalePaymentPeriodPayment "
                + "     LEFT OUTER JOIN Payment ON SalePaymentPeriodPayment.PaymentID = Payment.PaymentID  where Payment.PayDate <= (select Payment.PayDate from Payment  where Payment.PaymentID = ?)"
                + "GROUP BY SalePaymentPeriodID) Payment ON Period.SalePaymentPeriodID = Payment.SalePaymentPeriodID "
                + "LEFT OUTER JOIN (SELECT SalePaymentPeriodID, Amount, ReceiptCode, r.DatePayment ReceiptDate, r.ReceiptID FROM SalePaymentPeriodPayment sp INNER JOIN Receipt r ON sp.ReceiptID = r.ReceiptID WHERE sp.PaymentID = ?) Current ON Period.SalePaymentPeriodID = Current.SalePaymentPeriodID "
                + "WHERE Period.RefNo = ? ORDER BY PaymentPeriodNumber";
        return executeQueryList(QUERY_SALE_PAYMENT_SUMMARY, new String[]{paymentID, paymentID, refNo}, SalePaymentPeriodInfo.class);
    }

    public List<SalePaymentPeriodInfo> getPaymentSummaryForCredit(String paymentID) {
        final String QUERY_SALE_PAYMENT_SUMMARY = "SELECT spp.*" +
                ", r.ReceiptCode, r.DatePayment AS ReceiptDate, r.ReceiptID" +
                ", spp.NetAmount - IFNULL(sppSum.PayAmount, 0) OutstandingAmount" +
                ", sppp.Amount AS CurrentPaymentAmount" +
                " FROM Payment p" +
                " INNER JOIN SalePaymentPeriodPayment sppp on sppp.PaymentID = p.PaymentID" +
                " INNER JOIN Receipt r ON r.ReceiptID = sppp.ReceiptID " +
                " INNER JOIN SalePaymentPeriod spp on spp.SalePaymentPeriodID = sppp.SalePaymentPeriodID" +
                " LEFT OUTER JOIN (" +
                "		SELECT SUM(Amount) as PayAmount, SalePaymentPeriodID " +
                "		FROM SalePaymentPeriodPayment " +
                "		GROUP BY SalePaymentPeriodID" +
                ") AS sppSum ON sppSum.SalePaymentPeriodID  = spp.SalePaymentPeriodID" +
                " WHERE p.PaymentID = ?" +
                " ORDER BY spp.PaymentPeriodNumber ASC";
        return executeQueryList(QUERY_SALE_PAYMENT_SUMMARY, new String[]{paymentID}, SalePaymentPeriodInfo.class);
    }

    public List<SalePaymentPeriodInfo> getSalePaymentPeriod() {
        return executeQueryList("select * from SalePaymentPeriod", null, SalePaymentPeriodInfo.class);
    }

    public SalePaymentPeriodInfo getSummaryNetAmount(String refNO) {
//        String sql="select IFNULL(sum(NetAmount),0) as NetAmount"
//                +" from SalePaymentPeriod"
//                +" where refNo=?"
//                +" group by RefNo";
        String sql = "select totalAmount.RefNo, ifnull(totalAmount.NetAmount,0) as NetAmount, ifnull(sumamt.Amount,0) as Amount, ifnull(totalAmount.NetAmount,0) - ifnull(sumamt.Amount,0) as OutstandingAmount"
                + " from "
                + " ( "
                + "	select RefNo, sum(NetAmount)  as NetAmount"
                + "	from SalePaymentPeriod"
                + " where RefNo=?"
                + "	group by RefNo"
                + " ) as totalAmount left outer join"
                + " ( "
                + "	select spp.RefNo, sum(sppp.Amount) as Amount"
                + "	from SalePaymentPeriod as spp inner join"
                + "	SalePaymentPeriodPayment as sppp"
                + "	on sppp.SalePaymentPeriodID=spp.SalePaymentPeriodID"
                + " where RefNo=?"
                + "	group by spp.RefNo"
                + " ) as sumamt"
                + " on sumamt.RefNo=totalAmount.RefNo"
                + " where totalAmount.RefNo=?";

        return executeQueryObject(sql, new String[]{refNO, refNO, refNO}, SalePaymentPeriodInfo.class);
    }

    public SalePaymentPeriodInfo getSalePaymentPeriodInfoByPaymentComplete(String RefNo, String PaymentComplete) {
        SalePaymentPeriodInfo ret = null;
        String sql;
        sql = "SELECT * FROM SalePaymentPeriod WHERE RefNo = ? AND PaymentComplete = ?";
        ret = executeQueryObject(sql, new String[]{RefNo, PaymentComplete}, SalePaymentPeriodInfo.class);
        return ret;
    }

    public SalePaymentPeriodPaymentInfo getSumSalePaymentPeriodPaymentBySalePaymentPeriodID(String SalePaymentPeriodID) {
        String sql = "SELECT SUM(Amount) AS SumAmount FROM SalePaymentPeriodPayment WHERE SalePaymentPeriodID = ?";
        return executeQueryObject(sql, new String[]{SalePaymentPeriodID}, SalePaymentPeriodPaymentInfo.class);
    }

    public SalePaymentPeriodInfo getSummarySalePaymentPeriodByRefNo(String RefNo) {
        String sql = "SELECT IFNULL(SUM(NetAmount), 0) AS SummaryNetAmount FROM SalePaymentPeriod WHERE RefNo = ?";
        return executeQueryObject(sql, new String[]{RefNo}, SalePaymentPeriodInfo.class);
    }

    public SalePaymentPeriodInfo getSalePaymentPeriodInfoByRefNoAndPaymentPeriodNumber(String RefNo, int PaymentPeriodNumber) {
        String sql = "SELECT * FROM SalePaymentPeriod WHERE RefNo = ? AND PaymentPeriodNumber = ?";
        return executeQueryObject(sql, new String[]{RefNo, valueOf(PaymentPeriodNumber)}, SalePaymentPeriodInfo.class);
    }

    /***
     * [START] - Fixed - [Android-เก็บเงินค่างวด] เปลี่ยนเงื่อนไขการนับจำนวนงวดที่ค้าง
     ***/

    public List<SalePaymentPeriodInfo> getSalePaymentPeriodOutStandingByAssignID(String assignID, String RefNo) {
        Date getDueDate = new TripController().getDueDate();

        String sql = "SELECT " +
                " DISTINCT sppOutStanding.* " +
                ", sppOutStanding.NetAmount - IFNULL(sppSum.SumAmount, 0) AS OutstandingAmount" +
                ", CASE WHEN DATE(sppOutStanding.PaymentDueDate) < DATE(?) THEN 1 ELSE 0 END AS OverDue" +
                " FROM Assign a " +
                " INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                " LEFT OUTER JOIN SalePaymentPeriod sppOutStanding ON sppOutStanding.RefNo = a.RefNo " +
//                "       AND (DATE(sppOutStanding.PaymentDueDate) < DATE('now') OR sppOutStanding.SalePaymentPeriodID = a.ReferenceID) " +
                "       AND (DATE(sppOutStanding.PaymentDueDate) < DATE(?) OR sppOutStanding.PaymentPeriodNumber <= spp.PaymentPeriodNumber) " +
                "       AND sppOutStanding.PaymentComplete = 0" +
                " LEFT OUTER JOIN (" +
                "                   SELECT SUM(sppp.Amount) AS SumAmount, spp.SalePaymentPeriodID " +
                "                   FROM SalePaymentPeriod as spp " +
                "                   INNER JOIN SalePaymentPeriodPayment as sppp ON sppp.SalePaymentPeriodID = spp.SalePaymentPeriodID " +
                "                   WHERE spp.RefNo = ? " +
                "                   GROUP BY sppp.SalePaymentPeriodID " +
                "                  ) AS sppSum ON sppSum.SalePaymentPeriodID  = sppOutStanding.SalePaymentPeriodID" +
                " WHERE a.AssignID = ?" +
                " ORDER BY sppOutStanding.PaymentPeriodNumber ASC";
        return executeQueryList(sql, new String[]{valueOf(getDueDate), valueOf(getDueDate), RefNo, assignID}, SalePaymentPeriodInfo.class);
    }

    public List<SalePaymentPeriodInfo> getSalePaymentPeriodOutStandingByRefNo(String refNo) {
        Date getDueDate = new TripController().getDueDate();

        // งวดค้างที่เกิน PaymentDueDate ถึงงวดปัจจุบันที่ถึงกำหนด
        String sql = "SELECT " +
                "      DISTINCT sppOutStanding.* " +
                "      , sppOutStanding.NetAmount - IFNULL(sppSum.SumAmount, 0) AS OutstandingAmount" +
                "      , CASE WHEN DATE(sppOutStanding.PaymentDueDate) < DATE(?) THEN 1 ELSE 0 END AS OverDue" +
                " FROM Contract c " +
                " INNER JOIN (" +
                "       SELECT MIN(PaymentPeriodNumber) AS PaymentPeriodNumber, RefNo" +
                "       FROM SalePaymentPeriod" +
                "       WHERE PaymentComplete = 0 and RefNo = ? " +
                "       GROUP BY RefNo" +
                " ) AS minPeriod ON minPeriod.RefNo = c.RefNo" +
                " LEFT OUTER JOIN SalePaymentPeriod sppOutStanding ON sppOutStanding.RefNo = c.RefNo " +
                "       AND (DATE(sppOutStanding.PaymentDueDate) <= DATE(?) OR DATE(sppOutStanding.PaymentAppointmentDate) <= DATE('now') OR sppOutStanding.PaymentPeriodNumber = minPeriod.PaymentPeriodNumber)" +
                "       AND sppOutStanding.PaymentComplete = 0" +
                " LEFT OUTER JOIN (" +
                "                   SELECT SUM(sppp.Amount) AS SumAmount, spp.SalePaymentPeriodID " +
                "                   FROM SalePaymentPeriod as spp " +
                "                   INNER JOIN SalePaymentPeriodPayment as sppp ON sppp.SalePaymentPeriodID = spp.SalePaymentPeriodID " +
                "                   WHERE spp.RefNo = ? " +
                "                   GROUP BY sppp.SalePaymentPeriodID " +
                "                 ) AS sppSum ON sppSum.SalePaymentPeriodID  = sppOutStanding.SalePaymentPeriodID" +
                " WHERE c.RefNo = ?" +
                " ORDER BY sppOutStanding.PaymentPeriodNumber ASC";
        return executeQueryList(sql, new String[]{valueOf(getDueDate), refNo, valueOf(getDueDate), refNo, refNo}, SalePaymentPeriodInfo.class);
    }

    /***
     * [END] - Fixed - [Android-เก็บเงินค่างวด] เปลี่ยนเงื่อนไขการนับจำนวนงวดที่ค้าง
     ***/

    public SalePaymentPeriodInfo getSalePaymentPeriodByRefNoAndPaymentPeriodNumber(String refNo, int paymentPeriodNumber) {
        String sql = "select * from SalePaymentPeriod " +
                        "where RefNo = ? And PaymentPeriodNumber = ? ";

        return executeQueryObject(sql, new String[]{refNo, valueOf(paymentPeriodNumber)}, SalePaymentPeriodInfo.class);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteSalePaymentPeriodAll() {
        String sql = "DELETE FROM SalePaymentPeriod";
        executeNonQuery(sql, null);
    }

    public void deleteSalePaymentPeriodByRefNo(String refNo) {
        String sql = "DELETE FROM SalePaymentPeriod WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[]{refNo});
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /*** [START] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/
    public List<SalePaymentPeriodInfo> getSalePaymentPeriodByRefNoAndPaymentComplete(String RefNo, int PaymentComplete) {
        String sql = "SELECT * FROM SalePaymentPeriod WHERE RefNo = ? AND PaymentComplete = ?  ORDER BY PaymentPeriodNumber ASC";
        return executeQueryList(sql, new String[]{RefNo, valueOf(PaymentComplete)}, SalePaymentPeriodInfo.class);
    }

    public void updateSalePaymentPeriodForSortOrderDefaultForCredit(SalePaymentPeriodInfo info) {
       /* String sql = "UPDATE [SalePaymentPeriod] " +
                "   SET " +
                "       PaymentDueDate = ?, " +
                "       PaymentAppointmentDate = ?, " +
                "	    LastUpdateDate = ?, " +
                "	    LastUpdateBy = ? " +
                "   WHERE SalePaymentPeriodID = ? ";
        executeNonQuery(sql, new String[]{valueOf(info.PaymentDueDate), valueOf(info.PaymentAppointmentDate), valueOf(info.LastUpdateDate), info.LastUpdateBy, info.SalePaymentPeriodID});*/

        String sql = "UPDATE [SalePaymentPeriod] " +
                "   SET " +
                "       PaymentAppointmentDate = ?, " +
                "	    LastUpdateDate = ?, " +
                "	    LastUpdateBy = ? " +
                "   WHERE SalePaymentPeriodID = ? ";
        executeNonQuery(sql, new String[]{valueOf(info.PaymentAppointmentDate), valueOf(info.LastUpdateDate), info.LastUpdateBy, info.SalePaymentPeriodID});
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/



    public List<SalePaymentPeriodInfo> getSalePaymentPeriodContractCloseAccountForOverdueByRefNo(String RefNo, String OrganizationCode) {
        String strDueDate = valueOf(new TripController().getDueDate());

        String sql = " SELECT spp.* " +
                "       , CASE WHEN DATE(spp.PaymentDueDate) < DATE(?) THEN 1 ELSE 0 END AS OverDue " +
                "       , spp.NetAmount - IFNULL(sppSum.SumAmount, 0) AS OutstandingAmount " +
                "       , IFNULL(packageDetail.CloseDiscountAmount, 0) AS CloseDiscountAmount " +
                "       , findMinPeriod.PaymentPeriodNumber AS MinPaymentPeriodNumberForContractCloseAccount" +
                "       , MinSalePaymentPeriodID.SalePaymentPeriodID AS MinSalePaymentPeriodIDForContractCloseAccount" +
                "       , TotalOutstandingAmountForContractCloseAccount.TotalOutstandingAmount AS TotalOutstandingAmount" +
                "       , c.MODE " +
                " FROM Contract AS c" +
                " INNER JOIN SalePaymentPeriod AS spp ON spp.RefNo = c.RefNo AND spp.PaymentComplete = 0 " +
                " LEFT OUTER JOIN ( " +
                "                   SELECT SUM(sppp.Amount) AS SumAmount, spp.SalePaymentPeriodID " +
                "                   FROM SalePaymentPeriod as spp " +
                "                   INNER JOIN SalePaymentPeriodPayment as sppp ON sppp.SalePaymentPeriodID = spp.SalePaymentPeriodID " +
                "                   WHERE spp.RefNo = ? " +
                "                   GROUP BY sppp.SalePaymentPeriodID " +
                "                 ) AS sppSum ON sppSum.SalePaymentPeriodID  = spp.SalePaymentPeriodID " +
                " LEFT OUTER JOIN ( " +

//                "		SELECT  SUM(IFNULL(SalePaymentPeriod.NetAmount, 0)) - SUM(IFNULL(SalePaymentPeriodPayment.Amount, 0)) AS TotalOutstandingAmount, SalePaymentPeriod.RefNo " +
//                "		FROM SalePaymentPeriod " +
//                "           LEFT OUTER JOIN SalePaymentPeriodPayment ON SalePaymentPeriodPayment.SalePaymentPeriodID = SalePaymentPeriod.SalePaymentPeriodID " +
//                "       WHERE SalePaymentPeriod.RefNo = ? AND SalePaymentPeriod.PaymentComplete = 0 AND DATE(SalePaymentPeriod.PaymentDueDate) >= DATE(?) " +
//                "		GROUP BY SalePaymentPeriod.RefNo " +

                " SELECT  SUM(IFNULL(CloseAccountAmount.NetAmount, 0)) - SUM(IFNULL(CloseAccountAmount.Amount, 0)) AS TotalOutstandingAmount, CloseAccountAmount.RefNo " +
                " FROM (select distinct SalePaymentPeriod.SalePaymentPeriodID, SalePaymentPeriod.RefNo, SalePaymentPeriod.PaymentPeriodNumber, SalePaymentPeriod.NetAmount, SalePaymentPeriodPayment.Amount  from SalePaymentPeriod " +
                " LEFT OUTER JOIN SalePaymentPeriodPayment ON SalePaymentPeriodPayment.SalePaymentPeriodID = SalePaymentPeriod.SalePaymentPeriodID " +
                " And (SalePaymentPeriodPayment.ReceiptID is null or SalePaymentPeriodPayment.ReceiptID not in (Select ReceiptID from Receipt where TotalPayment = 0 and RefNo = SalePaymentPeriod.RefNo)) " +
                " WHERE SalePaymentPeriod.RefNo = ? AND SalePaymentPeriod.PaymentComplete = 0 AND DATE(SalePaymentPeriod.PaymentDueDate) >= DATE(?) " +
   //             " And (SalePaymentPeriodPayment.ReceiptID is null or SalePaymentPeriodPayment.ReceiptID not in (Select ReceiptID from Receipt where TotalPayment = 0)) " +
                " ) as CloseAccountAmount " +
                " group by CloseAccountAmount.RefNo " +




                " ) AS TotalOutstandingAmountForContractCloseAccount ON TotalOutstandingAmountForContractCloseAccount.RefNo  = c.RefNo " +
                " INNER JOIN ( " +
                "       SELECT MIN(PaymentPeriodNumber) AS PaymentPeriodNumber, RefNo " +
                "       FROM SalePaymentPeriod " +
                "       WHERE PaymentComplete = 0 AND RefNo = ? AND DATE(PaymentDueDate) >= DATE(?) " +
                "       GROUP BY RefNo " +
                " ) AS findMinPeriod ON findMinPeriod.RefNo = c.RefNo " +
                " INNER JOIN SalePaymentPeriod AS MinSalePaymentPeriodID ON MinSalePaymentPeriodID.RefNo = findMinPeriod.RefNo " +
                "                                                               AND MinSalePaymentPeriodID.PaymentPeriodNumber = findMinPeriod.PaymentPeriodNumber " +
                " LEFT OUTER JOIN PackagePeriodDetail AS packageDetail ON packageDetail.Model = c.MODEL " +
                "       AND packageDetail.PaymentPeriodNumber = findMinPeriod.PaymentPeriodNumber " +
                "       AND packageDetail.OrganizationCode = c.OrganizationCode " +
                " WHERE c.RefNo = ? AND c.OrganizationCode = ? AND c.IsActive = 1 " +
                " ORDER BY spp.PaymentPeriodNumber ASC ";

        return executeQueryList(sql, new String[]{strDueDate, RefNo, RefNo, strDueDate, RefNo, strDueDate, RefNo, OrganizationCode}, SalePaymentPeriodInfo.class);
    }

}
