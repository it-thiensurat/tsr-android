package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ReportSummaryChangeContractInfo;
import th.co.thiensurat.data.info.ReportSummaryChangeProductInfo;
import th.co.thiensurat.data.info.ReportSummaryImpoundProductInfo;
import th.co.thiensurat.data.info.ReportSummaryRangOfFirstReceiveInfo;
import th.co.thiensurat.data.info.ReportSummaryTradeProductNotReceiveInfo;
import th.co.thiensurat.data.info.ReportSummaryTradeProductReceiveInfo;
import th.co.thiensurat.data.info.ReportSummaryWriteOffNPLInfo;

public class ReportController extends BaseController {


	
	public List<ReportSummaryChangeContractInfo> getReportSummaryChangeContract(String FortnightID, String TeamCode)
	{
		if (TeamCode == null || TeamCode.equals("")) {
			TeamCode = "%";
		}
		
		final String sql = "select * from ReportChangeContract where fortnightid = ? and teamcode like ?   order by changecontractdate";
		
		return executeQueryList(sql, new String[] { FortnightID, TeamCode  }, ReportSummaryChangeContractInfo.class);

	}
	
	public List<ReportSummaryChangeProductInfo> getReportSummaryChangeProduct(String FortnightID, String TeamCode)
	{
		if (TeamCode == null || TeamCode.equals("")) {
			TeamCode = "%";
		}
		
		final String sql = "select * from ReportChangeProduct where fortnightid = ? and teamcode like ? order by changeproductdate";
		
		return executeQueryList(sql, new String[] { FortnightID, TeamCode  }, ReportSummaryChangeProductInfo.class);

	}
	
	public List<ReportSummaryTradeProductReceiveInfo> getReportSummaryTradeProductReceive(String FortnightID, String TeamCode, String SaleCode)
	{
		if (SaleCode == null || SaleCode.equals("")) {
			SaleCode = "%";
		}
		
		final String sql = "select reporttradeproduct.*, reporttradeproductreturn.remainproductcounttotal as 'ReturnProductCount', reporttradeproductreturn.ReturnDate from reporttradeproduct left outer join reporttradeproductreturn on reporttradeproduct.saledate = reporttradeproductreturn.saledate where fortnightid = ? and teamcode = ?  and salecode like ? order by reporttradeproduct.saledate";
		
		return executeQueryList(sql, new String[] { FortnightID, TeamCode, SaleCode  }, ReportSummaryTradeProductReceiveInfo.class);

	}
	
	public List<ReportSummaryTradeProductNotReceiveInfo> getReportSummaryTradeProductNotReceive(String FortnightID, String TeamCode, String SaleCode)
	{
		if (SaleCode == null || SaleCode.equals("")) {
			SaleCode = "%";
		}
		
		final String sql = "select reporttradeproduct.FortnightID, reporttradeproduct.Fortnight, reporttradeproduct.FortnightTime, reporttradeproduct.TeamCode, reporttradeproduct.SaleCode,reporttradeproduct.SaleName,reporttradeproduct.SaleDate, reporttradeproductreturn.ReturnDate, reporttradeproductreturn.RemainTSRProductCount, reporttradeproductreturn.RemainOtherProductCount, reporttradeproductreturn.remainproductcounttotal as 'RemainProductCountTotal' from reporttradeproduct left outer join reporttradeproductreturn on reporttradeproduct.saledate = reporttradeproductreturn.saledate where fortnightid = ? and teamcode = ?  and salecode like ?  order by reporttradeproduct.saledate";
		
		return executeQueryList(sql, new String[] { FortnightID, TeamCode, SaleCode  }, ReportSummaryTradeProductNotReceiveInfo.class);

	}
	
	public List<ReportSummaryImpoundProductInfo> getReportSummaryImpoundProduct(String FortnightID, String TeamCode)
	{
		if (TeamCode == null || TeamCode.equals("")) {
			TeamCode = "%";
		}
		
		final String sql = "select * from ReportImpoundProduct where fortnightid = ? and teamcode like ? order by impoundproductdate";
		
		return executeQueryList(sql, new String[] { FortnightID, TeamCode  }, ReportSummaryImpoundProductInfo.class);

	}
	
	public List<ReportSummaryWriteOffNPLInfo> getReportSummaryWriteOffNPL(String FortnightID, String TeamCode, String WriteOffNPLType)
	{
		if (TeamCode == null || TeamCode.equals("")) {
			TeamCode = "%";
		}
		
		final String sql = "select * from ReportWriteOffNPL where fortnightid = ? and teamcode like ? and writeoffnpltype = ? order by writeoffnpldate";
		
		return executeQueryList(sql, new String[] { FortnightID, TeamCode, WriteOffNPLType  }, ReportSummaryWriteOffNPLInfo.class);

	}
	
	public List<ReportSummaryRangOfFirstReceiveInfo> getReportSummaryRangOfFirstReceive(String FortnightID, String TeamCode)
	{
		if (TeamCode == null || TeamCode.equals("")) {
			TeamCode = "%";
		}
		
		final String sql = "select * from ReportRangOfFirstReceive where fortnightid = ? and teamcode like ? order by daterang";
		
		return executeQueryList(sql, new String[] { FortnightID, TeamCode  }, ReportSummaryRangOfFirstReceiveInfo.class);

	}
	
}
