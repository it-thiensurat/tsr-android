package th.co.thiensurat.data.controller;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ImpoundProductInfo;

public class ImpoundProductController extends BaseController {

	public enum ImpoundProductStatus {
		REQUEST, APPROVED, COMPLETED, REJECT
	}
	public void addImpoundProduct(ImpoundProductInfo info) {
		String sql = " INSERT INTO ImpoundProduct(ImpoundProductID, OrganizationCode, RefNo, Status, "
				+ "			RequestProblemID, RequestDetail, RequestDate, RequestBy, RequestTeamCode, "
				+ "			ApproveDetail, ApprovedDate, ApprovedBy, "
				+ "			ResultProblemID, ResultDetail, EffectiveDate, EffectiveBy, "
				+ "			ImpoundProductPaperID, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, RequestEmployeeLevelPath, EffectiveEmployeeLevelPath) "
				+ " VALUES (?,?,?,?,?,?,?,?,?, ?,datetime('now'),?,?, ?,datetime('now'),?, ?,datetime('now'),?,datetime('now'),?,?,?)";

                executeNonQuery(sql, new String[]{info.ImpoundProductID, info.OrganizationCode, info.RefNo, info.Status, info.RequestProblemID, info.RequestDetail,
                        valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode, info.ApproveDetail, info.ApprovedBy, info.RequestProblemID, info.ResultDetail,
                        info.EffectiveBy, info.ImpoundProductPaperID, info.CreateBy, info.LastUpdateBy, info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath});

		TSRController.updateRunningNumber(TSRController.DocumentGenType.ImpoundProduct, info.ImpoundProductPaperID, info.EffectiveBy);
	}

    public void addImpoundProductOtherTeam(ImpoundProductInfo info) {
        String sql = " INSERT INTO ImpoundProduct(ImpoundProductID, OrganizationCode, RefNo, Status, "
                + "			RequestProblemID, RequestDetail, RequestDate, RequestBy, RequestTeamCode, "
                + "			CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, RequestEmployeeLevelPath, EffectiveEmployeeLevelPath) "
                + " VALUES (?, ?, ?,?,?,?,?,?,?,datetime('now'),?,datetime('now'),?,?,?)";

        executeNonQuery(sql, new String[]{info.ImpoundProductID, info.OrganizationCode, info.RefNo, info.Status,
				info.RequestProblemID, info.RequestDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode, info.CreateBy,
				info.LastUpdateBy, info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath});
	}

	public void updateImpoundProduct(ImpoundProductInfo info) {
		String sql = "";

		if (info.RequestBy != null && !info.RequestBy.trim().equals("")) {
			sql = "UPDATE ImpoundProduct "
				+ " SET OrganizationCode = ?, RefNo = ?, Status = ?, "
				+ "			RequestProblemID = ?, RequestDetail = ?, RequestDate = ?, RequestBy = ?, RequestTeamCode = ?, "
				+ "			ImpoundProductPaperID = ?, LastUpdateDate = datetime('now'), LastUpdateBy = ?, RequestEmployeeLevelPath = ? "
				+ " WHERE (ImpoundProductID = ?)";
		} else if (info.ApprovedBy != null && !info.ApprovedBy.trim().equals("")) {
			sql = "UPDATE ImpoundProduct SET OrganizationCode = ?, RefNo = ?, Status = ?, "
				+ "			ApproveDetail = ?, ApprovedDate = ?, ApprovedBy = ?, "
				+ "			ImpoundProductPaperID = ?, LastUpdateDate = datetime('now'), LastUpdateBy = ? "
				+ " WHERE (ImpoundProductID = ?)";
		} else if (info.EffectiveBy != null && !info.EffectiveBy.trim().equals("")) {
			sql = "UPDATE ImpoundProduct set OrganizationCode = ?, RefNo = ?, Status = ?, "
				+ "			ResultProblemID = ?, ResultDetail = ?, EffectiveDate = ?, EffectiveBy = ?, "
				+ "			ImpoundProductPaperID = ?, LastUpdateDate = datetime('now'), LastUpdateBy = ?, EffectiveEmployeeLevelPath = ?"
				+ " WHERE (ImpoundProductID = ?)";
		}

		if (info.RequestBy != null && !info.RequestBy.trim().equals("")) {
			executeNonQuery(sql, new String[]{info.OrganizationCode, info.RefNo, info.Status, info.RequestProblemID, info.RequestDetail,
					valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode,
					info.ImpoundProductPaperID, info.LastUpdateBy, info.RequestEmployeeLevelPath, info.ImpoundProductID});
		} else if (info.ApprovedBy != null && !info.ApprovedBy.trim().equals("")) {
			executeNonQuery(sql, new String[] { info.OrganizationCode, info.RefNo, info.Status,
					info.ApproveDetail, valueOf(info.ApprovedDate), info.ApprovedBy,
					info.ImpoundProductPaperID, info.LastUpdateBy, info.ImpoundProductID });
		} else if (info.EffectiveBy != null && !info.EffectiveBy.trim().equals("")) {
			executeNonQuery(sql, new String[] { info.OrganizationCode, info.RefNo, info.Status,
					info.ResultProblemID, info.ResultDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.ImpoundProductPaperID, info.LastUpdateBy,
					info.EffectiveEmployeeLevelPath, info.ImpoundProductID });
		}

		TSRController.updateRunningNumber(TSRController.DocumentGenType.ImpoundProduct, info.ImpoundProductPaperID, info.EffectiveBy);
	}

    public void updateImpoundProductByImpoundProductID(ImpoundProductInfo info) {
        String sql = "UPDATE ImpoundProduct "
				+ " SET Status = ?, ApproveDetail = ?, ApprovedDate = ?, ApprovedBy = ? "
				+ " WHERE (ImpoundProductID = ?)";

            executeNonQuery(sql, new String[]{info.Status,
                    info.ApproveDetail, valueOf(info.ApprovedDate), info.ApprovedBy, info.ImpoundProductID});

    }

	public ImpoundProductInfo getImpoundProductByID(String OrganizationCode, String ImpoundProductID) {
		String sql = "SELECT ImpoundProduct.*, "
				+ "			Contract.ProductSerialNumber,"
				+ "			Contract.RefNo,"
				+ "			Contract.SaleEmployeeCode,"
				+ "			Contract.InstallDate,"
				+ "			DebtorCustomer.PrefixName || IFNULL(DebtorCustomer.CustomerName,'') AS CustomerFullName,"
                + "			DebtorCustomer.CompanyName , "
				+ "			Product.ProductName, Contract.CONTNO, "
				+ "			Contract.SaleTeamCode"
				/*** [START] :: Fixed [BHPROJ-0026-978] :: Display detail of Sale ***/
				+ "			, Contract.SaleCode "
				+ "			, (IFNULL(Employee.FirstName,'') || ' ' || IFNULL(Employee.LastName,'')) AS SaleEmployeeName "
				+ "			, (IFNULL(TeamHeadEmp.FirstName,'') || ' ' || IFNULL(TeamHeadEmp.LastName,'')) AS TeamHeadName "
				/*** [END] :: Fixed [BHPROJ-0026-978] :: Display detail of Sale ***/
				+ " FROM ImpoundProduct "
				+ "			INNER JOIN Contract ON (Contract.OrganizationCode = ImpoundProduct.OrganizationCode) AND (ImpoundProduct.RefNo = Contract.RefNo) "
				+ "			INNER JOIN DebtorCustomer ON (DebtorCustomer.OrganizationCode = Contract.OrganizationCode) AND (DebtorCustomer.CustomerID = Contract.CustomerID) "
				+ "			INNER JOIN Product ON (Product.OrganizationCode = Contract.OrganizationCode) AND (Product.ProductID = Contract.ProductID) "
				/*** [START] :: Fixed [BHPROJ-0026-978] :: Display detail of Sale ***/
				+ "			INNER JOIN Employee ON (Contract.SaleEmployeeCode = Employee.EmpID) "
				+ "			LEFT OUTER JOIN EmployeeDetailHistory AS edh ON (Contract.OrganizationCode = edh.OrganizationCode) "
				+ " 							AND (Contract.SaleEmployeeLevelPath = edh.TreeHistoryID) "
				+ "  							AND (Contract.SaleEmployeeCode = edh.EmployeeCode) AND (edh.PositionCode = 'Sale') "
				+ "                				AND (Contract.SaleTeamCode = edh.TeamCode) AND (Contract.SaleCode = edh.SaleCode) "
				+ "			LEFT OUTER JOIN Employee AS TeamHeadEmp ON (edh.TeamHeadCode = TeamHeadEmp.EmpID) "
				/*** [END] :: Fixed [BHPROJ-0026-978] :: Display detail of Sale ***/
				+ " WHERE (Contract.OrganizationCode = ?) AND (ImpoundProduct.ImpoundProductID = ?)";

		return executeQueryObject(sql, new String[] { OrganizationCode, ImpoundProductID }, ImpoundProductInfo.class);
	}

	public void deleteImpoundProductByID(String OrganizationCode, String ImpoundProductID) {
		executeNonQuery("DELETE FROM ImpoundProduct WHERE (OrganizationCode = ?) AND (ImpoundProductID = ?)", new String[] { OrganizationCode, ImpoundProductID });
	}

    public ImpoundProductInfo getImpoundProductByIDForOtherTeam(String OrganizationCode, String ImpoundProductID) {
        String sql = "SELECT * FROM ImpoundProduct WHERE (OrganizationCode = ?) AND (ImpoundProductID = ?)";

        return executeQueryObject(sql, new String[] { OrganizationCode, ImpoundProductID }, ImpoundProductInfo.class);
    }

    // Remove by Anan.
	// public ImpoundProductInfo getLastImpoundProduct(String documentTypeCode,
	// String teamCode, String employeeID, String yearMonth) {
	// //select * from ImpoundProduct where EffectiveBy = 'A10687' and
	// substring(ImpoundProductPaperID,len(ImpoundProductPaperID)-6,4) = '5712'
	// order by substring(ImpoundProductPaperID,len(ImpoundProductPaperID)-2,3)
	// desc
	//
	// final String sql =
	// "SELECT * FROM ImpoundProduct WHERE substr(impoundproductpaperid,1,1) = ? and substr(impoundproductpaperid,2,8) = ? and substr(impoundproductpaperid,10,2) = ? and substr(impoundproductpaperid,12,4) = ? order by CreateDate desc";
	//
	// List<ImpoundProductInfo> tmpImpoundProductList = executeQueryList(sql,
	// new String[] { documentTypeCode, teamCode, employeeID, yearMonth },
	// ImpoundProductInfo.class);
	//
	// if (tmpImpoundProductList == null)
	// return null;
	// else
	// return tmpImpoundProductList.get(0);
	// }

	public ImpoundProductInfo getLastImpoundProduct(String employeeID, String yearMonth) {
		final String sql = "SELECT * FROM ImpoundProduct "
				+ " WHERE (EffectiveBy = ?) AND (substr(ImpoundProductPaperID,length(ImpoundProductPaperID)-6,4) = ?) "
				+ " ORDER BY substr(ImpoundProductPaperID,length(ImpoundProductPaperID)-2,3) DESC";
		List<ImpoundProductInfo> tmpImpoundProductList = executeQueryList(sql, new String[] { employeeID, yearMonth }, ImpoundProductInfo.class);
		if (tmpImpoundProductList == null)
			return null;
		else
			return tmpImpoundProductList.get(0);
	}

	public void deleteImpoundProductAll() {
		executeNonQuery("delete from ImpoundProduct", null);
	}

	public List<ImpoundProductInfo> getImpoundProductByStatus(String Status) {
		final String sql = "SELECT ImpoundProduct.*, Contract.InstallDate, Contract.ProductSerialNumber, Contract.CONTNO, Contract.CustomerID, DebtorCustomer.IDCard "
				+ " 		, DebtorCustomer.PrefixName || DebtorCustomer.CustomerName AS CustomerFullName "
				+ " FROM ImpoundProduct "
				+ " 		INNER JOIN Contract ON (ImpoundProduct.RefNo = Contract.RefNo) "
				+ " 		INNER JOIN DebtorCustomer ON (Contract.CustomerID = DebtorCustomer.CustomerID) "
				+ " WHERE (ImpoundProduct.Status=?)";
		return executeQueryList(sql, new String[] {Status}, ImpoundProductInfo.class);
	}

	public List<ImpoundProductInfo> getImpoundProductByTeamCodeForSearch(String OrganizationCode, String TeamCode, String TextSearch, String refNo) {
		ArrayList<String> args = new ArrayList<>();
		String sql = "SELECT distinct c.InstallDate, c.ProductSerialNumber, c.CONTNO, c.CustomerID, c.OrganizationCode "
				+ " 		, dc.IDCard, dc.PrefixName || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName "
				+ " 		, dc.CompanyName, c.SaleTeamCode, p.ProductName, c.RefNo, c.IsMigrate "
				+ " FROM Contract c "
				+ " 		INNER JOIN ProductStock ps ON ps.ProductSerialNumber = c.ProductSerialNumber"
				+ " 		INNER JOIN Product p ON p.ProductID = ps.ProductID"
				+ " 		INNER JOIN DebtorCustomer dc on dc.CustomerID = c.CustomerID"
				+ " 		INNER JOIN SalePaymentPeriod spp ON spp.RefNo = c.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = 0 "
				+ " WHERE (c.OrganizationCode = ?) AND (c.isActive = 1) "
				+ " 		AND (c.Status = ?) AND (c.saleTeamCode = ?) ";

		args.add(OrganizationCode);
		args.add(ContractInfo.ContractStatus.NORMAL.toString());
		args.add(TeamCode);

		if(TextSearch != null) {
			sql += " 		and ifnull(c.ProductSerialNumber,'') || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'')  || ifnull(c.CONTNO,'') like ?  ";
			args.add("%" + TextSearch + "%");
		}

		if(refNo != null){
			sql += "        and c.RefNo = ? ";
			args.add(refNo);
		}

		sql += "   ORDER BY c.InstallDate ASC";
		return executeQueryList(sql, args.toArray(new String[args.size()]), ImpoundProductInfo.class);
	}

    public List<ImpoundProductInfo> getImpoundProductOtherTeamByStatusRequestOrApproved(String OrganizationCode, String EmpID, String TeamCode, String TextSearch, boolean PaymentComplete, String refNo) {
		ArrayList<String> args = new ArrayList<>();
		String sql = "SELECT distinct c.InstallDate, c.ProductSerialNumber, c.CONTNO, c.CustomerID, c.OrganizationCode "
				+ " 		, dc.IDCard, dc.PrefixName || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName "
				+ " 		, dc.CompanyName, c.SaleTeamCode, p.ProductName, c.RefNo, im.Status, im.ImpoundProductID "
				+ "			, c.IsMigrate, im.ImpoundProductID "
				+ " FROM Contract c "
				+ " 		INNER JOIN ProductStock ps ON ps.ProductSerialNumber = c.ProductSerialNumber"
				+ " 		INNER JOIN Product p ON p.ProductID = ps.ProductID"
				+ " 		INNER JOIN DebtorCustomer dc on dc.CustomerID = c.CustomerID"
				+ " 		INNER JOIN SalePaymentPeriod spp ON spp.RefNo = c.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = ? "
				+ " 		INNER JOIN ImpoundProduct im ON im.RefNo = c.RefNo and im.OrganizationCode = c.OrganizationCode "
				+ " WHERE (c.OrganizationCode = ?) AND (c.isActive = 1) "
				+ " 		AND (c.Status = ?) AND (c.saleTeamCode <> ?) "
				+ " 		AND (im.RequestTeamCode = ? and im.Status in (?, ?)) ";

		args.add(valueOf(PaymentComplete));
		args.add(OrganizationCode);
		args.add(ContractInfo.ContractStatus.NORMAL.toString());
		args.add(TeamCode);
		args.add(TeamCode);
		args.add(ImpoundProductStatus.REQUEST.toString());
		args.add(ImpoundProductStatus.APPROVED.toString());

		if(TextSearch != null) {
			sql += " 		and ifnull(c.ProductSerialNumber,'') || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'')  || ifnull(c.CONTNO,'') like ?  ";

			args.add("%" + TextSearch + "%");
		}

		if(refNo != null){
			sql += "        and c.RefNo = ? ";
			args.add(refNo);
		}

		sql += "   ORDER BY c.InstallDate ASC";
		return executeQueryList(sql, args.toArray(new String[args.size()]), ImpoundProductInfo.class);
	}

	public List<ImpoundProductInfo> getImpoundProductOtherTeamByStatusRequestOrApprovedForCredit(String OrganizationCode, String EmpID, String TeamCode, String TextSearch, boolean PaymentComplete, String refNo) {
		ArrayList<String> args = new ArrayList<>();
		String sql = "SELECT distinct c.InstallDate, c.ProductSerialNumber, c.CONTNO, c.CustomerID, c.OrganizationCode "
				+ " 		, dc.IDCard, dc.PrefixName || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName "
				+ " 		, dc.CompanyName, c.SaleTeamCode, p.ProductName, c.RefNo, im.Status, im.ImpoundProductID "
				+ "			, c.IsMigrate, im.ImpoundProductID "
				+ " FROM Contract c "
				+ " 		INNER JOIN ProductStock ps ON ps.ProductSerialNumber = c.ProductSerialNumber"
				+ " 		INNER JOIN Product p ON p.ProductID = ps.ProductID"
				+ " 		INNER JOIN DebtorCustomer dc on dc.CustomerID = c.CustomerID"
				+ " 		INNER JOIN SalePaymentPeriod spp ON spp.RefNo = c.RefNo AND spp.PaymentPeriodNumber = 1 AND spp.PaymentComplete = ? "
				+ " 		LEFT JOIN ImpoundProduct im ON im.RefNo = c.RefNo and im.OrganizationCode = c.OrganizationCode and im.Status NOT IN (?, ?) "
				+ " 		LEFT JOIN Assign a ON a.TaskType in (?, ?) and a.RefNo = c.RefNo AND a.OrganizationCode = c.OrganizationCode "
				+ " WHERE (c.OrganizationCode = ?) AND (c.isActive = 1) "
				+ " 		AND (c.Status = ?) AND (c.saleTeamCode <> ?) "
				+ " 		AND (a.AssigneeEmpID = ? or (im.RequestBy = ? and im.Status in (?, ?))) ";

		args.add(valueOf(PaymentComplete));
		args.add(ImpoundProductStatus.REJECT.toString());
		args.add(ImpoundProductStatus.COMPLETED.toString());
		args.add(AssignController.AssignTaskType.SalePaymentPeriod.toString());
		args.add(AssignController.AssignTaskType.SaleAudit.toString());
		args.add(OrganizationCode);
		args.add(ContractInfo.ContractStatus.NORMAL.toString());
		args.add(TeamCode);
		args.add(EmpID);
		args.add(EmpID);
		args.add(ImpoundProductStatus.REQUEST.toString());
		args.add(ImpoundProductStatus.APPROVED.toString());

		if(TextSearch != null) {
			sql += " 		and ifnull(c.ProductSerialNumber,'') || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'')  || ifnull(c.CONTNO,'') like ?  ";

			args.add("%" + TextSearch + "%");
		}

		if(refNo != null){
			sql += "        and c.RefNo = ? ";
			args.add(refNo);
		}

		sql += "   ORDER BY c.InstallDate ASC";
		return executeQueryList(sql, args.toArray(new String[args.size()]), ImpoundProductInfo.class);
	}

    public List<ImpoundProductInfo> getImpoundProductOtherTeamByStatusComplete(String EmpID, String TeamCode, String TextSearch) {
        final String sql = "SELECT ImpoundProduct.*, Contract.InstallDate, Contract.ProductSerialNumber, Contract.CONTNO, Contract.CustomerID "
				+ " 		, DebtorCustomer.IDCard,DebtorCustomer.PrefixName || DebtorCustomer.CustomerName AS CustomerFullName "
                + " FROM ImpoundProduct "
                + "			INNER JOIN Contract ON (ImpoundProduct.RefNo = Contract.RefNo) "
                + "			INNER JOIN DebtorCustomer ON (Contract.CustomerID = DebtorCustomer.CustomerID) "
                + " WHERE (ImpoundProduct.Status=?) AND (ImpoundProduct.RequestTeamCode = ?) AND (Contract.saleTeamCode <> ?) "
                + "			AND (CustomerFullName LIKE ? OR DebtorCustomer.IDCard LIKE ? OR Contract.ProductSerialNumber LIKE ? OR Contract.CONTNO LIKE ? )";
        return executeQueryList(sql, new String[] {ImpoundProductStatus.COMPLETED.toString(), TeamCode, TeamCode, "%" + TextSearch + "%", "%" + TextSearch + "%", "%" + TextSearch + "%", "%" + TextSearch + "%"}, ImpoundProductInfo.class);
    }
}
