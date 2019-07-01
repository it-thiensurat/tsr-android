package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.OrganizationInfo;

public class OrganizationController extends BaseController {

	public void addOrganization(OrganizationInfo info) {
		String sql="insert into Organization(OrganizationCode,OrganizationName,OrganizationDescription,DisplayOrder)"
				+" values(?,?,?,?)";
		executeNonQuery(sql, new String[] {info.OrganizationCode, info.OrganizationName, info.OrganizationDescription, valueOf(info.DisplayOrder)});
	}
	
	public void deleteOrganization() {
		executeNonQuery("delete from Organization", null);
	}
}
