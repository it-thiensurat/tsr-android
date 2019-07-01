package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ManualDocumentTypeInfo;

public class ManualDocumentTypeController extends BaseController {
	
	public List<ManualDocumentTypeInfo> getManualDocumentType() {
		String sql = "SELECT * FROM ManualDocumentType ORDER BY ManualDocTypeID";
		return executeQueryList(sql, null, ManualDocumentTypeInfo.class);
	}
	
	public void addManualDocumentType(ManualDocumentTypeInfo info) {
		String sql = "INSERT INTO ManualDocumentType(ManualDocTypeID, ManualDocTypeName)  VALUES (?, ?)";
		executeNonQuery(sql, new String[] { info.ManualDocTypeID, info.ManualDocTypeName });
	}

    public void deleteManualDocumentTypeAll() {
        String sql = "DELETE FROM ManualDocumentType";
        executeNonQuery(sql, null);
    }

}
