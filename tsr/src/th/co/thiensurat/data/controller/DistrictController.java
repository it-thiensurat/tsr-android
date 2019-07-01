package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.DistrictInfo;

public class DistrictController extends BaseController {

	private static final String QUERY_DISTRICT_GET_BY_ID = "SELECT * FROM District WHERE ProvinceCode = ? ORDER BY DistrictName";

	public List<DistrictInfo> getDistrict(String ProvinceCode) {
		List<DistrictInfo> ret = null;
		ret = executeQueryList(QUERY_DISTRICT_GET_BY_ID,
				new String[] { ProvinceCode }, DistrictInfo.class);
		return ret;
	}

	public List<DistrictInfo> getDistrictAll() {
		List<DistrictInfo> ret = null;
		String sql = "SELECT * FROM District ORDER BY DistrictName";
		ret = executeQueryList(sql, null, DistrictInfo.class);
		return ret;
	}
	
	public void addDistrict(DistrictInfo info) {
		String sql = "insert into District(DistrictCode, DistrictName, ProvinceCode)"
				+" values(?,?,?)";
		executeNonQuery(sql, new String[] {info.DistrictCode, info.DistrictName, info.ProvinceCode});		
	}
	
	public void deleteDistrict() {
		executeNonQuery("delete from District", null);
	}

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public DistrictInfo getDistrictByDistrictCode(String DistrictCode) {
        final String sql = "SELECT * FROM District WHERE DistrictCode = ?";
        return executeQueryObject(sql, new String[]{DistrictCode}, DistrictInfo.class);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

	public DistrictInfo getDistrictByProvinceCodeAndDistrictName(String ProvinceCode, String DistrictName) {
		final String sql = "SELECT * FROM District WHERE ProvinceCode = ? and DistrictName = ?";
		return executeQueryObject(sql, new String[]{ProvinceCode, DistrictName}, DistrictInfo.class);
	}
}
