package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.AreaEmployeeInfo;

public class AreaEmployeeController extends BaseController {


	public void addAreaEmployee(AreaEmployeeInfo info) {
		final String QUERY_ADD = "INSERT INTO AreaEmployee(EmployeeCode, SourceSystem, AreaCode, SyncedDate, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy) "
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(QUERY_ADD, new String[] { info.EmployeeCode, info.SourceSystem, info.AreaCode, valueOf(info.SyncedDate), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy });
	}
	
	public void deleteAreaEmployeeAll() {
		executeNonQuery("DELETE FROM AreaEmployee", null);
	}
}
