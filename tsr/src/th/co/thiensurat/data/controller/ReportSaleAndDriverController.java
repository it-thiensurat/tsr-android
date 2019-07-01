package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ReportSaleAndDriverInfo;

public class ReportSaleAndDriverController extends BaseController{
	
	public List<ReportSaleAndDriverInfo> getReportByFortnight(String teamcode ,String fortnight) {
		String sql = "SELECT * FROM ReportSaleAndDriver WHERE TeamCode = ? AND Fortnight = ? ";
		List<ReportSaleAndDriverInfo> ret = null;
		ret = executeQueryList(sql, new String[] { teamcode, fortnight }, ReportSaleAndDriverInfo.class);
		return ret;		
	}
	
	
}
