package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ChangePartSpareInfo;
import th.co.thiensurat.data.info.ChangeProductInfo;

public class ChangePartSpareController extends BaseController {

	public enum ChangePartSpareStatus {
		REQUEST, APPROVED, COMPLETED , REJECT
	}

	public List<ChangePartSpareInfo> getChangePartSpareByRequestTeamCode(String organizationCode, String requestTeamCode) {
		String sql = "SELECT * FROM ChangePartSpare WHERE (OrganizationCode = ?) AND (RequestTeamCode = ?) ";
		return executeQueryList(sql, new String[]{organizationCode, requestTeamCode}, ChangePartSpareInfo.class);
	}

	public List<ChangePartSpareInfo> getChangePartSpareByRequestEmpID(String organizationCode, String requestBy) {
		String sql = "SELECT * FROM ChangePartSpare WHERE (OrganizationCode = ?) AND (RequestBy = ?) ";
		return executeQueryList(sql, new String[]{organizationCode, requestBy}, ChangePartSpareInfo.class);
	}

	public void addChangePartSpare(ChangePartSpareInfo info) {
		String sql = "INSERT INTO [ChangePartSpare](ChangePartSpareID, OrganizationCode, RefNo, Status, RequestDate, RequestBy, RequestTeamCode, RequestProblemDetail "
				+ "		, ApprovedDate, ApprovedBy, ApproveDetail, EffectiveDate, EffectiveBy, EffectiveDetail, ChangePartSparePaperID "
				+ "		, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql, new String[] { info.ChangePartSpareID, info.OrganizationCode, info.RefNo, info.Status, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode, info.RequestProblemDetail
						, valueOf(info.ApprovedDate), info.ApprovedBy, info.ApproveDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.EffectiveDetail, info.ChangePartSparePaperID
						, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate)});
	}
	
	public void updateChangePartSpare(ChangePartSpareInfo info) {
		String sql = "UPDATE [ChangePartSpare] "
				+ " SET RefNo = ? "
				+ " 	, [Status] = ? "
				+ " 	, RequestDate = ? "
				+ " 	, RequestBy = ? "
				+ " 	, RequestTeamCode = ? "
				+ " 	, RequestProblemDetail = ? "
				+ " 	, ApprovedDate = ? "
				+ " 	, ApprovedBy = ? "
				+ " 	, ApproveDetail = ? "
				+ " 	, EffectiveDate = ? "
				+ " 	, EffectiveBy = ? "
				+ " 	, EffectiveDetail = ? "
				+ " 	, ChangePartSparePaperID = ? "
				+ "		, LastUpdateDate = ? "
				+ "		, LastUpdateBy = ? "
				+ " WHERE (OrganizationCode = ?) AND (ChangePartSpareID = ?)";
		executeNonQuery(sql, new String[] {info.RefNo, info.Status, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode, info.RequestProblemDetail
				, valueOf(info.ApprovedDate), info.ApprovedBy, info.ApproveDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.EffectiveDetail, info.ChangePartSparePaperID
				, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.OrganizationCode, info.ChangePartSpareID});
	}

	public void deleteChangePartSpareAll() {
		String sql = "DELETE FROM [ChangePartSpare]";
		executeNonQuery(sql, null);
	}

}
