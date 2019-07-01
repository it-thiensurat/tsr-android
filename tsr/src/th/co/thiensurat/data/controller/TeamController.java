package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.TeamInfo;

public class TeamController extends BaseController {


	public void addTeam(TeamInfo info) {
		String sql = "INSERT INTO Team (Code, OrganizationCode, Name, NoPrint, TeamType, SourceSystem)"
					+" VALUES (?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql, new String[] {info.Code, info.OrganizationCode, info.Name, valueOf(info.NoPrint), info.TeamType, info.SourceSystem});
	}
	
	public void deleteTeamByCode(String code) {
		String sql = "DELETE FROM Team WHERE Code = ?";
		executeNonQuery(sql, new String[] {code});
	}

	public void deleteTeamAll() {
		String sql = "DELETE FROM Team";
		executeNonQuery(sql, null);
	}

	public TeamInfo getTeamByID(String code) {
		String sql = "select * from Team where Code=?";
		return executeQueryObject(sql, new String[] {code}, TeamInfo.class);
	}
	public List<TeamInfo> getTeamBySup(String supvisorcode) {
		String sql = "select * from Team where supervisorcode = ?";
		return executeQueryList(sql, new String[] { supvisorcode }, TeamInfo.class);
	}
	public List<TeamInfo> getTeam() {
		final String QUERY_TEAM_GET_ALL = "SELECT * FROM TEAM";
		return executeQueryList(QUERY_TEAM_GET_ALL, null, TeamInfo.class);
	}
//	List<FortnightInfo> ret = null;
//	ret = executeQueryList(sql, new String[] { organizationCode }, FortnightInfo.class);
//	return ret;
}
