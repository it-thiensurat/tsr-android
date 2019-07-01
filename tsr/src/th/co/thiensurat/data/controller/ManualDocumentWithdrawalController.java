package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ManualDocumentWithdrawalInfo;
import  th.co.thiensurat.data.info.ManualDocumentInfo;

public class ManualDocumentWithdrawalController extends BaseController {
	
	public List<ManualDocumentWithdrawalInfo> getManualDocumentWithdrawalByVolumeNo(String volumeNo) {
		String sql = "SELECT * FROM ManualDocumentWithdrawal WHERE VolumeNo = ? ORDER BY StartNo";
		return executeQueryList(sql, new String[] { volumeNo }, ManualDocumentWithdrawalInfo.class);
	}
	
	public void addManualDocumentWithdrawal(ManualDocumentWithdrawalInfo info) {
		String sql = "INSERT INTO ManualDocumentWithdrawal(ManualDocID, TeamCode, SubTeamCode, EmpID, "
					+ " ManualDocTypeID, VolumeNo, StartNo, EndNo, WithdrawalDate, "
					+ " ReturnDate, ReturnBy, ReceivedBy, ReceivedDate, ReturnStartNo, ReturnEndNo, "
					+ " CreatedBy, CreatedDate, UpdateBy, UpdateDate, Remark, ReturnRemark, IsReturnComplete) "
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql, new String[] { info.ManualDocID, info.TeamCode, info.SubTeamCode, info.EmpID, 
					info.ManualDocTypeID, info.VolumeNo, valueOf(info.StartNo), valueOf(info.EndNo), valueOf(info.WithdrawalDate),
					valueOf(info.ReturnDate), info.ReturnBy, info.ReceivedBy, valueOf(info.ReceivedDate), valueOf(info.ReturnStartNo), valueOf(info.ReturnEndNo),
					info.CreatedBy, valueOf(info.CreatedDate), info.UpdateBy, valueOf(info.UpdateDate), info.Remark, info.ReturnRemark, valueOf(info.IsReturnComplete) });
	}

	public void deleteManualDocumentWithdrawalAll() {
        String sql = "DELETE FROM ManualDocumentWithdrawal";
        executeNonQuery(sql, null);
    }
	
	public ManualDocumentWithdrawalInfo getManualDocumentWithdrawalByType(String manualDocTypeID) {
		//String sql = "SELECT * FROM ManualDocument m inner join  ManualDocumentWithdrawal mw on m.ManualDocTypeID = mw.ManualDocTypeID where  m.ManualDocTypeID = ? ORDER BY ManualRunningNo DESC";
        String sql = "SELECT * FROM ManualDocumentWithdrawal where  ManualDocTypeID = ? ORDER BY ManualDocID DESC";
        return executeQueryObject(sql, new String[] { manualDocTypeID }, ManualDocumentWithdrawalInfo.class);
	}

	public ManualDocumentWithdrawalInfo getManualDocumentWithdrawalByVolumeNoASC(String manualDocTypeID, String EmpID) {
		String sql = "SELECT mdw.VolumeNo, MAX(IFNULL(md.ManualRunningNo, 0)) as MaxRunningNo, mdw.EndNo, mdw.CreatedDate, mdw.ManualDocID, mdw.ManualDocTypeID, mdw.StartNo " +
				" FROM ManualDocumentWithdrawal mdw " +
				" LEFT JOIN ManualDocument md ON (md.ManualVolumeNo = mdw.VolumeNo) AND (md.ManualDocTypeID = mdw.ManualDocTypeID)" +
				//"LEFT JOIN ManualDocumentType mdt ON mdt.ManualDocTypeID = mdw.ManualDocTypeID and mdt.ManualDocTypeID = md.ManualDocTypeID " +
				" WHERE mdw.ManualDocTypeID = ? AND mdw.EmpID = ?" +
				//"AND (mdw.TeamCode = 'AAA-01') AND (mdw.IsDelete = 0) AND (mdw.ReturnDate IS NULL) " +
				" GROUP BY mdw.VolumeNo, mdw.EndNo, mdw.CreatedDate, mdw.ManualDocID, mdw.ManualDocTypeID, mdw.StartNo " +
				" Having max(IFNULL(md.ManualRunningNo, 0)) < mdw.EndNo " +
				" ORDER BY mdw.CreatedDate ASC " +
				" LIMIT 1";
		return executeQueryObject(sql, new String[] { manualDocTypeID, EmpID }, ManualDocumentWithdrawalInfo.class);
	}

    public ManualDocumentWithdrawalInfo getManualDocumentWithdrawalByVolume(String manualDocTypeID, String VolumeNo, String EmpID) {
        String sql = "SELECT mdw.VolumeNo, MAX(IFNULL(md.ManualRunningNo, 0)) as MaxRunningNo, mdw.EndNo, mdw.CreatedDate, mdw.ManualDocID, mdw.ManualDocTypeID, mdw.StartNo " +
                "FROM ManualDocumentWithdrawal mdw " +
                "LEFT JOIN ManualDocument md ON (md.ManualVolumeNo = mdw.VolumeNo) AND (md.ManualDocTypeID = mdw.ManualDocTypeID) " +
                "LEFT JOIN ManualDocumentType mdt ON mdt.ManualDocTypeID = mdw.ManualDocTypeID and mdt.ManualDocTypeID = md.ManualDocTypeID " +
                "WHERE mdw.ManualDocTypeID = ? AND mdw.VolumeNo = ? AND mdw.EmpID = ?" +
                //"AND (mdw.TeamCode = 'AAA-01') AND (mdw.IsDelete = 0) AND (mdw.ReturnDate IS NULL) " +
                "GROUP BY mdw.VolumeNo, mdw.EndNo, mdw.CreatedDate, mdw.ManualDocID, mdw.ManualDocTypeID, mdw.StartNo " +
                "Having max(IFNULL(md.ManualRunningNo, 0)) < mdw.EndNo " +
                "ORDER BY mdw.CreatedDate ASC " +
                "LIMIT 1";
        return executeQueryObject(sql, new String[] { manualDocTypeID, VolumeNo, EmpID }, ManualDocumentWithdrawalInfo.class);
    }


	public ManualDocumentInfo getManualDocumentByNumber(String manualDocTypeID, String VolumnNo, String DocumentNumber) {

		String sql = " SELECT DocumentID, DocumentType, DocumentNumber, ManualDocTypeID, ManualVolumeNo, ManualRunningNo, Note, TeamCode, SubTeamCode, EmpID, CreatedBy, CreatedDate, UpdateBy, UpdateDate, SyncedDate, isActive " +
				"  FROM ManualDocument " +
				"  where  ManualDocTypeID = ? and ManualVolumeNo = ? and ManualRunningNo = ?";
		return executeQueryObject(sql, new String[]{manualDocTypeID, VolumnNo, DocumentNumber}, ManualDocumentInfo.class);
	}
}
