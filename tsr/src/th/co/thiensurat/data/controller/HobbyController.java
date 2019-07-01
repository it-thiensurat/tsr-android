package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.HobbyInfo;

public class HobbyController extends BaseController {

	private static final String QUERY_HOBBY_GET_ALL = "SELECT * FROM Hobby";

	public List<HobbyInfo> getHobby() {
		return executeQueryList(QUERY_HOBBY_GET_ALL, null, HobbyInfo.class);
	}

    public HobbyInfo getHobbyByHobbyCode (String HobbyCode ) {
        HobbyInfo ret = null;
        String sql = "SELECT * FROM Hobby where HobbyCode = ?";
        ret = executeQueryObject(sql, new String[]{HobbyCode}, HobbyInfo.class);
        return ret;
    }

	public void addHobby(HobbyInfo info) {
		String sql = "insert into Hobby(HobbyCode,hobbyName)"
					+" values(?,?)";	
		executeNonQuery(sql, new String[] {info.HobbyCode, info.HobbyName});
	}
	
	public void deleteHobby() {
		executeNonQuery("delete from Hobby", null);
	}

}
