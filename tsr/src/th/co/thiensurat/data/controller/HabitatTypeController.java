package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.HabitatTypeInfo;

public class HabitatTypeController extends BaseController {

	private static final String QUERY_HABITATTYPE_GET_ALL = "SELECT * FROM HabitatType";

	public List<HabitatTypeInfo> getHabitatType() {
		return executeQueryList(QUERY_HABITATTYPE_GET_ALL, null, HabitatTypeInfo.class);
	}

    public HabitatTypeInfo getHabitatTypeByHabitatTypeCode (String HabitatTypeCode ) {
        HabitatTypeInfo ret = null;
        String sql = "SELECT * FROM HabitatType where HabitatTypeCode = ?";
        ret = executeQueryObject(sql, new String[]{HabitatTypeCode}, HabitatTypeInfo.class);
        return ret;
    }

	public void addHabitatType(HabitatTypeInfo info) {
		String sql = "insert into HabitatType(HabitatTypeCode, HabitatTypeName)"
				+" values(?,?)";
		executeNonQuery(sql, new String[] {info.HabitatTypeCode, info.HabitatTypeName});
	}
	
	public void deleteHabitatType() {
		executeNonQuery("delete from HabitatType", null);
	}

}
