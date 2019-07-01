package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.AddressTypeInfo;

public class AddressTypeController extends BaseController {

	public void addAddressType(AddressTypeInfo info) {
		String sql = "insert into AddressType(AddressTypeCode,AddressTypeName)"
					+" values(?,?)";
		executeNonQuery(sql, new String[] {info.AddressTypeCode, info.AddressTypeName});
	}
	
	public void deleteAddressType() {
		executeNonQuery("delete from AddressType", null);
	}
}
