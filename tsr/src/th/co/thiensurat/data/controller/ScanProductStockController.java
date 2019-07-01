package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.ProductStockInfo;

public class ScanProductStockController extends BaseController {

	public ProductStockInfo getScanProductStockInfo(String productSerialNumber, String organizationCode, String status) {
		final String QUERY_SCANPRODUCTSTOCK_GET_BY_ID = "SELECT * FROM ProductStock WHERE ProductSerialNumber = ? AND OrganizationCode = ? AND Status = ?";
		return executeQueryObject(QUERY_SCANPRODUCTSTOCK_GET_BY_ID, new String[] { productSerialNumber, organizationCode, status }, ProductStockInfo.class);
	}
	
	public ProductStockInfo getScanProductStockInfoJoinProduct(String ProductSerialNumber, String OrganizationCode, String Status) {
		final String QUERY_SCANPRODUCTSTOCK_JOIN_PRODUCT_GET_BY_ID = "SELECT ProductStock.ProductSerialNumber, " +
				"ProductStock.OrganizationCode, " +
				"ProductStock.ProductID, " +
				"ProductStock.Type, " +
				"ProductStock.TeamCode, " +
				"ProductStock.Status, " +
				"ProductStock.ImportDate, " +
				"ProductStock.ScanDate, " +
				"Product.ProductName " +
				"FROM ProductStock LEFT JOIN Product on ProductStock.ProductID = Product.ProductID " +
				"WHERE ProductStock.ProductSerialNumber = ? AND ProductStock.OrganizationCode = ? AND ProductStock.Status = ? ";
		return executeQueryObject(QUERY_SCANPRODUCTSTOCK_JOIN_PRODUCT_GET_BY_ID, new String[] { ProductSerialNumber, OrganizationCode, Status }, ProductStockInfo.class);
	}
		
}
