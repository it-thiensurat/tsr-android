package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.AreaInfo;

public class AreaController extends BaseController {


	public void addArea(AreaInfo info) {
		final String QUERY_ADD = "INSERT INTO Area(AreaCode, AreaName, TeamCode, SyncedDate, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SourceSystem) "
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(QUERY_ADD, new String[] { info.AreaCode, info.AreaName, info.TeamCode, valueOf(info.SyncedDate), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.SourceSystem });
	}
	
	public void deleteAreaAll() {
		executeNonQuery("DELETE FROM Area", null);
	}
}
