package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ProvinceInfo;

public class ProvinceController extends BaseController {

	public List<ProvinceInfo> getProvinces() {
		final String QUERY_PROVINCE_GET_ALL = "SELECT * FROM Province ORDER BY ProvinceName";
		return executeQueryList(QUERY_PROVINCE_GET_ALL, null, ProvinceInfo.class);
	}
	
	public void addProvince(ProvinceInfo info) {
		String sql="insert into Province(ProvinceCode, ProvinceName)"
				+" values(?,?)";
		executeNonQuery(sql, new String[] {info.ProvinceCode, info.ProvinceName});
	}
	
	public void deleteProvince() {
		executeNonQuery("delete from Province", null);
	}

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public ProvinceInfo getProvinceByProvinceCode(String ProvinceCode) {
        final String sql = "SELECT * FROM Province WHERE ProvinceCode = ?";
        return executeQueryObject(sql, new String[]{ProvinceCode}, ProvinceInfo.class);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

	public ProvinceInfo getProvinceByProvinceName(String ProvinceName) {
		final String sql = "SELECT * FROM Province WHERE ProvinceName = ?";
		return executeQueryObject(sql, new String[]{ProvinceName}, ProvinceInfo.class);
	}
}
