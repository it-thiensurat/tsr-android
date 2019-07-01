package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ReportInventoryInfo;

public class ReportInventoryController extends BaseController {

	public List<ReportInventoryInfo> getReportInventory(String fortnight,
			String productcode, String teamcode) {
		String sql = "select * from ReportInventory where fortnight = ? and productcode = ? and teamcode = ?";
		List<ReportInventoryInfo> ret = null;
		ret = executeQueryList(sql, new String[] { fortnight, productcode,
				teamcode }, ReportInventoryInfo.class);
		return ret;
	}

}
