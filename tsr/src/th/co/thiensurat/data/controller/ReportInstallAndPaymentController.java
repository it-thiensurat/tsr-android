package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ReportInstallAndPaymentInfo;

public class ReportInstallAndPaymentController extends BaseController {
	public List<ReportInstallAndPaymentInfo> getReportBySale(String fortnight,
			 String salecode , String reporttype) {
		String sql = "select * from ReportInstallAndPayment where fortnight = ? and reporttype = ? and SaleLevel01 = ?";
		List<ReportInstallAndPaymentInfo> ret = null;
		ret = executeQueryList(sql, new String[] { fortnight, salecode, reporttype }, ReportInstallAndPaymentInfo.class);
		return ret;
	}
	public List<ReportInstallAndPaymentInfo> getReportByTeam(String fortnight,
			 String teamcode , String reporttype) {
		String sql = "select * from ReportInstallAndPayment where fortnight = ? and reporttype = ? and teamcode = ?";
		List<ReportInstallAndPaymentInfo> ret = null;
		ret = executeQueryList(sql, new String[] { fortnight, teamcode, reporttype }, ReportInstallAndPaymentInfo.class);
		return ret;
	}

}
