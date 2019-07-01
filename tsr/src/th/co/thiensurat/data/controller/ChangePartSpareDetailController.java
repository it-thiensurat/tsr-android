package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ChangePartSpareDetailInfo;
import th.co.thiensurat.data.info.ChangePartSpareInfo;

public class ChangePartSpareDetailController extends BaseController {

	public List<ChangePartSpareDetailInfo> getChangePartSpareDetailByRequestTeamCode(String organizationCode, String requestTeamCode) {
		String sql = "SELECT * FROM ChangePartSpare WHERE (OrganizationCode = ?) AND (RequestTeamCode = ?) ";
		return executeQueryList(sql, new String[]{organizationCode, requestTeamCode}, ChangePartSpareDetailInfo.class);
	}

	public List<ChangePartSpareDetailInfo> getChangePartSpareDetailByChangePartSpareID(String organizationCode, String changePartSpareID) {
		String sql = "SELECT * FROM ChangePartSpare WHERE (OrganizationCode = ?) AND (ChangePartSpareID = ?) ";
		return executeQueryList(sql, new String[]{organizationCode, changePartSpareID}, ChangePartSpareDetailInfo.class);
	}

	public void addChangePartSpareDetail(ChangePartSpareDetailInfo info) {
		String sql = "INSERT INTO [ChangePartSpareDetail](ChangePartSpareID, PartSpareID, QTYUsed, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql, new String[] { info.ChangePartSpareID, info.PartSpareID, valueOf(info.QTYUsed), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate)});
	}
	
	public void updateChangePartSpareDetail(ChangePartSpareDetailInfo info) {
		String sql = "UPDATE [ChangePartSpare] "
				+ " SET QTYUsed = ? "
				+ "		, LastUpdateDate = ? "
				+ "		, LastUpdateBy = ? "
				+ " WHERE (ChangePartSpareID = ?) AND (PartSpareID = ?)";
		executeNonQuery(sql, new String[] {valueOf(info.QTYUsed), valueOf(info.LastUpdateDate), info.LastUpdateBy, info.ChangePartSpareID, info.PartSpareID});
	}

	public void deleteChangePartSpareDetailAll() {
		String sql = "DELETE FROM [ChangePartSpareDetail]";
		executeNonQuery(sql, null);
	}

}
