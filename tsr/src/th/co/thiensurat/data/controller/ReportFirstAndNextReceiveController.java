package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ReportFirstAndNextReceiveInfo;

public class ReportFirstAndNextReceiveController extends BaseController {

	public List<ReportFirstAndNextReceiveInfo> getReportFirstandNext(String fortnight,
			String teamcode) {
		String sql = "select * from ReportFirstAndNextReceive where fortnight = ?  and teamcode = ? ";
		List<ReportFirstAndNextReceiveInfo> ret = null;
		ret = executeQueryList(sql, new String[] { fortnight, teamcode },
				ReportFirstAndNextReceiveInfo.class);
		return ret;
	}
}
