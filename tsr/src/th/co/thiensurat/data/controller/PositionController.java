package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.PositionInfo;

public class PositionController extends BaseController {
    public enum PositionCode {
        Sale, SubTeamLeader, SaleLeader, Credit, CreditSubTeamLeader, CreditTeamLeader
    }

	public void addPosition(PositionInfo info) {
		String sql="insert into Position([PositionID],[OrganizationCode],[PositionName],[ParentPosition],[PositionLevel], [SourceSystem])"
				+" values(?,?,?,?,?,?)";
		executeNonQuery(sql, new String[] {info.PositionID, info.OrganizationCode, info.PositionName, info.ParentPosition, valueOf(info.PositionLevel), info.SourceSystem});
	}
	
	public void deletePosition() {
		executeNonQuery("delete from Position", null);
	}
}
