package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.SubTeamInfo;

public class SubTeamController extends BaseController {


	public void addSubTeam(SubTeamInfo info) {
		String sql = "INSERT INTO SubTeam (SubTeamCode, TeamCode, SubTeamName, SourceSystem)"
					+" VALUES (?, ?, ?, ?)";
		executeNonQuery(sql, new String[] {info.SubTeamCode, info.TeamCode, info.SubTeamName, info.SourceSystem});
	}
	
//	public void deleteSubTeamByTeamCode(String teamCode) {
//		String sql = "DELETE FROM SubTeam WHERE TeamCode=?";
//		executeNonQuery(sql, new String[] {teamCode});
//	}
	public void deleteSubTeamAll() {
		String sql = "delete from SubTeam";
		executeNonQuery(sql, null);
	}

	public List<SubTeamInfo> getSubTeamByTeamCode(String teamCode) {
		String sql ="SELECT * " +
				" FROM SubTeam " +
				" WHERE TeamCode = ? " +
				" ORDER BY SubTeamCode ASC";
		return executeQueryList(sql, new String[]{teamCode}, SubTeamInfo.class);
	}

	public List<SubTeamInfo> getSubTeamBySubTeamCode(String SubTeamCode) {
		String sql ="SELECT * " +
				" FROM SubTeam " +
				" WHERE SubTeamCode = ? " +
				" ORDER BY SubTeamCode ASC";
		return executeQueryList(sql, new String[]{SubTeamCode}, SubTeamInfo.class);
	}

}
