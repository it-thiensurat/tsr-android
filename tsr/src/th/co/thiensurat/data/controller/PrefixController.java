package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.PrefixInfo;

public class PrefixController extends BaseController {

	public List<PrefixInfo> getPrefixes() {
		final String QUERY_PREFIX_GET_ALL = "SELECT * FROM Prefix ORDER BY PrefixCode DESC";
		return executeQueryList(QUERY_PREFIX_GET_ALL, null, PrefixInfo.class);
	}



	
	public List<PrefixInfo> getPrefixByPrefixCode(String prefixCode) {
		List<PrefixInfo> ret = null;
		String sql = "SELECT * FROM Prefix where PrefixCode like ?";
		ret = executeQueryList(sql, new String[] {prefixCode}, PrefixInfo.class);
		return ret;
	}

	public PrefixInfo getPrefixeByPrefixName(String prefixName) {
		String sql = "SELECT * FROM Prefix where PrefixName = ?";
		return executeQueryObject(sql, new String[] {prefixName}, PrefixInfo.class);
	}

	public void addPrefix(PrefixInfo info) {
		String sql = "insert into Prefix(PrefixCode, PrefixName)"
				+" values(?,?)";
		executeNonQuery(sql, new String[] {info.PrefixCode, info.PrefixName});
	}
	
	public void deletePrefix() {
		executeNonQuery("delete from Prefix", null);
	}

}
