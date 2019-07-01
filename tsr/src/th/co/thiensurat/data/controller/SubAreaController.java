package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.SubAreaInfo;

public class SubAreaController extends BaseController {


	public void addSubArea(SubAreaInfo info) {
		final String QUERY_ADD = "INSERT INTO SubArea(SubAreaCode, AreaCode, ProvinceCode, DistrictCode, SubDistrictCode, Detail, Zipcode, SyncedDate, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy) "
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(QUERY_ADD, new String[] { info.SubAreaCode, info.AreaCode, info.ProvinceCode, info.DistrictCode, info.SubDistrictCode, info.Detail, info.Zipcode, valueOf(info.SyncedDate), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy });
	}
	
	public void deleteSubAreaAll() {
		executeNonQuery("DELETE FROM SubArea", null);
	}
}
