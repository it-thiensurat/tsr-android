package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.WriteOffNPLInfo;

public class WriteOffNPLController extends BaseController {

	public void addWriteOffNPL(WriteOffNPLInfo info) {
		String sql = "INSERT INTO WriteOffNPL(WriteOffNPLID, OrganizationCode, RefNo, Status, "
				+ "			RequestProblemID, RequestDetail, RequestDate, RequestBy, RequestTeamCode, "
				+ "			ApproveDetail, ApprovedDate, ApprovedBy, "
				+ "			ResultProblemID, ResultDetail, EffectiveDate, EffectiveBy, "
				+ "			WriteOffNPLPaperID, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, "
				+ " 		RequestEmployeeLevelPath, EffectiveEmployeeLevelPath) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		executeNonQuery(sql, new String[] { info.WriteOffNPLID, info.OrganizationCode, info.RefNo, info.Status, info.RequestProblemID, info.RequestDetail,
				valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode, info.ApproveDetail, valueOf(info.ApprovedDate), info.ApprovedBy,
				info.RequestProblemID, info.ResultDetail, valueOf(info.EffectiveDate), info.EffectiveBy, info.WriteOffNPLPaperID, valueOf(info.CreateDate),
				info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.RequestEmployeeLevelPath, info.EffectiveEmployeeLevelPath });
	}

	public void updateWriteOffNPL(WriteOffNPLInfo info) {

		String sql = "";

		if (info.RequestBy != null && !info.RequestBy.trim().equals("")) {
			sql = "UPDATE WriteOffNPL "
				+ " SET OrganizationCode = ?, RefNo = ?, Status = ?, "
				+ "		RequestProblemID = ?, RequestDetail = ?, RequestDate = ?, RequestBy = ?, RequestTeamCode = ?, "
				+ "		WriteOffNPLPaperID = ?, LastUpdateDate = datetime('now'), LastUpdateBy = ?, RequestEmployeeLevelPath = ? "
				+ " WHERE (WriteOffNPLID = ?)";
		} else if (info.ApprovedBy != null && !info.ApprovedBy.trim().equals("")) {
			sql = "UPDATE WriteOffNPL "
				+ " SET OrganizationCode = ?, RefNo = ?, Status = ?, "
				+ "		ApproveDetail = ?, ApprovedDate = ?, ApprovedBy = ?, "
				+ "		WriteOffNPLPaperID = ?, LastUpdateDate = datetime('now'), LastUpdateBy = ? "
				+ " WHERE (WriteOffNPLID = ?)";
		} else if (info.EffectiveBy != null && !info.EffectiveBy.trim().equals("")) {
			sql = "UPDATE WriteOffNPL "
				+ " SET OrganizationCode = ?, RefNo = ?, Status = ?, "
				+ "		ResultProblemID = ?, ResultDetail = ?, EffectiveDate = ?, EffectiveBy = ?, "
				+ "		WriteOffNPLPaperID = ?, LastUpdateDate = datetime('now'), LastUpdateBy = ?, EffectiveEmployeeLevelPath = ? "
				+ " WHERE (WriteOffNPLID = ?)";
		}

		if (info.RequestBy != null && !info.RequestBy.trim().equals("")) {
			executeNonQuery(sql, new String[] { info.OrganizationCode, info.RefNo, info.Status,
				info.RequestProblemID, info.RequestDetail, valueOf(info.RequestDate), info.RequestBy, info.RequestTeamCode,
				info.WriteOffNPLPaperID, info.LastUpdateBy, info.RequestEmployeeLevelPath, info.WriteOffNPLID });
		} else if (info.ApprovedBy != null && !info.ApprovedBy.trim().equals("")) {
			executeNonQuery(sql, new String[] { info.OrganizationCode, info.RefNo, info.Status,
				info.ApproveDetail, valueOf(info.ApprovedDate), info.ApprovedBy,
				info.WriteOffNPLPaperID, info.LastUpdateBy, info.WriteOffNPLID });
		} else if (info.EffectiveBy != null && !info.EffectiveBy.trim().equals("")) {
			executeNonQuery(sql, new String[] { info.OrganizationCode, info.RefNo, info.Status,
				info.ResultProblemID, info.ResultDetail, valueOf(info.EffectiveDate), info.EffectiveBy,
				info.WriteOffNPLPaperID, info.LastUpdateBy, info.EffectiveEmployeeLevelPath, info.WriteOffNPLID });
		}
	}
}