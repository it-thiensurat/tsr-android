package th.co.thiensurat.data.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.TripInfo;

public class FortnightController extends BaseController {

	// GetCurrentFortnight
	public FortnightInfo getCurrentFortnight(String organizationCode) {
		String sql = "SELECT * FROM Fortnight WHERE (OrganizationCode = ?) AND (DATE() BETWEEN DATE(StartDate) AND DATE(EndDate))";
		FortnightInfo ret = null;
		ret = executeQueryObject(sql, new String[] { organizationCode }, FortnightInfo.class);
		return ret;
	}

	public FortnightInfo getFortnight(String OrganizationCode) {
		final String sql = "SELECT  * FROM [Fortnight] where date('now') between [StartDate] and [EndDate] and OrganizationCode = ?";
		return executeQueryObject(sql, new String[]{OrganizationCode}, FortnightInfo.class);
	}

	public FortnightInfo getCurrentFortnightInfo() {
		String sql = "SELECT * FROM Fortnight WHERE DATE() BETWEEN DATE(StartDate) AND DATE(EndDate)";
		FortnightInfo ret = null;
		ret = executeQueryObject(sql, new String[] {}, FortnightInfo.class);
		return ret;
	}

    public FortnightInfo getFortnightByFortnightID(String FortnightID) {
        String sql = "SELECT * FROM Fortnight WHERE FortnightID = ?";
        FortnightInfo ret = null;
        ret = executeQueryObject(sql, new String[]{FortnightID}, FortnightInfo.class);
        return ret;
    }

	// GetAllFortnight
	public List<FortnightInfo> getAllFortnight(String organizationCode) {
		String sql = "SELECT * FROM Fortnight WHERE  OrganizationCode = ? ORDER BY fortnightnumber ASC";
		List<FortnightInfo> ret = null;
		ret = executeQueryList(sql, new String[]{organizationCode}, FortnightInfo.class);
		return ret;
	}

	public void addFortnight(FortnightInfo info) {
		String sql = "insert into Fortnight([FortnightID],[OrganizationCode],[FortnightNumber],[Year],[StartDate],[EndDate])"
				+" values(?,?,?,?,?,?)";
		executeNonQuery(sql, new String[] {info.FortnightID, info.OrganizationCode, valueOf(info.FortnightNumber), valueOf(info.Year), valueOf(info.StartDate), valueOf(info.EndDate)});
	}
	
	public void deleteFortnight() {
		executeNonQuery("delete from Fortnight", null);
	}

}
