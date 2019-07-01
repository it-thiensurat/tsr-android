package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.LimitInfo;

public class LimitController extends BaseController {
    public enum LimitType {
        Moneyonhand, Productonhand, ImportAuditDay, ImportCreditDay, MoneyonhandCredit
    }

    public LimitInfo getLimitByLimitTypeAndEmployee(String LimitType, String EmpID) {
        String sql = "SELECT * FROM [Limit] " +
                " WHERE LimitType = ? AND (EmpID = ? OR EmpID is null) " +
                " ORDER BY EmpID DESC LIMIT 1";
        return executeQueryObject(sql, new String[]{LimitType, EmpID}
                , LimitInfo.class);
    }
	
	public void addLimit(LimitInfo info) {
		String sql = "INSERT INTO [Limit](OrganizationCode, LimitID, LimitType, EmpID, LimitMax, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy) "
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql, new String[] { info.OrganizationCode, info.LimitID, info.LimitType, info.EmpID, valueOf(info.LimitMax),
				valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy });
	}

	public void deleteLimitAll() {
        String sql = "DELETE FROM [Limit]";
        executeNonQuery(sql, null);
    }


}
