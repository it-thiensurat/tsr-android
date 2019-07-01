package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class TransactionLogInfo extends BHParcelable {
	public int TransactionID;
	public String ServiceName;
	public String ServiceInputName;
	public String ServiceInputType;
	public String ServiceOutputType;
	public String ServiceInputData;
	public Date TransactionDate;
	public boolean SyncStatus;
	public Date SyncDate;
}
