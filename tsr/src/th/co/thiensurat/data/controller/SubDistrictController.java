package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.SubDistrictInfo;

public class SubDistrictController extends BaseController {

	private static final String QUERY_DISTRICT_GET_BY_ID = "SELECT * FROM SubDistrict WHERE DistrictCode = ? ORDER BY SubDistrictName";

	public List<SubDistrictInfo> getSubDistrict(String DistrictCode) {
		List<SubDistrictInfo> ret = null;

		ret = executeQueryList(QUERY_DISTRICT_GET_BY_ID,
				new String[] { DistrictCode }, SubDistrictInfo.class);

		return ret;
	}

	public List<SubDistrictInfo> getSubDistrictAll() {
		List<SubDistrictInfo> ret = null;
		String sql = "SELECT * FROM SubDistrict ORDER BY SubDistrictName";
		ret = executeQueryList(sql, null, SubDistrictInfo.class);
		return ret;
	}
	
	public void addSubDistrict(SubDistrictInfo info) {
		String sql = "insert into SubDistrict(SubDistrictCode, SubDistrictName, DistrictCode, Postcode)"
				+" values(?,?,?,?)";
		executeNonQuery(sql, new String[] {info.SubDistrictCode, info.SubDistrictName, info.DistrictCode, info.Postcode});
	}
	
	public void deleteSubDistrict() {
		executeNonQuery("delete from SubDistrict", null);
	}

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public SubDistrictInfo getSubDistrictBySubDistrictCode(String SubDistrictCode) {
        final String sql = "SELECT * FROM SubDistrict WHERE SubDistrictCode = ?";
        return executeQueryObject(sql, new String[]{SubDistrictCode}, SubDistrictInfo.class);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

	public SubDistrictInfo getSubDistrictByDistrictCodeAndSubDistrictName(String DistrictCode, String SubDistrictName) {
		final String sql = "SELECT * FROM SubDistrict WHERE DistrictCode = ? and SubDistrictName = ?";
		return executeQueryObject(sql, new String[]{DistrictCode, SubDistrictName}, SubDistrictInfo.class);
	}
}
