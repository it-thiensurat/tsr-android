package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ReportCustomerProblemInfo;

public class ReportCustomerProblemController extends BaseController {
	public List<ReportCustomerProblemInfo> getReportProblem(String fortnight,
			String teamcode, String reporttype) {
		String sql = "SELECT * FROM ReportCustomerProblem where fortnight = ?  and teamcode = ? and reporttype = ? ";
		List<ReportCustomerProblemInfo> ret = null;
		ret = executeQueryList(sql, new String[] { fortnight, teamcode,
				reporttype }, ReportCustomerProblemInfo.class);
		return ret;
	}

}
