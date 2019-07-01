package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ProductInfo;

public class ProductController extends BaseController {


	public List<ProductInfo> getProduct() {
		return executeQueryList("select *from Product", null, ProductInfo.class);
	}
	
	public ProductInfo getProduct(String organizationCode, String productID) {
		final String QUERY_PRODUCT_GET_BY_ID = "SELECT * FROM Product WHERE OrganizationCode = ? AND ProductID = ?";
		return executeQueryObject(QUERY_PRODUCT_GET_BY_ID, new String[] { organizationCode, productID }, ProductInfo.class);
	}
	
	public void addProduct(ProductInfo info)
	{
		final String QUERY_ADD_PRODUCT = "insert into Product (ProductID, OrganizationCode, ProductName, ProductCode, ProductModel, ProductPrice, CashDiscount, CreditDiscount, ProductDescription, Type, SyncedDate) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(QUERY_ADD_PRODUCT, new String[] {info.ProductID, info.OrganizationCode, info.ProductName, info.ProductCode, info.ProductModel, Float.toString(info.ProductPrice), Float.toString(info.CashDiscount), Float.toString(info.CreditDiscount), info.ProductDescription, info.Type, info.SyncedDate});
	}
	
	public void deleteProductAll() {
		executeNonQuery("DELETE FROM Product", null);
	}
}
