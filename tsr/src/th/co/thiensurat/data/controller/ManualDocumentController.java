package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ManualDocumentInfo;

public class ManualDocumentController extends BaseController {
	
	/*
	public static enum DocumentType {
	        Contract("0"), Receipt("1"), ChangeProduct("2"), ImpoundProduct("3"), ChangeContract("4"), SendMoney("5");
	        private String value;

	        private DocumentType(String val) {
	            this.value = val;
	        }

	        @Override
	        public String toString() {
	            return value;
	        }
	    }
	*/

	public List<ManualDocumentInfo> getManualDocumentForContract(String documentNumber, String DocumentType, String ManualDocTypeID) {
		String sql = "SELECT * FROM ManualDocument WHERE DocumentNumber = ? " +
				" AND DocumentType = ? and ManualDocTypeID = ? ORDER BY CreatedDate DESC";
		return executeQueryList(sql, new String[]{documentNumber, DocumentType, ManualDocTypeID}, ManualDocumentInfo.class);
	}

	public List<ManualDocumentInfo> getManualDocumentByDocumentNumber(String documentNumber) {
		String sql = "SELECT * FROM ManualDocument WHERE DocumentNumber = ? ORDER BY ManualRunningNo DESC ";
		return executeQueryList(sql, new String[] { documentNumber }, ManualDocumentInfo.class);
	}
	
	public ManualDocumentInfo getManualDocumentContractByDocumentNumber(String documentNumber) {
		String sql = "SELECT * FROM ManualDocument WHERE DocumentNumber = ? ";
		return executeQueryObject(sql, new String[]{documentNumber}, ManualDocumentInfo.class);
	}

	public List<ManualDocumentInfo> getManualDocumentForChangeContract(String documentNumber, String DocumentType, String ManualDocTypeID) {
		String sql = "SELECT * FROM ManualDocument WHERE DocumentNumber = ? " +
				" AND DocumentType = ? and ManualDocTypeID = ? ";
		return executeQueryList(sql, new String[]{documentNumber, DocumentType, ManualDocTypeID}, ManualDocumentInfo.class);
	}

	public ManualDocumentInfo getManualDocumentByDocumentID(String documentID) {
		String sql = "SELECT * FROM ManualDocument WHERE DocumentID = ? ";
		return executeQueryObject(sql, new String[] { documentID }, ManualDocumentInfo.class);
	}

    public ManualDocumentInfo getManualDocumentByManualDocTypeID(String ManualDocTypeID) {
        String sql = "SELECT * FROM ManualDocument WHERE ManualDocTypeID = ? ORDER BY ManualRunningNo DESC ";
        return executeQueryObject(sql, new String[] { ManualDocTypeID }, ManualDocumentInfo.class);
    }
	/*
	public void addManualDocument(ManualDocumentInfo manualDocumentInfo) {
		String sql = "insert into ManualDocument (DocumentID, DocumentNo, DocumentType, ManualBookNo, ManualRunningNo, Note) "
				+ "values (?,?,?,?,?,?)";
		executeNonQuery(sql, new String[] { manualDocumentInfo.DocumentID, manualDocumentInfo.DocumentNo, manualDocumentInfo.DocumentType, manualDocumentInfo.ManualBookNo, manualDocumentInfo.ManualRunningNo, manualDocumentInfo.Note });
	}
	*/
	
	public void addManualDocument(ManualDocumentInfo info) {
		String sql = "INSERT INTO ManualDocument(DocumentID, DocumentType, DocumentNumber, ManualDocTypeID, ManualVolumeNo, "
					+ " ManualRunningNo, Note, TeamCode, SubTeamCode, EmpID, "
					+ " CreatedBy, CreatedDate, UpdateBy, UpdateDate, SyncedDate, isActive) "
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql, new String[] { info.DocumentID, info.DocumentType, info.DocumentNumber, info.ManualDocTypeID, info.ManualVolumeNo, 
						valueOf(info.ManualRunningNo), info.Note, info.TeamCode, info.SubTeamCode, info.EmpID, 
						info.CreatedBy, valueOf(info.CreatedDate), info.UpdateBy, valueOf(info.UpdateDate), valueOf(info.SyncedDate), valueOf(info.isActive) });
	}
	
	public void updateManualDocument(ManualDocumentInfo info) {
		String sql = "UPDATE ManualDocument SET DocumentType = ?, DocumentNumber = ?, ManualDocTypeID = ?, ManualVolumeNo = ?, "
					+ " ManualRunningNo = ?, Note = ?, TeamCode = ?, SubTeamCode = ?, EmpID = ?, "
					+ " UpdateBy = ?, UpdateDate = ?, isActive = ? "
					+ " WHERE (DocumentNumber = ?) ";
		executeNonQuery(sql, new String[] { info.DocumentType, info.DocumentNumber, info.ManualDocTypeID, info.ManualVolumeNo, 
						valueOf(info.ManualRunningNo), info.Note, info.TeamCode, info.SubTeamCode, info.EmpID, 
						info.UpdateBy, valueOf(info.UpdateDate), valueOf(info.isActive), info.DocumentNumber });
	}

    public void deleteManualDocumentAll() {
        String sql = "DELETE FROM ManualDocument";
        executeNonQuery(sql, null);
    }

}
