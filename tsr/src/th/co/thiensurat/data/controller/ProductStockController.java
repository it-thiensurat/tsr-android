package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ProductStockInfo;

public class ProductStockController extends BaseController {

	public enum ProductStockStatus {
		WAIT, 				// รอตรวจสอบ : สินค้ารอตรวจสอบ
		CHECKED, 			// ตรวจสอบแล้ว : สินค้าผ่านการตรวจสอบแล้ว
		SOLD, 				// ขายแล้ว : ขายไปแล้ว
		OVER, 				// สินค้าเกิน
		RETURN, 			// ถอดเครื่อง/เปลี่ยนเครื่อง : สินค้าที่ถูกถอดจากการถอดเครื่องหรือถูกเปลี่ยนจากการเปลี่ยนเครื่อง
		DAMAGE, 			// ชำรุด : สินค้าที่นำคืนจากสินค้าชำรุด
		TEAM_DESTROY, 		// ยุบทีม: สินค้าที่นำคืนจากการยุบทีม
		TRADEIN,			// เครื่องเทิร์น
		WAIT_RETURN			// รอตรวจสอบจากการส่งคืนสินค้าเข้าระบบ
	}

	private static final String QUERY_PRODUCTSTOCK_INSERT = " INSERT INTO ProductStock(ProductSerialNumber, OrganizationCode, ProductID, Type, TeamCode, Status, ImportDate,ScanDate, ScanByTeam, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy)"
			+ "			VALUES(?, ?, ?, ?, ?, ?,datetime('now'),' ',?,?, ?,?,?)";

	private static final String QUERY_PRODUCTSTOCK_GET_OF_TEAM = "SELECT * FROM ProductStock WHERE (OrganizationCode=?) AND (Status not in ('SOLD','RETURN','WAIT_RETURN','DAMAGE','TEAM_DESTROY')) AND ((TeamCode like ? || '%') OR (ScanByTeam like ? || '%')) ORDER BY ProductSerialNumber ";
	private static final String QUERY_PRODUCTSTOCK_GET_BY_STATUS = "SELECT * FROM ProductStock WHERE (OrganizationCode=?) AND (TeamCode = ?) AND (Status = ?) ";
	private static final String QUERY_PRODUCTSTOCK_ALL_TEAM_GET_BY_STATUS = "SELECT * FROM ProductStock WHERE (OrganizationCode=?) AND (Status = ?) ";
	private static final String QUERY_PRODUCTSTOCK_GET_ALL = "SELECT * FROM ProductStock";

	// private static final String QUERY_PRODUCTSTOCK_DELETE =
	// "DELETE FROM ProductStock WHERE (OrganizationCode=?) AND (TeamCode=?) AND (Status<>'SOLD') AND (date(ImportDate)=date('now')) ";
	// // PRODUCTION
	// For Mock-up data
	// private static final String QUERY_PRODUCTSTOCK_DELETE =
	// "DELETE FROM ProductStock WHERE (OrganizationCode=?) AND ((Status='WAIT') OR (Status='OVER') OR (date(ImportDate)=date('now'))";
	private static final String QUERY_PRODUCTSTOCK_DELETE = "DELETE FROM ProductStock WHERE (OrganizationCode=?) and (TeamCode=?) and (Status in ('CHECKED','WAIT'))"; // MOCK-UP

	// private static final String QUERY_PRODUCTSTOCK_DELETE =
	// "DELETE FROM ProductStock WHERE (OrganizationCode=?) AND ((Status='WAIT') OR (Status='OVER') OR ((Status='CHECKED') AND (date(ImportDate)=date('now')))) ";
	// // Test all clear with mock-up

	public void addProductStock(ProductStockInfo info) {
		// if (getProductStock(info.ProductSerialNumber) == null) {
		executeNonQuery(QUERY_PRODUCTSTOCK_INSERT, new String[] { info.ProductSerialNumber, info.OrganizationCode, info.ProductID, info.Type, info.TeamCode,
				info.Status, info.ScanByTeam, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy });

		// }
	}

	// public void updateProductStock(ProductStockInfo info) {
	// beginTransaction();
	//
	// // UPDATE ProductStock SET ScanByTeam = "PAK23" WHERE
	// // (ProductSerialNumber = "885125478970") AND (Status = "CHECKED")
	// try {
	// executeNonQuery(QUERY_PRODUCTSTOCK_UPDATE, new String[] {
	// info.ScanByTeam, info.ProductSerialNumber });
	//
	// commitTransaction();
	//
	// } catch (Exception e) {
	// rollbackTransaction();
	// }
	// }

	public List<ProductStockInfo> getProductStockOfTeam(String organizationCode, String teamcode) {
		List<ProductStockInfo> result = executeQueryList(QUERY_PRODUCTSTOCK_GET_OF_TEAM, new String[] { organizationCode, teamcode, teamcode }, ProductStockInfo.class);
		return result;
	}

	public List<ProductStockInfo> getProductStockByEmployeeID(String organizationCode, String employeeID) {
		String sql = "SELECT * FROM ProductStock WHERE OrganizationCode = ? AND CreateBy = ? AND (Status not in ('SOLD','RETURN','WAIT_RETURN','DAMAGE','TEAM_DESTROY')) ORDER BY ProductSerialNumber ";

		List<ProductStockInfo> result = executeQueryList(sql, new String[] { organizationCode, employeeID}, ProductStockInfo.class);
		return result;
	}

	public List<ProductStockInfo> getProductStockByStatus(String organizationCode, String teamcode, String status) {
		return executeQueryList(QUERY_PRODUCTSTOCK_GET_BY_STATUS, new String[] { organizationCode, teamcode, status }, ProductStockInfo.class);
	}
	
	public List<ProductStockInfo> getProductStockAllTeamByStatus(String organizationCode, String status) {
		return executeQueryList(QUERY_PRODUCTSTOCK_ALL_TEAM_GET_BY_STATUS, new String[] { organizationCode, status }, ProductStockInfo.class);
	}

	public ProductStockInfo updateProductStockStatus(ProductStockInfo info) {
        if(info.ScanByTeam != null){
            final String QUERY_PRODUCTSTOCK_UPDATE_STATUS = "UPDATE ProductStock SET ScanDate = datetime('now'),Status = ? , ScanByTeam = ?, LastUpdateDate = datetime('now'), LastUpdateBy = ? WHERE (OrganizationCode=?) AND (ProductSerialNumber = ?)";
            return executeQueryObject(QUERY_PRODUCTSTOCK_UPDATE_STATUS, new String[] { info.Status, info.ScanByTeam, info.LastUpdateBy, info.OrganizationCode,
                    info.ProductSerialNumber }, ProductStockInfo.class);
        }else{
            final String QUERY_PRODUCTSTOCK_UPDATE_STATUS = "UPDATE ProductStock SET ScanDate = datetime('now'),Status = ? , LastUpdateDate = datetime('now'), LastUpdateBy = ? WHERE (OrganizationCode=?) AND (ProductSerialNumber = ?)";
            return executeQueryObject(QUERY_PRODUCTSTOCK_UPDATE_STATUS, new String[] { info.Status, info.LastUpdateBy, info.OrganizationCode,
                    info.ProductSerialNumber }, ProductStockInfo.class);
        }
	}

	// public void updateProductStockStat(ProductStockInfo ps) {
	// String sql = "update ProductStock set Status=?"
	// +" where OrganizationCode=?"
	// +" and ProductSerialNumber=?";
	// executeNonQuery(sql, new String[] {ps.Status, ps.OrganizationCode,
	// ps.ProductSerialNumber });
	// }
//	public void updateProductStockScan(ProductStockInfo ps) {
//		String sql = "update ProductStock set ScanDate=?," + " ScanByTeam=?, LastUpdateDate = datetime('now'), LastUpdateBy = ?" + " where OrganizationCode=?" + " and ProductSerialNumber=?";
//		executeNonQuery(sql, new String[] { valueOf(ps.ScanDate), ps.ScanByTeam, ps.LastUpdateBy, ps.OrganizationCode, ps.ProductSerialNumber });
//	}

	public List<ProductStockInfo> getProductStocks() {
		return executeQueryList(QUERY_PRODUCTSTOCK_GET_ALL, null, ProductStockInfo.class);
	}

	public void updateProductStock(ProductStockInfo info) {
		String sql = "UPDATE ProductStock " + " SET OrganizationCode = ?, ProductID = ?, Type = ?, "
				+ "TeamCode = ?, Status = ?, ImportDate = ?, ScanDate = ?, " + " ScanByTeam = ?, LastUpdateDate = datetime('now'), LastUpdateBy = ? " + " WHERE ProductSerialNumber = ?";
		executeNonQuery(sql, new String[] { info.OrganizationCode, info.ProductID, info.Type, info.TeamCode, info.Status, valueOf(info.ImportDate),
				valueOf(info.ScanDate), info.ScanByTeam, info.LastUpdateBy, info.ProductSerialNumber });
	}

	public ProductStockInfo getProductStock(String productSerialNumber) {
		final String QUERY_PRODUCTSTOCK_GET_BY_SERIAL = "SELECT ps.*, p.ProductName FROM ProductStock as ps left outer join Product as p on ps.ProductID=p.ProductID WHERE ps.ProductSerialNumber = ?";
		return executeQueryObject(QUERY_PRODUCTSTOCK_GET_BY_SERIAL, new String[] { productSerialNumber }, ProductStockInfo.class);
	}

	public ProductStockInfo getProductStockForCRD(String productSerialNumber, String EmployeeID) {
		final String QUERY_PRODUCTSTOCK_GET_BY_SERIAL = "SELECT ps.*, p.ProductName FROM ProductStock as ps left outer join Product as p on ps.ProductID=p.ProductID WHERE ps.ProductSerialNumber = ? AND ps.CreateBy = ?";
		return executeQueryObject(QUERY_PRODUCTSTOCK_GET_BY_SERIAL, new String[] { productSerialNumber, EmployeeID }, ProductStockInfo.class);
	}


	public ProductStockInfo getProductStock(String productSerialNumber, ProductStockStatus status) {
		final String QUERY_PRODUCTSTOCK_GET_BY_SERIAL = "SELECT * FROM ProductStock WHERE ProductSerialNumber = ? AND Status = ?";
		return executeQueryObject(QUERY_PRODUCTSTOCK_GET_BY_SERIAL, new String[] { productSerialNumber, status.name() }, ProductStockInfo.class);
	}
	public ProductStockInfo getProductStock(String organizationCode, String productSerialNumber, String status) {
		String sql = "select ps.*, p.ProductName from ProductStock as ps left outer join Product as p on ps.ProductID=p.ProductID"
				+" where ps.OrganizationCode = ?"
				+" and ps.ProductSerialNumber = ?"
				+" and ps.Status = ?";
		return executeQueryObject(sql, new String[]{organizationCode, productSerialNumber, status}, ProductStockInfo.class);
	}

	public ProductStockInfo getProductStockByProductSerialNumber(String organizationCode, String productSerialNumber) {
		String sql = "SELECT * FROM ProductStock WHERE OrganizationCode = ? AND ProductSerialNumber = ? ";
		return executeQueryObject(sql, new String[] {organizationCode, productSerialNumber}, ProductStockInfo.class);
	}
	
	// For test
	public void checkedProducts() {
		final String QUERY_PRODUCTSTOCK_CHECKED = "UPDATE ProductStock SET Status = 'CHECKED' WHERE Status = 'WAIT'";
		executeNonQuery(QUERY_PRODUCTSTOCK_CHECKED, null);
	}

	public ProductStockInfo getCheckedProductStock() {
		final String QUERY_GET_FIRST_CHECKED = "SELECT * FROM ProductStock WHERE Status = 'CHECKED'";
		return executeQueryObject(QUERY_GET_FIRST_CHECKED, null, ProductStockInfo.class);
	}
	
	public List<ProductStockInfo> getProductStock(String organizationCode, String teamCode) {
		return executeQueryList("select *from ProductStock where OrganizationCode=? and TeamCode=?", new String[]{organizationCode, teamCode}, ProductStockInfo.class);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public void deleteProductStockAll() {
		executeNonQuery("DELETE FROM ProductStock", null);
	}
	public void deleteProductStockAllByTeamCode(String organizationCode, String teamCode) {
		String sql = "DELETE FROM ProductStock WHERE OrganizationCode = ? and ((TeamCode like ? || '%') OR (ScanByTeam like ? || '%')) ";
		executeNonQuery(sql, new String[]{organizationCode, teamCode, teamCode});
	}
	public void deleteProductStockByTeamCode(String organizationCode, String teamCode) {
		executeNonQuery(QUERY_PRODUCTSTOCK_DELETE, new String[] { organizationCode, teamCode });
	}
	public void deleteProductStockBySerialNumber(String organizationCode, String SerialNumber) {
		executeNonQuery("DELETE FROM ProductStock WHERE (OrganizationCode=?) AND (ProductSerialNumber=?)", new String[] { organizationCode, SerialNumber });
	}
	public void deleteProductStockByRefNo(String refNo) {
		String sql = "DELETE FROM ProductStock WHERE (ProductSerialNumber IN (SELECT ProductSerialNumber FROM Contract WHERE RefNo = ?))";
		executeNonQuery(sql, new String[]{refNo});
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////


}
