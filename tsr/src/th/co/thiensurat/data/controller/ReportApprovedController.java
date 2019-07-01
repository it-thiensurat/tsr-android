package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ReportApprovedInfo;
import th.co.thiensurat.data.info.ReportCustomerProblemInfo;

public class ReportApprovedController extends BaseController {
	public List<ReportApprovedInfo> getReportRequest(String fortnight,
			String teamcode, String reporttype) {
		String sql = "SELECT * FROM ReportApproved where fortnight = ?  and teamcode = ? and reporttype = ? ";
		List<ReportApprovedInfo> ret = null;
		ret = executeQueryList(sql,
				new String[] { fortnight, teamcode, reporttype },
				ReportApprovedInfo.class);
		return ret;
	}

	

}
