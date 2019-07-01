package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.RoleInfo;

public class RoleController extends BaseController {
	
	public void addRole(RoleInfo info) {
		executeNonQuery("insert into Role(RoleCode,  RoleName) values(?,?)", new String[] {info.RoleCode, info.RoleName});
	}
	
	public void deleteRole() {
		executeNonQuery("delete from Role", null);
	}
}
