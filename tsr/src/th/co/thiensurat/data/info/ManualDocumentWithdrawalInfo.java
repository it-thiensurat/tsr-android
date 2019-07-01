package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ManualDocumentWithdrawalInfo extends BHParcelable {
	public String ManualDocID;
    public String TeamCode;
    public String SubTeamCode;
    public String EmpID;
    public String ManualDocTypeID;
    public String VolumeNo;
    public long StartNo;
    public long EndNo;       
    public Date WithdrawalDate;
    public Date ReturnDate;
    public String ReturnBy;
    public String ReceivedBy;
    public Date ReceivedDate;
    public long ReturnStartNo;
    public long ReturnEndNo;
    public String CreatedBy;
    public Date CreatedDate;
    public String UpdateBy;
    public Date UpdateDate;
    public String Remark;
    public String ReturnRemark;
    public boolean IsReturnComplete;

    // Extra-Field
    public String EmpName;
    public String ManualDocTypeName;
    public long ManualRunningNo;

    // MAX
    public long MaxRunningNo;
}
