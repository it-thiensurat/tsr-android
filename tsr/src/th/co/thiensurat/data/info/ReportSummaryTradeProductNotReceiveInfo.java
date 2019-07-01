package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ReportSummaryTradeProductNotReceiveInfo extends BHParcelable{

	public String ReportTradeProductID;
    public String FortnightID;
    public String Fortnight;
    public String FortnightTime;
    public String TeamCode;
    public String SaleCode;
    public String SaleName;
    public Date SaleDate;
    public int RemainTSRProductCount;
    public int RemainOtherProductCount;
    public int RemainProductCountTotal;
    public Date ReturnDate;
}
