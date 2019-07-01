package th.co.thiensurat.data.controller;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ChangeProductInfo;
import th.co.thiensurat.data.info.ContractInfo;

public class ChangeProductController extends BaseController {

	public enum ChangeProductStatus {
		REQUEST, APPROVED, COMPLETED, REJECT
	}
	
	public void addChangeProduct(ChangeProductInfo info) {
		String sql = "INSERT INTO [ChangeProduct](ChangeProductID, OrganizationCode, RefNo, OldProductSerialNumber, NewProductSerialNumber,"
				+ " [Status], RequestProblemID, RequestDetail, RequestDate, RequestBy, RequestTeamCode,"
				+" ApproveDetail, ApprovedDate, ApprovedBy, ResultProblemID, ResultDetail, EffectiveDate, EffectiveBy, ChangeProductPaperID,"
				+ " CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, RequestEmployeeLevelPath, EffectiveEmployeeLevelPath)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		executeNonQuery(sql, new String[] { info.ChangeProductID, info.OrganizationCode, info.RefNo, info.OldProductSerialNumber, info.NewProductSerialNumber,
				info.Status, info.RequestProblemID, info.RequestDetail, valueOf( info.RequestDate), info.RequestBy, info.RequestTeamCode,
				info.ApproveDetail, valueOf(info.ApprovedDate), info.ApprovedBy,  info.ResultProblemID, info.ResultDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.ChangeProductPaperID,
				valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath});

		TSRController.updateRunningNumber(TSRController.DocumentGenType.ChangeProduct, info.ChangeProductPaperID, info.EffectiveBy);

//		String sql = "INSERT INTO [ChangeProduct](ChangeProductID, OrganizationCode, RefNo, "
//				+ " [Status], ApproveDetail, ApprovedDate, ApprovedBy, "
//				+ " CreateDate, CreateBy, LastUpdateDate, LastUpdateBy)" 
//				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
//
//		executeNonQuery(sql, new String[] { info.ChangeProductID, info.OrganizationCode, info.RefNo, 
//				info.Status, info.ApproveDetail, valueOf(info.ApprovedDate), info.ApprovedBy, 
//				valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy});
	}
	
	public void updateChangeProduct(ChangeProductInfo info) {
		String sql = "UPDATE ChangeProduct"
				+" SET 	OrganizationCode=?,"
				+" 		RefNo=?,"
				+" 		OldProductSerialNumber=?,"
				+" 		NewProductSerialNumber=?,"
				+" 		[Status]=?,"
				+" 		RequestProblemID=?,"
				+" 		RequestDetail=?,"
				+" 		RequestDate=?,"
				+" 		RequestBy=?,"
				+" 		RequestTeamCode=?,"
				+" 		ApproveDetail=?,"
				+" 		ApprovedDate=?,"
				+" 		ApprovedBy=?,"
				+" 		ResultProblemID=?,"
				+" 		ResultDetail=?,"
				+" 		EffectiveDate=?,"
				+" 		EffectiveBy=?,"
				+" 		ChangeProductPaperID=?,"
				+" 		LastUpdateDate=?,"
				+" 		LastUpdateBy=?,"
				+"		RequestEmployeeLevelPath = ? ,"
				+"		[EffectiveEmployeeLevelPath] = ?"
				+" WHERE (ChangeProductID=?)";
		executeNonQuery(sql, new String[] {info.OrganizationCode, info.RefNo, info.OldProductSerialNumber, info.NewProductSerialNumber, 
				info.Status, info.RequestProblemID, info.RequestDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode,
				info.ApproveDetail, valueOf(info.ApprovedDate), info.ApprovedBy, info.ResultProblemID, info.ResultDetail,
				valueOf(info.EffectiveDate), info.EffectiveBy, info.ChangeProductPaperID, 
				valueOf(info.LastUpdateDate), info.LastUpdateBy, info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath, info.ChangeProductID});

		TSRController.updateRunningNumber(TSRController.DocumentGenType.ChangeProduct, info.ChangeProductPaperID, info.EffectiveBy);
	}
	
//	public void addChangeProduct(ChangeProductInfo info) {
//
//		String sql = "INSERT INTO [ChangeProduct]([ChangeProductID], [OrganizationCode], [RefNo], [OldProductSerialNumber], [NewProductSerialNumber], "
//				+ "[Status], [RequestProblemID], [RequestDetail], [RequestDate], [RequestBy], "
//				+ "[RequestTeamCode], [ApproveDetail], [ApprovedDate], [ApprovedBy], [ResultProblemID], "
//				+ "[ResultDetail], [EffectiveDate], [EffectiveBy], [ChangeProductPaperID], [CreateDate], "
//				+ "[CreateBy], [LastUpdateDate], [LastUpdateBy], [SyncedDate]) " + "VALUES (?,?,?,?,?," + "?,?,?,datetime('now'),?," + "?,?,?,?,?,"
//				+ "?,?,?,?,datetime('now')," + "?,datetime('now'),?,?)";
//
//		executeNonQuery(sql, new String[] { info.ChangeProductID, info.OrganizationCode, info.RefNo, info.OldProductSerialNumber, info.NewProductSerialNumber,
//				info.Status, info.RequestProblemID, info.RequestDetail, info.RequestBy, info.RequestTeamCode, info.ApproveDetail, valueOf(info.ApprovedDate),
//				info.ApprovedBy, info.ResultProblemID, info.ResultDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.ChangeProductPaperID,
//				info.CreateBy, info.LastUpdateBy, valueOf(info.SyncedDate) });
//
//	}

//	public void addRequestChangeProduct(ChangeProductInfo info) {
//		String sql = "INSERT INTO [ChangeProduct](ChangeProductID, OrganizationCode, RefNo, OldProductSerialNumber, NewProductSerialNumber,"
//				+ " [Status], RequestProblemID, RequestDetail, RequestDate, RequestBy, RequestTeamCode,"
//				+ " CreateDate, CreateBy, LastUpdateDate, LastUpdateBy)"
//				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//		executeNonQuery(sql, new String[] { info.ChangeProductID, info.OrganizationCode, info.RefNo, info.OldProductSerialNumber, info.NewProductSerialNumber,
//				info.Status, info.RequestProblemID, info.RequestDetail, valueOf( info.RequestDate), info.RequestBy, info.RequestTeamCode,
//				valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy});
//	}
//
//	public void actionChangeProduct(ChangeProductInfo info) {
//		String sql = "update ChangeProduct"
//				+" set OrganizationCode=?,"
//				+" RefNo=?,"
//				+" NewProductSerialNumber=?,"
//				+" Status=?,"
//				+" RequestProblemID=?,"
//				+" RequestDetail=?,"
//				+" EffectiveDate=?,"
//				+" EffectiveBy=?,"
//				+" ChangeProductPaperID=?,"
//				+" LastUpdateDate=?,"
//				+" LastUpdateBy=?"
//				+" where ChangeProductID=?";
//		executeNonQuery(sql, new String[] {info.OrganizationCode, info.RefNo, info.NewProductSerialNumber,
//				info.Status, info.RequestProblemID, info.RequestDetail, valueOf(info.EffectiveDate), info.EffectiveBy,
//				info.ChangeProductPaperID, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.ChangeProductID});
//	}
//
//	public void approveChangeProduct(ChangeProductInfo info) {
//		String sql = "update ChangeProduct"
//				+" set OrganizationCode=?,"
//				+" RefNo=?,"
//				+" Status=?,"
//				+" ApprovedDate=?,"
//				+" ApproveDetail=?,"
//				+" ApprovedBy=?,"
//				+" LastUpdateDate=?,"
//				+" LastUpdateBy=?"
//				+" where ChangeProductID=?";
//		executeNonQuery(sql, new String[] {info.OrganizationCode, info.RefNo,
//				info.Status, valueOf(info.ApprovedDate), info.ApproveDetail, info.ApprovedBy,
//				valueOf(info.LastUpdateDate), info.LastUpdateBy, info.ChangeProductID});
//	}

//	public void updateChangeProduct(ChangeProductInfo info) {
//
//		String sql = "";
//
//		if (info.ApprovedBy != null && !info.ApprovedBy.trim().equals("")) {
//			sql = "UPDATE [ChangeProduct] SET [Status] = ?, [ApproveDetail] = ?, [ApprovedDate] = datetime('now'), "
//					+ "[ApprovedBy] = ?, [LastUpdateDate] = datetime('now'), [LastUpdateBy] = ? " + "WHERE [ChangeProductID] = ?";
//			// Status = APPROVED
//		} else if (info.EffectiveBy != null && !info.EffectiveBy.trim().equals("")) {
//			sql = "UPDATE [ChangeProduct] SET [Status] = ?, [ResultProblemID] = ?, [ResultDetail] = ?, "
//					+ "[EffectiveDate] = datetime('now'), [EffectiveBy] = ?, [ChangeProductPaperID] = ? " + "WHERE [ChangeProductID] = ?";
//			// Status = COMPLETED
//		}
//
//		if (info.ApprovedBy != null && !info.ApprovedBy.trim().equals("")) {
//			executeNonQuery(sql, new String[] { info.Status, info.ApproveDetail, info.ApprovedBy, info.LastUpdateBy, info.ChangeProductID });
//		} else if (info.EffectiveBy != null && !info.EffectiveBy.trim().equals("")) {
//			executeNonQuery(sql, new String[] { info.Status, info.ResultProblemID, info.ResultDetail, info.EffectiveBy, info.ChangeProductPaperID,
//					info.ChangeProductID });
//		}
//
//	}

	public ChangeProductInfo getChangeProductByID(String OrganizationCode, String ChangeProductID) {
		String sql = "SELECT "
				+ "		ChangeProduct.OldProductSerialNumber,"
				+ "		ChangeProduct.NewProductSerialNumber,"
				+ "		ChangeProduct.EffectiveDate,"
				+ "		ChangeProduct.ChangeProductPaperID,"
				+"		ChangeProduct.RequestEmployeeLevelPath,"
				+"		ChangeProduct.EffectiveEmployeeLevelPath,"
				+ "		Contract.RefNo,"
				+ "		Contract.InstallDate,"
				+ "		ifnull(DebtorCustomer.PrefixName, '') || ' ' || ifnull(DebtorCustomer.CustomerName, '') || ifnull(DebtorCustomer.CompanyName, '') AS CustomerFullName,"
				+ "		Product.ProductName "
				+ "FROM ChangeProduct "
				+ "		inner join Contract on Contract.OrganizationCode = ChangeProduct.OrganizationCode AND ChangeProduct.RefNo = Contract.RefNo "
				+ "		inner join DebtorCustomer on DebtorCustomer.OrganizationCode = Contract.OrganizationCode AND DebtorCustomer.CustomerID = Contract.CustomerID "
				+ "		inner join Product on Product.OrganizationCode = Contract.OrganizationCode AND Product.ProductID = Contract.ProductID "
				+ "WHERE (Contract.OrganizationCode = ?) AND (ChangeProduct.ChangeProductID = ?)";

		return executeQueryObject(sql, new String[] { OrganizationCode, ChangeProductID }, ChangeProductInfo.class);
	}

	// Remove by Anan.
//	public ChangeProductInfo getLastChangeProduct(String documentTypeCode, String subTeamCode, String employeeID, String yearMonth) {
//		//select * from changeproduct where EffectiveBy = 'P15001' and substring(ChangeProductPaperID,len(ChangeProductPaperID)-6,4) = '5712' order by substring(ChangeProductPaperID,len(ChangeProductPaperID)-2,3) desc
//
//		final String sql = "SELECT * FROM ChangeProduct WHERE substr(changeproductpaperid,1,1) = ? and substr(changeproductpaperid,2,8) = ? and substr(changeproductpaperid,10,2) = ? and substr(changeproductpaperid,12,4) = ? order by changeproductpaperid desc";
//
//		List<ChangeProductInfo> tmpChangeProductList = executeQueryList(sql, new String[] { documentTypeCode, subTeamCode, employeeID, yearMonth },
//				ChangeProductInfo.class);
//
//		if (tmpChangeProductList == null)
//			return null;
//		else
//			return tmpChangeProductList.get(0);
//	}
	
	public ChangeProductInfo getLastChangeProduct(String employeeID, String yearMonth) {
		final String sql = "SELECT * FROM ChangeProduct "
				+ " WHERE (EffectiveBy = ?) AND (substr(ChangeProductPaperID, length(ChangeProductPaperID)-6,4) = ?) "
				+ " ORDER BY substr(ChangeProductPaperID, length(ChangeProductPaperID)-2,3) DESC";
		List<ChangeProductInfo> tmpChangeProductList = executeQueryList(sql, new String[] {  employeeID, yearMonth },
				ChangeProductInfo.class);

		if (tmpChangeProductList == null)
			return null;
		else
			return tmpChangeProductList.get(0);
	}
	
	public void deleteChangeProductAll() {
		executeNonQuery("delete from ChangeProduct", null);
	}
	
//	public List<ChangeProductInfo> getChangeProductList(String organizationCode, String teamCode, String status) {
//		List<ChangeProductInfo> ret = null;
//		String sql = "SELECT c.*, dc.PrefixName || dc.CustomerName AS CustomerFullName, dc.CompanyName, dc.IDCard,p.ProductName, e.FirstName || '  ' || e.LastName AS SaleEmployeeName"
//				+ " FROM Contract as c left outer join"
//				+ " DebtorCustomer as dc on c.OrganizationCode=dc.OrganizationCode and c.CustomerID=dc.CustomerID inner join"
//				+ " Product as p on c.ProductID=p.ProductID inner join"
//				+ " Employee as e on c.SaleEmployeeCode=e.EmpID left outer join"
//				+ " ChangeProduct as cp on c.RefNo=cp.RefNo"
//				+ " where c.isActive='1' and cp.Status not in ('COMPLETED','CANCELED')"
//				+ " and c.OrganizationCode=?" + " and c.SaleTeamCode = ?" + " and c.Status=?";
//
//		ret = executeQueryList(sql, new String[] { organizationCode, teamCode, status }, ChangeProductInfo.class);	
//		return ret;
//	}

	/* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
	public List<ChangeProductInfo> getChangeProductList(String organizationCode, String teamCode, String searchText, String refNo) {
		ArrayList<String> args = new ArrayList<>();
		String sql = "SELECT c.[RefNo],c.[CONTNO], c.OrganizationCode, c.ProductSerialNumber "
				+ "			 , IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName, c.IsMigrate "
				+ "   FROM Contract as c "
				+ "		   inner join DebtorCustomer as dc on dc.OrganizationCode = c.OrganizationCode and dc.CustomerID = c.CustomerID "
				+ "   where c.isActive='1' and (c.StatusCode=(select StatusCode from ContractStatus where (StatusName = 'COMPLETED'))) "
				+ " 		and c.OrganizationCode = ? and (c.Status IN (?, ?)) "
				+ " 		and c.SaleTeamCode = ? "
				+ " 		and (date(c.EFFDATE) BETWEEN date('now', '-90 days') AND date('now')) ";

		args.add(organizationCode);
		args.add(ContractInfo.ContractStatus.NORMAL.toString());
		args.add(ContractInfo.ContractStatus.F.toString());
		args.add(teamCode);

		if(searchText != null){
			sql += " 		and ifnull(c.ProductSerialNumber,'') || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'')  || ifnull(c.CONTNO,'') like ? ";
			args.add("%" + searchText + "%");
		}

		if(refNo != null){
			sql += "        and c.RefNo = ? ";
			args.add(refNo);
		}

		sql += "   ORDER BY c.InstallDate ASC";

		return executeQueryList(sql, args.toArray(new String[args.size()]), ChangeProductInfo.class);
	}

	/* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
	public List<ChangeProductInfo> getChangeProductListForCredit(String organizationCode, String searchText, String refNo, String empId) {
		ArrayList<String> args = new ArrayList<>();
		String sql = "SELECT distinct c.[RefNo],c.[CONTNO], c.OrganizationCode, c.ProductSerialNumber "
				+ "			 , IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName "
				+ " 		 , cp.Status, cp.ChangeProductID, c.IsMigrate "
				+ "   FROM Contract as c "
				+ "		   inner join DebtorCustomer as dc on dc.OrganizationCode = c.OrganizationCode and dc.CustomerID = c.CustomerID "
				+ "        LEFT JOIN Assign as a ON a.TaskType in (?, ?) and a.RefNo = c.RefNo and a.OrganizationCode = c.OrganizationCode "
				+ "        LEFT join ChangeProduct as cp ON cp.RefNo = c.RefNo and cp.OrganizationCode = c.OrganizationCode and cp.Status NOT IN (?, ?) "
				+ "   where c.isActive='1' and (c.StatusCode=(select StatusCode from ContractStatus where (StatusName = 'COMPLETED'))) "
				+ " 		and c.OrganizationCode = ? and (c.Status IN (?, ?)) "
				+ " 		and (date(c.EFFDATE) BETWEEN date('now', '-90 days') AND date('now')) "
				+ " 		and (a.AssigneeEmpID = ? or (cp.RequestBy = ? and cp.Status in (?, ?))) ";

		args.add(AssignController.AssignTaskType.SalePaymentPeriod.toString());
		args.add(AssignController.AssignTaskType.SaleAudit.toString());
		args.add(ChangeProductStatus.REJECT.toString());
		args.add(ChangeProductStatus.COMPLETED.toString());
		args.add(organizationCode);
		args.add(ContractInfo.ContractStatus.NORMAL.toString());
		args.add(ContractInfo.ContractStatus.F.toString());
		args.add(empId);
		args.add(empId);
		args.add(ChangeProductStatus.REQUEST.toString());
		args.add(ChangeProductStatus.APPROVED.toString());

		if(searchText != null){
			sql += " 		and ifnull(c.ProductSerialNumber,'') || ifnull(dc.CustomerName,'') || ifnull(dc.CompanyName,'')  || ifnull(c.CONTNO,'') like ? ";
			args.add("%" + searchText + "%");
		}

		if(refNo != null){
			sql += "        and c.RefNo = ? ";
			args.add(refNo);
		}

		sql += "   ORDER BY c.InstallDate ASC";

		return executeQueryList(sql, args.toArray(new String[args.size()]), ChangeProductInfo.class);
	}
	
	public ChangeProductInfo getChangeProductByRefNoByStatus(String refNo, String status) {
		String sql = "select * from ChangeProduct where RefNo = ? and Status = ?";
		return executeQueryObject(sql, new String[] { refNo, status }, ChangeProductInfo.class);
	}

	/*** [START] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
	public void deleteChangeProductByID(String OrganizationCode, String ChangeProductID) {
		executeNonQuery("DELETE FROM ChangeProduct WHERE (OrganizationCode = ?) AND (ChangeProductID = ?) ", new String[] { OrganizationCode, ChangeProductID });
	}
	/*** [END] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/

}
