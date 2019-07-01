package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.data.info.ReturnProductDetailInfo;
import th.co.thiensurat.data.info.ReturnProductInfo;

public class ReturnProductController extends BaseController {

	public enum ReturnProductStatus {
		REQUEST, 			// รอการตรวจสอบ
		REJECT,				// ไม่ผ่านการตรวจสอบสินค้า
		PASS				// ผ่านการตรวจสอบสินค้า
	}

	/*
	public enum ReturnProductDetailSourceStatus {
		WAIT, 				// รอตรวจสอบ : สินค้ารอตรวจสอบ
		CHECKED, 			// ตรวจสอบแล้ว : สินค้าผ่านการตรวจสอบแล้ว
		SOLD, 				// ขายแล้ว : ขายไปแล้ว
		OVER, 				// สินค้าเกิน
		RETURN, 			// ถอดเครื่อง/เปลี่ยนเครื่อง : สินค้าที่ถูกถอดจากการถอดเครื่องหรือถูกเปลี่ยนจากการเปลี่ยนเครื่อง
		DAMAGE, 			// ชำรุด : สินค้าที่นำคืนจากสินค้าชำรุด
		TEAM_DESTROY, 		// ยุบทีม: สินค้าที่นำคืนจากการยุบทีม
		TRADEIN				// เครื่องเทิร์น
	}
	*/


	private static final String QUERY_RETURNPRODUCT_GET = "SELECT RetProd.ReturnProductID, RetProd.OrganizationCode "
				+ " 	, RetProd.EmpID, (Emp.FirstName || ' ' || Emp.LastName) AS EmployeeName "
				+ " 	, RetProd.TeamCode, RetProd.SubTeamCode, RetProd.ReturnDate, RetProd.FortnightID "
				+ " 	, RetProd.RecevieDate, RetProd.Remark, RetProd.Status,RetProd.CreateDate, RetProd.CreateBy "
				+ " 	, RetProd.LastUpdateDate, RetProd.LastUpdateBy, RetProd.SyncedDate "
				+ " FROM ReturnProduct AS RetProd "
				+ " 	INNER JOIN Employee AS Emp ON (RetProd.OrganizationCode = Emp.OrganizationCode) AND (RetProd.EmpID = Emp.EmpID) "
				+ " WHERE (RetProd.OrganizationCode = ?) AND (RetProd.ReturnProductID LIKE '%' || ? || '%') "
				+ " 	AND (RetProd.TeamCode LIKE '%' || ? || '%') AND (RetProd.EmpID LIKE '%' || ? || '%') "
				+ " ORDER BY RetProd.ReturnProductID";
	
	private static final String QUERY_RETURNPRODUCT_DETAIL_GET = "SELECT RetProd.ReturnProductID, RetProd.OrganizationCode "
				+ " 	, RetProd.EmpID, (Emp.FirstName || ' ' || Emp.LastName) AS EmployeeName "
				+ " 	, RetProd.TeamCode, RetProd.SubTeamCode, RetProd.ReturnDate, RetProd.FortnightID "
				+ " 	, RetProd.RecevieDate, RetProd.Remark, RetProd.Status, RetProd.CreateDate, RetProd.CreateBy " 
				+ " 	, RetProd.LastUpdateDate, RetProd.LastUpdateBy,RetProdDet.ProductSerialNumber, RetProdDet.ProductID, RetProdDet.SyncedDate "
				+ " FROM ReturnProduct AS RetProd "
				+ " 	INNER JOIN Employee AS Emp ON (RetProd.OrganizationCode = Emp.OrganizationCode) AND (RetProd.EmpID = Emp.EmpID) "
				+ " 	INNER JOIN ReturnProductDetail AS RetProdDet ON (RetProd.OrganizationCode = RetProdDet.OrganizationCode) "
				+ "																AND (RetProd.ReturnProductID = RetProdDet.ReturnProductID) "
				+ " WHERE (RetProd.OrganizationCode = ?) AND (RetProd.ReturnProductID = ?) ";

    private static final String QUERY_RETURNPRODUCT_GET_AVAILABLE_PRODUCTSTOCK = "SELECT ProdStk.ProductSerialNumber AS ProductSerialNumber"
            + " 		, ProdStk.OrganizationCode AS OrganizationCode, ProdStk.ProductID AS ProductID"
            + " 		, ProdStk.TeamCode AS TeamCode, ProdStk.Status AS Status"
            + " 		, '' AS RefNo, '' AS TradeInBrandCode, '' AS TradeInProductModel"
            + "	FROM ProductStock AS ProdStk "
            + " 		LEFT OUTER JOIN ( "
            + " 			SELECT RetProdDet.* "
            + " 			FROM ReturnProduct AS RetProd "
            + " 					INNER JOIN ReturnProductDetail AS RetProdDet "
            + " 							ON (RetProd.OrganizationCode = RetProdDet.OrganizationCode) "
            + " 							AND (RetProd.ReturnProductID = RetProdDet.ReturnProductID) "
            + " 			WHERE (RetProd.Status <> 'REJECT') "
            + " 		) AS ReqOrApprRetProd "
            + " 		ON (ProdStk.OrganizationCode = ReqOrApprRetProd.OrganizationCode) AND (ProdStk.ProductSerialNumber = ReqOrApprRetProd.ProductSerialNumber) "
            + " WHERE (ProdStk.OrganizationCode = ?) AND "
            // [START] Fixed-[BHPRJ00301-3923] Product from Impound-Product by Other-Team
//            + "             (ProdStk.TeamCode = ?) AND (ProdStk.Status IN ('CHECKED', 'RETURN', 'DAMAGE', 'TEAM_DESTROY')) "
            + "             ( "
            + "             ((ProdStk.TeamCode = ?) AND (ProdStk.Status IN ('CHECKED', 'RETURN', 'DAMAGE', 'TEAM_DESTROY'))) "
            + "             OR "
            + "             ((ProdStk.TeamCode <> ?) AND (ProdStk.Status = 'RETURN')) "
            + "             ) "
            // [END] Fixed-[BHPRJ00301-3923] Product from Impound-Product by Other-Team
            + " 			AND (ReqOrApprRetProd.ProductSerialNumber IS NULL) "
            + " UNION "
            + " SELECT (CASE WHEN Cont.TradeInProductCode IS NOT NULL THEN Cont.TradeInProductCode ELSE '' END) AS ProductSerialNumber"
            + " 		, Cont.OrganizationCode  AS OrganizationCode, '' AS ProductID"
            + " 		, Cont.SaleTeamCode AS TeamCode, 'TRADEIN' AS Status"
            + " 		, Cont.RefNo AS RefNo, (CASE WHEN Cont.TradeInBrandCode IS NOT NULL THEN Cont.TradeInBrandCode ELSE '' END) AS TradeInBrandCode"
            + " 		, (CASE WHEN Cont.TradeInProductModel IS NOT NULL THEN Cont.TradeInProductModel ELSE '' END) AS TradeInProductModel"
            + " FROM Contract AS Cont " 
            + " WHERE (Cont.isActive=1) AND (Cont.HasTradeIn=1) AND (ifnull(Cont.TradeInReturnFlag,0)=0) AND  (Cont.OrganizationCode = ?) AND (Cont.SaleTeamCode=?)"
			+ " UNION "
			+ " SELECT p.ProductSerialNumber AS ProductSerialNumber"
			+ "			, p.OrganizationCode AS OrganizationCode, p.ProductID AS ProductID"
			+ "			, p.TeamCode AS TeamCode, p.Status AS Status"
			+ "			, im.RefNo AS RefNo, '' AS TradeInBrandCode, '' AS TradeInProductModel"
			+ " FROM ImpoundProduct im"
			+ " 		INNER JOIN Contract c on c.RefNo = im.RefNo"
			+ " 		INNER JOIN ProductStock p on p.ProductSerialNumber = c.ProductSerialNumber"
			+ " WHERE im.OrganizationCode = ? AND im.RequestTeamCode = ? AND c.SaleTeamCode <> ? AND im.Status = 'COMPLETED' AND c.IsActive = 1"
            ;

    private static final String QUERY_RETURNPRODUCT_INSERT =	" INSERT INTO ReturnProduct(OrganizationCode, ReturnProductID, EmpID, TeamCode, SubTeamCode"
				+ "		, ReturnDate, FortnightID, RecevieDate, Remark, Status, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy)"
				+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String QUERY_RETURNPRODUCT_DETAIL_INSERT_BY_CONTRACT =	" INSERT INTO ReturnProductDetail(OrganizationCode, ReturnProductID, ProductSerialNumber "
            + " 	, ProductID, TradeInBrandCode, TradeInProductModel, RefNo"
    		+ " ,SourceStatus, Status, ReceivedDate, ReceivedBy, ReceivedRemark, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy)"
    		+ " VALUES(?, ?, ?, ?, ?, ?, ? , ? ,?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String QUERY_RETURNPRODUCT_GET_LAST = "SELECT * FROM ReturnProduct WHERE CreateBy = ? AND substr(ReturnProductID, length(ReturnProductID)-6, 4) = ? ORDER BY substr(ReturnProductID, length(ReturnProductID)-2, 3) DESC";

	public List<ReturnProductInfo> getReturnProductByCondition(String organizationCode, String returnProductID, String teamCode, String empID) {
		return executeQueryList(QUERY_RETURNPRODUCT_GET, new String[] { organizationCode, returnProductID, teamCode, empID }, ReturnProductInfo.class);
	}
	public List<ReturnProductDetailInfo> getReturnProductDetailByID(String organizationCode, String returnProductID) {
		return executeQueryList(QUERY_RETURNPRODUCT_DETAIL_GET, new String[] { organizationCode, returnProductID }, ReturnProductDetailInfo.class);
	}
	public List<ProductStockInfo> getAvailableProductStockForReturn(String organizationCode, String subTeamCode, String teamCode) {
		return executeQueryList(QUERY_RETURNPRODUCT_GET_AVAILABLE_PRODUCTSTOCK, new String[] { organizationCode, subTeamCode, teamCode, organizationCode, teamCode, organizationCode, teamCode, teamCode}, ProductStockInfo.class);
	}

	public void insertReturnProduct(ReturnProductInfo info) {
		executeNonQuery(QUERY_RETURNPRODUCT_INSERT, new String[]{info.OrganizationCode, info.ReturnProductID, info.EmpID, info.TeamCode, info.SubTeamCode,
				valueOf(info.ReturnDate), info.FortnightID, valueOf(info.RecevieDate), info.Remark, info.Status, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy});

		TSRController.updateRunningNumber(TSRController.DocumentGenType.ReturnProduct, info.ReturnProductID, info.CreateBy);
	}
	public void insertReturnProductDetail(ReturnProductDetailInfo info) {
        executeNonQuery(QUERY_RETURNPRODUCT_DETAIL_INSERT_BY_CONTRACT, new String[]{info.OrganizationCode, info.ReturnProductID, info.ProductSerialNumber, info.ProductID, info.TradeInBrandCode, info.TradeInProductModel, info.RefNo, info.SourceStatus, info.Status, valueOf(info.ReceivedDate), info.ReceivedBy, info.ReceivedRemark, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy});
	}

	public ReturnProductInfo getLastReturnProduct(String employeeID, String yearMonth) {
		List<ReturnProductInfo> tmpReturnProductList = executeQueryList(QUERY_RETURNPRODUCT_GET_LAST, new String[] { employeeID, yearMonth }, ReturnProductInfo.class);
		if (tmpReturnProductList == null) {
			return null;
		} else {
			return tmpReturnProductList.get(0);
		}
	}

    public void deleteReturnProductAll() {
        String sql = "DELETE FROM ReturnProduct";
        executeNonQuery(sql, null);
    }

    public void deleteReturnProductDetailAll() {
        String sql = "DELETE FROM ReturnProductDetail";
        executeNonQuery(sql, null);
    }

}
