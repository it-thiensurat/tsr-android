package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.SaleTypeInfo;

public class SaleTypeController extends BaseController {

	
	public void addSaleType(SaleTypeInfo info) {
		String sql = "insert into SaleType(SaleTypeCode, OrganizationCode, SaleTypeName, PositionID)"
				+" values(?,?,?,?)";
		executeNonQuery(sql, new String[] {info.SaleTypeCode, info.OrganizationCode, info.SaleTypeName, info.PositionID});
	}
	
	public void deleteSaleType() {
		executeNonQuery("delete from SaleType", null);
	}
}
