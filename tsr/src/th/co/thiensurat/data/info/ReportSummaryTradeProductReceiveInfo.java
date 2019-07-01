package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ReportSummaryTradeProductReceiveInfo extends BHParcelable {

	public String ReportTradeProductID;
    public String FortnightID;
    public String Fortnight;
    public String FortnightTime;
    public String TeamCode;
    public String SaleCode;
    public String SaleName;
    public Date SaleDate;
    public int TSRProductCount;
    public int OtherProductCount;
    public int ProductCountTotal;
    public int ReturnProductCount;
    public Date ReturnDate;
}
